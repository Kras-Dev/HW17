package pages;

import components.FooterComponent;
import components.HeaderComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class WebFormPage extends BasePage {
    HeaderComponent headerComponent;
    FooterComponent footerComponent;

    @FindBy(id = "my-text-id")
    @CacheLookup
    WebElement textInput;

    @FindBy(name = "my-password")
    @CacheLookup
    WebElement passwordInput;

    @FindBy(name = "my-textarea")
    @CacheLookup
    WebElement  textArea;

    @FindBy(name = "my-disabled")
    @CacheLookup
    WebElement  disabledInput;

    @FindBy(name = "my-readonly")
    @CacheLookup
    WebElement  readonlyInput;

    @FindBy(linkText = "Return to index")
    @CacheLookup
    WebElement link;

    @FindBy(name = "my-select")
    @CacheLookup
    WebElement  dropdownSelect;

    @FindBy(name = "my-datalist")
    @CacheLookup
    WebElement  dropdownDatalist;

    @FindBy(name = "my-file")
    @CacheLookup
    WebElement  fileInput;

    @FindBy(id = "my-check-1")
    @CacheLookup
    WebElement  checkedCheckbox;

    @FindBy(id = "my-check-2")
    @CacheLookup
    WebElement  defaultCheckbox;

    @FindBy(id = "my-radio-1")
    @CacheLookup
    WebElement  checkedRadio;

    @FindBy(id = "my-radio-2")
    @CacheLookup
    WebElement  defaultRadio;

    @FindBy(xpath = "//button[text() = 'Submit']")
    @CacheLookup
    WebElement submitButton;

    @FindBy(name = "my-colors")
    @CacheLookup
    WebElement  colorPicker;

    @FindBy(name = "my-date")
    @CacheLookup
    WebElement  datePicker;

    @FindBy(name = "my-range")
    @CacheLookup
    WebElement  exampleRange;


    public WebFormPage(String browser) {
        super(browser);
        this.headerComponent = new HeaderComponent(driver);
        this.footerComponent = new FooterComponent(driver);
        PageFactory.initElements(driver, this);
    }

    public WebFormPage(WebDriver driver) {
        super(driver);
        headerComponent = new HeaderComponent(driver);
        footerComponent = new FooterComponent(driver);
        PageFactory.initElements(driver, this);
    }

    public WebElement getTextInput() {
        return textInput;
    }

    public WebElement getPasswordInput() {
        return passwordInput;
    }

    public WebElement getTextArea() {
        return textArea;
    }
    public WebElement getDisabledInput() {
        return disabledInput;
    }

    public WebElement getReadonlyInput() {
        return readonlyInput;
    }

    public WebElement getLink() {
        return link;
    }

    public WebElement getDropdownSelect() {
        return dropdownSelect;
    }

    public WebElement getDropdownDatalist() {
        return dropdownDatalist;
    }

    public WebElement getFileInput() {
        return fileInput;
    }

    public WebElement getCheckedCheckbox() {
        return checkedCheckbox;
    }

    public WebElement getDefaultCheckbox() {
        return defaultCheckbox;
    }

    public WebElement getCheckedRadio() {
        return checkedRadio;
    }

    public WebElement getDefaultRadio() {
        return defaultRadio;
    }

    public WebElement getSubmitButton() {
        return submitButton;
    }

    public WebElement getColorPicker() {
        return colorPicker;
    }

    public WebElement getDatePicker() {
        return datePicker;
    }

    public WebElement getExampleRange() {
        return exampleRange;
    }

    public HeaderComponent headerComponent(){
        return headerComponent;
    }

    public FooterComponent footerComponent(){
        return footerComponent;
    }
}
