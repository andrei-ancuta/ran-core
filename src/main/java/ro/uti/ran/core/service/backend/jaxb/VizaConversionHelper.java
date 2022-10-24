package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.model.registru.Atestat;
import ro.uti.ran.core.model.registru.AtestatViza;
import ro.uti.ran.core.xml.model.capitol.nested.Viza;

/**
 * Created by Dan on 10-Nov-15.
 */
public class VizaConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param atestatViza entity pojo
     * @return jaxb pojo
     */
    public static Viza toSchemaType(AtestatViza atestatViza) {
        if (atestatViza == null) {
            throw new IllegalArgumentException("AtestatViza  nedefinit!");
        }
        Viza viza = new Viza();
        viza.setDataViza(atestatViza.getDataViza());
        viza.setNumarViza(atestatViza.getNumarViza());
        return viza;
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param viza    jaxb pojo
     * @param atestat entity pojo
     */
    public static void populeazaFromSchemaType(Viza viza, Atestat atestat) {
        if (viza == null) {
            throw new IllegalArgumentException("Viza nedefinit!");
        }
        if (atestat == null) {
            throw new IllegalArgumentException("Atestat nedefinit!");
        }
        AtestatViza atestatViza = new AtestatViza();
        atestatViza.setDataViza(viza.getDataViza());
        atestatViza.setNumarViza(viza.getNumarViza());
        //
        atestat.addAtestatViza(atestatViza);
    }
}
