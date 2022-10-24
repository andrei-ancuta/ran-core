package ro.uti.ran.core.service.backend;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.service.backend.dto.InsertGeometrieDTO;
import ro.uti.ran.core.service.parametru.ParametruService;
import ro.uti.ran.core.utils.GmlValidator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Clob;

/**
 * Created by Dan on 17-Nov-15.
 */
@Service
@Transactional("registruTransactionManager")
public class GeometrieServiceImpl implements GeometrieService {

    private static Logger logger = LoggerFactory.getLogger(GeometrieServiceImpl.class);

    @PersistenceContext(unitName = "ran-registru")
    private EntityManager em;

    @Autowired
    private Environment env;

    @Autowired
    private ParametruService parametruService;

    /**
     * salveaza in baza de date geometria adresei
     *
     * @param idAdresa     id adresa din baza de date
     * @param geometrieGML geometria in format GML
     * @param fkNomJudet   id judet
     */
    @Override
    public void insertAdresaGIS(Long idAdresa, String geometrieGML, Long fkNomJudet) {
        if (idAdresa == null) {
            throw new IllegalArgumentException("idAdresa nedefinit!");
        }
        if (StringUtils.isEmpty(geometrieGML)) {
            throw new IllegalArgumentException("geometrieGML nedefinit!");
        }
        InsertGeometrieDTO insertGeometrieDTO = new InsertGeometrieDTO.InsertGeometrieDTOBuilder()
                .fk(idAdresa)
                .geometrie(geometrieGML)
                .tableName("GEOLOCATOR_ADRESA")
                .columnIdName("ID_GEOLOCATOR_ADRESA")
                .columnFkName("FK_ADRESA")
                .fkNomJudet(fkNomJudet)
                .build();
        insertGeometrie(insertGeometrieDTO, false);
    }

    /**
     * @param idGeolocatorAdresa id GIS din baza de date
     * @return geometria in format GML a adresei
     */
    @Override
    public String getAdresaGIS(Long idGeolocatorAdresa) {
        if (idGeolocatorAdresa == null) {
            throw new IllegalArgumentException("idGeolocatorAdresa nedefinit!");
        }
        return selectGeometrie(idGeolocatorAdresa, "GEOLOCATOR_ADRESA", "ID_GEOLOCATOR_ADRESA");
    }

    /**
     * salveaza in baza de date geometria parcelei de teren
     *
     * @param idParcelaTeren id parcela teren din baza de date
     * @param geometrieGML   geometria in format GML
     * @param fkNomJudet     id judet
     */
    @Override
    public void insertParcelaTerenGIS(Long idParcelaTeren, String geometrieGML, Long fkNomJudet, Integer isFolosinta) {
        if (idParcelaTeren == null) {
            throw new IllegalArgumentException("idParcelaTeren nedefinit!");
        }
        if (StringUtils.isEmpty(geometrieGML)) {
            throw new IllegalArgumentException("geometrieGML nedefinit!");
        }
        InsertGeometrieDTO insertGeometrieDTO = new InsertGeometrieDTO.InsertGeometrieDTOBuilder()
                .fk(idParcelaTeren)
                .geometrie(geometrieGML)
                .tableName("GEOMETRIE_PARCELA_TEREN")
                .columnIdName("ID_GEOMETRIE_PARCELA_TEREN")
                .columnFkName("FK_PARCELA_TEREN")
                .columnIsName("IS_FOLOSINTA")
                .columnIsValoare(isFolosinta)
                .fkNomJudet(fkNomJudet)
                .build();
        insertGeometrie(insertGeometrieDTO, true);
    }

    /**
     * @param idGeometrieParcelaTeren id GIS din baza de date
     * @return geometria in format GML a parcelei de teren
     */
    @Override
    public String getParcelaTerenGIS(Long idGeometrieParcelaTeren) {
        if (idGeometrieParcelaTeren == null) {
            throw new IllegalArgumentException("idGeometrieParcelaTeren nedefinit!");
        }
        return selectGeometrie(idGeometrieParcelaTeren, "GEOMETRIE_PARCELA_TEREN", "ID_GEOMETRIE_PARCELA_TEREN");
    }

