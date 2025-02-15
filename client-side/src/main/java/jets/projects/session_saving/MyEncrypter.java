package jets.projects.session_saving;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MyEncrypter {
    public String encrypt(String message, String key) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("No such algorithm catch.");
            System.err.println(ex.getMessage());
            return null;
        } catch (NoSuchPaddingException ex) {
            System.err.println("No such padding catch.");
            System.err.println(ex.getMessage());
            return null;
        }
        
        SecretKey secretKey = convertStringToKey(key);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (InvalidKeyException ex) {
            System.err.println("Invalikd key catch.");
            System.err.println(ex.getMessage());
            return null;
        }
        
        byte[] encrypedBytes;
        try {
            encrypedBytes = cipher.doFinal(message.getBytes());
        } catch (BadPaddingException ex) {
            System.err.println("Bad padding catch.");
            System.err.println(ex.getMessage());
            return null;
        } catch (IllegalBlockSizeException ex) {
            System.err.println("Illegal block size catch.");
            System.err.println(ex.getMessage());
            return null;
        }
        
        return Base64.getEncoder().encodeToString(encrypedBytes);
    }
    
    public String decrypt(String encryptedMessage, String key) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("No such algorithm catch.");
            System.err.println(ex.getMessage());
            return null;
        } catch (NoSuchPaddingException ex) {
            System.err.println("No such padding catch.");
            System.err.println(ex.getMessage());
            return null;
        }
        
        SecretKey secretKey = convertStringToKey(key);
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (InvalidKeyException ex) {
            System.err.println("Invalikd key catch.");
            System.err.println(ex.getMessage());
            return null;
        }

        byte[] decryptedBytes;
        try {
            decryptedBytes = cipher.doFinal(
                    Base64.getDecoder().decode(encryptedMessage));
        } catch (BadPaddingException ex) {
            System.err.println("Bad padding catch.");
            System.err.println(ex.getMessage());
            return null;
        } catch (IllegalBlockSizeException ex) {
            System.err.println("Illegal block size catch.");
            System.err.println(ex.getMessage());
            return null;
        }
        return new String(decryptedBytes);
    }
    
    public String generateNewKey() {
        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("No such algorithm exception.");
            System.err.println(ex.getMessage());
            return null;
        }
        
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();
        return convertKeyToString(secretKey);
    }
    
    private SecretKey convertStringToKey(String key) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(
                decodedKey, 
                0, 
                decodedKey.length, 
                "AES");
    }
    
    private String convertKeyToString(SecretKey secretKey) {
        byte[] encodedKey = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(encodedKey);
    }
}
