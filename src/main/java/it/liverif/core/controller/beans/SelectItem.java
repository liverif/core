package it.liverif.core.controller.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectItem {

    String id;
    String text;

    public SelectItem(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public static SelectItem build(String id, String text){
        return new SelectItem(id,text);
    }

}
