package fast.main.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;


public class DruidUtil {
	
	private static Properties p;
	private static DataSource dataSource;
	
	static{
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = loader.getResourceAsStream("conf/jdbc.properties");
			//InputStream inputStream = DruidUtil.class.getClassLoader().getResourceAsStream("conf/jdbc.properties");
			p = new Properties();
			p.load(inputStream);
			p.setProperty("driver", p.getProperty("jdbc.driver"));
			p.setProperty("url", p.getProperty("jdbc.url"));
			p.setProperty("username", p.getProperty("jdbc.username"));
			p.setProperty("password", p.getProperty("jdbc.password"));
			//通过工厂类获取DataSource对象
			dataSource = DruidDataSourceFactory.createDataSource(p);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static Connection getConnection(){
		try {
			return dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DataSource getDataSource(){
		return dataSource;
	}
 
	public static void close(Connection conn,Statement state,ResultSet result){
		
			try {
				if(result != null){
					result.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					if(conn != null){
						conn.close();
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}finally{
					try {
						if(state != null){
							state.close();
						}
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
			
		}
	
	}
	
	


}
