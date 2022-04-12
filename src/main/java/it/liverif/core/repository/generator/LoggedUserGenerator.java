package it.liverif.core.repository.generator;

import org.hibernate.Session;
import org.hibernate.tuple.ValueGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoggedUserGenerator implements ValueGenerator<String> {

    public LoggedUserGenerator() {}

    public String generateValue(Session session, Object o) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) return authentication.getName();
        return "_APPLICATION_";
    }

}