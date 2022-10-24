package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ATESTAT database table.
 */
@Entity
@NamedQuery(name = "Atestat.findAll", query = "SELECT a FROM Atestat a")
public class Atestat implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ATESTAT_IDATESTAT_GENERATOR", sequenceName = "SEQ_ATESTAT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATESTAT_IDATESTAT_GENERATOR")
    @Column(name = "ID_ATESTAT")
    private Long idAtestat;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_ELIBERARE_ATESTAT")
    private Date dataEliberareAtestat;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    @Column(name = "SERIE_NUMAR_ATESTAT")
    private String serieNumarAtestat;

    //bi-directional many-to-one association to Act
    @ManyToOne
    @JoinColumn(name = "FK_ACT_AVIZ_CONSULTATIV")
    private Act act;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    //bi-directional many-to-one association to AtestatProdus
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "atestat", fetch = FetchType.EAGER)
    private List<AtestatProdus> atestatProduses = new ArrayList<AtestatProdus>();

    //bi-directional many-to-one association to AtestatViza
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "atestat", fetch = FetchType.EAGER)
    private List<AtestatViza> atestatVizas = new ArrayList<AtestatViza>();

    //bi-directional many-to-one association to CertificatCom
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "atestat", fetch = FetchType.EAGER)
    private List<CertificatCom> certificatComs = new ArrayList<CertificatCom>();

    public Atestat() {
    }

    public Long getIdAtestat() {
        return this.idAtestat;
    }

    public void setIdAtestat(Long idAtestat) {
        this.idAtestat = idAtestat;
    }

    public Date getDataEliberareAtestat() {
        return this.dataEliberareAtestat;
    }

    public void setDataEliberareAtestat(Date dataEliberareAtestat) {
        this.dataEliberareAtestat = dataEliberareAtestat;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }


    public String getSerieNumarAtestat() {
        return this.serieNumarAtestat;
    }

    public void setSerieNumarAtestat(String serieNumarAtestat) {
        this.serieNumarAtestat = serieNumarAtestat;
    }

    public Act getAct() {
        return this.act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

    public List<AtestatProdus> getAtestatProduses() {
        return this.atestatProduses;
    }

    public void setAtestatProduses(List<AtestatProdus> atestatProduses) {
        this.atestatProduses = atestatProduses;
    }

    public AtestatProdus addAtestatProdus(AtestatProdus atestatProdus) {
        getAtestatProduses().add(atestatProdus);
        atestatProdus.setAtestat(this);

        return atestatProdus;
    }

    public AtestatProdus removeAtestatProdus(AtestatProdus atestatProdus) {
        getAtestatProduses().remove(atestatProdus);
        atestatProdus.setAtestat(null);

        return atestatProdus;
    }

    public List<AtestatViza> getAtestatVizas() {
        return this.atestatVizas;
    }

    public void setAtestatVizas(List<AtestatViza> atestatVizas) {
        this.atestatVizas = atestatVizas;
    }

    public AtestatViza addAtestatViza(AtestatViza atestatViza) {
        getAtestatVizas().add(atestatViza);
        atestatViza.setAtestat(this);

        return atestatViza;
    }

    public AtestatViza removeAtestatViza(AtestatViza atestatViza) {
        getAtestatVizas().remove(atestatViza);
        atestatViza.setAtestat(null);

        return atestatViza;
    }

    public List<CertificatCom> getCertificatComs() {
        return this.certificatComs;
    }

    public void setCertificatComs(List<CertificatCom> certificatComs) {
        this.certificatComs = certificatComs;
    }

    public CertificatCom addCertificatCom(CertificatCom certificatCom) {
        getCertificatComs().add(certificatCom);
        certificatCom.setAtestat(this);

        return certificatCom;
    }

    public CertificatCom removeCertificatCom(CertificatCom certificatCom) {
        getCertificatComs().remove(certificatCom);
        certificatCom.setAtestat(null);

        return certificatCom;
    }

}