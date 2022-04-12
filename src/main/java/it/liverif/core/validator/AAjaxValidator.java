package it.liverif.core.validator;

import it.liverif.core.repository.AModelBean;
import it.liverif.core.repository.TableNameReserved;
import it.liverif.core.utils.CommonUtils;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

public abstract class AAjaxValidator<X, T extends AModelBean> {
    
    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected MessageSource messageSource;

    protected String message(String label) {
        return messageSource.getMessage(label, new String[]{""}, LocaleContextHolder.getLocale());
    }

    protected String message(String label, String... params) {
        return messageSource.getMessage(label, params, LocaleContextHolder.getLocale());
    }

    protected Object getHttpSession(String attribute) {
        return request.getSession().getAttribute(attribute);
    }

    protected Class<T> modelEntityClass(){
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected String tableName(){
        return modelName().toLowerCase();
    }

    protected String modelName(){
        String className=CommonUtils.removeEntitySuffix(modelEntityClass().getSimpleName());
        return StringUtils.uncapitalize(className);
    }
    
    protected boolean entityInChange(T entityObj) throws Exception {
        return (entityObj!=null && entityObj.getId()>0L);
    }

    public abstract ArrayList<String> validate(X obj,T entityObj) throws Exception;
}
