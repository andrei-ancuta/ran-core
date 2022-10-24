package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Stanciu Neculai on 07.Dec.2015.
 */
@Entity
@Table(name = "NOM_CAPITOL")
@NamedQuery(name = "NomCapitolPortal.findAll", query = "SELECT n FROM NomCapitol n")
public class NomCapitolPortal extends Nomenclator {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_CAPITOL_PORTAL_ID_GENERATOR", sequenceName = "SEQ_NOM_CAPITOL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_CAPITOL_PORTAL_ID_GENERATOR")
    @Column(name = "ID_NOM_CAPITOL", updatable = false)
    private Long id;

    @Column(name = "BASE_ID")
    private Long baseId;

    @Enumerated(EnumType.STRING)
    private TipCapitol cod;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_START")
    private Date dataStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_STOP")
    private Date dataStop;

    private String denumire;

    private String descriere;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBaseId() {
        return baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

    public TipCapitol getCod() {
        return cod;
    }

    public void setCod(TipCapitol cod) {
        this.cod = cod;
    }

    public Date getDataStart() {
        return dataStart;
    }

    public void setDataStart(Date dataStart) {
        this.dataStart = dataStart;
    }

    public Date getDataStop() {
        return dataStop;
    }

    public void setDataStop(Date dataStop) {
        this.dataStop = dataStop;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
}
