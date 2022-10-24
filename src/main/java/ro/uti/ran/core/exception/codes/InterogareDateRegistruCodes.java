package ro.uti.ran.core.exception.codes;

import ro.uti.ran.core.exception.base.MessageHolder;
import ro.uti.ran.core.exception.base.MessageProvider;

/**
 * Created by Dan on 13-Nov-15.
 */
public enum InterogareDateRegistruCodes implements MessageProvider {

    GOSPODARIE_NOT_FOUND(1, "Gospodarie inexistenta cu identificator '%s' si cod siruta UAT '%s'"),
    CAPITOL_NOT_FOUND(2, "Capitol '%s' inexistent pentru gospodaria cu identificator '%s' si cod siruta UAT '%s'"),
    CAPITOL_AN_NOT_FOUND(3, "Capitol '%s' inexistent pentru gospodaria cu identificator '%s' si cod siruta UAT '%s', pentru anul '%s'"),
    CAPITOL_AN_SEMESTRU_NOT_FOUND(4, "Capitol '%s' inexistent pentru gospodaria cu identificator '%s' si cod siruta UAT '%s', pentru anul '%s', semestrul '%s' "),
    CNP_NOT_FOUND(5, "CNP '%s' inexistent"),
    CUI_NOT_FOUND(6, "CUI '%s' inexistent"),
    CAPITOL_CENTRALIZATOR_NOT_FOUND(7, "Capitolul '%s' nu exista!"),
    CAPITOL_CENTRALIZATOR_EMPTY(8, "Pe anul '%s' nu exista informatii pentru capitolul '%s' si cod siruta UAT '%s'!"),
    COD_SIRUTA_INVALID(9, "Nu exista UAT pentru care codSiruta='%s'!"),
    NIF_NOT_FOUND(10, "NIF '%s' inexistent"),
    UNAUTH_ACCESS(11,"Authentificare esuata. Acces neautorizat!"),
    INSUFICIENT_PRIVELEGES(12,"Nu se permite interogarea datelor pentru UAT cod siruta '%s'")
    ;

    private MessageHolder provider;

    private InterogareDateRegistruCodes(int secondaryCode, String message, String hint) {
        provider = new MessageHolder(secondaryCode, message, hint);
    }

    private InterogareDateRegistruCodes(int secondaryCode, String message) {
        this(secondaryCode, message, null);
    }

    public MessageHolder getMessageHolder() {
        return provider;
    }
}
