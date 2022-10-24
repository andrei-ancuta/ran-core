package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.HasPath;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-26 13:50
 */
public enum RegistruPortalDetailsField implements HasPath {

    id("id"),
    indexRegistru("indexRegistru"),
    denumireFisier("denumireFisier"),
    dataRegistru("dataRegistru"),
    stareRegistru_denumire("denumireStareRegistruPortal"),
    ;

    private String path;

    RegistruPortalDetailsField(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
