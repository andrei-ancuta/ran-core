package ro.uti.ran.core.model.portal;

/**
 * Context sistem de generare credentiale utilizatori sistem.
 */
public enum ContextSistem {

    /**
     * Utilizator sistem pentru sisteme UAT
     */
    UAT("UAT"),

    /**
     * Utilizator sistem pentru sisteme UAT Judet
     */
    JUDET("JUDET"),

    /**
     * Utilizator sistem pentru sisteme institutii
     */
    INSTITUTIE("INSTITUTIE")
    ;


    private final String cod;

    ContextSistem(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }
}
