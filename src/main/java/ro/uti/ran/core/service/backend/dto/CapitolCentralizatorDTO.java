package ro.uti.ran.core.service.backend.dto;

import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.io.Serializable;
import java.util.List;

/**
 * Created by smash on 10/11/15.
 */
public class CapitolCentralizatorDTO implements Serializable{

    private Integer codSiruta;
    private Integer an;
    private TipCapitol tipCapitol;
    private String denumire;
    private List<? extends RandCapitolCentralizator> randCapitolList;

    public CapitolCentralizatorDTO() {
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public Integer getCodSiruta() {
        return codSiruta;
    }

    public void setCodSiruta(Integer codSiruta) {
        this.codSiruta = codSiruta;
    }

    public Integer getAn() {
        return an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public TipCapitol getTipCapitol() {
        return tipCapitol;
    }

    public void setTipCapitol(TipCapitol tipCapitol) {
        this.tipCapitol = tipCapitol;
    }

    public List<? extends RandCapitolCentralizator> getRandCapitolList() {
        return randCapitolList;
    }

    public void setRandCapitolList(List<? extends RandCapitolCentralizator> randCapitolList) {
        this.randCapitolList = randCapitolList;
    }
}
