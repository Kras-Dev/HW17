import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import static SeleniumHelper.DriverUtils.setupDriver;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("Selenium_Locators")
public class SeleniumLocatorsTest {
    private final static String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
    WebDriver driver;
    Actions actions;

    @BeforeEach
    void init(){
        driver = new EdgeDriver();
        setupDriver(driver, BASE_URL);
    }

    @AfterAll
    void close(){
        driver.close();
    }

    @Test
    void locatorsExample() throws InterruptedException {
        //By.id
        WebElement inputTextForm = driver.findElement(By.id("my-text-id"));
        inputTextForm.sendKeys("inputTextForm");
        Assertions.assertEquals("inputTextForm",inputTextForm.getAttribute("value"),
                "Значение в поле inputTextForm не соответствует ожидаемому");

        //By.cssSelector
        WebElement passwordForm = driver.findElement(By.cssSelector("[name='my-password']"));
        passwordForm.sendKeys("passwordForm");
        Assertions.assertEquals("passwordForm",passwordForm.getAttribute("value"),
                "Значение в поле passwordForm не соответствует ожидаемому");

        //By.name
        WebElement textArea = driver.findElement(By.name("my-textarea"));
        textArea.sendKeys("my-textarea");
        Assertions.assertEquals("my-textarea",textArea.getAttribute("value"),
                "Значение в поле textArea не соответствует ожидаемому");

        //By.className
        WebElement formSelect = driver.findElement(By.className("form-select"));
        // Выбираем элемент из выпадающего списка по видимому тексту
        Select select = new Select(formSelect);
        select.selectByVisibleText("Two");
        WebElement selectedOption = select.getFirstSelectedOption();
        String selectedText = selectedOption.getText();
        Assertions.assertEquals("Two",selectedText,
                "Значение formSelect не соответствует ожидаемому");
        Assertions.assertTrue(select.getFirstSelectedOption().isSelected());

        //By.xpath
        WebElement dataList = driver.findElement(By.xpath("//datalist[@id='my-options']//option[@value='New York']"));

        //By.cssSelector
        WebElement checkbox = driver.findElement(By.cssSelector("input[type='checkbox'][name='my-check']"));
        checkbox.click();
        Assertions.assertFalse(checkbox.isSelected(), "После нажатия флажок должен быть снят");

        //By.xpath
        WebElement radio = driver.findElement(By.xpath("//input[@type='radio'][@name='my-radio']"));
        actions = new Actions(driver);
        actions.moveToElement(radio).click().perform();
        Assertions.assertTrue(radio.isSelected(),"Радиокнопка должна быть выбрана после нажатия");

        //By.linkText
        WebElement linkText = driver.findElement(By.linkText("Return to index"));
        actions.moveToElement(linkText).click().perform();
        Assertions.assertEquals("https://bonigarcia.dev/selenium-webdriver-java/index.html", driver.getCurrentUrl(),
                "Значение URL не соответствует ожидаемому");
    }
}
