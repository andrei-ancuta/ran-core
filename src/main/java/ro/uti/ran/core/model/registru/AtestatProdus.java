package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the ATESTAT_PRODUS database table.
 */
@Entity
@Table(name = "ATESTAT_PRODUS")
@NamedQuery(name = "AtestatProdus.findAll", query = "SELECT a FROM AtestatProdus a")
public class AtestatProdus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ATESTAT_PRODUS_IDATESTATPRODUS_GENERATOR", sequenceName = "SEQ_ATESTAT_PRODUS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATESTAT_PRODUS_IDATESTATPRODUS_GENERATOR")
    @Column(name = "ID_ATESTAT_PRODUS")
    private Long idAtestatProdus;

    @Column(name = "DENUMIRE_PRODUS")
    private String denumireProdus;


    //bi-directional many-to-one association to Atestat
    @ManyToOne
    @JoinColumn(name = "FK_ATESTAT")
    private Atestat atestat;

    public AtestatProdus() {
    }

    public Long getIdAtestatProdus() {
        return this.idAtestatProdus;
    }

    public void setIdAtestatProdus(Long idAtestatProdus) {
        this.idAtestatProdus = idAtestatProdus;
    }

    public String getDenumireProdus() {
        return this.denumireProdus;
    }

    public void setDenumireProdus(String denumireProdus) {
        this.denumireProdus = denumireProdus;
    }


    public Atestat getAtestat() {
        return this.atestat;
    }

    public void setAtestat(Atestat atestat) {
        this.atestat = atestat;
    }

}