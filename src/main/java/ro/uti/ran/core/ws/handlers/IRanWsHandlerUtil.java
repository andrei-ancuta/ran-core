package ro.uti.ran.core.ws.handlers;

import ro.uti.ran.core.messages.MessageData;
import ro.uti.ran.core.messages.MessageRequestData;
import ro.uti.ran.core.messages.MessageResponseData;

import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.List;
import java.util.Map;

/**
 * Created by neculai.stanciu on 15.12.2016.
 */
public interface IRanWsHandlerUtil {
    public void logToSystemOut(SOAPMessageContext smc);
    public void logToSystemOut(MessageContext smc);
    public void fillWithWsdlServiceAndOperationName(SOAPMessageContext context, MessageRequestData messageRequest);
    public void fillWithSoapMessage(SOAPMessageContext context, MessageData message);
    public void fillWithFault(SOAPMessageContext context, MessageResponseData messageResponse);
    public void fillWithIpAddress(SOAPMessageContext context, MessageRequestData messageRequest);
    public void fillWithHttpHeaders(SOAPMessageContext context, MessageRequestData messageRequest);
}
