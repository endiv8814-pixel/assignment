package assignment1;

public class TransportationEmission extends EmissionSource{

    private double distanceKM;

    private String transportMode;


    public TransportationEmission(String sourceID, String category, String date, String userName,
        double distanceKM, String transportMode
    ) {
        super(sourceID, category, date, userName);

        this.distanceKM=Math.abs(distanceKM);

        this.transportMode=transportMode;
    }


    public double getDistanceKM() {
        return distanceKM;
    }
    public void setDistanceKM(double distanceKM) {
        this.distanceKM = Math.abs(distanceKM);
    }

    public String getTransportMode() {
        return transportMode;
    }
    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }
    
    @Override
    public double calculateEmission() {

        if (this.getTransportMode().equalsIgnoreCase("bicycle")) {
            return 0.0;
        }

        else if (this.getTransportMode().equalsIgnoreCase("car")) {
            return this.getDistanceKM() * 0.18;
        }

        else if (this.getTransportMode().equalsIgnoreCase("bus")) {
            return this.getDistanceKM() * 0.09;
        }

        else if (this.getTransportMode().equalsIgnoreCase("train")) {
            return this.getDistanceKM() * 0.04;
        }

        else {
            return 0.0;
        }
    }

    @Override
    public String toString() {
        
        return super.toString() + 
               ", Distance (KM): " + distanceKM + 
               ", Transport Mode: " + transportMode + 
               ", Total Emission: " + String.format("%.2f", calculateEmission());
    }
    
    
}
