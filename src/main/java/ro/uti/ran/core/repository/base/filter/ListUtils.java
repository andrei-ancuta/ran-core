package ro.uti.ran.core.repository.base.filter;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-11 10:59
 */
public class ListUtils {

    public static String join(List list, String joinWith){
        StringBuilder stringBuilder = new StringBuilder();
        for (Object item : list) {
            if(stringBuilder.length() > 0){
                stringBuilder.append(joinWith);
            }
            stringBuilder.append(item);
        }
        return stringBuilder.toString();
    }

    public static String csv(List list){
        return join(list, ",");
    }
}
