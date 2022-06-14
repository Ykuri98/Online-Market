package model.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yzw
 * @since 2022/06/09
 */

@Data
@NoArgsConstructor
public class Goods {
    private Integer id;
    private String name;
    private Integer typeId;
    private String typeName;
    private String image;
    private String description;
    private List<GoodsSpec> goodsSpecList;
}
