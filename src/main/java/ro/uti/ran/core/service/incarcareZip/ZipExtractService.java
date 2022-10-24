package ro.uti.ran.core.service.incarcareZip;

/**
 * Created by Stanciu Neculai on 03.Nov.2015.
 */

public interface ZipExtractService extends AsyncZipExtractorHandler {
    void processZipScheduled();

    void processUnprocessed();
}
