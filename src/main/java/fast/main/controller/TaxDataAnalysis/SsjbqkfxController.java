package fast.main.controller.TaxDataAnalysis;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.Super;

/**
 * 税收基本情况分析
 *
 */
@Controller
@RequestMapping(value="Symbcx",produces = "text/plain;charset=utf-8")
public class SsjbqkfxController extends Super {
	@Autowired
	BaseService bs;
	
	/**
	 * 进入税收基本情况分析跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		
		return "TaxDataAnalysis/Ssjbqkfx";
		
	}
	
	/**
	 * 根据下拉列表选择进行查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("Symbcx_getdata.do")
	@ResponseBody
	public String getdata(HttpServletRequest request, HttpServletResponse response) {

		String jd_dm = getValue(request.getSession().getAttribute("dwid"));
		String mbmc = getValue(request.getParameter("mbmc"));		//模板名称
		String page = getValue(request.getParameter("page"));
		String pagesize = getValue(request.getParameter("limit"));
		String ybggys_type = "";
		String jd = "";
		pagesize = Integer.toString(Integer.parseInt(pagesize) - 1);
		if (jd_dm.equals("00")) {
			jd="%";
		}else {
			jd=jd_dm;
		}
		String sql = "";
		if (mbmc.equals("FAST_YLJ")) {
			sql = "select yf 月份,to_char(dyljje/10000,'fm9999999990.0099') 当月累计金额,to_char(tqljje/10000,'fm9999999990.0099') 同期累计金额,to_char(zf/10000,'fm9999999990.0099') 增幅,(case when zzbl is not null then to_char(zzbl*100,'fm9999999990.0099')||'%' else null end) 增长比例 from FAST_YLJ where JD_DM="
					+ jd_dm;
		} else if (mbmc.equals("FAST_NSZZB")) {
			sql = "select zsxm_mc 征收项目,to_char(dnljje/10000,'fm9999999990.0099') 当年累计金额,to_char(tqljje/10000,'fm9999999990.0099') 同期累计金额,to_char(zf/10000,'fm9999999990.0099') 增幅,(case when zzbl is not null then to_char(zzbl*100,'fm9999999990.0099')||'%' else null end) 增长比例,(case when zb is not null then to_char(zb*100,'fm9999999990.0099')||'%' else null end) 占比 from FAST_NSZZB WHERE JD_DM='"
					+ jd_dm + "' order by dnljje desc";
		} else if (mbmc.equals("FAST_NHYZB")) {
			sql = "select hymc 行业名称,to_char(dnljje/10000,'fm9999999990.0099') 当年累计金额,to_char(tqljje/10000,'fm9999999990.0099') 同期累计金额,to_char(zf/10000,'fm9999999990.0099') 增幅,(case when zzbl is not null then to_char(zzbl*100,'fm9999999990.0099')||'%' else null end) 增长比例,(case when zb is not null then to_char(zb*100,'fm9999999990.0099')||'%' else null end) 占比 from FAST_NHYZB WHERE  JD_DM='"
					+ jd_dm + "'  order by dnljje desc";
		} else if (mbmc.matches(".*?_[0-2]{1}$")) {
			ybggys_type = mbmc.substring(mbmc.length() - 1, mbmc.length());
			mbmc = mbmc.replaceAll("_[0-9]{1}$", "");
			if (ybggys_type.equals("0")) {
				sql = "select nsrmc 纳税人名称,to_char(dnljje/10000,'fm9999999990.0099') 当年累计金额,to_char(tqljje/10000,'fm9999999990.0099') 同期累计金额,to_char(zf/10000,'fm9999999990.0099') 增幅,(case when zzbl is not null then to_char(zzbl*100,'fm9999999990.0099')||'%' else null end) 增长比例  from "
						+ mbmc + " where bs = '" + ybggys_type + "' AND JD_DM like '"+jd+"%' order by dnljje desc";
			} else if (ybggys_type.equals("1")) {
				sql = "select nsrmc 纳税人名称,to_char(dnljje/10000,'fm9999999990.0099') 当年累计金额,to_char(tqljje/10000,'fm9999999990.0099') 同期累计金额,to_char(zf/10000,'fm9999999990.0099') 增幅,(case when zzbl is not null then to_char(zzbl*100,'fm9999999990.0099')||'%' else null end) 增长比例  from "
						+ mbmc + " where bs = '" + ybggys_type + "' AND JD_DM like '"+jd+"%' order by zf desc";
			} else {
				sql = "select nsrmc 纳税人名称,to_char(dnljje/10000,'fm9999999990.0099') 当年累计金额,to_char(tqljje/10000,'fm9999999990.0099') 同期累计金额,to_char(zf/10000,'fm9999999990.0099') 增幅,(case when zzbl is not null then to_char(zzbl*100,'fm9999999990.0099')||'%' else null end) 增长比例  from "
						+ mbmc + " where bs = '" + ybggys_type + "' AND JD_DM like '"+jd+"%' order by zf asc";
			}
		} else if (mbmc.equals("")) {
			return this.toJson("000", "查询成功", null, 0);
		}

		// 获取count
		List<Map<String, Object>> jglistct = bs.query(sql);
		if (mbmc.equals("FAST_YLJ")) {
			sql = "select * from (select row_.*, rownum rowno from (" + sql + ") row_ where rownum <= " + pagesize + "*"
					+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize
					+ " + 1 union all select '合计' 月份,to_char(SUM(dyljje)/10000,'fm9999999990.0099') 当月累计金额,to_char(SUM(tqljje)/10000,'fm9999999990.0099') 同期累计金额, null 增幅, null 增长比例, 10 rowno from FAST_YLJ where jd_dm="
					+ jd_dm;

		} else if (mbmc.equals("FAST_NSZZB")) {
			sql = "select * from (select row_.*, rownum rowno from (" + sql + ") row_ where rownum <= " + pagesize + "*"
					+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize
					+ " + 1 union all select '合计' 征收项目,to_char(SUM(dnljje)/10000,'fm9999999990.0099') 当年累计金额,to_char(SUM(tqljje)/10000,'fm9999999990.0099') 同期累计金额, null 增幅, null 增长比例,null 占比, 10 rowno from FAST_NSZZB where jd_dm='"
					+ jd_dm + "'";

		} else if (mbmc.equals("FAST_NHYZB")) {
			sql = "select * from (select row_.*, rownum rowno from (" + sql + ") row_ where rownum <= " + pagesize + "*"
					+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize
					+ " + 1 union all select '合计' 行业名称,to_char(SUM(dnljje)/10000,'fm9999999990.0099') 当年累计金额,to_char(SUM(tqljje)/10000,'fm9999999990.0099') 同期累计金额, null 增幅, null 增长比例,null 占比, 10 rowno from FAST_NHYZB where jd_dm='"
					+ jd_dm + "'";

		} else {
			sql = "select * from (select row_.*, rownum rowno from (" + sql + ") row_ where rownum <= " + pagesize + "*"
					+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize
					+ " + 1 union all select '合计' 纳税人名称,to_char(SUM(dnljje)/10000,'fm9999999990.0099') 当年累计金额,to_char(SUM(tqljje)/10000,'fm9999999990.0099') 同期累计金额, null 增幅, null 增长比例, 10 rowno from "
					+ mbmc + " where JD_DM like '"+jd+"%' and bs=" + ybggys_type; //+ " and jd_dm=" + jd_dm;
		}

		List<Map<String, Object>> jglist = bs.query(sql);
		return this.toJson("000", "查询成功", jglist, jglistct.size());

	}
	
	/**
	 * 根据下拉列表选择进行匹配显示的字段
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("Symbcx_getheader.do")
	@ResponseBody
	public String getheader(HttpServletRequest request, HttpServletResponse response) {


		String mbmc = getValue(request.getParameter("mbmc"));			//模板名称

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (mbmc.equals("FAST_YLJ")) {
			String[] arr = { "月份", "当月累计金额", "同期累计金额", "增幅", "增长比例" };
			for (int i = 0; i < arr.length; i++) {
				Map<String, String> map = new LinkedHashMap<>();
				map.put("field", arr[i]);
				map.put("title", arr[i]);
				map.put("align", "left");
				list.add(map);

			}

		} else if (mbmc.equals("FAST_NSZZB")) {
			String[] arr = { "征收项目", "当年累计金额", "同期累计金额", "增幅", "增长比例", "占比" };
			for (int i = 0; i < arr.length; i++) {
				Map<String, String> map = new LinkedHashMap<>();
				map.put("field", arr[i]);
				map.put("title", arr[i]);

				if ("征收项目".equals(arr[i])) {
					map.put("align", "left");
				} else {
					map.put("align", "right");
				}

				list.add(map);
			}

		} else if (mbmc.equals("FAST_NHYZB")) {

			String[] arr = { "行业名称", "当年累计金额", "同期累计金额", "增幅", "增长比例", "占比" };
			for (int i = 0; i < arr.length; i++) {
				Map<String, String> map = new LinkedHashMap<>();
				map.put("field", arr[i]);
				map.put("title", arr[i]);

				if ("行业名称".equals(arr[i])) {
					map.put("align", "left");
				} else {
					map.put("align", "right");
				}

				list.add(map);

			}

		} else {

			String[] arr = { "纳税人名称", "当年累计金额", "同期累计金额", "增幅", "增长比例" };
			for (int i = 0; i < arr.length; i++) {
				Map<String, String> map = new LinkedHashMap<>();
				map.put("field", arr[i]);
				map.put("title", arr[i]);
				if ("纳税人名称".equals(arr[i])) {
					map.put("align", "left");
				} else {
					map.put("align", "right");
				}

				list.add(map);

			}

		}

		return this.toJson("000", "查询成功", list);

	}
}
