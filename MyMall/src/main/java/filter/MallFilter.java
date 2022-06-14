package filter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/api/mall/*")
public class MallFilter implements Filter {
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

        chain.doFilter(req, resp);
    }
}
