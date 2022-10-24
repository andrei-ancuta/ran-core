package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the PLANTATIE database table.
 */
@Entity
@Table(name = "PLANTATIE")
@NamedQuery(name = "Plantatie.findAll", query = "SELECT p FROM Plantatie p")
public class Plantatie implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PLANTATIE_IDPLANTATIE_GENERATOR", sequenceName = "SEQ_PLANTATIE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLANTATIE_IDPLANTATIE_GENERATOR")
    @Column(name = "ID_PLANTATIE")
    private Long idPlantatie;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    @Column(name = "NR_POM_ROD")
    private Integer nrPomRod;

    private Integer suprafata;

    //bi-directional many-to-one association to CapPlantatie
    @ManyToOne
    @JoinColumn(name = "FK_CAP_PLANTATIE")
    private CapPlantatie capPlantatie;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    //bi-directional many-to-one association to GometriePlantatie
    @OneToMany(mappedBy = "plantatie",cascade = CascadeType.ALL)
    private List<GeometriePlantatie> geometriePlantatii  = new ArrayList<>();

    public Plantatie() {
    }

    public Long getIdPlantatie() {
        return this.idPlantatie;
    }

    public void setIdPlantatie(Long idPlantatie) {
        this.idPlantatie = idPlantatie;
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


    public Integer getNrPomRod() {
        return this.nrPomRod;
    }

    public void setNrPomRod(Integer nrPomRod) {
        this.nrPomRod = nrPomRod;
    }

    public Integer getSuprafata() {
        return this.suprafata;
    }

    public void setSuprafata(Integer suprafata) {
        this.suprafata = suprafata;
    }

    public CapPlantatie getCapPlantatie() {
        return this.capPlantatie;
    }

    public void setCapPlantatie(CapPlantatie capPlantatie) {
        this.capPlantatie = capPlantatie;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

    public List<GeometriePlantatie> getGeometriePlantatii() {
        return geometriePlantatii;
    }

    public void setGeometriePlantatii(List<GeometriePlantatie> geometriePlantatii) {
        this.geometriePlantatii = geometriePlantatii;
    }
}