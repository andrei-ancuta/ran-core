package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the POM_RAZLET database table.
 */
@Entity
@Table(name = "POM_RAZLET")
@NamedQuery(name = "PomRazlet.findAll", query = "SELECT p FROM PomRazlet p")
public class PomRazlet implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "POM_RAZLET_IDPOMRAZLET_GENERATOR", sequenceName = "SEQ_POM_RAZLET", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POM_RAZLET_IDPOMRAZLET_GENERATOR")
    @Column(name = "ID_POM_RAZLET")
    private Long idPomRazlet;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    @Column(name = "NR_POM_ROD")
    private Integer nrPomRod;

    @Column(name = "NR_POM_TANAR")
    private Integer nrPomTanar;

    //bi-directional many-to-one association to CapPomRazlet
    @ManyToOne
    @JoinColumn(name = "FK_CAP_POM_RAZLET")
    private CapPomRazlet capPomRazlet;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    public PomRazlet() {
    }

    public Long getIdPomRazlet() {
        return this.idPomRazlet;
    }

    public void setIdPomRazlet(Long idPomRazlet) {
        this.idPomRazlet = idPomRazlet;
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


    public Integer getNrPomRod() {
        return this.nrPomRod;
    }

    public void setNrPomRod(Integer nrPomRod) {
        this.nrPomRod = nrPomRod;
    }

    public Integer getNrPomTanar() {
        return this.nrPomTanar;
    }

    public void setNrPomTanar(Integer nrPomTanar) {
        this.nrPomTanar = nrPomTanar;
    }

    public CapPomRazlet getCapPomRazlet() {
        return this.capPomRazlet;
    }

    public void setCapPomRazlet(CapPomRazlet capPomRazlet) {
        this.capPomRazlet = capPomRazlet;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

}