package ro.uti.ran.core.service.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.repository.registru.GospodarieRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;

/**
 * Created by Dan on 03-Feb-16.
 */
@Service
@Transactional("registruTransactionManager")
public class GospodarieServiceImpl implements GospodarieService {

    @Autowired
    private GospodarieRepository gospodarieRepository;

    @PersistenceContext(unitName = "ran-registru")
    private EntityManager em;

    @Override
    public boolean isGospodarieActiva(Integer codSirutaUAT, String identificatorGospodarie) {
        Gospodarie gospodarie = gospodarieRepository.findByUatAndIdentificator(codSirutaUAT, identificatorGospodarie);
        if (gospodarie != null) {
            return (RanConstants.GOSPODARIE_IS_ACTIV_DA.equals(gospodarie.getIsActiv()));
        }
        return true;
    }

    @Override
    public boolean isGospodarie(Integer codSirutaUAT, String identificatorGospodarie) {
        return (gospodarieRepository.findByUatAndIdentificator(codSirutaUAT, identificatorGospodarie) != null);
    }


    @Override
    public Gospodarie getByUatAndIdentificator(Integer codSirutaUAT, String identificatorGospodarie) {
        return gospodarieRepository.findByUatAndIdentificator(codSirutaUAT, identificatorGospodarie);
    }

    @Override
    public boolean isCapitolGospodarie(Long id) {
        Query query = em.createNativeQuery("select pkg_gospodarie.is_capitol_gospodarie(?1) from dual");
        query.setParameter(1, id);
        BigDecimal tmp = (BigDecimal) query.getSingleResult();
        return 0 != tmp.intValue();
    }


}
