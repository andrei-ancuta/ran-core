package ro.uti.ran.core.service.backend.capitol;

import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;

/**
 * Created by smash on 04/11/15.
 */
public interface CapitolBaseService {

    /**
     * @return
     */
    Class getCapitolClass();

    /**
     * Valideaza datele
     * Populare cu nomenclatoare:
     * - identific dupa (cod, codRand, dataExport) linia din nomenclatoare corespunzatoare;
     * - nu se accepta date duplicate (randuri duplicate).
     *
     * @param ranDocDTO datele ce trebuiesc validate (din mesajul XML)
     * @return datele existente in baza de date la momentul primirii mesajului sau null (in cazul cap. 0_12 si cap. 0_34)  sau exceptie pe null (restul capitolelor)
     * @throws DateRegistruValidationException semnaleaza date invalide
     */
    void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException;

    /**
     * @param ranDocDTO datele ce trebuiesc salvate in DB
     */
    void salveazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException;

}
