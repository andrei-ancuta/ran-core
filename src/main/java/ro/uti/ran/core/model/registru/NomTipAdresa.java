package ro.uti.ran.core.model.registru;

import javax.persistence.*;


/**
 * The persistent class for the NOM_TIP_ADRESA database table.
 */
@Entity
@Table(name = "NOM_TIP_ADRESA")
@NamedQuery(name = "NomTipAdresa.findAll", query = "SELECT n FROM NomTipAdresa n")
public class NomTipAdresa extends Nomenclator implements NomRegistrulAgricol {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_TIP_ADRESA_ID_GENERATOR", sequenceName = "SEQ_NOM_TIP_ADRESA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_TIP_ADRESA_ID_GENERATOR")
    @Column(name = "ID_NOM_TIP_ADRESA", updatable = false)
    private Long id;

    private String cod;

    private String denumire;

    public NomTipAdresa() {
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

}