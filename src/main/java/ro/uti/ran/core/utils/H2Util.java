package ro.uti.ran.core.utils;

import java.util.Date;
import java.util.Objects;

/**
 * Created by miroslav.rusnac on 11/02/2016.
 */
public class H2Util {

    public static Date nvl(Date value,Date result){
        if(value==null){
            return result;
        }
        else{
            return value;
        }
    }

}
