package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitolCentralizator;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12aDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_12a;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12a;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smash on 02/11/15.
 */
public class Capitol_12aConversionHelper {

    public static Capitol_12a toSchemaType(CapitolCentralizatorDTO capitolCentralizatorDTO) {
        Capitol_12a capitol_12a = new Capitol_12a();

        if(capitolCentralizatorDTO!=null){
            capitol_12a.setDenumire(capitolCentralizatorDTO.getDenumire());
            capitol_12a.setCodCapitol(capitolCentralizatorDTO.getTipCapitol().name());
            List<SectiuneCapitol_12a> randuri = new ArrayList<SectiuneCapitol_12a>();

            for(RandCapitolCentralizator rand : capitolCentralizatorDTO.getRandCapitolList()){
                if(rand instanceof RandCapitol_12aDTO){
                    RandCapitol_12aDTO randCurent =  (RandCapitol_12aDTO) rand;
                    INTREG_POZITIV suprafata = new INTREG_POZITIV();
                    INTREG_POZITIV prodMedie = new INTREG_POZITIV();
                    INTREG_POZITIV prodTotal = new INTREG_POZITIV();
                    suprafata.setValue(randCurent.getSuprafata());
                    prodMedie.setValue(randCurent.getProductieMedie());
                    prodTotal.setValue(randCurent.getProductieTotala());

                    SectiuneCapitol_12a randCapitol_12a = new SectiuneCapitol_12a();
                    randCapitol_12a.setNrHA(suprafata);
                    randCapitol_12a.setProdMedieKgPerHa(null != randCurent.getProductieMedie() ? prodMedie : null);
                    randCapitol_12a.setProdTotalaTone(null != randCurent.getProductieTotala() ? prodTotal : null);
                    randCapitol_12a.setDenumire(randCurent.getDenumire());
                    randCapitol_12a.setCodRand(randCurent.getCodRand());
                    randCapitol_12a.setCodNomenclator(randCurent.getCod());

                    randuri.add(randCapitol_12a);

                }else{
                    throw new UnsupportedOperationException("Unul dintre randurile capitolului nu este corect definit!");
                }
            }
            capitol_12a.setRandCapitol(randuri);
        }

        return capitol_12a;
    }

    public static void populeazaFromSchemaType(Capitol_12a capitol_12a, List randuriDto) throws RequestValidationException {
        if(capitol_12a == null){
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_12a");
        }

        if(capitol_12a.getCodCapitol()==null){
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"CodCapitol");
        }

        for(SectiuneCapitol_12a rand : capitol_12a.getRandCapitol()){
            RandCapitol_12aConversionHelper.populeazaFromSchemaType(rand, randuriDto);
        }

    }

}
