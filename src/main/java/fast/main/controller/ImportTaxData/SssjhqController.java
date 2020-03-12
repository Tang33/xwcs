package fast.main.controller.ImportTaxData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import fast.main.service.BaseService;
import fast.main.util.ExcelRead;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Super;
import fast.main.util.ZipUtil;

/**
 * 税收数据获取页面
 * 本功能页面用于对外部数据导入，请选择入库年月、导入方式、全量/断点、选择上传的数据文件，点击上传比对进行数据的导入!
 */

@Controller
@RequestMapping("sssjhq")
public class SssjhqController extends Super{
	
	@Autowired 
	BaseService bs;
	
	private static Connection connection = null;
	private static CallableStatement callableStatement = null;
	
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "ImportTaxData/Sssjhq";
		} catch (Exception e) {
			e.printStackTrace();
			return "ImportTaxData/Sssjhq";
		}
	}
	

	/**
	 * 获取最新的时间
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping("/getNsDate.do")
	@ResponseBody
	public String getNsDate(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, String> form) {
		try {
			String sql = "SELECT to_char(max(RK_RQ),'yyyyMM') as rkrq FROM SB_NSRXX ORDER BY RK_RQ DESC  ";
			List<Map<String, Object>> result = bs.query(sql);
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
	
	/**
	 * 判断要导入月的数据是否加工过
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping("/isjgg.do")
	@ResponseBody
	public String isjgg(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, String> form) {
		try {
			String rkrq = getValue(form.get("rkrq"));		//获取入库日期
			String sql = "select count(*) zs from sb_nsrxx where to_char(rk_rq,'yyyyMM') = ? ";
			sql = getSql2(sql, new Object[] {rkrq});
			List<Map<String, Object>> query = bs.query(sql);
			if(query.size() > 0 && !"0".equals(getValue(query.get(0).get("ZS")))) {
				return this.toJson("000", "加工过");
			}else {
				return this.toJson("001", "未加工过");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}


	/**
	 * 	 将数据从excel取出并存到临时表
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping("/doInputnew.do")
	@ResponseBody
	public String doInputnew(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, String> form) throws ServletException, IOException {
		String filenameszip = "";
		connection = getConnection();
		String filenames = getValue(form.get("filenames"));		//获取上传excel文件名
		String ybm = "";
		String sswj = "";
		String rkrq = getValue(form.get("rkrq"));		//获取入库日期
		String drfs = getValue(form.get("drfs"));		//获取导入方式
		String scfs = getValue(form.get("an"));			//获取上传方式
		String path = request.getRealPath("/upload");	//获取上传方法
//		String sql = "select * from sb_nsrxx where to_char(rk_rq,'yyyyMM') = ?";
//		sql = getSql2(sql, new Object[] {rkrq});
//		int queryCount = bs.queryCount(sql);
//		if(queryCount > 0) {
//			return this.toJson("003", "已加工过本月的数据!");
//		}
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
			bs.delete("delete from " + tableName + " where  RK_RQ = '" + rkrq + "'");
			bs.delete("delete from xwcs_gsdr_temp where drfs = '" + drfs + "' and RK_RQ = '" + rkrq + "'");
		}
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
						ybm = new File(filenamezip).getName();
						sswj = System.currentTimeMillis() + ybm.substring(ybm.lastIndexOf("."), ybm.length());
						results = insttempnew(is, drfs, filenamezip, ybm, sswj, scfs, rkrq);
					}
				} else {
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
			long  starttime= System.currentTimeMillis();
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
				long  endtime= System.currentTimeMillis();
//				System.out.println("导入耗时:"+(endtime-starttime));
				return this.toJson("000", "查询成功！", results);
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败！");
			} finally {
				close();
			}
		}
	}

	/**
	 * 根据导入方式选择不同的模板（0：入库，1：退库，2：个人所得税）
	 */
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
			
			String qcsql = " delete from "+tableName+" where id in( select id from "+tableName+" WHERE (sjzw) IN ( SELECT sjzw FROM "+tableName+
					"     where rk_rq='"+rkrq+"'  GROUP BY sjzw HAVING COUNT(sjzw) > 1) AND ROWID NOT"
					+ " IN (SELECT MIN(ROWID) FROM "+tableName+"  where rk_rq='"+rkrq+"' GROUP BY sjzw HAVING COUNT(*) > 1))";
			System.out.println(qcsql);
			Integer delete = bs.delete(qcsql);
			System.out.println("去除重复数量:"+delete);
			result.put("RENUM", delete);
			String sqlselect = "insert into fast_drmx values (?,?,?,?,sysdate,?,?)";
			String bm ="录入数据";
			if (drfs.equals("0")) {
				bm = rkrq + "月入库数据";
			} else if (drfs.equals("1")) {
				bm = rkrq + "月退库数据";
			} else if (drfs.equals("2")) {
				bm = rkrq + "月个人所得税数据";
			} else if (drfs.equals("3")) {
				bm = rkrq + "月净入库数据";
			}
			sqlselect = getSql2(sqlselect, new Object[] {ybm,sswj,result.get("NUM"),result.get("RENUM"),
					rkrq,bm	
			});
			bs.insert(sqlselect);
