package ro.uti.ran.core.model.utils;

/**
 * Created by Dan on 15-Oct-15.
 */
public enum IndicativXml {
    ADAUGA_SI_INLOCUIESTE("ADD_MOD"), ANULEAZA("CNL"), DEZACTIVARE("DG"), REACTIVARE("RG");

    private final String cod;

    IndicativXml(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }

}
