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
public class GoodsDetailBO {

    private String name;
    private String typeId;
    private String img;
    private String desc;
    private List<GoodsSpecBO> specList;

}
