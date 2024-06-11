package allureSteps;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.codeborne.selenide.Screenshots.takeScreenShotAsFile;

public class AllureSteps {
    // Метод для захвата скриншота и добавления его в отчет Allure как вложение типа "image/png"
    @Attachment(value = "Screenshot", type = "image/png")
    @Step("Capture screenshot")
    public byte[] captureScreenshot(WebDriver driver){
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
    // Метод для захвата скриншота и добавления его в отчет Allure как вложение с использованием InputStream
    @Step("Capture screenshot (spoiler)")
    public void captureScreenshotSpoiler(WebDriver driver){
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }
    // Метод для захвата скриншота и добавления его в отчет Allure, используя драйвер из BaseSteps
    @Step("Capture screenshot (extension)")
    public void captureScreenshotSpoiler() {
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(((TakesScreenshot) BaseStepsEach.getDriver()).getScreenshotAs(OutputType.BYTES)));
    }
    // Метод для загрузки файла по ссылке, сохранения на диск и добавления в отчет Allure
    // Аннотация для формирования Steps в отчете Allure, где {destination} будет заменен на имя файла
    @Step("Download file: {destination}")
    public void download(String link, File destination) throws IOException {
        // Создание CloseableHttpClient для выполнения HTTP-запросов
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            // Создание GET-запроса по указанному URL-адресу
            HttpUriRequestBase request = new HttpGet(link);
            // Выполнение запроса и передача обработчика ответа
            client.execute(request, (HttpClientResponseHandler<byte[]>) response -> {
                // Запись данных из InputStream в файл
                Files.write(destination.toPath(), response.getEntity().getContent().readAllBytes());
                // Добавление содержимого файла как вложения к отчету Allure
                Allure.addAttachment(destination.getName(), Files.newInputStream(destination.toPath()));
                // Возвращаем null, поскольку у нас нет необходимости возвращать значение из обработчика
                return null;
            });
        }
    }

    @Attachment(value = "Screenshot", type = "image/png")
    @Step("Capture screenshot with Selenide")
    public byte[] captureScreenshotSelenide() throws IOException {
        return com.google.common.io.Files.toByteArray(takeScreenShotAsFile());
    }

    @Step("Capture screenshot with Selenide (extension)")
    public void captureScreenshotSelenideSpoiler() throws IOException {
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(com.google.common.io.Files.toByteArray(takeScreenShotAsFile())));
    }
}
