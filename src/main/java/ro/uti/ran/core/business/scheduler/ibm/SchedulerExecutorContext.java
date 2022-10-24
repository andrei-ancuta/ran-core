package ro.uti.ran.core.business.scheduler.ibm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.business.scheduler.annotations.Cluster;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class SchedulerExecutorContext implements ApplicationContextAware {

    static final long serialVersionUID = 02L;
    private final Log log = LogFactory.getLog(SchedulerExecutorContext.class);
    private ApplicationContext applicationContext = null;

    public void execute(String className, String methodName) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        className = getNotProxiedClassName(className);

        Method method = getMethod(className, methodName);
        method.setAccessible(true);

        Object bean = null;
        if (applicationContext != null) {
            Map beans = applicationContext.getBeansOfType(Class.forName(className));

            if (beans.keySet().size() > 0) {
                Iterator iterator = beans.keySet().iterator();

                while (iterator.hasNext()) {
                    bean = beans.get(iterator.next());

                    try {
                        log.debug("SCHED00: The method " + className + "." + methodName + " will be invoked by the IBM Scheduler");
                        method.invoke(bean, new Object[]{});
                        log.debug("SCHED00: The method " + className + "." + methodName + " was invoked by the IBM Scheduler");
                        break;
                    } catch (Throwable t) {
                        log.debug("SCHED00: The method " + className + "." + methodName + " has exited with error", t);
                        t.printStackTrace();
                    }
                }

            }

        }


    }

    public boolean isClusterable(String className, String methodName) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        className = getNotProxiedClassName(className);
        Method method = getMethod(className, methodName);

        boolean clusterable = false;
        if (method.isAnnotationPresent(Cluster.class)) {
            clusterable = true;
        }


        System.out.println("The clusterable status for task " + className + ":" + methodName + " is " + clusterable);

        return clusterable;

    }

    public boolean isClusterable(Runnable runnable) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Entry<String, String> target = getTarget(runnable);
        return isClusterable(target.getKey(), target.getValue());
    }

    private Method getMethod(String className, String methodName) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        className = getNotProxiedClassName(className);
        Class clazz = Class.forName(className);
        Method method = clazz.getMethod(methodName);
        return method;
    }

    protected Entry<String, String> getTarget(Runnable runnable) {
        String className = ((ScheduledMethodRunnable) runnable).getTarget().getClass().getCanonicalName();
        className = getNotProxiedClassName(className);
        Method method = ((ScheduledMethodRunnable) runnable).getMethod();
        return new AbstractMap.SimpleEntry<String, String>(className, method.getName());
    }


    private String getNotProxiedClassName(String className) {
        if (className.contains("$")) {
            return className.substring(0, className.indexOf("$"));
        }

        return className;

    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
