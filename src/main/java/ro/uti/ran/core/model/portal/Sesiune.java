package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 14:32
 */
@Entity
@Table(name="APP_SESIUNE")
public class Sesiune extends Model {

    @Id
    @GeneratedValue(generator = "SesiuneSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SesiuneSeq", sequenceName = "SEQ_APP_SESIUNE", allocationSize = 1)
    @Column(name = "ID_APP_SESIUNE", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_APP_UTILIZATOR")
    private Utilizator utilizator;

    @Column(name = "UID_SESIUNE_HTTP")
    private String uidSesiuneHttp;

    @Column(name = "ADRESA_IP")
    private String adresaIp;

    @Column(name = "DATA_START")
    private Date dataStart;

    @Column(name = "DATA_STOP")
    private Date dataStop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_APP_CONTEXT")
    private Context context;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilizator getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    public String getUidSesiuneHttp() {
        return uidSesiuneHttp;
    }

    public void setUidSesiuneHttp(String uidSesiuneHttp) {
        this.uidSesiuneHttp = uidSesiuneHttp;
    }

    public String getAdresaIp() {
        return adresaIp;
    }

    public void setAdresaIp(String adresaIp) {
        this.adresaIp = adresaIp;
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
