package model.bo.order;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/11
 */

@NoArgsConstructor
@Data
public class OrderSendCommentBO {

    private String token;
    private Integer orderId;
    private Integer goodsId;
    private Integer goodsDetailId;
    private String content;
    private Integer score;

}
