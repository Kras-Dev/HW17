import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.PDFUtils;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@Story("Download API")
@Tag("Rest_Assured_Download_Tests")
public class DownloadAPITest {
    PDFUtils pdfUtils = new PDFUtils();

    @Test
    @DisplayName("Upload file Test")
    void uploadFileTest(){
        int petId = 21;
        String url = String.format("https://petstore.swagger.io/v2/pet/%s/uploadImage", petId);
        String fileName = "1523649151_1.png";
        File file = new File(fileName);

        given().
                when().
                multiPart("file", file, "image/png").
                contentType("multipart/form-data").
                accept("application/json").
                post(url).
                then().
                log().
                all().
                assertThat().
                contentType(ContentType.JSON).
                statusCode(200).
                body("code", equalTo(200)).
                body("type", equalTo("unknown")).
                body("message", containsString("File uploaded to ./" + fileName));
    }

    @Test
    @DisplayName("Download file Test")
    void downloadFileTest(){
        String endPoint = "https://alfabank.servicecdn.ru/site-upload/96/81/1869/dogovor_cbo_03062024.pdf";
        String fileName = "downloaded.pdf";

        Response response =
                given().
                        when().
                        get(endPoint).
                        then().
                        contentType("application/pdf").
                        statusCode(200).
                        extract().response();
        pdfUtils.savePdf(response, fileName);
        String pdfText = pdfUtils.readPdf(fileName);
        assertThat(pdfText).contains("о комплексном банковском обслуживании физических лиц")
                .contains("Общие условия открытия и совершения операций по Счету «Мой сейф»");
    }
}
