package fast.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;

import fast.main.util.ExcelRead;
import fast.main.util.ExcelUtil;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

public class aqycx extends Super {

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String ssdw_dm = this.getRequest().getSession().getAttribute("dwid").toString();
			this.getRequest().setAttribute("dwid", ssdw_dm);
			return "xtgl/aqycx";
		} catch (Exception e) {
			e.printStackTrace();
			return "xtgl/aqycx";
		}
	}

	public static void main(String[] args) {
		InputStream is = null;
		InputStream iss = null;
		try {
			is = new FileInputStream(new File("D:\\13.98亿元对应11月份收入.xlsx"));
			// 判断接收到的文件格式
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("企业名称", -1);
			map.put("街道", -1);
			map.put("求和项:合计", -1);
			map.put("求和项:增值税（10101）", -1);
			map.put("求和项:营业税（10103）", -1);
			map.put("求和项:企业所得税（10104）", -1);
			map.put("求和项:个人所得税（10106）", -1);
			map.put("求和项:城市维护建设税（10109）", -1);
			map.put("求和项:城镇土地使用税（10112）", -1);
			map.put("求和项:印花税（10111）", -1);
			map.put("求和项:房产税（10110）", -1);
			map.put("求和项:车船税（10114）", -1);
			map.put("求和项:土地增值税（10113）", -1);
			map.put("求和项:耕地占用税（10118）", -1);
			map.put("求和项:其他", -1);
			map.put("求和项:环境保护税（10121）", -1);
			map.put("1-10月增值税", -1);
			map.put("01-10月营业税", -1);
			map.put("01-10月个人所得税", -1);
			map.put("01-10月环保税", -1);
			map.put("01-10月印花税", -1);
			map.put("01-10月车船税", -1);
			map.put("01-10月城镇土地使用税", -1);
			map.put("01-10月城市维护建设税", -1);
			map.put("01-10月房产税", -1);
			map.put("01-10月企业所得税", -1);
			map.put("1-10月合计", -1);
			map.put("备注", -1);
			// 读取excel文件
			List<Map<String, String>> list = ExcelRead.pomExcel("D:\\13.98亿元对应11月份收入.xlsx", is, map);
			System.out.println(list.get(0));
			iss = new FileInputStream(new File("D:\\年度明细查询.xls"));
			Map<String, Integer> mapp = new HashMap<String, Integer>();
			mapp.put("纳税人名称", -1);
			mapp.put("2019年01-11月增值税地方口径", -1);
			mapp.put("2019年01-11月营业税地方口径", -1);
			mapp.put("2019年01-11月企业所得税地方口径",-1);
			mapp.put("2019年01-11月个人所得税地方口径", -1);
			mapp.put("2019年01-11月城市维护建设税地方口径", -1);
			mapp.put("2019年01-11月城镇土地使用税地方口径",-1);
			mapp.put("2019年01-11月印花税地方口径", -1);
			mapp.put("2019年01-11月房产税地方口径", -1);
			mapp.put("2019年01-11月车船税地方口径", -1);
			mapp.put("2019年01-11月环保税地方口径", -1);
			mapp.put("1-11月合计",-1);
			List<Map<String, String>> list2 = ExcelRead.pomExcel("D:\\年度明细查询.xls", iss, mapp);
			List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map1 = list.get(i);
				for (int j = 0; j < list2.size(); j++) {
					Map<String, String> map2 = list2.get(j);
					System.out.println(map1.get("企业名称")+"                 "+map2.get("纳税人名称"));
					if (map1.get("企业名称").equals(map2.get("纳税人名称"))) {
						Map<String, Object> map3 = new HashMap<String, Object>();

						if (map1.get("求和项:增值税（10101）").equals("0")) {
							map2.put("2019年01-11月增值税地方口径", "0");
						}
						if (map1.get("求和项:营业税（10103）").equals("0")) {
							map2.put("2019年01-11月营业税地方口径", "0");
						}
						if (map1.get("求和项:企业所得税（10104）").equals("0")) {
							map2.put("2019年01-11月企业所得税地方口径", "0");
						}
						if (map1.get("求和项:个人所得税（10106）").equals("0")) {
							map2.put("2019年01-11月个人所得税地方口径", "0");
						}
						if (map1.get("求和项:城市维护建设税（10109）").equals("0")) {
							map2.put("2019年01-11月城市维护建设税地方口径", "0");
						}
						if (map1.get("求和项:城镇土地使用税（10112）").equals("0")) {
							map2.put("2019年01-11月城镇土地使用税地方口径", "0");
						}
						if (map1.get("求和项:印花税（10111）").equals("0")) {
							map2.put("2019年01-11月印花税地方口径", "0");
						}
						if (map1.get("求和项:房产税（10110）").equals("0")) {
							map2.put("2019年01-11月房产税地方口径", "0");
						}
						if (map1.get("求和项:车船税（10114）").equals("0")) {
							map2.put("2019年01-11月车船税地方口径", "0");
						}
						if (map1.get("求和项:环境保护税（10121）").equals("0")) {
							map2.put("2019年01-11月环保税地方口径", "0");
						}
						map3.put("企业名称", map2.get("纳税人名称"));
						map3.put("2019年01-11月增值税地方口径", map2.get("2019年01-11月增值税地方口径"));
						map3.put("2019年01-11月营业税地方口径", map2.get("2019年01-11月营业税地方口径"));
						map3.put("2019年01-11月企业所得税地方口径", map2.get("2019年01-11月企业所得税地方口径"));
						map3.put("2019年01-11月个人所得税地方口径", map2.get("2019年01-11月个人所得税地方口径"));
						map3.put("2019年01-11月城市维护建设税地方口径", map2.get("2019年01-11月城市维护建设税地方口径"));
						map3.put("2019年01-11月城镇土地使用税地方口径", map2.get("2019年01-11月城镇土地使用税地方口径"));
						map3.put("2019年01-11月印花税地方口径", map2.get("2019年01-11月印花税地方口径"));
						map3.put("2019年01-11月房产税地方口径", map2.get("2019年01-11月房产税地方口径"));
						map3.put("2019年01-11月车船税地方口径", map2.get("2019年01-11月车船税地方口径"));
						map3.put("2019年01-11月环保税地方口径", map2.get("2019年01-11月环保税地方口径"));
						map3.put("1-11月合计", map2.get("1-11月合计"));
						list3.add(map3);					
					}
				}
			}
			String[] cols = { "企业名称", "2019年01-11月增值税地方口径", "2019年01-11月营业税地方口径", "2019年01-11月企业所得税地方口径",
					"2019年01-11月个人所得税地方口径", "2019年01-11月城市维护建设税地方口径", "2019年01-11月城镇土地使用税地方口径",
					"2019年01-11月印花税地方口径", "2019年01-11月房产税地方口径", "2019年01-11月车船税地方口径",
					"2019年01-11月环保税地方口径" };

			String[] keys = { "企业名称", "2019年01-11月增值税地方口径", "2019年01-11月营业税地方口径", "2019年01-11月企业所得税地方口径",
					"2019年01-11月个人所得税地方口径", "2019年01-11月城市维护建设税地方口径", "2019年01-11月城镇土地使用税地方口径",
					"2019年01-11月印花税地方口径", "2019年01-11月房产税地方口径", "2019年01-11月车船税地方口径",
					"2019年01-11月环保税地方口径" };
			FileOutputStream fileOutputStream = null;
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtil.createWorkBook(list3, keys, cols).write(os);
			fileOutputStream = new FileOutputStream(
					"D:\\test.xlsx");
			fileOutputStream.write(os.toByteArray());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("处理完成");
	}

	public String queryInit(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String dwid = this.getRequest().getSession().getAttribute("dwid").toString();
			String sql = "";
			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
			// 街道
			if ("00".equals(dwid)) {
				sql = this.getSql("jd_query");
				List<Map<String, Object>> jdlist = this.getBs().query(sql);
				map.put("jdlist", jdlist);
			} else {
				sql = "select * from dm_jd where jd_dm = '" + dwid + "'";
				List<Map<String, Object>> jdlist = this.getBs().query(sql);
				map.put("jdlist", jdlist);
			}
			// 行业
			sql = this.getSql("hy_query");
			List<Map<String, Object>> hylist = this.getBs().query(sql);
			map.put("hylist", hylist);
			return this.toJson("000", "查询成功！", map);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	public Object export(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");
			// 街道
			String sql = this.getSql("jd_query");
			List<Map<String, Object>> jdlist = this.getBs().query(sql);

			Map<String, Object> map1 = new HashMap<String, Object>();
			String[] cols = { "街道代码", "街道名称", "街道管理FW", "选用标志" };
			String[] keys = { "JD_DM", "JD_MC", "JD_GLFW", "XYBZ" };
			map1.put("fileName", "导出excel.xls");
			map1.put("cols", cols);
			map1.put("keys", keys);
			map1.put("list", jdlist);
			return map1;
			// map.put("zdsylist",zdsylist);
			// return this.toJson("000", "查询成功！",map);
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
			// 获取当前用户的街道代码
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");

			String jd = getValue(this.getForm().get("jd")).toString();// 街道
			if (!ssdw_dm.equals("00")) {
				jd = ssdw_dm;
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
			if (rs.size() != 0) {
				System.out.println(rs.get(0));

			} else {
				System.out.println("SJ_CX_NEW.QUERYBYQY201605_NEW1存储过程查询结果为空！");
			}
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
			if (sortname.equals("ZSE") && rs.size() > 1) {
				long start = System.currentTimeMillis();
				rs = Sort(rs, px);
				System.out.println("调用排序用时：" + (System.currentTimeMillis() - start) / 1000.0);
				Map<String, Object> map = rs.get(0);
				Map<String, Object> map1 = rs.get(1);
				rs.remove(0);
				rs.remove(0);
				if (getValue(map.get("JD_MC")).equals("总合计")) {
					rs.add(map1);
					rs.add(map);
				} else {
					rs.add(map);
					rs.add(map1);
				}
			}
			return this.toJson("000", "查询成功！", rs, counts);

		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	public Object exportExcl(Map<String, Object> rmap) {

		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");

			String yearNmonth = getValue(this.getForm().get("yearNmonth"));// 年月
			String jd = getValue(this.getForm().get("jdlist"));// 街道
			String type = getValue(this.getForm().get("cxls"));// 合计或月明细
			String nsName = getValue(this.getForm().get("nsName"));// 纳税人名称

			String sortname = getValue(this.getForm().get("sortname"));// 税种名字
			String px = getValue(this.getForm().get("px"));// 排序
			String qyxz = getValue(this.getForm().get("qyxz"));// 企业性质
			String islp = getValue(this.getForm().get("type1"));// 是否合伙
			String tjkj = getValue(this.getForm().get("tjkj"));// 统计口径
			String hylist = getValue(this.getForm().get("hylist"));// 行业
			String zdsyh = getValue(this.getForm().get("zdsyh"));// 重点税源

			String starTime = "";
			String endTime = "";
			String[] star = yearNmonth.split(" - ");
			if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
				starTime = star[0];
				endTime = star[1];
				starTime = starTime.substring(0, 4) + starTime.substring(5, 7);
				endTime = endTime.substring(0, 4) + endTime.substring(5, 7);
			}
			String ssdw_dm = getValue(this.getRequest().getSession().getAttribute("dwid"));

			if (!ssdw_dm.equals("00")) {
				jd = ssdw_dm;
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
			list.add(new Mode("IN", "String", "0"));// 写死
			list.add(new Mode("IN", "String", "800000"));// 写死
			list.add(new Mode("OUT", "RS", ""));
			// System.out.println(list);
			// SJ_CX_NEW.QUERYBYQY201605_NEW1
			Long start = System.currentTimeMillis();
			List<Map<String, Object>> rs = (List<Map<String, Object>>) JdbcConnectedPro
					.call("SJ_CX_NEW.QUERYBYQY201605_NEW1", list);// 调用存储过程
			System.out.println(rs.get(0));
			System.out.println("调用存储过程用时：" + (System.currentTimeMillis() - start) / 1000.0);
			List<Map<String, Object>> rss = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < rs.size(); i++) {
				Map<String, Object> maps = rs.get(i);
				String ZZS = String.valueOf(maps.get("ZZS"));
				String YGZZZS = String.valueOf(maps.get("YGZZZS"));
				String YYS = String.valueOf(maps.get("YYS"));
				String QYSDS = String.valueOf(maps.get("QYSDS"));
				String GRSDS = String.valueOf(maps.get("GRSDS"));
				String FCS = String.valueOf(maps.get("FCS"));
				String YHS = String.valueOf(maps.get("YHS"));
				String CCS = String.valueOf(maps.get("CCS"));
				String CSWHJSS = String.valueOf(maps.get("CSWHJSS"));
				String DFJYFJ = String.valueOf(maps.get("DFJYFJ"));
				String JYFJ = String.valueOf(maps.get("JYFJ"));
				String CZTDSYS = String.valueOf(maps.get("CZTDSYS"));//
				String HBS = String.valueOf(maps.get("HBS"));
				double ZZS1 = Double.parseDouble(ZZS);
				double YGZZZS1 = Double.parseDouble(YGZZZS);
				double YYS1 = Double.parseDouble(YYS);
				double QYSDS1 = Double.parseDouble(QYSDS);
				double GRSDS1 = Double.parseDouble(GRSDS);
				double FCS1 = Double.parseDouble(FCS);
				double YHS1 = Double.parseDouble(YHS);
				double CCS1 = Double.parseDouble(CCS);
				double CSWHJSS1 = Double.parseDouble(CSWHJSS);
				double DFJYFJ1 = Double.parseDouble(DFJYFJ);
				double JYFJ1 = Double.parseDouble(JYFJ);
				double CZTDSYS1 = Double.parseDouble(CZTDSYS);
				double HBS1 = Double.parseDouble(HBS);
				double hj = ZZS1 + YGZZZS1 + YYS1 + QYSDS1 + GRSDS1 + FCS1 + YHS1 + CCS1 + CSWHJSS1 + DFJYFJ1 + JYFJ1
						+ CZTDSYS1 + HBS1;
				String hj1 = formatDouble(hj);
				maps.put("HJ", hj1);
				rss.add(maps);
			}
			if (sortname.equals("ZSE")) {
				start = System.currentTimeMillis();
				rss = Sort(rss, px);
				System.out.println("调用排序用时：" + (System.currentTimeMillis() - start) / 1000.0);
				Map<String, Object> map = rss.get(0);
				Map<String, Object> map1 = rss.get(1);
				rss.remove(0);
				rss.remove(0);
				if (getValue(map.get("JD_MC")).equals("总合计")) {
					rss.add(map1);
					rss.add(map);
				} else {
					rss.add(map);
					rss.add(map1);
				}
			}

			Map<String, Object> map = new HashMap<String, Object>();
			String[] cols = { "所属街道", "纳税人名称", "增值税", "营改增增值税", "营业税", "企业所得税", "个人所得税", "房产税", "印花税", "车船税", "城市维护建设税",
					"地方教育附加", "教育费附加", "城镇土地使用税", "环保税", "合计" };

			String[] keys = { "JD_MC", "NSRMC", "ZZS", "YGZZZS", "YYS", "QYSDS", "GRSDS", "FCS", "YHS", "CCS",
					"CSWHJSS", "DFJYFJ", "JYFJ", "CZTDSYS", "HBS", "HJ" };
			map.put("fileName", "导出excel.xls");
			map.put("cols", cols);
			map.put("keys", keys);
			map.put("list", rss);

			return map;

		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	private List<Map<String, Object>> Sort(List<Map<String, Object>> list, String px) {
		System.out.println(px);
		if (px.equals("ASC")) {
			Collections.sort(list, new Comparator<Map<String, Object>>() {
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					Double name1 = Double.valueOf(o1.get("HJ").toString().replace(",", "").replace("\"", ""));// name1是从你list里面拿出来的一个
					Double name2 = Double.valueOf(o2.get("HJ").toString().replace(",", "").replace("\"", "")); // name1是从你list里面拿出来的第二个name
					return name1.compareTo(name2);
				}
			});
		} else {
			Collections.sort(list, new Comparator<Map<String, Object>>() {
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					Double name1 = Double.valueOf(o1.get("HJ").toString().replace(",", "").replace("\"", ""));// name1是从你list里面拿出来的一个
					Double name2 = Double.valueOf(o2.get("HJ").toString().replace(",", "").replace("\"", "")); // name1是从你list里面拿出来的第二个name
					return name2.compareTo(name1);
				}
			});
		}

		return list;
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
