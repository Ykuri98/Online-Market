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
public class OrdersUpdateGetVO {

    private Integer id;
    private Double amount;
    private Integer num;
    private Integer goodsDetailId;
    private Integer state;
    private String goods;

    private Integer specId;
    private OrderCurStateVO curState = new OrderCurStateVO();
    private OrderCurSpecVO curSpec = new OrderCurSpecVO();

    private List<OrderSpecVO> spec;
    private List<OrderStatesVO> states;

    public void setState(Integer state) {
        this.state = state;
        curState.setId(state);
    }

    public void setSpecId(Integer specId) {
        this.specId = specId;
        curSpec.setId(specId);
    }

}
