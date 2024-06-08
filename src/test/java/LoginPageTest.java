import allureSteps.BaseStepsEach;
import extensions.AllureExtensions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.LoginPage;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("Login_Page_Test")

@Epic("Testing with Allure")
@Link(name="", value = "", url = "https://bonigarcia.dev/selenium-webdriver-java/login-form.html")
@Feature("Login form page tests")
@ExtendWith(AllureExtensions.class)
public class LoginPageTest extends BaseStepsEach {
    private final static String URL = "https://bonigarcia.dev/selenium-webdriver-java/login-form.html";
    LoginPage loginPage;

    @BeforeEach
    void setUp() {
        loginPage = new LoginPage(getDriver());
        loginPage.openUrl(URL);
    }

    @AfterEach
    void tearDown() {
        loginPage.quit();
    }

    @Test
    @DisplayName("Valid Login test")
    @Step("Login Success")
    void validLoginTest(){
        loginPage.setLoginInput("user", "user");
        Assertions.assertThat(loginPage.successAlertPresent()).isTrue();
        Assertions.assertThat(loginPage.invalidCredentialsAlertPresent()).isFalse();
    }

    @Test
    @DisplayName("InValid Login test")
    @Step("Login Failure")
    void invalidLoginTest(){
        loginPage.setLoginInput("test", "test");
        Assertions.assertThat(loginPage.successAlertPresent()).isFalse();
        Assertions.assertThat(loginPage.invalidCredentialsAlertPresent()).isTrue();
    }

    @Test
    @DisplayName("flaky test")
    @Step("Invalid Login for crash")
    void flakyLoginTest(){
        loginPage.setLoginInput("test", "test");
        Assertions.assertThat(loginPage.successAlertPresent()).isTrue();
        Assertions.assertThat(loginPage.invalidCredentialsAlertPresent()).isFalse();
    }

    @Test
    @DisplayName("Header test")
    @Step("Header text")
    void headerTest(){
        Assertions.assertThat(loginPage.headerComponent().getTitleText()).isEqualTo("Hands-On Selenium WebDriver with Java");
        Assertions.assertThat(loginPage.headerComponent().getSubTitleText()).isEqualTo("Practice site");
        loginPage.headerComponent().clickLogo();
        Assertions.assertThat(loginPage.getUrl()).isEqualTo("https://github.com/bonigarcia/selenium-webdriver-java");
    }

    @Test
    @DisplayName("Footer test")
    @Step("Footer Text")
    void footerTest(){
        Assertions.assertThat(loginPage.footerComponent().footerText()).isEqualTo("Copyright © 2021-2024 Boni García");
        loginPage.footerComponent().clickLink();
        Assertions.assertThat(loginPage.getUrl()).isEqualTo("https://bonigarcia.dev/");
    }
}
