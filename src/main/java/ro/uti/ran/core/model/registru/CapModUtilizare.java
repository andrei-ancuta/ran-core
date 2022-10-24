package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CAP_MOD_UTILIZARE database table.
 */
@Entity
@Table(name = "CAP_MOD_UTILIZARE")
@NamedQuery(name = "CapModUtilizare.findAll", query = "SELECT c FROM CapModUtilizare c")
public class CapModUtilizare extends Nomenclator implements Versioned,Editable, NomCapRegistrulAgricol {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CAP_MOD_UTILIZARE_IDCAP_MOD_UTILIZARE_GENERATOR", sequenceName = "SEQ_CAP_MOD_UTILIZARE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAP_MOD_UTILIZARE_IDCAP_MOD_UTILIZARE_GENERATOR")
    @Column(name = "ID_CAP_MOD_UTILIZARE")
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

    //bi-directional many-to-one association to CapModUtilizare
    @ManyToOne
    @JoinColumn(name = "FK_CAP_MOD_UTILIZARE")
    private CapModUtilizare capModUtilizare;

    //bi-directional many-to-one association to CapModUtilizare
    @OneToMany(mappedBy = "capModUtilizare")
    private List<CapModUtilizare> capModUtilizares;

    //bi-directional many-to-one association to NomCapitol
    @ManyToOne
    @JoinColumn(name = "FK_NOM_CAPITOL")
    private NomCapitol nomCapitol;

    @Transient
    private boolean isLatestVersion;

    public CapModUtilizare() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long idCapModUtilizare) {
        this.id = idCapModUtilizare;
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

    public CapModUtilizare getCapModUtilizare() {
        return this.capModUtilizare;
    }

    public void setCapModUtilizare(CapModUtilizare capModUtilizare) {
        this.capModUtilizare = capModUtilizare;
    }

    public List<CapModUtilizare> getCapModUtilizares() {
        return this.capModUtilizares;
    }

    public void setCapModUtilizares(List<CapModUtilizare> capModUtilizares) {
        this.capModUtilizares = capModUtilizares;
    }

    public CapModUtilizare addCapModUtilizare(CapModUtilizare capModUtilizare) {
        getCapModUtilizares().add(capModUtilizare);
        capModUtilizare.setCapModUtilizare(this);

        return capModUtilizare;
    }

    public CapModUtilizare removeCapModUtilizare(CapModUtilizare capModUtilizare) {
        getCapModUtilizares().remove(capModUtilizare);
        capModUtilizare.setCapModUtilizare(null);

        return capModUtilizare;
    }

    public NomCapitol getNomCapitol() {
        return this.nomCapitol;
    }

    public void setNomCapitol(NomCapitol nomCapitol) {
        this.nomCapitol = nomCapitol;
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