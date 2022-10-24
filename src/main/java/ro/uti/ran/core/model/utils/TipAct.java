package ro.uti.ran.core.model.utils;

/**
 * Created by Dan on 22-Jan-16.
 */
public enum TipAct {
    AVIZ_MADR_DADR("AMD"), ADEVERINTA_VANZARE("AV"),AVIZ_CONSULTATIV("AC");

    private final String cod;

    TipAct(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }
}
