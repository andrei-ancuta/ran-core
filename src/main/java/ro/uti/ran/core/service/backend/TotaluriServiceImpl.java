package ro.uti.ran.core.service.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.registru.Nomenclator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dan on 19-Nov-15.
 */
@Service
@Transactional("registruTransactionManager")
public class TotaluriServiceImpl implements TotaluriService {

    private static Logger logger = LoggerFactory.getLogger(TotaluriServiceImpl.class);

    @PersistenceContext(unitName = "ran-registru")
    private EntityManager em;

    /**
     * SELECT DISTINCT parinte FROM CapCultura parinte,IN(parinte.capCultura) copil  WHERE copil.id IN (?1)
     * <p/>
     * Nomenclatoarele au o structura ierarhica;
     * - Se afla toti parintii (primul nivel) corespunzatori la copii trimisi
     * ca parametri in cadrul nomenclatorului configurat prin parametrii trimisi
     *
     * @param childrenIds           id-uri copii in cadrul structurii ierarhice a nomenclatorului
     * @param nomEntityName         ex: CapCultura nomenclator
     * @param nomFieldChildrensName ex: capCulturas din nomenclatorul CapCultura
     * @return lista de parinti
     */
    @Override
    public <T extends Nomenclator> List<T> getParentsForChildrenIds(Collection<Long> childrenIds, String nomEntityName, String nomFieldChildrensName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT parinte FROM ").append(nomEntityName).append(" parinte,IN(parinte.").append(nomFieldChildrensName).append(") copil  ");
        sql.append("WHERE copil.id IN (?1) ");
        sql.append("AND parinte.isFormula = 1");
        Query query = em.createQuery(sql.toString());
        query.setParameter(1, childrenIds);
        return query.getResultList();
    }

    /**
     * SELECT SUM(a1.capCultura.tipFormulaRelatie * a1.suprafata) FROM Cultura a1 WHERE a1.capCultura.capCultura.id = ?1
     * AND a1.gospodarie.idGospodarie = ?2 AND a1.an = ?3 AND a1.semestru = ?4
     *
     * @param idGospodarie               id gospodarie
     * @param an                         an raportare
     * @param semestru                   semestru raportare
     * @param nomIdParent                id parinte in cadrul structurii ierarhice a nomenclatorului
     * @param randEntityName             ex: Cultura entitate
     * @param randEntityFieldValoareName ex: suprafata din entitatea Cultura
     * @param randEntityFieldNomName     ex: capCultura din entitatea Cultura
     * @param nomFieldParentName         ex: capCultura din nomenclatorul CapCultura
     * @return ex: total suprafata pentru toti copii parintelui trimis ca parametru
     */
    @Override
    public Number getTotalForParent(Long idGospodarie, Integer an, Integer semestru, Long nomIdParent, String randEntityName, String randEntityFieldValoareName, String randEntityFieldNomName, String nomFieldParentName) {
        StringBuilder sql = new StringBuilder(buildSQL(randEntityName, randEntityFieldValoareName, randEntityFieldNomName, nomFieldParentName));
        sql.append("AND a1.an = ?3 ");
        sql.append("AND a1.semestru = ?4 ");
        Query query = em.createQuery(sql.toString());
        query.setParameter(1, nomIdParent);
        query.setParameter(2, idGospodarie);
        query.setParameter(3, an);
        query.setParameter(4, semestru);
        return (Number) query.getSingleResult();
    }

    /**
     * SELECT SUM(a1.capCultura.tipFormulaRelatie * a1.suprafata) FROM Cultura a1 WHERE a1.capCultura.capCultura.id = ?1
     * AND a1.gospodarie.idGospodarie = ?2  AND a1.an = ?3
     *
     * @param idGospodarie               id gospodarie
     * @param an                         an raportare
     * @param nomIdParent                id parinte in cadrul structurii ierarhice a nomenclatorului
     * @param randEntityName             ex: Cultura entitate
     * @param randEntityFieldValoareName ex: suprafata din entitatea Cultura
     * @param randEntityFieldNomName     ex: capCultura din entitatea Cultura
     * @param nomFieldParentName         ex: capCultura din nomenclatorul CapCultura
     * @return ex: total suprafata pentru toti copii parintelui trimis ca parametru
     */
    @Override
    public Number getTotalForParent(Long idGospodarie, Integer an, Long nomIdParent, String randEntityName, String randEntityFieldValoareName, String randEntityFieldNomName, String nomFieldParentName) {
        StringBuilder sql = new StringBuilder(buildSQL(randEntityName, randEntityFieldValoareName, randEntityFieldNomName, nomFieldParentName));
        sql.append("AND a1.an = ?3 ");
        Query query = em.createQuery(sql.toString());
        query.setParameter(1, nomIdParent);
        query.setParameter(2, idGospodarie);
        query.setParameter(3, an);
        return (Number) query.getSingleResult();
    }

