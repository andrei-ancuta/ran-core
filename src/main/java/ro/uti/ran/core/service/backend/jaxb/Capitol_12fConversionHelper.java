package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitolCentralizator;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12fDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_12f;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12f;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smash on 19/11/15.
 */
public class Capitol_12fConversionHelper {

    public static Capitol_12f toSchemaType(CapitolCentralizatorDTO capitolCentralizatorDTO) {
        Capitol_12f capitol_12f = new Capitol_12f();
        capitol_12f.setDenumire(capitolCentralizatorDTO.getDenumire());
        capitol_12f.setCodCapitol(capitolCentralizatorDTO.getTipCapitol().name());
        List<SectiuneCapitol_12f> randuri = new ArrayList<SectiuneCapitol_12f>();

        if(capitolCentralizatorDTO!=null){
            for(RandCapitolCentralizator rand : capitolCentralizatorDTO.getRandCapitolList()){
                if(rand instanceof RandCapitol_12fDTO){
                    RandCapitol_12fDTO randCurent =  (RandCapitol_12fDTO) rand;
                    INTREG_POZITIV suprafata = new INTREG_POZITIV();
                    INTREG_POZITIV prodMedieStruguri = new INTREG_POZITIV();
                    INTREG_POZITIV prodTotalaStruguri = new INTREG_POZITIV();

                    suprafata.setValue(randCurent.getSuprafata());
                    prodMedieStruguri.setValue(randCurent.getProductieMedie());
                    prodTotalaStruguri.setValue(randCurent.getProductieTotala());

                    SectiuneCapitol_12f sectiuneCapitol_12f = new SectiuneCapitol_12f();
                    sectiuneCapitol_12f.setSuprafata(suprafata);
                    sectiuneCapitol_12f.setProductieMedie(prodMedieStruguri);
                    sectiuneCapitol_12f.setProductieTotala(prodTotalaStruguri);

                    sectiuneCapitol_12f.setDenumire(randCurent.getDenumire());
                    sectiuneCapitol_12f.setCodRand(randCurent.getCodRand());
                    sectiuneCapitol_12f.setCodNomenclator(randCurent.getCod());

                    randuri.add(sectiuneCapitol_12f);

                }else{
                    throw new UnsupportedOperationException("Unul dintre randurile capitolului nu este corect definit!");
                }
            }
            capitol_12f.setRandCapitol(randuri);
        }

        return capitol_12f;
    }

    public static void populeazaFromSchemaType(Capitol_12f capitol_12f, List randuriDto) throws RequestValidationException {
        if(capitol_12f == null){

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_12f");
        }

        for(SectiuneCapitol_12f rand : capitol_12f.getRandCapitol()){
            RandCapitol_12fConversionHelper.populeazaFromSchemaType(rand, randuriDto);
        }

    }

}
