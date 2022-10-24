package ro.uti.ran.core.model.utils;

/**
 * Created by Dan on 14-Oct-15.
 */
public enum TipDetinator {

    PF_DOM_FISCAL_IN_LOC("1"), PF_DOM_FISCAL_ALTA_LOC("2"), PJ_DOM_FISCAL_IN_LOC("3"), PJ_DOM_FISCAL_ALTA_LOC("4");

    private final String cod;

    TipDetinator(String cod) {
        this.cod = cod;
    }

    public  String getCod() {
        return cod;
    }

    public static TipDetinator getTipDetinatorByCod(String cod) {
        if (cod == null) {
            throw new IllegalArgumentException("Cod nedefinit!");
        }
        if (TipDetinator.PF_DOM_FISCAL_IN_LOC.getCod().toLowerCase().equals(cod.toLowerCase())) {
            return TipDetinator.PF_DOM_FISCAL_IN_LOC;
        }
        if (TipDetinator.PF_DOM_FISCAL_ALTA_LOC.getCod().toLowerCase().equals(cod.toLowerCase())) {
            return TipDetinator.PF_DOM_FISCAL_ALTA_LOC;
        }
        if (TipDetinator.PJ_DOM_FISCAL_IN_LOC.getCod().toLowerCase().equals(cod.toLowerCase())) {
            return TipDetinator.PJ_DOM_FISCAL_IN_LOC;
        }
        if (TipDetinator.PJ_DOM_FISCAL_ALTA_LOC.getCod().toLowerCase().equals(cod.toLowerCase())) {
            return TipDetinator.PJ_DOM_FISCAL_ALTA_LOC;
        }
        return null;
    }
}
