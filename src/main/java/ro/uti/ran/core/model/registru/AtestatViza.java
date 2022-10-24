package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the ATESTAT_VIZA database table.
 */
@Entity
@Table(name = "ATESTAT_VIZA")
@NamedQuery(name = "AtestatViza.findAll", query = "SELECT a FROM AtestatViza a")
public class AtestatViza implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ATESTAT_VIZA_IDATESTATVIZA_GENERATOR", sequenceName = "SEQ_ATESTAT_VIZA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATESTAT_VIZA_IDATESTATVIZA_GENERATOR")
    @Column(name = "ID_ATESTAT_VIZA")
    private Long idAtestatViza;

    @Column(name = "NUMAR_VIZA")
    private Integer numarViza;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_VIZA")
    private Date dataViza;

    //bi-directional many-to-one association to Atestat
    @ManyToOne
    @JoinColumn(name = "FK_ATESTAT")
    private Atestat atestat;

    public AtestatViza() {
    }

    public Long getIdAtestatViza() {
        return this.idAtestatViza;
    }

    public void setIdAtestatViza(Long idAtestatViza) {
        this.idAtestatViza = idAtestatViza;
    }

    public Date getDataViza() {
        return this.dataViza;
    }

    public void setDataViza(Date dataViza) {
        this.dataViza = dataViza;
    }

    public Atestat getAtestat() {
        return this.atestat;
    }

    public void setAtestat(Atestat atestat) {
        this.atestat = atestat;
    }

    public Integer getNumarViza() {
        return numarViza;
    }

    public void setNumarViza(Integer numarViza) {
        this.numarViza = numarViza;
    }
}