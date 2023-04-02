package it.liverif.core.web.view.list;

import it.liverif.core.format.DecimalFormatter;
import it.liverif.core.format.LocalDateFormatter;
import it.liverif.core.format.LocalDateTimeFormatter;
import it.liverif.core.format.LocalTimeFormatter;
import it.liverif.core.repository.AModelBean;
import it.liverif.core.repository.FieldNameReserved;
import it.liverif.core.utils.CommonUtils;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.util.Pair;
import org.springframework.web.util.HtmlUtils;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ARowList<T extends AModelBean, R extends AListResponse> {

    public static final String ROWACTION="rowaction";
    public static final String LINKDETAIL="linkdetail";
    
    protected LocalDateFormatter localDateFormatter;
    protected LocalTimeFormatter localTimeFormatter;
    protected LocalDateTimeFormatter localDateTimeFormatter;
    
    @Autowired
    protected MessageSource messageSource;
    
    @Autowired
    protected HttpServletRequest request;
    
    @PostConstruct
    protected void ARowList(){
        localDateFormatter=new LocalDateFormatter(message("format.date.pattern"));
        localTimeFormatter=new LocalTimeFormatter(message("format.time.pattern"));
        localDateTimeFormatter= new LocalDateTimeFormatter(message("format.datetime.pattern" ));
    }
    
    public Map<String,String> create(T obj, Pair<List<String>,String> rowAction) {
        String linkDetail="";
        if(obj.getViewButtonDetail()) linkDetail=rowAction.getSecond();;
        Map<String,String> row=new HashMap<>(
                Map.of(
                        FieldNameReserved.ID,obj.getId().toString(),
                        LINKDETAIL, linkDetail,
                        ROWACTION, String.join("\n",rowAction.getFirst())
                )
        );
        return row;
    }

    protected String message(String label){
        return messageSource.getMessage(label,new String[]{""}, LocaleContextHolder.getLocale());
    }

    protected String path() {
        return request.getContextPath();
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

    public String toHtml(Object value){
        String result="";
        if (value!=null){
            if (value instanceof String){
                result=(String) value;
            }else{
                result=value.toString();
            }
        }
        return HtmlUtils.htmlEscape(result, StandardCharsets.UTF_8.name());
    }

    protected Object getHttpSession(String attribute) {
        return request.getSession().getAttribute(attribute);
    }

    protected R getListResponse() {
        return (R) getListResponse(modelName());
    }

    protected AListResponse getListResponse(String modelName){
        AListResponse listResponse = (AListResponse) getHttpSession(AListResponse.SESSION_LIST_RESPONSE_PREFIX + modelName);
        return listResponse;
    }

    public abstract void init();
    public abstract List<String> getListColumn() throws Exception;
    public abstract Map<String,String> row(T obj, Pair<List<String>,String> rowAction) throws Exception;
    public abstract Map<String,String> cssColumn();
    public abstract Map<String,String> cssHeaderColumn();

    public String numberToString(BigDecimal value, String pattern) {
        DecimalFormatter numberFormatter=new DecimalFormatter(pattern);
        String tovalue=numberFormatter.print(value,LocaleContextHolder.getLocale());
        return tovalue;
    }
}
