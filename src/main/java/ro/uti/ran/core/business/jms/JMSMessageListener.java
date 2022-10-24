package ro.uti.ran.core.business.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.jms.*;

/**
 * Created with IntelliJ IDEA. User: mala
 */
@Component
public class JMSMessageListener implements MessageListener {

    private static Log LOGGER = LogFactory.getLog(JMSMessageListener.class);


    @Override
    public void onMessage(Message message) {
        // process map messages
        if (message instanceof MapMessage) {
            try {
                processMapMessage((MapMessage) message);
            } catch (JMSException e) {
                LOGGER.error("Error: ", e);
                e.printStackTrace();
            }
        }
        // process text message
        else if (message instanceof TextMessage) {
            try {
                processTextMessage((TextMessage) message);
            } catch (JMSException e) {
                LOGGER.error("Error: ", e);
                e.printStackTrace();
            }
        }
        // unknown message
        else {
            LOGGER.info("Unknown message type: " + message.getClass().getName());
        }
    }

    private void processMapMessage(MapMessage mapMessage) throws JMSException {
        // un-marshall context (TODO maybe make a message converter)
        LOGGER.debug("JMS Map message: " + mapMessage.toString());
    }


    /**
     * process test messages that could be a string message view as list of triggers Ex: 1452124,51254,415215
     *
     * @param textMessage
     * @throws JMSException
     */
    private void processTextMessage(TextMessage textMessage) throws JMSException {
        LOGGER.debug("JMS Text message: " + textMessage.getText());
    }
}
