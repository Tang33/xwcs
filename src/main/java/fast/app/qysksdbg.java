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
 * 企业税款属地变更查询
 * @author Administrator
 *
 */
public class qysksdbg extends Super {
	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "zdsSjcx/qysksdbg";
		}catch(Exception e){
			e.printStackTrace();
			return "zdsSjcx/qysksdbg";
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
	public String querySdbg(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			
			
			String cxrq = getValue(this.getForm().get("cxrq"));
			String jd_dm = getValue(this.getForm().get("jd_dm"));
			if("".equals(jd_dm) || jd_dm ==null) {
				jd_dm="%";
			}
			String nsrmc = getValue(this.getForm().get("qymc"));
			String zsxm_dm = getValue(this.getForm().get("zsxm_dm"));
			if("".equals(zsxm_dm) || zsxm_dm ==null) {
				zsxm_dm="%";
			}
			
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
			
			List<Mode> list=new ArrayList<Mode>();
			list.add(new Mode("IN","String",nsrmc));
			list.add(new Mode("IN","String",qsrq));
			list.add(new Mode("IN","String",jzrq));
			list.add(new Mode("IN","String",zsxm_dm));
			list.add(new Mode("IN","String",jd_dm));
			list.add(new Mode("OUT","RS",""));
			
			List<Map<String, Object>> sjList= (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYBG", list);
			
			return this.toJson("000", "查询成功！",sjList);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	
	//导出excel
		public Object export(Map<String, Object> rmap){
			try{
				//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
				initMap(rmap);
				String jd_dm = getValue(this.getForm().get("jd_dm"));
				if("".equals(jd_dm) || jd_dm ==null) {
					jd_dm="%";
				}
				String cxrq = getValue(this.getForm().get("cxrq"));

				String nsrmc = getValue(this.getForm().get("qymc"));
				String zsxm_dm = getValue(this.getForm().get("zsxm_dm"));
				if("".equals(zsxm_dm) || zsxm_dm ==null) {
					zsxm_dm="%";
				}
				
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
				
				List<Mode> list=new ArrayList<Mode>();
				list.add(new Mode("IN","String",nsrmc));
				list.add(new Mode("IN","String",qsrq));
				list.add(new Mode("IN","String",jzrq));
				list.add(new Mode("IN","String",zsxm_dm));
				list.add(new Mode("IN","String",jd_dm));
				list.add(new Mode("OUT","RS",""));
				
				List<Map<String, Object>> sjList= (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYBG", list);

				
				Map<String, Object> map=new HashMap<String, Object>();
				String[] cols= {"是否变更","起始月份","截至月份","企业名称","税种","所属行业","街道","总税源","税款来源","变更类型"};
				String[] keys= {"SFBG","QSRQ","JZRQ","NSRMC","SZ_MC","JD_MC","ZSE","GDS","BGLX"};
				map.put("fileName", "导出excel.xlsx");
				map.put("cols", cols);
				map.put("keys", keys);
				map.put("list", sjList);
				return map;
			}catch(Exception e){
				e.printStackTrace();
				return this.toJson("009", "查询异常！");
			}
		}
	
	
}
