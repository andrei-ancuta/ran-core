package ro.uti.ran.core.service.registru;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.registru.view.ViewRegistruNomStare;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.repository.criteria.AndRepositoryCriteria;
import ro.uti.ran.core.repository.criteria.Operation;
import ro.uti.ran.core.repository.criteria.RepositoryCriteria;
import ro.uti.ran.core.repository.registru.GospodarieRepository;
import ro.uti.ran.core.repository.registru.RegistruRepository;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.exception.SyntaxXmlException;
import ro.uti.ran.core.service.backend.nomenclator.NomenclatorService;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.index.IndexGenerator;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 16:14
 */
@Service
@Transactional("registruTransactionManager")
public class RegistruServiceImpl implements RegistruService {

    @Autowired
    IndexGenerator indexGenerator;

    @Autowired
    private RegistruRepository registruRepository;

    @Autowired
    private NomenclatorService nomSrv;

    @Autowired
    private FluxRegistruService fluxRegistruService;

    @Autowired
    private GospodarieRepository gospodarieRepository;

    @Override
    public ViewRegistruNomStare getByIndexRegistruOrNull(String indexRegistru) {
        if (indexRegistru == null) {
            throw new IllegalArgumentException("Index registru nedefinit");
        }

        return registruRepository.findByIndexRegistruNew(indexRegistru);
    }

    @Override
    public ViewRegistruNomStare getByIndexRegistruOrThrow(String indexRegistru) throws RegistruNotFoundException {

        ViewRegistruNomStare registru = getByIndexRegistruOrNull(indexRegistru);

        if (registru == null) {
            throw new RegistruNotFoundException("Nu exista intrare in registru cu identificator " + indexRegistru);
        }

        return registru;
    }

    @Override
    public Registru saveRegistru(Registru registru) {
        if (registru == null) {
            throw new IllegalArgumentException("Registru nedefinit");
        }

        /*if (registru.getIdRegistru() <= 0) {
            // todo:
            //registru.setIndexRegistru(indexGenerator.getNextIndex(Index.REGISTRU));
            registru.setDataRegistru(new Date());

            //todo: Stare registru
            //incarcare.setStareIncarcare(new StareIncarcare());
        }*/


        return registruRepository.save(registru);
    }

    @Override
    public GenericListResult<Registru> getListaRegistru(final RegistruSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo) {
        return registruRepository.getListResult(
                searchFilter != null ? new AndRepositoryCriteria() {
                    {
                        if (searchFilter.getDataRegistruStart() != null) {
                            add(new RepositoryCriteria<Date>("dataRegistru", Operation.GTE, searchFilter.getDataRegistruStart()));
                        }

                        if (searchFilter.getDataRegistruStop() != null) {
                            add(new RepositoryCriteria<Date>("dataRegistru", Operation.LTE, searchFilter.getDataRegistruStop()));
                        }

                        if (searchFilter.getIndexRegistru() != null) {
                            add(new RepositoryCriteria<Long>("indexRegistru", Operation.EQ, searchFilter.getIndexRegistru()));
                        }

                        if (searchFilter.getIdUat() != null) {
                            NomUat nomUat = new NomUat();
                            nomUat.setId(searchFilter.getIdUat());
                            add(new RepositoryCriteria<NomUat>("uat.id", Operation.EQ, nomUat));
                        }

                        if (searchFilter.getIdStareRegistru() != null) {
                            NomStareRegistru nomStareRegistru = new NomStareRegistru();
                            nomStareRegistru.setId(searchFilter.getIdStareRegistru());
                            add(new RepositoryCriteria<NomStareRegistru>("nomStareRegistru", Operation.EQ, nomStareRegistru));
                        }

                        if (searchFilter.getIdSursaRegistru() != null) {
                            NomSursaRegistru nomSursaRegistru = new NomSursaRegistru();
                            nomSursaRegistru.setId(searchFilter.getIdSursaRegistru());
                            add(new RepositoryCriteria<NomSursaRegistru>("nomSursaRegistru.id", Operation.EQ, nomSursaRegistru));
                        }

                        if (searchFilter.getIsRecipisaSemnata() != null) {
                            add(new RepositoryCriteria<Boolean>("isRecipisaSemnata", Operation.EQ, searchFilter.getIsRecipisaSemnata()));
                        }
                    }
                } : null, pagingInfo, sortInfo);
    }


