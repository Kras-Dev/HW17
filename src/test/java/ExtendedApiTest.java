import configurations.APIConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.path.json.JsonPath.from;
import modelsAPI.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("Rest_Assured_Extended_Tests")
public class ExtendedApiTest {
    private String USERNAME = "";
    APIConfig apiConfig;
    @BeforeEach
    void setUp() {
        // Инициализация apiConfig
        apiConfig = new APIConfig();
    }
    @AfterEach
    void cleanupTestData(){
        // Проверка существования пользователя перед удалением
        Response response = given()
                .when()
                .get(apiConfig.getBaseUrl() + "user/{username}", USERNAME)
                .thenReturn();

        if (response.statusCode() == 200) {
            given()
                    .when()
                    .delete(apiConfig.getBaseUrl() + "user/{username}", USERNAME)
                    .then()
                    .statusCode(200);
            System.out.println("@AfterEach: user " + USERNAME + " was deleted");
        }

    }
    @Test
    @DisplayName("Get User Deserialization Test")
    void  getUserDeserializationTest(){
        // Создание объекта User
        User newUser = User.builder()
                .id(0)
                .username("extendedUser")
                .firstName("firstName")
                .lastName("lastName")
                .email("test@example.com")
                .password("test123")
                .phone("123443321")
                .userStatus(0)
                .build();
        // Вызов метода для создания нового пользователя через API
        createUser(newUser);
        USERNAME = newUser.getUsername();
        // Получение фактического пользователя через API и сравнение с ожидаемым пользователем
        User actualUser = given().when().get(apiConfig.getBaseUrl() + "user/{username}", USERNAME).as(User.class);
        assertThat(actualUser).usingRecursiveComparison().ignoringFields("id").isEqualTo(newUser);
        // Использование SoftAssertions для проверки каждого поля пользователя по отдельности
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actualUser.getFirstName()).isEqualTo(newUser.getFirstName());
        softly.assertThat(actualUser.getLastName()).isEqualTo(newUser.getLastName());
        softly.assertThat(actualUser.getUsername()).isEqualTo(newUser.getUsername());
        softly.assertThat(actualUser.getUserStatus()).isEqualTo(newUser.getUserStatus());
        softly.assertThat(actualUser.getEmail()).isEqualTo(newUser.getEmail());
        softly.assertThat(actualUser.getPassword()).isEqualTo(newUser.getPassword());
        softly.assertThat(actualUser.getPhone()).isEqualTo(newUser.getPhone());
        // Проверка всех условий и вывод всех ошибок одновременно
        softly.assertAll();
    }

    @Test
    @DisplayName("Update User Test using jsonPath")
    void updateUserTest(){
        User newUser = User.builder()
                .id(0)
                .username("extendedUser")
                .firstName("firstName")
                .lastName("lastName")
                .email("test@example.com")
                .password("test123")
                .phone("123443321")
                .userStatus(0)
                .build();
        // Создание нового пользователя
        createUser(newUser);
        USERNAME = newUser.getUsername();
        // Изменение данных пользователя
        User updatedUser = User.builder()
                .id(newUser.getId())
                .username("updatedUser")
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email("updated@example.com")
                .password("updated123")
                .phone("987654321")
                .userStatus(1)
                .build();
        // Выполнение запроса PUT для обновления пользователя
        given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(updatedUser)
                .when()
                .put(apiConfig.getBaseUrl() + "user/{username}",USERNAME)
                .then()
                .statusCode(200);
        USERNAME = updatedUser.getUsername();
        // Получение обновленного пользователя и проверка его данных через jsonPath
        String responseBody = given()
                .when()
                .get(apiConfig.getBaseUrl() + "user/{username}", USERNAME)
                .then()
                .statusCode(200)
                .extract()
                .body().asString();
        // Инициализируем объект JsonPath для работы с JSON из ответа
        JsonPath jsonPath = from(responseBody);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(jsonPath.getLong("id")).isPositive();
        softly.assertThat(jsonPath.getString("firstName")).isEqualTo(updatedUser.getFirstName());
        softly.assertThat(jsonPath.getString("lastName")).isEqualTo(updatedUser.getLastName());
        softly.assertThat(jsonPath.getString("username")).isEqualTo(updatedUser.getUsername());
        softly.assertThat(jsonPath.getInt("userStatus")).isEqualTo(updatedUser.getUserStatus());
        softly.assertThat(jsonPath.getString("email")).isEqualTo(updatedUser.getEmail());
        softly.assertThat(jsonPath.getString("password")).isEqualTo(updatedUser.getPassword());
        softly.assertThat(jsonPath.getString("phone")).isEqualTo(updatedUser.getPhone());
        // Проверка всех условий и вывод всех ошибок одновременно
        softly.assertAll();

    }

    @Test
    @DisplayName("Get Pet by status Test using JsonSchema")
    void getPetByStatusTest(){
        given().
                header("accept", "application/json").
                // Добавляем параметр запроса status со значением "available"
                queryParam("status", "available").
                when().
                get(apiConfig.getBaseUrl() + "pet/findByStatus").
                then().
                // Проверяем, что полученный ответ соответствует заданной JSON схеме
                assertThat().
                body(matchesJsonSchemaInClasspath("jsonSchema/petSchema.json"));
    }

    @Test
    @DisplayName("Delete User Test")
    void deleteUserTest() {
        // Отправка DELETE запроса для удаления пользователя
        given()
                .when()
                .delete(apiConfig.getBaseUrl() + "user/{username}", USERNAME)
                .then()
                .statusCode(404);
        // Для проверки, что пользователь успешно удален
        Response response = given()
                .when()
                .get(apiConfig.getBaseUrl() + "user/{username}", USERNAME)
                .thenReturn();
        Assertions.assertEquals(404, response.statusCode());
        // Проверяем, что ответ пустой после удаления пользователя
        JsonPath jsonPath = response.jsonPath();
        // Преобразуем JSON в строку для проверки
        String responseString = jsonPath.prettify();

        Assertions.assertTrue(responseString.isEmpty(), "Пользователь не был удален");
    }

    // Метод для создания нового пользователя через API
    void createUser(User user){
        given().
                header("accept", "application/json").
                header("Content-Type", "application/json").
                body(user).
                when().
                post(apiConfig.getBaseUrl() + "user").
                then().
                statusCode(200);
    }
}
