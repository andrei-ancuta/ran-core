package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.model.registru.Atestat;
import ro.uti.ran.core.model.registru.AtestatProdus;
import ro.uti.ran.core.xml.model.capitol.nested.Produs;

/**
 * Created by Dan on 02-Nov-15.
 */
public class ProdusConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param atestatProdus entity pojo
     * @return jaxb pojo
     */
    public static Produs toSchemaType(AtestatProdus atestatProdus) {
        if (atestatProdus == null) {
            throw new IllegalArgumentException("AtestatProdus  nedefinit!");
        }
        Produs produs = new Produs();
        produs.setDenumire(atestatProdus.getDenumireProdus());
        return produs;
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param produs  jaxb pojo
     * @param atestat entity pojo
     */
    public static void populeazaFromSchemaType(Produs produs, Atestat atestat) {
        if (produs == null) {
            throw new IllegalArgumentException("Produs nedefinit!");
        }
        if (atestat == null) {
            throw new IllegalArgumentException("Atestat nedefinit!");
        }
        AtestatProdus atestatProdus = new AtestatProdus();
        atestatProdus.setDenumireProdus(produs.getDenumire());
        atestat.addAtestatProdus(atestatProdus);
    }
}
