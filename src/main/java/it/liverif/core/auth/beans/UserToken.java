package it.liverif.core.auth.beans;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter
public class UserToken {

    private String username;
    private ArrayList<String> roles=new ArrayList<>();

    public String getRolesName(){
        return Arrays.toString(roles.toArray());
    }

}
