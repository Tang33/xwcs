package fast.main.controller.BusinessMove;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import fast.main.service.BaseService;
import fast.main.util.Super;
/**
 * 企业迁移流程管理 流程管理
 *  本功能用于对系统的“企业迁出流程”进行操作管理！
 *
 */

@Controller
@RequestMapping("lcgl")
public class LcglController extends Super{
	@Autowired
	BaseService bs;
	
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "BusinessMove/lcgl";
		} catch (Exception e) {
			e.printStackTrace();
			return "BusinessMove/lcgl";
		}
	}
	
	/**
	 * 查询街道下拉选
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/lcgl_queryInit.do", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String queryInit(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			request.setCharacterEncoding("utf-8");
			response.setContentType("application/json;charset=UTF-8");
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String dwid = request.getSession().getAttribute("dwid").toString();
			String sql = "";
			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
			// 街道
			if("00".equals(dwid) || "01".equals(dwid)) {
				sql = "select * from dm_jd  where xYbz = '1'";//in (91,92,93,94,95,96,97,98,99)";
				List<Map<String, Object>> jdlist = bs.query(sql);
				map.put("jdlist", jdlist);
			} else {
				sql = "select * from dm_jd where jd_dm = '"+dwid+"'";
				List<Map<String, Object>> jdlist = bs.query(sql);
				map.put("jdlist", jdlist);
			}
			String json = toJson("000", "查询成功！", map);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return toJson("009", "查询异常！");
		}
	}
	
	// 查询
	@RequestMapping("lcgl_doQuery.do")
	@ResponseBody
	public String doQuery(HttpServletRequest request) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String dwid = request.getSession().getAttribute("dwid").toString();
			String yearNmonth = getValue(request.getParameter("yearNmonth"));
			String jd=getValue(request.getParameter("jdlist"));
			String nsrmc=getValue(request.getParameter("paramNsrmc"));
			String nsrsbh=getValue(request.getParameter("paramNsrsbh"));
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
			List<Map<String, Object>> list = bs.query(sql);
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			//e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	// 监听查询
	@RequestMapping("lcgl_doQueryByID.do")
	@ResponseBody
	public String doQueryByID(HttpServletRequest request) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String id = getValue(request.getParameter("ID"));
			String status = getValue(request.getParameter("STATUS"));
			String sql = "";
			if ("0".equals(status)) {
				sql = "select FBRQ,NSRMC,NQRD from fast_qyqc where id = '"+id+"'";
			} else if ("1".equals(status) || "2".equals(status)) {
				sql = "select FBRQ,NSRMC,NQRD from fast_qyqc where id = '"+id+"' UNION ALL select FKRQ,NSRMC,NQRD from fast_qyqc where id = '"+id+"'";
			} else {
				sql = "select FBRQ,NSRMC,NQRD from fast_qyqc where id = '"+id+"' UNION ALL select FKRQ ,NSRMC,NQRD from fast_qyqc where id = '"+id+"' UNION ALL select ZSRQ,NSRMC,NQRD from fast_qyqc where id = '"+id+"'";
			}
			
			System.out.println(sql);
			
			List<Map<String, Object>> list = bs.query(sql);
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	// 通过审核
	@RequestMapping("lcgl_doAgree.do")
	@ResponseBody
	public String doAgree(HttpServletRequest request) {
		try {
			String dwid = request.getSession().getAttribute("dwid").toString();
			String id = getValue(request.getParameter("id"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String LY = getValue(request.getParameter("ly"));
			Date date = new Date();		
			String sql = "update FAST_QYQC set STATUS = '1' ,FKZ = '"+dwid+"' ,FKRQ = to_date('" + formatter.format(date) + "','yyyy-mm-dd HH24:mi:ss'),ly = '"+LY+"' where id = '"+id+"'";
			bs.update(sql);
			return this.toJson("000", "审核成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "审核失败！");
		}
	}
	
	// 拒绝审核
	@RequestMapping("lcgl_doJJ.do")
	@ResponseBody
	public String doJJ(HttpServletRequest request) {
		try {
			
			String dwid = request.getSession().getAttribute("dwid").toString();
			String id = getValue(request.getParameter("id"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();	
			String LY = getValue(request.getParameter("ly"));
			String sql = "update FAST_QYQC set STATUS = '2' ,FKZ = '"+dwid+"' ,FKRQ = to_date('" + formatter.format(date) + "','yyyy-mm-dd HH24:mi:ss') ,ly = '"+LY+"' where id = '"+id+"'";
			bs.update(sql);
			return this.toJson("000", "审核成功！");
		} catch (Exception e) {
			//e.printStackTrace();
			return this.toJson("009", "审核失败！");
		}
	}
}
