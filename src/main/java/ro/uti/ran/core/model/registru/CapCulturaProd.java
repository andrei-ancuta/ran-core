package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CAP_CULTURA_PROD database table.
 */
@Entity
@Table(name = "CAP_CULTURA_PROD")
@NamedQuery(name = "CapCulturaProd.findAll", query = "SELECT c FROM CapCulturaProd c")
public class CapCulturaProd extends Nomenclator implements Versioned, Editable, NomProductieNivelUat{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CAP_CULTURA_PROD_IDCAP_CULTURA_PROD_GENERATOR", sequenceName = "SEQ_CAP_CULTURA_PROD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAP_CULTURA_PROD_IDCAP_CULTURA_PROD_GENERATOR")
    @Column(name = "ID_CAP_CULTURA_PROD")
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

    @Column(name = "IS_PROD_MEDIE")
    private Integer isProdMedie;

    @Column(name = "IS_PROD_TOTAL")
    private Integer isProdTotal;

    @Column(name = "IS_SUPRAFATA")
    private Integer isSuprafata;


    @Column(name = "TIP_FORMULA_RELATIE")
    private Integer tipFormulaRelatie;

    //bi-directional many-to-one association to CapCulturaProd
    @ManyToOne
    @JoinColumn(name = "FK_CAP_CULTURA_PROD")
    private CapCulturaProd capCulturaProd;

    //bi-directional many-to-one association to CapCulturaProd
    @OneToMany(mappedBy = "capCulturaProd")
    private List<CapCulturaProd> capCulturaProds = new ArrayList<>();

    //bi-directional many-to-one association to NomCapitol
    @ManyToOne
    @JoinColumn(name = "FK_NOM_CAPITOL")
    private NomCapitol nomCapitol;

    //bi-directional many-to-one association to NomPlantaCultura
    @ManyToOne
    @JoinColumn(name = "FK_NOM_PLANTA_CULTURA")
    private NomPlantaCultura nomPlantaCultura;

    //bi-directional many-to-one association to NomUnitateMasura
    @ManyToOne
    @JoinColumn(name = "FK_NOM_UNITATE_MASURA")
    private NomUnitateMasura nomUnitateMasura;

    @Transient
    private boolean isLatestVersion;

    public CapCulturaProd() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long idCapCulturaProd) {
        this.id = idCapCulturaProd;
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

    public Integer getIsProdMedie() {
        return this.isProdMedie;
    }

    public void setIsProdMedie(Integer isProdMedie) {
        this.isProdMedie = isProdMedie;
    }

    public Integer getIsProdTotal() {
        return this.isProdTotal;
    }

    public void setIsProdTotal(Integer isProdTotal) {
        this.isProdTotal = isProdTotal;
    }

    public Integer getIsSuprafata() {
        return this.isSuprafata;
    }

    public void setIsSuprafata(Integer isSuprafata) {
        this.isSuprafata = isSuprafata;
    }


    public Integer getTipFormulaRelatie() {
        return this.tipFormulaRelatie;
    }

    public void setTipFormulaRelatie(Integer tipFormulaRelatie) {
        this.tipFormulaRelatie = tipFormulaRelatie;
    }

    public CapCulturaProd getCapCulturaProd() {
        return this.capCulturaProd;
    }

    public void setCapCulturaProd(CapCulturaProd capCulturaProd) {
        this.capCulturaProd = capCulturaProd;
    }

    public List<CapCulturaProd> getCapCulturaProds() {
        return this.capCulturaProds;
    }

    public void setCapCulturaProds(List<CapCulturaProd> capCulturaProds) {
        this.capCulturaProds = capCulturaProds;
    }

    public CapCulturaProd addCapCulturaProd(CapCulturaProd capCulturaProd) {
        getCapCulturaProds().add(capCulturaProd);
        capCulturaProd.setCapCulturaProd(this);

        return capCulturaProd;
    }

    public CapCulturaProd removeCapCulturaProd(CapCulturaProd capCulturaProd) {
        getCapCulturaProds().remove(capCulturaProd);
        capCulturaProd.setCapCulturaProd(null);

        return capCulturaProd;
    }

    public NomCapitol getNomCapitol() {
        return this.nomCapitol;
    }

    public void setNomCapitol(NomCapitol nomCapitol) {
        this.nomCapitol = nomCapitol;
    }

    public NomPlantaCultura getNomPlantaCultura() {
        return this.nomPlantaCultura;
    }

    public void setNomPlantaCultura(NomPlantaCultura nomPlantaCultura) {
        this.nomPlantaCultura = nomPlantaCultura;
    }

    public NomUnitateMasura getNomUnitateMasura() {
        return this.nomUnitateMasura;
    }

    public void setNomUnitateMasura(NomUnitateMasura nomUnitateMasura) {
        this.nomUnitateMasura = nomUnitateMasura;
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