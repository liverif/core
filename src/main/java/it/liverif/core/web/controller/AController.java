package it.liverif.core.web.controller;

import it.liverif.core.auth.ACentralPolicy;
import it.liverif.core.exeptions.PolicyException;
import it.liverif.core.repository.AFileFsModelBean;
import it.liverif.core.repository.IFileDbModelBean;
import it.liverif.core.repository.AModelBean;
import it.liverif.core.utils.FileUtils;
import it.liverif.core.utils.UnZipUtils;
import it.liverif.core.utils.ZipUtils;
import it.liverif.core.web.beans.*;
import it.liverif.core.web.beans.MenuItem;
import it.liverif.core.web.beans.StackWebBean;
import it.liverif.core.web.view.detail.ADetailResponse;
import it.liverif.core.web.view.list.AListResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpEntity;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.ui.ModelMap;

import java.io.*;
import java.util.*;

@Slf4j
public abstract class AController<T extends AModelBean, R extends AListResponse,P extends ADetailResponse> extends ABaseController<T,R,P> {
    
    protected final ArrayList<MenuItem> menuItems = new ArrayList();

    @Autowired
    private ACentralPolicy centralPolicy;
    
    public Map<String, ArrayList<MenuItem>> menu() {
        Map<String, ArrayList<MenuItem>> menuGroup = new TreeMap();

        for(MenuItem item: menuItems) {
            String group = item.getGroup();
            if (group != null && !group.isEmpty()) {
                ArrayList menuList;
                if (menuGroup.containsKey(group)) {
                    menuList = (ArrayList)menuGroup.get(group);
                    menuList.add(item);
                } else {
                    menuList = new ArrayList();
                    menuList.add(item);
                    menuGroup.put(group, menuList);
                }
            }
        }

        return menuGroup;
    }

    public String stackExecute(ModelMap model) throws Exception {
        log.debug("request: " + request.getRequestURI());

        Map parameters=request.getParameterMap();
        Iterator i=parameters.keySet().iterator();
        while(i.hasNext()){
            String paramName=(String) i.next();
            String[] paramValues=(String[]) parameters.get(paramName);
            String values=String.join(" | ", Arrays.asList(paramValues));
            log.debug("REQUEST> "+paramName+" = "+values);
        }

        Enumeration<String> attributes = request.getSession().getAttributeNames();
        while (attributes.hasMoreElements()) {
            String attribute = (String) attributes.nextElement();
            log.debug("SESSION> "+attribute+" = "+request.getSession().getAttribute(attribute));
        }

        stackWebEngine.execute();
        stackWebEngine.increaseSessionValidator();

        List<StackWebBean> stack = (List<StackWebBean>) getHttpSession(StackWebEngine.STACKWEB);
        log.debug("stack.size=" + stack.size());
        for (StackWebBean stackWebBean : stack) {
            log.debug("-----------------");
            log.debug("  stackWebBean.label=" + stackWebBean.getLabel());
            log.debug("  stackWebBean.target=" + stackWebBean.getTarget());
            log.debug("  stackWebBean.targetAction=" + stackWebBean.getTargetAction());
            log.debug("  stackWebBean.validator=" + stackWebBean.getValidator());
            log.debug("  stackWebBean.params={}" , stackWebBean.getParams());
        }
        log.debug("-----------------");
        StackWebBean sb = getStackWebBean();
        log.debug("  this >>> stackWebBean.label=" + sb.getLabel());
        log.debug("  this >>> stackWebBean.target=" + sb.getTarget());
        log.debug("  this >>> stackWebBean.targetAction=" + sb.getTargetAction());
        log.debug("  this >>> stackWebBean.validator=" + sb.getValidator());
        log.debug("  this >>> stackWebBean.params={}" , sb.getParams());
        log.debug("-----------------");
        if(!centralPolicy.execute(getUser(),sb)) throw new PolicyException(sb.getTarget());

        if (StringUtils.hasText(sb.getTarget())) {
            if (!StringUtils.hasText(sb.getTargetAction())) sb.setTargetAction(METHOD_EXECUTE);
            log.debug("FORWARD ---> " + sb.getTargetAction()+"#"+sb.getTargetAction());
            stackWebEngine.createAllLinkSkip(stackWebConfig.getStackContext());
        } else {
            stackWebEngine.clean();
        }
        
        String page= stackWebConfig.getDefaultTemplatePage();

        if (StringUtils.hasText(sb.getTarget())){
            Object stackController=applicationContext.getBean(sb.getTarget()+"StackController");
            
            String targetAction=sb.getTargetAction();
            String stackAction=stackAction();
            
            //if (stackAction.equals(StackWebEngine.Action.SKIP) && sb.getParams().containsKey(StackWebBean.PARAMETER_METHOD)){
            //    targetAction=sb.getParams().get(StackWebBean.PARAMETER_METHOD);
            //}
            
            page = (String) stackController.getClass().getMethod(targetAction, new Class[]{ModelMap.class}).invoke(stackController, new Object[]{model});
            
        }

        return page;
    }

    public void stackWebEngineBack(){
        Long validator = (Long) getHttpSession(JsonAction.SESSION_VALIDATOR);
        StackWebBean stackWebBean=new StackWebBean();
        stackWebBean.setStackAction(StackWebEngine.Action.BACK);
        stackWebBean.setValidator(validator.toString());
        request.setAttribute(StackWebEngine.OVERWRITE_REQUEST_STACKWEB,stackWebBean);
    }

