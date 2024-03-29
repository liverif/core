package it.liverif.core.web.controller;

import it.liverif.core.exeptions.StackWebException;
import it.liverif.core.utils.CommonUtils;
import it.liverif.core.utils.RandomStringUtils;
import it.liverif.core.web.beans.StackWebBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Component
public class StackWebEngine {

    public static final String OVERWRITE_REQUEST_STACKWEB = "overwrite_stackweb";
    public static final String REQUEST_STACKWEB = "s";
    public static final String STACKWEB = "stackweb";
    public static final String STACKWEB_ACTION = "stackweb_action";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JsonAction jsonAction;

    public static final class Action {
        public static final String ADD = "add";
        public static final String SKIP = "skip";
        public static final String CADD = "cadd";
        public static final String BACK = "back";
        public static final String CLEAN = "clean";
        public static final String REFRESH = "refresh";
    }

    private void refresh() {
        log.debug("IN");
        StackWebBean sb;
        ArrayList<StackWebBean> stack = (ArrayList) request.getSession().getAttribute(STACKWEB);
        if (stack.size() > 0) {
            sb = stack.get(stack.size() - 1);
            request.setAttribute(STACKWEB, sb);
        } else {
            clean();
        }
    }

    private void back() {
        log.debug("IN");
        ArrayList<StackWebBean> stack = (ArrayList) request.getSession().getAttribute(STACKWEB);
        if (stack.size() > 0) {
            StackWebBean sb = stack.get(stack.size() - 1);
            stack.remove(stack.size() - 1);
            if (stack.size() > 0) {
                sb = stack.get(stack.size() - 1);
                request.setAttribute(STACKWEB, sb);
            } else {
                clean();
            }
        } else {
            clean();
        }
    }

    private void skip(StackWebBean sb) throws StackWebException {
        log.debug("IN");
        ArrayList<StackWebBean> stack = (ArrayList) request.getSession().getAttribute(STACKWEB);
        Set<String> memory = new HashSet();
        if (stack.size() > 0) {
            for (int i = 0; i < stack.size(); i++) {
                StackWebBean sbm = stack.get(i);
                memory.add(sbm.getTarget()+"#"+sbm.getTargetAction());
            }
        }
        if (memory.contains(sb.getTarget()+"#"+sb.getTargetAction())) {
            int search = -1;
            for (int i = stack.size() - 1; i >= 0; i--) {
                StackWebBean sbm = stack.get(i);
                if ((sbm.getTarget()+"#"+sbm.getTargetAction()).equals(sb.getTarget()+"#"+sb.getTargetAction())) {
                    search = i;
                    for (Iterator it = sb.getParams().entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry entry = (Map.Entry)it.next();
                        String key =(String) entry.getKey();
                        String value =(String) entry.getValue();
                        if(!key.equals(StackWebBean.PARAMETER_ACTION)) sbm.getParams().put(key,value);
                    }
                    for (Iterator it = sbm.getParams().entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry entry = (Map.Entry)it.next();
                        String key =(String) entry.getKey();
                        String value =(String) entry.getValue();
                        sb.getParams().put(key,value);
                    }
                    request.setAttribute(STACKWEB, sb);
                    break;
                }
            }
            if (search == -1) {
                clean();
                throw new StackWebException("StackWeb not found: "+sb.getTarget()+"#"+sb.getTargetAction());
            } else {
                if (stack.size() > 0) {
                    for (int i = stack.size() - 1; i > search; i--) {
                        stack.remove(i);
                    }
                }
            }
        } else {
            clean();
            throw new StackWebException("StackWeb error: "+sb.getTarget()+"#"+sb.getTargetAction());
        }
    }

    private void addOrUpdate(StackWebBean sb) throws StackWebException {
        log.debug("IN");
        ArrayList<StackWebBean> stack = (ArrayList) request.getSession().getAttribute(STACKWEB);
        Set<String> memory = new HashSet();
        if (stack.size() > 0) {
            for (int i = 0; i < stack.size(); i++) {
                StackWebBean sbm = stack.get(i);
                memory.add(sbm.getTarget()+"#"+sbm.getTargetAction());
            }
        }
        if (memory.contains(sb.getTarget()+"#"+sb.getTargetAction())) {
            int search = -1;
            for (int i = stack.size() - 1; i >= 0; i--) {
                StackWebBean sbm = stack.get(i);
                if ((sbm.getTarget()+"#"+sbm.getTargetAction()).equals(sb.getTarget()+"#"+sb.getTargetAction())) {
                    search = i;
                    break;
                }
            }
            if (search == -1) {
                clean();
                throw new StackWebException("StackWeb not found: "+sb.getTarget()+"#"+sb.getTargetAction());
            } else {
                if (stack.size() > 0) {
                    for (int i = stack.size() - 1; i >= search; i--) {
                        stack.remove(i);
                    }
                }
            }
        }
        StackWebBean sbCopy=new StackWebBean();
        BeanUtils.copyProperties(sb,sbCopy);
        if(sbCopy.getParams().containsKey(StackWebBean.PARAMETER_ACTION)) sbCopy.getParams().remove(StackWebBean.PARAMETER_ACTION);
        stack.add(sbCopy);
        
        request.setAttribute(STACKWEB, sb);
    }

