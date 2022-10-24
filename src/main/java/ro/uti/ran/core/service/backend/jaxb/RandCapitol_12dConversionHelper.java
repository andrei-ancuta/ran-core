package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12dDTO;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12d;

import java.util.List;

/**
 * Created by smash on 18/11/15.
 */
public class RandCapitol_12dConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_12d toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Capitol centralizator nedefinit!");
        }
        SectiuneCapitol_12d sectiuneCapitol_12d = new SectiuneCapitol_12d();
        return sectiuneCapitol_12d;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_12d jaxb pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_12d sectiuneCapitol_12d, List randuriDto) throws RequestValidationException {
        if (sectiuneCapitol_12d == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_12d");
        }

        if(sectiuneCapitol_12d.getProductieTotalaLivezi()==null){

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"ProductieTotalaLivezi");
        }

        if(sectiuneCapitol_12d.getArieLiveziHa()==null){
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"ArieLiveziHa");
        }

        if(sectiuneCapitol_12d.getCodRand() == null){
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"CodRand");
        }

        if(sectiuneCapitol_12d.getCodNomenclator() == null){
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"CodNomenclator");
        }

        RandCapitol_12dDTO randNou = new RandCapitol_12dDTO();
        randNou.setDenumire(sectiuneCapitol_12d.getDenumire());
        randNou.setCodRand(sectiuneCapitol_12d.getCodRand());
        randNou.setCod(sectiuneCapitol_12d.getCodNomenclator());

        randNou.setNrPomiRazleti(sectiuneCapitol_12d.getNrPomiRazleti()!=null ? sectiuneCapitol_12d.getNrPomiRazleti().getValue() : null);
        randNou.setProdMediePomiRazleti(sectiuneCapitol_12d.getProductieMediePomiRazleti()!=null ? sectiuneCapitol_12d.getProductieMediePomiRazleti().getValue() : null);
        randNou.setProdTotalaPomiRazleti(sectiuneCapitol_12d.getProductieTotalaPomiRazleti()!=null ? sectiuneCapitol_12d.getProductieTotalaPomiRazleti().getValue() : null);
        randNou.setSuprafataLivezi(sectiuneCapitol_12d.getArieLiveziHa().getValue());
        randNou.setProdMedieLivezi(sectiuneCapitol_12d.getProductieMedieLivezi()!=null ? sectiuneCapitol_12d.getProductieMedieLivezi().getValue() : null);
        randNou.setProdTotalaLivezi(sectiuneCapitol_12d.getProductieTotalaLivezi().getValue());

        randuriDto.add(randNou);
    }

}
