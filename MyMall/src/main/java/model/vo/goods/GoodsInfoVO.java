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
public class GoodsInfoVO {

    private List<GoodsInfoSpecVO> specs;
    private GoodsInfoDetailVO goods;

}
