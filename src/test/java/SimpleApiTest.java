import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import modelsAPI.User;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("Rest Assured Simple Tests")
public class SimpleApiTest {
    private final static String URL = "https://petstore.swagger.io/v2/user";
    private String USERNAME = "testUser";
    @AfterAll
    void cleanupTestData(){
        // Проверка существования пользователя перед удалением
        Response response = given()
                .when()
                .get(URL + "/{username}", USERNAME)
                .thenReturn();

        if (response.statusCode() == 200) {
            given()
                    .when()
                    .delete(URL + "/{username}", USERNAME)
                    .then()
                    .statusCode(200);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Create user test")
    void createUserTest(){
        // Определение тела запроса для создания нового пользователя
        String requestBody = """
            {
              "id": 0,
              "username": "testUser",
              "firstName": "firstName",
              "lastName": "lastName",
              "email": "test@example.com",
              "password": "test123",
              "phone": "phone not exist",
              "userStatus": 0
            }
            """;
        // Отправка POST запроса для создания пользователя и получение ответа
        ValidatableResponse response = given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(URL)
                .then()
                .statusCode(200);
        //Для проверки, что пользователь был успешно создан
        given()
                .when()
                .get(URL + "/{username}", USERNAME)
                .then()
                .statusCode(200)
                .body("username", equalTo("testUser"));
    }

    @Test
    @DisplayName("Get user Test")
    @Order(2)
    void getUserTest(){
        // Проверка, что созданный пользователь имеет ожидаемый username
        Response getResponse = given()
                .when()
                .get(URL + "/{username}", USERNAME)
                .then()
                .extract().response();
        Assertions.assertEquals("testUser", getResponse.jsonPath().getString("username"));
    }

    @Test
    @DisplayName("Update User Test")
    @Order(3)
    void updateUserTest() {
        String requestBody = """
            {
                "id": 0,
                "username": "testUserUpdated",
                "firstName": "updatedFirstName",
                "lastName": "updatedLastName",
                "email": "test.updated@example.com",
                "password": "updated123",
                "phone": "updatedPhone",
                "userStatus": 1
            }
            """;
        // Отправка PUT запроса для изменения пользователя и получение ответа
        given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .put(URL + "/{username}", USERNAME)
                .then()
                .statusCode(200);

        USERNAME = "testUserUpdated";

        //Для проверки, что пользователь был успешно изменён
        given()
                .when()
                .get(URL + "/{username}", USERNAME)
                .then()
                .statusCode(200)
                .body("username", equalTo("testUserUpdated"));
    }

    @Test
    @DisplayName("PUT with model User Test")
    @Order(4)
    void putWithModelUserTest(){
        User user = new User(0, "modelUser", "modelFirstName", "modelLastName", "model@example.com",
                "modelPassword", "11223344", 0);

        ValidatableResponse response = given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .put(URL + "/{username}", USERNAME)
                .then()
                .statusCode(200);

        USERNAME = user.getUsername();
        //Для проверки, что пользователь был успешно изменён
        given()
                .when()
                .get(URL + "/{username}", USERNAME)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .body("username", equalTo("modelUser"))
                .body("firstName", startsWith("modelFirstName"))
                .body("lastName", equalToIgnoringCase("MODELLastName"))
                .body("email", matchesPattern("^[a-zA-Z0-9._%+-]+@example\\.com$"))
                .body("password", equalTo("modelPassword"))
                .body("phone", equalTo("11223344"));
    }
    @Test
    @DisplayName("Delete User Test")
    @Order(5)
    void deleteUserTest() {
        // Отправка DELETE запроса для удаления пользователя
        given()
                .when()
                .delete(URL + "/{username}", USERNAME)
                .then()
                .statusCode(200);

        // Для проверки, что пользователь успешно удален
        Response response = given()
                .when()
                .get(URL + "/{username}", USERNAME)
                .thenReturn();
        Assertions.assertEquals(404, response.statusCode());
    }
}