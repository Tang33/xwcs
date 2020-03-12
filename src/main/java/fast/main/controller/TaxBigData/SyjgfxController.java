package fast.main.controller.TaxBigData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.ExcelUtil;
import fast.main.util.Super;

/**
 * 税源结构分析
 *
 */
@Controller
@RequestMapping("syqkfx")
public class SyjgfxController extends Super{
	@Autowired
	BaseService bs;
	private Map<String, Object> user = null;

	/**
	 * 进入税源结构分析跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		
		return "TaxBigData/syjgfx";
		
	}
	
	/**
	 * 查询按税务总体情况分析数据(echarts图表)
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="querySwztqk.do")
	@ResponseBody
	public String querySwztqk(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
		
		user = (Map<String, Object>) request.getSession().getAttribute("user");
		String jd = (String) user.get("DWID");
		String and ="";
		if(null != jd && jd == "00") {
			and =" jd_dm='00'";
			
		}else {
			and =" jd_dm = '"+jd+"'";
		}
		try {
			Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
			if (getValue(user.get("DWID")).equals("00")) {
				String sql = "select distinct kzztlx name,zhs value from SWZTQK where "+and+"";//'"+getValue(user.get("DWID"))+"'";
				List<Map<String, Object>> list = bs.query(sql);
				
				Map<String, String> map;
				for (int i = 0; i < list.size(); i++) {
					map = new HashMap<String, String>();
					for (String key : list.get(i).keySet()) {
						map.put(key.toLowerCase(), list.get(i).get(key).toString());
					}
					list1.add(map);
				}
				return this.toJson("000", "查询成功!!",list1);
			}else {
				String sql = "select distinct kzztlx name,zhs value from SWZTQK where "+and+"";//'"+getValue(user.get("DWID"))+"'";
				List<Map<String, Object>> list = bs.query(sql);
				
				Map<String, String> map;
				for (int i = 0; i < list.size(); i++) {
					map = new HashMap<String, String>();
					for (String key : list.get(i).keySet()) {
						map.put(key.toLowerCase(), list.get(i).get(key).toString());
					}
					list1.add(map);
				}
				
				return this.toJson("000", "查询成功!!",list1);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败!!");
		}
	}
	
	
		@RequestMapping("querySwztqkDetail.do")
		@ResponseBody
		//查询税务总体情况详情
		public String querySwztqkDetail(HttpServletRequest request , HttpServletResponse response,
				@RequestParam Map<String, Object> form) {
			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String jd = (String) user.get("DWID");
			String and ="";
			if(null != jd && jd == "00") {
				and =" jd_dm='00'";
				
			}else {
				and =" jd_dm = '"+jd+"'";
			}
			try {
				if (getValue(user.get("DWID")).equals("00")) {
					String sql = "select distinct * from SWZTQK where "+and+"";//'"+getValue(user.get("DWID"))+"'";
					List<Map<String, Object>> list = bs.query(sql);
					return this.toJson("000", "查询成功!!",list);
				}else {
					String sql = "select distinct * from SWZTQK where "+and+"";//'"+getValue(user.get("DWID"))+"'";
					List<Map<String, Object>> list = bs.query(sql);
					return this.toJson("000", "查询成功!!",list);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败!!");
			}
		}
		
		
		/**
		 * 查询按登记注册情况分析数据(echarts图表)
		 * @param request
		 * @param response
		 * @param form
		 * @return
		 */
		@RequestMapping(value="queryDjzclx.do",produces = "text/plain;charset=utf-8")
		@ResponseBody
		public String queryDjzclx(HttpServletRequest request , HttpServletResponse response,
				@RequestParam Map<String, Object> form) {
			List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String jd = (String) user.get("DWID");
			String and ="";
			if(null != jd && jd == "00") {
				and =" and jd_dm='00'";
				
			}else {
				and =" and jd_dm = '"+jd+"'";
			}
			try {
				
				//"+getValue(user.get("DWID"))+"
				//if (getValue(user.get("DWID")).equals("00")) {		
				String sql = "select distinct* from (select  distinct* from SW_DJZCLX  ORDER BY  CAST(ZHS AS INTEGER) desc) aa where ROWNUM <=7 "+and+"";
				List<Map<String, Object>> list = bs.query(sql);
				return this.toJson("000", "查询成功!!",list);
//				}else {
//					String sql = "select * from (select * from SW_DJZCLX where jd_dm = '"+getValue(user.get("DWID"))+"' ORDER BY  CAST(ZHS AS INTEGER) desc) aa where ROWNUM <=7 ";
//					List<Map<String, Object>> list = bs.query(sql);
//					return this.toJson("000", "查询成功!!",list);
//					
//				}
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败!!");
			}
		}
		
