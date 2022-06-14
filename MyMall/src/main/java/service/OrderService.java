package service;

import model.bo.order.*;
import model.po.Order;
import model.vo.order.OrderStateAndTokenVO;
import model.vo.order.OrdersByPageVO;
import model.vo.order.OrdersUpdateGetVO;

import java.util.List;

public interface OrderService {
    /**
     * 根据前端返回的查询需求和分页需求，返回对应的订单
     * @param ordersByPageBO 前端json包装类
     * @return 数据库包装类OrdersByPageVO
     */
    OrdersByPageVO ordersByPage(OrdersByPageBO ordersByPageBO);

    /**
     * 根据订单id获取订单信息
     * @param id 订单id
     * @return 数据库包装类OrdersUpdateGetVO
     */
    OrdersUpdateGetVO getOrderInfo(String id);

    /**
     * 根据前端的数据修改订单信息
     * @param orderChangeBO 前端json包装类
     * @return 1代表修改成功，0代表修改失败
     */
    Integer orderChange(OrderChangeBO orderChangeBO);

    /**
     * 根据订单id删除订单
     * @param id 订单id
     * @return 1代表删除成功，0代表删除失败
     */
    Integer deleteOrder(String id);

    List<OrderStateAndTokenVO> getOrderByStateAndToken(String state, String token);

    Integer settleAccounts(OrderSettleAccountsBO orderSettleAccountsBO);

    Integer pay(String id);

    Integer confirmReceive(String id);

    Integer sendComment(OrderSendCommentBO orderSendCommentBO);

    Integer addOrder(OrderAddOrderBO orderAddOrderBO);

}
