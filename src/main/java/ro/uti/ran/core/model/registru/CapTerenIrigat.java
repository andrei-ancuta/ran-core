package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CAP_TEREN_IRIGAT database table.
 */
@Entity
@Table(name = "CAP_TEREN_IRIGAT")
@NamedQuery(name = "CapTerenIrigat.findAll", query = "SELECT c FROM CapTerenIrigat c")
public class CapTerenIrigat extends Nomenclator implements Serializable,Editable, Versioned, NomCapRegistrulAgricol {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CAP_TEREN_IRIGAT_IDCAP_TEREN_IRIGAT_GENERATOR", sequenceName = "SEQ_CAP_TEREN_IRIGAT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAP_TEREN_IRIGAT_IDCAP_TEREN_IRIGAT_GENERATOR")
    @Column(name = "ID_CAP_TEREN_IRIGAT")
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

    //bi-directional many-to-one association to CapTerenIrigat
    @ManyToOne
    @JoinColumn(name = "FK_CAP_TEREN_IRIGAT")
    private CapTerenIrigat capTerenIrigat;

    //bi-directional many-to-one association to CapTerenIrigat
    @OneToMany(mappedBy = "capTerenIrigat")
    private List<CapTerenIrigat> capTerenIrigats;

    //bi-directional many-to-one association to NomCapitol
    @ManyToOne
    @JoinColumn(name = "FK_NOM_CAPITOL")
    private NomCapitol nomCapitol;

    @Transient
    private boolean isLatestVersion;

    public CapTerenIrigat() {
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

    public CapTerenIrigat getCapTerenIrigat() {
        return this.capTerenIrigat;
    }

    public void setCapTerenIrigat(CapTerenIrigat capTerenIrigat) {
        this.capTerenIrigat = capTerenIrigat;
    }

    public List<CapTerenIrigat> getCapTerenIrigats() {
        return this.capTerenIrigats;
    }

    public void setCapTerenIrigats(List<CapTerenIrigat> capTerenIrigats) {
        this.capTerenIrigats = capTerenIrigats;
    }

    public CapTerenIrigat addCapTerenIrigat(CapTerenIrigat capTerenIrigat) {
        getCapTerenIrigats().add(capTerenIrigat);
        capTerenIrigat.setCapTerenIrigat(this);

        return capTerenIrigat;
    }

    public CapTerenIrigat removeCapTerenIrigat(CapTerenIrigat capTerenIrigat) {
        getCapTerenIrigats().remove(capTerenIrigat);
        capTerenIrigat.setCapTerenIrigat(null);

        return capTerenIrigat;
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
    public void setId(Long aLong) {
        this.id = aLong;
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