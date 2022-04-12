package it.liverif.core.repository;

import com.sun.istack.NotNull;
import it.liverif.core.model.annotations.FieldOrder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@MappedSuperclass
public abstract class AFileDb2ModelBean extends AModelBean implements IFileDbModelBean{

    @FieldOrder(-910)
    @NotNull
    @Size(min=1,max=255)
    @Column(length = 255)
    String filename;

    @FieldOrder(-900)
    @NotNull
    @Size(min=3,max=255)
    @Column(length = 255)
    String contenttype;

    @FieldOrder(-890)
    @NotNull
    @Lob
    @Basic(fetch = FetchType.LAZY)
    byte[] data;

}