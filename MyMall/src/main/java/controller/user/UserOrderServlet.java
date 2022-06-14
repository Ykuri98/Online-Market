package controller.user;

import com.google.gson.Gson;
import model.Result;
import model.bo.admin.AdminLoginBO;
import model.bo.order.OrderAddOrderBO;
import model.bo.order.OrderSendCommentBO;
import model.bo.order.OrderSettleAccountsBO;
import model.vo.order.OrderStateAndTokenVO;
import service.GoodsService;
import service.GoodsServiceImpl;
import service.OrderService;
import service.OrderServiceImpl;
import utils.HttpRequestUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/mall/order/*")
public class UserOrderServlet extends HttpServlet {
    OrderService orderService = new OrderServiceImpl();

    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取URI后缀，根据后缀执行功能
        String requestURI = request.getRequestURI();
        String op = requestURI.replace(request.getContextPath() + "/api/mall/order/", "");

        if ("getOrderByState".equals(op)){
            getOrderByState(request,response);
        }
        if ("pay".equals(op)){
            pay(request,response);
        }
        if ("confirmReceive".equals(op)){
            confirmReceive(request,response);
        }
        if ("deleteOrder".equals(op)){
            deleteOrder(request,response);
        }
    }

    private void deleteOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Integer deleteResult = orderService.deleteOrder(id);

        Result result = new Result();
        if (deleteResult == 0){
            result.setCode(10000);
            result.setMessage("删除失败");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    private void confirmReceive(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Integer confirmResult = orderService.confirmReceive(id);

        Result result = new Result();
        if (confirmResult == 0){
            result.setCode(10000);
            result.setMessage("确认收货失败");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    private void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Integer payResult = orderService.pay(id);

        Result result = new Result();
        if (payResult == 0){
            result.setCode(10000);
            result.setMessage("付款失败");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    private void getOrderByState(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String state = request.getParameter("state");
        String token = request.getParameter("token");

        List<OrderStateAndTokenVO> orderByStateAndTokenList = orderService.getOrderByStateAndToken(state, token);

        Result result = new Result();
        result.setCode(0);
        result.setData(orderByStateAndTokenList);
        response.getWriter().println(gson.toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取URI后缀，根据后缀执行功能
        String requestURI = request.getRequestURI();
        String op = requestURI.replace(request.getContextPath() + "/api/mall/order/", "");

        if ("settleAccounts".equals(op)){
            settleAccounts(request,response);
        }
        if ("sendComment".equals(op)){
            sendComment(request,response);
        }
        if ("addOrder".equals(op)){
            addOrder(request,response);
        }
    }

    private void addOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        // 将请求体中的json字符串转为OrderSettleAccountsBO对象
        OrderAddOrderBO orderAddOrderBO = gson.fromJson(requestBody, OrderAddOrderBO.class);
        Integer addResult = orderService.addOrder(orderAddOrderBO);

        Result result = new Result();
        if (addResult == 0){
            result.setCode(10000);
            result.setMessage("购买失败，该商品没货了");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    private void sendComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        // 将请求体中的json字符串转为OrderSettleAccountsBO对象
        OrderSendCommentBO orderSendCommentBO = gson.fromJson(requestBody, OrderSendCommentBO.class);
        Integer settleResult = orderService.sendComment(orderSendCommentBO);

        Result result = new Result();
        if (settleResult == 0){
            result.setCode(10000);
            result.setMessage("评价失败");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    private void settleAccounts(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        // 将请求体中的json字符串转为OrderSettleAccountsBO对象
        OrderSettleAccountsBO orderSettleAccountsBO = gson.fromJson(requestBody, OrderSettleAccountsBO.class);
        Integer settleResult = orderService.settleAccounts(orderSettleAccountsBO);

        Result result = new Result();
        // 返回0状态码，下单失败
        if (settleResult == 0){
            result.setCode(10000);
            result.setMessage("下单失败，存货不足");
            response.getWriter().println(gson.toJson(result));
            return;
        }
        // 添加成功
        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }
}
