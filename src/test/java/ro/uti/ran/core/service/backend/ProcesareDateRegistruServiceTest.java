package ro.uti.ran.core.service.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.*;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.repository.registru.NomenclatorRepositoryImpl;
import ro.uti.ran.core.repository.registru.RegistruRepository;

import java.io.*;
import java.util.List;


/**
 * Created by bogdan.ardeleanu on 10/19/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                PortalPersistenceLayerConfig.class,
                ImportConfiguration.class,
                NomenclatorRepositoryImpl.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class ProcesareDateRegistruServiceTest {
    @ContextConfiguration
    @ComponentScan({"ro.uti.ran.core.service.backend", "ro.uti.ran.core.repository", "ro.uti.ran.core.repository.registru"})
    static class ImportConfiguration {

    }

    @Autowired
    @Qualifier("procesareDateRegistruService")
    private ProcesareDateRegistruService procesareDateRegistruService;

    @Autowired
    RegistruRepository registruRepository;


    @Test
    public void testProcesareDateRegistruService() throws RanBusinessException {
        Long idRegistru = new Long(6322);//1722-cap034; 1738-cap5a
        //6322

//        List<Registru> res = registruRepository.findAllByIdCapitol(57);

        List<Registru> res = registruRepository.findAll();
        Long errorCount = 0L;
        StringBuilder stringBuilder = new StringBuilder();
            for(int i =0; i <res.size();i++ ){
            Long id = res.get(i).getIdRegistru();
            try {

                procesareDateRegistruService.procesareDateRegistru(id);
            } catch (Exception e) {

                if(!(e instanceof RanBusinessException)){
                    stringBuilder.append("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    stringBuilder.append(e.getClass());
                    stringBuilder.append(": =>");
                    stringBuilder.append(e.getMessage().toString());
                    errorCount++;


                    try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream("foo.txt"), "utf-8"))) {
                        writer.write(stringBuilder.toString());
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }



                continue;
            }

        }



        System.out.println(errorCount);

        System.out.println(stringBuilder);

    }


}
