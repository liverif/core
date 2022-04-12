package it.liverif.core.controller;

import it.liverif.core.auth.beans.UserToken;
import it.liverif.core.controller.beans.ActionBean;
import it.liverif.core.controller.beans.GenericParametersBean;
import it.liverif.core.controller.beans.StackWebBean;
import it.liverif.core.controller.beans.StackWebConfig;
import it.liverif.core.repository.AModelBean;
import it.liverif.core.utils.CommonUtils;
import it.liverif.core.view.detail.ADetailResponse;
import it.liverif.core.view.list.AListResponse;
import it.liverif.core.web.Notification;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.LocaleResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.Locale;

public class ABaseController <T extends AModelBean, R extends AListResponse,P extends ADetailResponse> {

    public static final String REQUEST_GENERIC_PARAMETERS = "g";
    public static final String FORM_MODEL_ACTION = "model_action";

    public static final String ACTION_ADD = "action_add";
    public static final String ACTION_SAVE = "action_save";
    public static final String ACTION_UPDATE = "action_update";
    public static final String ACTION_DELETE = "action_delete";

    public static final String METHOD_LIST = "list";
    public static final String METHOD_DETAIL = "detail";
    public static final String METHOD_EXECUTE = "execute";

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    protected StackWebEngine stackWebEngine;

    @Autowired
    protected Notification notification;

    @Autowired
    protected StackWebConfig stackWebConfig;

    @Autowired
    protected JsonAction jsonAction;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected LocaleResolver localeResolver;

    @Autowired
    protected Environment environment;

    protected String baseTemplate() {
        return stackWebConfig.getBaseTemplateModelPages();
    }

    protected String message(String label) {
        return messageSource.getMessage(label, new String[]{""}, LocaleContextHolder.getLocale());
    }

    protected String message(String label, String... params) {
        return messageSource.getMessage(label, params, LocaleContextHolder.getLocale());
    }
    
    protected String selectPage() {
        StackWebBean sb = (StackWebBean) request.getAttribute(StackWebEngine.STACKWEB);
        String result = sb.getParams().get(StackWebBean.PARAMETER_PAG);
        return result;
    }

    protected String selectField() {
        StackWebBean sb = (StackWebBean) request.getAttribute(StackWebEngine.STACKWEB);
        String result = sb.getParams().get(StackWebBean.PARAMETER_FIELD);
        return result;
    }

    protected Long selectId() {
        StackWebBean sb = (StackWebBean) request.getAttribute(StackWebEngine.STACKWEB);
        String result = sb.getParams().get(StackWebBean.PARAMETER_ID);
        if (result == null) return null;
        return Long.valueOf(result);
    }

    protected String selectUsername() {
        StackWebBean sb = (StackWebBean) request.getAttribute(StackWebEngine.STACKWEB);
        String result = sb.getParams().get(StackWebBean.PARAMETER_USERNAME);
        return result;
    }

    protected String stackAction() {
        String action = (String) request.getAttribute(StackWebEngine.STACKWEB_ACTION);
        return action;
    }

    protected ActionBean formAction() {
        String caction = request.getParameter(FORM_MODEL_ACTION);
        ActionBean action = jsonAction.decodeAction(caction);
        return action;
    }

    protected String createGenericParameters(Pair<String,String> ...parameters){
        GenericParametersBean genericParametersBean=new GenericParametersBean();
        for(Pair<String,String> param:parameters){
            genericParametersBean.getParams().put(param.getFirst(),param.getSecond());
        }
        return jsonAction.encode(genericParametersBean);
    }

    protected GenericParametersBean requestGenericParameters(boolean validateSession) throws Exception {
        String generic = request.getParameter(REQUEST_GENERIC_PARAMETERS);
        GenericParametersBean genericParameters = jsonAction.decodeGenericParameters(generic);
        Long validator = (Long) request.getSession().getAttribute(JsonAction.SESSION_VALIDATOR);
        if (validateSession && !validator.toString().equals(genericParameters.getValidator())) {
            String val = genericParameters.getValidator();
            throw new Exception("Not valid session: genericParameters.validator=" + val + " session.validator=" + validator);
        } else {
            return genericParameters;
        }
    }

    protected StackWebBean getStackWebBean() {
        StackWebBean sb = (StackWebBean) request.getAttribute(StackWebEngine.STACKWEB);
        if (request.getAttribute(StackWebEngine.STACKWEB) == null) sb = new StackWebBean();
        return sb;
    }

    protected String path() {
        return request.getContextPath();
    }

    protected boolean containHttpSession(String attribute) {
        return request.getSession().getAttribute(attribute) != null;
    }

    protected boolean notContainHttpSession(String attribute) {
        return request.getSession().getAttribute(attribute) == null;
    }

    protected void setLanguage(Locale locale) {
        localeResolver.setLocale(request, response, locale);
    }

    protected String getLanguage() {
        return localeResolver.resolveLocale(request).getLanguage();
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

    protected R getListResponse() throws Exception{
        return (R) getListResponse(modelName());
    }

    protected AListResponse getListResponse(String modelName){
        AListResponse listResponse = (AListResponse) getHttpSession(AListResponse.SESSION_LIST_RESPONSE_PREFIX + modelName);
        return listResponse;
    }

    protected void removeListResponse(String modelName){
        removeHttpSession(AListResponse.SESSION_LIST_RESPONSE_PREFIX + modelName);
    }

    protected ADetailResponse getDetailResponse(String modelName) throws Exception{
        ADetailResponse detailResponse = (ADetailResponse) getHttpSession(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName);
        return detailResponse;
    }

    protected void removeDetailResponse(String modelName){
        removeHttpSession(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName);
    }

    protected P getDetailResponse() throws Exception{
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
    
    protected String publicRepository(){
        return environment.getProperty("public.repository.files");
    }

    protected String privateRepository(){
        return environment.getProperty("private.repository.files");
    }
    
    protected boolean existStackRequestParameter(){
        return StringUtils.hasText(request.getParameter("s"));
    }
    
    protected boolean entityInChange() throws Exception {
        P detailResponse=getDetailResponse();
        return (detailResponse.getRecord()!=null && detailResponse.getRecord().getId()>0L);
    }

    protected Long getId() throws Exception {
        P detailResponse=getDetailResponse();
        return detailResponse.getRecord().getId();
    }

    public static HttpEntity<byte[]> downloadFile(byte[] file, String filename, String contenttype) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(file);
        HttpHeaders header = new HttpHeaders();
        header.set(HttpHeaders.CONTENT_TYPE, contenttype);
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+filename);
        header.setContentLength(out.size());
        return new HttpEntity(out.toByteArray(), header);
    }

    protected UserToken getUser() {
        UserToken userToken=new UserToken();
        Authentication currentAuth= SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth!=null){
            Object principal = currentAuth.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetail=(UserDetails) principal;
                userToken.setUsername(userDetail.getUsername());
                for(GrantedAuthority ga: userDetail.getAuthorities()){
                    userToken.getRoles().add(ga.getAuthority().substring("ROLE_".length()));
                }
            } else {
                userToken.setUsername(principal.toString());
            }
        }
        return userToken;
    }
    
}
