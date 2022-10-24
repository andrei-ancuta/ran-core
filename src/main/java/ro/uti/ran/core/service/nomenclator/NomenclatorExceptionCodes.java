package ro.uti.ran.core.service.nomenclator;

import ro.uti.ran.core.exception.base.MessageHolder;
import ro.uti.ran.core.exception.base.MessageProvider;

/**
 * Created by ciprian.chiriac@greensoft.com.ro on 15.02.2016.
 */
public enum NomenclatorExceptionCodes implements MessageProvider {

    NOMENCLATOR_REFERENCED_EXCEPTION(1, "Intrare Nomenclator referentiata in alte tabele, nu poate fi stearsa"),
    NOMENCLATOR_IS_NOT_LAST_VERSION(2, "Intrare Nomenclator nu este ultima versiune, nu poate fi stearsa"),
    NOMENCLATOR_SYSTEM_CANT_BE_DELETED(3, "Intrare Nomenclator de sistem nu poate fi stearsa"),
    NOMENCLATOR_WITHOUT_DATA_STOP(4, "Intrare Nomenclator fara data stop"),
    NOMENCLATOR_NOT_VERSIONED(5, "Intrare Nomenclator neversionabila"),
    NOMENCLATOR_INTERSECTION_VERSION(6,"Intrare nomenclator versionalizat suprapusa cu alta versiune" ),
    NOMENCLATOR_WITHOUT_DATA_START(4, "Intrare Nomenclator fara data stop")
    ;

    private MessageHolder provider;

    private NomenclatorExceptionCodes(int secondaryCode, String message, String hint) {
        provider = new MessageHolder(secondaryCode, message, hint);
    }

    private NomenclatorExceptionCodes(int secondaryCode, String message) {
        this(secondaryCode, message, null);
    }

    @Override
    public MessageHolder getMessageHolder() {
        return provider;
    }
}
