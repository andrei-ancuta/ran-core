package ro.uti.ran.core.service.backend.capitol;

import ro.uti.ran.core.service.backend.dto.GospodarieDTO;

/**
 * Created by Dan on 14-Oct-15.
 */
public interface CapitolService extends CapitolBaseService {

    /**
     * se foloseste la interogare; se iau din DB doar informatiile cerute de interogare;
     *
     * @param gospodarieDTO obiectul ce trebuie populat
     * @param an            criteriu de interogare
     * @param semestru      criteriu de interogare
     */
    void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru);

    /**
     * se foloseste la interogare; se iau din DB doar informatiile cerute de interogare;
     *
     * @param gospodarieDTO obiectul ce trebuie populat
     * @param an            criteriu de interogare
     */
    void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an);

    /**
     * se foloseste la interogare; se iau din DB doar informatiile cerute de interogare;
     *
     * @param gospodarieDTO obiectul ce trebuie populat
     */
    void populeazaDateCapitol(GospodarieDTO gospodarieDTO);

}
