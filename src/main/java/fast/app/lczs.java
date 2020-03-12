package fast.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fast.main.util.Super;

public class lczs extends Super {
	
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "qrqcgl/lczs";
		} catch (Exception e) {
			e.printStackTrace();
			return "qrqcgl/lczs";
		}
	}
	
	/**
	 * 初始化查询数据
	 * 
	 * @param rmap
	 * @return
	 */
	public String queryInit(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");
			
			String sql = this.getSql("jd_query");
			List<Map<String, Object>> jdlist = this.getBs().query(sql);
			sql = this.getSql("hy_query");
			List<Map<String, Object>> hylist = this.getBs().query(sql);

			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
			map.put("hylist", hylist);
			map.put("jdlist", jdlist);

			return this.toJson("000", "查询成功！", map);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	/**
	 * 查询企业名称
	 * 
	 * @param rmap
	 * @return
	 */
	public String queryQymc(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String qymc = getValue(this.getForm().get("qymc"));
			String querysql = "select distinct nsrmc from sb_nsrxx s  " + "where s.nsrmc like '%" + qymc + "%' ";
			System.out.println(querysql);
			List<Map<String, Object>> list = this.getBs().query(querysql);

			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	public String queryQyqc(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String yearNmonth = getValue(this.getForm().get("yearNmonth"));
			String jd=getValue(this.getForm().get("ssjd"));
			String nsrsbh = getValue(this.getForm().get("nsrsbh"));
			String nsrmc = getValue(this.getForm().get("nsrmc"));
			//处理时间
			String starTime = "";
			String endTime = "";
			String[] star = yearNmonth.split(" - ");
			if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
				starTime = star[0];
				endTime = star[1];
				starTime = starTime.substring(0, 4) + starTime.substring(5, 7);
				endTime = endTime.substring(0, 4) + endTime.substring(5, 7);
			}
			String querysql = "select s.*, j.jd_mc jdmc from fast_qyqc s, dm_jd j where s.ssjd = j.jd_dm and s.status in (1,4,5) ";
			if("0".equals(jd)) {
				if(StringUtils.isNotBlank(nsrsbh)) {
					querysql += " and s.nsrsbh = '" + nsrsbh + "' ";
				}
				if(StringUtils.isNotBlank(nsrmc)) {
					querysql += " and s.nsrmc like '%" + nsrmc + "%' ";
				}
				if(StringUtils.isNotBlank(yearNmonth)) {
					querysql += " and to_char(s.fbrq,'yyyymm') >='"+starTime+"' and to_char(s.fbrq,'yyyymm') <= '"+endTime+"' ";
				}
			} else {
				if(StringUtils.isNotBlank(nsrsbh)) {
					querysql += " and s.nsrsbh = '" + nsrsbh + "' ";
				}
				if(StringUtils.isNotBlank(nsrmc)) {
					querysql += " and s.nsrmc like '%" + nsrmc + "%' ";
				}
				if(StringUtils.isNotBlank(yearNmonth)) {
					querysql += " and to_char(s.fbrq,'yyyymm') >='"+starTime+"' and to_char(s.fbrq,'yyyymm') <= '"+endTime+"' ";
				}
				if(StringUtils.isNotBlank(jd)) {
					querysql += " and s.ssjd = '" + jd + "' ";
				}	
			}			
			querysql +=  " order by s.status,s.fbrq desc";
			System.out.println(querysql);
			List<Map<String, Object>> list = this.getBs().query(querysql);
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	// 监听查询
	public String doQueryByID(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String id = getValue(this.getForm().get("ID"));
			String sql = "SELECT A.*,B.JD_MC from FAST_QYQC A,DM_JD B WHERE A.SSJD = B.JD_DM AND A.ID = '"+id+"'";
			List<Map<String, Object>> list = this.getBs().query(sql);
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			//e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	

	
	//终审saveQyqczs
	public String saveQyqczs(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String id = getValue(this.getForm().get("id"));
			String LY = getValue(this.getForm().get("ly"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();	
			String updatesql = "update FAST_QYQC set STATUS = '4' ,ZSRQ = to_date('" + formatter.format(date) + "','yyyy-mm-dd HH24:mi:ss'),SHYJ = '"+LY+"' where id = '"+id+"'";
			this.getBs().update(updatesql);
			return this.toJson("000", "终审成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "终审异常！");
		}
	}
	
	
	public String removeQyqc(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);	
			String id = getValue(this.getForm().get("id"));	
			String LY = getValue(this.getForm().get("ly"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();		
			String updatesql = "update FAST_QYQC set STATUS = '5' ,ZSRQ = to_date('" + formatter.format(date) + "','yyyy-mm-dd HH24:mi:ss'),SHYJ = '"+LY+"' where id = '"+id+"'";
			this.getBs().update(updatesql);
			return this.toJson("000", "终止成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "终止异常！");
		}
	}

}
