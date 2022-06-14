package model.vo.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yzw
 * @since 2022/06/10
 */

@NoArgsConstructor
@Data
public class OrdersByPageVO {

    private Integer total;
    private List<OrdersVO> orders;

}
