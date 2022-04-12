package it.liverif.core.repository;

import com.sun.istack.NotNull;
import it.liverif.core.model.annotations.FieldOrder;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostRemove;
import javax.validation.constraints.Size;
import java.io.File;


@Getter
@Setter
@MappedSuperclass
public abstract class AFileFsModelBean extends AModelBean {

    @FieldOrder(-910)
    @NotNull
    @Size(min=1,max=255)
    @Column(length = 255)
    private String repository;

    @FieldOrder(-900)
    @NotNull
    @Size(min=1,max=255)
    @Column(length = 255)
    String filename;

    @FieldOrder(-890)
    @NotNull
    @Size(min=3,max=255)
    @Column(length = 255)
    String contenttype;

    @FieldOrder(-880)
    @NotNull
    @Size(min=1,max=1000)
    @Column(length = 1000)
    String pathfile;

    @PostRemove
    public void removeFile() {
        File file=new File(this.getRepository()+File.separator+this.getPathfile());
        if (file.exists()) file.delete();
    }

}