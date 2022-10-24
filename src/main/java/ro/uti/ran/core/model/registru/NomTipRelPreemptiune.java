package ro.uti.ran.core.model.registru;

import javax.persistence.*;


/**
 * The persistent class for the NOM_TIP_REL_PREEMPTIUNE database table.
 */
@Entity
@Table(name = "NOM_TIP_REL_PREEMPTIUNE")
@NamedQuery(name = "NomTipRelPreemptiune.findAll", query = "SELECT n FROM NomTipRelPreemptiune n")
public class NomTipRelPreemptiune extends Nomenclator implements NomRegistrulAgricol {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_TIP_REL_PREEMPTIUNE_IDNOMTIPRELPREEMPTIUNE_GENERATOR", sequenceName = "SEQ_NOM_TIP_REL_PREEMPTIUNE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_TIP_REL_PREEMPTIUNE_IDNOMTIPRELPREEMPTIUNE_GENERATOR")
    @Column(name = "ID_NOM_TIP_REL_PREEMPTIUNE")
    private Long id;

    private String cod;

    private String denumire;


    public NomTipRelPreemptiune() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
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
