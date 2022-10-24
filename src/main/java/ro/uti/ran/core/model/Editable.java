package ro.uti.ran.core.model;

import java.util.Date;

/**
 * Created by miroslav.rusnac on 19/05/2016.
 */
public interface Editable {
    String getCodePrim() ;
    String getCodePrimName();
    Integer getCodeSec();
    String getCodeSecName();
    Date getStarting();

}
