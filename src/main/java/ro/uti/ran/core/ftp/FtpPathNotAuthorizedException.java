package ro.uti.ran.core.ftp;

/**
 * Created by Stanciu Neculai on 27.Oct.2015.
 */
public class FtpPathNotAuthorizedException extends Exception {
    public FtpPathNotAuthorizedException(String pathNotAuthorizedMessage) {
        super(pathNotAuthorizedMessage);
    }
}
