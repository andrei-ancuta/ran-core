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
 * The persistent class for the NOM_JUDET database table.
 */
@Entity
@Table(name = "NOM_JUDET")
@NamedQuery(name = "NomJudet.findAll", query = "SELECT n FROM NomJudet n")
public class NomJudet extends Nomenclator implements Versioned, NomOrganizareTeritoriala,Editable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_JUDET_ID_GENERATOR", sequenceName = "SEQ_NOM_JUDET", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_JUDET_ID_GENERATOR")
    @Column(name = "ID_NOM_JUDET", updatable = false)
    private Long id;

    @Column(name = "BASE_ID")
    private Long baseId;

    @Column(name = "COD_ALFA")
    private String codAlfa;

    @Column(name = "COD_SIRUTA", scale = 6)
    private int codSiruta;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_START")
    private Date dataStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_STOP")
    private Date dataStop;

    @Column(name = "UID_CMS")
    private String uidCms;

    private String denumire;

    //bi-directional many-to-one association to NomUat
    @OneToMany(mappedBy = "nomJudet")
    private List<NomUat> nomUats = new ArrayList<NomUat>();

    @Version
    @Column(name = "LAST_MODIFIED_DATE")
    private Timestamp lastModifiedDate;

    @Transient
    private boolean isLatestVersion;

    public NomJudet() {
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

    public String getCodAlfa() {
        return this.codAlfa;
    }

    public void setCodAlfa(String codAlfa) {
        this.codAlfa = codAlfa;
    }

    public int getCodSiruta() {
        return this.codSiruta;
    }

    public void setCodSiruta(int codSiruta) {
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

    public List<NomUat> getNomUats() {
        return this.nomUats;
    }

    public void setNomUats(List<NomUat> nomUats) {
        this.nomUats = nomUats;
    }

    public String getUidCms() {
        return uidCms;
    }

    public void setUidCms(String uidCms) {
        this.uidCms = uidCms;
    }

    public NomUat addNomUat(NomUat nomUat) {
        getNomUats().add(nomUat);
        nomUat.setNomJudet(this);

        return nomUat;
    }

    public NomUat removeNomUat(NomUat nomUat) {
        getNomUats().remove(nomUat);
        nomUat.setNomJudet(null);

        return nomUat;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String getCodePrim() {
        return this.codAlfa;
    }

    @Override
    public String getCodePrimName() {
        return "codAlfa";
    }

    @Override
    public Integer getCodeSec() {
        return null;
    }

    @Override
    public String getCodeSecName() {
        return null;
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