package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the NOM_LOCALITATE database table.
 */
@Entity
@Table(name = "NOM_LOCALITATE")
@NamedQuery(name = "NomLocalitate.findAll", query = "SELECT n FROM NomLocalitate n")
public class NomLocalitate extends Nomenclator implements Versioned,Editable, NomOrganizareTeritoriala {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_LOCALITATE_ID_GENERATOR", sequenceName = "SEQ_NOM_LOCALITATE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_LOCALITATE_ID_GENERATOR")
    @Column(name = "ID_NOM_LOCALITATE", updatable = false)
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

    @Version
    @Column(name = "LAST_MODIFIED_DATE")
    private Timestamp lastModifiedDate;

    private Long tip;

    @Column(name = "UID_CMS")
    private Long uidCms;

    //bi-directional many-to-one association to NomUat
    @ManyToOne
    @JoinColumn(name = "FK_NOM_UAT")
    private NomUat nomUat;

    @Transient
    private boolean isLatestVersion;

    public NomLocalitate() {
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

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }


    public Long getTip() {
        return this.tip;
    }

    public void setTip(Long tip) {
        this.tip = tip;
    }

    public Long getUidCms() {
        return this.uidCms;
    }

    public void setUidCms(Long uidCms) {
        this.uidCms = uidCms;
    }


    public NomUat getNomUat() {
        return this.nomUat;
    }

    public void setNomUat(NomUat nomUat) {
        this.nomUat = nomUat;
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