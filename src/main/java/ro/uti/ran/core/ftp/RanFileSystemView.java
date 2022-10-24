package ro.uti.ran.core.ftp;

import org.apache.ftpserver.filesystem.nativefs.impl.NameEqualsFileFilter;
import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpFile;
import org.apache.ftpserver.ftplet.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.StringTokenizer;

/**
 * Created by Stanciu Neculai on 08.Feb.2016.
 */
public class RanFileSystemView implements FileSystemView {

    private final Logger LOG = LoggerFactory.getLogger(RanFileSystemView.class);


    // the root directory will always end with '/'.
    private String rootDir;

    // the first and the last character will always be '/'
    // It is always with respect to the root directory.
    private String currDir;

    private final User user;


    private final boolean caseInsensitive;

    private RanFileFilter fileFilter;

    /**
     * Constructor - internal do not use directly, use {@link RanFileSystemFactory} instead
     */
    protected RanFileSystemView(User user,RanFileFilter fileFilter) throws FtpException {
        this(user, false,fileFilter);
    }

    /**
     * Constructor - internal do not use directly, use {@link RanFileSystemFactory} instead
     */
    public RanFileSystemView(User user, boolean caseInsensitive,RanFileFilter fileFilter)
            throws FtpException {
        if (user == null) {
            throw new IllegalArgumentException("user can not be null");
        }
        if (user.getHomeDirectory() == null) {
            throw new IllegalArgumentException(
                    "User home directory can not be null");
        }

        this.caseInsensitive = caseInsensitive;

        // add last '/' if necessary
        String rootDir = user.getHomeDirectory();
        rootDir = normalizeSeparateChar(rootDir);
        rootDir = appendSlash(rootDir);

        LOG.debug("Native filesystem view created for user \"{}\" with root \"{}\"", user.getName(), rootDir);

        this.rootDir = rootDir;

        this.user = user;

        this.fileFilter = fileFilter;

        currDir = "/";
    }

    @Override
    public FtpFile getHomeDirectory() throws FtpException {
        return new RanFtpFile("/", new File(rootDir), user,fileFilter);
    }

    @Override
    public FtpFile getWorkingDirectory() throws FtpException {
        FtpFile fileObj = null;
        if (currDir.equals("/")) {
            fileObj = new RanFtpFile("/", new File(rootDir), user,fileFilter);
        } else {
            File file = new File(rootDir, currDir.substring(1));
            fileObj = new RanFtpFile(currDir, file, user,fileFilter);

        }
        return fileObj;
    }

    @Override
    public boolean changeWorkingDirectory(String dir) throws FtpException {

        // not a directory - return false
        dir = getPhysicalName(rootDir, currDir, dir,
                caseInsensitive);
        File dirObj = new File(dir);
        boolean isDirVisible = fileFilter.isVisible(dir);
        if (!dirObj.isDirectory() || !isDirVisible) {
            return false;
        }

        // strip user root and add last '/' if necessary
        dir = dir.substring(rootDir.length() - 1);
        if (dir.charAt(dir.length() - 1) != '/') {
            dir = dir + '/';
        }

        currDir = dir;
        return true;
    }

    @Override
    public FtpFile getFile(String file) throws FtpException {
        // get actual file object
        String physicalName = getPhysicalName(rootDir,
                currDir, file, caseInsensitive);
        File fileObj = new File(physicalName);

        // strip the root directory and return
        String userFileName = physicalName.substring(rootDir.length() - 1);
        return new RanFtpFile(userFileName, fileObj, user,fileFilter);
    }

    @Override
    public boolean isRandomAccessible() throws FtpException {
        return true;
    }

    @Override
    public void dispose() {

    }

    /**
     * Normalize separate character. Separate character should be '/' always.
     */
    private String normalizeSeparateChar(final String pathName) {
        String normalizedPathName = pathName.replace(File.separatorChar, '/');
        normalizedPathName = normalizedPathName.replace('\\', '/');
        return normalizedPathName;
    }

