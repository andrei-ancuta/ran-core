package ro.uti.ran.core.service.backend.dto;

import java.math.BigDecimal;

/**
 * Created by smash on 20/11/15.
 */
public class RandCapitol_13centDTO extends RandCapitolCentralizator {

    private BigDecimal valoareProductie;

    public RandCapitol_13centDTO() {
    }

    public BigDecimal getValoareProductie() {
        return valoareProductie;
    }

    public void setValoareProductie(BigDecimal valoareProductie) {
        this.valoareProductie = valoareProductie;
    }
}
