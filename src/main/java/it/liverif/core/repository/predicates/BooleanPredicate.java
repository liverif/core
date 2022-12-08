package it.liverif.core.repository.predicates;

import it.liverif.core.repository.APredicate;
import it.liverif.core.web.beans.SearchField;
import it.liverif.core.repository.AModelBean;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Component;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Component
public class BooleanPredicate<T extends AModelBean> extends APredicate<T> {

    public Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, SearchField searchField) {

        switch (searchField.getOperation()) {
            case SearchField.EQUAL:
                return StringUtils.hasText(searchField.getValue())
                        ? criteriaBuilder.equal(buildExpression(root, searchField.getField(), searchField.getOperation()), "S".equalsIgnoreCase(searchField.getValue()))
                        : null;
            default:
                return null;

        }
    }

}
