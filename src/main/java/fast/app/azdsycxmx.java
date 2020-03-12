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
 * 按重点税源查询明细
 * @author Administrator
 *
 */
public class azdsycxmx extends Super {
	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "zdsSjcx/azdsycxmx";
		}catch(Exception e){
			e.printStackTrace();
			return "zdsSjcx/azdsycxmx";
		}
	}
	
	

	/**
	 * 企业汇总表
	 * @param rmap
	 * @return
	 */
	public String queryAzdsycxmx(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			
			String pageNo = getValue(this.getForm().get("pageNo"));
			String pageSize = getValue(this.getForm().get("pageSize"));
			
			String cxrq = getValue(this.getForm().get("cxrq"));
			String tjkj = getValue(this.getForm().get("tjkj"));
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

			String jd="00";
			String strSql = "Select t.jd_dm as JD_DM,jd.jd_mc as JD_MC,t.lb_dm as LB_DM,t.lb_mc as LB_MC,"
					+ " (select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.lrry_dm) as LRRY_MC,"
					+ "to_char(t.LR_SJ,'yyyy-mm-dd') as LRRQ,"
					+ "(select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.xgry_dm) as XGRY_MC,"
					+ "to_char(t.XG_SJ,'yyyy-mm-dd') as XGRQ,"
					+ "to_char((select count(*) from JD_ZDSYH zdsyh where zdsyh.jd_dm=t.jd_dm and zdsyh.lb_dm=t.lb_dm)) as NSRSL"
					+ "   from JD_ZDSYLB t,dm_jd jd where t.jd_dm=jd.jd_dm and t.jd_dm='"
					+ jd + "' order by t.LB_DM";
			System.out.println(strSql);
			List<Map<String, Object>> listJd=this.getBs().query(strSql);
			List<Map<String, Object>> sjList = null;
			if(listJd!=null && listJd.size()>0){
				for (int i = 0; i < listJd.size(); i++) {
					Map<String, Object> zdsymap=(Map<String, Object>)listJd.get(i);
					String zdsyh=String.valueOf(zdsymap.get("LB_DM"));
					String zdsyhmc=String.valueOf(zdsymap.get("LB_MC"));
					//map.put("ZDSYH", zdsyh);
					List<Mode> list=new ArrayList<Mode>();
					list.add(new Mode("IN","String","NSRMC"));
					list.add(new Mode("IN","String","%"));
					list.add(new Mode("IN","String",qsrq));//qsrq
					list.add(new Mode("IN","String",jzrq));//jzrq
					list.add(new Mode("IN","String","0"));

					list.add(new Mode("IN","String","%"));
					list.add(new Mode("IN","String","%"));
					
					list.add(new Mode("IN","String","1"));
					
					list.add(new Mode("IN","String","NSRMC"));

					list.add(new Mode("IN","String","DESC"));
					
					
					list.add(new Mode("IN","String",tjkj));
					list.add(new Mode("IN","String",zdsyh));//ZDSYH
					
					
					
					list.add(new Mode("IN","String","0"));
					list.add(new Mode("IN","String","100"));
					
					
					list.add(new Mode("OUT","RS",""));
					
					sjList= (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYQY_NEW1", list);
					
				}
			}
			System.out.println("-----------");
			System.out.println(sjList);
			return this.toJson("000", "查询成功！",sjList);
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

					String tjkj = getValue(this.getForm().get("tjkj"));
					

					String jd="00";
					String strSql = "Select t.jd_dm as JD_DM,jd.jd_mc as JD_MC,t.lb_dm as LB_DM,t.lb_mc as LB_MC,"
							+ " (select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.lrry_dm) as LRRY_MC,"
							+ "to_char(t.LR_SJ,'yyyy-mm-dd') as LRRQ,"
							+ "(select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.xgry_dm) as XGRY_MC,"
							+ "to_char(t.XG_SJ,'yyyy-mm-dd') as XGRQ,"
							+ "to_char((select count(*) from JD_ZDSYH zdsyh where zdsyh.jd_dm=t.jd_dm and zdsyh.lb_dm=t.lb_dm)) as NSRSL"
							+ "   from JD_ZDSYLB t,dm_jd jd where t.jd_dm=jd.jd_dm and t.jd_dm='"
							+ jd + "' order by t.LB_DM";
					System.out.println(strSql);
					List<Map<String, Object>> listJd=this.getBs().query(strSql);
					List<Map<String, Object>> sjList = null;
					if(listJd!=null && listJd.size()>0){
						for (int i = 0; i < listJd.size(); i++) {
							Map<String, Object> zdsymap=(Map<String, Object>)listJd.get(i);
							String zdsyh=String.valueOf(zdsymap.get("LB_DM"));
							String zdsyhmc=String.valueOf(zdsymap.get("LB_MC"));
							//map.put("ZDSYH", zdsyh);
							List<Mode> list=new ArrayList<Mode>();
							list.add(new Mode("IN","String","NSRMC"));
							list.add(new Mode("IN","String","%"));
							list.add(new Mode("IN","String","201001"));//qsrq
							list.add(new Mode("IN","String","201906"));//jzrq
							list.add(new Mode("IN","String","0"));

							list.add(new Mode("IN","String","%"));
							list.add(new Mode("IN","String","%"));
							
							list.add(new Mode("IN","String","1"));
							
							list.add(new Mode("IN","String","NSRMC"));

							list.add(new Mode("IN","String","DESC"));
							
							
							list.add(new Mode("IN","String",tjkj));
							list.add(new Mode("IN","String",zdsyh));//ZDSYH
							
							
							
							list.add(new Mode("IN","String","1"));
							list.add(new Mode("IN","String","1000"));
							
							
							list.add(new Mode("OUT","RS",""));
							
							sjList= (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYQY_NEW1", list);
							
						}
					}

					Map<String, Object> map = new HashMap<String, Object>();
					String[] cols = { "所属街道", "纳税人名称", "增值税", "“营改增”增值税", "营业税", "企业所得税（国税）", "企业所得税（地税）", "企业所得税（合计）", "个人所得税", "房产税", " 印花税 ", "车船税", "城市维护建设税", "地方教育附加", "教育费附加", "合计"};
					String[] keys = { "JD_MC", "NSRMC", "ZZS", "YGZZZS", "YYS", "QYSDS_GS", "QYSDS_DS", "QYSDS", "GRSDS", "FCS", "YHS", "CCS", "CSWHJSS", "DFJYFJ", "JYFJ", "HJ"};
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
