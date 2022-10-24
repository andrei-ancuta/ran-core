package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.xml.model.types.NIF;

/**
 * Created by Dan on 12-Jan-16.
 */
public class NIFConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param value valoare cnp
     * @return jaxb pojo
     */
    public static NIF toSchemaType(String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("Valoare nif nedefinit!");
        }
        NIF nif = new NIF();
        nif.setValue(value);
        return nif;
    }
}
