package it.liverif.core.format;

import org.springframework.util.StringUtils;
import org.springframework.format.Formatter;
import org.springframework.lang.Nullable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class DecimalFormatter implements Formatter<BigDecimal> {
    
    public static final String CURRENCY_PATTERN="###,###,###,###,##0.00";
    public static final String PERCENT_PATTERN="##0.00";
    public static final String NUMBER_PATTERN="###,###,###,###,##0";

    public String pattern;

    public DecimalFormatter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public @Nullable BigDecimal parse(final String text, final Locale locale) throws ParseException {
        if (!StringUtils.hasText(text)) return null;
        DecimalFormat decimalFormat = createFormat(locale);        
        BigDecimal temp =  (BigDecimal) decimalFormat.parse(text);
        return temp;
    }

    @Override
    public String print(final BigDecimal object, final Locale locale) {
        if (object == null) return "";
        DecimalFormat decimalFormat = createFormat(locale);
        return decimalFormat.format(object);
    }
    
    private DecimalFormat createFormat(final Locale locale) {
        DecimalFormatSymbols mySymbols = new DecimalFormatSymbols(locale);
        mySymbols.setDecimalSeparator(',');
        mySymbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat(pattern,mySymbols);
        decimalFormat.setParseBigDecimal(true);
        return decimalFormat;
    }
    
    
}
