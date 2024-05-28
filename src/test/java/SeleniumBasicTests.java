import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("Selenium")
public class SeleniumBasicTests {
    private final static String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/index.html";
    WebDriver driver;

    @BeforeEach
    void init(){
        driver = new EdgeDriver();
        driver.get(BASE_URL);
    }

//    @AfterAll
//    void close(){
//        driver.close();
//    }

    @Test
    void testGetTitle(){
        Assertions.assertEquals("Hands-On Selenium WebDriver with Java", driver.getTitle());
    }

    @Test
    void testOpenWebForm(){
        String xpath = "/html/body/main/div/div[4]/div[1]/div/div/a[1]";
        WebElement webFormButton = driver.findElement(By.xpath(xpath));
        webFormButton.click();
        WebElement inputTextField = driver.findElement(By.id("my-text-id"));
        inputTextField.sendKeys("Example text");
        // Покидаем элемент inputTextField
        inputTextField.sendKeys(Keys.ENTER);
        //Используем WebDriverWait для динамического ожидания появления элемента receivedText
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement receivedText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/main/div/div[4]/div/p")));
        Assertions.assertEquals("Received!", receivedText.getText());
    }

}
