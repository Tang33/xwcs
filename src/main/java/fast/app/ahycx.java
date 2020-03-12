package fast.app;


import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.ss.usermodel.Workbook;

import fast.main.util.ExcelUtil;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

public class ahycx extends Super{

	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/ahycx";
		}catch(Exception e){
			e.printStackTrace();
			return "xtgl/ahycx";
		}
	}
	
	
	public String queryInit(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");   
			String sql=this.getSql("jd_query");
			List<Map<String, Object>> jdlist=this.getBs().query(sql);
			sql=this.getSql("hy_query");
			List<Map<String, Object>> hylist=this.getBs().query(sql);
			Map<String, List<Map<String, Object>>> map=new HashMap<String, List<Map<String,Object>>>();
			map.put("hylist", hylist);
			map.put("jdlist", jdlist);
			return this.toJson("000", "查询成功！",map);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	//分税种查询
	public String queryData(Map<String, Object> rmap){
		try{
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");

			String yearNmonth = getValue(this.getForm().get("date")).toString();//年月
			String starTime = ""; String endTime = ""; 
			String[] star=yearNmonth.split(" - ");
			if(yearNmonth!=null &&!yearNmonth.trim().equals("")&&star!=null&&star.length>0) {
				starTime=star[0]; endTime=star[1];
				starTime=starTime.substring(0,4)+starTime.substring(5,7);
				endTime=endTime.substring(0,4)+endTime.substring(5,7); 
			}
			String hylist =getValue(this.getForm().get("hylist")).toString();//行业 
			String tjkj =getValue(this.getForm().get("tjkj")).toString();//统计口径 
			int count = Integer.parseInt(getValue(this.getForm().get("pageNo")));//
			int count1 = Integer.parseInt(getValue(this.getForm().get("pageSize")));//
			count = (count - 1) * count1;
			count1 = count + count1;
			List<Mode> listcount = new ArrayList<Mode>();
			listcount.add(new Mode("IN", "String", "%"));// 纳税人名字
			/*
			 * listcount.add(new Mode("IN", "String", jd));// 街道代码
			 */			
			listcount.add(new Mode("IN", "String", starTime));// 时间起
			listcount.add(new Mode("IN", "String", endTime));// 时间止
			listcount.add(new Mode("IN", "String", "0"));// 是否按月合计
			listcount.add(new Mode("IN", "String", "%"));// 企业性质
			listcount.add(new Mode("IN", "String", hylist));// 行业代码
			listcount.add(new Mode("IN", "String", "1"));// 是否合伙
			listcount.add(new Mode("IN", "String", "%"));// 重点税源户代码
			listcount.add(new Mode("OUT", "RS", ""));
			// SJ_CX_2012.QUERYBYQY_JLS
			List<Map<String, Object>> rscount = (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_2012.QUERYBYQY_JLS", listcount);// 调用存储过程
			
			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN","String","%"));//纳税人名字
			list.add(new Mode("IN","String",starTime));//时间起
			list.add(new Mode("IN","String",endTime));//时间止
			list.add(new Mode("IN","String","0"));//是否按月合计
			list.add(new Mode("IN","String","%"));//企业性质
			list.add(new Mode("IN","String",hylist));//行业代码
			list.add(new Mode("IN","String","1"));//是否合伙
			list.add(new Mode("IN","String","ZSE"));//税种名字
			list.add(new Mode("IN","String","DESC"));//排序方式
			list.add(new Mode("IN","String",tjkj));//统计口径
			list.add(new Mode("IN","String","%"));//重点税源户代码			
			list.add(new Mode("IN","String",count+""));//写死
			list.add(new Mode("IN","String",count1+""));//写死
			list.add(new Mode("OUT","RS",""));	
			// SJ_CX_2012.QUERYBYQY201605_NEW1  SJ_CX_2012.QUERYBYQY201605_NEW1
			List<Map<String, Object>> rs =  (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_2012.QUERYBYQY201605_NEW1", list);//调用存储过程
						
			System.out.println(rs);
			int counts = Integer.parseInt(getValue(rscount.get(0).get("COUNTS")));
			
			return this.toJson("000", "查询成功！",rs,counts);
			
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	//分税种查询
	public Object exportData(Map<String, Object> rmap){
		try{
			//初始化        
			//bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");
			
			String yearNmonth = getValue(this.getForm().get("yearNmonth")).toString();//年月
			String starTime = ""; String endTime = ""; 
			String[] star=yearNmonth.split(" - ");
			if(yearNmonth!=null &&!yearNmonth.trim().equals("")&&star!=null&&star.length>0) {
				starTime=star[0]; endTime=star[1];
				starTime=starTime.substring(0,4)+starTime.substring(5,7);
				endTime=endTime.substring(0,4)+endTime.substring(5,7); 
			}
			String hylist =getValue(this.getForm().get("hylist")).toString();//行业 
			String tjkj =getValue(this.getForm().get("tjkj")).toString();//统计口径 
			String countTotal =getValue(this.getForm().get("myCount")).toString();//条数		
			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN","String","%"));//纳税人名字
			list.add(new Mode("IN","String",starTime));//时间起
			list.add(new Mode("IN","String",endTime));//时间止
			list.add(new Mode("IN","String","0"));//是否按月合计
			list.add(new Mode("IN","String","%"));//企业性质
			list.add(new Mode("IN","String",hylist));//行业代码
			list.add(new Mode("IN","String","1"));//是否合伙
			list.add(new Mode("IN","String","ZSE"));//税种名字
			list.add(new Mode("IN","String","DESC"));//排序方式
			list.add(new Mode("IN","String",tjkj));//统计口径
			list.add(new Mode("IN","String","%"));//重点税源户代码			
			list.add(new Mode("IN","String","0"));//写死
			list.add(new Mode("IN","String","800000"));//写死
			list.add(new Mode("OUT","RS",""));	
			String sortname="ZSE";
			// SJ_CX_2012.QUERYBYQY201605_NEW1  SJ_CX_2012.QUERYBYQY201605_NEW1
			List<Map<String, Object>> rs =  (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_2012.QUERYBYQY201605_NEW1", list);//调用存储过程
//			if (sortname.equals("ZSE")) {
//				long start = System.currentTimeMillis();
//				rs=Sort(rs,"DESC");
//				System.out.println("调用排序用时："+(System.currentTimeMillis()-start)/1000.0);
//				Map<String, Object> map =rs.get(0);
//				Map<String, Object> map1 =rs.get(1);
//				rs.remove(0);
//				rs.remove(0);
//				rs.add(map);
//				rs.add(map1);
//			}
				
			Map<String, Object> map = new HashMap<String, Object>();
			String[] cols= {"企业名称","增值税","营改增增值税", "营业税","企业所得税（国税）","企业所得税（地税）",
					  		"个人所得税","房产税","印花税", "车船税","城市维护建设税","地方教育附加","教育费附加",
					  			"城镇土地使用税", "环保税","合计"};
			
			  String[] keys= {"NSRMC","ZZS","YGZZZS", "YYS","QYSDS_GS","QYSDS_DS",
					  		"GRSDS","FCS","YHS", "CCS","CSWHJSS","DFJYFJ","JYFJ",
					  			"CZTDSYS","HBS","HJ"};
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
	
	private List<Map<String, Object>> Sort(List<Map<String, Object>> list,String px) {
		System.out.println(px);
		if (px.equals("ASC")) {
			Collections.sort(list, new Comparator<Map<String, Object>>() {
	            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
	            		Double name1 = Double.valueOf(o1.get("HJ").toString()) ;//name1是从你list里面拿出来的一个 
	            		Double name2 = Double.valueOf(o2.get("HJ").toString()) ; //name1是从你list里面拿出来的第二个name
	            		return name1.compareTo(name2);
	            }
	        });
		}else {
			Collections.sort(list, new Comparator<Map<String, Object>>() {
	            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
	            	Double name1 = Double.valueOf(o1.get("HJ").toString()) ;//name1是从你list里面拿出来的一个 
	            	Double name2 = Double.valueOf(o2.get("HJ").toString()) ; //name1是从你list里面拿出来的第二个name
	                return name2.compareTo(name1);
	            }
	        });
		}
		 
	        return list;
	}
}
