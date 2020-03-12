package fast.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fast.main.util.Super;

public class lcgl extends Super{

	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String ssdw_dm=this.getRequest().getSession().getAttribute("uno").toString();
			this.getRequest().setAttribute("uno", ssdw_dm);
			return "qrqcgl/lcgl";
		}catch(Exception e){
			e.printStackTrace();
			return "qrqcgl/lcgl";
		}
	}
	
	public String queryInit(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String dwid = this.getRequest().getSession().getAttribute("dwid").toString();
			String sql = "";
			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
			// 街道
			if("00".equals(dwid) || "01".equals(dwid)) {
				sql = "select * from dm_jd where jd_dm in (91,92,93,94,95,96,97,98,99)";
				List<Map<String, Object>> jdlist = this.getBs().query(sql);
				map.put("jdlist", jdlist);
			} else {
				sql = "select * from dm_jd where jd_dm = '"+dwid+"'";
				List<Map<String, Object>> jdlist = this.getBs().query(sql);
				map.put("jdlist", jdlist);
			}

			return this.toJson("000", "查询成功！", map);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	// 查询
	public String doQuery(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String dwid = this.getRequest().getSession().getAttribute("dwid").toString();
			String yearNmonth = getValue(this.getForm().get("yearNmonth"));
			String jd=getValue(this.getForm().get("jdlist"));
			String nsrmc=getValue(this.getForm().get("paramNsrmc"));
			String nsrsbh=getValue(this.getForm().get("paramNsrsbh"));
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
			String sql = "select s.*,j.jd_mc jdmc from fast_qyqc s, dm_jd j where s.ssjd = j.jd_dm and s.status in (0,1,2) ";
			if("00".equals(dwid) || "01".equals(dwid)) {
				if("0".equals(jd)) {
					if(StringUtils.isNotBlank(nsrsbh)) {
						sql += " and s.nsrsbh = '" + nsrsbh + "' ";
					}
					if(StringUtils.isNotBlank(nsrmc)) {
						sql += " and s.nsrmc like '%" + nsrmc + "%' ";
					}
					if(StringUtils.isNotBlank(yearNmonth)) {
						sql += " and to_char(s.fbrq,'yyyymm') >='"+starTime+"' and to_char(s.fbrq,'yyyymm') <= '"+endTime+"' ";
					}
				} else {
					if(StringUtils.isNotBlank(nsrsbh)) {
						sql += " and s.nsrsbh = '" + nsrsbh + "' ";
					}
					if(StringUtils.isNotBlank(nsrmc)) {
						sql += " and s.nsrmc like '%" + nsrmc + "%' ";
					}
					if(StringUtils.isNotBlank(jd)) {
						sql += " and s.ssjd = '" + jd + "' ";
					}
					if(StringUtils.isNotBlank(yearNmonth)) {
						sql += " and to_char(s.fbrq,'yyyymm') >='"+starTime+"' and to_char(s.fbrq,'yyyymm') <= '"+endTime+"' ";
					}
				}
			} else {
				sql +=  " AND S.SSJD = '"+dwid+"'";
				if("0".equals(jd)) {
					if(StringUtils.isNotBlank(nsrsbh)) {
						sql += " and s.nsrsbh = '" + nsrsbh + "' ";
					}
					if(StringUtils.isNotBlank(nsrmc)) {
						sql += " and s.nsrmc like '%" + nsrmc + "%' ";
					}
					if(StringUtils.isNotBlank(yearNmonth)) {
						sql += " and to_char(s.fbrq,'yyyymm') >='"+starTime+"' and to_char(s.fbrq,'yyyymm') <= '"+endTime+"' ";
					}
				} else {
					if(StringUtils.isNotBlank(nsrsbh)) {
						sql += " and s.nsrsbh = '" + nsrsbh + "' ";
					}
					if(StringUtils.isNotBlank(nsrmc)) {
						sql += " and s.nsrmc like '%" + nsrmc + "%' ";
					}
					if(StringUtils.isNotBlank(jd)) {
						sql += " and s.ssjd = '" + jd + "' ";
					}
					if(StringUtils.isNotBlank(yearNmonth)) {
						sql += " and to_char(s.fbrq,'yyyymm') >='"+starTime+"' and to_char(s.fbrq,'yyyymm') <= '"+endTime+"' ";
					}	
				}
			}
			sql +=  " order by s.status,s.fbrq desc";
			System.out.println(sql);
			List<Map<String, Object>> list = this.getBs().query(sql);
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			//e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	// 监听查询
	public String doQueryByID(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String id = getValue(this.getForm().get("ID"));
			String status = getValue(this.getForm().get("STATUS"));
			String sql = "";
			if ("0".equals(status)) {
				sql = "select FBRQ from fast_qyqc where id = '"+id+"'";
			} else if ("1".equals(status) || "2".equals(status)) {
				sql = "select FBRQ from fast_qyqc where id = '"+id+"' UNION ALL select FKRQ from fast_qyqc where id = '"+id+"'";
			} else {
				sql = "select FBRQ from fast_qyqc where id = '"+id+"' UNION ALL select FKRQ from fast_qyqc where id = '"+id+"' UNION ALL select ZSRQ from fast_qyqc where id = '"+id+"'";
			}
			
			System.out.println(sql);
			
			List<Map<String, Object>> list = this.getBs().query(sql);
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			//e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	// 通过审核
	public String doAgree(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String dwid = this.getRequest().getSession().getAttribute("dwid").toString();
			String id = getValue(this.getForm().get("id"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String LY = getValue(this.getForm().get("ly"));
			Date date = new Date();		
			String sql = "update FAST_QYQC set STATUS = '1' ,FKZ = '"+dwid+"' ,FKRQ = to_date('" + formatter.format(date) + "','yyyy-mm-dd HH24:mi:ss'),ly = '"+LY+"' where id = '"+id+"'";
			this.getBs().update(sql);
			return this.toJson("000", "审核成功！");
		} catch (Exception e) {
			//e.printStackTrace();
			return this.toJson("009", "审核失败！");
		}
	}
	
	// 拒绝审核
	public String doJJ(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String dwid = this.getRequest().getSession().getAttribute("dwid").toString();
			String id = getValue(this.getForm().get("id"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();	
			String LY = getValue(this.getForm().get("ly"));
			String sql = "update FAST_QYQC set STATUS = '2' ,FKZ = '"+dwid+"' ,FKRQ = to_date('" + formatter.format(date) + "','yyyy-mm-dd HH24:mi:ss') ,ly = '"+LY+"' where id = '"+id+"'";
			this.getBs().update(sql);
			return this.toJson("000", "审核成功！");
		} catch (Exception e) {
			//e.printStackTrace();
			return this.toJson("009", "审核失败！");
		}
	}
}
