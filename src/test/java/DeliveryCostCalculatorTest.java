import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.example.DeliveryCostCalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("Доставка")
public class DeliveryCostCalculatorTest {
    private DeliveryCostCalculator calculator;

    @BeforeAll
    public void setUp(){
        calculator = new DeliveryCostCalculator();
    }

    @Test
    @DisplayName("Расчёт стоимости доставки")
    void testCalculateDelivery(){
        String result = calculator.calculateDeliveryCost(15, "small", false, "normal");
        assertEquals("400.0", result);
    }

    @Test
    @DisplayName("Расчёт стоимости доставки хрупкого груза на разрешённом расстоянии")
    void testDeliveryFragileWithDistance(){
        String result = calculator.calculateDeliveryCost(20, "small", true, "normal");
        assertEquals("600.0", result);
    }

    @Test
    @DisplayName("Расчёт стоимости доставки хрупкого груза за пределами разрешённого расстояния")
    void testDeliveryFragileBeyondDistance(){
        String result = calculator.calculateDeliveryCost(35, "small", true, "normal");
        assertEquals("Хрупкие грузы нельзя возить на расстояние более 30 км", result);
    }

    @ParameterizedTest
    @CsvSource({
            "5, small, false, normal, 400.0",
            "25, large, true, very_high, 1120.0",
            "1, small, true, elevated, 540.0",
            "60, large, false, high, 700.0"
    })
    @DisplayName("Расчёт стоимости доставки для различных сценариев")
    public void testCalculateDeliveryParameterized(double distance, String size, boolean fragile, String workload, double expectedCost) {
        String result = calculator.calculateDeliveryCost(distance, size, fragile, workload);
        assertEquals(expectedCost, Double.parseDouble(result));
    }
}