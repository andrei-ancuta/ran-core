package ro.uti.ran.core.repository.portal;

import ro.uti.ran.core.model.portal.Sistem;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 15:35
 */
public interface SistemRepository extends RanRepository<Sistem, Long> {

    /*Sistem findByNumeUtilizator(String numeUtilizator);

    Sistem findByNumeUtilizatorAndParola(String numeUtilizator, String parola);*/

    Sistem findByUat_Id(Long idUat);

    Sistem findByInstitutie_Id(Long idInstitutie);

    Sistem findByJudet_Id(Long idJudet);
}
