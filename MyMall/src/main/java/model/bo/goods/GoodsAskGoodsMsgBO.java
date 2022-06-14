package model.bo.goods;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/12
 */

@NoArgsConstructor
@Data
public class GoodsAskGoodsMsgBO {

    private String token;
    private String msg;
    private String goodsId;

}
