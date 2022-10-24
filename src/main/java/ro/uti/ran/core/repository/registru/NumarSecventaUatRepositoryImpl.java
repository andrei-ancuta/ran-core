package ro.uti.ran.core.repository.registru;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.registru.NomUat;

import javax.persistence.*;

@Repository
public class NumarSecventaUatRepositoryImpl implements NumarSecventaUatRepository {

    @PersistenceContext(unitName = "ran-registru")
    private EntityManager em;

    @Override
    @Transactional("registruTransactionManager")
    public BigDecimal viewSecventaByBaseId(Long baseId) throws SQLException {
        BigDecimal value = getSecventaByBaseId(baseId);
        resetSecventa(baseId, value);
        return value.subtract(BigDecimal.valueOf(1L));
    }

    @Override
    @Transactional("registruTransactionManager")
    public long viewMaxNrCerereByUat(Long idUat) throws SQLException {
        Number value = null;
        try {
            value = (Number) em.createNativeQuery(
                    "SELECT REPLACE(titlu, 'Cerere corecție nr. ', '') " +
                            "  FROM   (SELECT   c.titlu, RANK() OVER(PARTITION BY g.fk_nom_uat ORDER BY id_cerere DESC) last_numar_cerere " +
                            "            FROM   cerere c INNER JOIN gospodarie g ON c.fk_gospodarie = g.id_gospodarie " +
                            "           WHERE   g.fk_nom_uat = ?) " +
                            " WHERE   last_numar_cerere = 1"
            ).setParameter(1, idUat).getSingleResult();
        } catch (Throwable t2) {
            //e.printStackTrace();
        }

        if (value == null) {
            return 0L;
        }

        return value.longValue();

    }

    @Override
    @Transactional("registruTransactionManager")
    public BigDecimal getSecventaByBaseId(Long baseId) throws SQLException {

        return (BigDecimal) em.createNativeQuery(
                "SELECT SEQ_CERERE_NUMAR_" + baseId + ".NEXTVAL FROM DUAL"
        ).setMaxResults(1)
                .getSingleResult();

    }

    @Override
    @Transactional("registruTransactionManager")
    public void resetSecventa(Long baseId, BigDecimal value) throws SQLException {

        BigDecimal currentValue = (BigDecimal) em.createNativeQuery(
                "SELECT SEQ_CERERE_NUMAR_" + baseId + ".NEXTVAL FROM DUAL"
        ).setMaxResults(1)
                .getSingleResult();

        currentValue = value.subtract(currentValue).subtract(BigDecimal.valueOf(1L));

        if (currentValue.compareTo(BigDecimal.valueOf(0L)) == 0) {
            return;
        }

        em.createNativeQuery("ALTER SEQUENCE SEQ_CERERE_NUMAR_" + baseId + " INCREMENT BY " + currentValue + " MINVALUE 0").executeUpdate();
        em.createNativeQuery("SELECT SEQ_CERERE_NUMAR_" + baseId + ".NEXTVAL FROM DUAL").getSingleResult();
        em.createNativeQuery("ALTER SEQUENCE SEQ_CERERE_NUMAR_" + baseId + " INCREMENT BY 1 MINVALUE 0").executeUpdate();


    }


}
