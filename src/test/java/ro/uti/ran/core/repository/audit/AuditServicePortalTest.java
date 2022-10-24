package ro.uti.ran.core.repository.audit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.PortalPersistenceLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.model.portal.OperatieSesiune;
import ro.uti.ran.core.model.portal.Sesiune;
import ro.uti.ran.core.repository.portal.OperatieSesiuneRepository;
import ro.uti.ran.core.ws.utils.AuditInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by adrian.boldisor on 5/5/2016.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                PortalPersistenceLayerConfig.class,
                AuditServicePortalTest.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class AuditServicePortalTest {


    @ContextConfiguration
    @ComponentScan({"ro.uti.ran.core.repository.portal"})
    static class ImportConfiguration {

    }



    @Autowired
    private OperatieSesiuneRepository operatieSesiuneRepo;


    @Autowired
    OperatieSesiuneRepository getOperatieSesiuneRepo;


    @Test
    public void checkInsertInoperatieSesiune(){

        List<OperatieSesiune> operatieSesiuneList = getOperatieSesiuneRepo.findAll();

        System.out.println(operatieSesiuneList.get(0));


        OperatieSesiune sess = new OperatieSesiune();

        sess.setDataOperatie(operatieSesiuneList.get(0).getDataOperatie());
        sess.setDescriere("Test Desriere");
        sess.setDescriereComplet(operatieSesiuneList.get(0).getDescriereComplet());
        sess.setSesiune(operatieSesiuneList.get(0).getSesiune());
        sess.setTipOperatie(operatieSesiuneList.get(0).getTipOperatie());
        operatieSesiuneRepo.save(sess);


    }
}
