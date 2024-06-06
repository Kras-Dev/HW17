package testDataAPI;

import modelsAPI.CartItem;
import modelsAPI.CartItems;
import modelsAPI.User;

import java.util.List;

public class ApiTestData {
    public static final User DEFAULT_USER = new User(0, "FPMI_user_1", "firstName",
            "lastName", "email@gmail.com", "qwerty", "12345678", 0);
    public static final User DEFAULT_USER_BUILDER = User.builder()
            .id(0)
            .username("FPMI_user_1")
            .firstName("firstName")
            .lastName("lastName")
            .email("email@gmail.com")
            .password("qwerty")
            .phone("12345678")
            .userStatus(0)
            .build();

    public static final User TEST = User.builder()
            .id(1)
            .username("test")
            .build();

    public static final CartItems JEANS_ITEM = CartItems.builder()
            .items(List.of(new CartItem("0040716474", 1, null)))
            .build();
    public static final CartItems POLO_ITEM = CartItems.builder()
            .items(List.of(new CartItem("0041024928", 1, null)))
            .build();
}
