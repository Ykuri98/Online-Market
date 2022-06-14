package service;

import model.bo.goods.*;
import model.vo.goods.*;

import java.util.List;

public interface GoodsService {
    /**
     * 获取所有商品品类
     * @return 数据库包装类GoodsTypeVO的集合
     */
    List<GoodsTypeVO> getType();

    /**
     * 根据传入的商品品类id获取该品类下所有商品信息
     * @param typeId 商品品类id
     * @return 数据库包装类GoodsDetailVO的集合
     */
    List<GoodsDetailVO> getGoodsByType(String typeId);

    /**
     * 根据传入的goodsTypeBO类添加商品品类
     * @param goodsTypeBO 前端json包装类
     * @return 1表示添加成功，0表示添加失败
     */
    Integer addGoodsType(GoodsTypeBO goodsTypeBO);

    /**
     * 根据传入的goodsDetailBO类添加商品信息
     * @param goodsDetailBO 前端json包装类
     * @return 1表示添加成功，0表示添加失败
     */
    Integer addGoods(GoodsDetailBO goodsDetailBO);

    /**
     * 根据传入的id删除商品信息
     * @param id 商品信息id
     * @return 1表示删除成功，0表示删除失败
     */
    Integer deleteGoodsById(String id);

    /**
     * 根据传入的id得到商品信息
     * @param id 商品信息id
     * @return 数据库包装类GoodsInfoVO
     */
    GoodsInfoVO getGoodsInfo(String id);

    /**
     * 根据传入的goodsInfoBO类更新商品信息
     * @param goodsInfoBO 前端json包装类
     * @return 1表示更新成功，0表示更新失败
     */
    Integer updateGoods(GoodsInfoBO goodsInfoBO);

    /**
     * 根据传入的goodsAddSpecBO类添加商品规格
     * @param goodsAddSpecBO 前端json包装类
     * @return 数据库包装类GoodsAddSpecVO
     */
    GoodsAddSpecVO addSpec(GoodsAddSpecBO goodsAddSpecBO);

    /**
     * 获取所有未回复的留言
     * @return 数据库包装类GoodsNoReplyMsgVO的list
     */
    List<GoodsNoReplyMsgVO> getNoReplyMsg();

    /**
     * 获取所有已回复的留言
     * @return 数据库包装类GoodsRepliedMsgVO的list
     */
    List<GoodsRepliedMsgVO> getRepliedMsg();

    /**
     * 根据前端数据更新留言，并将状态从未回复变为已回复
     * @param goodsReplyMsgBO 前端json包装类
     * @return 1表示更新成功，0表示更新失败
     */
    Integer replyMsg(GoodsReplyMsgBO goodsReplyMsgBO);

    List<GoodsDetailVO> getGoodsByName(String name);

    GoodsInfoUserVO getGoodsInfoUser(String id);

    List<GoodsMsgGetByUserVO> getGoodsMsg(String id);

    GoodsCommentVO getGoodsComment(String goodsId);

    Integer askGoodsMsg(GoodsAskGoodsMsgBO goodsAskGoodsMsgBO);

    Integer deleteGoodsType(String typeId);
}
