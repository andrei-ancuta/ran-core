package ro.uti.ran.core.service.backend.dto;

/**
 * Created by smash on 03/11/15.
 */
public class RandCapitol_12aDTO extends RandCapitolCentralizator {


    private Integer suprafata;
    private Integer productieMedie;
    private Integer productieTotala;

    public RandCapitol_12aDTO() {
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
}
