package TCP;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestDiscountCalculator {
    @Test
    public void testCalculateDiscount() {
        System.out.println("\n=== Running testCalculateDiscount ===\n");
        
        // Test 1: No discount
        System.out.println("Test 1: No discount (0%)");
        int result = (int) DiscountCalculator.calculateDiscount(100.0, 0);
        System.out.println("Input: price=100.0, discount=0%");
        System.out.println("Expected: 100");
        System.out.println("Got: " + result);
        assertEquals("No discount should return the original price.", 100, result);
        System.out.println("PASSED\n");

        // Test 2: 100% discount
        System.out.println("Test 2: Full discount (100%)");
        result = (int) DiscountCalculator.calculateDiscount(200.0, 100);
        System.out.println("Input: price=200.0, discount=100%");
        System.out.println("Expected: 0");
        System.out.println("Got: " + result);
        assertEquals("100% discount should return 0.", 0, result);
        System.out.println("PASSED\n");

        // Test 3: 30% discount
        System.out.println("Test 3: 30% discount");
        result = (int) DiscountCalculator.calculateDiscount(200.0, 30);
        System.out.println("Input: price=200.0, discount=30%");
        System.out.println("Expected: 140");
        System.out.println("Got: " + result);
        assertEquals("30% discount should return 140.", 140, result);
        System.out.println("PASSED\n");

        // Test 4: 1% discount
        System.out.println("Test 4: 1% discount");
        result = (int) DiscountCalculator.calculateDiscount(200.0, 1);
        System.out.println("Input: price=200.0, discount=1%");
        System.out.println("Expected: 198");
        System.out.println("Got: " + result);
        assertEquals("1% discount should return 198.", 198, result);
        System.out.println("PASSED\n");

        // Test 5: Negative discount
        System.out.println("Test 5: Negative discount (-10%)");
        result = (int) DiscountCalculator.calculateDiscount(200.0, -10);
        System.out.println("Input: price=200.0, discount=-10%");
        System.out.println("Expected: 200");
        System.out.println("Got: " + result);
        assertEquals("Negative discount should return the original price.", 200, result);
        System.out.println("PASSED\n");
        
        System.out.println("=== ALL TESTS PASSED ===\n");
    }
}