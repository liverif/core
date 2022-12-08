package it.liverif.core.model.validator;

import it.liverif.core.model.annotations.SingleEmail;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class SingleEmailValidator implements ConstraintValidator<SingleEmail, String> {

    private Pattern pattern;
    private String regex="^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Override
    public void initialize(SingleEmail constraintAnnotation) {
        pattern = Pattern.compile(regex);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.length() == 0) return true;
        return pattern.matcher(s).matches();
    }


}
