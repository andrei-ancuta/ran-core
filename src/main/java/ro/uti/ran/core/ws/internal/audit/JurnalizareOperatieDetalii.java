package ro.uti.ran.core.ws.internal.audit;

import ro.uti.ran.core.model.portal.OperatieSesiune;

import java.util.Date;

/**
 * Created by smash on 04/01/16.
 */
public class JurnalizareOperatieDetalii {

    private Long id;
    private String utilizator;
    private Date dataOperatie;
    private String descriere;
    private String descriereCompleta;
    private String tipOperatie;
    private String uidSesiuneHttp;
    private String adresaIp;
    private String denumireContext;

    public JurnalizareOperatieDetalii() {
    }

    public JurnalizareOperatieDetalii(OperatieSesiune operatieSesiune) {
        this.id = operatieSesiune.getId();
        this.dataOperatie = operatieSesiune.getDataOperatie();
        this.descriere = operatieSesiune.getDescriere();
        this.descriereCompleta = operatieSesiune.getDescriereComplet();
        this.tipOperatie = operatieSesiune.getTipOperatie().getDenumire();
        this.utilizator  = operatieSesiune.getSesiune().getUtilizator().getNumeUtilizator();
        this.adresaIp = operatieSesiune.getSesiune().getAdresaIp();
        this.uidSesiuneHttp =operatieSesiune.getSesiune().getUidSesiuneHttp();
        this.denumireContext = operatieSesiune.getSesiune().getContext().getDenumire();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(String utilizator) {
        this.utilizator = utilizator;
    }

    public Date getDataOperatie() {
        return dataOperatie;
    }

    public void setDataOperatie(Date dataOperatie) {
        this.dataOperatie = dataOperatie;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getDescriereCompleta() {
        return descriereCompleta;
    }

    public void setDescriereCompleta(String descriereCompleta) {
        this.descriereCompleta = descriereCompleta;
    }

    public String getTipOperatie() {
        return tipOperatie;
    }

    public void setTipOperatie(String tipOperatie) {
        this.tipOperatie = tipOperatie;
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

    public String getDenumireContext() {
        return denumireContext;
    }

    public void setDenumireContext(String denumireContext) {
        this.denumireContext = denumireContext;
    }
}
