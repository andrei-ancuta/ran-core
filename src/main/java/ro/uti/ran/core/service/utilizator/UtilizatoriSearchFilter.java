package ro.uti.ran.core.service.utilizator;

import ro.uti.ran.core.utils.SearchFilter;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-16 09:52
 */
public class UtilizatoriSearchFilter extends SearchFilter {
    private String numeUtilizator;
    private String nume;
    private String prenume;
    private String email;
    private String cnp;
    private String nif;

    /**
     *  Filtrare utilizatori pentru UAT dat
     */
    private List<Long> idUat;

    /**
     * Filtrare utilizatori pentru JUDET dat si toate UAT din JUDET dat
     */
    private List<Long> idJudet;

    /**
     * Filtrare utilzitori pentru INSTITUTIE data
     */
    private List<Long> idInstitutie;

    /**
     * Filtrare utilizator care au asignate aceste contexte
     */
    private List<Long> idContext;

    /**
     * Filtrare utilizatori care au asignare aceste roluri
     */
    private List<Long> idRol;


    public List<Long> getIdInstitutie() {
        return idInstitutie;
    }

    public void setIdInstitutie(List<Long> idInstitutie) {
        this.idInstitutie = idInstitutie;
    }

    public String getNumeUtilizator() {
        return numeUtilizator;
    }

    public void setNumeUtilizator(String numeUtilizator) {
        this.numeUtilizator = numeUtilizator;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public List<Long> getIdUat() {
        return idUat;
    }

    public void setIdUat(List<Long> idUat) {
        this.idUat = idUat;
    }

    public List<Long> getIdJudet() {
        return idJudet;
    }

    public void setIdJudet(List<Long> idJudet) {
        this.idJudet = idJudet;
    }

    public List<Long> getIdContext() {
        return idContext;
    }

    public void setIdContext(List<Long> idContext) {
        this.idContext = idContext;
    }

    public List<Long> getIdRol() {
        return idRol;
    }

    public void setIdRol(List<Long> idRol) {
        this.idRol = idRol;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }
}
