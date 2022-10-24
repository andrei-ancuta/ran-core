package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.xml.model.types.NString;
import ro.uti.ran.core.xml.model.types.RC;

/**
 * Created by Dan on 14-Oct-15.
 */
public class RCConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param persoanaRc entity pojo
     * @return jaxb pojo
     */
    public static RC toSchemaType(PersoanaRc persoanaRc) {
        if (persoanaRc == null) {
            throw new IllegalArgumentException("PersoanaRc nedefinit!");
        }
        RC rc = new RC();
        /*CUI*/
        rc.setCui(CUIConversionHelper.toSchemaType(persoanaRc.getCui()));
        /*DENUMIRE*/
        rc.setDenumire(new NString(persoanaRc.getDenumire()));
        /*CIF*/
        if (StringUtils.isNotEmpty(persoanaRc.getCif())) {
            rc.setCif(CIFConversionHelper.toSchemaType(persoanaRc.getCif()));
        }
        /*formaOrganizareRC*/
        rc.setFormaOrganizareRC(new NString(persoanaRc.getNomFormaOrganizareRc().getCod()));
        return rc;
    }

    /**
     * @param rc jaxb pojo
     * @return entity pojo
     */
    public static PersoanaRc transformaFromSchemaTypeToPersoanaRc(RC rc) {
        if (rc == null) {
            throw new IllegalArgumentException("RC nedefinit!");
        }
        PersoanaRc persoanaRc = new PersoanaRc();
       /*denumire*/
        persoanaRc.setDenumire(rc.getDenumire().getValue().toUpperCase());
        /*cui*/
        if (rc.getCui() != null) {
            persoanaRc.setCui(rc.getCui().getValue().toUpperCase());
        }
        /*cif*/
        if (rc.getCif() != null) {
            persoanaRc.setCif(rc.getCif().getValue().toUpperCase());
        }
        /*formaOrganizareRC*/
        NomFormaOrganizareRc nomFormaOrganizareRc = new NomFormaOrganizareRc();
        nomFormaOrganizareRc.setCod(rc.getFormaOrganizareRC().getValue());
        persoanaRc.setNomFormaOrganizareRc(nomFormaOrganizareRc);
        //
        return persoanaRc;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param rc          jaxb pojo
     * @param detinatorPj entity pojo
     */
    public static void populeazaFromSchemaType(RC rc, DetinatorPj detinatorPj) {
        if (rc == null) {
            throw new IllegalArgumentException("RC nedefinit!");
        }
        if (detinatorPj == null) {
            throw new IllegalArgumentException("DetinatorPj nedefinit!");
        }
        detinatorPj.setPersoanaRc(transformaFromSchemaTypeToPersoanaRc(rc));
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param rc       jaxb pojo
     * @param contract entity pojo
     */
    public static void populeazaFromSchemaType(RC rc, Contract contract) {
        if (rc == null) {
            throw new IllegalArgumentException("RC nedefinit!");
        }
        if (contract == null) {
            throw new IllegalArgumentException("Contract nedefinit!");
        }
        contract.setPersoanaRc(transformaFromSchemaTypeToPersoanaRc(rc));
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param rc                  jaxb pojo
     * @param persoanaPreemptiune entity pojo
     */
    public static void populeazaFromSchemaType(RC rc, PersoanaPreemptiune persoanaPreemptiune) {
        if (rc == null) {
            throw new IllegalArgumentException("RC nedefinit!");
        }
        if (persoanaPreemptiune == null) {
            throw new IllegalArgumentException("PersoanaPreemptiune nedefinit!");
        }
        persoanaPreemptiune.setPersoanaRc(transformaFromSchemaTypeToPersoanaRc(rc));
    }

}
