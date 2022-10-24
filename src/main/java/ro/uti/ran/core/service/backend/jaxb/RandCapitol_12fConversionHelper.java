package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12fDTO;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12f;

import java.util.List;

/**
 * Created by smash on 19/11/15.
 */
public class RandCapitol_12fConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_12f toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Capitol centralizator nedefinit!");
        }
        SectiuneCapitol_12f sectiuneCapitol_12f = new SectiuneCapitol_12f();
        return sectiuneCapitol_12f;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_12f jaxb pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_12f sectiuneCapitol_12f, List randuriDto) throws RequestValidationException {
        if (sectiuneCapitol_12f == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_12f");
        }

        if(sectiuneCapitol_12f.getProductieTotala()==null){
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"ProductieTotala");
        }

        if(sectiuneCapitol_12f.getProductieMedie()==null){
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"ProductieMedie");
        }

        if(sectiuneCapitol_12f.getSuprafata()==null){
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Suprafata");
        }

        if(sectiuneCapitol_12f.getCodRand() == null){
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"CodRand");
        }

        if(sectiuneCapitol_12f.getCodNomenclator() == null){
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"CodNomenclator");
        }

        RandCapitol_12fDTO randNou = new RandCapitol_12fDTO();
        randNou.setDenumire(sectiuneCapitol_12f.getDenumire());
        randNou.setCodRand(sectiuneCapitol_12f.getCodRand());
        randNou.setCod(sectiuneCapitol_12f.getCodNomenclator());

        randNou.setSuprafata(sectiuneCapitol_12f.getSuprafata().getValue());
        randNou.setProductieMedie(sectiuneCapitol_12f.getProductieMedie().getValue());
        randNou.setProductieTotala(sectiuneCapitol_12f.getProductieTotala().getValue());

        randuriDto.add(randNou);
    }

}