    /**
     * actualizare recipisa si FK_NOM_STARE_REGISTRU dupa prelucrare xml si salvare in DB cu succes
     *
     * @param idRegistru   inregistrare din baza de date ce trebuie actualizata
     * @param ranDocDTO    info din xml
     * @param succesAsHtml mesajul de succes in format html
     */
    @Transactional(value = "registruTransactionManager", propagation = Propagation.REQUIRES_NEW)
    @Override
    public void actualizareRegistruCazSucces(Long idRegistru, RanDocDTO ranDocDTO, String succesAsHtml) {
        Registru registru = registruRepository.getOne(idRegistru);
        /*RECIPISA*/
        registru.setRecipisa(succesAsHtml.getBytes(Charset.forName("UTF-8")));
        /*FK_NOM_STARE_REGISTRU*/
        NomStareRegistru nomStareRegistru = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomStareRegistru, RanConstants.STARE_REGISTRU_SALVATA_COD);
        registru.setNomStareRegistru(nomStareRegistru);
        // notificare de corelare
        registru.setStareCorelare(2);
        //gospodarie
        Gospodarie gospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(),ranDocDTO.getIdentificatorGospodarie());
        registru.setGospodarie(gospodarie);
        //
        Registru savedRegistru = registruRepository.save(registru);
        fluxRegistruService.saveFluxRegistru(savedRegistru, nomStareRegistru, null);
    }

    /**
     * actualizare recipisa si FK_NOM_STARE_REGISTRU dupa prelucrare xml si salvare in DB cu eroare
     *
     * @param idRegistru  inregistrare din baza de date ce trebuie actualizata
     * @param ranDocDTO   info din xml
     * @param ex          exceptia rezultata in urma procesarii
     * @param errorAsHtml mesajul de eroare in format Html
     */
    @Transactional(value = "registruTransactionManager", propagation = Propagation.REQUIRES_NEW)
    @Override
    public void actualizareRegistruCazEroare(Long idRegistru, RanDocDTO ranDocDTO, Throwable ex, String errorAsHtml) {
        Registru registru = registruRepository.getOne(idRegistru);
        /*RECIPISA*/
        registru.setRecipisa(errorAsHtml.getBytes(Charset.forName("UTF-8")));
        /*FK_NOM_STARE_REGISTRU*/
        NomStareRegistru nomStareRegistru = null;
        if (ex instanceof SyntaxXmlException || ex instanceof DateRegistruValidationException) {
            nomStareRegistru = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomStareRegistru, RanConstants.STARE_REGISTRU_INVALIDATA_COD);
        } else {
            nomStareRegistru = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomStareRegistru, RanConstants.STARE_REGISTRU_EROARE_LA_SALVARE_COD);
        }
        Integer totalProcesari = registru.getTotalProcesare();
        if(totalProcesari == null){
            totalProcesari = 0;
            registru.setTotalProcesare(totalProcesari);
        } else {
            registru.setTotalProcesare(totalProcesari + 1);
        }

        registru.setNomStareRegistru(nomStareRegistru);
        //gospodarie
        Gospodarie gospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(),ranDocDTO.getIdentificatorGospodarie());
        registru.setGospodarie(gospodarie);
        //
        Registru savedRegistru = registruRepository.save(registru);
        if (ex instanceof RanBusinessException) {
            fluxRegistruService.saveFluxRegistru(savedRegistru, nomStareRegistru, fluxRegistruService.extractRespinsMesaj((RanBusinessException) ex));
        } else {
            fluxRegistruService.saveFluxRegistru(savedRegistru, nomStareRegistru, ex.getMessage());
        }
    }

    @Override
    public Registru getRegistruById(Long idRegistru) {
        return registruRepository.findOne(idRegistru);
    }

    @Override
    public  List<Registru> getAllUnprocessedErrorData(){

        return registruRepository.getAllErrorUnprocessedData();
    }
}
