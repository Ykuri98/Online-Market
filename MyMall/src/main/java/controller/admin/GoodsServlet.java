package controller.admin;

import com.google.gson.Gson;
import model.Result;
import model.bo.goods.*;
import model.vo.goods.*;
import service.GoodsService;
import service.GoodsServiceImpl;
import utils.FileUploadUtils;
import utils.HttpRequestUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/admin/goods/*")
public class GoodsServlet extends HttpServlet {

    GoodsService goodsService = new GoodsServiceImpl();

    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取URI后缀，根据后缀执行功能
        String requestURI = request.getRequestURI();
        String op = requestURI.replace(request.getContextPath() + "/api/admin/goods/", "");

        if ("getType".equals(op)){
            getType(request,response);
        }
        if ("getGoodsByType".equals(op)){
            getGoodsByType(request,response);
        }
        if ("deleteGoods".equals(op)){
            deleteGoods(request,response);
        }
        if ("getGoodsInfo".equals(op)){
            getGoodsInfo(request,response);
        }
        if ("noReplyMsg".equals(op)){
            noReplyMsg(request,response);
        }
        if ("repliedMsg".equals(op)){
            repliedMsg(request,response);
        }
        if ("deleteType".equals(op)){
            deleteType(request,response);
        }
    }

    private void deleteType(HttpServletRequest request, HttpServletResponse response) {
        String typeId = request.getParameter("typeId");
        List<GoodsDetailVO> GoodsDetailVOList = goodsService.getGoodsByType(typeId);

        for (GoodsDetailVO goodsDetailVO : GoodsDetailVOList) {
            Integer deleteGoodsRes = goodsService.deleteGoodsById(String.valueOf(goodsDetailVO.getId()));
        }
        goodsService.deleteGoodsType(typeId);
    }

    /**
     * 返回已回复的留言
     * @param request
     * @param response
     * @throws IOException
     */
    private void repliedMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<GoodsRepliedMsgVO> repliedMsg = goodsService.getRepliedMsg();

        Result result = new Result();
        result.setCode(0);
        result.setData(repliedMsg);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 返回未回复的留言
     * @param request
     * @param response
     * @throws IOException
     */
    private void noReplyMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<GoodsNoReplyMsgVO> noReplyMsg = goodsService.getNoReplyMsg();

        Result result = new Result();
        result.setCode(0);
        result.setData(noReplyMsg);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 获取商品信息
     * @param request
     * @param response
     * @throws IOException
     */
    private void getGoodsInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        GoodsInfoVO goodsInfo = goodsService.getGoodsInfo(id);

        Result result = new Result();
        result.setCode(0);
        result.setData(goodsInfo);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 删除商品信息
     * @param request
     * @param response
     * @throws IOException
     */
    private void deleteGoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Integer deleteResult = goodsService.deleteGoodsById(id);

        Result result = new Result();
        if (deleteResult == 0){
            result.setCode(10000);
            result.setMessage("删除失败，商品不存在");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
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
        // 获取URI后缀，根据后缀执行功能
        String requestURI = request.getRequestURI();
        String op = requestURI.replace(request.getContextPath() + "/api/admin/goods/", "");

        if ("addType".equals(op)){
            addType(request,response);
        }
        if ("addGoods".equals(op)){
            addGoods(request,response);
        }
        if ("updateGoods".equals(op)){
            updateGoods(request,response);
        }
        if ("imgUpload".equals(op)){
            imgUpload(request,response);
        }
        if ("addSpec".equals(op)){
            addSpec(request,response);
        }
        if ("reply".equals(op)){
            reply(request,response);
        }
        // deleteSpec
    }

    /**
     * 回复留言
     * @param request
     * @param response
     * @throws IOException
     */
    private void reply(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        GoodsReplyMsgBO goodsReplyMsgBO = gson.fromJson(requestBody, GoodsReplyMsgBO.class);
        Integer replyResult = goodsService.replyMsg(goodsReplyMsgBO);

        Result result = new Result();
        if (replyResult == 0){
            result.setCode(10000);
            result.setMessage("回复失败！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 添加商品规格
     * @param request
     * @param response
     * @throws IOException
     */
    private void addSpec(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        GoodsAddSpecBO goodsAddSpecBO = gson.fromJson(requestBody, GoodsAddSpecBO.class);

        Result result = new Result();

        // 数据校验
        try{
            Integer stockNum = Integer.parseInt(goodsAddSpecBO.getStockNum());
            Double unitPrice = Double.parseDouble(goodsAddSpecBO.getUnitPrice());
        }catch (NumberFormatException e){
            result.setCode(10000);
            result.setMessage("该规格不合法！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        GoodsAddSpecVO goodsAddSpecVO = goodsService.addSpec(goodsAddSpecBO);

        // 返回null，说明该规格已存在，返回错误代码
        if (goodsAddSpecVO == null){
            result.setCode(10000);
            result.setMessage("该规格已存在！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        result.setData(goodsAddSpecVO);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 文件上传
     * @param request
     * @param response
     * @throws IOException
     */
    private void imgUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> map = FileUploadUtils.parseRequest(request);
        String path = map.get("file");

        Result result = new Result();
        result.setCode(0);
        result.setData(path);
        response.getWriter().println(gson.toJson(result));

    }

    /**
     * 更新商品信息
     * @param request
     * @param response
     * @throws IOException
     */
    private void updateGoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        GoodsInfoBO goodsInfoBO = gson.fromJson(requestBody, GoodsInfoBO.class);
        Integer updateResult = goodsService.updateGoods(goodsInfoBO);

        Result result = new Result();
        if (updateResult != 1){
            result.setCode(10000);
            result.setMessage("更新失败！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 添加商品信息
     * @param request
     * @param response
     * @throws IOException
     */
    private void addGoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        // 将请求体中的json字符串转为GoodsDetailBO对象
        GoodsDetailBO goodsDetailBO = gson.fromJson(requestBody, GoodsDetailBO.class);

        Result result = new Result();
        // 验证输入是否合法
        try{
            for (GoodsSpecBO goodsSpecBO : goodsDetailBO.getSpecList()) {
                Integer stockNum = Integer.parseInt(goodsSpecBO.getStockNum());
                Double unitPrice = Double.parseDouble(goodsSpecBO.getUnitPrice());
            }
        }catch (NumberFormatException e){
            result.setCode(10000);
            result.setMessage("添加失败，规格输入不合法");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        Integer addResult = goodsService.addGoods(goodsDetailBO);

        if (addResult == 0){
            result.setCode(10000);
            result.setMessage("添加失败，已有重复商品");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 添加商品品类
     * @param request
     * @param response
     * @throws IOException
     */
    private void addType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        // 将请求体中的json字符串转为GoodsTypeBO对象
        GoodsTypeBO goodsTypeBO = gson.fromJson(requestBody, GoodsTypeBO.class);
        Integer addResult = goodsService.addGoodsType(goodsTypeBO);

        Result result = new Result();
        // 判断addResult，如果为0，说明品目已存在，添加失败
        if (addResult == 0){
            result.setCode(10000);
            result.setMessage("添加失败，品目已存在！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }
}
