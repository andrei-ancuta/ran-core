package ro.uti.ran.core.model.utils;

/**
 * Created by Dan on 02-Nov-15.
 */
public enum TipContract {
    ARENDARE("A"), CONCESIUNE("C");

    private final String cod;

    TipContract(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }

    public static TipContract getTipContractByCod(String cod) {
        if (cod == null) {
            throw new IllegalArgumentException("Cod nedefinit!");
        }
        if (TipContract.ARENDARE.getCod().toLowerCase().equals(cod.toLowerCase())) {
            return TipContract.ARENDARE;
        }
        if (TipContract.CONCESIUNE.getCod().toLowerCase().equals(cod.toLowerCase())) {
            return TipContract.CONCESIUNE;
        }
        return null;
    }
}
