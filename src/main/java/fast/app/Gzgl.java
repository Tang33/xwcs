package fast.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.dbcp.BasicDataSource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import fast.main.util.ExcelRead;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;
import fast.main.util.ZipUtil;

public class Gzgl extends Super {
	private static Connection connection = null;
	private Map<String, Object> user = null;

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			Integer rows = this.getBs().queryCount("select * from XWCS_JGZT where zt='1' and NAME='数据加工' and isdelete='0'");
			 this.getRequest().setAttribute("JGZT_SJJG", rows);
			return "sssjgl/Gzgl";
		} catch (Exception e) {
			e.printStackTrace();
			return "sssjgl/Gzgl";
		}
	}

	
	
	public static Connection getConnection() {

		try {
			Properties properties = new Properties();
			// 使用ClassLoader加载properties配置文件生成对应的输入流
			InputStream in = JdbcConnectedPro.class.getClassLoader().getResourceAsStream("conf/jdbc.properties");
			// 使用properties对象加载输入流
			properties.load(in);
			// 获取key对应的value值
			String driver = properties.getProperty("jdbc.driver");
			String url = properties.getProperty("jdbc.url");
			String username = properties.getProperty("jdbc.username");
			String pwd = properties.getProperty("jdbc.password");
			// 创建dataSource
			BasicDataSource dataSource = new BasicDataSource();
			// 加载数据库驱动
			dataSource.setDriverClassName(driver);
			// 设置用户名和密码
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(pwd);
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

}
