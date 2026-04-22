package assignment1;

public class FoodEmission extends EmissionSource {

    private String mealType;
    private double numberOfMeals;

    public FoodEmission(String sourceID, String category, String date, String userName, 
                        String mealType, double numberOfMeals) {
        super(sourceID, category, date, userName);
        this.mealType = mealType;
        this.numberOfMeals = Math.abs(numberOfMeals);
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public double getNumberOfMeals() {     
        return numberOfMeals;
    }

    public void setNumberOfMeals(double numberOfMeals) {  
        this.numberOfMeals = Math.abs(numberOfMeals);      
    }

    @Override
    public double calculateEmission() {
       
        if (this.getMealType().equalsIgnoreCase("vegan")){

            return this.getNumberOfMeals()*0.3;
        }
        else if (this.getMealType().equalsIgnoreCase("beef")){

            return this.getNumberOfMeals()*7.0;
        }
        else if (this.getMealType().equalsIgnoreCase("poultry")){

            return this.getNumberOfMeals()*1.5;
        }
        else if (this.getMealType().equalsIgnoreCase("vegetarian")){

            return this.getNumberOfMeals()*0.7;
        }
        else{
            return 0.0;
        }
    }
    @Override
    public String toString() {
        return super.toString() +
               ", Meal Type: " + mealType +
               ", Meals: " + numberOfMeals +          
               ", Total Emission: " + String.format("%.2f", calculateEmission());
    }
}