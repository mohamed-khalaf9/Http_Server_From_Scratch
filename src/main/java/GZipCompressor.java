import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GZipCompressor {

    public byte[] compressFile(String filePath) throws IOException {
        try(
                FileInputStream fis = new FileInputStream(filePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream gzos = new GZIPOutputStream(baos);
                )
        {
            byte[] buffer = new byte[4192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                gzos.write(buffer, 0, bytesRead);
            }
            gzos.finish();
            return baos.toByteArray();
        }

    }
}
