package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the SISTEM_TEHNIC database table.
 */
@Entity
@Table(name = "SISTEM_TEHNIC")
@NamedQuery(name = "SistemTehnic.findAll", query = "SELECT s FROM SistemTehnic s")
public class SistemTehnic implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SISTEM_TEHNIC_IDSISTEMTEHNIC_GENERATOR", sequenceName = "SEQ_SISTEM_TEHNIC", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SISTEM_TEHNIC_IDSISTEMTEHNIC_GENERATOR")
    @Column(name = "ID_SISTEM_TEHNIC")
    private Long idSistemTehnic;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    private Integer numar;

    //bi-directional many-to-one association to CapSistemTehnic
    @ManyToOne
    @JoinColumn(name = "FK_CAP_SISTEM_TEHNIC")
    private CapSistemTehnic capSistemTehnic;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    public SistemTehnic() {
    }

    public Long getIdSistemTehnic() {
        return this.idSistemTehnic;
    }

    public void setIdSistemTehnic(Long idSistemTehnic) {
        this.idSistemTehnic = idSistemTehnic;
    }

    public Integer getAn() {
        return this.an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }


    public Integer getNumar() {
        return this.numar;
    }

    public void setNumar(Integer numar) {
        this.numar = numar;
    }

    public CapSistemTehnic getCapSistemTehnic() {
        return this.capSistemTehnic;
    }

    public void setCapSistemTehnic(CapSistemTehnic capSistemTehnic) {
        this.capSistemTehnic = capSistemTehnic;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

}