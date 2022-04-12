package it.liverif.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.config.BeanDefinition;

public class CoreBeanUtils {

    public static String getBeanName(BeanDefinition bean) throws ClassNotFoundException {
        return Class.forName(bean.getBeanClassName()).getSimpleName();
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        return jsonResult;
    }
    
}
