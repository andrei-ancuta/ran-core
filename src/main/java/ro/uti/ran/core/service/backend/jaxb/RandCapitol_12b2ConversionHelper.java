package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12b2DTO;
import ro.uti.ran.core.service.backend.utils.TipSpatiuProtejat;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12b2;

import java.util.List;

/**
 * Created by smash on 18/11/15.
 */
public class RandCapitol_12b2ConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_12b2 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        SectiuneCapitol_12b2 sectiuneCapitol_12b2 = new SectiuneCapitol_12b2();
        return sectiuneCapitol_12b2;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_12b2 jaxb pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_12b2 sectiuneCapitol_12b2, List randuriDto) throws RequestValidationException {
        if (sectiuneCapitol_12b2 == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_12b2");
        }

        if(sectiuneCapitol_12b2.getProdTotalaTone()==null){

            throw new RequestValidationException(RequestCodes.MISSING_VALUE,"Valoarea productiei totale este nedefinita!");
        }

        if(sectiuneCapitol_12b2.getNrHA()==null) {

            throw new RequestValidationException(RequestCodes.MISSING_VALUE,"Valoarea suprafetei este nedefinita!");
        }

        RandCapitol_12b2DTO randNou = new RandCapitol_12b2DTO();
        randNou.setCodTipSpatiu(TipSpatiuProtejat.SO.name());
        randNou.setDenumire(sectiuneCapitol_12b2.getDenumire());
        randNou.setCodRand(sectiuneCapitol_12b2.getCodRand());
        randNou.setCod(sectiuneCapitol_12b2.getCodNomenclator());
        randNou.setProductieMedie(sectiuneCapitol_12b2.getProdMedieKgPerHa()!=null ? sectiuneCapitol_12b2.getProdMedieKgPerHa().getValue() : null);
        randNou.setProductieTotala(sectiuneCapitol_12b2.getProdTotalaTone().getValue());
        randNou.setSuprafata(sectiuneCapitol_12b2.getNrHA().getValue());
        randuriDto.add(randNou);
    }
}
