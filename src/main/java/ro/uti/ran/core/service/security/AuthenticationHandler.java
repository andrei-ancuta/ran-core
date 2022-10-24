package ro.uti.ran.core.service.security;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.uti.ran.core.model.portal.Sistem;
import ro.uti.ran.core.utils.ContextEnum;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Stanciu Neculai on 20.Oct.2015.
 */

public class AuthenticationHandler implements SOAPHandler<SOAPMessageContext> {
    public static final Logger log = LoggerFactory.getLogger(AuthenticationHandler.class);

    @Autowired
    private RanUserDetailsService ranUserDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    private AuthorizationService authorizationService;


    private static final String WSSE_NS_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    private static final QName QNAME_WSSE_USERNAMETOKEN = new QName(WSSE_NS_URI, "UsernameToken");
    private static final QName QNAME_WSSE_USERNAME = new QName(WSSE_NS_URI, "Username");
    private static final QName QNAME_WSSE_PASSWORD = new QName(WSSE_NS_URI, "Password");
    private static final QName QNAME_WSSE_SECURYTY = new QName(WSSE_NS_URI, "Security");

    private static final QName QNAME_WSSE_USERNAMETOKEN_INVALID = new QName("", "UsernameTokenInvalid");

    private static final QName QNAME_RAN_AUTHORIZATION_HEADER_NAME = new QName("core.ran.uti.ro", "ranAuthorization", "core");
    private static final String RAN_AUTHORIZATION_CONTEXT_CHILD = "context";
    private static final String RAN_AUTHORIZATION_ID_ENTITY_CHILD = "idEntity";


    private static final String WSSE_SECURITY_NAME = "Security";
    private static final String AUTH_ERROR_MSG = "Autentificare esuata : Informatiile din UsernameToken sunt incorecte sau utilizatorul furnizat nu are drepturi suficiente.";

    @Override
    public Set<QName> getHeaders() {
        HashSet<QName> headers = new HashSet<QName>();
        headers.add(QNAME_WSSE_SECURYTY);
        return headers;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        boolean response = false;
        Boolean outbound = (Boolean) context
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if ((outbound != null) && (!outbound.booleanValue())) {
            response = handleInboundMessage(context);
        }
        logToSystemOut(context);
        return response;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        //logToSystemOut(context);
        return true;
    }

