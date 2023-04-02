package it.liverif.core.web.component;

import it.liverif.core.repository.AModelBean;
import it.liverif.core.utils.CommonUtils;
import it.liverif.core.web.view.detail.ADetailResponse;
import it.liverif.core.web.view.list.AListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;

@Slf4j
public abstract class AMapper<T extends AModelBean, P extends ADetailResponse> {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected Notification notification;

    protected String messageRequired(String field) throws Exception {
        return messageSource.getMessage("error.field.required", new String[]{getDetailResponse().getAttribute().getLabel().get(field)}, LocaleContextHolder.getLocale());
    }

    protected Object getHttpSession(String attribute) {
        return request.getSession().getAttribute(attribute);
    }

    protected void setHttpSession(String attribute, Object object) {
        request.getSession().setAttribute(attribute, object);
    }

    protected void removeHttpSession(String attribute) {
        request.getSession().removeAttribute(attribute);
    }

    protected void removeListResponse(String modelName){
        removeHttpSession(AListResponse.SESSION_LIST_RESPONSE_PREFIX + modelName);
    }

    protected ADetailResponse getDetailResponse(String modelName) {
        ADetailResponse detailResponse = (ADetailResponse) getHttpSession(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName);
        return detailResponse;
    }

    protected void removeDetailResponse(String modelName){
        removeHttpSession(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName);
    }

    protected P getDetailResponse() {
        return (P) getDetailResponse(modelName());
    }

    protected Class<T> modelEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected String tableName(){
        return modelName().toLowerCase();
    }

    protected String modelName(){
        String className=CommonUtils.removeEntitySuffix(modelEntityClass().getSimpleName());
        return StringUtils.uncapitalize(className);
    }

    public abstract void binding(T formObj, T sessionObj) throws Exception;

    public abstract void verify(T sessionObj) throws Exception;


}