//			PreparedStatement stmt = connection.prepareStatement(sqlselect);
//			stmt.setString(1, ybm);
//			stmt.setString(2, sswj);
//			stmt.setDouble(3, result.get("NUM"));
//			stmt.setDouble(4, result.get("RENUM"));
//			stmt.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
//			stmt.setString(6, rkrq);
//			if (drfs.equals("0")) {
//				stmt.setString(7, rkrq + "月入库数据");
//			} else if (drfs.equals("1")) {
//				stmt.setString(7, rkrq + "月退库数据");
//			} else if (drfs.equals("2")) {
//				stmt.setString(7, rkrq + "月个人所得税数据");
//			} else if (drfs.equals("3")) {
//				stmt.setString(7, rkrq + "月净入库数据");
//			}
//			stmt.execute();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  finally {
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
	
	/**
	 * 将excel表格中的数据导入临时表和对应的原始数据表
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping("/dr.do")
	@ResponseBody
	public String dr(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, String> form) {
		try {
			String rkrq = getValue(form.get("rkrq")).replaceAll("-", "");
			String deleteSql = "delete from sb_nsrxx s where s.gds='0' and to_char(rk_rq,'yyyyMM')='" + rkrq + "'";
			System.out.println(deleteSql);
			bs.delete(deleteSql);
			String istSql = "insert into sb_nsrxx(xh,dzsphm,nsrmc,nsrsbh,rk_rq,jd_dm,hy_dm,zzs,ygzzzs,yys,qysds_gs,qysds_ds,qysds,grsds,fcs,yhs,ccs,cswhjss,dfjyfj,jyfj,cztdsys,hbs,zse,dfzse,qyxz,hhnsrmc,lrry_dm,lr_sj,gds,bl,hydl,hyzl,yskmdm,djxhs) ";
			istSql += "  select seq_sb_nsrxx.nextval xh,s.*"
					+ "from (select distinct z.dzsphm,z.nsrmc,z.nsrsbh,z.rk_rq,z.jd_dm,z.hy_dm,"
					+ "     sum(decode(z.zsxm, '增值税', zse, 0)) as zzs,"
					+ "     sum(decode(zsxm, '营改增增值税', zse, 0)) as ygz,"
					+ "     sum(decode(zsxm, '营业税', zse, 0)) as yys,"
					+ "     sum(decode(zsxm, '企业所得税国税', zse, 0)) as qysdsgs,"
					+ "     sum(decode(zsxm, '企业所得税', zse, 0)) as qysdsds,"
					+ "     (sum(decode(zsxm, '企业所得税国税', zse, 0))+sum(decode(zsxm, '企业所得税', zse, 0))) as qysds,"
					+ "     sum(decode(zsxm, '个人所得税', zse, 0)) as grsds,"
					+ "     sum(decode(zsxm, '房产税', zse, 0)) as fcs," + "     sum(decode(zsxm, '印花税', zse, 0)) as yhs,"
					+ "     sum(decode(zsxm, '车船税', zse, 0)) as ccs,"
					+ "     sum(decode(zsxm, '城市维护建设税', zse, 0)) as cswhjss,"
					+ "     sum(decode(zsxm, '地方教育附加', zse, 0)) as dfjyfj,"
					+ "     sum(decode(zsxm, '教育费附加', zse, 0)) as jyfj,"
					+ "     sum(decode(zsxm, '城镇土地使用税', zse, 0)) as cztdsys,"
					+ "     sum(decode(zsxm, '环境保护税', zse, 0)) as hbs," + "     sum(zse) zse,"
					+ "     sum(z.sjse) dfzse," + "     z.qyxz," + "     z.nsrmc hhnsrmc," + "     z.lrry_dm,"
					+ "     z.lr_sj," + "     gds,bl,z.hydl,z.hyzl,z.yskmdm,z.djxhs" + " from sb_zsxx z"
					+ " where to_char(rk_rq, 'yyyyMM') = '" + rkrq + "'"
					+ " and gds = '0' group by nsrmc,nsrsbh,rk_rq,jd_dm,hy_dm,z.qyxz,z.lrry_dm,z.lr_sj,gds,bl,z.hydl,z.hyzl,z.yskmdm,z.dzsphm,z.djxhs) s ";
			System.out.println(istSql);
			bs.insert(istSql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}

	}
	
	/**
	 * 查询导入明细
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping("/query1.do")
	@ResponseBody
	public String query(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, String> form) {
		
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String pageNo = getValue(form.get("pageNo")).replaceAll("-", "");	
			String pageSize = getValue(form.get("pageSize")).replaceAll("-", "");
			String sql = "select * from fast_drmx where 1 = 1   order by drsj  desc";
			List<Map<String, Object>> mxlist = bs.query(sql,pageNo,pageSize);
			// 获取count
			String cont = "";
			String sqlcot = "select count(*) from fast_drmx";
			List<Map<String, Object>> mxlistct = bs.query(sqlcot);
			for (int m = 0; m < mxlistct.size(); m++) {

				Map<String, Object> mapcot = mxlistct.get(m);
				cont = mapcot.get("COUNT(*)").toString();
			}

			return this.toJsonct("000", "查询成功！", mxlist, cont);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	/**
	 * 备份sb_zsxx表，根据条件修改sb_zsxx表中的数据
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping("/Update.do")
	@ResponseBody
	public String Update(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, String> form) {
		try {
			String ids = getValue(form.get("ids"));			//获取
			String jddm = getValue(form.get("jddm"));		//获取街道代码
			String jdmc = getValue(form.get("jdmc"));		//获取街道名称
			String nsrmc = getValue(form.get("nsrmc"));		//获取纳税人名称
			if (ids.equals("") || (nsrmc.equals("") && jddm.equals(""))) {
				return this.toJson("001", "修改失败！");
			}
			ids = ids.substring(0, ids.length() - 1);
			System.out.println(ids);
			String deletesql = "delete from sb_zsxx_bf where xh in(" + ids + ") ";
			bs.delete(deletesql);
			String bfsql = " insert into sb_zsxx_bf select * from sb_zsxx where xh in (" + ids + ")";
			bs.insert(bfsql);
			String sql = "update sb_zsxx set SFBG='1' ";
			if (!nsrmc.equals("")) {
				sql += ",NSRMC='" + nsrmc + "'";
			}
			if (!jddm.equals("")) {
				sql += ",JD_DM='" + jddm + "',JD_MC='" + jdmc + "' ";
			}
			sql += "where xh in(" + ids + ")";
			System.out.println(sql);
			bs.update(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	@RequestMapping("/UpdateAll.do")
	@ResponseBody
	public String UpdateAll(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, String> form) {
		try {
			String jddm = getValue(form.get("jddm"));		//获取街道代码
			String nsrmc = getValue(form.get("nsrmc"));		//获取纳税人名称
			String rkrq = getValue(form.get("rkrq"));		//获取入库日期
			String gjz = getValue(form.get("gjz"));			//
			String jdmc = getValue(form.get("jdmc"));		//获取街道名称
			String hylist = getValue(form.get("hylist"));	//获取行业
			String jdlist = getValue(form.get("jdlist"));	//获取街道
			String zslist = getValue(form.get("zslist"));	
			if (nsrmc.equals("") && jddm.equals("")) {
				return this.toJson("001", " 请输入要修改的值！");
			}
			String deletesql = "delete from sb_zsxx_bf where 1=1 ";
			if (rkrq != null && rkrq != "") {
				deletesql += " and rk_rq = to_date('" + rkrq + "','yyyyMM') ";
			}
			if (gjz != null && gjz != "") {
				deletesql += " and ( DJXHS LIKE '%" + gjz + "%' or DZSPHM LIKE '%" + gjz + "%' OR YZPZXH LIKE '%" + gjz
						+ "%' or NSRSBH LIKE '%" + gjz + "%' or NSRMC LIKE '%" + gjz + "%' ) ";
			}
			if (hylist != null && hylist != "") {
				deletesql += " and HY LIKE '%" + hylist + "' ";
			}
			if (jdlist != null && jdlist != "") {
				deletesql += " and JD_DM LIKE '%" + jdlist + "' ";
			}
			if (zslist != null && zslist != "") {
				deletesql += " and ZSXM LIKE '%" + zslist + "' ";
			}
			bs.delete(deletesql);
			String bfsql = " insert into sb_zsxx_bf select * from sb_zsxx where 1=1 ";
			if (rkrq != null && rkrq != "") {
				bfsql += " and rk_rq = to_date('" + rkrq + "','yyyyMM') ";
			}

			if (gjz != null && gjz != "") {
				bfsql += " and ( DJXHS LIKE '%" + gjz + "%' or DZSPHM LIKE '%" + gjz + "%' OR YZPZXH LIKE '%" + gjz
						+ "%' or NSRSBH LIKE '%" + gjz + "%' or NSRMC LIKE '%" + gjz + "%' ) ";
			}
			if (hylist != null && hylist != "") {
				bfsql += " and HY LIKE '%" + hylist + "' ";
			}
			if (jdlist != null && jdlist != "") {
				bfsql += " and JD_DM LIKE '%" + jdlist + "' ";
			}
			if (zslist != null && zslist != "") {
				bfsql += " and ZSXM LIKE '%" + zslist + "' ";
			}
			bs.insert(bfsql);
			String sql = "update sb_zsxx set SFBG='1' ";
			if (!nsrmc.equals("")) {
				sql += ",NSRMC='" + nsrmc + "'";
			}
			if (!jddm.equals("")) {
				sql += ",JD_DM='" + jddm + "',JD_MC='" + jdmc + "' ";
			}
			sql += " where 1=1 ";
			if (rkrq != null && rkrq != "") {
				sql += " and rk_rq = to_date('" + rkrq + "','yyyyMM') ";
			}

			if (gjz != null && gjz != "") {
				sql += " and ( DJXHS LIKE '%" + gjz + "%' or DZSPHM LIKE '%" + gjz + "%' OR YZPZXH LIKE '%" + gjz
						+ "%' or NSRSBH LIKE '%" + gjz + "%' or NSRMC LIKE '%" + gjz + "%' ) ";
			}
			if (hylist != null && hylist != "") {
				sql += " and HY LIKE '%" + hylist + "' ";
			}
			if (jdlist != null && jdlist != "") {
				sql += " and JD_DM LIKE '%" + jdlist + "' ";
			}
			if (zslist != null && zslist != "") {
				sql += " and ZSXM LIKE '%" + zslist + "' ";
			}
			bs.update(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
}
