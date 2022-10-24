package ro.uti.ran.core.ws.internal.incarcare;

import ro.uti.ran.core.model.portal.StareIncarcare;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.model.portal.Utilizator;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-27 16:12
 */
public class DetaliiIncarcare implements Serializable {

    private Long id;

    private Utilizator utilizator;

    private UAT uat;

    private StareIncarcare stareIncarcare;

    private String indexIncarcare;

    private Date dataIncarcare;

    private String denumireFisier;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilizator getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    public UAT getUat() {
        return uat;
    }

    public void setUat(UAT uat) {
        this.uat = uat;
    }

    public StareIncarcare getStareIncarcare() {
        return stareIncarcare;
    }

    public void setStareIncarcare(StareIncarcare stareIncarcare) {
        this.stareIncarcare = stareIncarcare;
    }

    public String getIndexIncarcare() {
        return indexIncarcare;
    }

    public void setIndexIncarcare(String indexIncarcare) {
        this.indexIncarcare = indexIncarcare;
    }

    public Date getDataIncarcare() {
        return dataIncarcare;
    }

    public void setDataIncarcare(Date dataIncarcare) {
        this.dataIncarcare = dataIncarcare;
    }

    public String getDenumireFisier() {
        return denumireFisier;
    }

    public void setDenumireFisier(String denumireFisier) {
        this.denumireFisier = denumireFisier;
    }
}
