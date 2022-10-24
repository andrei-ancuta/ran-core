package ro.uti.ran.core.exception.base;

public enum RanExceptionBaseCodes {
    PKIValidationException("PKI"),
    RequestValidationException("REQ-VAL-ERROR"),
    MessageStoreException("MSG-STORE-ERROR"),
    UserAlreadyExistsException("USR-EXISTS-ERROR"),
    InterogareDateRegistruException("LOAD-CAP"),
    DateRegistruValidationException("SAVE-CAP"),
    NomenclatorException("NOM-ERROR"),
    NomenclatorValidationException("NOM-VLD-CAT"),
    StructuralValidationException("STR-VLD-ERROR"),
    RegistruNotFoundException("REG-NOT-FOUND"),
    NumarSecventaNotFoundException("NB-SECV-NOT-FOUND"),
    NumarSecventaInvalidException("NB-SECV-ERROR"),
    WsAuthenticationException("WS-AUTH"),
    TipTransmitereDateException("TIP-TRANS-ERROR");
    private final String code;


    private RanExceptionBaseCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
