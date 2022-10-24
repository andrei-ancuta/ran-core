package ro.uti.ran.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import ro.uti.ran.core.repository.registru.NomenclatorRepository;
import ro.uti.ran.core.repository.registru.NomenclatorRepositoryImpl;
import ro.uti.ran.core.service.exportNomenclatoare.NomenclatorClassificationService;
import ro.uti.ran.core.service.exportNomenclatoare.impl.NomenclatorClassificationServiceImpl;
import ro.uti.ran.core.service.registru.FluxRegistruService;
import ro.uti.ran.core.service.registru.FluxRegistruServiceImpl;
import ro.uti.ran.core.service.registru.RegistruService;
import ro.uti.ran.core.service.registru.RegistruServiceImpl;
import ro.uti.ran.core.ws.WsUtilsService;

/**
 * Created by Anastasia cea micuta on 1/28/2016.
 */
@ContextConfiguration
@ComponentScan({
        "ro.uti.ran.core.service.backend",
        "ro.uti.ran.core.service.index",
        "ro.uti.ran.core.service.parametru",
        "ro.uti.ran.core.service.gospodarii",
        "ro.uti.ran.core.ws.internal.interogare",
        "ro.uti.ran.core.ws.internal.transmitere",
        "ro.uti.ran.core.ws.utils"
})
public class ImportConfiguration {

    @Bean
    public RegistruService getRegistruService() {
        return new RegistruServiceImpl();
    }

    @Bean
    public FluxRegistruService getFluxRegistruService() {
        return new FluxRegistruServiceImpl();
    }

    @Bean
    public WsUtilsService getWsUtilsService() {
        return new WsUtilsService();
    }

    @Bean
    public NomenclatorClassificationService getNomenclatorClassificationService() {
        return new NomenclatorClassificationServiceImpl();
    }

    @Bean
    public NomenclatorRepository getNomenclatorRepository() {
        return new NomenclatorRepositoryImpl();
    }
}
