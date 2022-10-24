package ro.uti.ran.core.repository.registru;

import ro.uti.ran.core.model.registru.PersoanaRc;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Created by Dan on 19-Jan-16.
 */
public interface PersoanaRcRepository extends RanRepository<PersoanaRc, Long> {

    List<PersoanaRc> findByCuiAndDenumireAndNomFormaOrganizareRcId(String cui, String denumire, Long id);

    List<PersoanaRc> findByCuiAndCifAndDenumireAndNomFormaOrganizareRcId(String cui, String cif, String denumire, Long id);
}
