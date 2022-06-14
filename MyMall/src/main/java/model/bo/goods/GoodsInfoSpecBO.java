package model.bo.goods;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/09
 */
@Data
@NoArgsConstructor
public class GoodsInfoSpecBO {

    private Integer id;
    private String specName;
    private Integer stockNum;
    private Integer unitPrice;

}
