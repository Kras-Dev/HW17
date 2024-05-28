import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("Browser_Agnostic_Features")
public class SeleniumBrowserAgnosticFeaturesTest {
    private final static String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";
    WebDriver driver;
    String mainTab;

    @BeforeAll
    void start(){
        EdgeOptions edgeOptions = new EdgeOptions();
        // Устанавливаем стратегию загрузки страницы
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        // Принимаем незащищенные сертификаты
        edgeOptions.setAcceptInsecureCerts(true);
        // Устанавливаем время ожидания загрузки страницы
        edgeOptions.setPageLoadTimeout(Duration.ofSeconds(60));
        // Устанавливаем время ожидания выполнения JavaScript
        edgeOptions.setScriptTimeout(Duration.ofSeconds(30));
        // Устанавливаем неявное время ожидания элементов на странице
        edgeOptions.setImplicitWaitTimeout(Duration.ofSeconds(20));
        // Создаем драйвер с опциями
        driver = new EdgeDriver(edgeOptions);
        driver.get(BASE_URL);
    }

    @BeforeEach
    void init(){
        // Сохраняем идентификатор основной вкладки
        mainTab = driver.getWindowHandle();
        driver.switchTo().newWindow(WindowType.TAB);
    }

    @AfterEach
    void close(){
        // Сохраняем список всех открытых вкладок
        Set<String> tabs = driver.getWindowHandles();
        // Переключаем фокус на новую вкладку
        for (String tab : tabs) {
            if (!tab.equals(mainTab)) {
                driver.switchTo().window(tab);
                break;
            }
        }
        // Закрываем новую вкладку
        driver.close();
        // Переключаемся на основную вкладку
        driver.switchTo().window(mainTab);
    }

    @AfterAll
    void quit(){
        driver.quit();
    }

