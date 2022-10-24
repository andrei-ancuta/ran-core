package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.xml.model.types.CIF;

/**
 * Created by Dan on 22-Jan-16.
 */
public class CIFConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param value valoare cnp
     * @return jaxb pojo
     */
    public static CIF toSchemaType(String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("Valoare cif nedefinit!");
        }
        CIF cif = new CIF();
        cif.setValue(value);
        return cif;
    }
}
