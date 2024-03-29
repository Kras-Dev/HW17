import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.example.DeliveryCostCalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


public class DeliveryCostCalculatorMockitoTest {

    @Test
    void testSizeIsLarge(){
        DeliveryCostCalculator mockCalculator = Mockito.mock(DeliveryCostCalculator.class);
        // Переопределяем поведение метода для параметра size равным "large"
        when(mockCalculator.calculateDeliveryCost(anyDouble(), eq("large"), anyBoolean(), anyString()))
                .thenCallRealMethod();
        String result = mockCalculator.calculateDeliveryCost(15, "large", false, "normal");
        assertEquals("400.0", result);
    }

    @Test
    void testWorkloadIsHigh() {
        DeliveryCostCalculator calculator = new DeliveryCostCalculator();
        DeliveryCostCalculator spyCalculator = Mockito.spy(calculator);
        // Переопределяем поведение метода для параметра workload равным "high"
        when(spyCalculator.calculateDeliveryCost(anyDouble(), anyString(), anyBoolean(), eq("high")))
                .thenCallRealMethod();

        String result = spyCalculator.calculateDeliveryCost(15, "small", false, "high");

        assertEquals("420.0", result);
    }

    @Test
    void testMathMax(){
        DeliveryCostCalculator mockCalculator = Mockito.mock(DeliveryCostCalculator.class);
        // Переопределяем поведение метода для параметра size равным "large"
        when(mockCalculator.calculateDeliveryCost(anyDouble(), anyString(), anyBoolean(), anyString()))
                .thenCallRealMethod();
        String result1 = mockCalculator.calculateDeliveryCost(15, "small", false, "normal");
        String result2 = mockCalculator.calculateDeliveryCost(35, "small", true, "normal");

        assertEquals("400.0", result1);
        assertEquals("Хрупкие грузы нельзя возить на расстояние более 30 км", result2);
    }
}

