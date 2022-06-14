package model.vo.goods;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yzw
 * @since 2022/06/11
 */
@Data
@NoArgsConstructor
public class GoodsInfoUserVO {

    private Integer id;
    private String img;
    private String name;
    private String desc;
    private Integer typeId;
    private Double unitPrice;
    private List<GoodsInfoSpecVO> specs;
}
