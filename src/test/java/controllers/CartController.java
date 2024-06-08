package controllers;

import configurations.AuthConfig;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import modelsAPI.CartItem;
import modelsAPI.CartItems;

import java.util.List;

import static io.restassured.RestAssured.given;

public class CartController {
    AuthConfig config = new AuthConfig();
    RequestSpecification requestSpecification = given();

    public CartController(){
        RestAssured.defaultParser = Parser.JSON;
        this.requestSpecification.header("X-Access-Token", getGuestToken());
        this.requestSpecification.header("Aecountry", "US");
        this.requestSpecification.header("Aelang", "en_US");
        this.requestSpecification.header("Aesite", "AEO_US");
        this.requestSpecification.contentType(ContentType.JSON);
        this.requestSpecification.accept(ContentType.JSON);
        this.requestSpecification.baseUri(config.getBagBaseUrl());
        this.requestSpecification.filter(new AllureRestAssured());
    }

    @Step("Get guest token")
    public String getGuestToken(){
        return given().
                when().
                header("Authorization", config.getBasicAuth()).
                header("Aesite", "AEO_US").
                formParam("grant_type", "client_credentials").
                post(config.getAuthUrl()).
                then().
                assertThat().
                statusCode(200).
                extract().jsonPath().get("access_token");
    }

    @Step("Add item to bag")
    public Response addItems(CartItems items) {
        this.requestSpecification.body(items);
        return given(this.requestSpecification)
                .post("/items")
                .then()
                .statusCode(202)
                .log().body()
                .extract().response();
    }

    @Step("Get bag data")
    public Response getBag() {
        return given(this.requestSpecification).get()
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
    }

    @Step("Edit item in bag")
    public Response editItems(CartItems items) {
        this.requestSpecification.body(items);
        return given(this.requestSpecification)
                .patch("/items")
                .then()
                .statusCode(202)
                .log().body()
                .extract().response();
    }

    @Step("Update item in bag")
    public Response updateItem(CartItems items) {
        List<CartItem> updatedItemsList = items.getItems();
        CartController controller = new CartController();

        for (CartItem updatedItem : updatedItemsList) {
            String skuIdToMatch = updatedItem.getSkuId();
            // Находим элемент в корзине по skuId
            List<CartItem> cartItems = controller.getBag().jsonPath().getList(
                    "data.items.findAll { it -> it.skuId == '" + skuIdToMatch + "' }", CartItem.class);
            // Обновляем skuId элемента
            for (CartItem cartItem : cartItems) {
                if (cartItem.getSkuId().equals(skuIdToMatch)) {
                    cartItem.setSkuId(updatedItem.getSkuId());
                }
            }
        }
        this.requestSpecification.body(items);
        return given(this.requestSpecification)
                .put("/items")
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
    }
}
