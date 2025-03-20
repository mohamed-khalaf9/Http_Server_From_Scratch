import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ETagManager {
    private final Map<String, String> fileEtags;

    public ETagManager() {
        fileEtags = new ConcurrentHashMap<>();
    }

    public synchronized String generateEtag(String filePath) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(filePath);

        byte[] buffer = new byte[4192];
        int bytesRead;

        while((bytesRead = fis.read(buffer)) !=-1)
        {
            digest.update(buffer, 0, (int)bytesRead);
        }
        fis.close();
        byte[] hash = digest.digest();
        String etag = Base64.getEncoder().encodeToString(hash);
        fileEtags.put(filePath, etag);
        return etag;
    }

    public synchronized boolean compare(String filePath,String etag) throws NoSuchAlgorithmException, IOException {
        String currentEtag = this.generateEtag(filePath);
        return currentEtag.equals(etag);
    }


}
