package ro.uti.ran.core.service.registru;

import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.model.registru.view.ViewRegistruNomStare;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 16:14
 */
public interface RegistruService {

    /**
     * @param indexRegistru
     * @return
     */
    ViewRegistruNomStare getByIndexRegistruOrNull(String indexRegistru);

    /**
     * @param indexRegistru
     * @return
     */
    ViewRegistruNomStare getByIndexRegistruOrThrow(String indexRegistru) throws RegistruNotFoundException;


    /**
     * Salvare registru.
     *
     * @param registru
     * @return
     */
    Registru saveRegistru(Registru registru);


    /**
     * Preluare lista registru
     *
     * @param searchFilter
     * @param pagingInfo
     * @param sortInfo
     * @return
     */
    GenericListResult<Registru> getListaRegistru(RegistruSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo);

    /**
     * actualizare recipisa si FK_NOM_STARE_REGISTRU dupa prelucrare xml si salvare in DB cu succes
     *
     * @param idRegistru   inregistrare din baza de date ce trebuie actualizata
     * @param ranDocDTO    info din xml
     * @param succesAsHtml mesajul de succes in format html
     */
    void actualizareRegistruCazSucces(Long idRegistru, RanDocDTO ranDocDTO, String succesAsHtml);

    /**
     * actualizare recipisa si FK_NOM_STARE_REGISTRU dupa prelucrare xml si salvare in DB cu eroare
     *
     * @param idRegistru  inregistrare din baza de date ce trebuie actualizata
     * @param ranDocDTO   info din xml
     * @param ex          exceptia rezultata in urma procesarii
     * @param errorAsHtml mesajul de eroare in format Html
     */
    void actualizareRegistruCazEroare(Long idRegistru, RanDocDTO ranDocDTO, Throwable ex, String errorAsHtml);

    Registru getRegistruById(Long idRegistru);

    List<Registru> getAllUnprocessedErrorData();
}
