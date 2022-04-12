package it.liverif.core.controller.beans;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class StackWebBean extends AValidationBean implements Serializable {

    public static final String PARAMETER_ID="id";
    public static final String PARAMETER_FATHER_ID="father_id";
    public static final String PARAMETER_USERNAME="username";
    public static final String PARAMETER_PAG="pag";
    public static final String PARAMETER_MODEL="model";
    public static final String PARAMETER_FIELD="field";
    public static final String PARAMETER_ACTION="action";
    private String label = "";
    private String stackAction = "";
    private String target = "";
    private String targetAction = "";
    private Map<String, String> params = new HashMap<>();
    private String linkSkip = "";

}
