package ro.uti.ran.core.ws.internal;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-05 22:38
 */
public class RanAuthorization implements Serializable {

    /**
     * Identificator entitate.
     */
    private Long idEntity;

    /**
     * Context entitate
     */
    private String context;


    /**
     * Daca apelul este se face din RAL-ul central (UAT Introducere)
     */
    private Boolean local;

    /**
     * Daca apelul WS-ul extern se face cu autorizarea unui UAT-local
     */
    private Boolean isUATLocal;


    @XmlElement(required = true)
    public Long getIdEntity() {
        return idEntity;
    }

    public void setIdEntity(Long idEntity) {
        this.idEntity = idEntity;
    }

    @XmlElement(required = true)
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Boolean getLocal() {
        return local;
    }

    public void setLocal(Boolean local) {
        this.local = local;
    }

    public Boolean getUATLocal() {
        return isUATLocal;
    }

    public void setUATLocal(Boolean UATLocal) {
        isUATLocal = UATLocal;
    }
}
