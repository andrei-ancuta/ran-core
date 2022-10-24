package ro.uti.ran.core.model.utils;

/**
 * Created by Dan on 10-Nov-15.
 */
public enum TipRelPreemptiune {
    VANZATOR("V"),
    PREEMPTOR("P"),
    CUMPARATOR("C");

    private final String cod;

    TipRelPreemptiune(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }
}
