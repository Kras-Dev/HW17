package SeleniumHelper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DriverUtils {
    //Метод для настройки драйвера и открытия указанной страницы
    public static void setupDriver(WebDriver driver, String url) {
        driver.get(url);
        driver.manage().window().fullscreen();
        waitForPageToLoad(driver, url);
    }
    //Метод для ожидания появления элемента на странице
    public static void preLoad(WebDriver driver, WebElement element) {
        //Используем WebDriverWait для динамического ожидания появления элемента
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(element));
        //если элемент доступен для взаимодействия
        if (element.isEnabled()) {
            wait.until(ExpectedConditions.elementToBeClickable(element));
        }
    }
    ///Метод для ожидание загрузки страницы
    public static void waitForPageToLoad(WebDriver driver, String url) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(url));
    }
}
