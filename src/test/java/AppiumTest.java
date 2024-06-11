import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Story;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Interaction;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("Appium")
@Story("Appium Tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppiumTest {
    private static final String APP = "https://github.com/appium-pro/TheApp/releases/download/v1.12.0/TheApp.apk";
    private static final String SERVER = "http://172.30.160.1:4723/";
    private AndroidDriver driver;

    @BeforeEach
    void setup() throws MalformedURLException {
        DesiredCapabilities ds = new DesiredCapabilities();
        // Устанавливаем необходимые параметры для запуска на Android устройстве
        ds.setCapability("platformName", "Android");
        ds.setCapability("platformVersion", "14");
        ds.setCapability("deviceName", "emulator-5554");
        ds.setCapability("app", APP);
        ds.setCapability("automationName", "UiAutomator2");

        driver = new AndroidDriver(new URL(SERVER), ds);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Simple Test with Implicit wait")
    void simpleWithImplicitWaitTest() {
        // Устанавливаем неявное ожидание в 5 секунд
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        // Находим элемент "Login Screen" и проверяем, что он отображается
        WebElement login = driver.findElement(AppiumBy.accessibilityId("Login Screen"));
        assertThat(login.isDisplayed()).isTrue();
    }

    @Test
    @DisplayName("Simple Test with Explicit wait")
    void simpleWithExplicitWaitTest() {
        // Используем явное ожидание для ожидания отображения элемента "Login Screen" в течение 5 секунд
        assertThat(new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Login Screen")))
                .isDisplayed()).isTrue();
    }

    @Test
    @DisplayName("Simple Test with Fluent wait")
    void simpleWithFluentWaitTest() {
        // Используем "Fluent wait" для поиска элемента "Login Screen" с таймаутом 5 секунд, проверяя каждую секунду и игнорируя NoSuchElementException
        FluentWait<AndroidDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(5))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
        // Ожидаем до появления элемента "Login Screen"
        WebElement login = wait.until((Function<WebDriver, WebElement>) driver -> driver.findElement(AppiumBy.accessibilityId("Login Screen")));
        assertThat(login.isDisplayed()).isTrue();
    }

    @Test
    @DisplayName("Simple Actions Test")
    void simpleActionsTest() {
        // Ожидаем отображения элемента "Login Screen" в течение 5 секунд
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement login = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Login Screen")));
        // Используем SoftAssertions для проверки нескольких условий без остановки при первом провале
        SoftAssertions softly = new SoftAssertions();
        // Проверяем текст элемента
        softly.assertThat(login.getText()).isEqualTo("");
        // Проверяем видимость элемента
        softly.assertThat(login.isDisplayed()).isTrue();
        // Проверяем, что элемент не выбран
        softly.assertThat(login.isSelected()).isFalse();
        // Проверяем, что элемент доступен для взаимодействия
        softly.assertThat(login.isEnabled()).isTrue();
        softly.assertAll();
    }

    @Test
    @DisplayName("Login Screen Test")
    void loginTest() throws InterruptedException {
        String user = "alice";
        String password = "mypassword";
        // Ожидание элементов на экране
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // Находим и нажимаем на элемент "Login Screen"
        WebElement login = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Login Screen")));
        login.click();
        // Находим элементы для ввода логина и пароля, вводим значения и нажимаем кнопку
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("username")));
        username.sendKeys(user);
        driver.findElement(AppiumBy.accessibilityId("password")).sendKeys(password);
        driver.findElement(AppiumBy.accessibilityId("loginBtn")).click();
        // Проверяем сообщение об успешном входе
        String source = driver.getPageSource();
        WebElement loginMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy
                .xpath("//android.widget.TextView[contains(@text, 'You are logged in')]")));
        assertThat(loginMessage.getText()).contains(user);
    }

    @Test
    @DisplayName("Login Screen Invalid Test")
    void invalidUsernameTest() {
        String user = "invalid_username";
        String password = "mypassword";
        // Ожидание элементов на экране
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // Находим и нажимаем на элемент
        WebElement login = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Login Screen")));
        login.click();
        // Находим элементы для ввода логина и пароля, вводим значения и нажимаем кнопку
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("username")));
        username.sendKeys(user);
        driver.findElement(AppiumBy.accessibilityId("password")).sendKeys(password);
        driver.findElement(AppiumBy.accessibilityId("loginBtn")).click();
        // Проверяем сообщение об ошибке при вводе неверного логина
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath("//android" +
                ".widget.TextView[contains(@text, 'Invalid login credentials, please try again')]")));
        assertThat(errorMessage.getText()).contains("Invalid username or password");
    }

    @Test
    @DisplayName("Swipe Test")
    void swipeTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement listDemoItem = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("List Demo")));
        listDemoItem.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("AWS")));
        // Получаем размер экрана устройства
        Dimension size = driver.manage().window().getSize();
        int screenWidth = size.getWidth();
        int screenHeight = size.getHeight();
        // Вычисляем координаты для свайпа
        int startX = screenWidth / 2;
        int startY = (int) (screenHeight * 0.8);
        int endY = (int) (screenHeight * 0.2);
        // Создаем последовательность действий для свайпа
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Interaction moveToStart = finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY);
        Interaction pressDown = finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg());
        Interaction moveToEnd = finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), startX, endY);
        Interaction pressUp = finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg());

        Sequence swipe = new Sequence(finger, 0);
        swipe.addAction(moveToStart);
        swipe.addAction(pressDown);
        swipe.addAction(moveToEnd);
        swipe.addAction(pressUp);
        // Выполняем свайп дважды и проверяем отображение элемента
        driver.perform(List.of(swipe));
        driver.perform(List.of(swipe));

        WebElement lastElement = driver.findElement(AppiumBy.accessibilityId("Stratus"));
        assertThat(lastElement.isDisplayed()).isTrue();
    }

    @Test
    @DisplayName("Echo Box Test")
    void echoBoxTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        // Ожидание элементов на экране
        WebElement echoBox = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Echo Box")));
        echoBox.click();
        // Находим поле ввода сообщения, вводим текст
        WebElement msgInput = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy
                .xpath("//android.widget.EditText[@content-desc=\"messageInput\"]")));
        msgInput.sendKeys("12345");
        // Находим кнопку
        WebElement submit =  wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy
                .xpath("//android.widget.TextView[@text=\"Save\"]\n")));
        submit.click();
        // Проверяем результат сохранения сообщения
        WebElement result =  wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy
                .xpath("//android.widget.TextView[@resource-id=\"savedMessage\"]\n")));
        assertThat(result.getText().equals("12345")).isTrue();
    }
}
