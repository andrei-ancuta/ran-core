package ro.uti.ran.core.repository.introducere;

import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import ro.uti.ran.core.dto.ParametriiInterogarePF;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;

@Repository
public class PersoanaPFRepositoryImpl implements PersoanaPFRepository {

    @PersistenceContext(unitName = "ran-registru")
    private EntityManager em;


    @Override
    public Long findPersoanaFizicaFromIntroducere(ParametriiInterogarePF parametriiInterogare) {
        StringBuilder sql = new StringBuilder("SELECT a.ID_PERSOANA_FIZICA FROM PERSOANA_FIZICA a ");
        sql.append("WHERE ");
        sql.append("UPPER(a.NUME) = ?1 AND UPPER(a.PRENUME) = ?2 AND UPPER(a.INITIALA_TATA) = ?3 AND a.FK_NOM_UAT = ?4 ");
        if (StringUtils.isNotEmpty(parametriiInterogare.getCnp())) {
            sql.append("AND UPPER(a.CNP) = ?5 ");
        } else {
            sql.append("AND UPPER(a.NIF) = ?5 ");
        }
        Query q = em.createNativeQuery(sql.toString());
        q.setParameter(1, parametriiInterogare.getNume().toUpperCase());
        q.setParameter(2, parametriiInterogare.getPrenume().toUpperCase());
        q.setParameter(3, parametriiInterogare.getInitialaTata().toUpperCase());
        q.setParameter(4, parametriiInterogare.getIdNomUat());
        if (StringUtils.isNotEmpty(parametriiInterogare.getCnp())) {
            q.setParameter(5, parametriiInterogare.getCnp().toUpperCase());
        } else {
            q.setParameter(5, parametriiInterogare.getNif().toUpperCase());
        }
        return ((BigDecimal)q.getSingleResult()).longValue();
    }
}
