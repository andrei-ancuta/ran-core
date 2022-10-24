package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitolCentralizator;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12b2DTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_12b2;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12b2;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smash on 18/11/15.
 */
public class Capitol_12b2ConversionHelper {

    public static Capitol_12b2 toSchemaType(CapitolCentralizatorDTO capitolCentralizatorDTO) {
        Capitol_12b2 capitol_12b2 = new Capitol_12b2();

        if (capitolCentralizatorDTO != null) {
            capitol_12b2.setDenumire(capitolCentralizatorDTO.getDenumire());
            capitol_12b2.setCodCapitol(capitolCentralizatorDTO.getTipCapitol().name());
            List<SectiuneCapitol_12b2> randuri = new ArrayList<SectiuneCapitol_12b2>();

            for (RandCapitolCentralizator rand : capitolCentralizatorDTO.getRandCapitolList()) {
                if (rand instanceof RandCapitol_12b2DTO) {
                    RandCapitol_12b2DTO randCurent = (RandCapitol_12b2DTO) rand;
                    INTREG_POZITIV suprafata = new INTREG_POZITIV();
                    INTREG_POZITIV prodMedie = new INTREG_POZITIV();
                    INTREG_POZITIV prodTotal = new INTREG_POZITIV();
                    suprafata.setValue(randCurent.getSuprafata());
                    prodMedie.setValue(randCurent.getProductieMedie());
                    prodTotal.setValue(randCurent.getProductieTotala());

                    SectiuneCapitol_12b2 sectiuneCapitol_12b2 = new SectiuneCapitol_12b2();
                    sectiuneCapitol_12b2.setNrHA(suprafata);
                    sectiuneCapitol_12b2.setProdMedieKgPerHa(null != randCurent.getProductieMedie() ? prodMedie : null);
                    sectiuneCapitol_12b2.setProdTotalaTone(prodTotal);
                    sectiuneCapitol_12b2.setDenumire(randCurent.getDenumire());
                    sectiuneCapitol_12b2.setCodRand(randCurent.getCodRand());
                    sectiuneCapitol_12b2.setCodNomenclator(randCurent.getCod());

                    randuri.add(sectiuneCapitol_12b2);

                } else {
                    throw new UnsupportedOperationException("Unul dintre randurile capitolului nu este corect definit!");
                }
            }
            capitol_12b2.setRandCapitol(randuri);
        }

        return capitol_12b2;
    }

    public static void populeazaFromSchemaType(Capitol_12b2 capitol_12b2, List randuriDto) throws RequestValidationException {
        if (capitol_12b2 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_12b2");
        }

        for (SectiuneCapitol_12b2 rand : capitol_12b2.getRandCapitol()) {
            RandCapitol_12b2ConversionHelper.populeazaFromSchemaType(rand, randuriDto);
        }

    }
}
