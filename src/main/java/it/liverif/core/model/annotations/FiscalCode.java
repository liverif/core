package it.liverif.core.model.annotations;

import it.liverif.core.validator.FiscalCodeValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FiscalCodeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FiscalCode {

    String message() default "{error.fiscalcode.format}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
