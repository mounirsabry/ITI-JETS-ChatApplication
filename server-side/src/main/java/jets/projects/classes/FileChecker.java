package jets.projects.classes;

public class FileChecker {
    public static final int MAX_FILE_SIZE = 15 * 1024 * 1024; // 15 MB.
    
    public static boolean isSizeValid(byte[] fileData) {
        if (fileData == null) {
            throw new NullPointerException("parameter cannot be null.");
        }
        return fileData.length <= MAX_FILE_SIZE;
    }
    
    public static boolean isTypeValid(byte[] fileData) {
        if (fileData == null) {
            throw new NullPointerException("parameter cannot be null.");
        }
        return true;
    }
}
