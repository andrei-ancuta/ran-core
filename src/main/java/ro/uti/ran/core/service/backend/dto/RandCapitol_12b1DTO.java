package ro.uti.ran.core.service.backend.dto;

/**
 * Created by smash on 16/11/15.
 */
public class RandCapitol_12b1DTO extends RandCapitolCentralizator {

    private Integer suprafata;
    private Integer productieMedie;
    private Integer productieTotala;
    private String codTipSpatiu;


    public RandCapitol_12b1DTO() {
    }

    public Integer getSuprafata() {
        return suprafata;
    }

    public void setSuprafata(Integer suprafata) {
        this.suprafata = suprafata;
    }

    public Integer getProductieMedie() {
        return productieMedie;
    }

    public void setProductieMedie(Integer productieMedie) {
        this.productieMedie = productieMedie;
    }

    public Integer getProductieTotala() {
        return productieTotala;
    }

    public void setProductieTotala(Integer productieTotala) {
        this.productieTotala = productieTotala;
    }

    public String getCodTipSpatiu() {
        return codTipSpatiu;
    }

    public void setCodTipSpatiu(String codTipSpatiu) {
        this.codTipSpatiu = codTipSpatiu;
    }
}
