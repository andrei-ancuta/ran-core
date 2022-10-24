package ro.uti.ran.core.ws.internal.transmitere;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.registru.NomStareRegistru;
import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.ProcesareDateRegistruService;
import ro.uti.ran.core.service.registru.RegistruService;

import static ro.uti.ran.core.config.Profiles.DEV;
import static ro.uti.ran.core.config.Profiles.TEST;

/**
 * Created by Stanciu Neculai on 05.Nov.2015.
 */
@Component(value = "jmsMessageSender")
@Profile({DEV, TEST})
public class ProcesareDateRegistruAsyncDev implements ProcesareDateRegistruAsync {
    private static final Logger log = LoggerFactory.getLogger(ProcesareDateRegistruAsyncDev.class);
    @Autowired
    @Qualifier("procesareDateRegistruService")
    private ProcesareDateRegistruService procesareDateRegistruService;

    @Autowired
    private RegistruService registruService;

    @Override
    @Async
    public void procesareDateRegistru(Long idRegistru) {
        if (idRegistru != null) {
            try {
                Thread.sleep(1000);
                if(!isStareSalvatSauInvalid(idRegistru)) {
                    procesareDateRegistruService.procesareDateRegistru(idRegistru);
                }
            } catch (RanBusinessException | InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    private boolean isStareSalvatSauInvalid(Long idRegistru) {
        Registru registru = registruService.getRegistruById(idRegistru);
        if(registru != null){
            NomStareRegistru nomStareRegistru = registru.getNomStareRegistru();
            if(nomStareRegistru != null && (RanConstants.STARE_REGISTRU_INVALIDATA_COD.equals(nomStareRegistru.getCod())
                    || RanConstants.STARE_REGISTRU_SALVATA_COD.equals(nomStareRegistru.getCod()))){
                return true;
            }
        }
        return false;
    }
}
