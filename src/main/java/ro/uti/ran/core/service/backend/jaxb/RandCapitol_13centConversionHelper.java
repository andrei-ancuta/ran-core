package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitol_13centDTO;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_13_centralizator;

import java.util.List;

/**
 * Created by smash on 20/11/15.
 */
public class RandCapitol_13centConversionHelper {


    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_13_centralizator toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Capitol centralizator nedefinit!");
        }
        SectiuneCapitol_13_centralizator sectiuneCapitol13Centralizator = new SectiuneCapitol_13_centralizator();
        return sectiuneCapitol13Centralizator;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol13Centralizator jaxb pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_13_centralizator sectiuneCapitol13Centralizator, List randuriDto) throws RequestValidationException {
        if (sectiuneCapitol13Centralizator == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol13Centralizator");
        }

        if (sectiuneCapitol13Centralizator.getValoareProductie() == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"valoareProductie");
        }

        if (sectiuneCapitol13Centralizator.getCodRand() == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"CodRand");
        }

        if (sectiuneCapitol13Centralizator.getCodNomenclator() == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"CodNomenclator");
        }

        RandCapitol_13centDTO randNou = new RandCapitol_13centDTO();
        randNou.setDenumire(sectiuneCapitol13Centralizator.getDenumire());
        randNou.setCodRand(sectiuneCapitol13Centralizator.getCodRand());
        randNou.setCod(sectiuneCapitol13Centralizator.getCodNomenclator());

        randNou.setValoareProductie(sectiuneCapitol13Centralizator.getValoareProductie().getValue());

        randuriDto.add(randNou);
    }

}
