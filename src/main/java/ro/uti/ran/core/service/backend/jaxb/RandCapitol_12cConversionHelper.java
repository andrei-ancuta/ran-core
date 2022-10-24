package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12cDTO;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12c;

import java.util.List;

/**
 * Created by smash on 18/11/15.
 */
public class RandCapitol_12cConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_12c toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Capitol centralizator nedefinit!");
        }
        SectiuneCapitol_12c sectiuneCapitol_12c = new SectiuneCapitol_12c();
        return sectiuneCapitol_12c;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_12c jaxb pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_12c sectiuneCapitol_12c, List randuriDto) throws RequestValidationException {
        if (sectiuneCapitol_12c == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_12c");
        }

        if(sectiuneCapitol_12c.getProdTotalaTone()==null){

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"ProdTotalaTone");
        }

        if(sectiuneCapitol_12c.getNrHA()==null){

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"NrHA");
        }

        if(sectiuneCapitol_12c.getCodRand() == null){

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"CodRand");
        }

        if(sectiuneCapitol_12c.getCodNomenclator() == null){

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"CodNomenclator");
        }

        RandCapitol_12cDTO randNou = new RandCapitol_12cDTO();
        randNou.setDenumire(sectiuneCapitol_12c.getDenumire());
        randNou.setCodRand(sectiuneCapitol_12c.getCodRand());
        randNou.setCod(sectiuneCapitol_12c.getCodNomenclator());
        randNou.setProductieMedie(sectiuneCapitol_12c.getProdMedieKgPerHa()!=null ? sectiuneCapitol_12c.getProdMedieKgPerHa().getValue() : null);
        randNou.setProductieTotala(sectiuneCapitol_12c.getProdTotalaTone().getValue());
        randNou.setSuprafata(sectiuneCapitol_12c.getNrHA().getValue());
        randuriDto.add(randNou);
    }

}
