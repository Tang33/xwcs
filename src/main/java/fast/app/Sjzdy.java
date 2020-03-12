package fast.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;

import fast.main.util.ExcelRead;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

public class Sjzdy extends Super {
	private static Connection connection = null;
	private Map<String, Object> user = null;

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			
			return "sjfx/Sjzdy";
		} catch (Exception e) {
			e.printStackTrace();
			return "sjfx/Sjzdy";
		}
	}
	
	public static String getSz(String sz){
		String result = "";
		if(sz.equals("增值税")){
			result = "ZZS";
		}else if(sz.equals("营业税")){
			result = "YYS";
		}else if(sz.equals("个人所得税")){
			result = "GRSDS";
		}else if(sz.equals("房产税")){
			result = "FCS";
		}else if(sz.equals("印花税")){
			result = "YHS";
		}else if(sz.equals("车船税")){
			result = "CCS";
		}else if(sz.equals("企业所得税")){
			result = "QYSDS";
		}else if(sz.equals("营改增增值税")){
			result = "YGZZZS";
		}else if(sz.equals("城市维护建设税")){
			result = "CSWHJSS";
		}else if(sz.equals("地方教育附加")){
			result = "DFJYFJ";
		}else if(sz.equals("教育附加")){
			result = "JYFJ";
		}else if(sz.equals("城镇土地使用税")){
			result = "CZTDSYS";
		}else if(sz.equals("环保税")){
			result = "HBS";
		}
		return result;
	}
	
	public double parse(double d){
		BigDecimal b = new BigDecimal(d);
		d = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return d;
	}
	
	/**
	 * 获取定制企业模板名称
	 * */
	public String queryDzqymb(Map<String, Object> rmap){
		init(rmap);
		String jd_dm=getValue(this.getRequest().getSession().getAttribute("dwid"));
		
		String sql = "select mbmc,u_id from FAST_ZDYXX where mblx='定制企业模板' and zt='1' and jd_dm="+jd_dm;
		List<Map<String, Object>> mbmc = this.getBs().query(sql);
		return this.toJson("000", "查询成功", mbmc);
	}
	
	/**
	 * 获取定制查询模板名称
	 * */
	public String queryDzcxmb(Map<String, Object> rmap){
		init(rmap);
		String jd_dm=getValue(this.getRequest().getSession().getAttribute("dwid"));
		String sql = "select mbmc,u_id from FAST_ZDYXX where mblx='定制查询模板' and zt='1'and jd_dm="+jd_dm;
		List<Map<String, Object>> mbmc = this.getBs().query(sql);
		return this.toJson("000", "查询成功", mbmc);
	}
	
	/**
	 * 根据定制企业模板与定制查询模板查询数据
	 * */
	public String queryDataByMB(Map<String, Object> rmap){
		try{
			init(rmap);
			List<Map<String,Object>> list_result=new ArrayList<Map<String,Object>>();
			String jd_dm=getValue(this.getRequest().getSession().getAttribute("dwid"));
			String sql_jddm="";
			if("00".equals(jd_dm)){
				sql_jddm=" and 1=1";
			}else{
				sql_jddm=" and jd_dm="+jd_dm;
			}
			String page = getValue(this.getForm().get("page"));
			String pagesize = getValue(this.getForm().get("limit"));
			String dzqymb_id = getValue(this.getForm().get("dzqymb_id"));
			String dzcxmb_id = getValue(this.getForm().get("dzcxmb_id"));
//			String sql_rk_rq_max = "select to_char(max(rk_rq),'yyyymm') rkrq_max from sb_nsrxx";
//			String rkrq_max = this.getBs().queryOne(sql_rk_rq_max).get("RKRQ_MAX").toString();
			String sql_rk_rq_max_tq = "select to_char(add_months((select max(rk_rq) from sb_nsrxx),-12),'yyyymm') rkrq_max_tq from dual";
			String rkrq_max_tq = this.getBs().queryOne(sql_rk_rq_max_tq).get("RKRQ_MAX_TQ").toString();
			
			//根据模板id获取定制查询模板相关数据
			String sql_dzcxmb = "select * from fast_dzcxmb where u_id='"+dzcxmb_id+"'";
			List<Map<String,Object>> list_dzcxmb = this.getBs().query(sql_dzcxmb);
			//根据模板id获取定制企业模板相关数据
			String sql_zdyxx="select u_id,to_char(bt) bt from fast_zdyxx where u_id='"+dzqymb_id+"'";
			List<Map<String,Object>> list_zdyxx = this.getBs().query(sql_zdyxx);
			String u_id=list_zdyxx.get(0).get("U_ID").toString();
			String[] bt=list_zdyxx.get(0).get("BT").toString().split(",");
			String sql_zdydr = "select * from fast_zdydr where U_ID='"+u_id+"'";
			int total = this.getBs().query(sql_zdydr).size();
			String sql_fy = "select * from (select row_.*, rownum rowno from ("+sql_zdydr+") row_ where rownum <= " + pagesize + "*"
					+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize + " + 1";
			List<Map<String,Object>> list_zdydr = this.getBs().query(sql_fy);
			for(int i=0;i<list_zdydr.size();++i){
				Map<String,Object> map = new HashMap<>();
				Map<String,Object> map_zdydr = list_zdydr.get(i);
				Map<String,Object> map_xh = new HashMap<>();
				for(int m=0;m<bt.length;++m){
					map.put(bt[m], map_zdydr.get("A"+(m+1)).equals("null")||map_zdydr.get("A"+(m+1))==null?"":map_zdydr.get("A"+(m+1)));
				}

				System.out.println(map.get("纳税人名称"));
				String nsrmc = map.get("纳税人名称").toString().trim();
				labelA :for(Map<String,Object> map_cxx:list_dzcxmb){
					String cxxmc = map_cxx.get("CXXMC").toString().trim();
					String sfwgs = map_cxx.get("SFWGS").toString();
					String xh = map_cxx.get("XH").toString();
					String nf = map_cxx.get("NF")==null?"":map_cxx.get("NF").toString();
					String yfq = map_cxx.get("YFQ")==null?"":map_cxx.get("YFQ").toString();
					String yfz = map_cxx.get("YFZ")==null?"":map_cxx.get("YFZ").toString();
					String sz = map_cxx.get("SZ")==null?"":map_cxx.get("SZ").toString();
					String kj = map_cxx.get("KJ")==null?"":map_cxx.get("KJ").toString();
					if(!sfwgs.equals("Y")){
						String sql_cxx = "select ";
						//sz包含,表示为小计
						if (cxxmc.contains("所属街道")) {
							 String JDsql = "SELECT JD_MC AS JD FROM DM_JD A ,(select DISTINCT JD_DM from sb_nsrxx where nsrmc='"+nsrmc+"')  B WHERE A.JD_DM = B.JD_DM";
							 if (this.getBs().queryOne(JDsql) != null) {
								 Object obj_jd = this.getBs().queryOne(JDsql).get("JD");
								 map.put(cxxmc, obj_jd.toString());	 
							 } else {
								 map.put(cxxmc, "");	 
							 }
							 continue labelA; 
						} else if(sz.contains(",")){
							String[] szs = sz.split(",");
							for(int n=0;n<szs.length;n++){
								szs[n] = getSz(szs[n]);
							}
							if(kj.equals("地方口径")){
								for(int j=0;j<szs.length;j++){
									if(j==0){
										sql_cxx = sql_cxx+"sum("+szs[j]+"*bl/100)";
									}else{
										sql_cxx = sql_cxx+"+sum("+szs[j]+"*bl/100)";
									}
								}
							}else if(kj.equals("全口径")){
								for(int j=0;j<szs.length;j++){
									if(j==0){
										sql_cxx = sql_cxx+"sum("+szs[j]+")";
									}else{
										sql_cxx = sql_cxx+"+sum("+szs[j]+")";
									}
								}
							}
							sql_cxx = sql_cxx+" as je from sb_nsrxx where nsrmc='"+nsrmc+"' and ";
							if(yfq.equals("")){
								if(cxxmc.contains("同期") && nf.equals(rkrq_max_tq.substring(0, 4))){
									sql_cxx = sql_cxx+"rk_rq>=to_date('"+rkrq_max_tq.substring(0, 4)+"01','yyyymm') and rk_rq<=to_date('"+rkrq_max_tq+"','yyyymm')";
								}else{
									sql_cxx = sql_cxx+"to_char(rk_rq,'yyyy')='"+nf+"'";
								}
							}else{
								if(yfz.equals("")){
									sql_cxx = sql_cxx+"to_char(rk_rq,'yyyymm')='"+nf+yfq+"'";
								}else{
									sql_cxx = sql_cxx+"rk_rq>=to_date('"+nf+yfq+"','yyyymm') and rk_rq<=to_date('"+nf+yfz+"','yyyymm')";
								}
							}
						}else{
							if(kj.equals("地方口径")){
								if(sz.equals("")){
									sql_cxx = sql_cxx+"sum(dfzse)";
								}else{
									sql_cxx = sql_cxx+"sum("+getSz(sz)+"*bl/100)";
								}
							}else if(kj.equals("全口径")){
								if(sz.equals("")){
									sql_cxx = sql_cxx+"sum(zse)";
								}else{
									sql_cxx = sql_cxx+"sum("+getSz(sz)+")";
								}
							}
							sql_cxx = sql_cxx+" as je from sb_nsrxx where nsrmc='"+nsrmc+"' and ";
							if(yfq.equals("")){
								if(cxxmc.contains("同期") && nf.equals(rkrq_max_tq.substring(0, 4))){
									sql_cxx = sql_cxx+"rk_rq>=to_date('"+rkrq_max_tq.substring(0, 4)+"01','yyyymm') and rk_rq<=to_date('"+rkrq_max_tq+"','yyyymm')";
								}else{
									sql_cxx = sql_cxx+"to_char(rk_rq,'yyyy')='"+nf+"'";
								}
							}else{
								if(yfz.equals("")){
									sql_cxx = sql_cxx+"to_char(rk_rq,'yyyymm')='"+nf+yfq+"'";
								}else{
									sql_cxx = sql_cxx+"rk_rq>=to_date('"+nf+yfq+"','yyyymm') and rk_rq<=to_date('"+nf+yfz+"','yyyymm')";
								}
							}
						}
						
						
						
						sql_cxx = sql_cxx+sql_jddm;
						System.out.println(sql_cxx);
						Object obj_je = this.getBs().queryOne(sql_cxx).get("JE");
						if(obj_je == null || obj_je.toString().equals("0")){
							obj_je="0.00";
						}
						BigDecimal se = getBigDecimal(obj_je);
						map.put(cxxmc, se);
						map_xh.put(xh, se);
					}
				}
				for(Map<String,Object> map_cxx:list_dzcxmb){
					String sfwgs = map_cxx.get("SFWGS").toString();
					String cxxmc = map_cxx.get("CXXMC").toString().trim();
					if(sfwgs.equals("Y")){
						String gs = map_cxx.get("GS")==null?"":map_cxx.get("GS").toString().trim();
						Matcher mtc = Pattern.compile("列[0-9]{1,}").matcher(gs);
						while(mtc.find()){
							if(map_xh.get(mtc.group(0)) == null){
								gs="";
								break;
							}
							gs = gs.replace(mtc.group(0), map_xh.get(mtc.group(0)).toString());
							System.out.println("--------------");
							System.out.println(gs);
						}
						if(gs.equals("")){
							map.put(cxxmc, "公式有误！");
						}else{
							JexlEngine jexlEngine = new JexlBuilder().create();
							JexlExpression jexlExpression = jexlEngine.createExpression(gs);
							Matcher mtc_gscl = Pattern.compile("/0\\.00[^0-9]{1}").matcher(gs);
							if(mtc_gscl.find()){
								map.put(cxxmc, "除数为0");
							}else{
								Object evaluate = jexlExpression.evaluate(null);
								BigDecimal je_gs = getBigDecimal(evaluate).setScale(2, RoundingMode.HALF_UP);;
								map.put(cxxmc, je_gs);
							}
						}
					}
				}
				list_result.add(map);
			}
			System.out.println(list_result.toString()+"000000000");
			return this.toJson("000", "查询成功！",list_result,total);
		}catch(Exception e){
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
	
	public String getTitle(Map<String, Object> rmap){
		init(rmap);
		String dzqymb_id = getValue(this.getForm().get("dzqymb_id"));
		String dzcxmb_id = getValue(this.getForm().get("dzcxmb_id"));
		List<Map<String, Object>> rs = null;
		if(!dzcxmb_id.equals("请选择")){
			rs = this.getBs().query("select cxxmc from FAST_DZCXMB where u_id = '" + dzcxmb_id + "'");	
		}
		Map<String, Object> rs1 = this.getBs().queryOne("select to_char(bt) bt from FAST_ZDYXX where u_id = '" + dzqymb_id + "'");
		String head = rs1.get("BT") == null ? "" : rs1.get("BT").toString();
		String[] arr = head.split(",");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(int i = 0;i<arr.length;i++){
			String field = arr[i];
			String title = arr[i];
			Map<String,String> map = new LinkedHashMap<String,String>();
			map.put("field", field);
			map.put("title", title);
			if(title.equals("纳税人名称")){
				map.put("width", "330");
			}else if(title.length()<=10){
				map.put("width", "163");
			}else if(title.length()>10 && title.length()<=20){
				map.put("width", "330");
			}else{
				map.put("width", "400");
			}
			list.add(map);
		}
		if(rs != null){
			for(int i = 0;i<rs.size();i++){
				String field = rs.get(i).get("CXXMC").toString();
				String title = rs.get(i).get("CXXMC").toString();
				Map<String,String> map = new LinkedHashMap<String,String>();
				map.put("field", field);
				map.put("title", title);
				map.put("align", "right");
				if(title.length()<=10){
					map.put("width", "163");
				}else if(title.length()>10 && title.length()<=20){
					map.put("width", "330");
				}else{
					map.put("width", "400");
				}
				list.add(map);
			}
		}
		System.out.println("23456"+list.toString());
		return this.toJson("000", "查询成功", list);
	}

	
	// 导出excel
		 public Object export(Map<String, Object> rmap) {
			 
			 
			 List<Map<String, Object>> list_result = getData(rmap);
			 System.out.println(list_result+"1111111111");
			 
			 //获取表头
			
			 List<Map<String, String>> bt = getBT(rmap);
			 System.out.println("bt"+bt);
			 String[] cols=new String[bt.size()];
			 String[] keys = new String[bt.size()];
			 Map<String, Object> map1 = new HashMap<String, Object>();
			 
			 
			
			 for(int i=0;i<bt.size();i++){
				 cols[i]=bt.get(i).get("field");
				 keys[i]=bt.get(i).get("field");
				 
			 }
			 map1.put("fileName", "年度明细查询.xls");
			 map1.put("cols", cols);
			 map1.put("keys", keys);
			 map1.put("list", list_result);
			 return map1;
		
			    
		 }
		 
		 private List<Map<String, String>> getBT(Map<String, Object> rmap) {
				// TODO Auto-generated method stub
				init(rmap);
				String dzqymb_id = getValue(this.getForm().get("dzqymb_id"));
				String dzcxmb_id = getValue(this.getForm().get("dzcxmb_id"));
				List<Map<String, Object>> rs = null;
				if(!dzcxmb_id.equals("请选择")){
					rs = this.getBs().query("select cxxmc from FAST_DZCXMB where u_id = '" + dzcxmb_id + "'");	
				}
				Map<String, Object> rs1 = this.getBs().queryOne("select to_char(bt) bt from FAST_ZDYXX where u_id = '" + dzqymb_id + "'");
				String head = rs1.get("BT") == null ? "" : rs1.get("BT").toString();
				String[] arr = head.split(",");
				List<Map<String,String>> list = new ArrayList<Map<String,String>>();
				for(int i = 0;i<arr.length;i++){
					String field = arr[i];
					String title = arr[i];
					Map<String,String> map = new LinkedHashMap<String,String>();
					map.put("field", field);
					map.put("title", title);
					if(title.equals("纳税人名称")){
						map.put("width", "330");
					}else if(title.length()<=10){
						map.put("width", "163");
					}else if(title.length()>10 && title.length()<=20){
						map.put("width", "330");
					}else{
						map.put("width", "400");
					}
					list.add(map);
				}
				if(rs != null){
					for(int i = 0;i<rs.size();i++){
						String field = rs.get(i).get("CXXMC").toString();
						String title = rs.get(i).get("CXXMC").toString();
						Map<String,String> map = new LinkedHashMap<String,String>();
						map.put("field", field);
						map.put("title", title);
						
						if(title.length()<=10){
							map.put("width", "163");
						}else if(title.length()>10 && title.length()<=20){
							map.put("width", "330");
						}else{
							map.put("width", "400");
						}
						list.add(map);
					}
				}
				
				
				return list;
				
			}
		 
		 private List<Map<String, Object>> getData(Map<String, Object> rmap) {
				// TODO Auto-generated method stub
				init(rmap);
				List<Map<String,Object>> list_result=new ArrayList<Map<String,Object>>();
				
				String jd_dm=getValue(this.getRequest().getSession().getAttribute("dwid"));
				String sql_jddm="";
				if("00".equals(jd_dm)){
					sql_jddm=" and 1=1";
				}else{
					sql_jddm=" and jd_dm="+jd_dm;
				}
				String page = getValue(this.getForm().get("page"));
				String pagesize = getValue(this.getForm().get("limit"));
				String dzqymb_id = getValue(this.getForm().get("dzqymb_id"));
				String dzcxmb_id = getValue(this.getForm().get("dzcxmb_id"));
//				String sql_rk_rq_max = "select to_char(max(rk_rq),'yyyymm') rkrq_max from sb_nsrxx";
//				String rkrq_max = this.getBs().queryOne(sql_rk_rq_max).get("RKRQ_MAX").toString();
				String sql_rk_rq_max_tq = "select to_char(add_months((select max(rk_rq) from sb_nsrxx),-12),'yyyymm') rkrq_max_tq from dual";
				String rkrq_max_tq = this.getBs().queryOne(sql_rk_rq_max_tq).get("RKRQ_MAX_TQ").toString();
				
				//根据模板id获取定制查询模板相关数据
				String sql_dzcxmb = "select * from fast_dzcxmb where u_id='"+dzcxmb_id+"'";
				List<Map<String,Object>> list_dzcxmb = this.getBs().query(sql_dzcxmb);
				//根据模板id获取定制企业模板相关数据
				String sql_zdyxx="select u_id,to_char(bt) bt from fast_zdyxx where u_id='"+dzqymb_id+"'";
				List<Map<String,Object>> list_zdyxx = this.getBs().query(sql_zdyxx);
				String u_id=list_zdyxx.get(0).get("U_ID").toString();
				String[] bt=list_zdyxx.get(0).get("BT").toString().split(",");
				String sql_zdydr = "select * from fast_zdydr where U_ID='"+u_id+"'";
				int total = this.getBs().query(sql_zdydr).size();
//				String sql_fy = "select * from (select row_.*, rownum rowno from ("+sql_zdydr+") row_ where rownum <= " + pagesize + "*"
//						+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize + " + 1";
				List<Map<String,Object>> list_zdydr = this.getBs().query(sql_zdydr);
				for(int i=0;i<list_zdydr.size();++i){
					Map<String,Object> map = new HashMap<>();
					Map<String,Object> map_zdydr = list_zdydr.get(i);
					Map<String,Object> map_xh = new HashMap<>();
					for(int m=0;m<bt.length;++m){
						map.put(bt[m], map_zdydr.get("A"+(m+1)).equals("null")||map_zdydr.get("A"+(m+1))==null?"":map_zdydr.get("A"+(m+1)));
					}
					System.out.println(map.get("纳税人名称"));
					String nsrmc = map.get("纳税人名称").toString();
					for(Map<String,Object> map_cxx:list_dzcxmb){
						String cxxmc = map_cxx.get("CXXMC").toString();
						String sfwgs = map_cxx.get("SFWGS").toString();
						String xh = map_cxx.get("XH").toString();
						String nf = map_cxx.get("NF")==null?"":map_cxx.get("NF").toString();
						String yfq = map_cxx.get("YFQ")==null?"":map_cxx.get("YFQ").toString();
						String yfz = map_cxx.get("YFZ")==null?"":map_cxx.get("YFZ").toString();
						String sz = map_cxx.get("SZ")==null?"":map_cxx.get("SZ").toString();
						String kj = map_cxx.get("KJ")==null?"":map_cxx.get("KJ").toString();
						if(!sfwgs.equals("Y")){
							String sql_cxx = "select ";
							//sz包含,表示为小计
							if(sz.contains(",")){
								String[] szs = sz.split(",");
								for(int n=0;n<szs.length;n++){
									szs[n] = getSz(szs[n]);
								}
								if(kj.equals("地方口径")){
									for(int j=0;j<szs.length;j++){
										if(j==0){
											sql_cxx = sql_cxx+"sum("+szs[j]+"*bl/100)";
										}else{
											sql_cxx = sql_cxx+"+sum("+szs[j]+"*bl/100)";
										}
									}
								}else if(kj.equals("全口径")){
									for(int j=0;j<szs.length;j++){
										if(j==0){
											sql_cxx = sql_cxx+"sum("+szs[j]+")";
										}else{
											sql_cxx = sql_cxx+"+sum("+szs[j]+")";
										}
									}
								}
								sql_cxx = sql_cxx+" as je from sb_nsrxx where nsrmc='"+nsrmc+"' and ";
								if(yfq.equals("")){
									if(cxxmc.contains("同期") && nf.equals(rkrq_max_tq.substring(0, 4))){
										sql_cxx = sql_cxx+"rk_rq>=to_date('"+rkrq_max_tq.substring(0, 4)+"01','yyyymm') and rk_rq<=to_date('"+rkrq_max_tq+"','yyyymm')";
									}else{
										sql_cxx = sql_cxx+"to_char(rk_rq,'yyyy')='"+nf+"'";
									}
								}else{
									if(yfz.equals("")){
										sql_cxx = sql_cxx+"to_char(rk_rq,'yyyymm')='"+nf+yfq+"'";
									}else{
										sql_cxx = sql_cxx+"rk_rq>=to_date('"+nf+yfq+"','yyyymm') and rk_rq<=to_date('"+nf+yfz+"','yyyymm')";
									}
								}
							}else{
								if(kj.equals("地方口径")){
									if(sz.equals("")){
										sql_cxx = sql_cxx+"sum(dfzse)";
									}else{
										sql_cxx = sql_cxx+"sum("+getSz(sz)+"*bl/100)";
									}
								}else if(kj.equals("全口径")){
									if(sz.equals("")){
										sql_cxx = sql_cxx+"sum(zse)";
									}else{
										sql_cxx = sql_cxx+"sum("+getSz(sz)+")";
									}
								}
								sql_cxx = sql_cxx+" as je from sb_nsrxx where nsrmc='"+nsrmc+"' and ";
								if(yfq.equals("")){
									if(cxxmc.contains("同期") && nf.equals(rkrq_max_tq.substring(0, 4))){
										sql_cxx = sql_cxx+"rk_rq>=to_date('"+rkrq_max_tq.substring(0, 4)+"01','yyyymm') and rk_rq<=to_date('"+rkrq_max_tq+"','yyyymm')";
									}else{
										sql_cxx = sql_cxx+"to_char(rk_rq,'yyyy')='"+nf+"'";
									}
								}else{
									if(yfz.equals("")){
										sql_cxx = sql_cxx+"to_char(rk_rq,'yyyymm')='"+nf+yfq+"'";
									}else{
										sql_cxx = sql_cxx+"rk_rq>=to_date('"+nf+yfq+"','yyyymm') and rk_rq<=to_date('"+nf+yfz+"','yyyymm')";
									}
								}
							}
							sql_cxx = sql_cxx+sql_jddm;
							System.out.println(sql_cxx);
							BigDecimal se = getBigDecimal(this.getBs().queryOne(sql_cxx).get("JE"));
							map.put(cxxmc, se);
							map_xh.put(xh, se);
						}
					}
					for(Map<String,Object> map_cxx:list_dzcxmb){
						String sfwgs = map_cxx.get("SFWGS").toString();
						String cxxmc = map_cxx.get("CXXMC").toString();
						if(sfwgs.equals("Y")){
							String gs = map_cxx.get("GS")==null?"":map_cxx.get("GS").toString();
							Matcher mtc = Pattern.compile("列[0-9]{1,}").matcher(gs);
							while(mtc.find()){
								if(map_xh.get(mtc.group(0)) == null){
									gs="";
									break;
								}
								gs = gs.replace(mtc.group(0), map_xh.get(mtc.group(0)).toString());
							}
							if(gs.equals("")){
								map.put(cxxmc, "公式有误！");
							}else{
								JexlEngine jexlEngine = new JexlBuilder().create();
								JexlExpression jexlExpression = jexlEngine.createExpression(gs);
								Object evaluate = jexlExpression.evaluate(null);
								BigDecimal je_gs = getBigDecimal(evaluate).setScale(2, RoundingMode.HALF_UP);
								map.put(cxxmc, je_gs);
							}
						}
					}
					list_result.add(map);
				}
				return list_result;
			}
}
