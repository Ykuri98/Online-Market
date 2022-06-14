package model.vo.goods;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/10
 */
@Data
@NoArgsConstructor
public class GoodsRepliedMsgVO {
    private Integer id;
    private Integer userId;
    private Integer goodsId;
    private String content;
    private Integer state;
    private String createtime;
    private String replyContent;

    private String username;
    private String goodsName;
    private GoodsMsgGoodsVO goods = new GoodsMsgGoodsVO();
    private GoodsMsgUserVO user = new GoodsMsgUserVO();

    public void setUsername(String username) {
        user.setName(username);
    }

    public void setGoodsName(String goodsName) {
        goods.setName(goodsName);
    }
}
