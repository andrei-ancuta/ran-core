package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the CERTIFICAT_COM database table.
 */
@Entity
@Table(name = "CERTIFICAT_COM")
@NamedQuery(name = "CertificatCom.findAll", query = "SELECT c FROM CertificatCom c")
public class CertificatCom implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CERTIFICAT_COM_IDCERTIFICATCOM_GENERATOR", sequenceName = "SEQ_CERTIFICAT_COM", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CERTIFICAT_COM_IDCERTIFICATCOM_GENERATOR")
    @Column(name = "ID_CERTIFICAT_COM")
    private Long idCertificatCom;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_ELIBERARE")
    private Date dataEliberare;


    private String serie;

    //bi-directional many-to-one association to Atestat
    @ManyToOne
    @JoinColumn(name = "FK_ATESTAT")
    private Atestat atestat;

    public CertificatCom() {
    }

    public Long getIdCertificatCom() {
        return this.idCertificatCom;
    }

    public void setIdCertificatCom(Long idCertificatCom) {
        this.idCertificatCom = idCertificatCom;
    }

    public Date getDataEliberare() {
        return this.dataEliberare;
    }

    public void setDataEliberare(Date dataEliberare) {
        this.dataEliberare = dataEliberare;
    }


    public String getSerie() {
        return this.serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Atestat getAtestat() {
        return this.atestat;
    }

    public void setAtestat(Atestat atestat) {
        this.atestat = atestat;
    }

}