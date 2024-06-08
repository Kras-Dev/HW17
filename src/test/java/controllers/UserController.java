package controllers;

import configurations.APIConfig;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import modelsAPI.User;

import static io.restassured.RestAssured.given;
import static testDataAPI.ApiTestData.DEFAULT_USER;

public class UserController {
    APIConfig apiConfig = new APIConfig();
    RequestSpecification requestSpecification = given();

    public UserController(){
        // Устанавливаем парсер по умолчанию для ответов в формате JSON
        RestAssured.defaultParser = Parser.JSON;
        // Устанавливаем тип контента запросов как JSON
        this.requestSpecification.contentType(ContentType.JSON);
        // Устанавливаем заголовок Accept для принятия JSON ответов
        this.requestSpecification.accept(ContentType.JSON);
        // Устанавливаем базовый URI для всех запросов
        this.requestSpecification.baseUri(apiConfig.getBaseUrl());
        // Добавляем фильтр AllureRestAssured для интеграции с Allure Reporting в тестах
        this.requestSpecification.filter(new AllureRestAssured());
    }

    @Step("Add default user")
    public Response addDefaultUser() {
        this.requestSpecification.body(DEFAULT_USER);
        return given(this.requestSpecification).post("user").andReturn();
    }

    @Step("Add user")
    public Response createUser(User user){
        this.requestSpecification.body(user);
        return given(this.requestSpecification).post("user").andReturn();
    }

    @Step("Get user by name")
    public Response getUserByName(String username) {
        return given(this.requestSpecification).get("user/" + username).andReturn();
    }
    @Step
    public Response updateUserByName(User user, String username){
        this.requestSpecification.body(user);
        return given(this.requestSpecification).put("user/" + username).andReturn();
    }

    @Step("Delete user by name")
    public Response deleteUserByName(String username) {
        return given(this.requestSpecification).delete("user/" + username).andReturn();
    }
}
