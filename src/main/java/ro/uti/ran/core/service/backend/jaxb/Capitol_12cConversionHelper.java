package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitolCentralizator;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12cDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_12c;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12c;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smash on 18/11/15.
 */
public class Capitol_12cConversionHelper {

    public static Capitol_12c toSchemaType(CapitolCentralizatorDTO capitolCentralizatorDTO) {
        Capitol_12c capitol_12c = new Capitol_12c();

        if (capitolCentralizatorDTO != null) {
            capitol_12c.setDenumire(capitolCentralizatorDTO.getDenumire());
            capitol_12c.setCodCapitol(capitolCentralizatorDTO.getTipCapitol().name());
            List<SectiuneCapitol_12c> randuri = new ArrayList<SectiuneCapitol_12c>();

            for (RandCapitolCentralizator rand : capitolCentralizatorDTO.getRandCapitolList()) {
                if (rand instanceof RandCapitol_12cDTO) {
                    RandCapitol_12cDTO randCurent = (RandCapitol_12cDTO) rand;
                    INTREG_POZITIV suprafata = new INTREG_POZITIV();
                    INTREG_POZITIV prodMedie = new INTREG_POZITIV();
                    INTREG_POZITIV prodTotal = new INTREG_POZITIV();
                    suprafata.setValue(randCurent.getSuprafata());
                    prodMedie.setValue(null != randCurent.getProductieMedie() ? randCurent.getProductieMedie() : null);
                    prodTotal.setValue(randCurent.getProductieTotala());

                    SectiuneCapitol_12c sectiuneCapitol_12c = new SectiuneCapitol_12c();
                    sectiuneCapitol_12c.setNrHA(suprafata);
                    sectiuneCapitol_12c.setProdMedieKgPerHa(null != randCurent.getProductieMedie() ? prodMedie : null);
                    sectiuneCapitol_12c.setProdTotalaTone(prodTotal);
                    sectiuneCapitol_12c.setDenumire(randCurent.getDenumire());
                    sectiuneCapitol_12c.setCodRand(randCurent.getCodRand());
                    sectiuneCapitol_12c.setCodNomenclator(randCurent.getCod());

                    randuri.add(sectiuneCapitol_12c);

                } else {
                    throw new UnsupportedOperationException("Unul dintre randurile capitolului nu este corect definit!");
                }
            }
            capitol_12c.setRandCapitol(randuri);
        }

        return capitol_12c;
    }

    public static void populeazaFromSchemaType(Capitol_12c capitol_12c, List randuriDto) throws RequestValidationException {
        if (capitol_12c == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_12c");
        }

        for (SectiuneCapitol_12c rand : capitol_12c.getRandCapitol()) {
            RandCapitol_12cConversionHelper.populeazaFromSchemaType(rand, randuriDto);
        }

    }

}
