package ro.uti.ran.core.business.scheduler.ibm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;


@Component
public class QueueExecutor implements MessageListener {

    private final Log log = LogFactory.getLog(SchedulerExecutorContext.class);

    @Autowired
    private SchedulerExecutorContext schedulerExecutorContext;

    @Override
    public void onMessage(Message message) {

        try {

            log.debug("SCHED00: A new message was received: " + message);

            if (message instanceof BytesMessage) {
                BytesMessage bytesMessage = (BytesMessage) message;
                byte[] bytes = new byte[(int) bytesMessage.getBodyLength()];
                bytesMessage.readBytes(bytes);
                ByteArrayInputStream b = new ByteArrayInputStream(bytes);
                ObjectInputStream o = new ObjectInputStream(b);
                Map map = (HashMap) o.readObject();
                log.debug("SCHED00: The method " + (String) map.get(WASScheduler.CLASS_NAME) + "." + (String) map.get(WASScheduler.METHOD_NAME) + " will be executed");
                schedulerExecutorContext.execute((String) map.get(WASScheduler.CLASS_NAME), (String) map.get(WASScheduler.METHOD_NAME));
            }

            message.acknowledge();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}


