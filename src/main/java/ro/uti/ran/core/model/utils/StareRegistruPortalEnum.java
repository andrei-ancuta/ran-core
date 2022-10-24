package ro.uti.ran.core.model.utils;

/**
 * Created by Stanciu Neculai on 05.Nov.2015.
 */
public enum StareRegistruPortalEnum {
    INCARCAT(1L,"I"),
    TRANSMIS(2L,"T"),
    RESPINS(3L,"R");

    private final Long id;
    private final String cod;

    StareRegistruPortalEnum(Long id, String cod) {
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
