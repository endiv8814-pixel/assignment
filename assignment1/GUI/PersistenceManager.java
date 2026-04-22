package assignment1.GUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import assignment1.EmissionSource;
import assignment1.EnergyEmission;
import assignment1.FoodEmission;
import assignment1.FootprintTracker;
import assignment1.TransportationEmission;
import javafx.collections.ObservableList;
// handles saving and loading app state to greenprint_state.txt
public class PersistenceManager { 
    // saves all entries in tracker to greenprint_state.txt
    // overwrites the file each time
    // returns nothing
    public static void saveState(FootprintTracker tracker){
        ObservableList<EmissionSource> entries = tracker.getArrayList();

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("greenprint_state.txt", false));
            for(EmissionSource e : entries){
                String line = makeLine(e);
                if(line != null){
                    writer.write(line);
                    writer.newLine();
                }
            }writer.close();
            LoggerUtil.logStateSaved(entries.size());
        } catch(IOException e){ System.out.println("save error: " + e.getMessage());}
    }

 // returns String like "Transportation|T-001|..." or null if unknown type
    public static String makeLine(EmissionSource e){
        if(e instanceof TransportationEmission){
            TransportationEmission t = (TransportationEmission) e;
            return "Transportation|" + t.getSourceID() + "|" + t.getCategory() + "|" + t.getDate() + "|" + t.getUserName() + "|" + t.getDistanceKM() + "|" + t.getTransportMode();
        }

        else if(e instanceof EnergyEmission){
            EnergyEmission en = (EnergyEmission) e;
            return "Energy|" + en.getSourceID() + "|" + en.getCategory() + "|" + en.getDate() + "|" + en.getUserName() + "|" + en.getkWhUsed() + "|" + en.getEnergySource();

        }
        else if(e instanceof FoodEmission){
            FoodEmission f = (FoodEmission) e;
            return "Food|" + f.getSourceID() + "|" + f.getCategory() + "|" + f.getDate() + "|" + f.getUserName() + "|" + f.getMealType() + "|" + f.getNumberOfMeals();
        } return null;

    }

    // reads greenprint_state.txt and restores entries to tracker
    // if file doesnt exist does nothing
    // returns nothing
    public static void loadState(FootprintTracker tracker){
        File file = new File("greenprint_state.txt");
        if(!file.exists()){
            return;
        }

        int count = 0;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null){

                line = line.trim();

                if(line.isEmpty()) continue;

                String[] parts = line.split("\\|");

                if(parts.length < 5) continue;

                String type = parts[0];
                String id = parts[1];
                String cat = parts[2];
                String date = parts[3];
                String user = parts[4];

                try{
                    if(type.equals("Transportation") && parts.length >= 7){
                        double dist = Double.parseDouble(parts[5]);
                        String mode = parts[6];
                        tracker.addEntry(new TransportationEmission(id, cat, date, user, dist, mode));
                        count++;

                    } else if(type.equals("Energy") && parts.length >= 7){
                        double kwh = Double.parseDouble(parts[5]);
                        String src = parts[6];
                        tracker.addEntry(new EnergyEmission(id, cat, date, user, kwh, src));
                        count++;

                    } else if(type.equals("Food") && parts.length >= 7){
                        String meal = parts[5];
                        double meals = Double.parseDouble(parts[6]);
                        tracker.addEntry(new FoodEmission(id, cat, date, user, meal, meals));
                        count++;
                    }

                } catch(NumberFormatException ex){System.out.println("bad line skipped: " + line);}
            }

            reader.close();
            LoggerUtil.logStateLoaded(count);
        } catch(IOException e){
            System.out.println("load error: " + e.getMessage());
        }
    }
}
