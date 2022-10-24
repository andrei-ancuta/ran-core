package ro.uti.ran.core.config;

import org.apache.ftpserver.*;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import ro.uti.ran.core.ftp.RanFileSystemFactory;
import ro.uti.ran.core.ftp.RanUserManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by Stanciu Neculai on 28.Oct.2015.
 */
@Configuration
public class FtpServerConfig {
    public static final Logger LOG = LoggerFactory.getLogger(FtpServerConfig.class);
    private static final String FTP_PORT_KEY = "ftp.port";
    private static final String IDLE_TIMEOUT_KEY = "ftp.idleTimeout";
    private static final String USE_SSL_KEY = "ftp.useSsl";
    private static final String KEYSTORE_FILE_KEY = "ftp.keystoreFile";
    private static final String KEYSTORE_ALIAS_KEY = "ftp.keyAlias";
    private static final String KEYSTORE_PASSWORD = "ftp.keystorePassword";

    //   connection config
    private static final String ANONYMOUS_LOGIN_ENABLED_KEY = "ftp.anonymousLoginEnabled";
    private static final String LOGIN_FAILURE_DELAY_KEY = "ftp.loginFailureDelay";
    private static final String MAX_THREADS_KEY = "ftp.maxThreads";
    private static final String MAX_LOGINS_KEY = "ftp.maxLogins";
    private static final String MAX_ANONYMOUS_LOGINS_KEY = "ftp.maxAnonymousUsers";
    private static final String MAX_LOGIN_FAILURES_KEY ="ftp.maxLoginFailures";
    private static final String DEFAULT_LISTEN_ADDRESS = "0.0.0.0";
    // data connection config
    private static final String PASSIVE_ADDRESS_KEY = "ftp.passiveAddress";
    private static final String PASSIVE_EXTERNAL_ADDRESS_KEY = "ftp.passiveExternalAddress";
    private static final String PASSIVE_PORTS_KEY = "ftp.passivePorts";
    private static final String ACTIVE_CONN_ENABLED_KEY = "ftp.activeConnEnabled";

    private static final String SERVER_LISTEN_ADDRESS_KEY = "ftp.serverAddress";

    @Autowired
    private RanUserManager ranUserManager;
    @Autowired
    private Environment env;

    @Autowired
    private RanFileSystemFactory ranFileSystemFactory;


    @Bean
    public FtpServer ftpServer() {
        try {
            String passiveAddress = env.getProperty(PASSIVE_ADDRESS_KEY);
            String passiveExternalAddress = env.getProperty(PASSIVE_EXTERNAL_ADDRESS_KEY);
            String serverAddress = env.getProperty(SERVER_LISTEN_ADDRESS_KEY);
            String ports = env.getProperty(PASSIVE_PORTS_KEY);
            Boolean activeConnEnbled = Boolean.valueOf(env.getProperty(ACTIVE_CONN_ENABLED_KEY));
            if(passiveAddress == null || passiveAddress.isEmpty()){
                passiveAddress = DEFAULT_LISTEN_ADDRESS;
            }
            if(passiveExternalAddress == null || passiveExternalAddress.isEmpty()){
                passiveExternalAddress = DEFAULT_LISTEN_ADDRESS;
            }
            if(serverAddress == null || serverAddress.isEmpty()){
                serverAddress = DEFAULT_LISTEN_ADDRESS;
            }
            FtpServerFactory serverFactory = new FtpServerFactory();
            serverFactory.setUserManager(addUsersToFtpServer());
            serverFactory.setFileSystem(ranFileSystemFactory);
            // set the port of the listener
            ListenerFactory factory = new ListenerFactory();
            factory.setPort(Integer.valueOf(env.getProperty(FTP_PORT_KEY)));
            factory.setIdleTimeout(Integer.valueOf(env.getProperty(IDLE_TIMEOUT_KEY)));
            factory.setServerAddress(serverAddress);
            //define Connection configuration
            defineConnectionConf(serverFactory);
            // define SSL configuration
            defineSslConf(factory);
            DataConnectionConfigurationFactory dataConnectionConfigurationFactory = new DataConnectionConfigurationFactory();
            dataConnectionConfigurationFactory.setActiveEnabled(activeConnEnbled);
            dataConnectionConfigurationFactory.setPassiveAddress(passiveAddress);
            dataConnectionConfigurationFactory.setPassiveExternalAddress(passiveExternalAddress);
            dataConnectionConfigurationFactory.setPassivePorts(ports);
            factory.setDataConnectionConfiguration(dataConnectionConfigurationFactory.createDataConnectionConfiguration());
            // replace the default listener
            Listener listener = factory.createListener();
            serverFactory.addListener("default", listener);

            FtpServer server = serverFactory.createServer();
            // start the server
            return server;
        } catch (FtpException e){
            LOG.error(e.getMessage(),e);
        }
        return  null;
    }

