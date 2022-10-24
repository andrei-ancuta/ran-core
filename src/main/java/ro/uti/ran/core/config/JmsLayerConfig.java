package ro.uti.ran.core.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jms.connection.TransactionAwareConnectionFactoryProxy;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ErrorHandler;
import ro.uti.ran.core.business.jms.JMSMessageListener;
import ro.uti.ran.core.business.jms.JMSTransmisieListener;
import ro.uti.ran.core.model.utils.JMSContextEnum;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.naming.NamingException;

/**
 * Configuration layer for JMS provider. For now JMS layer is only available on production profile
 * User: mala
 */
@Configuration
@Profile(Profiles.PRODUCTION)
@PropertySource("classpath:jms/jms-env.properties")
public class JmsLayerConfig {

    private static final Log logger = LogFactory.getLog(JmsLayerConfig.class);

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("registruTransactionManager")
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JMSMessageListener jmsMessageListener;
    @Autowired
    private JMSTransmisieListener jmsTransmisieListener;


    @Bean
    public ConnectionFactory connectionFactory() throws NamingException {
        return createConnectionFactory(JMSContextEnum.DEFAULT);
    }

    @Bean
    public Queue destination() throws NamingException {
       return createDestination(JMSContextEnum.DEFAULT);
    }

    @Bean
    public JmsOperations jmsOperations() throws NamingException {
       return createJmsOperations(JMSContextEnum.DEFAULT);
    }

    @Bean
    public DefaultMessageListenerContainer listenerContainer() throws NamingException {
        return createListenerContainer(JMSContextEnum.DEFAULT,jmsMessageListener);
    }
    //config jms transmisie
    @Bean
    public ConnectionFactory connectionFactoryTransmisie() throws NamingException {
        return createConnectionFactory(JMSContextEnum.TRANSMISIE);
    }

    @Bean
    public Queue destinationTransmisie() throws NamingException {
        return createDestination(JMSContextEnum.TRANSMISIE);
    }

    @Bean(name = "jmsOperationsTransmisie")
    public JmsOperations jmsOperationsTransmisie() throws NamingException {
        return createJmsOperations(JMSContextEnum.TRANSMISIE);
    }

    @Bean
    public DefaultMessageListenerContainer listenerContainerTransmisie() throws NamingException {
        return createListenerContainer(JMSContextEnum.TRANSMISIE,jmsTransmisieListener);
    }

    private ConnectionFactory createConnectionFactory(JMSContextEnum jmsContextEnum) throws NamingException {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName(env.getProperty(jmsContextEnum.getConnectionFactoryProperty()));
        jndiObjectFactoryBean.setProxyInterface(ConnectionFactory.class);
        jndiObjectFactoryBean.afterPropertiesSet();
        ConnectionFactory connectionFactory = (ConnectionFactory) jndiObjectFactoryBean.getObject();
        // TODO cache connections
        return connectionFactory;
    }

    private Queue createDestination(JMSContextEnum jmsContextEnum) throws NamingException {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName(env.getProperty(jmsContextEnum.getQueueProperty()));
        jndiObjectFactoryBean.setProxyInterface(Queue.class);
        jndiObjectFactoryBean.afterPropertiesSet();
        Queue destination = (Queue) jndiObjectFactoryBean.getObject();
        return destination;
    }

    private JmsOperations createJmsOperations(JMSContextEnum jmsContextEnum)  throws NamingException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        TransactionAwareConnectionFactoryProxy connectionFactoryProxy =
                new TransactionAwareConnectionFactoryProxy(createConnectionFactory(jmsContextEnum));
        jmsTemplate.setConnectionFactory(connectionFactoryProxy);
        jmsTemplate.setDefaultDestination(createDestination(jmsContextEnum));
        return jmsTemplate;
    }

    private DefaultMessageListenerContainer createListenerContainer(JMSContextEnum jmsContextEnum,Object jmsListenerObject) throws NamingException {
        DefaultMessageListenerContainer listenerContainer = new DefaultMessageListenerContainer();
        listenerContainer.setConnectionFactory(createConnectionFactory(jmsContextEnum));
        listenerContainer.setMessageListener(jmsListenerObject);
        listenerContainer.setDestination(createDestination(jmsContextEnum));
        // transaction manager should be the same as the database one
        // JTA transaction manager for supporting XA transaction as the datasource on
        // production is configured as XA datasource
        listenerContainer.setTransactionManager(transactionManager);

        listenerContainer.setCacheLevelName("CACHE_CONSUMER");
        // set message listener container properties
        listenerContainer.setConcurrency(env.getProperty("jms.concurrency"));
        listenerContainer.setIdleTaskExecutionLimit(env.getProperty("jms.idleTaskExecutionLimit", Integer.class));
        listenerContainer.setIdleConsumerLimit(env.getProperty("jms.idleConsumerLimit", Integer.class));
        listenerContainer.setReceiveTimeout(env.getProperty("jms.receiveTimeout", Integer.class));

        listenerContainer.setErrorHandler(new ErrorHandler() {
            @Override
            public void handleError(Throwable t) {
                logger.error(t);
            }
        });

        listenerContainer.afterPropertiesSet();
        listenerContainer.start();
        return listenerContainer;
    }
}
