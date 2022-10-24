package ro.uti.ran.core.model.utils;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-05 12:38
 */
public enum  RolEnum {

    GOSPODAR("GOSPODAR")
    ;

    private final String cod;

    RolEnum(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }
}
