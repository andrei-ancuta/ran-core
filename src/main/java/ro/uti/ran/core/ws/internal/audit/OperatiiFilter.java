package ro.uti.ran.core.ws.internal.audit;

import ro.uti.ran.core.utils.SearchFilter;

import java.util.Date;

/**
 * Created by adrian.boldisor on 3/4/2016.
 */
public class OperatiiFilter extends SearchFilter {


    private String utilizator;
    private Long tipOperatie;
    private Date startDataOperatie;
    private Date endtDataOperatie;
    private String uidSesiuneHttp;
    private String denumireContext;
    private String adresaIp;

    public String getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(String utilizator) {
        this.utilizator = utilizator;
    }

    public Long getTipOperatie() {
        return tipOperatie;
    }

    public void setTipOperatie(Long tipOperatie) {
        this.tipOperatie = tipOperatie;
    }

    public Date getStartDataOperatie() {
        return startDataOperatie;
    }

    public void setStartDataOperatie(Date startDataOperatie) {
        this.startDataOperatie = startDataOperatie;
    }

    public Date getEndtDataOperatie() {
        return endtDataOperatie;
    }

    public void setEndtDataOperatie(Date endtDataOperatie) {
        this.endtDataOperatie = endtDataOperatie;
    }

    public String getUidSesiuneHttp() {
        return uidSesiuneHttp;
    }

    public void setUidSesiuneHttp(String uidSesiuneHttp) {
        this.uidSesiuneHttp = uidSesiuneHttp;
    }

    public String getDenumireContext() {
        return denumireContext;
    }

    public void setDenumireContext(String denumireContext) {
        this.denumireContext = denumireContext;
    }

    public String getAdresaIp() {
        return adresaIp;
    }

    public void setAdresaIp(String adresaIp) {
        this.adresaIp = adresaIp;
    }
}
