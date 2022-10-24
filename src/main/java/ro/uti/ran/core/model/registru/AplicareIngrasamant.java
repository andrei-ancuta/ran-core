package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the APLICARE_INGRASAMANT database table.
 */
@Entity
@Table(name = "APLICARE_INGRASAMANT")
@NamedQuery(name = "AplicareIngrasamant.findAll", query = "SELECT a FROM AplicareIngrasamant a")
public class AplicareIngrasamant implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "APLICARE_INGRASAMANT_IDAPLICARE_INGRASAMANT_GENERATOR", sequenceName = "SEQ_APLICARE_INGRASAMANT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APLICARE_INGRASAMANT_IDAPLICARE_INGRASAMANT_GENERATOR")
    @Column(name = "ID_APLICARE_INGRASAMANT")
    private Long idAplicareIngrasamant;

    @Column(name = "\"AN\"")
    private Integer an;

    private Integer cantitate;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    private Integer suprafata;

    //bi-directional many-to-one association to CapAplicareIngrasamant
    @ManyToOne
    @JoinColumn(name = "FK_CAP_APLICARE_INGRASAMANT")
    private CapAplicareIngrasamant capAplicareIngrasamant;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    //bi-directional many-to-one association to GometrieAplicareIngras
    @OneToMany(mappedBy = "aplicareIngrasamant",cascade = CascadeType.ALL)
    private List<GeometrieAplicareIngras> geometrieAplicareIngrases = new ArrayList<>();

    public AplicareIngrasamant() {
    }

    public Long getIdAplicareIngrasamant() {
        return this.idAplicareIngrasamant;
    }

    public void setIdAplicareIngrasamant(Long idAplicareIngrasamant) {
        this.idAplicareIngrasamant = idAplicareIngrasamant;
    }

    public Integer getAn() {
        return this.an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public Integer getCantitate() {
        return this.cantitate;
    }

    public void setCantitate(Integer cantitate) {
        this.cantitate = cantitate;
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

    public CapAplicareIngrasamant getCapAplicareIngrasamant() {
        return this.capAplicareIngrasamant;
    }

    public void setCapAplicareIngrasamant(CapAplicareIngrasamant capAplicareIngrasamant) {
        this.capAplicareIngrasamant = capAplicareIngrasamant;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

    public List<GeometrieAplicareIngras> getGeometrieAplicareIngrases() {
        return geometrieAplicareIngrases;
    }

    public void setGeometrieAplicareIngrases(List<GeometrieAplicareIngras> geometrieAplicareIngrases) {
        this.geometrieAplicareIngrases = geometrieAplicareIngrases;
    }
}