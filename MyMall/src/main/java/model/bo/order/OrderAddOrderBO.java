package model.bo.order;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/12
 */

@NoArgsConstructor
@Data
public class OrderAddOrderBO {
    private String token;
    private Integer goodsDetailId;
    private Integer num;
    private Integer state;
    private Integer amount;
}
