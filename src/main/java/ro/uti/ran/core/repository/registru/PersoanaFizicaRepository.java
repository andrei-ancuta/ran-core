package ro.uti.ran.core.repository.registru;

import ro.uti.ran.core.model.registru.PersoanaFizica;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Created by Dan on 19-Jan-16.
 */
public interface PersoanaFizicaRepository extends RanRepository<PersoanaFizica, Long> {

    List<PersoanaFizica> findByNumeAndPrenumeAndInitialaTataAndCnp(String nume, String prenume, String initialaTata, String cnp);

    List<PersoanaFizica> findByNumeAndPrenumeAndInitialaTataAndNif(String nume, String prenume, String initialaTata, String nif);
}