		/**
		 * 查询登记注册详情
		 * @param request
		 * @param response
		 * @param form
		 * @return
		 */
		@RequestMapping(value="queryDJZCLXDetail.do",produces = "text/plain;charset=utf-8")
		@ResponseBody
		public String queryDJZCLXDetail(HttpServletRequest request , HttpServletResponse response,
				@RequestParam Map<String, Object> form) {
			try {
				user = (Map<String, Object>) request.getSession().getAttribute("user");
				String jd = (String) user.get("DWID");
				String and ="";
				if(null != jd && jd == "00") {
					and =" and jd_dm='00'";
					
				}else {
					and =" jd_dm = '"+jd+"'";
				}
//				Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
//				if (getValue(user.get("DWID")).equals("00")) {
				String page = this.getValue(form.get("page"));
				String pageSize = this.getValue(form.get("limit"));
				int count = 0;
					String sql = "select distinct  * from SW_DJZCLX where "+and+"";
					List<Map<String, Object>> list = bs.query(sql,page,pageSize);
					count =  bs.queryCount(sql);
					return this.toJson("000", "查询成功!!",list,count);
//				}else {
//					String sql = "select * from SW_DJZCLX where jd_dm = '"+getValue(user.get("DWID"))+"'";
//					List<Map<String, Object>> list = bs.query(sql);
//					return this.toJson("000", "查询成功!!",list);
//				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败!!");
			}
		}
		
		/**
		 * 查询按行业类型分析数据(echarts图表)
		 * @param request
		 * @param response
		 * @param form
		 * @return
		 */
		@RequestMapping(value="queryHY.do",produces = "text/plain;charset=utf-8")
		@ResponseBody
		public String queryHY(HttpServletRequest request , HttpServletResponse response,
				@RequestParam Map<String, Object> form) {
			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String jd = (String) user.get("DWID");
			String and ="";
			if(null != jd && jd == "00") {
				and =" jd_dm='00'";
				
			}else {
				and =" jd_dm = '"+jd+"'";
			}
			List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
			try {
				Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
				//if (getValue(user.get("DWID")).equals("00")) {
					String sql = "select distinct * from (select HY name,zhs value from SW_HYLX  where "+and+"  ORDER BY  CAST(ZHS AS INTEGER) desc) aa where ROWNUM <=10 ";
					List<Map<String, Object>> list = bs.query(sql);
					
					Map<String, String> map;
					for (int i = 0; i < list.size(); i++) {
						map = new HashMap<String, String>();
						for (String key : list.get(i).keySet()) {
							map.put(key.toLowerCase(), list.get(i).get(key).toString());
						}
						list1.add(map);
					}
					
					return this.toJson("000", "查询成功!!",list1);
//				}else {
//					String sql = "select * from (select HY name,zhs value from SW_HYLX where jd_dm = '"+getValue(user.get("DWID"))+"'  ORDER BY  CAST(ZHS AS INTEGER) desc) aa where ROWNUM <=10 ";
//					List<Map<String, Object>> list = bs.query(sql);
//					
//					Map<String, String> map;
//					for (int i = 0; i < list.size(); i++) {
//						map = new HashMap<String, String>();
//						for (String key : list.get(i).keySet()) {
//							map.put(key.toLowerCase(), list.get(i).get(key).toString());
//						}
//						list1.add(map);
//					}
//					
//					return this.toJson("000", "查询成功!!",list1);
//				}
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败!!");
			}
		}
		
