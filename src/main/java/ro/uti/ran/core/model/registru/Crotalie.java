package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the CROTALIE database table.
 */
@Entity
@NamedQuery(name = "Crotalie.findAll", query = "SELECT c FROM Crotalie c")
public class Crotalie implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CROTALIE_IDCROTALIE_GENERATOR", sequenceName = "SEQ_CROTALIE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CROTALIE_IDCROTALIE_GENERATOR")
    @Column(name = "ID_CROTALIE")
    private Long idCrotalie;

    @Column(name = "COD_IDENTIFICARE")
    private String codIdentificare;


    //bi-directional many-to-one association to CategorieAnimal
    @ManyToOne
    @JoinColumn(name = "FK_CATEGORIE_ANIMAL")
    private CategorieAnimal categorieAnimal;

    public Crotalie() {
    }

    public Long getIdCrotalie() {
        return this.idCrotalie;
    }

    public void setIdCrotalie(Long idCrotalie) {
        this.idCrotalie = idCrotalie;
    }

    public String getCodIdentificare() {
        return this.codIdentificare;
    }

    public void setCodIdentificare(String codIdentificare) {
        this.codIdentificare = codIdentificare;
    }


    public CategorieAnimal getCategorieAnimal() {
        return this.categorieAnimal;
    }

    public void setCategorieAnimal(CategorieAnimal categorieAnimal) {
        this.categorieAnimal = categorieAnimal;
    }

}