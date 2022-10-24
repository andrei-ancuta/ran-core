package ro.uti.ran.core.ws.internal.incarcare;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-11 15:09
 */
public class DetaliiFisierXml implements Serializable {

    private Long id;

    private String indexRegistru;

    private Date dataRegistru;

    private String denumireFisier;

    private Boolean isRecipisaSemnata;

    private String codStare;

    private String denumireStare;

    private String mesajStare;

    private String codStareCore;

    private String denumireStareCore;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndexRegistru() {
        return indexRegistru;
    }

    public void setIndexRegistru(String indexRegistru) {
        this.indexRegistru = indexRegistru;
    }

    public Date getDataRegistru() {
        return dataRegistru;
    }

    public void setDataRegistru(Date dataRegistru) {
        this.dataRegistru = dataRegistru;
    }

    public String getDenumireFisier() {
        return denumireFisier;
    }

    public void setDenumireFisier(String denumireFisier) {
        this.denumireFisier = denumireFisier;
    }

    public Boolean getIsRecipisaSemnata() {
        return isRecipisaSemnata;
    }

    public void setIsRecipisaSemnata(Boolean isRecipisaSemnata) {
        this.isRecipisaSemnata = isRecipisaSemnata;
    }

    public String getCodStare() {
        return codStare;
    }

    public void setCodStare(String codStare) {
        this.codStare = codStare;
    }

    public String getDenumireStare() {
        return denumireStare;
    }

    public void setDenumireStare(String denumireStare) {
        this.denumireStare = denumireStare;
    }

    public String getMesajStare() {
        return mesajStare;
    }

    public void setMesajStare(String mesajStare) {
        this.mesajStare = mesajStare;
    }

    public String getCodStareCore() {
        return codStareCore;
    }

    public void setCodStareCore(String codStareCore) {
        this.codStareCore = codStareCore;
    }

    public String getDenumireStareCore() {
        return denumireStareCore;
    }

    public void setDenumireStareCore(String denumireStareCore) {
        this.denumireStareCore = denumireStareCore;
    }
}
