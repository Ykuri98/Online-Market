package model.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yzw
 * @since 2022/06/10
 */

@Data
@NoArgsConstructor
public class Order {
    Integer id;
    Integer userId;
    String nickname;
    String receiver;
    String address;
    String phone;
    String goods;
    Integer goodsId;
    String  spec;
    Integer specId;
    Integer number;
    Double amount;
    Integer stateId;
    Date createTime;
    String hasComment;
    Double score;
}
