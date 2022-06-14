package model.po;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/09
 */

@Data
@NoArgsConstructor
public class GoodsSpec {

    private Integer id;
    private Integer goodsId;
    private Double price;
    private Integer stockNum;
    private String specName;
}
