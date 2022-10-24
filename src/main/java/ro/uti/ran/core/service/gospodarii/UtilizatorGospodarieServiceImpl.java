package ro.uti.ran.core.service.gospodarii;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.audit.Audit;
import ro.uti.ran.core.exception.DuplicateElementException;
import ro.uti.ran.core.model.portal.UtilizatorGospodarie;
import ro.uti.ran.core.model.registru.DetinatorPj;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.repository.base.AbstractRepositoryFilter;
import ro.uti.ran.core.repository.portal.UtilizatorGospodarieRepository;
import ro.uti.ran.core.repository.registru.DetinatorPjRepository;
import ro.uti.ran.core.repository.registru.GospodarieRepository;
import ro.uti.ran.core.repository.registru.NomUatRepository;
import ro.uti.ran.core.utils.*;
import ro.uti.ran.core.utils.Order;
import ro.uti.ran.core.ws.internal.gospodarii.InfoUtilizatoriGosp;
import ro.uti.ran.core.ws.internal.gospodarii.UtilizatoriGospList;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static ro.uti.ran.core.audit.AuditOpType.ASIGNARE_GOSPODARIE_PJ;
import static ro.uti.ran.core.audit.AuditOpType.REVOCARE_GOSPODARIE_PJ;


/**
 * Created by adrian.boldisor on 2/4/2016.
 */
@Service
@Transactional("portalTransactionManager")
public class UtilizatorGospodarieServiceImpl implements UtilizatorGospodarieService {

    @Autowired
    UtilizatorGospodarieRepository utilizatorGospodarieRepository;

    @Autowired
    DetinatorPjRepository detinatorPjRepository;

    @Autowired
    GospodarieRepository gospodarieRepository;

    @Autowired
    NomUatRepository nomUatRepository;

    @Override
    public List<UtilizatorGospodarie> getByIdUtilizatorGospodarie(long IdUtilizatorGospodarie) {
        return utilizatorGospodarieRepository.findByIdUtilizatorGospodarie(IdUtilizatorGospodarie);
    }

    @Override
    public List<UtilizatorGospodarie> getByidUtilizator(long idUtilizator) {
        return utilizatorGospodarieRepository.findByidUtilizator(idUtilizator);
    }

    @Override
    public List<UtilizatorGospodarie> getAllGospodariiAsociate(long idUtilizator) {
        return utilizatorGospodarieRepository.findByidUtilizator(idUtilizator);
    }


//    @
//    public List<UtilizatorGospodarie> getAllGospodariiAsociate(long idUtilizator) {
//        return utilizatorGospodarieRepository.findAll();
//    }

    @Override
    public List<UtilizatorGospodarie> getByidGospodaries(long idGospodarie) {
        return utilizatorGospodarieRepository.findByidGospodarie(idGospodarie);
    }

    @Override
    public UtilizatoriGospList getByIdUserListOfGospodariiPj(SortInfo sortInfo,PagingInfo pagingInfo, Long idUtilizator, Long idUta) {

        if (sortInfo == null) {
            sortInfo = new SortInfo();
            SortCriteria sortCriteria = new SortCriteria("an", Order.desc);
            sortInfo.getCriterias().add(sortCriteria);
        }



        List<UtilizatorGospodarie> gospadariiListAsociate = getByidUtilizator(idUtilizator);

        List<Long> idsGospodarie = new ArrayList<Long>();
        for (UtilizatorGospodarie gospodariiAsociate : gospadariiListAsociate) {
            idsGospodarie.add(gospodariiAsociate.getIdGospodarie());
        }

        ///Lista de gopodarii  la cere se permite operatia de revocare
       List<Gospodarie> gospodarie = gospodarieRepository.findByIdGospodarieListAndUat(idsGospodarie, idUta);

        GenericListResult<DetinatorPj> utilizatorGospodarii = getListOfGospodariiAsociateByUserId(sortInfo,pagingInfo, idUtilizator, idsGospodarie);


        HashSet<Long> hashSet = new HashSet<>();
        List<Long> list = new ArrayList<>();  // Contains the intersection
        for(int i = 0; i < gospodarie.size(); i++){
            hashSet.add(gospodarie.get(i).getIdGospodarie());

        }

        for(int i = 0; i < utilizatorGospodarii.getItems().size(); i++) {
            if(hashSet.contains(utilizatorGospodarii.getItems().get(i).getGospodarie().getIdGospodarie())) {
                list.add(utilizatorGospodarii.getItems().get(i).getGospodarie().getIdGospodarie());
            }
        }



        for( DetinatorPj allowList : utilizatorGospodarii.getItems()){
            if(!list.contains(allowList.getGospodarie().getIdGospodarie())){
                allowList.getGospodarie().setIdGospodarie(-1L);
            }

        }



        return ListResultHelper.build(UtilizatoriGospList.class, utilizatorGospodarii,
                new ListResultHelper.Mapper<DetinatorPj, InfoUtilizatoriGosp>() {
                    @Override
                    public InfoUtilizatoriGosp map(DetinatorPj source) {
                        InfoUtilizatoriGosp infoUtilizatoriGosp = new InfoUtilizatoriGosp(source);
                        BeanUtils.copyProperties(infoUtilizatoriGosp, source);
                        return infoUtilizatoriGosp;
                    }
                });
    }



