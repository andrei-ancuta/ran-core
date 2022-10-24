package ro.uti.ran.core.model.registru;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the NOM_TARA database table.
 */
@Entity
@Table(name = "NOM_TARA")
@NamedQuery(name = "NomTara.findAll", query = "SELECT n FROM NomTara n")
public class NomTara extends Nomenclator implements Versioned, NomOrganizareTeritoriala,Editable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_TARA_IDNOMTARA_GENERATOR", sequenceName = "SEQ_NOM_TARA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_TARA_IDNOMTARA_GENERATOR")
    @Column(name = "ID_NOM_TARA")
    private Long id;

    @Column(name = "BASE_ID")
    private Long baseId;

    @Column(name = "COD_ALFA_2")
    private String codAlfa2;

    @Column(name = "COD_ALFA_3")
    private String codAlfa3;

    @Column(name = "COD_NUMERIC")
    private Integer codNumeric;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_START")
    private Date dataStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_STOP")
    private Date dataStop;

    private String denumire;

    @Transient
    @Column
    private boolean isLatestVersion;

    @Version
    @Column(name = "LAST_MODIFIED_DATE")
    private Timestamp lastModifiedDate;

    public NomTara() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getBaseId() {
        return this.baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

    public String getCodAlfa2() {
        return this.codAlfa2;
    }

    public void setCodAlfa2(String codAlfa2) {
        this.codAlfa2 = codAlfa2;
    }

    public String getCodAlfa3() {
        return this.codAlfa3;
    }

    public void setCodAlfa3(String codAlfa3) {
        this.codAlfa3 = codAlfa3;
    }

    public Integer getCodNumeric() {
        return this.codNumeric;
    }

    public void setCodNumeric(Integer codNumeric) {
        this.codNumeric = codNumeric;
    }

    @Override
    public String getCodePrim() {
        return this.codAlfa2;
    }

    @Override
    public String getCodePrimName() {
        return "codAlfa2";
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

    @Override
    @XmlElement(name="isLatestVersion")
    public boolean isLatestVersion() {
        return isLatestVersion;
    }


}