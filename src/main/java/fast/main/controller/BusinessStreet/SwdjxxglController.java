package fast.main.controller.BusinessStreet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mysql.fabric.xmlrpc.base.Array;

import fast.app.User;
import fast.app.insertSWXX;
import fast.main.service.BaseService;
import fast.main.util.DruidUtil;
import fast.main.util.ExcelRead;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Super;
import fast.main.util.SwdjInsert;
import fast.main.util.ZipUtil;

/**
 *  税务登记信息管理
 *  本功能页面用于对税务数据导入，请选择上传的数据文件，点击上传进行数据的导入!
 *
 */
@Controller
@RequestMapping("swdjxxgl")
public class SwdjxxglController extends Super{
	@Autowired
	BaseService bs;
	
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "BusinessStreet/swdjxxgl";
		} catch (Exception e) {
			e.printStackTrace();
			return "BusinessStreet/swdjxxgl";
		}
	}
	
	private Map<String, Object> user;
	private static Connection connection = null;
	  /**
	   * 默认5条线程（默认数量，即最少数量），
	   * 最大20线程（指定了线程池中的最大线程数量），
	   * 空闲时间0秒（当线程池梳理超过核心数量时，多余的空闲时间的存活时间，即超过核心线程数量的空闲线程，在多长时间内，会被销毁），
	   * 等待队列长度1024，
	   * 线程名称[MXR-Task-%d],方便回溯，
	   * 拒绝策略：当任务队列已满，抛出RejectedExecutionException
	   * 异常。
	   */
	
	/**
	 * 查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("swdjxxgl_query.do")
	@ResponseBody
	public String query(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String pageNo = getValue(request.getParameter("pageNo")).replaceAll("-", "");
			String pageSize = getValue(request.getParameter("pageSize")).replaceAll("-", "");

			String sql = "select * from sy_drmx order by drsj desc";
			sql = "select * from (select row_.*, rownum rowno from (" + sql + ") row_ where rownum <= " + pageSize + "*"
					+ pageNo + ") a where a.rowno >= (" + pageNo + "- 1) * " + pageSize + " + 1";

			List<Map<String, Object>> mxlist = bs.query(sql);
			
			// 获取count
		
			String sqlcot = "select * from sy_drmx";
			Integer cont = bs.queryCount(sqlcot);
		

			return this.toJsonct("000", "查询成功！", mxlist, cont+"");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	
		// 将数据从excel取出并存到临时表
		@RequestMapping("swdjxxgl_doInputnew.do")
		@ResponseBody
		public void doInputnew(HttpServletRequest request) {
			String filenameszip = "";
			
			//connection = getConnection();
			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String filenames = getValue(request.getParameter("filenames"));
			String ybm = "";
			String sswj = "";
			String path = request.getRealPath("/upload");
			String uid =  UUID.randomUUID().toString().replace("-", "");
			
		    bs.insert("insert into xwcs_jgzt(zt ,starttime ,createuser ,isdelete ,name ,RWSBM)"
		    		+ " values('1',sysdate,'"+getValue(user.get("UUID"))+"','0','税务登记','"+uid+"')");
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

							results = insttempnew(is,filenamezip, ybm, sswj,uid);

						}
					} else {
						filenamezip = filenameszip.split("\\$_1\\$")[0];
						ybm = filenamezip.split("\\$_1\\$")[1];
						sswj = System.currentTimeMillis()
								+ filenamezip.substring(filenamezip.lastIndexOf("."), filenamezip.length());

						results = insttempnew(is,filenamezip, ybm, sswj,uid);
					}
					//更新税收大数据查询
					gx();
					return ;

				} catch (Exception e) {
					e.printStackTrace();
					bs.update("update  XWCS_JGZT set zt = '2' ,JGZTSM='excle不支持，只支持单sheet的excle' where RWSBM='"+uid+"'");
					return ;
				}
			} else if (filenames.endsWith(".rar")) {
				bs.update("update  XWCS_JGZT set zt = '2' ,JGZTSM='文件类型不支持'  where RWSBM='"+uid+"'");
				return ;
			} else {
				InputStream is = null;
				String filename = "";
				//String results = "";
				try {
					// 循环将获取到的excel文件一次读取（多文件）
					if (filenames.contains("$_$")) {
						String[] filsp = filenames.split("\\$_\\$");
						for (int i = 0; i < filsp.length; i++) {
							filename = filsp[i];
							ybm = new File(filename).getName();
							sswj = System.currentTimeMillis() + ybm.substring(ybm.lastIndexOf("."), ybm.length());
							insttempnew(is,filename, ybm, sswj,uid);
						}
					} else {
						filename = filenames.split("\\$_1\\$")[0];
						ybm = filenames.split("\\$_1\\$")[1];
						sswj = System.currentTimeMillis()
								+ filename.substring(filename.lastIndexOf("."), filename.length());

						 insttempnew(is,filename, ybm, sswj,uid);
					}
					//更新税收大数据查询
					gx();
					return ;
				} catch (Exception e) {
					e.printStackTrace();
					bs.update("update  XWCS_JGZT set zt = '2' ,JGZTSM='未知错误' where RWSBM='"+uid+"'");
					return ;
				} finally {
					//close();
				}
			}
		}
		

		// 根据导入方式选择不同的模板（0：入库，1：退库，2：个人所得税）
		private String insttempnew(InputStream is, String filename, String ybm, String sswj,String uid) {
			
			Map<String, Integer> result = new HashMap<>();
			result.put("NUM", 0);
			result.put("RENUM", 0);
			result.put("UPNUM", 0);
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
				List<Future<Map<String, Integer>>> f = new ArrayList<Future<Map<String, Integer>>>();
				//大于五千就使用线程池 20191125
				long time1 = System.currentTimeMillis();
				if(list.size()>5000) {
					
					ThreadPoolExecutor threadPool = new ThreadPoolExecutor(8, 10, 0L,
						      TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024)
						      , new ThreadFactoryBuilder().setNameFormat("My-Task-%d").build()
						      , new CustomRejectedExecutionHandler()
						  );
					int a = 0;
					for (int i = 0; i < list.size(); i+=1000) {
						a++;
						List<Map<String, String>> listA =  new ArrayList<Map<String, String>>();
						if (i + 1000 < list.size()) {
							for (int j = i; j < i + 1000; j++) {
								listA.add(list.get(j));
							}
						} else {
							for (int j = i; j < list.size(); j++) {
								listA.add(list.get(j));
							}
						}
						try {
							//线程池的
							
							SwdjInsert sw = new SwdjInsert();
							sw.list(listA);
							sw.index(a);
							
							Future<Map<String, Integer>> future = threadPool.submit(sw);
							f.add(future);	
							//原来的
							//result = JdbcConnectedPro.insertAll_sy(listA, sswj);
						} catch (Exception e) {
							e.printStackTrace();
						}
						//result = insertAll_sy(listA, sswj);
					}
					//合并执行的数量
					for (Future<Map<String, Integer>> future2 : f) {
						Map<String, Integer> future = future2.get();
						
						for(Entry<String, Integer> fu:future.entrySet()) {
							
							for(Entry<String, Integer> re:result.entrySet()) {
								if(fu.getKey().equals(re.getKey())) {
									result.put(re.getKey(), fu.getValue()+re.getValue());
								}
							}
						}
					}
					System.err.println("线程结束了！！！！！！！！！！！！！！！！");
					//关闭线程池
					threadPool.shutdown();
				}else {//普通方法
					System.out.println("正在存入"+list.size()+"条数据");
					try {
						result = JdbcConnectedPro.insertAll_sy(list, sswj);
					} catch (SQLException e) {
						
						e.printStackTrace();
					}
				}
				long time2 = System.currentTimeMillis();
				System.out.println("导入"+list.size()+"条数据，花费了"+((time2-time1)/1000)+"秒");
//				//导入完成后往分析表插入数据  原来的
//				for (int i = 0; i < list.size(); i+=5000) {
//				List<Map<String, String>> listA =  new ArrayList<Map<String, String>>();
//				if (i + 5000 < list.size()) {
//					for (int j = i; j < i + 5000; j++) {
//						listA.add(list.get(j));
//					}
//				} else {
//					for (int j = i; j < list.size(); j++) {
//						listA.add(list.get(j));
//					}
//				}
//				System.out.println("正在存入"+listA.size());
//				try {
//					
//					result = JdbcConnectedPro.insertAll_sy(listA, sswj);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				//result = insertAll_sy(listA, sswj);
//			}
				bs.delete("delete from SWDJXX a where (a.DJBDZS) in(select DJBDZS from SWDJXX group by DJBDZS having count(*) > 1)\r\n" + 
						"						  and rowid not in (select min(rowid) from SWDJXX group by DJBDZS having count(*)>1)");
				
				//gx();
				//插入导入明细表
				String sqlselect = "insert into sy_drmx(wjm,wjid,xzsl,qcsl,drsj,GXSL) values (?,?,?,?,sysdate,?)";
				sqlselect = getSql2(sqlselect, new Object[] {ybm, sswj, result.get("NUM"),result.get("RENUM"),result.get("UPNUM")});
				bs.insert(sqlselect);				
				bs.update("update  XWCS_JGZT set zt = '0' where RWSBM='"+uid+"'");
			} catch (Exception e) {
				this.bs.update("update  XWCS_JGZT set zt = '2' where RWSBM='"+uid+"'");
				e.printStackTrace();
			} 
			return "成功";
		}
		@RequestMapping("swdjxxgl_ifTable.do")
		@ResponseBody
		public String ifTable(HttpServletRequest request, HttpServletResponse response) {
			

			Integer rows = bs.queryCount("select * from XWCS_JGZT where zt='1' and NAME='税务登记' and isdelete='0'");
			request.setAttribute("JGZT_SWDJ", rows);
			Map<String,Integer> map = new HashMap<>();
			map.put("code", rows);
			return toJson("000", "OK" ,map);
			
		}
		@RequestMapping("swdjxxgl_getTable.do")
		@ResponseBody
		public String getTable(HttpServletRequest request, HttpServletResponse response) {
			
			List<Map<String , Object>> list = bs.query("select * from XWCS_JGZT where isdelete = '0' and NAME='税务登记' order by STARTTIME desc");
			
			return toJson("000", "OK",list);
			
		}
		/**
		 * 更新税收大数据查询
		 */
		private void gx() {
	            	//System.err.println("往分析表插入数据开始！！！");
	            	long time1 = System.currentTimeMillis();
	            	inset2014();
	    			inset2015();
	    			inset2016();
	    			inset2017();
	    			inset2018();
	    			inset2019();
	    			querySWDJXQ();
	    			queryDJ();
	    			queryJD();
	    			queryHYLX();
	    			queryDJRQ();
	    			long time2 = System.currentTimeMillis();
	    			System.err.println("往分析表插入数据完成！！！耗时"+(time2-time1));
		}
		
		//更新SWZTQK表数据
		private void updateSWZTQK(Map<String,Object> rmap) {
			initMap(rmap);	
			//查询单位
			String dwSql = "select distinct \r\n" + 
					"(select count(*) from SWDJXX where KZZTDJLX LIKE '%单位%') ZHS,\r\n" + 
					"(select count(*) from (select DISTINCT NSRMC from SB_NSRXX) n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX LIKE '%单位%') dw where n.nsrmc = dw.NSRMC) JSHS,\r\n" + 
					"round((select count(*) from (select DISTINCT NSRMC from SB_NSRXX) n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX LIKE '%单位%') dw where n.nsrmc = dw.NSRMC)*100/(select count(*) from SWDJXX where KZZTDJLX LIKE '%单位%'),2) JSHSZB,\r\n" + 
					"(select sum(n.dfzse) dwssje FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX LIKE '%单位%') dw  where n.nsrmc = dw.NSRMC) JNSSJE,\r\n" + 
					"round((select sum(n.dfzse) dwssje FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX LIKE '%单位%') dw  where n.nsrmc = dw.NSRMC)*100/(select sum(n.dfzse) dwssje FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX) dw  where n.nsrmc = dw.NSRMC)，2) JNSSZB\r\n" + 
					"from SWDJXX";
			 List<Map<String, Object>> count1 = bs.query(dwSql);
			 String insertDW ="update SWZTQK set ZHS = "+getValue(count1.get(0).get("ZHS"))+",JSHS = "+getValue(count1.get(0).get("JSHS"))+",JSHSZB = "+getValue(count1.get(0).get("JSHSZB"))+",JNSSJE = "+getValue(count1.get(0).get("JNSSJE"))+",JNSSZB = "+getValue(count1.get(0).get("JNSSZB"))+" WHERE KZZTLX = '单位纳税人'";
			 bs.update(insertDW);
			 //查询个体
			 String gtSql = "select distinct \r\n" + 
			 		"(select count(*) from SWDJXX where KZZTDJLX LIKE '%个体%') ZHS,\r\n" + 
			 		"(select count(*) from (select DISTINCT NSRMC from SB_NSRXX) n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX LIKE '%个体%') dw where n.nsrmc = dw.NSRMC) JSHS,\r\n" + 
			 		"round((select count(*) from (select DISTINCT NSRMC from SB_NSRXX) n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX LIKE '%个体%') dw where n.nsrmc = dw.NSRMC)*100/(select count(*) from SWDJXX where KZZTDJLX LIKE '%个体%'),2) JSHSZB,\r\n" + 
			 		"(select sum(n.dfzse) dwssje FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX LIKE '%个体%') dw  where n.nsrmc = dw.NSRMC) JNSSJE,\r\n" + 
			 		"round((select sum(n.dfzse) dwssje FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX LIKE '%个体%') dw  where n.nsrmc = dw.NSRMC)*100/(select sum(n.dfzse) dwssje FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX) dw  where n.nsrmc = dw.NSRMC)，2) JNSSZB\r\n" + 
			 		"from SWDJXX";
			 List<Map<String, Object>> count2 = bs.query(gtSql);
			 String insertGT ="update SWZTQK set ZHS = "+getValue(count2.get(0).get("ZHS"))+",JSHS = "+getValue(count2.get(0).get("JSHS"))+",JSHSZB = "+getValue(count2.get(0).get("JSHSZB"))+",JNSSJE = "+getValue(count2.get(0).get("JNSSJE"))+",JNSSZB = "+getValue(count2.get(0).get("JNSSZB"))+" WHERE KZZTLX = '个体纳税人'";
			 bs.update(insertGT);
			 //查询其他
			 String qtSql = "select distinct \r\n" + 
			 		"(select count(*) from SWDJXX where KZZTDJLX NOT LIKE '%单位%' and KZZTDJLX NOT LIKE '%个体%') ZHS,\r\n" + 
			 		"(select count(*) from (select DISTINCT NSRMC from SB_NSRXX) n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX NOT LIKE '%单位%' and KZZTDJLX NOT LIKE '%个体%') dw where n.nsrmc = dw.NSRMC) JSHS,\r\n" + 
			 		"round((select count(*) from (select DISTINCT NSRMC from SB_NSRXX) n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX NOT LIKE '%单位%' and KZZTDJLX NOT LIKE '%个体%') dw where n.nsrmc = dw.NSRMC)*100/(select count(*) from SWDJXX where KZZTDJLX NOT LIKE '%单位%' and KZZTDJLX NOT LIKE '%个体%'),2) JSHSZB,\r\n" + 
			 		"(select sum(n.dfzse) dwssje FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX NOT LIKE '%单位%' and KZZTDJLX NOT LIKE '%个体%') dw  where n.nsrmc = dw.NSRMC) JNSSJE,\r\n" + 
			 		"round((select sum(n.dfzse) dwssje FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX NOT LIKE '%单位%' and KZZTDJLX NOT LIKE '%个体%') dw  where n.nsrmc = dw.NSRMC)*100/(select sum(n.dfzse) dwssje FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX) dw  where n.nsrmc = dw.NSRMC)，2) JNSSZB\r\n" + 
			 		"from SWDJXX";
			 List<Map<String, Object>> count3 = bs.query(qtSql);
			 String insertQT ="update SWZTQK set ZHS = "+getValue(count3.get(0).get("ZHS"))+",JSHS = "+getValue(count3.get(0).get("JSHS"))+",JSHSZB = "+getValue(count3.get(0).get("JSHSZB"))+",JNSSJE = "+getValue(count3.get(0).get("JNSSJE"))+",JNSSZB = "+getValue(count3.get(0).get("JNSSZB"))+" WHERE KZZTLX = '其他纳税人'";
			 bs.update(insertQT);
		}
		
		
		
		
		//将2014年之前的所有纳税人存储到NSRXX_2014中
		public String inset2014() {
			try {
				bs.delete("delete from NSRXX_2014 where djrq<=2014");
				String sql = "insert into NSRXX_2014 select * from(\r\n" + 
						"SELECT DISTINCT NSRMC,to_char(TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy')"
						+ " djrq,(select jd.jd_dm from DM_JD jd where INSTR(dj.jd, jd.jd_mc) > 0 and rownum<=1) jd "
						+ "  from  SWDJXX dj where  TO_CHAR (TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy')<=2014)";
				
				Integer result = bs.insert(sql);
				if (result>0) {
					return this.toJson("000", "插入数据成功!!");
					
				}
				return this.toJson("003", "插入数据异常!!");
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "插入数据失败!!");
			}
		}
		
		
		//将2015年的所有纳税人存储到NSRXX_2015中
			public String inset2015() {
				try {
					bs.delete("delete from NSRXX_2015 where djrq=2015");
					String sql = "insert into NSRXX_2015 select * from(\r\n" + 
							"SELECT DISTINCT NSRMC,to_char(TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy') djrq,(select jd.jd_dm from DM_JD jd where INSTR(dj.jd, jd.jd_mc) > 0 and rownum<=1) jd "
							+ "  from  SWDJXX dj where  TO_CHAR (TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy')=2015)\r\n" + 
							"";
					
					Integer result = bs.insert(sql);
					if (result>0) {
						return this.toJson("000", "插入数据成功!!");
						
					}
					return this.toJson("003", "插入数据异常!!");
				} catch (Exception e) {
					e.printStackTrace();
					return this.toJson("009", "插入数据失败!!");
				}
			}
			
			//将2016年之前的所有纳税人存储到NSRXX_2016中
			public String inset2016() {
				try {
					bs.delete("delete from NSRXX_2016 where djrq=2016");
					String sql = "insert into NSRXX_2016 select * from(\r\n" + 
							"SELECT DISTINCT NSRMC,to_char(TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy') djrq,(select jd.jd_dm from DM_JD jd where INSTR(dj.jd, jd.jd_mc) > 0 and rownum<=1) jd "
							+ "  from  SWDJXX dj where  TO_CHAR (TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy')=2016)\r\n" + 
							"";
					
					Integer result = bs.insert(sql);
					if (result>0) {
						return this.toJson("000", "插入数据成功!!");
						
					}
					return this.toJson("003", "插入数据异常!!");
				} catch (Exception e) {
					e.printStackTrace();
					return this.toJson("009", "插入数据失败!!");
				}
			}
			
			
			//将2017年的所有纳税人存储到NSRXX_2017中
			public String inset2017() {
				try {
					bs.delete("delete from NSRXX_2017 where djrq=2017");
					String sql = "insert into NSRXX_2017 select * from(\r\n" + 
							"SELECT DISTINCT NSRMC,to_char(TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy') djrq,(select jd.jd_dm from DM_JD jd where INSTR(dj.jd, jd.jd_mc) > 0 and rownum<=1) jd "
							+ "  from  SWDJXX dj where  TO_CHAR (TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy')=2017)\r\n" + 
							"";
					
					Integer result = bs.insert(sql);
					if (result>0) {
						return this.toJson("000", "插入数据成功!!");
						
					}
					return this.toJson("003", "插入数据异常!!");
				} catch (Exception e) {
					e.printStackTrace();
					return this.toJson("009", "插入数据失败!!");
				}
			}
			
			//将2018年的所有纳税人存储到NSRXX_2018中
			public String inset2018() {
				try {
					bs.delete("delete from NSRXX_2018 where djrq=2018");
					String sql = "insert into NSRXX_2018 select * from(\r\n" + 
							"SELECT DISTINCT NSRMC,to_char(TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy') djrq,(select jd.jd_dm from DM_JD jd where INSTR(dj.jd, jd.jd_mc) > 0 and rownum<=1) jd "
							+ "  from  SWDJXX dj where  TO_CHAR (TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy')=2018)\r\n" + 
							"";
					
					Integer result = bs.insert(sql);
					if (result>0) {
						return this.toJson("000", "插入数据成功!!");
						
					}
					return this.toJson("003", "插入数据异常!!");
				} catch (Exception e) {
					e.printStackTrace();
					return this.toJson("009", "插入数据失败!!");
				}
			}
			
			//将2019年之前的所有纳税人存储到NSRXX_2019中
			public String inset2019() {
				try {
					bs.delete("delete from NSRXX_2019 where djrq=2019");
					String sql = "insert into NSRXX_2019 select * from(\r\n" + 
							"SELECT DISTINCT NSRMC,to_char(TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy') djrq,(select jd.jd_dm from DM_JD jd where INSTR(dj.jd, jd.jd_mc) > 0 and rownum<=1) jd "
							+ "  from  SWDJXX dj where  TO_CHAR (TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy')=2019)\r\n" + 
							"";
					
					Integer result = bs.insert(sql);
					if (result>0) {
						return this.toJson("000", "插入数据成功!!");
						
					}
					return this.toJson("003", "插入数据异常!!");
				} catch (Exception e) {
					e.printStackTrace();
					return this.toJson("009", "插入数据失败!!");
				}
			}
		
		
			
		
		//插入税务总体情况数据
		public String querySWDJXQ(){
			
				
				//当年总税额
				String sql1 = "select sum(dfzse) zse from SB_NSRXX   where to_char(RK_RQ, 'yyyy') = to_char(sysdate, 'yyyy')";
				List<Map<String, Object>> zse = bs.query(sql1);
				BigDecimal zse1 = new BigDecimal(getValue(zse.get(0).get("ZSE")));
				System.out.println(sql1);
				List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
				
				//税务总体情况
				//查询单位和个体数据
				String sql2 = "SELECT aa.*, ROUND((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
						"  FROM (select d.KZZTDJLX,\r\n" + 
						"               count(n1) zhs,\r\n" + 
						"               count(n2) jshs,\r\n" + 
						"               NVL(sum(zse), 0) zse,jd_dm\r\n" + 
						"          from ( \r\n" + 
						"                select j.nsrmc n1, j.KZZTDJLX, ss.nsrmc n2, zse,ss.jd_dm\r\n" + 
						"                  from SWDJXX j\r\n" + 
						"                  left join (select nsrmc, sum(t.DFZSE) zse,jd_dm\r\n" + 
						"                               from sb_nsrxx t\r\n" + 
						"                              GROUP BY nsrmc,jd_dm) ss\r\n" + 
						"                    on j.nsrmc = ss.nsrmc) d\r\n" + 
						"         WHERE d.KZZTDJLX LIKE '%单位%'\r\n" + 
						"            or d.KZZTDJLX LIKE '%个体%'\r\n" + 
						"        \r\n" + 
						"         group by KZZTDJLX,jd_dm\r\n" + 
						"         ORDER BY zhs desc) aa\r\n" + 
						"  union       \r\n" + 
						"SELECT aa.*, ROUND((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
						"  FROM (select '其他纳税人税务登记' KZZTDJLX ,count(n1) zhs, count(n2) jshs, NVL(sum(zse), 0) zse,jd_dm\r\n" + 
						"          from (  \r\n" + 
						"                select j.nsrmc n1, j.KZZTDJLX, ss.nsrmc n2, zse,ss.jd_dm\r\n" + 
						"                  from SWDJXX j\r\n" + 
						"                  left join (select nsrmc, sum(t.DFZSE) zse,jd_dm\r\n" + 
						"                               from sb_nsrxx t\r\n" + 
						"                              GROUP BY nsrmc,jd_dm) ss\r\n" + 
						"                    on j.nsrmc = ss.nsrmc) d\r\n" + 
						"         WHERE d.KZZTDJLX not LIKE '%单位%'\r\n" + 
						"           and d.KZZTDJLX not LIKE '%个体%'\r\n" + 
						"          group by jd_dm,'其他纳税人税务登记'\r\n" + 
						"         ORDER BY zhs desc) aa";
				System.out.println(sql2);
				Map<String, Object> map;
				List<Map<String, Object>> list2 = bs.query(sql2);
				for (int i = 0; i < list2.size(); i++) {
					map = list2.get(i);
					BigDecimal zse2 = new BigDecimal(getValue(map.get("ZSE")));
					double result = zse2.divide(zse1,4,BigDecimal.ROUND_HALF_DOWN).doubleValue()*100;
					map.put("SSZB", result+"%");
				}
				
//				//查询除单位和个体外有个课证主体
//				String sql3 = "SELECT\r\n" + 
//						" aa.*, ROUND ((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
//						"FROM\r\n" + 
//						" (\r\n" + 
//						"select count(n1) zhs,count(n2) jshs,NVL (sum(zse),0) zse from(\r\n" + 
//						"\r\n" + 
//						"select j.nsrmc n1,j.KZZTDJLX,ss.nsrmc n2,zse from SWDJXX j left join(\r\n" + 
//						"select nsrmc,sum(t.DFZSE) zse from sb_nsrxx t GROUP BY nsrmc\r\n" + 
//						") ss on j.nsrmc=ss.nsrmc) d WHERE d.KZZTDJLX not LIKE '%单位%' and  d.KZZTDJLX not LIKE '%个体%'\r\n" + 
//						"ORDER BY zhs desc) aa";
//				List<Map<String, Object>> list2 = bs.query(sql3);
//				Map<String, Object> map1;
//					map1  = list2.get(0);
//					map1.put("KZZTDJLX", "其他纳税人税务登记");
//					BigDecimal zse3 = new BigDecimal(getValue(map1.get("ZSE")));
//					BigDecimal result1 = zse3.divide(zse1,4,BigDecimal.ROUND_HALF_DOWN);
//					  NumberFormat percent = NumberFormat.getPercentInstance();
//				       percent.setMaximumFractionDigits(2);
//				       map1.put("SSZB", percent.format(result1.doubleValue()));
//				
//					list2.addAll(list1);
//					if(user==null) {
//						return this.toJson("009", "插入数据失败!!");
//					}
//					
//					bs.delete("delete from swztqk where jd_dm='"+user.get("DWID")+"'");
					bs.delete("truncate table swztqk");

					
					for (int i = 0; i < list2.size(); i++) {
						String insertSql ="insert into swztqk(KZZTLX,ZHS,JSHS,JSHSZB,JNSSJE,JNSSZB,jd_dm) "
								+ "values('"+list2.get(i).get("KZZTDJLX")+"','"+list2.get(i).get("ZHS")+"',"
										+ "'"+list2.get(i).get("JSHS")+"','"+list2.get(i).get("JSHSZB")+"',"
												+ "'"+list2.get(i).get("ZSE")+"','"+list2.get(i).get("SSZB")+"','"+list2.get(i).get("JD_DM")+"')"; 
						bs.insert(insertSql);
					}
					
					

					//System.err.println("税务总体情况插入条数："+list2.size());
				
				return this.toJson("000", "插入数据成功!!");
				
			
		}
			
		
		
			//插入登记注册类型详情
			public String queryDJ() {
				try {
					//当年总税额
					String sql1 = "select sum(dfzse) zse from SB_NSRXX   where to_char(RK_RQ, 'yyyy') = to_char(sysdate, 'yyyy')";
					List<Map<String, Object>> zse = bs.query(sql1);
					BigDecimal zse1 = new BigDecimal(getValue(zse.get(0).get("ZSE")));
					
					List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
					
					//税务总体情况
					//查询单位和个体数据
//					String sql2 = "SELECT\r\n" + 
//							" aa.*, ROUND ((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
//							"FROM\r\n" + 
//							" (\r\n" + 
//							"select d.djzclx,count(n1) zhs,count(n2) jshs,NVL (sum(zse),0) zse from(\r\n" + 
//							"\r\n" + 
//							"select j.nsrmc n1,j.djzclx,ss.nsrmc n2,zse from SWDJXX j left join(\r\n" + 
//							"select nsrmc,sum(t.DFZSE) zse from sb_nsrxx t  where t.RK_RQ >=(select to_date(to_char(sysdate,'yyyy')||'01','yyyymm') from dual) GROUP BY nsrmc\r\n" + 
//							") ss on j.nsrmc=ss.nsrmc) d\r\n" + 
//							"\r\n" + 
//							"group by djzclx ORDER BY zhs desc) aa";
					String sql2 = "SELECT aa.*, ROUND((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
							"  FROM (select d.djzclx, count(n1) zhs, count(n2) jshs, NVL(sum(zse), 0) zse,jd_dm\r\n" + 
							"          from (\r\n" + 
							"                \r\n" + 
							"                select j.nsrmc n1, j.djzclx, ss.nsrmc n2, zse,jd_dm\r\n" + 
							"                  from SWDJXX j\r\n" + 
							"                  left join (select nsrmc, sum(t.DFZSE) zse,jd_dm\r\n" + 
							"                               from sb_nsrxx t\r\n" + 
							"                              where t.RK_RQ >=\r\n" + 
							"                                    (select to_date(to_char(sysdate, 'yyyy') || '01',\r\n" + 
							"                                                    'yyyymm')\r\n" + 
							"                                       from dual)\r\n" + 
							"                              GROUP BY nsrmc ,jd_dm) ss\r\n" + 
							"                    on j.nsrmc = ss.nsrmc) d\r\n" + 
							"         group by djzclx,jd_dm\r\n" + 
							"         ORDER BY zhs desc) aa";
					Map<String, Object> map;
					List<Map<String, Object>> list1 = bs.query(sql2);
					for (int i = 0; i < list1.size(); i++) {
						map = list1.get(i);
						BigDecimal zse2 = new BigDecimal(getValue(map.get("ZSE")));
						BigDecimal result = zse2.divide(zse1,4,BigDecimal.ROUND_HALF_DOWN );
						  NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						map.put("SSZB", percent.format(result.doubleValue()));
					}
					bs.delete("truncate table SW_DJZCLX");
						for (int i = 0; i < list1.size(); i++) {
							String insertSql ="insert into SW_DJZCLX(DJZCLX,ZHS,JSHS,JSHSZB,JNSSJE,JNSSZB,jd_dm) "
									+ "values('"+list1.get(i).get("DJZCLX")+"','"+list1.get(i).get("ZHS")+"',"
											+ "'"+list1.get(i).get("JSHS")+"','"+list1.get(i).get("JSHSZB")+"',"
													+ "'"+list1.get(i).get("ZSE")+"','"+list1.get(i).get("SSZB")+"','"+list1.get(i).get("JD_DM")+"')"; 
							bs.insert(insertSql);
						}
						//System.err.println("插入登记注册类型条数："+list1.size());
					return this.toJson("000", "插入数据成功!!");
					
				} catch (Exception e) {
					e.printStackTrace();
					return this.toJson("009", "插入数据失败!!");
				}
			}
			
			
			
			//插入街道类型详情
			public String queryJD() {
				try {
					//当年总税额
					String sql1 = "select sum(dfzse) zse from SB_NSRXX   where to_char(RK_RQ, 'yyyy') = to_char(sysdate, 'yyyy')";
					List<Map<String, Object>> zse = bs.query(sql1);
					BigDecimal zse1 = new BigDecimal(getValue(zse.get(0).get("ZSE")));
					
					
					//税务总体情况
					//查询单位和个体数据
					String sql2 = "SELECT\r\n" + 
							" aa.*, ROUND ((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
							"FROM\r\n" + 
							" (\r\n" + 
							"select nvl(d.jd,'待定') JD,count(n1) zhs,count(n2) jshs,sum(zse) zse from(\r\n" + 
							"select DISTINCT j.jd,j.nsrmc n1,ss.nsrmc n2,zse from SWDJXX j left join(\r\n" + 
							"select DISTINCT nsrmc,t.DFZSE zse from sb_nsrxx t where exists(select * from SWDJXX s where t.nsrmc=s.nsrmc)\r\n" + 
							") ss on j.nsrmc=ss.nsrmc) d\r\n" + 
							"\r\n" + 
							"group by jd) aa\r\n" + 
							"";
					Map<String, Object> map;
					List<Map<String, Object>> list1 = bs.query(sql2);
					
					List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < list1.size(); i++) {
						
						if (getValue(list1.get(i).get("JD")).indexOf("玄武") >-1 && !getValue(list1.get(i).get("JD")).contains("玄武区徐庄管委会") &&
								!getValue(list1.get(i).get("JD")).contains("玄武区紫金科创特区") && !getValue(list1.get(i).get("JD")).contains("玄武区后宰门街道") &&
								!getValue(list1.get(i).get("JD")).contains("玄武区梅园街道")) {
							map = list1.get(i);
							BigDecimal zse2 = new BigDecimal(getValue(map.get("ZSE")));
//							double result = zse2.divide(zse1,2,1).doubleValue()*100;
							BigDecimal result = zse2.divide(zse1,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map.put("SSZB", percent.format(result.doubleValue()));
							lists.add(map);
						}
					}
					
					//查询紫金科创和徐庄合并
					String sql3 = "SELECT\r\n" + 
							" aa.*, ROUND ((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
							"FROM\r\n" + 
							" (\r\n" + 
							"SELECT sum(zhs) zhs,sum(jshs) jshs ,sum(zse) zse from (\r\n" + 
							"select nvl(d.jd,'待定') JD,count(n1) zhs,count(n2) jshs,sum(zse) zse from(\r\n" + 
							"select DISTINCT j.jd,j.nsrmc n1,ss.nsrmc n2,zse from SWDJXX j left join(\r\n" + 
							"select DISTINCT nsrmc,t.DFZSE zse from sb_nsrxx t where exists(select * from SWDJXX s where t.nsrmc=s.nsrmc)\r\n" + 
							") ss on j.nsrmc=ss.nsrmc) d\r\n" + 
							"\r\n" + 
							"group by jd) bb where bb.jd like '%徐庄%' OR bb.jd like '%紫金科创%') aa";
					List<Map<String, Object>> list2 = bs.query(sql3);
					Map<String, Object> map1;
						map1  = list2.get(0);
						map1.put("JD", "玄武区徐庄管委会");
						BigDecimal zse3 = new BigDecimal(getValue(map1.get("ZSE")));
						BigDecimal result1 = zse3.divide(zse1,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
					       map1.put("SSZB", percent.format(result1.doubleValue()));
					
						//查询梅园和后宰门合并
						String sql4 = "SELECT\r\n" + 
								" aa.*, ROUND ((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
								"FROM\r\n" + 
								" (\r\n" + 
								"SELECT sum(zhs) zhs,sum(jshs) jshs ,sum(zse) zse from (\r\n" + 
								"select nvl(d.jd,'待定') JD,count(n1) zhs,count(n2) jshs,sum(zse) zse from(\r\n" + 
								"select DISTINCT j.jd,j.nsrmc n1,ss.nsrmc n2,zse from SWDJXX j left join(\r\n" + 
								"select DISTINCT nsrmc,t.DFZSE zse from sb_nsrxx t where exists(select * from SWDJXX s where t.nsrmc=s.nsrmc)\r\n" + 
								") ss on j.nsrmc=ss.nsrmc) d\r\n" + 
								"\r\n" + 
								"group by jd) bb where bb.jd like '%梅园%' OR bb.jd like '%后宰门%') aa";
						List<Map<String, Object>> list3 = bs.query(sql4);
						Map<String, Object> map2;
							map2  = list3.get(0);
							map2.put("JD", "玄武区梅园街道");
							BigDecimal zse4 = new BigDecimal(getValue(map1.get("ZSE")));
							BigDecimal result2 = zse4.divide(zse1,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent1 = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
						       map2.put("SSZB", percent1.format(result2.doubleValue()));
						
							lists.addAll(list2);
							lists.addAll(list3);
						
						
						bs.delete("truncate table SW_JDLX");
						for (int i = 0; i < lists.size(); i++) {
							String insertSql ="insert into SW_JDLX(JD,ZHS,JSHS,JSHSZB,JNSSJE,JNSSZB,jd_dm) "
									+ "values('"+lists.get(i).get("JD")+"','"+lists.get(i).get("ZHS")+"',"
											+ "'"+lists.get(i).get("JSHS")+"','"+lists.get(i).get("JSHSZB")+"',"
													+ "'"+lists.get(i).get("ZSE")+"','"+lists.get(i).get("SSZB")+"','"+user.get("DWID")+"')"; 
							bs.insert(insertSql);
						}
						//System.err.println("插入街道类型条数："+lists.size());
					return this.toJson("000", "插入数据成功!!");
					
				} catch (Exception e) {
					e.printStackTrace();
					return this.toJson("009", "插入数据失败!!");
				}
			}
			
			
			//插入行业类型详情
				public String queryHYLX() {
					try {
//						//当年总税额
//						String sql1 = "select sum(dfzse) zse from SB_NSRXX   where to_char(RK_RQ, 'yyyy') = to_char(sysdate, 'yyyy')";
//						List<Map<String, Object>> zse = bs.query(sql1);
//						BigDecimal zse1 = new BigDecimal(getValue(zse.get(0).get("ZSE")));
//						//税务总体情况
//						//查询单位和个体数据
//						String sql2 = "SELECT\r\n" + 
//								" aa.*, ROUND ((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
//								"FROM\r\n" + 
//								" (\r\n" + 
//								"select d.hy,count(n1) zhs,count(n2) jshs,sum(zse) zse from(\r\n" + 
//								"select j.hy,j.nsrmc n1,ss.nsrmc n2,zse from SWDJXX j left join(\r\n" + 
//								"select DISTINCT nsrmc,t.DFZSE zse from sb_nsrxx t where exists(select * from SWDJXX s where t.nsrmc=s.nsrmc)\r\n" + 
//								") ss on j.nsrmc=ss.nsrmc) d\r\n" + 
//								"\r\n" + 
//								"group by hy) aa";
//						Map<String, Object> map;
//						List<Map<String, Object>> list1 = bs.query(sql2);
//						for (int i = 0; i < list1.size(); i++) {
//							map = list1.get(i);
//							if (getValue(map.get("ZSE")) == null ||getValue(map.get("ZSE")) == "") {
//								map.put("ZSE", 0);
//								map.put("SSZB", 0+"%");
//							}else {
//								BigDecimal zse2 = new BigDecimal(getValue(map.get("ZSE")));
//								BigDecimal result = zse2.divide(zse1,4,BigDecimal.ROUND_HALF_DOWN);
//								 NumberFormat percent = NumberFormat.getPercentInstance();
//							       percent.setMaximumFractionDigits(2);
//								
//								System.err.println(percent.format(result.doubleValue()));
//								map.put("SSZB", percent.format(result.doubleValue()));
//							}
//						}
//							for (int i = 0; i < list1.size(); i++) {
//								String insertSql ="insert into SW_HYLX(HY,ZHS,JSHS,JSHSZB,JNSSJE,JNSSZB) "
//										+ "values('"+list1.get(i).get("HY")+"','"+list1.get(i).get("ZHS")+"',"
//												+ "'"+list1.get(i).get("JSHS")+"','"+list1.get(i).get("JSHSZB")+"',"
//														+ "'"+list1.get(i).get("ZSE")+"','"+list1.get(i).get("SSZB")+"')"; 
//								bs.insert(insertSql);
//							}
//							System.err.println("插入行业类型条数："+list1.size());
					

					
//					String sql = "insert into SW_HYLX(HY,ZHS,JSHS,JSHSZB,JNSSZB,JNSSJE,jd_dm)   " + " SELECT HY,zhs,jshs,zse,"
//							+ "  ROUND((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb,"
//							+ " to_char((NVL(aa.zse,0) / aa.dnzse) * 100,'fm9999999990.09') || '%' SSZB,'"+user.get("DWID")+"' jd_dm FROM (  "
//							+ " select d.hy, count(n1) zhs,count(n2) jshs,sum(zse) zse," + " (select sum(dfzse) zse"
//							+ " from SB_NSRXX" + " where to_char(RK_RQ, 'yyyy') = to_char(sysdate, 'yyyy')) dnzse"
//							+ " from (select j.hy, j.nsrmc n1, ss.nsrmc n2, zse" + " from SWDJXX j"
//							+ " left join (select DISTINCT nsrmc, t.DFZSE zse" + " from sb_nsrxx t" + " where exists (select *"
//							+ " from SWDJXX s" + " where t.nsrmc = s.nsrmc)) ss" + "  on j.nsrmc = ss.nsrmc) d"
//							+ " group by hy) aa";
						
						bs.delete("truncate table SW_HYLX");
					String sql = "insert into SW_HYLX\r\n" + 
							"  (HY, ZHS, JSHS, JSHSZB, JNSSZB, JNSSJE, jd_dm)\r\n" + 
							"  SELECT HY,\r\n" + 
							"         zhs,\r\n" + 
							"         jshs,\r\n" + 
							"         zse,\r\n" + 
							"         ROUND((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb,\r\n" + 
							"         to_char((NVL(aa.zse, 0) / aa.dnzse) * 100, 'fm9999999990.09') || '%' SSZB,\r\n" + 
							"         jd_dm\r\n" + 
							"    FROM (select d.hy,\r\n" + 
							"                 count(n1) zhs,\r\n" + 
							"                 count(n2) jshs,\r\n" + 
							"                 sum(zse) zse,\r\n" + 
							"                 (select sum(dfzse) zse\r\n" + 
							"                    from SB_NSRXX\r\n" + 
							"                   where to_char(RK_RQ, 'yyyy') = to_char(sysdate, 'yyyy')) dnzse,jd_dm\r\n" + 
							"            from (select j.hy, j.nsrmc n1, ss.nsrmc n2, zse,jd_dm\r\n" + 
							"                    from SWDJXX j\r\n" + 
							"                    left join (select DISTINCT nsrmc, t.DFZSE zse,jd_dm\r\n" + 
							"                                from sb_nsrxx t\r\n" + 
							"                               where exists (select *\r\n" + 
							"                                        from SWDJXX s\r\n" + 
							"                                       where t.nsrmc = s.nsrmc)) ss\r\n" + 
							"                      on j.nsrmc = ss.nsrmc) d\r\n" + 
							"           group by hy,jd_dm) aa";
					Integer rows = bs.insert(sql);
					System.err.println("插入行业类型条数："+rows);
					if(rows>0) {
						return this.toJson("000", "插入数据成功!!");
					}
						return this.toJson("000", "插入数据失败!!");
						
					} catch (Exception e) {
						e.printStackTrace();
						return this.toJson("009", "插入数据失败!!");
					}
				}
			
			
				
			//插入登记日期详情
			public String queryDJRQ() {
				try {
					//当年总税额
					String sqlS = "SELECT nvl(sum(dfzse),0) s_zse from SB_NSRXX where  TO_CHAR (RK_RQ,'yyyy') = '2017'";
					List<Map<String, Object>> zse_S = bs.query(sqlS);
				/*	if (zse_S == null) {
						zse_S.set(0, 0)
					}*/
					BigDecimal zseS = new BigDecimal(getValue(zse_S.get(0).get("S_ZSE")));
					
					String sqlE = "SELECT nvl(sum(dfzse),0) e_zse from SB_NSRXX where  TO_CHAR (RK_RQ,'yyyy') = '2018'";
					List<Map<String, Object>> zse_E = bs.query(sqlE);
					BigDecimal zseE = new BigDecimal(getValue(zse_E.get(0).get("E_ZSE")));
					
					 
//					String sql = " SELECT DISTINCT NSRMC, to_char(TO_DATE(DJ.DJRQ, 'yyyy-MM-dd'), 'yyyy') djrq  from SWDJXX dj" 
//					 +" where TO_CHAR(TO_DATE(DJ.DJRQ, 'yyyy-MM-dd'), 'yyyy') in ('2014','2015')"
//					 +" group by"
//					 +" djrq, NSRMC";
//					List<Map<String, Object>> mz  = bs.query(sql);
					String sqlN = "SELECT nvl(sum(dfzse),0) n_zse from SB_NSRXX where  TO_CHAR (RK_RQ,'yyyy') = '2019'";
					List<Map<String, Object>> zse_N = bs.query(sqlN);
					BigDecimal zseN = new BigDecimal(getValue(zse_N.get(0).get("N_ZSE")));
					
					//税务总体情况
					//14年
					String sql14 = "select aa.*,round((aa.jshs/aa.zhs),2)*100||'%' jshszb,round((aa.s_jshs/aa.zhs),2)*100||'%' s_jshszb\r\n" + 
							",round((aa.e_jshs/aa.zhs),2)*100||'%' e_jshszb,round((aa.n_jshs/aa.zhs),2)*100||'%' n_jshszb from \r\n" + 
							"(SELECT count(DISTINCT nsrmc) zhs,'2014' djrq,jd_dm,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_JSHS from SB_NSRXX aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2014 group by nsrmc) and jd_dm=n.jd_dm and to_char(rk_rq,'yyyy')='2014') jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2017 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2014 group by nsrmc) and jd_dm=n.jd_dm) s_jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2018 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2014 group by nsrmc) and jd_dm=n.jd_dm) e_jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2019 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2014 group by nsrmc) and jd_dm=n.jd_dm) n_jshs,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2014 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2017') s_ssze,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2014 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2018') e_ssze,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2014 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2019') n_ssze\r\n" + 
							" from NSRXX_2014 n WHERE to_char(TO_DATE (DJRQ, 'yyyy'),'yyyy') <= 2014 and n.jd_dm is not null GROUP BY '2014', n.jd_dm) aa";
					
					Map<String, Object> map;
					List<Map<String, Object>> list1 = bs.query(sql14);
					for (int i = 0; i < list1.size(); i++) {
						map = list1.get(i);
						BigDecimal zse2 = new BigDecimal(getValue(map.get("S_SSZE")));
						BigDecimal zse3 = new BigDecimal(getValue(map.get("E_SSZE")));
						BigDecimal zse4 = new BigDecimal(getValue(map.get("N_SSZE")));
						if (zseS!=null&&zseS.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result1 = zse2.divide(zseS,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map.put("S_SSJEZB",percent.format(result1.doubleValue()));
						}else {
							map.put("S_SSJEZB", 0+"%");
						}
						if (zseE!=null&&zseE.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result2 = zse3.divide(zseE,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map.put("E_SSJEZB", percent.format(result2.doubleValue()));
							
						}else {
							map.put("E_SSJEZB", 0+"%");
						}
						if (zseN!=null&&zseN.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result3 = zse4.divide(zseN,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map.put("N_SSJEZB",percent.format(result3.doubleValue()));
						}else {
							map.put("N_SSJEZB", 0+"%");
							
						}
					}
					
					//15年
					String sql15 = " select aa.*,round((aa.jshs/aa.zhs),2)*100||'%' jshszb,round((aa.s_jshs/aa.zhs),2)*100||'%' s_jshszb\r\n" + 
							",round((aa.e_jshs/aa.zhs),2)*100||'%' e_jshszb,round((aa.n_jshs/aa.zhs),2)*100||'%' n_jshszb from \r\n" + 
							"(SELECT count(DISTINCT nsrmc) zhs,'2015' djrq,jd_dm,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_JSHS from SB_NSRXX aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2015 group by nsrmc) and jd_dm=n.jd_dm and to_char(rk_rq,'yyyy')='2015') jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2017 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2015 group by nsrmc) and jd_dm=n.jd_dm) s_jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2018 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2015 group by nsrmc) and jd_dm=n.jd_dm) e_jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2019 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2015 group by nsrmc) and jd_dm=n.jd_dm) n_jshs,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2015 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2017') s_ssze,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2015 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2018') e_ssze,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2015 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2019') n_ssze\r\n" + 
							" from NSRXX_2015 n WHERE to_char(TO_DATE (DJRQ, 'yyyy'),'yyyy') = 2015 and n.jd_dm is not null GROUP BY '2015', n.jd_dm) aa";
					
					Map<String, Object> map2;
					List<Map<String, Object>> list2 = bs.query(sql15);
					for (int i = 0; i < list2.size(); i++) {
						map2 = list2.get(i);
						BigDecimal zse215 = new BigDecimal(getValue(map2.get("S_SSZE")));
						BigDecimal zse315 = new BigDecimal(getValue(map2.get("E_SSZE")));
						BigDecimal zse415 = new BigDecimal(getValue(map2.get("N_SSZE")));
						
						if (zseS!=null&&zseS.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result115 = zse215.divide(zseS,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map2.put("S_SSJEZB", percent.format(result115.doubleValue()));
						}else {
							map2.put("S_SSJEZB", 0+"%");
						}
						if (zseE!=null&&zseE.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result215 = zse315.divide(zseE,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map2.put("E_SSJEZB", percent.format(result215.doubleValue()));
							
						}else {
							map2.put("E_SSJEZB", 0+"%");
						}
						if (zseN!=null&&zseN.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result315 = zse415.divide(zseN,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map2.put("N_SSJEZB", percent.format(result315.doubleValue()));
						}else {
							map2.put("N_SSJEZB", 0+"%");
							
						}
						
					}
					
					//16年
					String sql16 = "select aa.*,round((aa.jshs/aa.zhs),2)*100||'%' jshszb,round((aa.s_jshs/aa.zhs),2)*100||'%' s_jshszb\r\n" + 
							",round((aa.e_jshs/aa.zhs),2)*100||'%' e_jshszb,round((aa.n_jshs/aa.zhs),2)*100||'%' n_jshszb from \r\n" + 
							"(SELECT  count(DISTINCT nsrmc) zhs,'2016' djrq,jd_dm,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_JSHS from SB_NSRXX aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2016 group by nsrmc) and jd_dm=n.jd_dm and to_char(rk_rq,'yyyy')='2016') jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2017 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2016 group by nsrmc) and jd_dm=n.jd_dm) s_jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2018 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2016 group by nsrmc) and jd_dm=n.jd_dm) e_jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2019 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2016 group by nsrmc) and jd_dm=n.jd_dm) n_jshs,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2016 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2017') s_ssze,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2016 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2018') e_ssze,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2016 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2019') n_ssze\r\n" + 
							" from NSRXX_2016 n WHERE to_char(TO_DATE (DJRQ, 'yyyy'),'yyyy') = 2016 and n.jd_dm is not null GROUP BY '2016', n.jd_dm) aa";
					
					Map<String, Object> map3;
					List<Map<String, Object>> list3 = bs.query(sql16);
					for (int i = 0; i < list3.size(); i++) {
						map3 = list3.get(i);
						BigDecimal zse216 = new BigDecimal(getValue(map3.get("S_SSZE")));
						BigDecimal zse316 = new BigDecimal(getValue(map3.get("E_SSZE")));
						BigDecimal zse416 = new BigDecimal(getValue(map3.get("S_SSZE")));
						
						if (zseS!=null&&zseS.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result116 = zse216.divide(zseS,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map3.put("S_SSJEZB", percent.format(result116.doubleValue()));
						}else {
							map3.put("S_SSJEZB", 0+"%");
						}
						if (zseE!=null&&zseE.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result216 = zse316.divide(zseE,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map3.put("E_SSJEZB", percent.format(result216.doubleValue()));
							
						}else {
							map3.put("E_SSJEZB", 0+"%");
						}
						if (zseN!=null&&zseN.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result316 = zse416.divide(zseN,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map3.put("N_SSJEZB", percent.format(result316.doubleValue()));
						}else {
							map3.put("N_SSJEZB", 0+"%");
							
						}
						
					}
					
					
					//17年
					String sql17 = "select aa.*,round((aa.jshs/aa.zhs),2)*100||'%' jshszb,round((aa.s_jshs/aa.zhs),2)*100||'%' s_jshszb\r\n" + 
							",round((aa.e_jshs/aa.zhs),2)*100||'%' e_jshszb,round((aa.n_jshs/aa.zhs),2)*100||'%' n_jshszb from \r\n" + 
							"(SELECT  count(DISTINCT nsrmc)  zhs,'2017' djrq,jd_dm,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_JSHS from SB_NSRXX aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2017 group by nsrmc) and jd_dm=n.jd_dm and to_char(rk_rq,'yyyy')='2017') jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2017 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2017 group by nsrmc) and jd_dm=n.jd_dm) s_jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2018 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2017 group by nsrmc) and jd_dm=n.jd_dm) e_jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2019 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2017 group by nsrmc) and jd_dm=n.jd_dm) n_jshs,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2017 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2017') s_ssze,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2017 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2018') e_ssze,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2017 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2019') n_ssze\r\n" + 
							" from NSRXX_2017 n WHERE to_char(TO_DATE (DJRQ, 'yyyy'),'yyyy') = 2017 and n.jd_dm is not null GROUP BY '2017', n.jd_dm) aa";
					
					Map<String, Object> map4;
					List<Map<String, Object>> list4 = bs.query(sql17);
					for (int i = 0; i < list4.size(); i++) {
						map4 = list4.get(i);
						BigDecimal zse217 = new BigDecimal(getValue(map4.get("S_SSZE")));
						BigDecimal zse317 = new BigDecimal(getValue(map4.get("E_SSZE")));
						BigDecimal zse417 = new BigDecimal(getValue(map4.get("N_SSZE")));
						
						if (zseS!=null&&zseS.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result117 = zse217.divide(zseS,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map4.put("S_SSJEZB", percent.format(result117.doubleValue()));
						}else {
							map4.put("S_SSJEZB", 0+"%");
						}
						if (zseE!=null&&zseE.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result217 = zse317.divide(zseE,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map4.put("E_SSJEZB", percent.format(result217.doubleValue()));
							
						}else {
							map4.put("E_SSJEZB", 0+"%");
						}
						if (zseN!=null&&zseN.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result317 = zse417.divide(zseN,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map4.put("N_SSJEZB", percent.format(result317.doubleValue()));
						}else {
							map4.put("N_SSJEZB", 0+"%");
							
						}
						
					}
					
					
					//18年
					String sql18 = " select aa.*,round((aa.jshs/aa.zhs),2)*100||'%' jshszb,round((aa.s_jshs/aa.zhs),2)*100||'%' s_jshszb\r\n" + 
							",round((aa.e_jshs/aa.zhs),2)*100||'%' e_jshszb,round((aa.n_jshs/aa.zhs),2)*100||'%' n_jshszb from \r\n" + 
							"(SELECT  count(DISTINCT nsrmc)  zhs,'2018' djrq,jd_dm,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_JSHS from SB_NSRXX aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2018 group by nsrmc) and jd_dm=n.jd_dm and to_char(rk_rq,'yyyy')='2018') jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2017 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2018 group by nsrmc) and jd_dm=n.jd_dm) s_jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2018 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2018 group by nsrmc) and jd_dm=n.jd_dm) e_jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2019 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2018 group by nsrmc) and jd_dm=n.jd_dm) n_jshs,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2018 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2017') s_ssze,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2018 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2018') e_ssze,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2018 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2019') n_ssze\r\n" + 
							" from NSRXX_2018 n WHERE to_char(TO_DATE (DJRQ, 'yyyy'),'yyyy') = 2018 and n.jd_dm is not null GROUP BY '2018', n.jd_dm) aa";
					
					Map<String, Object> map5;
					List<Map<String, Object>> list5 = bs.query(sql18);
					for (int i = 0; i < list5.size(); i++) {
						map5 = list5.get(i);
						BigDecimal zse218 = new BigDecimal(getValue(map5.get("S_SSZE")));
						BigDecimal zse318 = new BigDecimal(getValue(map5.get("E_SSZE")));
						BigDecimal zse418 = new BigDecimal(getValue(map5.get("N_SSZE")));
						if (zseS!=null&&zseS.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result318 = zse218.divide(zseS,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map5.put("S_SSJEZB", percent.format(result318.doubleValue()));
						}else {
							map5.put("S_SSJEZB", 0+"%");
						}
						if (zseE!=null&&zseE.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result218 = zse318.divide(zseE,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map5.put("E_SSJEZB", percent.format(result218.doubleValue()));
							
						}else {
							map5.put("E_SSJEZB", 0+"%");
						}
						if (zseN!=null&&zseN.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result118 = zse418.divide(zseN,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map5.put("N_SSJEZB", percent.format(result118.doubleValue()));
						}else {
							map5.put("N_SSJEZB", 0+"%");
							
						}
						
					}
					
					
					//19年
					String sql19 = "select aa.*,round((aa.jshs/aa.zhs),2)*100||'%' jshszb,round((aa.s_jshs/aa.zhs),2)*100||'%' s_jshszb\r\n" + 
							",round((aa.e_jshs/aa.zhs),2)*100||'%' e_jshszb,round((aa.n_jshs/aa.zhs),2)*100||'%' n_jshszb from \r\n" + 
							"(SELECT  count(DISTINCT nsrmc) zhs,'2019' djrq,jd_dm,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_JSHS from SB_NSRXX aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2019 group by nsrmc) and jd_dm=n.jd_dm and to_char(rk_rq,'yyyy')='2019') jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2017 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2019 group by nsrmc) and jd_dm=n.jd_dm) s_jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2018 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2019 group by nsrmc) and jd_dm=n.jd_dm) e_jshs,\r\n" + 
							"  (SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2019 aa where aa.nsrmc in (SELECT  nsrmc from NSRXX_2019 group by nsrmc) and jd_dm=n.jd_dm) n_jshs,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2019 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2017') s_ssze,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2019 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2018') e_ssze,\r\n" + 
							"  ( SELECT nvl(sum(dfzse),0) from SB_NSRXX aa\r\n" + 
							"  where aa.nsrmc in (SELECT  nsrmc from NSRXX_2019 group by nsrmc) and jd_dm=n.jd_dm and  TO_CHAR (RK_RQ,'yyyy' ) = '2019') n_ssze\r\n" + 
							" from NSRXX_2019 n WHERE to_char(TO_DATE (DJRQ, 'yyyy'),'yyyy') = 2019 and n.jd_dm is not null GROUP BY '2019', n.jd_dm) aa";
					
					Map<String, Object> map6;
					List<Map<String, Object>> list6 = bs.query(sql19);
					for (int i = 0; i < list6.size(); i++) {
						map6 = list6.get(i);
						BigDecimal zse219 = new BigDecimal(getValue(map6.get("S_SSZE")));
						BigDecimal zse319 = new BigDecimal(getValue(map6.get("E_SSZE")));
						BigDecimal zse419 = new BigDecimal(getValue(map6.get("N_SSZE")));
						if (zseS!=null&&zseS.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result119 = zse219.divide(zseS,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map6.put("S_SSJEZB", percent.format(result119.doubleValue()));
						}else {
							map6.put("S_SSJEZB", 0+"%");
						}
						if (zseE!=null&&zseE.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result219 = zse319.divide(zseE,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map6.put("E_SSJEZB", percent.format(result219.doubleValue()));
							
						}else {
							map6.put("E_SSJEZB", 0+"%");
						}
						if (zseN!=null&&zseN.compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal result319 = zse419.divide(zseN,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							map6.put("N_SSJEZB", percent.format(result319.doubleValue()));
						}else {
							map6.put("N_SSJEZB", 0+"%");
							
						}
						
					}
					
					list1.addAll(list2);
					list1.addAll(list3);
					list1.addAll(list4);
					list1.addAll(list5);
					list1.addAll(list6);
					
					bs.delete("truncate table SW_DJRQLX");
					for (int i = 0; i < list1.size(); i++) {
						String insertSql ="insert into SW_DJRQLX(DJRQLX,ZHS,JSHS,SEVENJSHS,SEVENJSHSZB,EIGHTJSHS,EIGHTJSHSZB,NINEJSHS,"
								+ "NINEJSHSZB,SEVENSSJE,SEVENSSJEZB,EIGHTSSJE,EIGHTSSJEZB,NINETSSJE,NINETSSJEZB,JD_DM) "
								+ "values('"+list1.get(i).get("DJRQ")+"','"+list1.get(i).get("ZHS")+"',"
								+ "'"+list1.get(i).get("JSHS")+"','"+list1.get(i).get("S_JSHS")+"',"
								+ "'"+list1.get(i).get("S_JSHSZB")+"','"+list1.get(i).get("E_JSHS")+"',"
								+ "'"+list1.get(i).get("E_JSHSZB")+"','"+list1.get(i).get("N_JSHS")+"',"
								+ "'"+list1.get(i).get("N_JSHSZB")+"','"+list1.get(i).get("S_SSZE")+"',"
								+ "'"+list1.get(i).get("S_SSJEZB")+"','"+list1.get(i).get("E_SSZE")+"',"
								+ "'"+list1.get(i).get("E_SSJEZB")+"','"+list1.get(i).get("N_SSZE")+"',"
								+ "'"+list1.get(i).get("N_SSJEZB")+"','"+list1.get(i).get("JD_DM")+"')";
						bs.insert(insertSql);
					}
					//System.err.println("插入登记日期条数："+list1.size());
					return this.toJson("000", "插入数据成功!!");
					
				} catch (Exception e) {
					e.printStackTrace();
					return this.toJson("009", "插入数据失败!!");
				}
			}
			
			
			
		private class  CustomRejectedExecutionHandler implements RejectedExecutionHandler {
	        @Override
	        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
	            try {
	                // 核心改造点，由blockingqueue的offer改成put阻塞方法
	                executor.getQueue().put(r);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }	
			
				
	
}
