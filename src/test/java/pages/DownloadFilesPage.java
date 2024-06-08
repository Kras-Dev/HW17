package pages;

import allureSteps.AllureSteps;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadFilesPage extends BasePage{
    private final String DOWNLOAD_LOCATION = "./";
    private AllureSteps allureSteps = new AllureSteps();
    @FindBy(linkText = "WebDriverManager logo")
    @CacheLookup
    WebElement webDriverManagerlogo;

    @FindBy(linkText = "WebDriverManager doc")
    @CacheLookup
    WebElement webDriverManagerdoc;

    @FindBy(linkText = "Selenium-Jupiter logo")
    @CacheLookup
    WebElement seleniumJupiterlogo;

    @FindBy(linkText = "Selenium-Jupiter doc")
    @CacheLookup
    WebElement seleniumJupiterdoc;

    public DownloadFilesPage(String browser) {
        super(browser);
        PageFactory.initElements(driver, this);
    }

    public DownloadFilesPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public WebElement getWebDriverManagerLogo() {
        return webDriverManagerlogo;
    }

    public WebElement getWebDriverManagerDoc() {
        return webDriverManagerdoc;
    }

    public WebElement getSeleniumJupiterLogo() {
        return seleniumJupiterlogo;
    }

    public WebElement getSeleniumJupiterDoc() {
        return seleniumJupiterdoc;
    }

    public boolean downloadFile(String fileName, String fileType, WebElement webDriver) throws IOException {
        File file = new File(Paths.get(DOWNLOAD_LOCATION, fileName + fileType).toString());

        if (file.exists()) {
            Files.delete(Paths.get(file.getPath()));
        }

        allureSteps.download(webDriver.getAttribute("href"), file);
        return file.exists();
    }
}
