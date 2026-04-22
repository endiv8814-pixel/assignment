package assignment1;

public class EnergyEmission extends EmissionSource{

    private double kWhUsed;

    private String EnergySource;
    
    public EnergyEmission(String sourceID, String category, String date, String userName,
        double kWhUsed, String EnergySource
    ) {


        super(sourceID, category, date, userName);

        this.kWhUsed=Math.abs(kWhUsed);

        this.EnergySource=EnergySource;
        

    }

    public double getkWhUsed() {
        return kWhUsed;
    }
    public void setkWhUsed(double kWhUsed) {
        this.kWhUsed = Math.abs(kWhUsed);
    }

    public String getEnergySource() {
        return EnergySource;
    }
    public void setEnergySource(String energySource) {
        this.EnergySource = energySource;
    }

    @Override
    public double calculateEmission() {
       
        if (this.getEnergySource().equalsIgnoreCase("grid")){

            return this.getkWhUsed()*0.5;
        }
        else if (this.getEnergySource().equalsIgnoreCase("solar")){

            return getkWhUsed()*0.05;
        } 
        else if (this.getEnergySource().equalsIgnoreCase("wind")){

            return getkWhUsed()*0.015;
        } 
        else{
            return 0.0;
        }
    }

    
    @Override
    public String toString() {
        
        return super.toString() + 
               ", Energy Source: " + EnergySource + 
               ", kWh Used: " + kWhUsed + 
               ", Total Emission: " + String.format("%.2f", calculateEmission());
    }

    
}
