package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * The persistent class for the ANIMAL_PROD database table.
 */
@Entity
@Table(name = "ANIMAL_PROD")
@NamedQuery(name = "AnimalProd.findAll", query = "SELECT a FROM AnimalProd a")
public class AnimalProd implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ANIMAL_PROD_IDANIMALPROD_GENERATOR", sequenceName = "SEQ_ANIMAL_PROD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANIMAL_PROD_IDANIMALPROD_GENERATOR")
    @Column(name = "ID_ANIMAL_PROD")
    private Long idAnimalProd;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    @Digits(integer = 15, fraction = 2)
    @Column(name = "VALOARE")
    private BigDecimal valoare;

    //bi-directional many-to-one association to CapAnimalProd
    @ManyToOne
    @JoinColumn(name = "FK_CAP_ANIMAL_PROD")
    private CapAnimalProd capAnimalProd;

    //bi-directional many-to-one association to NomUat
    @ManyToOne
    @JoinColumn(name = "FK_NOM_UAT")
    private NomUat nomUat;

    public AnimalProd() {
    }

    public Long getIdAnimalProd() {
        return this.idAnimalProd;
    }

    public void setIdAnimalProd(Long idAnimalProd) {
        this.idAnimalProd = idAnimalProd;
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


    public BigDecimal getValoare() {
        return this.valoare;
    }

    public void setValoare(BigDecimal valoare) {
        this.valoare = valoare;
    }

    public CapAnimalProd getCapAnimalProd() {
        return this.capAnimalProd;
    }

    public void setCapAnimalProd(CapAnimalProd capAnimalProd) {
        this.capAnimalProd = capAnimalProd;
    }

    public NomUat getNomUat() {
        return this.nomUat;
    }

    public void setNomUat(NomUat nomUat) {
        this.nomUat = nomUat;
    }

}