package ro.uti.ran.core.ws.internal.transmitere;

import java.io.Serializable;

/**
 * Created by Stanciu Neculai on 13.Nov.2015.
 */
public class DateRegistruDto implements Serializable {
    private Long idRegistru;

    public Long getIdRegistru() {
        return idRegistru;
    }

    public void setIdRegistru(Long idRegistru) {
        this.idRegistru = idRegistru;
    }
}
