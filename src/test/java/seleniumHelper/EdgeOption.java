package seleniumHelper;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;

import static constants.Constants.*;

public class EdgeOption implements BrowserOptions {
    private EdgeOptions edgeOptions;

    public EdgeOption() {
        edgeOptions = new EdgeOptions();
    }
    @Override
    public void setOptions() {
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        edgeOptions.setAcceptInsecureCerts(ACCEPT_INSURE_CERTS);
        edgeOptions.setPageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        edgeOptions.setScriptTimeout(Duration.ofSeconds(SCRIPT_TIMEOUT));
        edgeOptions.setImplicitWaitTimeout(Duration.ofSeconds(IMPLICIT_WAIT_TIMEOUT));
    }
    // Метод для получения настроенного объекта EdgeOptions
    public EdgeOptions getEdgeOptions() {
        setOptions();
        return edgeOptions;
    }
}
