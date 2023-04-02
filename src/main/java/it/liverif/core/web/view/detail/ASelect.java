package it.liverif.core.web.view.detail;

import it.liverif.core.auth.AUserAuth;
import it.liverif.core.repository.AModelBean;
import it.liverif.core.utils.CommonUtils;
import it.liverif.core.web.view.list.AListResponse;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;

public class ASelect<T extends AModelBean,P extends ADetailResponse> extends AUserAuth {

    @Autowired
    MessageSource messageSource;

    @Autowired
    protected HttpServletRequest request;
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
    
    protected String message(String label){
        return messageSource.getMessage(label,new String[]{""}, LocaleContextHolder.getLocale());
    }
}
