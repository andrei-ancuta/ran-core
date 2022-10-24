package ro.uti.ran.core.ws.handlers;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.uti.ran.core.model.portal.Sesiune;
import ro.uti.ran.core.repository.portal.SesiuneRepository;
import ro.uti.ran.core.ws.utils.AuditInfo;
import ro.uti.ran.core.ws.utils.AuditInfoThreadLocal;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-12-22 15:00
 */
@Component
public class RanAuthorisationWsHandler implements SOAPHandler<SOAPMessageContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RanAuthorisationWsHandler.class);

    @Autowired
    SesiuneRepository sesiuneRepository;

    @PostConstruct
    public void init() {
        LOGGER.debug("initialized");
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean requestInbound = !(Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (requestInbound) {
            Map<String, List<String>> httpHeaders = getHttpHeaders(context);

            LOGGER.debug("Http Headers { ...");
            for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
                LOGGER.debug(entry.getKey() + "=" + entry.getValue());
            }
            LOGGER.debug(" ...Http Headers }");

            String username = getHeaderValue(httpHeaders, "X-USERNAME");
            String ctx = getHeaderValue(httpHeaders, "X-CONTEXT");

            //todo: care identificator de sesiune folosim ?
           // String jsessionId = getHeaderValue(httpHeaders, "X-JSESSIONID");

            String token = getHeaderValue(httpHeaders, "X-TOKEN");
            String remoteIp = getHeaderValue(httpHeaders, "X-REMOTE-IP");

            //token = "AQIC5wM2LY4SfcxvjWqx1kK4Og_xJ35OXZpH2Zl8n-_ER-o.*AAJTSQACMDEAAlNLABI5ODg3Nzc0NzAyNzQ3OTMxMjE.*";

            AuditInfo auditInfo = AuditInfoThreadLocal.get();
            if (auditInfo == null) {
                auditInfo = new AuditInfo();
                AuditInfoThreadLocal.set(auditInfo);
            }

            auditInfo.setUsername(username);
            auditInfo.setContext(ctx);
            auditInfo.setToken(token);
            auditInfo.setRemoteIp(remoteIp);

            if (StringUtils.isNotEmpty(token)) {
                List<Sesiune> sesiuneList = sesiuneRepository.findByToken(token);
                if(sesiuneList != null && sesiuneList.size() >= 1){
                    auditInfo.setSesiune(sesiuneList.get(0));
                }

            }
        } else {
            //remove thread local on outbound message
            AuditInfoThreadLocal.remove();
        }

        // continue other handler chain
        return true;
    }

    private Map<String, List<String>> getHttpHeaders(SOAPMessageContext context) {

        @SuppressWarnings("unchecked")
		Map<String, List<String>> headers = (Map<String, List<String>>) context
                .get(MessageContext.HTTP_REQUEST_HEADERS);

        return headers;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        // continue other handler chain
        return true;
    }

    public String getHeaderValue(Map<String, List<String>> headers, String name) {
        List<String> value = headers.get(name);
        return value != null && value.size() > 0 ? value.get(0) : null;
    }

    @Override
    public void close(MessageContext context) {

    }
}
