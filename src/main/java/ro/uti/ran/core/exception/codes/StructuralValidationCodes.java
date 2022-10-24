package ro.uti.ran.core.exception.codes;

import ro.uti.ran.core.exception.base.MessageHolder;
import ro.uti.ran.core.exception.base.MessageProvider;
import ro.uti.ran.core.exception.hint.GlobalHints;

/**
 * Created by bogdan.ardeleanu on 11/23/2015.
 */
public enum StructuralValidationCodes implements MessageProvider {
    XML_INVALID_STRUCTURAL(1, "XML invalid structural. Detalii: %s");

    private MessageHolder provider;

    StructuralValidationCodes(int secondaryCode, String message, String hint) {
        provider = new MessageHolder(secondaryCode, message, hint);
    }

    StructuralValidationCodes(int secondaryCode, String message) {
        this(secondaryCode, message, null);
    }

    public MessageHolder getMessageHolder() {
        return provider;
    }
}
