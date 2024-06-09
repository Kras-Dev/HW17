package allureSteps;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import patterns.WebDriverFactory;

public class BaseStepsAll {
    private static WebDriver driver;

    @BeforeAll
    public void setup() {
        String browser = "chrome";
        driver = WebDriverFactory.createWebDriver(browser);
        driver.manage().window().maximize();
    }

    protected static WebDriver getDriver() {
        return driver;
    }

    @AfterAll
    public void teardown() {
        driver.quit();
    }
}
