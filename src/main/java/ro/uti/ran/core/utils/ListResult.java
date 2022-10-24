package ro.uti.ran.core.utils;

import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-08-27 15:06
 */
public class ListResult implements Serializable{

    private long firstResult = 0;
    private long recordsPerPage = 0;
    private long totalRecordCount = 0;

    public ListResult() {
    }

    public ListResult(ListResult listResult) {
        this.setFirstResult(listResult.getFirstResult());
        this.setRecordsPerPage(listResult.getRecordsPerPage());
        this.setTotalRecordCount(listResult.getTotalRecordCount());
    }

    public long getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(long firstResult) {
        this.firstResult = firstResult;
    }

    public long getRecordsPerPage() {
        return recordsPerPage;
    }

    public void setRecordsPerPage(long recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

    public long getTotalRecordCount() {
        return totalRecordCount;
    }

    public void setTotalRecordCount(long totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }
}
