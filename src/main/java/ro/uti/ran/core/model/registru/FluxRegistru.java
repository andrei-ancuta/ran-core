package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the FLUX_REGISTRU database table.
 */
@Entity
@Table(name = "FLUX_REGISTRU")
@NamedQuery(name = "FluxRegistru.findAll", query = "SELECT f FROM FluxRegistru f")
public class FluxRegistru implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "FLUX_REGISTRU_IDFLUXREGISTRU_GENERATOR", sequenceName = "SEQ_FLUX_REGISTRU", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FLUX_REGISTRU_IDFLUXREGISTRU_GENERATOR")
    @Column(name = "ID_FLUX_REGISTRU")
    private Long idFluxRegistru;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_STARE")
    private Date dataStare;

    @Column(name = "MESAJ_STARE")
    private String mesajStare;

    //bi-directional many-to-one association to NomStareRegistru
    @ManyToOne
    @JoinColumn(name = "FK_NOM_STARE_REGISTRU")
    private NomStareRegistru nomStareRegistru;

    //bi-directional many-to-one association to Registru
    @ManyToOne
    @JoinColumn(name = "FK_REGISTRU")
    private Registru registru;

    public FluxRegistru() {
    }

    public Long getIdFluxRegistru() {
        return this.idFluxRegistru;
    }

    public void setIdFluxRegistru(Long idFluxRegistru) {
        this.idFluxRegistru = idFluxRegistru;
    }

    public Date getDataStare() {
        return this.dataStare;
    }

    public void setDataStare(Date dataStare) {
        this.dataStare = dataStare;
    }

    public String getMesajStare() {
        return this.mesajStare;
    }

    public void setMesajStare(String mesajStare) {
        this.mesajStare = mesajStare;
    }

    public NomStareRegistru getNomStareRegistru() {
        return this.nomStareRegistru;
    }

    public void setNomStareRegistru(NomStareRegistru nomStareRegistru) {
        this.nomStareRegistru = nomStareRegistru;
    }

    public Registru getRegistru() {
        return this.registru;
    }

    public void setRegistru(Registru registru) {
        this.registru = registru;
    }

}