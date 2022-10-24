package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12b1DTO;
import ro.uti.ran.core.service.backend.utils.TipSpatiuProtejat;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12b1;

import java.util.List;

/**
 * Created by smash on 17/11/15.
 */
public class RandCapitol_12b1ConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_12b1 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        SectiuneCapitol_12b1 sectiuneCapitol_12b1 = new SectiuneCapitol_12b1();
        return sectiuneCapitol_12b1;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_12b1 jaxb pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_12b1 sectiuneCapitol_12b1, List randuriDto) throws RequestValidationException {
        if (sectiuneCapitol_12b1 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_12b1");
        }

        if(sectiuneCapitol_12b1.getProdTotalaTone()==null){

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"ProdTotalaTone");
        }

        if(sectiuneCapitol_12b1.getNrHA()==null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"NrHA");
        }

        RandCapitol_12b1DTO randNou = new RandCapitol_12b1DTO();
        randNou.setCodTipSpatiu(TipSpatiuProtejat.SE.name());
        randNou.setDenumire(sectiuneCapitol_12b1.getDenumire());
        randNou.setCodRand(sectiuneCapitol_12b1.getCodRand());
        randNou.setCod(sectiuneCapitol_12b1.getCodNomenclator());
        randNou.setProductieMedie(sectiuneCapitol_12b1.getProdMedieKgPerHa()!=null ? sectiuneCapitol_12b1.getProdMedieKgPerHa().getValue() : null);
        randNou.setProductieTotala(sectiuneCapitol_12b1.getProdTotalaTone().getValue());
        randNou.setSuprafata(sectiuneCapitol_12b1.getNrHA().getValue());
        randuriDto.add(randNou);
    }

}
