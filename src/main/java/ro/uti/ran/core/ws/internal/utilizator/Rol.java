package ro.uti.ran.core.ws.internal.utilizator;

import ro.uti.ran.core.model.portal.Institutie;
import ro.uti.ran.core.model.portal.Judet;
import ro.uti.ran.core.model.portal.UAT;

import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-22 10:29
 */
public class Rol implements Serializable {

    private String cod;

    private String denumire;

    private String descriere;


    private Institutie institutie;

    private UAT uat;

    private Judet judet;


    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Institutie getInstitutie() {
        return institutie;
    }

    public void setInstitutie(Institutie institutie) {
        this.institutie = institutie;
    }

    public UAT getUat() {
        return uat;
    }

    public void setUat(UAT uat) {
        this.uat = uat;
    }

    public Judet getJudet() {
        return judet;
    }

    public void setJudet(Judet judet) {
        this.judet = judet;
    }
}
