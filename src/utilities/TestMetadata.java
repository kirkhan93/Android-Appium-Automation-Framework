package utilities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Artur Spirin on 11/5/15.
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestMetadata {

    String name() default "";
    String id() default "AC4-000";
    String expected() default "";
    String jira() default "";
}
