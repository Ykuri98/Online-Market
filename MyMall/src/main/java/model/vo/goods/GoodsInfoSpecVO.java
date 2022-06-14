package model.vo.goods;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/09
 */
@Data
@NoArgsConstructor
public class GoodsInfoSpecVO {

    private Integer id;
    private String specName;
    private Integer stockNum;
    private Double unitPrice;
}
