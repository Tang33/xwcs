package fast.app;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.Super;

public class syqkfx extends Super{
	
	
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
	
	//查询税务总体情况(echarts图表)
	public String querySwztqk(Map<String,Object> rmap) {
		initMap(rmap);
		List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
		try {
			Map<String, Object> user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			if (getValue(user.get("DWID")).equals("00")) {
				String sql = "select kzztlx name,zhs value from SWZTQK where jd_dm = '"+getValue(user.get("DWID"))+"'";
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
			}else {
				String sql = "select kzztlx name,zhs value from SWZTQK where jd_dm = '"+getValue(user.get("DWID"))+"'";
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
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败!!");
		}
	}
	
		//查询税务总体情况详情
		public String querySwztqkDetail(Map<String,Object> rmap) {
			initMap(rmap);
			try {
				Map<String, Object> user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
				if (getValue(user.get("DWID")).equals("00")) {
					String sql = "select * from SWZTQK where jd_dm = '"+getValue(user.get("DWID"))+"'";
					List<Map<String, Object>> list = this.getBs().query(sql);
					return this.toJson("000", "查询成功!!",list);
				}else {
					String sql = "select * from SWZTQK where jd_dm = '"+getValue(user.get("DWID"))+"'";
					List<Map<String, Object>> list = this.getBs().query(sql);
					return this.toJson("000", "查询成功!!",list);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败!!");
			}
		}
		
		//查询登记注册(echarts图表)
		public String queryDjzclx(Map<String,Object> rmap) {
			initMap(rmap);
			List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
			try {
				Map<String, Object> user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
				if (getValue(user.get("DWID")).equals("00")) {
				
				String sql = "select * from (select * from SW_DJZCLX where jd_dm = '"+getValue(user.get("DWID"))+"' ORDER BY  CAST(ZHS AS INTEGER) desc) aa where ROWNUM <=7";
				List<Map<String, Object>> list = this.getBs().query(sql);
				
				/*Map<String, String> map;
				for (int i = 0; i < list.size(); i++) {
					map = new HashMap<String, String>();
					for (String key : list.get(i).keySet()) {
						map.put(key.toLowerCase(), list.get(i).get(key).toString());
					}
					list1.add(map);
				}*/
				
				return this.toJson("000", "查询成功!!",list);
				}else {
					String sql = "select * from (select * from SW_DJZCLX where jd_dm = '"+getValue(user.get("DWID"))+"' ORDER BY  CAST(ZHS AS INTEGER) desc) aa where ROWNUM <=7 ";
					List<Map<String, Object>> list = this.getBs().query(sql);
					
					/*Map<String, String> map;
					for (int i = 0; i < list.size(); i++) {
						map = new HashMap<String, String>();
						for (String key : list.get(i).keySet()) {
							map.put(key.toLowerCase(), list.get(i).get(key).toString());
						}
						list1.add(map);
					}*/
					
					return this.toJson("000", "查询成功!!",list);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败!!");
			}
		}
		
		//查询登记注册详情
		public String queryDJZCLXDetail(Map<String,Object> rmap) {
			initMap(rmap);
			try {
				Map<String, Object> user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
				if (getValue(user.get("DWID")).equals("00")) {
				
					String sql = "select * from SW_DJZCLX where jd_dm = '"+getValue(user.get("DWID"))+"'";
					List<Map<String, Object>> list = this.getBs().query(sql);
					
					return this.toJson("000", "查询成功!!",list);
				}else {
					String sql = "select * from SW_DJZCLX where jd_dm = '"+getValue(user.get("DWID"))+"'";
					List<Map<String, Object>> list = this.getBs().query(sql);
					return this.toJson("000", "查询成功!!",list);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败!!");
			}
		}
		
		
		//查询行业(echarts图表)
				public String queryHY(Map<String,Object> rmap) {
					initMap(rmap);
					List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
					try {
						Map<String, Object> user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
						if (getValue(user.get("DWID")).equals("00")) {
							String sql = "select * from (select HY name,zhs value from SW_HYLX  where jd_dm = '"+getValue(user.get("DWID"))+"'  ORDER BY  CAST(ZHS AS INTEGER) desc) aa where ROWNUM <=10";
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
						}else {
							String sql = "select * from (select HY name,zhs value from SW_HYLX where jd_dm = '"+getValue(user.get("DWID"))+"'  ORDER BY  CAST(ZHS AS INTEGER) desc) aa where ROWNUM <=10 ";
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
						}
					} catch (Exception e) {
						e.printStackTrace();
						return this.toJson("009", "查询失败!!");
					}
				}
				
				//查询行业详情
				public String queryHYDetail(Map<String,Object> rmap) {
					initMap(rmap);
					try {
						Map<String, Object> user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
						if (getValue(user.get("DWID")).equals("00")) {
						
							String sql = "select * from SW_HYLX aa where aa.jd_dm = '"+getValue(user.get("DWID"))+"' ORDER BY CAST(aa.ZHS AS INTEGER) desc";
							List<Map<String, Object>> list = this.getBs().query(sql);
							
							return this.toJson("000", "查询成功!!",list);
						}else {
							String sql = "select * from SW_HYLX aa where aa.jd_dm = '"+getValue(user.get("DWID"))+"' ORDER BY CAST(aa.ZHS AS INTEGER) desc";
							List<Map<String, Object>> list = this.getBs().query(sql);
							
							return this.toJson("000", "查询成功!!",list);
							
						}
					} catch (Exception e) {
						e.printStackTrace();
						return this.toJson("009", "查询失败!!");
					}
				}
		
		
		
				//查询街道类型(echarts图表)
				public String queryJDLX(Map<String,Object> rmap) {
					initMap(rmap);
					List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
					try {
						Map<String, Object> user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
						if (getValue(user.get("DWID")).equals("00")) {
							String sql = "select jd,zhs,jshs,jnssje from SW_JDLX where jd_dm = '"+getValue(user.get("DWID"))+"' and  rownum <= 5 order by CAST(ZHS AS INTEGER) desc";
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
						}else {
							String sql = "select jd,zhs,jshs,jnssje from SW_JDLX where jd_dm = '"+getValue(user.get("DWID"))+"' and rownum <= 5 order by CAST(ZHS AS INTEGER) desc";
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
							
						}
					} catch (Exception e) {
						e.printStackTrace();
						return this.toJson("009", "查询失败!!");
					}
				}
				
				/**
				   * 数据挖掘街道
				 * @param rmap
				 * @return
				 */
				public String JDDatamining(Map<String, Object> rmap){
					initMap(rmap);
					List<Map<String, Object>> list = new ArrayList<>();
					try{
						Map<String, Object> user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
						if (getValue(user.get("DWID")).equals("00")) {
							String sql = "select * from SW_JDLX where jd_dm = '"+getValue(user.get("DWID"))+"' order by CAST(ZHS AS INTEGER) desc";
							list = this.getBs().query(sql);
							return this.toJson("000", "查询成功！",list);
						}else {
							String sql = "select * from SW_JDLX where jd_dm = '"+getValue(user.get("DWID"))+"' order by CAST(ZHS AS INTEGER) desc";
							list = this.getBs().query(sql);
							return this.toJson("000", "查询成功！",list);
						}
					}catch(Exception e){
						e.printStackTrace();
						return this.toJson("001", "查询失败！",list);
					}
				}
				
				 // 导出excel(街道类型)
				 public Object exportJD(Map<String, Object> rmap) {
					  try {
					   // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
					   initMap(rmap);
					   // 街道
					   String sqlall="select * from SW_JDLX order by CAST(ZHS AS INTEGER) desc";
					   List<Map<String, Object>> map=this.getBs().query(sqlall);
					   Map<String, Object> map1 = new HashMap<String, Object>();
					   String[] cols = { "街道", "总户数", "见税户数", "见税户数占比" , "今年税收金额（元）" , "今年税收金额占比"};
					   String[] keys = { "JD", "ZHS", "JSHS", "JSHSZB" , "JNSSJE" , "JNSSZB"};
					   map1.put("fileName", "按街道分析.xls");
					   map1.put("cols", cols);
					   map1.put("keys", keys);
					   map1.put("list", map);
					   return map1;
					  } catch (Exception e) {
						  e.printStackTrace();
						  return this.toJson("009", "查询异常！");
					  }
				 }
				
				//查询登记日期
				public String queryDJRQLX(Map<String,Object> rmap) {
					initMap(rmap);
					List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
					try {
						Map<String, Object> user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
						if (getValue(user.get("DWID")).equals("00")) {
							String sql = "select DJRQLX YEAR,ZHS,JSHS from SW_DJRQLX where jd_dm = '"+getValue(user.get("DWID"))+"' and rownum <= 5 order by CAST(DJRQLX AS INTEGER)";
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
						}else {
							String sql = "select DJRQLX YEAR,ZHS,JSHS from SW_DJRQLX where jd_dm = '"+getValue(user.get("DWID"))+"' and rownum <= 5 order by CAST(DJRQLX AS INTEGER)";
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
						}
					} catch (Exception e) {
						e.printStackTrace();
						return this.toJson("009", "查询失败!!");
					}
				}
				
				/**
				   * 数据挖掘年份
				 * @param rmap
				 * @return
				 */
				public String YEARDatamining(Map<String, Object> rmap){
					initMap(rmap);
					List<Map<String, Object>> list = new ArrayList<>();
					try{
						Map<String, Object> user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
						if (getValue(user.get("DWID")).equals("00")) {
						
							String sql = "select * from SW_DJRQLX where jd_dm = '"+getValue(user.get("DWID"))+"' order by CAST(DJRQLX AS INTEGER)";
							list = this.getBs().query(sql);
							return this.toJson("000", "查询成功！",list);
						}else {
							String sql = "select * from SW_DJRQLX where jd_dm = '"+getValue(user.get("DWID"))+"'  order by CAST(DJRQLX AS INTEGER)";
							list = this.getBs().query(sql);
							return this.toJson("000", "查询成功！",list);
						}
					}catch(Exception e){
						e.printStackTrace();
						return this.toJson("001", "查询失败！",list);
					}
				}

				 
				//查询街道注册详情
				public String queryJDDetail(Map<String,Object> rmap) {
					initMap(rmap);
					try {
						
						String sql = "select * from SW_JDLX";
						List<Map<String, Object>> list = this.getBs().query(sql);
						
						return this.toJson("000", "查询成功!!",list);
					} catch (Exception e) {
						e.printStackTrace();
						return this.toJson("009", "查询失败!!");
					}
				}
		
				 // 导出excel(年份类型)
				 public Object exportYEAR(Map<String, Object> rmap) {
					  try {
					   // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
					   initMap(rmap);
					   // 街道
					   String sqlall="select * from SW_DJRQLX order by CAST(DJRQLX AS INTEGER)";
					   List<Map<String, Object>> map=this.getBs().query(sqlall);
					   Map<String, Object> map1 = new HashMap<String, Object>();
					   String[] cols = { "登录日期类型", "总户数", "见税户数", "2017见税户数" , "2017见税户数占比" , "2018见税户数", "2018见税户数占比", "2019见税户数", "2019见税户数占比", "2017税收金额（元）", "2017税收金额占比", "2018税收金额（元）", "2018税收金额占比", "2019税收金额（元）", "2019税收占比"};
					   String[] keys = { "DJRQLX", "ZHS", "JSHS", "SEVENJSHS" , "SEVENJSHSZB" , "EIGHTJSHS", "EIGHTJSHSZB", "NINEJSHS", "NINEJSHSZB", "SEVENSSJE", "SEVENSSJEZB", "EIGHTSSJE", "EIGHTSSJEZB", "NINETSSJE", "NINETSSJEZB"};
					   map1.put("fileName", "按年份分析.xls");
					   map1.put("cols", cols);
					   map1.put("keys", keys);
					   map1.put("list", map);
					   return map1;
					  } catch (Exception e) {
						  e.printStackTrace();
						  return this.toJson("009", "查询异常！");
					  }
				 }
				

}
