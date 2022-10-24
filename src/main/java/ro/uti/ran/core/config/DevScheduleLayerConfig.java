package ro.uti.ran.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import ro.uti.ran.core.business.scheduler.SchedulerManager;
import ro.uti.ran.core.business.scheduler.ibm.QueueExecutor;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.naming.NamingException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


@Configuration
@Profile(Profiles.DEV)
@EnableScheduling
@PropertySource("classpath:scheduler/dev-scheduler-env.properties")
public class DevScheduleLayerConfig implements SchedulingConfigurer {

	@Autowired
	private Environment environment;

	@Bean(destroyMethod = "shutdown")
	public Executor taskExecutor() {
		return Executors.newScheduledThreadPool(environment.getProperty("sched.core.pool.size", Integer.class));
	}

	@Bean
	public TaskScheduler taskScheduler() {
		return new ConcurrentTaskScheduler((ScheduledExecutorService) taskExecutor());
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskScheduler());
	}
}
