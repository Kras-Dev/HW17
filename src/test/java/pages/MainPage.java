package pages;

import components.FooterComponent;
import components.HeaderComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage extends BasePage{
    @FindBy(linkText = "Login form")
    @CacheLookup
    WebElement loginFormButton;

    @FindBy(linkText = "Web form")
    @CacheLookup
    WebElement webFormButton;

    HeaderComponent headerComponent;
    FooterComponent footerComponent;

    public MainPage(String browser) {
        super(browser);
        this.headerComponent = new HeaderComponent(driver);
        this.footerComponent = new FooterComponent(driver);
        PageFactory.initElements(driver, this);
    }

    public MainPage(WebDriver driver) {
        super(driver);
        headerComponent = new HeaderComponent(driver);
        footerComponent = new FooterComponent(driver);
        PageFactory.initElements(driver, this);
    }

    public void clickLoginFormButton(){
        moveToElement(loginFormButton);
        loginFormButton.click();
    }

    public void clickWebFormButton(){
        moveToElement(webFormButton);
        webFormButton.click();
    }
    public HeaderComponent headerComponent(){
        return headerComponent;
    }

    public FooterComponent footerComponent(){
        return footerComponent;
    }
}
