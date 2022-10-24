package ro.uti.ran.core.service.backend.dto;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Dan on 03-Mar-16.
 */
public class InsertGeometrieDTO {

    private final Long fk;
    private final String geometrie;
    private final String tableName;
    private final String columnIdName;
    private final String columnFkName;
    private final String columnIsName;
    private final Long fkNomJudet;
    private final Integer columnIsValoare;
    ;

    private InsertGeometrieDTO(InsertGeometrieDTOBuilder builder) {
        this.fk = builder.fk;
        this.geometrie = builder.geometrie;
        this.tableName = builder.tableName;
        this.columnIdName = builder.columnIdName;
        this.columnFkName = builder.columnFkName;
        this.fkNomJudet = builder.fkNomJudet;
        this.columnIsName = builder.columnIsName;
        this.columnIsValoare = builder.columnIsValoare;
    }

    public Long getFk() {
        return fk;
    }

    public String getGeometrie() {
        return geometrie;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnIdName() {
        return columnIdName;
    }

    public String getColumnFkName() {
        return columnFkName;
    }

    public Long getFkNomJudet() {
        return fkNomJudet;
    }

    public String getColumnIsName() {
        return columnIsName;
    }

    public Integer getColumnIsValoare() {
        return columnIsValoare;
    }

    //Builder Class
    public static class InsertGeometrieDTOBuilder {
        private Long fk;
        private String geometrie;
        private String tableName;
        private String columnIdName;
        private String columnFkName;
        private String columnIsName;
        private Long fkNomJudet;
        private Integer columnIsValoare;

        public InsertGeometrieDTOBuilder() {
        }

        /**
         * @param fk valoarea ce se insereaza in campul columnFkName
         * @return builder
         */
        public InsertGeometrieDTOBuilder fk(Long fk) {
            this.fk = fk;
            return this;
        }

        /**
         * @param geometrie valoarea ce se insereaza in campul GEOMETRIE
         * @return builder
         */
        public InsertGeometrieDTOBuilder geometrie(String geometrie) {
            this.geometrie = geometrie;
            return this;
        }

        /**
         * @param tableName exemplu: GEOLOCATOR_ADRESA, GEOMETRIE_CLADIRE, GEOMETRIE_PARCELA_TEREN
         * @return builder
         */
        public InsertGeometrieDTOBuilder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        /**
         * @param columnIdName exemplu: ID_GEOLOCATOR_ADRESA, ID_GEOMETRIE_CLADIRE, ID_GEOMETRIE_PARCELA_TEREN
         * @return builder
         */
        public InsertGeometrieDTOBuilder columnIdName(String columnIdName) {
            this.columnIdName = columnIdName;
            return this;
        }

        /**
         * @param columnFkName exemplu: FK_ADRESA, FK_CLADIRE, FK_PARCELA_TEREN
         * @return builder
         */
        public InsertGeometrieDTOBuilder columnFkName(String columnFkName) {
            this.columnFkName = columnFkName;
            return this;
        }

        /**
         * @param fkNomJudet valoare ce se insereaza in campul FK_NOM_JUDET
         * @return builder
         */
        public InsertGeometrieDTOBuilder fkNomJudet(Long fkNomJudet) {
            this.fkNomJudet = fkNomJudet;
            return this;
        }

        /**
         * @param columnIsValoare valoare ce se insereaza in campul IS_FOLOSINTA sau IS_PRINCIPALA
         * @return builder
         */
        public InsertGeometrieDTOBuilder columnIsValoare(Integer columnIsValoare) {
            this.columnIsValoare = columnIsValoare;
            return this;
        }

        /**
         * @param columnIsName IS_FOLOSINTA sau IS_PRINCIPALA
         * @return builder
         */
        public InsertGeometrieDTOBuilder columnIsName(String columnIsName) {
            this.columnIsName = columnIsName;
            return this;
        }

        /**
         * @return obiectul contruit
         */
        public InsertGeometrieDTO build() {
            InsertGeometrieDTO insertGeometrieDTO = new InsertGeometrieDTO(this);
            //validari
            if (insertGeometrieDTO.getFk() == null) {
                throw new IllegalArgumentException("Fk nedefinit!");
            }
            if (StringUtils.isEmpty(insertGeometrieDTO.getGeometrie())) {
                throw new IllegalArgumentException("Geometrie nedefinit!");
            }
            if (StringUtils.isEmpty(insertGeometrieDTO.getTableName())) {
                throw new IllegalArgumentException("TableName nedefinit!");
            }
            if (StringUtils.isEmpty(insertGeometrieDTO.getColumnIdName())) {
                throw new IllegalArgumentException("ColumnIdName nedefinit!");
            }
            if (StringUtils.isEmpty(insertGeometrieDTO.getColumnFkName())) {
                throw new IllegalArgumentException("ColumnFkName nedefinit!");
            }
            if (insertGeometrieDTO.getFkNomJudet() == null) {
                throw new IllegalArgumentException("FkNomJudet nedefinit!");
            }
            //
            return insertGeometrieDTO;
        }


    }
}
