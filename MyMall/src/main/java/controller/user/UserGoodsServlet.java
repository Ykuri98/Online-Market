package controller.user;

import com.google.gson.Gson;
import com.sun.deploy.net.HttpUtils;
import model.Result;
import model.bo.goods.GoodsAskGoodsMsgBO;
import model.bo.goods.GoodsReplyMsgBO;
import model.vo.goods.*;
import service.GoodsService;
import service.GoodsServiceImpl;
import utils.HttpRequestUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/mall/goods/*")
public class UserGoodsServlet extends HttpServlet {
    GoodsService goodsService = new GoodsServiceImpl();

    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取URI后缀，根据后缀执行功能
        String requestURI = request.getRequestURI();
        String op = requestURI.replace(request.getContextPath() + "/api/mall/goods/", "");

        if ("getGoodsByType".equals(op)){
            getGoodsByType(request,response);
        }
        else if ("searchGoods".equals(op)){
            searchGoods(request,response);
        }
        else if ("getGoodsInfo".equals(op)){
            getGoodsInfo(request,response);
        }
        else if ("getGoodsMsg".equals(op)){
            getGoodsMsg(request,response);
        }
        else if ("getGoodsComment".equals(op)){
            getGoodsComment(request,response);
        }
    }

    private void getGoodsComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String goodsId = request.getParameter("goodsId");
        GoodsCommentVO goodsCommentVO = goodsService.getGoodsComment(goodsId);

        Result result = new Result();
        result.setCode(0);
        result.setData(goodsCommentVO);
        response.getWriter().println(gson.toJson(result));
    }

    private void getGoodsMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        List<GoodsMsgGetByUserVO> goodsMsgGetByUserVOList = goodsService.getGoodsMsg(id);

        Result result = new Result();
        result.setCode(0);
        result.setData(goodsMsgGetByUserVOList);
        response.getWriter().println(gson.toJson(result));
    }

    private void getGoodsInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        GoodsInfoUserVO goodsInfo = goodsService.getGoodsInfoUser(id);

        Result result = new Result();
        result.setCode(0);
        result.setData(goodsInfo);
        response.getWriter().println(gson.toJson(result));
    }

    private void searchGoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String keyword = request.getParameter("keyword");
        List<GoodsDetailVO> goodsDetailVOList = goodsService.getGoodsByName(keyword);

        Result result = new Result();
        result.setCode(0);
        result.setData(goodsDetailVOList);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 根据商品品类获取该品类下所有商品
     * @param request
     * @param response
     * @throws IOException
     */
    private void getGoodsByType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String typeId = request.getParameter("typeId");
        List<GoodsDetailVO> goodsDetailVOList = goodsService.getGoodsByType(typeId);

        Result result = new Result();
        result.setCode(0);
        result.setData(goodsDetailVOList);
        response.getWriter().println(gson.toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取URI后缀，根据后缀执行功能
        String requestURI = request.getRequestURI();
        String op = requestURI.replace(request.getContextPath() + "/api/mall/goods/", "");

        if ("askGoodsMsg".equals(op)){
            askGoodsMsg(request,response);
        }

    }

    private void askGoodsMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        GoodsAskGoodsMsgBO goodsAskGoodsMsgBO = gson.fromJson(requestBody, GoodsAskGoodsMsgBO.class);
        Integer askResult = goodsService.askGoodsMsg(goodsAskGoodsMsgBO);

        Result result = new Result();
        if (askResult == 0){
            result.setCode(10000);
            result.setMessage("留言失败!");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }
}
