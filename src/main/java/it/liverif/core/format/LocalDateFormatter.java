package it.liverif.core.format;

import org.springframework.util.StringUtils;
import org.springframework.format.Formatter;
import org.springframework.lang.Nullable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {

    public String pattern;

    public LocalDateFormatter(String pattern) {
        this.pattern = pattern;
    }
    
    @Override
    public @Nullable LocalDate parse(String text, Locale locale) {
        if (!StringUtils.hasText(text)) return null;
        DateTimeFormatter dateFormat = createFormat(locale);
        return LocalDate.parse(text,dateFormat);
    }

    @Override
    public String print(final LocalDate object, Locale locale) {
        if (object == null) return "";
        DateTimeFormatter dateFormat = createFormat(locale);
        return dateFormat.format(object);
    }

    private DateTimeFormatter createFormat(final Locale locale) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern,locale);
        return dateFormat;
    }
    
}
