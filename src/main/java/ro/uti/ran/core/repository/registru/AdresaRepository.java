package ro.uti.ran.core.repository.registru;

import ro.uti.ran.core.model.registru.Adresa;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Created by Dan on 16-Oct-15.
 */
public interface AdresaRepository extends RanRepository<Adresa, Long> {

    List<Adresa> findByUidRenns(String uidRenns);
}
