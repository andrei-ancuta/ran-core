package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CAP_CULTURA database table.
 */
@Entity
@Table(name = "CAP_CULTURA")
@NamedQuery(name = "CapCultura.findAll", query = "SELECT c FROM CapCultura c")
public class CapCultura extends Nomenclator implements Versioned, Editable, NomCapRegistrulAgricol {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CAP_CULTURA_IDCAP_CULTURA_GENERATOR", sequenceName = "SEQ_CAP_CULTURA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAP_CULTURA_IDCAP_CULTURA_GENERATOR")
    @Column(name = "ID_CAP_CULTURA")
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


    @Column(name = "TIP_FORMULA_RELATIE")
    private Integer tipFormulaRelatie;

    //bi-directional many-to-one association to CapCultura
    @ManyToOne
    @JoinColumn(name = "FK_CAP_CULTURA")
    private CapCultura capCultura;

    //bi-directional many-to-one association to CapCultura
    @OneToMany(mappedBy = "capCultura")
    private List<CapCultura> capCulturas = new ArrayList<>();

    //bi-directional many-to-one association to NomCapitol
    @ManyToOne
    @JoinColumn(name = "FK_NOM_CAPITOL")
    private NomCapitol nomCapitol;

    //bi-directional many-to-one association to NomPlantaCultura
    @ManyToOne
    @JoinColumn(name = "FK_NOM_PLANTA_CULTURA")
    private NomPlantaCultura nomPlantaCultura;

    @Transient
    private boolean isLatestVersion;

    public CapCultura() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long idCapCultura) {
        this.id = idCapCultura;
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


    public Integer getTipFormulaRelatie() {
        return this.tipFormulaRelatie;
    }

    public void setTipFormulaRelatie(Integer tipFormulaRelatie) {
        this.tipFormulaRelatie = tipFormulaRelatie;
    }

    public CapCultura getCapCultura() {
        return this.capCultura;
    }

    public void setCapCultura(CapCultura capCultura) {
        this.capCultura = capCultura;
    }

    public List<CapCultura> getCapCulturas() {
        return this.capCulturas;
    }

    public void setCapCulturas(List<CapCultura> capCulturas) {
        this.capCulturas = capCulturas;
    }

    public CapCultura addCapCultura(CapCultura capCultura) {
        getCapCulturas().add(capCultura);
        capCultura.setCapCultura(this);

        return capCultura;
    }

    public CapCultura removeCapCultura(CapCultura capCultura) {
        getCapCulturas().remove(capCultura);
        capCultura.setCapCultura(null);

        return capCultura;
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