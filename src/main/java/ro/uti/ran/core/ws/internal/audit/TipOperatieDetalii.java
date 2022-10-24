package ro.uti.ran.core.ws.internal.audit;

import ro.uti.ran.core.model.portal.TipOperatie;

/**
 * Created by adrian.boldisor on 3/4/2016.
 */
public class TipOperatieDetalii {

    private Long  idTipOperatie;
    String cod ;
    String denumire;


  public TipOperatieDetalii(){}


    public TipOperatieDetalii(TipOperatie tipOperatie){

        this.idTipOperatie = tipOperatie.getId();
        this.cod = tipOperatie.getCod();
        this.denumire = tipOperatie.getDenumire();

    }

    public Long getIdTipOperatie() {
        return idTipOperatie;
    }

    public void setIdTipOperatie(Long idTipOperatie) {
        this.idTipOperatie = idTipOperatie;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }
}
