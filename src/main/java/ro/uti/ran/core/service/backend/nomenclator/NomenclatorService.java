package ro.uti.ran.core.service.backend.nomenclator;

import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;

/**
 * Created by Anastasia cea micuta on 10/20/2015.
 */
public interface NomenclatorService {


    <T extends Nomenclator> T getNomenlatorForId(NomenclatorCodeType nomenclator, Long id);

    <T extends Nomenclator> T getNomenclatorForStringParam(NomenclatorCodeType nomenclator, Object code);

    <T extends Nomenclator> T getNomenclatorForStringParam(NomenclatorCodeType nomenclator, Object code, DataRaportareValabilitate dataRaportareValabilitate);

    <T extends Nomenclator> T getNomenclatorForStringParam(NomenclatorCodeType nomenclator, Object code, DataRaportareValabilitate dataRaportareValabilitate, String codCapitol);

    <T extends Nomenclator> T findCapByCodAndCodRandAndCapitolAndDataValabilitate(NomenclatorCodeType nomenclator, String cod, Integer codRand, String codCapitol, DataRaportareValabilitate dataRaportareValabilitate);

}