		/**
		 * 查询行业详情
		 * @param request
		 * @param response
		 * @param form
		 * @return
		 */
		@RequestMapping(value="queryHYDetail.do",produces = "text/plain;charset=utf-8")
		@ResponseBody
		public String queryHYDetail(HttpServletRequest request , HttpServletResponse response,
				@RequestParam Map<String, Object> form) {
			try {
				//Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
				//if (getValue(user.get("DWID")).equals("00")) {
				user = (Map<String, Object>) request.getSession().getAttribute("user");
				String jd = (String) user.get("DWID");
				String and ="";
				if(null != jd && jd == "00") {
					and =" jd_dm='00'";
					
				}else {
					and =" jd_dm = '"+jd+"'";
				}
					String page = this.getValue(form.get("page"));
					String pageSize = this.getValue(form.get("limit"));
					int count = 0;
					String sql = "select distinct *  from SW_HYLX aa where  "+and+" ORDER BY CAST(aa.ZHS AS INTEGER) desc ";
					List<Map<String, Object>> list = bs.query(sql,page,pageSize);
					count =  bs.queryCount(sql);
					return this.toJson("000", "查询成功!!",list,count);
//				}else {
//					String sql = "select * from SW_HYLX aa where aa.jd_dm = '"+getValue(user.get("DWID"))+"' ORDER BY CAST(aa.ZHS AS INTEGER) desc";
//					List<Map<String, Object>> list = bs.query(sql);
//					
//					return this.toJson("000", "查询成功!!",list);
//					
//				}
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败!!");
			}
		}
		
		/**
		 * 查询按街道类型分析数据(echarts图表)
		 * @param request
		 * @param response
		 * @param form
		 * @return
		 */
		@RequestMapping(value="queryJDLX.do",produces = "text/plain;charset=utf-8")
		@ResponseBody
		public String queryJDLX(HttpServletRequest request , HttpServletResponse response,
				@RequestParam Map<String, Object> form) {
			List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String jd = (String) user.get("DWID");
			String and ="";
			if(null != jd && jd == "00") {
				and =" jd_dm='00'";
				
			}else {
				and =" jd_dm = '"+jd+"'";
			}
			try {
				//Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
				//if (getValue(user.get("DWID")).equals("00")) {
					String sql = "select distinct jd,zhs,jshs,jnssje from SW_JDLX where jd_dm='00' and  rownum <= 5 order by CAST(ZHS AS INTEGER) desc";
					List<Map<String, Object>> list = bs.query(sql);
					Map<String, String> map;
					for (int i = 0; i < list.size(); i++) {
						map = new HashMap<String, String>();
						for (String key : list.get(i).keySet()) {
							map.put(key.toLowerCase(), list.get(i).get(key).toString());
						}
						list1.add(map);
					}
					
					return this.toJson("000", "查询成功!!",list1);
//				}else {
//					String sql = "select jd,zhs,jshs,jnssje from SW_JDLX where jd_dm = '"+getValue(user.get("DWID"))+"' and rownum <= 5 order by CAST(ZHS AS INTEGER) desc";
//					List<Map<String, Object>> list = bs.query(sql);
//					Map<String, String> map;
//					for (int i = 0; i < list.size(); i++) {
//						map = new HashMap<String, String>();
//						for (String key : list.get(i).keySet()) {
//							map.put(key.toLowerCase(), list.get(i).get(key).toString());
//						}
//						list1.add(map);
//					}
//					
//					return this.toJson("000", "查询成功!!",list1);
//					
//				}
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败!!");
			}
		}
		
		/**
		 * 数据挖掘街道
		 * @param request
		 * @param response
		 * @param form
		 * @return
		 */
		@RequestMapping(value="JDDatamining.do",produces = "text/plain;charset=utf-8")
		@ResponseBody
		public String JDDatamining(HttpServletRequest request , HttpServletResponse response,
				@RequestParam Map<String, Object> form){
			List<Map<String, Object>> list = new ArrayList<>();
			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String jd = (String) user.get("DWID");
			String and ="";
			if(null != jd && jd == "00") {
				and =" jd_dm='00'";
				
			}else {
				and =" jd_dm = '"+jd+"'";
			}
			try{
				//Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
				//if (getValue(user.get("DWID")).equals("00")) {
					String sql = "select distinct * from SW_JDLX where "+and+" order by CAST(ZHS AS INTEGER) desc";
					list = bs.query(sql);
					return this.toJson("000", "查询成功！",list);
//				}else {
//					String sql = "select * from SW_JDLX where jd_dm = '"+getValue(user.get("DWID"))+"' order by CAST(ZHS AS INTEGER) desc";
//					list = bs.query(sql);
//					return this.toJson("000", "查询成功！",list);
//				}
			}catch(Exception e){
				e.printStackTrace();
				return this.toJson("001", "查询失败！",list);
			}
		}
		
