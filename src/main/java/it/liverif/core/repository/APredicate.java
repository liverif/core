package it.liverif.core.repository;

import lombok.extern.slf4j.Slf4j;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

@Slf4j
public abstract class APredicate<T extends AModelBean> {

    protected Expression buildExpression(Root<T> root, String field, String operation) {
        if (field.contains(".")) {
            String[] path = field.split("\\.");
            if (path.length == 2)
                return root.join(path[0]).get(path[1]);
            if (path.length == 3)
                return root.join(path[0]).join(path[1]).get(path[2]);
            if (path.length == 4)
                return root.join(path[0]).join(path[1]).join(path[2]).get(path[3]);
            if (path.length > 4)
                log.error("path.length>4 - field=" + field + " operation=" + operation);
        } else {
            return root.get(field);
        }
        return null;
    }

}
