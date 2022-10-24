package ro.uti.ran.core.model.registru;

import javax.persistence.*;


/**
 * The persistent class for the NOM_TIP_CONTRACT database table.
 */
@Entity
@Table(name = "NOM_TIP_CONTRACT")
@NamedQuery(name = "NomTipContract.findAll", query = "SELECT n FROM NomTipContract n")
public class NomTipContract extends Nomenclator implements NomRegistrulAgricol{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_TIP_CONTRACT_ID_GENERATOR", sequenceName = "SEQ_NOM_TIP_CONTRACT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_TIP_CONTRACT_ID_GENERATOR")
    @Column(name = "ID_NOM_TIP_CONTRACT", updatable = false)
    private Long id;

    private String cod;

    private String denumire;

    public NomTipContract() {
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