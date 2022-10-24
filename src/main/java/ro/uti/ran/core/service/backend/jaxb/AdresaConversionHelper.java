package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.model.registru.NomJudet;
import ro.uti.ran.core.model.registru.NomLocalitate;
import ro.uti.ran.core.model.registru.NomTara;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.xml.model.capitol.nested.AdresaExterna;
import ro.uti.ran.core.xml.model.types.CUA;

/**
 * Created by Dan on 13-Oct-15.
 */
public class AdresaConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param adresaEntity entity pojo
     * @return jaxb pojo
     */
    public static ro.uti.ran.core.xml.model.capitol.nested.Adresa toSchemaType(ro.uti.ran.core.model.registru.Adresa adresaEntity) {
        if (adresaEntity == null) {
            throw new IllegalArgumentException("Adresa entity nedefinit!");
        }
        ro.uti.ran.core.xml.model.capitol.nested.Adresa adresaJaxb = new ro.uti.ran.core.xml.model.capitol.nested.Adresa();
        /*sirutaJudet*/
        if (adresaEntity.getNomJudet() != null && adresaEntity.getNomJudet().getCodSiruta() != 0) {
            adresaJaxb.setSirutaJudet(adresaEntity.getNomJudet().getCodSiruta());
        }
        /*sirutaUAT*/
        if (adresaEntity.getNomUat() != null && adresaEntity.getNomUat().getCodSiruta() != null) {
            adresaJaxb.setSirutaUAT(adresaEntity.getNomUat().getCodSiruta());
        }
        /*sirutaLocalitate*/
        if (adresaEntity.getNomLocalitate() != null && adresaEntity.getNomLocalitate().getCodSiruta() != null) {
            adresaJaxb.setSirutaLocalitate(adresaEntity.getNomLocalitate().getCodSiruta());
        }
        /*strada*/
        adresaJaxb.setStrada(adresaEntity.getStrada());
        /*numar*/
        adresaJaxb.setNumar(adresaEntity.getNrStrada());
        /*referinta geo xml; geolocator*/
        if (StringUtils.isNotEmpty(adresaEntity.getGeometrieGML())) {
            adresaJaxb.setReferintaGeoXml(adresaEntity.getGeometrieGML());
        }
        /*CUA - renns*/
        if (StringUtils.isNotEmpty(adresaEntity.getUidRenns())) {
            CUA cua = new CUA();
            cua.setValue(adresaEntity.getUidRenns());
            adresaJaxb.setCua(cua);
        }
        /*bloc*/
        adresaJaxb.setBloc(adresaEntity.getBloc());
        /*scara*/
        adresaJaxb.setScara(adresaEntity.getScara());
        /*etaj*/
        adresaJaxb.setEtaj(adresaEntity.getEtaj());
        /*apartament*/
        adresaJaxb.setApartament(adresaEntity.getApartament());
        return adresaJaxb;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param adresaJaxb   jaxb pojo
     * @param adresaEntity entity pojo
     */
    public static void populeazaFromSchemaType(ro.uti.ran.core.xml.model.capitol.nested.Adresa adresaJaxb, ro.uti.ran.core.model.registru.Adresa adresaEntity) {
        if (adresaEntity == null) {
            throw new IllegalArgumentException("Adresa entity nedefinit!");
        }
        if (adresaJaxb == null) {
            throw new IllegalArgumentException("Adresa jaxb nedefinit!");
        }
        /*strada*/
        adresaEntity.setStrada(adresaJaxb.getStrada());
        /*numar*/
        adresaEntity.setNrStrada(adresaJaxb.getNumar());
        /*bloc*/
        adresaEntity.setBloc(adresaJaxb.getBloc());
        /*scara*/
        adresaEntity.setScara(adresaJaxb.getScara());
        /*etaj*/
        adresaEntity.setEtaj(adresaJaxb.getEtaj());
        /*apartament*/
        adresaEntity.setApartament(adresaJaxb.getApartament());
        /*CUA - renns*/
        if (adresaJaxb.getCua() != null) {
            adresaEntity.setUidRenns(adresaJaxb.getCua().getValue());
        }
        /*geolocator*/
        if (StringUtils.isNotEmpty(adresaJaxb.getReferintaGeoXml())) {
            adresaEntity.setGeometrieGML(adresaJaxb.getReferintaGeoXml());
        }
        /*FK_NOM_TARA*/
        NomTara nomTara = new NomTara();
        nomTara.setCodNumeric(RanConstants.NOM_TARA_COD_NUMERIC_ROMANIA);
        adresaEntity.setNomTara(nomTara);
        /*sirutaJudet*/
        if (adresaJaxb.getSirutaJudet() != null) {
            NomJudet nomJudet = new NomJudet();
            nomJudet.setCodSiruta(adresaJaxb.getSirutaJudet());
            adresaEntity.setNomJudet(nomJudet);
        }
        /*sirutaUAT*/
        if (adresaJaxb.getSirutaUAT() != null) {
            NomUat nomUat = new NomUat();
            nomUat.setCodSiruta(adresaJaxb.getSirutaUAT());
            adresaEntity.setNomUat(nomUat);
        }
        /*sirutaLocalitate*/
        if (adresaJaxb.getSirutaLocalitate() != null) {
            NomLocalitate nomLocalitate = new NomLocalitate();
            nomLocalitate.setCodSiruta(adresaJaxb.getSirutaLocalitate());
            adresaEntity.setNomLocalitate(nomLocalitate);
        }

    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param adresaJaxb   jaxb pojo
     * @param adresaEntity entity pojo
     */
    public static void populeazaFromSchemaType(AdresaExterna adresaJaxb, ro.uti.ran.core.model.registru.Adresa adresaEntity) {
        if (adresaEntity == null) {
            throw new IllegalArgumentException("Adresa entity nedefinit!");
        }
        if (adresaJaxb == null) {
            throw new IllegalArgumentException("AdresaExterna jaxb nedefinit!");
        }
        /*FK_NOM_TARA*/
        if (adresaJaxb.getCodTara() != null) {
            NomTara nomTara = new NomTara();
            nomTara.setCodNumeric(adresaJaxb.getCodTara());
            adresaEntity.setNomTara(nomTara);
        }
        /*adresaText*/
        adresaEntity.setExceptieAdresa(adresaJaxb.getAdresaText().getValue());
    }
}
