package assignment1;



public class GreenPrintCLI {

    public static void main(String[] args) {

        FootprintTracker tracker = new FootprintTracker("RIT GreenPrint 2026");

        
        TransportationEmission t1 = new TransportationEmission(
                "T-001", "Transportation", "2026-02-12", "Azizkhan",
                15.0, "Car");

        FoodEmission f1 = new FoodEmission(
                "F-001", "Food", "2026-02-12", "Azizkhan",
                "Vegan", 2);

        tracker.addEntry(t1);
        tracker.addEntry(f1);


        
        EnergyEmission e1 = new EnergyEmission(
                "E-001", "Energy", "2026-02-12", "Roman",
                8.5, "Grid");

        TransportationEmission t2 = new TransportationEmission(
                "T-002", "Transportation", "2026-02-12", "Roman",
                -22.0, "Bus");

        tracker.addEntry(e1);
        tracker.addEntry(t2);


    
        FoodEmission f2 = new FoodEmission(
                "F-002", "Food", "2026-02-12", "Danil",
                "Beef", 1);

        EnergyEmission e2 = new EnergyEmission(
                "E-002", "Energy", "2026-02-12", "Danil",
                5.0, "Solar");

        tracker.addEntry(f2);
        tracker.addEntry(e2);


        
        tracker.generateDailyReport();

        System.out.println("\nTotal emissions today: "
                + tracker.getTotalEmissions());
    }
}