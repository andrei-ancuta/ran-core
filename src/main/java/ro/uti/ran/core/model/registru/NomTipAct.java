package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;


/**
 * The persistent class for the NOM_TIP_ACT database table.
 */
@Entity
@Table(name = "NOM_TIP_ACT")
@NamedQuery(name = "NomTipAct.findAll", query = "SELECT n FROM NomTipAct n")
public class NomTipAct extends Nomenclator implements Versioned, Editable, NomRegistrulAgricol{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_TIP_ACT_IDNOM_TIP_ACT_GENERATOR", sequenceName = "SEQ_NOM_TIP_ACT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_TIP_ACT_IDNOM_TIP_ACT_GENERATOR")
    @Column(name = "ID_NOM_TIP_ACT")
    private Long id;

    @Column(name = "BASE_ID")
    private Long baseId;

    private String cod;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_START")
    private Date dataStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_STOP")
    private Date dataStop;

    private String denumire;

    private String descriere;

    @Transient
    private boolean isLatestVersion;


    public NomTipAct() {
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

    public String getCod() {
        return this.cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
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

    public String getDescriere() {
        return this.descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    @Override
    public String getCodePrim() {
        return this.cod;
    }

    @Override
    public String getCodePrimName() {
        return "cod";
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