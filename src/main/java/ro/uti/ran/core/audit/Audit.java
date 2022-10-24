package ro.uti.ran.core.audit;

import java.lang.annotation.*;


/**
 * Created by Stanciu Neculai on 04.Jan.2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Audit {
    AuditOpType opType();
}
