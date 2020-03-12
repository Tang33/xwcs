package fast.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

public class qyqcmbcx extends Super {

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String ssdw_dm = this.getRequest().getSession().getAttribute("dwid").toString();
			this.getRequest().setAttribute("dwid", ssdw_dm);
			return "xtgl/qyqcmbcx";
		} catch (Exception e) {
			e.printStackTrace();
			return "xtgl/qyqcmbcx";
		}
	}

	public String queryInit(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");
			// 街道
			String sql = this.getSql("jd_query");
			List<Map<String, Object>> jdlist = this.getBs().query(sql);
			// 行业
			sql = this.getSql("hy_query");
			List<Map<String, Object>> hylist = this.getBs().query(sql);
			// 重点税源企业
			/*
			 * sql=this.getSql("去查重点税源企业"); List<Map<String, Object>>
			 * zdsylist=this.getBs().query(sql);
			 */

			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();

			map.put("hylist", hylist);
			map.put("jdlist", jdlist);
			// map.put("zdsylist",zdsylist);
			return this.toJson("000", "查询成功！", map);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	// 初始化模板
		public String initmb(Map<String, Object> rmap) {
			try {
				// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
				initMap(rmap);
				String sql = "select * from MBDC_QYQCMBGL order by uuid";
				List<Map<String, Object>> list = this.getBs().query(sql);
				return this.toJson("000", "查询成功！", list);

			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询异常！");
			}
		}
	
	// 按企业查询数据查询
	public String queryData(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String isadmin = (String) this.getRequest().getSession().getAttribute("dwid");
			// 高级权限
			if ("00".equals(isadmin)) {

			}
			this.getRequest().getSession().getAttribute("uno");
			String yearNmonth = getValue(this.getForm().get("date")).toString();// 年月
			String starTime = "";
			String endTime = "";
			String[] star = yearNmonth.split(" - ");
			if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
				starTime = star[0];
				endTime = star[1];
				starTime = starTime.substring(0, 4) + starTime.substring(5, 7);
				endTime = endTime.substring(0, 4) + endTime.substring(5, 7);
			}
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");

			if (!ssdw_dm.equals("00")) {
				int staryear = Integer.parseInt(starTime.substring(0, 4));
				int starmonth = Integer.parseInt(starTime.substring(4, 6));
				int endryear = Integer.parseInt(endTime.substring(0, 4));
				int endmonth = Integer.parseInt(endTime.substring(4, 6));
				int m = ((endryear - staryear) * 12 + endmonth - starmonth) + 1;
				for (int i = 0; i < m; i++) {
					String rq = staryear + "" + (starmonth < 10 ? "0" + starmonth : starmonth);
					String statesql = "select state from xwcs_cxzt where to_Char(rkrq,'yyyyMM')='" + rq + "'";
					Map<String, Object> map = this.getBs().queryOne(statesql);
					if (map == null) {
						SimpleDateFormat dateparse = new SimpleDateFormat("yyyyMM");
						Date date = dateparse.parse(rq);
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月");// 可以方便地修改日期格式
						String resultrq = dateFormat.format(date);
						return this.toJson("001", resultrq + "没有数据！");
					} else {
						if (getValue(map.get("STATE")).equals("0")) {
							endTime = rq;
							break;
						} else if (getValue(map.get("STATE")).equals("")) {
							endTime = rq;
							break;
						}
					}
					starmonth++;
					if (starmonth > 12) {
						starmonth = 1;
						staryear++;
					}
				}
			}

			String jd = getValue(this.getForm().get("jd")).toString();// 街道
			String type = getValue(this.getForm().get("type")).toString();// 合计或月明细
			String nsName = getValue(this.getForm().get("nsName")).toString();// 纳税人名称
			/*
			 * if("".equals(nsName)) { nsName="%"; }
			 */
			String sortname = getValue(this.getForm().get("sortname")).toString();// 税种名字
			String px = getValue(this.getForm().get("px")).toString();// 排序
			String qyxz = getValue(this.getForm().get("qyxz")).toString();// 企业性质
			String islp = getValue(this.getForm().get("islp")).toString();// 是否合伙
			String tjkj = getValue(this.getForm().get("tjkj")).toString();// 统计口径
			String hylist = getValue(this.getForm().get("hylist")).toString();// 行业
			String zdsyh = getValue(this.getForm().get("zdsyh")).toString();// 重点税源
			/*
			 * String count = getValue(this.getForm().get("count")).toString();// 写死 0
			 * String count1 = getValue(this.getForm().get("count1")).toString();//
			 */
			int count = Integer.parseInt(getValue(this.getForm().get("pageNo")));// 写死 0
			int count1 = Integer.parseInt(getValue(this.getForm().get("pageSize")));// 写死 80000
			count = (count - 1) * count1;
			count1 = count + count1;

			List<Mode> listcount = new ArrayList<Mode>();
			listcount.add(new Mode("IN", "String", nsName));// 纳税人名字
			listcount.add(new Mode("IN", "String", jd));// 街道代码
			listcount.add(new Mode("IN", "String", starTime));// 时间起
			listcount.add(new Mode("IN", "String", endTime));// 时间止
			listcount.add(new Mode("IN", "String", type));// 是否按月合计
			listcount.add(new Mode("IN", "String", qyxz));// 企业性质
			listcount.add(new Mode("IN", "String", hylist));// 行业代码
			listcount.add(new Mode("IN", "String", islp));// 是否合伙
			listcount.add(new Mode("IN", "String", zdsyh));// 重点税源户代码
			listcount.add(new Mode("OUT", "RS", ""));

			List<Map<String, Object>> rscount = (List<Map<String, Object>>) JdbcConnectedPro
					.call("SJ_CX_NEW.QUERYBYQY_JLS", listcount);// 调用存储过程
			System.out.println(rscount);

			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN", "String", nsName));// 纳税人名字
			list.add(new Mode("IN", "String", jd));// 街道代码
			list.add(new Mode("IN", "String", starTime));// 时间起
			list.add(new Mode("IN", "String", endTime));// 时间止
			list.add(new Mode("IN", "String", type));// 是否按月合计
			list.add(new Mode("IN", "String", qyxz));// 企业性质
			list.add(new Mode("IN", "String", hylist));// 行业代码
			list.add(new Mode("IN", "String", islp));// 是否合伙
			list.add(new Mode("IN", "String", sortname));// 税种名字
			list.add(new Mode("IN", "String", px));// 排序方式
			list.add(new Mode("IN", "String", tjkj));// 统计口径
			list.add(new Mode("IN", "String", zdsyh));// 重点税源户代码
			list.add(new Mode("IN", "String", getValue(count)));// 写死
			list.add(new Mode("IN", "String", getValue(count1)));// 写死

			list.add(new Mode("OUT", "RS", ""));
			System.out.println(list);
			// SJ_CX_NEW.QUERYBYQY201605_NEW1
			List<Map<String, Object>> rs = (List<Map<String, Object>>) JdbcConnectedPro
					.call("SJ_CX_NEW.QUERYBYQY201605_NEW1", list);// 调用存储过程
			int counts = Integer.parseInt(getValue(rscount.get(0).get("COUNTS")));
			// System.out.println(rs);
			// HJ 合计
			// ZZS 增值税
			// YGZZZS 营改增增值税
			// YYS 营业税
			// QYSDS 企业所得税（合计）
			// GRSDS 个人所得税
			// FCS 房产税
			// YHS 印花税
			// CCS 车船税
			// CSWHJSS 城市维护建设税
			// DFJYFJ 地方教育附加
			// JYFJ 教育附加
			// CZTDSYS 城镇土地使用税
			// HBS 环保税
			DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
			for (int i = 0; i < rs.size(); i++) {
				Map map = rs.get(i);
				double hj = Double.parseDouble((String) rs.get(i).get("ZZS"))
						+ Double.parseDouble((String) rs.get(i).get("YGZZZS"))
						+ Double.parseDouble((String) rs.get(i).get("YYS"))
						+ Double.parseDouble((String) rs.get(i).get("QYSDS"))
						+ Double.parseDouble((String) rs.get(i).get("GRSDS"))
						+ Double.parseDouble((String) rs.get(i).get("FCS"))
						+ Double.parseDouble((String) rs.get(i).get("YHS"))
						+ Double.parseDouble((String) rs.get(i).get("CCS"))
						+ Double.parseDouble((String) rs.get(i).get("DFJYFJ"))
						+ Double.parseDouble((String) rs.get(i).get("JYFJ"))
						+ Double.parseDouble(rs.get(i).get("CSWHJSS") == null ? "0" : (String) rs.get(i).get("CSWHJSS"))
						+ Double.parseDouble(rs.get(i).get("CZTDSYS") == null ? "0" : (String) rs.get(i).get("CZTDSYS"))
						+ Double.parseDouble(rs.get(i).get("HBS") == null ? "0" : (String) rs.get(i).get("HBS"));
				map.put("HJ", decimalFormat.format(hj));
			}
			// 其他街道
			if (!"00".equals(ssdw_dm)) {
				for (int i = 0; i < rs.size(); i++) {
					Map map = rs.get(i);
					double hj = Double.parseDouble((String) rs.get(i).get("ZZS"))
							+ Double.parseDouble((String) rs.get(i).get("YGZZZS"))
							+ Double.parseDouble((String) rs.get(i).get("YYS"))
							+ Double.parseDouble((String) rs.get(i).get("QYSDS"))
							+ Double.parseDouble((String) rs.get(i).get("GRSDS"))
							+ Double.parseDouble((String) rs.get(i).get("FCS"))
							+ Double.parseDouble((String) rs.get(i).get("YHS"))
							+ Double.parseDouble((String) rs.get(i).get("CCS"))
							+ Double.parseDouble((String) rs.get(i).get("DFJYFJ"))
							+ Double.parseDouble((String) rs.get(i).get("JYFJ"));
//							+ Double.parseDouble(sjList.get(i).get("CSWHJSS") == null ? "0":(String)sjList.get(i).get("CSWHJSS"))
//							+ Double.parseDouble(sjList.get(i).get("CZTDSYS") == null ? "0":(String)sjList.get(i).get("CZTDSYS"))
//							+ Double.parseDouble(sjList.get(i).get("HBS") == null ? "0":(String)sjList.get(i).get("HBS"));
					map.put("HJ", decimalFormat.format(hj));
				}
			}
			return this.toJson("000", "查询成功！", rs, counts);

		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	private String formatDouble(double d) {
		NumberFormat nf = NumberFormat.getInstance();
		// 设置保留多少位小数
		nf.setMaximumFractionDigits(2);
		// 取消科学计数法
		nf.setGroupingUsed(false);
		// 返回结果
		return nf.format(d);
	}
}
