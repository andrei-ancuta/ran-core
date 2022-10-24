package ro.uti.ran.core.model.registru;

import javax.persistence.*;


/**
 * The persistent class for the NOM_TIP_SPATIU_PROT database table.
 */
@Entity
@Table(name = "NOM_TIP_SPATIU_PROT")
@NamedQuery(name = "NomTipSpatiuProt.findAll", query = "SELECT n FROM NomTipSpatiuProt n")
public class NomTipSpatiuProt extends Nomenclator implements  NomRegistrulAgricol{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_TIP_SPATIU_PROT_ID_GENERATOR", sequenceName = "SEQ_NOM_TIP_SPATIU_PROT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_TIP_SPATIU_PROT_ID_GENERATOR")
    @Column(name = "ID_NOM_TIP_SPATIU_PROT", updatable = false)
    private Long id;

    private String cod;

    private String denumire;

    public NomTipSpatiuProt() {
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