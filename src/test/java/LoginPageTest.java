import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import pages.LoginPage;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("Login_Page_Test")
public class LoginPageTest {
    private final static String URL = "https://bonigarcia.dev/selenium-webdriver-java/login-form.html";
    LoginPage loginPage;

    @BeforeEach
    void setup() {
        loginPage = new LoginPage("edge");
        loginPage.openUrl(URL);
    }

    @AfterEach
    void teardown() {
        loginPage.quit();
    }

    @Test
    @DisplayName("Valid Login test")
    void validLoginTest(){
        loginPage.setLoginInput("user", "user");
        Assertions.assertThat(loginPage.successAlertPresent()).isTrue();
        Assertions.assertThat(loginPage.invalidCredentialsAlertPresent()).isFalse();
    }

    @Test
    @DisplayName("InValid Login test")
    void invalidLoginTest(){
        loginPage.setLoginInput("test", "test");
        Assertions.assertThat(loginPage.successAlertPresent()).isFalse();
        Assertions.assertThat(loginPage.invalidCredentialsAlertPresent()).isTrue();
    }

    @Test
    @DisplayName("Header test")
    void headerTest(){
        Assertions.assertThat(loginPage.headerComponent().getTitleText()).isEqualTo("Hands-On Selenium WebDriver with Java");
        Assertions.assertThat(loginPage.headerComponent().getSubTitleText()).isEqualTo("Practice site");
        loginPage.headerComponent().clickLogo();
        Assertions.assertThat(loginPage.getUrl()).isEqualTo("https://github.com/bonigarcia/selenium-webdriver-java");
    }

    @Test
    @DisplayName("Footer test")
    void footerTest(){
        Assertions.assertThat(loginPage.footerComponent().footerText()).isEqualTo("Copyright © 2021-2024 Boni García");
        loginPage.footerComponent().clickLink();
        Assertions.assertThat(loginPage.getUrl()).isEqualTo("https://bonigarcia.dev/");
    }
}
