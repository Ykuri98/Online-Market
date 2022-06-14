package controller.user;

import com.google.gson.Gson;
import model.Result;
import model.vo.goods.GoodsDetailVO;
import model.vo.goods.GoodsTypeVO;
import service.GoodsService;
import service.GoodsServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/mall/index/*")
public class UserIndexServlet extends HttpServlet {

    GoodsService goodsService = new GoodsServiceImpl();

    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取URI后缀，根据后缀执行功能
        String requestURI = request.getRequestURI();
        String op = requestURI.replace(request.getContextPath() + "/api/mall/index/", "");

        if ("getType".equals(op)){
            getType(request,response);
        }
    }


    /**
     * 获取所有商品品类
     * @param request
     * @param response
     * @throws IOException
     */
    private void getType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<GoodsTypeVO> goodsTypeVOList = goodsService.getType();

        Result result = new Result();
        result.setCode(0);
        result.setData(goodsTypeVOList);
        response.getWriter().println(gson.toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
