package ro.uti.ran.core.exception.codes;

import ro.uti.ran.core.exception.base.MessageHolder;
import ro.uti.ran.core.exception.base.MessageProvider;

public enum WsAutenticationCodes implements MessageProvider {

    // @formatter:off
    USER_NOT_FOUND(0, "Cod institutie %s inexistent."),
    USER_INACTIVE(1, "Cont institutie %s inactiv."),
    COD_LICENTA_INCORECT(2, "Credentialele utilizate pentru autentificare/autorizare sunt incorecte!"),
    CONT_INSTITUTIE_INEXISTENT(3, "Cont institutie %s inexistent."),
    TRANSMITERE_UAT_LOCAL(4,"Codul de identificare %s nu este pentru un  UAT local!")
    ;
    // @formatter:on

    private MessageHolder provider;

    private WsAutenticationCodes(int secondaryCode, String message, String hint) {
        provider = new MessageHolder(secondaryCode, message, hint);
    }

    private WsAutenticationCodes(int secondaryCode, String message, String hint, String resourceKey) {
        provider = new MessageHolder(secondaryCode, message, hint);
    }

    private WsAutenticationCodes(int secondaryCode, String message) {
        this(secondaryCode, message, null);
    }

    public MessageHolder getMessageHolder() {
        return provider;
    }

}
