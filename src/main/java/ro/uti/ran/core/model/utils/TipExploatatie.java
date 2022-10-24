package ro.uti.ran.core.model.utils;

/**
 * Created by Dan on 16-Nov-15.
 */
public enum TipExploatatie {
    GOSPODARIE_INDIVIDUALA("1"), PFA_II_IF("2"), REGIA_AUTONOMA("3"), SOCIETATE_ASOCIATIE_AGRICOLA("4");

    private final String cod;

    TipExploatatie(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }
}
