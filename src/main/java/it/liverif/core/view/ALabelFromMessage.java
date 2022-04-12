package it.liverif.core.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public abstract class ALabelFromMessage<L extends AAttribute> {

    @Autowired
    protected MessageSource messageSource;
    
    public abstract void init(L attribute);

    protected String message(String label) {
        return messageSource.getMessage(label, new String[]{""}, LocaleContextHolder.getLocale());
    }

    protected String message(String label, String... params) {
        return messageSource.getMessage(label, params, LocaleContextHolder.getLocale());
    }

}
