package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 13:38
 */
@Entity
@Table(name = "APP_UTILIZATOR")
public class UtilizatorSin implements Serializable {
    @Id
    @GeneratedValue(generator = "UtilizatorSinSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "UtilizatorSinSeq", sequenceName = "SEQ_APP_UTILIZATOR", allocationSize = 1)
    @Column(name = "ID_APP_UTILIZATOR", updatable = false)
    private Long id;

    @Column(name = "NUME_UTILIZATOR", length = 50, nullable = false)
    private String numeUtilizator;

    @Column(name = "NUME", length = 50, nullable = true)
    private String nume;

    @Column(name = "PRENUME", length = 50, nullable = true)
    private String prenume;

    @Column(name = "EMAIL", length = 50, nullable = true)
    private String email;

    @Column(name = "CNP", length = 13, nullable = true)
    private String cnp;

    @Column(name = "OBSERVATII", length = 500, nullable = true)
    private String observatii;

    @Column(name = "IS_ACTIV", nullable = false)
    private Boolean activ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getObservatii() {
        return observatii;
    }

    public void setObservatii(String observatii) {
        this.observatii = observatii;
    }

    public Boolean getActiv() {
        return activ;
    }

    public void setActiv(Boolean activ) {
        this.activ = activ;
    }
}
