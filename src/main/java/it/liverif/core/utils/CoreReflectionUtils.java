package it.liverif.core.utils;

import it.liverif.core.repository.AModelBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CoreReflectionUtils {

    public static void setPropertyValue (AModelBean obj, String field, Object value) throws Exception {
        obj.getClass().getMethod("set" + StringUtils.capitalize(field), new Class[]{value.getClass()}).invoke(obj, new Object[]{value});
    }

    public static Object getPropertyValue (AModelBean obj, String field) throws Exception {
        return obj.getClass().getMethod("get" + StringUtils.capitalize(field), new Class[0]).invoke(obj, new Object[0]);
    }

    public static List<Field> getStaticFields(Class<?> type){
        return Arrays.stream(type.getDeclaredFields())
                .filter(f->Modifier.isStatic(f.getModifiers()))
                .toList();
    }

    public static String getStaticFieldNameFromValue(Class clazz, String value) throws IllegalAccessException {
        List<Field> fields= getStaticFields(clazz);
        for(Field f: fields){
            String fieldValue=(String) f.get(null);
            if(fieldValue.equals(value)){
                return f.getName();
            }
        }
        return null;
    }

}
