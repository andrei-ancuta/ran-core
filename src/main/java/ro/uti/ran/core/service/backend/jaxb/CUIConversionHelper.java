package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.xml.model.types.CUI;

/**
 * Created by Dan on 13-Oct-15.
 */
public class CUIConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param value valoare cui
     * @return jaxb pojo
     */
    public static CUI toSchemaType(String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("Valoare cui nedefinit!");
        }
        CUI cui = new CUI();
        cui.setValue(value);
        return cui;
    }
}
