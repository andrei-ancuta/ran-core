package ro.uti.ran.core.ws.model.transmitere;

import java.io.Serializable;

/**
 * Created by Anastasia cea micuta on 11/29/2015.
 */
public enum ModalitateTransmitere implements Serializable {
    AUTOMAT(1),
    SEMIAUTOMAT(2),
    MANUAL(3);

    private int value;

    ModalitateTransmitere(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ModalitateTransmitere parse(int id) {
        ModalitateTransmitere modalitateTransmitere = null; // Default
        for (ModalitateTransmitere item : ModalitateTransmitere.values()) {
            if (item.getValue() == id) {
                modalitateTransmitere = item;
                break;
            }
        }
        return modalitateTransmitere;
    }
}
