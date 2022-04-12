package it.liverif.core.view.detail;

import it.liverif.core.repository.AModelBean;
import it.liverif.core.repository.TableNameReserved;
import it.liverif.core.utils.CommonUtils;
import it.liverif.core.view.AAttribute;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class ADetailResponse<T extends AModelBean,L extends AAttribute> {

    public static final String SESSION_DETAIL_RESPONSE_PREFIX="detailresponse_";

    private T record;

    private L attribute=null;

    private Boolean viewTooltipInfo=true;

    private Integer minWidth=null;

    private String htmlHeader="";
    private String htmlFooter="";

    private Map<String,String> linkDownloadFile=new HashMap<>();

    private List<String> menuAction=new ArrayList<>();
    public String htmlMenuAction(){
        return String.join("\n",menuAction);
    }

    private List<String> menuCrudAction=new ArrayList<>();
    public String htmlmenuCrudAction(){
        return String.join("\n",menuCrudAction);
    }

    private List<String> menuContext=new ArrayList<>();
    public String htmlMenuContext(){
        return String.join("\n",menuContext);
    }

    public Class<T> modelEntityClass(){
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public String modelName(){
        String className=CommonUtils.removeEntitySuffix(modelEntityClass().getSimpleName());
        return StringUtils.uncapitalize(className);
    }

    public String tableName(){
        String className= CommonUtils.removeEntitySuffix(modelEntityClass().getSimpleName());
        return className.toLowerCase();
    }

    public ADetailResponse(L attribute){
        setAttribute(attribute);
    }

    public boolean newRow(String field){
        return attribute.getParams().newrow().get(field);
    }

    public String getTooltip(String field){
        if (attribute.getParams().tooltip().containsKey(field)) return attribute.getParams().tooltip().get(field);
        return "";
    }

    public String getPasswordinfo(String field){
        if (attribute.getParams().passwordinfo().containsKey(field)) return attribute.getParams().passwordinfo().get(field);
        return "";
    }

    public String getAjaxlink(String field){
        if (attribute.getParams().ajaxlink().containsKey(field)) return attribute.getParams().ajaxlink().get(field);
        return "";
    }

}