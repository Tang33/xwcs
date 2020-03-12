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
 * 五百万基本情况汇总
 * 
 * @author Administrator
 *
 */
public class jbqkhz extends Super {

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "wbw/jbqkhz";
		} catch (Exception e) {
			e.printStackTrace();
			return "wbw/jbqkhz";
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
//			sql="SELECT * FROM TEMP_QUERYYDHZBBYQY";
//			System.out.println("sql>>>>: "+sql);
//			List<Map<String, Object>> sjlist=this.getBs().query(sql);
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
	public String queryQyhzb(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);

			String pageNo = getValue(this.getForm().get("pageNo"));
			String pageSize = getValue(this.getForm().get("pageSize"));

			String qsrq = getValue(this.getForm().get("cxrq"));
			String jd_dm = getValue(this.getForm().get("jd_dm"));

			String sortname = getValue(this.getForm().get("sortname"));
			String sorttype = getValue(this.getForm().get("sorttype"));
			String tjkj = getValue(this.getForm().get("tjkj"));
			String tjdw = getValue(this.getForm().get("tjdw"));

			String count = getValue(this.getForm().get("count"));// 先获取页面数据总量

			int counts = 0;// 声明一个变量暂时存储查到的数据总量

			String nd = "";
			String yf = "";

			if ("".equals(qsrq) || qsrq == null) {
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				qsrq = sdf.format(dt);
			}
			nd = qsrq.substring(0, qsrq.indexOf('-'));
			yf = qsrq.substring(qsrq.indexOf('-') + 1);
//			将年月拆开
//			if (qsrq!=null && !qsrq.equals("")) {
//				nd = qsrq.substring(0,qsrq.indexOf('-'));
//				yf = qsrq.substring(qsrq.indexOf('-')+1);
//			}else {
//				Date dt = new Date();
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//				qsrq = sdf.format(dt);
//			}

			if ("".equals(pageNo)) {
				// 1表示是合伙企业

			} else {
				// 如果为空，则是首次查询，
				if (count.equals("0")) {

					List<Mode> list1 = new ArrayList<Mode>();
					list1.add(new Mode("IN", "String", nd));
					list1.add(new Mode("IN", "String", yf));
					list1.add(new Mode("IN", "String", tjkj));
					list1.add(new Mode("IN", "String", sortname));
					list1.add(new Mode("IN", "String", sorttype));
					list1.add(new Mode("IN", "String", jd_dm));
					list1.add(new Mode("IN", "String", "1"));
					list1.add(new Mode("IN", "String", "1"));
					list1.add(new Mode("IN", "String", "10000"));
					list1.add(new Mode("IN", "String", tjdw));

					list1.add(new Mode("OUT", "RS", ""));

					List<Map<String, Object>> rsjList = (List<Map<String, Object>>) JdbcConnectedPro
							.call("PKG_CX_WBW.QUERYYDHZBBYQY", list1);
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

			list.add(new Mode("IN", "String", recorders1));
			list.add(new Mode("IN", "String", recorders2));

			list.add(new Mode("IN", "String", tjdw));
			list.add(new Mode("OUT", "RS", ""));

			List<Map<String, Object>> sjList = (List<Map<String, Object>>) JdbcConnectedPro
					.call("PKG_CX_WBW.QUERYYDHZBBYQY", list);
			System.out.println(">>>>查询数据<<<<");
			System.out.println(sjList);
			return this.toJson("000", "查询成功！", sjList, counts);
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

			String sortname = getValue(this.getForm().get("sortname"));
			String sorttype = getValue(this.getForm().get("sorttype"));
			String tjkj = getValue(this.getForm().get("tjkj"));
			String tjdw = getValue(this.getForm().get("tjdw"));

			if ("".equals(jd_dm) || jd_dm == null) {
				jd_dm = "%";
			}

			String nd = "";
			String yf = "";

			if ("".equals(qsrq) || qsrq == null) {
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				qsrq = sdf.format(dt);
			}
			nd = qsrq.substring(0, qsrq.indexOf('-'));
			yf = qsrq.substring(qsrq.indexOf('-') + 1);

			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN", "String", nd));
			list.add(new Mode("IN", "String", yf));
			list.add(new Mode("IN", "String", tjkj));
			list.add(new Mode("IN", "String", sortname));
			list.add(new Mode("IN", "String", sorttype));

			list.add(new Mode("IN", "String", jd_dm));
			list.add(new Mode("IN", "String", "2"));

			list.add(new Mode("IN", "String", "1"));
			list.add(new Mode("IN", "String", "10000"));

			list.add(new Mode("IN", "String", tjdw));
			list.add(new Mode("OUT", "RS", ""));

			List<Map<String, Object>> sjList = (List<Map<String, Object>>) JdbcConnectedPro
					.call("PKG_CX_WBW.QUERYYDHZBBYQY", list);

			Map<String, Object> map = new HashMap<String, Object>();
			String[] cols = { "本年累计(税收情况)", "上年同期(税收情况)", "增减额(税收情况)", "增减率(税收情况)", "上年合计(税收情况)", "本年累计(营业收入情况)",
					"上年同期(营业收入情况)", "增减额(营业收入情况)", "增减率(营业收入情况)", "上年合计(营业收入情况)", "收入增减/税收增减" };
			String[] keys = { "SSBNYLJ", "SNSSTQS", "SSTBZJE", "SSTBZJL", "SSSNQNHJ", "SSBNYLJ", "YSSRSNTQS",
					"YSSRTBZJE", "YYSRTBZJL", "YYSRSNQNHJ", "YYSRZJLSSZJL" };
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
