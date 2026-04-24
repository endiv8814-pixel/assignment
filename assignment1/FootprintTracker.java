package assignment1;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FootprintTracker {

    private String trackerName;

    private ObservableList<EmissionSource> entries;

    public void addEntry(EmissionSource entry){

        entries.add(entry);
    }
    public ObservableList<EmissionSource> getArrayList(){

        return this.entries;
    }
    public double getTotalEmissions(){

        double sum = 0.0;
        for (EmissionSource source : entries){
            sum += source.calculateEmission();
        }
        sum -= assignment1.GUI.PurchaseHandler.getOffsetAmount();
        System.out.println("Total emissions after offset: " + sum + " kg CO2");
        return sum;
    }


    

    public double getTotalEmissionsForUser(String userName){

        double sum = 0.0;

        for (EmissionSource name : entries){

            if (name.getUserName().equals(userName)){

                sum+=name.calculateEmission();

                
            }
        }
        return sum;
    }

    public String getHighestUser() {
        String highestUser = "";
        double highestAmount = 0.0;

        for (EmissionSource source : entries) {
            double userTotal = getTotalEmissionsForUser(source.getUserName());
            if (userTotal > highestAmount) {
                highestAmount = userTotal;
                highestUser = source.getUserName();
            }
        }
        return highestUser;
    }

public FootprintTracker(String trackerName) {
    this.trackerName = trackerName;
    this.entries = FXCollections.observableArrayList();
}

public void generateDailyReport() {
    
    System.out.println(" " + trackerName + " - Daily Report \n");

    ArrayList<String> users = new ArrayList<>();

    for (EmissionSource e : entries) {
        if (!users.contains(e.getUserName())) {
            users.add(e.getUserName());
        }
    }

    double grandTotal = 0.0;

    for (String user : users) {

        System.out.println("User: " + user);

        double userTotal = 0.0;

        for (EmissionSource e : entries) {
            if (e.getUserName().equals(user)) {

                System.out.println("  " + e);

                userTotal += e.calculateEmission();
            }
        }

        System.out.println("  Subtotal: "
                + String.format("%.2f", userTotal) + " kg CO2\n");

        grandTotal += userTotal;
    }

    System.out.println("Grand Total: "
            + String.format("%.2f", grandTotal) + " kg CO2");
    }
}