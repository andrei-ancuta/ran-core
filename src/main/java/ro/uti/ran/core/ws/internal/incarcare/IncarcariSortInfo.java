package ro.uti.ran.core.ws.internal.incarcare;

import ro.uti.ran.core.utils.HasCriterias;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-26 17:59
 */
public class IncarcariSortInfo implements HasCriterias<IncarcariSortCriteria> {

    private List<IncarcariSortCriteria> criterias;

    public List<IncarcariSortCriteria> getCriterias() {
        if( criterias == null){
            criterias = new LinkedList<IncarcariSortCriteria>();
        }
        return criterias;
    }

    public void setCriterias(List<IncarcariSortCriteria> criterias) {
        this.criterias = criterias;
    }
}
