package TCP;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TestDiscountCalculator {
    @Test
    public void testCalculateDiscount() {
        System.out.println("\n=== Running testCalculateDiscount ===\n");
        
        // Test 1: No discount
        System.out.println("Test 1: No discount (0%)");
        int result = (int) DiscountCalculator.calculateDiscount(100.0, 0);
        assertEquals(100, result, "No discount should return the original price.");
    }
    @Test
    public void testCalculateDiscount2(){
        int result = (int) DiscountCalculator.calculateDiscount(200.0, 100);
        assertEquals(0, result, "100% discount should return 0.");
    }
    @Test
    public void testCalculateDiscount3(){
        int result = (int) DiscountCalculator.calculateDiscount(200.0, 30);
        assertEquals(140, result, "30% discount should return 140.");
    }
    @Test
    public void testCalculateDiscount4(){
        int result = (int) DiscountCalculator.calculateDiscount(200.0, 1);
        assertEquals(198, result, "1% discount should return 198.");
    }  
    @Test
    public void testCalculateDiscount5(){
        int result = (int) DiscountCalculator.calculateDiscount(200.0, -10);
        assertEquals(200, result, "Negative discount should return the original price.");
    }

    @ParameterizedTest
    @CsvSource({
    "100, 100, 0",
    "200, 140, 30",
    "200, 0, 100",
    "200, 198, 1",
    "200, 200, -10"
    })
    public void testCalculateDiscount2(double initial, int expected, int percent) {
    assertEquals(expected, (int) DiscountCalculator.calculateDiscount(initial, percent));
    }

}