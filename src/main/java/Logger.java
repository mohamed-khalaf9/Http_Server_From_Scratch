import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Logger {
    private static final String LOG_FILE_PATH = "http_logs.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public Logger(){

    }


    public static synchronized void logRequestAndResponse(Object requestLog, Object responseLog) {
        List<Object> logs = new ArrayList<>();

        // Read existing logs if the file exists
        File logFile = new File(LOG_FILE_PATH);
        if (logFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
                logs = new Gson().fromJson(reader, List.class);
                if (logs == null) {
                    logs = new ArrayList<>();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Create a single log entry containing both request and response
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("request", requestLog);
        logEntry.put("response", responseLog);

        // Append the new log entry
        logs.add(logEntry);

        // Write the updated logs back to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, false))) {
            writer.println(gson.toJson(logs));  // Write as a JSON array
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


