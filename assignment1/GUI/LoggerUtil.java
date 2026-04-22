package assignment1.GUI;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtil { // utility class for logging operations to greenprint_log.txt
    // writes a line to greenprint_log.txt
    // type: operation type (ENTRY_ADDED, OFFSET_PURCHASED, etc)
    // details: relevant info about the operation
    public static void log(String type, String details){

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String line = time + " " + type + " " + details;

        try{

            BufferedWriter writer = new BufferedWriter(new FileWriter("greenprint_log.txt", true));

            writer.write(line);
            writer.newLine();
            writer.close();

        } catch(IOException e){

            System.out.println("log error: " + e.getMessage());
        }
    }

    // logs when a new entry is added
    public static void logEntryAdded(String id, String user, double emission){log("ENTRY_ADDED", id + " " + user + " " + String.format("%.2f", emission) + "kg"); }
    // logs when offset is purchased
    public static void logOffsetPurchased(double kg, String payment){log("OFFSET_PURCHASED", String.format("%.2f", kg) + "kg " + payment);}
    // logs when state is saved
    public static void logStateSaved(int count){log("STATE_SAVED", "total=" + count);}
    // logs when state is loaded
    public static void logStateLoaded(int count){log("STATE_LOADED", "total=" + count);}


}
