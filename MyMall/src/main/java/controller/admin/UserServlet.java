package controller.admin;

import com.google.gson.Gson;
import model.Result;
import model.vo.user.UserInfoVO;
import service.UserService;
import service.UserServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/admin/user/*")
public class UserServlet extends HttpServlet {
    UserService userService = new UserServiceImpl();

    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取URI后缀，根据后缀执行功能
        String requestURI = request.getRequestURI();
        String op = requestURI.replace(request.getContextPath() + "/api/admin/user/", "");

        if ("allUser".equals(op)){
            allUser(request,response);
        }
        if ("deleteUser".equals(op)){
            deleteUser(request,response);
        }
        if ("searchUser".equals(op)){
            searchUser(request,response);
        }
    }

    /**
     * 查找单个用户
     * @param request
     * @param response
     * @throws IOException
     */
    private void searchUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nickname = request.getParameter("word");
        List<UserInfoVO> searchResult = userService.searchUser(nickname);

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
     * 删除用户
     * @param request
     * @param response
     * @throws IOException
     */
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Integer isDeleted = userService.deleteUser(id);

        Result result = new Result();
        if (isDeleted == 0){
            result.setCode(10000);
            result.setMessage("删除错误，该用户已被删除！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 获得所有用户
     * @param request
     * @param response
     * @throws IOException
     */
    private void allUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<UserInfoVO> userInfoVOList = userService.allUser();

        Result result = new Result();
        result.setCode(0);
        result.setData(userInfoVOList);
        response.getWriter().println(gson.toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
