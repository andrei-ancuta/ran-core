//package ro.uti.ran.core.service.incarcareZip;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//import ro.uti.ran.core.config.Profiles;
//import ro.uti.ran.core.config.RootApplicationConfig;
//import ro.uti.ran.core.config.SecurityConfig;
//import ro.uti.ran.core.model.portal.Incarcare;
//import ro.uti.ran.core.model.portal.StareIncarcare;
//import ro.uti.ran.core.model.utils.StareIncarcareEnum;
//import ro.uti.ran.core.repository.portal.IncarcareRepository;
//import ro.uti.ran.core.repository.portal.StareIncarcareRepository;
//
///**
// * Created by Stanciu Neculai on 03.Nov.2015.
// */
//
//@ComponentScan(basePackages = "ro.uti.ran.core")
//@RunWith(SpringJUnit4ClassRunner.class)
//@ActiveProfiles(Profiles.DEV)
//@ContextConfiguration(
//        loader = AnnotationConfigContextLoader.class,
//        classes = {RootApplicationConfig.class,SecurityConfig.class})
//public class ZipExtractServiceTest {
//    public static final Logger log = LoggerFactory.getLogger(ZipExtractServiceTest.class);
//
//    @Autowired
//    private ZipExtractService zipExtractService;
//
//    @Autowired
//    private IncarcareRepository incarcareRepository;
//    @Autowired
//    private StareIncarcareRepository stareIncarcareRepository;
//
//    @Test
//    public void testExtractZip(){
//        Incarcare incarcare = incarcareRepository.findOne(11L);
//        StareIncarcare stareIncarcareReceptionat = stareIncarcareRepository.findOne(StareIncarcareEnum.RECEPTIONAT.getId());
//        incarcare.setStareIncarcare(stareIncarcareReceptionat);
//        incarcareRepository.save(incarcare);
//        zipExtractService.processZip();
//    }
//}
