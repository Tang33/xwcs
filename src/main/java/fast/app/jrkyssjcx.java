package fast.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Super;

public class jrkyssjcx extends Super{
	private static Connection connection = null;
	private Map<String, Object> user = null;
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/jrkyssjcx";
		} catch (Exception e) {
			e.printStackTrace();
			return "xtgl/jrkyssjcx";
		}
	}
	
	//查询分组
	 public String queryfz(Map<String, Object> rmap) {
		  try {
		   initMap(rmap);
		   String sql = " SELECT DISTINCT mbm,zdm FROM FAST_YSSJCX_MB where DYCXB = 'xwcs_gsdr_yssjjrk'  ";
		   List<Map<String,Object>> list = this.getBs().query(sql);
		   return this.toJson("000","查询成功", list);
		  }catch(Exception e) {
		   return this.toJson("009", "查询异常");
		  }
		 }
	//查询展示
		public String queryzd(Map<String, Object> rmap) {
			try {
				// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
				initMap(rmap);

				String xz = getValue(this.getForm().get("xz"));
				
				SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				c.add(Calendar.MONTH, -1);
				Date m = c.getTime();
				String rkrq = format.format(m);
				
				
				String sql="select "+xz+" xmmc from xwcs_gsdr_yssjjrk group by "+xz;
				List<Map<String, Object>> sjjgall = this.getBs().query(sql);
				
				List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
				Map<String, String> map;
				   for (int i = 0; i < sjjgall.size(); i++) {
					   if (sjjgall.get(i) == null) {
						   sjjgall.remove(i);
						   i--;
					   }else {
						  map = new HashMap<String, String>();
						    for (String key : sjjgall.get(i).keySet()) {
						    	map.put(key.toLowerCase(), sjjgall.get(i).get(key).toString());
						    }
							  
						    list1.add(map);
					   }  
				}
				
				
				return this.toJson("000", "查询成功！", list1);
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询异常！");
			}
		}

		//查询页面加载下拉框中的数据
		public String selectList(Map<String, Object> rmap) {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			try {
				initMap(rmap);
				
				String sql = "select MBM,ZDM FROM  FAST_YSSJCX_MB where DYCXB = 'xwcs_gsdr_yssjjrk' group by MBM,ZDM ";
				List<Map<String,Object>> list = this.getBs().query(sql);
				
				return this.toJson("000","查询成功", list);
			}catch(Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询异常");
			}
		}
		
		
		//点击添加按钮依据传过来的名称查询
		public String selectislike(Map<String, Object> rmap) {
			try {
				initMap(rmap);
				String zdm = getValue(this.getForm().get("xz"));
				String sql = "select * from fast_yssjcx_mb where ZDM = '" + zdm + "' and DYCXB = 'xwcs_gsdr_yssjjrk'";
				List<Map<String,Object>> list = this.getBs().query(sql);
				
				return this.toJson("000","查询成功", list);
			}catch(Exception e) {
				return this.toJson("009", "查询异常");
			}
		}
		
		//查询多选框
		 public String selectSZ(Map<String, Object> rmap) {
		  try {
		   initMap(rmap);
		   String xz = getValue(this.getForm().get("xz"));
		   
		   String sql = "select DISTINCT "+xz+" selname from xwcs_gsdr_yssjjrk";
		   List<Map<String,Object>> list = this.getBs().query(sql);
		   for (int i = 0; i < list.size(); i++) {
				if (list.get(i) == null) {
					list.remove(i);
					i--;
				}
		   }
		   return this.toJson("000","查询成功", list);
		   
		  }catch(Exception e) {
			  e.printStackTrace();
		   return this.toJson("009", "查询异常");
		  }
		 }
		
		
		//查询展示  maplist
			public String querySdbg(Map<String, Object> rmap) {
				try {
					// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
					initMap(rmap);
					
					String page = getValue(this.getForm().get("page"));
					String pagesize = getValue(this.getForm().get("limit"));
					//获取页面传递过来的input中的值
					String fz = getValue(this.getForm().get("fz"));
					//获取表单中的数据
					String form = getValue(this.getForm().get("form"));
					String rk_rq2 = getValue(this.getForm().get("drrq"));
					String arr[] = rk_rq2.split("-");
					String rk_rq = arr[0] + arr[1];
					
					String sqlcs="";
					
					
					JSONArray form1  =JSONArray.parseArray(form);
					//下标从一开始取值
					if (form1.size() > 1) {
						for (int i = 1; i < form1.size(); i++) {
								JSONObject obj = (JSONObject) JSONObject.toJSON(form1.get(i));
								sqlcs += " " + getValue(obj.get("value"));
						}
					}
					
					
					
					
//					for (int i = 0; i< form1.size(); i++) {
//						JSONObject obj = (JSONObject) JSONObject.toJSON(form1.get(i));
//					    String name=getValue(obj.get("name"));
//					    String zd="";
//					    String value=getValue(obj.get("value"));
//					    String tj="";
//					    String lx="";
//					    String index="";
//					    if(name.indexOf("*")>-1) {
//					    	String[] strs = value.split(",");
//					    	String name1 = name.substring(0,name.length()-1);
//					    	for(int j = 0;j<strs.length;j++) {
//					    		if (j==0) {
//					    			sqlcs += " and ("+name1+"='" + strs[j]+"'";
//								}else {
//
//						    		sqlcs += " or  "+name1+"='" + strs[j]+"'";
//								}
//					    	}
//				    		sqlcs += " ) ";
//					    }else {
//					    	//如果页面传递过来的name包括下划线
//						     if (name.lastIndexOf("_")>-1) {
//						    	 //取出后面跟上的数字
//								index=name.substring(name.lastIndexOf("_")+1);
//								//取出前面的名字
//								zd=name.substring(0,name.lastIndexOf("_"));
//						     }
//						     if (!index.equals("")) {
//						    	 for (int j = 0; j < form1.size(); j++) {
//							    	  JSONObject obj1 = (JSONObject) JSONObject.toJSON(form1.get(j));
//							    	  if (getValue(obj1.get("name")).equals("select"+index)) {
//							    		  tj=getValue(obj1.get("value"));
//							    		  lx=tj.substring(tj.lastIndexOf("_")+1);
//							    		  tj=tj.substring(0,tj.lastIndexOf("_"));
//									}	
//								}   
//							}			     
//						    switch (tj) {
//						    case "<":
//								if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
//										|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
//									
//										sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') < to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
//									
//								}else{
//									sqlcs+=" and "+zd+" < "+value+"";
//								}
//								break;
//							case ">":
//								
//								if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
//										|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
//									
//										sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') > to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
//									
//								}else{
//									sqlcs+=" and "+zd+" > "+value+"";
//								}
//								
//								break;
//							case "=":
//								if (lx.contains("text")) {
//									sqlcs+=" and "+zd+" = '"+value+"'";
//								}else {
//									if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
//											|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
//										
//											sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') = to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
//										
//									}else{
//										sqlcs+=" and "+zd+" = "+value+"";
//									}
//								}
//								
//								break;
//							case ">=":
//								
//								if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
//										|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
//									
//										sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') >= to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
//									
//								}else{
//									sqlcs+=" and "+zd+" <= "+value+"";
//								}
//								
//								break;
//							case "<=":
//								if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
//										|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
//									
//										sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') <= to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
//									
//								}else{
//									sqlcs+=" and "+zd+" >= "+value+"";
//								}
//								break;
//							case "like":
//								sqlcs+=" and "+zd+" like '%"+value+"%'";
//								break;
//							case "not like":
//								sqlcs+=" and "+zd+" not like '%"+value+"%'";
//								break;							
//							case "!=":
//								sqlcs+=" and "+zd+" != '"+value+"'";
//								break;
//							default:
//								if(name.lastIndexOf("-")>-1){
//									zd=name.substring(0,name.lastIndexOf("-"));
//								sqlcs+=" and "+zd+" like '%"+value+"'";
//								}
//								break;
//							}
//					    }
//					}
					
					
					System.out.println(sqlcs);
					
					//List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
					String sql = "select * from xwcs_gsdr_yssjjrk where 1=1";
				    sql+=sqlcs;
				    
				    if (!rk_rq.equals("")) {
						sql +=" and rk_rq='"+rk_rq+"' ";
						
					}
				    
				    if (!fz.equals("")) {
				    	
						if (fz.equals("sjje")) {
							sql = "select t."+fz+"," + 
									"SUM(t.zyj) zyj,SUM(t.ssj) ssj,SUM(t.dsj) dsj,SUM(t.xzj) xzj from xwcs_gsdr_yssjjrk t where 1=1";
							
							
						} else {
							sql = "select t."+fz+",sum(t.sjje) sjje," + 
									"SUM(t.zyj) zyj,SUM(t.ssj) ssj,SUM(t.dsj) dsj,SUM(t.xzj) xzj from xwcs_gsdr_yssjjrk t where 1=1";
						}
						
						
					    sql+=sqlcs;
					    if (!rk_rq.equals("")) {
							sql +=" and rk_rq='"+rk_rq+"' ";
							
						}
					    sql+=" group by t."+fz+" ";
					}
					
					sql = "select * from (select row_.*, rownum rowno from ("+sql+") row_ where rownum <= " + pagesize + "*"
							+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize + " + 1";
					System.out.println(sql);
					List<Map<String, Object>> sjjgall = this.getBs().query(sql);
					
					
					//查询count
					String sqlcount = "select count(*) CS from xwcs_gsdr_yssjjrk a where 1=1" + sqlcs;
					 if (!rk_rq.equals("")) {
							sqlcount +=" and rk_rq='"+rk_rq+"' ";
							
					}
					 if (!fz.equals("")) {
							sql = "select t."+fz+",sum(t.sjje) sjje,sum(t.qxj) qxj," + 
									"SUM(t.zyj) zyj,SUM(t.ssj) ssj,SUM(t.dsj) dsj,SUM(t.xzj) xzj from xwcs_gsdr_yssjjrk t where 1=1";
						    sql+=sqlcs;
						    sql+=" group by t."+fz+" ";
						}
					System.out.println(sqlcount);
					List<Map<String, Object>> sjjgallcount = this.getBs().query(sqlcount);
					String cont=getValue(sjjgallcount.get(0).get("CS"));
					
					
					List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < sjjgall.size(); i++) {
						Map<String, Object> map=sjjgall.get(i);
						map.put("nsrsbh", map.get("NSRSBH"));
						map.put("nsrmc", map.get("NSRMC"));
						map.put("zsxmmc", map.get("ZSXMMC"));
						map.put("zspm", map.get("ZSPM"));
						map.put("zspmdm", map.get("ZSPMDM"));
						map.put("skshqq", map.get("SKSHQQ"));
						map.put("skshqz", map.get("SKSHQZ"));
						map.put("djzclx", map.get("DJZCLX"));
						map.put("ssgly", map.get("SSGLY"));
						map.put("hyml", map.get("HYML"));
						map.put("hydl", map.get("HYDL"));
						map.put("hyzl", map.get("HYZL"));
						map.put("hy", map.get("HY"));
						map.put("zsdlfs", map.get("ZSDLFS"));
						map.put("jsyj", map.get("JSYJ"));
						map.put("kssl", map.get("KSSL"));
						map.put("sl", map.get("SL"));
						map.put("sjje", map.get("SJJE"));
						map.put("skssswjg", map.get("SKSSSWJG"));
						map.put("zgsws", map.get("ZGSWS"));
						map.put("yskmdm", map.get("YSKMDM"));
						map.put("yskm", map.get("YSKM"));
						map.put("ysfpbl", map.get("YSFPBL"));
						map.put("zyjbl", map.get("ZYJBL"));
						map.put("ssjbl", map.get("SSJBL"));
						map.put("dsjbl", map.get("DSJBL"));
						map.put("qxjbl", map.get("QXJBL"));
						map.put("xzjbl", map.get("XZJBL"));
						map.put("zydfpbl", map.get("ZYDFPBL"));
						map.put("dsdfpbl", map.get("DSDFPBL"));
						map.put("sjdfpbl", map.get("SJDFPBL"));
						map.put("skgk", map.get("SKGK"));
						map.put("kjrq", map.get("KJRQ"));
						map.put("sjrq", map.get("SJRQ"));
						map.put("sjxhrq", map.get("SJXHRQ"));
						map.put("sjxhr", map.get("SJXHR"));
						map.put("rkrq", map.get("RKRQ"));
						map.put("rkxhrq", map.get("RKXHRQ"));
						map.put("rkxhr", map.get("RKXHR"));
						map.put("pzzl", map.get("PZZL"));
						map.put("pzzg", map.get("PZZG"));
						map.put("pzhm", map.get("PZHM"));
						map.put("skzl", map.get("SKZL"));
						map.put("sksx", map.get("SKSX"));
						map.put("jdxz", map.get("JDXZ"));
						map.put("yhhb", map.get("YHHB"));
						map.put("yhyywd", map.get("YHYYWD"));
						map.put("yhzh", map.get("YHZH"));
						map.put("hzrq", map.get("HZRQ"));
						map.put("hzr", map.get("HZR"));
						map.put("hzpzzl", map.get("HZPZZL"));
						map.put("hzpzzg", map.get("HZPZZG"));
						map.put("hzpzhm", map.get("HZPZHM"));
						map.put("tzlx", map.get("TZLX"));
						map.put("jkqx", map.get("JKQX"));
						map.put("zsswjg", map.get("ZSSWJG"));
						map.put("dzsphm", map.get("DZSPHM"));
						map.put("zyj", map.get("ZYJ"));
						map.put("ssj", map.get("SSJ"));
						map.put("dsj", map.get("DSJ"));
						map.put("qxj", map.get("QXJ"));
						map.put("xzj", map.get("XZJ"));
						map.put("zydfp", map.get("ZYDFP"));
						map.put("sjdfp", map.get("SJDFP"));
						map.put("dsdfp", map.get("DSDFP"));
						map.put("djxh", map.get("DJXH"));
						map.put("rk_rq", map.get("RK_RQ"));
						
						
						lists.add(map);
					}
					
					return this.toJsonct("000", "查询成功！", lists, cont);
				} catch (Exception e) {
					e.printStackTrace();
					return this.toJson("009", "查询异常！");
				}
			}
			
			
			private String getsql(String key, String value) {
				
				String sqlcs="";
				if(key.equals("jsyj")||key.equals("sl")||key.equals("sjje")||key.equals("zyj")||key.equals("ssj")
						||key.equals("dsj")||key.equals("qxj")||key.equals("xzj")||key.equals("zydfp")||key.equals("sjdfp")||key.equals("dsdfp")) {
					
					if(!StringUtils.isEmpty(value)) {
						
						sqlcs=sqlcs+" and a."+key+"="+value+"";
						
					}
					
				}else {
					
					if(!StringUtils.isEmpty(value)) {
						
						if(key.equals("nsrsbh")){
							
							sqlcs=sqlcs+" and a."+key+" like '"+value+"%'";
						}else{
							
							sqlcs=sqlcs+" and a."+key+"='"+value+"'";
						}
						
					}
					
				}
				return sqlcs;
			}


			//将查询的结果进行修改
			public String modify(Map<String, Object> rmap) {
				initMap(rmap);
				
				String jdxz = getValue(this.getForm().get("jdxz"));
				
				String maplist = getValue(this.getForm().get("maplist"));
				JSONObject json = JSONObject.parseObject(maplist);
				
				String sqlcs="";
			    for(String key : json.keySet()) {
			    	String value = json.getString(key);
			    	
			    	if(!StringUtils.isEmpty(value)) {
			    		
			    		String sqlcsdg=getsql(key, value);
			    		sqlcs+=sqlcsdg;
			    	}
			    	
			    }
				
				
				sqlcs+=" and a.id=t.ysid";
				
				String updatesql = " update xwcs_gsdr_temp set jd_mc='"+jdxz+"' where ysid in (select t.ysid from xwcs_gsdr_temp t,xwcs_gsdr_yssjgs a where 1=1";
				updatesql+=sqlcs+")";
				
				
				Integer upnum = this.getBs().update(updatesql);
				if(upnum!=0) {
					
					return this.toJson("000", Integer.toString(upnum));
				}else {
					
					return this.toJson("000", "修改失败！");
				}
				
			}
			
			//获取街道
			public String getjd(Map<String, Object> rmap) {
				initMap(rmap);

				String sql = "select distinct jd_mc from dm_jd";
				
				List<Map<String, Object>> list = this.getBs().query(sql);
				return this.toJson("000", "查询成功！", list);
				
			}
			
			
			//保存模板
			public String addmb(Map<String, Object> rmap) {
				
				initMap(rmap);
				String jdxz = getValue(this.getForm().get("jdxz"));
				String mbmc = getValue(this.getForm().get("mbmc"));
				
				String maplist = getValue(this.getForm().get("maplist"));
				JSONObject json = JSONObject.parseObject(maplist);
				
				String sqlcs="";
			    for(String key : json.keySet()) {
			    	String value = json.getString(key);
			    	
			    	if(!StringUtils.isEmpty(value)) {
			    		
			    		String sqlcsdg=getsql(key, value);
			    		sqlcs+=sqlcsdg;
			    	}
			    	
			    }
				
				
				String updatesql = " update xwcs_gsdr_temp set jd_mc='"+jdxz+"' where ysid in (select t.ysid from xwcs_gsdr_temp t,xwcs_gsdr_yssjgs a where 1=1";
				updatesql+=sqlcs;
				updatesql+=" and a.id=t.ysid)";
				
				//将名称以及sql语句插入模板表
				String sqlsert="insert into fast_mb (mbmc,sql,status,createtime,bs) values(?,?,?,?,?)";
				try {
					
					connection = getConnection();
					connection.setAutoCommit(false);
					
					PreparedStatement stmt = connection.prepareStatement(sqlsert);
					
					stmt.setString(1, mbmc);
					//stmt.setClob(2, new StringReader(updatesql),updatesql.length());
				    StringReader reader = new StringReader(updatesql);  
				    stmt.setCharacterStream(2, reader, updatesql.length());  
					stmt.setString(3, "1");
					stmt.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
					stmt.setString(5, "2");
					
					stmt.execute();
					connection.commit();
					return this.toJson("000", "插入成功！");
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
				
				
				
			}


			public static Connection getConnection() {

				try {
					Properties properties = new Properties();
					// 使用ClassLoader加载properties配置文件生成对应的输入流
					InputStream in = JdbcConnectedPro.class.getClassLoader().getResourceAsStream("conf/jdbc.properties");
					// 使用properties对象加载输入流
					properties.load(in);
					// 获取key对应的value值
					String driver = properties.getProperty("jdbc.driver");
					String url = properties.getProperty("jdbc.url");
					String username = properties.getProperty("jdbc.username");
					String pwd = properties.getProperty("jdbc.password");
					// 创建dataSource
					BasicDataSource dataSource = new BasicDataSource();
					// 加载数据库驱动
					dataSource.setDriverClassName(driver);
					// 设置用户名和密码
					dataSource.setUrl(url);
					dataSource.setUsername(username);
					dataSource.setPassword(pwd);
					connection = dataSource.getConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return connection;
			}

}
