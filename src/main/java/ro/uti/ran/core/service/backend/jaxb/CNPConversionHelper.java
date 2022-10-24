package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.xml.model.types.CNP;

/**
 * Created by Dan on 13-Oct-15.
 */
public class CNPConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param value valoare cnp
     * @return jaxb pojo
     */
    public static CNP toSchemaType(String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("Valoare cnp nedefinit!");
        }
        CNP cnp = new CNP();
        cnp.setValue(value);
        return cnp;
    }
}
