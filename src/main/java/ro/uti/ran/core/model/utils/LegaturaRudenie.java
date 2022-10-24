package ro.uti.ran.core.model.utils;

/**
 * Created by Dan on 30-Oct-15.
 */
public enum LegaturaRudenie {
    CAP_GOSPODARIE("1"), SOT_SOTIE("2"), FIU_FICA("3"), ALTE_RUDE("4"), NEINRUDIT("5");
    private final String cod;

    LegaturaRudenie(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }
}
