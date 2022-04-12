package it.liverif.core.view.detail;

import it.liverif.core.auth.beans.UserToken;
import it.liverif.core.controller.ABaseController;
import it.liverif.core.controller.JsonAction;
import it.liverif.core.controller.StackWebEngine;
import it.liverif.core.controller.beans.ActionBean;
import it.liverif.core.controller.beans.GenericParametersBean;
import it.liverif.core.controller.beans.StackWebBean;
import it.liverif.core.controller.beans.StackWebConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public abstract class AView {

    public static final String BREADCRUMB_HOME = "breadcrumb_home";
    public static final String BREADCRUMB_BACK = "breadcrumb_back";
    public static final String BREADCRUMB_REFRESH = "breadcrumb_refresh";

    public static final String ACTION_LIST_SEARCH="action_list_search";
    public static final String ACTION_LIST_SEARCH_RESET="action_list_search_reset";

    public static final String ACTION_ADD="action_add";
    public static final String ACTION_SAVE="action_save";
    public static final String ACTION_UPDATE="action_update";
    public static final String ACTION_DELETE="action_delete";

    public static final String FORM_SEARCH_LINK="form_search_link";
    public static final String S_FORM_DETAIL="s_form_detail";

    @Autowired
    protected JsonAction jsonAction;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected StackWebConfig stackWebConfig;

    protected String message(String label) {
        return messageSource.getMessage(label, new String[]{""}, LocaleContextHolder.getLocale());
    }

    protected String message(String label, String... params) {
        return messageSource.getMessage(label, params, LocaleContextHolder.getLocale());
    }

    public String createAction(String action) {
        ActionBean actionBean = new ActionBean();
        actionBean.setAction(action);
        return jsonAction.encode(actionBean);
    }

    public String createLink(String action, String target, String targetAction, String label){
        return createLink(action,target,targetAction,label,null);
    }

    public String createLinkSkip(String target, String targetAction){
        return createLink(StackWebEngine.Action.SKIP,target,targetAction,null);
    }

    public String createLinkSkip(String target, String targetAction, Pair<String,String>...params){
        return createLink(StackWebEngine.Action.SKIP,target,targetAction,null,params);
    }

    public String createLinkClean(){
        StackWebBean stackWebBean=new StackWebBean();
        stackWebBean.setStackAction(StackWebEngine.Action.CLEAN);
        return jsonAction.encode(stackWebBean);
    }

    public String createLinkClean(Pair<String,String>...params){
        StackWebBean stackWebBean=new StackWebBean();
        stackWebBean.setStackAction(StackWebEngine.Action.CLEAN);
        if (params!=null) {
            for (Pair<String, String> pair : params) {
                stackWebBean.getParams().put(pair.getFirst(), pair.getSecond());
            }
        }
        return jsonAction.encode(stackWebBean);
    }
    
    public String createLinkBack(){
        StackWebBean stackWebBean=new StackWebBean();
        stackWebBean.setStackAction(StackWebEngine.Action.BACK);
        return jsonAction.encode(stackWebBean);
    }

    public String createLinkRefresh(){
        StackWebBean stackWebBean=new StackWebBean();
        stackWebBean.setStackAction(StackWebEngine.Action.REFRESH);
        return jsonAction.encode(stackWebBean);
    }

    public String createLink(String action, String target, String targetAction, String label, Pair<String,String>...params){
        StackWebBean stackWebBean=new StackWebBean();
        stackWebBean.setStackAction(action);
        stackWebBean.setTarget(target);
        stackWebBean.setTargetAction(targetAction);
        stackWebBean.setLabel(label);
        if (params!=null) {
            for (Pair<String, String> pair : params) {
                stackWebBean.getParams().put(pair.getFirst(), pair.getSecond());
            }
        }
        return jsonAction.encode(stackWebBean);
    }

    protected String createGenericParameters(Pair<String,String> ...parameters){
        GenericParametersBean genericParametersBean=new GenericParametersBean();
        for(Pair<String,String> param:parameters){
            genericParametersBean.getParams().put(param.getFirst(),param.getSecond());
        }
        return jsonAction.encode(genericParametersBean);
    }

    protected ArrayList<Integer> linkOfPages(Integer pageSelected, Integer numPages, Integer nLinkOfPages) {
        ArrayList<Integer> result = new ArrayList<>();
        if (numPages > 1) {
            for (int i = 0; i < numPages; i++) {
                if (i > (pageSelected - (nLinkOfPages / 2) - 1) && i < (pageSelected + (nLinkOfPages / 2))) {
                    result.add(i + 1);
                }
            }
        }
        return result;
    }

    protected StackWebBean getStackWebBean() {
        StackWebBean sb = (StackWebBean) request.getAttribute(StackWebEngine.STACKWEB);
        if (request.getAttribute(StackWebEngine.STACKWEB) == null) sb = new StackWebBean();
        return sb;
    }

    protected String path(){
        return request.getContextPath();
    }
    
    protected Object getHttpSession(String attribute) {
        return this.request.getSession().getAttribute(attribute);
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

    protected GenericParametersBean requestGenericParameters(boolean validateSession) throws Exception {
        String generic = request.getParameter(ABaseController.REQUEST_GENERIC_PARAMETERS);
        GenericParametersBean genericParameters = jsonAction.decodeGenericParameters(generic);
        Long validator = (Long) request.getSession().getAttribute(JsonAction.SESSION_VALIDATOR);
        if (validateSession && !validator.toString().equals(genericParameters.getValidator())) {
            String val = genericParameters.getValidator();
            throw new Exception("Not valid session: genericParameters.validator=" + val + " session.validator=" + validator);
        } else {
            return genericParameters;
        }
    }
    
}
