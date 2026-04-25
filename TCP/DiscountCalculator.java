package TCP;

public class DiscountCalculator {

    public static double calculateDiscount(double initial, int percent){

<<<<<<< HEAD
        if (percent < 0) {
            percent = 0;
        }
        
=======
        if (percent < 0 || percent > 100) {
            return initial; // Return original price for invalid discount percentages
        }
>>>>>>> 133d3624fe25527a7ba60abd8ec5660718e7df29
        return initial * (1.0 - (percent / 100.0));
    }
    
}
