package fast.app;

import fast.main.util.Super;

import java.sql.Connection;
import java.util.Map;

public class dzmbDemo  extends Super {
    private static Connection connection = null;
    private Map<String, Object> user = null;

    public String init(Map<String, Object> rmap) {
        try {
            // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
            initMap(rmap);
            user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");

            return "xtgl/dzmbDemo";
        } catch (Exception e) {
            e.printStackTrace();
            return "xtgl/dzmbDemo";
        }
    }
}