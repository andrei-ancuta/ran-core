package ro.uti.ran.core.service.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.portal.Institutie;
import ro.uti.ran.core.repository.portal.InstitutieRepository;

import java.util.List;

/**
 * Created by Dan on 14-Mar-16.
 */
@Component
@Transactional("portalTransactionManager")
public class NomInstitutieServiceImpl implements NomInstitutieService {

    @Autowired
    InstitutieRepository institutieRepository;

    @Override
    public Institutie getInstitutieByCod(String codInstitutie) {
        return institutieRepository.findByCod(codInstitutie);
    }

    @Override
    public List<Institutie> getInstitutieByTipInstitutie(Long fkNomTipInstitutie) {
        return institutieRepository.findByFkNomTipInstitutie(fkNomTipInstitutie);
    }
}
