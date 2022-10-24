package ro.uti.ran.core.business.scheduler.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AppParametruEnable {
	String value();
}
