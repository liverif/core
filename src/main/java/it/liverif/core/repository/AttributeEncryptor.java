package it.liverif.core.repository;

import it.liverif.core.component.crypt.ACryptSyncAES;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.persistence.AttributeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.util.Base64;

@Slf4j
@Component
public class AttributeEncryptor extends ACryptSyncAES implements AttributeConverter<String, String>{

    @Autowired
    Environment environment;

    private String secret;
    private IvParameterSpec ips;
    private SecretKey aesKey;

    @PostConstruct
    public void init(){
        secret=environment.getProperty("app.key.encrypt");
        if(StringUtils.hasText(secret)) {
            ips = generateIVfromKey(secret);
            aesKey = getKey(secret);
        }else{
            log.warn("app.key.encrypt not defined");
        }
    }

    @Override
    public String convertToDatabaseColumn(String value) {
        if (value==null) return null;
        try {
            return enc(value);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String value) {
        if (value==null) return null;
        try {
            return dec(value);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String enc(Serializable value) throws Exception{
        byte[] data = SerializationUtils.serialize(value);
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        ByteArrayOutputStream os= new ByteArrayOutputStream();
        encrypt(aesKey, is, os, ips);
        String result= Base64.getEncoder().encodeToString(os.toByteArray());
        return result;
    }

    public String dec(String value) throws Exception{
        byte[] data = Base64.getDecoder().decode(value);
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        decrypt(aesKey, is, os, ips);
        Object result= SerializationUtils.deserialize(os.toByteArray());
        return String.valueOf(result);
    }


}
