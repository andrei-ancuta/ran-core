package ro.uti.ran.core.service.registru;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.audit.Audit;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.*;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.model.utils.StareIncarcareEnum;
import ro.uti.ran.core.model.utils.StareRegistruPortalEnum;
import ro.uti.ran.core.repository.base.AbstractRepositoryFilter;
import ro.uti.ran.core.repository.base.RepositoryFilter;
import ro.uti.ran.core.repository.portal.*;
import ro.uti.ran.core.service.index.Index;
import ro.uti.ran.core.service.index.IndexGenerator;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;

import javax.persistence.criteria.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static ro.uti.ran.core.audit.AuditOpType.UPLOAD;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 15:58
 */
@Service
@Transactional("portalTransactionManager")
public class IncarcareServiceImpl implements IncarcareService {

    private static final Logger logger = LoggerFactory.getLogger(IncarcareServiceImpl.class);

    @Autowired
    IncarcareRepository incarcareRepository;

    @Autowired
    RegistruPortalRepository registruPortalRepository;

    @Autowired
    RegistruPortalDetailsRepository registruPortalDetailsRepository;

    @Autowired
    IndexGenerator indexGenerator;

    @Override
    public Incarcare getIncarcareById(Long id) {
        return incarcareRepository.findOne(id);
    }

    @Autowired
    private RegistruCoreRepository registruCoreRepository;

    @Autowired
    private FluxRegistruCoreRepository fluxRegistruCoreRepository;

    @Audit(opType = UPLOAD)
    @Override
    public Incarcare saveIncarcare(Incarcare incarcare) throws RanBusinessException {
        if (incarcare == null) {
            throw new IllegalArgumentException("Incarcare nedefinit");
        }

        if (incarcare.getId() == null || incarcare.getId() <= 0) {
            incarcare.setIndexIncarcare(indexGenerator.getNextIndex(Index.INCARCARE));
            incarcare.setDataIncarcare(new Date());

            StareIncarcare stareIncarcare = new StareIncarcare();
            stareIncarcare.setId(StareIncarcareEnum.RECEPTIONAT.getId());
            incarcare.setStareIncarcare(stareIncarcare);
        }

        //todo: validarile de business necesare unei incarcari

        return incarcareRepository.save(incarcare);
    }

    @Override
    public GenericListResult<Incarcare> getListaIncarcari(IncarcariSearchFilter incarcariSearchFilter, PagingInfo pagingInfo, SortInfo sortInfo) {
        final IncarcariSearchFilter searchFilter = incarcariSearchFilter == null ? new IncarcariSearchFilter() : incarcariSearchFilter;

        return incarcareRepository.getListResult(
                new AbstractRepositoryFilter<Incarcare>() {
                    @Override
                    public Predicate[] buildFilter(CriteriaBuilder cb, CriteriaQuery<Incarcare> cq, Root<Incarcare> from) {

                        if (StringUtils.isNotEmpty(searchFilter.getIndexIncarcare())) {
                            addPredicate(cb.equal(from.get(Incarcare_.indexIncarcare), searchFilter.getIndexIncarcare()));
                        }

                        if (searchFilter.getDataIncarcareStart() != null) {
                            addPredicate(cb.greaterThanOrEqualTo(from.get(Incarcare_.dataIncarcare), searchFilter.getDataIncarcareStart()));
                        }

                        if (searchFilter.getDataIncarcareStop() != null) {
                            addPredicate(cb.lessThanOrEqualTo(from.get(Incarcare_.dataIncarcare), searchFilter.getDataIncarcareStop()));
                        }

                        if (StringUtils.isNotEmpty(searchFilter.getDenumireFisier())) {
                            addLikePredicate(cb, from.get(Incarcare_.denumireFisier), searchFilter.getDenumireFisier());
                        }

                        if (StringUtils.isNotEmpty(searchFilter.getNumeUtilizator())) {
                            addLikePredicate(cb, from.join(Incarcare_.utilizator).get(Utilizator_.numeUtilizator), searchFilter.getNumeUtilizator());
                        }

                        if (searchFilter.getIdUat() != null && searchFilter.getIdUat().size() > 0) {
                            addPredicate(from.join(Incarcare_.uat).get(UAT_.id).in(searchFilter.getIdUat()));
                        }

                        return predicatesArray();
                    }
                },
                pagingInfo, sortInfo
        );
    }

    @Override
    public GenericListResult<RegistruPortal> getListaRegistruPortal(final Long idIncarcare, PagingInfo pagingInfo, SortInfo sortInfo) {
        if (idIncarcare == null) {
            throw new IllegalArgumentException("Parametru idIncarcare nespecificat");
        }

        return registruPortalRepository.getListResult(new RepositoryFilter<RegistruPortal>() {
            @Override
            public Predicate[] buildFilter(CriteriaBuilder cb, CriteriaQuery<RegistruPortal> cq, Root<RegistruPortal> from) {
                Path<Long> path = from.join(RegistruPortal_.incarcare).get(Incarcare_.id);
                return new Predicate[]{
                        cb.equal(path, idIncarcare)
                };
            }
        }, pagingInfo, sortInfo);
    }

