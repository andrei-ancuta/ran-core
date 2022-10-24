package ro.uti.ran.core.service.gospodarii;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.PortalPersistenceLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.model.portal.UtilizatorGospodarie;
import ro.uti.ran.core.model.registru.DetinatorPf;
import ro.uti.ran.core.model.registru.DetinatorPj;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.exportNomenclatoare.impl.NomenclatorClassificationServiceImpl;
import ro.uti.ran.core.service.registru.RegistruSearchFilter;
import ro.uti.ran.core.utils.Order;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortCriteria;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.internal.gospodarii.InfoUtilizatoriGosp;
import ro.uti.ran.core.ws.internal.gospodarii.UtilizatoriGospList;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrian.boldisor on 2/4/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                PortalPersistenceLayerConfig.class,
                ImportConfiguration.class

        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class AsociereGospodariiServiceTest {

     @Autowired
     UtilizatorGospodarieService asociereGospodariiService;




    @Test
    public void getGospodariiAsociateByIdTest()  {
        List<UtilizatorGospodarie> listGospodariiAsociate =  asociereGospodariiService.getByIdUtilizatorGospodarie(1);
        System.out.println("result =" + listGospodariiAsociate.size() );

    }

    @Test
    public void getGospodariiAsociateByIdUserTest()  {
        List<UtilizatorGospodarie> listGospodariiAsociateByIdUtilizator =  asociereGospodariiService.getByidUtilizator(1000);
        System.out.println("result =" + listGospodariiAsociateByIdUtilizator.size() );
    }









    @Test
    public void getDetinatorPfByIdGospodarie(){
        SortInfo sortInfo = new SortInfo();
        UtilizatoriGospList utilizatoriGospList =  asociereGospodariiService.getByIdUserListOfGospodariiPj(sortInfo,new PagingInfo(),1000L,100L);
        List <InfoUtilizatoriGosp>infoUtilizatoriGosps =  utilizatoriGospList.getTotalListGospodariiPjByUser();
        for(InfoUtilizatoriGosp infoUtilizatoriGosp : infoUtilizatoriGosps){

                  System.out.println(infoUtilizatoriGosp);

        }
     }


    @Test
    public void getGospodariiAsociateByIdGospodarie(){
        List<UtilizatorGospodarie> listGospodariiasociateByIdGospodarie= asociereGospodariiService.getByidGospodaries(1150);
        System.out.println("result =" + listGospodariiasociateByIdGospodarie.size());

    }

//    @Test
//    public void getByIdUserListForGospodariiPjTest(){
//        SortInfo sortInfo = new SortInfo();
//        SortCriteria sortCriteria = new SortCriteria();
//        sortCriteria.setOrder(Order.desc);
//        sortCriteria.setPath("gospodarie.identificator");
//        sortInfo.getCriterias().add(sortCriteria);
//
//
//        GospodariiSearchFilter filter = new GospodariiSearchFilter();
//
//        UtilizatoriGospList utilizatoriGospList = asociereGospodariiService.getByIdUserListForGospodariiPj(sortInfo, new PagingInfo(), 1000L,filter);
//
//
//
//
//    }



    @Test
    public void deleteGospodariiPjFromUser(){

      //  asociereGospodariiService.deleteAsignedGospodariePj(1000L,1150L);
    }

}
