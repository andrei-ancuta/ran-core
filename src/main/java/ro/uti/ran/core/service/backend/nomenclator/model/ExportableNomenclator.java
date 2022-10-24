package ro.uti.ran.core.service.backend.nomenclator.model;

import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.model.registru.TipNomenclator;
import ro.uti.ran.core.service.backend.nomenclator.type.SqlConditionType;

/**
 * Interface for a exportable nomenclator
 * User: mala
 */
public interface ExportableNomenclator {

    /**
     * @return the proper name of the nomenclator
     */
    String getName();

    /**
     * @return code column name
     */
    String getCodeColumn();

    /**
     * @return description column name
     */
    String getDescriptionColumn();

    /**
     * @return table name for identifying the table to issue a select clause
     */
    String getTableName();

    /**
     * @return true if catalog is exportable
     */
    boolean isExportable();

    /**
     * @return a sql condition type based on the catalog
     */
    SqlConditionType getConditionType();

    boolean isAppUpdatable();

    TipNomenclator getNomType();

    String getNumeNomenclator(NomCapitol nomCapitol);



}