    public void execute() throws Exception {
        log.debug("IN");

        if (request.getSession().getAttribute(STACKWEB) == null) {
            jsonAction.initSession();
            request.getSession().setAttribute(STACKWEB, new ArrayList<StackWebBean>());
        }

        log.debug("getRequestURI=" + request.getRequestURI());
        log.debug("getQueryString=" + request.getQueryString());

        StackWebBean stackWebBean = validateStackWebFromRequest(false);
        if (request.getAttribute(OVERWRITE_REQUEST_STACKWEB)!=null){
            stackWebBean = (StackWebBean) request.getAttribute(OVERWRITE_REQUEST_STACKWEB);
        }

        if (StringUtils.hasText(stackWebBean.getStackAction())) {
            validateStackWebFromRequest(true);
            String action=stackWebBean.getStackAction();
            request.setAttribute(STACKWEB_ACTION,action);
            stackWebBean.setStackAction(null);
            stackWebBean.setLinkSkip(null);
            if (action.equals(Action.CADD)) {
                clean();
                addOrUpdate(stackWebBean);
            } else if (action.equals(Action.ADD)) {
                addOrUpdate(stackWebBean);
            } else if (action.equals(Action.SKIP)) {
                skip(stackWebBean);
            } else if (action.equals(Action.BACK)) {
                back();
            } else if (action.equals(Action.REFRESH)) {
                refresh();
            } else if (action.equals(Action.CLEAN)) {
                request.setAttribute(STACKWEB, stackWebBean);
                clean();
            }
        } else {
            clean();
        }
    }

    public Long increaseSessionValidator() {
        Long validator = (Long) request.getSession().getAttribute(JsonAction.SESSION_VALIDATOR);
        request.getSession().setAttribute(JsonAction.SESSION_VALIDATOR, validator + 1);
        return validator;
    }

    public void createAllLinkSkip(String stackContext) {
        Long validator = (Long) request.getSession().getAttribute(JsonAction.SESSION_VALIDATOR);
        List<StackWebBean> stack = (List<StackWebBean>) request.getSession().getAttribute(StackWebEngine.STACKWEB);
        for (StackWebBean stackWebBean : stack) {
            stackWebBean.setValidator(validator.toString());
            String random = RandomStringUtils.generateAlfanumeric(10);
            stackWebBean.setRandom(random);
            stackWebBean.setStackAction(Action.SKIP);
            stackWebBean.setLinkSkip(null);
            String link = jsonAction.encode(stackWebBean);
            stackWebBean.setLinkSkip(stackContext + "/?" + REQUEST_STACKWEB + "=" + link);
        }
        request.getSession().setAttribute(StackWebEngine.STACKWEB, stack);
    }

    public void clean() {
        log.debug("IN");
        ArrayList<StackWebBean> stack = (ArrayList) request.getSession().getAttribute(STACKWEB);
        if (stack != null) stack.clear();
        CommonUtils.cleanSession(request);
    }

    public StackWebBean validateStackWebFromRequest(boolean validateSession) throws Exception{
        String s = request.getParameter(REQUEST_STACKWEB);
        log.debug("s=" + s);
        StackWebBean stackWebBean= jsonAction.decodeStackWeb(s);
        log.debug("stack_action=" + stackWebBean.getStackAction());
        log.debug("stack_label=" + stackWebBean.getLabel());
        log.debug("stack_target=" + stackWebBean.getTarget());
        log.debug("stack_targetaction=" + stackWebBean.getTargetAction());
        log.debug("stack_validator=" + stackWebBean.getValidator());
        Long validator = (Long) request.getSession().getAttribute(JsonAction.SESSION_VALIDATOR);

        if (validateSession && !validator.toString().equals(stackWebBean.getValidator())) {
            throw new StackWebException("Not valid session: stackWebBean.validator="+stackWebBean.getValidator()+" session.validator="+validator);
        }
        return stackWebBean;
    }

    public StackWebBean searchStackWebBean(String target, String targetAction) {
        ArrayList<StackWebBean> stack = (ArrayList) request.getSession().getAttribute(STACKWEB);
        Set<String> memory = new HashSet();
        if (stack.size() > 0) {
            for (int i = 0; i < stack.size(); i++) {
                StackWebBean sbm = stack.get(i);
                memory.add(sbm.getTarget()+"#"+sbm.getTargetAction());
            }
        }
        if (memory.contains(target+"#"+targetAction)) {
            for (int i = stack.size() - 1; i >= 0; i--) {
                StackWebBean sbm = stack.get(i);
                if ((sbm.getTarget()+"#"+sbm.getTargetAction()).equals(target+"#"+targetAction)) {
                    return sbm;
                }
            }
        }
        return null;
    }

    public StackWebBean lastStackWebBean() {
        ArrayList<StackWebBean> stack = (ArrayList) request.getSession().getAttribute(STACKWEB);
        if(stack==null || stack.isEmpty()) return null;
        return stack.get(stack.size()-1);
    }

}
