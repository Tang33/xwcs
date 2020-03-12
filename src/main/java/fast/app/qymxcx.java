package fast.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;



/**
 * 企业明细查询
 * @author Administrator
 *
 */
public class qymxcx extends Super {
	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "wbw/qymxcx";
		}catch(Exception e){
			e.printStackTrace();
			return "wbw/qymxcx";
		}
	}
	
	
	/**
	 * 初始化查询数据
	 * @param rmap
	 * @return
	 */
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
	
	

	
	/**
	 * 企业汇总表
	 * @param rmap
	 * @return
	 */
	public String queryMxcx(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			
	
			String pageNo = getValue(this.getForm().get("pageNo"));
			String pageSize = getValue(this.getForm().get("pageSize"));
			
			String cxrq = getValue(this.getForm().get("cxrq"));
			String nsrmc = getValue(this.getForm().get("nsrmc"));
			String jd_dm = getValue(this.getForm().get("jd_dm"));
			String qyxz = getValue(this.getForm().get("qyxz"));
			String hy_dm = getValue(this.getForm().get("hy_dm"));
			String hhqy = getValue(this.getForm().get("hhqy"));

			String sortname = getValue(this.getForm().get("sortname"));
			String sorttype = getValue(this.getForm().get("sorttype"));
			String tjkj = getValue(this.getForm().get("tjkj"));
			String yhj = getValue(this.getForm().get("yhj"));
			String fxlx = getValue(this.getForm().get("fxlx"));
			String zdsyh = getValue(this.getForm().get("zdsyh"));
			
			String qsrq="";
			String jzrq="";
			
			if("".equals(cxrq) || cxrq ==null) {
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				Calendar c = Calendar.getInstance();
				c.add(Calendar.YEAR, -1);// 
				qsrq = sdf.format(c.getTime());
				jzrq = sdf.format(dt);
			}else {
				qsrq = cxrq.substring(0,cxrq.indexOf('-')).trim();
				jzrq = cxrq.substring(cxrq.indexOf('-')+1).trim();
			}
			
			String count = getValue(this.getForm().get("count"));// 先获取页面数据总量

			int counts = 0;// 声明一个变量暂时存储查到的数据总量

			if ("".equals(pageNo)) {
				// 1表示是合伙企业

			} else {
				// 如果为空，则是首次查询，
				if (count.equals("0")) {


					List<Mode> list=new ArrayList<Mode>();
					list.add(new Mode("IN","String",nsrmc));
					list.add(new Mode("IN","String",jd_dm));
					list.add(new Mode("IN","String",qsrq));
					list.add(new Mode("IN","String",jzrq));
					
					list.add(new Mode("IN","String",yhj));
					list.add(new Mode("IN","String",qyxz));
					list.add(new Mode("IN","String",hy_dm));
					list.add(new Mode("IN","String",hhqy));
					list.add(new Mode("IN","String",sortname));
					list.add(new Mode("IN","String",sorttype));
					
					list.add(new Mode("IN","String",tjkj));
					list.add(new Mode("IN","String",zdsyh));

					list.add(new Mode("IN","String","1"));
					list.add(new Mode("IN","String","10000"));

					list.add(new Mode("IN","String",fxlx));
					list.add(new Mode("OUT","RS",""));
					List<Map<String, Object>> rsjList= (List<Map<String, Object>>) JdbcConnectedPro.call("PKG_CX_WBW.QUERYBYQY_NEW1", list);
					System.out.println(rsjList);
					if (rsjList.size()>0) {
						counts = rsjList.size();
					} else {
						counts=0;
					}
				}else {
					counts=Integer.parseInt(count);
				}
			}
			
			
			String recorders1 = String.valueOf((Integer.parseInt(pageNo)-1)*Integer.parseInt(pageSize));// 查询条数开始数
			String recorders2 = String.valueOf(Integer.parseInt(pageNo)*Integer.parseInt(pageSize));// 查询条数结束数
			
			
			List<Mode> list=new ArrayList<Mode>();
			list.add(new Mode("IN","String",nsrmc));
			list.add(new Mode("IN","String",jd_dm));
			list.add(new Mode("IN","String",qsrq));
			list.add(new Mode("IN","String",jzrq));
			
			list.add(new Mode("IN","String",yhj));
			list.add(new Mode("IN","String",qyxz));
			list.add(new Mode("IN","String",hy_dm));
			list.add(new Mode("IN","String",hhqy));
			list.add(new Mode("IN","String",sortname));
			list.add(new Mode("IN","String",sorttype));
			
			list.add(new Mode("IN","String",tjkj));
			list.add(new Mode("IN","String",zdsyh));

			list.add(new Mode("IN","String",recorders1));
			list.add(new Mode("IN","String",recorders2));

			list.add(new Mode("IN","String",fxlx));
			list.add(new Mode("OUT","RS",""));
			List<Map<String, Object>> sjList= (List<Map<String, Object>>) JdbcConnectedPro.call("PKG_CX_WBW.QUERYBYQY_NEW1", list);
			System.out.println(sjList);
			return this.toJson("000", "查询成功！",sjList,counts);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	
	// 导出excel
			public Object export(Map<String, Object> rmap) {
				try {
					// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
					initMap(rmap);

					String qsrq = getValue(this.getForm().get("cxrq"));
					String jzrq = getValue(this.getForm().get("cxrq"));
					String nsrmc = getValue(this.getForm().get("nsrmc"));
					String jd_dm = getValue(this.getForm().get("jd_dm"));//
					String qyxz = getValue(this.getForm().get("qyxz"));//
					String hy_dm = getValue(this.getForm().get("hy_dm"));//
					String hhqy = getValue(this.getForm().get("hhqy"));

					String sortname = getValue(this.getForm().get("sortname"));
					String sorttype = getValue(this.getForm().get("sorttype"));
					String tjkj = getValue(this.getForm().get("tjkj"));
					String yhj = getValue(this.getForm().get("yhj"));
					String fxlx = getValue(this.getForm().get("fxlx"));
					String zdsyh = getValue(this.getForm().get("zdsyh"));//
					
					if("".equals(jd_dm) || jd_dm ==null) {
						jd_dm="%";
					}
					if("".equals(qyxz) || qyxz ==null) {
						qyxz="%";
					}
					if("".equals(hy_dm) || hy_dm ==null) {
						hy_dm="%";
					}
					if("".equals(zdsyh) || zdsyh ==null) {
						zdsyh="%";
					}
					
					
//					String nd="";
//					String yf="";
//					
////					将年月拆开
//					if (qsrq!=null && !qsrq.equals("")) {
//						nd = qsrq.substring(0,qsrq.indexOf('-'));
//						yf = qsrq.substring(qsrq.indexOf('-')+1);
//					}

					
					List<Mode> list=new ArrayList<Mode>();
					list.add(new Mode("IN","String",nsrmc));
					list.add(new Mode("IN","String",jd_dm));
					list.add(new Mode("IN","String","201805"));
					list.add(new Mode("IN","String","201905"));
					
					list.add(new Mode("IN","String",yhj));
					list.add(new Mode("IN","String",qyxz));
					list.add(new Mode("IN","String",hy_dm));
					list.add(new Mode("IN","String",hhqy));
					list.add(new Mode("IN","String",sortname));
					list.add(new Mode("IN","String",sorttype));
					
					list.add(new Mode("IN","String",tjkj));
					list.add(new Mode("IN","String",zdsyh));

					list.add(new Mode("IN","String","1"));
					list.add(new Mode("IN","String","10000"));

					list.add(new Mode("IN","String",fxlx));
					list.add(new Mode("OUT","RS",""));
					List<Map<String, Object>> sjList= (List<Map<String, Object>>) JdbcConnectedPro.call("PKG_CX_WBW.QUERYBYQY_NEW1", list);
					System.out.println(sjList);

					Map<String, Object> map = new HashMap<String, Object>();
					String[] cols = { "所属街道", "纳税人名称", "税收", "上年同期", "营业收入", "上年同期", "增值税", "\"营改增\"增值税", "营业税", "企业所得税（国税）", "企业所得税（地税）", "企业所得税（合计）", "个人所得税", "房产税", "印花税", "车船税", "合计"};
					String[] keys = { "JD_MC", "NSRMC", "SS", "SN_SS", "YYS1", "SN_SS", "ZZS", "YGZZZS", "YYS", "QYSDS_GS", "QYSDS_DS", "QYSDS", "GRSDS", "FCS", "YHS", "CCS", "HJ"};
					map.put("fileName", "导出excel.xlsx");
					map.put("cols", cols);
					map.put("keys", keys);
					map.put("list", sjList);
					return map;
				} catch (Exception e) {
					e.printStackTrace();
					return this.toJson("009", "查询异常！");
				}
			}
		
	
	
	
	

}
