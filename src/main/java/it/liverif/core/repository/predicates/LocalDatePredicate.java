package it.liverif.core.repository.predicates;

import it.liverif.core.repository.APredicate;
import it.liverif.core.repository.AModelBean;
import it.liverif.core.view.list.SearchField;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDatePredicate<T extends AModelBean> extends APredicate<T> {

    @Autowired
    MessageSource messageSource;

    public Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, SearchField searchField) {
        String dateFormat = "yyyy-MM-dd";

        switch (searchField.getOperation()) {
            case SearchField.EMPTY:
                return criteriaBuilder.isNull(buildExpression(root, searchField.getField(), searchField.getOperation()));

            case SearchField.DIFFERENT_FROM_EMPTY:
                return criteriaBuilder.isNotNull(buildExpression(root, searchField.getField(), searchField.getOperation()));

            case SearchField.BETWEEN:
                if (StringUtils.hasText(searchField.getValue()) && StringUtils.hasText(searchField.getValue2())) {
                    LocalDate ldt = LocalDate.parse(searchField.getValue(), DateTimeFormatter.ofPattern(dateFormat));
                    LocalDate ldt2 = LocalDate.parse(searchField.getValue2(), DateTimeFormatter.ofPattern(dateFormat));
                    return criteriaBuilder.between(buildExpression(root, searchField.getField(), searchField.getOperation()), ldt, ldt2);

                } else if (StringUtils.hasText(searchField.getValue()) && !StringUtils.hasText(searchField.getValue2())) {
                    LocalDate ldt = LocalDate.parse(searchField.getValue(), DateTimeFormatter.ofPattern(dateFormat));
                    return criteriaBuilder.greaterThanOrEqualTo(buildExpression(root, searchField.getField(), searchField.getOperation()), ldt);

                } else if (!StringUtils.hasText(searchField.getValue()) && StringUtils.hasText(searchField.getValue2())) {
                    LocalDate ldt2 = LocalDate.parse(searchField.getValue2(), DateTimeFormatter.ofPattern(dateFormat));
                    return criteriaBuilder.lessThanOrEqualTo(buildExpression(root, searchField.getField(), searchField.getOperation()), ldt2);
                }
                return null;


            case SearchField.DIFFERENT:
                if (StringUtils.hasText(searchField.getValue())) {
                    LocalDate ldt1 = LocalDate.parse(searchField.getValue(), DateTimeFormatter.ofPattern(dateFormat));
                    return criteriaBuilder.not(criteriaBuilder.notEqual(buildExpression(root, searchField.getField(), searchField.getOperation()), ldt1));
                } else return null;

            case SearchField.EQUAL:
                if (StringUtils.hasText(searchField.getValue())) {
                    LocalDate ldt1 = LocalDate.parse(searchField.getValue(), DateTimeFormatter.ofPattern(dateFormat));
                    return criteriaBuilder.equal(buildExpression(root, searchField.getField(), searchField.getOperation()), ldt1);
                } else return null;


            case SearchField.GREATER:
                if (StringUtils.hasText(searchField.getValue())) {
                    return criteriaBuilder.greaterThan(buildExpression(root, searchField.getField(), searchField.getOperation()), LocalDate.parse(searchField.getValue(), DateTimeFormatter.ofPattern(dateFormat)));
                } else {
                    return null;
                }

            case SearchField.GREATER_OR_EQUAL:
                return StringUtils.hasText(searchField.getValue())
                        ? criteriaBuilder.greaterThanOrEqualTo(buildExpression(root, searchField.getField(), searchField.getOperation()), LocalDate.parse(searchField.getValue(), DateTimeFormatter.ofPattern(dateFormat)))
                        : null;

            case SearchField.LESS:
                return StringUtils.hasText(searchField.getValue())
                        ? criteriaBuilder.lessThan(buildExpression(root, searchField.getField(), searchField.getOperation()), LocalDate.parse(searchField.getValue(), DateTimeFormatter.ofPattern(dateFormat)))
                        : null;

            case SearchField.LESS_OR_EQUAL:
                return StringUtils.hasText(searchField.getValue())
                        ? criteriaBuilder.lessThanOrEqualTo(buildExpression(root, searchField.getField(), searchField.getOperation()), LocalDate.parse(searchField.getValue(), DateTimeFormatter.ofPattern(dateFormat)))
                        : null;

            default:
                return null;

        }
    }
}
