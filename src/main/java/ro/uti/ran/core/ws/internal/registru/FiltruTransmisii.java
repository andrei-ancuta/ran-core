package ro.uti.ran.core.ws.internal.registru;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Andreea on 11/3/2015.
 */
public class FiltruTransmisii implements Serializable {


    private Date dataRegistruDeLa;
    private Date dataRegistruPanaLa;
    private String identificatorGospodarie;
    private String indexRegistru;
    private List<String> codCapitol;
    private Integer modalitateTransmitere;
    private String codStareRegistru;
    private Long baseIdUat;
    private Long idUatUtilizatorLogat;
    private Integer codSirutaUat;
    private String codNomIndicativXml;
    private Long fkNomJudet;

    public Date getDataRegistruDeLa() {
        return dataRegistruDeLa;
    }

    public void setDataRegistruDeLa(Date dataRegistruDeLa) {
        this.dataRegistruDeLa = dataRegistruDeLa;
    }

    public Date getDataRegistruPanaLa() {
        return dataRegistruPanaLa;
    }

    public void setDataRegistruPanaLa(Date dataRegistruPanaLa) {
        this.dataRegistruPanaLa = dataRegistruPanaLa;
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

    public Integer getModalitateTransmitere() {
        return modalitateTransmitere;
    }

    public void setModalitateTransmitere(Integer modalitateTransmitere) {
        this.modalitateTransmitere = modalitateTransmitere;
    }

    public String getCodStareRegistru() {
        return codStareRegistru;
    }

    public void setCodStareRegistru(String codStareRegistru) {
        this.codStareRegistru = codStareRegistru;
    }

    public List<String> getCodCapitol() {
        return codCapitol;
    }

    public void setCodCapitol(List<String> codCapitol) {
        this.codCapitol = codCapitol;
    }

    public Long getBaseIdUat() {
        return baseIdUat;
    }

    public void setBaseIdUat(Long baseIdUat) {
        this.baseIdUat = baseIdUat;
    }

    public Long getIdUatUtilizatorLogat() {
        return idUatUtilizatorLogat;
    }

    public void setIdUatUtilizatorLogat(Long idUatUtilizatorLogat) {
        this.idUatUtilizatorLogat = idUatUtilizatorLogat;
    }

    public Integer getCodSirutaUat() {
        return codSirutaUat;
    }

    public void setCodSirutaUat(Integer codSirutaUat) {
        this.codSirutaUat = codSirutaUat;
    }

    public String getCodNomIndicativXml() {
        return codNomIndicativXml;
    }

    public void setCodNomIndicativXml(String codNomIndicativXml) {
        this.codNomIndicativXml = codNomIndicativXml;
    }

    public Long getFkNomJudet() {
        return fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }
}
