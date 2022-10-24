package ro.uti.ran.core.model.utils;

/**
 * Created by Dan on 14-Oct-15.
 */
public enum TipAdresa {

    GOSPODARIE("G"), DOMICILIU_FISCAL("DFG"), SEDIU_SOCIAL("SS"), DOMICILIU_REPREZENTANT_LEGAL("DRL"), SEDIU_PCT_LUCRU("SPL");
    private final String cod;

    TipAdresa(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }

    public static TipAdresa getTipAdresaByCod(String cod) {
        if (cod == null) {
            throw new IllegalArgumentException("Cod nedefinit!");
        }
        if (TipAdresa.GOSPODARIE.getCod().toLowerCase().equals(cod.toLowerCase())) {
            return TipAdresa.GOSPODARIE;
        }
        if (TipAdresa.DOMICILIU_FISCAL.getCod().toLowerCase().equals(cod.toLowerCase())) {
            return TipAdresa.DOMICILIU_FISCAL;
        }
        if (TipAdresa.SEDIU_SOCIAL.getCod().toLowerCase().equals(cod.toLowerCase())) {
            return TipAdresa.SEDIU_SOCIAL;
        }
        if (TipAdresa.DOMICILIU_REPREZENTANT_LEGAL.getCod().toLowerCase().equals(cod.toLowerCase())) {
            return TipAdresa.DOMICILIU_REPREZENTANT_LEGAL;
        }
        if (TipAdresa.SEDIU_PCT_LUCRU.getCod().toLowerCase().equals(cod.toLowerCase())) {
            return TipAdresa.SEDIU_PCT_LUCRU;
        }
        return null;
    }
}
