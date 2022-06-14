package model.vo.goods;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/09
 */

@NoArgsConstructor
@Data
public class GoodsAddSpecVO {

    private String goodsId;
    private String specName;
    private String stockNum;
    private String unitPrice;

}
