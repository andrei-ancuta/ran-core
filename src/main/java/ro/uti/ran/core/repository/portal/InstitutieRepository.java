package ro.uti.ran.core.repository.portal;

import ro.uti.ran.core.model.portal.Institutie;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-14 10:57
 */
public interface InstitutieRepository extends RanRepository<Institutie, Long> {

    Institutie findByCod(String cod);

    List<Institutie> findByFkNomTipInstitutie(Long fkNomTipInstitutie);
}
