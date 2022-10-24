package ro.uti.ran.core.business.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.registru.NomStareRegistru;
import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.ProcesareDateRegistruService;
import ro.uti.ran.core.service.registru.RegistruService;
import ro.uti.ran.core.ws.internal.transmitere.DateRegistruDto;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Created by Stanciu Neculai on 05.Nov.2015.
 */
@Component
public class JMSTransmisieListener implements MessageListener {
    private static final Logger log = LoggerFactory.getLogger(JMSTransmisieListener.class);

    @Autowired
    @Qualifier("procesareDateRegistruService")
    private ProcesareDateRegistruService procesareDateRegistruService;
    @Autowired
    private RegistruService registruService;

    @Override
    public void onMessage(Message message) {
        log.debug("Procesare mesaj de tranmisie asincron...");
        if (message instanceof ObjectMessage) {
            try {
                Object receivedObject = ((ObjectMessage) message).getObject();
                if (receivedObject instanceof DateRegistruDto) {
                    DateRegistruDto dateRegistruDto = (DateRegistruDto) receivedObject;
                    if (dateRegistruDto.getIdRegistru() != null) {
                        try {
                            // TODO...make tranzactionr register syncronization: procesarea mesajului din coada trebuie realizat ulterior commiterii tranzactiei pt. ca ID-ul din registru sa existe
                            Thread.sleep(3000);
                            Long idRegsitru = dateRegistruDto.getIdRegistru();
                            if(!isStareSalvatSauInvalid(idRegsitru)) {
                                procesareDateRegistruService.procesareDateRegistru(dateRegistruDto.getIdRegistru());
                            }
                        } catch (RanBusinessException e) {
                            processErrorMessage(e);
                        }
                    }
                }
            } catch (Throwable e) {
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

    private void processErrorMessage(RanBusinessException e) {
        log.error(e.getMessage(), e);
    }
}
