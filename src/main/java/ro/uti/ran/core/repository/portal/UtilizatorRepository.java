package ro.uti.ran.core.repository.portal;

import ro.uti.ran.core.model.portal.Utilizator;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 15:35
 */
public interface UtilizatorRepository extends RanRepository<Utilizator, Long> {
	
    Utilizator findByNumeUtilizatorIgnoreCase(String numeUtilizator);

    Utilizator findByTokenUtilizator(String tokenUtilizator);
}
