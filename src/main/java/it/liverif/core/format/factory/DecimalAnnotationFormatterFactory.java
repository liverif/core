package it.liverif.core.format.factory;

import it.liverif.core.format.DecimalFormatter;
import it.liverif.core.model.annotations.DecimalFormat;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import static java.util.Arrays.asList;

public class DecimalAnnotationFormatterFactory implements AnnotationFormatterFactory<DecimalFormat> {
    
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<Class<?>>(asList(new Class<?>[] {BigDecimal.class}));
    }

    @Override
    public Printer<BigDecimal> getPrinter(DecimalFormat annotation, Class<?> fieldType) {        
        return configureFormatterFrom(annotation, fieldType);
    }

    @Override
    public Parser<BigDecimal> getParser(DecimalFormat annotation, Class<?> fieldType) {        
        return configureFormatterFrom(annotation, fieldType);        
    }

    private Formatter<BigDecimal> configureFormatterFrom(DecimalFormat annotation, Class<?> fieldType) {
        if (!annotation.pattern().isEmpty()) {
            return new DecimalFormatter(annotation.pattern());
        } else {
            DecimalFormat.Style style = annotation.style();
            if (style == DecimalFormat.Style.PERCENT) {
                return new DecimalFormatter(DecimalFormatter.PERCENT_PATTERN);
            } else if (style == DecimalFormat.Style.CURRENCY) {
                return new DecimalFormatter(DecimalFormatter.CURRENCY_PATTERN);
            } else {
                return new DecimalFormatter(DecimalFormatter.NUMBER_PATTERN);
            }
        }
    }
    
}
