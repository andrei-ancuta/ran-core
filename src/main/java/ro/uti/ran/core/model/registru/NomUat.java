package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the NOM_UAT database table.
 */
@Entity
@Table(name = "NOM_UAT")
@NamedQuery(name = "NomUat.findAll", query = "SELECT n FROM NomUat n")
public class NomUat extends Nomenclator
        implements Versioned, Editable,NomOrganizareTeritoriala {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_UAT_ID_GENERATOR", sequenceName = "SEQ_NOM_UAT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_UAT_ID_GENERATOR")
    @Column(name = "ID_NOM_UAT", updatable = false)
    private Long id;

    @Column(name = "BASE_ID")
    private Long baseId;

    @Column(name = "COD_SIRUTA")
    private Integer codSiruta;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_START")
    private Date dataStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_STOP")
    private Date dataStop;

    private String denumire;

    //bi-directional many-to-one association to NomLocalitate
    @OneToMany(mappedBy = "nomUat")
    private List<NomLocalitate> nomLocalitates = new ArrayList<NomLocalitate>();

    //bi-directional many-to-one association to NomJudet
    @ManyToOne
    @JoinColumn(name = "FK_NOM_JUDET")
    private NomJudet nomJudet;

    @Column(name = "UID_CMS")
    private String uidCms;

    @Version
    @Column(name = "LAST_MODIFIED_DATE")
    private Timestamp lastModifiedDate;

    @Transient
    private boolean isLatestVersion;

    public NomUat() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBaseId() {
        return this.baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

    public Integer getCodSiruta() {
        return this.codSiruta;
    }

    public void setCodSiruta(Integer codSiruta) {
        this.codSiruta = codSiruta;
    }

    public Date getDataStart() {
        return this.dataStart;
    }

    public void setDataStart(Date dataStart) {
        this.dataStart = dataStart;
    }

    public Date getDataStop() {
        return this.dataStop;
    }

    public void setDataStop(Date dataStop) {
        this.dataStop = dataStop;
    }

    public String getDenumire() {
        return this.denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public List<NomLocalitate> getNomLocalitates() {
        return this.nomLocalitates;
    }

    public void setNomLocalitates(List<NomLocalitate> nomLocalitates) {
        this.nomLocalitates = nomLocalitates;
    }

    public String getUidCms() {
        return uidCms;
    }

    public void setUidCms(String uidCms) {
        this.uidCms = uidCms;
    }

    public NomLocalitate addNomLocalitate(NomLocalitate nomLocalitate) {
        getNomLocalitates().add(nomLocalitate);
        nomLocalitate.setNomUat(this);

        return nomLocalitate;
    }

    public NomLocalitate removeNomLocalitate(NomLocalitate nomLocalitate) {
        getNomLocalitates().remove(nomLocalitate);
        nomLocalitate.setNomUat(null);

        return nomLocalitate;
    }

    public NomJudet getNomJudet() {
        return this.nomJudet;
    }

    public void setNomJudet(NomJudet nomJudet) {
        this.nomJudet = nomJudet;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String getCodePrim() {
        return null;
    }

    @Override
    public String getCodePrimName() {
        return null;
    }

    @Override
    public Integer getCodeSec() {
        return this.codSiruta;
    }

    @Override
    public String getCodeSecName() {
        return "codSiruta";
    }

    @Override
    public Date getStarting(){
        return this.dataStart;
    }

    @Override
    @XmlElement(name="isLatestVersion")
    public boolean isLatestVersion() {
        return isLatestVersion;
    }

}