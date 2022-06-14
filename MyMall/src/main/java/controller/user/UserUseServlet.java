package controller.user;

import com.google.gson.Gson;
import model.Result;
import model.bo.admin.AdminLoginBO;
import model.bo.user.UserLogoutClientBO;
import model.bo.user.UserSignupBO;
import model.bo.user.UserUpdatePwd;
import model.bo.user.UserUpdateUserDataBO;
import model.vo.user.UserInfoVO;
import model.vo.user.UserLoginVO;
import org.apache.commons.lang3.StringUtils;
import service.UserService;
import service.UserServiceImpl;
import utils.HttpRequestUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/mall/user/*")
public class UserUseServlet extends HttpServlet {
    UserService userService = new UserServiceImpl();

    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取URI后缀，根据后缀执行功能
        String requestURI = request.getRequestURI();
        String op = requestURI.replace(request.getContextPath() + "/api/mall/user/", "");

        if ("data".equals(op)){
            data(request,response);
        }
    }

    private void data(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getParameter("token");
        List<UserInfoVO> userInfoVOList = userService.searchUser(token);
        UserInfoVO userInfoVO = userInfoVOList.get(0);
        userInfoVO.setCode(0);

        Result result = new Result();
        result.setCode(0);
        result.setData(userInfoVO);
        response.getWriter().println(gson.toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取URI后缀，根据后缀执行功能
        String requestURI = request.getRequestURI();
        String op = requestURI.replace(request.getContextPath() + "/api/mall/user/", "");

        if("login".equals(op)){
            login(request,response);
        }
        if ("signup".equals(op)){
            signup(request,response);
        }
        if ("logoutClient".equals(op)){
            logoutClient(request,response);
        }
        if ("updateUserData".equals(op)){
            updateUserData(request,response);
        }
        if ("updatePwd".equals(op)){
            updatePwd(request,response);
        }
    }

    private void updatePwd(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        UserUpdatePwd userUpdatePwd = gson.fromJson(requestBody, UserUpdatePwd.class);

        Result result = new Result();

        // 数据校验，不能存在空的数据
        Integer id = userUpdatePwd.getId();
        String oldPwd = userUpdatePwd.getOldPwd();
        String newPwd = userUpdatePwd.getNewPwd();
        String confirmPwd = userUpdatePwd.getConfirmPwd();
        if (StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd) || StringUtils.isEmpty(confirmPwd)){
            result.setCode(10000);
            result.setMessage("不能存在空的字段！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        // 新密码需与确认密码相同
        if (!newPwd.equals(confirmPwd)){
            result.setCode(10000);
            result.setMessage("新密码与确认密码不相同！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        // 验证旧密码
        String password = userService.searchUserPwdById(id);
        if (!password.equals(oldPwd)){
            result.setCode(10000);
            result.setMessage("旧密码错误！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    private void updateUserData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        UserUpdateUserDataBO userUpdateUserDataBO = gson.fromJson(requestBody, UserUpdateUserDataBO.class);

        Result result = new Result();

        // 数据校验，不能存在空的数据
        String nickname = userUpdateUserDataBO.getNickname();
        String address = userUpdateUserDataBO.getAddress();
        String recipient = userUpdateUserDataBO.getRecipient();
        String phone = userUpdateUserDataBO.getPhone();
        if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(address) || StringUtils.isEmpty(recipient) || StringUtils.isEmpty(phone)){
            result.setCode(10000);
            result.setMessage("不能存在空的字段！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        Integer updateResult = userService.updateUserData(userUpdateUserDataBO);

        if (updateResult == 0){
            result.setCode(10000);
            result.setMessage("修改失败！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    private void logoutClient(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        UserLogoutClientBO userLogoutClientBO = gson.fromJson(requestBody,UserLogoutClientBO.class);

        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("token");

        Result result = new Result();
        if (!userLogoutClientBO.getToken().equals(token)){
            result.setCode(10000);
            result.setMessage("退出失败！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        result.setCode(0);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 用户注册
     * @param request
     * @param response
     * @throws IOException
     */
    private void signup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = HttpRequestUtil.getRequestBody(request);

        // 将请求体中的json字符串转为UserSignupBO对象
        UserSignupBO userSignupBO = gson.fromJson(requestBody, UserSignupBO.class);

        String nickname = userService.signup(userSignupBO);

        Result result = new Result();
        // 对查询获得值判空
        if (StringUtils.isEmpty(nickname)){
            result.setCode(10000);
            result.setMessage("注册失败，邮箱或昵称已有人使用！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        // 取得查询值，登录成功
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setToken(nickname);
        userLoginVO.setName(nickname);
        userLoginVO.setCode(0);

        HttpSession session = request.getSession();
        session.setAttribute("token",nickname);

        result.setCode(0);
        result.setData(userLoginVO);
        response.getWriter().println(gson.toJson(result));
    }

    /**
     * 用户登录
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
            result.setMessage("用户名或密码为空！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        // 获得查询值
        String nickname = userService.login(adminLoginBO);
        // 对查询获得值判空
        if (StringUtils.isEmpty(nickname)){
            result.setCode(10000);
            result.setMessage("用户名或密码错误！");
            response.getWriter().println(gson.toJson(result));
            return;
        }

        // 取得查询值，登录成功
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setToken(nickname);
        userLoginVO.setName(nickname);
        userLoginVO.setCode(0);

        //写入session
        request.getSession().setAttribute("token", nickname);

        result.setCode(0);
        result.setData(userLoginVO);
        response.getWriter().println(gson.toJson(result));
    }
}
