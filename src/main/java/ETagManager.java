import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECField;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ETagManager {
    private final Map<String, String> fileEtags;
    private static  ETagManager instance;

    private ETagManager() {
        fileEtags = new ConcurrentHashMap<>();

    }
    public static synchronized ETagManager getInstance() {
        if(instance == null) {
            instance = new ETagManager();
        }
        return instance;

    }


    public synchronized String generateETag(String fileName) throws IOException, NoSuchAlgorithmException {
        Path filePath = Path.of(fileName);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + fileName);
        }

        byte[] fileContent = Files.readAllBytes(filePath);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(fileContent);
        return Base64.getEncoder().encodeToString(hash);
    }
    public synchronized void  createFileETag(String fileName) throws IOException, NoSuchAlgorithmException {
        String etag = generateETag(fileName);
        fileEtags.put(fileName.toString(), etag);
    }


    public synchronized boolean compare(String filePath,String etag) {
        try {
            String currentEtag = this.generateETag(filePath);
            return currentEtag.equals(etag);
        }
        catch (Exception c)
        {
            return false;
        }
    }
    public synchronized String getFileEtag(String fileName) {
        return fileEtags.get(fileName);
    }

}
