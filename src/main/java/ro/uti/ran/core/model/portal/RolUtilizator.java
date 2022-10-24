package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.Model;

import javax.persistence.*;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 15:18
 */
@Entity
@Table(name = "APP_ROL_UTILIZATOR")
public class RolUtilizator extends Model{

    @Id
    @GeneratedValue(generator = "RolUtilizatorSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "RolUtilizatorSeq", sequenceName = "SEQ_APP_ROL_UTILIZATOR", allocationSize = 1)
    @Column(name = "ID_APP_ROL_UTILIZATOR", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="FK_APP_ROL")
    private Rol rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_APP_UTILIZATOR")
    private Utilizator utilizator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="FK_NOM_INSTITUTIE")
    private Institutie institutie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="FK_NOM_UAT")
    private UAT uat;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="FK_NOM_JUDET")
    private Judet judet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Utilizator getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
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
