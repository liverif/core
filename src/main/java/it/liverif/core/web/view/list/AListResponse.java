package it.liverif.core.web.view.list;

import it.liverif.core.repository.AModelBean;
import it.liverif.core.repository.TableNameReserved;
import it.liverif.core.utils.CommonUtils;
import it.liverif.core.web.beans.SearchField;
import it.liverif.core.web.view.AAttribute;
import lombok.Getter;
import lombok.Setter;
import java.lang.reflect.ParameterizedType;
import java.util.*;

@Getter
@Setter
public abstract class AListResponse<T extends AModelBean,L extends AAttribute> {
    
    public static final String SESSION_LIST_RESPONSE_PREFIX="listresponse_";

    private Long numRecords;
    private Integer selectPage=1;
    private String firstPageLink;
    private String previousPageLink;
    private Map<Integer,String> pagelink=new HashMap<>();
    private String nextPageLink;
    private String lastPageLink;
    private Long selectedId;

    private L attribute=null;

    private List<SearchField> searchFields=new ArrayList<>();
    private List<Map<String,String>> records=new ArrayList<>();
    private List<String> listColumn=new ArrayList<>();
    private Map<String,String> cssRow=new HashMap<>();
    private Map<String,String> cssColumn=new HashMap<>();

    private String htmlHeader="";
    private String htmlFooter="";

    public boolean selectedId(String id) {
        if (selectedId==null) return false;
        return selectedId.toString().equals(id);
    }

    private List<String> menuAction=new ArrayList<>();
    public String htmlMenuAction(){
        return String.join("\n",menuAction);
    }

    private List<String> menuContext=new ArrayList<>();
    public String htmlMenuContext(){
        return String.join("\n",menuContext);
    }

    private List<String> menuCrudAction=new ArrayList<>();
    public String htmlMenuCrudAction(){
        return String.join("\n",menuCrudAction);
    }
    
    protected Class<T> modelEntityClass(){
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public String tableName(){
        String className= CommonUtils.removeEntitySuffix(modelEntityClass().getSimpleName());
        return className.toLowerCase();
    }
    
    public AListResponse(L attribute){
        setAttribute(attribute);
    }
    
    public String getCssColumn(String field){
        return cssColumn.get(field)!=null?cssColumn.get(field):"";
    }

    public String getCssRow(String id){
        return cssRow.get(id)!=null?cssRow.get(id):"";
    }

}