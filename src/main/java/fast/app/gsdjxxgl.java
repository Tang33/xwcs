package fast.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

import fast.main.util.ExcelRead;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Super;
import fast.main.util.ZipUtil;

public class gsdjxxgl extends Super{
	private static Connection connection = null;
	private Map<String, Object> user = null;
	private static CallableStatement callableStatement = null;
	
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/gsdjxxgl";
		} catch (Exception e) {
			e.printStackTrace();
			return "xtgl/gsdjxxgl";
		}
	}
	
	public String query(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String pageNo = getValue(this.getForm().get("pageNo")).replaceAll("-", "");
			String pageSize = getValue(this.getForm().get("pageSize")).replaceAll("-", "");

			String sql = "select * from sy_drmx order by drsj desc";
			sql = "select * from (select row_.*, rownum rowno from (" + sql + ") row_ where rownum <= " + pageSize + "*"
					+ pageNo + ") a where a.rowno >= (" + pageNo + "- 1) * " + pageSize + " + 1";

			List<Map<String, Object>> mxlist = this.getBs().query(sql);
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < mxlist.size(); i++) {
				Map<String, Object> map = mxlist.get(i);
				map.put("wjm", map.get("WJM"));
				map.put("xzsl", map.get("XZSL"));
				map.put("qcsl", map.get("QCSL"));
				map.put("drsj", map.get("DRSJ"));
				lists.add(map);
			}

			// 获取count
			String cont = "";
			String sqlcot = "select count(*) from sy_drmx";
			List<Map<String, Object>> mxlistct = this.getBs().query(sqlcot);
			for (int m = 0; m < mxlistct.size(); m++) {

				Map<String, Object> mapcot = mxlistct.get(m);
				cont = mapcot.get("COUNT(*)").toString();
			}

			return this.toJsonct("000", "查询成功！", lists, cont);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
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
	
		// 将数据从excel取出并存到临时表
		public String doInputnew(Map<String, Object> rmap) {
			String filenameszip = "";
			initMap(rmap);
			connection = getConnection();
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String filenames = getValue(this.getForm().get("filenames"));
			String ybm = "";
			String sswj = "";
			String path = this.getRequest().getRealPath("/upload");

			// 压缩包
			if (filenames.endsWith(".zip")) {
				filenameszip = ZipUtil.unZipnew(path + "/", filenames.substring(0, filenames.indexOf("$_1$")));
				InputStream is = null;
				String filenamezip = "";
				String results = "";
				try {
					// 循环将获取到的excel文件一次读取
					if (filenameszip.contains("$_$")) {
						String[] filsp = filenameszip.split("\\$_\\$");
						for (int i = 0; i < filsp.length; i++) {
							filenamezip = filsp[i];
							System.out.println(filenamezip);
							ybm = new File(filenamezip).getName();
							sswj = System.currentTimeMillis() + ybm.substring(ybm.lastIndexOf("."), ybm.length());

							results = insttempnew(is,filenamezip, ybm, sswj,rmap);

						}
					} else {
						filenamezip = filenameszip.split("\\$_1\\$")[0];
						ybm = filenamezip.split("\\$_1\\$")[1];
						sswj = System.currentTimeMillis()
								+ filenamezip.substring(filenamezip.lastIndexOf("."), filenamezip.length());

						results = insttempnew(is,filenamezip, ybm, sswj,rmap);
					}
					return this.toJson("000", "查询成功！", results);

				} catch (Exception e) {
					e.printStackTrace();
					return this.toJson("009", "查询失败！");
				}
			} else if (filenames.endsWith(".rar")) {
				return "";
			} else {
				InputStream is = null;
				String filename = "";
				String results = "";
				try {
					// 循环将获取到的excel文件一次读取（多文件）
					if (filenames.contains("$_$")) {
						String[] filsp = filenames.split("\\$_\\$");
						for (int i = 0; i < filsp.length; i++) {
							filename = filsp[i];
							ybm = new File(filename).getName();
							sswj = System.currentTimeMillis() + ybm.substring(ybm.lastIndexOf("."), ybm.length());
							results = insttempnew(is,filename, ybm, sswj,rmap);
						}
					} else {
						filename = filenames.split("\\$_1\\$")[0];
						ybm = filenames.split("\\$_1\\$")[1];
						sswj = System.currentTimeMillis()
								+ filename.substring(filename.lastIndexOf("."), filename.length());

						results = insttempnew(is,filename, ybm, sswj,rmap);
					}
					return this.toJson("000", "查询成功！", results);
				} catch (Exception e) {
					e.printStackTrace();
					return this.toJson("009", "查询失败！");
				} finally {
					close();
				}
			}
		}
		
		public static void close() {
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
		
		// 根据导入方式选择不同的模板（0：入库，1：退库，2：个人所得税）
		private String insttempnew(InputStream is, String filename, String ybm, String sswj,Map<String,Object> rmap) {
			initMap(rmap);
			Map<String, Integer> result = null;
			try {
				is = new FileInputStream(new File(filename));
				// 判断接收到的文件格式
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put("登记表单展示", -1);
				map.put("纳税人联系信息（有独立查询功能）", -1);
				map.put("社会信用代码（纳税人识别号）", -1);
				map.put("纳税人名称", -1);
				map.put("纳税人状态", -1);
				map.put("课征主体登记类型", -1);
				map.put("登记注册类型", -1);
				map.put("组织机构", -1);
				map.put("国地管户类型", -1);
				map.put("单位隶属关系", -1);
				map.put("批准设立机关", -1);
				map.put("证照名称", -1);
				map.put("证照编号", -1);
				map.put("开业设立日期", -1);
				map.put("从业人数", -1);
				map.put("固定工人数", -1);
				map.put("组织机构类型", -1);
				map.put("会计制度（准则）", -1);
				map.put("经营范围", -1);
				map.put("行业", -1);
				map.put("登记机关", -1);
				map.put("登记日期", -1);
				map.put("主管税务机关", -1);
				map.put("主管税务所（科、分局）", -1);
				map.put("税收管理员", -1);
				map.put("街道乡镇", -1);
				map.put("国有控股类型", -1);
				map.put("国有投资比例", -1);
				map.put("自然人投资比例", -1);
				map.put("外资投资比例", -1);
				map.put("注册资本", -1);
				map.put("投资总额", -1);
				map.put("营改增纳税人类型", -1);
				map.put("办证方式", -1);
				map.put("核算方式", -1);
				map.put("非居民企业标志", -1);
				map.put("跨区财产税主体登记标志", -1);
				map.put("有效标志", -1);
				map.put("注册地址行政区划", -1);
				map.put("注册地址", -1);
				map.put("注册地联系电话", -1);
				map.put("生产经营地址行政区划", -1);
				map.put("生产经营地址", -1);
				map.put("生产经营地联系电话", -1);
				map.put("法定代表人姓名", -1);
				map.put("法定代表人身份证件类型", -1);
				map.put("法定代表人身份证号码", -1);
				map.put("法定代表人固定电话", -1);
				map.put("法定代表人移动电话", -1);
				map.put("财务负责人姓名", -1);
				map.put("财务负责人身份证件号码", -1);
				map.put("财务负责人固定电话", -1);
				map.put("财务负责人移动电话", -1);
				map.put("办税人姓名", -1);
				map.put("办税人身份证件号码", -1);
				map.put("办税人固定电话", -1);
				map.put("办税人移动电话", -1);
				map.put("录入人", -1);
				map.put("录入日期", -1);
				map.put("修改人", -1);
				map.put("修改日期", -1);
				map.put("纳税人编号", -1);
				map.put("税收档案编号", -1);
				map.put("社会信用代码", -1);
				map.put("原纳税人识别号", -1);
				map.put("评估机关", -1);
				map.put("工商注销日期", -1);
				map.put("是否三证合一或两证整合纳税人", -1);
				map.put("受理信息", -1);
				map.put("总分机构类型", -1);
				map.put("总机构信息", -1);
				map.put("分支机构信息", -1);
				map.put("跨区域涉税事项报验管理编号", -1);
				map.put("纳税人主体类型", -1);
				map.put("登记户自定义类别", -1);
				map.put("民营企业", -1);
				// 读取excel文件
				List<Map<String, String>> list = ExcelRead.pomExcel(filename, is, map);
				result = JdbcConnectedPro.insertAll_sy(list, sswj);
				insertSWXX sy = new insertSWXX();
				sy.querySWDJXQ(rmap);
				sy.queryDJ(rmap);
				sy.queryDJRQ(rmap);
				sy.queryHYLX(rmap);
				sy.queryJD(rmap);
				String sqlselect = "insert into sy_drmx values (?,?,?,?,?)";
				PreparedStatement stmt = connection.prepareStatement(sqlselect);
				stmt.setString(1, ybm);
				stmt.setString(2, sswj);
				stmt.setDouble(3, result.get("NUM"));
				stmt.setDouble(4, result.get("RENUM"));
				stmt.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
				stmt.execute();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return "";
		}
	
}
