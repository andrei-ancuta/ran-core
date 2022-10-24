package ro.uti.ran.core.exception;

import org.apache.poi.UnsupportedFileFormatException;

/**
 * Created by adrian.boldisor on 3/16/2016.
 */
public class FileFormatException extends UnsupportedFileFormatException {
    public FileFormatException(String s) {
        super(s);
    }
}
