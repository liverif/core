package it.liverif.core.web.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AValidationBean {

    private String random = "";
    private String validator = "";

}
