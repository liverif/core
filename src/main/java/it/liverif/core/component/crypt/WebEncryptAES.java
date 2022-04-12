package it.liverif.core.component.crypt;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class WebEncryptAES {

    private static final Integer KEY_SIZE = 256;
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    public String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGen.init(KEY_SIZE);
        SecretKey secretKey = keyGen.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        return encodedKey;
    }

    public SecretKey getKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, KEY_ALGORITHM);
        return originalKey;
    }

    public void encrypt(String key, InputStream is, OutputStream os) throws Exception {
        encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
    }

    public void decrypt(String key, InputStream is, OutputStream os) throws Exception {
        encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
    }

    public void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Exception {
        SecretKey aesKey = getKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            IOUtils.copy(cis, os);
            os.flush();
            os.close();
            cis.close();
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            IOUtils.copy(is, cos);
            cos.flush();
            cos.close();
            is.close();
        }
    }

    public String encryptString(String key, String value) throws Exception {
        InputStream is = new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        encrypt(key, is, os);
        return Base64.getEncoder().encodeToString(os.toByteArray());
    }

    public String decryptString(String key, String value) throws Exception {
        byte[] byteEnc = Base64.getDecoder().decode(value);
        InputStream is2 = new ByteArrayInputStream(byteEnc);
        ByteArrayOutputStream os2 = new ByteArrayOutputStream();
        decrypt(key, is2, os2);
        return new String(os2.toByteArray(), StandardCharsets.UTF_8);
    }

    public String encryptStringSafetyUrl(String key, String value) throws Exception {
        return encryptString(key, value).replace("+", "-").replace("/", "_");
    }

    public String decryptStringSafetyUrl(String key, String value) throws Exception {
        return decryptString(key, value.replace("-", "+").replace("_", "/"));
    }

}

