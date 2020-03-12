package fast.app;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils.Null;

import fast.main.util.Super;

public class insertSWXX extends Super{
	
	
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/syqkfx";
		} catch (Exception e) {
			e.printStackTrace();
			return "xtgl/syqkfx";
		}
	}
	
	
	//将2014年之前的所有纳税人存储到NSRXX_2014中
	public String inset2014(Map<String,Object> rmap) {
		initMap(rmap);
		try {
			String sql = "insert into NSRXX_2014 select * from(\r\n" + 
					"SELECT DISTINCT NSRMC,to_char(TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy') djrq  from  SWDJXX dj where  TO_CHAR (TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy')<=2014)\r\n" + 
					"";
			
			Integer result = this.getBs().insert(sql);
			if (result>0) {
				return this.toJson("000", "插入数据成功!!");
				
			}
			return this.toJson("003", "插入数据异常!!");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "插入数据失败!!");
		}
	}
	
	
	//将2015年的所有纳税人存储到NSRXX_2015中
		public String inset2015(Map<String,Object> rmap) {
			initMap(rmap);
			try {
				String sql = "insert into NSRXX_2015 select * from(\r\n" + 
						"SELECT DISTINCT NSRMC,to_char(TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy') djrq  from  SWDJXX dj where  TO_CHAR (TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy')=2015)\r\n" + 
						"";
				
				Integer result = this.getBs().insert(sql);
				if (result>0) {
					return this.toJson("000", "插入数据成功!!");
					
				}
				return this.toJson("003", "插入数据异常!!");
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "插入数据失败!!");
			}
		}
		
		//将2016年之前的所有纳税人存储到NSRXX_2016中
		public String inset2016(Map<String,Object> rmap) {
			initMap(rmap);
			try {
				String sql = "insert into NSRXX_2016 select * from(\r\n" + 
						"SELECT DISTINCT NSRMC,to_char(TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy') djrq  from  SWDJXX dj where  TO_CHAR (TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy')=2016)\r\n" + 
						"";
				
				Integer result = this.getBs().insert(sql);
				if (result>0) {
					return this.toJson("000", "插入数据成功!!");
					
				}
				return this.toJson("003", "插入数据异常!!");
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "插入数据失败!!");
			}
		}
		
		
		//将2017年的所有纳税人存储到NSRXX_2017中
		public String inset2017(Map<String,Object> rmap) {
			initMap(rmap);
			try {
				String sql = "insert into NSRXX_2017 select * from(\r\n" + 
						"SELECT DISTINCT NSRMC,to_char(TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy') djrq  from  SWDJXX dj where  TO_CHAR (TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy')=2017)\r\n" + 
						"";
				
				Integer result = this.getBs().insert(sql);
				if (result>0) {
					return this.toJson("000", "插入数据成功!!");
					
				}
				return this.toJson("003", "插入数据异常!!");
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "插入数据失败!!");
			}
		}
		
		//将2018年的所有纳税人存储到NSRXX_2018中
		public String inset2018(Map<String,Object> rmap) {
			initMap(rmap);
			try {
				String sql = "insert into NSRXX_2018 select * from(\r\n" + 
						"SELECT DISTINCT NSRMC,to_char(TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy') djrq  from  SWDJXX dj where  TO_CHAR (TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy')=2018)\r\n" + 
						"";
				
				Integer result = this.getBs().insert(sql);
				if (result>0) {
					return this.toJson("000", "插入数据成功!!");
					
				}
				return this.toJson("003", "插入数据异常!!");
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "插入数据失败!!");
			}
		}
		
		//将2019年之前的所有纳税人存储到NSRXX_2019中
		public String inset2019(Map<String,Object> rmap) {
			initMap(rmap);
			try {
				String sql = "insert into NSRXX_2019 select * from(\r\n" + 
						"SELECT DISTINCT NSRMC,to_char(TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy') djrq  from  SWDJXX dj where  TO_CHAR (TO_DATE (DJ.DJRQ, 'yyyy-MM-dd'),'yyyy')=2019)\r\n" + 
						"";
				
				Integer result = this.getBs().insert(sql);
				if (result>0) {
					return this.toJson("000", "插入数据成功!!");
					
				}
				return this.toJson("003", "插入数据异常!!");
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "插入数据失败!!");
			}
		}
	
	
		
	
	//插入税务总体情况数据
	public String querySWDJXQ(Map<String,Object> rmap) {
		initMap(rmap);
		try {
			//当年总税额
			String sql1 = "SELECT sum(dfzse) zse from SB_NSRXX sb right JOIN (select DISTINCT to_char(RK_RQ,'yyyy') d from SB_NSRXX where to_char(RK_RQ,'yyyy') = to_char(sysdate, 'yyyy' )) dd on  TO_CHAR (RK_RQ,'yyyy') = dd.d";
			List<Map<String, Object>> zse = this.getBs().query(sql1);
			BigDecimal zse1 = new BigDecimal(getValue(zse.get(0).get("ZSE")));
			System.out.println(sql1);
			List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
			
			//税务总体情况
			//查询单位和个体数据
			String sql2 = "SELECT\r\n" + 
					" aa.*, ROUND ((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
					"FROM\r\n" + 
					" (\r\n" + 
					"select d.KZZTDJLX,count(n1) zhs,count(n2) jshs,NVL (sum(zse),0) zse from(\r\n" + 
					"\r\n" + 
					"select j.nsrmc n1,j.KZZTDJLX,ss.nsrmc n2,zse from SWDJXX j left join(\r\n" + 
					"select nsrmc,sum(t.DFZSE) zse from sb_nsrxx t GROUP BY nsrmc\r\n" + 
					") ss on j.nsrmc=ss.nsrmc) d WHERE d.KZZTDJLX LIKE '%单位%' or d.KZZTDJLX LIKE '%个体%'\r\n" + 
					"\r\n" + 
					"group by KZZTDJLX ORDER BY zhs desc) aa";
			System.out.println(sql2);
			Map<String, Object> map;
			List<Map<String, Object>> list1 = this.getBs().query(sql2);
			for (int i = 0; i < list1.size(); i++) {
				map = list1.get(i);
				BigDecimal zse2 = new BigDecimal(getValue(map.get("ZSE")));
				double result = zse2.divide(zse1,4,BigDecimal.ROUND_HALF_DOWN).doubleValue()*100;
				map.put("SSZB", result+"%");
			}
			
			//查询除单位和个体外有个课证主体
			String sql3 = "SELECT\r\n" + 
					" aa.*, ROUND ((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
					"FROM\r\n" + 
					" (\r\n" + 
					"select count(n1) zhs,count(n2) jshs,NVL (sum(zse),0) zse from(\r\n" + 
					"\r\n" + 
					"select j.nsrmc n1,j.KZZTDJLX,ss.nsrmc n2,zse from SWDJXX j left join(\r\n" + 
					"select nsrmc,sum(t.DFZSE) zse from sb_nsrxx t GROUP BY nsrmc\r\n" + 
					") ss on j.nsrmc=ss.nsrmc) d WHERE d.KZZTDJLX not LIKE '%单位%' and  d.KZZTDJLX not LIKE '%个体%'\r\n" + 
					"ORDER BY zhs desc) aa";
			List<Map<String, Object>> list2 = this.getBs().query(sql3);
			Map<String, Object> map1;
				map1  = list2.get(0);
				map1.put("KZZTDJLX", "其他纳税人税务登记");
				BigDecimal zse3 = new BigDecimal(getValue(map1.get("ZSE")));
				BigDecimal result1 = zse3.divide(zse1,4,BigDecimal.ROUND_HALF_DOWN);
				  NumberFormat percent = NumberFormat.getPercentInstance();
			       percent.setMaximumFractionDigits(2);
				
				//System.out.println(percent.format(result.doubleValue()));
			       map1.put("SSZB", percent.format(result1.doubleValue()));
			
				list2.addAll(list1);
				
				for (int i = 0; i < list2.size(); i++) {
					String insertSql ="insert into swztqk(KZZTLX,ZHS,JSHS,JSHSZB,JNSSJE,JNSSZB) "
							+ "values('"+list2.get(i).get("KZZTDJLX")+"','"+list2.get(i).get("ZHS")+"',"
									+ "'"+list2.get(i).get("JSHS")+"','"+list2.get(i).get("JSHSZB")+"',"
											+ "'"+list2.get(i).get("ZSE")+"','"+list2.get(i).get("SSZB")+"')"; 
					this.getBs().insert(insertSql);
				}
				
			return this.toJson("000", "插入数据成功!!");
			
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "插入数据失败!!");
		}
	}
		
	
	
		//插入登记注册类型详情
		public String queryDJ(Map<String,Object> rmap) {
			initMap(rmap);
			try {
				//当年总税额
				String sql1 = "SELECT sum(dfzse) zse from SB_NSRXX sb right JOIN (select DISTINCT to_char(RK_RQ,'yyyy') d from SB_NSRXX where to_char(RK_RQ,'yyyy') = to_char(sysdate, 'yyyy' )) dd on  TO_CHAR (RK_RQ,'yyyy') = dd.d";
				List<Map<String, Object>> zse = this.getBs().query(sql1);
				BigDecimal zse1 = new BigDecimal(getValue(zse.get(0).get("ZSE")));
				
				List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
				
				//税务总体情况
				//查询单位和个体数据
				String sql2 = "SELECT\r\n" + 
						" aa.*, ROUND ((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
						"FROM\r\n" + 
						" (\r\n" + 
						"select d.djzclx,count(n1) zhs,count(n2) jshs,NVL (sum(zse),0) zse from(\r\n" + 
						"\r\n" + 
						"select j.nsrmc n1,j.djzclx,ss.nsrmc n2,zse from SWDJXX j left join(\r\n" + 
						"select nsrmc,sum(t.DFZSE) zse from sb_nsrxx t  where t.RK_RQ >=(select to_date(to_char(sysdate,'yyyy')||'01','yyyymm') from dual) GROUP BY nsrmc\r\n" + 
						") ss on j.nsrmc=ss.nsrmc) d\r\n" + 
						"\r\n" + 
						"group by djzclx ORDER BY zhs desc) aa";
				Map<String, Object> map;
				List<Map<String, Object>> list1 = this.getBs().query(sql2);
				for (int i = 0; i < list1.size(); i++) {
					map = list1.get(i);
					BigDecimal zse2 = new BigDecimal(getValue(map.get("ZSE")));
					BigDecimal result = zse2.divide(zse1,4,BigDecimal.ROUND_HALF_DOWN );
					  NumberFormat percent = NumberFormat.getPercentInstance();
				       percent.setMaximumFractionDigits(2);
					
					//System.out.println(percent.format(result.doubleValue()));
					map.put("SSZB", percent.format(result.doubleValue()));
				}
					for (int i = 0; i < list1.size(); i++) {
						String insertSql ="insert into SW_DJZCLX(DJZCLX,ZHS,JSHS,JSHSZB,JNSSJE,JNSSZB) "
								+ "values('"+list1.get(i).get("DJZCLX")+"','"+list1.get(i).get("ZHS")+"',"
										+ "'"+list1.get(i).get("JSHS")+"','"+list1.get(i).get("JSHSZB")+"',"
												+ "'"+list1.get(i).get("ZSE")+"','"+list1.get(i).get("SSZB")+"')"; 
						this.getBs().insert(insertSql);
					}
					
				return this.toJson("000", "插入数据成功!!");
				
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "插入数据失败!!");
			}
		}
		
		
		
		//插入街道类型详情
		public String queryJD(Map<String,Object> rmap) {
			initMap(rmap);
			try {
				//当年总税额
				String sql1 = "SELECT sum(dfzse) zse from SB_NSRXX sb right JOIN (select DISTINCT to_char(RK_RQ,'yyyy') d from SB_NSRXX where to_char(RK_RQ,'yyyy') = to_char(sysdate, 'yyyy' )) dd on  TO_CHAR (RK_RQ,'yyyy') = dd.d";
				List<Map<String, Object>> zse = this.getBs().query(sql1);
				BigDecimal zse1 = new BigDecimal(getValue(zse.get(0).get("ZSE")));
				
				
				//税务总体情况
				//查询单位和个体数据
				String sql2 = "SELECT\r\n" + 
						" aa.*, ROUND ((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
						"FROM\r\n" + 
						" (\r\n" + 
						"select nvl(d.jd,'待定') JD,count(n1) zhs,count(n2) jshs,sum(zse) zse from(\r\n" + 
						"select DISTINCT j.jd,j.nsrmc n1,ss.nsrmc n2,zse from SWDJXX j left join(\r\n" + 
						"select DISTINCT nsrmc,t.DFZSE zse from sb_nsrxx t where exists(select * from SWDJXX s where t.nsrmc=s.nsrmc)\r\n" + 
						") ss on j.nsrmc=ss.nsrmc) d\r\n" + 
						"\r\n" + 
						"group by jd) aa\r\n" + 
						"";
				Map<String, Object> map;
				List<Map<String, Object>> list1 = this.getBs().query(sql2);
				
				List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < list1.size(); i++) {
					
					if (getValue(list1.get(i).get("JD")).indexOf("玄武") >-1 && !getValue(list1.get(i).get("JD")).contains("玄武区徐庄管委会") &&
							!getValue(list1.get(i).get("JD")).contains("玄武区紫金科创特区") && !getValue(list1.get(i).get("JD")).contains("玄武区后宰门街道") &&
							!getValue(list1.get(i).get("JD")).contains("玄武区梅园街道")) {
						map = list1.get(i);
						BigDecimal zse2 = new BigDecimal(getValue(map.get("ZSE")));
//						double result = zse2.divide(zse1,2,1).doubleValue()*100;
						BigDecimal result = zse2.divide(zse1,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map.put("SSZB", percent.format(result.doubleValue()));
						lists.add(map);
					}
				}
				
				//查询紫金科创和徐庄合并
				String sql3 = "SELECT\r\n" + 
						" aa.*, ROUND ((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
						"FROM\r\n" + 
						" (\r\n" + 
						"SELECT sum(zhs) zhs,sum(jshs) jshs ,sum(zse) zse from (\r\n" + 
						"select nvl(d.jd,'待定') JD,count(n1) zhs,count(n2) jshs,sum(zse) zse from(\r\n" + 
						"select DISTINCT j.jd,j.nsrmc n1,ss.nsrmc n2,zse from SWDJXX j left join(\r\n" + 
						"select DISTINCT nsrmc,t.DFZSE zse from sb_nsrxx t where exists(select * from SWDJXX s where t.nsrmc=s.nsrmc)\r\n" + 
						") ss on j.nsrmc=ss.nsrmc) d\r\n" + 
						"\r\n" + 
						"group by jd) bb where bb.jd like '%徐庄%' OR bb.jd like '%紫金科创%') aa";
				List<Map<String, Object>> list2 = this.getBs().query(sql3);
				Map<String, Object> map1;
					map1  = list2.get(0);
					map1.put("JD", "玄武区徐庄管委会");
					BigDecimal zse3 = new BigDecimal(getValue(map1.get("ZSE")));
					BigDecimal result1 = zse3.divide(zse1,4,BigDecimal.ROUND_HALF_DOWN);
					 NumberFormat percent = NumberFormat.getPercentInstance();
				       percent.setMaximumFractionDigits(2);
					
					//System.out.println(percent.format(result.doubleValue()));
				       map1.put("SSZB", percent.format(result1.doubleValue()));
				
					//查询梅园和后宰门合并
					String sql4 = "SELECT\r\n" + 
							" aa.*, ROUND ((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
							"FROM\r\n" + 
							" (\r\n" + 
							"SELECT sum(zhs) zhs,sum(jshs) jshs ,sum(zse) zse from (\r\n" + 
							"select nvl(d.jd,'待定') JD,count(n1) zhs,count(n2) jshs,sum(zse) zse from(\r\n" + 
							"select DISTINCT j.jd,j.nsrmc n1,ss.nsrmc n2,zse from SWDJXX j left join(\r\n" + 
							"select DISTINCT nsrmc,t.DFZSE zse from sb_nsrxx t where exists(select * from SWDJXX s where t.nsrmc=s.nsrmc)\r\n" + 
							") ss on j.nsrmc=ss.nsrmc) d\r\n" + 
							"\r\n" + 
							"group by jd) bb where bb.jd like '%梅园%' OR bb.jd like '%后宰门%') aa";
					List<Map<String, Object>> list3 = this.getBs().query(sql4);
					Map<String, Object> map2;
						map2  = list3.get(0);
						map2.put("JD", "玄武区梅园街道");
						BigDecimal zse4 = new BigDecimal(getValue(map1.get("ZSE")));
						BigDecimal result2 = zse4.divide(zse1,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent1 = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
					       map2.put("SSZB", percent1.format(result2.doubleValue()));
					
						lists.addAll(list2);
						lists.addAll(list3);
					
					for (int i = 0; i < lists.size(); i++) {
						String insertSql ="insert into SW_JDLX(JD,ZHS,JSHS,JSHSZB,JNSSJE,JNSSZB) "
								+ "values('"+lists.get(i).get("JD")+"','"+lists.get(i).get("ZHS")+"',"
										+ "'"+lists.get(i).get("JSHS")+"','"+lists.get(i).get("JSHSZB")+"',"
												+ "'"+lists.get(i).get("ZSE")+"','"+lists.get(i).get("SSZB")+"')"; 
						this.getBs().insert(insertSql);
					}
					
				return this.toJson("000", "插入数据成功!!");
				
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "插入数据失败!!");
			}
		}
		
		
		//插入行业类型详情
			public String queryHYLX(Map<String,Object> rmap) {
				initMap(rmap);
				try {
					//当年总税额
					String sql1 = "SELECT sum(dfzse) zse from SB_NSRXX sb right JOIN (select DISTINCT to_char(RK_RQ,'yyyy') d from SB_NSRXX where to_char(RK_RQ,'yyyy') = to_char(sysdate, 'yyyy' )) dd on  TO_CHAR (RK_RQ,'yyyy') = dd.d";
					List<Map<String, Object>> zse = this.getBs().query(sql1);
					BigDecimal zse1 = new BigDecimal(getValue(zse.get(0).get("ZSE")));
					//税务总体情况
					//查询单位和个体数据
					String sql2 = "SELECT\r\n" + 
							" aa.*, ROUND ((aa.jshs / aa.zhs), 2) * 100 || '%' jshszb\r\n" + 
							"FROM\r\n" + 
							" (\r\n" + 
							"select d.hy,count(n1) zhs,count(n2) jshs,sum(zse) zse from(\r\n" + 
							"select j.hy,j.nsrmc n1,ss.nsrmc n2,zse from SWDJXX j left join(\r\n" + 
							"select DISTINCT nsrmc,t.DFZSE zse from sb_nsrxx t where exists(select * from SWDJXX s where t.nsrmc=s.nsrmc)\r\n" + 
							") ss on j.nsrmc=ss.nsrmc) d\r\n" + 
							"\r\n" + 
							"group by hy) aa";
					Map<String, Object> map;
					List<Map<String, Object>> list1 = this.getBs().query(sql2);
					for (int i = 0; i < list1.size(); i++) {
						map = list1.get(i);
						if (getValue(map.get("ZSE")) == null ||getValue(map.get("ZSE")) == "") {
							map.put("ZSE", 0);
							map.put("SSZB", 0+"%");
						}else {
							BigDecimal zse2 = new BigDecimal(getValue(map.get("ZSE")));
							BigDecimal result = zse2.divide(zse1,4,BigDecimal.ROUND_HALF_DOWN);
							 NumberFormat percent = NumberFormat.getPercentInstance();
						       percent.setMaximumFractionDigits(2);
							
							//System.out.println(percent.format(result.doubleValue()));
							map.put("SSZB", percent.format(result.doubleValue()));
						}
					}
						for (int i = 0; i < list1.size(); i++) {
							String insertSql ="insert into SW_HYLX(HY,ZHS,JSHS,JSHSZB,JNSSJE,JNSSZB) "
									+ "values('"+list1.get(i).get("HY")+"','"+list1.get(i).get("ZHS")+"',"
											+ "'"+list1.get(i).get("JSHS")+"','"+list1.get(i).get("JSHSZB")+"',"
													+ "'"+list1.get(i).get("ZSE")+"','"+list1.get(i).get("SSZB")+"')"; 
							this.getBs().insert(insertSql);
						}
						
					return this.toJson("000", "插入数据成功!!");
					
				} catch (Exception e) {
					e.printStackTrace();
					return this.toJson("009", "插入数据失败!!");
				}
			}
		
		
		
		//插入登记日期详情
		public String queryDJRQ(Map<String,Object> rmap) {
			initMap(rmap);
			try {
				//当年总税额
				String sqlS = "SELECT nvl(sum(dfzse),0) s_zse from SB_NSRXX where  TO_CHAR (RK_RQ,'yyyy') = '2017'";
				List<Map<String, Object>> zse_S = this.getBs().query(sqlS);
			/*	if (zse_S == null) {
					zse_S.set(0, 0)
				}*/
				BigDecimal zseS = new BigDecimal(getValue(zse_S.get(0).get("S_ZSE")));
				
				String sqlE = "SELECT nvl(sum(dfzse),0) e_zse from SB_NSRXX where  TO_CHAR (RK_RQ,'yyyy') = '2018'";
				List<Map<String, Object>> zse_E = this.getBs().query(sqlE);
				BigDecimal zseE = new BigDecimal(getValue(zse_E.get(0).get("E_ZSE")));
				
				String sqlN = "SELECT nvl(sum(dfzse),0) n_zse from SB_NSRXX where  TO_CHAR (RK_RQ,'yyyy') = '2019'";
				List<Map<String, Object>> zse_N = this.getBs().query(sqlN);
				BigDecimal zseN = new BigDecimal(getValue(zse_N.get(0).get("N_ZSE")));
				
				//税务总体情况
				//14年
				String sql14 = "select aa.*,round((aa.jshs/aa.zhs),2)*100||'%' jshszb,round((aa.s_jshs/aa.zhs),2)*100||'%' s_jshszb\r\n" + 
						",round((aa.e_jshs/aa.zhs),2)*100||'%' e_jshszb,round((aa.n_jshs/aa.zhs),2)*100||'%' n_jshszb from \r\n" + 
						"(SELECT count(DISTINCT nsrmc) zhs,'2014' djrq,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_JSHS from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2014) bb on aa.nsrmc = bb.NSRMC) jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2017 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2014) bb on aa.nsrmc = bb.NSRMC) s_jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2018 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2014) bb on aa.nsrmc = bb.NSRMC) e_jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2019 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2014) bb on aa.nsrmc = bb.NSRMC) n_jshs,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from \r\n" + 
						"		(SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2014) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"			TO_CHAR (RK_RQ,'yyyy'\r\n" + 
						"   ) = '2017') s_ssze,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from (\r\n" + 
						"	SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2014) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"		 TO_CHAR (RK_RQ,\r\n" + 
						"			'yyyy'\r\n" + 
						"		 ) = '2018') e_ssze,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from (\r\n" + 
						"	SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2014) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"		 TO_CHAR (RK_RQ,\r\n" + 
						"			'yyyy'\r\n" + 
						"		 ) = '2019') n_ssze\r\n" + 
						" from NSRXX_2014 WHERE to_char(TO_DATE (DJRQ, 'yyyy'),'yyyy') <= 2014 GROUP BY '2014') aa";
				
				Map<String, Object> map;
				List<Map<String, Object>> list1 = this.getBs().query(sql14);
				for (int i = 0; i < list1.size(); i++) {
					map = list1.get(i);
					BigDecimal zse2 = new BigDecimal(getValue(map.get("S_SSZE")));
					BigDecimal zse3 = new BigDecimal(getValue(map.get("E_SSZE")));
					BigDecimal zse4 = new BigDecimal(getValue(map.get("N_SSZE")));
					if (zseS!=null&&zseS.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result1 = zse2.divide(zseS,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map.put("S_SSJEZB",percent.format(result1.doubleValue()));
					}else {
						map.put("S_SSJEZB", 0+"%");
					}
					if (zseE!=null&&zseE.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result2 = zse3.divide(zseE,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						
						map.put("E_SSJEZB", percent.format(result2.doubleValue()));
						
					}else {
						map.put("E_SSJEZB", 0+"%");
					}
					if (zseN!=null&&zseN.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result3 = zse4.divide(zseN,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map.put("N_SSJEZB",percent.format(result3.doubleValue()));
					}else {
						map.put("N_SSJEZB", 0+"%");
						
					}
				}
				
				//15年
				String sql15 = "select aa.*,round((aa.jshs/aa.zhs),2)*100||'%' jshszb,round((aa.s_jshs/aa.zhs),2)*100||'%' s_jshszb\r\n" + 
						",round((aa.e_jshs/aa.zhs),2)*100||'%' e_jshszb,round((aa.n_jshs/aa.zhs),2)*100||'%' n_jshszb from \r\n" + 
						"(SELECT count(DISTINCT nsrmc) zhs,'2015' djrq,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_JSHS from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2015) bb on aa.nsrmc = bb.NSRMC) jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2017 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2015) bb on aa.nsrmc = bb.NSRMC) s_jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2018 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2015) bb on aa.nsrmc = bb.NSRMC) e_jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2019 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2015) bb on aa.nsrmc = bb.NSRMC) n_jshs,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from \r\n" + 
						"		(SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2015) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"			TO_CHAR (RK_RQ,'yyyy'\r\n" + 
						"   ) = '2017') s_ssze,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from (\r\n" + 
						"	SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2015) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"		 TO_CHAR (RK_RQ,\r\n" + 
						"			'yyyy'\r\n" + 
						"		 ) = '2018') e_ssze,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from (\r\n" + 
						"	SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2015) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"		 TO_CHAR (RK_RQ,\r\n" + 
						"			'yyyy'\r\n" + 
						"		 ) = '2019') n_ssze\r\n" + 
						" from NSRXX_2015 WHERE to_char(TO_DATE (DJRQ, 'yyyy'),'yyyy') = 2015 GROUP BY '2015') aa\r\n" + 
						"";
				
				Map<String, Object> map2;
				List<Map<String, Object>> list2 = this.getBs().query(sql15);
				for (int i = 0; i < list2.size(); i++) {
					map2 = list2.get(i);
					BigDecimal zse215 = new BigDecimal(getValue(map2.get("S_SSZE")));
					BigDecimal zse315 = new BigDecimal(getValue(map2.get("E_SSZE")));
					BigDecimal zse415 = new BigDecimal(getValue(map2.get("N_SSZE")));
					
					if (zseS!=null&&zseS.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result115 = zse215.divide(zseS,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map2.put("S_SSJEZB", percent.format(result115.doubleValue()));
					}else {
						map2.put("S_SSJEZB", 0+"%");
					}
					if (zseE!=null&&zseE.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result215 = zse315.divide(zseE,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map2.put("E_SSJEZB", percent.format(result215.doubleValue()));
						
					}else {
						map2.put("E_SSJEZB", 0+"%");
					}
					if (zseN!=null&&zseN.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result315 = zse415.divide(zseN,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map2.put("N_SSJEZB", percent.format(result315.doubleValue()));
					}else {
						map2.put("N_SSJEZB", 0+"%");
						
					}
					
				}
				
				//16年
				String sql16 = "select aa.*,round((aa.jshs/aa.zhs),2)*100||'%' jshszb,round((aa.s_jshs/aa.zhs),2)*100||'%' s_jshszb\r\n" + 
						",round((aa.e_jshs/aa.zhs),2)*100||'%' e_jshszb,round((aa.n_jshs/aa.zhs),2)*100||'%' n_jshszb from \r\n" + 
						"(SELECT count(DISTINCT nsrmc) zhs,'2016' djrq,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_JSHS from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2016) bb on aa.nsrmc = bb.NSRMC) jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2017 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2016) bb on aa.nsrmc = bb.NSRMC) s_jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2018 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2016) bb on aa.nsrmc = bb.NSRMC) e_jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2019 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2016) bb on aa.nsrmc = bb.NSRMC) n_jshs,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from \r\n" + 
						"		(SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2016) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"			TO_CHAR (RK_RQ,'yyyy'\r\n" + 
						"   ) = '2017') s_ssze,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from (\r\n" + 
						"	SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2016) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"		 TO_CHAR (RK_RQ,\r\n" + 
						"			'yyyy'\r\n" + 
						"		 ) = '2018') e_ssze,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from (\r\n" + 
						"	SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2016) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"		 TO_CHAR (RK_RQ,\r\n" + 
						"			'yyyy'\r\n" + 
						"		 ) = '2019') n_ssze\r\n" + 
						" from NSRXX_2016 WHERE to_char(TO_DATE (DJRQ, 'yyyy'),'yyyy') = 2016 GROUP BY '2016') aa\r\n" + 
						"\r\n" + 
						"";
				
				Map<String, Object> map3;
				List<Map<String, Object>> list3 = this.getBs().query(sql16);
				for (int i = 0; i < list3.size(); i++) {
					map3 = list3.get(i);
					BigDecimal zse216 = new BigDecimal(getValue(map3.get("S_SSZE")));
					BigDecimal zse316 = new BigDecimal(getValue(map3.get("E_SSZE")));
					BigDecimal zse416 = new BigDecimal(getValue(map3.get("S_SSZE")));
					
					if (zseS!=null&&zseS.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result116 = zse216.divide(zseS,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map3.put("S_SSJEZB", percent.format(result116.doubleValue()));
					}else {
						map3.put("S_SSJEZB", 0+"%");
					}
					if (zseE!=null&&zseE.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result216 = zse316.divide(zseE,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map3.put("E_SSJEZB", percent.format(result216.doubleValue()));
						
					}else {
						map3.put("E_SSJEZB", 0+"%");
					}
					if (zseN!=null&&zseN.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result316 = zse416.divide(zseN,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map3.put("N_SSJEZB", percent.format(result316.doubleValue()));
					}else {
						map3.put("N_SSJEZB", 0+"%");
						
					}
					
				}
				
				
				//17年
				String sql17 = "select aa.*,round((aa.jshs/aa.zhs),2)*100||'%' jshszb,round((aa.s_jshs/aa.zhs),2)*100||'%' s_jshszb\r\n" + 
						",round((aa.e_jshs/aa.zhs),2)*100||'%' e_jshszb,round((aa.n_jshs/aa.zhs),2)*100||'%' n_jshszb from \r\n" + 
						"(SELECT count(DISTINCT nsrmc) zhs,'2017' djrq,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_JSHS from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2017) bb on aa.nsrmc = bb.NSRMC) jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2017 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2017) bb on aa.nsrmc = bb.NSRMC) s_jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2018 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2017) bb on aa.nsrmc = bb.NSRMC) e_jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2019 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2017) bb on aa.nsrmc = bb.NSRMC) n_jshs,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from \r\n" + 
						"		(SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2017) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"			TO_CHAR (RK_RQ,'yyyy'\r\n" + 
						"   ) = '2017') s_ssze,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from (\r\n" + 
						"	SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2017) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"		 TO_CHAR (RK_RQ,\r\n" + 
						"			'yyyy'\r\n" + 
						"		 ) = '2018') e_ssze,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from (\r\n" + 
						"	SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2017) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"		 TO_CHAR (RK_RQ,\r\n" + 
						"			'yyyy'\r\n" + 
						"		 ) = '2019') n_ssze\r\n" + 
						" from NSRXX_2017 WHERE to_char(TO_DATE (DJRQ, 'yyyy'),'yyyy') = 2017 GROUP BY '2017') aa";
				
				Map<String, Object> map4;
				List<Map<String, Object>> list4 = this.getBs().query(sql17);
				for (int i = 0; i < list4.size(); i++) {
					map4 = list4.get(i);
					BigDecimal zse217 = new BigDecimal(getValue(map4.get("S_SSZE")));
					BigDecimal zse317 = new BigDecimal(getValue(map4.get("E_SSZE")));
					BigDecimal zse417 = new BigDecimal(getValue(map4.get("N_SSZE")));
					
					if (zseS!=null&&zseS.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result117 = zse217.divide(zseS,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map4.put("S_SSJEZB", percent.format(result117.doubleValue()));
					}else {
						map4.put("S_SSJEZB", 0+"%");
					}
					if (zseE!=null&&zseE.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result217 = zse317.divide(zseE,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map4.put("E_SSJEZB", percent.format(result217.doubleValue()));
						
					}else {
						map4.put("E_SSJEZB", 0+"%");
					}
					if (zseN!=null&&zseN.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result317 = zse417.divide(zseN,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map4.put("N_SSJEZB", percent.format(result317.doubleValue()));
					}else {
						map4.put("N_SSJEZB", 0+"%");
						
					}
					
				}
				
				
				//18年
				String sql18 = "select aa.*,round((aa.jshs/aa.zhs),2)*100||'%' jshszb,round((aa.s_jshs/aa.zhs),2)*100||'%' s_jshszb\r\n" + 
						",round((aa.e_jshs/aa.zhs),2)*100||'%' e_jshszb,round((aa.n_jshs/aa.zhs),2)*100||'%' n_jshszb from \r\n" + 
						"(SELECT count(DISTINCT nsrmc) zhs,'2018' djrq,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_JSHS from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2018) bb on aa.nsrmc = bb.NSRMC) jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2017 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2018) bb on aa.nsrmc = bb.NSRMC) s_jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2018 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2018) bb on aa.nsrmc = bb.NSRMC) e_jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2019 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2018) bb on aa.nsrmc = bb.NSRMC) n_jshs,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from \r\n" + 
						"		(SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2018) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"			TO_CHAR (RK_RQ,'yyyy'\r\n" + 
						"   ) = '2017') s_ssze,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from (\r\n" + 
						"	SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2018) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"		 TO_CHAR (RK_RQ,\r\n" + 
						"			'yyyy'\r\n" + 
						"		 ) = '2018') e_ssze,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from (\r\n" + 
						"	SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2018) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"		 TO_CHAR (RK_RQ,\r\n" + 
						"			'yyyy'\r\n" + 
						"		 ) = '2019') n_ssze\r\n" + 
						" from NSRXX_2018 WHERE to_char(TO_DATE (DJRQ, 'yyyy'),'yyyy') = 2018 GROUP BY '2018') aa\r\n" + 
						"\r\n" + 
						"";
				
				Map<String, Object> map5;
				List<Map<String, Object>> list5 = this.getBs().query(sql18);
				for (int i = 0; i < list5.size(); i++) {
					map5 = list5.get(i);
					BigDecimal zse218 = new BigDecimal(getValue(map5.get("S_SSZE")));
					BigDecimal zse318 = new BigDecimal(getValue(map5.get("E_SSZE")));
					BigDecimal zse418 = new BigDecimal(getValue(map5.get("N_SSZE")));
					if (zseS!=null&&zseS.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result318 = zse218.divide(zseS,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map5.put("S_SSJEZB", percent.format(result318.doubleValue()));
					}else {
						map5.put("S_SSJEZB", 0+"%");
					}
					if (zseE!=null&&zseE.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result218 = zse318.divide(zseE,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map5.put("E_SSJEZB", percent.format(result218.doubleValue()));
						
					}else {
						map5.put("E_SSJEZB", 0+"%");
					}
					if (zseN!=null&&zseN.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result118 = zse418.divide(zseN,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map5.put("N_SSJEZB", percent.format(result118.doubleValue()));
					}else {
						map5.put("N_SSJEZB", 0+"%");
						
					}
					
				}
				
				
				//19年
				String sql19 = "select aa.*,round((aa.jshs/aa.zhs),2)*100||'%' jshszb,round((aa.s_jshs/aa.zhs),2)*100||'%' s_jshszb\r\n" + 
						",round((aa.e_jshs/aa.zhs),2)*100||'%' e_jshszb,round((aa.n_jshs/aa.zhs),2)*100||'%' n_jshszb from \r\n" + 
						"(SELECT count(DISTINCT nsrmc) zhs,'2019' djrq,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_JSHS from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2019) bb on aa.nsrmc = bb.NSRMC) jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2017 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2019) bb on aa.nsrmc = bb.NSRMC) s_jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2018 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2019) bb on aa.nsrmc = bb.NSRMC) e_jshs,\r\n" + 
						"	(SELECT count(DISTINCT aa.nsrmc) F_S_JSHS from NSRXX_2019 aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2019) bb on aa.nsrmc = bb.NSRMC) n_jshs,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from \r\n" + 
						"		(SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2019) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"			TO_CHAR (RK_RQ,'yyyy'\r\n" + 
						"   ) = '2017') s_ssze,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from (\r\n" + 
						"	SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2019) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"		 TO_CHAR (RK_RQ,\r\n" + 
						"			'yyyy'\r\n" + 
						"		 ) = '2018') e_ssze,\r\n" + 
						"	(SELECT nvl(sum(dfzse),0) zse from (\r\n" + 
						"	SELECT aa.nsrmc F_JSHS,dfzse,rk_rq from SB_NSRXX aa INNER JOIN (SELECT DISTINCT nsrmc from NSRXX_2019) bb on aa.nsrmc = bb.NSRMC)  WHERE\r\n" + 
						"		 TO_CHAR (RK_RQ,\r\n" + 
						"			'yyyy'\r\n" + 
						"		 ) = '2019') n_ssze\r\n" + 
						" from NSRXX_2019 WHERE to_char(TO_DATE (DJRQ, 'yyyy'),'yyyy') = 2019 GROUP BY '2019') aa\r\n" + 
						"";
				
				Map<String, Object> map6;
				List<Map<String, Object>> list6 = this.getBs().query(sql19);
				for (int i = 0; i < list6.size(); i++) {
					map6 = list6.get(i);
					BigDecimal zse219 = new BigDecimal(getValue(map6.get("S_SSZE")));
					BigDecimal zse319 = new BigDecimal(getValue(map6.get("E_SSZE")));
					BigDecimal zse419 = new BigDecimal(getValue(map6.get("N_SSZE")));
					if (zseS!=null&&zseS.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result119 = zse219.divide(zseS,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map6.put("S_SSJEZB", percent.format(result119.doubleValue()));
					}else {
						map6.put("S_SSJEZB", 0+"%");
					}
					if (zseE!=null&&zseE.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result219 = zse319.divide(zseE,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map6.put("E_SSJEZB", percent.format(result219.doubleValue()));
						
					}else {
						map6.put("E_SSJEZB", 0+"%");
					}
					if (zseN!=null&&zseN.compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal result319 = zse419.divide(zseN,4,BigDecimal.ROUND_HALF_DOWN);
						 NumberFormat percent = NumberFormat.getPercentInstance();
					       percent.setMaximumFractionDigits(2);
						
						//System.out.println(percent.format(result.doubleValue()));
						map6.put("N_SSJEZB", percent.format(result319.doubleValue()));
					}else {
						map6.put("N_SSJEZB", 0+"%");
						
					}
					
				}
				
				list1.addAll(list2);
				list1.addAll(list3);
				list1.addAll(list4);
				list1.addAll(list5);
				list1.addAll(list6);
				for (int i = 0; i < list1.size(); i++) {
					String insertSql ="insert into SW_DJRQLX(DJRQLX,ZHS,JSHS,SEVENJSHS,SEVENJSHSZB,EIGHTJSHS,EIGHTJSHSZB,NINEJSHS,NINEJSHSZB,SEVENSSJE,SEVENSSJEZB,EIGHTSSJE,EIGHTSSJEZB,NINETSSJE,NINETSSJEZB) "
							+ "values('"+list1.get(i).get("DJRQ")+"','"+list1.get(i).get("ZHS")+"',"
							+ "'"+list1.get(i).get("JSHS")+"','"+list1.get(i).get("S_JSHS")+"',"
							+ "'"+list1.get(i).get("S_JSHSZB")+"','"+list1.get(i).get("E_JSHS")+"',"
							+ "'"+list1.get(i).get("E_JSHSZB")+"','"+list1.get(i).get("N_JSHS")+"',"
							+ "'"+list1.get(i).get("N_JSHSZB")+"','"+list1.get(i).get("S_SSZE")+"',"
							+ "'"+list1.get(i).get("S_SSJEZB")+"','"+list1.get(i).get("E_SSZE")+"',"
							+ "'"+list1.get(i).get("E_SSJEZB")+"','"+list1.get(i).get("N_SSZE")+"',"
							+ "'"+list1.get(i).get("N_SSJEZB")+"')";
					this.getBs().insert(insertSql);
				}
					
				return this.toJson("000", "插入数据成功!!");
				
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "插入数据失败!!");
			}
		}
		
}
