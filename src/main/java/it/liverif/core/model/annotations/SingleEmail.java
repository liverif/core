package it.liverif.core.model.annotations;

import it.liverif.core.validator.SingleEmailValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SingleEmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SingleEmail {

    String message() default "{error.mail.format}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
