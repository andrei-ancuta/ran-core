package ro.uti.ran.core.utils;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-27 15:22
 */
public class SortInfoHelper {

    public static SortInfo buildSortInfo(HasCriterias<? extends ISortCriteria> hasCriterias){
        if( hasCriterias == null || hasCriterias.getCriterias() == null || hasCriterias.getCriterias().size() == 0) return null;

        SortInfo sortInfo = new SortInfo();

        for(ISortCriteria sortCriteria : hasCriterias.getCriterias()){
            sortInfo.getCriterias().add(new SortCriteria(
                            sortCriteria.getPath(),
                            sortCriteria.getOrder())
            );
        }
        return sortInfo;
    }
}
