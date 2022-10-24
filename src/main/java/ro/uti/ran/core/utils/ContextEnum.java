package ro.uti.ran.core.utils;

/**
 * Created by Stanciu Neculai on 27.Oct.2015.
 */
public enum ContextEnum {
    UAT("UAT"),
    ANCPI("ANCPI"),
    GOSPODAR("GOSPODAR"),
    INSTITUTIE("INSTITUTIE");

    ContextEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
