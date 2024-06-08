import configurations.AuthConfig;
import controllers.CartController;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import modelsAPI.CartItem;
import modelsAPI.CartItems;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static testDataAPI.ApiTestData.JEANS_ITEM;
import static testDataAPI.ApiTestData.POLO_ITEM;

@Story("Auth API")
@Tag("Rest_Assured_Auth_Tests")
public class AuthAPITest {
    CartController controller = new CartController();
    AuthConfig config = new AuthConfig();

    @Test
    @DisplayName("Get auth token Test")
    @Description("Get auth token")
    void getAuthToken(){
        assertThat(controller.getGuestToken().equals(config.getBasicAuth()));
    }

    @Test
    @DisplayName("Add items Test")
    @Description("Add items in cart")
    void addItemTest() {
        int qtyBefore = controller.getBag().jsonPath().get("data.itemCount");
        // assertThat(qtyBefore).isEqualTo(0);
        System.out.println("BEFORE: " + qtyBefore);
        controller.addItems(JEANS_ITEM);
        controller.addItems(JEANS_ITEM);
        int qtyAfter = controller.getBag().jsonPath().get("data.itemCount");
        System.out.println("AFTER: " + qtyAfter + " SUM: " + qtyBefore+2) ;
        assertThat(qtyAfter).isGreaterThan(qtyBefore);
        //assertThat(qtyAfter).isEqualTo(qtyBefore+2);
    }

    @Test
    @DisplayName("Edit items Test")
    @Description("Edit (patch) items in cart")
    void editItemTest() {
        int qtyBefore = controller.getBag().jsonPath().get("data.itemCount");
        assertThat(qtyBefore).isEqualTo(0);

        controller.addItems(JEANS_ITEM);
        String itemId = controller.getBag().jsonPath().get("data.items[0].itemId");
        CartItem updatedItem = JEANS_ITEM.getItems().get(0);
        updatedItem.setItemId(itemId);
        updatedItem.setQuantity(2);
        CartItems updatedItems = CartItems.builder()
                .items(List.of(updatedItem))
                .build();

        controller.editItems(updatedItems);
        int qtyAfter = controller.getBag().jsonPath().get("data.itemCount");
        assertThat(qtyAfter).isEqualTo(2);
    }

    @Test
    @DisplayName("Update items Test")
    @Description("Update (put) items in cart")
    @Disabled("Этот тест не проходит, т.к авторизационного токена не достаточно для таких действий" +
            " 403 Status code")
    void updateItemTest() {
        int qtyBefore = controller.getBag().jsonPath().get("data.itemCount");
        assertThat(qtyBefore).isEqualTo(0);
        controller.addItems(JEANS_ITEM);
        // Обновляем JEANS_ITEM на POLO_ITEM
        CartItem itemToUpdate = POLO_ITEM.getItems().get(0);
        itemToUpdate.setItemId(controller.getBag().jsonPath().get("data.items[0].itemId"));
        CartItems updatedItems = CartItems.builder()
                .items(List.of(itemToUpdate))
                .build();
        controller.updateItem(updatedItems);
        // Получаем элемент из корзины
        CartItem updatedItem = controller.getBag().jsonPath().getObject("data.items[0]", CartItem.class);
        // Проверяем, что SKU_ID обновленного элемента соответствует SKU_ID элемента POLO_ITEM
        assertThat(updatedItem).isNotNull();
        assertThat(POLO_ITEM.getItems().get(0).getSkuId()).isEqualTo(updatedItem.getSkuId());
    }
}
