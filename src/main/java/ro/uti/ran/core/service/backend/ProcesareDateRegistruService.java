package ro.uti.ran.core.service.backend;

import ro.uti.ran.core.exception.base.RanBusinessException;

/**
 * Created by Dan on 12-Oct-15.
 */
public interface ProcesareDateRegistruService {
    /**
     * @param idRegistru
     */
    void procesareDateRegistru(Long idRegistru) throws RanBusinessException ;

    /**
     *
     * @param xml
     * @param idNomUat
     * @param local Propaga informatia ca cererea vine de la alicatia de introducere (daca are valoarea true)
     * @throws RanBusinessException
     */
    void procesareDateXml(String xml, Long idNomUat, boolean local) throws RanBusinessException ;
}
