package ro.uti.ran.core.ws.internal.gospodarii;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.service.gospodarii.GospodariiSearchFilter;
import ro.uti.ran.core.service.gospodarii.UtilizatorGospodarieService;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;

import javax.jws.WebService;

/**
 * Created by adrian.boldisor on 2/8/2016.
 */

@WebService(name ="gospodariiPJ", serviceName = "UtilizatorGospService",
         endpointInterface = "ro.uti.ran.core.ws.internal.gospodarii.UtilizatorGospService",
         targetNamespace = "http://info.internal.ws.core.ran.uti.ro",
         portName = "UtilizatorGospServiceport")
@Service("UtilizatorGospService")
public class UtilizatorGospServiceImpl implements UtilizatorGospService {


    @Autowired
    UtilizatorGospodarieService asociereGospodariiService;


    @Override
    public  UtilizatoriGospList getUtilizatorGospList(SortInfo sortInfo, PagingInfo pagingInfo, Long idUtilizator, Long idUta){
        return asociereGospodariiService.getByIdUserListOfGospodariiPj(sortInfo,pagingInfo,idUtilizator,idUta);
    }


//    @Override
//    public UtilizatoriGospList getByIdUserListForGospodariiPj(SortInfo sortInfo, PagingInfo pagingInfo, Long idUtilizator,  GospodariiSearchFilter searchFilter) {
//        return asociereGospodariiService.getByIdUserListForGospodariiPj(sortInfo, pagingInfo,idUtilizator,searchFilter);
//    }

    @Override
    public UtilizatoriGospList getByIdUatUserListForGospodariiPj(SortInfo sortInfo, PagingInfo pagingInfo, Long idUser ,Long utaId,GospodariiSearchFilter searchFilter) {
        return  asociereGospodariiService.getByIdUatListForGospodariiPj(sortInfo, pagingInfo,idUser,utaId ,searchFilter);
    }

    @Override
    public Boolean deleteAsignedGospodariePj(Long IdUser, Long idGosp,Long utaId) {
        return asociereGospodariiService.deleteAsignedGospodariePj(IdUser,idGosp, utaId);
    }

    @Override
    public Integer setGospodariePj(Long idUser, Long idGosp) {
        return asociereGospodariiService.setGospodariePj(idUser,idGosp);
    }

}


