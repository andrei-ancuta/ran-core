package ro.uti.ran.core.model;

import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-02 15:34
 */
public interface HasId<ID extends Serializable> {

    String ID = "id";

    ID getId();

    void setId(ID id);
}
