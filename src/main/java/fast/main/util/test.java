package fast.main.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class test {
	
	public static void main(String[] args) {

	       Connection conn = null;
	       PreparedStatement pstmt = null;

	       try{
	           //1.连接数据库
	           conn=JDBCUtils.getConnection();
	           //2.定义sql语句
	           String sql = "INSERT INTO student VALUES(?,?)";
	           //3.执行sql
	           pstmt = conn.prepareCall(sql);
	           //4.解释参数
	           pstmt.setString(1,"张飞");
	           pstmt.setInt(2,28);
	           //5.执行结果（影响行数）
	           int count = pstmt.executeUpdate();
	           //打印受影响行数
	           System.out.println(count+"行受影响...");

	       } catch (SQLException e) {
	           e.printStackTrace();
	       }finally {
	           //释放资源
	          // JDBCUtils.close(pstmt,conn);
	       }
	   }
	
}
