package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitolCentralizator;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12eDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_12e;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12e;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smash on 19/11/15.
 */
public class Capitol_12eConversionHelper {

    public static Capitol_12e toSchemaType(CapitolCentralizatorDTO capitolCentralizatorDTO) {
        Capitol_12e capitol_12e = new Capitol_12e();

        if (capitolCentralizatorDTO != null) {
            capitol_12e.setDenumire(capitolCentralizatorDTO.getDenumire());
            capitol_12e.setCodCapitol(capitolCentralizatorDTO.getTipCapitol().name());
            List<SectiuneCapitol_12e> randuri = new ArrayList<SectiuneCapitol_12e>();

            for (RandCapitolCentralizator rand : capitolCentralizatorDTO.getRandCapitolList()) {
                if (rand instanceof RandCapitol_12eDTO) {
                    RandCapitol_12eDTO randCurent = (RandCapitol_12eDTO) rand;
                    INTREG_POZITIV suprafataLivezi = new INTREG_POZITIV();
                    INTREG_POZITIV prodMedieLivezi = new INTREG_POZITIV();
                    INTREG_POZITIV prodTotalaLivezi = new INTREG_POZITIV();

                    suprafataLivezi.setValue(randCurent.getSuprafataLivezi());
                    prodMedieLivezi.setValue(randCurent.getProdMedieLivezi());
                    prodTotalaLivezi.setValue(randCurent.getProdTotalaLivezi());

                    SectiuneCapitol_12e sectiuneCapitol_12e = new SectiuneCapitol_12e();
                    sectiuneCapitol_12e.setArieLiveziHa(suprafataLivezi);
                    sectiuneCapitol_12e.setProductieMedieLivezi(null != randCurent.getProdMedieLivezi() ? prodMedieLivezi : null);
                    sectiuneCapitol_12e.setProductieTotalaLivezi(prodTotalaLivezi);
                    sectiuneCapitol_12e.setDenumire(randCurent.getDenumire());
                    sectiuneCapitol_12e.setCodRand(randCurent.getCodRand());
                    sectiuneCapitol_12e.setCodNomenclator(randCurent.getCod());

                    randuri.add(sectiuneCapitol_12e);

                } else {
                    throw new UnsupportedOperationException("Unul dintre randurile capitolului nu este corect definit!");
                }
            }
            capitol_12e.setRandCapitol(randuri);
        }

        return capitol_12e;
    }

    public static void populeazaFromSchemaType(Capitol_12e capitol_12e, List randuriDto) throws RequestValidationException {
        if (capitol_12e == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_12e");
        }

        for (SectiuneCapitol_12e rand : capitol_12e.getRandCapitol()) {
            RandCapitol_12eConversionHelper.populeazaFromSchemaType(rand, randuriDto);
        }

    }

}
