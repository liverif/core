package it.liverif.core.web.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AValidationBean {

    // numero casuale per rendere  piu' difficile la decifratura
    private String random = "";

    // numero di validazione
    private String validator = "";

}
