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
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
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
import org.aspectj.weaver.ast.Var;

import fast.main.util.ExcelRead;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;
import fast.main.util.ZipUtil;
import oracle.sql.DATE;

public class Sjdr extends Super {
	private static Connection connection = null;
	private Map<String, Object> user = null;
	private static CallableStatement callableStatement = null;

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");

			return "sssjgl/Sjdr";
		} catch (Exception e) {
			e.printStackTrace();
			return "sssjgl/Sjdr";
		}
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

		String rkrq = getValue(this.getForm().get("rkrq"));
		String drfs = getValue(this.getForm().get("drfs"));
		String scfs = getValue(this.getForm().get("an"));
		String path = this.getRequest().getRealPath("/upload");

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

			this.getBs().delete("delete from " + tableName + " where  RK_RQ = '" + rkrq + "'");
			this.getBs().delete("delete from xwcs_gsdr_temp where drfs = '" + drfs + "' and RK_RQ = '" + rkrq + "'");
			// cs.executeUpdate("truncate table " + tableName);
			// connection.commit();
			// cs.executeUpdate("delete from xwcs_gsdr_temp where drfs = '" +
			// drfs + "'");
			// connection.commit();
		}

		// 压缩包
		if (filenames.endsWith(".zip")) {
			// ZipUtil.unZip(new File(filenames), filenames);

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

						results = insttempnew(is, drfs, filenamezip, ybm, sswj, scfs, rkrq);

					}
				} else {
					/*
					 * filenamezip = filenames; ybm = new File(filenamezip).getName(); sswj =
					 * System.currentTimeMillis() +
					 * ybm.substring(ybm.lastIndexOf("."),ybm.length());
					 */
					filenamezip = filenameszip.split("\\$_1\\$")[0];
					ybm = filenamezip.split("\\$_1\\$")[1];
					sswj = System.currentTimeMillis()
							+ filenamezip.substring(filenamezip.lastIndexOf("."), filenamezip.length());

					results = insttempnew(is, drfs, filenamezip, ybm, sswj, scfs, rkrq);
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
						results = insttempnew(is, drfs, filename, ybm, sswj, scfs, rkrq);
					}
				} else {
					filename = filenames.split("\\$_1\\$")[0];
					ybm = filenames.split("\\$_1\\$")[1];
					sswj = System.currentTimeMillis()
							+ filename.substring(filename.lastIndexOf("."), filename.length());

					results = insttempnew(is, drfs, filename, ybm, sswj, scfs, rkrq);
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

	// 根据导入方式选择不同的模板（0：入库，1：退库，2：个人所得税）
	private String insttempnew(InputStream is, String drfs, String filename, String ybm, String sswj, String scfs,
			String rkrq) {
		Map<String, Integer> result = null;
		try {
			is = new FileInputStream(new File(filename));
			// 判断接收到的文件格式
			Map<String, Integer> map = new HashMap<String, Integer>();
			if (drfs != null) {
				if (drfs.equals("0")) {

					map.put("社会信用代码（纳税人识别号）", -1);
					map.put("纳税人名称", -1);
					map.put("征收项目", -1);
					map.put("征收品目", -1);
					map.put("征收品目代码", -1);
					map.put("税款所属期起", -1);
					map.put("税款所属期止", -1);
					map.put("登记注册类型", -1);
					map.put("税收管理员", -1);
					map.put("行业门类", -1);
					map.put("行业大类", -1);
					map.put("行业中类", -1);
					map.put("行业", -1);
					map.put("征收代理方式", -1);
					map.put("计税依据", -1);
					map.put("课税数量", -1);
					map.put("税率", -1);
					map.put("实缴金额", -1);
					map.put("税款所属税务机关", -1);
					map.put("主管税务所（科、分局）", -1);
					map.put("预算科目代码", -1);
					map.put("预算科目", -1);
					map.put("预算分配比例", -1);
					map.put("中央级比例", -1);
					map.put("省市级比例", -1);
					map.put("地市级比例", -1);
					map.put("区县级比例", -1);
					map.put("乡镇级比例", -1);
					map.put("中央待分配比例", -1);
					map.put("地市待分配比例", -1);
					map.put("省局待分配比例", -1);
					map.put("收款国库", -1);
					map.put("开具日期", -1);
					map.put("上解日期", -1);
					map.put("上解销号日期", -1);
					map.put("上解销号人", -1);
					map.put("入库日期", -1);
					map.put("入库销号日期", -1);
					map.put("入库销号人", -1);
					map.put("票证种类", -1);
					map.put("票证字轨", -1);
					map.put("票证号码", -1);
					map.put("税款种类", -1);
					map.put("税款属性", -1);
					map.put("街道乡镇", -1);
					map.put("银行行别", -1);
					map.put("银行营业网点", -1);
					map.put("银行账号", -1);
					map.put("汇总日期", -1);
					map.put("汇总人", -1);
					map.put("汇总票证种类", -1);
					map.put("汇总票证字轨", -1);
					map.put("汇总票证号码", -1);
					map.put("调账类型", -1);
					map.put("缴款期限", -1);
					map.put("征收税务机关", -1);
					map.put("电子税票号码", -1);
					map.put("中央级", -1);
					map.put("省市级", -1);
					map.put("地市级", -1);
					map.put("区县级", -1);
					map.put("乡镇级", -1);
					map.put("中央待分配", -1);
					map.put("省级待分配", -1);
					map.put("地市待分配", -1);
					map.put("登记序号", -1);
					map.put("总分机构类型", -1);
					map.put("跨地区税收转移企业类型", -1);
					map.put("查补属性", -1);
					map.put("应征凭证种类", -1);
					map.put("税源编号", -1);
					map.put("扣缴纳税人信息", -1);
					map.put("应征凭证序号", -1);
					map.put("开票人", -1);
					map.put("欠税标记", -1);

				} else if (drfs.equals("1")) {
					map.put("社会信用代码（纳税人识别号）", -1);
					map.put("纳税人名称", -1);
					map.put("票证种类", -1);
					map.put("票证字轨", -1);
					map.put("票证号码", -1);
					map.put("征收项目", -1);
					map.put("征收品目", -1);
					map.put("税款所属期起", -1);
					map.put("细目名称", -1);
					map.put("税款所属期止", -1);
					map.put("税款种类", -1);
					map.put("税款属性", -1);
					map.put("登记注册类型", -1);
					map.put("行业门类", -1);
					map.put("行业大类", -1);
					map.put("行业中类", -1);
					map.put("行业", -1);
					map.put("税额", -1);
					map.put("预算科目", -1);
					map.put("预算科目名称", -1);
					map.put("预算分配比例", -1);
					map.put("收款国库", -1);
					map.put("提退税金类型", -1);
					map.put("退抵税（费）依据文书字号", -1);
					map.put("银行营业网点", -1);
					map.put("账户名称", -1);
					map.put("银行账号", -1);
					map.put("开票日期", -1);
					map.put("退还日期", -1);
					map.put("销号日期", -1);
					map.put("销号人", -1);
					map.put("街道乡镇", -1);
					map.put("提退机构", -1);
					map.put("税收管理员", -1);
					map.put("税款所属税务机关", -1);
					map.put("主管税务所（科、分局）", -1);
					map.put("税务机关", -1);
					map.put("退抵税费原因类型", -1);
					map.put("电子税票号码", -1);
					map.put("调账类型", -1);
					map.put("操作类型", -1);
					map.put("税收减免性质大类", -1);
					map.put("税收减免性质小类", -1);
					map.put("复核结果", -1);
					map.put("录入人", -1);
					map.put("开票人", -1);
					map.put("录入日期", -1);

				} else if (drfs.equals("2")) {
					map.put("序号", -1);
					map.put("纳税人识别号", -1);
					map.put("纳税人姓名", -1);
					map.put("身份证件类型", -1);
					map.put("身份证件号码", -1);
					map.put("登记注册类型", -1);
					map.put("征收项目", -1);
					map.put("征收品目", -1);
					map.put("应征税金额", -1);
					map.put("税款状态", -1);
					map.put("申报日期", -1);
					map.put("开具日期", -1);
					map.put("上解日期", -1);
					map.put("入库日期", -1);
					map.put("税款所属期起", -1);
					map.put("税款所属期止", -1);
					map.put("税款属性", -1);
					map.put("税款缴纳方式", -1);
					map.put("预算科目", -1);
					map.put("预算分配比例", -1);
					map.put("收款国库", -1);
					map.put("票证种类", -1);
					map.put("电子税票号码", -1);
					map.put("开票人", -1);
					map.put("上解销号人", -1);
					map.put("征收机关", -1);
					map.put("<SPAN title=主管税务所（科，分局）>主管税务所（科，分局）</SPAN>", -1);
					map.put("街道乡镇", -1);
					map.put("<SPAN title=税款所属税务机关>税款所属税务机关</SPAN>", -1);
					map.put("应征凭证种类", -1);
					map.put("<SPAN title=现金汇总凭证号码>现金汇总凭证号码</SPAN>", -1);

				} else if (drfs.equals("3")) {

					map.put("社会信用代码（纳税人识别号）", -1);
					map.put("纳税人名称", -1);
					map.put("征收项目", -1);
					map.put("征收品目", -1);
					map.put("征收品目代码", -1);
					map.put("税款所属期起", -1);
					map.put("税款所属期止", -1);
					map.put("登记注册类型", -1);
					map.put("税收管理员", -1);
					map.put("行业门类", -1);
					map.put("行业大类", -1);
					map.put("行业中类", -1);
					map.put("行业", -1);
					map.put("征收代理方式", -1);
					map.put("计税依据", -1);
					map.put("课税数量", -1);
					map.put("税率", -1);
					map.put("实缴金额", -1);
					map.put("税款所属税务机关", -1);
					map.put("主管税务所（科、分局）", -1);
					map.put("预算科目代码", -1);
					map.put("预算科目", -1);
					map.put("预算分配比例", -1);
					map.put("中央级比例", -1);
					map.put("省市级比例", -1);
					map.put("地市级比例", -1);
					map.put("区县级比例", -1);
					map.put("乡镇级比例", -1);
					map.put("中央待分配比例", -1);
					map.put("地市待分配比例", -1);
					map.put("省局待分配比例", -1);
					map.put("收款国库", -1);
					map.put("开具日期", -1);
					map.put("上解日期", -1);
					map.put("上解销号日期", -1);
					map.put("上解销号人", -1);
					map.put("入库日期", -1);
					map.put("入库销号日期", -1);
					map.put("入库销号人", -1);
					map.put("票证种类", -1);
					map.put("票证字轨", -1);
					map.put("票证号码", -1);
					map.put("税款种类", -1);
					map.put("税款属性", -1);
					map.put("街道乡镇", -1);
					map.put("银行行别", -1);
					map.put("银行营业网点", -1);
					map.put("银行账号", -1);
					map.put("汇总日期", -1);
					map.put("汇总人", -1);
					map.put("汇总票证种类", -1);
					map.put("汇总票证字轨", -1);
					map.put("汇总票证号码", -1);
					map.put("调账类型", -1);
					map.put("缴款期限", -1);
					map.put("征收税务机关", -1);
					map.put("电子税票号码", -1);
					map.put("中央级", -1);
					map.put("省市级", -1);
					map.put("地市级", -1);
					map.put("区县级", -1);
					map.put("乡镇级", -1);
					map.put("中央待分配", -1);
					map.put("省级待分配", -1);
					map.put("地市待分配", -1);
					map.put("登记序号", -1);
				}
			}

			// 读取excel文件
			List<Map<String, String>> list = ExcelRead.pomExcel(filename, is, map);
			// List<String[]> list = XLSXCovertCSVReader.readerExcel(is,
			// "Sheet1", 100);
			// result = JdbcConnectedPro.insertAllnew(list, drfs);
			result = JdbcConnectedPro.insertAll_new(list, drfs, sswj, scfs, rkrq);
			String tableName="";
			if (drfs.equals("0")) {
				tableName = "xwcs_gsdr_yssjrk";
			} else if (drfs.equals("1")) {
				tableName = "xwcs_gsdr_yssjtk";
			} else if (drfs.equals("2")) {
				tableName = "xwcs_gsdr_yssjgs";
			} else if (drfs.equals("3")) {
				tableName = "xwcs_gsdr_yssjjrk";
			}
			String qcsql = " delete from "+tableName+" where id in( select id from "+tableName+" WHERE (sjzw) IN ( SELECT sjzw FROM "+tableName+" GROUP BY sjzw HAVING COUNT(sjzw) > 1) AND ROWID NOT IN (SELECT MIN(ROWID) FROM "+tableName+" GROUP BY sjzw HAVING COUNT(*) > 1))";
			result.put("RENUM", this.getBs().delete(qcsql));

			String sqlselect = "insert into fast_drmx values (?,?,?,?,to_date(?,'yyyy-MM-dd HH24:mi:ss'),?,?)";
			PreparedStatement stmt = connection.prepareStatement(sqlselect);
			stmt.setString(1, ybm);
			stmt.setString(2, sswj);
			stmt.setDouble(3, result.get("NUM"));
			stmt.setDouble(4, result.get("RENUM"));
			stmt.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
			stmt.setString(6, rkrq);
			if (drfs.equals("0")) {
				stmt.setString(7, rkrq + "月入库数据");
			} else if (drfs.equals("1")) {
				stmt.setString(7, rkrq + "月退库数据");
			} else if (drfs.equals("2")) {
				stmt.setString(7, rkrq + "月个人所得税数据");
			} else if (drfs.equals("3")) {
				stmt.setString(7, rkrq + "月净入库数据");
			}
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

	// 获取纳税人信息表中当前最新日期
	public String getNsDate(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String sql = "SELECT to_char(max(RK_RQ),'yyyyMM') as rkrq FROM SB_NSRXX ORDER BY RK_RQ DESC  ";

			List<Map<String, Object>> result = this.getBs().query(sql);
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < result.size(); i++) {
				Map<String, Object> map = result.get(i);
				lists.add(map);
			}

			return this.toJson("000", "查询成功！", lists);

		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	public String query(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);

			String pageNo = getValue(this.getForm().get("pageNo")).replaceAll("-", "");
			String pageSize = getValue(this.getForm().get("pageSize")).replaceAll("-", "");

			String sql = "select * from fast_drmx order by drsj desc";
			sql = "select * from (select row_.*, rownum rowno from (" + sql + ") row_ where rownum <= " + pageSize + "*"
					+ pageNo + ") a where a.rowno >= (" + pageNo + "- 1) * " + pageSize + " + 1";

			List<Map<String, Object>> mxlist = this.getBs().query(sql);
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < mxlist.size(); i++) {
				Map<String, Object> map = mxlist.get(i);
				map.put("wjm", map.get("WJM"));
				map.put("wjdrsl", map.get("WJDRSL"));
				map.put("qcsl", map.get("QCSL"));
				map.put("drsj", map.get("DRSJ"));
				map.put("rkrq", map.get("RK_RQ"));
				map.put("bz", map.get("BZ"));
				lists.add(map);
			}

			// 获取count
			String cont = "";
			String sqlcot = "select count(*) from fast_drmx";
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

}
