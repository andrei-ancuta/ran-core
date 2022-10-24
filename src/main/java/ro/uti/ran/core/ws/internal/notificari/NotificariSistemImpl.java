package ro.uti.ran.core.ws.internal.notificari;


import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.utils.ExceptionUtil;


/**
 * Created by Anastasia cea micuta on 11/22/2015.
 */
@WebService(
        serviceName = "NotificariSistemWebService",
        endpointInterface = "ro.uti.ran.core.ws.internal.notificari.NotificariSistem",
        targetNamespace = "http://notificari.internal.ws.core.ran.uti.ro",
        portName = "NotificariSistemServicePort"
)
@Service("NotificariSistemWS")
public class NotificariSistemImpl implements NotificariSistem {
    private static final Logger logger = LoggerFactory.getLogger(NotificariSistemImpl.class);

    @Autowired
    private NotificariSistemService notificariSistemService;

    @Autowired
    private ExceptionUtil exceptionUtil;
    
    @Override
    public void notificareRapoarteCentralizatoare(Integer sirutaUAT) throws RanException, RanRuntimeException {
        logger.info("--- START: notificareRapoarteCentralizatoare (sirutaUAT=" + sirutaUAT + ") ---");
        try {
        	notificariSistemService.notificareRapoarteCentralizatoare(sirutaUAT);
        } catch (Throwable th){
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
        logger.info("--- STOP: notificareRapoarteCentralizatoare ---");
    }

    @Override
    public void notificareCereriCompletareDate(Integer sirutaUAT, String identificatorGospodarie, String details) throws RanException, RanRuntimeException {       
    	 logger.info("--- START: notificareCereriCompletareDate (sirutaUAT=" + sirutaUAT +
                 ", identificatorGospodarie=" + identificatorGospodarie +
                 ", details: " + details + ") ---");        
    	 try {
        	notificariSistemService.notificareCereriCompletareDate(sirutaUAT, identificatorGospodarie, details);
        } catch (Throwable th){
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
        logger.info("--- STOP: notificareCereriCompletareDate ---");
    }

    @Override
    public void notificareCereriCorectieDate(Integer sirutaUAT, String identificatorGospodarie, String details) throws RanException, RanRuntimeException {       
    	 logger.info("--- START: notificareCereriCorectieDate (sirutaUAT=" + sirutaUAT +
                 ", identificatorGospodarie=" + identificatorGospodarie +
                 ", details: " + details + ") ---");        
    	 try {
        	notificariSistemService.notificareCereriCorectieDate(sirutaUAT, identificatorGospodarie, details);
        } catch (Throwable th){
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
        
        logger.info("--- STOP: notificareCereriCorectieDate ---");
    }

    @Override
    public void notificareRaportAdHoc(String username, String subiect, String continutHtml, byte[] atasament) throws RanException, RanRuntimeException {

        	logger.info("--- START: notificareRaportAdHoc (username=" + username + ", denumireRaport=" + subiect +
                ", continutHtml=" + continutHtml + ", atasament[length]=" + (atasament != null ? atasament.length : "null") + ") ---");

        	try {
        		 notificariSistemService.notificareRaportAdHoc(username, subiect, continutHtml, atasament);
	        } catch (Throwable th){
	              throw exceptionUtil.buildException(new RanRuntimeException(th));
	        }	
           
            logger.info("--- STOP: notificareRaportAdHoc ---");



    }

//    @Override
//    public void notificareSimplaRaportAdHoc(@WebParam(name = "username") String username, @WebParam(name = "denumireRaport") String subiect, @WebParam(name = "continutHtml") String continutHtml) {
//
//        logger.info("--- START: notificareRaportAdHoc (username=" + username + ", denumireRaport=" + subiect +
//                ", continutHtml=" + continutHtml + ") ---");
//        notificariSistemService.notificareRaportAdHoc(username, subiect, continutHtml);
//        logger.info("--- STOP: notificareRaportAdHoc ---");
//    }
}
