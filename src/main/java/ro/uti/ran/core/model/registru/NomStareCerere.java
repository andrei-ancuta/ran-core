package ro.uti.ran.core.model.registru;

import javax.persistence.*;


/**
 * The persistent class for the NOM_STARE_CERERE database table.
 */
@Entity
@Table(name = "NOM_STARE_CERERE")
@NamedQuery(name = "NomStareCerere.findAll", query = "SELECT n FROM NomStareCerere n")
public class NomStareCerere extends Nomenclator implements NomSistem {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_STARE_CERERE_ID_GENERATOR", sequenceName = "SEQ_NOM_STARE_CERERE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_STARE_CERERE_ID_GENERATOR")
    @Column(name = "ID_NOM_STARE_CERERE", updatable = false)
    private Long id;

    private String cod;

    private String denumire;

    public NomStareCerere() {
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