    /**
     * Append trailing slash ('/') if missing
     */
    private String appendSlash(String path) {
        if (path.charAt(path.length() - 1) != '/') {
            return path + '/';
        } else {
            return path;
        }
    }

    /**
     * Get the physical canonical file name. It works like
     * File.getCanonicalPath().
     *
     * @param rootDir  The root directory.
     * @param currDir  The current directory. It will always be with respect to the
     *                 root directory.
     * @param fileName The input file name.
     * @return The return string will always begin with the root directory. It
     * will never be null.
     */
    protected String getPhysicalName(final String rootDir,
                                     final String currDir, final String fileName,
                                     final boolean caseInsensitive) {

        // normalize root dir
        String normalizedRootDir = normalizeSeparateChar(rootDir);
        normalizedRootDir = appendSlash(normalizedRootDir);

        // normalize file name
        String normalizedFileName = normalizeSeparateChar(fileName);
        String result;

        // if file name is relative, set resArg to root dir + curr dir
        // if file name is absolute, set resArg to root dir
        if (normalizedFileName.charAt(0) != '/') {
            // file name is relative
            String normalizedCurrDir = normalize(currDir, "/");

            result = normalizedRootDir + normalizedCurrDir.substring(1);
        } else {
            result = normalizedRootDir;
        }

        // strip last '/'
        result = trimTrailingSlash(result);

        // replace ., ~ and ..
        // in this loop resArg will never end with '/'
        StringTokenizer st = new StringTokenizer(normalizedFileName, "/");
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();

            // . => current directory
            if (tok.equals(".")) {
                // ignore and move on
            } else if (tok.equals("..")) {
                // .. => parent directory (if not root)
                if (result.startsWith(normalizedRootDir)) {
                    int slashIndex = result.lastIndexOf('/');
                    if (slashIndex != -1) {
                        result = result.substring(0, slashIndex);
                    }
                }
            } else if (tok.equals("~")) {
                // ~ => home directory (in this case the root directory)
                result = trimTrailingSlash(normalizedRootDir);
                continue;
            } else {
                // token is normal directory name

                if (caseInsensitive) {
                    // we're case insensitive, find a directory with the name, ignoring casing
                    File[] matches = new File(result)
                            .listFiles(new NameEqualsFileFilter(tok, true));

                    if (matches != null && matches.length > 0) {
                        // found a file matching tok, replace tok for get the right casing
                        tok = matches[0].getName();
                    }
                }

                result = result + '/' + tok;

            }
        }

        // add last slash if necessary
        if ((result.length()) + 1 == normalizedRootDir.length()) {
            result += '/';
        }

        // make sure we did not end up above root dir
        if (!result.startsWith(normalizedRootDir)) {
            result = normalizedRootDir;
        }

        return result;
    }

    /**
     * Prepend leading slash ('/') if missing
     */
    private String prependSlash(String path) {
        if (path.charAt(0) != '/') {
            return '/' + path;
        } else {
            return path;
        }
    }

    /**
     * Trim trailing slash ('/') if existing
     */
    private String trimTrailingSlash(String path) {
        if (path.charAt(path.length() - 1) == '/') {
            return path.substring(0, path.length() - 1);
        } else {
            return path;
        }
    }

    /**
     * Normalize separator char, append and prepend slashes. Default to
     * defaultPath if null or empty
     */
    private String normalize(String path, String defaultPath) {
        if (path == null || path.trim().length() == 0) {
            path = defaultPath;
        }

        path = normalizeSeparateChar(path);
        path = prependSlash(appendSlash(path));
        return path;
    }

    public RanFileFilter getFileFilter() {
        return fileFilter;
    }

    public void setFileFilter(RanFileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public String getCurrDir() {
        return currDir;
    }

    public void setCurrDir(String currDir) {
        this.currDir = currDir;
    }

    public User getUser() {
        return user;
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }
}
