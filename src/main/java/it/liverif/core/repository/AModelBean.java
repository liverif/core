package it.liverif.core.repository;

import it.liverif.core.auth.beans.UserToken;
import it.liverif.core.model.annotations.FieldOrder;
import it.liverif.core.repository.generator.LoggedUserGenerator;
import it.liverif.core.web.beans.SelectItem;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@MappedSuperclass
public abstract class AModelBean extends AModelBaseBean {

    @GeneratorType(
            type = LoggedUserGenerator.class,
            when = GenerationTime.INSERT
    )
    @Column(name="create_by",length = 255)
    @FieldOrder(-980)
    private String createBy;

    @CreationTimestamp
    @Column(name="create_date")
    @FieldOrder(-970)
    private LocalDateTime createDate;

    @GeneratorType(
            type = LoggedUserGenerator.class,
            when = GenerationTime.ALWAYS
    )
    @Column(name="modify_by",length = 255)
    @FieldOrder(-960)
    private String modifyBy;

    @UpdateTimestamp
    @Column(name="modify_date")
    @FieldOrder(-950)
    private LocalDateTime modifyDate;

    @Column(name="text_search",columnDefinition = "TEXT")
    @FieldOrder(-945)
    private String textSearch;

    @Transient
    @FieldOrder(-940)
    private Map<String,String> htmlvalue=new LinkedHashMap<>();

    @Transient
    @FieldOrder(-930)
    private Map<String,SelectItem> htmlitem=new LinkedHashMap<>();

    @Transient
    @FieldOrder(-920)
    private Map<String, ArrayList<SelectItem>> htmlitems=new LinkedHashMap<>();

    @Transient
    @FieldOrder(-910)
    private Boolean viewButtonDetail=true;

    @Transient
    @FieldOrder(-900)
    private Boolean viewButtonUpdate=true;

    @Transient
    @FieldOrder(-890)
    private Boolean viewButtonDelete=true;

    @Transient
    @FieldOrder(-880)
    private Boolean viewButtonSave=true;

    @Transient
    @FieldOrder(-870)
    private String html;

    public abstract void initAction(UserToken userToken);

    public abstract String getHtml();

    public abstract void beforePersist();

    public String getHtmlvalue(String field){
        return (htmlvalue.get(field)!=null)?htmlvalue.get(field):"";
    }

    public SelectItem getHtmlitem(String field){
        return (htmlitem.get(field)!=null)?htmlitem.get(field):SelectItem.build("","");
    }

    public ArrayList<SelectItem> getHtmlitems(String field){
        return (htmlitems.get(field)!=null)?htmlitems.get(field):null;
    }

    public boolean initHtmlValue(){
        getHtmlitem().clear();
        getHtmlitems().clear();
        getHtmlvalue().clear();
        return true;
    }

    protected boolean entityInChange(){
        return getId() != null && getId() > 0L;
    }

}
