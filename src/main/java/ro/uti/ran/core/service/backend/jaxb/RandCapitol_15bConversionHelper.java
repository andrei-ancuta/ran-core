package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Contract;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.NomTipContract;
import ro.uti.ran.core.model.utils.TipContract;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_15b;
import ro.uti.ran.core.xml.model.types.PF;
import ro.uti.ran.core.xml.model.types.RC;

/**
 * Înregistrări privind contractele de concesiune
 * Created by Dan on 02-Nov-15.
 */
public class RandCapitol_15bConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param contract entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_15b toSchemaType(Contract contract) {
        if (contract == null) {
            throw new IllegalArgumentException("Contract nedefinit!");
        }
        /*Contract*/
        SectiuneCapitol_15b sectiuneCapitol_15B = new SectiuneCapitol_15b();
        ContractConversionHelper.toSchemaType(contract, sectiuneCapitol_15B);
        /*concesionar*/
        if (contract.getPersoanaRc() != null) {
            sectiuneCapitol_15B.setConcesionar(RCConversionHelper.toSchemaType(contract.getPersoanaRc()));
        } else if (contract.getPersoanaFizica() != null) {
            sectiuneCapitol_15B.setConcesionar(PFConversionHelper.toSchemaType(contract.getPersoanaFizica()));
        }
        return sectiuneCapitol_15B;
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_15B jaxb pojo
     * @param gospodarie      entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_15b sectiuneCapitol_15B, Gospodarie gospodarie) throws RequestValidationException {
        if (sectiuneCapitol_15B == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_15B");
        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        /*Contract*/
        Contract contractEntity = new Contract();
        ContractConversionHelper.populeazaFromSchemaType(sectiuneCapitol_15B, contractEntity);
        /*concesionar*/
        if (sectiuneCapitol_15B.getConcesionar() instanceof PF) {
            PF pf = (PF) sectiuneCapitol_15B.getConcesionar();
            PFConversionHelper.populeazaFromSchemaType(pf, contractEntity);
        }
        if (sectiuneCapitol_15B.getConcesionar() instanceof RC) {
            RC RC = (RC) sectiuneCapitol_15B.getConcesionar();
            RCConversionHelper.populeazaFromSchemaType(RC, contractEntity);
        }
        /*FK_NOM_TIP_CONTRACT*/
        NomTipContract nomTipContract = new NomTipContract();
        nomTipContract.setCod(TipContract.CONCESIUNE.getCod());
        contractEntity.setNomTipContract(nomTipContract);
        /*populare entity pojo*/
        gospodarie.addContract(contractEntity);
    }
}
