package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the PERSOANA_PREEMPTIUNE database table.
 */
@Entity
@Table(name = "PERSOANA_PREEMPTIUNE")
@NamedQuery(name = "PersoanaPreemptiune.findAll", query = "SELECT p FROM PersoanaPreemptiune p")
public class PersoanaPreemptiune implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PERSOANA_PREEMPTIUNE_IDPERSOANAPREEMPTIUNE_GENERATOR", sequenceName = "SEQ_PERSOANA_PREEMPTIUNE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSOANA_PREEMPTIUNE_IDPERSOANAPREEMPTIUNE_GENERATOR")
    @Column(name = "ID_PERSOANA_PREEMPTIUNE")
    private Long idPersoanaPreemptiune;


    //bi-directional many-to-one association to NomTipRelPreemptiune
    @ManyToOne
    @JoinColumn(name = "FK_NOM_TIP_REL_PREEMPTIUNE")
    private NomTipRelPreemptiune nomTipRelPreemptiune;

    //bi-directional many-to-one association to PersoanaFizica
    @ManyToOne
    @JoinColumn(name = "FK_PERSOANA_FIZICA")
    private PersoanaFizica persoanaFizica;

    //bi-directional many-to-one association to PersoanaRc
    @ManyToOne
    @JoinColumn(name = "FK_PERSOANA_RC")
    private PersoanaRc persoanaRc;

    //bi-directional many-to-one association to Preemptiune
    @ManyToOne
    @JoinColumn(name = "FK_PREEMPTIUNE")
    private Preemptiune preemptiune;

    public PersoanaPreemptiune() {
    }

    public Long getIdPersoanaPreemptiune() {
        return this.idPersoanaPreemptiune;
    }

    public void setIdPersoanaPreemptiune(Long idPersoanaPreemptiune) {
        this.idPersoanaPreemptiune = idPersoanaPreemptiune;
    }


    public NomTipRelPreemptiune getNomTipRelPreemptiune() {
        return this.nomTipRelPreemptiune;
    }

    public void setNomTipRelPreemptiune(NomTipRelPreemptiune nomTipRelPreemptiune) {
        this.nomTipRelPreemptiune = nomTipRelPreemptiune;
    }

    public PersoanaFizica getPersoanaFizica() {
        return this.persoanaFizica;
    }

    public void setPersoanaFizica(PersoanaFizica persoanaFizica) {
        this.persoanaFizica = persoanaFizica;
    }

    public PersoanaRc getPersoanaRc() {
        return this.persoanaRc;
    }

    public void setPersoanaRc(PersoanaRc persoanaRc) {
        this.persoanaRc = persoanaRc;
    }

    public Preemptiune getPreemptiune() {
        return this.preemptiune;
    }

    public void setPreemptiune(Preemptiune preemptiune) {
        this.preemptiune = preemptiune;
    }

}