package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CAP_CATEGORIE_FOLOSINTA database table.
 */
@Entity
@Table(name = "CAP_CATEGORIE_FOLOSINTA")
@NamedQuery(name = "CapCategorieFolosinta.findAll", query = "SELECT c FROM CapCategorieFolosinta c")
public class CapCategorieFolosinta extends Nomenclator implements Versioned, Editable, NomCapRegistrulAgricol {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CAP_CATEGORIE_FOLOSINTA_IDCAP_CATEGORIE_FOLOSINTA_GENERATOR", sequenceName = "SEQ_CAP_CATEGORIE_FOLOSINTA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAP_CATEGORIE_FOLOSINTA_IDCAP_CATEGORIE_FOLOSINTA_GENERATOR")
    @Column(name = "ID_CAP_CATEGORIE_FOLOSINTA")
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

    //bi-directional many-to-one association to CapCategorieFolosinta
    @ManyToOne
    @JoinColumn(name = "FK_CAP_CATEGORIE_FOLOSINTA")
    private CapCategorieFolosinta capCategorieFolosinta;

    //bi-directional many-to-one association to CapCategorieFolosinta
    @OneToMany(mappedBy = "capCategorieFolosinta")
    private List<CapCategorieFolosinta> capCategorieFolosintas = new ArrayList<>();

    //bi-directional many-to-one association to NomCapitol
    @ManyToOne
    @JoinColumn(name = "FK_NOM_CAPITOL")
    private NomCapitol nomCapitol;

    //bi-directional many-to-one association to NomCategorieFolosinta
    @ManyToOne
    @JoinColumn(name = "FK_NOM_CATEGORIE_FOLOSINTA")
    private NomCategorieFolosinta nomCategorieFolosinta;

    @Transient
    private boolean isLatestVersion;

    public CapCategorieFolosinta() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long idCapCategorieFolosinta) {
        this.id = idCapCategorieFolosinta;
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

    public CapCategorieFolosinta getCapCategorieFolosinta() {
        return this.capCategorieFolosinta;
    }

    public void setCapCategorieFolosinta(CapCategorieFolosinta capCategorieFolosinta) {
        this.capCategorieFolosinta = capCategorieFolosinta;
    }

    public List<CapCategorieFolosinta> getCapCategorieFolosintas() {
        return this.capCategorieFolosintas;
    }

    public void setCapCategorieFolosintas(List<CapCategorieFolosinta> capCategorieFolosintas) {
        this.capCategorieFolosintas = capCategorieFolosintas;
    }

    public CapCategorieFolosinta addCapCategorieFolosinta(CapCategorieFolosinta capCategorieFolosinta) {
        getCapCategorieFolosintas().add(capCategorieFolosinta);
        capCategorieFolosinta.setCapCategorieFolosinta(this);

        return capCategorieFolosinta;
    }

    public CapCategorieFolosinta removeCapCategorieFolosinta(CapCategorieFolosinta capCategorieFolosinta) {
        getCapCategorieFolosintas().remove(capCategorieFolosinta);
        capCategorieFolosinta.setCapCategorieFolosinta(null);

        return capCategorieFolosinta;
    }

    public NomCapitol getNomCapitol() {
        return this.nomCapitol;
    }

    public void setNomCapitol(NomCapitol nomCapitol) {
        this.nomCapitol = nomCapitol;
    }

    public NomCategorieFolosinta getNomCategorieFolosinta() {
        return this.nomCategorieFolosinta;
    }

    public void setNomCategorieFolosinta(NomCategorieFolosinta nomCategorieFolosinta) {
        this.nomCategorieFolosinta = nomCategorieFolosinta;
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