package ro.uti.ran.core.ws.internal.notificari;

import net.sf.jmimemagic.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.RolUtilizator;
import ro.uti.ran.core.model.portal.Utilizator;
import ro.uti.ran.core.model.portal.UtilizatorGospodarie;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.utils.RolEnum;
import ro.uti.ran.core.model.utils.TipAdresa;
import ro.uti.ran.core.repository.portal.UtilizatorGospodarieRepository;
import ro.uti.ran.core.repository.portal.UtilizatorRepository;
import ro.uti.ran.core.repository.registru.DetinatorPjRepository;
import ro.uti.ran.core.repository.registru.GospodarieRepository;
import ro.uti.ran.core.repository.registru.MembruPfRepository;
import ro.uti.ran.core.service.sistem.MailService;
import ro.uti.ran.core.service.sistem.MessageBundleService;
import ro.uti.ran.core.service.utilizator.UtilizatorService;
import ro.uti.ran.core.utils.AllowedImagesFormat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sache on 12/16/2015.
 */
@Component
@Transactional
public class NotificariSistemServiceImpl implements NotificariSistemService {

    private static final Logger logger = LoggerFactory.getLogger(NotificariSistemServiceImpl.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private MessageBundleService messageBundleService;

    @Autowired
    private UtilizatorService utilizatorService;

    @Autowired
    private GospodarieRepository gospodarieRepository;

    @Autowired
    private UtilizatorGospodarieRepository utilizatorGospodarieRepository;

    @Autowired
    private MembruPfRepository membruPfRepository;


    @Autowired
    private UtilizatorRepository utilizatorRepository;

    @Override
    public void notificareRapoarteCentralizatoare(Integer sirutaUAT) throws Exception {

        List<String> toList = getOperatoriUatBySirutaUAT(sirutaUAT);

        if (!toList.isEmpty()) {
            mailService.sendEmail(toList, messageBundleService.getMessage("notificareRapoarteCentralizatoare.subject"), messageBundleService.getMessage("notificareRapoarteCentralizatoare.content"));
        } else {
            logger.info("--- INFO: notificareRapoarteCentralizatoare - nu exista utilizatori cu rolul operator UAT pentru codul siruta: " + sirutaUAT + " ---");
        }
    }

    @Override
    public void notificareCereriCompletareDate(Integer sirutaUAT, String identificatorGospodarie, String details) throws Exception {
        List<String> toList = getOperatoriUatBySirutaUAT(sirutaUAT);

        if (!toList.isEmpty()) {
            mailService.sendEmail(toList, messageBundleService.getMessage("notificareCereriCompletareDate.subject"), messageBundleService.getMessage("notificareCereriCompletareDate.content",
                    new String[]{identificatorGospodarie, details}));
        } else {
            logger.info("--- INFO: notificareRapoarteCentralizatoare - nu exista utilizatori cu rolul operator UAT pentru codul siruta: " + sirutaUAT + " ---");
        }

        Gospodarie gospodarie = gospodarieRepository.findByUatAndIdentificator(sirutaUAT, identificatorGospodarie);
        List<String> gospodari = getGospodari(gospodarie);
        String denumireUAT = gospodarie != null && gospodarie.getNomUat() != null ? gospodarie.getNomUat().getDenumire() : "";

        if (!gospodari.isEmpty()) {
            mailService.sendEmail(gospodari, messageBundleService.getMessage("notificareCereriCompletareDate.subject"), messageBundleService.getMessage("notificareCereriCompletareDate.gospodar.content",
                    new String[]{denumireUAT, sirutaUAT != null ? sirutaUAT.toString() : "", identificatorGospodarie, details}));
        } else {
            logger.info("--- INFO: notificareCereriCompletareDate - nu exista gospodari pentru codul siruta: " + sirutaUAT + " si cod gospodarie: " + identificatorGospodarie + " ---");
        }
    }

    @Override
    public void notificareCereriCorectieDate(Integer sirutaUAT, String identificatorGospodarie, String details) throws Exception {
        Gospodarie gospodarie = gospodarieRepository.findByUatAndIdentificator(sirutaUAT, identificatorGospodarie);
        String adresaGospodarie = getAdresaGospodarieAsString(gospodarie);

        List<String> toList = getOperatoriUatBySirutaUAT(sirutaUAT);
        if (!toList.isEmpty()) {
            mailService.sendEmail(toList, messageBundleService.getMessage("notificareCereriCorectieDate.subject"), messageBundleService.getMessage("notificareCereriCorectieDate.content",
                    new String[]{identificatorGospodarie, adresaGospodarie, details}));
        } else {
            logger.info("--- INFO: notificareRapoarteCentralizatoare - nu exista utilizatori cu rolul operator UAT pentru codul siruta: " + sirutaUAT + " ---");
        }

        List<String> gospodari = getGospodari(gospodarie);
        String denumireUAT = gospodarie != null && gospodarie.getNomUat() != null ? gospodarie.getNomUat().getDenumire() : "";

        if (!gospodari.isEmpty()) {
            mailService.sendEmail(gospodari, messageBundleService.getMessage("notificareCereriCorectieDate.subject"), messageBundleService.getMessage("notificareCereriCorectieDate.gospodar.content",
                    new String[]{denumireUAT, sirutaUAT != null ? sirutaUAT.toString() : "", identificatorGospodarie, adresaGospodarie, details}));
        } else {
            logger.info("--- INFO: notificareCereriCorectieDate - nu exista gospodari pentru codul siruta: " + sirutaUAT + " si cod gospodarie: " + identificatorGospodarie + " ---");
        }
    }


    @Override
    public void notificareRaportAdHoc(String username, String denumireRaport, String continutHtml, byte[] atasament) throws Exception {

        try {

            String email = checkIfUserExists(username);

            if (email == null) {
                throw new Exception("Utilizatorul " + username + "nu exista in sitem");
            }

            if (null != atasament) {
                MagicMatch fileTypeMatch = Magic.getMagicMatch(atasament);
                boolean isAllowedImagesFormat = false;
                for (AllowedImagesFormat format : AllowedImagesFormat.values()) {
                    if (fileTypeMatch.getMimeType().equalsIgnoreCase(format.getType())) {
                        isAllowedImagesFormat = true;
                        break;
                    }
                }
                if (!isAllowedImagesFormat) {
                    throw new Exception("Fisierul atasat  nu corespunde formatului permis de imagine!");
                }
            }
            List<String> emailList = new ArrayList<>();
            emailList.add(email);
            mailService.sendEmail(emailList, denumireRaport, continutHtml, true, true, atasament);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void notificareRaportAdHoc(String username, String denumireRaport, String continutHtml) throws Exception {
        try {
            String email = checkIfUserExists(username);
            if (email == null) {
                throw new Exception("Utilizatorul " + username + "nu exista in sitem");
            }
            List<String> emailList = new ArrayList<>();
            emailList.add(email);
            mailService.sendEmail(emailList, denumireRaport, continutHtml, false, true);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    private String checkIfUserExists(String username) {
        //TODO de verificat daca utilizatoru cu email exista in sistem ....  daca nu false
        Utilizator user = null;
        try {
            user = utilizatorService.getUtilizator(username);
            if (user == null) {
                return null;
            }

        } catch (RanBusinessException e) {
            e.printStackTrace();
        }


        return user.getEmail();
    }

    private List<String> getOperatoriUatBySirutaUAT(Integer sirutaUAT) {
        List<Utilizator> utilizatorList = utilizatorService.getUtilizatoriActiviByRolAndUatCodSiruta("OP-UAT", true, sirutaUAT, true);
        List<String> toList = new ArrayList<>();
        if (utilizatorList != null && !utilizatorList.isEmpty()) {
            for (Utilizator utilizator : utilizatorList) {
                if (utilizator.getEmail() != null) {
                    toList.add(utilizator.getEmail());
                }
            }
        }
        return toList;
    }

    private List<String> getGospodari(Gospodarie gospodarie) {

        Set<String> gospodari = new HashSet<>();

        if (gospodarie != null) {
            if (gospodarie.getDetinatorPfs() != null && !gospodarie.getDetinatorPfs().isEmpty()) {
                for (DetinatorPf detinatorPf : gospodarie.getDetinatorPfs()) {
                    boolean detinatorHasCnp = detinatorPf.getPersoanaFizica().getCnp() != null && !detinatorPf.getPersoanaFizica().getCnp().isEmpty();
                    boolean detinatorHasNif = detinatorPf.getPersoanaFizica().getNif() != null && !detinatorPf.getPersoanaFizica().getNif().isEmpty();

                    if (detinatorPf.getPersoanaFizica() != null && (detinatorHasCnp || detinatorHasNif)) {
                        List<DetinatorPf> detinatorPfsList = gospodarie.getDetinatorPfs();
                        for (DetinatorPf detinatorPfValue : detinatorPfsList) {

                            List<RolUtilizator> rolUtilizator = null;
                            if (detinatorHasCnp) {
                                rolUtilizator = utilizatorService.getUtilizatorByCNPAndRol(RolEnum.GOSPODAR.getCod(), detinatorPfValue.getPersoanaFizica().getCnp());
                            } else {
                                rolUtilizator = utilizatorService.getUtilizatorByNIFAndRol(RolEnum.GOSPODAR.getCod(), detinatorPfValue.getPersoanaFizica().getNif());
                            }
                            addGospodari(rolUtilizator, gospodari);
                        }

                        List<MembruPf> membruCnpList = null;
                        if (detinatorHasCnp) {
                            membruCnpList = membruPfRepository.findMembersCnpByMainCnpAndFkOfDetinatorPf(detinatorPf.getIdDetinatorPf(), detinatorPf.getPersoanaFizica().getCnp());
                        } else {
                            membruCnpList = membruPfRepository.findMembersNifByMainNifAndFkOfDetinatorPf(detinatorPf.getIdDetinatorPf(), detinatorPf.getPersoanaFizica().getNif());
                        }

                        for (MembruPf membruPfValue : membruCnpList) {
                            boolean membruHasCnp = membruPfValue.getPersoanaFizica().getCnp() != null && !membruPfValue.getPersoanaFizica().getCnp().isEmpty();
                            List<RolUtilizator> rolUtilizator = null;
                            if (membruHasCnp) {
                                rolUtilizator = utilizatorService.getUtilizatorByCNPAndRol(RolEnum.GOSPODAR.getCod(), membruPfValue.getPersoanaFizica().getCnp());
                            } else {
                                rolUtilizator = utilizatorService.getUtilizatorByNIFAndRol(RolEnum.GOSPODAR.getCod(), membruPfValue.getPersoanaFizica().getNif());
                            }
                            addGospodari(rolUtilizator, gospodari);
                        }
                    }
                }
            }
            if (gospodarie.getDetinatorPjs() != null && !gospodarie.getDetinatorPjs().isEmpty()) {
                for (DetinatorPj detinatorPj : gospodarie.getDetinatorPjs()) {
                    if (detinatorPj.getPersoanaRc() != null && detinatorPj.getPersoanaRc().getCui() != null && !detinatorPj.getPersoanaRc().getCui().isEmpty()) {
                        List<DetinatorPj> detinatorPjsList = gospodarie.getDetinatorPjs();
                        for (DetinatorPj detinatorPjValue : detinatorPjsList) {
                            boolean detinatorPjHasCnp = detinatorPjValue.getPersoanaFizica().getCnp() != null && !detinatorPjValue.getPersoanaFizica().getCnp().isEmpty();
                            List<RolUtilizator> rolUtilizator = null;
                            if (detinatorPjHasCnp) {
                                rolUtilizator = utilizatorService.getUtilizatorByCNPAndRol(RolEnum.GOSPODAR.getCod(), detinatorPjValue.getPersoanaFizica().getCnp());
                            } else {
                                rolUtilizator = utilizatorService.getUtilizatorByNIFAndRol(RolEnum.GOSPODAR.getCod(), detinatorPjValue.getPersoanaFizica().getNif());
                            }
                            addGospodari(rolUtilizator, gospodari);
                        }

                        List<UtilizatorGospodarie> utilizatorGospodarielist = utilizatorGospodarieRepository.findByidGospodarie(gospodarie.getIdGospodarie());
                        for (UtilizatorGospodarie utilizatorGospodarieValue : utilizatorGospodarielist) {
                            Utilizator utilizator = utilizatorRepository.findOne(utilizatorGospodarieValue.getIdUtilizator());
                            boolean utilizatorHasCnp = utilizator.getCnp() != null && !utilizator.getCnp().isEmpty();
                            List<RolUtilizator> rolUtilizator = null;
                            if (utilizatorHasCnp) {
                                utilizatorService.getUtilizatorByCNPAndRol(RolEnum.GOSPODAR.getCod(), utilizator.getCnp());
                            } else {
                                utilizatorService.getUtilizatorByNIFAndRol(RolEnum.GOSPODAR.getCod(), utilizator.getNif());
                            }
                            addGospodari(rolUtilizator, gospodari);
                        }
                    }
                }
            }
        }

        List finalListOfgospodarii = new ArrayList();
        finalListOfgospodarii.addAll(gospodari);
        return finalListOfgospodarii;
    }

    private String getAdresaGospodarieAsString(Gospodarie gospodarie) {
        String adresaAsString = "";
        List<AdresaGospodarie> adrese = gospodarie.getAdresaGospodaries();
        if (adrese != null) {
            for (AdresaGospodarie adresaGospodarie : adrese) {
                if (TipAdresa.GOSPODARIE.getCod().equals(adresaGospodarie.getNomTipAdresa().getCod())) {
                    String strada = adresaGospodarie.getAdresa().getStrada();
                    if (strada == null || strada.isEmpty()) {
                        strada = "";
                    }
                    String nrStrada = adresaGospodarie.getAdresa().getNrStrada();
                    if (nrStrada == null || nrStrada.isEmpty()) {
                        nrStrada = "";
                    }
                    adresaAsString += "str. " + strada;
                    adresaAsString += ", nr. " + nrStrada;
                    break;
                }
            }
        }
        return adresaAsString;
    }

    private void addGospodari(List<RolUtilizator> rolUtilizator, Set<String> gospodari) {
        if (null != rolUtilizator && !rolUtilizator.isEmpty()) {
            for (RolUtilizator r : rolUtilizator) {
                if (r.getUtilizator().getEmail() != null && !r.getUtilizator().getEmail().isEmpty()) {
                    gospodari.add(r.getUtilizator().getEmail());
                }
            }
        }
    }
}
