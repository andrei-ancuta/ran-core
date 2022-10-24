package ro.uti.ran.core.exception.codes;

import ro.uti.ran.core.exception.base.MessageHolder;
import ro.uti.ran.core.exception.base.MessageProvider;

public enum RequestCodes implements MessageProvider {

    // @formatter:off
    MISSING_ELEMENT(0, "Elementul %s este obligatoriu."),
    INVALID_ELEMENT(1, "Elementul %s este invalid."),
    MISSING_UUID_TRANSMISIE(5, "Elementul uuidTransmisie este obligatoriu."),
    MISSING_XML_CDATA(5, "Elementul xmlCDATA este obligatoriu."),
    MISSING_START_DATE(6, "Data de inceput este obligatorie."),
    MISSING_END_DATE(7, "Data de sfarsit este obligatorie."),
    INVALID_STOP_DATE(8, "Data stop trebuie sa fie cu cel putin %s luni in urma fata de data curenta."),
    INVALID_INTERVAL_FORMAT(9, "Data stop nu poate fi mai mica decat data start."),
    INVALID_INTERVAL_SIZE(10, "Perioada maxima pe care se poate efectua cautarea este de %s zile."),
    AUTH_EMPTY(17, "Autentificare esuata! Serviciul are nevoie de BASIC Authentication!"),
    MISSING_VALUE(0,"%s"),
    HEADER_ELEMENT_MISSING(11,"Proprietatea \"%s\" din header-ul fisierului xml trebuie sa aiba o valoare!");

    // @formatter:on

    private MessageHolder provider;

    private RequestCodes(int secondaryCode, String message, String hint) {
        provider = new MessageHolder(secondaryCode, message, hint);
    }

    private RequestCodes(int secondaryCode, String message, String hint, String resourceKey) {
        provider = new MessageHolder(secondaryCode, message, hint);
    }

    private RequestCodes(int secondaryCode, String message) {
        this(secondaryCode, message, null);
    }

    public MessageHolder getMessageHolder() {
        return provider;
    }

}
