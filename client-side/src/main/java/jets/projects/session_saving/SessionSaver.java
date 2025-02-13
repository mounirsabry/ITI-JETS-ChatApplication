package jets.projects.session_saving;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SessionSaver {
    private static final String KEY_FILE_PATH = "session_key.txt";
    private static final String SESSION_FILE_PATH = "session.xml";
    
    private final MyEncrypter myEncrypter;
    private final MyMarshaller myMarshaller;
    
    public SessionSaver() {
        myEncrypter = new MyEncrypter();
        myMarshaller = new MyMarshaller();
    }
    
    public void save(NormalUserSavedSession session) {
        String key = loadSavedKey();
        if (key == null) {
            System.err.println("There is no saved key.");
            return;
        }
        
        String encrypedPhoneNumber = myEncrypter.encrypt(
                session.getPhoneNumber(), key);
        if (encrypedPhoneNumber == null) {
            System.err.println("Failed to encrypt the phone number.");
            return;
        }
        
        String encrypedPassword = myEncrypter.encrypt(
                session.getPassword(), key);
        if (encrypedPassword == null) {
            System.err.println("Failed to encrypt the password.");
            return;
        }
        
        NormalUserSavedSession encryptedSession = new NormalUserSavedSession(
                encrypedPhoneNumber, encrypedPassword);
        
        boolean isSaved = myMarshaller.marshal(
                SESSION_FILE_PATH, encryptedSession);
        if (!isSaved) {
            System.err.println("Could not save the session file.");
        }
    }
    
    public NormalUserSavedSession load() {
        String key = loadSavedKey();
        if (key == null) {
            System.err.println("There is no saved key.");
            return null;
        }
        
        File file = new File(SESSION_FILE_PATH);
        if (!file.exists()) {
            System.err.println("There is no saved session.");
            return null;
        }
        
        NormalUserSavedSession encryptedSession = myMarshaller.unmarshal(
                SESSION_FILE_PATH);
        if (encryptedSession == null) {
            System.err.println("Failed to read the saved session.");
            return null;
        }
        
        String decryptedPhoneNumber = myEncrypter.decrypt(
                encryptedSession.getPhoneNumber(), key);
        if (decryptedPhoneNumber == null) {
            System.err.println("Could not decrypt the phone number.");
            return null;
        }
        
        String decrypedPassword = myEncrypter.decrypt(
                encryptedSession.getPassword(), key);
        if (decrypedPassword == null) {
            System.err.println("Could not decrypt the password.");
            return null;
        }
        
        return new NormalUserSavedSession(
                decryptedPhoneNumber, decrypedPassword);
    }
    
    public boolean deleteSessionFile() {
        Path sessionFilePath = Paths.get(SESSION_FILE_PATH);
        
        try {
            Files.delete(sessionFilePath);
            return true;
        } catch (IOException ex) {
            System.err.println("Could not delete the session file.");
            System.err.println(ex.getMessage());
            return false;
        }
    }
    
    private String loadSavedKey() {
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream(KEY_FILE_PATH);
        if (inputStream == null) {
            System.err.println("Could not load the key file.");
            return null;
        }
        
        byte[] fileContet;
        try {
            fileContet = inputStream.readAllBytes();
        } catch (IOException ex) {
            System.err.println("Could not read the content of the key file.");
            System.err.println(ex.getMessage());
            return null;
        }
        
        try {
            inputStream.close();
        } catch (IOException ex) {
            System.err.println("Failed to close the "
                    + "stream after reading the key.");
            System.err.println(ex.getMessage());
        }
        
        return new String(fileContet);
    }
}
