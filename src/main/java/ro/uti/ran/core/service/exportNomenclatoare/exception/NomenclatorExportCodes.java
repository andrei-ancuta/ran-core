package ro.uti.ran.core.service.exportNomenclatoare.exception;

import ro.uti.ran.core.exception.base.MessageHolder;
import ro.uti.ran.core.exception.base.MessageProvider;

public enum NomenclatorExportCodes implements MessageProvider {

    PATH_NOT_FOUND(1, "Catalogs export path not found"),
    JAXB_MARSHALLER_NOT_LOADED(2, "JAXB Marshaller not initialized"),
    EXPORT_ERROR(3, "Export error"),
    NOMENCLATOR_ISNOT_EXPORTABLE(4, "Nomenclatorul '%s' nu este eligibil pentru export!"),
    NOMENCLATOR_NOT_FOUND(5, "Nomenclatorul '%s' nu exista!"),
    NOMENCLATOR_SUMMARY_NOT_FOUND(6, "Sumarul listei de nomenclatoare nu a fost inca exportat!"),
    NOMENCLATOR_PARSE_DATE(7, "Eroare la parsare data: '%s', cu formatul: '%s'!"),
    NOMENCLATOR_NOT_EXPORTED(8, "Nomenclatorul '%s' nu a fost identificat pentru a fi exportat!", "Va rugam consultati sumarul de nomenclatoare pentru a identifica codurile de nomenclator care sunt suportate de operatia de export");

    private MessageHolder provider;

    private NomenclatorExportCodes(int secondaryCode, String message, String hint) {
        provider = new MessageHolder(secondaryCode, message, hint);
    }

    private NomenclatorExportCodes(int secondaryCode, String message) {
        this(secondaryCode, message, null);
    }

    @Override
    public MessageHolder getMessageHolder() {
        return provider;
    }
}