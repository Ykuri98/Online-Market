package model.vo.order;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/10
 */

@Data
@NoArgsConstructor
public class OrdersVO {
    private Integer id;
    private Integer userId;
    private Integer goodsDetailId;
    private String goods;
    private String spec;
    private Integer goodsNum;
    private Double amount;
    private Integer stateId;
    private String state;
    private OrdersUserVO user = new OrdersUserVO();

    private String nickname;
    private String name;
    private String address;
    private String phone;

    public void setNickname(String nickname) {
        user.setNickname(nickname);
    }

    public void setName(String name) {
        user.setName(name);
    }

    public void setAddress(String address) {
        user.setAddress(address);
    }

    public void setPhone(String phone) {
        user.setPhone(phone);
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
        if (stateId == 0){
            setState("未付款");
        }
        if (stateId == 1){
            setState("未发货");
        }
        if (stateId == 2){
            setState("已发货");
        }
        if (stateId == 3){
            setState("已到货");
        }
    }
}
