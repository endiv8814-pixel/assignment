package TCP;
import static org.junit.Assert.assertEquals;
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
<<<<<<< HEAD
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
=======
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
>>>>>>> 133d3624fe25527a7ba60abd8ec5660718e7df29
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