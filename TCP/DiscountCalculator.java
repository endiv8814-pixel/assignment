package TCP;

public class DiscountCalculator {

    public static double calculateDiscount(double initial, int percent){

        if (percent < 0) {
            percent = 0;
        }
        
        return initial * (1.0 - (percent / 100.0));
    }
    
}
