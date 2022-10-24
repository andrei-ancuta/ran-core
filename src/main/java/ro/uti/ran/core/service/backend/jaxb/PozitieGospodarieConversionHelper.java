package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.xml.model.capitol.nested.PozitieGospodarie;
import ro.uti.ran.core.xml.model.types.NString;

/**
 * Created by Dan on 13-Oct-15.
 */
public class PozitieGospodarieConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarie entity pojo
     * @return jaxb pojo
     */
    public static PozitieGospodarie toSchemaType(Gospodarie gospodarie) {
        if (gospodarie == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        PozitieGospodarie pozitieGospodarie = new PozitieGospodarie();
        if (gospodarie.getIdentPozitieAnterioara() != null) {
            pozitieGospodarie.setPozitiaAnterioara(gospodarie.getIdentPozitieAnterioara());
        }
        if (gospodarie.getIdentPozitieCurenta() != null) {
            pozitieGospodarie.setPozitieCurenta(gospodarie.getIdentPozitieCurenta());
        }
        if (gospodarie.getIdentRolNominalUnic() != null) {
            pozitieGospodarie.setRolNominalUnic(gospodarie.getIdentRolNominalUnic());
        }
        if (StringUtils.isNotEmpty(gospodarie.getIdentVolum())) {
            pozitieGospodarie.setVolumul(new NString(gospodarie.getIdentVolum()));
        }
        return pozitieGospodarie;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param pozitieGospodarie jaxb pojo
     * @param gospodarie        entity pojo
     */
    public static void populeazaFromSchemaType(PozitieGospodarie pozitieGospodarie, Gospodarie gospodarie) {
        if (pozitieGospodarie == null) {
            throw new IllegalArgumentException("PozitieGospodarie nedefinit!");
        }
        if (gospodarie == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        if (pozitieGospodarie.getPozitiaAnterioara() != null) {
            gospodarie.setIdentPozitieAnterioara(pozitieGospodarie.getPozitiaAnterioara());
        }
        if (pozitieGospodarie.getPozitieCurenta() != null) {
            gospodarie.setIdentPozitieCurenta(pozitieGospodarie.getPozitieCurenta());
        }
        if (pozitieGospodarie.getRolNominalUnic() != null) {
            gospodarie.setIdentRolNominalUnic(pozitieGospodarie.getRolNominalUnic());
        }
        if (StringUtils.isNotEmpty(pozitieGospodarie.getVolumul().getValue())) {
            gospodarie.setIdentVolum(pozitieGospodarie.getVolumul().getValue());
        }
    }
}
