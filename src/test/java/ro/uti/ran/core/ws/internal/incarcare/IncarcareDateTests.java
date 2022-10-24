//package ro.uti.ran.core.ws.internal.incarcare;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//import ro.uti.ran.core.config.DevDataSourceConfig;
//import ro.uti.ran.core.config.Profiles;
//import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
//
///**
// * Author: vitalie.babalunga@greensoft.com.ro
// * Date: 2015-11-30 22:37
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
//        classes = {
//                DevDataSourceConfig.class,
//                RegistruPersistenceLayerConfig.class,
//                IncarcareDateTests.ImportConfiguration.class
//        })
//@ActiveProfiles(profiles = {Profiles.DEV})
//
//public class IncarcareDateTests {
//    @ContextConfiguration
//    @ComponentScan({
//            "ro.uti.ran.core.repository.portal",
//            "ro.uti.ran.core.ws.internal.incarcare",
//            "ro.uti.ran.core.ws.utils"
//    })
//    static class ImportConfiguration {
////        @Bean
////        public In getRegistruService() {
////            return new RegistruServiceImpl();
////        }
//
//    }
//
//    @Autowired
//    @Qualifier(value = "incarcareDateService")
//    private IncarcareDate transmitereDate;
//
//
//    @Test
//    public void testIncarcare
//
//}
