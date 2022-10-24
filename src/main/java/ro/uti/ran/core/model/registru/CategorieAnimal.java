package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the CATEGORIE_ANIMAL database table.
 */
@Entity
@Table(name = "CATEGORIE_ANIMAL")
@NamedQuery(name = "CategorieAnimal.findAll", query = "SELECT c FROM CategorieAnimal c")
public class CategorieAnimal implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CATEGORIE_ANIMAL_IDCATEGORIEANIMAL_GENERATOR", sequenceName = "SEQ_CATEGORIE_ANIMAL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORIE_ANIMAL_IDCATEGORIEANIMAL_GENERATOR")
    @Column(name = "ID_CATEGORIE_ANIMAL")
    private Long idCategorieAnimal;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    @Column(name = "NR_CAP")
    private Integer nrCap;

    private Integer semestru;

    //bi-directional many-to-one association to CapCategorieAnimal
    @ManyToOne
    @JoinColumn(name = "FK_CAP_CATEGORIE_ANIMAL")
    private CapCategorieAnimal capCategorieAnimal;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    //bi-directional many-to-one association to Crotalie
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categorieAnimal", fetch = FetchType.EAGER)
    private List<Crotalie> crotalies = new ArrayList<Crotalie>();

    public CategorieAnimal() {
    }

    public Long getIdCategorieAnimal() {
        return this.idCategorieAnimal;
    }

    public void setIdCategorieAnimal(Long idCategorieAnimal) {
        this.idCategorieAnimal = idCategorieAnimal;
    }

    public Integer getAn() {
        return this.an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }


    public Integer getNrCap() {
        return this.nrCap;
    }

    public void setNrCap(Integer nrCap) {
        this.nrCap = nrCap;
    }

    public Integer getSemestru() {
        return this.semestru;
    }

    public void setSemestru(Integer semestru) {
        this.semestru = semestru;
    }

    public CapCategorieAnimal getCapCategorieAnimal() {
        return this.capCategorieAnimal;
    }

    public void setCapCategorieAnimal(CapCategorieAnimal capCategorieAnimal) {
        this.capCategorieAnimal = capCategorieAnimal;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

    public List<Crotalie> getCrotalies() {
        return this.crotalies;
    }

    public void setCrotalies(List<Crotalie> crotalies) {
        this.crotalies = crotalies;
    }

    public Crotalie addCrotaly(Crotalie crotaly) {
        getCrotalies().add(crotaly);
        crotaly.setCategorieAnimal(this);

        return crotaly;
    }

    public Crotalie removeCrotaly(Crotalie crotaly) {
        getCrotalies().remove(crotaly);
        crotaly.setCategorieAnimal(null);

        return crotaly;
    }

}