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
public class StringPredicate<T extends AModelBean> extends APredicate<T> {

    public Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, SearchField searchField) {

        switch (searchField.getOperation()) {
            case SearchField.EMPTY:
                return criteriaBuilder.isNull(buildExpression(root, searchField.getField(), searchField.getOperation()));

            case SearchField.DIFFERENT_FROM_EMPTY:
                return criteriaBuilder.isNotNull(buildExpression(root, searchField.getField(), searchField.getOperation()));

            case SearchField.DIFFERENT:
                return StringUtils.hasText(searchField.getValue())
                        ? criteriaBuilder.notEqual(criteriaBuilder.upper(buildExpression(root, searchField.getField(), searchField.getOperation())), searchField.getValue().toUpperCase())
                        : null;

            case SearchField.EQUAL:
                return StringUtils.hasText(searchField.getValue())
                        ? criteriaBuilder.equal(criteriaBuilder.upper(buildExpression(root, searchField.getField(), searchField.getOperation())), searchField.getValue().toUpperCase())
                        : null;

            case SearchField.CONTAINS:
                return StringUtils.hasText(searchField.getValue())
                        ? criteriaBuilder.like(criteriaBuilder.upper(buildExpression(root, searchField.getField(), searchField.getOperation())), '%'+searchField.getValue().toUpperCase()+'%')
                        : null;

            case SearchField.DIFFERENT_CONTAINS:
                return StringUtils.hasText(searchField.getValue())
                        ? criteriaBuilder.notLike(criteriaBuilder.upper(buildExpression(root, searchField.getField(), searchField.getOperation())), '%'+searchField.getValue().toUpperCase()+'%')
                        : null;

            default:
                return null;

        }
    }

}
