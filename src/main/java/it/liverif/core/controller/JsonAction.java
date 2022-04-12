package it.liverif.core.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.liverif.core.component.crypt.WebEncryptAES;
import it.liverif.core.controller.beans.AValidationBean;
import it.liverif.core.controller.beans.ActionBean;
import it.liverif.core.controller.beans.GenericParametersBean;
import it.liverif.core.controller.beans.StackWebBean;
import it.liverif.core.utils.CoreBeanUtils;
import it.liverif.core.utils.RandomStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JsonAction {

    @Autowired
    private WebEncryptAES webEncryptAES;

    @Autowired
    private HttpServletRequest request;

    public static final String SESSION_KEYJSONACTION = "session_keyjsonaction";
    public static final String SESSION_VALIDATOR = "session_validator";

    public String encode(AValidationBean parameter) {
        return _encode(parameter, true);
    }

    private String _encode(AValidationBean stackWebBean, boolean base64) {
        String result = "";
        if (stackWebBean != null) {
            String jsonResult = "";
            try {
                Long validator = (Long) request.getSession().getAttribute(SESSION_VALIDATOR);
                String random = RandomStringUtils.generateAlfanumeric(10);
                stackWebBean.setRandom(random);
                stackWebBean.setValidator(validator.toString());
                jsonResult = CoreBeanUtils.toJson(stackWebBean);
                if (base64) {
                    String encResult = encrypt(jsonResult);
                    result = Base64.getEncoder().encodeToString(encResult.getBytes(Charset.defaultCharset())).replace("+", "-").replace("/", "_");
                } else
                    result = jsonResult;
            } catch (Exception e) {
            }
        }
        return result;
    }

    public StackWebBean decodeStackWeb(String value) {
        return _decodeStackWeb(value, true);
    }

    private StackWebBean _decodeStackWeb(String value, boolean base64) {
        StackWebBean stackWebBean = new StackWebBean();
        if (StringUtils.hasText(value)) {
            String decoded = "";
            try {
                if (base64) {
                    byte[] decodedBytes = Base64.getDecoder().decode(value.replace("-", "+").replace("_", "/"));
                    decoded = decrypt(new String(decodedBytes,Charset.defaultCharset()));
                } else {
                    decoded = value;
                }
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<StackWebBean> typeRef = new TypeReference<StackWebBean>() {};
                stackWebBean = mapper.readValue(decoded, typeRef);
            } catch (Exception e) {
            }
        }
        return stackWebBean;
    }

    public ActionBean decodeAction(String value) {
        return _decodeAction(value, true);
    }

    private ActionBean _decodeAction(String value, boolean base64) {
        ActionBean stackWebBean = new ActionBean();
        if (StringUtils.hasText(value)) {
            String decoded = "";
            try {
                if (base64) {
                    byte[] decodedBytes = Base64.getDecoder().decode(value.replace("-", "+").replace("_", "/"));
                    decoded = decrypt(new String(decodedBytes,Charset.defaultCharset()));
                } else {
                    decoded = value;
                }
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<ActionBean> typeRef = new TypeReference<ActionBean>() {};
                stackWebBean = mapper.readValue(decoded, typeRef);
            } catch (Exception e) {
            }
        }
        return stackWebBean;
    }

    public GenericParametersBean decodeGenericParameters(String value) {
        return _decodeGenericParameters(value, true);
    }

    private GenericParametersBean _decodeGenericParameters(String value, boolean base64) {
        GenericParametersBean genericParametersBean = new GenericParametersBean();
        if (StringUtils.hasText(value)) {
            String decoded = "";
            try {
                if (base64) {
                    byte[] decodedBytes = Base64.getDecoder().decode(value.replace("-", "+").replace("_", "/"));
                    decoded = decrypt(new String(decodedBytes,Charset.defaultCharset()));
                } else {
                    decoded = value;
                }
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<GenericParametersBean> typeRef = new TypeReference<GenericParametersBean>() {};
                genericParametersBean = mapper.readValue(decoded, typeRef);
            } catch (Exception e) {
            }
        }
        return genericParametersBean;
    }

    public void initSession() throws Exception {
        String key = webEncryptAES.generateKey();
        Long validator = new Date().getTime() / 1000;
        request.getSession().setAttribute(SESSION_KEYJSONACTION, key);
        request.getSession().setAttribute(SESSION_VALIDATOR, validator);
    }

    private String encrypt(String value) throws Exception {
        String key = (String) request.getSession().getAttribute(SESSION_KEYJSONACTION);
        Long validator = (Long) request.getSession().getAttribute(SESSION_VALIDATOR);
        return webEncryptAES.encryptString(key, value);
    }

    private String decrypt(String value) throws Exception {
        String key = (String) request.getSession().getAttribute(SESSION_KEYJSONACTION);
        Long validator = (Long) request.getSession().getAttribute(SESSION_VALIDATOR);
        return webEncryptAES.decryptString(key, value);
    }

}
