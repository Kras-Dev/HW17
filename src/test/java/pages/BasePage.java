package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import patterns.WebDriverFactory;
import org.slf4j.Logger;

import java.time.Duration;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import static seleniumHelper.DriverUtils.waitForPageToLoad;

public class BasePage {
    static final Logger log = getLogger(lookup().lookupClass());

    WebDriver driver;
    WebDriverWait wait;
    int timeoutSec = 5;
    // Конструктор для инициализации WebDriver при передаче типа браузера
    public BasePage(String browser) {
        driver = WebDriverFactory.createWebDriver(browser);
        wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
        // Интервал опроса каждые мс
        wait.pollingEvery(Duration.ofMillis(200));
    }
    // Конструктор для передачи уже инициализированного WebDriver
    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
        // Интервал опроса каждые мс
        wait.pollingEvery(Duration.ofMillis(200));
    }
    // Метод для настройки окна браузера
    public void setupWindow() {
        driver.manage().window().fullscreen();
    }

    public void setTimeoutSec(int timeoutSec) {
        this.timeoutSec = timeoutSec;
        // Обновляем wait при изменении timeoutSec
        wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
        // Устанавливаем интервал опроса
        wait.pollingEvery(Duration.ofMillis(200));
    }
    // Метод для закрытия браузера
    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }
    // Открытие URL в браузере
    public void openUrl(String url) {
        driver.get(url);
        waitForPageToLoad(driver, url);
    }
    // Получение текущего URL
    public String getUrl(){
        return driver.getCurrentUrl();
    }
    // Поиск элемента на странице по локатору
    public WebElement find(By element) {
        return driver.findElement(element);
    }
    // Нажатие на элемент WebElement
    public void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }
    // Нажатие на элемент по локатору
    public void click(By element) {
        click(find(element));
    }
    // Заполнение текстового поля вводом текста
    public void enterText(WebElement element, String text) {
        element.sendKeys(text);
    }
    // Заполнение текстового поля по локатору вводом текста
    public void enterText(By element, String text) {
        enterText(find(element), text);
    }
    // Проверка отображения элемента WebElement
    public boolean isDisplayed(WebElement element) {
        return isDisplayed(ExpectedConditions.visibilityOf(element));
    }
    // Проверка отображения элемента по локатору
    public boolean isDisplayed(By locator) {
        return isDisplayed(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    // Полиморфный метод для проверки отображения элемента с указанием времени ожидания
    public boolean isDisplayed(ExpectedCondition<?> expectedCondition) {
        try {
            wait.until(expectedCondition);
        } catch (TimeoutException e) {
            log.warn("Тайм-аут {} ожидания элемента ", timeoutSec);
            return false;
        }
        return true;
    }

    // Метод изменения масштаба содержимого страницы на указанный процент
    public void zoomByPercent(int percent) {
        if (percent >= 10 && percent <= 100) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("document.body.style.zoom = '" + percent + "%';");
        } else {
            throw new IllegalArgumentException("Процент масштабирования должен быть от 10 до 100.");
        }
    }
    // Метод перемещения курсора мыши к элементу на странице
    public void moveToElement(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).build().perform();
    }
    // Метод навигации назад на предыдущую страницу
    public void navigateBack() {
        driver.navigate().back();
    }
}
