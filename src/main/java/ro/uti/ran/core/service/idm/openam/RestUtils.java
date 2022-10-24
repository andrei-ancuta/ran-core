package ro.uti.ran.core.service.idm.openam;

import org.springframework.http.HttpStatus;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-14 09:50
 */
public class RestUtils {

    public static boolean isError(HttpStatus status) {
        HttpStatus.Series series = status.series();
        return (HttpStatus.Series.CLIENT_ERROR.equals(series)
                || HttpStatus.Series.SERVER_ERROR.equals(series));
    }
}
