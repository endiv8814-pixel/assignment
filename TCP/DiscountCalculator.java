package TCP;

public class DiscountCalculator {

    public static double calculateDiscount(double initial, int percent){

        if (percent < 0 || percent > 100) {
            return initial; // Return original price for invalid discount percentages
        }
        return initial * (1.0 - (percent / 100.0));


    }
    
}
