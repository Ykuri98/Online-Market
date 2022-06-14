package model.bo.goods;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yzw
 * @since 2022/06/09
 */

@NoArgsConstructor
@Data
public class GoodsInfoBO {

    private String id;
    private String name;
    private Integer typeId;
    private String desc;
    private String img;
    private List<GoodsInfoSpecBO> specList;

}
