import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
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


    public synchronized String generateETag(String fileName) throws IOException {
        try {
            Path filePath = Path.of(fileName);
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException("File not found: " + fileName);
            }

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Read file in chunks
            try (InputStream fis = new BufferedInputStream(Files.newInputStream(filePath))) {
                byte[] buffer = new byte[8192]; // 8KB buffer
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }


            byte[] hash = digest.digest();
            String etag= Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            fileEtags.put(fileName,etag);
            return etag;

        } catch (NoSuchAlgorithmException e) {
            throw new IOException("SHA-256 algorithm not available", e);
        }
    }




    public synchronized boolean compare(String fileName,String etag) throws IOException{
        String currentETag = generateETag(fileName);
        return currentETag != null && currentETag.equals(etag);
    }
    public synchronized String getFileEtag(String fileName) {
        return fileEtags.get(fileName);
    }

}
