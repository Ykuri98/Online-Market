package model.vo.order;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/10
 */
@NoArgsConstructor
@Data
public class OrderStateAndTokenGoodsVO {

    private Integer id;
    private String img;
    private String name;
    private Integer goodsDetailId;
    private String spec;
    private Double unitPrice;

}
