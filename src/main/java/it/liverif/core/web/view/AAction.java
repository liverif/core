package it.liverif.core.web.view;

import it.liverif.core.auth.ACentralPolicy;
import it.liverif.core.repository.AModelBean;
import it.liverif.core.utils.CommonUtils;
import it.liverif.core.web.beans.FieldTypeBean;
import it.liverif.core.web.beans.StackWebBean;
import it.liverif.core.web.controller.StackWebEngine;
import it.liverif.core.web.view.detail.ADetailResponse;
import it.liverif.core.web.view.detail.AView;
import it.liverif.core.web.view.detail.DataAccess;
import it.liverif.core.web.view.list.AListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.ui.ModelMap;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AAction<T extends AModelBean, L extends AAttribute, R extends AListResponse, P extends ADetailResponse> extends AView {

    @Autowired
    protected Environment environment;

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

    protected Object getHttpSession(String attribute) {
        return request.getSession().getAttribute(attribute);
    }

    protected void setHttpSession(String attribute, Object object) {
        request.getSession().setAttribute(attribute, object);
    }

    protected void removeHttpSession(String attribute) {
        request.getSession().removeAttribute(attribute);
    }

    protected R getListResponse() {
        return (R) getListResponse(modelName());
    }

    protected AListResponse getListResponse(String modelName){
        AListResponse listResponse = (AListResponse) getHttpSession(AListResponse.SESSION_LIST_RESPONSE_PREFIX + modelName);
        return listResponse;
    }

    protected void removeListResponse(String modelName){
        removeHttpSession(AListResponse.SESSION_LIST_RESPONSE_PREFIX + modelName);
    }

    protected ADetailResponse getDetailResponse(String modelName){
        ADetailResponse detailResponse = (ADetailResponse) getHttpSession(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName);
        return detailResponse;
    }

    protected void removeDetailResponse(String modelName){
        removeHttpSession(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName);
    }

    protected P getDetailResponse() {
        return (P) getDetailResponse(modelName());
    }

    protected StackWebBean getStackWebBean() {
        StackWebBean sb = (StackWebBean) request.getAttribute(StackWebEngine.STACKWEB);
        if (request.getAttribute(StackWebEngine.STACKWEB) == null) sb = new StackWebBean();
        return sb;
    }
    
    public abstract void initDetail(ModelMap model) throws Exception;

    public abstract void initList(ModelMap model) throws Exception;

    public abstract void menuDetail(ModelMap model) throws Exception;

    public abstract void menuContextDetail(ModelMap model) throws Exception;

    public abstract void menuCrudActionDetail(ModelMap model) throws Exception;

    public abstract void menuList(ModelMap model) throws Exception;

    public abstract void menuContextList(ModelMap model) throws Exception;

    public abstract void menuCrudActionList(ModelMap model) throws Exception;

    public abstract Pair<List<String>,String> rowActionList(L attribute,ModelMap model, T bean, String[] params) throws Exception;

    protected boolean entityInChange() {
        P detailResponse=getDetailResponse();
        return (detailResponse.getRecord()!=null && detailResponse.getRecord().getId()>0L);
    }

    protected void fieldNoNewRow(String field) {
        getDetailResponse().getAttribute().getParams().newrow().put(field,false);
    }

    protected void fieldNewRow(String field) {
        getDetailResponse().getAttribute().getParams().newrow().put(field,true);
    }

    protected void fieldReadonly(String field) {
        getDetailResponse().getAttribute().getParams().access().put(field, DataAccess.VIEW_READONLY);
    }

    protected void fieldNoView(String field) {
        getDetailResponse().getAttribute().getParams().newrow().put(field,true);
        getDetailResponse().getAttribute().getParams().access().put(field, DataAccess.NULLSTYLE);
    }

    protected void fieldViewData(String field) {
        getDetailResponse().getAttribute().getParams().access().put(field, DataAccess.VIEW_DATA);
    }

    protected void fieldViewRequiredData(String field) {
        getDetailResponse().getAttribute().getParams().access().put(field, DataAccess.VIEW_REQUIRED_DATA);
    }

    public void fieldAllFieldsToReadonly() {
        for (String field: getDetailResponse().getAttribute().getFields()) {
            DataAccess dataAccess=getDetailResponse().getAttribute().getParams().access().get(field);
            if (dataAccess.equals(DataAccess.VIEW_DATA) ||
                    dataAccess.equals(DataAccess.VIEW_REQUIRED_DATA)){
                getDetailResponse().getAttribute().getParams().access().put(field, DataAccess.VIEW_READONLY);
            }else if (dataAccess.equals(DataAccess.HIDDEN_DATA)){
                getDetailResponse().getAttribute().getParams().access().put(field, DataAccess.HIDDEN);
            }
        }
    }

    public void fieldAllFieldsWitdhLimit(Integer limit) {
        AAttribute attribute=getDetailResponse().getAttribute();
        HashMap<String, FieldTypeBean> types=attribute.getType();
        for (Iterator it = types.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            String field = (String) entry.getKey();
            FieldTypeBean type=(FieldTypeBean) entry.getValue();
            if (type.getType().equals(FieldTypeBean.Type.TEXT) ||
                    type.getType().equals(FieldTypeBean.Type.TEXTAREA)){
                if(attribute.getParams().colsize().get(field)>limit)
                    attribute.getParams().colsize().put(field,limit);
            }
        }
    }


    public void fieldAllFieldsToNullStyle() {
        for (String field:  getDetailResponse().getAttribute().getFields()) {
            getDetailResponse().getAttribute().getParams().access().put(field, DataAccess.NULLSTYLE);
        }
    }

    protected Long getId() throws Exception {
        P detailResponse=getDetailResponse();
        return detailResponse.getRecord().getId();
    }

    protected boolean containHttpSession(String attribute) {
        return request.getSession().getAttribute(attribute) != null;
    }

    protected boolean notContainHttpSession(String attribute) {
        return request.getSession().getAttribute(attribute) == null;
    }

    protected String getPolicy(){
        if (request.getAttribute(ACentralPolicy.REQUEST_POLICY)!=null)
            return (String) request.getAttribute(ACentralPolicy.REQUEST_POLICY);
        else
            return "";
    }

}
