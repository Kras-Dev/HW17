import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pages.MainPage;


public class MainPageTest {
    private final static String URL = "https://bonigarcia.dev/selenium-webdriver-java/";
    MainPage mainPage;

    @BeforeEach
    void setup() {
        mainPage = new MainPage("chrome");
        mainPage.openUrl(URL);
    }

    @AfterEach
    void teardown() {
        mainPage.quit();
    }

    @Test
    @DisplayName("go to Login form test")
    void loginFormTest(){
        mainPage.clickLoginFormButton();
        Assertions.assertThat(mainPage.getUrl()).isEqualTo("https://bonigarcia.dev/selenium-webdriver-java/login-form.html");
    }
    @Test
    @DisplayName("go to Web form test")
    void webFormTest(){
        mainPage.clickWebFormButton();
        Assertions.assertThat(mainPage.getUrl()).isEqualTo("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
    }

    @Test
    @DisplayName("Header test")
    void headerTest(){
        Assertions.assertThat(mainPage.headerComponent().getTitleText()).isEqualTo("Hands-On Selenium WebDriver with Java");
        Assertions.assertThat(mainPage.headerComponent().getSubTitleText()).isEqualTo("Practice site");
        mainPage.headerComponent().clickLogo();
        Assertions.assertThat(mainPage.getUrl()).isEqualTo("https://github.com/bonigarcia/selenium-webdriver-java");
    }

    @Test
    @DisplayName("Footer test")
    void footerTest(){
        Assertions.assertThat(mainPage.footerComponent().footerText()).isEqualTo("Copyright © 2021-2024 Boni García");
        mainPage.footerComponent().clickLink();
        Assertions.assertThat(mainPage.getUrl()).isEqualTo("https://bonigarcia.dev/");
    }
}
