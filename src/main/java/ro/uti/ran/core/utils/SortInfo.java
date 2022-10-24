package ro.uti.ran.core.utils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-08-31 12:42
 */
public class SortInfo implements Serializable{

    /**
     * Lista criterii sortare
     */
    private List<SortCriteria> criterias;

    public List<SortCriteria> getCriterias() {
        if( criterias == null){
            criterias = new LinkedList<SortCriteria>();
        }
        return criterias;
    }

    public void setCriterias(List<SortCriteria> criterias) {
        this.criterias = criterias;
    }
}