    /**
     * salveaza in baza de date geometria cladirii
     *
     * @param idCladire    id cladire din baza de date
     * @param geometrieGML geometria in format GML
     * @param fkNomJudet   id judet
     */
    @Override
    public void insertCladireGIS(Long idCladire, String geometrieGML, Long fkNomJudet) {
        if (idCladire == null) {
            throw new IllegalArgumentException("idCladire nedefinit!");
        }
        if (StringUtils.isEmpty(geometrieGML)) {
            throw new IllegalArgumentException("geometrieGML nedefinit!");
        }
        InsertGeometrieDTO insertGeometrieDTO = new InsertGeometrieDTO.InsertGeometrieDTOBuilder()
                .fk(idCladire)
                .geometrie(geometrieGML)
                .tableName("GEOMETRIE_CLADIRE")
                .columnIdName("ID_GEOMETRIE_CLADIRE")
                .columnFkName("FK_CLADIRE")
                .fkNomJudet(fkNomJudet)
                .build();
        insertGeometrie(insertGeometrieDTO, false);
    }

    /**
     * @param idGeometrieCladire id GIS din baza de date
     * @return geometria in format GML a cladirii
     */
    @Override
    public String getCladireGIS(Long idGeometrieCladire) {
        if (idGeometrieCladire == null) {
            throw new IllegalArgumentException("idGeometrieCladire nedefinit!");
        }
        return selectGeometrie(idGeometrieCladire, "GEOMETRIE_CLADIRE", "ID_GEOMETRIE_CLADIRE");
    }

    @Override
    public void insertSuprafataUtilizGIS(Long idSuprafataUtilizare, String geometrieGML, Long fkNomJudet) {
        if (idSuprafataUtilizare == null) {
            throw new IllegalArgumentException("idSuprafataUtilizare nedefinit!");
        }
        if (StringUtils.isEmpty(geometrieGML)) {
            throw new IllegalArgumentException("geometrieGML nedefinit!");
        }
        InsertGeometrieDTO insertGeometrieDTO = new InsertGeometrieDTO.InsertGeometrieDTOBuilder()
                .fk(idSuprafataUtilizare)
                .geometrie(geometrieGML)
                .tableName("GEOMETRIE_SUPRAFATA_UTILIZ")
                .columnIdName("ID_GEOMETRIE_SUPRAFATA_UTILIZ")
                .columnFkName("FK_SUPRAFATA_UTILIZARE")
                .fkNomJudet(fkNomJudet)
                .build();
        insertGeometrie(insertGeometrieDTO, false);
    }

    @Override
    public String getSuprafataUtilizGIS(Long idGeometrieSuprafataUtiliz) {
        if (idGeometrieSuprafataUtiliz == null) {
            throw new IllegalArgumentException("idGeometrieSuprafataUtiliz nedefinit!");
        }
        return selectGeometrie(idGeometrieSuprafataUtiliz, "GEOMETRIE_SUPRAFATA_UTILIZ", "ID_GEOMETRIE_SUPRAFATA_UTILIZ");
    }

    @Override
    public void insertCulturaGIS(Long idCultura, String geometrieGML, Long fkNomJudet, Integer isPrincipala) {
        if (idCultura == null) {
            throw new IllegalArgumentException("idCultura nedefinit!");
        }
        if (StringUtils.isEmpty(geometrieGML)) {
            throw new IllegalArgumentException("geometrieGML nedefinit!");
        }
        InsertGeometrieDTO insertGeometrieDTO = new InsertGeometrieDTO.InsertGeometrieDTOBuilder()
                .fk(idCultura)
                .geometrie(geometrieGML)
                .tableName("GEOMETRIE_CULTURA")
                .columnIdName("ID_GEOMETRIE_CULTURA")
                .columnFkName("FK_CULTURA")
                .fkNomJudet(fkNomJudet)
                .columnIsName("IS_PRINCIPALA")
                .columnIsValoare(isPrincipala)
                .build();
        insertGeometrie(insertGeometrieDTO, true);
    }

