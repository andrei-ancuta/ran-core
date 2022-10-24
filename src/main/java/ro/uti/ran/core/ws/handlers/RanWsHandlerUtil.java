package ro.uti.ran.core.ws.handlers;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import net.sf.extcos.ComponentQuery;
import net.sf.extcos.ComponentScanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.messages.FaultData;
import ro.uti.ran.core.messages.MessageData;
import ro.uti.ran.core.messages.MessageRequestData;
import ro.uti.ran.core.messages.MessageResponseData;
import ro.uti.ran.core.service.parametru.ParametruService;
import ro.uti.ran.core.ws.handlers.annotation.NoMessageStore;
import ro.uti.ran.core.ws.handlers.filter.MessageStorageExecutor;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

@Component
@Profile(Profiles.PRODUCTION)
public class RanWsHandlerUtil implements IRanWsHandlerUtil {

    private static final String CLIENT_IP_HEADER_NAME = "message.client.ip.header.name";
    private String _CANONICAL_HOST_NAME = null;
    private Set<String> _OPERATIONS = new HashSet<String>();
    private static Set<String> _NO_LOGABLE_OPS = new HashSet<String>();
    private static final Log log = LogFactory.getLog(RanWsHandlerUtil.class);

    @Autowired
    private ExceptionUtil exceptionUtil;

    @Autowired
    private MessageStorageExecutor messageStorageExecutor;

    @Autowired
    private ParametruService parametruService;

    public static boolean isMessageStorable(String serviceName, String operationName) {
    	if(_NO_LOGABLE_OPS.contains(serviceName + "." + operationName)) {
    		return false;
    	}
    	
    	return true;
    }
    
    public void logToSystemOut(SOAPMessageContext smc) {

        boolean debug = false;

        if (!debug) {
            return;
        }

        logHttpHeaders(smc);

        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outboundProperty) {
            System.out.println("\nOutbound message:");
        } else {
            System.out.println("\nInbound message:");
        }

        SOAPMessage message = smc.getMessage();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            message.writeTo(baos);
            System.out.println(baos.toString());
        } catch (Exception e) {
            System.out.println("Exception in handler: " + e);
        }
    }


    public void logToSystemOut(MessageContext  smc) {

        boolean debug = false;

        if (!debug) {
            return;
        }

        logHttpHeaders(smc);

        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outboundProperty) {
            System.out.println("\nOutbound message:");
        } else {
            System.out.println("\nInbound message:");
        }



