package utils;

import com.sun.deploy.net.HttpRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author yzw
 * @since 2022/06/09
 */

public class HttpRequestUtil {

    /**
     * 读取请求体
     * @param request
     * @return requestBody 请求体
     */
    public static String getRequestBody(HttpServletRequest request){
        ServletInputStream inputStream = null;
        String requestBody = null;
        try {
            inputStream = request.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,length);
            }
            requestBody = outputStream.toString("utf-8");

            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return requestBody;
    }
}
