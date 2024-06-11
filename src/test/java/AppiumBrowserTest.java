import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("Appium")
@Story("Appium Browser Tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppiumBrowserTest {
    //Запуск: appium --allow-insecure chromedriver_autodownload
    private static final String SERVER = "http://127.0.0.1:4723/";
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";
    private AndroidDriver driver;

    @BeforeEach
    void setup() throws MalformedURLException {
        // Устанавливаем опции для UiAutomator2
        UiAutomator2Options options = new UiAutomator2Options();
        options
                .setPlatformName("Android")
                .setPlatformVersion("14")
                .setAutomationName("UiAutomator2")
                .setDeviceName("emulator-5554")
                .noReset()
                .withBrowserName("Chrome");

        driver = new AndroidDriver(new URL(SERVER), options);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Simple Test")
    void webFormTest() {
        String item = "Web form";
        // Ожидание элементов на странице
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        // Переходим на базовый URL и кликаем на элемент с текстом "Web form"
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(item))).click();
        // Находим заголовок страницы и проверяем его соответствие элементу
        WebElement title =  wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy
                .xpath("//h1[@class = 'display-6']")));
        assertThat(title.getText()).isEqualTo(item);
    }

    @Test
    @DisplayName("Login Form Test")
    void loginFormTest(){
        // Переходим на страницу с примером формы входа
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/slow-calculator.html");
        // Находим поле для ввода логина, вводим "user"
        WebElement usernameInput = new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.name("username")));
        usernameInput.sendKeys("user");
        // Находим поле для ввода пароля, вводим "user"
        WebElement passwordInput = driver.findElement(AppiumBy.name("password"));
        passwordInput.sendKeys("user");
        // Находим и нажимаем кнопку "Submit"
        WebElement submitButton = driver.findElement(AppiumBy.xpath("//button[@type='submit']"));
        submitButton.click();
        // Проверяем заголовок страницы после входа пользователя
        assertThat( driver.findElement(AppiumBy.className("display-6")).getText())
                .isEqualTo("Login form");
        // Ожидаем появления сообщения об успешной аутентификации
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id("success")));

        WebElement successMessage = driver.findElement(AppiumBy.id("success"));
        assertThat(successMessage.isDisplayed()).isTrue();
    }
}
