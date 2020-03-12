package fast.main.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
* @ClassNmme:      JDBCUtils
* @Author:         Mollen
* @CreateDate:     2018-09-07 16:44:42
* @Description:
*          Druid连接池工具类
*/
public class JDBCUtils {
    /**
     * 1.定义成员变量
     */
    private static DataSource ds;


    /**
     * 2.加载数据源
     */
    static {
        try {
            Properties pro = new Properties();
            InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("conf/jdbc.properties");
            pro.load(is);
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 3.获取连接池对象
     * @return
     */
    public static DataSource getDataSource() {
        return ds;
    }


    /**
     * 4.获取数据库连接对象
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}
