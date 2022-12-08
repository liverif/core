package it.liverif.core.web.beans;

import it.liverif.core.model.annotations.EntityTransient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileUpload {

    String field;
    MultipartFile file;

}
