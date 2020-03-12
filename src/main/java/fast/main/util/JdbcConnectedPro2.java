package fast.main.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.sql.PooledConnection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.datasource.pooled.PoolState;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.jdbc.datasource.DataSourceUtils;

import oracle.jdbc.OracleTypes;

public class JdbcConnectedPro2 {
	private static BasicDataSource dataSource = null;
	private static Connection connection = null;
	private static CallableStatement callableStatement = null;

	/**
	 * 创建connection对象
	 * 
	 * @return
	 */
	public static Connection getConnection() {

		try {
			Properties properties = new Properties();
			// 使用ClassLoader加载properties配置文件生成对应的输入流
			InputStream in = JdbcConnectedPro2.class.getClassLoader().getResourceAsStream("conf/jdbc.properties");
			// 使用properties对象加载输入流
			properties.load(in);
			// 获取key对应的value值
			String driver = properties.getProperty("jdbc.driver");
			String url = properties.getProperty("jdbc.url");
			String username = properties.getProperty("jdbc.username");
			String pwd = properties.getProperty("jdbc.password");
			// 创建dataSource
			dataSource = new BasicDataSource();
			// 加载数据库驱动
			dataSource.setDriverClassName(driver);
			// 设置用户名和密码
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(pwd);
			dataSource.setMaxActive(50);
			dataSource.setMinIdle(5);// 设置最小空闲连接数字
			dataSource.setMaxIdle(10);// 这只最大的空闲连接数
			dataSource.setMinEvictableIdleTimeMillis(1000);
			dataSource.setMaxWait(3000000);//设置超时等待时间为1000秒
			dataSource.setValidationQuery("SELECT 1 from dual");//设置连接测试语句
			dataSource.setTestWhileIdle(true);//设置是否开启连接测试
			dataSource.setTestOnBorrow(true);//设置返回连接开启有效性验证
			dataSource.setTestOnReturn(true);//设置空闲连接有效性验证
			
			
			dataSource.setRemoveAbandoned(true);//是否自动回收超时连接
			dataSource.setRemoveAbandonedTimeout(180000);//超时时间(以秒数为单位)
			connection = dataSource.getConnection();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	public static Connection getDatasource() {

		try {
			Properties properties = new Properties();
			// 使用ClassLoader加载properties配置文件生成对应的输入流
			InputStream in = JdbcConnectedPro2.class.getClassLoader().getResourceAsStream("conf/jdbc.properties");
			// 使用properties对象加载输入流
			properties.load(in);
			// 获取key对应的value值
			String driver = properties.getProperty("jdbc.driver");
			String url = properties.getProperty("jdbc.url");
			String username = properties.getProperty("jdbc.username");
			String pwd = properties.getProperty("jdbc.password");
			// 创建dataSource
			dataSource = new BasicDataSource();
			// 加载数据库驱动
			dataSource.setDriverClassName(driver);
			// 设置用户名和密码
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(pwd);
			dataSource.setInitialSize(10);// 初始化时创建10个链接
			dataSource.setMaxActive(200);
			dataSource.setMinIdle(1);// 设置最小空闲连接数字
			dataSource.setMaxIdle(10);// 这只最大的空闲连接数
			dataSource.setMinEvictableIdleTimeMillis(300000);
			dataSource.setTimeBetweenEvictionRunsMillis(60000);
			dataSource.setValidationQuery("select 1 from dual");
			dataSource.setTestWhileIdle(true);
			dataSource.setTestOnReturn(false);
			dataSource.setTestOnBorrow(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	public static Connection getConnection1() {
		Connection connection1 = null;
		try {
			if (dataSource != null && dataSource.getMaxActive() < 200) {
				connection1 = dataSource.getConnection();
			} else {
				getDatasource();
				connection1 = dataSource.getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection1;
	}

	// 关闭资源
	public static void close() {
		if (dataSource != null) {
			try {
				dataSource.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (null != callableStatement) {
			try {
				callableStatement.close();
				if (null != connection) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String insertAll(List<Map<String, String>> list, String drfs) {
		try {
			System.out.println("--------正在存储数据库:");
			connection = getConnection();
			connection.setAutoCommit(false);
			String sql = "insert into xwcs_gsdr_temp(id,nsrsbh,nsrmc, hy_mc, zse,zsxm_mc, yskm_mc, ysfpbl_mc,jd_mc, type,lr_sj,bl,HY,se,ZSPM,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,yzpzxh,sksx) VALUES (seq_xwcs_gsdr.nextval,?,?,?,to_number(?),?,?,?,?,'0',sysdate,?,?,to_number(?),?,?,?,?,?,?,?,?,?) ";
			PreparedStatement stmt = connection.prepareStatement(sql);
			int num = 0;
			int count = 0;
			int count1 = 0;
			System.out.println("--------sql:" + sql);
			for (int x = 0; x < list.size(); x++) {
				Map<String, String> map = list.get(x);
				count++;
				if (map == null || map.isEmpty()) {
					continue;
				}
				count1++;
				String zsxm_mc = "";
				String sksx = "";
				String zse = "";
				String yskm_mc = "";
				String ysfpbl_mc = "";
				String bl = "";
				String nsrsbh = "";
				String nsrmc = "";
				String hy_mc = "";
				String jd_mc = "";
				String hy = "";
				String qxj = "";
				String zspm = "";
				String zspmdm = "";
				String hydls = "";
				String hyzl = "";
				String yskmdms = "";
				String dzsphms = "";
				String djxhs = "";
				String yzpzxh = "";

				if (drfs.equals("0")) {
					zsxm_mc = map.get("征收项目");
					sksx = map.get("税款属性");
					zse = map.get("实缴金额");
					yskm_mc = map.get("预算科目");
					ysfpbl_mc = map.get("预算分配比例");
					bl = map.get("区县级比例");
					nsrsbh = map.get("社会信用代码（纳税人识别号）");
					nsrmc = map.get("纳税人名称");
					hy_mc = map.get("行业门类");
					jd_mc = map.get("街道乡镇");
					hy = map.get("行业");
					qxj = map.get("区县级");
					zspm = map.get("征收品目");
					zspmdm = map.get("征收品目代码");
					hydls = map.get("行业大类");
					hyzl = map.get("行业中类");
					yskmdms = map.get("预算科目代码");
					dzsphms = map.get("电子税票号码");
					djxhs = map.get("登记序号");
					yzpzxh = map.get("应征凭证序号");
				} else if (drfs.equals("1")) {
					zsxm_mc = map.get("征收项目");
					sksx = map.get("税款属性");
					zse = map.get("税额");
					yskm_mc = map.get("预算科目");
					ysfpbl_mc = map.get("预算分配比例");
					bl = map.get("区县级比例");
					nsrsbh = map.get("社会信用代码（纳税人识别号）");
					nsrmc = map.get("纳税人名称");
					hy_mc = map.get("行业门类");
					jd_mc = map.get("街道乡镇");
					hy = map.get("行业");
					qxj = map.get("区县级");
					zspm = map.get("征收品目");
					zspmdm = map.get("征收品目代码");
					hydls = map.get("行业大类");
					hyzl = map.get("行业中类");
					yskmdms = map.get("预算科目代码");
					dzsphms = map.get("电子税票号码");
					djxhs = map.get("登记序号");
					yzpzxh = map.get("应征凭证序号");
				} else if (drfs.equals("2")) {
					zsxm_mc = map.get("征收项目");
					sksx = map.get("税款属性");
					zse = map.get("应征税金额");
					yskm_mc = map.get("预算科目");
					ysfpbl_mc = map.get("预算分配比例");
					bl = map.get("区县级比例");
					nsrsbh = map.get("纳税人识别号");
					nsrmc = map.get("纳税人姓名");
					hy_mc = map.get("行业门类");
					jd_mc = map.get("街道乡镇");
					hy = map.get("行业");
					qxj = map.get("区县级");
					zspm = map.get("征收品目");
					zspmdm = map.get("征收品目代码");
					hydls = map.get("行业大类");
					hyzl = map.get("行业中类");
					yskmdms = map.get("预算科目代码");
					dzsphms = map.get("电子税票号码");
					djxhs = map.get("登记序号");
					yzpzxh = map.get("应征凭证序号");
				}
				if (drfs.equals("3")) {
					zsxm_mc = map.get("征收项目");
					sksx = map.get("税款属性");
					zse = map.get("实缴金额");
					yskm_mc = map.get("预算科目");
					ysfpbl_mc = map.get("预算分配比例");
					bl = map.get("区县级比例");
					nsrsbh = map.get("社会信用代码（纳税人识别号）");
					nsrmc = map.get("纳税人名称");
					hy_mc = map.get("行业门类");
					jd_mc = map.get("街道乡镇");
					hy = map.get("行业");
					qxj = map.get("区县级");
					zspm = map.get("征收品目");
					zspmdm = map.get("征收品目代码");
					hydls = map.get("行业大类");
					hyzl = map.get("行业中类");
					yskmdms = map.get("预算科目代码");
					dzsphms = map.get("电子税票号码");
					djxhs = map.get("登记序号");
					yzpzxh = map.get("应征凭证序号");
				}
				System.out.println(Double.parseDouble(zse.replaceAll("\"", "").replaceAll(",", "")));
				if (zse != null && !zse.trim().equals("") && !zse.trim().equals("null")
						&& Double.parseDouble(zse.replaceAll("\"", "").replaceAll(",", "")) != 0) {
					zse = zse.replaceAll(",", "");

				} else {
					continue;
				}
				if (drfs.equals("1")) {
					if (zse.indexOf("-") >= 0) {
						zse.replace("-", "");
					} else {
						zse = "-" + zse;
					}
				}

				zse = zse.replaceAll(",", "").replaceAll("\"", "");

				nsrsbh = nsrsbh == null ? "" : nsrsbh;
				nsrmc = nsrmc == null ? "" : nsrmc;
				hy = hy == null ? "" : hy;
				hy_mc = hy_mc == null ? "" : hy_mc;
				yskm_mc = yskm_mc == null ? "" : yskm_mc;
				zsxm_mc = zsxm_mc == null ? "" : zsxm_mc;
				ysfpbl_mc = ysfpbl_mc == null ? "" : ysfpbl_mc;
				jd_mc = jd_mc == null ? "" : jd_mc;
				sksx = sksx == null ? "" : sksx;
				zspm = zspm == null ? "" : zspm;
				zspmdm = zspmdm == null ? "" : zspmdm;
				hydls = hydls == null ? "" : hydls;
				hyzl = hyzl == null ? "" : hyzl;
				yskmdms = yskmdms == null ? "" : yskmdms;
				dzsphms = dzsphms == null ? "" : dzsphms;
				djxhs = djxhs == null ? "" : djxhs;
				yzpzxh = yzpzxh == null ? "" : yzpzxh;
				if (bl != null && !bl.trim().equals("") && !bl.trim().equals("null")) {
					bl = bl.replaceAll(",", "");
				} else {
					bl = "100";
				}
				bl = bl.replaceAll(",", "").replaceAll("\"", "");
				if (qxj != null && !qxj.trim().equals("") && !qxj.trim().equals("null")) {
					qxj = qxj.replaceAll(",", "");
				} else {
					qxj = "0";
				}
				qxj = qxj.replaceAll(",", "").replaceAll("\"", "");

				num++;
				if (zsxm_mc.indexOf("企业所得税") >= 0 && sksx.indexOf("代扣代缴税款") >= 0) {
					nsrsbh = "";
					if (Double.parseDouble(zse) > 200000) {
						nsrmc = nsrmc + "(预提企业所得税)";
					} else {
						nsrmc = "预提企业所得税";
					}
					jd_mc = "外区";
					hy_mc = "";
				}

				if (drfs.equals("1") || drfs.equals("2")) {
					if (ysfpbl_mc.indexOf("区(县)级40") >= 0)
						bl = "40";
					if (ysfpbl_mc.indexOf("区(县)级100") >= 0)
						bl = "100";
					if (ysfpbl_mc.indexOf("区(县)级50") >= 0)
						bl = "50";
					if (ysfpbl_mc.indexOf("区(县)级20") >= 0)
						bl = "20";
				}

				stmt.setString(1, nsrsbh.replaceAll("\"", ""));
				stmt.setString(2, nsrmc.replaceAll("\"", ""));
				stmt.setString(3, hy_mc.replaceAll("\"", ""));
				stmt.setDouble(4, Double.parseDouble(zse.replaceAll("\"", "").replaceAll(",", "")));
				stmt.setString(5, zsxm_mc.replaceAll("\"", ""));
				stmt.setString(6, yskm_mc.replaceAll("\"", ""));
				stmt.setString(7, ysfpbl_mc.replaceAll("\"", ""));
				stmt.setString(8, jd_mc.replaceAll("\"", ""));
				stmt.setInt(9, Integer.parseInt(bl.replaceAll("\"", "").replaceAll(",", "")));
				stmt.setString(10, hy.replaceAll("\"", ""));
				stmt.setDouble(11, Double.parseDouble(qxj.replaceAll("\"", "").replaceAll(",", "")));
				stmt.setString(12, zspm.replaceAll("\"", ""));
				stmt.setString(13, zspmdm.replaceAll("\"", ""));
				stmt.setString(14, hydls.replaceAll("\"", ""));
				stmt.setString(15, hyzl.replaceAll("\"", ""));
				stmt.setString(16, yskmdms.replaceAll("\"", ""));
				stmt.setString(17, dzsphms.replaceAll("\"", ""));
				stmt.setString(18, djxhs.replaceAll("\"", ""));
				stmt.setString(19, yzpzxh.replaceAll("\"", ""));
				stmt.setString(20, sksx.replaceAll("\"", ""));
				// stmt.setString(19, dzsphms.replaceAll("\"", ""));
				stmt.addBatch();
				// 注意: 每5万，提交一次;这里不能一次提交过多的数据,我测试了一下，6万5000是极
				// 限，6万6000就会出问题，插入的数据量不对。
				if (num % 10000 == 0) {
					stmt.executeBatch();
					connection.commit();
					num = 0;
				} else if (count == list.size()) {
					stmt.executeBatch();
					connection.commit();
				}

			}
			System.out.println(count1 + "        " + count);
			stmt.executeBatch();
			connection.commit();
			return "success";
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		} finally {
			// 释放资源
			if (connection != null) {
				close();
			}
		}
	}

	public static String insertAllNsrxx(String lrr, List<String[]> list) {
		try {
			if (connection == null) {
				connection = getConnection();
			}
			connection.setAutoCommit(false);
			// Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
			// ResultSet.CONCUR_READ_ONLY);
			// 循环行Row
			int nsrsbhNo = -1;
			int nsrmcNo = -1;
			int hymlNo = -1;
			int jdNo = -1;
			int djxh = -1;

			String[] str = list.get(0);
			for (int i = 0; i < str.length; i++) {
				String result = str[i] == null ? "" : str[i];
				if (result.indexOf("纳税人识别号") > -1) {
					nsrsbhNo = i;
				} else if (result.indexOf("纳税人名称") > -1) {
					nsrmcNo = i;
				} else if (result.indexOf("行业门类") > -1) {
					hymlNo = i;
				} else if (result.indexOf("街道") > -1) {
					jdNo = i;
				}
			}
			String sql = "insert into dj_nsrxx(nsrsbh,nsrmc,jd_dm,hangye,isstop,lrry_dm,lr_sj)";
			sql += "values(?,?,?,?,'N','" + lrr + "',sysdate)";
			PreparedStatement stmt = connection.prepareStatement(sql);
			int num = 0;
			for (int x = 1; x < list.size(); x++) {
				String[] record = list.get(x);
				String nsrsbh = "";
				String nsrmc = "";
				String hy_mc = "";
				String jd_mc = "";
				String hy = "";
				String djxhs = "";
				if (nsrsbhNo > -1) {
					nsrsbh = record[nsrsbhNo] == null ? "" : record[nsrsbhNo];
				}
				if (nsrmcNo > -1) {
					nsrmc = record[nsrmcNo] == null ? "" : record[nsrmcNo];
				}
				if (hymlNo > -1) {
					hy_mc = record[hymlNo] == null ? "" : record[hymlNo];
				}
				if (jdNo > -1) {
					jd_mc = record[jdNo] == null ? "" : record[jdNo];
				}
				if (djxh > -1) {
					djxhs = record[djxh] == null ? "" : record[djxh];
				}
				num++;
				stmt.setString(1, nsrsbh.replaceAll("\"", ""));
				stmt.setString(2, nsrmc.replaceAll("\"", ""));
				stmt.setString(3, hy_mc.replaceAll("\"", ""));
				stmt.setString(8, jd_mc.replaceAll("\"", ""));
				stmt.setString(10, hy.replaceAll("\"", ""));
				stmt.setString(18, djxhs.replaceAll("\"", ""));
				// stmt.setString(19, dzsphms.replaceAll("\"", ""));
				stmt.addBatch();
				// 注意: 每5万，提交一次;这里不能一次提交过多的数据,我测试了一下，6万5000是极
				// 限，6万6000就会出问题，插入的数据量不对。
				if (num > 50000) {
					stmt.executeBatch();
					connection.commit();
					num = 0;
				}

			}
			stmt.executeBatch();
			connection.commit();
			return "success";
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		} finally {
			// 释放资源
			if (connection != null) {
				close();
			}

		}
	}

	public static Object call(String name, List<Mode> list) {
		Object rs = null;
		List<Map<String, Object>> rslist = new ArrayList<Map<String, Object>>();
		String ootype = "";
		String sql = "{call " + name + " (";
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (i == list.size() - 1) {
					sql += "?";
				} else {
					sql += "?,";
				}
			}
		}
		sql += ")}";
		System.out.println("执行存储过程：" + sql);
		Connection connection1 = null;
		CallableStatement callableStatement1 = null;
		try {
			connection1 = getConnection1();
			if (connection1 == null) {
				call(name, list);
			}
			callableStatement1 = connection1.prepareCall(sql);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Mode mode = list.get(i);
					String otype = mode.getOtype().toUpperCase();
					String type = mode.getType().toUpperCase();
					String value = mode.getValue().toUpperCase();
					if (otype.equals("IN")) {
						switch (type) {
						case "STRING":
							System.out.println(value);
							callableStatement1.setString(i + 1, value);
							break;
						case "INT":
							callableStatement1.setInt(i + 1, Integer.parseInt(value));
							break;
						default:
							break;
						}
					} else {
						switch (type) {
						case "STRING":
							ootype = "STRING";
							callableStatement1.registerOutParameter(i + 1, OracleTypes.VARCHAR);
							break;
						case "INT":
							ootype = "INT";
							callableStatement1.registerOutParameter(i + 1, OracleTypes.NUMBER);
							break;
						case "RS":
							ootype = "RS";
							callableStatement1.registerOutParameter(i + 1, OracleTypes.CURSOR);
							break;
						default:
							break;
						}
					}

				}
			}

			callableStatement1.execute();// 执行
			if (ootype != null && !ootype.equals("")) {
				if (ootype != null && !ootype.equals("") && !connection1.isClosed()) {
					switch (ootype) {
					case "STRING":
						rs = (String) callableStatement1.getObject(list.size());// 此处的2要与存储过程中cursor的问题对应
						break;
					case "INT":
						rs = (Integer) callableStatement1.getObject(list.size());// 此处的2要与存储过程中cursor的问题对应
						break;
					case "RS":
						ootype = "RS";
						ResultSet rss = (ResultSet) callableStatement1.getObject(list.size());// 此处的2要与存储过程中cursor的问题对应
						ResultSetMetaData md = rss.getMetaData(); // 获得结果集结构信息,元数据
						int columnCount = md.getColumnCount(); // 获得列数
						while (rss.next()) {
							Map<String, Object> rowData = new HashMap<String, Object>();
							for (int i = 1; i <= columnCount; i++) {
								rowData.put(md.getColumnName(i), rss.getObject(i));
							}
							rslist.add(rowData);
						}
						rs = rslist;
						break;
					default:
						break;
					}
				} else {
					call(name, list);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			if (dataSource != null) {
				try {
					dataSource.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (connection1 != null) {
				try {
					connection1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (callableStatement1 != null) {
					try {
						callableStatement1.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}
		return rs;
	}

	public static Object call(String name, List<Mode> list, Connection connection1) {
		Object rs = null;
		List<Map<String, Object>> rslist = new ArrayList<Map<String, Object>>();
		String ootype = "";
		String sql = "{call " + name + " (";
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (i == list.size() - 1) {
					sql += "?";
				} else {
					sql += "?,";
				}
			}
		}
		sql += ")}";
		System.out.println("执行存储过程：" + sql);
		CallableStatement callableStatement1 = null;
		try {
			connection1 = getConnection1();
			callableStatement1 = connection1.prepareCall(sql);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Mode mode = list.get(i);
					String otype = mode.getOtype().toUpperCase();
					String type = mode.getType().toUpperCase();
					String value = mode.getValue().toUpperCase();
					if (otype.equals("IN")) {
						switch (type) {
						case "STRING":
							System.out.println(value);
							callableStatement1.setString(i + 1, value);
							break;
						case "INT":
							callableStatement1.setInt(i + 1, Integer.parseInt(value));
							break;
						default:
							break;
						}
					} else {
						switch (type) {
						case "STRING":
							ootype = "STRING";
							callableStatement1.registerOutParameter(i + 1, OracleTypes.VARCHAR);
							break;
						case "INT":
							ootype = "INT";
							callableStatement1.registerOutParameter(i + 1, OracleTypes.NUMBER);
							break;
						case "RS":
							ootype = "RS";
							callableStatement1.registerOutParameter(i + 1, OracleTypes.CURSOR);
							break;
						default:
							break;
						}
					}

				}
			}

			callableStatement1.execute();// 执行
			if (ootype != null && !ootype.equals("")) {
				if (ootype != null && !ootype.equals("") && !connection1.isClosed()) {
					switch (ootype) {
					case "STRING":
						rs = (String) callableStatement1.getObject(list.size());// 此处的2要与存储过程中cursor的问题对应
						break;
					case "INT":
						rs = (Integer) callableStatement1.getObject(list.size());// 此处的2要与存储过程中cursor的问题对应
						break;
					case "RS":
						ootype = "RS";
						ResultSet rss = (ResultSet) callableStatement1.getObject(list.size());// 此处的2要与存储过程中cursor的问题对应
						ResultSetMetaData md = rss.getMetaData(); // 获得结果集结构信息,元数据
						int columnCount = md.getColumnCount(); // 获得列数
						while (rss.next()) {
							Map<String, Object> rowData = new HashMap<String, Object>();
							for (int i = 1; i <= columnCount; i++) {
								rowData.put(md.getColumnName(i), rss.getObject(i));
							}
							rslist.add(rowData);
						}
						rs = rslist;
						break;
					default:
						break;
					}
				} else {
					call(name, list);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			if (dataSource != null) {
				try {
					dataSource.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (callableStatement1 != null) {
				try {
					callableStatement1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return rs;
	}

	public static String insertAllnew(List<Map<String, String>> list, String drfs) {
		try {
			System.out.println("--------正在存储数据库:");
			connection = getConnection();
			connection.setAutoCommit(false);
			String sql = "";
			if (drfs.equals("0")) {
				sql = "insert into xwcs_gsdr_yssjrk (nsrsbh,nsrmc,zsxmmc,zspm,zspmdm,skshqq,skshqz,djzclx,ssgly,hyml,hydl,hyzl,hy,zsdlfs,jsyj,kssl,sl,sjje,skssswjg,zgsws,yskmdm,yskm,ysfpbl,zyjbl,ssjbl,dsjbl,qxjbl,xzjbl,zydfpbl,dsdfpbl,sjdfpbl,skgk,kjrq,sjrq,sjxhrq,sjxhr,rkrq,rkxhrq,rkxhr,pzzl,pzzg,pzhm,skzl,sksx,jdxz,yhhb,yhyywd,yhzh,hzrq,hzr,hzpzzl,hzpzzg,hzpzhm,tzlx,jkqx,zsswjg,dzsphm,zyj,ssj,dsj,qxj,xzj,zydfp,sjdfp,dsdfp,djxh,zfjglx,kdqsszyqylx,cbsx,yzpzzl,sybh,kjnsrxx,yzpzxh,kpr,qsbj) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_number(?),to_number(?),to_number(?),to_number(?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_number(?),to_number(?),to_number(?),to_number(?),to_number(?),to_number(?),to_number(?),to_number(?),?,?,?,?,?,?,?,?,?,?) ";
			} else if (drfs.equals("1")) {
				sql = "insert into xwcs_gsdr_yssjtk  values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			} else if (drfs.equals("2")) {
				sql = "insert into xwcs_gsdr_yssjgs values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			}
//			String sql = "insert into xwcs_gsdr_yssjrk VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

			PreparedStatement stmt = connection.prepareStatement(sql);
			int num = 0;
			int count = 0;
			int count1 = 0;
			System.out.println("--------sql:" + sql);
			for (int x = 0; x < list.size(); x++) {
				Map<String, String> map = list.get(x);
				count++;
				if (map == null || map.isEmpty()) {
					continue;
				}
				count1++;
				String nsrsbh = "";
				String nsrmc = "";
				String zsxmmc = "";
				String zspm = "";
				String zspmdm = "";
				String skshqq = "";
				String skshqz = "";
				String djzclx = "";
				String ssgly = "";
				String hyml = "";
				String hydl = "";
				String hyzl = "";
				String hy = "";
				String zsdlfs = "";
				String jsyj = "";
				String kssl = "";
				String sl = "";
				String sjje = "";
				String skssswjg = "";
				String zgsws = "";
				String yskmdm = "";
				String yskm = "";
				String ysfpbl = "";
				String zyjbl = "";
				String ssjbl = "";
				String dsjbl = "";
				String qxjbl = "";
				String xzjbl = "";
				String zydfpbl = "";
				String dsdfpbl = "";
				String sjdfpbl = "";
				String skgk = "";
				String kjrq = "";
				String sjrq = "";
				String sjxhrq = "";
				String sjxhr = "";
				String rkrq = "";
				String rkxhrq = "";
				String rkxhr = "";
				String pzzl = "";
				String pzzg = "";
				String pzhm = "";
				String skzl = "";
				String sksx = "";
				String jdxz = "";
				String yhhb = "";
				String yhyywd = "";
				String yhzh = "";
				String hzrq = "";
				String hzr = "";
				String hzpzzl = "";
				String hzpzzg = "";
				String hzpzhm = "";
				String tzlx = "";
				String jkqx = "";
				String zsswjg = "";
				String dzsphm = "";
				String zyj = "";
				String ssj = "";
				String dsj = "";
				String qxj = "";
				String xzj = "";
				String zydfp = "";
				String sjdfp = "";
				String dsdfp = "";
				String djxh = "";
				String zfjglx = "";
				String kdqsszyqylx = "";
				String cbsx = "";
				String yzpzzl = "";
				String sybh = "";
				String kjnsrxx = "";
				String yzpzxh = "";
				String kpr = "";
				String qsbj = "";

				if (drfs.equals("0")) {

					nsrsbh = map.get("社会信用代码（纳税人识别号）") == null ? "" : map.get("社会信用代码（纳税人识别号）");
					nsrmc = map.get("纳税人名称") == null ? "" : map.get("纳税人名称");
					zsxmmc = map.get("征收项目") == null ? "" : map.get("征收项目");
					zspm = map.get("征收品目") == null ? "" : map.get("征收品目");
					zspmdm = map.get("征收品目代码") == null ? "" : map.get("征收品目代码");
					skshqq = map.get("税款所属期起") == null ? "" : map.get("税款所属期起");
					skshqz = map.get("税款所属期止") == null ? "" : map.get("税款所属期止");
					djzclx = map.get("登记注册类型") == null ? "" : map.get("登记注册类型");
					ssgly = map.get("税收管理员") == null ? "" : map.get("税收管理员");
					hyml = map.get("行业门类") == null ? "" : map.get("行业门类");
					hydl = map.get("行业大类") == null ? "" : map.get("行业大类");
					hyzl = map.get("行业中类") == null ? "" : map.get("行业中类");
					hy = map.get("行业") == null ? "" : map.get("行业");
					zsdlfs = map.get("征收代理方式") == null ? "" : map.get("征收代理方式");
					jsyj = map.get("计税依据") == null ? "0" : map.get("计税依据");
					kssl = map.get("课税数量") == null ? "0" : map.get("课税数量");
					sl = map.get("税率") == null ? "0" : map.get("税率");
					sjje = map.get("实缴金额") == null ? "0" : map.get("实缴金额");

					skssswjg = map.get("税款所属税务机关") == null ? "" : map.get("税款所属税务机关");
					zgsws = map.get("主管税务所（科、分局）") == null ? "" : map.get("主管税务所（科、分局）");
					yskmdm = map.get("预算科目代码") == null ? "" : map.get("预算科目代码");
					yskm = map.get("预算科目") == null ? "" : map.get("预算科目");
					ysfpbl = map.get("预算分配比例") == null ? "" : map.get("预算分配比例");

					zyjbl = map.get("中央级比例") == null ? "" : map.get("中央级比例");
					ssjbl = map.get("省市级比例") == null ? "" : map.get("省市级比例");
					dsjbl = map.get("地市级比例") == null ? "" : map.get("地市级比例");
					qxjbl = map.get("区县级比例") == null ? "" : map.get("区县级比例");
					xzjbl = map.get("乡镇级比例") == null ? "" : map.get("乡镇级比例");
					zydfpbl = map.get("中央待分配比例") == null ? "" : map.get("中央待分配比例");
					dsdfpbl = map.get("地市待分配比例") == null ? "" : map.get("地市待分配比例");
					sjdfpbl = map.get("省局待分配比例") == null ? "" : map.get("省局待分配比例");
					skgk = map.get("收款国库") == null ? "" : map.get("收款国库");
					kjrq = map.get("开具日期") == null ? "" : map.get("开具日期");

					sjrq = map.get("上解日期") == null ? "" : map.get("上解日期");
					sjxhrq = map.get("上解销号日期") == null ? "" : map.get("上解销号日期");
					sjxhr = map.get("上解销号人") == null ? "" : map.get("上解销号人");
					rkrq = map.get("入库日期") == null ? "" : map.get("入库日期");
					rkxhrq = map.get("入库销号日期") == null ? "" : map.get("入库销号日期");

					rkxhr = map.get("入库销号人") == null ? "" : map.get("入库销号人");
					pzzl = map.get("票证种类") == null ? "" : map.get("票证种类");
					pzzg = map.get("票证字轨") == null ? "" : map.get("票证字轨");
					pzhm = map.get("票证号码") == null ? "" : map.get("票证号码");
					skzl = map.get("税款种类") == null ? "" : map.get("税款种类");

					sksx = map.get("税款属性") == null ? "" : map.get("税款属性");
					jdxz = map.get("街道乡镇") == null ? "" : map.get("街道乡镇");
					yhhb = map.get("银行行别") == null ? "" : map.get("银行行别");
					yhyywd = map.get("银行营业网点") == null ? "" : map.get("银行营业网点");
					yhzh = map.get("银行账号") == null ? "" : map.get("银行账号");

					hzrq = map.get("汇总日期") == null ? "" : map.get("汇总日期");
					hzr = map.get("汇总人") == null ? "" : map.get("汇总人");
					hzpzzl = map.get("汇总票证种类") == null ? "" : map.get("汇总票证种类");
					hzpzzg = map.get("汇总票证字轨") == null ? "" : map.get("汇总票证字轨");
					hzpzhm = map.get("汇总票证号码") == null ? "" : map.get("汇总票证号码");

					tzlx = map.get("调账类型") == null ? "" : map.get("调账类型");
					jkqx = map.get("缴款期限") == null ? "" : map.get("缴款期限");
					zsswjg = map.get("征收税务机关") == null ? "" : map.get("征收税务机关");
					dzsphm = map.get("电子税票号码") == null ? "" : map.get("电子税票号码");

					zyj = map.get("中央级") == null ? "0" : map.get("中央级");
					ssj = map.get("省市级") == null ? "0" : map.get("省市级");
					dsj = map.get("地市级") == null ? "0" : map.get("地市级");
					qxj = map.get("区县级") == null ? "0" : map.get("区县级");
					xzj = map.get("乡镇级") == null ? "0" : map.get("乡镇级");
					zydfp = map.get("中央待分配") == null ? "0" : map.get("中央待分配");
					sjdfp = map.get("省级待分配") == null ? "0" : map.get("省级待分配");
					dsdfp = map.get("地市待分配") == null ? "0" : map.get("地市待分配");

					djxh = map.get("登记序号") == null ? "" : map.get("登记序号");
					zfjglx = map.get("总分机构类型") == null ? "" : map.get("总分机构类型");
					kdqsszyqylx = map.get("跨地区税收转移企业类型") == null ? "" : map.get("跨地区税收转移企业类型");
					cbsx = map.get("查补属性") == null ? "" : map.get("查补属性");
					yzpzzl = map.get("应征凭证种类") == null ? "" : map.get("应征凭证种类");
					sybh = map.get("税源编号") == null ? "" : map.get("税源编号");
					kjnsrxx = map.get("扣缴纳税人信息") == null ? "" : map.get("扣缴纳税人信息");
					yzpzxh = map.get("应征凭证序号") == null ? "" : map.get("应征凭证序号");
					kpr = map.get("开票人") == null ? "" : map.get("开票人");
					qsbj = map.get("欠税标记") == null ? "" : map.get("欠税标记");

				} else if (drfs.equals("1")) {

				} else if (drfs.equals("2")) {

				}

				stmt.setString(1, nsrsbh.replaceAll("\"", ""));
				stmt.setString(2, nsrmc.replaceAll("\"", ""));
				stmt.setString(3, zsxmmc.replaceAll("\"", ""));
				stmt.setString(4, zspm.replaceAll("\"", ""));
				stmt.setString(5, zspmdm.replaceAll("\"", ""));
				stmt.setString(6, skshqq.replaceAll("\"", ""));
				stmt.setString(7, skshqz.replaceAll("\"", ""));
				stmt.setString(8, djzclx.replaceAll("\"", ""));
				stmt.setString(9, ssgly.replaceAll("\"", ""));
				stmt.setString(10, hyml.replaceAll("\"", ""));
				stmt.setString(11, hydl.replaceAll("\"", ""));
				stmt.setString(12, hyzl.replaceAll("\"", ""));
				stmt.setString(13, hy.replaceAll("\"", ""));
				stmt.setString(14, zsdlfs.replaceAll("\"", ""));
				stmt.setString(15, jsyj.replaceAll("\"", "").replaceAll(",", ""));
				stmt.setString(16, kssl.replaceAll("\"", "").replaceAll(",", ""));
				stmt.setString(17, sl.replaceAll("\"", "").replaceAll(",", ""));
				stmt.setString(18, sjje.replaceAll("\"", "").replaceAll(",", ""));

				stmt.setString(19, skssswjg.replaceAll("\"", ""));
				stmt.setString(20, zgsws.replaceAll("\"", ""));
				stmt.setString(21, yskmdm.replaceAll("\"", ""));
				stmt.setString(22, yskm.replaceAll("\"", ""));
				stmt.setString(23, ysfpbl.replaceAll("\"", ""));

				stmt.setString(24, zyjbl.replaceAll("\"", ""));
				stmt.setString(25, ssjbl.replaceAll("\"", ""));
				stmt.setString(26, dsjbl.replaceAll("\"", ""));
				stmt.setString(27, qxjbl.replaceAll("\"", ""));
				stmt.setString(28, xzjbl.replaceAll("\"", ""));

				stmt.setString(29, zydfpbl.replaceAll("\"", ""));
				stmt.setString(30, dsdfpbl.replaceAll("\"", ""));
				stmt.setString(31, sjdfpbl.replaceAll("\"", ""));
				stmt.setString(32, skgk.replaceAll("\"", ""));
				stmt.setString(33, kjrq.replaceAll("\"", ""));

				stmt.setString(34, sjrq.replaceAll("\"", ""));
				stmt.setString(35, sjxhrq.replaceAll("\"", ""));
				stmt.setString(36, sjxhr.replaceAll("\"", ""));
				stmt.setString(37, rkrq.replaceAll("\"", ""));
				stmt.setString(38, rkxhrq.replaceAll("\"", ""));

				stmt.setString(39, rkxhr.replaceAll("\"", ""));
				stmt.setString(40, pzzl.replaceAll("\"", ""));
				stmt.setString(41, pzzg.replaceAll("\"", ""));
				stmt.setString(42, pzhm.replaceAll("\"", ""));
				stmt.setString(43, skzl.replaceAll("\"", ""));
				stmt.setString(44, sksx.replaceAll("\"", ""));
				stmt.setString(45, jdxz.replaceAll("\"", ""));
				stmt.setString(46, yhhb.replaceAll("\"", ""));
				stmt.setString(47, yhyywd.replaceAll("\"", ""));
				stmt.setString(48, yhzh.replaceAll("\"", ""));

				stmt.setString(49, hzrq.replaceAll("\"", ""));
				stmt.setString(50, hzr.replaceAll("\"", ""));
				stmt.setString(51, hzpzzl.replaceAll("\"", ""));
				stmt.setString(52, hzpzzg.replaceAll("\"", ""));
				stmt.setString(53, hzpzhm.replaceAll("\"", ""));

				stmt.setString(54, tzlx.replaceAll("\"", ""));
				stmt.setString(55, jkqx.replaceAll("\"", ""));
				stmt.setString(56, zsswjg.replaceAll("\"", ""));
				stmt.setString(57, dzsphm.replaceAll("\"", ""));

				stmt.setString(58, zyj.replaceAll("\"", "").replaceAll(",", ""));
				stmt.setString(59, ssj.replaceAll("\"", "").replaceAll(",", ""));
				stmt.setString(60, dsj.replaceAll("\"", "").replaceAll(",", ""));
				stmt.setString(61, qxj.replaceAll("\"", "").replaceAll(",", ""));
				stmt.setString(62, xzj.replaceAll("\"", "").replaceAll(",", ""));
				stmt.setString(63, zydfp.replaceAll("\"", "").replaceAll(",", ""));
				stmt.setString(64, sjdfp.replaceAll("\"", "").replaceAll(",", ""));
				stmt.setString(65, dsdfp.replaceAll("\"", "").replaceAll(",", ""));

				stmt.setString(66, djxh.replaceAll("\"", ""));
				stmt.setString(67, zfjglx.replaceAll("\"", ""));
				stmt.setString(68, kdqsszyqylx.replaceAll("\"", ""));
				stmt.setString(69, cbsx.replaceAll("\"", ""));
				stmt.setString(70, yzpzzl.replaceAll("\"", ""));
				stmt.setString(71, sybh.replaceAll("\"", ""));
				stmt.setString(72, kjnsrxx.replaceAll("\"", ""));
				stmt.setString(73, yzpzxh.replaceAll("\"", ""));
				stmt.setString(74, kpr.replaceAll("\"", ""));
				stmt.setString(75, qsbj.replaceAll("\"", ""));

				stmt.addBatch();
				// 注意: 每5万，提交一次;这里不能一次提交过多的数据,我测试了一下，6万5000是极
				// 限，6万6000就会出问题，插入的数据量不对。
				if (num % 10000 == 0) {
					stmt.executeBatch();
					connection.commit();
					num = 0;
				} else if (count == list.size()) {
					stmt.executeBatch();
					connection.commit();
				}

			}
			System.out.println(count1 + "        " + count);
			stmt.executeBatch();
			connection.commit();
			return "success";
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		} finally {
			// 释放资源
			close();
		}
	}

	public static Map<String, Integer> insertAll_new(List<Map<String, String>> list, String drfs, String sswj,
			String scfs, String rkrq) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		if (connection == null) {
			connection = getConnection();
		}
		try {
			System.out.println("--------正在存储数据库:");
			connection.setAutoCommit(false);
			Statement cs = connection.createStatement();

			String tableName = "";
			if (drfs.equals("0")) {
				tableName = "xwcs_gsdr_yssjrk";
			} else if (drfs.equals("1")) {
				tableName = "xwcs_gsdr_yssjtk";
			} else if (drfs.equals("2")) {
				tableName = "xwcs_gsdr_yssjgs";
			} else if (drfs.equals("3")) {
				tableName = "xwcs_gsdr_yssjjrk";
			}
			if (scfs.equals("0")) {// 0：全量，1：断点续传
				cs.executeUpdate("delete from " + tableName + " where rk_rq = '" + rkrq + "'");
				connection.commit();
				cs.executeUpdate("delete from xwcs_gsdr_temp where drfs = '" + drfs + "'");
				connection.commit();
			}

			Map<String, Map<String, String>> infoMap = getTableInfo(tableName);
			String keySql = "insert into " + tableName + " (";
			String valueSql = " values (";
			for (String comments : infoMap.keySet()) {
				String columnName = infoMap.get(comments).get("COLUMN_NAME");
				keySql += columnName + ",";
				valueSql += "?,";
			}
			String sql = keySql.replaceAll(",$", ")") + valueSql.replaceAll(",$", ")");
			System.out.println(sql);
			PreparedStatement stmt = connection.prepareStatement(sql);
			int num = 0;
			int renum = 0;
			int count = 0;

			lableA: for (Map<String, String> one : list) {
				count++;
				String sjzw = "";
				int i = 0;
				int sjzwIndex = 0;
				long startTime1 = System.currentTimeMillis(); // 获取开始时间
				for (String comments : infoMap.keySet()) {
					i++;
					String dataType = infoMap.get(comments).get("DATA_TYPE");
					String value = one.get(comments);
					String jdxz = one.get("街道乡镇");
					System.out.println("街道乡镇："+jdxz);
					sjzw += value;
					if ((comments.equals("应征税金额") || comments.equals("税额") || comments.equals("实缴金额"))
							&& ("0".equals(value) || value == null)) {// 金额为0不导
						System.out.println("去除金额为0的数据！");
						continue lableA;
					} else if (drfs.equals("1") && comments.equals("税额")) {// 退库,金额置负数
						stmt.setDouble(i, Double.valueOf(value.replaceAll("[,|，]", "")) * -1);
						continue;
					} else if (comments.equals("导入日期")) {
						stmt.setString(i, rkrq);
						continue;
					} else if (comments.equals("所属文件")) {
						stmt.setString(i, sswj);
						continue;
					} else if (comments.equals("ID")) {
						stmt.setString(i, UUID.randomUUID().toString().replace("-", ""));
						continue;
					} else if (comments.equals("导入方式")) {
						stmt.setString(i, drfs);
						continue;
					} else if (comments.equals("数据指纹")) {
						sjzwIndex = i;
						continue;
					}
					if (dataType.equals("VARCHAR2")) {
						if (value == null) {
							stmt.setNull(i, Types.VARCHAR);
						} else {
							stmt.setString(i, value);
						}
					} else if (dataType.equals("NUMBER")) {
						if (value == null) {
							stmt.setNull(i, Types.DOUBLE);
						} else {
							if (value.replaceAll("[,|，]", "").equals("")) {
								value = "0";
							}
							stmt.setDouble(i, Double.valueOf(value.replaceAll("[,|，]", "")));
						}
					}
				}

				long endTime1 = System.currentTimeMillis(); // 获取结束时间
				System.out.println("代码运行时间1：" + (endTime1 - startTime1) + "ms"); // 输出程序运行时间
				startTime1 = System.currentTimeMillis();
				String strId = StrToId(sjzw);
				endTime1 = System.currentTimeMillis();
				System.out.println("代码运行时间2：" + (endTime1 - startTime1) + "ms"); // 输出程序运行时间
				// ResultSet rs = cs.executeQuery("select count(*) from " + tableName + " where
				// sjzw = '" + strId + "'");
				// if (rs.next() && rs.getInt(1) > 0) {
				// System.out.println("根据数据指纹去除重复数据！");
				// renum++;
				// continue lableA;
				// } else {
				stmt.setString(sjzwIndex, strId);
				// }
				num++;
				stmt.addBatch();
				try {
					if (count == list.size()) {
						endTime1 = System.currentTimeMillis();
						System.out.println("代码运行时间3：" + (endTime1 - startTime1) + "ms"); // 输出程序运行时间
						startTime1 = System.currentTimeMillis();

						System.out.println("执行数据库");
						stmt.executeBatch();
						endTime1 = System.currentTimeMillis();
						System.out.println("数据库执行时间：" + (endTime1 - startTime1) + "ms"); // 输出程序运行时间
						connection.commit();
					}
					if (num % 50 == 0) {
						endTime1 = System.currentTimeMillis();
						System.out.println("代码运行时间3：" + (endTime1 - startTime1) + "ms"); // 输出程序运行时间
						startTime1 = System.currentTimeMillis();
						System.out.println(num);
						System.out.println("执行数据库");
						stmt.executeBatch();
						endTime1 = System.currentTimeMillis();
						System.out.println("数据库执行时间：" + (endTime1 - startTime1) + "ms"); // 输出程序运行时间
						connection.commit();
					}
//					if (num%5000 == 0) {
//						long startTime1 = System.currentTimeMillis();    //获取开始时间
//						stmt.execute();
//						connection.commit();
//						long endTime1 = System.currentTimeMillis();    //获取结束时间
//
//						System.out.println("代码运行时间：" + (endTime1 - startTime1) + "ms");    //输出程序运行时间
//					}else {
//						stmt.execute();
//						connection.commit();
//					}
				} catch (Exception e) {
					System.err.println("导入异常：" + e.getMessage());
				}
			}
			cs.executeUpdate("delete from xwcs_gsdr_temp where drfs = '" + drfs + "'");
			ResultSet rs = cs.executeQuery("select * from " + tableName + " where rk_rq ='" + rkrq + "'");
			List<Map<String, String>> valuelist = new ArrayList<Map<String, String>>();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				for (String comment : infoMap.keySet()) {
					String column = infoMap.get(comment).get("COLUMN_NAME");
					map.put(comment, rs.getString(column) == null ? "" : rs.getString(column));
				}
				valuelist.add(map);
			}
			insertAll_new(valuelist, drfs);
			System.out.println("导入完成--" + num + "行");

			result.put("NUM", num);
			result.put("RENUM", renum);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().contains("closed")) {
				connection = getConnection();
				insertAll_new(list, drfs, sswj, scfs, rkrq);
			}
			return result;
		} finally {
			// 释放资源
			if (connection != null) {
				close();
			}
		}
	}

	public static Map<String, Integer> insertAll_sy(List<Map<String, String>> list, String sswj) throws SQLException {
		Map<String, Integer> result = new HashMap<String, Integer>();
		if (connection == null||!(connection.isClosed())) {
			connection = getConnection();
		}
		try {
			System.out.println("--------正在存储数据库:");
			connection.setAutoCommit(false);
			Statement cs = connection.createStatement();

			String tableName = "SWDJXX";
			Map<String, Map<String, String>> infoMap = getTableInfo(tableName);
			// 拼接插入SQL
			String keySql = "insert into " + tableName + " (";
			String valueSql = " values (";
			for (String comments : infoMap.keySet()) {
				String columnName = infoMap.get(comments).get("COLUMN_NAME");
				keySql += columnName + ",";
				valueSql += "?,";
			}
			String sql = keySql.replaceAll(",$", ")") + valueSql.replaceAll(",$", ")");
			// 拼接更新SQL
			String keySql1 = "update " + tableName + " set ";
			for (String comments : infoMap.keySet()) {
				String columnName = infoMap.get(comments).get("COLUMN_NAME");
				keySql1 += columnName + " = ?,";
			}
			String updatesql = keySql1.replaceAll(",$", "");
			System.out.println(sql);
			PreparedStatement stmt = connection.prepareStatement(sql);
			int num = 0;
			int upnum = 0;
			int renum = 0;
			lableA: for (Map<String, String> one : list) {
				String sjzw = "";
				String nsrsbhQC = "";
				int i = 0;
				int sjzwIndex = 0;
				nsrsbhQC = one.get("社会信用代码（纳税人识别号）");
				String updatesql1 = updatesql + " where nsrsbh = '" + nsrsbhQC + "'";
				PreparedStatement updatestmt = connection.prepareStatement(updatesql1);
				ResultSet rs1 = cs
						.executeQuery("select count(*) from " + tableName + " where nsrsbh = '" + nsrsbhQC + "'");
				if (rs1.next() && rs1.getInt(1) > 0) {
					// System.out.println("根据纳税人识别号重复更新数据！");
					for (String comments : infoMap.keySet()) {
						i++;
						String dataType = infoMap.get(comments).get("DATA_TYPE");
						String value = one.get(comments);
						sjzw += value;
						if (dataType.equals("VARCHAR2")) {
							if (value == null) {
								updatestmt.setNull(i, Types.VARCHAR);
							} else {
								updatestmt.setString(i, value);
							}
						} else if (dataType.equals("NUMBER")) {
							if (value == null) {
								updatestmt.setNull(i, Types.DOUBLE);
							} else {
								if (value.replaceAll("[,|，]", "").equals("")) {
									value = "0";
								}
								updatestmt.setDouble(i, Double.valueOf(value.replaceAll("[,|，]", "")));
							}
						}
					}
					upnum++;
					updatestmt.addBatch();
					try {
						updatestmt.executeBatch();
						connection.commit();
						updatestmt.close();
					} catch (Exception e) {
						System.err.println("导入异常：" + e.getMessage());
					}
					continue lableA;
				} else {
					for (String comments : infoMap.keySet()) {
						i++;
						String dataType = infoMap.get(comments).get("DATA_TYPE");
						String value = one.get(comments);
						sjzw += value;
						if ((comments.equals("应征税金额") || comments.equals("税额") || comments.equals("实缴金额"))
								&& ("0".equals(value) || value == null)) {// 金额为0不导
							System.out.println("去除金额为0的数据！");
							continue lableA;
						} else if (comments.equals("所属文件")) {
							stmt.setString(i, sswj);
							continue;
						} else if (comments.equals("ID")) {
							stmt.setString(i, UUID.randomUUID().toString().replace("-", ""));
							continue;
						} else if (comments.equals("数据指纹")) {
							sjzwIndex = i;
							continue;
						}
						if (dataType.equals("VARCHAR2")) {
							if (value == null) {
								stmt.setNull(i, Types.VARCHAR);
							} else {
								stmt.setString(i, value);
							}
						} else if (dataType.equals("NUMBER")) {
							if (value == null) {
								stmt.setNull(i, Types.DOUBLE);
							} else {
								if (value.replaceAll("[,|，]", "").equals("")) {
									value = "0";
								}
								stmt.setDouble(i, Double.valueOf(value.replaceAll("[,|，]", "")));
							}
						}
					}
					String strId = StrToId(sjzw);
					ResultSet rs = cs
							.executeQuery("select count(*) from " + tableName + " where sjzw = '" + strId + "'");
					if (rs.next() && rs.getInt(1) > 0) {
						System.out.println("根据数据指纹去除重复数据！");
						renum++;
						continue lableA;
					} else {
						stmt.setString(sjzwIndex, strId);
					}
					num++;
					stmt.addBatch();
					try {
						stmt.executeBatch();
						connection.commit();
					} catch (Exception e) {
						System.err.println("导入异常：" + e.getMessage());
					}
				}
			}
			System.out.println("导入完成--" + num + "行");
			result.put("NUM", num);
			result.put("RENUM", renum);
			result.put("UPNUM", upnum);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		} finally {
			// 释放资源
			if (connection != null) {
				close();
			}
		}
	}

	public static String StrToId(String str) throws NoSuchAlgorithmException {
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

	public static List<String> getTableColumns(String tableName) {
		List<String> list = new ArrayList<String>();
		try {
			if (connection == null) {
				connection = getConnection();
			}
			Statement cs = connection.createStatement();
			ResultSet rs = cs.executeQuery(
					"select column_name from user_tab_columns where table_name = upper('" + tableName + "')");
			while (rs.next()) {
				list.add(rs.getString(1));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		} finally {
			// 释放资源
			if (connection != null) {
				close();
			}
		}
	}

	public static String insertAll_new(List<Map<String, String>> list, String drfs) {
		try {
			if (connection == null) {
				connection = getConnection();
			}
			connection.setAutoCommit(false);
			String sql = "insert into xwcs_gsdr_temp(id,nsrsbh,nsrmc, hy_mc, zse,zsxm_mc, yskm_mc, ysfpbl_mc,jd_mc, type,lr_sj,bl,HY,se,ZSPM,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,yzpzxh,sksx,YSID,DRFS,RK_RQ) VALUES (seq_xwcs_gsdr.nextval,?,?,?,to_number(?),?,?,?,?,'0',sysdate,?,?,to_number(?),?,?,?,?,?,?,?,?,?,?,?,?) ";
			PreparedStatement stmt = connection.prepareStatement(sql);
			int num = 0;
			int count = 0;
			int count1 = 0;
			for (int x = 0; x < list.size(); x++) {
				Map<String, String> map = list.get(x);
				count++;
				if (map == null || map.isEmpty()) {
					continue;
				}
				count1++;
				String zsxm_mc = "";
				String sksx = "";
				String zse = "";
				String yskm_mc = "";
				String ysfpbl_mc = "";
				String bl = "";
				String nsrsbh = "";
				String nsrmc = "";
				String hy_mc = "";
				String jd_mc = "";
				String hy = "";
				String qxj = "";
				String zspm = "";
				String zspmdm = "";
				String hydls = "";
				String hyzl = "";
				String yskmdms = "";
				String dzsphms = "";
				String djxhs = "";
				String yzpzxh = "";
				String ysid = "";
				String rk_rq = "";

				if (drfs.equals("0")) {
					zsxm_mc = map.get("征收项目");
					sksx = map.get("税款属性");
					zse = map.get("实缴金额");
					yskm_mc = map.get("预算科目");
					ysfpbl_mc = map.get("预算分配比例");
					bl = map.get("区县级比例");
					nsrsbh = map.get("社会信用代码（纳税人识别号）");
					nsrmc = map.get("纳税人名称");
					hy_mc = map.get("行业门类");
					jd_mc = map.get("街道乡镇");
					hy = map.get("行业");
					qxj = map.get("区县级");
					zspm = map.get("征收品目");
					zspmdm = map.get("征收品目代码");
					hydls = map.get("行业大类");
					hyzl = map.get("行业中类");
					yskmdms = map.get("预算科目代码");
					dzsphms = map.get("电子税票号码");
					djxhs = map.get("登记序号");
					yzpzxh = map.get("应征凭证序号");
					ysid = map.get("ID");
					rk_rq = map.get("导入日期");
				} else if (drfs.equals("1")) {
					zsxm_mc = map.get("征收项目");
					sksx = map.get("税款属性");
					zse = map.get("税额");
					yskm_mc = map.get("预算科目");
					ysfpbl_mc = map.get("预算分配比例");
					bl = map.get("区县级比例");
					nsrsbh = map.get("社会信用代码（纳税人识别号）");
					nsrmc = map.get("纳税人名称");
					hy_mc = map.get("行业门类");
					jd_mc = map.get("街道乡镇");
					hy = map.get("行业");
					qxj = map.get("区县级");
					zspm = map.get("征收品目");
					zspmdm = map.get("征收品目代码");
					hydls = map.get("行业大类");
					hyzl = map.get("行业中类");
					yskmdms = map.get("预算科目代码");
					dzsphms = map.get("电子税票号码");
					djxhs = map.get("登记序号");
					yzpzxh = map.get("应征凭证序号");
					ysid = map.get("ID");
					rk_rq = map.get("导入日期");
				} else if (drfs.equals("2")) {
					zsxm_mc = map.get("征收项目");
					sksx = map.get("税款属性");
					zse = map.get("应征税金额");
					yskm_mc = map.get("预算科目");
					ysfpbl_mc = map.get("预算分配比例");
					bl = map.get("区县级比例");
					nsrsbh = map.get("纳税人识别号");
					nsrmc = map.get("纳税人姓名");
					hy_mc = map.get("行业门类");
					jd_mc = map.get("街道乡镇");
					hy = map.get("行业");
					qxj = map.get("区县级");
					zspm = map.get("征收品目");
					zspmdm = map.get("征收品目代码");
					hydls = map.get("行业大类");
					hyzl = map.get("行业中类");
					yskmdms = map.get("预算科目代码");
					dzsphms = map.get("电子税票号码");
					djxhs = map.get("登记序号");
					yzpzxh = map.get("应征凭证序号");
					ysid = map.get("ID");
					rk_rq = map.get("导入日期");
				} else if (drfs.equals("3")) {
					zsxm_mc = map.get("征收项目");
					sksx = map.get("税款属性");
					zse = map.get("实缴金额");
					yskm_mc = map.get("预算科目");
					ysfpbl_mc = map.get("预算分配比例");
					bl = map.get("区县级比例");
					nsrsbh = map.get("社会信用代码（纳税人识别号）");
					nsrmc = map.get("纳税人名称");
					hy_mc = map.get("行业门类");
					jd_mc = map.get("街道乡镇");
					hy = map.get("行业");
					qxj = map.get("区县级");
					zspm = map.get("征收品目");
					zspmdm = map.get("征收品目代码");
					hydls = map.get("行业大类");
					hyzl = map.get("行业中类");
					yskmdms = map.get("预算科目代码");
					dzsphms = map.get("电子税票号码");
					djxhs = map.get("登记序号");
					yzpzxh = map.get("应征凭证序号");
					ysid = map.get("ID");
					rk_rq = map.get("导入日期");
				}
				System.out.println(Double.parseDouble(zse.replaceAll("\"", "").replaceAll(",", "")));
				if (zse != null && !zse.trim().equals("") && !zse.trim().equals("null")
						&& Double.parseDouble(zse.replaceAll("\"", "").replaceAll(",", "")) != 0) {
					zse = zse.replaceAll(",", "");

				} else {
					continue;
				}
				if (drfs.equals("1")) {
					if (zse.indexOf("-") >= 0) {
						zse.replace("-", "");
					} else {
						zse = "-" + zse;
					}
				}

				zse = zse.replaceAll(",", "").replaceAll("\"", "");

				nsrsbh = nsrsbh == null ? "" : nsrsbh;
				nsrmc = nsrmc == null ? "" : nsrmc;
				hy = hy == null ? "" : hy;
				hy_mc = hy_mc == null ? "" : hy_mc;
				yskm_mc = yskm_mc == null ? "" : yskm_mc;
				zsxm_mc = zsxm_mc == null ? "" : zsxm_mc;
				ysfpbl_mc = ysfpbl_mc == null ? "" : ysfpbl_mc;
				jd_mc = jd_mc == null ? "" : jd_mc;
				sksx = sksx == null ? "" : sksx;
				zspm = zspm == null ? "" : zspm;
				zspmdm = zspmdm == null ? "" : zspmdm;
				hydls = hydls == null ? "" : hydls;
				hyzl = hyzl == null ? "" : hyzl;
				yskmdms = yskmdms == null ? "" : yskmdms;
				dzsphms = dzsphms == null ? "" : dzsphms;
				djxhs = djxhs == null ? "" : djxhs;
				yzpzxh = yzpzxh == null ? "" : yzpzxh;
				if (bl != null && !bl.trim().equals("") && !bl.trim().equals("null")) {
					bl = bl.replaceAll(",", "");
				} else {
					bl = "100";
				}
				bl = bl.replaceAll(",", "").replaceAll("\"", "");
				if (qxj != null && !qxj.trim().equals("") && !qxj.trim().equals("null")) {
					qxj = qxj.replaceAll(",", "");
				} else {
					qxj = "0";
				}
				qxj = qxj.replaceAll(",", "").replaceAll("\"", "");

				num++;
				if (zsxm_mc.indexOf("企业所得税") >= 0 && sksx.indexOf("代扣代缴税款") >= 0) {
					nsrsbh = "";
					if (Double.parseDouble(zse) > 200000) {
						nsrmc = nsrmc + "(预提企业所得税)";
					} else {
						nsrmc = "预提企业所得税";
					}
					jd_mc = "外区";
					hy_mc = "";
				}

				if (drfs.equals("1") || drfs.equals("2")) {
					if (ysfpbl_mc.indexOf("区(县)级40") >= 0)
						bl = "40";
					if (ysfpbl_mc.indexOf("区(县)级100") >= 0)
						bl = "100";
					if (ysfpbl_mc.indexOf("区(县)级50") >= 0)
						bl = "50";
					if (ysfpbl_mc.indexOf("区(县)级20") >= 0)
						bl = "20";
				}

				stmt.setString(1, nsrsbh.replaceAll("\"", ""));
				stmt.setString(2, nsrmc.replaceAll("\"", ""));
				stmt.setString(3, hy_mc.replaceAll("\"", ""));
				stmt.setDouble(4, Double.parseDouble(zse.replaceAll("\"", "").replaceAll(",", "")));
				stmt.setString(5, zsxm_mc.replaceAll("\"", ""));
				stmt.setString(6, yskm_mc.replaceAll("\"", ""));
				stmt.setString(7, ysfpbl_mc.replaceAll("\"", ""));
				stmt.setString(8, jd_mc.replaceAll("\"", ""));
				stmt.setInt(9, Integer.parseInt(bl.replaceAll("\"", "").replaceAll(",", "")));
				stmt.setString(10, hy.replaceAll("\"", ""));
				stmt.setDouble(11, Double.parseDouble(qxj.replaceAll("\"", "").replaceAll(",", "")));
				stmt.setString(12, zspm.replaceAll("\"", ""));
				stmt.setString(13, zspmdm.replaceAll("\"", ""));
				stmt.setString(14, hydls.replaceAll("\"", ""));
				stmt.setString(15, hyzl.replaceAll("\"", ""));
				stmt.setString(16, yskmdms.replaceAll("\"", ""));
				stmt.setString(17, dzsphms.replaceAll("\"", ""));
				stmt.setString(18, djxhs.replaceAll("\"", ""));
				stmt.setString(19, yzpzxh.replaceAll("\"", ""));
				stmt.setString(20, sksx.replaceAll("\"", ""));
				stmt.setString(21, ysid.replaceAll("\"", ""));
				stmt.setString(22, drfs);
				stmt.setString(23, rk_rq.replaceAll("\"", ""));
				// stmt.setString(19, dzsphms.replaceAll("\"", ""));
				stmt.addBatch();
				// 注意: 每5万，提交一次;这里不能一次提交过多的数据,我测试了一下，6万5000是极
				// 限，6万6000就会出问题，插入的数据量不对。
				if (num % 10000 == 0) {
					stmt.executeBatch();
					connection.commit();
					num = 0;
				} else if (count == list.size()) {
					stmt.executeBatch();
					connection.commit();
				}

			}
			System.out.println(count1 + "        " + count);
			stmt.executeBatch();
			connection.commit();
			// 去除区县级比例为0的数据
			String sql_blisnull = "delete from xwcs_gsdr_temp where bl='0'";
			PreparedStatement stmt_blisnull = connection.prepareStatement(sql_blisnull);
			int num_blisnull = stmt_blisnull.executeUpdate();
			System.out.println("去除了" + num_blisnull + "条预算比例为0的数据");
			connection.commit();
			return "success";
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		} finally {
			// 释放资源
			close();
		}
	}

	public static Map<String, Map<String, String>> getTableInfo(String tableName) {
		Map<String, Map<String, String>> map = new LinkedHashMap<String, Map<String, String>>();
		if (connection == null) {
			connection = getConnection();
		}
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