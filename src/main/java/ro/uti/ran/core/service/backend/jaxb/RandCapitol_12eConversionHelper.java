package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12eDTO;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12e;

import java.util.List;

/**
 * Created by smash on 19/11/15.
 */
public class RandCapitol_12eConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_12e toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Capitol centralizator nedefinit!");
        }
        SectiuneCapitol_12e sectiuneCapitol_12e = new SectiuneCapitol_12e();
        return sectiuneCapitol_12e;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_12e jaxb pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_12e sectiuneCapitol_12e, List randuriDto) throws RequestValidationException {
        if (sectiuneCapitol_12e == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_12e");
        }

        if(sectiuneCapitol_12e.getProductieTotalaLivezi()==null){

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"ProductieTotalaLivezi");
        }

        if(sectiuneCapitol_12e.getArieLiveziHa()==null){

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"ArieLiveziHa");
        }

        if(sectiuneCapitol_12e.getCodRand() == null){

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"CodRand");
        }

        if(sectiuneCapitol_12e.getCodNomenclator() == null){

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"CodNomenclator");
        }

        RandCapitol_12eDTO randNou = new RandCapitol_12eDTO();
        randNou.setDenumire(sectiuneCapitol_12e.getDenumire());
        randNou.setCodRand(sectiuneCapitol_12e.getCodRand());
        randNou.setCod(sectiuneCapitol_12e.getCodNomenclator());

        randNou.setSuprafataLivezi(sectiuneCapitol_12e.getArieLiveziHa().getValue());
        randNou.setProdMedieLivezi(sectiuneCapitol_12e.getProductieMedieLivezi()!=null ? sectiuneCapitol_12e.getProductieMedieLivezi().getValue() : null);
        randNou.setProdTotalaLivezi(sectiuneCapitol_12e.getProductieTotalaLivezi().getValue());

        randuriDto.add(randNou);
    }

}
