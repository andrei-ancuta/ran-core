package ro.uti.ran.core.ftp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.service.parametru.ParametruService;

/**
 * Created by Stanciu Neculai on 09.Feb.2016.
 */
@Component
public class RanFileFilterImpl implements RanFileFilter {

    public static final String SLASH_SEPARATOR = "/";
    public static final String FTP_IGNORE_FILES_PARAM_KEY = "ftp.ignore.files";

    @Autowired
    private ParametruService parametruService;
    @Override
    public boolean isVisible(String filePath) {
        if(filePath == null || filePath.isEmpty() || SLASH_SEPARATOR.equals(filePath)){
            return true;
        }
        Parametru parametruListaDirs = parametruService.getParametru(FTP_IGNORE_FILES_PARAM_KEY);
        if(parametruListaDirs != null) {
            String[] listaDirsAscunse = null;
            if(parametruListaDirs.getValoare() != null) {
                listaDirsAscunse = parametruListaDirs.getValoare().split(",");
            } else if(parametruListaDirs.getValoareImplicita() != null) {
                listaDirsAscunse = parametruListaDirs.getValoare().split(",");
            }
            if(listaDirsAscunse != null){
                for (String dir : listaDirsAscunse){
                    if(filePath.contains(dir)){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
