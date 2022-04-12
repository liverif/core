package it.liverif.core.controller.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StackWebConfig {

    private String defaultTemplatePage;
    private String baseTemplateModelPages;
    private String stackContext;

    public static StackWebConfig build(String defaultTemplatePage, String baseTemplateModelPages, String stackContext){
        StackWebConfig stackWebConfig =new StackWebConfig();
        stackWebConfig.setDefaultTemplatePage(defaultTemplatePage);
        stackWebConfig.setBaseTemplateModelPages(baseTemplateModelPages);
        stackWebConfig.setStackContext(stackContext);
        return stackWebConfig;
    }

}
