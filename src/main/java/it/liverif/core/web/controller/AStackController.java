package it.liverif.core.web.controller;

import it.liverif.core.repository.AModelBean;
import it.liverif.core.web.beans.ActionBean;
import it.liverif.core.web.beans.SearchField;
import it.liverif.core.web.view.AAction;
import it.liverif.core.web.view.AAttribute;
import it.liverif.core.web.view.ALabelFromMessage;
import it.liverif.core.web.view.detail.ADetailResponse;
import it.liverif.core.web.view.list.AListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Slf4j
public abstract class AStackController<T extends AModelBean,L extends AAttribute, R extends AListResponse,P extends ADetailResponse, M extends ALabelFromMessage> extends ABaseController<T,R,P> {
    
    public static final String PREFIX_FIELD="F_";
    
    @Autowired
    M message;

    protected L createAttribute() throws Exception {
        return attributeClass().getConstructor().newInstance();
    }

    protected R resetList(List<SearchField> listSearch) throws Exception {
        L attribute=createAttribute();
        R listResponse = listResponseClass().getConstructor(attribute.getClass()).newInstance(attribute);
        setHttpSession(AListResponse.SESSION_LIST_RESPONSE_PREFIX + modelName(), listResponse);
        listResponse.getSearchFields().addAll(listSearch);
        message.init(attribute);
        return listResponse;
    }

    protected R initList(ModelMap model, List<SearchField> listSearch) throws Exception {
        ActionBean action=formAction();
        log.debug("formAction="+action.getAction());

        L attribute=createAttribute();
        R listResponse;
        if (notContainHttpSession(AListResponse.SESSION_LIST_RESPONSE_PREFIX + modelName()) ||
                StackWebEngine.Action.REFRESH.equals(stackAction()) ||
                AAction.ACTION_LIST_SEARCH_RESET.equals(action.getAction())
        ) {
            listResponse = listResponseClass().getConstructor(attribute.getClass()).newInstance(attribute);
            setHttpSession(AListResponse.SESSION_LIST_RESPONSE_PREFIX + modelName(), listResponse);
            listResponse.getSearchFields().addAll(listSearch);
            message.init(attribute);
        } else {
            listResponse = (R) getHttpSession(AListResponse.SESSION_LIST_RESPONSE_PREFIX + modelName());
            for (String field:  listResponse.getAttribute().getFields()) {
                listResponse.getAttribute().getParams().access().put(field,attribute.getParams().access().get(field));
            }
        }
        
        if (!listResponse.getSearchFields().isEmpty()) {

            if (AAction.ACTION_LIST_SEARCH.equals(action.getAction())) {
                for (int i = 0; i < listResponse.getSearchFields().size(); i++) {
                    SearchField searchField=((SearchField) listResponse.getSearchFields().get(i));
                    String op = request.getParameter("operation_" + i);
                    String v1 = request.getParameter("value_" + i);
                    String v2 = request.getParameter("value2_" + i);
                    searchField.setOperation(op);

                    if (op.equals(SearchField.EMPTY) || op.equals(SearchField.DIFFERENT_FROM_EMPTY)) {
                        v1 = null;
                        v2 = null;
                    } else if (op.equals(SearchField.BETWEEN)) {
                        // Non fare nulla
                    } else {
                        v2 = null;
                    }
                    searchField.setValue(v1);
                    searchField.setValue2(v2);
                }
            }
        }

        String selectPage=selectPage();
        if (selectPage!=null){
            log.debug("Select page:"+selectPage);
            listResponse.setSelectPage(Integer.valueOf(selectPage));
        }
        
        removeHttpSession(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName());
        
        listResponse.getRecords().clear();
        listResponse.getCssRow().clear();
        return listResponse;
    }

    protected P initDetail() throws Exception {
        L attribute=createAttribute();
        P detailResponse;

        ActionBean action=formAction();
        log.debug("formAction="+action.getAction());

        if (notContainHttpSession(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName()) ||
                StackWebEngine.Action.REFRESH.equals(stackAction())
        ) {
            detailResponse = detailResponseClass().getConstructor(attribute.getClass()).newInstance(attribute);
            request.getSession().setAttribute(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName(), detailResponse);
            message.init(attribute);
        } else {
            detailResponse = (P) getHttpSession(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName());
            for (String field: detailResponse.getAttribute().getFields()) {
                getDetailResponse().getAttribute().getParams().access().put(field,attribute.getParams().access().get(field));
            }
        }
        
        return detailResponse;
    }
    
    protected void setDetailMenuCrudAction(List<String> menuCrudAction){
        P detailResponse = (P) getHttpSession(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName());
        detailResponse.getMenuCrudAction().clear();
        if (menuCrudAction!=null) detailResponse.getMenuCrudAction().addAll(menuCrudAction);
    }
    
    protected void setDetailMenuAction(List<String> menuAction){
        P detailResponse = (P) getHttpSession(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX + modelName());
        detailResponse.getMenuAction().clear();
        if (menuAction!=null) detailResponse.getMenuAction().addAll(menuAction);
    }

    protected void setListMenuCrudAction(List<String> menuCrudAction){
        R listResponse = (R) getHttpSession(AListResponse.SESSION_LIST_RESPONSE_PREFIX + modelName());
        listResponse.getMenuCrudAction().clear();
        if (menuCrudAction!=null) listResponse.getMenuCrudAction().addAll(menuCrudAction);
    }

    protected void setListMenuAction(List<String> menuAction){
        R listResponse = (R) getHttpSession(AListResponse.SESSION_LIST_RESPONSE_PREFIX + modelName());
        listResponse.getMenuAction().clear();
        if (menuAction!=null) listResponse.getMenuAction().addAll(menuAction);
    }

    protected Class<L> attributeClass(){
        return (Class<L>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    protected Class<R> listResponseClass(){
        return (Class<R>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];
    }

    protected Class<P> detailResponseClass(){
        return (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
    }
    
}
