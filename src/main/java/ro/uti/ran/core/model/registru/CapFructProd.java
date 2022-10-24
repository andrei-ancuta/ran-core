package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CAP_FRUCT_PROD database table.
 */
@Entity
@Table(name = "CAP_FRUCT_PROD")
@NamedQuery(name = "CapFructProd.findAll", query = "SELECT c FROM CapFructProd c")
public class CapFructProd extends Nomenclator implements Versioned, Editable, NomProductieNivelUat {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CAP_FRUCT_PROD_IDCAP_FRUCT_PROD_GENERATOR", sequenceName = "SEQ_CAP_FRUCT_PROD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAP_FRUCT_PROD_IDCAP_FRUCT_PROD_GENERATOR")
    @Column(name = "ID_CAP_FRUCT_PROD")
    private Long id;

    @Column(name = "BASE_ID")
    private Long baseId;

    private String cod;

    @Column(name = "COD_RAND")
    private Integer codRand;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_START")
    private Date dataStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_STOP")
    private Date dataStop;

    private String denumire;

    private String descriere;

    @Column(name = "IS_FORMULA")
    private Integer isFormula;

    @Column(name = "IS_NR_POM_RAZLET")
    private Integer isNrPomRazlet;

    @Column(name = "IS_PROD_MEDIE_LIVADA")
    private Integer isProdMedieLivada;

    @Column(name = "IS_PROD_MEDIE_POM_RAZLET")
    private Integer isProdMediePomRazlet;

    @Column(name = "IS_PROD_TOTAL_LIVADA")
    private Integer isProdTotalLivada;

    @Column(name = "IS_PROD_TOTAL_POM_RAZLET")
    private Integer isProdTotalPomRazlet;

    @Column(name = "IS_SUPRAFATA_LIVADA")
    private Integer isSuprafataLivada;


    @Column(name = "TIP_FORMULA_RELATIE")
    private Integer tipFormulaRelatie;

    //bi-directional many-to-one association to CapFructProd
    @ManyToOne
    @JoinColumn(name = "FK_CAP_FRUCT_PROD")
    private CapFructProd capFructProd;

    //bi-directional many-to-one association to CapFructProd
    @OneToMany(mappedBy = "capFructProd")
    private List<CapFructProd> capFructProds;

    //bi-directional many-to-one association to NomCapitol
    @ManyToOne
    @JoinColumn(name = "FK_NOM_CAPITOL")
    private NomCapitol nomCapitol;

    //bi-directional many-to-one association to NomPomArbust
    @ManyToOne
    @JoinColumn(name = "FK_NOM_POM_ARBUST")
    private NomPomArbust nomPomArbust;

    @Transient
    private boolean isLatestVersion;

    public CapFructProd() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long idCapFructProd) {
        this.id = idCapFructProd;
    }

    public Long getBaseId() {
        return this.baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

    public String getCod() {
        return this.cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Integer getCodRand() {
        return this.codRand;
    }

    public void setCodRand(Integer codRand) {
        this.codRand = codRand;
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

    public String getDenumire() {
        return this.denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getDescriere() {
        return this.descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Integer getIsFormula() {
        return this.isFormula;
    }

    public void setIsFormula(Integer isFormula) {
        this.isFormula = isFormula;
    }

    public Integer getIsNrPomRazlet() {
        return this.isNrPomRazlet;
    }

    public void setIsNrPomRazlet(Integer isNrPomRazlet) {
        this.isNrPomRazlet = isNrPomRazlet;
    }

    public Integer getIsProdMedieLivada() {
        return this.isProdMedieLivada;
    }

    public void setIsProdMedieLivada(Integer isProdMedieLivada) {
        this.isProdMedieLivada = isProdMedieLivada;
    }

    public Integer getIsProdMediePomRazlet() {
        return this.isProdMediePomRazlet;
    }

    public void setIsProdMediePomRazlet(Integer isProdMediePomRazlet) {
        this.isProdMediePomRazlet = isProdMediePomRazlet;
    }

    public Integer getIsProdTotalLivada() {
        return this.isProdTotalLivada;
    }

    public void setIsProdTotalLivada(Integer isProdTotalLivada) {
        this.isProdTotalLivada = isProdTotalLivada;
    }

    public Integer getIsProdTotalPomRazlet() {
        return this.isProdTotalPomRazlet;
    }

    public void setIsProdTotalPomRazlet(Integer isProdTotalPomRazlet) {
        this.isProdTotalPomRazlet = isProdTotalPomRazlet;
    }

    public Integer getIsSuprafataLivada() {
        return this.isSuprafataLivada;
    }

    public void setIsSuprafataLivada(Integer isSuprafataLivada) {
        this.isSuprafataLivada = isSuprafataLivada;
    }


    public Integer getTipFormulaRelatie() {
        return this.tipFormulaRelatie;
    }

    public void setTipFormulaRelatie(Integer tipFormulaRelatie) {
        this.tipFormulaRelatie = tipFormulaRelatie;
    }

    public CapFructProd getCapFructProd() {
        return this.capFructProd;
    }

    public void setCapFructProd(CapFructProd capFructProd) {
        this.capFructProd = capFructProd;
    }

    public List<CapFructProd> getCapFructProds() {
        return this.capFructProds;
    }

    public void setCapFructProds(List<CapFructProd> capFructProds) {
        this.capFructProds = capFructProds;
    }

    public CapFructProd addCapFructProd(CapFructProd capFructProd) {
        getCapFructProds().add(capFructProd);
        capFructProd.setCapFructProd(this);

        return capFructProd;
    }

    public CapFructProd removeCapFructProd(CapFructProd capFructProd) {
        getCapFructProds().remove(capFructProd);
        capFructProd.setCapFructProd(null);

        return capFructProd;
    }

    public NomCapitol getNomCapitol() {
        return this.nomCapitol;
    }

    public void setNomCapitol(NomCapitol nomCapitol) {
        this.nomCapitol = nomCapitol;
    }

    public NomPomArbust getNomPomArbust() {
        return this.nomPomArbust;
    }

    public void setNomPomArbust(NomPomArbust nomPomArbust) {
        this.nomPomArbust = nomPomArbust;
    }

    @Override
    public String getCodePrim() {
        return this.cod;
    }

    @Override
    public String getCodePrimName() {
        return "cod";
    }

    @Override
    public Integer getCodeSec() {
        return this.codRand;
    }

    @Override
    public String getCodeSecName() {
        return "codRand";
    }

    @Override
    public Date getStarting(){
        return this.dataStart;
    }

    @Override
    @XmlElement(name="isLatestVersion")
    public boolean isLatestVersion() {
        return isLatestVersion;
    }

}