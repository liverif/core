package it.liverif.core.view;

import it.liverif.core.controller.beans.SelectItem;
import it.liverif.core.view.detail.DataAccess;
import it.liverif.core.view.detail.FieldTypeBean;
import it.liverif.core.view.detail.IAttribute;
import java.util.*;

public abstract class AAttribute implements IAttribute {
    
    protected final HashMap<String,String> label=new HashMap();
    protected final HashMap<String, FieldTypeBean> type=new HashMap();
    protected final ArrayList<String> fields=new ArrayList();
    protected final Params params=new Params();

    private Boolean viewButtonAdd=true;
    
    private String titleList;
    private String titleDetail;

    @Override
    public String getTitleList(){
        return titleList;
    }
    
    @Override
    public void setTitleList(String titleList){
        this.titleList=titleList;
    }

    @Override
    public String getTitleDetail(){
        return titleDetail;
    }

    @Override
    public void setTitleDetail(String titleDetail){
        this.titleDetail=titleDetail;
    }
    
    public AAttribute() {
       labelInit();
       typeInit();
       paramsInit();
    }

    @Override
    public HashMap<String, String> getLabel(){return label;}

    @Override
    public HashMap<String, FieldTypeBean> getType(){return type;}

    @Override
    public ArrayList<String> getFields(){return fields;}

    @Override
    public Params getParams(){return params;}

    public class Params implements IAttribute.FieldParams{
        protected final HashMap<String,DataAccess> access=new HashMap();
        protected final HashMap<String,Double> size=new HashMap();
        protected final HashMap<String,Integer> rowsize=new HashMap();
        protected final HashMap<String,Integer> colsize=new HashMap();
        protected final HashMap<String,Integer> maxlen=new HashMap();
        protected final HashMap<String,String> align=new HashMap();
        protected final HashMap<String,Boolean> newrow=new HashMap();
        protected final HashMap<String,String> tooltip=new HashMap();
        protected final HashMap<String,String> passwordinfo=new HashMap();
        protected final HashMap<String,String> ajaxlink=new HashMap();
        protected final HashMap<String,String> htmlcss=new HashMap();
        protected final HashMap<String,String> htmllinecss=new HashMap();
        protected final HashMap<String,String> htmlup=new HashMap();
        protected final HashMap<String,String> htmldown=new HashMap();
        protected final HashMap<String,String> htmlleft=new HashMap();
        protected final HashMap<String,String> htmlright=new HashMap();
        protected final HashMap<String,ArrayList<String>> button=new HashMap();
        protected final HashMap<String,ArrayList<SelectItem>> select=new HashMap();
        
        @Override
        public HashMap<String, DataAccess> access() {
            return access;
        }
        
        @Override
        public HashMap<String, Double> size() {
            return size;
        }

        @Override
        public HashMap<String, Integer> rowsize() {
            return rowsize;
        }

        @Override
        public HashMap<String, Integer> colsize() {
            return colsize;
        }

        @Override
        public HashMap<String, Integer> maxlen() {
            return maxlen;
        }
        
        @Override
        public HashMap<String, String> align() {
            return align;
        }

        @Override
        public HashMap<String, Boolean> newrow() {
            return newrow;
        }

        @Override
        public HashMap<String, String> tooltip() {
            return tooltip;
        }

        @Override
        public HashMap<String, String> htmlcss() {
            return htmlcss;
        }

        @Override
        public HashMap<String, String> htmllinecss() {
            return htmllinecss;
        }

        @Override
        public HashMap<String, String> passwordinfo() {
            return passwordinfo;
        }

        @Override
        public HashMap<String, String> ajaxlink() {
            return ajaxlink;
        }
        
        @Override
        public HashMap<String, String> htmlup() {
            return htmlup;
        }

        @Override
        public HashMap<String, String> htmldown() {
            return htmldown;
        }

        @Override
        public HashMap<String, String> htmlleft() {
            return htmlleft;
        }

        @Override
        public HashMap<String, String> htmlright() {
            return htmlright;
        }
        
        @Override
        public HashMap<String, ArrayList<String>> button() {
            return button;
        }
        
        @Override
        public HashMap<String, ArrayList<SelectItem>> select() {
            return select;
        }

    }

    public Boolean getViewButtonAdd() {
        return viewButtonAdd;
    }

    public void setViewButtonAdd(Boolean viewButtonAdd) {
        this.viewButtonAdd = viewButtonAdd;
    }

    public boolean hiddenAccess(String field){
        DataAccess dataAccess=getParams().access().get(field);
        return dataAccess.equals(DataAccess.HIDDEN)||
                dataAccess.equals(DataAccess.HIDDEN_DATA);
    }

    public boolean viewAccess(String field){
        DataAccess dataAccess=getParams().access().get(field);
        return dataAccess.equals(DataAccess.VIEW)||
                dataAccess.equals(DataAccess.VIEW_DATA)||
                dataAccess.equals(DataAccess.VIEW_READONLY)||
                dataAccess.equals(DataAccess.VIEW_REQUIRED_DATA);
    }

    public boolean dataAccess(String field){
        DataAccess dataAccess=getParams().access().get(field);
        return dataAccess.equals(DataAccess.HIDDEN_DATA) ||
                dataAccess.equals(DataAccess.VIEW_DATA) ||
                dataAccess.equals(DataAccess.VIEW_REQUIRED_DATA);
    }

    public boolean dataReadonly(String field){
        DataAccess dataAccess=getParams().access().get(field);
        return dataAccess.equals(DataAccess.VIEW_READONLY);
    }
    

    public boolean dataRequired(String field){
        DataAccess dataAccess=getParams().access().get(field);
        return dataAccess.equals(DataAccess.VIEW_REQUIRED_DATA);
    }
    
    public String getHtmlType(String field){
        if (getType().get(field).getType().equals(FieldTypeBean.Type.DATETIME)) return "text";
        if (getType().get(field).getType().equals(FieldTypeBean.Type.DATE)) return "date";
        if (getType().get(field).getType().equals(FieldTypeBean.Type.TIME)) return "time";
        return "text";
    }
    
    public String getFileType(String field){
        return getType().get(field).getType().equals(FieldTypeBean.Type.FILEDB)?"filedb":"file";
    }

}
