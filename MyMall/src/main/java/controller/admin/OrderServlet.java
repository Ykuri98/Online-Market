package controller.admin;

import com.google.gson.Gson;
import model.Result;
import model.bo.order.OrderChangeBO;
import model.bo.order.OrdersByPageBO;
import model.vo.order.OrdersByPageVO;
import model.vo.order.OrdersUpdateGetVO;
import service.OrderService;
import service.OrderServiceImpl;
import utils.HttpRequestUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/api/admin/order/*")
public class OrderServlet extends HttpServlet {

    OrderService orderService = new OrderServiceImpl();

    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String op = requestURI.replace(request.getContextPath() + "/api/admin/order/", "");

        if ("order".equals(op)){
            order(request,response);
        }
        if ("deleteOrder".equals(op)){
            deleteOrder(request,response);
        }
    }

    /**
     * 删除订单
     * @param request
     * @param response
     * @throws IOException
     */
    private void deleteOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Integer deleteResult = orderService.deleteOrder(id);

        Result result = new Result();
        if (deleteResult == 0){
            result.setCode(10000);
            result.setMessage("删除失败");
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 查询订单
     * @param request
     * @param response
     * @throws IOException
     */
    private void order(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        OrdersUpdateGetVO orderInfo = orderService.getOrderInfo(id);

        Result result = new Result();
        result.setCode(0);
        result.setData(orderInfo);
        response.getWriter().println(gson.toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String op = requestURI.replace(request.getContextPath() + "/api/admin/order/", "");

        if ("ordersByPage".equals(op)){
            ordersByPage(request,response);
        }
        if ("changeOrder".equals(op)){
            changeOrder(request,response);
        }
    }

    /**
     * 修改订单
     * @param request
     * @param response
     * @throws IOException
     */
    private void changeOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);
        OrderChangeBO orderChangeBO = gson.fromJson(requestBody, OrderChangeBO.class);
        Integer updateResult = orderService.orderChange(orderChangeBO);

        Result result = new Result();
        if (updateResult == 0){
            result.setCode(10000);
            result.setMessage("修改失败");
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 分页显示订单
     * @param request
     * @param response
     * @throws IOException
     */
    private void ordersByPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);
        OrdersByPageBO ordersByPageBO = gson.fromJson(requestBody, OrdersByPageBO.class);
        OrdersByPageVO ordersByPageVO = orderService.ordersByPage(ordersByPageBO);

        Result result = new Result();
        result.setCode(0);
        result.setData(ordersByPageVO);
        response.getWriter().println(gson.toJson(result));
    }
}
