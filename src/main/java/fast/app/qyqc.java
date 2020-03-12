package fast.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fast.main.util.Super;

public class qyqc extends Super {
	
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "qyqrqc/qyqc";
		} catch (Exception e) {
			e.printStackTrace();
			return "qyqrqc/qyqc";
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
			String jd = this.getRequest().getSession().getAttribute("dwid").toString();
			
			String nsrsbh = getValue(this.getForm().get("nsrsbh"));
			String nsrmc = getValue(this.getForm().get("nsrmc"));
			
			String querysql = "";
			
			if(StringUtils.isNotBlank(jd) && !jd.equals("00")) {
				querysql = "select s.*, j.jd_mc jdmc from fast_qyqc s, dm_jd j where s.ssjd = j.jd_dm and s.ssjd = '" + jd + "' ";
				if(StringUtils.isNotBlank(nsrsbh)) {
					querysql += " and s.nsrsbh = '" + nsrsbh + "' ";
				}
				if(StringUtils.isNotBlank(nsrmc)) {
					querysql += " and s.nsrmc like '%" + nsrmc + "%' ";
				}
				querysql +=  " order by s.id";
			} else {
				querysql = "select s.*, j.jd_mc jdmc from fast_qyqc s, dm_jd j where s.ssjd = j.jd_dm ";
				if(StringUtils.isNotBlank(nsrsbh)) {
					querysql += " and s.nsrsbh = '" + nsrsbh + "' ";
				}
				if(StringUtils.isNotBlank(nsrmc)) {
					querysql += " and s.nsrmc like '%" + nsrmc + "%' ";
				}
				querysql +=  " order by s.id";
			}
			System.out.println(querysql);
			List<Map<String, Object>> list = this.getBs().query(querysql);

			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	public String addQyqc(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String uno = this.getRequest().getSession().getAttribute("uno").toString();
			
			String nsrsbh = getValue(this.getForm().get("nsrsbh"));
			String nsrmc = getValue(this.getForm().get("nsrmc"));
			String ssjd = getValue(this.getForm().get("ssjd"));
			String zczb = getValue(this.getForm().get("zczb"));
			String xzcdz = getValue(this.getForm().get("xzcdz"));
			String dnljsk = getValue(this.getForm().get("dnljsk"));
			String qnljsk = getValue(this.getForm().get("qnljsk"));
			String nqrd = getValue(this.getForm().get("yqrd"));
			String qcyy = getValue(this.getForm().get("qryy"));
			String filename = getValue(this.getForm().get("filename"));
			String filesrc = getValue(this.getForm().get("filesrc"));
			
			String querySql = "select f.* from fast_qyqc f where f.NSRSBH = '" + nsrsbh + "' and f.NSRMC = '" + nsrmc + "'" 
					+ " and f.SSJD = '" + ssjd + "' and f.ZCZB = '" + zczb + "'"
					+ " and f.ZCDZ = '" + xzcdz + "' and f.NQRD = '" + nqrd + "'";
			System.out.println(querySql);
			List<Map<String, Object>> list = this.getBs().query(querySql);
			if(list.size() > 0) {
				return this.toJson("500", "新增失败，已有该迁出任务！");
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			
			String insertsql = "insert into fast_qyqc(ID,NSRSBH,NSRMC,SSJD,ZCZB,ZCDZ,DNLJSK,QNLJSK,NQRD,QCYY,FILENAME,FILEURL,FBRQ,FBZ)"
					+ " values(seq_fast_qyqrqc.nextval, '" + nsrsbh + "', '" + nsrmc + "', '" + ssjd + "', '" + zczb + "', '" + xzcdz
					+ "', '" + dnljsk + "', '" + qnljsk + "', '" + nqrd + "', '" + qcyy + "', '" + filename + "', '" + filesrc 
					+ "', to_date('" + formatter.format(date) + "','yyyy-MM-dd'), '" + uno + "')";
			System.out.println(insertsql);
			this.getBs().insert(insertsql);

			return this.toJson("000", "新增成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "新增异常！");
		}
	}
	
	public String checkQyqc(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String uno = this.getRequest().getSession().getAttribute("uno").toString();
			
			String id = getValue(this.getForm().get("id"));
			String shyj = getValue(this.getForm().get("shyj"));
			String shly = getValue(this.getForm().get("shly"));
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			
			String insertsql = "update fast_qyqc f set f.SHYJ = '" + shyj + "', f.LY = '" + shly + "', f.FKRQ = to_date('" 
					+ formatter.format(date) + "','yyyy-MM-dd'), f.FKZ = '" + uno + "', f.STATUS = '1' where id = '" + id + "'";
			System.out.println(insertsql);
			this.getBs().insert(insertsql);

			return this.toJson("000", "审核成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "审核异常！");
		}
	}
	
	public String removeQyqc(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			
			String id = getValue(this.getForm().get("id"));
			
			String deletesql = "delete from fast_qyqc f where f.id = '" + id + "'";
			System.out.println(deletesql);
			this.getBs().delete(deletesql);

			return this.toJson("000", "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "删除异常！");
		}
	}

}
