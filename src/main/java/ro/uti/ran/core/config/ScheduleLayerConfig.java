package ro.uti.ran.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import ro.uti.ran.core.business.scheduler.SchedulerManager;
import ro.uti.ran.core.business.scheduler.ibm.QueueExecutor;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.naming.NamingException;


@Configuration
@Profile(Profiles.PRODUCTION)
@EnableScheduling
@PropertySource("classpath:scheduler/scheduler-env.properties")
public class ScheduleLayerConfig implements SchedulingConfigurer {

	@Autowired
	private SchedulerManager schedulerManager;
	
	
	@Autowired
	private QueueExecutor queueExecutor;

	@Autowired
	private Environment env;

	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
		scheduledTaskRegistrar.setScheduler(getScheduler());
	}
	
	private TaskScheduler getScheduler() {
		 return schedulerManager; 
	}

	
    private ConnectionFactory connectionFactory() {
    	ConnectionFactory connectionFactory = null;
    	
    	try {
	        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
	        jndiObjectFactoryBean.setJndiName(env.getProperty("sched.connection.fatory.queue"));
	        jndiObjectFactoryBean.setProxyInterface(ConnectionFactory.class);
	        jndiObjectFactoryBean.afterPropertiesSet();
	        connectionFactory = (ConnectionFactory)jndiObjectFactoryBean.getObject();
    	} catch(Exception e){
    		//nu se initializeaza
			e.printStackTrace();
    	}
    	
        return connectionFactory;
    }

    private Queue destination() throws NamingException {
    	Queue destination = null;
    	
    	try {
	        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
	        jndiObjectFactoryBean.setJndiName(env.getProperty("sched.connection.queue"));
	        jndiObjectFactoryBean.setProxyInterface(Queue.class);
	        jndiObjectFactoryBean.afterPropertiesSet();
	        destination = (Queue)jndiObjectFactoryBean.getObject();
    	} catch(Exception e){
    		//nu se initializeaza
			e.printStackTrace();
    	}
    	
        return destination;
    }
 
    @Bean
    public Object listenerContainerForScheduler() throws NamingException{
       
    	ConnectionFactory connectionFactory = connectionFactory();
    	Queue destination = destination();
    	
    	if(connectionFactory == null || destination == null) {
    		return new Object();
    	}
    			
    	DefaultMessageListenerContainer listenerContainer = new DefaultMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setMessageListener(queueExecutor);
        listenerContainer.setDestination(destination());

        // set message listener container properties
        listenerContainer.setConcurrency("2-8");
        listenerContainer.setIdleTaskExecutionLimit(5);
        listenerContainer.setIdleConsumerLimit(2);
        listenerContainer.setReceiveTimeout(1000);

        listenerContainer.afterPropertiesSet();
        listenerContainer.start();
        return listenerContainer;
    }
}
