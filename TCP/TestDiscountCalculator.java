package TCP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;

public class TestDiscountCalculator {
    @Test
    public void testCalculateDiscount() {
        int result = (int) DiscountCalculator.calculateDiscount(100.0, 0);
        assertEquals(100, result, "No discount should return the original price.");

        result = (int) DiscountCalculator.calculateDiscount(200.0, 100);
        assertEquals(0, result, "100% discount should return 0.");

        result = (int) DiscountCalculator.calculateDiscount(200.0, 30);
        assertEquals(140, result, "30% discount should return 140.");

        result = (int) DiscountCalculator.calculateDiscount(200.0, 1);
        assertEquals(198, result, "1% discount should return 198.");

        result = (int) DiscountCalculator.calculateDiscount(200.0, -10);
        assertEquals(200, result, "Negative discount should return the original price.");
    }
}