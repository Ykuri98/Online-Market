package dao;

import model.bo.order.OrderAddOrderBO;
import model.bo.order.OrderCartListBO;
import model.bo.order.OrderSendCommentBO;
import model.po.Order;
import model.po.User;
import model.vo.order.OrderSpecVO;
import model.vo.order.OrderStateAndTokenVO;
import model.vo.order.OrdersUpdateGetVO;
import model.vo.order.OrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDao {

    List<OrdersVO> queryOrdersWithLimit(@Param("order") Order order, @Param("currentLine") Integer currentLine, @Param("pagesize") Integer pagesize, @Param("moneyHighLimit") String moneyHighLimit, @Param("moneyLowLimit") String moneyLowLimit);

    Integer queryTotal(@Param("order") Order order);

    OrdersUpdateGetVO queryOrdersById(Integer id);

    List<OrderSpecVO> queryGoodsSpecById(Integer id);

    Integer updateOrder(Order order);

    Integer deleteOrder(int id);

    List<OrderStateAndTokenVO> queryOrdersByStateAndNickname(@Param("stateId") Integer stateId, @Param("nickname") String nickname);

    OrderStateAndTokenVO queryGoodsSpecBySpecId(Integer specId);

    OrderStateAndTokenVO queryGoodsById(Integer goodsDetailId);

    Integer updateOrderStateById(@Param("id") Integer id, @Param("stateId") Integer stateId);

    Integer updateOrderCommentById(OrderSendCommentBO orderSendCommentBO);

    Integer queryUserIdByNickname(String nickname);

    String queryGoodsNameById(Integer id);

    Integer insertGoodsMsg(@Param("orderSendCommentBO") OrderSendCommentBO orderSendCommentBO, @Param("userId") Integer userId, @Param("createTime") String createTime, @Param("goodsName") String goodsName);

    User queryUserByNickname(String nickname);

    Integer insertOrder(@Param("addOrder") OrderAddOrderBO orderAddOrderBO, @Param("userId") Integer userId, @Param("receiver") String receiver, @Param("address") String address, @Param("phone") String phone, @Param("goods") String goods, @Param("spec") String spec, @Param("specId") Integer specId,@Param("createTime") String createTime,@Param("goodsId") Integer goodsId);

    Integer updateGoodsSpecStockNumById(@Param("id") Integer id, @Param("num") int num);

    Integer updateOrderById(@Param("orderCartList") OrderCartListBO orderCartListBO, @Param("stateId") Integer stateId);
}
