package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CAP_APLICARE_INGRASAMANT database table.
 */
@Entity
@Table(name = "CAP_APLICARE_INGRASAMANT")
@NamedQuery(name = "CapAplicareIngrasamant.findAll", query = "SELECT c FROM CapAplicareIngrasamant c")
public class CapAplicareIngrasamant extends Nomenclator implements Versioned, Editable, NomCapRegistrulAgricol {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CAP_APLICARE_INGRASAMANT_IDCAP_APLICARE_INGRASAMANT_GENERATOR", sequenceName = "SEQ_CAP_APLICARE_INGRASAMANT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAP_APLICARE_INGRASAMANT_IDCAP_APLICARE_INGRASAMANT_GENERATOR")
    @Column(name = "ID_CAP_APLICARE_INGRASAMANT")
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


    //bi-directional many-to-one association to CapAplicareIngrasamant
    @ManyToOne
    @JoinColumn(name = "FK_CAP_APLICARE_INGRASAMANT")
    private CapAplicareIngrasamant capAplicareIngrasamant;

    //bi-directional many-to-one association to CapAplicareIngrasamant
    @OneToMany(mappedBy = "capAplicareIngrasamant")
    private List<CapAplicareIngrasamant> capAplicareIngrasamants = new ArrayList<>();

    //bi-directional many-to-one association to NomCapitol
    @ManyToOne
    @JoinColumn(name = "FK_NOM_CAPITOL")
    private NomCapitol nomCapitol;

    @Transient
    private boolean isLatestVersion;

    public CapAplicareIngrasamant() {
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


    public CapAplicareIngrasamant getCapAplicareIngrasamant() {
        return this.capAplicareIngrasamant;
    }

    public void setCapAplicareIngrasamant(CapAplicareIngrasamant capAplicareIngrasamant) {
        this.capAplicareIngrasamant = capAplicareIngrasamant;
    }

    public List<CapAplicareIngrasamant> getCapAplicareIngrasamants() {
        return this.capAplicareIngrasamants;
    }

    public void setCapAplicareIngrasamants(List<CapAplicareIngrasamant> capAplicareIngrasamants) {
        this.capAplicareIngrasamants = capAplicareIngrasamants;
    }

    public CapAplicareIngrasamant addCapAplicareIngrasamant(CapAplicareIngrasamant capAplicareIngrasamant) {
        getCapAplicareIngrasamants().add(capAplicareIngrasamant);
        capAplicareIngrasamant.setCapAplicareIngrasamant(this);

        return capAplicareIngrasamant;
    }

    public CapAplicareIngrasamant removeCapAplicareIngrasamant(CapAplicareIngrasamant capAplicareIngrasamant) {
        getCapAplicareIngrasamants().remove(capAplicareIngrasamant);
        capAplicareIngrasamant.setCapAplicareIngrasamant(null);

        return capAplicareIngrasamant;
    }

    public NomCapitol getNomCapitol() {
        return this.nomCapitol;
    }

    public void setNomCapitol(NomCapitol nomCapitol) {
        this.nomCapitol = nomCapitol;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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