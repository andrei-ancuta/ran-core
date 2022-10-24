package ro.uti.ran.core.ftp;

import org.apache.ftpserver.filesystem.nativefs.impl.NativeFtpFile;
import org.apache.ftpserver.ftplet.FtpFile;
import org.apache.ftpserver.ftplet.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * Created by Stanciu Neculai on 08.Feb.2016.
 */
public class RanFtpFile extends NativeFtpFile {

    private final Logger LOG = LoggerFactory.getLogger(NativeFtpFile.class);


    private RanFileFilter ranFileFilter;

    // the file name with respect to the user root.
    // The path separator character will be '/' and
    // it will always begin with '/'.
    private final String fileName;

    private final File file;

    private final User user;

    /**
     * Constructor, internal do not use directly.
     *
     * @param fileName
     * @param file
     * @param user
     */
    protected RanFtpFile(String fileName, File file, User user,RanFileFilter ranFileFilter) {
        super(fileName, file, user);

        this.fileName = fileName;
        this.file = file;
        this.user = user;
        this.ranFileFilter = ranFileFilter;
    }



    @Override
    public List<FtpFile> listFiles() {

        // is a directory
        if (!file.isDirectory()) {
            return null;
        }

        // directory - return all the files
        File[] files = returnVisibleFiles(file.listFiles());
        if (files == null) {
            return null;
        }

        // make sure the files are returned in order
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return f1.getName().compareTo(f2.getName());
            }
        });

        // get the virtual name of the base directory
        String virtualFileStr = getAbsolutePath();
        if (virtualFileStr.charAt(virtualFileStr.length() - 1) != '/') {
            virtualFileStr += '/';
        }

        // now return all the files under the directory
        FtpFile[] virtualFiles = new FtpFile[files.length];
        for (int i = 0; i < files.length; ++i) {
            File fileObj = files[i];
            String fileName = virtualFileStr + fileObj.getName();
            virtualFiles[i] = new RanFtpFile(fileName, fileObj, user,ranFileFilter);
        }

        return Collections.unmodifiableList(Arrays.asList(virtualFiles));
    }

    private File[] returnVisibleFiles(File[] files) {
        List<File> visibleFiles = new ArrayList<>();
        for(File file : files){
            if(ranFileFilter.isVisible(file.getAbsolutePath())){
                visibleFiles.add(file);
            }
        }
        return visibleFiles.toArray(new File[visibleFiles.size()]);
    }

    public RanFileFilter getRanFileFilter() {
        return ranFileFilter;
    }

    public void setRanFileFilter(RanFileFilter ranFileFilter) {
        this.ranFileFilter = ranFileFilter;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }

    public User getUser() {
        return user;
    }
}
