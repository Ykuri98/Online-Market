package service;

import dao.GoodsDao;
import dao.OrderDao;
import model.bo.order.*;
import model.po.Goods;
import model.po.GoodsSpec;
import model.po.Order;
import model.po.User;
import model.vo.order.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import utils.MybatisUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yzw
 * @since 2022/06/10
 */

public class OrderServiceImpl implements OrderService{
    @Override
    public OrdersByPageVO ordersByPage(OrdersByPageBO ordersByPageBO) {
        // 获得SqlSession和orderDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);

        Order order = new Order();
        if (!StringUtils.isEmpty(ordersByPageBO.getId())){
            order.setId(Integer.parseInt(ordersByPageBO.getId()));
        }
        order.setAddress(ordersByPageBO.getAddress());
        order.setGoods(ordersByPageBO.getGoods());
        order.setNickname(ordersByPageBO.getName());
        order.setStateId(ordersByPageBO.getState());
        Integer currentPage = ordersByPageBO.getCurrentPage();
        Integer pagesize = ordersByPageBO.getPagesize();
        String moneyHighLimit = ordersByPageBO.getMoneyLimit1();
        String moneyLowLimit = ordersByPageBO.getMoneyLimit2();
        Integer currentLine = (currentPage - 1) * pagesize;

        Integer total = orderDao.queryTotal(order);
        List<OrdersVO> ordersVOList = orderDao.queryOrdersWithLimit(order, currentLine, pagesize, moneyHighLimit, moneyLowLimit);

        OrdersByPageVO ordersByPageVO = new OrdersByPageVO();
        ordersByPageVO.setTotal(total);
        ordersByPageVO.setOrders(ordersVOList);

        sqlSession.commit();
        sqlSession.close();
        return ordersByPageVO;
    }

    @Override
    public OrdersUpdateGetVO getOrderInfo(String id) {
        // 获得SqlSession和orderDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);

        // 获得ordersUpdateGetVO对象
        OrdersUpdateGetVO ordersUpdateGetVO = orderDao.queryOrdersById(Integer.parseInt(id));

        // 设置状态列表
        List<OrderStatesVO> states = new ArrayList<>();
        OrderStatesVO state0 = new OrderStatesVO();
        state0.setId(0);
        state0.setName("未付款");
        states.add(state0);
        OrderStatesVO state1 = new OrderStatesVO();
        state1.setId(1);
        state1.setName("未发货");
        states.add(state1);
        OrderStatesVO state2 = new OrderStatesVO();
        state2.setId(2);
        state2.setName("已发货");
        states.add(state2);
        OrderStatesVO state3 = new OrderStatesVO();
        state3.setId(3);
        state3.setName("已完成");
        states.add(state3);
        ordersUpdateGetVO.setStates(states);

        // 获得
        OrderCurSpecVO curSpec = ordersUpdateGetVO.getCurSpec();
        Integer specId = curSpec.getId();
        List<OrderSpecVO> orderSpecVOList = orderDao.queryGoodsSpecById(specId);
        ordersUpdateGetVO.setSpec(orderSpecVOList);

        sqlSession.commit();
        sqlSession.close();
        return ordersUpdateGetVO;
    }

    @Override
    public Integer orderChange(OrderChangeBO orderChangeBO) {
        // 获得SqlSession和orderDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);

        Order order = new Order();
        order.setId(Integer.parseInt(orderChangeBO.getId()));
        order.setStateId(orderChangeBO.getState());
        order.setNumber(orderChangeBO.getNum());
        order.setSpecId(orderChangeBO.getSpec());

        Integer updateResult = orderDao.updateOrder(order);

        sqlSession.commit();
        sqlSession.close();
        return updateResult;
    }

    @Override
    public Integer deleteOrder(String id) {
        // 获得SqlSession和orderDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);

        // 删除订单
        Integer deleteResult = orderDao.deleteOrder(Integer.parseInt(id));

        sqlSession.commit();
        sqlSession.close();
        return deleteResult;
    }

    @Override
    public List<OrderStateAndTokenVO> getOrderByStateAndToken(String state, String token) {
        // 获得SqlSession和orderDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);

        List<OrderStateAndTokenVO> orderStateAndTokenVOList = orderDao.queryOrdersByStateAndNickname(Integer.parseInt(state), token);
        for (OrderStateAndTokenVO orderStateAndTokenVO : orderStateAndTokenVOList) {
            Integer goodsDetailId = orderStateAndTokenVO.getGoods().getGoodsDetailId();
            Integer specId = orderStateAndTokenVO.getGoods().getId();
            OrderStateAndTokenVO imgAndName = orderDao.queryGoodsById(goodsDetailId);
            OrderStateAndTokenVO specAndUnitPrice = orderDao.queryGoodsSpecBySpecId(specId);
            orderStateAndTokenVO.setImg("http://192.168.13.40:8084/" + imgAndName.getGoods().getImg());
            orderStateAndTokenVO.setName(imgAndName.getGoods().getName());
            orderStateAndTokenVO.setSpec(specAndUnitPrice.getGoods().getSpec());
            orderStateAndTokenVO.setUnitPrice(specAndUnitPrice.getGoods().getUnitPrice());
            orderStateAndTokenVO.setCreatetime(orderStateAndTokenVO.getCreatetime().substring(0,19));
        }

        sqlSession.commit();
        sqlSession.close();
        return orderStateAndTokenVOList;
    }

