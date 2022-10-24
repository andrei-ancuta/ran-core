package ro.uti.ran.core.business.scheduler.ibm;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import ro.uti.ran.core.business.scheduler.annotations.AppParametruEnable;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.parametru.ParametruService;

/**
 * Created by Mihai Plavichianu
 */
@Aspect
@Order(value = RanConstants.SCHEDULER_ORDER)
@Component
public class TaskSchedulerEnabler {
    public static final Logger log = LoggerFactory.getLogger(TaskSchedulerEnabler.class);

    @Autowired
    private ParametruService parametruService;
    
    @Around("@annotation(appParametruEnable)")
    public Object afterOperation(ProceedingJoinPoint joinPoint, AppParametruEnable appParametruEnable) throws Throwable {
       
    		boolean proceed = true; 
    		try {
    			proceed = Boolean.valueOf(parametruService.getParametru(appParametruEnable.value()).getValoare());
    		}catch(Throwable t) {
    			log.debug("Au fost probleme la preluarea paramterului " + appParametruEnable.value(), t);
    		}
    			
    		if(proceed) {
    			return joinPoint.proceed();
    		}
    		
    		return null;

    }


  
}
