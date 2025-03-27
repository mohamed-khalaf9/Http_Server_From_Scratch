import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    private static final String LOG_FILE_PATH = "http_logs.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();





    private static synchronized void writeLogToFile(Object log) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
            String jsonLog = gson.toJson(log);
            writer.println(jsonLog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
