package ro.uti.ran.core.service.security;

/**
 * Created by Stanciu Neculai on 29.Oct.2015.
 */
public enum ExternWsContextEnum {
    WS_TRANSMITERE_DATE("TransmitereDate"),
    WS_INTEROGARE_DATE("InterogareDate");

    private String value;

    ExternWsContextEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
