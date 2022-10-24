package ro.uti.ran.core.ws.internal.utilizator;

import ro.uti.ran.core.model.portal.ContextSistem;

import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-15 17:05
 */
public class UtilizatorSistemSearchFilter implements Serializable{

    private String numeUtilizator;


    private Long idEntity;
    private ContextSistem context;


    public String getNumeUtilizator() {
        return numeUtilizator;
    }

    public void setNumeUtilizator(String numeUtilizator) {
        this.numeUtilizator = numeUtilizator;
    }


    public Long getIdEntity() {
        return idEntity;
    }

    public void setIdEntity(Long idEntity) {
        this.idEntity = idEntity;
    }

    public ContextSistem getContext() {
        return context;
    }

    public void setContext(ContextSistem context) {
        this.context = context;
    }
}
