package it.liverif.core.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;

@Component
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private static final String AESKey = "AES";
    private static final String AESAlg = "AES/ECB/PKCS5Padding";
    
    @Value("${app.key.encrypt}")
    private String secret;
    private Key key;
    private Cipher cipher;

    @PostConstruct
    public void AttributeEncryptor() throws Exception {
        key = new SecretKeySpec(secret.getBytes(Charset.defaultCharset()), AESKey);
        cipher = Cipher.getInstance(AESAlg);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute==null) return null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes(Charset.defaultCharset())));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData==null) return null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)),Charset.defaultCharset());
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalStateException(e);
        }
    }
}
