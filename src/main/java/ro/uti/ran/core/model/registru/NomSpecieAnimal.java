package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the NOM_SPECIE_ANIMAL database table.
 */
@Entity
@Table(name = "NOM_SPECIE_ANIMAL")
@NamedQuery(name = "NomSpecieAnimal.findAll", query = "SELECT n FROM NomSpecieAnimal n")
public class NomSpecieAnimal extends Nomenclator implements Serializable, Editable, NomTermeniAgricoli{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_SPECIE_ANIMAL_IDNOM_SPECIE_ANIMAL_GENERATOR", sequenceName = "SEQ_NOM_SPECIE_ANIMAL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_SPECIE_ANIMAL_IDNOM_SPECIE_ANIMAL_GENERATOR")
    @Column(name = "ID_NOM_SPECIE_ANIMAL")
    private Long id;

    private String cod;

    private String denumire;

    private String descriere;

    @Column(name = "FK_NOM_SPECIE_ANIMAL")
    private Long fkNomSpecieAnimal;

    //bi-directional many-to-one association to CapAnimalProd
    @OneToMany(mappedBy = "nomSpecieAnimal")
    private List<CapAnimalProd> capAnimalProds = new ArrayList<>();

    //bi-directional many-to-one association to CapCategorieAnimal
    @OneToMany(mappedBy = "nomSpecieAnimal")
    private List<CapCategorieAnimal> capCategorieAnimals = new ArrayList<>();

    public NomSpecieAnimal() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCod() {
        return this.cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
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

    public Long getFkNomSpecieAnimal() {
        return this.fkNomSpecieAnimal;
    }

    public void setFkNomSpecieAnimal(Long fkNomSpecieAnimal) {
        this.fkNomSpecieAnimal = fkNomSpecieAnimal;
    }

    public List<CapAnimalProd> getCapAnimalProds() {
        return this.capAnimalProds;
    }

    public void setCapAnimalProds(List<CapAnimalProd> capAnimalProds) {
        this.capAnimalProds = capAnimalProds;
    }

    public CapAnimalProd addCapAnimalProd(CapAnimalProd capAnimalProd) {
        getCapAnimalProds().add(capAnimalProd);
        capAnimalProd.setNomSpecieAnimal(this);

        return capAnimalProd;
    }

    public CapAnimalProd removeCapAnimalProd(CapAnimalProd capAnimalProd) {
        getCapAnimalProds().remove(capAnimalProd);
        capAnimalProd.setNomSpecieAnimal(null);

        return capAnimalProd;
    }

    public List<CapCategorieAnimal> getCapCategorieAnimals() {
        return this.capCategorieAnimals;
    }

    public void setCapCategorieAnimals(List<CapCategorieAnimal> capCategorieAnimals) {
        this.capCategorieAnimals = capCategorieAnimals;
    }

    public CapCategorieAnimal addCapCategorieAnimal(CapCategorieAnimal capCategorieAnimal) {
        getCapCategorieAnimals().add(capCategorieAnimal);
        capCategorieAnimal.setNomSpecieAnimal(this);

        return capCategorieAnimal;
    }

    public CapCategorieAnimal removeCapCategorieAnimal(CapCategorieAnimal capCategorieAnimal) {
        getCapCategorieAnimals().remove(capCategorieAnimal);
        capCategorieAnimal.setNomSpecieAnimal(null);

        return capCategorieAnimal;
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
        return null;
    }

    @Override
    public String getCodeSecName() {
        return null;
    }

    @Override
    public Date getStarting(){
        return null;
    }

}