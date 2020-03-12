package fast.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

/**
 * 街道属地税收统计表查询
 * 
 * @author Administrator
 *
 */
public class jdsdsstjb extends Super {

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "zdsSjcx/jdsdsstjb";
		} catch (Exception e) {
			e.printStackTrace();
			return "zdsSjcx/jdsdsstjb";
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
	 * 企业汇总表
	 * 
	 * @param rmap
	 * @return
	 */
	public String queryJdsd(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);

			String pageNo = getValue(this.getForm().get("pageNo"));
			String pageSize = getValue(this.getForm().get("pageSize"));

			String qsrq = getValue(this.getForm().get("cxrq"));
			String jd_dm = getValue(this.getForm().get("jd_dm"));

			if("".equals(jd_dm) || jd_dm ==null) {
				jd_dm="%";
			}
			
			if("".equals(qsrq) || qsrq ==null) {
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				qsrq = sdf.format(dt);
			}
			
			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN", "String", qsrq));
			list.add(new Mode("IN", "String", jd_dm));
			list.add(new Mode("OUT", "RS", ""));

			List<Map<String, Object>> sjList = (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYJDSD", list);
			System.out.println(sjList);
			return this.toJson("000", "查询成功！", sjList);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	// 导出excel
	public Object export(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String jd_dm = getValue(this.getForm().get("jd_dm"));

			String qsrq = getValue(this.getForm().get("cxrq"));
			if("".equals(jd_dm) || jd_dm ==null) {
				jd_dm="%";
			}

			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN", "String", "201905"));
			list.add(new Mode("IN", "String", jd_dm));
			list.add(new Mode("OUT", "RS", ""));

			List<Map<String, Object>> sjList = (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYJDSD", list);


			Map<String, Object> map = new HashMap<String, Object>();
			String[] cols = { "街道", "一般企业", "房地产企业", "亿元企业", "合计", "比重"};
			String[] keys = { "JD_MC", "YBQY", "FDCQY", "YYQY", "HJ", "BL"};
			map.put("fileName", "导出excel.xlsx");
			map.put("cols", cols);
			map.put("keys", keys);
			map.put("list", sjList);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

}
