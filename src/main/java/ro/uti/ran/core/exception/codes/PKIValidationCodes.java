package ro.uti.ran.core.exception.codes;

import ro.uti.ran.core.exception.base.MessageHolder;
import ro.uti.ran.core.exception.base.MessageProvider;
import ro.uti.ran.core.exception.hint.GlobalHints;


public enum PKIValidationCodes implements MessageProvider {
    INVALID_SIGNATURE(1, "Eroare validare semnatura", GlobalHints.DIGITAL_SIGNATURE_HINT),
    INVALID_DATA_FORMAT(2, "Decompresia datelor nu s-a putut realiza", GlobalHints.COMPRESS_ALGORITM_HINT),
    SIGNATURE_CORRUPT(4, "Semnatura digitala corupta", GlobalHints.DIGITAL_SIGNATURE_CORRUPT_HINT),
    SIGNATURE_NO_CERT_FOUND(20, "Certificatul semnatarului nu a fost localizat in semnatura", GlobalHints.DIGITAL_SIGNATURE_HINT),
    SIGNATURE_MULTIPLE_CERTS(21, "Semnatura digitala trebuie sa contina doar un sigur certificat al semnatarului", GlobalHints.DIGITAL_SIGNATURE_HINT);

    private MessageHolder holder;

    private PKIValidationCodes(int secondaryCode, String message, String hint) {
        holder = new MessageHolder(secondaryCode, message, hint);
    }

    private PKIValidationCodes(int secondaryCode, String message) {
        this(secondaryCode, message, null);
    }

    public MessageHolder getMessageHolder() {
        return holder;
    }

}
