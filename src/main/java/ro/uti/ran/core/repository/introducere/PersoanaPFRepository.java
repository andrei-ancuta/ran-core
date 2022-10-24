package ro.uti.ran.core.repository.introducere;

import ro.uti.ran.core.dto.ParametriiInterogarePF;
import ro.uti.ran.core.model.registru.PersoanaFizica;

public interface PersoanaPFRepository {
    Long findPersoanaFizicaFromIntroducere(ParametriiInterogarePF parametriiInterogare);
}
