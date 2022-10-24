package ro.uti.ran.core.service.backend.nomenclator.model;


import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.registru.TipNomenclator;
import ro.uti.ran.core.service.backend.nomenclator.type.SqlConditionType;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 * <p/>
 */
public interface ValidityNomenclator {

    /**
     * @return the proper name of the catalog
     */
    String getName();

    TipNomenclator getNomType();

    /**
     * @return code column name
     */
    String getCodeColumn();

    /**
     * @return table name for identifying the table to issue a select clause
     */
    String getTableName();

    /**
     * @return a sql condition type based on the catalog
     */
    SqlConditionType getConditionType();

    /**
     * @return true if the catalog should be validated
     */
    boolean isActive();             // enable or disable catalogs
}
