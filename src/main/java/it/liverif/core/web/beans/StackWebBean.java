package it.liverif.core.web.beans;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class StackWebBean extends AValidationBean implements Serializable {

    public static final String PARAMETER_ID="id";
    public static final String PARAMETER_FATHER_ID="father_id";
    public static final String PARAMETER_USERNAME="username";
    public static final String PARAMETER_PAG="pag";

    //Permette di passare un riferimento ad un modelname
    public static final String PARAMETER_MODEL="model";
    
    //Permette di passare un riferimento ad un campo selezionato
    public static final String PARAMETER_FIELD="field";
    
    //Azione da eseguire, da non confondere con l'azione del submit della form
    //Questo parametro non viene memorizzato nella sessione dello stack
    public static final String PARAMETER_ACTION="action";

/*    
    //Se usato con SKIP permette di richiamare un metodo (targetAction) diverso nello StackController non modificando lo StackWeb
    //Questo parametro non viene memorizzato nella sessione dello stack
    public static final String PARAMETER_METHOD="method";
 */
    // label da visualizzare come link
    private String label = "";

    // azione sullo stack riveniente dall URL (nello stack questa informazione viene eliminata)
    private String stackAction = "";

    // url che verra' chiamato
    private String target = "";

    private String targetAction = "";

    // ulteriori parametri
    private Map<String, String> params = new HashMap<>();

    // contiene il link per raggiungere direttamente questo elemento nello stack (action=SKIP)
    private String linkSkip = "";

}
