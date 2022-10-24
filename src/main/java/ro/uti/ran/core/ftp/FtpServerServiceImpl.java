package ro.uti.ran.core.ftp;

import org.apache.ftpserver.ConnectionConfig;
import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;

/**
 * Created by Stanciu Neculai on 14.Oct.2015.
 */
@Service("ftpServerService")
@PropertySource("classpath:ftp.properties")
public class FtpServerServiceImpl implements FtpServerService {
    public static final Logger LOG = LoggerFactory.getLogger(FtpServerServiceImpl.class);
    private static final String START_OP = "start";
    private static final String STOP_OP = "stop";
    private static final String RESTART_OP = "restart";

    @Autowired
    private FtpServer ftpServer;

    @PostConstruct
    public void init() {
        try {
            if(ftpServer.isStopped() || ftpServer.isSuspended()){
                ftpServer.start();
                LOG.debug("Waiting ftp client....");
            }
        } catch (AbstractMethodError error){
            LOG.error("Eroare:", error);
        } catch (FtpException e) {
            LOG.error("Eroare:", e);
        } catch (Exception e){
            LOG.error("Eroare:", e);
        } catch (Throwable e) {
            LOG.error("Eroare:", e);
        }
    }
    @PreDestroy
    public void close(){
        if(ftpServer != null){
            ftpServer.stop();
        }
    }

    private void doFtpServerOperation(String operation){
        try {
            if(operation.equals(START_OP)){
                ftpServer.start();
                LOG.debug("Waiting ftp client....");
            } else if(operation.equals(STOP_OP) && !ftpServer.isStopped()){
                ftpServer.stop();
            } else if(operation.equals(RESTART_OP)){
                LOG.debug("Restarting ftp server....");
                if(ftpServer.isStopped()){
                    ftpServer.start();
                    LOG.debug("Waiting ftp client....");
                } else {
                    ftpServer.stop();
                    ftpServer.start();
                    LOG.debug("Waiting ftp client....");
                }
            }
        } catch (AbstractMethodError error){
            LOG.error("Eroare:", error);
        } catch (FtpException e) {
            LOG.error("Eroare:", e);
        } catch (Exception e){
            LOG.error("Eroare:", e);
        } catch (Throwable e) {
            LOG.error("Eroare:", e);
        }
    }


    @Override
    public void startFtpServer() {
       doFtpServerOperation(START_OP);
    }

    @Override
    public void stopFtpServer() {
        doFtpServerOperation(STOP_OP);
    }

    @Override
    public void restartFtpServer() {
        doFtpServerOperation(RESTART_OP);
    }
}
