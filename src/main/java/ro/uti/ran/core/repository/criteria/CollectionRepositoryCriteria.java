package ro.uti.ran.core.repository.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-28 13:50
 */
public abstract class CollectionRepositoryCriteria extends RepositoryCriteria {

    private List<RepositoryCriteria> criterias;

    public CollectionRepositoryCriteria(RepositoryCriteria... criterias) {
        super(null, null, null);
        this.criterias = new LinkedList<RepositoryCriteria>( Arrays.asList(criterias));
    }

    public List<RepositoryCriteria> getCriterias() {
        if( criterias == null){
            criterias = new LinkedList<RepositoryCriteria>();
        }
        return criterias;
    }

    public CollectionRepositoryCriteria add(RepositoryCriteria criteria){
        if( criteria != null) {
            getCriterias().add(criteria);
        }

        return this;
    }
}
