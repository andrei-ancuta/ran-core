package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the SUBSTANTA_CHIMICA database table.
 */
@Entity
@Table(name = "SUBSTANTA_CHIMICA")
@NamedQuery(name = "SubstantaChimica.findAll", query = "SELECT s FROM SubstantaChimica s")
public class SubstantaChimica implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SUBSTANTA_CHIMICA_IDSUBSTANTACHIMICA_GENERATOR", sequenceName = "SEQ_SUBSTANTA_CHIMICA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBSTANTA_CHIMICA_IDSUBSTANTACHIMICA_GENERATOR")
    @Column(name = "ID_SUBSTANTA_CHIMICA")
    private Long idSubstantaChimica;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "CANTITATE_AZOTOASE")
    private Integer cantitateAzotoase;

    @Column(name = "CANTITATE_FOSFATICE")
    private Integer cantitateFosfatice;

    @Column(name = "CANTITATE_POTASICE")
    private Integer cantitatePotasice;

    @Column(name = "CANTITATE_TOTAL")
    private Integer cantitateTotal;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    @Column(name = "SUPRAFATA_AZOTOASE")
    private Integer suprafataAzotoase;

    @Column(name = "SUPRAFATA_FOSFATICE")
    private Integer suprafataFosfatice;

    @Column(name = "SUPRAFATA_POTASICE")
    private Integer suprafataPotasice;

    @Column(name = "SUPRAFATA_TOTAL")
    private Integer suprafataTotal;

    //bi-directional many-to-one association to CapSubstantaChimica
    @ManyToOne
    @JoinColumn(name = "FK_CAP_SUBSTANTA_CHIMICA")
    private CapSubstantaChimica capSubstantaChimica;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    public SubstantaChimica() {
    }

    public Long getIdSubstantaChimica() {
        return this.idSubstantaChimica;
    }

    public void setIdSubstantaChimica(Long idSubstantaChimica) {
        this.idSubstantaChimica = idSubstantaChimica;
    }

    public Integer getAn() {
        return this.an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public Integer getCantitateAzotoase() {
        return this.cantitateAzotoase;
    }

    public void setCantitateAzotoase(Integer cantitateAzotoase) {
        this.cantitateAzotoase = cantitateAzotoase;
    }

    public Integer getCantitateFosfatice() {
        return this.cantitateFosfatice;
    }

    public void setCantitateFosfatice(Integer cantitateFosfatice) {
        this.cantitateFosfatice = cantitateFosfatice;
    }

    public Integer getCantitatePotasice() {
        return this.cantitatePotasice;
    }

    public void setCantitatePotasice(Integer cantitatePotasice) {
        this.cantitatePotasice = cantitatePotasice;
    }

    public Integer getCantitateTotal() {
        return this.cantitateTotal;
    }

    public void setCantitateTotal(Integer cantitateTotal) {
        this.cantitateTotal = cantitateTotal;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }


    public Integer getSuprafataAzotoase() {
        return this.suprafataAzotoase;
    }

    public void setSuprafataAzotoase(Integer suprafataAzotoase) {
        this.suprafataAzotoase = suprafataAzotoase;
    }

    public Integer getSuprafataFosfatice() {
        return this.suprafataFosfatice;
    }

    public void setSuprafataFosfatice(Integer suprafataFosfatice) {
        this.suprafataFosfatice = suprafataFosfatice;
    }

    public Integer getSuprafataPotasice() {
        return this.suprafataPotasice;
    }

    public void setSuprafataPotasice(Integer suprafataPotasice) {
        this.suprafataPotasice = suprafataPotasice;
    }

    public Integer getSuprafataTotal() {
        return this.suprafataTotal;
    }

    public void setSuprafataTotal(Integer suprafataTotal) {
        this.suprafataTotal = suprafataTotal;
    }

    public CapSubstantaChimica getCapSubstantaChimica() {
        return this.capSubstantaChimica;
    }

    public void setCapSubstantaChimica(CapSubstantaChimica capSubstantaChimica) {
        this.capSubstantaChimica = capSubstantaChimica;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

}