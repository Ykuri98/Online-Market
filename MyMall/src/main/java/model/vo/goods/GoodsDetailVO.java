package model.vo.goods;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/09
 */

@NoArgsConstructor
@Data
public class GoodsDetailVO {

    private Integer id;
    private String img;
    private String name;
    private Double price;
    private Integer typeId;
    private Integer stockNum;


}
