package dao;

import model.bo.goods.GoodsAskGoodsMsgBO;
import model.po.Goods;
import model.po.GoodsSpec;
import model.po.Msg;
import model.po.Order;
import model.vo.goods.*;
import model.vo.order.OrderStateAndTokenGoodsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsDao {
    List<GoodsTypeVO> queryAllGoodsType();

    List<GoodsDetailVO> queryGoodsDetailByType(Integer typeId);

    Integer insertGoodsType(Goods goods);

    Integer queryGoodsType(Goods goods);

    Integer insertGoods(@Param("goods") Goods goods, @Param("price") Double price, @Param("stockNum") Integer stockNum);

    Integer insertGoodsSpec(@Param("goodsId") Integer id, @Param("name") String name,@Param("price") Double price, @Param("stockNum") Integer stockNum);

    Integer queryIsGoodsExist(@Param("name") String name, @Param("typeId") Integer typeId);

    Integer queryGoodsIdById(Integer id);

    Integer deleteGoodsById(Integer id);

    Integer deleteGoodsSpecById(Integer id);

    GoodsInfoDetailVO queryGoodsById(Integer id);

    List<GoodsInfoSpecVO> queryGoodsSpecById(Integer id);

    Integer updateGoodsDetail(@Param("goods") Goods goods,@Param("price")Double price, @Param("stockNum") Integer stockNum);

    Integer updateGoodsSpec(GoodsSpec goodsSpec);

    GoodsAddSpecVO queryGoodsSpec(@Param("goodsId") Integer goodsId, @Param("specName") String specName, @Param("price") Double price, @Param("stockNum") Integer stockNum);

    List<GoodsNoReplyMsgVO> queryGoodsNoReplyMsg();

    List<GoodsRepliedMsgVO> queryGoodsRepliedMsg();

    Integer updateGoodsMsg(Msg msg);

    List<GoodsDetailVO> queryGoodsByName(String name);

    List<GoodsMsgGetByUserVO> queryGoodsMsgByGoodsId(Integer goodsId);

    List<GoodsCommentListVO> queryOrderByGoodsId(int goodsId);

    Integer queryUserIdByNickname(String nickname);

    Integer insertGoodsAskMsg(@Param("askGoodsMsg") GoodsAskGoodsMsgBO goodsAskGoodsMsgBO, @Param("userId") Integer userId, @Param("goodsName") String goodsName, @Param("createTime") String createTime);

    Integer deleteGoodsMsgByGoodsId(int goodsId);

    Integer deleteOrderByGoodsId(int goodsId);

    Integer deleteGoodsTypeById(int id);
}
