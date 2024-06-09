import controllers.UserController;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import modelsAPI.User;
import org.junit.jupiter.api.*;
import testDataAPI.ApiTestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

@Feature("ControllerAPITests")
@Tag("Rest_Assured_Extended_Tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControllerApiTest {
    UserController userController = new UserController();
    private String createdUserName = "";

    @AfterEach
    void cleanupData(){
        userController.deleteUserByName(createdUserName);
        createdUserName = "";
    }

    @Test
    @DisplayName("Create and check new user Test")
    void createUserTest(){
        // Создание нового пользователя
        User user = createTestUser();
        // Сохранение имени созданного пользователя
        createdUserName = user.getUsername();
        // Получение информации о созданном пользователе
        Response getUserResponse = userController.getUserByName(user.getUsername());
        // Проверка успешного получения информации о пользователе
        getUserResponse.then().statusCode(200);
        // Десериализация данных ответа в объект пользователя для сравнения
        User responseBody = getUserResponse.getBody().as(User.class);
        // Сравнение ожидаемого созданного пользователя с полученными данными
        assertThat(responseBody).usingRecursiveComparison().ignoringFields("id").isEqualTo(user);
    }

    @Test
    @DisplayName("Get user by username")
    public void getUserByNameTest(){
        User user = getOrCreateTestUser();
        createdUserName = user.getUsername();
        // Выполнение запроса на получение информации о пользователе по имени
        Response response = userController.getUserByName(createdUserName);
        // Проверка статус кода ответа
        response.then().statusCode(200);
        // Проверка соответствия имени пользователя в ответе ожидаемому имени
        response.then().body("username", equalTo(createdUserName));
    }

    @Test
    @DisplayName("Update an existing user")
    public void updateUserTest() {
        User user = getOrCreateTestUser();
        createdUserName = user.getUsername();
        // Получаем информацию о пользователе по текущему имени
        Response getUserResponse = userController.getUserByName(createdUserName);
        getUserResponse.then().statusCode(200);
        // Получаем объект пользователя из ответа
        User existingUser = getUserResponse.getBody().as(User.class);
        // Обновляем данные пользователя
        existingUser.setFirstName("New First Name");
        existingUser.setLastName("New Last Name");
        // Обновляем информацию о пользователе
        Response updateResponse = userController.updateUserByName(existingUser, existingUser.getUsername());
        updateResponse.then().statusCode(200);
        // Получаем информацию о пользователе по новому имени
        Response getUpdatedUserResponse = userController.getUserByName(existingUser.getUsername());
        getUpdatedUserResponse.then().statusCode(200);
        // Получаем объект пользователя из ответа
        User updatedUser = getUpdatedUserResponse.getBody().as(User.class);
        // Проверяем, что информация о пользователе была успешно обновлена
        assertThat(updatedUser).usingRecursiveComparison().ignoringFields("id").isEqualTo(existingUser);
    }

    @Test
    @DisplayName("Delete user")
    public void deleteUserTest() {
        User user = getOrCreateTestUser();
        createdUserName = user.getUsername();
        // Удаляем пользователя по имени
        Response deleteUserResponse = userController.deleteUserByName(createdUserName);
        deleteUserResponse.then().statusCode(200);
        // Пытаемся получить информацию о пользователе после удаления
        Response getUserResponseAfterDeletion = userController.getUserByName(createdUserName);
        // Проверяем, что пользователь больше не найден
        getUserResponseAfterDeletion.then().statusCode(404);
    }

    // Вспомогательный метод для создания пользователя и возврата этого пользователя
    private User createTestUser() {
        User user = ApiTestData.DEFAULT_USER;
        Response createUserResponse = userController.createUser(user);
        createUserResponse.then().statusCode(200);
        return user;
    }
    private User getOrCreateTestUser() {
        User testUser;
        if (createdUserName.isEmpty()) {
            //Если пользователь не был создан, создаем тестового пользователя
            testUser = createTestUser();
            createdUserName = testUser.getUsername();
        } else {
            // Если пользователь уже существует, извлекаем данные пользователя
            Response getUserResponse = userController.getUserByName(createdUserName);
            testUser = getUserResponse.getBody().as(User.class);
        }
        return testUser;
    }
}
