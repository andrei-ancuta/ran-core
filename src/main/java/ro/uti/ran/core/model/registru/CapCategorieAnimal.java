package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CAP_CATEGORIE_ANIMAL database table.
 */
@Entity
@Table(name = "CAP_CATEGORIE_ANIMAL")
@NamedQuery(name = "CapCategorieAnimal.findAll", query = "SELECT c FROM CapCategorieAnimal c")
public class CapCategorieAnimal extends Nomenclator implements Versioned, Editable, NomCapRegistrulAgricol {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CAP_CATEGORIE_ANIMAL_IDCAP_CATEGORIE_ANIMAL_GENERATOR", sequenceName = "SEQ_CAP_CATEGORIE_ANIMAL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAP_CATEGORIE_ANIMAL_IDCAP_CATEGORIE_ANIMAL_GENERATOR")
    @Column(name = "ID_CAP_CATEGORIE_ANIMAL")
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

    //bi-directional many-to-one association to CapCategorieAnimal
    @ManyToOne
    @JoinColumn(name = "FK_CAP_CATEGORIE_ANIMAL")
    private CapCategorieAnimal capCategorieAnimal;

    //bi-directional many-to-one association to CapCategorieAnimal
    @OneToMany(mappedBy = "capCategorieAnimal")
    private List<CapCategorieAnimal> capCategorieAnimals = new ArrayList<>();

    //bi-directional many-to-one association to NomCapitol
    @ManyToOne
    @JoinColumn(name = "FK_NOM_CAPITOL")
    private NomCapitol nomCapitol;

    //bi-directional many-to-one association to NomSpecieAnimal
    @ManyToOne
    @JoinColumn(name = "FK_NOM_SPECIE_ANIMAL")
    private NomSpecieAnimal nomSpecieAnimal;

    @Transient
    private boolean isLatestVersion;

    public CapCategorieAnimal() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long idCapCategorieAnimal) {
        this.id = idCapCategorieAnimal;
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

    public CapCategorieAnimal getCapCategorieAnimal() {
        return this.capCategorieAnimal;
    }

    public void setCapCategorieAnimal(CapCategorieAnimal capCategorieAnimal) {
        this.capCategorieAnimal = capCategorieAnimal;
    }

    public List<CapCategorieAnimal> getCapCategorieAnimals() {
        return this.capCategorieAnimals;
    }

    public void setCapCategorieAnimals(List<CapCategorieAnimal> capCategorieAnimals) {
        this.capCategorieAnimals = capCategorieAnimals;
    }

    public CapCategorieAnimal addCapCategorieAnimal(CapCategorieAnimal capCategorieAnimal) {
        getCapCategorieAnimals().add(capCategorieAnimal);
        capCategorieAnimal.setCapCategorieAnimal(this);

        return capCategorieAnimal;
    }

    public CapCategorieAnimal removeCapCategorieAnimal(CapCategorieAnimal capCategorieAnimal) {
        getCapCategorieAnimals().remove(capCategorieAnimal);
        capCategorieAnimal.setCapCategorieAnimal(null);

        return capCategorieAnimal;
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