package ro.uti.ran.core.repository.introducere;

import ro.uti.ran.core.dto.ParametriiInterogarePJ;

public interface PersoanaPJRepository {

    Long findPersoanaRcFromIntroducere(ParametriiInterogarePJ parametriiInterogare);
}
