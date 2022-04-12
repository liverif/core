package it.liverif.core.component.crypt;

import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Base64;

@Component
public class DataEncryptAES extends ACryptSyncAES {

    public String enc(String key, Serializable value) throws Exception{
        byte[] data = SerializationUtils.serialize(value);
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        ByteArrayOutputStream os= new ByteArrayOutputStream();
        IvParameterSpec ips=generateIVfromKey(key);
        encrypt(key, is, os, ips);
        String result= Base64.getEncoder().encodeToString(os.toByteArray());
        return result;
    }

    public Object dec(String key, String value) throws Exception{
        byte[] data = Base64.getDecoder().decode(value);
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        IvParameterSpec ips=generateIVfromKey(key);
        decrypt(key, is, os, ips);
        Object result= SerializationUtils.deserialize(os.toByteArray());
        return result;
    }

}
