package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.utils.TipContract;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_15a;
import ro.uti.ran.core.xml.model.types.PF;
import ro.uti.ran.core.xml.model.types.RC;

/**
 * Înregistrări privind contractele de arendare
 * Created by Dan on 02-Nov-15.
 */
public class RandCapitol_15aConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param contract entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_15a toSchemaType(Contract contract) {
        if (contract == null) {
            throw new IllegalArgumentException("Contract nedefinit!");
        }
        SectiuneCapitol_15a sectiuneCapitol_15A = new SectiuneCapitol_15a();
        /*Contract*/
        ContractConversionHelper.toSchemaType(contract, sectiuneCapitol_15A);
        /*arendas*/
        if (contract.getPersoanaRc() != null) {
            sectiuneCapitol_15A.setArendas(RCConversionHelper.toSchemaType(contract.getPersoanaRc()));
        } else if (contract.getPersoanaFizica() != null) {
            sectiuneCapitol_15A.setArendas(PFConversionHelper.toSchemaType(contract.getPersoanaFizica()));
        }
        /*arendaInProduse*/
        sectiuneCapitol_15A.setArendaInProduse(contract.getRedeventaCompleta());
        //
        return sectiuneCapitol_15A;
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_15A jaxb pojo
     * @param gospodarie      entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_15a sectiuneCapitol_15A, Gospodarie gospodarie) throws RequestValidationException {
        if (sectiuneCapitol_15A == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_15A");
        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        /*Contract*/
        Contract contractEntity = new Contract();
        ContractConversionHelper.populeazaFromSchemaType(sectiuneCapitol_15A, contractEntity);
        /*arendas*/
        if (sectiuneCapitol_15A.getArendas() instanceof PF) {
            PF pf = (PF) sectiuneCapitol_15A.getArendas();
            PFConversionHelper.populeazaFromSchemaType(pf, contractEntity);
        }
        if (sectiuneCapitol_15A.getArendas() instanceof RC) {
            RC RC = (RC) sectiuneCapitol_15A.getArendas();
            RCConversionHelper.populeazaFromSchemaType(RC,contractEntity);
        }
        /*arendaInProduse*/
        contractEntity.setRedeventaCompleta(sectiuneCapitol_15A.getArendaInProduse());
        /*NOM_TIP_CONTRACT*/
        NomTipContract nomTipContract = new NomTipContract();
        nomTipContract.setCod(TipContract.ARENDARE.getCod());
        contractEntity.setNomTipContract(nomTipContract);
        /*populare entity pojo*/
        gospodarie.addContract(contractEntity);
    }
}
