package fast.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.Super;

public class zhcxfymx extends Super{
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "sjfx/zhcxfymx";
		}catch(Exception e){
			e.printStackTrace();
			return "sjfx/zhcxfymx";
		}
	}
	
	
	public String querymbmc(Map<String, Object> rmap){
		init(rmap);
		String jd_dm = getValue(this.getRequest().getSession().getAttribute("dwid"));
		String sql = "select mbmc,u_id from FAST_ZDYXX where mblx='定制企业模板' and zt='1' and jd_dm="+jd_dm;
		List<Map<String, Object>> mbmc = this.getBs().query(sql);
		return this.toJson("000", "查询成功", mbmc);
	}
	
	
	
	
	/**
	 * 月明细模板查询
	 * @author 顾勇
	 * */
	public String queryYmx(Map<String, Object> rmap){
		try{
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String jd_dm = getValue(this.getRequest().getSession().getAttribute("dwid")); 
			String sql_jddm="";
			if("00".equals(jd_dm)){
				sql_jddm=" and 1=1";
			}else{
				sql_jddm=" and jd_dm="+jd_dm;
			}
			String page = getValue(this.getForm().get("page"));
			String pagesize = getValue(this.getForm().get("limit"));
			String mbmc = getValue(this.getForm().get("mbmc"));
			String nf=getValue(this.getForm().get("nf"));
			String sz=getValue(this.getForm().get("sz"));
			String cxlx=getValue(this.getForm().get("cxlx"));//值有分月、分税种、分月分税种
			List<Map<String,Object>> list_result=new ArrayList<Map<String,Object>>();
			String[] sz_arr=sz.split(",");
			String sql_zdyxx="select u_id,to_char(bt) bt from fast_zdyxx where mbmc='"+mbmc+"'";
			List<Map<String,Object>> list_zdyxx = this.getBs().query(sql_zdyxx);
			int total = 0;
			if(list_zdyxx.size()==1){
				String u_id=list_zdyxx.get(0).get("U_ID").toString();
				String[] bt=list_zdyxx.get(0).get("BT").toString().split(",");
				String sql_zdydr = "select * from fast_zdydr where U_ID='"+u_id+"'";
				total = this.getBs().query(sql_zdydr).size();
				String sql_fy = "select * from (select row_.*, rownum rowno from ("+sql_zdydr+") row_ where rownum <= " + pagesize + "*"
						+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize + " + 1";
				List<Map<String,Object>> list_zdydr = this.getBs().query(sql_fy);				
				for(int i=0;i<list_zdydr.size();++i){
					Map<String,Object> map = new HashMap<>();
					Map<String,Object> map_zdydr = list_zdydr.get(i);
					System.out.println(bt.length);
					for(int m=0;m<bt.length;++m){
						map.put(bt[m], map_zdydr.get("A"+(m+1)).equals("null")||map_zdydr.get("A"+(m+1))==null?"":map_zdydr.get("A"+(m+1)));
					}
					if(cxlx.equals("分月")){
						String sql_query = "select sum(dfzse) se,to_char(rk_rq,'yyyymm') rq from sb_nsrxx where to_char(rk_rq,'yyyy')='"+nf+"' and nsrmc='"+list_zdydr.get(i).get("A1").toString().trim()+"'"+sql_jddm+" group by rk_rq,nsrmc";
						List<Map<String,Object>> list_query=this.getBs().query(sql_query);
						System.out.println(sql_query);
						for(int n=1;n<=12;++n){
							if(list_query == null||list_query.size()<1){
								map.put(n+"month", 0.00);
							}else{
								int flag=0;
								for(Map<String,Object> map_each : list_query){
									if(n==Integer.parseInt(map_each.get("RQ").toString().replaceAll("^[0-9]{4}0?", ""))){
										flag=1;
										map.put(n+"month", map_each.get("SE"));
									}
								}
								if(flag==0){
									map.put(n+"month", 0.00);
								}
							}
						}			
					}else if(cxlx.equals("分税种")){
						System.out.println(sz_arr.toString());
						for(int j=0;j<sz_arr.length;++j){
							String sz_jc=sz_arr[j];
							if(sz_jc.equals("")){
								continue;
							}
							String sql_query = "select sum("+sz_jc+"*bl/100) se from sb_nsrxx where to_char(rk_rq,'yyyy')='"+nf+"' and nsrmc='"+list_zdydr.get(i).get("A1").toString().trim()+"' "+sql_jddm+" group by nsrmc";
							List<Map<String,Object>> list_query=this.getBs().query(sql_query);
							System.out.println(sql_query);
							if(list_query == null||list_query.size()<1){
								map.put(sz_jc, 0.00);
							}else{
								map.put(sz_jc, list_query.get(0).get("SE"));
							}											
						}
					}else if(cxlx.equals("分月分税种")){
						for(int j=0;j<sz_arr.length;++j){
							String sz_jc=sz_arr[j];
							if(sz_jc.equals("")){
								continue;
							}
							String sql_query = "select sum("+sz_jc+"*bl/100) se,to_char(rk_rq,'yyyymm') rq from sb_nsrxx where to_char(rk_rq,'yyyy')='"+nf+"' and nsrmc='"+list_zdydr.get(i).get("A1").toString().trim()+"' "+sql_jddm+" group by rk_rq,nsrmc";
							List<Map<String,Object>> list_query=this.getBs().query(sql_query);
							System.out.println(sql_query);
							for(int n=1;n<=12;++n){
								if(list_query == null||list_query.size()<1){
									map.put(sz_jc+n+"month", 0.00);
								}else{
									int flag=0;
									for(Map<String,Object> map_each : list_query){
										if(n==Integer.parseInt(map_each.get("RQ").toString().replaceAll("^[0-9]{4}0?", ""))){
											flag=1;
											map.put(sz_jc+n+"month", map_each.get("SE"));
										}
									}
									if(flag==0){
										map.put(sz_jc+n+"month", 0.00);
									}
								}
							}					
						}
					}
					list_result.add(map);
				}
			}
			System.out.println(list_result.size());
			System.out.println(list_result);
			return this.toJson("000", "查询成功！", list_result, total);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	
	
	
	public String getTitle(Map<String, Object> rmap){
		init(rmap);
		
		List<Map<String,String>> listys = new ArrayList<Map<String,String>>();
		
		String sz = getValue(this.getForm().get("sz"));
		String szzw = getValue(this.getForm().get("szzw"));
		String mbmc = getValue(this.getForm().get("mbmc"));
		String radiolx = getValue(this.getForm().get("radiolx"));
	    
		if(radiolx.equals("分月分税种")){
			
			Map<String, Object> rs1 = this.getBs().queryOne("select to_char(bt) bt from FAST_ZDYXX where mbmc = '" + mbmc + "'");
			String head = rs1.get("BT") == null ? "" : rs1.get("BT").toString();
			String[] arr = head.split(",");
			
			for(int i = 0;i<arr.length;i++){
				String field = arr[i];
				String title = arr[i];
				Map<String,String> map = new LinkedHashMap<String,String>();
				map.put("field", field);
				map.put("title", title);
				map.put("rowspan", "2");
				if(title.equals("纳税人名称") || title.equals("纳税人识别号")){
					map.put("width", "300");
				}else if(title.length()<=5){
					map.put("width", "150");
				}else{
					map.put("width", "200");
				}
				//map.put("align", "center");
				listys.add(map);
			}
			
			for(int i=0;i<sz.split(",").length;i++){
				   
				   Map<String,String> maprow = new LinkedHashMap<String,String>();
				   maprow.put("field", sz.split(",")[i]);
				   maprow.put("title", szzw.split(",")[i]);
				   maprow.put("colspan", "12");
				   maprow.put("align", "center");
				   listys.add(maprow);
			}
			
		}else if(radiolx.equals("分月")){
			
			//获取之前的内容
			Map<String, Object> rs1 = this.getBs().queryOne("select to_char(bt) bt from FAST_ZDYXX where mbmc = '" + mbmc + "'");
			String head = rs1.get("BT") == null ? "" : rs1.get("BT").toString();
			String[] arr = head.split(",");
			for(int i = 0;i<arr.length;i++){
				String field = arr[i];
				String title = arr[i];
				Map<String,String> map = new LinkedHashMap<String,String>();
				map.put("field", field);
				map.put("title", title);
				if(title.equals("纳税人名称") || title.equals("纳税人识别号")){
					map.put("width", "300");
				}else if(title.length()<=5){
					map.put("width", "150");
				}else{
					map.put("width", "200");
				}
				//map.put("align", "center");
				listys.add(map);
			}
			
			
			for(int i=1;i<13;i++){
				
				Map<String,String> mapsed = new LinkedHashMap<String,String>();
				  
				mapsed.put("field", Integer.toString(i)+"month");
			    mapsed.put("title", Integer.toString(i)+"月");
			    mapsed.put("width", "120");
			    mapsed.put("align", "right");
			    listys.add(mapsed);
			}
		}else{
			//获取之前的内容
			Map<String, Object> rs1 = this.getBs().queryOne("select to_char(bt) bt from FAST_ZDYXX where mbmc = '" + mbmc + "'");
			String head = rs1.get("BT") == null ? "" : rs1.get("BT").toString();
			String[] arr = head.split(",");
			for(int i = 0;i<arr.length;i++){
				String field = arr[i];
				String title = arr[i];
				Map<String,String> map = new LinkedHashMap<String,String>();
				map.put("field", field);
				map.put("title", title);
				if(title.equals("纳税人名称") || title.equals("纳税人识别号")){
					map.put("width", "300");
				}else if(title.length()<=5){
					map.put("width", "150");
				}else{
					map.put("width", "200");
				}
				//map.put("align", "center");
				listys.add(map);
			}
			
			
			for(int i=0;i<sz.split(",").length;i++){
				   
				   Map<String,String> map = new LinkedHashMap<String,String>();
				   map.put("field", sz.split(",")[i]);
				   map.put("title", szzw.split(",")[i]);
				   map.put("width", "150");
				   map.put("align", "right");
				   listys.add(map);
			}
			
			
		}
		
		return this.toJson("000", "查询成功", listys);
		
	}
	
	
	public String getTitle1(Map<String, Object> rmap){
		init(rmap);
		
		List<Map<String,String>> listserow = new ArrayList<Map<String,String>>();
		
		String sz = getValue(this.getForm().get("sz"));
		String szzw = getValue(this.getForm().get("szzw"));
		String mbmc = getValue(this.getForm().get("mbmc"));
		String radiolx = getValue(this.getForm().get("radiolx"));
		
		for(int i=0;i<sz.split(",").length;i++){
			   
			   for(int m=1;m<13;m++){
			     
			     Map<String,String> mapsed = new LinkedHashMap<String,String>();
			     
			     mapsed.put("field", sz.split(",")[i]+Integer.toString(m)+"month");
			     mapsed.put("title", Integer.toString(m)+"月");
			     mapsed.put("width", "150");
			     mapsed.put("align", "right");
			     listserow.add(mapsed);
			     
			   }
		}
		
		
		return this.toJson("000", "查询成功", listserow);
		
	}
	
	// 导出excel
		 public Object export(Map<String, Object> rmap) {	 
			 List<Map<String, Object>> list_result = getData(rmap);	
			 
			 //获取表头
			 List<Map<String, String>> bt = getBT(rmap); 
			 String[] cols=new String[bt.size()];
			 System.out.println("111111111111");
			 System.out.println(bt);
			 System.out.println(list_result);
			
			String[] keys = new String[bt.size()];
			Map<String, Object> map1 = new HashMap<String, Object>();
			for (int i = 0; i < bt.size(); i++) {
				cols[i] = bt.get(i).get("title");
				keys[i] = bt.get(i).get("field");

			}
			map1.put("fileName", "cew.xls");
			map1.put("cols", cols);
			map1.put("keys", keys);
			map1.put("list", list_result);
			System.out.println("---------------"+map1);

			 return map1;	   
		 }
		 
		 public List<Map<String, Object>> getData(Map<String, Object> rmap){
				// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
				initMap(rmap);
				String jd_dm = getValue(this.getRequest().getSession().getAttribute("dwid"));
				String sql_jddm="";
				if("00".equals(jd_dm)){
					sql_jddm=" and 1=1";
				}else{
					sql_jddm=" and jd_dm="+jd_dm;
				}
				String mbmc = getValue(this.getForm().get("mbmc"));
				String nf=getValue(this.getForm().get("nf"));
				String sz=getValue(this.getForm().get("sz"));
				String cxlx=getValue(this.getForm().get("radiolx"));//值有分月、分税种、分月分税种
				List<Map<String,Object>> list_result=new ArrayList<Map<String,Object>>();
				String[] sz_arr=sz.split(",");

				String sql_zdyxx="select u_id,to_char(bt) bt from fast_zdyxx where mbmc='"+mbmc+"'";
				List<Map<String,Object>> list_zdyxx = this.getBs().query(sql_zdyxx);
				int total = 0;
				if (list_zdyxx.size() == 1) {
					String u_id = list_zdyxx.get(0).get("U_ID").toString();
					String[] bt = list_zdyxx.get(0).get("BT").toString().split(",");
					String sql_zdydr = "select * from fast_zdydr where U_ID='" + u_id + "'";
					total = this.getBs().query(sql_zdydr).size();
					
					List<Map<String, Object>> list_zdydr = this.getBs().query(sql_zdydr);
					//写入模板标题和数据
					String sql ="select A1 from fast_zdydr where U_ID='" + u_id + "'";
					String sql2 = "";
					for (int i = 0; i < list_zdydr.size(); ++i) {
						Map<String, Object> map = new HashMap<>();
						Map<String, Object> map_zdydr = list_zdydr.get(i);
						System.out.println(bt.length);
						for (int m = 0; m < bt.length; ++m) {
							map.put(bt[m],
									map_zdydr.get("A" + (m + 1)).equals("null") || map_zdydr.get("A" + (m + 1)) == null ? ""
											: map_zdydr.get("A" + (m + 1)));
						}
						
						//sql += "'" + list_zdydr.get(i).get("A1").toString().trim() + "',";
						
						list_result.add(map);
						
					}
					
					for (int j = 0; j < sz_arr.length; ++j) {
						String sz_jc = sz_arr[j];
						if (sz_jc.equals("")) {
							continue;
						}
						sql2 += " sum(" + sz_jc+ "*bl/100) "+sz_jc+",";
						
					}
					List<Map<String, Object>> list_query = null;
					String sql_query = "";
					if (cxlx.equals("分月")) {//.substring(0,sql.length()-1)
						sql_query = "select sum(dfzse) se,to_char(rk_rq,'yyyymm') rq,nsrmc from sb_nsrxx where to_char(rk_rq,'yyyy')='"
								+ nf + "' and nsrmc in (" +sql  + ") "+ sql_jddm
								+ " group by rk_rq,nsrmc";
						 list_query = this.getBs().query(sql_query);
						System.out.println(sql_query);

					} else if (cxlx.equals("分税种")) {
						System.out.println(sz_arr.toString());
					
							sql_query = "select "+sql2
									+ " nsrmc from sb_nsrxx where to_char(rk_rq,'yyyy')='" + nf + "' and nsrmc in ("
									+ sql  + ") " + sql_jddm
									+ " group by nsrmc";
							 list_query = this.getBs().query(sql_query);
							System.out.println(sql_query);

					} else if (cxlx.equals("分月分税种")) {
						sql_query = "select "+sql2
								+ " to_char(rk_rq,'yyyymm') rq,nsrmc from sb_nsrxx where to_char(rk_rq,'yyyy')='"
								+ nf + "' and nsrmc in (" + sql  + ") "
								+ sql_jddm + " group by rk_rq,nsrmc";
						list_query = this.getBs().query(sql_query);

					}
					
					
					
						for (Map<String, Object> map : list_result) {//头
							for (Map<String, Object> ma2 : list_query) {//数据
								if(!(getValue(map.get("纳税人名称")).equals(getValue(ma2.get("NSRMC"))))) {
									
									continue;
								}
								if (cxlx.equals("分月")) {
									
									for (int n = 1; n <= 12; ++n) {
										if (list_query == null||list_query.size()<1) {
											map.put(n + "month", 0.00);
										} else {
											int flag = 0;
											
												if (n == Integer.parseInt(ma2.get("RQ").toString().replaceAll("^[0-9]{4}0?", ""))) {
													flag = 1;
													map.put(n + "month", ma2.get("SE"));
												}
											
											if (flag == 0) {
												map.put(n + "month", 0.00);
											}
										}
									}
								} else if (cxlx.equals("分税种")) {
									
									for (int j = 0; j < sz_arr.length; ++j) {
										String sz_jc = sz_arr[j];
										if (sz_jc.equals("")) {
											continue;
										}
										if (list_query == null||list_query.size()<1) {
											map.put(sz_jc, 0.00);
										} else {
											
											map.put(sz_jc, ma2.get(sz_jc.toUpperCase()));
											
										}
									}
								} else if (cxlx.equals("分月分税种")) {
								
									for (int j = 0; j < sz_arr.length; ++j) {
										String sz_jc = sz_arr[j];
										if (sz_jc.equals("")) {
											continue;
										}
										//System.out.println(sql_query);
										for (int n = 1; n <= 12; ++n) {
											if (list_query == null||list_query.size()<1) {
												map.put(sz_jc + n + "month", 0.00);
											} else {
												int flag = 0;
													if (n == Integer.parseInt(
															ma2.get("RQ").toString().replaceAll("^[0-9]{4}0?", ""))) {
														flag = 1;
														map.put(sz_jc + n + "month",ma2.get(sz_jc.toUpperCase()));
													}
												if (flag == 0) {
													map.put(sz_jc + n + "month", 0.00);
												}
											}
										}
									}
								}
							}
						
					}
					
					

					
					
				}
				return list_result;
			 }
		
		 public List<Map<String, String>> getBT(Map<String, Object> rmap){
				init(rmap);		
				List<Map<String,String>> listys = new ArrayList<Map<String,String>>();
				String sz = getValue(this.getForm().get("sz"));
				String szzw = getValue(this.getForm().get("szzw"));
				String mbmc = getValue(this.getForm().get("mbmc"));
				String radiolx = getValue(this.getForm().get("radiolx")); 
				
				if(radiolx.equals("分月分税种")){
					
					Map<String, Object> rs1 = this.getBs().queryOne("select to_char(bt) bt from FAST_ZDYXX where mbmc = '" + mbmc + "'");
					String head = rs1.get("BT") == null ? "" : rs1.get("BT").toString();
					String[] arr = head.split(",");
					
					for(int i = 0;i<arr.length;i++){
						String field = arr[i];
						String title = arr[i];
						Map<String,String> map = new LinkedHashMap<String,String>();
						map.put("field", field);
						map.put("title", title);
						map.put("rowspan", "2");
						if(title.equals("纳税人名称") || title.equals("纳税人识别号")){
							map.put("width", "300");
						}else if(title.length()<=5){
							map.put("width", "150");
						}else{
							map.put("width", "200");
						}
						map.put("align", "center");
						listys.add(map);
					}
					
					for(int i=0;i<sz.split(",").length;i++){
						   
						   Map<String,String> maprow = new LinkedHashMap<String,String>();
						   maprow.put("field", sz.split(",")[i]);
						   maprow.put("title", szzw.split(",")[i]);
						   maprow.put("colspan", "12");
						   maprow.put("align", "center");
						   listys.add(maprow);
					}
					
				}else if(radiolx.equals("分月")){
					
					//获取之前的内容
					Map<String, Object> rs1 = this.getBs().queryOne("select to_char(bt) bt from FAST_ZDYXX where mbmc = '" + mbmc + "'");
					String head = rs1.get("BT") == null ? "" : rs1.get("BT").toString();
					String[] arr = head.split(",");
					for(int i = 0;i<arr.length;i++){
						String field = arr[i];
						String title = arr[i];
						Map<String,String> map = new LinkedHashMap<String,String>();
						map.put("field", field);
						map.put("title", title);
						if(title.equals("纳税人名称") || title.equals("纳税人识别号")){
							map.put("width", "300");
						}else if(title.length()<=5){
							map.put("width", "150");
						}else{
							map.put("width", "200");
						}
						map.put("align", "center");
						listys.add(map);
					}
					
					
					for(int i=1;i<13;i++){
						
						Map<String,String> mapsed = new LinkedHashMap<String,String>();
						  
						mapsed.put("field", Integer.toString(i)+"month");
					    mapsed.put("title", Integer.toString(i)+"月");
					    mapsed.put("width", "120");
					    listys.add(mapsed);
					}
				}else{
					//获取之前的内容
					Map<String, Object> rs1 = this.getBs().queryOne("select to_char(bt) bt from FAST_ZDYXX where mbmc = '" + mbmc + "'");
					String head = rs1.get("BT") == null ? "" : rs1.get("BT").toString();
					String[] arr = head.split(",");
					for(int i = 0;i<arr.length;i++){
						String field = arr[i];
						String title = arr[i];
						Map<String,String> map = new LinkedHashMap<String,String>();
						map.put("field", field);
						map.put("title", title);
						if(title.equals("纳税人名称") || title.equals("纳税人识别号")){
							map.put("width", "300");
						}else if(title.length()<=5){
							map.put("width", "150");
						}else{
							map.put("width", "200");
						}
						map.put("align", "center");
						listys.add(map);
					}	
					for(int i=0;i<sz.split(",").length;i++){
						   
						   Map<String,String> map = new LinkedHashMap<String,String>();
						   map.put("field", sz.split(",")[i]);
						   map.put("title", szzw.split(",")[i]);
						   map.put("width", "150");
						   listys.add(map);
					}						
				}		
				return listys;	
			}
	
	
}
