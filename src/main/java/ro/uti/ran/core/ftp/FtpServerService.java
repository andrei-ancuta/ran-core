package ro.uti.ran.core.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.FtpException;

/**
 * Created by Stanciu Neculai on 14.Oct.2015.
 */
public interface FtpServerService {
   void startFtpServer();
   void stopFtpServer();
   void restartFtpServer();
}
