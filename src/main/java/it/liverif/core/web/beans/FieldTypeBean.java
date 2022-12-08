package it.liverif.core.web.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldTypeBean {

    private Class clazz;
    private Type type;

    public static FieldTypeBean build(Class clazz, Type type){
        FieldTypeBean fieldTypeBean=new FieldTypeBean();
        fieldTypeBean.setClazz(clazz);
        fieldTypeBean.setType(type);
        return fieldTypeBean;
    }

    public enum Type{
        NUMBER,
        TEXTAREA,
        PASSWORD,
        PASSWORDUSER,
        FILEFS,
        FILEDB,
        CURRENCY,
        TIME,
        TEXT,
        SELECT,
        SELECTMAP,
        SELECTAJAX,
        MULTISELECTAJAX,
        DATE,
        DATETIME,
        CHECKBOX,
        HTML,
        HTMLEDITOR,
        BUTTON,
        BUTTONSUBMIT,
        LINESEPARATOR,
        NOVIEW
    }
}
