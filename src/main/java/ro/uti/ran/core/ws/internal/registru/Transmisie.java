package ro.uti.ran.core.ws.internal.registru;

import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.ws.model.transmitere.InformatiiTransmisie;
import ro.uti.ran.core.ws.model.transmitere.StareTransmisie;

import java.util.Date;

public class Transmisie extends InformatiiTransmisie {

    private String continut;
    private Boolean isCorelareSemnalata;

    private String denumireUat;
    private Date dataRegistru;
    private String dataRegistruString;
    private String indexRegistru;
    private String identificatorGospodarie;
    private String codCapitol;
    private String modalitateTransmitere;
    private String stare;
    private String indicativXml;


    public Transmisie() {
    }

    public Transmisie(Registru registru) {
        if (registru == null) {
            return;
        }
        this.setContinut(registru.getContinut());
        this.setDataRegistru(registru.getDataRegistru());
        this.setIdentificatorGospodarie(registru.getIdentificatorGospodarie());
        this.setIndexRegistru(registru.getIndexRegistru());
        this.setIsRecipisaSemnata(registru.getIsRecipisaSemnata());
        if (registru.getNomUat() != null) {
            setDenumireUat(registru.getNomUat().getDenumire());
        }
        if (registru.getNomCapitol() != null) {
            setCodCapitol(registru.getNomCapitol().getCod().name());
        }
        setModalitateTransmitere(registru.getModalitateTransmitere().name());
        if (registru.getNomStareRegistru() != null) {
            setStare(StareTransmisie.getByStareRegistru(registru.getNomStareRegistru()).name());
        }
        if (registru.getNomIndicativXml() != null) {
            this.setIndicativXml(registru.getNomIndicativXml().getDenumire());
        }
    }


    public String getContinut() {
        return continut;
    }

    public void setContinut(String continut) {
        this.continut = continut;
    }

    public Date getDataRegistru() {
        return dataRegistru;
    }

    public void setDataRegistru(Date dataRegistru) {
        this.dataRegistru = dataRegistru;
    }


    public String getIdentificatorGospodarie() {
        return identificatorGospodarie;
    }

    public void setIdentificatorGospodarie(String identificatorGospodarie) {
        this.identificatorGospodarie = identificatorGospodarie;
    }

    public String getIndexRegistru() {
        return indexRegistru;
    }

    public void setIndexRegistru(String indexRegistru) {
        this.indexRegistru = indexRegistru;
    }

    public Boolean getCorelareSemnalata() {
        return isCorelareSemnalata;
    }

    public void setCorelareSemnalata(Boolean corelareSemnalata) {
        isCorelareSemnalata = corelareSemnalata;
    }

    public String getDenumireUat() {
        return denumireUat;
    }

    public void setDenumireUat(String denumireUat) {
        this.denumireUat = denumireUat;
    }

    public String getCodCapitol() {
        return codCapitol;
    }

    public void setCodCapitol(String codCapitol) {
        this.codCapitol = codCapitol;
    }

    public String getModalitateTransmitere() {
        return modalitateTransmitere;
    }

    public void setModalitateTransmitere(String modalitateTransmitere) {
        this.modalitateTransmitere = modalitateTransmitere;
    }

    public String getStare() {
        return stare;
    }

    public void setStare(String stare) {
        this.stare = stare;
    }

    public String getDataRegistruString() {
        return dataRegistruString;
    }

    public void setDataRegistruString(String dataRegistruString) {
        this.dataRegistruString = dataRegistruString;
    }

    public String getIndicativXml() {
        return indicativXml;
    }

    public void setIndicativXml(String indicativXml) {
        this.indicativXml = indicativXml;
    }
}
