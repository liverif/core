package it.liverif.core.utils;

import it.liverif.core.repository.AModelBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class CoreReflectionUtils {

    public static void setPropertyValue (AModelBean obj, String field, Object value) throws Exception {
        obj.getClass().getMethod("set" + StringUtils.capitalize(field), new Class[]{value.getClass()}).invoke(obj, new Object[]{value});
    }

    public static Object getPropertyValue (AModelBean obj, String field) throws Exception {
        return obj.getClass().getMethod("get" + StringUtils.capitalize(field), new Class[0]).invoke(obj, new Object[0]);
    }

}
