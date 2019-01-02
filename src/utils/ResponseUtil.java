package utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/14 0014.
 */
public class ResponseUtil {
    public static void response(HttpServletResponse response, Map jsonMap){
        String jsonString = JSON.toJSONString(jsonMap);
        //将数据返回
        response.setCharacterEncoding("UTF-8");
        try {
            response.flushBuffer();
            response.getWriter().write(jsonString);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
