package fast.app;


import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

public class fszcx extends Super{

	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/fszcx";
		}catch(Exception e){
			e.printStackTrace();
			return "xtgl/fszcx";
		}
	}
	
	//分税种查询
	public String queryData(Map<String, Object> rmap){
		try{
			//初始化        
			//bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");			
			String yearNmonth = getValue(this.getForm().get("date")).toString();//获取到起止年月	
			String pageNo = getValue(this.getForm().get("pageNo")).toString();//页码
			String pageSize = getValue(this.getForm().get("pageSize")).toString();//条数
			
			String starTime = "";
			String endTime = "";
			String[] star=yearNmonth.split(" - ");
			if(yearNmonth!=null &&!yearNmonth.trim().equals("")&&star!=null&&star.length>0) {
				starTime=star[0];
				endTime=star[1];
			}
			starTime=starTime.substring(0,4)+starTime.substring(5,7);
			endTime=endTime.substring(0,4)+endTime.substring(5,7);
			
			
			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN","String",starTime));
			list.add(new Mode("IN","String",endTime));
			list.add(new Mode("OUT","RS",""));		
			List<Map<String, Object>> rs =  (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYSZ", list);
			System.out.println(rs);
			int count = rs.size();
			return this.toJson("000", "查询成功！",rs,count);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	
	public Object exportData(Map<String, Object> rmap){
		try{
			//初始化        
			//bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");			
			String yearNmonth = getValue(this.getForm().get("date")).toString();//获取到起止年月	
			String starTime = "";
			String endTime = "";
			String[] star=yearNmonth.split(" - ");
			if(yearNmonth!=null &&!yearNmonth.trim().equals("")&&star!=null&&star.length>0) {
				starTime=star[0];
				endTime=star[1];
			}
			starTime=starTime.substring(0,4)+starTime.substring(5,7);
			endTime=endTime.substring(0,4)+endTime.substring(5,7);
			
			
			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN","String",starTime));
			list.add(new Mode("IN","String",endTime));
			list.add(new Mode("OUT","RS",""));		
	//		SJ_CX_NEW.QUERYBYSZ
			List<Map<String, Object>> rs =  (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYSZ", list);		
			System.out.println(rs);
			Map<String, Object> map = new HashMap<String, Object>();
			String[] cols = { "增值税", "营业税", "企业所得税", "个人所得税", "房产税", "印花税", "车船税", "合计"};
			
			String[] keys = { "ZZS", "YYS", "QYSDS", "GRSDS", "FCS", "YHS", "CCS", "HJ"};
			
			map.put("fileName", "导出excel.xlsx");
			map.put("cols", cols);
			map.put("keys", keys);
			map.put("list", rs);
			return map;
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	
	
}
