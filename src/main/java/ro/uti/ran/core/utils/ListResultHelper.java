package ro.uti.ran.core.utils;

import ro.uti.ran.core.ws.fault.RanRuntimeException;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-18 13:49
 */
public class ListResultHelper {

    /**
     * Mapeaza un obiect cu al obiect.
     * Convenabil pentru conversie dintr-un tip de date in altul
     */
    public interface Mapper<S,D>{
        /**
         * Conversie din obiect in alt obiect
         * @param source
         * @return
         */
        D map(S source);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ListResult> T build(Class<T> c, ListResult source) {
        return build(c, source, null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ListResult> T build(Class<T> c, ListResult source, Mapper mapper) {

        try {
            T result = c.newInstance();

            result.setFirstResult(source.getFirstResult());
            result.setRecordsPerPage(source.getRecordsPerPage());
            result.setTotalRecordCount(source.getTotalRecordCount());

            if (result instanceof HasItems && source instanceof HasItems) {
                if( mapper == null) {
                    ((HasItems) result).setItems(((HasItems) source).getItems());
                }else{

                    List sourceItems = ((HasItems) source).getItems();
                    List destItems = new LinkedList();
                    if( sourceItems != null) for (Object sourceItem : sourceItems) {
                        destItems.add(mapper.map(sourceItem));
                    }

                    ((HasItems) result).setItems(destItems);
                }
            }
            return result;
        } catch (Throwable th) {
            throw new RuntimeException("Eroare la build ListResult");
        }
    }
}
