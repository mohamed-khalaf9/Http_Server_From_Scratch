import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GZipCompressor {

    public static byte[] compressFile(byte[] fileContent) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPOutputStream gzos = new GZIPOutputStream(baos)) {

            gzos.write(fileContent);
            gzos.finish();
            return baos.toByteArray();
        }
    }

    }

