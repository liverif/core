package it.liverif.core.format;

import org.springframework.util.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;
import org.springframework.lang.Nullable;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {

    public String pattern;

    public LocalTimeFormatter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public @Nullable LocalTime parse(String text, Locale locale) throws ParseException {
        if (!StringUtils.hasText(text)) return null;
        DateTimeFormatter dateFormat = createFormat(locale);
        return LocalTime.parse(text, dateFormat);
    }

    @Override
    public String print(final LocalTime object, Locale locale) {
        if (object == null) return "";
        DateTimeFormatter dateFormat = createFormat(locale);
        return dateFormat.format(object);
    }

    private DateTimeFormatter createFormat(final Locale locale) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern,locale);
        return dateFormat;
    }
}
