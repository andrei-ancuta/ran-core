package ro.uti.ran.core.utils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Basic search filter object.
 *
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-16 09:43
 */
public class SearchFilter implements Serializable {

    /**
     * Lista id-uri inregistrari de exclus
     */
    private List<Long> excludedIds;

    /**
     * Lista id-uri intregistrari pentru cautare
     */
    private List<Long> onlyFromIds;


    public List<Long> getExcludedIds() {
        if (excludedIds == null) {
            excludedIds = new LinkedList<Long>();
        }
        return excludedIds;
    }

    public void setExcludedIds(List<Long> excludedIds) {
        this.excludedIds = excludedIds;
    }

    public List<Long> getOnlyFromIds() {
        if (onlyFromIds == null) {
            onlyFromIds = new LinkedList<Long>();
        }
        return onlyFromIds;
    }

    public void setOnlyFromIds(List<Long> onlyFromIds) {
        this.onlyFromIds = onlyFromIds;
    }
}
