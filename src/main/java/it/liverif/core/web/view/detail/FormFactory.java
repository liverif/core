package it.liverif.core.web.view.detail;

import it.liverif.core.repository.AModelBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;


@Slf4j
public class FormFactory {

    private static Object _getValue(Object objBean, String field) throws Exception{
        log.debug("field="+field);
        Object result=null;
        if (objBean!=null && StringUtils.hasText(field)){
            String[] fields=field.split("\\.");
            String field2=fields[1];
            String attributeNameMethod=StringUtils.capitalize(field2);
            result=objBean.getClass().getMethod("get"+attributeNameMethod,new Class[0]).invoke(objBean,new Object[0]);
        }
        log.debug("result="+result);
        return result;
    }

    public static String getValue(AModelBean modelBean, String field) throws Exception{
        log.debug("field="+field);
        String value="";
        String[] fields=field.split("\\.");
        String table=fields[0];
        Object valueObj=_getValue(modelBean, field);
        if (valueObj!=null) {
            value = valueObj.toString();
        }
        log.debug("value="+value);
        return value;
    }

}
