package ro.uti.ran.core.service.backend.dto;

import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Aici se incapsuleaza datele din mesajul XML
 * Created by Dan on 13-Oct-15.
 */
public class RanDocDTO implements Serializable {
    private Gospodarie gospodarie;
    private List<? extends RandCapitolCentralizator> randuriCapitolCentralizator;
    private UUID codXml;
    private Integer sirutaUAT;
    private String username;
    private Date dataExport;
    private IndicativXml indicativ;
    private String clazz;
    private String codCapitol;

    private String identificatorGospodarie;
    private Integer anRaportare;
    private Integer semestruRaportare;
    private TipCapitol tipCapitol;


    private AnulareDTO anulareDTO;

    private boolean isDezactivare = false;
    private boolean isReactivare = false;


    public RanDocDTO() {
        gospodarie = new Gospodarie();
    }

    public Gospodarie getGospodarie() {
        return gospodarie;
    }

    public List<? extends RandCapitolCentralizator> getRanduriCapitolCentralizator() {
        return randuriCapitolCentralizator;
    }

    public void setRanduriCapitolCentralizator(List<? extends RandCapitolCentralizator> randuriCapitolCentralizator) {
        this.randuriCapitolCentralizator = randuriCapitolCentralizator;
    }

    public UUID getCodXml() {
        return codXml;
    }

    public void setCodXml(UUID codXml) {
        this.codXml = codXml;
    }

    public Integer getSirutaUAT() {
        return sirutaUAT;
    }

    public void setSirutaUAT(Integer sirutaUAT) {
        this.sirutaUAT = sirutaUAT;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDataExport() {
        return dataExport;
    }

    public void setDataExport(Date dataExport) {
        this.dataExport = dataExport;
    }

    public IndicativXml getIndicativ() {
        return indicativ;
    }

    public void setIndicativ(IndicativXml indicativ) {
        this.indicativ = indicativ;
    }

    public Integer getAnRaportare() {
        return anRaportare;
    }

    public void setAnRaportare(Integer anRaportare) {
        this.anRaportare = anRaportare;
    }

    public String getIdentificatorGospodarie() {
        return identificatorGospodarie;
    }

    public void setIdentificatorGospodarie(String identificatorGospodarie) {
        this.identificatorGospodarie = identificatorGospodarie;
    }

    public Integer getSemestruRaportare() {
        return semestruRaportare;
    }

    public void setSemestruRaportare(Integer semestruRaportare) {
        this.semestruRaportare = semestruRaportare;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public TipCapitol getTipCapitol() {
        return tipCapitol;
    }

    public void setTipCapitol(TipCapitol tipCapitol) {
        this.tipCapitol = tipCapitol;
    }

    public String getCodCapitol() {
        return codCapitol;
    }

    public void setCodCapitol(String codCapitol) {
        this.codCapitol = codCapitol;
    }


    public AnulareDTO getAnulareDTO() {
        return anulareDTO;
    }

    public void setAnulareDTO(AnulareDTO anulareDTO) {
        this.anulareDTO = anulareDTO;
    }

    public boolean getIsDezactivare() {
        return isDezactivare;
    }

    public void setIsDezactivare(boolean dezactivare) {
        isDezactivare = dezactivare;
    }

    public boolean getIsReactivare() {
        return isReactivare;
    }

    public void setIsReactivare(boolean reactivare) {
        isReactivare = reactivare;
    }
}
