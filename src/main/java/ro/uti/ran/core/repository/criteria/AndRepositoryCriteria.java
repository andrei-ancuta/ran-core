package ro.uti.ran.core.repository.criteria;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-28 13:50
 */
public class AndRepositoryCriteria extends CollectionRepositoryCriteria {

    public AndRepositoryCriteria(RepositoryCriteria... criterias) {
        super(criterias);
    }
}
