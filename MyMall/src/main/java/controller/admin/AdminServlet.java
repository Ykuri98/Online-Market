package controller.admin;

import com.google.gson.Gson;
import model.Result;
import model.bo.admin.AdminInfoBO;
import model.bo.admin.AdminLoginBO;
import model.bo.admin.AdminPwdBO;
import model.vo.admin.AdminInfoVO;
import model.vo.admin.AdminLoginVO;
import org.apache.commons.lang3.StringUtils;
import service.AdminService;
import service.AdminServiceImpl;
import utils.HttpRequestUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

/**
 * @author yzw
 */
@WebServlet("/api/admin/admin/*")
public class AdminServlet extends HttpServlet {

    AdminService adminService = new AdminServiceImpl();

    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取URI后缀，根据后缀执行功能
        String requestURI = request.getRequestURI();
        String op = requestURI.replace(request.getContextPath() + "/api/admin/admin/", "");

        if ("allAdmins".equals(op)){
            allAdmins(request,response);
        }
        if ("deleteAdmins".equals(op)){
            deleteAdmins(request,response);
        }
        if ("getAdminsInfo".equals(op)){
            getAdminsInfo(request,response);
        }
    }

    /**
     * 根据id查询Admin信息
     * @param request
     * @param response
     * @throws IOException
     */
    private void getAdminsInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取id
        String id = request.getParameter("id");
        AdminInfoBO adminInfoBO = new AdminInfoBO();
        adminInfoBO.setId(Integer.parseInt(id));
        AdminInfoVO adminInfoVO = adminService.getAdminInfo(adminInfoBO).get(0);

        Result result = new Result();
        // 返回0，表示获取成功
        result.setCode(0);
        result.setData(adminInfoVO);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 根据id删除Admin
     * @param request
     * @param response
     * @throws IOException
     */
    private void deleteAdmins(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取id
        String id = request.getParameter("id");
        Integer isDeleted = adminService.deleteAdmin(id);

        Result result = new Result();
        // isDeleted返回0表示删除失败
        if (isDeleted == 0){
            result.setCode(10000);
            result.setMessage("删除错误，该账户已被删除！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        // 返回1表示删除成功
        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 显示全部admin
     * @param request
     * @param response
     * @throws IOException
     */
    private void allAdmins(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<AdminInfoVO> adminInfoVOList = adminService.allAdmins();

        Result result = new Result();
        result.setCode(0);
        result.setData(adminInfoVOList);
        response.getWriter().println(gson.toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取URI后缀，根据后缀执行功能
        String requestURI = request.getRequestURI();
        String op = requestURI.replace(request.getContextPath() + "/api/admin/admin/", "");

        if ("login".equals(op)){
            login(request,response);
        }
        if ("addAdminss".equals(op)){
            addAdmin(request,response);
        }
        if ("updateAdminss".equals(op)){
            updateAdmin(request,response);
        }
        if ("getSearchAdmins".equals(op)){
            searchAdmins(request,response);
        }
        if ("changePwd".equals(op)){
            changePwd(request,response);
        }
    }

    /**
     * 更改密码
     * @param request
     * @param response
     * @throws IOException
     */
    private void changePwd(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        // 将请求体中的json字符串转为AdminPwdBO对象
        AdminPwdBO adminPwdBO = gson.fromJson(requestBody, AdminPwdBO.class);

        Result result = new Result();

        // 存在未填写的字段，修改失败
        if(StringUtils.isEmpty(adminPwdBO.getOldPwd()) || StringUtils.isEmpty(adminPwdBO.getNewPwd()) ||StringUtils.isEmpty(adminPwdBO.getConfirmPwd())){
            result.setCode(10000);
            result.setMessage("存在未填写字段！");
            response.getWriter().println(gson.toJson(result));
            return;
        }
        // 确认密码与新密码不一致，修改失败
        if (!adminPwdBO.getNewPwd().equals(adminPwdBO.getConfirmPwd())){
            result.setCode(10000);
            result.setMessage("新密码与确认密码不一致！");
            response.getWriter().println(gson.toJson(result));
            return;
        }
        Integer updateResult = adminService.updateAdminPwd(adminPwdBO);

        // 返回0状态码，说明旧密码与账户不匹配
        if (updateResult == 0){
            result.setCode(10000);
            result.setMessage("旧密码与账户不匹配！");
            response.getWriter().println(gson.toJson(result));
            return;
        }
        // 修改成功
        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 根据json数据查询Admin
     * @param request
     * @param response
     * @throws IOException
     */
    private void searchAdmins(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        // 将请求体中的json字符串转为AdminInfoBO对象
        AdminInfoBO adminInfoBO = gson.fromJson(requestBody, AdminInfoBO.class);
        List<AdminInfoVO> searchResult = adminService.getAdminInfo(adminInfoBO);

        Result result = new Result();
        // searchResult返回空，表示未找到
        if (searchResult == null){
            result.setCode(10000);
            result.setMessage("查找失败！");
            response.getWriter().println(gson.toJson(result));
            return;
        }
        // 查找成功
        result.setCode(0);
        result.setData(searchResult);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 根据json数据修改Admin
     * @param request
     * @param response
     * @throws IOException
     */
    private void updateAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        // 将请求体中的json字符串转为AdminInfoBO对象
        AdminInfoBO adminInfoBO = gson.fromJson(requestBody, AdminInfoBO.class);
        Integer updateResult = adminService.updateAdmin(adminInfoBO);

        Result result = new Result();
        // 返回0状态码，说明用户已存在，修改失败
        if (updateResult == 0){
            result.setCode(10000);
            result.setMessage("修改错误！");
            response.getWriter().println(gson.toJson(result));
            return;
        }
        // 修改成功
        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 根据json数据添加Admin
     * @param request
     * @param response
     * @throws IOException
     */
    private void addAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        // 将请求体中的json字符串转为AdminInfoBO对象
        AdminInfoBO adminInfoBO = gson.fromJson(requestBody, AdminInfoBO.class);
        Integer addResult = adminService.addAdmin(adminInfoBO);

        Result result = new Result();
        // 返回0状态码，说明用户已存在，添加失败
        if (addResult == 0){
            result.setCode(10000);
            result.setMessage("添加错误，已有相同账户！");
            response.getWriter().println(gson.toJson(result));
            return;
        }
        // 添加成功
        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 登录
     * @param request
     * @param response
     * @throws IOException
     */
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        // 将请求体中的json字符串转为AdminLoginBO对象
        AdminLoginBO adminLoginBO = gson.fromJson(requestBody, AdminLoginBO.class);

        Result result = new Result();
        // 对请求报文中的值判空
        if (StringUtils.isEmpty(adminLoginBO.getEmail()) || StringUtils.isEmpty(adminLoginBO.getPwd())){
            result.setCode(10000);
            result.setMessage("，用户名或密码为空！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        // 获得查询值
        String nickname = adminService.login(adminLoginBO);
        // 对查询获得值判空
        if (StringUtils.isEmpty(nickname)){
            result.setCode(10000);
            result.setMessage("，用户名或密码错误！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        // 取得查询值，登录成功
        AdminLoginVO adminLoginVO = new AdminLoginVO();
        adminLoginVO.setToken(nickname);
        adminLoginVO.setName(nickname);

        //写入session
        request.getSession().setAttribute("username", adminLoginBO.getEmail());

        result.setCode(0);
        result.setData(adminLoginVO);
        response.getWriter().println(gson.toJson(result));
    }
}
