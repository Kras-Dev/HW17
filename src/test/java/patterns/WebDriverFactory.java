package patterns;

import seleniumHelper.ChromeOption;
import seleniumHelper.EdgeOption;
import seleniumHelper.FireFoxOption;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebDriverFactory {
    // Метод для создания и возвращения WebDriver в зависимости от выбранного браузера
    public static WebDriver createWebDriver(String browser) {
        WebDriver driver = switch (browser.toLowerCase()) {
            case "chrome" -> createChromeDriver();
            case "edge" -> createEdgeDriver();
            case "firefox" -> createFirefoxDriver();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };

        driver.manage().window().maximize();
        return driver;
    }
    // Метод для создания и возвращения ChromeDriver с настроенными опциями ChromeOptions
    private static WebDriver createChromeDriver() {
        ChromeOptions chromeOptions = new ChromeOption().getChromeOptions();
        return new ChromeDriver(chromeOptions);
    }
    // Метод для создания и возвращения EdgeDriver с настроенными опциями EdgeOptions
    private static WebDriver createEdgeDriver() {
        EdgeOptions edgeOptions = new EdgeOption().getEdgeOptions();
        return new EdgeDriver(edgeOptions);
    }
    // Метод для создания и возвращения FirefoxDriver с настроенными опциями FirefoxOptions
    private static WebDriver createFirefoxDriver() {
        FirefoxOptions firefoxOptions = new FireFoxOption().getFireFoxOptions();
        return new FirefoxDriver(firefoxOptions);
    }

}

//WebDriver driver = WebDriverFactory.createWebDriver("chrome");