		 // 导出excel(街道类型)
		 public Object exportJD(HttpServletRequest request , HttpServletResponse response,
					@RequestParam Map<String, Object> form) {
			  try {
			   // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			   // 街道
				  user = (Map<String, Object>) request.getSession().getAttribute("user");
				  String jd = (String) user.get("DWID");
					String and ="";
					if(null != jd && jd == "00") {
						and =" jd_dm='00'";
						
					}else {
						and =" jd_dm = '"+jd+"'";
					}
			   String sqlall="select distinct * from SW_JDLX  where "+and+" order by CAST(ZHS AS INTEGER) desc";
			   List<Map<String, Object>> map=bs.query(sqlall);
			   Map<String, Object> map1 = new HashMap<String, Object>();
			   String[] cols = { "街道", "总户数", "见税户数", "见税户数占比" , "今年税收金额（元）" , "今年税收金额占比"};
			   String[] keys = { "JD", "ZHS", "JSHS", "JSHSZB" , "JNSSJE" , "JNSSZB"};
			   map1.put("fileName", "按街道分析.xls");
			   map1.put("cols", cols);
			   map1.put("keys", keys);
			   map1.put("list", map);
			   return map1;
			  } catch (Exception e) {
				  e.printStackTrace();
				  return this.toJson("009", "查询异常！");
			  }
		 }
		 
		/**
		 * 查询按登记日期分析数据
		 * @param request
		 * @param response
		 * @param form
		 * @return
		 */
			@RequestMapping(value="queryDJRQLX.do",produces = "text/plain;charset=utf-8")
			@ResponseBody
			public String queryDJRQLX(HttpServletRequest request , HttpServletResponse response,
					@RequestParam Map<String, Object> form) {
				List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
				String jd = getValue( user.get("DWID"));
				String and ="";
				if(jd.equals("00")) {
					and =" 1=1";
				}else {
					and =" jd_dm = '"+jd+"'";
				}
				try {
					
						String sql = "select distinct DJRQLX YEAR,sum(ZHS)  ZHS,sum(JSHS)  JSHS from SW_DJRQLX where "+and+"  group by DJRQLX  order by CAST(DJRQLX AS INTEGER)";
									
						List<Map<String, Object>> list = bs.query(sql);
						
						Map<String, String> map;
						for (int i = 0; i < list.size(); i++) {
							map = new HashMap<String, String>();
							for (String key : list.get(i).keySet()) {
								map.put(key.toLowerCase(), list.get(i).get(key).toString());
							}
							list1.add(map);
						}
						
						return this.toJson("000", "查询成功!!",list1);
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
					return this.toJson("009", "查询失败!!");
				}
			}
		
