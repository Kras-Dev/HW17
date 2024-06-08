package allureSteps;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import patterns.WebDriverFactory;

public class BaseStepsEach {
    private static WebDriver driver;

    @BeforeEach
    public void setup() {
        String browser = "edge";
        driver = WebDriverFactory.createWebDriver(browser);
        driver.manage().window().maximize();
    }

    protected static WebDriver getDriver() {
        return driver;
    }

    @AfterEach
    public void teardown() {
        driver.quit();
    }
}
