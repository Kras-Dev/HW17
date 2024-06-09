package seleniumHelper;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static constants.Constants.*;

public class ChromeOption implements BrowserOptions {
    private ChromeOptions chromeOptions;
    public ChromeOption() {
        chromeOptions = new ChromeOptions();
    }
    @Override
    public void setOptions() {
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        chromeOptions.setAcceptInsecureCerts(ACCEPT_INSURE_CERTS);
        chromeOptions.setPageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        chromeOptions.setScriptTimeout(Duration.ofSeconds(SCRIPT_TIMEOUT));
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(IMPLICIT_WAIT_TIMEOUT));
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--headless");
    }
    // Метод для получения настроенного объекта ChromeOptions
    public  ChromeOptions getChromeOptions(){
        setOptions();
        return chromeOptions;
    }
}

