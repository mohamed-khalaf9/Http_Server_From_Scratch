import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RangeRequestHandler {

    public byte[] handleRangeRequest(String filePath,long start,long end) throws IOException{
        File file = new File(filePath);
        if(!file.exists() || start<0 || start > end || end < 0 || end > file.length()){
            throw new IllegalArgumentException("Invalid range or file does not exist.");
        }
        long chunkSize = end - start +1;
        byte[] chunk = new byte[(int)chunkSize];
        try(RandomAccessFile raf = new RandomAccessFile(file, "r")){
            raf.seek(start);
            raf.readFully(chunk);
        }
        return chunk;
    }
}