//        MessageContext message = smc.getMessage();
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            message.writeTo(baos);
//            System.out.println(baos.toString());
//        } catch (Exception e) {
//            System.out.println("Exception in handler: " + e);
//        }
    }

    private Map<String, List<String>> getHttpHeaders(MessageContext context) {

        @SuppressWarnings("unchecked")
		Map<String, List<String>> headers = (Map<String, List<String>>) context
                .get(MessageContext.HTTP_REQUEST_HEADERS);

        return headers;

    }


    private Map<String, List<String>> getHttpHeaders(SOAPMessageContext context) {

        @SuppressWarnings("unchecked")
        Map<String, List<String>> headers = (Map<String, List<String>>) context
                .get(MessageContext.HTTP_REQUEST_HEADERS);

        return headers;

    }

    private void logHttpHeaders(MessageContext context) {

        boolean request = ((Boolean) context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();

        if (request) {

            Map<String, List<String>> headers = getHttpHeaders(context);

            if (headers == null || headers.keySet().size() == 0) {
                return;
            }

            System.out.println("******************");
            System.out.println("HTTP HEADERS:");

            for (String header : headers.keySet()) {
                System.out.println(header + " : " + headers.get(header));
            }

            System.out.println("******************");
        }

    }


    private void logHttpHeaders(SOAPMessageContext context) {

        boolean request = ((Boolean) context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();

        if (request) {

            Map<String, List<String>> headers = getHttpHeaders(context);

            if (headers == null || headers.keySet().size() == 0) {
                return;
            }

            System.out.println("******************");
            System.out.println("HTTP HEADERS:");

            for (String header : headers.keySet()) {
                System.out.println(header + " : " + headers.get(header));
            }

            System.out.println("******************");
        }

    }

    public void fillWithWsdlServiceAndOperationName(SOAPMessageContext context, MessageRequestData messageRequest) {
        QName svcn = (QName) context.get(SOAPMessageContext.WSDL_SERVICE);
        QName opn = (QName) context.get(SOAPMessageContext.WSDL_OPERATION);

        String wsdlServiceName = svcn.getLocalPart();
        String wsdlOperationName = opn.getLocalPart();

        System.out.println("WSDL Service=" + wsdlServiceName);
        System.out.println("WSDL Operation=" + wsdlOperationName);

        messageRequest.setWsdlOperationName(wsdlOperationName);
        messageRequest.setWsdlServiceName(wsdlServiceName);

        String key = wsdlServiceName + "." + wsdlOperationName;
        if (_OPERATIONS.contains(key)) {
            messageRequest.setRanOperationType(key);
        } else {
            throw new IllegalArgumentException("Operation not supported!");
        }

    }

    public void fillWithSoapMessage(SOAPMessageContext context, MessageData message) {
    	
    	if(message instanceof MessageRequestData) {
    		message.setSoapMessage(RanWsHandlerFilter.getRequest());
    		return;
    	}
    	
    	message.setSoapMessage(RanWsHandlerFilter.getResponse());
    }

    public void fillWithFault(SOAPMessageContext context, MessageResponseData messageResponse)  {
   
        FaultData fault = new FaultData();
        fault.setFaultStackTrace(exceptionUtil.getExceptionStackTrace());
        messageResponse.setFault(fault);
    
    }

    public void fillWithIpAddress(SOAPMessageContext context, MessageRequestData messageRequest) {
        HttpServletRequest httpRequest = (HttpServletRequest) context.get(MessageContext.SERVLET_REQUEST);
        // find out client's ip address
        String ip = getClientIp(httpRequest);
        System.out.println("Client's ip address :" + ip);
        messageRequest.setIpAddress(ip);
        messageRequest.setHostName(_CANONICAL_HOST_NAME);
    }

    public void fillWithHttpHeaders(SOAPMessageContext context, MessageRequestData messageRequest) {
        Map<String, List<String>> headers = getHttpHeaders(context);
        messageRequest.setHttpHeaders(headers);

    }

    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();

        // Handle proxy
        String headerParameter = "X-Forwarded-For";
        String originalHeaderParameter = headerParameter;
        try{
           headerParameter = parametruService.getParametru(CLIENT_IP_HEADER_NAME).getValoare();
        } catch (Exception e){
            log.debug("Nu s-a putut prelua parameterul - " + CLIENT_IP_HEADER_NAME + " - din serviciul de parametrii");
        }

        String header = request.getHeader(originalHeaderParameter);
        if (header != null && !header.isEmpty()) {
            clientIp = header.split(",")[0];
        }
        header = request.getHeader(headerParameter);
        if (header != null && !header.isEmpty()) {
            clientIp = header.split(",")[0];
        }
        
        
        return clientIp;
    }

    @PostConstruct
    private void init() {

    	RanWsHandlerFilter.setMessageStorageExecutor(messageStorageExecutor);
        RanHandlerFilter.setMessageStorageExecutor(messageStorageExecutor);
    	
        try {
            _CANONICAL_HOST_NAME = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Set<Class<?>> services = new HashSet<Class<?>>();
        (new ComponentScanner()).getClasses(new ComponentQuery() {
            protected void query() {
                select().from("ro.uti.ran.core").andStore(
                        thoseAnnotatedWith(WebService.class).into(services));
            }
        });
        final Set<Class<?>> ancpi_services = new HashSet<Class<?>>();
        (new ComponentScanner()).getClasses(new ComponentQuery() {
            protected void query() {
                select().from("ro.ancpi.ran.core").andStore(
                        thoseAnnotatedWith(WebService.class).into(ancpi_services));
            }
        });

        services.addAll(ancpi_services);

        String key = null;
        for (Class<?> clazz : services) {
            for (Method method : clazz.getMethods()) {
                key = ((WebService) clazz.getAnnotation(WebService.class)).serviceName() + "." + method.getName(); 
                    _OPERATIONS.add(key);
                    
                if(method.isAnnotationPresent(NoMessageStore.class)) {
                	_NO_LOGABLE_OPS.add(key);
                } else if(clazz.isAnnotationPresent(NoMessageStore.class)) {
                	_NO_LOGABLE_OPS.add(key);
                }
                
            }

        }
    }
}
