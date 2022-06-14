package model.vo.goods;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yzw
 * @since 2022/06/09
 */

@NoArgsConstructor
@Data
public class GoodsInfoDetailVO {
    private Integer id;
    private String img;
    private String name;
    private String desc;
    private Integer typeId;
    private Double unitPrice;
}
