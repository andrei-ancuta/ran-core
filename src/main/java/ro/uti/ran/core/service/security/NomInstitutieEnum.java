package ro.uti.ran.core.service.security;

/**
 * Created by Stanciu Neculai on 29.Oct.2015.
 */
public enum NomInstitutieEnum {
    ANCPI(1),
    ANAF(2),
    UAT(3),
    INS(4),
    JUDET(5);

    private int value;

    NomInstitutieEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
