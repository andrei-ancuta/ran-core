package ro.uti.ran.core.ws.internal.utilizator;

import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-23 14:16
 */
public class RolUtilizatorSearchFilter implements Serializable {

    /**
     *
     */
    private Long idInstitutie;

    /**
     *
     */
    private Long idUAT;

    /**
     *
     */
    private String codComponenta;


    public Long getIdInstitutie() {
        return idInstitutie;
    }

    public void setIdInstitutie(Long idInstitutie) {
        this.idInstitutie = idInstitutie;
    }

    public Long getIdUAT() {
        return idUAT;
    }

    public void setIdUAT(Long idUAT) {
        this.idUAT = idUAT;
    }

    public String getCodComponenta() {
        return codComponenta;
    }

    public void setCodComponenta(String codComponenta) {
        this.codComponenta = codComponenta;
    }
}
