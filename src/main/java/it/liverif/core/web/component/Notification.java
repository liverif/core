package it.liverif.core.web.component;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.context.annotation.RequestScope;
import java.util.ArrayList;

@Component
@RequestScope
public class Notification {
    
    protected ArrayList<String> infoList=new ArrayList();
    protected ArrayList<String> warningList=new ArrayList();
    protected ArrayList<String> errorList=new ArrayList();

    public Integer getAllNumberMessages(){ return infoList.size()+warningList.size()+errorList.size(); }

    public void addError(String error){add(error,errorList);}
    public boolean hasErrors(){return getError().size()>0;}
    public ArrayList<String> getError() { return (ArrayList<String>) errorList.clone(); }
    
    //Info
    public void addInfo(String info){add(info,infoList);}
    public ArrayList<String> getInfo() { return (ArrayList<String>) infoList.clone();}
    public boolean hasInfos(){return getWarning().size()>0;}
    
    //Warning
    public void addWarning(String warning){add(warning,warningList);}
    public ArrayList<String> getWarning() { return (ArrayList<String>) warningList.clone(); }
    public boolean hasWarnings(){return getWarning().size()>0;}

    private void add(String msg,ArrayList<String> list){
        if (!list.contains(msg)) list.add(msg);
    }

    public void merge(Notification anotification){
        ArrayList<String> info1 = anotification.infoList;
        if (info1!=null && !info1.isEmpty()){
            for(String s: info1) infoList.add(s);
        }
        ArrayList<String> warning1 = anotification.warningList;
        if (warning1!=null && !warning1.isEmpty()){
            for(String s: warning1) warningList.add(s);
        }
        ArrayList<String> error1 = anotification.errorList;
        if (error1!=null && !error1.isEmpty()){
            for(String s: error1) errorList.add(s);
        }    
    }
    
    public static final class ErrorMessage {
        public static final String ERROR_SAVE="error.action.save";
        public static final String ERROR_SAVE_DUPLICATE="error.action.save_duplicate";        
        public static final String ERROR_UPDATE="error.action.update";
        public static final String ERROR_UPDATE_DUPLICATE="error.action.update_duplicate";
        public static final String ERROR_DELETE="error.action.delete";
        public static final String ERROR_UPDATE_ROLESFORUSER="error.action.updaterolesforuser";
        public static final String ERROR_SEND_EMAIL="error.action.sendemail";
        public static final String ERROR_ACTION_NOT_AVAILABLE="error.action.not_available";
        public static final String ERROR_SPACE_DISK_NOT_AVAILABLE="error.application.space_disk_not_available";
        public static final String ERROR_SPACE_DATABASE_NOT_AVAILABLE="error.application.space_database_not_available";
        public static final String ERROR_PAGE_GENERIC_MESSAGE="error.page.genericmessage";
        public static final String ERROR_PREFIX_EXCEPTION="error.exception.";
        public static final String ERROR_PAGE_NOTFOUND="error.message.pagenotfound";
        public static final String ERROR_FIELD_REQUIRED="error.field.required";
    }
    
    public static final class InfoMessage {
        public static final String INFO_SAVE="info.general.save";
        public static final String INFO_UPDATE="info.general.update";
        public static final String INFO_DELETE="info.general.delete";
        public static final String INFO_SEND_EMAIL="info.general.sendemail";
    }

    public void cleanAll(){
        errorList.clear();
        infoList.clear();
        warningList.clear();
    }

    public Integer countAll(){
        return errorList.size()+warningList.size()+infoList.size();
    }

    public Integer countErrors(){
        return errorList.size();
    }
    
}
