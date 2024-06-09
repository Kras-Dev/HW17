import allureSteps.AllureSteps;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebElementCondition;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;

@Tag("Selenide")
@Story("Selenide Tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelenideTest {
    AllureSteps allureSteps = new AllureSteps();

    @BeforeAll
    void setup(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        options.addArguments("--no-sandbox"); // Bypass OS security model
        options.addArguments("--headless"); // without browser interface
        Configuration.browserCapabilities = options;
    }

    @BeforeEach
    void openUrl(){
        open("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
    }

    @AfterEach
    void close(){
        closeWebDriver();
    }

    @Test
    @DisplayName("Login form Test")
    void loginFormTest(){
        open("https://bonigarcia.dev/selenium-webdriver-java/login-form.html");
        $("#username").setValue("user");
        $("#password").setValue("user");
        $("button[type='submit']").click();
        $("#success").shouldHave(text("Login successful"));
    }

    @Test
    @DisplayName("Text input test")
    void textInputTest(){
        open("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        $("#my-text-id").setValue("test").shouldHave(value("test"));
    }

    @Test
    @DisplayName("Password input test")
    void inputPasswordTest() {
        $(By.name("my-password")).setValue("test").shouldHave(value("test"));
    }

    @Test
    @DisplayName("Text area input test")
    void textareaTest() {
        $("textarea[name='my-textarea']").setValue("Test").shouldHave(value("Test"));
    }
    @Test
    @DisplayName("Disabled input test")
    void inputDisabledTest() {
        $(By.name("my-disabled")).shouldBe(disabled);
    }

    @Test
    @DisplayName("Read only Input test")
    void readonlyInputTest(){
        $(By.name("my-readonly")).shouldBe(readonly);
    }
    @Test
    @DisplayName("Return to Index test")
    void linkReturnIndexTest() {
        $(By.linkText("Return to index")).click();
        $("body").shouldHave((WebElementCondition) url("https://bonigarcia.dev/selenium-webdriver-java/index.html"));
    }

    @Test
    @DisplayName("Dropdown list test")
    public void dropdownListTest() {
        $(By.name("my-select")).shouldBe(visible).selectOption("Two");
        $(By.name("my-select")).shouldHave(value("Two"));
    }

    @Test
    @DisplayName("Checked Checkbox test")
    public void checkedCheckboxTest() {
        $("#my-check-1").shouldBe(selected);
    }

    @Test
    @DisplayName("Check screenshot attachment")
    void infiniteScrollTestWithAttach() throws InterruptedException, IOException {
        Selenide.open("https://bonigarcia.dev/selenium-webdriver-java/infinite-scroll.html");
        WebDriver driver = Selenide.webdriver().object();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        By pLocator = By.tagName("p");
        List<WebElement> paragraphs = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(pLocator, 0));
        int initParagraphsNumber = paragraphs.size();

        WebElement lastParagraph = driver.findElement(By.xpath(String.format("//p[%d]", initParagraphsNumber)));
        String script = "arguments[0].scrollIntoView();";
        js.executeScript(script, lastParagraph);

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(pLocator, initParagraphsNumber));
        Thread.sleep(3000);
        allureSteps.captureScreenshotSelenide();
        allureSteps.captureScreenshotSelenideSpoiler();
    }


}
