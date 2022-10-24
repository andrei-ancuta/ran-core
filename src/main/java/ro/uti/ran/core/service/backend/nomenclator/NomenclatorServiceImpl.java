package ro.uti.ran.core.service.backend.nomenclator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.nomenclator.type.SqlConditionType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Anastasia cea micuta on 10/20/2015.
 */
@Service
@Transactional(value = "registruTransactionManager")
public class NomenclatorServiceImpl implements NomenclatorService {

    private static Logger logger = LoggerFactory.getLogger(NomenclatorServiceImpl.class);

    @PersistenceContext(unitName = "ran-registru")
    private EntityManager em;

    private Long getNomenclatorPrimaryKeyForStringParam(NomenclatorCodeType nomenclator, Object code, DataRaportareValabilitate dataRaportareValabilitate) {
        return getNomenclatorPrimaryKeyForStringParam(nomenclator, code, dataRaportareValabilitate, null);
    }

    private Long getNomenclatorPrimaryKeyForStringParam(NomenclatorCodeType nomenclator, Object code, DataRaportareValabilitate dataRaportareValabilitate, String codCapitol) {
        Date effectiveTime = dataRaportareValabilitate.getDataRaportareValabilitate();
        StringBuilder sb = new StringBuilder(200);
        sb.append("select ID_")
                .append(nomenclator.getTableName())
                .append(" from ")
                .append(nomenclator.getTableName())
                .append(" where ")
                .append(nomenclator.getCodeColumn()).append(" = ? ");
        if (nomenclator.getConditionType().equals(SqlConditionType.WITH_DATE_BETWEEN) ||
                nomenclator.getConditionType().equals(SqlConditionType.WITH_PARENT_AND_DATE_BETWEEN)
                || nomenclator.getConditionType().equals(SqlConditionType.COMPLEX)) {
            sb.append(" and to_date(?, 'DD-MM-YYYY HH24:MI:SS') between data_start and nvl(data_stop, to_date(?, 'DD-MM-YYYY HH24:MI:SS'))");
        }
        if (codCapitol != null) {
            sb.append(" and FK_NOM_CAPITOL = (SELECT a1.ID_NOM_CAPITOL FROM NOM_CAPITOL a1 WHERE a1.COD = ?)");
        }
        String sql = sb.toString();
        Query query = em.createNativeQuery(sql);

        if (code instanceof String) {
            String oldCode = (String) code;
            try {
                code = URLDecoder.decode((String) code, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                code = oldCode;
            }
        } else if (code instanceof Number || code instanceof Date) {
            // to nothing (just asure to be a desire type)
        } else {
            code = code.toString();
        }
        query.setParameter(1, code);
        String params = "code: " + code;
        if (nomenclator.getConditionType().equals(SqlConditionType.WITH_DATE_BETWEEN)  ||
                nomenclator.getConditionType().equals(SqlConditionType.WITH_PARENT_AND_DATE_BETWEEN)
                || nomenclator.getConditionType().equals(SqlConditionType.COMPLEX)) {
            //first check for obs date
            String date = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(effectiveTime);
            query.setParameter(2, date);
            query.setParameter(3, date);
            params = params.concat(", effectiveTime: " + date);
            if (codCapitol != null) {
                query.setParameter(4, codCapitol);
                params = params.concat(", codCapitol: " + codCapitol);
            }

        } else {
            if (codCapitol != null) {
                query.setParameter(2, codCapitol);
                params = params.concat(", codCapitol: " + codCapitol);
            }
        }

        List res = query.getResultList();
        if (res == null || res.size() == 0) {
            logger.error("nomenclator query: '".concat(sql + "'; params: " + params.toString() + "; should return exactly one value!! Actually returns: " + (res == null || res.isEmpty() ? 0 : res.size())));
            return null;
        } else if (res.isEmpty() || res.size() > 1) {
            throw new RuntimeException("nomenclator query: '".concat(sql
                    + "'; params: " + params + "; should return exactly one value!! Actually returns: " + (res == null || res.isEmpty() ? 0 : res.size())));
        }

        Object id = res.get(0);
        if (id == null) {
            return null;
        } else if (id instanceof Number) {
            return ((Number) id).longValue();
        } else {
            throw new IllegalStateException("Invalid long data type (" + id + ")");
        }
    }

    @Override
    @Cacheable(value = "nomenclatoare", key = "#nomenclator?.getName() + #code?.toString() + #dataRaportareValabilitate?.hashCode() + #codCapitol")
    public Nomenclator getNomenclatorForStringParam(NomenclatorCodeType nomenclator, Object code, DataRaportareValabilitate dataRaportareValabilitate, String codCapitol) {
        Long id = getNomenclatorPrimaryKeyForStringParam(nomenclator, code, dataRaportareValabilitate, codCapitol);
        return null != id ? em.find(nomenclator.getNomType().getClazz(), id) : null;
    }

    @Override
    @Cacheable(value = "nomenclatoare", key = "#nomenclator?.getName() + #code?.toString() + #dataRaportareValabilitate?.hashCode()")
    public Nomenclator getNomenclatorForStringParam(NomenclatorCodeType nomenclator, Object code, DataRaportareValabilitate dataRaportareValabilitate) {

        Long id = getNomenclatorPrimaryKeyForStringParam(nomenclator, code, dataRaportareValabilitate);
        return null != id ? em.find(nomenclator.getNomType().getClazz(), id) : null;
    }


    @Override
    @Cacheable(value = "nomenclatoare" ,key="#nomenclator?.getName() + #cod + #codRand + #codCapitol + #dataRaportareValabilitate?.hashCode()")
    public <T extends Nomenclator> T findCapByCodAndCodRandAndCapitolAndDataValabilitate(NomenclatorCodeType nomenclator, String cod, Integer codRand, String codCapitol,DataRaportareValabilitate dataRaportareValabilitate) {
        Date effectiveTime = dataRaportareValabilitate.getDataRaportareValabilitate();
        StringBuilder sb = new StringBuilder(200);
        sb.append("select a1.ID_" + nomenclator.getTableName() + " from " + nomenclator.getTableName() + " a1 ")
                .append(" INNER JOIN NOM_CAPITOL a2 ON a1.FK_NOM_CAPITOL = a2.ID_NOM_CAPITOL ")
                .append(" where ")
                .append(" a1.COD = ? ")
                .append(" AND a1.COD_RAND = ? ")
                .append(" AND a2.COD = ? ")
                .append(" AND to_date(?, 'DD-MM-YYYY HH24:MI:SS') between a1.data_start and nvl(a1.data_stop, to_date(?, 'DD-MM-YYYY HH24:MI:SS'))");
        String sql = sb.toString();
        Query query = em.createNativeQuery(sql);
        StringBuilder params = new StringBuilder();
        query.setParameter(1, cod);
        params.append("cod:" + cod);
        query.setParameter(2, codRand);
        params.append(", codRand:" + codRand);
        query.setParameter(3, codCapitol);
        params.append(", codCapitol:" + codCapitol);
        String date = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(effectiveTime);
        params.append(", effectiveTime:" + date);
        query.setParameter(4, date);
        query.setParameter(5, date);
        List res = query.getResultList();
        if (res == null || res.isEmpty()) {
            logger.error("nomenclator query: '".concat(sql + "'; params: " + params.toString() + "; should return exactly one value!! Actually returns: " + (res == null || res.isEmpty() ? 0 : res.size())));
            return null;
        } else if (res.isEmpty() || res.size() > 1) {
            throw new RuntimeException("nomenclator query: '".concat(sql + "'; params: " + params + "; should return exactly one value!! Actually returns: " + (res.isEmpty() ? 0 : res.size())));
        }

        Object id = res.get(0);
        if (id == null) {
            return null;
        } else if (id instanceof Number) {
            Long idCap = ((Number) id).longValue();
            return (T) em.find(nomenclator.getNomType().getClazz(), idCap);
        } else {
            throw new IllegalStateException("Invalid long data type (" + id + ")");
        }
    }

    @Override
    @Cacheable(value = "nomenclatoare", key = "#nomenclator?.getName() + #code?.toString()")
    public Nomenclator getNomenclatorForStringParam(NomenclatorCodeType nomenclator, Object code) {
        Long id = getNomenclatorPrimaryKeyForStringParam(nomenclator, code, new DataRaportareValabilitate());
        return null != id ? em.find(nomenclator.getNomType().getClazz(), id) : null;
    }

    @Override
    @Cacheable(value = "nomenclatoare" ,key="#nomenclator?.getName() + #id?.hashCode()")
    public Nomenclator getNomenlatorForId(NomenclatorCodeType nomenclator, Long id) {
        return id != null ? em.find(nomenclator.getNomType().getClazz(), id) : null;
    }

}
