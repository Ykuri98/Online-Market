package utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName FileUploadUtils
 * @Description: TODO
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2022/6/2 11:37
 * @Version V1.0
 **/
public class FileUploadUtils {

    public static Map<String, String> parseRequest(HttpServletRequest request){
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = request.getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        //下面这行代码其实是利用刚刚所做的配置，创建一个处理文件上传的处理器
        ServletFileUpload upload = new ServletFileUpload(factory);
        //可以解决上传的文件文件名中文乱码问题
        upload.setHeaderEncoding("utf-8");
        // bytes
//        upload.setFileSizeMax(1024);
        Map<String, String> params = new HashMap<>();
        try {
            //页面中提交的每一个input，都会被解析封装到一个FileItem对象中
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                //FileItem其实就是对于input的封装，那么可能是常规的表单数据，也可能是上传的文件
                if(item.isFormField()){
                    //是表单数据
                    processFormField(item, params);
                }else {
                    //上传的文件
                    processUploadeFile(item, params, request);
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * 处理上传的文件业务逻辑
     * 首先先获取一些文件相关的信息，然后将文件保存到服务器硬盘上面
     * @param item
     * @param params
     * @param request
     */
    private static void processUploadeFile(FileItem item, Map<String, String> params, HttpServletRequest request) {
        //input的name属性
        String fieldName = item.getFieldName();
        String fileName = item.getName();
        //对filename进行处理
        String uuid = UUID.randomUUID().toString();
        fileName = uuid + "-" + fileName;
        String contentType = item.getContentType();
        boolean isInMemory = item.isInMemory();
        long sizeInBytes = item.getSize();
//        System.out.println(fieldName + ":" + fileName + ":" + contentType + ":" + isInMemory + ":" + sizeInBytes);
        //string的hashcode源码为什么会采取31作为乘子
        int hashCode = fileName.hashCode();
        String hexString = Integer.toHexString(hashCode);
        char[] chars = hexString.toCharArray();
        String basePath = "image";
        for (char aChar : chars) {
            basePath = basePath + "/" + aChar;
        }
        String relativePath = basePath + "/" + fileName;
        String realPath = request.getServletContext().getRealPath(relativePath);
        File file = new File(realPath);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            item.write(file);
            //路径存储绝对硬盘路径还是存储一个相对路径？取决于接下来如何使用该数据？
            //我们保存到数据库中的路径，大概率情况下，是在下一次网络再次去请求访问该图片
            //如果路径存储的是一个绝对硬盘路径，后续依然不是特别容易访问到
            //  img src=/app/image/1.jpeg
            //http://localhost/app      /image/1.jpeg
            params.put(fieldName, relativePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理常规表单数据的业务逻辑
     * 只需要获取key和value值即可
     * @param item
     * @param params
     */
    private static void processFormField(FileItem item, Map<String, String> params) {
        //name的取值可能性  username password
        String name = item.getFieldName();
        String value = null;
        try {
            value = item.getString("UTF-8");
            params.put(name, value);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(name + ":" + value);
    }
}
