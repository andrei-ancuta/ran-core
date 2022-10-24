package ro.uti.ran.core.ftp;

import org.apache.ftpserver.ftplet.FileSystemFactory;
import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by Stanciu Neculai on 09.Feb.2016.
 */
@Component
public class RanFileSystemFactory implements FileSystemFactory {
    private static final Logger LOG = LoggerFactory.getLogger(RanFileSystemFactory.class);

    private boolean createHome;

    private boolean caseInsensitive;

    @Autowired
    private RanFileFilter fileFilter;


    @Override
    public FileSystemView createFileSystemView(User user) throws FtpException {
        synchronized (user) {
            // create home if does not exist
            if (createHome) {
                String homeDirStr = user.getHomeDirectory();
                File homeDir = new File(homeDirStr);
                if (homeDir.isFile()) {
                    LOG.warn("Not a directory :: " + homeDirStr);
                    throw new FtpException("Not a directory :: " + homeDirStr);
                }
                if ((!homeDir.exists()) && (!homeDir.mkdirs())) {
                    LOG.warn("Cannot create user home :: " + homeDirStr);
                    throw new FtpException("Cannot create user home :: "
                            + homeDirStr);
                }
            }

            FileSystemView fsView = new RanFileSystemView(user,
                    caseInsensitive,fileFilter);
            return fsView;
        }
    }

    public boolean isCreateHome() {
        return createHome;
    }

    public void setCreateHome(boolean createHome) {
        this.createHome = createHome;
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }
}
