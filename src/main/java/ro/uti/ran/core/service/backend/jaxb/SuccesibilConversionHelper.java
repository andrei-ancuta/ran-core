package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.model.registru.Adresa;
import ro.uti.ran.core.model.registru.PersoanaFizica;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.xml.model.capitol.nested.AdresaExterna;
import ro.uti.ran.core.xml.model.types.NString;

/**
 * Created by Dan on 06-Nov-15.
 */
public class SuccesibilConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param succesibilEntity entity pojo
     * @return jaxb pojo
     */
    public static ro.uti.ran.core.xml.model.capitol.nested.Succesibil toSchemaType(ro.uti.ran.core.model.registru.Succesibil succesibilEntity) {
        if (succesibilEntity == null) {
            throw new IllegalArgumentException("Succesibil entity nedefinit!");
        }
        ro.uti.ran.core.xml.model.capitol.nested.Succesibil succesibilJaxb = new ro.uti.ran.core.xml.model.capitol.nested.Succesibil();
        /*persoana*/
        succesibilJaxb.setPersoana(PFConversionHelper.toSchemaType(succesibilEntity.getPersoanaFizica()));
        /*adresa*/
        if (succesibilEntity.getAdresa() != null) {
            Integer codNumericTara = succesibilEntity.getAdresa().getNomTara().getCodNumeric();
            if (RanConstants.NOM_TARA_COD_NUMERIC_ROMANIA.equals(codNumericTara)) {
                succesibilJaxb.setAdresa(AdresaConversionHelper.toSchemaType(succesibilEntity.getAdresa()));
            } else {
                AdresaExterna adresaExterna = new AdresaExterna();
                adresaExterna.setCodTara(succesibilEntity.getAdresa().getNomTara().getCodNumeric());
                adresaExterna.setAdresaText(new NString(succesibilEntity.getAdresa().getExceptieAdresa()));
                succesibilJaxb.setAdresa(adresaExterna);
            }
        }
        //
        return succesibilJaxb;
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param succesibilJaxb   jaxb pojo
     * @param succesibilEntity entity pojo
     */
    public static void populeazaFromSchemaType(ro.uti.ran.core.xml.model.capitol.nested.Succesibil succesibilJaxb, ro.uti.ran.core.model.registru.Succesibil succesibilEntity) {
        if (succesibilEntity == null) {
            throw new IllegalArgumentException("Succesibil entity nedefinit!");
        }
        if (succesibilJaxb == null) {
            throw new IllegalArgumentException("Succesibil jaxb nedefinit!");
        }
        /*PF persoana*/
        succesibilEntity.setPersoanaFizica(PFConversionHelper.transformaFromSchemaTypeToPersoanaFizica(succesibilJaxb.getPersoana()));
        /*adresa*/
        if (succesibilJaxb.getAdresa() != null) {
            /*adresaRo*/
            if (succesibilJaxb.getAdresa() instanceof ro.uti.ran.core.xml.model.capitol.nested.Adresa) {
                ro.uti.ran.core.xml.model.capitol.nested.Adresa adresaJaxb = (ro.uti.ran.core.xml.model.capitol.nested.Adresa) succesibilJaxb.getAdresa();
                Adresa adresaEntity = new Adresa();
                succesibilEntity.setAdresa(adresaEntity);
                AdresaConversionHelper.populeazaFromSchemaType(adresaJaxb, adresaEntity);
            }
            /*adresaExtern*/
            if (succesibilJaxb.getAdresa() instanceof AdresaExterna) {
                AdresaExterna adresaJaxb = (AdresaExterna) succesibilJaxb.getAdresa();
                Adresa adresaEntity = new Adresa();
                succesibilEntity.setAdresa(adresaEntity);
                AdresaConversionHelper.populeazaFromSchemaType(adresaJaxb, adresaEntity);
            }
        }
    }
}
