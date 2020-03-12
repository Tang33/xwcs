package fast.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;

import fast.main.util.Super;

public class tksyssjcx extends Super {

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/tksyssjcx";
		} catch (Exception e) {
			e.printStackTrace();
			return "xtgl/tksyssjcx";
		}
	}

	// 查询分组
	public String queryfz(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String sql = "SELECT DISTINCT mbm,zdm FROM FAST_YSSJCX_MB where dycxb = 'xwcs_gsdr_yssjtk' ";
			List<Map<String, Object>> list = this.getBs().query(sql);
			return this.toJson("000", "查询成功", list);
		} catch (Exception e) {
			return this.toJson("009", "查询异常");
		}
	}

	// 获取下拉框中的数据
	public String selectList(Map<String, Object> rmap) {
		try {
			init(rmap);
			String sql = "select MBM,ZDM FROM  FAST_YSSJCX_MB where dycxb = 'xwcs_gsdr_yssjtk' group by MBM,ZDM";
			List<Map<String, Object>> list = this.getBs().query(sql);
			return this.toJson("000", "查询成功！", list);

		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	// 查询展示
	public String queryzd(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);

			String xz = getValue(this.getForm().get("xz"));

			SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -1);
			Date m = c.getTime();
			String rkrq = format.format(m);

			String sql = "select " + xz + " XMMC from xwcs_gsdr_yssjtk group by " + xz;
			List<Map<String, Object>> sjjgall = this.getBs().query(sql);

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

	// 点击添加按钮依据传过来的名称查询
	public String selectislike(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String zdm = getValue(this.getForm().get("xz"));
			String sql = "select * FROM FAST_YSSJCX_MB where ZDM = '" + zdm + "' and dycxb = 'xwcs_gsdr_yssjtk'";
			System.out.println("selectislike:" + sql);
			List<Map<String, Object>> list = this.getBs().query(sql);

			return this.toJson("000", "查询成功", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常");
		}
	}

	// 查询展示 maplist
	public String querySdbg(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);

			String page = getValue(this.getForm().get("page"));
			String pagesize = getValue(this.getForm().get("limit"));
			// 获取页面传递过来的input中的值
			String fz = getValue(this.getForm().get("fz"));
			// 获取表单中的数据
			String form = getValue(this.getForm().get("form"));
			String rk_rq2 = getValue(this.getForm().get("drrq"));
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
					+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize + " + 1";
			System.out.println(sql);
			List<Map<String, Object>> sjjgall = this.getBs().query(sql);

			// 查询count
			String sqlcount = "select count(*) cs from xwcs_gsdr_yssjtk a where 1=1" + sqlcs;
			if (!rk_rq.equals("")) {
				sqlcount += " and rk_rq='" + rk_rq + "' ";

			}
			if (!fz.equals("")) {
				sqlcount += " group by a." + fz + " ";
				sqlcount = "select count(cs) CS from (" + sqlcount + ")";
			}
			System.out.println(sqlcount);
			List<Map<String, Object>> sjjgallcount = this.getBs().query(sqlcount);
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

	// 查询税种
	public String selectSZ(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String xz = getValue(this.getForm().get("xz"));
			String sql = "select DISTINCT " + xz + " zsxmmc from XWCS_GSDR_YSSJTK";
			List<Map<String, Object>> list = this.getBs().query(sql);
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

	// 按企业查询数据查询
	public String queryData(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);

			// this.getRequest().getSession().getAttribute("uno");
			String yearNmonth = getValue(this.getForm().get("date")).toString();// 年月
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

			String nsName = getValue(this.getForm().get("nsName")).toString();// 纳税人名称

			// 第一页
			String pageNo = getValue(this.getForm().get("pageNo"));// 写死 0
			// 每页显示的条数
			String pageSize = getValue(this.getForm().get("pageSize"));// 写死 80000

			// DZSPHM 电子税票号码是唯一的

			String sql = "select * from XWCS_GSDR_YSSJTK gs ";

			if (yearNmonth != null && yearNmonth != "" && nsName != "" && nsName != null) {
				sql = sql + "where gs.NSRMC like '%" + nsName + "%'" + " and gs.RK_RQ between " + starTime + " and "
						+ endTime;
			}
			if (nsName != "" && yearNmonth == "") {
				sql = sql + "where gs.NSRMC like '%" + nsName + "%'";
			}
			if (nsName == "" && yearNmonth != "") {
				sql = sql + "where gs.RK_RQ between " + starTime + " and " + endTime;
				;
			}

			// sql += "select * from (select temp.*,ROWNUM RN from(" + sql +") temp where RN
			// between " + pageNo + "and" + pageSize;

			String sql1 = "select Count(*) from (" + sql + ")";
			List<Map<String, Object>> size = this.getBs().query(sql1);

			int counts = Integer.parseInt(getValue(size.get(0).get("COUNT(*)")));

			// sql = "select * from (select temp.*,ROWNUM rn from ("+sql+") temp) where rn
			// between " + pageNo + " and " + pageSize;

			List<Map<String, Object>> rscount = this.getBs().query(sql, pageNo, pageSize);
			System.out.println("rscount:" + rscount);
			System.out.println("counts:" + counts);
			System.out.println("sql:" + sql);
			// String sql1 = "select sum(XH) from XWCS_GSDR_YSSJGS";

			return this.toJson("000", "查询成功！", rscount, counts);

		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
}
