package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CAP_POM_RAZLET database table.
 */
@Entity
@Table(name = "CAP_POM_RAZLET")
@NamedQuery(name = "CapPomRazlet.findAll", query = "SELECT c FROM CapPomRazlet c")
public class CapPomRazlet extends Nomenclator implements Serializable, Editable, Versioned, NomCapRegistrulAgricol {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CAP_POM_RAZLET_IDCAP_POM_RAZLET_GENERATOR", sequenceName = "SEQ_CAP_POM_RAZLET", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAP_POM_RAZLET_IDCAP_POM_RAZLET_GENERATOR")
    @Column(name = "ID_CAP_POM_RAZLET")
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

    //bi-directional many-to-one association to CapPomRazlet
    @ManyToOne
    @JoinColumn(name = "FK_CAP_POM_RAZLET")
    private CapPomRazlet capPomRazlet;

    //bi-directional many-to-one association to CapPomRazlet
    @OneToMany(mappedBy = "capPomRazlet")
    private List<CapPomRazlet> capPomRazlets;

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


    public CapPomRazlet() {
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

    public CapPomRazlet getCapPomRazlet() {
        return this.capPomRazlet;
    }

    public void setCapPomRazlet(CapPomRazlet capPomRazlet) {
        this.capPomRazlet = capPomRazlet;
    }

    public List<CapPomRazlet> getCapPomRazlets() {
        return this.capPomRazlets;
    }

    public void setCapPomRazlets(List<CapPomRazlet> capPomRazlets) {
        this.capPomRazlets = capPomRazlets;
    }

    public CapPomRazlet addCapPomRazlet(CapPomRazlet capPomRazlet) {
        getCapPomRazlets().add(capPomRazlet);
        capPomRazlet.setCapPomRazlet(this);

        return capPomRazlet;
    }

    public CapPomRazlet removeCapPomRazlet(CapPomRazlet capPomRazlet) {
        getCapPomRazlets().remove(capPomRazlet);
        capPomRazlet.setCapPomRazlet(null);

        return capPomRazlet;
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