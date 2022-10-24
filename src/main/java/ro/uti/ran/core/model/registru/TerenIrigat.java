package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the TEREN_IRIGAT database table.
 */
@Entity
@Table(name = "TEREN_IRIGAT")
@NamedQuery(name = "TerenIrigat.findAll", query = "SELECT t FROM TerenIrigat t")
public class TerenIrigat implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TEREN_IRIGAT_IDTERENIRIGAT_GENERATOR", sequenceName = "SEQ_TEREN_IRIGAT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEREN_IRIGAT_IDTERENIRIGAT_GENERATOR")
    @Column(name = "ID_TEREN_IRIGAT")
    private Long idTerenIrigat;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    private Integer suprafata;

    //bi-directional many-to-one association to CapTerenIrigat
    @ManyToOne
    @JoinColumn(name = "FK_CAP_TEREN_IRIGAT")
    private CapTerenIrigat capTerenIrigat;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    //bi-directional many-to-one association to GometrieAplicareIngras
    @OneToMany(mappedBy = "terenIrigat",cascade = CascadeType.ALL)
    private List<GeometrieTerenIrigat> geometrieTerenIrigats = new ArrayList<>();

    public TerenIrigat() {
    }

    public Long getIdTerenIrigat() {
        return this.idTerenIrigat;
    }

    public void setIdTerenIrigat(Long idTerenIrigat) {
        this.idTerenIrigat = idTerenIrigat;
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


    public Integer getSuprafata() {
        return this.suprafata;
    }

    public void setSuprafata(Integer suprafata) {
        this.suprafata = suprafata;
    }

    public CapTerenIrigat getCapTerenIrigat() {
        return this.capTerenIrigat;
    }

    public void setCapTerenIrigat(CapTerenIrigat capTerenIrigat) {
        this.capTerenIrigat = capTerenIrigat;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

    public List<GeometrieTerenIrigat> getGeometrieTerenIrigats() {
        return geometrieTerenIrigats;
    }

    public void setGeometrieTerenIrigats(List<GeometrieTerenIrigat> geometrieTerenIrigats) {
        this.geometrieTerenIrigats = geometrieTerenIrigats;
    }
}