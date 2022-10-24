package ro.uti.ran.core.service.backend.nomenclator.type;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
public enum SqlConditionType {
    SIMPLE,                         // a simple condition like CODE= 'code'
    WITH_FOREIGN_KEY_AS_DESCRIPTION,
    WITH_DATE_BETWEEN,              // a condition like date between valid_from valid_to
    WITH_PARENT,
    WITH_PARENT_AND_DATE_BETWEEN,
    COMPLEX
    ;
}
