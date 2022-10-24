package ro.uti.ran.core.utils;

import java.io.Serializable;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-18 14:14
 */
public interface HasCriterias<T> extends Serializable {

    void setCriterias(List<T> criterias);

    List<T> getCriterias();
}
