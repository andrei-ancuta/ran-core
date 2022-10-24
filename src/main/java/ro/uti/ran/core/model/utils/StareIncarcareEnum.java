package ro.uti.ran.core.model.utils;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-27 12:08
 */
public enum StareIncarcareEnum {
    RECEPTIONAT(1L, "R"),
    PROCESARE_IN_CURS(2L, "PC"),
    PROCESARE_FINALIZATA(3L, "PF")
    ;

    private final Long id;
    private final String cod;

    StareIncarcareEnum(Long id, String cod) {
        this.id = id;
        this.cod = cod;
    }

    public Long getId() {
        return id;
    }

    public String getCod() {
        return cod;
    }
}
