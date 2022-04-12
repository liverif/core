package it.liverif.core.model.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldOrder {
    
    public int value() default 0;
    
}