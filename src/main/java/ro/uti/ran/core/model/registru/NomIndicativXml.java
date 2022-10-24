package ro.uti.ran.core.model.registru;

import javax.persistence.*;


/**
 * The persistent class for the NOM_INDICATIV_XML database table.
 */
@Entity
@Table(name = "NOM_INDICATIV_XML")
@NamedQuery(name = "NomIndicativXml.findAll", query = "SELECT n FROM NomIndicativXml n")
public class NomIndicativXml extends Nomenclator implements NomSistem{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_INDICATIV_XML_ID_GENERATOR", sequenceName = "SEQ_NOM_INDICATIV_XML", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_INDICATIV_XML_ID_GENERATOR")
    @Column(name = "ID_NOM_INDICATIV_XML", updatable = false)
    private Long id;

    private String cod;

    private String denumire;

    public NomIndicativXml() {
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