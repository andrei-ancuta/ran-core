package ro.uti.ran.core.service.backend.nomenclator;

import ro.uti.ran.core.model.registru.PersoanaFizica;
import ro.uti.ran.core.model.registru.PersoanaRc;

/**
 * Created by Dan on 19-Jan-16.
 */
public interface ReutilizareService {

    Long reutilizarePersoanaFizica(PersoanaFizica persoanaFizica, Long idNomUat);

    Long reutilizarePersoanaRc(PersoanaRc persoanaRc, Long idNomFormaOrganizareRc, Long idNomUat);
}
