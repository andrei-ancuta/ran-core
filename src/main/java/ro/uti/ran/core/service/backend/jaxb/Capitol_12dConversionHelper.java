package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitolCentralizator;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12dDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_12d;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12d;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smash on 18/11/15.
 */
public class Capitol_12dConversionHelper {

    public static Capitol_12d toSchemaType(CapitolCentralizatorDTO capitolCentralizatorDTO) {
        Capitol_12d capitol_12d = new Capitol_12d();

        if (capitolCentralizatorDTO != null) {
            capitol_12d.setDenumire(capitolCentralizatorDTO.getDenumire());
            capitol_12d.setCodCapitol(capitolCentralizatorDTO.getTipCapitol().name());
            List<SectiuneCapitol_12d> randuri = new ArrayList<SectiuneCapitol_12d>();

            for (RandCapitolCentralizator rand : capitolCentralizatorDTO.getRandCapitolList()) {
                if (rand instanceof RandCapitol_12dDTO) {
                    RandCapitol_12dDTO randCurent = (RandCapitol_12dDTO) rand;
                    INTREG_POZITIV nrPomiRazeleti = new INTREG_POZITIV();
                    INTREG_POZITIV prodMediePomiRazleti = new INTREG_POZITIV();
                    INTREG_POZITIV prodTotalaPomiRazleti = new INTREG_POZITIV();
                    INTREG_POZITIV suprafataLivezi = new INTREG_POZITIV();
                    INTREG_POZITIV prodMedieLivezi = new INTREG_POZITIV();
                    INTREG_POZITIV prodTotalaLivezi = new INTREG_POZITIV();

                    nrPomiRazeleti.setValue(randCurent.getNrPomiRazleti());
                    prodMediePomiRazleti.setValue(randCurent.getProdMediePomiRazleti());
                    prodTotalaPomiRazleti.setValue(randCurent.getProdTotalaPomiRazleti());
                    suprafataLivezi.setValue(randCurent.getSuprafataLivezi());
                    prodMedieLivezi.setValue(randCurent.getProdMedieLivezi());
                    prodTotalaLivezi.setValue(randCurent.getProdTotalaLivezi());

                    SectiuneCapitol_12d sectiuneCapitol_12d = new SectiuneCapitol_12d();
                    sectiuneCapitol_12d.setNrPomiRazleti(null != randCurent.getNrPomiRazleti() ? nrPomiRazeleti : null);
                    sectiuneCapitol_12d.setProductieMediePomiRazleti(null != randCurent.getProdMediePomiRazleti() ? prodMediePomiRazleti : null);
                    sectiuneCapitol_12d.setProductieTotalaPomiRazleti(null != randCurent.getProdTotalaPomiRazleti() ? prodTotalaPomiRazleti : null);
                    sectiuneCapitol_12d.setArieLiveziHa(suprafataLivezi);
                    sectiuneCapitol_12d.setProductieMedieLivezi(null != randCurent.getProdMedieLivezi() ? prodMedieLivezi : null);
                    sectiuneCapitol_12d.setProductieTotalaLivezi(prodTotalaLivezi);
                    sectiuneCapitol_12d.setDenumire(randCurent.getDenumire());
                    sectiuneCapitol_12d.setCodRand(randCurent.getCodRand());
                    sectiuneCapitol_12d.setCodNomenclator(randCurent.getCod());

                    randuri.add(sectiuneCapitol_12d);

                } else {
                    throw new UnsupportedOperationException("Unul dintre randurile capitolului nu este corect definit!");
                }
            }
            capitol_12d.setRandCapitol(randuri);
        }

        return capitol_12d;
    }

    public static void populeazaFromSchemaType(Capitol_12d capitol_12d, List randuriDto) throws RequestValidationException {
        if (capitol_12d == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_12d");

        }

        for (SectiuneCapitol_12d rand : capitol_12d.getRandCapitol()) {
            RandCapitol_12dConversionHelper.populeazaFromSchemaType(rand, randuriDto);
        }

    }

}
