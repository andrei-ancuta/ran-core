package ro.uti.ran.core.datasourcerouter;

/**
 * Created by IntelliJ IDEA.
 * User: bogdan.ardeleanu
 * Date: Mar 4, 2016
 * Time: 1:27:28 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnvironmentType {
    /**
     * Database schema for Registrul Agricol National (central)
     **/
    RAN("RAN"),

    /**
     * Database schema for Registrul Agricol UAT (local)
     **/
    RAL("RAL");

    private final String value;

    EnvironmentType(String s) {
        this.value = s;
    }

    public String getValue() {
        return value;
    }
}
