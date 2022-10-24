package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the FRUCT_PROD database table.
 */
@Entity
@Table(name = "FRUCT_PROD")
@NamedQuery(name = "FructProd.findAll", query = "SELECT f FROM FructProd f")
public class FructProd implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "FRUCT_PROD_IDFRUCTPROD_GENERATOR", sequenceName = "SEQ_FRUCT_PROD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FRUCT_PROD_IDFRUCTPROD_GENERATOR")
    @Column(name = "ID_FRUCT_PROD")
    private Long idFructProd;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    @Column(name = "NR_POM_RAZLET_ROD")
    private Integer nrPomRazletRod;

    @Column(name = "PROD_MEDIE_LIVADA")
    private Integer prodMedieLivada;

    @Column(name = "PROD_MEDIE_POM_RAZLET_ROD")
    private Integer prodMediePomRazletRod;

    @Column(name = "PROD_TOTAL_LIVADA")
    private Integer prodTotalLivada;

    @Column(name = "PROD_TOTAL_POM_RAZLET_ROD")
    private Integer prodTotalPomRazletRod;

    @Column(name = "SUPRAFATA_LIVADA")
    private Integer suprafataLivada;

    //bi-directional many-to-one association to CapFructProd
    @ManyToOne
    @JoinColumn(name = "FK_CAP_FRUCT_PROD")
    private CapFructProd capFructProd;

    //bi-directional many-to-one association to NomUat
    @ManyToOne
    @JoinColumn(name = "FK_NOM_UAT")
    private NomUat nomUat;

    public FructProd() {
    }

    public Long getIdFructProd() {
        return this.idFructProd;
    }

    public void setIdFructProd(Long idFructProd) {
        this.idFructProd = idFructProd;
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


    public Integer getNrPomRazletRod() {
        return this.nrPomRazletRod;
    }

    public void setNrPomRazletRod(Integer nrPomRazletRod) {
        this.nrPomRazletRod = nrPomRazletRod;
    }

    public Integer getProdMedieLivada() {
        return this.prodMedieLivada;
    }

    public void setProdMedieLivada(Integer prodMedieLivada) {
        this.prodMedieLivada = prodMedieLivada;
    }

    public Integer getProdMediePomRazletRod() {
        return this.prodMediePomRazletRod;
    }

    public void setProdMediePomRazletRod(Integer prodMediePomRazletRod) {
        this.prodMediePomRazletRod = prodMediePomRazletRod;
    }

    public Integer getProdTotalLivada() {
        return this.prodTotalLivada;
    }

    public void setProdTotalLivada(Integer prodTotalLivada) {
        this.prodTotalLivada = prodTotalLivada;
    }

    public Integer getProdTotalPomRazletRod() {
        return this.prodTotalPomRazletRod;
    }

    public void setProdTotalPomRazletRod(Integer prodTotalPomRazletRod) {
        this.prodTotalPomRazletRod = prodTotalPomRazletRod;
    }

    public Integer getSuprafataLivada() {
        return this.suprafataLivada;
    }

    public void setSuprafataLivada(Integer suprafataLivada) {
        this.suprafataLivada = suprafataLivada;
    }

    public CapFructProd getCapFructProd() {
        return this.capFructProd;
    }

    public void setCapFructProd(CapFructProd capFructProd) {
        this.capFructProd = capFructProd;
    }

    public NomUat getNomUat() {
        return this.nomUat;
    }

    public void setNomUat(NomUat nomUat) {
        this.nomUat = nomUat;
    }

}