    @Override
    public String getCulturaGIS(Long idGeometrieCultura) {
        if (idGeometrieCultura == null) {
            throw new IllegalArgumentException("idGeometrieCultura nedefinit!");
        }
        return selectGeometrie(idGeometrieCultura, "GEOMETRIE_CULTURA", "ID_GEOMETRIE_CULTURA");

    }

    @Override
    public void insertPlantatieGIS(Long idPlantatie, String geometrieGML, Long fkNomJudet) {
        if (idPlantatie == null) {
            throw new IllegalArgumentException("idPlantatie nedefinit!");
        }
        if (StringUtils.isEmpty(geometrieGML)) {
            throw new IllegalArgumentException("geometrieGML nedefinit!");
        }
        InsertGeometrieDTO insertGeometrieDTO = new InsertGeometrieDTO.InsertGeometrieDTOBuilder()
                .fk(idPlantatie)
                .geometrie(geometrieGML)
                .tableName("GEOMETRIE_PLANTATIE")
                .columnIdName("ID_GEOMETRIE_PLANTATIE")
                .columnFkName("FK_PLANTATIE")
                .fkNomJudet(fkNomJudet)
                .build();
        insertGeometrie(insertGeometrieDTO, false);
    }

    @Override
    public String getPlantatieGIS(Long idGeometriePlantatie) {

        if (idGeometriePlantatie == null) {
            throw new IllegalArgumentException("idGeometriePlantatie nedefinit!");
        }
        return selectGeometrie(idGeometriePlantatie, "GEOMETRIE_PLANTATIE", "ID_GEOMETRIE_PLANTATIE");


    }

    @Override
    public void insertAplicareIngrasamantGIS(Long idAplicareIngrasamant, String geometrieGML, Long fkNomJudet) {
        if (idAplicareIngrasamant == null) {
            throw new IllegalArgumentException("idAplicareIngrasamant nedefinit!");
        }
        if (StringUtils.isEmpty(geometrieGML)) {
            throw new IllegalArgumentException("geometrieGML nedefinit!");
        }
        InsertGeometrieDTO insertGeometrieDTO = new InsertGeometrieDTO.InsertGeometrieDTOBuilder()
                .fk(idAplicareIngrasamant)
                .geometrie(geometrieGML)
                .tableName("GEOMETRIE_APLICARE_INGRAS")
                .columnIdName("ID_GEOMETRIE_APLICARE_INGRAS")
                .columnFkName("FK_APLICARE_INGRASAMANT")
                .fkNomJudet(fkNomJudet)
                .build();
        insertGeometrie(insertGeometrieDTO, false);
    }

    @Override
    public String getAplicareIngrasamantGIS(Long idGeometrieAplicareIngras) {
        if (idGeometrieAplicareIngras == null) {
            throw new IllegalArgumentException("idGeometrieAplicareIngras nedefinit!");
        }
        return selectGeometrie(idGeometrieAplicareIngras, "GEOMETRIE_APLICARE_INGRAS", "ID_GEOMETRIE_APLICARE_INGRAS");
    }

    @Override
    public void insertTerenIrigatGIS(Long idTerenIrigat, String geometrieGML, Long fkNomJudet) {
        if (idTerenIrigat == null) {
            throw new IllegalArgumentException("idTerenIrigat nedefinit!");
        }
        if (StringUtils.isEmpty(geometrieGML)) {
            throw new IllegalArgumentException("geometrieGML nedefinit!");
        }
        InsertGeometrieDTO insertGeometrieDTO = new InsertGeometrieDTO.InsertGeometrieDTOBuilder()
                .fk(idTerenIrigat)
                .geometrie(geometrieGML)
                .tableName("GEOMETRIE_TEREN_IRIGAT")
                .columnIdName("ID_GEOMETRIE_TEREN_IRIGAT")
                .columnFkName("FK_TEREN_IRIGAT")
                .fkNomJudet(fkNomJudet)
                .build();
        insertGeometrie(insertGeometrieDTO, false);

    }

