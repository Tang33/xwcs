package fast.main.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 税务登记
 * 
 * @author Administrator
 *
 */
public class SwdjInsert implements Callable<Map<String, Integer>> {
	private int sjzwIndex = 0;
	private Connection connection = null;
	List<Map<String, String>> list = null;
	private int index = 0;

//	public void  connection(Connection connection) {
//		this.connection = connection;
//	}

	public void list(List<Map<String, String>> list) {
		this.list = list;
	}

	public synchronized void index(Integer index) {
		this.index = index;
	}

	@Override
	public Map<String, Integer> call() throws Exception {
		System.out.println("线程ID：" + Thread.currentThread().getId() + "=====开始！第" + index + "个任务");
		Map<String, Integer> result = new HashMap<String, Integer>();
		if (connection == null || !(connection.isClosed())) {
			connection = DruidUtil.getConnection();
		}

		try {

			connection.setAutoCommit(false);
			Statement cs = connection.createStatement();

			String tableName = "SWDJXX";
			Map<String, Map<String, String>> infoMap = getTableInfo(tableName);
			// 拼接插入SQL
			String keySql = "insert into " + tableName + " (";
			String valueSql = " values (";
			Map<Integer, String> insertMap = new HashMap<>();
			int in = 0;
			for (String comments : infoMap.keySet()) {
				in++;
				if (comments.equals("数据指纹")) {
					insertMap.put(in, comments);
				} else {
					insertMap.put(in, comments);
				}

				String columnName = infoMap.get(comments).get("COLUMN_NAME");
				keySql += columnName + ",";
				valueSql += "?,";
			}
			String sql = keySql.replaceAll(",$", ")") + valueSql.replaceAll(",$", ")");
			// 拼接更新SQL
			String keySql1 = "update " + tableName + " set ";
			Map<Integer, String> updateMap = new HashMap<>();
			int up = 0;
			for (String comments : infoMap.keySet()) {
				up++;
				updateMap.put(up, comments);
				String columnName = infoMap.get(comments).get("COLUMN_NAME");
				keySql1 += columnName + " = ?,";
			}
			String updatesql = keySql1.replaceAll(",$", "");

			PreparedStatement stmt = connection.prepareStatement(sql);
			PreparedStatement updatestmt = null;
			int num = 0;
			int upnum = 0;
			int renum = 0;
			int js = 0;
			int i = 0;
//			if(true) {
//				return result;
//			}
			lableA: for (Map<String, String> one : list) {
				js++;
				String sjzw = "";
				String nsrsbhQC = "";

				nsrsbhQC = one.get("社会信用代码（纳税人识别号）");
				String updatesql1 = updatesql + " where nsrsbh = '" + nsrsbhQC + "'";
				updatestmt = connection.prepareStatement(updatesql1);
				ResultSet rs1 = cs
						.executeQuery("select count(*) from " + tableName + " where nsrsbh = '" + nsrsbhQC + "'");
				if (rs1.next() && rs1.getInt(1) > 0) {
					// System.out.println("根据纳税人识别号重复更新数据！");
					for (int u = 1; u < 78; u++) {
						sy_util(u, one.get(updateMap.get(u)), updatestmt);
					}
					upnum++;
					i++;
					updatestmt.addBatch();

					// continue lableA;
				} else {// 以下是插入的
//					for (int s = 1; s < 78; s++) {
//						sjzw += sy_insert(s, insertMap.get(s), stmt, one);
//					}
//					String strId = StrToId(sjzw);
//					ResultSet rs = cs
//							.executeQuery("select count(*) from " + tableName + " where sjzw = '" + strId + "'");
//					if (rs.next() && rs.getInt(1) > 0) {
//						System.out.println("根据数据指纹去除重复数据！");
//						renum++;
//						continue lableA;
//					} else {
//						stmt.setString(sjzwIndex, strId);
//					}
					
					//不验证
					for (int s = 1; s < 78; s++) {
						sy_util(s, one.get(insertMap.get(s)), stmt);
					}
					
					num++;
					stmt.addBatch();
				} // if end
					// if(js%1000==0) {
					// 插入

			} // 循环end
			try {
				stmt.executeBatch();
				stmt.clearBatch();
				connection.commit();
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("导入异常：" + e.getMessage());
			}
			// 修改
			if (i > 0) {
				i = 0;
				try {
					updatestmt.executeBatch();
					updatestmt.clearBatch();
					connection.commit();
					updatestmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("导入异常：" + e.getMessage());
				}
			}
			// }
			// 修改剩下的数据
//			if(updatestmt!=null) {
//				try {
//					updatestmt.executeBatch();
//					connection.commit();
//					updatestmt.close();
//				} catch (Exception e) {
//					e.printStackTrace();
//					System.err.println("导入异常：" + e.getMessage());
//				}
//			}
			// 插入剩下的数据
//			try {
//				stmt.executeBatch();
//				connection.commit();
//				stmt.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.err.println("导入异常：" + e.getMessage());
//			}
			System.out.println("导入完成--" + num + "行");
			result.put("NUM", num);
			result.put("RENUM", renum);
			result.put("UPNUM", upnum);
			System.err.println("线程ID：" + Thread.currentThread().getId() + "=====【结束】第" + index + "个任务");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		} finally {
			if (connection != null) {
				try {
					connection.close();
					connection=null;
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 加工工具
	 * 
	 * @throws SQLException
	 */
	public String sy_util(Integer i, String value, PreparedStatement stmt) throws SQLException {

		if (value == null || value.equals("")) {
			stmt.setNull(i, Types.VARCHAR);
		} else {
			stmt.setString(i, value.replaceAll("[,|，]", "").equals("") ? "0" : value.replaceAll("[,|，]", ""));
		}
		return value;
	}

	/**
	 * 加工工具
	 * 
	 * @throws SQLException
	 */
	public String sy_insert(Integer i, String value, PreparedStatement stmt, Map<String, String> one)
			throws SQLException {

		String sj = one.get(value);
		if (value.equals("数据指纹")) {
			sjzwIndex = i;
			return value;
		} else if (sj == null || sj.equals("")) {
			stmt.setNull(i, Types.VARCHAR);
		} else {
			stmt.setString(i, sj.replaceAll("[,|，]", "").equals("") ? "0" : sj.replaceAll("[,|，]", ""));
		}
		return sj;
	}

	public String StrToId(String str) throws NoSuchAlgorithmException {
		MessageDigest arg16 = MessageDigest.getInstance("MD5");
		byte[] arg15 = arg16.digest(str.toString().getBytes());
		StringBuffer stringBuffer = new StringBuffer();
		byte[] arg8 = arg15;
		int arg9 = arg15.length;

		for (int arg10 = 0; arg10 < arg9; ++arg10) {
			byte b = arg8[arg10];
			int bt = b & 255;
			if (bt < 16) {
				stringBuffer.append(0);
			}

			stringBuffer.append(Integer.toHexString(bt));
		}

		return stringBuffer.toString();
	}

	public Map<String, Map<String, String>> getTableInfo(String tableName) {
		Map<String, Map<String, String>> map = new LinkedHashMap<String, Map<String, String>>();
		try {
			Statement cs = connection.createStatement();
			ResultSet rs = cs.executeQuery(
					"select a.*,b.DATA_TYPE from user_col_comments a,user_tab_columns b where a.table_name = b.table_name and a.table_name = upper('"
							+ tableName + "') and b.COLUMN_NAME = a.COLUMN_NAME");
			while (rs.next()) {
				String comments = rs.getString("COMMENTS");
				String column_name = rs.getString("COLUMN_NAME");
				String datatype = rs.getString("DATA_TYPE");
				Map<String, String> map1 = new LinkedHashMap<String, String>();
				map1.put("COLUMN_NAME", column_name);
				map1.put("DATA_TYPE", datatype);
				map.put(comments, map1);
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return map;
		}
	}

}
