package filter;

import com.google.gson.Gson;
import model.Result;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yzw
 */

@WebFilter("/api/admin/*")
public class AdminFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpServletRequest request = (HttpServletRequest) req;

        // 将请求报文和响应报文的字符集都设为utf-8
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        // 允许哪些来源的主机访问当前服务器(需更改)
        response.setHeader("Access-Control-Allow-Origin", "http://192.168.13.40:8085");
        // 访问服务时允许使用的方法
        response.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,PUT,DELETE");
        // 允许携带的头
        response.setHeader("Access-Control-Allow-Headers","x-requested-with,Authorization,Content-Type");
        // 是否允许携带cookie的凭证
        response.setHeader("Access-Control-Allow-Credentials","true");


        String requestURI = request.getRequestURI();
        String uri = requestURI.replace(request.getContextPath(), "");
        String method = request.getMethod();
        if (!"OPTIONS".equals(method)){
            if(!"/api/admin/admin/login".equals(uri)){
                // 如果请求的地址不是/api/admin/admin/login那么表示的是需要验证登录状态
                String username = (String)request.getSession().getAttribute("username");
                if(username == null){
                    // 需要登录后才可以访问，但是此时没有登录
                    Result result = new Result();
                    result.setMessage("当前接口仅允许登录后才可以访问");
                    response.getWriter().println(new Gson().toJson(result));
                    return;
                }
            }
        }

        chain.doFilter(req, resp);
    }
}
