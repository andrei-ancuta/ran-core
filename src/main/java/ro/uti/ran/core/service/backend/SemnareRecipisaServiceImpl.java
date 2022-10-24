package ro.uti.ran.core.service.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ro.uti.ran.core.business.scheduler.annotations.AppParametruEnable;
import ro.uti.ran.core.business.scheduler.annotations.Cluster;
import ro.uti.ran.core.model.registru.NomStareRegistru;
import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.nomenclator.NomenclatorService;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.registru.RegistruSearchFilter;
import ro.uti.ran.core.service.registru.RegistruService;
import ro.uti.ran.core.utils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Sache on 11/2/2015.
 */
@Service
public class SemnareRecipisaServiceImpl implements SemnareRecipisaService {

    @Autowired
    private RegistruService registruService;

    @Autowired
    private NomenclatorService nomSrv;

    @Autowired
    private Environment env;

    public static final Logger log = LoggerFactory.getLogger(SemnareRecipisaServiceImpl.class);

    @Override
    @Scheduled(cron = "0 0 22 * * ?")
    @AppParametruEnable("jobSemnare.enabled")
    @Cluster
    public void copiazaRecipiseIN() {
        boolean repeta = true;
        Integer chunkSize = env.getProperty("numar.recipise.selectate.chunk", Integer.class);
        chunkSize = chunkSize != null ? chunkSize : 100;
        Integer page = 0;

        log.debug("------------------------------- SemnareRecipisaServiceImpl.copiazaRecipise() IN -- START ----------------------------------------");
        try {
            //construieste filtrul pentru registrele cu stare "R" si fara recipisa semnata
            RegistruSearchFilter filtru = new RegistruSearchFilter();
            filtru.setIdStareRegistru(nomSrv.<NomStareRegistru>getNomenclatorForStringParam(
                    NomenclatorCodeType.NomStareRegistru,
                    "S"
            ).getId());
            filtru.setIsRecipisaSemnata(false);
            //repeta pana cand ultimele x inregistrari luate din baza dau eroare, sau daca nu mai sunt inregistrari conform conditiilor de filtrare
            while (repeta) {
                GenericListResult<Registru> registruGenericListResult =
                        registruService.getListaRegistru(filtru,
                                new PagingInfo(chunkSize * (page++), chunkSize),
                                createSortCriteria("idRegistru", "desc"));
                if (registruGenericListResult != null &&
                        registruGenericListResult.getItems() != null &&
                        !registruGenericListResult.getItems().isEmpty()) {
                    repeta = true;
                    for (Registru registru : registruGenericListResult.getItems()) {
                        try {
                            byte[] registruIN = getFileByIdRegistru(registru.getIdRegistru(), true);
                            if (registruIN == null) {
                                copyToFile(registru.getIdRegistru(), registru.getRecipisa());
                                log.debug("Am copiat din baza de date recipisa (IN) pentru id-ul: " + registru.getIdRegistru().toString());
                            }
                        } catch (Throwable t) {
                            log.error("Eroare in job-ul de copiere recipise (IN) pentru REGISTRU.ID = " + registru.getIdRegistru() + "!", t);
                        }
                    }
                } else {
                    repeta = false;
                }
            }
        } catch (Throwable t) {
            log.error("Eroare in job-ul de copiere recipise!", t);
        } finally {
            log.debug("------------------------------- SemnareRecipisaServiceImpl.copiazaRecipise() IN -- END ----------------------------------------");
        }
    }

