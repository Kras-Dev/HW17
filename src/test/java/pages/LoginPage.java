package pages;

import components.FooterComponent;
import components.HeaderComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage{
    HeaderComponent headerComponent;
    FooterComponent footerComponent;
    By loginInput = By.id("username");
    By passwordInput = By.id("password");
    By submitButton = By.xpath("//button[@type='submit']");
    By successAlert = By.id("success");
    By invalidCredentialsAlert = By.id("invalid");


    public LoginPage(String browser) {
        super(browser);
        this.headerComponent = new HeaderComponent(driver);
        this.footerComponent = new FooterComponent(driver);
    }

    public LoginPage(WebDriver driver) {
        super(driver);
        headerComponent = new HeaderComponent(driver);
        footerComponent = new FooterComponent(driver);
    }

    public void setLoginInput(String login, String password){
        enterText(loginInput, login);
        enterText(passwordInput, password);
        click(submitButton);
    }
    public boolean successAlertPresent() {
        return isDisplayed(successAlert);
    }

    public boolean invalidCredentialsAlertPresent() {
        return isDisplayed(invalidCredentialsAlert);
    }

    public HeaderComponent headerComponent(){
        return headerComponent;
    }

    public FooterComponent footerComponent(){
        return footerComponent;
    }
}