    @Override
    @Audit(opType =REVOCARE_GOSPODARIE_PJ)
    @Transactional(value = "portalTransactionManager", propagation = Propagation.REQUIRED)
    public Boolean deleteAsignedGospodariePj(Long IdUser, Long idGosp,Long utaId) {

        if (IdUser == null) {
            throw new IllegalArgumentException("Parametru IdUser nedefinit");
        }

        if (idGosp == null) {
            throw new IllegalArgumentException("Parametru idGosp nedefinit");
        }

        List<UtilizatorGospodarie> utilizatorGospodarieList = utilizatorGospodarieRepository.findByIdUserAndIdGospodariePj(IdUser, idGosp);
        if (utilizatorGospodarieList.size() > 1) {
             throw new IllegalArgumentException("Erroare in parametri");
        }

        if(utilizatorGospodarieList.size()==0){
            throw new IllegalArgumentException("there's nothing to delete");
        }

        Long idUtilizatorGospodarie = utilizatorGospodarieList.get(0).getIdUtilizatorGospodarie();
        Gospodarie gospodarie = gospodarieRepository.findByiIdGospodarieAndUat(idGosp, utaId);

        if(gospodarie == null){
            return false;
        }
        utilizatorGospodarieRepository.delete(idUtilizatorGospodarie);
        return true;
    }

    @Override
    @Audit(opType = ASIGNARE_GOSPODARIE_PJ)
    @Transactional(value = "portalTransactionManager", propagation = Propagation.REQUIRED)
    public Integer setGospodariePj(Long idUser, Long idGosp) {

     List<UtilizatorGospodarie> utilizatoriGospodarie = new ArrayList<UtilizatorGospodarie>();

         UtilizatorGospodarie  utilizatoriGospodarieEntity =  new UtilizatorGospodarie();
         utilizatoriGospodarieEntity.setIdUtilizator(idUser);
         utilizatoriGospodarieEntity.setIdGospodarie(idGosp);
         utilizatoriGospodarie.add(utilizatoriGospodarieEntity);


        List<UtilizatorGospodarie> utilizatorGospodarie =  utilizatorGospodarieRepository.findByIdUserAndIdGospodariePj(idUser, idGosp);

        if(utilizatorGospodarie.size() > 0 ){
            throw new DuplicateElementException("Gospodariea Pj deja este asignata");
        }

        List<UtilizatorGospodarie>  result =  utilizatorGospodarieRepository.save(utilizatoriGospodarie);

        return result.size();
    }


    @Override
    public UtilizatoriGospList getByIdUatListForGospodariiPj(SortInfo sortInfo, PagingInfo pagingInfo, Long idUser , Long utaId, GospodariiSearchFilter filter) {
        final GospodariiSearchFilter searchFilter = filter ==null ? new GospodariiSearchFilter(): filter;
        if (sortInfo == null) {
            sortInfo = new SortInfo();
            SortCriteria sortCriteria = new SortCriteria("an", Order.desc);
            sortInfo.getCriterias().add(sortCriteria);
        }




        List<UtilizatorGospodarie> gospadariiListAsociate = getAllGospodariiAsociate(idUser);


        List<Long> idsGospodarie = new ArrayList<Long>();
        for (UtilizatorGospodarie gospodariiAsociate : gospadariiListAsociate) {
            idsGospodarie.add(gospodariiAsociate.getIdGospodarie());
        }
        GenericListResult<DetinatorPj> utilizatorGospodarii = getListForGospodariiAsociateByIdUta(sortInfo,pagingInfo, idsGospodarie,utaId ,searchFilter);

    //   Gospodarie gospodarie = gospodarieRepository.findByiIdGospodarieAndUat(idGosp, utaId);

       return ListResultHelper.build(UtilizatoriGospList.class, utilizatorGospodarii,
                new ListResultHelper.Mapper<DetinatorPj, InfoUtilizatoriGosp>() {
                    @Override
                    public InfoUtilizatoriGosp map(DetinatorPj source) {
                        InfoUtilizatoriGosp infoUtilizatoriGosp = new InfoUtilizatoriGosp(source);
                        BeanUtils.copyProperties(infoUtilizatoriGosp, source);
                        return infoUtilizatoriGosp;
                    }
                });
    }

