package ro.uti.ran.core.ftp;

import org.apache.ftpserver.ftplet.FtpFile;

/**
 * Created by Stanciu Neculai on 09.Feb.2016.
 */

public interface RanFileFilter{
   boolean isVisible(String filePath);
}
