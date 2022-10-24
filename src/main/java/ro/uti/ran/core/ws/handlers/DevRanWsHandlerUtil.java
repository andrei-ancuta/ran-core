package ro.uti.ran.core.ws.handlers;

import net.sf.extcos.ComponentQuery;
import net.sf.extcos.ComponentScanner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.messages.MessageData;
import ro.uti.ran.core.messages.MessageRequestData;
import ro.uti.ran.core.messages.MessageResponseData;
import ro.uti.ran.core.ws.handlers.annotation.NoMessageStore;
import ro.uti.ran.core.ws.handlers.filter.MessageStorageExecutor;

import javax.annotation.PostConstruct;
import javax.jws.WebService;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by neculai.stanciu on 15.12.2016.
 */
@Component(value = "ranWsHandlerUtil")
@Profile({Profiles.DEV,Profiles.TEST})
@Primary
public class DevRanWsHandlerUtil implements IRanWsHandlerUtil{

    @Autowired
    private MessageStorageExecutor messageStorageExecutor;

    private static final String CLIENT_IP_HEADER_NAME = "message.client.ip.header.name";
    private String _CANONICAL_HOST_NAME = null;
    private Set<String> _OPERATIONS = new HashSet<String>();
    private static Set<String> _NO_LOGABLE_OPS = new HashSet<String>();
    private static final Log log = LogFactory.getLog(DevRanWsHandlerUtil.class);

    @Override
    public void logToSystemOut(SOAPMessageContext smc) {

    }

    @Override
    public void logToSystemOut(MessageContext smc) {

    }

    @Override
    public void fillWithWsdlServiceAndOperationName(SOAPMessageContext context, MessageRequestData messageRequest) {

    }

    @Override
    public void fillWithSoapMessage(SOAPMessageContext context, MessageData message) {

    }

    @Override
    public void fillWithFault(SOAPMessageContext context, MessageResponseData messageResponse) {

    }

    @Override
    public void fillWithIpAddress(SOAPMessageContext context, MessageRequestData messageRequest) {

    }

    @Override
    public void fillWithHttpHeaders(SOAPMessageContext context, MessageRequestData messageRequest) {

    }


    @PostConstruct
    private void init() {
        RanWsHandlerFilter.setMessageStorageExecutor(messageStorageExecutor);
        RanHandlerFilter.setMessageStorageExecutor(messageStorageExecutor);
    }
}
