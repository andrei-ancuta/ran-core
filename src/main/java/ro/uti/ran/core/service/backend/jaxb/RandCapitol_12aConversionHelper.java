package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitolCentralizator;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12a1DTO;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12aDTO;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12a;

import java.util.List;

/**
 * Created by smash on 02/11/15.
 */
public class RandCapitol_12aConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_12a toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Capitol centralizator nedefinit!");
        }
        SectiuneCapitol_12a sectiuneCapitol_12A = new SectiuneCapitol_12a();
        return sectiuneCapitol_12A;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_12a jaxb pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_12a sectiuneCapitol_12a, List randuriDto) throws RequestValidationException {
        if (sectiuneCapitol_12a == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"SectiuneCapitol_12a");
        }

        if(sectiuneCapitol_12a.getNrHA()==null){

            throw new RequestValidationException(RequestCodes.MISSING_VALUE,"NrHA");
        }

        if(sectiuneCapitol_12a.getCodRand() == null){

            throw new RequestValidationException(RequestCodes.MISSING_VALUE,"CodRand");
        }

        if(sectiuneCapitol_12a.getCodNomenclator() == null){

            throw new RequestValidationException(RequestCodes.MISSING_VALUE,"CodNomenclator");
        }


        RandCapitol_12aDTO randNou = new RandCapitol_12aDTO();
        randNou.setDenumire(sectiuneCapitol_12a.getDenumire());
        randNou.setCodRand(sectiuneCapitol_12a.getCodRand());
        randNou.setCod(sectiuneCapitol_12a.getCodNomenclator());
        randNou.setProductieMedie(sectiuneCapitol_12a.getProdMedieKgPerHa()!=null ? sectiuneCapitol_12a.getProdMedieKgPerHa().getValue() : null);
        randNou.setProductieTotala(sectiuneCapitol_12a.getProdTotalaTone()!=null ?sectiuneCapitol_12a.getProdTotalaTone().getValue() : null);
        randNou.setSuprafata(sectiuneCapitol_12a.getNrHA().getValue());
        randuriDto.add(randNou);
    }

}
