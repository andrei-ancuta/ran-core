package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitolCentralizator;
import ro.uti.ran.core.service.backend.dto.RandCapitol_13centDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_13_centralizator;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_13_centralizator;
import ro.uti.ran.core.xml.model.types.NR_POZITIV_DOUA_ZECIMALE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smash on 20/11/15.
 */
public class Capitol_13centConversionHelper {

    public static Capitol_13_centralizator toSchemaType(CapitolCentralizatorDTO capitolCentralizatorDTO) {
        Capitol_13_centralizator capitol13Centralizator = new Capitol_13_centralizator();
        capitol13Centralizator.setDenumire(capitolCentralizatorDTO.getDenumire());
        capitol13Centralizator.setCodCapitol(capitolCentralizatorDTO.getTipCapitol().name());
        List<SectiuneCapitol_13_centralizator> randuri = new ArrayList<SectiuneCapitol_13_centralizator>();

        if (capitolCentralizatorDTO != null) {
            for (RandCapitolCentralizator rand : capitolCentralizatorDTO.getRandCapitolList()) {
                if (rand instanceof RandCapitol_13centDTO) {
                    RandCapitol_13centDTO randCurent = (RandCapitol_13centDTO) rand;

                    SectiuneCapitol_13_centralizator sectiuneCapitol13Centralizator = new SectiuneCapitol_13_centralizator();
                    NR_POZITIV_DOUA_ZECIMALE nr_pozitiv_doua_zecimale = new NR_POZITIV_DOUA_ZECIMALE();
                    nr_pozitiv_doua_zecimale.setValue(randCurent.getValoareProductie());
                    sectiuneCapitol13Centralizator.setValoareProductie(nr_pozitiv_doua_zecimale);
                    sectiuneCapitol13Centralizator.setDenumire(randCurent.getDenumire());
                    sectiuneCapitol13Centralizator.setCodRand(randCurent.getCodRand());
                    sectiuneCapitol13Centralizator.setCodNomenclator(randCurent.getCod());

                    randuri.add(sectiuneCapitol13Centralizator);

                } else {
                    throw new UnsupportedOperationException("Unul dintre randurile capitolului nu este corect definit!");
                }
            }
            capitol13Centralizator.setRandCapitol(randuri);
        }

        return capitol13Centralizator;
    }

    public static void populeazaFromSchemaType(Capitol_13_centralizator capitol_13cent, List randuriDto) throws RequestValidationException {
        if (capitol_13cent == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "capitol_13_centralizator");
        }

        for (SectiuneCapitol_13_centralizator rand : capitol_13cent.getRandCapitol()) {
            RandCapitol_13centConversionHelper.populeazaFromSchemaType(rand, randuriDto);
        }

    }

}