		/**
		 * 数据挖掘年份
		 * @param request
		 * @param response
		 * @param form
		 * @return
		 */
		@RequestMapping(value="YEARDatamining.do",produces = "text/plain;charset=utf-8")
		@ResponseBody
		public String YEARDatamining(HttpServletRequest request , HttpServletResponse response,
				@RequestParam Map<String, Object> form){
			List<Map<String, Object>> list = new ArrayList<>();
			String jd = (String) user.get("DWID");
			String and ="";
			if(null != jd && jd != " ") {
				and =" 1=1";
			}else {
				and =" jd_dm = '"+jd+"'";
			}
			try{
				Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
				//if (getValue(user.get("DWID")).equals("00")) {
				
					String sql = "select  distinct* from SW_DJRQLX  where "+and+" order by CAST(DJRQLX AS INTEGER) ";
					list = bs.query(sql);
					return this.toJson("000", "查询成功！",list);
//				}else {
//					String sql = "select * from SW_DJRQLX where jd_dm = '"+getValue(user.get("DWID"))+"'  order by CAST(DJRQLX AS INTEGER)";
//					list = bs.query(sql);
//					return this.toJson("000", "查询成功！",list);
//				}
			}catch(Exception e){
				e.printStackTrace();
				return this.toJson("001", "查询失败！",list);
			}
		}
		// 导出excel(年份类型)
				 public Object exportYEAR(HttpServletRequest request , HttpServletResponse response,
							@RequestParam Map<String, Object> form) {
					  try {
					   // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
						  String jd = (String) user.get("DWID");
							String and ="";
							if(null != jd && jd == "00") {
								and =" jd_dm='00'";
								
							}else {
								and =" jd_dm = '"+jd+"'";
							}
					   // 街道
					   String sqlall="select distinct* from SW_DJRQLX where "+and+" order by CAST(DJRQLX AS INTEGER) ";
					   List<Map<String, Object>> map=bs.query(sqlall);
					   Map<String, Object> map1 = new HashMap<String, Object>();
					   String[] cols = { "登录日期类型", "总户数", "见税户数", "2017见税户数" , "2017见税户数占比" , "2018见税户数", "2018见税户数占比", "2019见税户数", "2019见税户数占比", "2017税收金额（元）", "2017税收金额占比", "2018税收金额（元）", "2018税收金额占比", "2019税收金额（元）", "2019税收占比"};
					   String[] keys = { "DJRQLX", "ZHS", "JSHS", "SEVENJSHS" , "SEVENJSHSZB" , "EIGHTJSHS", "EIGHTJSHSZB", "NINEJSHS", "NINEJSHSZB", "SEVENSSJE", "SEVENSSJEZB", "EIGHTSSJE", "EIGHTSSJEZB", "NINETSSJE", "NINETSSJEZB"};
					   map1.put("fileName", "按年份分析.xls");
					   map1.put("cols", cols);
					   map1.put("keys", keys);
					   map1.put("list", map);
					   return map1;
					  } catch (Exception e) {
						  e.printStackTrace();
						  return this.toJson("009", "查询异常！");
					  }
				 }
		
		
		 
		//(上面的第一张表格)
		 public Object ssTop(HttpServletRequest request , HttpServletResponse response,
					@RequestParam Map<String, Object> form) {
			 user = (Map<String, Object>) request.getSession().getAttribute("user");
			 String jd = (String) user.get("DWID");
				String and ="";
				if(null != jd && jd == "00") {
					and =" jd_dm='00'";
					
				}else {
					and =" jd_dm = '"+jd+"'";
				}
			  try {
			   // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
				  
			   String sqlall = "select distinct  * from SWZTQK where "+and+"";
			   System.out.println(sqlall);
			   List<Map<String, Object>> map=bs.query(sqlall);
			   Map<String, Object> map1 = new HashMap<String, Object>();
			   String[] cols = { "课征主体类型", "总户数", "见税户数", "见税户数占比" , "今年税收金额","今年税收占比" };
			   String[] keys = { "KZZTLX", "ZHS", "JSHS", "JSHSZB" , "JNSSJE","JNSSZB" };
			   map1.put("fileName", "税务总体情况.xls");
			   map1.put("cols", cols);
			   map1.put("keys", keys);
			   map1.put("list", map);
			   return map1;
			  } catch (Exception e) {
				  e.printStackTrace();
				  return this.toJson("009", "查询异常！");
			  }
		 }
		 
			//(上面的第二张表格)  
		 public Object hyzbAll(HttpServletRequest request , HttpServletResponse response,
					@RequestParam Map<String, Object> form) {
			 user = (Map<String, Object>) request.getSession().getAttribute("user");
			 String jd = (String) user.get("DWID");
				String and ="";
				if(null != jd && jd == "00") {
					and =" jd_dm='00'";
					
				}else {
					and =" jd_dm = '"+jd+"'";
				}
			  try {
			   // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			   String jd_dm = getValue(request.getSession().getAttribute("dwid"));
			  
			   String sqlall = "select distinct* from SW_DJZCLX where "+and+"";
			   List<Map<String, Object>> map=bs.query(sqlall);
		
			   Map<String, Object> map1 = new HashMap<String, Object>();
			   String[] cols = { "登记注册类型", "总户数", "见税户数", "见税户数占比" , "今年税收金额","今年税收占比"};
			   String[] keys = { "DJZCLX", "ZHS", "JSHS", "JSHSZB" , "JNSSJE","JNSSZB" };
			   map1.put("fileName", "按登记注册类型.xls");
			   map1.put("cols", cols);
			   map1.put("keys", keys);
			   map1.put("list", map);
			   return map1;
			  } catch (Exception e) {
				  e.printStackTrace();
				  return this.toJson("009", "查询异常！");
			  }
		 }

