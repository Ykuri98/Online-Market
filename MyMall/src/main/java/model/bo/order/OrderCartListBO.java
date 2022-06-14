package model.bo.order;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/11
 */
@NoArgsConstructor
@Data
public class OrderCartListBO {

    private Integer id;
    private Integer goodsNum;
    private Double amount;

}
