package modelsAPI;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CartItem {
    private String skuId;
    private int quantity;
    private String itemId;
}
