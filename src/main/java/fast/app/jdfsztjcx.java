package fast.app;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.print.DocFlavor.STRING;

import fast.main.util.Super;

public class jdfsztjcx extends Super{

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/jdfsztjcx";
		} catch (Exception e) {
			e.printStackTrace();
			return "xtgl/jdfsztjcx";
		}
	}

	//查询街道
	public String queryJD(Map<String, Object> rmap) {
		try {
			init(rmap);//初始化
			String dwid = this.getRequest().getSession().getAttribute("dwid").toString();
			// 街道
			String sql = "";
			List<Map<String, Object>> jdlist = null;
			if("00".equals(dwid)) {
				sql = this.getSql("jd_query");
				jdlist = this.getBs().query(sql);
			} else {
				sql = "select * from dm_jd where jd_dm = '"+dwid+"'";
				jdlist = this.getBs().query(sql);
			}
			return this.toJson("000", "查询成功!", jdlist);

		} catch (Exception e) {
			// TODO: handle exception
			return this.toJson("000", "查询失败!");

		}

	}


	// 按企业查询数据查询
	public String queryData(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String yearNmonth = getValue(this.getForm().get("date")).toString();// 年月
			String jddm  = getValue(this.getForm().get("jd"));
			String starTime = "";
			//结束时间
			String endTime = "";
			String[] star = yearNmonth.split(" - ");
			if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
				starTime = star[0];
				endTime = star[1];
				starTime = starTime.substring(0, 4) + starTime.substring(5, 7);
				endTime = endTime.substring(0, 4) + endTime.substring(5, 7);
			}
			String sql = "\r\n" + 
					"\r\n" + 
					"select jd.jd_mc JD,xx.* from dm_jd jd ,(\r\n" + 
					"select t.jd_dm JDDM,\r\n" + 
					"       sum(t.zzs * t.bl/100) \"ZZS\",\r\n" + 
					"       sum(yys * t.bl/100) \"YYS\",\r\n" + 
					"       sum(grsds * t.bl/100) \"GRSDS\",\r\n" + 
					"       sum(fcs * t.bl/100) \"FCS\",\r\n" + 
					"       sum(yhs * t.bl/100) \"YHS\",\r\n" + 
					"       sum(ccs * t.bl/100) \"CCS\",\r\n" + 
					"       sum(qysds * t.bl/100) \"QYSDS\",\r\n" + 
					"       sum(ygzzzs * t.bl/100) \"YGZZZS\",\r\n" + 
					"       sum(cswhjss * t.bl/100) \"CSWHJSS\",\r\n" + 
					"       sum(dfjyfj * t.bl/100) \"DFJYFJ\",\r\n" + 
					"       sum(jyfj * t.bl/100) \"JYFFJ\",\r\n" + 
					"       sum(CZTDSYS * t.bl/100) \"CZTDSYS\",\r\n" + 
					"       sum(HBS * t.bl/100) \"HBS\"\r\n" + 
					"       \r\n" + 
					"  from sb_nsrxx t\r\n" + 
					" where t.rk_rq >= to_date('"+starTime+"','yyyyMM')\r\n" + 
					"and t.rk_rq <= to_date('"+endTime+"','yyyyMM')\r\n" + 
					" group by t.jd_dm) xx where xx.JDDM = jd.jd_dm ";

			if(yearNmonth != null && yearNmonth != "" && jddm != "" && jddm != null) {
				sql = sql +"and xx.JDDM= '"+jddm+"'";
			}
			System.out.println(sql);
			List<Map<String, Object>> list = this.getBs().query(sql);
			return this.toJson("000", "查询成功！",list);

		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}


	public Object export(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String yearNmonth = getValue(this.getForm().get("date")).toString();// 年月
			String jddm  = getValue(this.getForm().get("jd"));
			String starTime = "";
			//结束时间
			String endTime = "";
			String[] star = yearNmonth.split(" - ");
			if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
				starTime = star[0];
				endTime = star[1];
				starTime = starTime.substring(0, 4) + starTime.substring(5, 7);
				endTime = endTime.substring(0, 4) + endTime.substring(5, 7);
			}

			String sql = "\r\n" + 
					"\r\n" + 
					"select jd.jd_mc JD,xx.* from dm_jd jd ,(\r\n" + 
					"select t.jd_dm JDDM,\r\n" + 
					"       sum(t.zzs * t.bl/100) \"ZZS\",\r\n" + 
					"       sum(yys * t.bl/100) \"YYS\",\r\n" + 
					"       sum(grsds * t.bl/100) \"GRSDS\",\r\n" + 
					"       sum(fcs * t.bl/100) \"FCS\",\r\n" + 
					"       sum(yhs * t.bl/100) \"YHS\",\r\n" + 
					"       sum(ccs * t.bl/100) \"CCS\",\r\n" + 
					"       sum(qysds * t.bl/100) \"QYSDS\",\r\n" + 
					"       sum(ygzzzs * t.bl/100) \"YGZZZS\",\r\n" + 
					"       sum(cswhjss * t.bl/100) \"CSWHJSS\",\r\n" + 
					"       sum(dfjyfj * t.bl/100) \"DFJYFJ\",\r\n" + 
					"       sum(jyfj * t.bl/100) \"JYFFJ\",\r\n" + 
					"       sum(CZTDSYS * t.bl/100) \"CZTDSYS\",\r\n" + 
					"       sum(HBS * t.bl/100) \"HBS\"\r\n" + 
					"       \r\n" + 
					"  from sb_nsrxx t\r\n" + 
					" where t.rk_rq >= to_date('"+starTime+"','yyyyMM')\r\n" + 
					"and t.rk_rq <= to_date('"+endTime+"','yyyyMM')\r\n" + 
					" group by t.jd_dm) xx where xx.JDDM = jd.jd_dm ";

			if(yearNmonth != null && yearNmonth != "" && jddm != "" && jddm != null) {
				sql = sql +"and xx.JDDM= '"+jddm+"'";
			}
			System.out.println(sql);

			List<Map<String, Object>> list = this.getBs().query(sql);
			for(int i=0;i<list.size();i++){

				BigDecimal aa =(BigDecimal) list.get(i).get("ZZS");

				aa =aa.add((BigDecimal) list.get(i).get("YYS")).add((BigDecimal) list.get(i).get("GRSDS")).add((BigDecimal) list.get(i).get("FCS"))
						.add((BigDecimal) list.get(i).get("YHS")).add((BigDecimal) list.get(i).get("CCS")).add((BigDecimal) list.get(i).get("QYSDS"))
						.add((BigDecimal) list.get(i).get("YGZZZS")).add((BigDecimal) list.get(i).get("CSWHJSS")).add((BigDecimal) list.get(i).get("DFJYFJ"))
						.add((BigDecimal) list.get(i).get("JYFFJ")).add((BigDecimal) list.get(i).get("CZTDSYS")).add((BigDecimal) list.get(i).get("HBS"));
				System.out.println("我是aa啊:"+aa);
				list.get(i).put("ZJ", aa);
			}

			Map<String, Object> map1 = new HashMap<String, Object>();
			String[] cols = { "街道", "街道代码", "增值税", "营业税" , "个人所得税" , "房产税" , "印花税" , "车船税" , "企业所得税" , "营改增增值税", "城市维护建设税", "地方教育附加", "教育费附加", "城镇土地使用税","环保税","总计" };
			String[] keys = { "JD", "JDDM", "ZZS", "YYS" , "GRSDS", "FCS" , "YHS" , "CCS" , "QYSDS" , "YGZZZS" , "CSWHJSS" , "DFJYFJ" , "JYFFJ" , "CZTDSYS" , "HBS"  , "ZJ"  };
			map1.put("fileName", "街道分税种统计查询.xls");
			map1.put("cols", cols);
			map1.put("keys", keys);
			map1.put("list", list);
			return map1;
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

}
