package it.liverif.core.validator;

import it.liverif.core.repository.AModelBean;
import it.liverif.core.utils.CommonUtils;
import it.liverif.core.view.detail.ADetailResponse;
import it.liverif.core.view.list.AListResponse;
import it.liverif.core.web.AMapper;
import org.springframework.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.*;
import it.liverif.core.web.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;

@Component
public abstract class AValidator<T extends AModelBean, P extends ADetailResponse, N extends AMapper<T,P>> {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected Notification notification;

    @Autowired
    protected MessageSource messageSource;
    
    @Autowired
    private LocalValidatorFactoryBean validator;

    @Autowired
    private N mapper;

    public void validate(T obj,Errors errorsRequestModel) throws Exception {
        mapper.verify(obj);
        createMessageErrors(errorsRequestModel);
        T newObj=modelEntityClass().getConstructor().newInstance();
        BeanUtils.copyProperties(obj,newObj);
        Errors errorsSessionModel = new BeanPropertyBindingResult(newObj, tableName());
        validator.validate(newObj,errorsSessionModel);
        createMessageErrors(errorsSessionModel);
        this.customValidate(obj);
    }
    
    protected abstract void customValidate(T obj) throws Exception;

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

    public void createMessageErrors(Errors errors) {
        for (FieldError e : errors.getFieldErrors()) {
            String field = e.getField();
            String fieldName = messageSource.getMessage("label." + tableName() + "." + field.toLowerCase(), null, LocaleContextHolder.getLocale());
            String code = e.getCode().toLowerCase();
            String message = messageSource.getMessage("error.validator." + code, e.getArguments(), LocaleContextHolder.getLocale());
            notification.addError("<b>" + fieldName + ":</b> " + message);
        }
        for (ObjectError e : errors.getGlobalErrors()) {
            String code = e.getCode().toLowerCase();
            String message = messageSource.getMessage("globalerror.validator." + code, new String[]{e.getDefaultMessage()}, LocaleContextHolder.getLocale());
            notification.addError(message);
        }
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

    protected ADetailResponse getDetailResponse(String modelName) throws Exception{
        ADetailResponse detailResponse = (ADetailResponse) getHttpSession(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName);
        return detailResponse;
    }

    protected String message(String label) {
        return messageSource.getMessage(label, new String[]{""}, LocaleContextHolder.getLocale());
    }

    protected String message(String label, String... params) {
        return messageSource.getMessage(label, params, LocaleContextHolder.getLocale());
    }

    protected String messageRequired(String field) throws Exception {
        return this.messageSource.getMessage("error.field.required", new String[]{(String)this.getDetailResponse().getAttribute().getLabel().get(field)}, LocaleContextHolder.getLocale());
    }

    protected String messageNotValid(String field) throws Exception {
        return this.messageSource.getMessage("error.field.notvalid", new String[]{(String)this.getDetailResponse().getAttribute().getLabel().get(field)}, LocaleContextHolder.getLocale());
    }

    protected P getDetailResponse() throws Exception{
        return (P) getDetailResponse(modelName());
    }

    protected boolean entityInChange(T entityObj) throws Exception {
        return (entityObj!=null && entityObj.getId()>0L);
    }


}
