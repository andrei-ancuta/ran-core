package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the CONTRACT database table.
 */
@Entity
@NamedQuery(name = "Contract.findAll", query = "SELECT c FROM Contract c")
public class Contract implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CONTRACT_IDCONTRACT_GENERATOR", sequenceName = "SEQ_CONTRACT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTRACT_IDCONTRACT_GENERATOR")
    @Column(name = "ID_CONTRACT")
    private Long idContract;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_CONTRACT")
    private Date dataContract;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_START")
    private Date dataStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_STOP")
    private Date dataStop;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    @Column(name = "NR_CONTRACT")
    private String nrContract;

    @Column(name = "NR_CRT")
    private Integer nrCrt;

    private BigDecimal redeventa;

    @Column(name = "REDEVENTA_COMPLETA")
    private String redeventaCompleta;

    private Integer suprafata;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    //bi-directional many-to-one association to NomCategorieFolosinta
    @ManyToOne
    @JoinColumn(name = "FK_NOM_CATEGORIE_FOLOSINTA")
    private NomCategorieFolosinta nomCategorieFolosinta;

    //bi-directional many-to-one association to NomTipContract
    @ManyToOne
    @JoinColumn(name = "FK_NOM_TIP_CONTRACT")
    private NomTipContract nomTipContract;

    //bi-directional many-to-one association to PersoanaFizica
    @ManyToOne
    @JoinColumn(name = "FK_PERSOANA_FIZICA")
    private PersoanaFizica persoanaFizica;

    //bi-directional many-to-one association to PersoanaRc
    @ManyToOne
    @JoinColumn(name = "FK_PERSOANA_RC")
    private PersoanaRc persoanaRc;

    public Contract() {
    }

    public Long getIdContract() {
        return this.idContract;
    }

    public void setIdContract(Long idContract) {
        this.idContract = idContract;
    }

    public Date getDataContract() {
        return this.dataContract;
    }

    public void setDataContract(Date dataContract) {
        this.dataContract = dataContract;
    }

    public Date getDataStart() {
        return this.dataStart;
    }

    public void setDataStart(Date dataStart) {
        this.dataStart = dataStart;
    }

    public Date getDataStop() {
        return this.dataStop;
    }

    public void setDataStop(Date dataStop) {
        this.dataStop = dataStop;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }


    public String getNrContract() {
        return this.nrContract;
    }

    public void setNrContract(String nrContract) {
        this.nrContract = nrContract;
    }

    public Integer getNrCrt() {
        return this.nrCrt;
    }

    public void setNrCrt(Integer nrCrt) {
        this.nrCrt = nrCrt;
    }

    public BigDecimal getRedeventa() {
        return this.redeventa;
    }

    public void setRedeventa(BigDecimal redeventa) {
        this.redeventa = redeventa;
    }

    public String getRedeventaCompleta() {
        return this.redeventaCompleta;
    }

    public void setRedeventaCompleta(String redeventaCompleta) {
        this.redeventaCompleta = redeventaCompleta;
    }

    public Integer getSuprafata() {
        return this.suprafata;
    }

    public void setSuprafata(Integer suprafata) {
        this.suprafata = suprafata;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

    public NomCategorieFolosinta getNomCategorieFolosinta() {
        return this.nomCategorieFolosinta;
    }

    public void setNomCategorieFolosinta(NomCategorieFolosinta nomCategorieFolosinta) {
        this.nomCategorieFolosinta = nomCategorieFolosinta;
    }

    public NomTipContract getNomTipContract() {
        return this.nomTipContract;
    }

    public void setNomTipContract(NomTipContract nomTipContract) {
        this.nomTipContract = nomTipContract;
    }

    public PersoanaFizica getPersoanaFizica() {
        return this.persoanaFizica;
    }

    public void setPersoanaFizica(PersoanaFizica persoanaFizica) {
        this.persoanaFizica = persoanaFizica;
    }

    public PersoanaRc getPersoanaRc() {
        return this.persoanaRc;
    }

    public void setPersoanaRc(PersoanaRc persoanaRc) {
        this.persoanaRc = persoanaRc;
    }

}