package ro.uti.ran.core.ws.model.transmitere;

import ro.uti.ran.core.model.portal.StareRegistru;
import ro.uti.ran.core.model.registru.NomStareRegistru;
import ro.uti.ran.core.model.utils.RanConstants;

import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-29 17:14
 */
public enum StareTransmisie implements Serializable {
    SALVATA,
    RECEPTIONATA,
    VALIDATA,
    INVALIDATA,
    EROARE;


    /**
     * Functie de conversie din stare registru in stare transmisie.
     *
     * @param stareRegistru
     * @return
     */
    public static StareTransmisie getByStareRegistru(StareRegistru stareRegistru) {
        if (stareRegistru == null) return null;

        //
        // todo:
        //
        return SALVATA;
    }

    public static StareTransmisie getByStareRegistru(NomStareRegistru nomStareRegistru) {
        if (nomStareRegistru == null) {
            return null;
        }
        switch (nomStareRegistru.getCod()) {
            case RanConstants.STARE_REGISTRU_RECEPTIONATA_COD:
                return RECEPTIONATA;
            case RanConstants.STARE_REGISTRU_VALIDATA_COD:
                return VALIDATA;
            case RanConstants.STARE_REGISTRU_INVALIDATA_COD:
                return INVALIDATA;
            case RanConstants.STARE_REGISTRU_SALVATA_COD:
                return SALVATA;
            case RanConstants.STARE_REGISTRU_EROARE_LA_SALVARE_COD:
                return EROARE;
            default:
                throw new IllegalArgumentException(nomStareRegistru.getCod() + " nu se regaseste in enum");

        }
    }


    public static StareTransmisie getByStareRegistru(String cod) {
        if (cod == null) {
            return null;
        }
        switch (cod) {
            case RanConstants.STARE_REGISTRU_RECEPTIONATA_COD:
                return RECEPTIONATA;
            case RanConstants.STARE_REGISTRU_VALIDATA_COD:
                return VALIDATA;
            case RanConstants.STARE_REGISTRU_INVALIDATA_COD:
                return INVALIDATA;
            case RanConstants.STARE_REGISTRU_SALVATA_COD:
                return SALVATA;
            case RanConstants.STARE_REGISTRU_EROARE_LA_SALVARE_COD:
                return EROARE;
            default:
                throw new IllegalArgumentException(cod + " nu se regaseste in enum");

        }
    }
}