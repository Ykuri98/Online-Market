package model.vo.order;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/10
 */

@NoArgsConstructor
@Data
public class OrderStateAndTokenVO {

    private Integer id;
    private Integer state;
    private Integer goodsNum;
    private Double amount;
    private Integer goodsDetailId;
    private String createtime;
    private Boolean hasComment;

    private Integer specId;
    private String img;
    private String name;
    private String spec;
    private Double unitPrice;
    private OrderStateAndTokenGoodsVO goods = new OrderStateAndTokenGoodsVO();

    public void setSpecId(Integer specId) {
        goods.setId(specId);
    }

    public void setGoodsDetailId(Integer goodsDetailId) {
        goods.setGoodsDetailId(goodsDetailId);
    }

    public void setImg(String img) {
        goods.setImg(img);
    }

    public void setName(String name) {
        goods.setName(name);
    }

    public void setSpec(String spec) {
        goods.setSpec(spec);
    }

    public void setUnitPrice(Double unitPrice) {
        goods.setUnitPrice(unitPrice);
    }
}