		 // 导出excel(下面的第一张表格)
		 public Object exportop1(HttpServletRequest request , HttpServletResponse response,
					@RequestParam Map<String, Object> form) {
			  try {
				 user = (Map<String, Object>) request.getSession().getAttribute("user");
					 String jd = (String) user.get("DWID");
						String and ="";
						if(null != jd && jd == "00") {
							and =" jd_dm='00'";
							
						}else {
							and =" jd_dm = '"+jd+"'";
						}
			   String jd_dm=getValue(request.getSession().getAttribute("dwid"));
			   String yearNmonth = getValue(form.get("yearNmonth")).toString();//年月
			   String sqlall="select distinct * from SW_HYLX where"+and+"";
			   List<Map<String, Object>> map=bs.query(sqlall);
		
			   Map<String, Object> map1 = new HashMap<String, Object>();
			   String[] cols = { "行业", "总户数", "见税户数", "见税户数占比" , "今年税收金额","今年税收占比"};
			   String[] keys = { "HY",  "ZHS", "JSHS", "JSHSZB" , "JNSSJE","JNSSZB"};
			   map1.put("fileName", "按行业类型分析表.xls");
			   map1.put("cols", cols);
			   map1.put("keys", keys);
			   map1.put("list", map);
			   return map1;
			  } catch (Exception e) {
				  e.printStackTrace();
				  return this.toJson("009", "查询异常！");
			  }
		 }
		@RequestMapping("export.do")
		@ResponseBody
		public void export(HttpServletRequest request, HttpServletResponse response,
				@RequestParam Map<String, Object> form) throws IOException {
			Map<String, Object> o = new HashMap<String, Object>();
			try {
				String a = this.getValue(form.get("method"));
				if(null!=a && a.equals("year")) {
					o=(Map<String, Object>) this.exportYEAR(request,response,form);
				}else if(null!=a && a.equals("sw")) {
					o=(Map<String, Object>) this.ssTop(request,response,form);
				}else if(null!=a && a.equals("zc")) {
					o=(Map<String, Object>) this.hyzbAll(request,response,form);
				}else if(null!=a && a.equals("jg")) {
					o=(Map<String, Object>) this.exportop1(request,response,form);
				}else if(null!=a && a.equals("jd")) {
					o=(Map<String, Object>) this.exportJD(request,response,form);
				}else if(null!=a && a.equals("jg")) {
					o=(Map<String, Object>) this.exportop1(request,response,form);
				}
			
				
				String fileName=(String) o.get("fileName");
				List<Map<String, Object>> list=(List<Map<String, Object>>) o.get("list");
				String cols[] = (String[]) o.get("cols");//列名
				String keys[] = (String[]) o.get("keys");//map中的key
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					ExcelUtil.createWorkBook(list, keys, cols).write(os);
				} catch (IOException e) {
					e.printStackTrace();
				}
				byte[] content = os.toByteArray();
				InputStream is = new ByteArrayInputStream(content);
				// 设置response参数，可以打开下载页面
				response.reset();
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "iso-8859-1"));
				ServletOutputStream out = response.getOutputStream();
				BufferedInputStream bis = null;
				BufferedOutputStream bos = null;
				try {
					bis = new BufferedInputStream(is);
					bos = new BufferedOutputStream(out);
					byte[] buff = new byte[2048];
					int bytesRead;
					// Simple read/write loop.
					while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
						bos.write(buff, 0, bytesRead);
					}
				} catch (final IOException e) {
					throw e;
				} finally {
					if (bis != null)
						bis.close();
					if (bos != null)
						bos.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.getMessage();
			} 
		}
		
		
}
