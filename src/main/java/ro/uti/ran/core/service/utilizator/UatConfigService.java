package ro.uti.ran.core.service.utilizator;

import ro.uti.ran.core.ws.internal.utilizator.UatConfig;

/**
 * Created by Anastasia cea micuta on 1/19/2016.
 */
public interface UatConfigService {
    UatConfig getUatConfig(Long idUat);

    void saveUatConfig(Long idUat, UatConfig uatConfig);

}
