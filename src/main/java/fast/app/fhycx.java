package fast.app;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

public class fhycx extends Super{

	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/fhycx";
		}catch(Exception e){
			e.printStackTrace();
			return "xtgl/fhycx";
		}
	}
	
	
	
	//分税种查询
	public String queryData(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");
			   
			String yearNmonth = getValue(this.getForm().get("date")).toString();//年月
			String jd = getValue(this.getForm().get("jd")).toString();//街道
			String type = getValue(this.getForm().get("type")).toString();//合计或月明细
			String nsName = getValue(this.getForm().get("nsName")).toString();//纳税人名称
			if("".equals(nsName)) {
				nsName="%";
			}
			String sortname = getValue(this.getForm().get("sortname")).toString();//税种名字
			if("".equals(sortname)) {
				sortname="%";
			}
			sortname="HYMC";
			String px = getValue(this.getForm().get("px")).toString();//排序
			String qyxz = getValue(this.getForm().get("qyxz")).toString();//企业性质
			String islp = getValue(this.getForm().get("islp")).toString();//是否合伙
			String tjkj = getValue(this.getForm().get("tjkj")).toString();//统计口径
			if("".equals(tjkj)) {
				tjkj="%";
			}
			String hylist = getValue(this.getForm().get("hylist")).toString();//行业
			String zdsyh = getValue(this.getForm().get("zdsyh")).toString();//重点税源			
			String pageNo = getValue(this.getForm().get("pageNo")).toString();//页码
			String pageSize = getValue(this.getForm().get("pageSize")).toString();//条数
			
			String starTime = "";
			String endTime = "";
			String[] star=yearNmonth.split(" - ");
			if(yearNmonth!=null &&!yearNmonth.trim().equals("")&&star!=null&&star.length>0) {
				starTime=star[0];
				endTime=star[1];
				starTime=starTime.substring(0,4)+starTime.substring(5,7);
				endTime=endTime.substring(0,4)+endTime.substring(5,7);
			}
			
			
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
			list.add(new Mode("OUT","RS",""));	
			System.out.println(list);
			//    sj_cx_new.querybyhy_new
			List<Map<String, Object>> rs =  (List<Map<String, Object>>) JdbcConnectedPro.call("sj_cx_new.querybyhy_new", list);//调用存储过程
			int counts=rs.size();
			//System.out.println(rs);
			return this.toJson("000", "查询成功！",rs,counts);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	
	//分税种查询
	public Object exportData(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");
			//从前台传过来的参数 是根据标签的name属性值获取到的
			String yearNmonth = getValue(this.getForm().get("yearNmonth")).toString();//年月
			String jd = getValue(this.getForm().get("jdlist")).toString();//街道
			String type = getValue(this.getForm().get("cxlx")).toString();//合计或月明细
			String nsName = getValue(this.getForm().get("nsName"));//纳税人名称
			if("".equals(nsName)){
				nsName="%";
			}
			String sortname = getValue(this.getForm().get("sortname"));//税种名字
			sortname="HYMC";
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
				starTime=starTime.substring(0,4)+starTime.substring(5,7);
				endTime=endTime.substring(0,4)+endTime.substring(5,7);
			}
			
			
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
		
			list.add(new Mode("OUT","RS",""));	
			System.out.println(list);
			//  sj_cx_new.querybyhy_new
			List<Map<String, Object>> rs =  (List<Map<String, Object>>) JdbcConnectedPro.call("sj_cx_new.querybyhy_new", list);//调用存储过程
			Map<String, Object> map = new HashMap<String, Object>();
			String[] cols = { "行业名称", "增值税", "营改增增值税", "营业税", "企业所得税（国税）", "企业所得税（地税）", "企业所得税（合计）","个人所得税","房产税","印花税","车船税","小计",
					" 城市维护建设税","地方教育附加","教育附加","合计"};
			/*
			 * HYDM=12,HYMC=租赁和商务服务业，ZZS=804768.7, YGZZZS=38123781.83,YYS=0,
			 * QYSDSG=42864728.28,QYSDSD=0, QYSDSH=42864728.28,GRSDS=0， FCS=0,YHS=0,
			 * CCS=0,XJ=81793278.81,CSWHJSS=0, DFJYFJ=0, JYFJ=0, HJ=81793278.81,
			 */
			String[] keys = { "HYMC", "ZZS", "YGZZZS", "YYS", "QYSDSG", "QYSDSD","QYSDSH", "GRSDS",
					"FCS","YHS","CCS","XJ","CSWHJSS","DFJYFJ","JYFJ","HJ"};
			
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