    private boolean checkAuthorization(String userName, String password) {
        try {
            UserDetails userDetails = ranUserDetailsService.loadUserByUsername(userName);
            if (encoder.matches(password, userDetails.getPassword())) {
                if (authorizationService.isAuthorized(userDetails)) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (UsernameNotFoundException e) {
            return false;
        }
        return false;
    }

    private boolean handleInboundMessage(SOAPMessageContext context) {
        String wsseUsername = null;
        String wssePassword = null;
        try {
            SOAPHeader header = context.getMessage().getSOAPHeader();

            Iterator<?> headerElements = header.examineAllHeaderElements();

            while (headerElements.hasNext()) {
                SOAPHeaderElement headerElement = (SOAPHeaderElement) headerElements
                        .next();
                if (headerElement.getElementName().getLocalName()
                        .equals(WSSE_SECURITY_NAME)) {
                    SOAPHeaderElement securityElement = headerElement;
                    Iterator<?> it2 = securityElement.getChildElements();
                    while (it2.hasNext()) {
                        Node soapNode = (Node) it2.next();
                        if (soapNode instanceof SOAPElement) {
                            SOAPElement element = (SOAPElement) soapNode;
                            QName elementQname = element.getElementQName();
                            if (QNAME_WSSE_USERNAMETOKEN.equals(elementQname)) {
                                SOAPElement usernameTokenElement = element;
                                wsseUsername = getFirstChildElementValue(usernameTokenElement, QNAME_WSSE_USERNAME);
                                wssePassword = getFirstChildElementValue(usernameTokenElement, QNAME_WSSE_PASSWORD);
                                break;
                            }
                        }


                        if (wsseUsername != null) {
                            break;
                        }
                    }
                }
            }
            if (checkAuthorization(wsseUsername, wssePassword)) {
                Sistem sistemUser = ranUserDetailsService.loadSistemUserByUsername(wsseUsername);

                Iterator<?> itHeaderChild = header.getChildElements();
                boolean ranAuthorizationExists = false;
                while (itHeaderChild.hasNext()) {
                    SOAPHeaderElement hElement = (SOAPHeaderElement) itHeaderChild.next();
                    if (hElement.getElementQName().getLocalPart().equals(QNAME_RAN_AUTHORIZATION_HEADER_NAME.getLocalPart())) {
                        ranAuthorizationExists = true;
                        Iterator<?> itChildElements = hElement.getChildElements();
                        while (itChildElements.hasNext()) {
                            Node soapNode = (Node) itChildElements.next();
                            if (soapNode instanceof SOAPElement) {
                                SOAPElement element = (SOAPElement) soapNode;
                                String elementName = element.getElementName().getLocalName();
                                if (RAN_AUTHORIZATION_CONTEXT_CHILD.equals(elementName)) {
                                    if (sistemUser.getUat() != null) {
                                        String contextValue = ContextEnum.UAT.getValue();
                                        element.setValue(contextValue);
                                    }
                                }
                                if (RAN_AUTHORIZATION_ID_ENTITY_CHILD.equals(elementName)) {
                                    element.setValue(String.valueOf(sistemUser.getUat().getId()));
                                }
                            }
                        }
                    }
                }
                if (!ranAuthorizationExists) {
                    SOAPHeaderElement soapHeaderElement = header.addHeaderElement(QNAME_RAN_AUTHORIZATION_HEADER_NAME);
                    SOAPElement soapElementContext = soapHeaderElement.addChildElement(RAN_AUTHORIZATION_CONTEXT_CHILD);
                    SOAPElement soapElementIdEntity = soapHeaderElement.addChildElement(RAN_AUTHORIZATION_ID_ENTITY_CHILD);
//                    Sistem sistemUser = ranUserDetailsService.loadSistemUserByUsername(wsseUsername);
                    String contextValue = null;
                    if (sistemUser.getUat() != null) {
                        contextValue = ContextEnum.UAT.getValue();
                        soapElementContext.setValue(contextValue);
                        soapElementIdEntity.setValue(String.valueOf(sistemUser.getUat().getId()));
                    }
                }
                return true;
            } else {
                SOAPMessage soapMsg = context.getMessage();
                generateSOAPErrMessage(soapMsg, AUTH_ERROR_MSG);
                return false;
            }
        } catch (SOAPFaultException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error reading SOAP message context:", e);
            return false;
        }

    }

    private String getFirstChildElementValue(SOAPElement soapElement, QName qNameToFind) {
        String value = null;
        Iterator<?> it = soapElement.getChildElements(qNameToFind);
        while (it.hasNext()) {
            SOAPElement element = (SOAPElement) it.next(); //use first
            value = element.getValue();
        }
        return value;
    }


    @Override
    public void close(MessageContext context) {

    }

    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    private void generateSOAPErrMessage(SOAPMessage msg, String reason) {
        try {
            SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
            SOAPFault soapFault = soapBody.addFault();
            soapFault.setFaultString(reason);
            throw new SOAPFaultException(soapFault);
        } catch (SOAPException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void logToSystemOut(SOAPMessageContext smc) {

        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        try {
            if (!outboundProperty.booleanValue()) {

                SOAPMessage message = smc.getMessage();

                System.out.println("Incoming message:");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                message.writeTo(stream);

                System.out.println(stream.toString());
                System.out.println("=====================================");
            }
        } catch (Exception e) {
            System.out.println("Exception in handler: " + e);
        }
    }
}
