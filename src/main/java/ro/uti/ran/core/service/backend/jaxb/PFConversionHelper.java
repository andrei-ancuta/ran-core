package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.xml.model.types.CNP;
import ro.uti.ran.core.xml.model.types.NIF;
import ro.uti.ran.core.xml.model.types.NString;
import ro.uti.ran.core.xml.model.types.PF;

/**
 * Created by Dan on 13-Oct-15.
 */
public class PFConversionHelper {


    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param persoanaFizica entity pojo
     * @return jaxb pojo
     */
    public static PF toSchemaType(PersoanaFizica persoanaFizica) {
        if (persoanaFizica == null) {
            throw new IllegalArgumentException("PersoanaFizica nedefinit!");
        }
        PF pf = new PF();
        /*cnp*/
        if (StringUtils.isNotEmpty(persoanaFizica.getCnp())) {
            pf.setIdentificator(CNPConversionHelper.toSchemaType(persoanaFizica.getCnp()));
        }
        /*nif*/
        if (StringUtils.isNotEmpty(persoanaFizica.getNif())) {
            pf.setIdentificator(NIFConversionHelper.toSchemaType(persoanaFizica.getNif()));
        }
        /*persoana*/
        pf.setPrenume(new NString(persoanaFizica.getPrenume()));
        pf.setInitialaTata(new NString(persoanaFizica.getInitialaTata()));
        pf.setNume(new NString(persoanaFizica.getNume()));
        //
        return pf;
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param pf          jaxb pojo
     * @param detinatorPf entity pojo
     */
    public static void populeazaFromSchemaType(PF pf, DetinatorPf detinatorPf) {
        if (pf == null) {
            throw new IllegalArgumentException("PF nedefinit!");
        }
        if (detinatorPf == null) {
            throw new IllegalArgumentException("DetinatorPf nedefinit!");
        }
        detinatorPf.setPersoanaFizica(transformaFromSchemaTypeToPersoanaFizica(pf));
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param pf          jaxb pojo
     * @param detinatorPj entity pojo
     */
    public static void populeazaFromSchemaType(PF pf, DetinatorPj detinatorPj) {
        if (pf == null) {
            throw new IllegalArgumentException("PF nedefinit!");
        }
        if (detinatorPj == null) {
            throw new IllegalArgumentException("DetinatorPj nedefinit!");
        }
        detinatorPj.setPersoanaFizica(transformaFromSchemaTypeToPersoanaFizica(pf));
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param pf       jaxb pojo
     * @param contract entity pojo
     */
    public static void populeazaFromSchemaType(PF pf, Contract contract) {
        if (pf == null) {
            throw new IllegalArgumentException("PF nedefinit!");
        }
        if (contract == null) {
            throw new IllegalArgumentException("Contract nedefinit!");
        }
        contract.setPersoanaFizica(transformaFromSchemaTypeToPersoanaFizica(pf));
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param pf                  jaxb pojo
     * @param persoanaPreemptiune entity pojo
     */
    public static void populeazaFromSchemaType(PF pf, PersoanaPreemptiune persoanaPreemptiune) {
        if (pf == null) {
            throw new IllegalArgumentException("PF nedefinit!");
        }
        if (persoanaPreemptiune == null) {
            throw new IllegalArgumentException("PersoanaPreemptiune nedefinit!");
        }
        persoanaPreemptiune.setPersoanaFizica(transformaFromSchemaTypeToPersoanaFizica(pf));
    }


    public static PersoanaFizica transformaFromSchemaTypeToPersoanaFizica(PF pf) {
        if (pf == null) {
            throw new IllegalArgumentException("PF nedefinit!");
        }
        PersoanaFizica persoanaFizica = new PersoanaFizica();
        /*cnp*/
        if (pf.getIdentificator() instanceof CNP) {
            persoanaFizica.setCnp(((CNP) pf.getIdentificator()).getValue());
        }
        /*nif*/
        if (pf.getIdentificator() instanceof NIF) {
            persoanaFizica.setNif(((NIF) pf.getIdentificator()).getValue());
        }
        /*persoana*/
        if (null != pf.getPrenume()) {
            persoanaFizica.setPrenume(pf.getPrenume().getValue().toUpperCase());
        }
        if (null != pf.getNume()) {
            persoanaFizica.setNume(pf.getNume().getValue().toUpperCase());
        }
        if (null != pf.getInitialaTata()) {
            persoanaFizica.setInitialaTata(pf.getInitialaTata().getValue().toUpperCase());
        }
        return persoanaFizica;
    }

}
