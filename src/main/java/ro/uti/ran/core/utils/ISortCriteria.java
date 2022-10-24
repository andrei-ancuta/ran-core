package ro.uti.ran.core.utils;

import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-27 14:45
 */
public interface ISortCriteria extends Serializable {

    String getPath();

    Order getOrder();
}
