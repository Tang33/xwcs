package fast.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.Super;

public class jdgl extends Super {
	private Map<String, Object> user = null;

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");

			return "xtgl/jdgl";
		} catch (Exception e) {
			e.printStackTrace();
			return "xtgl/jdgl";
		}
	}

	public String query(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String pageNo = getValue(this.getForm().get("pageNo"));
			String pageSize = getValue(this.getForm().get("pageSize"));
			String findText = getValue(this.getForm().get("findText"));
			String sql = "select * from dm_jd where jd_mc like'%"+findText+"%' or jd_dm ='%"+findText+"%'  order by jd_dm";
			List<Map<String, Object>> list = this.getBs().query(sql, pageNo, pageSize);
			int count = this.getBs().queryCount(sql);
			return this.toJson("000", "查询成功！", list,count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	
	public String del(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String id = getValue(this.getForm().get("id")).toUpperCase();
			String sql = "delete from dm_jd where jd_dm='" + id + "'";
			this.getBs().update(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	

	public String update(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String id = getValue(this.getForm().get("idedit")).toUpperCase();
			String jddm = getValue(this.getForm().get("jddmedit")).toString();
			String jdmc = getValue(this.getForm().get("jdmcedit")).toString();
			String jdms = getValue(this.getForm().get("jdmsedit")).toString();
			String glfw = getValue(this.getForm().get("glfwedit")).toString();
			String yxbz = getValue(this.getForm().get("yxbz3")).toString();
			String sql = "Update dm_jd set jd_dm='"+jddm+"',jd_mc='" + jdmc + "',jd_sm='" + jdms + "',jd_glfw='" + glfw
					+ "',xybz='"+yxbz+"' where jd_dm='" + id + "'";
			this.getBs().update(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String add(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String jddm = getValue(this.getForm().get("jddm")).toString();
			String jdmc = getValue(this.getForm().get("jdmc")).toString();
			String jdms = getValue(this.getForm().get("jdms")).toString();
			String glfw = getValue(this.getForm().get("glfw")).toString();
			String yxbz = getValue(this.getForm().get("yxbz2")).toString();
			String sql = "insert into dm_jd(jd_dm,jd_mc,jd_sm,jd_glfw,xybz) values('"+jddm+"','"+jdmc+"','"+jdms+"','"+glfw+"','"+yxbz+"')";
			this.getBs().insert(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String GetNowDate() {
		String temp_str = "";
		Date dt = new Date();
		// 最后的aa表示“上午”或“下午” HH表示24小时制 如果换成hh表示12小时制
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
		temp_str = sdf.format(dt);
		return temp_str;
	}

}
