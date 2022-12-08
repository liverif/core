package it.liverif.core.repository.predicates;

import it.liverif.core.repository.APredicate;
import it.liverif.core.web.beans.SearchField;
import it.liverif.core.repository.AModelBean;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateTimePredicate<T extends AModelBean> extends APredicate<T> {

    @Autowired
    MessageSource messageSource;

    public Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, SearchField searchField) {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";//messageSource.getMessage("format.datetime.pattern", null, LocaleContextHolder.getLocale());

        switch (searchField.getOperation()) {
            case SearchField.EMPTY:
                return criteriaBuilder.isNull(buildExpression(root, searchField.getField(), searchField.getOperation()));

            case SearchField.DIFFERENT_FROM_EMPTY:
                return criteriaBuilder.isNotNull(buildExpression(root, searchField.getField(), searchField.getOperation()));

            case SearchField.BETWEEN:
                if (StringUtils.hasText(searchField.getValue()) && StringUtils.hasText(searchField.getValue2())) {
                    String d1 = searchField.getValue() + " 00:00:00";
                    LocalDateTime ldt1 = LocalDateTime.parse(d1, DateTimeFormatter.ofPattern(dateFormat));
                    String d2 = searchField.getValue2() + " 23:59:59";
                    LocalDateTime ldt2 = LocalDateTime.parse(d2, DateTimeFormatter.ofPattern(dateFormat));
                    return criteriaBuilder.between(buildExpression(root, searchField.getField(), searchField.getOperation()), ldt1, ldt2);
                } else if (StringUtils.hasText(searchField.getValue()) && !StringUtils.hasText(searchField.getValue2())) {
                    String d1 = searchField.getValue() + " 00:00:00";
                    LocalDateTime ldt1 = LocalDateTime.parse(d1, DateTimeFormatter.ofPattern(dateFormat));
                    return criteriaBuilder.greaterThanOrEqualTo(buildExpression(root, searchField.getField(), searchField.getOperation()), ldt1);
                } else if (!StringUtils.hasText(searchField.getValue()) && StringUtils.hasText(searchField.getValue2())) {
                    String d2 = searchField.getValue2() + " 23:59:59";
                    LocalDateTime ldt2 = LocalDateTime.parse(d2, DateTimeFormatter.ofPattern(dateFormat));
                    return criteriaBuilder.lessThanOrEqualTo(buildExpression(root, searchField.getField(), searchField.getOperation()), ldt2);
                }
                return null;
            case SearchField.DIFFERENT:
                if (StringUtils.hasText(searchField.getValue())) {
                    String d1 = searchField.getValue() + " 00:00:00";
                    LocalDateTime ldt1 = LocalDateTime.parse(d1, DateTimeFormatter.ofPattern(dateFormat));
                    String d2 = searchField.getValue() + " 23:59:59";
                    LocalDateTime ldt2 = LocalDateTime.parse(d2, DateTimeFormatter.ofPattern(dateFormat));
                    return criteriaBuilder.not(criteriaBuilder.between(buildExpression(root, searchField.getField(), searchField.getOperation()), ldt1, ldt2));
                } else return null;

            case SearchField.EQUAL:
                if (StringUtils.hasText(searchField.getValue())) {
                    String d1 = searchField.getValue() + " 00:00:00";
                    LocalDateTime ldt1 = LocalDateTime.parse(d1, DateTimeFormatter.ofPattern(dateFormat));
                    String d2 = searchField.getValue() + " 23:59:59";
                    LocalDateTime ldt2 = LocalDateTime.parse(d2, DateTimeFormatter.ofPattern(dateFormat));
                    return criteriaBuilder.between(buildExpression(root, searchField.getField(), searchField.getOperation()), ldt1, ldt2);
                } else return null;

            case SearchField.GREATER:
                if (StringUtils.hasText(searchField.getValue())) {
                    String d1 = searchField.getValue() + " 00:00:00";
                    LocalDateTime ldt1 = LocalDateTime.parse(d1, DateTimeFormatter.ofPattern(dateFormat));
                    return criteriaBuilder.greaterThan(buildExpression(root, searchField.getField(), searchField.getOperation()), ldt1);
                } else {
                    return null;
                }
            case SearchField.GREATER_OR_EQUAL:
                if (StringUtils.hasText(searchField.getValue())) {
                    String d1 = searchField.getValue() + " 00:00:00";
                    LocalDateTime ldt1 = LocalDateTime.parse(d1, DateTimeFormatter.ofPattern(dateFormat));
                    return criteriaBuilder.greaterThanOrEqualTo(buildExpression(root, searchField.getField(), searchField.getOperation()), ldt1);
                } else {
                    return null;
                }
            case SearchField.LESS:
                if (StringUtils.hasText(searchField.getValue())) {
                    String d1 = searchField.getValue() + " 23:59:59";
                    LocalDateTime ldt1 = LocalDateTime.parse(d1, DateTimeFormatter.ofPattern(dateFormat));
                    return criteriaBuilder.lessThan(buildExpression(root, searchField.getField(), searchField.getOperation()), ldt1);
                } else {
                    return null;
                }
            case SearchField.LESS_OR_EQUAL:
                if (StringUtils.hasText(searchField.getValue())) {
                    String d1 = searchField.getValue() + " 23:59:59";
                    LocalDateTime ldt1 = LocalDateTime.parse(d1, DateTimeFormatter.ofPattern(dateFormat));
                    return criteriaBuilder.lessThanOrEqualTo(buildExpression(root, searchField.getField(), searchField.getOperation()), ldt1);
                } else {
                    return null;
                }
            default:
                return null;

        }
    }
}
