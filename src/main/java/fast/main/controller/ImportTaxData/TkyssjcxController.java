package fast.main.controller.ImportTaxData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;

import fast.main.service.BaseService;
import fast.main.util.Super;

@Controller
@RequestMapping("tk")
public class TkyssjcxController extends Super {
	
	@Autowired
	BaseService bs;
	private Map<String, Object> user = null;
	
	public String findJd(HttpServletRequest request, HttpServletResponse response) {
		String andJd = "";
		try {
			user = (Map<String, Object>)request.getSession().getAttribute("user");
			Integer jd = Integer.parseInt((String)user.get("DWID"));
			System.out.println(jd);
			
			if (user == null) {
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
			if (jd != null && jd == 00) {
				andJd = "and 1 = 1";
			}else {
				andJd = "and jd_dm = "+jd;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return andJd;
	}
	
	/**
	 * 退库原始数据表
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/tkcx.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String tkCx(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		try {
			String jd = this.findJd(request, response);

			String page = getValue(form.get("page")); 		//第几页
			String pagesize = getValue(form.get("limit"));		//每页显示几行
			//获取页面传递过来的input中的值
			String fz = getValue(form.get("fz"));

			String rk_rq2 = getValue(form.get("drrq"));

			String arr[] = rk_rq2.split("-");
			String rk_rq = arr[0] + arr[1];
			//获取表单中的数据
			String form1 = getValue(form.get("form"));

			String sqlcs="";

			JSONArray form2  =JSONArray.parseArray(form1);
			//下标从一开始取值
			if (form2.size() > 1) {
				for (int i = 1; i < form2.size(); i++) {
					JSONObject obj = (JSONObject) JSONObject.toJSON(form2.get(i));
					sqlcs += " " + getValue(obj.get("value"));
				}
			}

			//List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
			String sql = "select * from xwcs_gsdr_yssjtk where 1=1 and (-rk_rq=?-)"+jd;
			sql+=sqlcs;


//			if (!fz.equals("")) {
//
//				if (fz.equals("yzsje")) {
//					sql = "select t."+fz+" from xwcs_gsdr_yssjtk t where 1=1 and (-rk_rq=?-)"+jd;
//
//				} else {
//					sql = "select t."+fz+",sum(t.yzsje) yzsje from xwcs_gsdr_yssjtk t where 1=1 and (-rk_rq=?-)"+jd;
//				}
//
//				sql+=sqlcs;
//
//				sql+=" group by t."+fz+" ";
//				System.out.println(sql);
//			}
			
			if (!fz.equals("")) {
				if (fz.equals("se")) {
					sql = "select t." + fz + ",sum(t.dfbl) dfbl  from xwcs_gsdr_yssjtk t where 1=1"+jd;
				} else {
					sql = "select t." + fz + ",sum(t.se) se,sum(t.dfbl) dfbl,sum(t.qxj) qxj  from xwcs_gsdr_yssjtk t where 1=1"+jd;
				}

				sql += sqlcs;

				if (!rk_rq.equals("")) {
					sql += " and (-rk_rq=?-) ";
				}
				sql += " group by t." + fz + " ";
			}
			
			sql = this.getSql2(sql, rk_rq);
			
			List<Map<String, Object>> sjjgall = bs.query(sql,page,pagesize);
			
			//查询count
			int count =0;
			String sqlcount = "select 1  from xwcs_gsdr_yssjtk a where 1=1" + sqlcs+"and (-rk_rq=?-)"+jd;
			sqlcount = this.getSql2(sqlcount,rk_rq);
			count = bs.queryCount(sqlcount);
			if (!fz.equals("")) {
				sqlcount +=" group by a."+fz+" ";
				count = bs.queryCount(sqlcount);
			}
			System.out.println(count);

			return this.toJson("000", "查询成功！", sjjgall, count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}		
		
	}
	
	/**
	 * 获取下拉列表框数据
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/selectList.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String selectList(HttpServletRequest request , HttpServletResponse response,@RequestParam Map<String, Object> rmap) {
		try {
			String jd = this.findJd(request, response);
//			"+jd+"
			String sql = "select MBM,ZDM FROM  FAST_YSSJCX_MB where dycxb = 'xwcs_gsdr_yssjtk' group by MBM,ZDM";
			List<Map<String, Object>> list = bs.query(sql);
			return this.toJson("000", "查询成功！", list);

		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	/**
	 * 汇总条件
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/queryfz.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String queryfz(HttpServletRequest request,HttpServletResponse response, Map<String, Object> rmap) {
		try {
//			String jd = this.findJd(request, response);
			String sql = "SELECT DISTINCT mbm,zdm FROM FAST_YSSJCX_MB where dycxb = 'xwcs_gsdr_yssjtk' ";
			List<Map<String, Object>> list = bs.query(sql);
			return this.toJson("000", "查询成功", list);
		} catch (Exception e) {
			return this.toJson("009", "查询异常");
		}
	}

	@RequestMapping(value="/queryzd.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String queryzd(HttpServletRequest request , HttpServletResponse response,@RequestParam Map<String, Object> rmap) {
		try {
			String jd = this.findJd(request, response);
			String xz = getValue(rmap.get("xz"));
			SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -1);
			Date m = c.getTime();
			String rkrq = format.format(m);

			String sql = "select " + xz + " XMMC from xwcs_gsdr_yssjtk where 1=1 "+jd+" group by " + xz;
			List<Map<String, Object>> sjjgall = bs.query(sql);
			List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
			Map<String, String> map;
			for (int i = 0; i < sjjgall.size(); i++) {
				if (sjjgall.get(i) == null) {
					sjjgall.remove(i);
					i--;
				} else {
					map = new HashMap<String, String>();
					for (String key : sjjgall.get(i).keySet()) {
						map.put(key.toLowerCase(), sjjgall.get(i).get(key).toString());
					}

					list1.add(map);
				}
			}

			return this.toJson("000", "查询成功！", list1);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	@RequestMapping(value="/selectislike.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String selectislike(HttpServletRequest request , HttpServletResponse response,@RequestParam Map<String, Object> rmap) {
		try {
//			String jd = this.findJd(request, response);
			String zdm = getValue(rmap.get("xz"));
			String sql = "select * FROM FAST_YSSJCX_MB where ZDM = '" + zdm + "' and dycxb = 'xwcs_gsdr_yssjtk'";
			System.out.println("selectislike:" + sql);
			List<Map<String, Object>> list = bs.query(sql);

			return this.toJson("000", "查询成功", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常");
		}
	}

	@RequestMapping(value="/querySdbg.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String querySdbg(HttpServletRequest request , HttpServletResponse response,@RequestParam Map<String, Object> rmap) {
		try {
			String jd = this.findJd(request, response);
			String page = getValue(rmap.get("page"));
			String pagesize = getValue(rmap.get("limit"));
				// 获取页面传递过来的input中的值
			String fz = getValue(rmap.get("fz"));
				// 获取表单中的数据
			String form = getValue(rmap.get("form"));
			String rk_rq2 = getValue(rmap.get("drrq"));
			String arr[] = rk_rq2.split("-");
			String rk_rq = arr[0] + arr[1];

			String sqlcs = "";

			JSONArray form1 = JSONArray.parseArray(form);
				//下标从一开始取值
			if (form1.size() > 1) {
				for (int i = 1; i < form1.size(); i++) {
						JSONObject obj = (JSONObject) JSONObject.toJSON(form1.get(i));
						sqlcs += " " + getValue(obj.get("value"));
				}
			}

			System.out.println(sqlcs);

			String sql = "select * from xwcs_gsdr_yssjtk  where 1=1";

			sql += sqlcs;
			if (!rk_rq.equals("")) {
				sql += " and rk_rq='" + rk_rq + "' ";
			}
			if (!fz.equals("")) {
				if (fz.equals("se")) {
					sql = "select t." + fz + ",sum(t.dfbl) dfbl  from xwcs_gsdr_yssjtk t where 1=1";
				} else {
					sql = "select t." + fz + ",sum(t.se) se,sum(t.dfbl) dfbl,sum(t.qxj) qxj  from xwcs_gsdr_yssjtk t where 1=1";
				}

				sql += sqlcs;

				if (!rk_rq.equals("")) {
					sql += " and t.rk_rq='" + rk_rq + "' ";
				}
				sql += " group by t." + fz + " ";
			}

				// 总数据分页查询
			sql = "select * from (select row_.*, rownum rowno from (" + sql + ") row_ where rownum <= " + pagesize + "*"
					+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize + " + 1 ";
			System.out.println(sql);
			List<Map<String, Object>> sjjgall = bs.query(sql);

				// 查询count
			String sqlcount = "select count(*) cs from xwcs_gsdr_yssjtk a where 1=1" + sqlcs+"";
			if (!rk_rq.equals("")) {
				sqlcount += " and rk_rq='" + rk_rq + "' ";

			}
			if (!fz.equals("")) {
				sqlcount += " group by a." + fz + " ";
				sqlcount = "select count(cs) CS from (" + sqlcount + ") where 1=1";
			}
			System.out.println(sqlcount);
			List<Map<String, Object>> sjjgallcount = bs.query(sqlcount);
			System.out.println("sjjgallcount:" + sjjgallcount.get(0));
			String cont = "0";
			if (sjjgallcount.get(0) != null) {
				cont = getValue(sjjgallcount.get(0).get("CS"));
			}

			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < sjjgall.size(); i++) {
				Map<String, Object> map = sjjgall.get(i);
				map.put("NSRMC", map.get("NSRMC"));
				map.put("NSRSBH", map.get("NSRSBH"));
				map.put("PZZL", map.get("PZZL"));
				map.put("PZZG", map.get("PZZG"));
				map.put("PZHM", map.get("PZHM"));
				map.put("ZSXM", map.get("ZSXM"));
				map.put("ZSPM", map.get("ZSPM"));
				map.put("SKSSQQ", map.get("SKSSQQ"));

				map.put("XMMC", map.get("XMMC"));
				map.put("SKSSQZ", map.get("SKSSQZ"));
				map.put("SKZL", map.get("SKZL"));
				map.put("SKSX", map.get("SKSX"));
				map.put("HYML", map.get("HYML"));
				map.put("HYDL", map.get("HYDL"));
				map.put("HYZL", map.get("HYZL"));
				map.put("HY", map.get("HY"));

				map.put("SE", map.get("SE"));
				map.put("YSKM", map.get("YSKM"));
				map.put("YSKMMC", map.get("YSKMMC"));
				map.put("YSFPBL", map.get("YSFPBL"));
				map.put("SKGK", map.get("SKGK"));
				map.put("TTSJLX", map.get("TTSJLX"));
				map.put("TDSFYJWSZH", map.get("TDSFYJWSZH"));

				map.put("YHYYWD", map.get("YHYYWD"));

				map.put("ZHMC", map.get("ZHMC"));
				map.put("YHZH", map.get("YHZH"));
				map.put("KPRQ", map.get("KPRQ"));
				map.put("THRQ", map.get("THRQ"));
				map.put("XHRQ", map.get("XHRQ"));
				map.put("XHR", map.get("XHR"));
				map.put("JDXZ", map.get("JDXZ"));
				map.put("TTJG", map.get("TTJG"));

				map.put("SSGLY", map.get("SSGLY"));
				map.put("SKSSSWJG", map.get("SKSSSWJG"));
				map.put("ZGSWS", map.get("ZGSWS"));
				map.put("SWJG", map.get("SWJG"));
				map.put("TDSFYYLX", map.get("TDSFYYLX"));
				map.put("DZSPHM", map.get("DZSPHM"));
				map.put("TZLX", map.get("TZLX"));
				map.put("CZLX", map.get("CZLX"));

				map.put("SSJMXZDL", map.get("SSJMXZDL"));
				map.put("SSJMXZXL", map.get("SSJMXZXL"));
				map.put("FHJG", map.get("FHJG"));
				map.put("LRR", map.get("LRR"));
				map.put("KPR", map.get("KPR"));
				map.put("LRRQ", map.get("LRRQ"));
				map.put("RK_RQ", map.get("RK_RQ"));
				map.put("SSWJ", map.get("SSWJ"));

				map.put("ID", map.get("ID"));
				map.put("DRFS", map.get("DRFS"));
				map.put("SJZW", map.get("SJZW"));
				map.put("DFBL", map.get("DFBL"));
				map.put("QXJ", map.get("QXJ"));
				lists.add(map);
			}
			System.out.println("-----------:" + lists);
			return this.toJsonct("000", "查询成功！", lists, cont);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	/**
	 * 税种条件
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/selectSZ.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String selectSZ(HttpServletRequest request , HttpServletResponse response,@RequestParam Map<String, Object> rmap) {
		try {
			String jd = this.findJd(request, response);
			String xz = getValue(rmap.get("xz"));
			String sql = "select DISTINCT " + xz + " zsxmmc from XWCS_GSDR_YSSJTK where 1=1"+jd;
			List<Map<String, Object>> list = bs.query(sql);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) == null) {
					list.remove(i);
					i--;
				}
			}
			return this.toJson("000", "查询成功", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常");
		}
	}

	/**
	 * 退库数据查询
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	public String queryData(HttpServletRequest request , HttpServletResponse response,@RequestParam Map<String, Object> rmap) {
		try {
			String jd = this.findJd(request, response);
				// this.getRequest().getSession().getAttribute("uno");
			String yearNmonth = getValue(rmap.get("date")).toString();// 年月
				// 开始时间
			String starTime = "";
				// 结束时间
			String endTime = "";
			String[] star = yearNmonth.split(" - ");
			if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
				starTime = star[0];
				endTime = star[1];
				starTime = starTime.substring(0, 4) + starTime.substring(5, 7);
				endTime = endTime.substring(0, 4) + endTime.substring(5, 7);
			}

			String nsName = getValue(rmap.get("nsName")).toString();// 纳税人名称

				// 第一页
			String pageNo = getValue(rmap.get("pageNo"));// 写死 0
				// 每页显示的条数
			String pageSize = getValue(rmap.get("pageSize"));// 写死 80000

				// DZSPHM 电子税票号码是唯一的

			String sql = "select * from XWCS_GSDR_YSSJTK gs where 1 = 1 (-gs.NSRMC like '%'||?||'%'-) and (-gs.RK_RQ between ? and ?-)"+jd;

//			if (yearNmonth != null && yearNmonth != "" && nsName != "" && nsName != null) {
//				sql = sql + "where gs.NSRMC like '%" + nsName + "%'" + " and gs.RK_RQ between " + starTime + " and "
//						+ endTime;
//			}
//			if (nsName != "" && yearNmonth == "") {
//				sql = sql + "where gs.NSRMC like '%" + nsName + "%'";
//			}
//			if (nsName == "" && yearNmonth != "") {
//				sql = sql + "where gs.RK_RQ between " + starTime + " and " + endTime;
//			}

			String sql1 = "select Count(*) from (" + sql + ") where 1=1"+jd;
			sql1 = this.getSql2(sql1, new Object[] {nsName, starTime, endTime});
			List<Map<String, Object>> size = bs.query(sql1);
			int counts = Integer.parseInt(getValue(size.get(0).get("COUNT(*)")));

			List<Map<String, Object>> rscount = bs.query(sql, pageNo, pageSize);
			System.out.println("rscount:" + rscount);
			System.out.println("counts:" + counts);
			System.out.println("sql:" + sql);
				
			return this.toJson("000", "查询成功！", rscount, counts);

		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
}
