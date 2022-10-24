package ro.uti.ran.core.ws.handlers;


import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.uti.ran.core.common.ContextHolder;
import ro.uti.ran.core.common.HeadersContext;
import ro.uti.ran.core.messages.MessageRequestData;
import ro.uti.ran.core.messages.MessageResponseData;
import ro.uti.ran.core.service.messages.MessageService;
import ro.uti.ran.core.ws.handlers.filter.MessageStorageExecutor;

/**
 * 
 * Procesare erori ws
 * @author mihai.plavichianu
 *
 */
@Component
public class RanWsHandler implements SOAPHandler<SOAPMessageContext> {

	private static final Log LOGGER = LogFactory.getLog(RanWsHandler.class);
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private IRanWsHandlerUtil ranWsHandlerUtil;
	 
    @Autowired
    private MessageStorageExecutor messageStorageExecutor;
    
	@Override
	public void close(MessageContext messageContext) {
		
	}

	@Override
	public boolean handleFault(SOAPMessageContext context)  {
		
		MessageResponseData messageResponse = new MessageResponseData();
		messageStorageExecutor.setResponseMessage(messageResponse);
		
		ranWsHandlerUtil.fillWithFault(context, messageResponse);
		ranWsHandlerUtil.fillWithSoapMessage(context, messageResponse);
		
		LOGGER.debug("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><");
		LOGGER.debug("<><><><><><><><><><><><><><>   FAULT    <><><><><><><><><><><><><><><><><");
		LOGGER.debug("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><");

		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		LOGGER.debug("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><");
		LOGGER.debug("Client::handleMessage()");
		ranWsHandlerUtil.logToSystemOut(context);

		Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
	
		if (outboundProperty) {
			handleOutboundMessage(context);
		} else {
			handleInboundMessage(context);
		}

		// continue other handler chain
		return true;
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void handleInboundMessage(SOAPMessageContext context) {

		ContextHolder.getInstance().disposeAll();

		MessageRequestData messageRequest = new MessageRequestData();
		messageStorageExecutor.setRequestMessage(messageRequest);
		
		ranWsHandlerUtil.fillWithIpAddress(context, messageRequest);
		ranWsHandlerUtil.fillWithSoapMessage(context, messageRequest);
		ranWsHandlerUtil.fillWithWsdlServiceAndOperationName(context, messageRequest);
		ranWsHandlerUtil.fillWithHttpHeaders(context, messageRequest);
		
		HeadersContext headersContext = getHeadersContext();
		headersContext.setHeaders((Map<String, List<String>>) context
				.get(MessageContext.HTTP_REQUEST_HEADERS));		

	}
	
	 
	public void handleOutboundMessage(SOAPMessageContext context) {
	
		MessageResponseData messageResponse = new MessageResponseData();
		messageStorageExecutor.setResponseMessage(messageResponse);
		
		ranWsHandlerUtil.fillWithSoapMessage(context, messageResponse);
	
	}
	
	private HeadersContext getHeadersContext() {
		HeadersContext headersContext = ContextHolder.getInstance().getHeadersContext();
		if (null == headersContext) {
			headersContext = new HeadersContext();
		}
		ContextHolder.getInstance().setHeadersContext(headersContext);

		return headersContext;
	}

}
