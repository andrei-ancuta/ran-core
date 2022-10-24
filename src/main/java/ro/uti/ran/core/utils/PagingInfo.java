package ro.uti.ran.core.utils;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-08-31 12:42
 */
public class PagingInfo {

    private int firstResult;

    private int maxResults;

    public PagingInfo() {
    }

    public PagingInfo(int firstResult, int maxResults) {
        this.firstResult = firstResult;
        this.maxResults = maxResults;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    @Override
    public String toString() {
        return "PagingInfo{" +
                "firstResult=" + firstResult +
                ", maxResults=" + maxResults +
                '}';
    }
}
