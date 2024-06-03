package seleniumHelper;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

import static constants.Constants.*;

public class FireFoxOption implements BrowserOptions {
    private FirefoxOptions fireFoxOptions;

    public FireFoxOption(){
        fireFoxOptions = new FirefoxOptions();
    }
    @Override
    public void setOptions() {
        fireFoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        fireFoxOptions.setAcceptInsecureCerts(ACCEPT_INSURE_CERTS);
        fireFoxOptions.setPageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        fireFoxOptions.setScriptTimeout(Duration.ofSeconds(SCRIPT_TIMEOUT));
        fireFoxOptions.setImplicitWaitTimeout(Duration.ofSeconds(IMPLICIT_WAIT_TIMEOUT));
    }
    // Метод для получения настроенного объекта FireFoxOptions
    public FirefoxOptions getFireFoxOptions(){
        setOptions();
        return fireFoxOptions;
    }
}
