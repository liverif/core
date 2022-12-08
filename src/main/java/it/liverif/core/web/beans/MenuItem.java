package it.liverif.core.web.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuItem{

    private String group;
    private String label;
    private String link;

}
