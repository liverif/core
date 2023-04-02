package it.liverif.core.auth;

import it.liverif.core.auth.beans.UserToken;
import it.liverif.core.web.beans.StackWebBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import javax.servlet.http.HttpServletRequest;

public abstract class ACentralPolicy {

    public static final String REQUEST_POLICY="request_policy";

    @Autowired
    protected HttpServletRequest request;

    public abstract boolean execute(UserToken userToken, StackWebBean stackWebBean) throws Exception;

    public abstract boolean menuAccess(UserToken userToken, String target, String targetAction) throws Exception;

    public boolean execute(UserToken userToken, String target, String targetAction) throws Exception {
        return execute(userToken, target, targetAction, null);
    }

    public boolean execute(UserToken userToken, String target, String targetAction, Pair<String,String>...params) throws Exception {
        StackWebBean stackWebBean=new StackWebBean();
        stackWebBean.setTarget(target);
        stackWebBean.setTargetAction(targetAction);
        if (params!=null) {
            for (Pair<String, String> pair : params) {
                stackWebBean.getParams().put(pair.getFirst(), pair.getSecond());
            }
        }
        return execute(userToken,stackWebBean);
    }

    public String getPolicy(){
        if (request.getAttribute(ACentralPolicy.REQUEST_POLICY)!=null)
            return (String) request.getAttribute(ACentralPolicy.REQUEST_POLICY);
        else
            return "";
    }

    public void setPolicy(String policy){
        request.setAttribute(REQUEST_POLICY, policy);
    }

}
