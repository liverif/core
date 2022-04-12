package it.liverif.core.model.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface DecimalFormat {
    
    DecimalFormat.Style style() default Style.NUMBER;
    
    String pattern() default "";
    
    public static enum Style {
        NUMBER,
        PERCENT,
        CURRENCY
    }
    
}
