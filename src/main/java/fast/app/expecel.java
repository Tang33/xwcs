package fast.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.Super;

public class expecel extends Super{
	
		//(上面的第一张表格)
		 public Object ssTop(Map<String, Object> rmap) {
			  try {
			   // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			   initMap(rmap);
			   String jd_dm = getValue(this.getRequest().getSession().getAttribute("dwid"));
			   String sqlall = "select yf,ROUND(dyljje/10000,2) dyljje,ROUND(tqljje/10000,2) tqljje,ROUND(zf/10000,2) zf,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) zzbl from FAST_YLJ where JD_DM="+jd_dm;
			   List<Map<String, Object>> map=this.getBs().query(sqlall);
		
			   Map<String, Object> map1 = new HashMap<String, Object>();
			   String[] cols = { "月份", "当月累计金额", "同期累计金额", "增幅" , "增长比例" };
			   String[] keys = { "YF", "DYLJJE", "TQLJJE", "ZF" , "ZZBL" };
			   map1.put("fileName", "一般公共预算收入分月情况.xls");
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
			  
			   String sqlall = "select hymc,ROUND(dnljje/10000,2) dnljje,ROUND(tqljje/10000,2) tqljje,ROUND(zf/10000,2) zf,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) zzbl,(case when zb is not null then ROUND(zb*100,2)||'%' else null end) zb from FAST_NHYZB where jd_dm='"+jd_dm+"' order by dnljje desc";
			   List<Map<String, Object>> map=this.getBs().query(sqlall);
		
			   Map<String, Object> map1 = new HashMap<String, Object>();
			   String[] cols = { "行业名称", "当年累计金额", "同期累计金额", "增幅" , "增长比例" , "占比"};
			   String[] keys = { "HYMC", "DNLJJE", "TQLJJE", "ZF" , "ZZBL", "ZB" };
			   map1.put("fileName", "一般公共预算按行业占比情况.xls");
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
			   String sqlall = "select zsxm_mc,ROUND(dnljje/10000,2) dnljje,ROUND(tqljje/10000,2) tqljje,ROUND(zf/10000,2) zf,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) zzbl,(case when zb is not null then ROUND(zb*100,2)||'%' else null end) zb from FAST_NSZZB where jd_dm ='"+jd_dm+"' order by dnljje desc";
			   List<Map<String, Object>> map=this.getBs().query(sqlall);
		
			   Map<String, Object> map1 = new HashMap<String, Object>();
			   String[] cols = { "征收项目", "当年累计金额", "同期累计金额", "增幅" , "增长比例" , "占比"};
			   String[] keys = { "ZSXM_MC", "DNLJJE", "TQLJJE", "ZF" , "ZZBL", "ZB" };
			   map1.put("fileName", "一般公共预算按税种占比情况.xls");
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
		   if (jd_dm.equals("00")) {
			   jd_dm="%";
			}
		   String sqlall="select nsrmc 纳税人名称,ROUND(dnljje/10000,2) 当年累计金额,ROUND(tqljje/10000,2) 同期累计金额,ROUND(zf/10000,2) 增幅,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end)"
		   		+ " 增长比例  from FAST_YBGGYS where bs = 0 AND JD_DM like '"+jd_dm+"%' order by dnljje desc";
		   List<Map<String, Object>> map=this.getBs().query(sqlall);
	
		   Map<String, Object> map1 = new HashMap<String, Object>();
		   String[] cols = { "纳税人名称", "当年累计金额", "同期累计金额", "增幅" , "增长比例" };
		   String[] keys = { "NSRMC", "DNLJJE", "TQLJJE", "ZF" , "ZZBL" };
		   map1.put("fileName", "一般公共预算收入前二十.xls");
		   map1.put("cols", cols);
		   map1.put("keys", cols);
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
		   // 街道
		   if (jd_dm.equals("00")) {
			   jd_dm="%";
			}
		   String sqlall="select nsrmc 纳税人名称,ROUND(dnljje/10000,2) 当年累计金额,ROUND(tqljje/10000,2) 同期累计金额,ROUND(zf/10000,2) 增幅,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) 增长比例  from FAST_YBGGYS where bs = 1 AND JD_DM like '"+jd_dm+"%' order by dnljje desc";
		   List<Map<String, Object>> map=this.getBs().query(sqlall);
	
		   Map<String, Object> map1 = new HashMap<String, Object>();
		   String[] cols = { "纳税人名称", "当年累计金额", "同期累计金额", "增幅" , "增长比例" };
		   String[] keys = { "NSRMC", "DNLJJE", "TQLJJE", "ZF" , "ZZBL" };
		   map1.put("fileName", "一般公共预算收入增幅前二十.xls");
		   map1.put("cols", cols);
		   map1.put("keys", cols);
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
		   // 街道
		   if (jd_dm.equals("00")) {
			   jd_dm="%";
			}
		   String sqlall="select nsrmc 纳税人名称,ROUND(dnljje/10000,2) 当年累计金额,ROUND(tqljje/10000,2) 同期累计金额,ROUND(zf/10000,2) 增幅,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) 增长比例  from FAST_YBGGYS where bs = 2 AND JD_DM like '"+jd_dm+"%' order by dnljje desc";
		   List<Map<String, Object>> map=this.getBs().query(sqlall);
	
		   Map<String, Object> map1 = new HashMap<String, Object>();
		   String[] cols = { "纳税人名称", "当年累计金额", "同期累计金额", "增幅" , "增长比例" };
		   String[] keys = { "NSRMC", "DNLJJE", "TQLJJE", "ZF" , "ZZBL" };
		   map1.put("fileName", "一般公共预算收入减幅前二十.xls");
		   map1.put("cols", cols);
		   map1.put("keys", cols);
		   map1.put("list", map);
		   return map1;
		  } catch (Exception e) {
			  e.printStackTrace();
			  return this.toJson("009", "查询异常！");
		  }
	 }


}