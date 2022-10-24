package ro.uti.ran.core.model.utils;

/**
 * Created by Dan on 25-Jan-16.
 */
public enum FormaOrganizareRc {
    PERSOANA_FIZICA_AUTORIZATA("PFA"), INTREPRINDERE_INDIVIDUALA("II"), INTREPRINDERE_FAMILIALA("IF");

    private final String cod;

    FormaOrganizareRc(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }
}
