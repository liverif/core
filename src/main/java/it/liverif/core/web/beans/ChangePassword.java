package it.liverif.core.web.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassword {

    private String field;
    private String password;
    private String password_repeat;
    private String password_old;

}
