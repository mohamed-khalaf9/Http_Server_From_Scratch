import java.io.BufferedWriter;
import java.io.IOException;

public class Utilities {
    public static long[] parseRange(String rangeHeader, long fileSize) {
        if (rangeHeader == null || !rangeHeader.startsWith("bytes=")) {
            return new long[]{0, 0};
        }

        String rangePart = rangeHeader.substring(6).strip();
        String[] parts = rangePart.split("-");
        if (parts.length != 2) {
            return new long[]{0, 0};
        }

        try {
            long start = parts[0].isEmpty() ? 0 : Long.parseLong(parts[0]);
            long end = parts[1].isEmpty() ? fileSize - 1 : Long.parseLong(parts[1]);


            if (start < 0 || start > end || end >= fileSize) {
                return new long[]{0, 0};
            }

            return new long[]{start, end};
        } catch (NumberFormatException e) {
            return new long[]{0, 0};
        }
    }

    private static void sendResponse(BufferedWriter out, HttpResponse response) throws IOException {
        synchronized (out){
            out.write(response.getRawResponse());
            out.newLine();
        }

    }




}
