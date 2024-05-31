import allureSteps.BaseStepsEach;
import extensions.AllureExtensions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import pages.MainPage;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("Main_Page_Test")

@Epic("Testing with Allure")
@Link(name="", value = "", url = "https://bonigarcia.dev/selenium-webdriver-java/")
@Feature("Main page tests")
@ExtendWith(AllureExtensions.class)
public class MainPageTest extends BaseStepsEach {
    private final static String URL = "https://bonigarcia.dev/selenium-webdriver-java/";
    MainPage mainPage;

    @BeforeEach
    void setUp() {
        mainPage = new MainPage(getDriver());
        mainPage.openUrl(URL);
    }

    @AfterEach
    void tearDown() {
        mainPage.quit();
    }

    @Test
    @DisplayName("go to Login form test")
    @Step("Following to login form")
    void loginFormTest(){
        mainPage.clickLoginFormButton();
        Assertions.assertThat(mainPage.getUrl()).isEqualTo("https://bonigarcia.dev/selenium-webdriver-java/login-form.html");
    }
    @Test
    @DisplayName("go to Web form test")
    @Step("Following to web form")
    void webFormTest(){
        mainPage.clickWebFormButton();
        Assertions.assertThat(mainPage.getUrl()).isEqualTo("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
    }

    @Test
    @DisplayName("Header test")
    @Step("Header text")
    void headerTest(){
        Assertions.assertThat(mainPage.headerComponent().getTitleText()).isEqualTo("Hands-On Selenium WebDriver with Java");
        Assertions.assertThat(mainPage.headerComponent().getSubTitleText()).isEqualTo("Practice site");
        mainPage.headerComponent().clickLogo();
        Assertions.assertThat(mainPage.getUrl()).isEqualTo("https://github.com/bonigarcia/selenium-webdriver-java");
    }

    @Test
    @DisplayName("Footer test")
    @Step("Footer text")
    void footerTest(){
        Assertions.assertThat(mainPage.footerComponent().footerText()).isEqualTo("Copyright © 2021-2024 Boni García");
        mainPage.footerComponent().clickLink();
        Assertions.assertThat(mainPage.getUrl()).isEqualTo("https://bonigarcia.dev/");
    }
}
