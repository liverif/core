package it.liverif.core.repository;

import java.io.Serializable;

public interface IModelBaseBean extends Serializable{

    public Long getId();
    public void setId(Long id);

    public Long getVer();
    public void setVer(Long ver);

    public void beforePersist();
}
