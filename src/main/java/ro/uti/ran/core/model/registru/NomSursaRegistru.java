package ro.uti.ran.core.model.registru;

import javax.persistence.*;


/**
 * The persistent class for the NOM_SURSA_REGISTRU database table.
 */
@Entity
@Table(name = "NOM_SURSA_REGISTRU")
@NamedQuery(name = "NomSursaRegistru.findAll", query = "SELECT n FROM NomSursaRegistru n")
public class NomSursaRegistru extends Nomenclator {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_SURSA_REGISTRU_ID_GENERATOR", sequenceName = "SEQ_NOM_SURSA_REGISTRU", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_SURSA_REGISTRU_ID_GENERATOR")
    @Column(name = "ID_NOM_SURSA_REGISTRU", updatable = false)
    private Long id;

    private String cod;

    private String denumire;

    public NomSursaRegistru() {
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