package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the CULTURA_PROD database table.
 */
@Entity
@Table(name = "CULTURA_PROD")
@NamedQuery(name = "CulturaProd.findAll", query = "SELECT c FROM CulturaProd c")
public class CulturaProd implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CULTURA_PROD_IDCULTURAPROD_GENERATOR", sequenceName = "SEQ_CULTURA_PROD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CULTURA_PROD_IDCULTURAPROD_GENERATOR")
    @Column(name = "ID_CULTURA_PROD")
    private Long idCulturaProd;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    @Column(name = "PROD_MEDIE")
    private Integer prodMedie;

    @Column(name = "PROD_TOTAL")
    private Integer prodTotal;

    private Integer suprafata;

    //bi-directional many-to-one association to CapCulturaProd
    @ManyToOne
    @JoinColumn(name = "FK_CAP_CULTURA_PROD")
    private CapCulturaProd capCulturaProd;

    //bi-directional many-to-one association to NomTipSpatiuProt
    @ManyToOne
    @JoinColumn(name = "FK_NOM_TIP_SPATIU_PROT")
    private NomTipSpatiuProt nomTipSpatiuProt;

    //bi-directional many-to-one association to NomUat
    @ManyToOne
    @JoinColumn(name = "FK_NOM_UAT")
    private NomUat nomUat;

    public CulturaProd() {
    }

    public Long getIdCulturaProd() {
        return this.idCulturaProd;
    }

    public void setIdCulturaProd(Long idCulturaProd) {
        this.idCulturaProd = idCulturaProd;
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

    public CapCulturaProd getCapCulturaProd() {
        return this.capCulturaProd;
    }

    public void setCapCulturaProd(CapCulturaProd capCulturaProd) {
        this.capCulturaProd = capCulturaProd;
    }

    public NomTipSpatiuProt getNomTipSpatiuProt() {
        return this.nomTipSpatiuProt;
    }

    public void setNomTipSpatiuProt(NomTipSpatiuProt nomTipSpatiuProt) {
        this.nomTipSpatiuProt = nomTipSpatiuProt;
    }

    public NomUat getNomUat() {
        return this.nomUat;
    }

    public void setNomUat(NomUat nomUat) {
        this.nomUat = nomUat;
    }

}