    @Test
    @DisplayName("Infinite scroll")
    void infinityScrollTest(){
        String url = "https://bonigarcia.dev/selenium-webdriver-java/infinite-scroll.html";
        driver.get(url);
        // Используем JavascriptExecutor для выполнения JavaScript на странице
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // Инициализируем ожидание элементов
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        // Определяем локатор абзацев на странице
        By pLocator = By.tagName("p");
        // Ждем, пока на странице будет больше чем 0 абзацев
        List<WebElement> paragraphs = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(pLocator, 0));
        // Запоминаем количество абзацев на момент начала теста
        int initParagraphsNumber = paragraphs.size();
        // Находим последний абзац на странице
        WebElement lastParagraph = driver.findElement(By.xpath(String.format("//p[%d]", initParagraphsNumber)));
        // Прокручиваем страницу до последнего абзаца
        String script = "arguments[0].scrollIntoView();";
        js.executeScript(script, lastParagraph);
        // Ждем, пока количество абзацев на странице станет больше, чем в начале теста
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(pLocator, initParagraphsNumber));
        // Проверяем, что количество абзацев после прокрутки больше, чем в начале теста
        List<WebElement> updatedParagraphs = driver.findElements(pLocator);
        Assertions.assertTrue(updatedParagraphs.size() > initParagraphsNumber);
    }

    @Test
    @DisplayName("Shadow DOM")
    void shadowDOMTest(){
        String url = "https://bonigarcia.dev/selenium-webdriver-java/shadow-dom.html";
        driver.get(url);
        // Проверяем, что элемент <p> отсутствует в Shadow DOM и генерируется NoSuchElementException
        Assertions.assertThrows(NoSuchElementException.class, () -> driver.findElement(By.cssSelector("p")));
        // Находим элемент с id "content" в DOM
        WebElement content = driver.findElement(By.id("content"));
        // Получаем ShadowRoot элемента "content" для доступа к элементам внутри Shadow DOM
        SearchContext shadowRoot = content.getShadowRoot();
        // Находим элемент <p> внутри Shadow DOM с помощью shadowRoot
        WebElement textElement = shadowRoot.findElement(By.cssSelector("p"));
        // Проверяем, что текст элемента <p> содержит строку "Hello Shadow DOM"
        assertThat(textElement.getText()).contains("Hello Shadow DOM");
    }

    @Test
    @DisplayName("Cookies")
    void cookiesTest(){
        String url = "https://bonigarcia.dev/selenium-webdriver-java/cookies.html";
//        WebDriver.Options options = driver.manage();
        driver.get(url);
        // Получаем все cookies на странице
        Set<Cookie> cookies = driver.manage().getCookies();
//        for (Cookie cookie : cookies){
//            System.out.println("Name: " + cookie.getName() + ", Value: " + cookie.getValue() + ",
//            Path: " + cookie.getPath());
//        }
        assertThat(cookies).hasSize(2);
        // Получаем cookies с именами "username" и "date"
        Cookie username = driver.manage().getCookieNamed("username");
        Cookie date = driver.manage().getCookieNamed("date");
        assertThat(username.getValue()).isEqualTo("John Doe");
        assertThat(date.getValue()).isEqualTo("10/07/2018");
        driver.findElement(By.id("refresh-cookies")).click();
        // Добавляем новый cookie и проверяем его значение
        Cookie newCookie = new Cookie("new-cookie-key", "new-cookie-value");
        driver.manage().addCookie(newCookie);
        String newCookieValue = driver.manage().getCookieNamed(newCookie.getName()).getValue();
        assertThat(newCookie.getValue()).isEqualTo(newCookieValue);
        cookies = driver.manage().getCookies();
        assertThat(cookies).hasSize(3);
        driver.findElement(By.id("refresh-cookies")).click();
        // Удаляем cookie "username" и проверяем, что количество cookies уменьшилось
        driver.manage().deleteCookie(username);
        assertThat(driver.manage().getCookies()).hasSize(cookies.size() - 1);
        driver.findElement(By.id("refresh-cookies")).click();
    }

    @Test
    @DisplayName("IFrames")
    void iFramesTest(){
        String url = "https://bonigarcia.dev/selenium-webdriver-java/iframes.html";
        driver.get(url);
        // Проверяем, что элемент с классом "lead" не найден в основном контексте
        Assertions.assertThrows(NoSuchElementException.class, () -> driver.findElement(By.className("lead")));
        // Переключаемся на iFrame
        WebElement iframeElement = driver.findElement(By.id("my-iframe"));
        driver.switchTo().frame(iframeElement);
        // Проверяем, что элемент с классом "display-6" не найден внутри iFrame
        Assertions.assertThrows(NoSuchElementException.class, () -> driver.findElement(By.className("display-6")));
        // Проверяем текст элемента с классом "lead" внутри iFrame содержит указанный текст
        assertThat(driver.findElement(By.className("lead")).getText().contains("Lorem ipsum dolor sit amet"));
        // Переключаемся обратно в основной контекст страницы
        driver.switchTo().defaultContent();
        // Проверяем что текст элемента с классом "display-6" в основном контексте содержит указанный текст
        assertThat(driver.findElement(By.className("display-6")).getText().contains("IFrame"));
    }

    @Test
    @DisplayName("Dialog boxes")
    void dialogBoxesTest(){
        String url = "https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html";
        driver.get(url);
        // Проверяем диалоговое окно Alert
        driver.findElement(By.id("my-alert")).click();
        Alert alert = driver.switchTo().alert();
        assertThat(alert.getText()).isEqualTo("Hello world!");
        alert.accept();
        // Проверяем диалоговое окно Confirm
        driver.findElement(By.id("my-confirm")).click();
        driver.switchTo().alert().accept();
        assertThat(driver.findElement(By.id("confirm-text")).getText()).isEqualTo("You chose: true");
        driver.findElement(By.id("my-confirm")).click();
        driver.switchTo().alert().dismiss();
        assertThat(driver.findElement(By.id("confirm-text")).getText()).isEqualTo("You chose: false");
        // Проверяем диалоговое окно Prompt
        driver.findElement(By.id("my-prompt")).click();
        driver.switchTo().alert().sendKeys("test");
        driver.switchTo().alert().accept();
        assertThat(driver.findElement(By.id("prompt-text")).getText()).isEqualTo("You typed: test");
        // Проверяем модальное окно
        driver.findElement(By.id("my-modal")).click();
        WebElement saveButton = driver.findElement(By.xpath("//button[normalize-space() = 'Save changes']"));
        saveButton.click();
        assertThat(driver.findElement(By.id("modal-text")).getText()).isEqualTo("You chose: Save changes");
    }

    @Test
    @DisplayName("Web storage")
    void webStorageTest(){
        String url = "https://bonigarcia.dev/selenium-webdriver-java/web-storage.html";
        driver.get(url);
        // Получаем локальное хранилище (LocalStorage)
        WebStorage webStorage = (WebStorage) driver;
        LocalStorage localStorage = webStorage.getLocalStorage();
        System.out.printf("local storage elements: {%s}\n", localStorage.size());
        // Выводим все элементы из SessionStorage
        SessionStorage sessionStorage = webStorage.getSessionStorage();
        sessionStorage.keySet()
                .forEach(key -> System.out.printf("Session storage: {%s}={%s}\n", key, sessionStorage.getItem(key)));
        // Проверяем размер SessionStorage
        assertThat(sessionStorage.size()).isEqualTo(2);
        // Добавляем новый элемент в SessionStorage и проверяем новый размер
        sessionStorage.setItem("new element", "new value");
        assertThat(sessionStorage.size()).isEqualTo(3);
        driver.findElement(By.id("display-session")).click();
    }

}
