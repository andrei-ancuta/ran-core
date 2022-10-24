package ro.uti.ran.core.service.backend;

import ro.uti.ran.core.exception.InterogareDateRegistruException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.service.backend.dto.ParametriiInterogare;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.model.interogare.IdentificatorGospodarie;
import ro.uti.ran.core.ws.model.interogare.IdentificatorPF;
import ro.uti.ran.core.xml.model.RanDoc;

import java.util.ArrayList;

/**
 * Created by Dan on 19-Oct-15.
 */
public interface InterogareDateService {


    /**
     * @param parametriiInterogare parametri de interogare
     * @return informatia incapsulata intr-un obiect "jaxb"
     * @throws RanBusinessException
     */
    RanDoc getDateCapitol(ParametriiInterogare parametriiInterogare) throws RanBusinessException;


    ArrayList<IdentificatorGospodarie> getListaGospodariiPF(IdentificatorPF identificatorPF, Boolean activ, RanAuthorization ranAuthorization) throws InterogareDateRegistruException;

    ArrayList<IdentificatorGospodarie> getListaGospodariiPJ(String cui, Boolean activ, RanAuthorization ranAuthorization) throws InterogareDateRegistruException;

    IdentificatorGospodarie getGospodariePJ(String cui, Integer sirutaUAT, RanAuthorization ranAuthorization) throws InterogareDateRegistruException;

    IdentificatorGospodarie getGospodariePF(IdentificatorPF identificatorPF, Integer sirutaUAT, RanAuthorization ranAuthorization) throws InterogareDateRegistruException;

    void checkGospodarieRight(int codSiruta, String identificatorGospodarie, RanAuthorization ranAuthorization) throws InterogareDateRegistruException;
}