    @Override
    public Number getTotalCentralizatorForParent(Integer codSiruta, Integer an, Long nomIdParent, String randEntityName, String randEntityFieldValoareName, String randEntityFieldNomName, String nomFieldParentName) {
        StringBuilder sql = new StringBuilder(buildSQLCentralizator(randEntityName, randEntityFieldValoareName, randEntityFieldNomName, nomFieldParentName));
        Query query = em.createQuery(sql.toString());
        query.setParameter(1, nomIdParent);
        query.setParameter(2, codSiruta);
        query.setParameter(3, an);
        return (Number) query.getSingleResult();
    }

    /**
     * SELECT SUM(a1.capCultura.tipFormulaRelatie * a1.suprafata) FROM Cultura a1 WHERE a1.capCultura.capCultura.id = ?1 AND a1.gospodarie.idGospodarie = ?2
     *
     * @param idGospodarie               id gospodarie
     * @param nomIdParent                id parinte in cadrul structurii ierarhice a nomenclatorului
     * @param randEntityName             ex: Cultura entitate
     * @param randEntityFieldValoareName ex: suprafata din entitatea Cultura
     * @param randEntityFieldNomName     ex: capCultura din entitatea Cultura
     * @param nomFieldParentName         ex: capCultura din nomenclatorul CapCultura
     * @return ex: total suprafata pentru toti copii parintelui trimis ca parametru
     */
    @Override
    public Number getTotalForParent(Long idGospodarie, Long nomIdParent, String randEntityName, String randEntityFieldValoareName, String randEntityFieldNomName, String nomFieldParentName) {
        StringBuilder sql = new StringBuilder(buildSQL(randEntityName, randEntityFieldValoareName, randEntityFieldNomName, nomFieldParentName));
        Query query = em.createQuery(sql.toString());
        query.setParameter(1, nomIdParent);
        query.setParameter(2, idGospodarie);
        return (Number) query.getSingleResult();
    }

    /**
     * @param randEntityName             ex: Cultura entitate
     * @param randEntityFieldValoareName ex: suprafata din entitatea Cultura
     * @param randEntityFieldNomName     ex: capCultura din entitatea Cultura
     * @param nomFieldParentName         ex: capCultura din nomenclatorul CapCultura
     * @return SQL - trunchiul comun
     */
    private String buildSQL(String randEntityName, String randEntityFieldValoareName, String randEntityFieldNomName, String nomFieldParentName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(").append("a1.").append(randEntityFieldNomName).append(".tipFormulaRelatie * ").append("a1.").append(randEntityFieldValoareName).append(") FROM ").append(randEntityName).append(" a1 ");
        sql.append("WHERE ").append("a1.").append(randEntityFieldNomName).append(".").append(nomFieldParentName).append(".id").append(" = ?1 ");
        sql.append("AND a1.gospodarie.idGospodarie = ?2 ");
        return sql.toString();
    }

    /**
     * @param randEntityName             ex: CulturaProd entitate
     * @param randEntityFieldValoareName ex: suprafata din entitatea CulturaProd
     * @param randEntityFieldNomName     ex: nomCultura din entitatea CulturaProd
     * @param nomFieldParentName         ex: nomCultura din nomenclatorul NomCultura
     * @return SQL - trunchiul comun
     */
    private String buildSQLCentralizator(String randEntityName, String randEntityFieldValoareName, String randEntityFieldNomName, String nomFieldParentName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(").append("a1.").append(randEntityFieldNomName).append(".tipFormulaRelatie * ").append("a1.").append(randEntityFieldValoareName).append(") FROM ").append(randEntityName).append(" a1 ");
        sql.append("WHERE ").append("a1.").append(randEntityFieldNomName).append(".").append(nomFieldParentName).append(".id").append(" = ?1 ");
        sql.append("AND a1.nomUat.codSiruta = ?2 ");
        sql.append("AND a1.an = ?3 ");
        return sql.toString();
    }

    private String buildSQLCentralizator13cent(String randEntityName, String randEntityFieldValoareName, String randEntityFieldNomName, String nomFieldParentName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(").append("a1.").append(randEntityFieldNomName).append(".tipFormulaRelatie * ").append("a1.").append(randEntityFieldValoareName).append(") FROM ").append(randEntityName).append(" a1 ");
        sql.append("WHERE ").append("a1.").append(randEntityFieldNomName).append(".").append(nomFieldParentName).append(".id").append(" = ?1 ");
        sql.append("AND a1.nomUat.codSiruta = ?2 ");
        sql.append("AND a1.an = ?3 ");
        return sql.toString();
    }

    /*SELECT SUM(a1.capAnimalProd.tipFormulaRelatie * a1.valoareProductie)
    FROM AnimalProd a1
    WHERE a1.capAnimalProd.capAnimalProd.id = ?1 AND a1.nomUat.codSiruta = ?2 AND a1.an = ?3*/
}
