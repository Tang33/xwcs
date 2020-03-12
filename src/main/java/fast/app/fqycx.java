package fast.app;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

public class fqycx extends Super{

	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/fqycx";
		}catch(Exception e){
			e.printStackTrace();
			return "xtgl/fqycx";
		}
	}
	
	//按企业查询
	public String queryData(Map<String, Object> rmap){
		try{
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");
			
			String cxrq = getValue(this.getForm().get("date"));//时间
			String qsrq="";
			String jzrq="";
			String[] star = cxrq.split(" - ");
			if (cxrq != null && !cxrq.trim().equals("") && star != null && star.length > 0) {
				qsrq = star[0];
				jzrq = star[1];
				qsrq = qsrq.substring(0, 4) + qsrq.substring(5, 7);
				jzrq = jzrq.substring(0, 4) + jzrq.substring(5, 7);
			}
			
			
			String jd_dm = getValue(this.getForm().get("jd"));//街道			
			String nsrmc = getValue(this.getForm().get("nsName"));//纳税人名称
			String zdsyh = getValue(this.getForm().get("zdsyh"));//重点税源户
			String tjkj = getValue(this.getForm().get("tjkj"));//统计口径
			String sortname = getValue(this.getForm().get("sortname"));//纳税名称
			String sorttype = getValue(this.getForm().get("px"));//排序方式
			String hy_dm = getValue(this.getForm().get("hylist"));//行业
			String qyxz = getValue(this.getForm().get("qyxz"));//企业性质			
			String hhqy = getValue(this.getForm().get("islp"));//合伙企业
			String yhj = getValue(this.getForm().get("type"));//月合计
			int count = Integer.parseInt(getValue(this.getForm().get("pageNo")));// 写死 0
			int count1 = Integer.parseInt(getValue(this.getForm().get("pageSize")));// 写死 80000
			count = (count - 1) * count1;
			count1 = count + count1;

			List<Mode> listcount = new ArrayList<Mode>();
			listcount.add(new Mode("IN", "String", nsrmc));// 纳税人名字
			listcount.add(new Mode("IN", "String", jd_dm));// 街道代码
			listcount.add(new Mode("IN", "String", qsrq));// 时间起
			listcount.add(new Mode("IN", "String", jzrq));// 时间止
			listcount.add(new Mode("IN", "String", yhj));// 是否按月合计
			listcount.add(new Mode("IN", "String", qyxz));// 企业性质
			listcount.add(new Mode("IN", "String", hy_dm));// 行业代码
			listcount.add(new Mode("IN", "String", hhqy));// 是否合伙
			listcount.add(new Mode("IN", "String", zdsyh));// 重点税源户代码
			listcount.add(new Mode("OUT", "RS", ""));		
			List<Map<String, Object>> rscount = (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYQY_JLS", listcount);// 调用存储过程
			System.out.println(rscount);
			
			
			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN","String",nsrmc));//纳税人名字
			list.add(new Mode("IN","String",jd_dm));//街道代码
			list.add(new Mode("IN","String",qsrq));//时间起
			list.add(new Mode("IN","String",jzrq));//时间止
			list.add(new Mode("IN","String",yhj));//是否按月合计
			list.add(new Mode("IN","String",qyxz));//企业性质
			list.add(new Mode("IN","String",hy_dm));//行业代码
			list.add(new Mode("IN","String",hhqy));//是否合伙
			list.add(new Mode("IN","String",sortname));//税种名字
			list.add(new Mode("IN","String",sorttype));//排序方式
			list.add(new Mode("IN","String",tjkj));//统计口径
			list.add(new Mode("IN","String",zdsyh));//重点税源户代码	
			list.add(new Mode("IN","String",getValue(count)));//写死
			list.add(new Mode("IN","String",getValue(count1)));//写死

			list.add(new Mode("OUT","RS",""));	
			// SJ_CX_2012.QUERYBYQY201605_NEW1
			List<Map<String, Object>> rs =  (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYQY_NEW1", list);//调用存储过程
			int counts = Integer.parseInt(getValue(rscount.get(0).get("COUNTS")));
			//System.out.println(rs);
			return this.toJson("000", "查询成功！",rs,counts);
			
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	
	//导出数据
		public Object exportData(Map<String, Object> rmap){
			try{
				initMap(rmap);
				this.getRequest().getSession().getAttribute("uno");
				// 注意，从前台传过来的参数是根据 标签的name属性获取的
				String yearNmonth = getValue(this.getForm().get("yearNmonth")).toString();//年月
				String jd = getValue(this.getForm().get("jdlist")).toString();//街道
				String type = getValue(this.getForm().get("cxlx")).toString();//合计或月明细
				String nsName = getValue(this.getForm().get("nsName")).toString();//纳税人名称
				String sortname = getValue(this.getForm().get("sortname")).toString();//税种名字
				
				String px = getValue(this.getForm().get("px")).toString();//排序
				String qyxz = getValue(this.getForm().get("qyxz")).toString();//企业性质
				String islp = getValue(this.getForm().get("sfhh")).toString();//是否合伙
				String tjkj = getValue(this.getForm().get("tjkj")).toString();//统计口径
				String hylist = getValue(this.getForm().get("hylist")).toString();//行业
				String zdsyh = getValue(this.getForm().get("zdsyh")).toString();//重点税源
				
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
				list.add(new Mode("IN","String",nsName));//纳税人名字
				list.add(new Mode("IN","String",jd));//街道代码
				list.add(new Mode("IN","String",starTime));//时间起
				list.add(new Mode("IN","String",endTime));//时间止
				list.add(new Mode("IN","String",type));//是否按月合计
				list.add(new Mode("IN","String",qyxz));//企业性质
				list.add(new Mode("IN","String",hylist));//行业代码
				list.add(new Mode("IN","String",islp));//是否合伙
				list.add(new Mode("IN","String",sortname));//税种名字
				list.add(new Mode("IN","String",px));//排序方式
				list.add(new Mode("IN","String",tjkj));//统计口径
				list.add(new Mode("IN","String",zdsyh));//重点税源户代码	
				list.add(new Mode("IN","String","1"));//写死
				list.add(new Mode("IN","String","80000"));//写死

				list.add(new Mode("OUT","RS",""));	
				// SJ_CX_2012.QUERYBYQY201605_NEW1
				List<Map<String, Object>> rs =  (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYQY_NEW1", list);//调用存储过程
				
				Map<String, Object> map = new HashMap<String, Object>();
				String[] cols= {"纳税人名称","所属街道","增值税","营改增增值税", "营业税","企业所得税（国税）","企业所得税（地税）",
						  			"个人所得税","房产税","印花税", "车船税","城市维护建设税","地方教育附加","教育费附加",
						  			"合计"};
				
				  String[] keys= {"NSRMC","JD_DM","ZZS","YGZZZS", "YYS","QYSDS_GS","QYSDS_DS",
						  		"GRSDS","FCS","YHS", "CCS","CSWHJSS","DFJYFJ","JYFJ",
						  			"HJ"};
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
