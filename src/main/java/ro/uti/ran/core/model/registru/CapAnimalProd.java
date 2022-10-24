package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CAP_ANIMAL_PROD database table.
 */
@Entity
@Table(name = "CAP_ANIMAL_PROD")
@NamedQuery(name = "CapAnimalProd.findAll", query = "SELECT c FROM CapAnimalProd c")
public class CapAnimalProd extends Nomenclator implements Versioned, Editable, NomProductieNivelUat {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CAP_ANIMAL_PROD_IDCAP_ANIMAL_PROD_GENERATOR", sequenceName = "SEQ_CAP_ANIMAL_PROD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAP_ANIMAL_PROD_IDCAP_ANIMAL_PROD_GENERATOR")
    @Column(name = "ID_CAP_ANIMAL_PROD")
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


    @Column(name = "ORDIN_MULTIPLICARE")
    private Integer ordinMultiplicare;

    @Column(name = "TIP_OPERAND_RELATIE")
    private Integer tipOperandRelatie;


    //bi-directional many-to-one association to CapAnimalProd
    @ManyToOne
    @JoinColumn(name = "FK_CAP_ANIMAL_PROD")
    private CapAnimalProd capAnimalProd;

    //bi-directional many-to-one association to CapAnimalProd
    @OneToMany(mappedBy = "capAnimalProd")
    private List<CapAnimalProd> capAnimalProds = new ArrayList<>();

    //bi-directional many-to-one association to NomCapitol
    @ManyToOne
    @JoinColumn(name = "FK_NOM_CAPITOL")
    private NomCapitol nomCapitol;

    //bi-directional many-to-one association to NomSpecieAnimal
    @ManyToOne
    @JoinColumn(name = "FK_NOM_SPECIE_ANIMAL")
    private NomSpecieAnimal nomSpecieAnimal;

    //bi-directional many-to-one association to NomUnitateMasura
    @ManyToOne
    @JoinColumn(name = "FK_NOM_UNITATE_MASURA")
    private NomUnitateMasura nomUnitateMasura;

    @Transient
    private boolean isLatestVersion;

    public CapAnimalProd() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long idCapAnimalProd) {
        this.id = idCapAnimalProd;
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


    public Integer getOrdinMultiplicare() {
        return this.ordinMultiplicare;
    }

    public void setOrdinMultiplicare(Integer ordinMultiplicare) {
        this.ordinMultiplicare = ordinMultiplicare;
    }

    public Integer getTipOperandRelatie() {
        return this.tipOperandRelatie;
    }

    public void setTipOperandRelatie(Integer tipOperandRelatie) {
        this.tipOperandRelatie = tipOperandRelatie;
    }


    public CapAnimalProd getCapAnimalProd() {
        return this.capAnimalProd;
    }

    public void setCapAnimalProd(CapAnimalProd capAnimalProd) {
        this.capAnimalProd = capAnimalProd;
    }

    public List<CapAnimalProd> getCapAnimalProds() {
        return this.capAnimalProds;
    }

    public void setCapAnimalProds(List<CapAnimalProd> capAnimalProds) {
        this.capAnimalProds = capAnimalProds;
    }

    public CapAnimalProd addCapAnimalProd(CapAnimalProd capAnimalProd) {
        getCapAnimalProds().add(capAnimalProd);
        capAnimalProd.setCapAnimalProd(this);

        return capAnimalProd;
    }

    public CapAnimalProd removeCapAnimalProd(CapAnimalProd capAnimalProd) {
        getCapAnimalProds().remove(capAnimalProd);
        capAnimalProd.setCapAnimalProd(null);

        return capAnimalProd;
    }

    public NomCapitol getNomCapitol() {
        return this.nomCapitol;
    }

    public void setNomCapitol(NomCapitol nomCapitol) {
        this.nomCapitol = nomCapitol;
    }

    public NomSpecieAnimal getNomSpecieAnimal() {
        return this.nomSpecieAnimal;
    }

    public void setNomSpecieAnimal(NomSpecieAnimal nomSpecieAnimal) {
        this.nomSpecieAnimal = nomSpecieAnimal;
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