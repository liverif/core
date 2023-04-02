package it.liverif.core.web.beans;

import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class GenericParametersBean extends AValidationBean {

    private Map<String,String> params=new HashMap();

}
