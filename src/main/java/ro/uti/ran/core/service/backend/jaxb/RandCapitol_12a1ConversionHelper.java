package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12a1DTO;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12a1;

import java.util.List;

/**
 * Created by smash on 16/11/15.
 */
public class RandCapitol_12a1ConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_12a1 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Capitol centralizator nedefinit!");
        }
        SectiuneCapitol_12a1 sectiuneCapitol_12a1 = new SectiuneCapitol_12a1();
        return sectiuneCapitol_12a1;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_12a1 jaxb pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_12a1 sectiuneCapitol_12a1, List randuriDto) throws RequestValidationException {
        if (sectiuneCapitol_12a1 == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_12a1");
        }

        if(sectiuneCapitol_12a1.getProdTotalaTone()==null){

            throw new RequestValidationException(RequestCodes.MISSING_VALUE,"Valoarea productiei totale este nedefinita!");
        }

        if(sectiuneCapitol_12a1.getNrHA()==null){

            throw new RequestValidationException(RequestCodes.MISSING_VALUE,"Valoarea suprafetei este nedefinita");
        }

        if(sectiuneCapitol_12a1.getCodRand() == null){

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"CodRand");
        }

        if(sectiuneCapitol_12a1.getCodNomenclator() == null){

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"CodNomenclato");

        }

        RandCapitol_12a1DTO randNou = new RandCapitol_12a1DTO();
        randNou.setDenumire(sectiuneCapitol_12a1.getDenumire());
        randNou.setCodRand(sectiuneCapitol_12a1.getCodRand());
        randNou.setCod(sectiuneCapitol_12a1.getCodNomenclator());
        randNou.setProductieMedie(sectiuneCapitol_12a1.getProdMedieKgPerHa()!=null ? sectiuneCapitol_12a1.getProdMedieKgPerHa().getValue() : null);
        randNou.setProductieTotala(sectiuneCapitol_12a1.getProdTotalaTone().getValue());
        randNou.setSuprafata(sectiuneCapitol_12a1.getNrHA().getValue());
        randuriDto.add(randNou);
    }
}
