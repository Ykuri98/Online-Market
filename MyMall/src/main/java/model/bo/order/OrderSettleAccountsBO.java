package model.bo.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yzw
 * @since 2022/06/11
 */

@NoArgsConstructor
@Data
public class OrderSettleAccountsBO {

    private List<OrderCartListBO> cartList;

}