    @Override
    @Scheduled(cron = "0 30 22 * * ?")
    @AppParametruEnable("jobSemnare.enabled")
    @Cluster
    public void copiazaRecipiseOUT() {
        boolean repeta = true;
        Integer chunkSize = env.getProperty("numar.recipise.selectate.chunk", Integer.class);
        chunkSize = chunkSize != null ? chunkSize : 100;
        Integer page = 0;

        log.debug("------------------------------- SemnareRecipisaServiceImpl.copiazaRecipise() OUT -- START ----------------------------------------");
        try {
            //construieste filtrul pentru registrele cu stare "R" si fara recipisa semnata
            RegistruSearchFilter filtru = new RegistruSearchFilter();
            filtru.setIdStareRegistru(nomSrv.<NomStareRegistru>getNomenclatorForStringParam(
                    NomenclatorCodeType.NomStareRegistru,
                    "S"
            ).getId());
            filtru.setIsRecipisaSemnata(false);
            //repeta pana cand ultimele x inregistrari luate din baza dau eroare, sau daca nu mai sunt inregistrari conform conditiilor de filtrare
            while (repeta) {
                GenericListResult<Registru> registruGenericListResult =
                        registruService.getListaRegistru(filtru,
                                new PagingInfo(chunkSize * (page++), chunkSize),
                                createSortCriteria("idRegistru", "desc"));

                if (registruGenericListResult != null &&
                        registruGenericListResult.getItems() != null &&
                        !registruGenericListResult.getItems().isEmpty()) {
                    repeta = true;
                    for (Registru registru : registruGenericListResult.getItems()) {
                        try {
                            byte[] registruOUT = getFileByIdRegistru(registru.getIdRegistru(), false);
                            if (registruOUT != null) {
                                registru.setRecipisa(registruOUT);
                                registru.setIsRecipisaSemnata(true);
                                registruService.saveRegistru(registru);
                                log.debug("Am copiat in baza de date recipisa (OUT) pentru id-ul: " + registru.getIdRegistru().toString());
                            }
                        } catch (Throwable t) {
                            log.error("Eroare in job-ul de copiere recipise (OUT) pentru REGISTRU.ID = " + registru.getIdRegistru() + "!", t);
                        }
                    }
                } else {
                    repeta = false;
                }
            }
        } catch (Throwable t) {
            log.error("Eroare in job-ul de copiere recipise!", t);
        } finally {
            log.debug("------------------------------- SemnareRecipisaServiceImpl.copiazaRecipise() OUT -- END ----------------------------------------");
        }
    }

    private SortInfo createSortCriteria(final String criteria, String order) {
        if (criteria == null || order == null) {
            return null;
        }
        SortInfo sortInfo = new SortInfo();
        SortCriteria sortCriteria = new SortCriteria();
        sortCriteria.setPath(criteria);
        sortCriteria.setOrder(Order.valueOf(order));
        sortInfo.getCriterias().add(sortCriteria);
        return sortInfo;
    }

    private byte[] getFileByIdRegistru(Long idregistru, boolean in) {
        if (idregistru == null) {
            log.error("Am primit un id de registru null!");
            throw new IllegalArgumentException("SemnareRecipisaServiceImpl.getFileByIdRegistru: Am primit un id de registru null!");
        }
        String director = getDirector(in);

        String signedReceiptFilePath = director + idregistru.toString() + ".p7s";
        File registruFile = new File(signedReceiptFilePath);

        if (!registruFile.exists()) {
            return null;
        } else {
            try {
                return Files.readAllBytes(Paths.get(signedReceiptFilePath));
            } catch (IOException e) {
                log.error("Eroare la citirea fisierului!", e);
            }
        }

        return null;
    }

    private void copyToFile(Long idRegistru, byte[] continut) {
        String director = getDirector(true);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(director + idRegistru.toString());
            fos.write(continut != null ? continut : new byte[0]);
            fos.flush();
        } catch (FileNotFoundException e) {
            log.error("Eroare la scriere in fisierul " + idRegistru.toString() + "!", e);
        } catch (IOException e) {
            log.error("Eroare la scriere in fisierul " + idRegistru.toString() + "!", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error("Eroare la inchiderea fisierului!", e);
                }
            }
        }
    }

    private String getDirector(boolean in) {
        String locatie = null;
        if (in) {
            locatie = env.getProperty(RanConstants.LOCATIE_RECIPISA_REGISTRU_IN);
        } else {
            locatie = env.getProperty(RanConstants.LOCATIE_RECIPISA_REGISTRU_OUT);
        }

        if (locatie == null) {
            log.error("Locatia IN pentru recipise nu a fost setata!");
            return null;
        }

        if (!locatie.endsWith(File.separator)) {
            locatie += File.separator;
        }

        File locatieFile = new File(locatie);
        if (!locatieFile.exists()) {
            locatieFile.mkdirs();
            return null;
        }

        return locatie;
    }
}
