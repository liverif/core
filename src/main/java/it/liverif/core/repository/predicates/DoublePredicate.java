package it.liverif.core.repository.predicates;

import it.liverif.core.repository.APredicate;
import it.liverif.core.repository.AModelBean;
import it.liverif.core.view.list.SearchField;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Component;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Component
public class DoublePredicate<T extends AModelBean> extends APredicate<T> {

    public Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, SearchField searchField) {
        switch (searchField.getOperation()) {
            case SearchField.EMPTY:
                return criteriaBuilder.isNull(buildExpression(root, searchField.getField(), searchField.getOperation()));

            case SearchField.DIFFERENT_FROM_EMPTY:
                return criteriaBuilder.isNotNull(buildExpression(root, searchField.getField(), searchField.getOperation()));

            case SearchField.BETWEEN:
                if (StringUtils.hasText(searchField.getValue()) && StringUtils.hasText(searchField.getValue2())) {
                    return criteriaBuilder.between(buildExpression(root, searchField.getField(), searchField.getOperation()), Double.parseDouble(searchField.getValue()), Double.parseDouble(searchField.getValue2()));

                } else if (StringUtils.hasText(searchField.getValue()) && !StringUtils.hasText(searchField.getValue2())) {
                    return criteriaBuilder.greaterThanOrEqualTo(buildExpression(root, searchField.getField(), searchField.getOperation()), Double.parseDouble(searchField.getValue()));

                } else if (!StringUtils.hasText(searchField.getValue()) && StringUtils.hasText(searchField.getValue2())) {
                    return criteriaBuilder.lessThanOrEqualTo(buildExpression(root, searchField.getField(), searchField.getOperation()), Double.parseDouble(searchField.getValue2()));
                }
                return null;

            case SearchField.DIFFERENT:
                return StringUtils.hasText(searchField.getValue())
                        ? criteriaBuilder.notEqual(buildExpression(root, searchField.getField(), searchField.getOperation()), Double.parseDouble(searchField.getValue()))
                        : null;

            case SearchField.EQUAL:
                return StringUtils.hasText(searchField.getValue())
                        ? criteriaBuilder.equal(buildExpression(root, searchField.getField(), searchField.getOperation()), Double.parseDouble(searchField.getValue()))
                        : null;

            case SearchField.GREATER:
                if (StringUtils.hasText(searchField.getValue())) {
                    return criteriaBuilder.greaterThan(buildExpression(root, searchField.getField(), searchField.getOperation()), Double.parseDouble(searchField.getValue()));
                } else {
                    return null;
                }

            case SearchField.GREATER_OR_EQUAL:
                return StringUtils.hasText(searchField.getValue())
                        ? criteriaBuilder.greaterThanOrEqualTo(buildExpression(root, searchField.getField(), searchField.getOperation()), Double.parseDouble(searchField.getValue()))
                        : null;

            case SearchField.LESS:
                return StringUtils.hasText(searchField.getValue())
                        ? criteriaBuilder.lessThan(buildExpression(root, searchField.getField(), searchField.getOperation()), Double.parseDouble(searchField.getValue()))
                        : null;

            case SearchField.LESS_OR_EQUAL:
                return StringUtils.hasText(searchField.getValue())
                        ? criteriaBuilder.lessThanOrEqualTo(buildExpression(root, searchField.getField(), searchField.getOperation()), Double.parseDouble(searchField.getValue()))
                        : null;

            default:
                return null;

        }
    }

}
