package ro.uti.ran.core.repository.introducere;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import ro.uti.ran.core.dto.ParametriiInterogarePJ;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;

@Repository
public class PersoanaPJRepositoryImpl implements PersoanaPJRepository {

    @PersistenceContext(unitName = "ran-registru")
    private EntityManager em;

    @Override
    public Long findPersoanaRcFromIntroducere(ParametriiInterogarePJ parametriiInterogare) {
        StringBuilder sql = new StringBuilder("SELECT a.ID_PERSOANA_RC FROM PERSOANA_RC a ");
        sql.append("WHERE ");
        sql.append("UPPER(a.DENUMIRE) = ?1 AND UPPER(a.CUI) = ?2 AND a.FK_NOM_UAT = ?3 ");
        if (StringUtils.isNotEmpty(parametriiInterogare.getCif())) {
            sql.append("AND UPPER(a.CIF) = ?4 ");
        }
        Query q = em.createNativeQuery(sql.toString());
        q.setParameter(1, parametriiInterogare.getDenumire().toUpperCase());
        q.setParameter(2, parametriiInterogare.getCui().toUpperCase());
        q.setParameter(3, parametriiInterogare.getIdNomUat());
        if (StringUtils.isNotEmpty(parametriiInterogare.getCif())) {
            q.setParameter(4, parametriiInterogare.getCif().toUpperCase());
        }
        return ((BigDecimal) q.getSingleResult()).longValue();
    }
}
