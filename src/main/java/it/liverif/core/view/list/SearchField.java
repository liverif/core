package it.liverif.core.view.list;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
public class SearchField {

    public static final String GREATER_OR_EQUAL = "search.operator.ge";
    public static final String GREATER = "search.operator.gt";
    public static final String CONTAINS = "search.operator.cn";
    public static final String EQUAL = "search.operator.eq";
    public static final String LESS_OR_EQUAL = "search.operator.le";
    public static final String LESS = "search.operator.lt";
    public static final String BETWEEN = "search.operator.bt";
    public static final String DIFFERENT = "search.operator.ne";
    public static final String DIFFERENT_CONTAINS = "search.operator.nen";
    public static final String EMPTY = "search.operator.em";
    public static final String DIFFERENT_FROM_EMPTY = "search.operator.emn";
    
    private String field;
    private List<String> operations;
    private String operation;
    private String value;
    private String value2;
    private String type;

    public static List<String> addOperations(String... values) {
        return Arrays.asList(values);
    }

    public static List<String> addOperationsString() {
        return addOperations(
                CONTAINS,
                DIFFERENT_CONTAINS,
                EQUAL,
                DIFFERENT,
                EMPTY,
                DIFFERENT_FROM_EMPTY
        );
    }

    public static List<String> addOperationsDateTime() {
        return addOperations(
                EQUAL,
                DIFFERENT,
                GREATER,
                GREATER_OR_EQUAL,
                LESS,
                LESS_OR_EQUAL,
                BETWEEN,
                EMPTY,
                DIFFERENT_FROM_EMPTY
        );
    }

    public static List<String> addOperationsNumber() {
        return addOperations(
                EQUAL,
                DIFFERENT,
                GREATER,
                GREATER_OR_EQUAL,
                LESS,
                LESS_OR_EQUAL,
                BETWEEN,
                EMPTY,
                DIFFERENT_FROM_EMPTY
        );
    }

    public static List<String> addOperationsBoolean() {
        return addOperations(
                EQUAL
        );
    }

}
