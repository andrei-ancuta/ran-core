package ro.uti.ran.core.ws.internal.renns;

import java.io.Serializable;

/**
 * Created by bogdan.ardeleanu on 11/16/2016.
 */
public class AdministratifAddress implements Serializable {
    private String administratifNo;

    private String streetName;

    private String gml311String;

    public String getAdministratifNo() {
        return administratifNo;
    }

    public void setAdministratifNo(String administratifNo) {
        this.administratifNo = administratifNo;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getGml311String() {
        return gml311String;
    }

    public void setGml311String(String gml311String) {
        this.gml311String = gml311String;
    }
}
