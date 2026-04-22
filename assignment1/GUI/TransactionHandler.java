package assignment1.GUI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// handles carbon offset transaction calculations and receipt generation
public class TransactionHandler {

    public static double getCost(double kg){return kg * 0.015;}  // calculates cost of offsetting kg of CO2
    public static String makeReceipt(double kg, String payment){ // generates a receipt string for the offset transaction

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        double cost = getCost(kg);
       String receipt = "OFFSET RECEIPT\n" + 
        "date: " + time + "\n" +
        "amount: " + String.format("%.2f", kg) + " kg CO2\n" +
        "cost: $" + String.format("%.2f", cost) + "\n" +
        "payment: " + payment + "\n" +
        "status: confirmed";
        return receipt;
    }
    public static String makeHistoryLine(double kg, String payment){ // generates a one line summary for the history list
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return time + " | " + String.format("%.2f", kg) + " kg | $" + String.format("%.2f", getCost(kg)) + " | " + payment;
    }
}
