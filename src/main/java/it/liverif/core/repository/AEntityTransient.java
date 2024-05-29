package it.liverif.core.repository;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public abstract class AEntityTransient<T> {

    Class<T> getType(){
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public String getName(String value) throws IllegalAccessException {
        Field[] declaredFields = getType().getDeclaredFields();
        for (Field field : declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                if ((field.get(null)).equals(value)) return field.getName();
            }
        }
        return null;
    }

}