    public void stackWebEngineRefresh(){
        Long validator = (Long) getHttpSession(JsonAction.SESSION_VALIDATOR);
        StackWebBean stackWebBean=new StackWebBean();
        stackWebBean.setStackAction(StackWebEngine.Action.REFRESH);
        stackWebBean.setValidator(validator.toString());
        request.setAttribute(StackWebEngine.OVERWRITE_REQUEST_STACKWEB,stackWebBean);
    }

    public void stackWebEngineClean(){
        Long validator = (Long) getHttpSession(JsonAction.SESSION_VALIDATOR);
        StackWebBean stackWebBean=new StackWebBean();
        stackWebBean.setStackAction(StackWebEngine.Action.CLEAN);
        stackWebBean.setValidator(validator.toString());
        request.setAttribute(StackWebEngine.OVERWRITE_REQUEST_STACKWEB,stackWebBean);
    }

    public void stackWebEngineSkip(String target,String targetAction){
        Long validator = (Long) getHttpSession(JsonAction.SESSION_VALIDATOR);
        StackWebBean stackWebBean=new StackWebBean();
        stackWebBean.setStackAction(StackWebEngine.Action.SKIP);
        stackWebBean.setValidator(validator.toString());
        stackWebBean.setTarget(target);
        stackWebBean.setTargetAction(targetAction);
        request.setAttribute(StackWebEngine.OVERWRITE_REQUEST_STACKWEB,stackWebBean);
    }

    public void stackWebEngineSkip(String target,String targetAction, Pair<String,String>...params){
        Long validator = (Long) getHttpSession(JsonAction.SESSION_VALIDATOR);
        StackWebBean stackWebBean=new StackWebBean();
        stackWebBean.setStackAction(StackWebEngine.Action.SKIP);
        stackWebBean.setValidator(validator.toString());
        stackWebBean.setTarget(target);
        stackWebBean.setTargetAction(targetAction);
        if (params!=null) {
            for (Pair<String, String> pair : params) {
                stackWebBean.getParams().put(pair.getFirst(), pair.getSecond());
            }
        }
        request.setAttribute(StackWebEngine.OVERWRITE_REQUEST_STACKWEB,stackWebBean);
    }

    public void stackWebEngineAdd(String target,String targetAction, String label, Pair<String,String>...params){
        Long validator = (Long) getHttpSession(JsonAction.SESSION_VALIDATOR);
        StackWebBean stackWebBean=new StackWebBean();
        stackWebBean.setStackAction(StackWebEngine.Action.ADD);
        stackWebBean.setValidator(validator.toString());
        stackWebBean.setLabel(label);
        stackWebBean.setTarget(target);
        stackWebBean.setTargetAction(targetAction);
        if (params!=null) {
            for (Pair<String, String> pair : params) {
                stackWebBean.getParams().put(pair.getFirst(), pair.getSecond());
            }
        }
        request.setAttribute(StackWebEngine.OVERWRITE_REQUEST_STACKWEB,stackWebBean);
    }

    protected void createPublicFile(FileUpload fileUpload, AFileFsModelBean filefs) throws Exception{
        String filename = FileUtils.sanitizeFilename(fileUpload.getFile().getOriginalFilename());
        FileUtils.createDirectory(publicRepository(), tableName());
        filefs.setRepository(publicRepository());
        String subpath_filename = FileUtils.createFilename(filefs.getRepository(), tableName(), filename);
        filename = FilenameUtils.getName(subpath_filename);
        try (FileOutputStream outputStream = new FileOutputStream(filefs.getRepository() + File.separator + subpath_filename)) {
            outputStream.write(fileUpload.getFile().getBytes());
        }
        filefs.setFilename(filename);
        filefs.setPathfile(subpath_filename);
        filefs.setContenttype(fileUpload.getFile().getContentType());
    }

    protected void createPrivateFile(FileUpload fileUpload, AFileFsModelBean filefs) throws Exception{
        String filename = FileUtils.sanitizeFilename(fileUpload.getFile().getOriginalFilename());
        FileUtils.createDirectory(privateRepository(), tableName());
        filefs.setRepository(privateRepository());
        String subpath_filename_random = FileUtils.createRandomFilename(filefs.getRepository(), tableName());
        ZipUtils zip = new ZipUtils(filefs.getRepository() + File.separator + subpath_filename_random);
        zip.addFile(filename, fileUpload.getFile().getBytes());
        zip.close();
        filefs.setFilename(filename);
        filefs.setPathfile(subpath_filename_random);
        filefs.setContenttype(fileUpload.getFile().getContentType());
    }

    protected void createDbFile(FileUpload fileUpload, IFileDbModelBean filedb) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipUtils zip = new ZipUtils(bos);
        String filename = FileUtils.sanitizeFilename(fileUpload.getFile().getOriginalFilename());
        zip.addFile(filename, fileUpload.getFile().getBytes());
        zip.close();
        filedb.setFilename(filename);
        filedb.setContenttype(fileUpload.getFile().getContentType());
        filedb.setData(bos.toByteArray());
    }

    public HttpEntity<byte[]> downloadFileFs(AFileFsModelBean filefs) throws Exception {
        String path_file=filefs.getRepository()+File.separator+filefs.getPathfile();
        String filename=filefs.getFilename();
        String contenttype=filefs.getContenttype();
        UnZipUtils unzip = new UnZipUtils(path_file);
        byte[] filebytes= unzip.getFileByteArray();
        return downloadFile(filebytes, filename, contenttype);
    }

    public HttpEntity<byte[]> downloadFileDb(IFileDbModelBean filedb) throws Exception {
        String filename=filedb.getFilename();
        String contenttype=filedb.getContenttype();
        UnZipUtils unzip = new UnZipUtils(filedb.getData());
        byte[] filebytes= unzip.getFileByteArray();
        return downloadFile(filebytes, filename, contenttype);
    }

}
