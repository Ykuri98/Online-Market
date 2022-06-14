package model.vo.order;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/10
 */

@Data
@NoArgsConstructor
public class OrdersUserVO {

    private String nickname;
    private String name;
    private String address;
    private String phone;

}