    private void defineConnectionConf(FtpServerFactory serverFactory) {
        ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();

        String anonymousLoginStr = env.getProperty(ANONYMOUS_LOGIN_ENABLED_KEY);
        if(anonymousLoginStr != null && !anonymousLoginStr.isEmpty()) {
            boolean anonymousLogin =Boolean.valueOf(anonymousLoginStr);
            connectionConfigFactory.setAnonymousLoginEnabled(anonymousLogin);
        }

        String loginFailureDelayStr = env.getProperty(LOGIN_FAILURE_DELAY_KEY);
        if(loginFailureDelayStr != null && !loginFailureDelayStr.isEmpty()) {
            int loginFailureDelay =Integer.valueOf(loginFailureDelayStr);
            connectionConfigFactory.setLoginFailureDelay(loginFailureDelay);
        }

        String maxThreadsStr = env.getProperty(MAX_THREADS_KEY);
        if(maxThreadsStr != null && !maxThreadsStr.isEmpty()){
            int maxThreads = Integer.valueOf(loginFailureDelayStr);
            connectionConfigFactory.setMaxThreads(maxThreads);
        }
        String maxLoginsStr = env.getProperty(MAX_LOGINS_KEY);
        if(maxLoginsStr != null && !maxLoginsStr.isEmpty()){
            int maxLogins = Integer.valueOf(maxLoginsStr);
            connectionConfigFactory.setMaxLogins(maxLogins);
        }
        String maxAnonymousLoginsStr = env.getProperty(MAX_ANONYMOUS_LOGINS_KEY);
        if(maxAnonymousLoginsStr != null && !maxAnonymousLoginsStr.isEmpty()){
            int maxAnonymousLogins = Integer.valueOf(maxAnonymousLoginsStr);
            connectionConfigFactory.setMaxAnonymousLogins(maxAnonymousLogins);
        }
        String maxLoginFailuresStr = env.getProperty(MAX_LOGIN_FAILURES_KEY);
        if(maxLoginFailuresStr != null && !maxLoginFailuresStr.isEmpty()){
            int maxLoginFailures = Integer.valueOf(maxAnonymousLoginsStr);
            connectionConfigFactory.setMaxLoginFailures(maxLoginFailures);
        }


        ConnectionConfig connectionConfig = connectionConfigFactory.createConnectionConfig();
        serverFactory.setConnectionConfig(connectionConfig);
    }

    private void defineSslConf(ListenerFactory factory) {
        // define SSL configuration
        Boolean useSsl = Boolean.valueOf(env.getProperty(USE_SSL_KEY));
        if (useSsl) {
            SslConfigurationFactory ssl = new SslConfigurationFactory();
            ClassPathResource resource = new ClassPathResource(env.getProperty(KEYSTORE_FILE_KEY));
            try {
                if (resource.getFile() != null) {
                    ssl.setKeystoreFile(resource.getFile());
                } else {
                    ssl.setKeystoreFile(new File(env.getProperty(KEYSTORE_FILE_KEY)));
                }
            } catch (IOException e) {
                LOG.error("Eroare:", e);
            }
            ssl.setKeyAlias(env.getProperty(KEYSTORE_ALIAS_KEY));
            ssl.setKeystorePassword(env.getProperty(KEYSTORE_PASSWORD));
            // set the SSL configuration for the listener
            factory.setSslConfiguration(ssl.createSslConfiguration());
            factory.setImplicitSsl(true);
        }
    }

    private UserManager addUsersToFtpServer() throws FtpException {
        return ranUserManager;
    }
}
