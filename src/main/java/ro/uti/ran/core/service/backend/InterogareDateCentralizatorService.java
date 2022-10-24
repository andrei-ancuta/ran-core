package ro.uti.ran.core.service.backend;

import ro.uti.ran.core.exception.InterogareDateRegistruException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.xml.model.RanDoc;

/**
 * Created by smash on 10/11/15.
 */
public interface InterogareDateCentralizatorService {

    /**
     * @param codSirutaUAT            identificator unic UAT
     * @param an                      an raportare
     * @param tipCapitol              tipul capitolului solicitat (vezi nom_capitol)
     * @return informatia incapsulata intr-un obiect "jaxb"
     */
    RanDoc getDateCapitol(Integer codSirutaUAT, Integer an, TipCapitol tipCapitol) throws RanBusinessException;

    /**
     *
     * @param codSirutaUAT cod siruta uat pt care se cer date
     * @param ranAuthorization informatii autorizare
     * @throws InterogareDateRegistruException
     */
    void checkRight(Integer codSirutaUAT, RanAuthorization ranAuthorization) throws InterogareDateRegistruException;

}
