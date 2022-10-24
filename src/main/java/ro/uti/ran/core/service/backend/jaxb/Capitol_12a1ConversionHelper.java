package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitolCentralizator;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12a1DTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_12a1;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12a1;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smash on 16/11/15.
 */
public class Capitol_12a1ConversionHelper {


    public static Capitol_12a1 toSchemaType(CapitolCentralizatorDTO capitolCentralizatorDTO) {
        Capitol_12a1 capitol_12a1 = new Capitol_12a1();

        if (capitolCentralizatorDTO != null) {
            capitol_12a1.setDenumire(capitolCentralizatorDTO.getDenumire());
            capitol_12a1.setCodCapitol(capitolCentralizatorDTO.getTipCapitol().name());
            List<SectiuneCapitol_12a1> randuri = new ArrayList<SectiuneCapitol_12a1>();

            for (RandCapitolCentralizator rand : capitolCentralizatorDTO.getRandCapitolList()) {
                if (rand instanceof RandCapitol_12a1DTO) {
                    RandCapitol_12a1DTO randCurent = (RandCapitol_12a1DTO) rand;
                    INTREG_POZITIV suprafata = new INTREG_POZITIV();
                    INTREG_POZITIV prodMedie = new INTREG_POZITIV();
                    INTREG_POZITIV prodTotal = new INTREG_POZITIV();
                    suprafata.setValue(randCurent.getSuprafata());
                    prodMedie.setValue(randCurent.getProductieMedie());
                    prodTotal.setValue(randCurent.getProductieTotala());

                    SectiuneCapitol_12a1 sectiuneCapitol_12a1 = new SectiuneCapitol_12a1();
                    sectiuneCapitol_12a1.setNrHA(suprafata);
                    sectiuneCapitol_12a1.setProdMedieKgPerHa(null != randCurent.getProductieMedie() ? prodMedie : null);
                    sectiuneCapitol_12a1.setProdTotalaTone(prodTotal);
                    sectiuneCapitol_12a1.setDenumire(randCurent.getDenumire());
                    sectiuneCapitol_12a1.setCodRand(randCurent.getCodRand());
                    sectiuneCapitol_12a1.setCodNomenclator(randCurent.getCod());

                    randuri.add(sectiuneCapitol_12a1);

                } else {
                    throw new UnsupportedOperationException("Unul dintre randurile capitolului nu este corect definit!");
                }
            }
            capitol_12a1.setRandCapitol(randuri);
        }

        return capitol_12a1;
    }

    public static void populeazaFromSchemaType(Capitol_12a1 capitol_12a1, List randuriDto) throws RequestValidationException {
        if (capitol_12a1 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_12a1");
        }

        for (SectiuneCapitol_12a1 rand : capitol_12a1.getRandCapitol()) {
            RandCapitol_12a1ConversionHelper.populeazaFromSchemaType(rand, randuriDto);
        }

    }

}