    @Override
    public String getTerenIrigatGIS(Long idGeometrieTerenIrigat) {
        if (idGeometrieTerenIrigat == null) {
            throw new IllegalArgumentException("idGeometrieTerenIrigat nedefinit!");
        }
        return selectGeometrie(idGeometrieTerenIrigat, "GEOMETRIE_TEREN_IRIGAT", "ID_GEOMETRIE_TEREN_IRIGAT");
    }

    /**
     * @param insertGeometrieDTO @see InsertGeometrieDTO
     */
    private void insertGeometrie(InsertGeometrieDTO insertGeometrieDTO, boolean esteCampIS) {
        String geometrieGML = StringUtils.trim(insertGeometrieDTO.getGeometrie());
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("insert into ").append(insertGeometrieDTO.getTableName());
            sql.append("(");
            sql.append(insertGeometrieDTO.getColumnIdName().toUpperCase()).append(",");
            sql.append(insertGeometrieDTO.getColumnFkName().toUpperCase()).append(",");
            sql.append("GEOMETRIE").append(",");
            sql.append("FK_NOM_JUDET");
            if (esteCampIS) {
                sql.append(",").append(insertGeometrieDTO.getColumnIsName().toUpperCase());
            }
            sql.append(") ");
            sql.append("values ");
            sql.append("(");
            sql.append("SEQ_").append(insertGeometrieDTO.getTableName().toUpperCase()).append(".nextval, ");
            sql.append("?,");
            sql.append("SDO_UTIL.FROM_GML311GEOMETRY(?),");
            sql.append("?");
            if (esteCampIS) {
                sql.append(",?");
            }
            sql.append(")");
            /*JPA native SQL*/
            Query query = em.createNativeQuery(sql.toString())
                    .setParameter(1, insertGeometrieDTO.getFk())
                    .setParameter(2, geometrieGML)
                    .setParameter(3, insertGeometrieDTO.getFkNomJudet());
            if (esteCampIS) {
                query.setParameter(4, insertGeometrieDTO.getColumnIsValoare());
            }
            int tmp = query.executeUpdate();
            logger.debug("insertGeometrie return value = " + tmp);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void validateGeometrie(String gml311String) throws DateRegistruValidationException {
        String geometrieGML = StringUtils.trim(gml311String);
        try {
            String sqlString = "select SDO_GEOM.VALIDATE_GEOMETRY(SDO_UTIL.FROM_GML311GEOMETRY(?), 0.05) from dual";

            /*JPA native SQL*/
            String ret = (String) em.createNativeQuery(sqlString.toString())
                    .setParameter(1, geometrieGML)
                    .getSingleResult();
            if (!Boolean.TRUE.toString().equalsIgnoreCase(ret)) {
                logger.debug("geometry is invalid. Reason: " + ret);

                String errMsg = "ORA-" + ret;
                for (GEOM_VALIDATION_CODES errCode : GEOM_VALIDATION_CODES.values()) {
                    if (errCode.name().contains(ret)) {
                        errMsg = errCode.getDescription();
                    }
                }

                throw new RuntimeException(errMsg);
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DateRegistruValidationException(DateRegistruValidationCodes.EROARE_CONSISTENTA_GEOMETRIE, e.getMessage());
        }
    }

    @Override
    public void validateGeometriePunctUatLimit(int sirutaUat, String gml311String) throws DateRegistruValidationException {
        validateGeometrieUatLimit(sirutaUat, gml311String, 1);
    }

    @Override
    public void validateGeometriePoligonUatLimit(int sirutaUat, String gml311String) throws DateRegistruValidationException {
        validateGeometrieUatLimit(sirutaUat, gml311String, 3);
    }

    private void validateGeometrieUatLimit(int sirutaUat, String gml311String, Integer gType) throws DateRegistruValidationException {
        //
        Parametru parametru = parametruService.getParametru(ParametruService.ACTIVEAZA_VALIDARE_CU_FUNCTII_SPATIALE_COD_PARAM);
        Boolean activeazaValidareCuFunctiiSpatiale = Boolean.valueOf(parametru.getValoare());
        parametru = parametruService.getParametru(ParametruService.RAN_REFERENTIAL_EPSG);
        Number ranReferentialEpsg = Integer.valueOf(parametru.getValoare());
        //
        if (activeazaValidareCuFunctiiSpatiale) {
            String geometrieGML = StringUtils.trim(gml311String);
            String sqlString = "SELECT PKG_GEOMETRY_VALIDARE.f_validare_limita_uat(?, ?, ?, ?) FROM DUAL";
            try {
                 /*JPA native SQL*/
                String rezultatValidare = (String) em.createNativeQuery(sqlString)
                        .setParameter(1, geometrieGML)
                        .setParameter(2, sirutaUat)
                        .setParameter(3, gType)
                        .setParameter(4, ranReferentialEpsg)
                        .getSingleResult();
                if ("UAT_ERROR".equalsIgnoreCase(rezultatValidare)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.EROARE_VALIDARE_GEOMETRIE_UAT_LIMIT, sirutaUat);
                }
                if ("TYPE_ERROR".equalsIgnoreCase(rezultatValidare)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.EROARE_VALIDARE_GEOMETRIE_TYPE, 1 == gType ? "punct" : "poligon");
                }
                if ("EPSG_ERROR".equalsIgnoreCase(rezultatValidare)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.EROARE_VALIDARE_GEOMETRIE_EPSG, ranReferentialEpsg);
                }
                if ("CONFIG_ERROR".equalsIgnoreCase(rezultatValidare)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.EROARE_VALIDARE_GEOMETRIE_CONFIG);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new DateRegistruValidationException(DateRegistruValidationCodes.EROARE_CONSISTENTA_GEOMETRIE, e.getMessage());
            }
        } else {
            String uatLayerUrl = env.getProperty("url.uat");
            String checkGml = GmlValidator.ValidateGeometry(uatLayerUrl, sirutaUat, gml311String);
            if (!checkGml.equals("Ok")) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.EROARE_VALIDARE_GEOMETRIE_UAT_LIMIT, sirutaUat);
            }
        }
    }

    /**
     * @param id           id din DB
     * @param tableName    denumire tabel din DB
     * @param columnIdName denumire coloana id din DB
     * @return geometria in format GML sau null
     */
    private String selectGeometrie(Long id, String tableName, String columnIdName) {
        java.sql.Clob dbObject = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select SDO_UTIL.TO_GML311GEOMETRY(geometrie) from ");
            sql.append(tableName).append(" ");
            sql.append("WHERE ");
            sql.append(columnIdName).append(" = ?");
            /*JPA native SQL*/
            Query query = em.createNativeQuery(sql.toString());
            query.setParameter(1, id);
            dbObject = (Clob) query.getSingleResult();
            /*read Clob*/
            if (dbObject != null && dbObject.length() > 0) {
                return IOUtils.toString(dbObject.getAsciiStream(), "UTF-8");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return null;
    }

    private enum GEOM_VALIDATION_CODES {
        ORA13348("polygon boundary is not closed. Thus you need to make sure first vertex and last vertex are the same in your polygon definition"),
        ORA13349("polygon boundary crosses itself. You need to make sure no edges of the polygon intersect. In your case Edge# 5247 and Edge# 5252 intersect"),
        ORA13367("means wrong orientation for interior/exterior rings. Thus, you need to make sure the exterior rings are oriented counterclockwise and the interior rings are oriented clockwise."),
        ORA13011("A specified dimension value is outside the range defined for that dimension");

        private final String description;

        GEOM_VALIDATION_CODES(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
