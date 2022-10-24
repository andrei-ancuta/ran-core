package ro.uti.ran.core.service.incarcareZip;

import ro.uti.ran.core.model.portal.Incarcare;

/**
 * Created by Stanciu Neculai on 10.Dec.2015.
 */
public interface AsyncZipExtractorHandler {

    void processZipAsync(Long idIncarcare);
}
