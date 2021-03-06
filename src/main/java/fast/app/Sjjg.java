package fast.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;





import org.apache.commons.dbcp.BasicDataSource;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Super;

public class Sjjg extends Super {
	private static Connection connection = null;
	private Map<String, Object> user = null;

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");

			return "sssjgl/Sjjg";
		} catch (Exception e) {
			e.printStackTrace();
			return "sssjgl/Sjjg";
		}
	}
	
	
	
	//查询页面加载下拉框中的数据
	public String selectList(Map<String, Object> rmap) {
		// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
		try {
			initMap(rmap);
			String tableName =getValue(this.getForm().get("tableName"));
			tableName = tableName.toLowerCase();
			if (tableName == "") {
				tableName = "xwcs_gsdr_yssjrk";
				String sql = "select MBM,ZDM FROM  FAST_YSSJCX_MB where DYCXB = '"+tableName+"' group by MBM,ZDM";
				List<Map<String,Object>> list = this.getBs().query(sql);
				return this.toJson("000","查询成功", list);

			}else {
				String sql = "select MBM,ZDM FROM  FAST_YSSJCX_MB where DYCXB = '"+tableName+"' group by MBM,ZDM";
				List<Map<String,Object>> list = this.getBs().query(sql);
				return this.toJson("000","查询成功", list);
			}
			
			
		}catch(Exception e) {
			return this.toJson("009", "查询异常");
		}
	}
	
	//点击添加按钮依据传过来的名称查询
	public String selectislike(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			  String tableName =getValue(this.getForm().get("tableName"));
			   tableName = tableName.toLowerCase();
			String zdm = getValue(this.getForm().get("xz"));
			String sql = "select * FROM FAST_YSSJCX_MB where ZDM = '" + zdm + "' and DYCXB = '"+tableName+"' ";
			List<Map<String,Object>> list = this.getBs().query(sql);
			
			return this.toJson("000","查询成功", list);
		}catch(Exception e) {
			return this.toJson("009", "查询异常");
		}
	}
	
	//查询税种
	 public String selectSZ(Map<String, Object> rmap) {
	  try {
	   initMap(rmap);
	   String tableName =getValue(this.getForm().get("tableName"));
	   tableName = tableName.toLowerCase();
	   String xz = getValue(this.getForm().get("xz"));
	   String sql = "select DISTINCT "+xz+" zsxmmc from "+tableName+"";
	   List<Map<String,Object>> list = this.getBs().query(sql);
	   for (int i = 0; i < list.size(); i++) {
		  if(list.get(i) == null || list.get(i).size() == 0){
			   list.remove(i);
			  }
	}
	
	   return this.toJson("000","查询成功", list);
	  }catch(Exception e) {
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
			//获取表单中的数据
			String form = getValue(this.getForm().get("form"));
			String fz = getValue(this.getForm().get("fz"));
			String rk_rq =getValue(this.getForm().get("drrq"));
			String tableName =getValue(this.getForm().get("tableName"));
			
			String sqlcs="";	
			
			JSONArray form1  =JSONArray.parseArray(form);
			
			//下标从一开始取值
			if (form1.size() > 1) {
				for (int i = 1; i < form1.size(); i++) {
						JSONObject obj = (JSONObject) JSONObject.toJSON(form1.get(i));
						sqlcs += " " + getValue(obj.get("value"));
				}
			}
			System.out.println("前台传过来的值sql:" + sqlcs);
//			for (int i = 0; i< form1.size(); i++) {
//				JSONObject obj = (JSONObject) JSONObject.toJSON(form1.get(i));
//			    String name=getValue(obj.get("name"));
//			    String zd="";
//			    String value=getValue(obj.get("value"));
//			    String tj="";
//			    String lx="";
//			    String index="";
//			    if(name.indexOf("*")>-1) {
//			    	String[] strs = value.split(",");
//			    	String name1 = name.substring(0,name.length()-1);
//			    	for(int j = 0;j<strs.length;j++) {
//			    		if (j==0) {
//			    			sqlcs += " and (a."+name1+"='" + strs[j]+"'";
//						}else {
//
//				    		sqlcs += " or  a."+name1+"='" + strs[j]+"'";
//						}
//			    	}
//		    		sqlcs += " ) ";
//			    }else {
//			    	//如果页面传递过来的name包括下划线
//				     if (name.lastIndexOf("_")>-1) {
//				    	 //取出后面跟上的数字
//						index=name.substring(name.lastIndexOf("_")+1);
//						//取出前面的名字
//						zd="a."+name.substring(0,name.lastIndexOf("_"));
//				     }
//				     if (!index.equals("")) {
//				    	 for (int j = 0; j < form1.size(); j++) {
//					    	  JSONObject obj1 = (JSONObject) JSONObject.toJSON(form1.get(j));
//					    	  if (getValue(obj1.get("name")).equals("select"+index)) {
//					    		  tj=getValue(obj1.get("value"));
//					    		  lx=tj.substring(tj.lastIndexOf("_")+1);
//					    		  tj=tj.substring(0,tj.lastIndexOf("_"));
//							}	
//						}   
//					}	
//				    switch (tj) {
//				    case "<":
//						if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
//								|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
//							
//								sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') < to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
//							
//						}else{
//							sqlcs+=" and "+zd+" < "+value+"";
//						}
//						break;
//					case ">":
//						
//						if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
//								|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
//							
//								sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') > to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
//							
//						}else{
//							sqlcs+=" and "+zd+" > "+value+"";
//						}
//						
//						break;
//					case "=":
//						if (lx.contains("text")) {
//							sqlcs+=" and "+zd+" = '"+value+"'";
//						}else {
//							if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
//									|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
//								
//									sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') = to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
//								
//							}else{
//								sqlcs+=" and "+zd+" = "+value+"";
//							}
//						}
//						break;
//					case "<=":
//						
//						if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
//								|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
//							
//								sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') <= to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
//							
//						}else{
//							sqlcs+=" and "+zd+" <= "+value+"";
//						}
//						
//						break;
//					case ">=":
//						if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
//								|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
//							
//								sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') >= to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
//							
//						}else{
//							sqlcs+=" and "+zd+" >= "+value+"";
//						}
//						break;
//					case "like":
//						sqlcs+=" and "+zd+" like '%"+value+"%'";
//						break;
//					case "not like":
//						sqlcs+=" and "+zd+" not like '%"+value+"%'";
//						break;
//					case "!=":
//						sqlcs+=" and "+zd+" != '"+value+"'";
//						break;
//					default:
//						if(name.lastIndexOf("-")>-1){
//							zd="a."+name.substring(0,name.lastIndexOf("-"));
//						sqlcs+=" and "+zd+" like '%"+value+"'";
//						}
//						break;
//					}
//			    }
//			}
			
			
			
			sqlcs+=" and a.id=t.ysid";
			System.out.println(sqlcs);
			
			//List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
			
			String sql = "select t.zse,t.bl,t.nsrmc,t.nsrsbh,t.zsxm_mc,t.jd_mc JD_MC,t.hy,t.se,t.qxj,t.yskmdm from xwcs_gsdr_temp t,"+tableName+" a where 1=1";
			
		    sql+=sqlcs;
		    if (!rk_rq.equals("")) {
		    	sql +=" and a.rk_rq='"+rk_rq+"' ";
			}
		    //可用bs调用方法直接分页
			
			sql = "select * from (select row_.*, rownum rowno from ("+sql+") row_ where rownum <= " + pagesize + "*"
					+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize + " + 1";	
			System.out.println(sql);
			List<Map<String, Object>> sjjgall = this.getBs().query(sql);
			
			//查询count
			String sqlcount = "select count(*) cs from xwcs_gsdr_temp t,"+tableName+" a where 1=1" + sqlcs;
			 if (!rk_rq.equals("")) {
			    	sql +=" and rk_rq='"+rk_rq+"' ";
				}
			List<Map<String, Object>> sjjgallcount = this.getBs().query(sqlcount);
			String cont=getValue(sjjgallcount.get(0).get("CS"));
			System.out.println(sqlcount);			
			
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < sjjgall.size(); i++) {

				Map<String, Object> map=sjjgall.get(i);
				if (!fz.equals("")) {
					BigDecimal bd2 = getBigDecimal(map.get("DFKJ")).setScale(2, RoundingMode.HALF_UP);
					BigDecimal bd3 = getBigDecimal(map.get("ZSE")).setScale(2, RoundingMode.HALF_UP);
					DecimalFormat dft = new DecimalFormat("0.00");
					map.replace("DFKJ", dft.format(bd2));
					map.replace("ZSE", dft.format(bd3));
					map.put("nsrmc", map.get("NSRMC"));
					map.put("nsrsbh", map.get("NSRSBH"));
					map.put("zsxm_mc", map.get("ZSXM_MC"));
					map.put("jd_mc", map.get("JD_MC"));
					map.put("hy", map.get("HY"));
					map.put("se", map.get("SE"));
					map.put("qxj", map.get("QXJ"));
					map.put("yskmdm", map.get("YSKMDM"));
					map.put("dfkj",  map.get("DFKJ"));
					map.put("qkj", map.get("ZSE"));
					lists.add(map);	
				}else {
					Object zse = map.get("ZSE");
					Object bl = map.get("BL");
					BigDecimal money = getBigDecimal(zse).multiply(getBigDecimal(bl));
					BigDecimal money1 = money.divide(getBigDecimal(100), 2, RoundingMode.HALF_UP);
					DecimalFormat dft = new DecimalFormat("0.00");
					map.put("nsrmc", map.get("NSRMC"));
					map.put("nsrsbh", map.get("NSRSBH"));
					map.put("zsxm_mc", map.get("ZSXM_MC"));
					map.put("jd_mc", map.get("JD_MC"));
					map.put("hy", map.get("HY"));
					map.put("se", map.get("SE"));
					map.put("qxj", map.get("QXJ"));
					map.put("yskmdm", map.get("YSKMDM"));
					map.put("dfkj", dft.format(money1));
					map.put("qkj", dft.format(zse));
					lists.add(map);			
					}				
			}
			
			return this.toJsonct("000", "查询成功！", lists, cont);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	//查询展示  需修改条数    金额   纳税人数
	public String querySdbg1(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			
			
			//获取表单中的数据
			String form = getValue(this.getForm().get("form"));
			String fz = getValue(this.getForm().get("fz"));
			String rk_rq =getValue(this.getForm().get("drrq"));
			String tableName =getValue(this.getForm().get("tableName"));
			
			String sqlcs="";
			JSONArray form1  =JSONArray.parseArray(form);
			
			//下标从一开始取值
			if (form1.size() > 1) {
				for (int i = 1; i < form1.size(); i++) {
						JSONObject obj = (JSONObject) JSONObject.toJSON(form1.get(i));
						sqlcs += " " + getValue(obj.get("value"));
				}
			}
			System.out.println("前台传过来的值sql:" + sqlcs);
			
			
			
			/*JSONArray form1  =JSONArray.parseArray(form);
			for (int i = 0; i< form1.size(); i++) {
				JSONObject obj = (JSONObject) JSONObject.toJSON(form1.get(i));
			    String name=getValue(obj.get("name"));
			    String zd="";
			    String value=getValue(obj.get("value"));
			    String tj="";
			    String lx="";
			    String index="";
			    if(name.indexOf("*")>-1) {
			    	String[] strs = value.split(",");
			    	String name1 = name.substring(0,name.length()-1);
			    	for(int j = 0;j<strs.length;j++) {
			    		if (j==0) {
			    			sqlcs += " and (a."+name1+"='" + strs[j]+"'";
						}else {

				    		sqlcs += " or  a."+name1+"='" + strs[j]+"'";
						}
			    	}
		    		sqlcs += " ) ";
			    }else {
			    	//如果页面传递过来的name包括下划线
				     if (name.lastIndexOf("_")>-1) {
				    	 //取出后面跟上的数字
						index=name.substring(name.lastIndexOf("_")+1);
						//取出前面的名字
						zd="a."+name.substring(0,name.lastIndexOf("_"));
				     }
				     if (!index.equals("")) {
				    	 for (int j = 0; j < form1.size(); j++) {
					    	  JSONObject obj1 = (JSONObject) JSONObject.toJSON(form1.get(j));
					    	  if (getValue(obj1.get("name")).equals("select"+index)) {
					    		  tj=getValue(obj1.get("value"));
					    		  lx=tj.substring(tj.lastIndexOf("_")+1);
					    		  tj=tj.substring(0,tj.lastIndexOf("_"));
							}	
						}   
					}	
				    switch (tj) {
				    case "<":
						if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
								|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
							
								sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') < to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
							
						}else{
							sqlcs+=" and "+zd+" < "+value+"";
						}
						break;
					case ">":
						
						if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
								|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
							
								sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') > to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
							
						}else{
							sqlcs+=" and "+zd+" > "+value+"";
						}
						
						break;
					case "=":
						if (lx.contains("text")) {
							sqlcs+=" and "+zd+" = '"+value+"'";
						}else {
							if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
									|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
								
									sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') = to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
								
							}else{
								sqlcs+=" and "+zd+" = "+value+"";
							}
						}
						break;
					case "<=":
						
						if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
								|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
							
								sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') <= to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
							
						}else{
							sqlcs+=" and "+zd+" <= "+value+"";
						}
						
						break;
					case ">=":
						if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
								|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
							
								sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') >= to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
							
						}else{
							sqlcs+=" and "+zd+" >= "+value+"";
						}
						break;
					case "like":
						sqlcs+=" and "+zd+" like '%"+value+"%'";
						break;
					case "not like":
						sqlcs+=" and "+zd+" not like '%"+value+"%'";
						break;
					case "!=":
						sqlcs+=" and "+zd+" != '"+value+"'";
						break;
					default:
						if(name.lastIndexOf("-")>-1){
							zd="a."+name.substring(0,name.lastIndexOf("-"));
						sqlcs+=" and "+zd+" like '%"+value+"'";
						}
						break;
					}
			    }
			}
			*/
			
			
			sqlcs+=" and a.id=t.ysid";
			System.out.println(sqlcs);
			
			//List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
			
			
			
		    
		    if (!rk_rq.equals("")) {
		    	sqlcs +=" and a.rk_rq='"+rk_rq+"' ";
			}
		    String sql = "select  COUNT(*) allcount from xwcs_gsdr_temp t,"+tableName+" a where 1=1"+sqlcs;
		    String sq2 = "select SUM(t.se) dfzse  from xwcs_gsdr_temp t,"+tableName+" a where 1=1"+sqlcs;
		    String sq3 = "select   COUNT (DISTINCT t.nsrmc) nsrno from xwcs_gsdr_temp t,"+tableName+" a where 1=1"+sqlcs;
			 
		    String sqlall="SELECT * from"+"("+sql+"),"+"("+sq2+"),"+"("+sq3+")";
			List<Map<String, Object>> lists = this.getBs().query(sqlall);
			
			for (int i = 0; i < lists.size(); i++) {
				Map<String, Object> map = lists.get(i);
				BigDecimal bd3 = getBigDecimal(map.get("DFZSE")).setScale(2, RoundingMode.HALF_UP);
				DecimalFormat dft = new DecimalFormat("0.00");
				map.replace("dfzse", dft.format(bd3));
			}
			
			System.out.println(lists);			
			
		
			return this.toJson("000", "查询成功！", lists);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	
	
	
	
	
	
	public static BigDecimal getBigDecimal( Object value ) {  
        BigDecimal ret = null;  
        if( value != null ) {  
            if( value instanceof BigDecimal ) {  
                ret = (BigDecimal) value;  
            } else if( value instanceof String ) {  
                ret = new BigDecimal( (String) value );  
            } else if( value instanceof BigInteger ) {  
                ret = new BigDecimal( (BigInteger) value );  
            } else if( value instanceof Number ) {  
                ret = new BigDecimal( ((Number)value).doubleValue() );  
            } else {  
                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");  
            }  
        }  
        return ret;  
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
		//获取表单中的数据
		String form = getValue(this.getForm().get("form"));
		String rk_rq =getValue(this.getForm().get("drrq"));
		String tableName =getValue(this.getForm().get("tableName"));
		String sqlcs="";
		
		JSONArray form1  =JSONArray.parseArray(form);
		
		//下标从一开始取值
		if (form1.size() > 1) {
			for (int i = 1; i < form1.size(); i++) {
					JSONObject obj = (JSONObject) JSONObject.toJSON(form1.get(i));
					sqlcs += " " + getValue(obj.get("value"));
			}
		}
		System.out.println("前台传过来的值sql:" + sqlcs);
		
		/*JSONArray form1  =JSONArray.parseArray(form);
		for (int i = 0; i< form1.size(); i++) {
			JSONObject obj = (JSONObject) JSONObject.toJSON(form1.get(i));
		    String name=getValue(obj.get("name"));
		    String zd="";
		    String value=getValue(obj.get("value"));
		    String tj="";
		    String lx="";
		    String index="";
		    if(name.indexOf("*")>-1) {
		    	String[] strs = value.split(",");
		    	String name1 = name.substring(0,name.length()-1);
		    	for(int j = 0;j<strs.length;j++) {
		    		if (j==0) {
		    			sqlcs += " and (a."+name1+"='" + strs[j]+"'";
					}else {

			    		sqlcs += " or  a."+name1+"='" + strs[j]+"'";
					}
		    	}
	    		sqlcs += " ) ";
		    }else {
		    	//如果页面传递过来的name包括下划线
			     if (name.lastIndexOf("_")>-1) {
			    	 //取出后面跟上的数字
					index=name.substring(name.lastIndexOf("_")+1);
					//取出前面的名字
					zd="a."+name.substring(0,name.lastIndexOf("_"));
			     }
			     if (!index.equals("")) {
			    	 for (int j = 0; j < form1.size(); j++) {
				    	  JSONObject obj1 = (JSONObject) JSONObject.toJSON(form1.get(j));
				    	  if (getValue(obj1.get("name")).equals("select"+index)) {
				    		  tj=getValue(obj1.get("value"));
				    		  lx=tj.substring(tj.lastIndexOf("_")+1);
				    		  tj=tj.substring(0,tj.lastIndexOf("_"));
						}	
					}   
				}			     
			    switch (tj) {
			    case "<":
					if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
							|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
						
							sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') < to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
						
					}else{
						sqlcs+=" and "+zd+" < "+value+"";
					}
					break;
				case ">":
					
					if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
							|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
						
							sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') > to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
						
					}else{
						sqlcs+=" and "+zd+" > "+value+"";
					}
					
					break;
				case "=":
					if (lx.contains("text")) {
						sqlcs+=" and "+zd+" = '"+value+"'";
					}else {
						if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
								|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
							
								sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') = to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
							
						}else{
							sqlcs+=" and "+zd+" = "+value+"";
						}
					}
					break;
				case "<=":
					
					if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
							|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
						
							sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') <= to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
						
					}else{
						sqlcs+=" and "+zd+" <= "+value+"";
					}
					
					break;
				case ">=":
					if(name.indexOf("kjrq") > -1 || name.indexOf("sjrq") > -1 || name.indexOf("sjxhrq") > -1 || name.indexOf("rkxhrq") > -1
							|| name.indexOf("hzrq") > -1 || name.indexOf("rkrq") > -1) {
						
							sqlcs+=" and to_date("+zd+",'yyyy-MM-dd hh24:mi:ss') >= to_date('"+value+"','yyyy-MM-dd hh24:mi:ss')";
						
					}else{
						sqlcs+=" and "+zd+" >= "+value+"";
					}
					break;
				case "like":
					sqlcs+=" and "+zd+" like '%"+value+"%'";
					break;
				case "not like":
					sqlcs+=" and "+zd+" not like '%"+value+"%'";
					break;
				case "!=":
					sqlcs+=" and "+zd+" != '"+value+"'";
					break;
				default:
					if(name.lastIndexOf("-")>-1){
						zd="a."+name.substring(0,name.lastIndexOf("-"));
					sqlcs+=" and "+zd+" like '%"+value+"'";
					}
					break;
				}
		    }
		}*/
		
		
		sqlcs+=" and a.id=t.ysid";
		
		String updatesql = " update xwcs_gsdr_temp set jd_mc='"+jdxz+"' where ysid in (select t.ysid from xwcs_gsdr_temp t,"+tableName+" a where 1=1";
		updatesql+=sqlcs+")";
		
		System.out.println("------------");
		System.out.println(updatesql);
		
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
		String ms = getValue(this.getForm().get("ms"));
		
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
		
		
		String updatesql = " update xwcs_gsdr_temp set jd_mc='"+jdxz+"' where ysid in (select t.ysid from xwcs_gsdr_temp t,xwcs_gsdr_yssjrk a where 1=1";
		updatesql+=sqlcs;
		updatesql+=" and a.id=t.ysid)";
		
		//将名称以及sql语句插入模板表
		String sqlsert="insert into fast_mb (mbmc,sql,status,createtime,bs,ms) values(?,?,?,?,?,?)";
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
			stmt.setString(6, ms);
			
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


	
	
	
	//查询展示
		public String queryzd(Map<String, Object> rmap) {
			try {
				// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
				initMap(rmap);

				String xz = getValue(this.getForm().get("xz"));
				String tableName =getValue(this.getForm().get("tableName"));
				 tableName = tableName.toLowerCase();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				c.add(Calendar.MONTH, -1);
				Date m = c.getTime();
				String rkrq = format.format(m);
				
				
				String sql="select "+xz+" from "+tableName+" where rk_rq='"+rkrq+"' group by "+xz;
				List<Map<String, Object>> sjjgall = this.getBs().query(sql);
				
				List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
				if (sjjgall!=null) {
					Map<String, String> map;
					for (int i = 0; i < sjjgall.size(); i++) {
						map = new HashMap<String, String>();
						for (String key : sjjgall.get(i).keySet()) {
							map.put(key.toLowerCase(), sjjgall.get(i).get(key).toString());
						}
						list1.add(map);
					}
					return this.toJson("000", "查询成功！", list1);
					
				}else {
					return this.toJson("000", "查询成功！");
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询异常！");
			}
		}
		
		
		
		
	
}
