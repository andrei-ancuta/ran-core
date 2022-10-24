package ro.uti.ran.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ro.uti.ran.core.model.utils.RanConstants;

import java.util.Properties;
/**
 * Created by Sache on 12/15/2015.
 */
@Configuration
@EnableTransactionManagement(order = RanConstants.TRANSACTION_MANAGEMENT_ORDER)
@PropertySource("${devMailPropertiesPath:classpath:mail.properties}")
public class MailLayerConfig {

    @Autowired
    private Environment environment;

    @Bean
    public JavaMailSenderImpl javaMailSender() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(environment.getProperty("mail.host"));
        javaMailSender.setPort(environment.getProperty("mail.port", Integer.class));

        if (environment.getProperty("mail.username") != null) {
            javaMailSender.setUsername(environment.getProperty("mail.username"));
        }

        if (environment.getProperty("mail.password") != null) {
            javaMailSender.setPassword(environment.getProperty("mail.password"));
        }

        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", environment.getProperty("mail.transport.protocol"));

        if (environment.getProperty("mail.smtp.auth") != null) {
            properties.setProperty("mail.smtp.auth", environment.getProperty("mail.smtp.auth"));
        }

        if (environment.getProperty("mail.smtp.starttls.enable") != null) {
            properties.setProperty("mail.smtp.starttls.enable", environment.getProperty("mail.smtp.starttls.enable"));
        }

        if (environment.getProperty("mail.debug") != null) {
            properties.setProperty("mail.debug", environment.getProperty("mail.debug"));
        }

        if (environment.getProperty("mail.debug.auth") != null) {
            properties.setProperty("mail.debug.auth", environment.getProperty("mail.debug.auth"));
        }

        javaMailSender.setJavaMailProperties(properties);

        return javaMailSender;

    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:lang/messages");
        messageSource.setCacheSeconds(60); //reload messages every 60 seconds
        return messageSource;
    }
}
