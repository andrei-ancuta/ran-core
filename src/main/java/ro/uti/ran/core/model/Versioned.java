package ro.uti.ran.core.model;

import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-02 15:14
 */
public interface Versioned {

    String BASE_ID = "baseId";
    String DATA_START = "dataStart";
    String DATA_STOP = "dataStop";

    String LAST_MODIFIED_DATE = "lastModifiedDate";


    Date getDataStart();
    void setDataStart(Date dataStart);

    Date getDataStop();
    void setDataStop(Date dataStop);

    Long getBaseId();
    void setBaseId(Long baseId);

    boolean isLatestVersion();

}
