
package it.liverif.core.view.detail;

import it.liverif.core.controller.beans.SelectItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface IAttribute {

    public String getTitleList();
    public void setTitleList(String tilte);
    public String getTitleDetail();
    public void setTitleDetail(String tilte);
    
    public FieldParams getParams();
    public HashMap<String, String> getLabel();
    public HashMap<String, FieldTypeBean> getType();
    public ArrayList<String> getFields();
    public void paramsInit();
    public void labelInit();
    public void typeInit();
    
    public interface FieldParams{
        public HashMap<String, DataAccess> access();
        public HashMap<String, Double> size();
        public HashMap<String, Integer> rowsize();
        public HashMap<String, Integer> colsize();
        public HashMap<String, Integer> maxlen();
        public HashMap<String, String> align();
        public HashMap<String,Boolean> newrow();
        public HashMap<String,String> tooltip();
        public HashMap<String, String> htmlcss();
        public HashMap<String, String> htmllinecss();
        public HashMap<String, String> passwordinfo();
        public HashMap<String, String> ajaxlink();
        public HashMap<String, String> htmlup();
        public HashMap<String, String> htmldown();
        public HashMap<String, String> htmlleft();
        public HashMap<String, String> htmlright();
        public HashMap<String, ArrayList<String>> button();
        public HashMap<String, ArrayList<SelectItem>> select();
    }

}
