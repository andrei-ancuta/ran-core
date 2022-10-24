package ro.uti.ran.core.ws.internal.transmitere;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.xml.model.AnRaportareCentralizator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Created by Stanciu Neculai on 05.Nov.2015.
 */
@Component(value = "jmsMessageSender")
@Profile(Profiles.PRODUCTION)
public class ProcesareDateRegistruAsyncImpl implements ProcesareDateRegistruAsync {

    @Autowired
    @Qualifier("jmsOperationsTransmisie")
    private JmsOperations jmsOperations;


    @Override
    public void procesareDateRegistru(final Long idRegistru) {
        jmsOperations.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                DateRegistruDto dateRegistruDto = new DateRegistruDto();
                dateRegistruDto.setIdRegistru(idRegistru);
                Message message = session.createObjectMessage(dateRegistruDto);
                return message;
            }
        });
    }
}
