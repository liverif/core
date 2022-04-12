package it.liverif.core.repository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import javax.persistence.AttributeConverter;

@Component
public class AttributeBCrypt implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute==null) return null;
        if (attribute.startsWith("$2a$") && attribute.length()>=59) return attribute;
        return new BCryptPasswordEncoder().encode(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}
