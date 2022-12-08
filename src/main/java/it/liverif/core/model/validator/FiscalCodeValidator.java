package it.liverif.core.model.validator;

import it.liverif.core.model.annotations.FiscalCode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Deprecated
public class FiscalCodeValidator implements ConstraintValidator<FiscalCode, String> {

    private Pattern pattern;
    private String regex = "^([A-Z]{6}[0-9LMNPQRSTUV]{2}[ABCDEHLMPRST]{1}[0-9LMNPQRSTUV]{2}[A-Z]{1}[0-9LMNPQRSTUV]{3}[A-Z]{1})$|([0-9]{11})$";

    @Override
    public void initialize(FiscalCode constraintAnnotation) {
        pattern = Pattern.compile(regex);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.length() == 0) return true;
        return pattern.matcher(s).matches();
    }

}
