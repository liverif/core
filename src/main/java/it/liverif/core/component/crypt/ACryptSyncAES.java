package it.liverif.core.component.crypt;

import org.apache.commons.io.IOUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public abstract class ACryptSyncAES {

    public static final String KEY_ALGORITHM = "AES";
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final Integer KEY_SIZE = 256;
    public static final String SECRET_ALGORITHM = "PBKDF2WithHmacSHA256";
    public static final String DIGEST_ALGORITHM = "SHA-256";
    
    protected void encrypt(String key, InputStream is, OutputStream os, IvParameterSpec ips) throws Exception{
        encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os, ips);
    }

    protected void decrypt(String key, InputStream is, OutputStream os, IvParameterSpec ips) throws Exception {
        encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os, ips);
    }

    protected void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os, IvParameterSpec ips) throws Exception {
        SecretKey aesKey = getKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, ips);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            IOUtils.copy(cis, os);
            os.flush();
            os.close();
            cis.close();
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, aesKey, ips);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            IOUtils.copy(is, cos);
            cos.flush();
            cos.close();
            is.close();
        }
    }

    protected String generateKey() throws Exception{
        KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGen.init(KEY_SIZE);
        SecretKey secretKey = keyGen.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        return encodedKey;
    }

    protected SecretKey getKey(String encodedKey){
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, KEY_ALGORITHM);
        return originalKey;
    }

    protected static IvParameterSpec generateIVfromKey(String key){
        byte[] iv = Arrays.copyOfRange(key.getBytes(Charset.defaultCharset()), 0, 16);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        return ivParameterSpec;
    }

    public String generateKey(String password) throws Exception{
        byte[] key = password.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance(DIGEST_ALGORITHM);
        byte[] salt = sha.digest(key);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_ALGORITHM);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, KEY_SIZE);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), KEY_ALGORITHM);

        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        return encodedKey;
    }

}