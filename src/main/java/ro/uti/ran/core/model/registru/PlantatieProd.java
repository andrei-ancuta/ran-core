package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the PLANTATIE_PROD database table.
 */
@Entity
@Table(name = "PLANTATIE_PROD")
@NamedQuery(name = "PlantatieProd.findAll", query = "SELECT p FROM PlantatieProd p")
public class PlantatieProd implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PLANTATIE_PROD_IDPLANTATIE_PROD_GENERATOR", sequenceName = "SEQ_PLANTATIE_PROD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLANTATIE_PROD_IDPLANTATIE_PROD_GENERATOR")
    @Column(name = "ID_PLANTATIE_PROD")
    private Long idPlantatieProd;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    @Column(name = "PROD_MEDIE")
    private Integer prodMedie;

    @Column(name = "PROD_TOTAL")
    private Integer prodTotal;

    private Integer suprafata;

    //bi-directional many-to-one association to CapPlantatieProd
    @ManyToOne
    @JoinColumn(name = "FK_CAP_PLANTATIE_PROD")
    private CapPlantatieProd capPlantatieProd;

    //bi-directional many-to-one association to NomUat
    @ManyToOne
    @JoinColumn(name = "FK_NOM_UAT")
    private NomUat nomUat;

    public PlantatieProd() {
    }

    public Long getIdPlantatieProd() {
        return this.idPlantatieProd;
    }

    public void setIdPlantatieProd(Long idPlantatieProd) {
        this.idPlantatieProd = idPlantatieProd;
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


    public Integer getProdMedie() {
        return this.prodMedie;
    }

    public void setProdMedie(Integer prodMedie) {
        this.prodMedie = prodMedie;
    }

    public Integer getProdTotal() {
        return this.prodTotal;
    }

    public void setProdTotal(Integer prodTotal) {
        this.prodTotal = prodTotal;
    }

    public Integer getSuprafata() {
        return this.suprafata;
    }

    public void setSuprafata(Integer suprafata) {
        this.suprafata = suprafata;
    }

    public CapPlantatieProd getCapPlantatieProd() {
        return this.capPlantatieProd;
    }

    public void setCapPlantatieProd(CapPlantatieProd capPlantatieProd) {
        this.capPlantatieProd = capPlantatieProd;
    }

    public NomUat getNomUat() {
        return this.nomUat;
    }

    public void setNomUat(NomUat nomUat) {
        this.nomUat = nomUat;
    }

}