    @Override
    public GenericListResult<RegistruPortalDetails> getDetaliiRegistruPortal(final Long idIncarcare, PagingInfo pagingInfo, SortInfo sortInfo) {
        if (idIncarcare == null) {
            throw new IllegalArgumentException("Parametru idIncarcare nespecificat");
        }

        return registruPortalDetailsRepository.getListResult(new RepositoryFilter<RegistruPortalDetails>() {

            @Override
            public Predicate[] buildFilter(CriteriaBuilder cb, CriteriaQuery<RegistruPortalDetails> cq, Root<RegistruPortalDetails> from) {
                Path<Long> path = from.join(RegistruPortalDetails_.incarcare).get(Incarcare_.id);
                return new Predicate[]{
                        cb.equal(path, idIncarcare)
                };
            }
        }, pagingInfo, sortInfo);
    }

    /**
     * Generare pachet raspuns in memory
     *
     * todo: implementare
     *
     * @param incarcare
     * @return
     * @throws Exception
     */
    @Override
    public InputStream genereazaPachetRaspuns(Incarcare incarcare) throws Exception {
        logger.debug("Preluare lista registru pentru incarcare #" + incarcare.getId());

        List<RegistruPortal> list = registruPortalRepository.findByIncarcare_Id(incarcare.getId());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ZipOutputStream zos = new ZipOutputStream(bos);

            for (RegistruPortal registruPortal : list) {

                logger.debug("denumire fisier = {}", registruPortal.getDenumireFisier());

                String entryName = buildZipEntryName(registruPortal.getDenumireFisier(), registruPortal.getStareRegistru().getCod());

                logger.debug("entryName = {}", entryName);

                /* File is not on the disk, test.txt indicates only the file name to be put into the zip */
                ZipEntry entry = new ZipEntry(entryName);

                zos.putNextEntry(entry);
                zos.write(creazaDetaliiRaspuns(incarcare,registruPortal));

                /* use more Entries to add more files and use closeEntry() to close each file entry */
                zos.closeEntry();
            }
            zos.close();

        } catch (Throwable th) {
            throw new RuntimeException("Eroare la genereare zip", th);
        }

        return new ByteArrayInputStream(bos.toByteArray());
    }

    private byte[] creazaDetaliiRaspuns(Incarcare incarcare,RegistruPortal registruPortal) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>###### INFO INCARCARE ######</h3>").append("<br/>");
        sb.append("Index incarcare: ").append(incarcare.getIndexIncarcare()).append("<br/>");
        sb.append("Data incarcare: ").append(incarcare.getDataIncarcare()).append("<br/>");
        if(incarcare.getStareIncarcare()!= null && incarcare.getStareIncarcare().getDenumire() != null) {
            sb.append("Stare: ").append(incarcare.getStareIncarcare().getDenumire()).append("<br/>");
        }
        if(incarcare.getUtilizator() != null && incarcare.getUtilizator().getNumeUtilizator() != null) {
            sb.append("Utilizator: ").append(incarcare.getUtilizator().getNumeUtilizator()).append("<br/>");
        }
        if(incarcare.getUat() != null && incarcare.getUat().getDenumire() != null) {
            sb.append("UAT: ").append(incarcare.getUat().getDenumire()).append("<br/>");
        }

        sb.append("<br/>");
        sb.append("<h3>###### INFO FISIER XML ######</h3>").append("<br/>");

        RegistruPortalDetails rpDetails = registruPortalDetailsRepository.findOne(registruPortal.getId());
        String indexRegistru = rpDetails.getIndexRegistru()== null || rpDetails.getIndexRegistru().isEmpty()?"-":rpDetails.getIndexRegistru();
        sb.append("Index registru: ").append(indexRegistru).append("<br/>");
        sb.append("Data registru: ").append(rpDetails.getDataRegistru()).append("<br/>");
        sb.append("Denumire fisier: ").append(rpDetails.getDenumireFisier()).append("<br/>");
        if(rpDetails.getCodStareRegistruPortal().equals(StareRegistruPortalEnum.TRANSMIS.getCod())){
            sb.append("Stare registru: ").append(rpDetails.getDenumireStareRegistruCore()).append("<br/>");
            RegistruCore coreRegistru = registruCoreRepository.findByIndexRegistru(rpDetails.getIndexRegistru());
            if(coreRegistru != null) {
            	 
            	FluxRegistruCore fluxRegistruCore = null;
            	List<FluxRegistruCore> mesaje = fluxRegistruCoreRepository.findByIdRegistruAndStareRegistru(coreRegistru.getIdRegistru(), rpDetails.getCodStareRegistruCore());
            	if(mesaje.size() > 0) {
            		 fluxRegistruCore = mesaje.get(mesaje.size()-1);
            	}
                 
                if(fluxRegistruCore != null) {
                    sb.append("Mesaj stare: ").append(fluxRegistruCore.getMesajStare()).append("<br/>");
                    if(coreRegistru.getIsRecipisaSemnata()){
                        sb.append("Detalii procesare: ").append(" Recipisa este semnata.");
                    } else {
                        if (coreRegistru.getRecipisa() != null) {
                            sb.append("Detalii procesare: ").append(new String(coreRegistru.getRecipisa()));
                        }
                    }
                }

            }
        } else {
            sb.append("Stare registru: ").append(rpDetails.getDenumireStareRegistruPortal()).append("<br/>");
            sb.append("Mesaj stare: ").append(rpDetails.getMesajStareFluxRegistruPortal()).append("<br/>");
        }

        return sb.toString().getBytes();
    }

    private String buildZipEntryName(String fileName, String status){
        int index = fileName.indexOf('.');
        String fileNameOnly = ( index > 0 ? fileName.substring(0, index) : fileName);
        return fileNameOnly + "_" + status + ".html";
    }
}
