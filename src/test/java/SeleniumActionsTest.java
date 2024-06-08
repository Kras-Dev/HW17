import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.net.URL;

import static seleniumHelper.DriverUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("Selenium_Actions")
public class SeleniumActionsTest {
    private final static String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
    WebDriver driver;
    String url;

    @BeforeEach
    void init(){
        driver = new ChromeDriver();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
    }

    @AfterEach
    void close(){
        driver.close();
    }

    @Test
    @DisplayName("checking text and navigation")
    void navigationTest(){
        url = "https://bonigarcia.dev/selenium-webdriver-java/navigation1.html";
        setupDriver(driver, url);
        WebElement button = driver.findElement(By.cssSelector("li.page-item a.page-link[href='navigation2.html']"));
        preLoad(driver, button);
        button.click();
        Assertions.assertEquals("https://bonigarcia.dev/selenium-webdriver-java/navigation2.html", driver.getCurrentUrl(),
                "Значение URL не соответствует ожидаемому");
        WebElement text = driver.findElement(By.cssSelector("p.lead"));
        assertThat(text.getText())
                .as("Значение текста не содержит ожидаемую подстроку")
                .contains("Duis aute irure dolor");
    }

    @Test
    @DisplayName("dropdown check")
    void dropDownMenuTest(){
        url = "https://bonigarcia.dev/selenium-webdriver-java/dropdown-menu.html";
        setupDriver(driver, url);
        WebElement dropDown1 = driver.findElement(By.id("my-dropdown-1"));
        preLoad(driver, dropDown1);
        new Actions(driver)
                .click(dropDown1).perform();
        WebElement leftClickMenu = driver.findElement(By.cssSelector("#my-dropdown-1 + .dropdown-menu"));
        Assertions.assertTrue(leftClickMenu.isDisplayed(), "left-click кнопка должна быть нажата");

        WebElement dropDown2 = driver.findElement(By.id("my-dropdown-2"));
        new Actions(driver)
                .contextClick(dropDown2).perform();
        WebElement rightClickMenu = driver.findElement(By.cssSelector("[id='context-menu-2']"));
        Assertions.assertTrue(rightClickMenu.isDisplayed(), "right-click кнопка должна быть нажата");

        WebElement dropDown3 = driver.findElement(By.id("my-dropdown-3"));
        new Actions(driver)
                .doubleClick(dropDown3).perform();
        WebElement doubleClickMenu = driver.findElement(By.cssSelector("[id='context-menu-3']"));
        Assertions.assertTrue(doubleClickMenu.isDisplayed(), "double-click кнопка должна быть нажата");
    }

    @Test
    @DisplayName("drag & drop check")
    void dragAndDropTest() {
        url = "https://bonigarcia.dev/selenium-webdriver-java/drag-and-drop.html";
        setupDriver(driver, url);
        WebElement draggableElement = driver.findElement(By.id("draggable"));
        WebElement droppableElement = driver.findElement(By.id("target"));
        preLoad(driver, draggableElement);
        new Actions(driver)
                .dragAndDrop(draggableElement, droppableElement).perform();
        // Получаем координаты элемента "droppable"
        Point droppableLocation = droppableElement.getLocation();
        // Получаем координаты элемента "draggable" после перетаскивания
        Point draggableLocation = draggableElement.getLocation();

        Assertions.assertEquals(droppableLocation.getX(), draggableLocation.getX(), "Координаты X не совпадают");
        Assertions.assertEquals(droppableLocation.getY(), draggableLocation.getY(), "Координаты Y не совпадают");
    }

    @Test
    @DisplayName("input in a disabled field")
    void disabledInputTest(){
        setupDriver(driver,BASE_URL);
        WebElement disabledInput = driver.findElement(By.name("my-disabled"));
        preLoad(driver, disabledInput);
        Assertions.assertFalse(disabledInput.isEnabled());
    }

    @Test
    @DisplayName("file upload")
    void fileUploadTest() throws InterruptedException {
        //получаем URL ресурса "test.txt", используя загрузчик классов текущего класса
        URL url = SeleniumActionsTest.class.getClassLoader().getResource("test.txt");
        String absolutePath = null;
        if (url != null) {
            // Получаем абсолютный путь к файлу
            absolutePath = new File(url.getPath()).getAbsolutePath();
        } else {
            System.out.println("Файл не найден");
        }
        setupDriver(driver, BASE_URL);
        WebElement fileInput = driver.findElement(By.name("my-file"));
        preLoad(driver, fileInput);
        fileInput.sendKeys(absolutePath);
        Thread.sleep(5000);
        WebElement submitButton = driver.findElement(By.xpath("//button[text()='Submit']"));
        // Ожидаем, пока кнопка "Submit" не станет видимой и кликабельной
        preLoad(driver, submitButton);
        String uploadedFilePath = fileInput.getAttribute("value");
        // Проверяем, что путь к загруженному файлу содержит "test.txt"
        assertThat(uploadedFilePath)
                .as("Значение не содержит ожидаемую подстроку")
                .contains("test.txt");
        // Используем Actions для выполнения действия "click" на кнопке "Submit"
        new Actions(driver)
                .moveToElement(submitButton).click().perform();
        Thread.sleep(5000);
        // Проверяем, что текущий URL страницы содержит "test.txt"
        assertThat(driver.getCurrentUrl())
                .as("Значение не содержит ожидаемую подстроку")
                .contains("test.txt");
    }

    @Test
    @DisplayName("Date picker check")
    void datePickerTest(){
        setupDriver(driver, BASE_URL);
        WebElement datePicker = driver.findElement(By.name("my-date"));
        preLoad(driver, datePicker);
        datePicker.sendKeys("2024-04-10");
        Assertions.assertEquals("2024-04-10", datePicker.getAttribute("value"),
                "Значение datePicker не соответствует ожидаемому");
    }
}
