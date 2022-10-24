package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the SUPRAFATA_CATEGORIE database table.
 */
@Entity
@Table(name = "SUPRAFATA_CATEGORIE")
@NamedQuery(name = "SuprafataCategorie.findAll", query = "SELECT s FROM SuprafataCategorie s")
public class SuprafataCategorie implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SUPRAFATA_CATEGORIE_IDSUPRAFATACATEGORIE_GENERATOR", sequenceName = "SEQ_SUPRAFATA_CATEGORIE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUPRAFATA_CATEGORIE_IDSUPRAFATACATEGORIE_GENERATOR")
    @Column(name = "ID_SUPRAFATA_CATEGORIE")
    private Long idSuprafataCategorie;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    @Column(name = "SUPRAFATA_ALT")
    private Integer suprafataAlt;

    @Column(name = "SUPRAFATA_LOCAL")
    private Integer suprafataLocal;

    @Column(name = "SUPRAFATA_TOTAL")
    private Integer suprafataTotal;

    //bi-directional many-to-one association to CapCategorieFolosinta
    @ManyToOne
    @JoinColumn(name = "FK_CAP_CATEGORIE_FOLOSINTA")
    private CapCategorieFolosinta capCategorieFolosinta;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    public SuprafataCategorie() {
    }

    public Long getIdSuprafataCategorie() {
        return this.idSuprafataCategorie;
    }

    public void setIdSuprafataCategorie(Long idSuprafataCategorie) {
        this.idSuprafataCategorie = idSuprafataCategorie;
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


    public Integer getSuprafataAlt() {
        return this.suprafataAlt;
    }

    public void setSuprafataAlt(Integer suprafataAlt) {
        this.suprafataAlt = suprafataAlt;
    }

    public Integer getSuprafataLocal() {
        return this.suprafataLocal;
    }

    public void setSuprafataLocal(Integer suprafataLocal) {
        this.suprafataLocal = suprafataLocal;
    }

    public Integer getSuprafataTotal() {
        return this.suprafataTotal;
    }

    public void setSuprafataTotal(Integer suprafataTotal) {
        this.suprafataTotal = suprafataTotal;
    }

    public CapCategorieFolosinta getCapCategorieFolosinta() {
        return this.capCategorieFolosinta;
    }

    public void setCapCategorieFolosinta(CapCategorieFolosinta capCategorieFolosinta) {
        this.capCategorieFolosinta = capCategorieFolosinta;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

}