    @Override
    public Integer settleAccounts(OrderSettleAccountsBO orderSettleAccountsBO) {
        // 获得SqlSession和orderDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        Integer updateResults = 0;

        for (OrderCartListBO orderCartListBO : orderSettleAccountsBO.getCartList()) {
            // 获取订单id和订单商品数量
            Integer id = orderCartListBO.getId();
            Integer orderGoodsNum = orderCartListBO.getGoodsNum();

            // 根据订单id获取商品规格id
            OrdersUpdateGetVO ordersUpdateGetVO = orderDao.queryOrdersById(id);
            Integer specId = ordersUpdateGetVO.getSpecId();

            // 根据商品规格id查询该规格的商品数量
            OrderStateAndTokenVO orderStateAndTokenVO = orderDao.queryGoodsSpecBySpecId(specId);
            Integer goodsNum = orderStateAndTokenVO.getGoodsNum();

            // 如果订单商品数量大于规格商品数量，则下单失败
            if (orderGoodsNum > goodsNum){
                sqlSession.commit();
                sqlSession.close();
                return 0;
            }

            // 更新状态
            Integer updateResult = orderDao.updateOrderById(orderCartListBO,1);

            // 更新商品规格信息
            orderDao.updateGoodsSpecStockNumById(specId, goodsNum - orderGoodsNum);

            updateResults += updateResult;
        }
        sqlSession.commit();
        sqlSession.close();
        return updateResults;
    }

    @Override
    public Integer pay(String id) {
        // 获得SqlSession和orderDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);

        // bug！
        Integer updateResult = orderDao.updateOrderStateById(Integer.parseInt(id),1);

        sqlSession.commit();
        sqlSession.close();
        return updateResult;
    }

    @Override
    public Integer confirmReceive(String id) {
        // 获得SqlSession和orderDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);

        Integer updateResult = orderDao.updateOrderStateById(Integer.parseInt(id),3);

        sqlSession.commit();
        sqlSession.close();
        return updateResult;
    }

    @Override
    public Integer sendComment(OrderSendCommentBO orderSendCommentBO) {
        // 获得SqlSession和orderDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);

        Integer updateResult = orderDao.updateOrderCommentById(orderSendCommentBO);

        sqlSession.commit();
        sqlSession.close();
        return updateResult;
    }

    @Override
    public Integer addOrder(OrderAddOrderBO orderAddOrderBO) {
        // 获得SqlSession和orderDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);

        // 获得用户名,商品规格id,购买数量,订单状态
        String token = orderAddOrderBO.getToken();
        Integer specId = orderAddOrderBO.getGoodsDetailId();
        Integer num = orderAddOrderBO.getNum();
        Integer state = orderAddOrderBO.getState();

        // 根据昵称查询id、收件人、地址和电话
        User user = orderDao.queryUserByNickname(token);
        Integer userId = user.getId();
        String receiver = user.getRecipient();
        String address = user.getAddress();
        String phone = user.getPhone();

        // 根据商品规格id获得商品规格名称，商品id和规格数量
        OrderStateAndTokenVO orderStateAndTokenVO = orderDao.queryGoodsSpecBySpecId(specId);
        String spec = orderStateAndTokenVO.getGoods().getSpec();
        Integer goodsId = orderStateAndTokenVO.getGoods().getGoodsDetailId();
        Integer goodsNum = orderStateAndTokenVO.getGoodsNum();

        // 当订单状态为已付款时，需要考虑购买数量
        // 如果当前规格数量小于购买数量，则购买失败
        if (state == 1){
            int goodsNumAfterBuy = goodsNum - num;
            if (goodsNumAfterBuy < 0){
                sqlSession.commit();
                sqlSession.close();
                return 0;
            }

            // 更新对应规格的数量
            Integer updateResult = orderDao.updateGoodsSpecStockNumById(specId, goodsNumAfterBuy);
            if (updateResult == 0){
                sqlSession.commit();
                sqlSession.close();
                return 0;
            }
        }

        // 根据商品id获取商品名称
        String goods = orderDao.queryGoodsNameById(goodsId);

        // 获取当前时间
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sf.format(date);

        // 插入新订单
        Integer insertResult = orderDao.insertOrder(orderAddOrderBO,userId,receiver,address,phone,goods,spec,specId,createTime,goodsId);

        sqlSession.commit();
        sqlSession.close();
        return insertResult;
    }
}
