package it.liverif.core.view.detail;

public class FieldTypeBean {

    private Class clazz;
    private Type type;

    public static FieldTypeBean build(Class clazz, Type type){
        FieldTypeBean fieldTypeBean=new FieldTypeBean();
        fieldTypeBean.setClazz(clazz);
        fieldTypeBean.setType(type);
        return fieldTypeBean;
    }
    
    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
        NOVIEW
    }
}
