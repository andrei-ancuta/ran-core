package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.HasPath;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-26 13:50
 */
public enum IncarcareField implements HasPath {

    dataIncarcare("dataIncarcare"),
    indexIncarcare("indexIncarcare"),
    denumireFisier("denumireFisier"),
    stareIncarcare_denumire("stareIncarcare.denumire"),
    uat_denumire("uat.denumire"),
    utilizator_email("utilizator.email")
    ;

    private String path;

    IncarcareField(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
