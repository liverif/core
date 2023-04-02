package it.liverif.core.format;

import org.springframework.util.StringUtils;
import org.springframework.format.Formatter;
import org.springframework.lang.Nullable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

    public String pattern;

    public LocalDateTimeFormatter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public @Nullable LocalDateTime parse(String text, Locale locale) {
        if (!StringUtils.hasText(text)) return null;
        DateTimeFormatter dateFormat = createFormat(locale);
        return LocalDateTime.parse(text, dateFormat);
    }

    @Override
    public String print(final LocalDateTime object, Locale locale) {
        if (object == null) return "";
        DateTimeFormatter dateFormat = createFormat(locale);
        return dateFormat.format(object);
    }

    private DateTimeFormatter createFormat(final Locale locale) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern,locale);
        return dateFormat;
    }
}
