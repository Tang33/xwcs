package fast.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.Super;

public class expecel2 extends Super{
	
		//(上面的第一张表格)
		 public Object ssTop(Map<String, Object> rmap) {
			  try {
			   // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			   initMap(rmap);
			   String jd_dm = getValue(this.getRequest().getSession().getAttribute("dwid"));
			   String sqlall = "select * from SWZTQK";
			   System.out.println(sqlall);
			   List<Map<String, Object>> map=this.getBs().query(sqlall);
		
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
		 
		//(上面的第二张表格)  select * from FAST_NHYZB order by dnljje desc
		 public Object hyzbAll(Map<String, Object> rmap) {
			  try {
			   // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			   initMap(rmap);
			   String jd_dm = getValue(this.getRequest().getSession().getAttribute("dwid"));
			  
			   String sqlall = "select * from SW_DJZCLX";
			   List<Map<String, Object>> map=this.getBs().query(sqlall);
		
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
		 
		 //szzbAll  select * from FAST_NSZZB order by dnljje desc
		 public Object szzbAll(Map<String, Object> rmap) {
			  try {
			   // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			   initMap(rmap);
			   String jd_dm=getValue(this.getRequest().getSession().getAttribute("dwid"));
			   String sqlall = "select * from SW_JDLX";
			   List<Map<String, Object>> map=this.getBs().query(sqlall);
		
			   Map<String, Object> map1 = new HashMap<String, Object>();
			   String[] cols = { "街道",  "总户数", "见税户数", "见税户数占比" , "今年税收金额","今年税收占比"};
			   String[] keys = { "JD",  "ZHS", "JSHS", "JSHSZB" , "JNSSJE","JNSSZB"};
			   map1.put("fileName", "按街道类型分析.xls");
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
	 public Object exportop1(Map<String, Object> rmap) {
		  try {
		   // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
		   initMap(rmap);
		   String jd_dm=getValue(this.getRequest().getSession().getAttribute("dwid"));
		   String yearNmonth = getValue(this.getForm().get("yearNmonth")).toString();//年月
		   // 街道
		   
		   String sqlall="select * from SW_HYLX";
		   List<Map<String, Object>> map=this.getBs().query(sqlall);
	
		   Map<String, Object> map1 = new HashMap<String, Object>();
		   String[] cols = { "行业", "总户数", "见税户数", "见税户数占比" , "今年税收金额","今年税收占比"};
		   String[] keys = { "HY",  "ZHS", "JSHS", "JSHSZB" , "JNSSJE","JNSSZB"};
		   map1.put("fileName", "一般公共预算收入前二十.xls");
		   map1.put("cols", cols);
		   map1.put("keys", keys);
		   map1.put("list", map);
		   return map1;
		  } catch (Exception e) {
			  e.printStackTrace();
			  return this.toJson("009", "查询异常！");
		  }
	 }
	 
	 //(下面的第二张表格)
	 public Object ssZFTop(Map<String, Object> rmap) {
		  try {
		   // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
		   initMap(rmap);
		   String jd_dm=getValue(this.getRequest().getSession().getAttribute("dwid"));
		   String yearNmonth = getValue(this.getForm().get("yearNmonth")).toString();//年月
		   // 街道
		   
		   String sqlall="select nsrmc 纳税人名称,ROUND(dnljje/10000,2) 当年累计金额,ROUND(tqljje/10000,2) 同期累计金额,ROUND(zf/10000,2) 增幅,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) 增长比例  from FAST_YBGGYS where bs = 1 AND JD_DM="+jd_dm+" order by dnljje desc";
		   List<Map<String, Object>> map=this.getBs().query(sqlall);
	
		   Map<String, Object> map1 = new HashMap<String, Object>();
		   String[] cols = { "纳税人名称", "当年累计金额", "同期累计金额", "增幅" , "增长比例" };
		   String[] keys = { "NSRMC", "DNLJJE", "TQLJJE", "ZF" , "ZZBL" };
		   map1.put("fileName", "一般公共预算收入增幅前二十.xls");
		   map1.put("cols", cols);
		   map1.put("keys", keys);
		   map1.put("list", map);
		   return map1;
		  } catch (Exception e) {
			  e.printStackTrace();
			  return this.toJson("009", "查询异常！");
		  }
	 }
	 
	 
	//(下面的第三张表格)
	 public Object ssJFTop(Map<String, Object> rmap) {
		  try {
		   // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
		   initMap(rmap);
		   String jd_dm=getValue(this.getRequest().getSession().getAttribute("dwid"));
		   String yearNmonth = getValue(this.getForm().get("yearNmonth")).toString();//年月
		   // 街道
		   
		   String sqlall="select nsrmc 纳税人名称,ROUND(dnljje/10000,2) 当年累计金额,ROUND(tqljje/10000,2) 同期累计金额,ROUND(zf/10000,2) 增幅,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) 增长比例  from FAST_YBGGYS where bs = 2 AND JD_DM="+jd_dm+" order by dnljje desc";
		   List<Map<String, Object>> map=this.getBs().query(sqlall);
	
		   Map<String, Object> map1 = new HashMap<String, Object>();
		   String[] cols = { "纳税人名称", "当年累计金额", "同期累计金额", "增幅" , "增长比例" };
		   String[] keys = { "NSRMC", "DNLJJE", "TQLJJE", "ZF" , "ZZBL" };
		   map1.put("fileName", "一般公共预算收入减幅前二十.xls");
		   map1.put("cols", cols);
		   map1.put("keys", keys);
		   map1.put("list", map);
		   return map1;
		  } catch (Exception e) {
			  e.printStackTrace();
			  return this.toJson("009", "查询异常！");
		  }
	 }


}