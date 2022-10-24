package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the NOM_TIP_EXPLOATATIE database table.
 */
@Entity
@Table(name = "NOM_TIP_EXPLOATATIE")
@NamedQuery(name = "NomTipExploatatie.findAll", query = "SELECT n FROM NomTipExploatatie n")
public class NomTipExploatatie extends Nomenclator implements Versioned, Editable, NomRegistrulAgricol{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_TIP_EXPLOATATIE_ID_GENERATOR", sequenceName = "SEQ_NOM_TIP_EXPLOATATIE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_TIP_EXPLOATATIE_ID_GENERATOR")
    @Column(name = "ID_NOM_TIP_EXPLOATATIE", updatable = false)
    private Long id;

    @Column(name = "BASE_ID")
    private Long baseId;

    private String cod;

    @Column(name = "COD_RAND")
    private Integer codRand;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_START")
    private Date dataStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_STOP")
    private Date dataStop;

    private String denumire;

    private String descriere;

    @Column(name = "TIP_PERSOANA")
    private Integer tipPersoana;

    @Version
    @Column(name = "LAST_MODIFIED_DATE")
    private Timestamp lastModifiedDate;

    @Transient
    private boolean isLatestVersion;

    public NomTipExploatatie() {
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

    public Integer getCodRand() {
        return this.codRand;
    }

    public void setCodRand(Integer codRand) {
        this.codRand = codRand;
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

    public Integer getTipPersoana() {
        return this.tipPersoana;
    }

    public void setTipPersoana(Integer tipPersoana) {
        this.tipPersoana = tipPersoana;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
        return this.codRand;
    }

    @Override
    public String getCodeSecName() {
        return "codRand";
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