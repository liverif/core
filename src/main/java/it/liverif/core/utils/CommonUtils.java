package it.liverif.core.utils;

import it.liverif.core.model.annotations.EntityTransient;
import it.liverif.core.repository.TableNameReserved;
import it.liverif.core.view.detail.ADetailResponse;
import it.liverif.core.view.list.AListResponse;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Set;

public class CommonUtils {

    public static StringWriter errorPrintStackTrace(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        sw.flush();
        return sw;
    }

    public static Set<BeanDefinition> scanPackage(String scanPackage) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(EntityTransient.class));
        provider.setResourcePattern("*.class");
        return provider.findCandidateComponents(scanPackage);
    }

    public static  void cleanSession(HttpServletRequest request){
        Enumeration<String> attributes = request.getSession().getAttributeNames();
        while (attributes.hasMoreElements()) {
            String attribute = (String) attributes.nextElement();
            if (attribute.startsWith(AListResponse.SESSION_LIST_RESPONSE_PREFIX) ||
                    attribute.startsWith(ADetailResponse.SESSION_DETAIL_RESPONSE_PREFIX)){
                request.getSession().removeAttribute(attribute);
            }
        }
    }
    
    public static String removeEntitySuffix(String className){
        if(className.endsWith(TableNameReserved.ENTITY_SUFFIX)) className=className.substring(0,className.length()-TableNameReserved.ENTITY_SUFFIX.length());
        return className;
    }

    public static String sanitizeString(String name) {
        return name.replaceAll("[^A-Za-z0-9 ]", "").replace(" ","_");
    }

}
