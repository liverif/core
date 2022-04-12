package it.liverif.core.repository;

import it.liverif.core.model.annotations.FieldOrder;

import javax.persistence.*;

@MappedSuperclass
public abstract class AModelBaseBean implements IModelBaseBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @FieldOrder(-1000)
    private Long id;

    @Version
    @Column(columnDefinition = "INT")
    @FieldOrder(-990)
    private Long ver;

    @Override
    public void setId(Long id){
        this.id=id;
    }

    @Override
    public Long getId(){
        if (this.id==null) return 0L;
        return this.id;
    }

    @Override
    public Long getVer() {
        return ver;
    }

    @Override
    public void setVer(Long ver) {
        this.ver = ver;
    }
    
}