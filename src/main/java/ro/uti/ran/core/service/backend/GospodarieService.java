package ro.uti.ran.core.service.backend;

import ro.uti.ran.core.model.registru.Gospodarie;

/**
 * Created by Dan on 03-Feb-16.
 */
public interface GospodarieService {

    boolean isGospodarieActiva(Integer codSirutaUAT, String identificatorGospodarie);

    boolean isGospodarie(Integer codSirutaUAT, String identificatorGospodarie);

    Gospodarie getByUatAndIdentificator(Integer codSirutaUAT, String identificatorGospodarie);

    boolean isCapitolGospodarie(Long id);
}
