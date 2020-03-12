package fast.app;

import java.sql.ResultSet;
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
 * 企业汇总表（附表1）
 * 
 * @author Administrator
 *
 */
public class qyhzb1 extends Super {

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "wbw/qyhzb1";
		} catch (Exception e) {
			e.printStackTrace();
			return "wbw/qyhzb1";
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

//			String pageNo = getValue(this.getForm().get("pageNo"));
//			String pageSize = getValue(this.getForm().get("pageSize"));
//			
//			sql="SELECT * FROM TEMP_QUERYYDHZBBYQY_MX1";
//			System.out.println("sql>>>>: "+sql);
//			List<Map<String, Object>> sjlist=this.getBs().query(sql);
//			int count = this.getBs().queryCount(sql);
//			map.put("sjlist", sjlist);

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
	public String queryHzb1(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);

			String pageNo = getValue(this.getForm().get("pageNo"));
			String pageSize = getValue(this.getForm().get("pageSize"));

			String qsrq = getValue(this.getForm().get("cxrq"));
			String jd_dm = getValue(this.getForm().get("jd_dm"));
			String hy_dm = getValue(this.getForm().get("hy_dm"));
			String zsxm_dm = getValue(this.getForm().get("zsxm_dm"));

			String sortname = getValue(this.getForm().get("sortname"));
			String sorttype = getValue(this.getForm().get("sorttype"));
			String tjkj = getValue(this.getForm().get("tjkj"));
			String tjdw = getValue(this.getForm().get("tjdw"));
			

			String count = getValue(this.getForm().get("count"));// 先获取页面数据总量

			int counts = 0;// 声明一个变量暂时存储查到的数据总量
			
			

			String nd = "";
			String yf = "";

//			将年月拆开
			if ("".equals(qsrq) || qsrq == null) {
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				qsrq = sdf.format(dt);
			}
			nd = qsrq.substring(0, qsrq.indexOf('-'));
			yf = qsrq.substring(qsrq.indexOf('-') + 1);

			
			

			if ("".equals(pageNo)) {
				// 1表示是合伙企业

			} else {
				// 如果为空，则是首次查询，
				if (count.equals("0")) {

					List<Mode> list = new ArrayList<Mode>();
					list.add(new Mode("IN", "String", nd));
					list.add(new Mode("IN", "String", yf));
					list.add(new Mode("IN", "String", tjkj));
					list.add(new Mode("IN", "String", sortname));
					list.add(new Mode("IN", "String", sorttype));

					list.add(new Mode("IN", "String", jd_dm));
					list.add(new Mode("IN", "String", "2"));
					list.add(new Mode("IN", "String", hy_dm));
					list.add(new Mode("IN", "String", zsxm_dm));

					list.add(new Mode("IN", "String", "1"));
					list.add(new Mode("IN", "String", "10000"));

					list.add(new Mode("IN", "String", tjdw));
					list.add(new Mode("OUT", "RS", ""));
					List<Map<String, Object>> rsjList = (List<Map<String, Object>>) JdbcConnectedPro
							.call("PKG_CX_WBW.QUERYYDHZBBYQY_FSZ_FB1", list);
					System.out.println(rsjList);
					if (rsjList.size()>0) {
						counts = rsjList.size();
					} else {
						counts=0;
					}
				}else {
					counts=Integer.parseInt(count);
				}
			}

			String recorders1 = String.valueOf((Integer.parseInt(pageNo)-1)*Integer.parseInt(pageSize));// 查询条数开始数
			String recorders2 = String.valueOf(Integer.parseInt(pageNo)*Integer.parseInt(pageSize));// 查询条数结束数
			
			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN", "String", nd));
			list.add(new Mode("IN", "String", yf));
			list.add(new Mode("IN", "String", tjkj));
			list.add(new Mode("IN", "String", sortname));
			list.add(new Mode("IN", "String", sorttype));

			list.add(new Mode("IN", "String", jd_dm));
			list.add(new Mode("IN", "String", "2"));
			list.add(new Mode("IN", "String", hy_dm));
			list.add(new Mode("IN", "String", zsxm_dm));

			list.add(new Mode("IN", "String", recorders1));
			list.add(new Mode("IN", "String", recorders2));

			list.add(new Mode("IN", "String", tjdw));
			list.add(new Mode("OUT", "RS", ""));
			List<Map<String, Object>> sjList = (List<Map<String, Object>>) JdbcConnectedPro
					.call("PKG_CX_WBW.QUERYYDHZBBYQY_FSZ_FB1", list);
			//int count1 = Integer.parseInt(counts);
			return this.toJson("000", "查询成功！", sjList,counts);
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

			String qsrq = getValue(this.getForm().get("cxrq"));
			String jd_dm = getValue(this.getForm().get("jd_dm"));
			String hy_dm = getValue(this.getForm().get("hy_dm"));
			String zsxm_dm = getValue(this.getForm().get("zsxm_dm"));

			String sortname = getValue(this.getForm().get("sortname"));
			String sorttype = getValue(this.getForm().get("sorttype"));
			String tjkj = getValue(this.getForm().get("tjkj"));
			String tjdw = getValue(this.getForm().get("tjdw"));

			String nd = "";
			String yf = "";

//				将年月拆开
			if ("".equals(qsrq) || qsrq == null) {
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				qsrq = sdf.format(dt);
			}
			nd = qsrq.substring(0, qsrq.indexOf('-'));
			yf = qsrq.substring(qsrq.indexOf('-') + 1);

			if ("".equals(jd_dm) || jd_dm == null) {
				jd_dm = "%";
			}
			if ("".equals(hy_dm) || hy_dm == null) {
				hy_dm = "%";
			}
			if ("".equals(zsxm_dm) || zsxm_dm == null) {
				zsxm_dm = "%";
			}

			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN", "String", nd));
			list.add(new Mode("IN", "String", yf));
			list.add(new Mode("IN", "String", tjkj));
			list.add(new Mode("IN", "String", sortname));
			list.add(new Mode("IN", "String", sorttype));

			list.add(new Mode("IN", "String", jd_dm));
			list.add(new Mode("IN", "String", "2"));
			list.add(new Mode("IN", "String", hy_dm));
			list.add(new Mode("IN", "String", zsxm_dm));

			list.add(new Mode("IN", "String", "1"));
			list.add(new Mode("IN", "String", "10000"));

			list.add(new Mode("IN", "String", tjdw));
			list.add(new Mode("OUT", "RS", ""));
			List<Map<String, Object>> sjList = (List<Map<String, Object>>) JdbcConnectedPro
					.call("PKG_CX_WBW.QUERYYDHZBBYQY_FSZ_FB1", list);

			Map<String, Object> map = new HashMap<String, Object>();
			String[] cols = { "纳税人名称", "营业收入情况(本年1月)", "营业收入情况(上年1月)", "营业收入情况(本年2月)", "营业收入情况(上年2月)", "营业收入情况(本年3月)","营业收入情况(上年3月)",
					"营业收入情况(本年4月)", "营业收入情况(上年4月)", "营业收入情况(本年5月)", "营业收入情况(上年5月)", "营业收入情况(本年6月)","营业收入情况(上年6月)", 
					"营业收入情况(本年7月)", "营业收入情况(上年7月)", "营业收入情况(本年8月)", "营业收入情况(上年8月)", "营业收入情况(本年9月)","营业收入情况(上年9月)", 
					"营业收入情况(本年10月)", "营业收入情况(上年10月)", "营业收入情况(本年11月)", "营业收入情况(上年11月)", "营业收入情况(本年12月)","营业收入情况(上年12月)","本年累计","上年同期","上年累计",
					"税收情况(本年1月)", "税收情况(上年1月)", "税收情况(本年2月)", "税收情况(上年2月)", "税收情况(本年3月)","税收情况(上年3月)",
					"税收情况(本年4月)", "税收情况(上年4月)", "税收情况(本年5月)", "税收情况(上年5月)", "税收情况(本年6月)","税收情况(上年6月)", 
					"税收情况(本年7月)", "税收情况(上年7月)", "税收情况(本年8月)", "税收情况(上年8月)", "税收情况(本年9月)","税收情况(上年9月)", 
					"税收情况(本年10月)", "税收情况(上年10月)", "税收情况(本年11月)", "税收情况(上年11月)", "税收情况(本年12月)","税收情况(上年12月)",
					"本年累计","上年同期","上年累计"};
			String[] keys = { "NSRMC", "BNYYSSYLJ1", "SNYYSSYLJ1", "BNYYSSYLJ2", "SNYYSSYLJ2", "BNYYSSYLJ3", "SNYYSSYLJ3", "BNYYSSYLJ4", "SNYYSSYLJ4"
					, "BNYYSSYLJ5", "SNYYSSYLJ5", "BNYYSSYLJ6", "SNYYSSYLJ6", "BNYYSSYLJ7", "SNYYSSYLJ7", "BNYYSSYLJ8", "SNYYSSYLJ8"
					, "BNYYSSYLJ9", "SNYYSSYLJ9", "BNYYSSYLJ10", "SNYYSSYLJ10", "BNYYSSYLJ11", "SNYYSSYLJ11", "BNYYSSYLJ12", "SNYYSSYLJ12", "YSSRBYLJ", "YSSRSNTQS", "YYSRSNQNHJ"
					, "BNSSYLJ1", "SNSSYLJ1", "BNSSYLJ2", "SNSSYLJ2", "BNSSYLJ3", "SNSSYLJ3", "BNSSYLJ4", "SNSSYLJ4"
					, "BNSSYLJ5", "SNSSYLJ5", "BNSSYLJ6", "SNSSYLJ6", "BNSSYLJ7", "SNSSYLJ7", "BNSSYLJ8", "SNSSYLJ8"
					, "BNSSYLJ9", "SNSSYLJ9", "BNSSYLJ10", "SNSSYLJ10", "BNSSYLJ11", "SNSSYLJ11", "BNSSYLJ12", "SNSSYLJ12", "SSBNYLJ", "SNSSTQS", "SSSNQNHJ"};
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
