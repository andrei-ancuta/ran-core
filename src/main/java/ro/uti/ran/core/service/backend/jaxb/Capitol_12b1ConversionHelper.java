package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitolCentralizator;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12b1DTO;
import ro.uti.ran.core.service.backend.utils.TipSpatiuProtejat;
import ro.uti.ran.core.xml.model.capitol.Capitol_12b1;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12b1;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smash on 17/11/15.
 */
public class Capitol_12b1ConversionHelper {

    public static Capitol_12b1 toSchemaType(CapitolCentralizatorDTO capitolCentralizatorDTO) {
        Capitol_12b1 capitol_12b1 = new Capitol_12b1();

        if(capitolCentralizatorDTO!=null){
            capitol_12b1.setDenumire(capitolCentralizatorDTO.getDenumire());
            capitol_12b1.setCodCapitol(capitolCentralizatorDTO.getTipCapitol().name());
            List<SectiuneCapitol_12b1> randuri = new ArrayList<SectiuneCapitol_12b1>();

            for(RandCapitolCentralizator rand : capitolCentralizatorDTO.getRandCapitolList()){
                if(rand instanceof RandCapitol_12b1DTO){
                    RandCapitol_12b1DTO randCurent =  (RandCapitol_12b1DTO) rand;
                    INTREG_POZITIV suprafata = new INTREG_POZITIV();
                    INTREG_POZITIV prodMedie = new INTREG_POZITIV();
                    INTREG_POZITIV prodTotal = new INTREG_POZITIV();
                    suprafata.setValue(randCurent.getSuprafata());
                    prodMedie.setValue(randCurent.getProductieMedie());
                    prodTotal.setValue(randCurent.getProductieTotala());

                    SectiuneCapitol_12b1 sectiuneCapitol_12b1 = new SectiuneCapitol_12b1();
                    sectiuneCapitol_12b1.setNrHA(suprafata);
                    sectiuneCapitol_12b1.setProdMedieKgPerHa(null != randCurent.getProductieMedie() ? prodMedie : null);
                    sectiuneCapitol_12b1.setProdTotalaTone(prodTotal);
                    sectiuneCapitol_12b1.setDenumire(randCurent.getDenumire());
                    sectiuneCapitol_12b1.setCodRand(randCurent.getCodRand());
                    sectiuneCapitol_12b1.setCodNomenclator(randCurent.getCod());

                    randuri.add(sectiuneCapitol_12b1);

                }else{
                    throw new UnsupportedOperationException("Unul dintre randurile capitolului nu este corect definit!");
                }
            }
            capitol_12b1.setRandCapitol(randuri);
        }

        return capitol_12b1;
    }

    public static void populeazaFromSchemaType(Capitol_12b1 capitol_12b1, List randuriDto) throws RequestValidationException {
        if(capitol_12b1 == null){
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_12b1");
        }

        for(SectiuneCapitol_12b1 rand : capitol_12b1.getRandCapitol()){
            RandCapitol_12b1ConversionHelper.populeazaFromSchemaType(rand, randuriDto);
        }

    }

}
