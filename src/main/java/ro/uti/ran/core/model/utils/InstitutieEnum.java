package ro.uti.ran.core.model.utils;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-30 00:26
 */
public enum InstitutieEnum {

    ANCPI(1L),
    ANAF(2L),
    UAT(3L),
    INS(4L),
    JUDET(5L),
    ;


    private final Long id;

    InstitutieEnum(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
