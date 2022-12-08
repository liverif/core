package it.liverif.core.web.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageElement {

    private String title;
    private String value;

    public ImageElement(){}

    public ImageElement(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public static ImageElement build(String title, String value) {
        return new ImageElement(title,value);
    }

}
