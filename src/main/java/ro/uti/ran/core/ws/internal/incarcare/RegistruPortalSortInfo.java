package ro.uti.ran.core.ws.internal.incarcare;

import ro.uti.ran.core.utils.HasCriterias;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-26 17:59
 */
public class RegistruPortalSortInfo implements HasCriterias<RegistruPortalSortCriteria> {

    private List<RegistruPortalSortCriteria> criterias;

    public List<RegistruPortalSortCriteria> getCriterias() {
        if( criterias == null){
            criterias = new LinkedList<RegistruPortalSortCriteria>();
        }
        return criterias;
    }

    public void setCriterias(List<RegistruPortalSortCriteria> criterias) {
        this.criterias = criterias;
    }
}
