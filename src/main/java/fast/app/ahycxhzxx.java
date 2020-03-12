package fast.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

public class ahycxhzxx extends Super {

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/ahycxhzxx";
		} catch (Exception e) {
			e.printStackTrace();
			return "xtgl/ahycxhzxx";
		}
	}

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

	// 分税种查询
	public String queryData(Map<String, Object> rmap) {
		Connection connection = null;
		try {
			// 初始化
			// bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");

			String yearNmonth = getValue(this.getForm().get("date")).toString();// 年月
			String starTime = "";
			String endTime = "";
			String jnstarTime = "";
			String jnendTime = "";
			String[] star = yearNmonth.split(" - ");
			if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
				starTime = star[0];
				endTime = star[1];
				jnstarTime = starTime.substring(0, 4) + starTime.substring(5, 7);

				jnendTime = endTime.substring(0, 4) + endTime.substring(5, 7);
			}
			String qnqsrq = (Integer.parseInt(starTime.substring(0, 4)) - 1) + starTime.substring(4);
			qnqsrq = qnqsrq.substring(0, 4) + qnqsrq.substring(5, 7);
			String qnjzrq = (Integer.parseInt(endTime.substring(0, 4)) - 1) + endTime.substring(4);
			qnjzrq = qnjzrq.substring(0, 4) + qnjzrq.substring(5, 7);
			String sql = this.getSql("hy_query");
			List<Map<String, Object>> hylist = this.getBs().query(sql);
			String tjkj = getValue(this.getForm().get("tjkj")).toString();// 统计口径
			List<Map<String, Object>> relist = new ArrayList<Map<String, Object>>();
			DecimalFormat df = new DecimalFormat("######0.00");
			connection=JdbcConnectedPro.getConnection();
				List<Mode> list = new ArrayList<Mode>();
				list.add(new Mode("IN", "String", ""));// 纳税人名字
				list.add(new Mode("IN", "String", jnstarTime));// 时间起
				list.add(new Mode("IN", "String", jnendTime));// 时间止
				list.add(new Mode("IN", "String", "0"));// 是否按月合计
				list.add(new Mode("IN", "String", "%"));// 企业性质
				list.add(new Mode("IN", "String", "%"));// 行业代码
				list.add(new Mode("IN", "String", "1"));// 是否合伙
				list.add(new Mode("IN", "String", "ZSE"));// 税种名字
				list.add(new Mode("IN", "String", "DESC"));// 排序方式
				list.add(new Mode("IN", "String", tjkj));// 统计口径
				list.add(new Mode("IN", "String", "%"));// 重点税源户代码
				list.add(new Mode("IN", "String", "0"));// 写死
				list.add(new Mode("IN", "String", "100"));// 写死
				list.add(new Mode("OUT", "RS", ""));
				List<Map<String, Object>> rs = (List<Map<String, Object>>) JdbcConnectedPro
						.call("SJ_CX_2012.querybyqyhyhz", list,connection);// 调用存储过程
				List<Mode> listqn = new ArrayList<Mode>();
				listqn.add(new Mode("IN", "String", ""));// 纳税人名称
				listqn.add(new Mode("IN", "String", qnqsrq));// 去年时间起
				listqn.add(new Mode("IN", "String", qnjzrq));// 去年时间止
				listqn.add(new Mode("IN", "String", "0"));// 按月合计
				listqn.add(new Mode("IN", "String", "%"));// 企业性质
				listqn.add(new Mode("IN", "String", "%"));// 行业代码
				listqn.add(new Mode("IN", "String", "1"));// 合伙企业
				listqn.add(new Mode("IN", "String", "ZSE"));// 排序名称
				listqn.add(new Mode("IN", "String", "DESC"));// 排序方式
				listqn.add(new Mode("IN", "String", tjkj));// 统计口径
				listqn.add(new Mode("IN", "String", "%"));// 重点税源户
				listqn.add(new Mode("IN", "String", "0"));// 0
				listqn.add(new Mode("IN", "String", "100"));// 0
				listqn.add(new Mode("OUT", "RS", ""));
				// SJ_CX_NEW.QUERYBYQY_NEW1 调用的存储过程
				List<Map<String, Object>> qnrs = (List<Map<String, Object>>) JdbcConnectedPro
						.call("SJ_CX_2012.querybyqyhyhz", listqn);// 调用存储过程

				for (int i = 0; i < rs.size(); i++) {
					// 获取结果集的第一条，并且把上面的行业mc放到结果集里的行业名称
					Map<String, Object> mmap = rs.get(i);
					mmap.put("HY_MC", rs.get(i).get("NSRMC"));
					for (int j = 0; j < qnrs.size(); j++) {
						if (getValue(mmap.get("HY_MC")).equals(getValue(qnrs.get(j).get("NSRMC")))) {
							mmap.put("QN_ZZS", qnrs.get(j).get("ZZS"));
							mmap.put("QN_YGZZZS", qnrs.get(j).get("YGZZZS"));
							mmap.put("QN_YYS", qnrs.get(j).get("YYS"));
							mmap.put("QN_QYSDS_GS", qnrs.get(j).get("QYSDS_GS"));
							mmap.put("QN_QYSDS_DS", qnrs.get(j).get("QYSDS_DS"));
							mmap.put("QN_GRSDS", qnrs.get(j).get("GRSDS"));
							mmap.put("QN_FCS", qnrs.get(j).get("FCS"));
							mmap.put("QN_YHS", qnrs.get(j).get("YHS"));
							mmap.put("QN_CCS", qnrs.get(j).get("CCS"));
							mmap.put("QN_CSWHJSS", qnrs.get(j).get("CSWHJSS"));
							mmap.put("QN_DFJYFJ", qnrs.get(j).get("DFJYFJ"));
							mmap.put("QN_JYFJ", qnrs.get(j).get("JYFJ"));
							mmap.put("QN_HJ", qnrs.get(j).get("HJ"));
							Double hj = Double.parseDouble(String.valueOf(mmap.get("HJ")));
							Double hj1 = Double.parseDouble(String.valueOf(mmap.get("QN_HJ")));
							Double zje = hj - hj1;
							Double tb = zje / hj1 * 100;
							if (hj1 != null && hj1 != 0) {
								mmap.put("TB", df.format(tb) + "%");
							} else {
								mmap.put("TB", "-");
							}
							mmap.put("ZJE", df.format(zje));
							System.out.println(mmap);
							break;
						}					
					}
					relist.add(mmap);
				}
				//将合计移到最下面
				Map<String, Object> mmap = relist.get(1);
				relist.remove(relist.get(1));
				relist.add(mmap);
				mmap = relist.get(0);
				relist.remove(relist.get(0));
				relist.add(mmap);
			int count = relist.size();
			return this.toJson("000", "查询成功！", relist, count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}finally {
			JdbcConnectedPro.close();
			if (connection!=null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 分税种查询
	public Object exportData(Map<String, Object> rmap) {
		Connection connection = null;
		try {
			// 初始化
			// bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");
			String yearNmonth = getValue(this.getForm().get("yearNmonth")).toString();// 年月
			String starTime = "";
			String endTime = "";
			String jnstarTime = "";
			String jnendTime = "";
			String[] star = yearNmonth.split(" - ");
			if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
				starTime = star[0];
				endTime = star[1];
				jnstarTime = starTime.substring(0, 4) + starTime.substring(5, 7);

				jnendTime = endTime.substring(0, 4) + endTime.substring(5, 7);
			}
			String qnqsrq = (Integer.parseInt(starTime.substring(0, 4)) - 1) + starTime.substring(4);
			qnqsrq = qnqsrq.substring(0, 4) + qnqsrq.substring(5, 7);
			String qnjzrq = (Integer.parseInt(endTime.substring(0, 4)) - 1) + endTime.substring(4);
			qnjzrq = qnjzrq.substring(0, 4) + qnjzrq.substring(5, 7);
			String sql = this.getSql("hy_query");
			List<Map<String, Object>> hylist = this.getBs().query(sql);
			String tjkj = getValue(this.getForm().get("tjkj")).toString();// 统计口径
			List<Map<String, Object>> relist = new ArrayList<Map<String, Object>>();
			DecimalFormat df = new DecimalFormat("######0.00");
			connection=JdbcConnectedPro.getConnection();
				List<Mode> list = new ArrayList<Mode>();
				list.add(new Mode("IN", "String", ""));// 纳税人名字
				list.add(new Mode("IN", "String", jnstarTime));// 时间起
				list.add(new Mode("IN", "String", jnendTime));// 时间止
				list.add(new Mode("IN", "String", "0"));// 是否按月合计
				list.add(new Mode("IN", "String", "%"));// 企业性质
				list.add(new Mode("IN", "String", "%"));// 行业代码
				list.add(new Mode("IN", "String", "1"));// 是否合伙
				list.add(new Mode("IN", "String", "ZSE"));// 税种名字
				list.add(new Mode("IN", "String", "DESC"));// 排序方式
				list.add(new Mode("IN", "String", tjkj));// 统计口径
				list.add(new Mode("IN", "String", "%"));// 重点税源户代码
				list.add(new Mode("IN", "String", "0"));// 写死
				list.add(new Mode("IN", "String", "100"));// 写死
				list.add(new Mode("OUT", "RS", ""));
				List<Map<String, Object>> rs = (List<Map<String, Object>>) JdbcConnectedPro
						.call("SJ_CX_2012.querybyqyhyhz", list,connection);// 调用存储过程
				List<Mode> listqn = new ArrayList<Mode>();
				listqn.add(new Mode("IN", "String", ""));// 纳税人名称
				listqn.add(new Mode("IN", "String", qnqsrq));// 去年时间起
				listqn.add(new Mode("IN", "String", qnjzrq));// 去年时间止
				listqn.add(new Mode("IN", "String", "0"));// 按月合计
				listqn.add(new Mode("IN", "String", "%"));// 企业性质
				listqn.add(new Mode("IN", "String", "%"));// 行业代码
				listqn.add(new Mode("IN", "String", "1"));// 合伙企业
				listqn.add(new Mode("IN", "String", "ZSE"));// 排序名称
				listqn.add(new Mode("IN", "String", "DESC"));// 排序方式
				listqn.add(new Mode("IN", "String", tjkj));// 统计口径
				listqn.add(new Mode("IN", "String", "%"));// 重点税源户
				listqn.add(new Mode("IN", "String", "0"));// 0
				listqn.add(new Mode("IN", "String", "100"));// 0
				listqn.add(new Mode("OUT", "RS", ""));
				// SJ_CX_NEW.QUERYBYQY_NEW1 调用的存储过程
				List<Map<String, Object>> qnrs = (List<Map<String, Object>>) JdbcConnectedPro
						.call("SJ_CX_2012.querybyqyhyhz", listqn);// 调用存储过程

				for (int i = 0; i < rs.size(); i++) {
					// 获取结果集的第一条，并且把上面的行业mc放到结果集里的行业名称
					Map<String, Object> mmap = rs.get(i);
					mmap.put("HY_MC", rs.get(i).get("NSRMC"));
					for (int j = 0; j < qnrs.size(); j++) {
						if (getValue(mmap.get("HY_MC")).equals(getValue(qnrs.get(j).get("NSRMC")))) {
							mmap.put("QN_ZZS", qnrs.get(j).get("ZZS"));
							mmap.put("QN_YGZZZS", qnrs.get(j).get("YGZZZS"));
							mmap.put("QN_YYS", qnrs.get(j).get("YYS"));
							mmap.put("QN_QYSDS_GS", qnrs.get(j).get("QYSDS_GS"));
							mmap.put("QN_QYSDS_DS", qnrs.get(j).get("QYSDS_DS"));
							mmap.put("QN_GRSDS", qnrs.get(j).get("GRSDS"));
							mmap.put("QN_FCS", qnrs.get(j).get("FCS"));
							mmap.put("QN_YHS", qnrs.get(j).get("YHS"));
							mmap.put("QN_CCS", qnrs.get(j).get("CCS"));
							mmap.put("QN_CSWHJSS", qnrs.get(j).get("CSWHJSS"));
							mmap.put("QN_DFJYFJ", qnrs.get(j).get("DFJYFJ"));
							mmap.put("QN_JYFJ", qnrs.get(j).get("JYFJ"));
							mmap.put("QN_HJ", qnrs.get(j).get("HJ"));
							Double hj = Double.parseDouble(String.valueOf(mmap.get("HJ")));
							Double hj1 = Double.parseDouble(String.valueOf(mmap.get("QN_HJ")));
							Double zje = hj - hj1;
							Double tb = zje / hj1 * 100;
							if (hj1 != null && hj1 != 0) {
								mmap.put("TB", df.format(tb) + "%");
							} else {
								mmap.put("TB", "-");
							}
							mmap.put("ZJE", df.format(zje));
							System.out.println(mmap);
							break;
						}					
					}
					relist.add(mmap);
				}
				//将合计移到最下面
				Map<String, Object> mmap = relist.get(1);
				relist.remove(relist.get(1));
				relist.add(mmap);
				mmap = relist.get(0);
				relist.remove(relist.get(0));
				relist.add(mmap);
			Map<String, Object> map = new HashMap<String, Object>();
			String[] cols = { "行业名称", "增值税", "营改增增值税", "营业税", "企业所得税（国税）", "企业所得税（地税）", "个人所得税", "房产税", "印花税", "车船税",
					"城市维护建设税", "地方教育附加", "教育费附加", "合计", "增值税（上年）", "营改增增值税（上年）", "营业税（上年）", "企业所得税（国税）（上年）",
					"企业所得税（地税）（上年）", "个人所得税（上年）", "房产税（上年）", "印花税（上年）", "车船税（上年）", "城市维护建设税（上年）", "地方教育附加（上年）",
					"教育费附加（上年）", "合计（上年）", "合计同比", "增减额" };
			String[] keys = { "HY_MC", "ZZS", "YGZZZS", "YYS", "QYSDS_GS", "QYSDS_DS", "GRSDS", "FCS", "YHS", "CCS",
					"CSWHJSS", "DFJYFJ", "JYFJ", "HJ", "QN_ZZS", "QN_YGZZZS", "QN_YYS", "QN_QYSDS_GS", "QN_QYSDS_DS",
					"QN_GRSDS", "QN_FCS", "QN_YHS", "QN_CCS", "QN_CSWHJSS", "QN_DFJYFJ", "QN_JYFJ", "QN_HJ", "TB",
					"ZJE" };
			map.put("fileName", "导出excel.xlsx");
			map.put("cols", cols);
			map.put("keys", keys);
			map.put("list", relist);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

}
