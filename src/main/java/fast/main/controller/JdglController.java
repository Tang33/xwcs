package fast.main.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.Super;

/**
 * 系统管理
 * 街道管理
 * @author Administrator
 *
 */

@Controller
@RequestMapping("jdgl")
public class JdglController  extends Super{

	@Autowired
	private BaseService bs;

	private Map<String, Object> user = null;	

	/**
	 * 街道查询
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/query.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String query(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String pageNo = getValue(form.get("pageNo"));
			String pageSize = getValue(form.get("pageSize"));
			String findText = getValue(form.get("findText"));
			String sql = "select * from dm_jd where jd_mc like'%"+findText+"%' or jd_dm ='%"+findText+"%'  order by jd_dm";
			List<Map<String, Object>> list = bs.query(sql, pageNo, pageSize);
			int count = bs.queryCount(sql);
			return this.toJson("000", "查询成功！", list,count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	@RequestMapping(value="/add.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String add(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String jddm = getValue(form.get("jddm")).toString();
			String jdmc = getValue(form.get("jdmc")).toString();
			String jdms = getValue(form.get("jdsm")).toString();
			String glfw = getValue(form.get("glfw")).toString();
			String yxbz = getValue(form.get("yxbz2")).toString();
			String sql = "insert into dm_jd(jd_dm,jd_mc,jd_sm,jd_glfw,xybz) values('"+jddm+"','"+jdmc+"','"+jdms+"','"+glfw+"','"+yxbz+"')";
			bs.insert(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		} 
	}
	
	/**
	 * 修改街道
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/update.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String update(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String id = getValue(form.get("idedit")).toUpperCase();
			String jddm = getValue(form.get("jddmedit")).toString();
			String jdmc = getValue(form.get("jdmcedit")).toString();
			String jdms = getValue(form.get("jdsmedit")).toString();
			String glfw = getValue(form.get("glfwedit")).toString();
			String yxbz = getValue(form.get("yxbz3")).toString();
			String sql = "Update dm_jd set jd_dm='"+jddm+"',jd_mc='" + jdmc + "',jd_sm='" + jdms + "',jd_glfw='" + glfw
					+ "',xybz='"+yxbz+"' where jd_dm='" + id + "'";
			this.bs.update(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	/**
	 * 删除街道
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/del.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String del(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> data) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String id = getValue(data.get("jddm")).toUpperCase();
			String sql = "delete from dm_jd where jd_dm='" + id + "'";
			this.bs.update(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
}
