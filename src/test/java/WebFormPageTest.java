import org.junit.jupiter.api.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import pages.WebFormPage;
import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("Web_Form_Page_Test")
public class WebFormPageTest {
    private final static String URL = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";

    WebFormPage webFormPage;

    @BeforeAll
    void setup() {
        webFormPage = new WebFormPage("edge");
        webFormPage.openUrl(URL);
    }

    @AfterAll
    void teardown() throws InterruptedException {
        Thread.sleep(5000);
        webFormPage.quit();
    }

    @Test
    @DisplayName("Text input test")
    void textInputTest(){
        WebElement textInput = webFormPage.getTextInput();
        webFormPage.enterText(textInput, "test");
        Assertions.assertEquals("test", textInput.getAttribute("value"));
    }

    @Test
    @DisplayName("Password input test")
    void passwordInputTest(){
        WebElement passwordInput = webFormPage.getPasswordInput();
        webFormPage.enterText(passwordInput, "test");
        Assertions.assertEquals("test", passwordInput.getAttribute("value"));
    }

    @Test
    @DisplayName("Text area input test")
    void textAreaInputTest(){
        WebElement textAreaInput = webFormPage.getTextArea();
        webFormPage.enterText(textAreaInput, "test");
        Assertions.assertEquals("test", textAreaInput.getAttribute("value"));
    }
    @Test
    @DisplayName("Disabled input test")
    void disabledInputTest(){
        WebElement disabledInput = webFormPage.getDisabledInput();
        Assertions.assertFalse(disabledInput.isEnabled());
    }
    @Test
    @DisplayName("Readonly input test")
    void readonlyInputTest(){
        WebElement readonlyInput = webFormPage.getReadonlyInput();
        String value = readonlyInput.getAttribute("value");
        Assertions.assertEquals("Readonly input", value);
        Assertions.assertTrue(readonlyInput.getAttribute("readonly").equals("true"));
    }
    @Test
    @DisplayName("Return to index test")
    void returnToIndexTest(){
        WebElement linkText = webFormPage.getLink();
        webFormPage.moveToElement(linkText);
        webFormPage.click(linkText);
        Assertions.assertEquals("https://bonigarcia.dev/selenium-webdriver-java/index.html", webFormPage.getUrl(),
                "Значение URL не соответствует ожидаемому");
        webFormPage.navigateBack();
    }

    @Test
    @DisplayName("Dropdown select test")
    void dropdownSelectTest(){
        WebElement dropdownSelect = webFormPage.getDropdownSelect();
        Select select = new Select(dropdownSelect);
        select.selectByVisibleText("Two");
        // Получаем выбранное значение из dropdown
        WebElement selectedOption = select.getFirstSelectedOption();
        String selectedValue = selectedOption.getText();
        Assertions.assertEquals("Two", selectedValue,
                "Значение formSelect не соответствует ожидаемому");
        Assertions.assertTrue(select.getFirstSelectedOption().isSelected());
    }

    @Test
    @DisplayName("File input test")
    void fileInputTest(){
        WebElement fileInput = webFormPage.getFileInput();
        //получаем URL ресурса "test.txt", используя загрузчик классов текущего класса
        java.net.URL url = WebFormPageTest.class.getClassLoader().getResource("test.txt");
        String absolutePath = null;
        if (url != null) {
            // Получаем абсолютный путь к файлу
            absolutePath = new File(url.getPath()).getAbsolutePath();
        } else {
            System.out.println("Файл не найден");
        }
        fileInput.sendKeys(absolutePath);
        WebElement submitButton = webFormPage.getSubmitButton();
        String uploadedFilePath = fileInput.getAttribute("value");
        // Проверяем, что путь к загруженному файлу содержит "test.txt"
        assertThat(uploadedFilePath)
                .as("Значение не содержит ожидаемую подстроку")
                .contains("test.txt");
        webFormPage.moveToElement(submitButton);
        webFormPage.click(submitButton);
        // Проверяем, что текущий URL страницы содержит "test.txt"
        assertThat(webFormPage.getUrl())
                .as("Значение не содержит ожидаемую подстроку")
                .contains("test.txt");
        webFormPage.navigateBack();
    }

    @Test
    @DisplayName("Check box test")
    void checkBoxTest(){
        WebElement checkedCheckbox = webFormPage.getCheckedCheckbox();
        Assertions.assertTrue(checkedCheckbox.isSelected());
        webFormPage.click(checkedCheckbox);
        Assertions.assertFalse(checkedCheckbox.isSelected());
        WebElement defaultCheckbox = webFormPage.getDefaultCheckbox();
        Assertions.assertFalse(defaultCheckbox.isSelected());
        webFormPage.click(defaultCheckbox);
        Assertions.assertTrue(defaultCheckbox.isSelected());
    }

    @Test
    @DisplayName("Radio button test")
    void radioTest(){
        WebElement checkedRadio = webFormPage.getCheckedRadio();
        Assertions.assertTrue(checkedRadio.isSelected());
        WebElement defaultRadio = webFormPage.getDefaultRadio();
        Assertions.assertFalse(defaultRadio.isSelected());
        webFormPage.click(defaultRadio);
        Assertions.assertTrue(defaultRadio.isSelected());
    }

    @Test
    @DisplayName("Color picker test")
    void colorPickerTest(){
        WebElement colorPicker = webFormPage.getColorPicker();
        webFormPage.click(colorPicker);
        Assertions.assertTrue(colorPicker.isDisplayed());
        Assertions.assertEquals("#563d7c", colorPicker.getAttribute("value"));
    }

    @Test
    @DisplayName("Date picker test")
    void datePickerTest(){
        WebElement datePicker = webFormPage.getDatePicker();
        datePicker.sendKeys("2024-04-10");
        Assertions.assertEquals("2024-04-10", datePicker.getAttribute("value"),
                "Значение datePicker не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Example Range test")
    void rangeTest(){
        WebElement range = webFormPage.getExampleRange();
        // Получаем значение атрибута 'value' текущего диапазона
        String currentValue = range.getAttribute("value");
        Assertions.assertEquals("5", currentValue);
    }

    @Test
    @DisplayName("Header test")
    void headerTest(){
        org.assertj.core.api.Assertions.assertThat(webFormPage.headerComponent().getTitleText()).isEqualTo("Hands-On Selenium WebDriver with Java");
        org.assertj.core.api.Assertions.assertThat(webFormPage.headerComponent().getSubTitleText()).isEqualTo("Practice site");
        webFormPage.headerComponent().clickLogo();
        org.assertj.core.api.Assertions.assertThat(webFormPage.getUrl()).isEqualTo("https://github.com/bonigarcia/selenium-webdriver-java");
        webFormPage.navigateBack();
    }

    @Test
    @DisplayName("Footer test")
    void footerTest(){
        org.assertj.core.api.Assertions.assertThat(webFormPage.footerComponent().footerText()).isEqualTo("Copyright © 2021-2024 Boni García");
        webFormPage.footerComponent().clickLink();
        org.assertj.core.api.Assertions.assertThat(webFormPage.getUrl()).isEqualTo("https://bonigarcia.dev/");
        webFormPage.navigateBack();
    }

}
