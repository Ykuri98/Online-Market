package model.bo.goods;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/09
 */

@NoArgsConstructor
@Data
public class GoodsAddSpecBO {

    private String goodsId;
    private String specName;
    private String stockNum;
    private String unitPrice;

}
