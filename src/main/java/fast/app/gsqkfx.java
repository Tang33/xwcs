package fast.app;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.Super;

public class gsqkfx extends Super{
	
	
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/gsqkfx";
		} catch (Exception e) {
			e.printStackTrace();
			return "xtgl/gsqkfx";
		}
	}
	
	//查询总户数
	public String querySwztqk(Map<String,Object> rmap) {
		initMap(rmap);
		List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
		try {
			
			String sql = "select kzztlx name,zhs value from SWZTQK";
			List<Map<String, Object>> list = this.getBs().query(sql);
			
			Map<String, String> map;
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, String>();
				for (String key : list.get(i).keySet()) {
					map.put(key.toLowerCase(), list.get(i).get(key).toString());
				}
				list1.add(map);
			}
			
			return this.toJson("000", "查询成功!!",list1);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败!!");
		}
	}
	
		//查询总户数
		public String queryDJZCLX(Map<String,Object> rmap) {
			initMap(rmap);
			List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
			try {
				
				String sql = "select kzztlx name,zhs value from SWZTQK";
				List<Map<String, Object>> list = this.getBs().query(sql);
				
				Map<String, String> map;
				for (int i = 0; i < list.size(); i++) {
					map = new HashMap<String, String>();
					for (String key : list.get(i).keySet()) {
						map.put(key.toLowerCase(), list.get(i).get(key).toString());
					}
					list1.add(map);
				}
				
				return this.toJson("000", "查询成功!!",list1);
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败!!");
			}
		}
	
	
		//查询详情
		public String querySwztqkDetail(Map<String,Object> rmap) {
			initMap(rmap);
			try {
				
				String sql = "select * from SWZTQK";
				List<Map<String, Object>> list = this.getBs().query(sql);
				
				
				return this.toJson("000", "查询成功!!",list);
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败!!");
			}
		}
		
	
	//查询税务登记数据详情
	public String querySWDJXQ(Map<String,Object> rmap) {
		initMap(rmap);
		try {
			//查询总户数
			String sumSql = "select count(*) sumCounts from SWDJXX where 1=1";
			System.out.println("sumSql:"+sumSql);
			List<Map<String, Object>> count1 = this.getBs().query(sumSql);
			int sumCounts=Integer.parseInt(getValue(count1.get(0).get("SUMCOUNTS")));
			
			//查询单位纳税人总户数
			String dwSql = "select count(*) dwCounts from SWDJXX where KZZTDJLX LIKE '%单位%'";
			System.out.println("dwSql:"+dwSql);
			List<Map<String, Object>> count2 = this.getBs().query(dwSql);
			Integer dwCounts= Integer.parseInt(getValue(count2.get(0).get("DWCOUNTS")));
			
			//查询个人纳税人总户数
			String gtSql = "select count(*) grCounts from SWDJXX where KZZTDJLX LIKE '%个体%'";
			System.out.println("gtSql:"+gtSql);
			List<Map<String, Object>> count3 = this.getBs().query(gtSql);
			Integer gtCounts= Integer.parseInt(getValue(count3.get(0).get("GRCOUNTS")));
			//其他纳税人总户数
			int qtCounts = sumCounts - (dwCounts + gtCounts);
			
			
			
			
			//总体见税户数
			String sumSql1 = "SELECT count(*) sumjshs from (select n.nsrmc FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX) dw  where n.nsrmc = dw.NSRMC)";
			List<Map<String, Object>> count11 = this.getBs().query(sumSql1);
			Integer sumCounts1= Integer.parseInt(getValue(count11.get(0).get("SUMJSHS")));
			
			
			//单位见税户数
			String dwSql1 = "SELECT count(*) dwjshs from (select n.nsrmc FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX LIKE '%单位%') dw  where n.nsrmc = dw.NSRMC)";
			List<Map<String, Object>> count22 = this.getBs().query(dwSql1);
			Integer dwCounts1= Integer.parseInt(getValue(count22.get(0).get("DWJSHS")));
			
			//个体见税户数
			String gtSql1 = "SELECT count(*) gtjshs from (select n.nsrmc FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX LIKE '%个体%') dw  where n.nsrmc = dw.NSRMC)";
			List<Map<String, Object>> count33 = this.getBs().query(gtSql1);
			Integer gtCounts1= Integer.parseInt(getValue(count33.get(0).get("GTJSHS")));
			
			
			//其他见税户数
			int qtCounts1 = sumCounts1 - (dwCounts1 + gtCounts1);
			
			// 创建一个数值格式化对象   
			NumberFormat numberFormat = NumberFormat.getInstance();   
			// 设置精确到小数点后2位   
			numberFormat.setMaximumFractionDigits(4);   
			//单位占比
			String dwzb = numberFormat.format((float)dwCounts1/(float)sumCounts1*100);
			//个体占比
			String gtzb = numberFormat.format((float)gtCounts1/(float)sumCounts1*100);
			//其他占比
			String qtzb = numberFormat.format((float)qtCounts1/(float)sumCounts1*100);
			
			
			//今年税收总金额
			String sumSql2 = "select sum(n.dfzse) jnssje FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX) dw  where n.nsrmc = dw.NSRMC";
			List<Map<String, Object>> sumSSJE = this.getBs().query(sumSql2);
			Double sumSSJE1= Double.parseDouble(getValue(sumSSJE.get(0).get("JNSSJE")));
			BigDecimal bd1 = new BigDecimal(sumSSJE1);
			bd1 = bd1.setScale(2, BigDecimal.ROUND_HALF_UP);
		    System.out.println(bd1.toPlainString());
		    
			//今年单位税收金额
			String dwSql3 = "select sum(n.dfzse) dwssje FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX LIKE '%单位%') dw  where n.nsrmc = dw.NSRMC";
			List<Map<String, Object>> dwSSJE = this.getBs().query(dwSql3);
			Double dwSSJE1= Double.parseDouble(getValue(dwSSJE.get(0).get("DWSSJE")));
			BigDecimal bd2 = new BigDecimal(dwSSJE1);
			bd2 = bd2.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//今年个体税收金额
			String gtSql3 = "select sum(n.dfzse) gtssje FROM SB_NSRXX n,(select DISTINCT NSRMC from SWDJXX where KZZTDJLX LIKE '%个体%') dw  where n.nsrmc = dw.NSRMC";
			List<Map<String, Object>> gtSSJE = this.getBs().query(gtSql3);
			Double gtSSJE1= Double.parseDouble(getValue(gtSSJE.get(0).get("GTSSJE")));
			BigDecimal bd3 = new BigDecimal(gtSSJE1);
			bd3 = bd3.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//今年其他税收金额
			double qtSSJE = sumSSJE1 - (dwSSJE1 + gtSSJE1);
			BigDecimal bd4 = new BigDecimal(qtSSJE);
			bd4 = bd4.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			
			//单位税收金额占比
			Double dwssjezb1 =  (double) (dwSSJE1 / sumSSJE1);
			BigDecimal bd5 = new BigDecimal(dwssjezb1);
			bd5 = bd5.setScale(4, BigDecimal.ROUND_HALF_UP);
			//个体税收金额占比
			Double gtssjezb =  (double) (gtSSJE1 / sumSSJE1);
			BigDecimal bd6 = new BigDecimal(gtssjezb);
			bd6 = bd6.setScale(4, BigDecimal.ROUND_HALF_UP);
			//其他税收金额占比
			Double qtssjezb =  (double) (qtSSJE / sumSSJE1);
			BigDecimal bd7 = new BigDecimal(qtssjezb);
			bd7 = bd7.setScale(4, BigDecimal.ROUND_HALF_UP);
			
			String insertDW = "INSERT INTO SWZTQK(KZZTLX,ZHS,JSHS,JSHSZB,JNSSJE,JNSSZB)VALUES('单位纳税人税务登记','"+dwCounts+"','"+dwCounts1+"','"+dwzb+"','"+bd2+"','"+bd5+"')";
			String insertGT = "INSERT INTO SWZTQK(KZZTLX,ZHS,JSHS,JSHSZB,JNSSJE,JNSSZB)VALUES('个体纳税人税务登记','"+gtCounts+"','"+gtCounts1+"','"+gtzb+"','"+bd3+"','"+bd6+"')";
			String insertQT = "INSERT INTO SWZTQK(KZZTLX,ZHS,JSHS,JSHSZB,JNSSJE,JNSSZB)VALUES('其他纳税人税务登记','"+qtCounts+"','"+qtCounts1+"','"+qtzb+"','"+bd4+"','"+bd7+"')";
			
			this.getBs().insert(insertDW);
			this.getBs().insert(insertGT);
			this.getBs().insert(insertQT);
			
			return this.toJson("000", "插入数据成功!!");
			
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "插入数据失败!!");
		}
	}
		
}