    /*
    Lista de gospodarii asociate by IDutilizator

     */
    private GenericListResult<DetinatorPj> getListOfGospodariiAsociateByUserId(final SortInfo sortInfo,PagingInfo pagingInfo, final Long idUtilizator, final List<Long> idsGospodarie) {

        return detinatorPjRepository.getListResult(
                new AbstractRepositoryFilter<DetinatorPj>() {
                    public Predicate[] buildFilter(CriteriaBuilder cb, CriteriaQuery cq, Root from) {
                        if (idUtilizator != null) {
                            Predicate gospodarieRestriction = from.get("gospodarie").get("idGospodarie").in(idsGospodarie);
                            addPredicate(gospodarieRestriction);
                        }
                        return predicatesArray();
                    }

                }, pagingInfo, sortInfo);
    }


    /*
       Lista de gospodarii care inca nu sunt asociate pe utilizator
    */
    private GenericListResult<DetinatorPj> getListForGospodariiAsociateByUserId(final SortInfo sortInfo,PagingInfo pagingInfo, final Long idUtilizator,  final List<Long> idsGospodarie, final GospodariiSearchFilter searchFilter) {

        return detinatorPjRepository.getListResult(
                new AbstractRepositoryFilter<DetinatorPj>() {
                    public Predicate[] buildFilter(CriteriaBuilder cb, CriteriaQuery cq, Root from) {
                        if (idUtilizator != null ) {
                            if(idsGospodarie.size()!=0){
                                Predicate gospodarieRestriction = from.get("gospodarie").get("idGospodarie").in(idsGospodarie);
                                addPredicate(cb.not(gospodarieRestriction));
                            }

                            if(StringUtils.isNotEmpty(searchFilter.getCui()) &&
                                    StringUtils.isNotEmpty(searchFilter.getDenumireFirma()) ){

                                  addPredicate(cb.or(cb.like(from.get("persoanaRc").get("cui"), searchFilter.getCui()),
                                              cb.like(from.get("persoanaRc").get("denumire"), searchFilter.getDenumireFirma()))
                                              );
                            } else {
                                if (StringUtils.isNotEmpty(searchFilter.getCui())) {
                                    addLikePredicate(cb, from.get("persoanaRc").get("cui"), searchFilter.getCui());
                                }


                                if (StringUtils.isNotEmpty(searchFilter.getDenumireFirma())) {
                                    addLikePredicate(cb, from.get("persoanaRc").get("denumire"), searchFilter.getDenumireFirma());
                                }

                            }


                            addPredicate(cb.equal(from.get("persoanaRc").get("idPersoanaRc"), idUtilizator));
                        }
                        return predicatesArray();
                    }

                }, pagingInfo, sortInfo);
    }



    private GenericListResult<DetinatorPj> getListForGospodariiAsociateByIdUta(final SortInfo sortInfo,PagingInfo pagingInfo, final List<Long> idsGospodarie, final Long utaId,final GospodariiSearchFilter searchFilter) {

        return detinatorPjRepository.getListResult(
                new AbstractRepositoryFilter<DetinatorPj>() {
                    public Predicate[] buildFilter(CriteriaBuilder cb, CriteriaQuery cq, Root from) {
                        if (utaId != null ) {
                            if(idsGospodarie.size()!=0){
                                Predicate gospodarieRestriction = from.get("gospodarie").get("idGospodarie").in(idsGospodarie);
                                addPredicate(cb.not(gospodarieRestriction));
                            }

                            if(StringUtils.isNotEmpty(searchFilter.getCui()) &&
                                    StringUtils.isNotEmpty(searchFilter.getDenumireFirma()) ){

                                addPredicate(cb.or(cb.like(from.get("persoanaRc").get("cui"), searchFilter.getCui()),
                                        cb.like(from.get("persoanaRc").get("denumire"), searchFilter.getDenumireFirma()))
                                );
                            } else {
                                if (StringUtils.isNotEmpty(searchFilter.getCui())) {
                                    addLikePredicate(cb, from.get("persoanaRc").get("cui"), searchFilter.getCui());
                                }


                                if (StringUtils.isNotEmpty(searchFilter.getDenumireFirma())) {
                                    addLikePredicate(cb, from.get("persoanaRc").get("denumire"), searchFilter.getDenumireFirma());
                                }

                            }
                            addPredicate(cb.equal(from.get("gospodarie").get("nomUat").get("id"),utaId));
                        }
                        return predicatesArray();
                    }

                }, pagingInfo, sortInfo);
    }



}
