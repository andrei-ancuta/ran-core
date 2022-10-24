package ro.uti.ran.core.repository.criteria;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-28 13:50
 */
public class OrRepositoryCriteria extends CollectionRepositoryCriteria {

    public OrRepositoryCriteria(RepositoryCriteria... criterias) {
        super(criterias);
    }
}
