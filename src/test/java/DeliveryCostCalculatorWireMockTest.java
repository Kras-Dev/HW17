import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;
import ru.example.DeliveryCostCalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("WireMock_тесты")
public class DeliveryCostCalculatorWireMockTest {
    private WireMockServer wireMockServer;

    @BeforeAll
    void setUp(){
        // Конфигурируем WireMock на случайном порту
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        // Запускаем WireMock
        wireMockServer.start();
        // Настраиваем клиент WireMock на использование случайного порта
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    void tearDown(){
        // Останавливаем WireMock после завершения всех тестов
        wireMockServer.stop();
    }

    @BeforeEach
    void beforeEachTest(){
        // Сброс настроек и истории запросов между тестами
        WireMock.reset();
    }

    @Test
    @DisplayName("Расчёт стоимости доставки хрупкого груза")
    void testCalculateDeliveryCostWithFragileItem(){
        // Настройка мок-сервера для эмуляции ответа при запросе "/fragile"
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/fragile"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withBody("Хрупкие грузы нельзя возить на расстояние более 30 км")));
        // Создаем экземпляр класса DeliveryCostCalculator
        DeliveryCostCalculator calculator = new DeliveryCostCalculator();
        // Вызываем метод calculateDeliveryCost с заданными параметрами
        String result = calculator.calculateDeliveryCost(40, "small", true, "high");
        // Проверяем, что метод вернул ожидаемый результат
        assertEquals("Хрупкие грузы нельзя возить на расстояние более 30 км", result);
    }

    @Test
    @DisplayName("Расчёт стоимости доставки крупного груза")
    void testCalculateDeliveryCostWithLargeItem(){
        // Устанавливаем заглушку для запроса на "/large" и возвращаем сообщение
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/large"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withBody("Большие грузы доставляются только на завтрашний день")));
        // Создаем экземпляр калькулятора стоимости доставки
        DeliveryCostCalculator calculator = new DeliveryCostCalculator();
        // Вычисляем стоимость доставки для большого товара
        String resultString = calculator.calculateDeliveryCost(5, "large", false, "standard");
        // Преобразуем результат из String в Double
        Double result = Double.valueOf(resultString);
        // Проверяем, что метод вернул ожидаемый результат - стоимость доставки 400.0
        assertEquals(Double.valueOf(400.0), result);
    }
}
