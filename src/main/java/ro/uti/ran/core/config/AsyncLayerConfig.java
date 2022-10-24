package ro.uti.ran.core.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ro.uti.ran.core.service.exportNomenclatoare.task.AsyncNomenclatorsExport;
import ro.uti.ran.core.service.incarcareZip.CustomAsyncExceptionHandler;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Async layer configuration. All classes annotated with @Async are candidates for running as async task in the configured executors User:
 * mala
 */
@Configuration
@EnableAsync(mode = AdviceMode.PROXY, proxyTargetClass = true)
@PropertySource("classpath:async/async.properties")
public class AsyncLayerConfig implements AsyncConfigurer {

    private static final Log logger = LogFactory.getLog(AsyncLayerConfig.class);

    
    public static final String MESSAGE_REQUEST_EXECUTOR = "MessageRequestExecutor";
    public static final String MESSAGE_RESPONSE_EXECUTOR = "MessageResponseExecutor";
    
    @Autowired
    private Environment env;

    @Bean
    public AsyncNomenclatorsExport catalogsExportAsync() {
        return new AsyncNomenclatorsExport();
    }


    @Bean
    public Executor catalogsExportExecutor() {
        return getCustomExecutor("NomenclatorsExportExecutor");
    }

    @Bean
    public Executor zipExtractor() {
        return getCustomExecutor("ZipExtractor");
    }

    @Bean
    public Executor processUnprocessed() {
        return getCustomExecutor("ZipExtractor");
    }

    @Bean
    public Executor eroareTransmisiiJobExecutor(){
        return getCustomExecutor("EroareTransmisiiJobExecutor");
    }

    @Bean
    public Executor procesareDateRegistruErori(){
        return getCustomExecutor("EroareTransmisiiJobExecutor");
    }

    @Bean
    public Executor reProcesareDateRegistru(){
        return getCustomExecutor("EroareTransmisiiJobExecutor");
    }

    @Override
    public Executor getAsyncExecutor() {
        return getCustomExecutor("DefaultExecutor");
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }

    @Bean
    public Executor loggingExecutor() {
        return getCustomExecutor("LoggingExecutor");
    }

    @Bean
    public Executor authExecutor() {
        return getCustomExecutor("AuthExecutor");
    }

    @Bean
    public Executor validateAggregationExecutor() {
        return getCustomExecutor("ValidateAggregationExecutor");
    }
    
    @Bean
    public Executor messageRequestExecutor() {
        return getCustomExecutor(MESSAGE_REQUEST_EXECUTOR);
    }
    
    @Bean
    public Executor messageResponseExecutor() {
        return getCustomExecutor(MESSAGE_RESPONSE_EXECUTOR);
    }

    private Executor getCustomExecutor(String executorPrefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(env.getProperty(executorPrefix.concat(".corePoolSize"), Integer.class));
        executor.setMaxPoolSize(env.getProperty(executorPrefix.concat(".maxPoolSize"), Integer.class));
        executor.setQueueCapacity(env.getProperty(executorPrefix.concat(".queueCapacity"), Integer.class));
        executor.setThreadNamePrefix(executorPrefix.concat("-"));
        executor.setRejectedExecutionHandler(new AsyncDiscardPolicy());
        executor.initialize();
        return executor;
    }

    static class AsyncDiscardPolicy extends ThreadPoolExecutor.DiscardPolicy {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            logger.info("Task is rejected: " + r + " by " + e.getThreadFactory());
            super.rejectedExecution(r, e);
        }
    }
}
