import allureSteps.BaseStepsAll;
import extensions.AllureExtensions;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;
import pages.DownloadFilesPage;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Epic - Аннотация позволяет сгруппировать тесты в рамках общей цели или темы. Применяется к тестовому классу.
@Epic("Testing with Allure")
//@Feature - Аннотация позволяет описывать отдельные функциональные возможности в рамках общего эпика.
@Feature("Download files page tests")
//@Link - Аннотация позволяет добавлять ссылки в отчет.
@Link(name="", value = "", url = "https://bonigarcia.dev/selenium-webdriver-java/download.html")
@ExtendWith(AllureExtensions.class)
public class DownloadFilesPageTest extends BaseStepsAll {
    private final static String URL = "https://bonigarcia.dev/selenium-webdriver-java/download.html";

    DownloadFilesPage downloadFilesPage;

    @BeforeAll
    void setUp() {
        downloadFilesPage = new DownloadFilesPage(getDriver());
        downloadFilesPage.openUrl(URL);
    }

    @AfterAll
    void tearDown() {
        downloadFilesPage.quit();
    }

    //@Story - Аннотация позволяет описывать конкретные сценарии или истории использования.
    @Story("Loading files one by one")
    //@Step - Аннотация позволяет определить шаг в рамках теста. Применяется к методу, который содержит шаг теста.
    @Step("Download WebDriverManager logo")
    //@Attachment - Аннотация позволяет прикрепить к отчету файл или скриншот. Применяется к методу, который создает и возвращает содержимое для прикрепления.
    //@Description - Аннотация позволяет добавить описание к тесту или шагу. Применяется к тестовому методу или шагу.
    @Description("test checks file upload function")
    //@Severity - Аннотация позволяет указать уровень важности теста.
    @Severity(SeverityLevel.MINOR)
    //@Issue - Аннотация позволяет привязать тест к задаче в баг-трекере.
    @Issue("Homework-24")
    @Test
    @DisplayName("Download WebDriverManager logo")
    void downloadWebDriverManagerLogo() throws IOException {
        WebElement webDriverManagerLogo = downloadFilesPage.getWebDriverManagerLogo();
        if (webDriverManagerLogo != null) {
            Assertions.assertTrue(downloadFilesPage.downloadFile("WebDriverManagerlogo", ".png", webDriverManagerLogo));
        } else {
            Assertions.fail("WebElement for WebDriverManager logo not found.");
        }
    }
    @Test
    @DisplayName("Download WebDriverManager doc")
    @Step("Download WebDriverManager doc")
    void downloadWebDriverManagerDoc() throws IOException {
        WebElement webDriverManagerDoc = downloadFilesPage.getWebDriverManagerDoc();
        if (webDriverManagerDoc != null) {
            Assertions.assertTrue(downloadFilesPage.downloadFile("WebDriverManagerdoc", ".pdf", webDriverManagerDoc));
        } else {
            Assertions.fail("WebElement for WebDriverManager doc not found.");
        }
    }
    @Test
    @DisplayName("Download Selenium-Jupiter logo")
    @Step("Download Selenium-Jupiter logo")
    void downloadSeleniumJupiterLogo() throws IOException {
        WebElement seleniumJupiterLogo = downloadFilesPage.getSeleniumJupiterLogo();
        if (seleniumJupiterLogo != null) {
            Assertions.assertTrue(downloadFilesPage.downloadFile("seleniumJupiterLogo", ".png", seleniumJupiterLogo));
        } else {
            Assertions.fail("WebElement for Selenium-Jupiter logo not found.");
        }
    }

    @Test
    @DisplayName("Download Selenium-Jupiter doc")
    @Step("Download Selenium-Jupiter doc")
    void downloadSeleniumJupiterDoc() throws IOException {
        WebElement seleniumJupiterDoc = downloadFilesPage.getSeleniumJupiterDoc();
        if (seleniumJupiterDoc != null) {
            Assertions.assertTrue(downloadFilesPage.downloadFile("seleniumJupiterDoc", ".pdf", seleniumJupiterDoc));
        } else {
            Assertions.fail("WebElement for Selenium-Jupiter doc not found.");
        }
    }
}
