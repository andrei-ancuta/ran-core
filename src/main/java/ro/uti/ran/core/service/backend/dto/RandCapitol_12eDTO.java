package ro.uti.ran.core.service.backend.dto;

/**
 * Created by smash on 19/11/15.
 */
public class RandCapitol_12eDTO extends RandCapitolCentralizator {

    private Integer suprafataLivezi;
    private Integer prodMedieLivezi;
    private Integer prodTotalaLivezi;

    public RandCapitol_12eDTO() {
    }

    public Integer getSuprafataLivezi() {
        return suprafataLivezi;
    }

    public void setSuprafataLivezi(Integer suprafataLivezi) {
        this.suprafataLivezi = suprafataLivezi;
    }

    public Integer getProdMedieLivezi() {
        return prodMedieLivezi;
    }

    public void setProdMedieLivezi(Integer prodMedieLivezi) {
        this.prodMedieLivezi = prodMedieLivezi;
    }

    public Integer getProdTotalaLivezi() {
        return prodTotalaLivezi;
    }

    public void setProdTotalaLivezi(Integer prodTotalaLivezi) {
        this.prodTotalaLivezi = prodTotalaLivezi;
    }
}
