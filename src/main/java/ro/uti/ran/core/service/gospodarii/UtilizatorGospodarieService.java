package ro.uti.ran.core.service.gospodarii;


import ro.uti.ran.core.model.portal.UtilizatorGospodarie;
import ro.uti.ran.core.model.registru.DetinatorPf;
import ro.uti.ran.core.model.registru.DetinatorPj;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.internal.gospodarii.UtilizatoriGospList;

import java.util.List;
import java.util.Map;

/**
 * Created by adrian.boldisor on 2/4/2016.
 */
public interface UtilizatorGospodarieService {
    List<UtilizatorGospodarie> getByIdUtilizatorGospodarie(long IdUtilizatorGospodarie);

    List<UtilizatorGospodarie> getByidUtilizator(long idUtilizator);

    List<UtilizatorGospodarie> getAllGospodariiAsociate(long idUtilizator);

    List<UtilizatorGospodarie> getByidGospodaries(long idGospodarie);

    UtilizatoriGospList getByIdUserListOfGospodariiPj(SortInfo sortInfo, PagingInfo pagingInfo, Long idUtilizator, Long idUta);



   // UtilizatoriGospList getByIdUserListForGospodariiPj(SortInfo sortInfo,PagingInfo pagingInfo, Long idUtilizator, GospodariiSearchFilter filter);

    UtilizatoriGospList getByIdUatListForGospodariiPj(SortInfo sortInfo, PagingInfo pagingInfo, Long idUser, Long utaId, GospodariiSearchFilter searchFilter);

    Boolean deleteAsignedGospodariePj(Long IdUser, Long idGosp, Long utaId);


    Integer setGospodariePj(Long idUser, Long idGosp);


}
