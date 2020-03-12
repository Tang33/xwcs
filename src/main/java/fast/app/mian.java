package fast.app;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

public class mian extends Super {

	// 本类不用初始化，直接调用查询方法

	// 新增纳税人方法
	/**
	 * 新增纳税人查询
	 * 
	 * @param rmap
	 * @return 返回纳税人的人数，和最后录入的年月份
	 */
	public String xznsrQuery(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");	
			String sql = "select * from gh_xznsr where jd_dm='"+ssdw_dm+"'";	
			System.out.println(sql);	
			List<Map<String, Object>> list = this.getBs().query(sql);

			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	// 新增税收方法
	/**
	 * 新增税收查询
	 * 
	 * @param rmap
	 * @return 返回税收的金额，和当前月份
	 */
	public String xzssQuery(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");	
			String sql = "select sum(zse) zse,to_char(max(RK_RQ),'yyyyMM')nyf from sb_nsrxx s where s.RK_RQ=(select max(RK_RQ) from sb_nsrxx) and jd_dm='"+ssdw_dm+"'";

			System.out.println(sql);
			List<Map<String, Object>> list = this.getBs().query(sql);

			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	// 总体税收方法
	/**
	 * 总体税收
	 * 
	 * @param rmap
	 * @return 返回总体税收的金额，和当前年份
	 */
	public String ztssQuery(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");
			String sql = "select sum(zse)zse,to_char(max(RK_RQ),'yyyy')nf from sb_nsrxx s where rk_rq>=to_date((select to_char(sysdate,'yyyy') rq from dual),'yyyy') and jd_dm='"+ssdw_dm+"'";
			System.out.println(sql);
			List<Map<String, Object>> list = this.getBs().query(sql);

			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	//月总税收与同期对比折线图
	public String zxQuery(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
//			String sql = "select * from GH_SYZXT ORDER by year,MONTH";
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");
			String sql = "select * from GH_SYZXT where year=(select TO_CHAR(SYSDATE,'yyyy') from dual) and jd_dm='"+ssdw_dm+"' union select * from GH_SYZXT where year=(select TO_CHAR(SYSDATE,'yyyy')-1 from dual) and jd_dm='"+ssdw_dm+"'";
			System.out.println(sql);
			List<Map<String, Object>> list = this.getBs().query(sql);
			List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
			Map<String, String> map;
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, String>();
				for (String key : list.get(i).keySet()) {
					map.put(key.toLowerCase(), list.get(i).get(key).toString());
				}
				list1.add(map);
			}
			return this.toJson("000", "查询成功！",list1);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	// 图表生成方法
	/**
	 * 生成图表
	 * 
	 * @param rmap
	 * @return 返回结果集 GH_SYSZTJ
	 */
	public String tbQuery(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			// 先将数据进行排序
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");
			String sql = "select (se+0) value,szmc name from (select t.*,rank() over (order by (se+0) desc) mc from GH_SYSZTJ t) a where a.mc <= 3 and jd_dm='"+ssdw_dm+"' union all select sum(se) value,'其他' name from (select t.*,rank() over (order by (se+0) desc) mc from GH_SYSZTJ t) a where a.mc > 3 and jd_dm='"+ssdw_dm+"'";
			System.out.println(sql);
			List<Map<String, Object>> list = this.getBs().query(sql);
			List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
			Map<String, String> map;
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, String>();
				for (String key : list.get(i).keySet()) {
					map.put(key.toLowerCase(), list.get(i).get(key).toString());
				}
				list1.add(map);
			}
			return this.toJson("000", "查询成功！", list1);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	//首页行业占比饼图生成
	public String hytbQuery(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			//先将数据进行排序
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");
			String sql = "select (se+0) value,hyml_mc name from (select t.*,rank() over (order by (se+0) desc) mc from (select sum(m.dfzse) se,n.hyml_mc from SB_NSRXX m,dm_hyml n where m.hy_dm=n.hyml_dm and to_char(m.rk_rq, 'yyyy')=(select TO_CHAR(SYSDATE,'yyyy') from dual) group by n.hyml_mc order by (se+0) desc) t) a where a.mc <= 3 and jd_dm='"+ssdw_dm+"'" 
						 +"union all "
						 +"select sum(se) value,'其它' name from (select t.*,rank() over (order by (se+0) desc) mc from (select sum(m.dfzse) se,n.hyml_mc from SB_NSRXX m,dm_hyml n where m.hy_dm=n.hyml_dm and to_char(m.rk_rq, 'yyyy')=(select TO_CHAR(SYSDATE,'yyyy') from dual) group by n.hyml_mc order by (se+0) desc) t) a where a.mc > 3 and jd_dm='"+ssdw_dm+"'";

			System.out.println(sql);
			List<Map<String, Object>> list = this.getBs().query(sql);
			List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
			Map<String, String> map;
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, String>();
				for (String key : list.get(i).keySet()) {
					map.put(key.toLowerCase(), list.get(i).get(key).toString());
				}
				list1.add(map);
			}
			return this.toJson("000", "查询成功！",list1);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	// 图表生成方法
	/**
	 * 生成图表
	 * 
	 * @param rmap
	 * @return 返回结果集
	 */
	public String nsrInsert(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			DecimalFormat decimalFormat = new DecimalFormat("###0.00");
			String sql = "SELECT count(*) SL from (SELECT TO_CHAR(max(RK_RQ),'yyyy') rq,count(*) sl from SB_NSRXX WHERE TO_CHAR(RK_RQ,'yyyyMM') = (SELECT TO_CHAR(max(RK_RQ),'yyyyMM') rq from SB_NSRXX) and nsrmc not in (select nsrmc from DJ_NSRXX) GROUP BY nsrmc) a";
			sql+=" GROUP BY nsrmc) a";
			String sql2 = "SELECT TO_CHAR(max(RK_RQ),'yyyyMM') rq from SB_NSRXX";
			List<Map<String, Object>> list = this.getBs().query(sql);
			List<Map<String, Object>> list2 = this.getBs().query(sql2);

			if (list != null && list.size() > 0) {
				String maxdate = list2.get(0).get("RQ").toString();
				String maxYear = list2.get(0).get("RQ").toString().substring(0, 4);
				String maxMonth = list2.get(0).get("RQ").toString().substring(4, 6);
				// 首页新增纳税人纳税人
				String desql = "delete from XWCS.GH_XZNSR ";
				this.getBs().delete(desql);
				String insql = "INSERT INTO XWCS.GH_XZNSR (RKRQ, SL) VALUES ('" + maxdate + "', "
						+ list.get(0).get("SL") + ")";
				this.getBs().insert(insql);
				List<Mode> inlist = new ArrayList<Mode>();
				inlist.add(new Mode("IN", "String", ""));// 纳税人名字
				inlist.add(new Mode("IN", "String", "%"));// 街道代码
				inlist.add(new Mode("IN", "String", maxdate));// 时间起
				inlist.add(new Mode("IN", "String", maxdate));// 时间止
				inlist.add(new Mode("IN", "String", "0"));// 是否按月合计
				inlist.add(new Mode("IN", "String", "%"));// 企业性质
				inlist.add(new Mode("IN", "String", "%"));// 行业代码
				inlist.add(new Mode("IN", "String", "0"));// 是否合伙
				inlist.add(new Mode("IN", "String", "NSRMC"));// 税种名字
				inlist.add(new Mode("IN", "String", "DESC"));// 排序方式
				inlist.add(new Mode("IN", "String", "1"));// 统计口径
				inlist.add(new Mode("IN", "String", "%"));// 重点税源户代码
				inlist.add(new Mode("IN", "String", "0"));// 写死
				inlist.add(new Mode("IN", "String", "0"));// 写死
				inlist.add(new Mode("OUT", "RS", ""));
				List<Map<String, Object>> rs = (List<Map<String, Object>>) JdbcConnectedPro
						.call("SJ_CX_NEW.QUERYBYQY201605_NEW1", inlist);
				// {"QYSDS_GS":"317099962","CCS1":"0","HJ":"628951098.27","JYFJ1":"0","CSWHJSS":"0","JD_MC":"总合计",
				// "CZTDSYS1":"0","ZZS":"30407065.82","DFJYFJ":"0","ZZS1":"0","GRSDS":"0","HBS":"0","HBS1":"0",
				// "YGZZZS1":"0","FCS1":"0","QYSDS_GS1":"0","JD_DM":"BB","NSRSBH":"
				// ","QYSDS_DS1":"0","RK_RQ":" ",
				// "HY_MC":null,"YHS1":"0","QYSDS":"317099962","QYSDS_DS":"0","YHS":"0","GRSDS1":"0","CSWHJSS1":"0","DFJYFJ1":"0",
				// "HJ1":"0","NSRMC":"
				// ","JYFJ":"0","YYS":"0","CCS":"0","QYSDS1":"0","CZTDSYS":"0","YGZZZS":"281444070.45","FCS":"0","YYS1":"0","DSGLM":"
				// "}
				System.out.println(rs.size());
				Map<String, String> map = new HashMap<String, String>();
				// INSERT INTO XWCS.GH_XZNSR (RKRQ, SL) VALUES (NULL, NULL);
				if (rs.size() > 0) {
					int index = rs.size() - 1;
					map.put("个人所得税", rs.get(index).get("GRSDS").toString());
					map.put("企业所得税", rs.get(index).get("QYSDS").toString());
					map.put("房产税", rs.get(index).get("FCS").toString());
					double hj = Double.parseDouble((String) rs.get(index).get("ZZS"))
							+ Double.parseDouble((String) rs.get(index).get("YGZZZS"));
					map.put("增值税", decimalFormat.format(hj));
					double qt = Double.parseDouble((String) rs.get(index).get("YHS"))
							+ Double.parseDouble((String) rs.get(index).get("CCS"))
							+ Double.parseDouble((String) rs.get(index).get("CSWHJSS"))
							+ Double.parseDouble((String) rs.get(index).get("DFJYFJ"))
							+ Double.parseDouble((String) rs.get(index).get("JYFJ"))
							+ Double.parseDouble((String) rs.get(index).get("CZTDSYS"))
							+ Double.parseDouble((String) rs.get(index).get("HBS"))
							+ Double.parseDouble((String) rs.get(index).get("YYS"));
					map.put("其他", qt + "");
				}
				desql = "delete from GH_SYSZTJ";
				this.getBs().delete(desql);
				String sql1 = "";
				for (String key : map.keySet()) {
					System.out.println("key= " + key + " and value= " + map.get(key));
					sql1 = "INSERT INTO XWCS.GH_SYSZTJ (SZMC, SE,RKRQ) VALUES ('" + key + "', '" + map.get(key) + "','"
							+ maxYear + "')";
					this.getBs().insert(sql1);
				}

				int thisYear = Integer.parseInt(maxYear);
				int thisMonth = Integer.parseInt(maxMonth);
				int minYear = thisYear - 1;
				// 加入GH_SYZXT
				// select TO_CHAR(RK_RQ,'yyyyMM') rq,sum(zse) from SB_NSRXX
				// where TO_CHAR(RK_RQ,'yyyy') = '2018' GROUP BY
				// TO_CHAR(RK_RQ,'yyyyMM')
				String sqlxt = "select TO_CHAR(RK_RQ,'yyyyMM') rq,sum(dfzse) se from SB_NSRXX where TO_CHAR(RK_RQ,'yyyy') = '"
						+ maxYear + "' GROUP BY TO_CHAR(RK_RQ,'yyyyMM')";
				String sqlxtl = "select TO_CHAR(RK_RQ,'yyyyMM') rq,sum(dfzse) se from SB_NSRXX where TO_CHAR(RK_RQ,'yyyy') = '"
						+ minYear + "' GROUP BY TO_CHAR(RK_RQ,'yyyyMM')";

				List<Map<String, Object>> listxt = this.getBs().query(sqlxt);
				List<Map<String, Object>> listxtl = this.getBs().query(sqlxtl);
				// INSERT INTO "XWCS"."GH_SYZXT" ("YEAR", "MONTH", "SE",
				// "ROWID") VALUES (NULL, NULL, NULL, NULL);
				String sqldel = "delete from GH_SYZXT";
				this.getBs().delete(sqldel);
				for (int i = 1; i <= thisMonth; i++) {
					String querymonth = "";
					if (i < 10) {
						querymonth = maxYear + "0" + i;

					} else {
						querymonth = maxYear + i;
					}

					String se = "";

					for (int j = 0; j < listxt.size(); j++) {
						if (querymonth.equals(getValue(listxt.get(j).get("RQ")))) {
							se = listxt.get(j).get("SE").toString();
							break;
						} else {
							se = "0";
						}
					}

					String sqlm = "INSERT INTO XWCS.GH_SYZXT (YEAR, MONTH, SE) VALUES (" + thisYear + ", " + i + ", "
							+ se + ")";
					this.getBs().insert(sqlm);
				}

				for (int j = 1; j <= thisMonth; j++) {
					String querymonth = "";
					if (j < 10) {
						querymonth = minYear + "0" + j;

					} else {
						querymonth = minYear + "" + j;
					}

					String se = "";
					for (int k = 0; k < listxtl.size(); k++) {
						if (querymonth.equals(listxtl.get(k).get("RQ"))) {
							se = listxtl.get(k).get("SE").toString();
							break;
						} else {
							se = "0";
						}
					}
					String sqlm = "INSERT INTO XWCS.GH_SYZXT (YEAR, MONTH, SE) VALUES (" + minYear + ", " + j + ", "
							+ se + ")";
					this.getBs().insert(sqlm);
				}
			}
			System.out.println("处理完成");
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	/**
	 * 月累计
	 * @param rmap
	 * @return
	 */
	public String monthTotal(Map<String, Object> rmap){
		initMap(rmap);
		List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
		try{
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");
			String sql = "select TO_CHAR(SYSDATE,'yyyy')+0 year,yf,ROUND(dyljje/10000,2) se from FAST_YLJ where jd_dm='"+ssdw_dm+"' union select TO_CHAR(SYSDATE,'yyyy')-1 year,yf,ROUND(tqljje/10000,2) se from FAST_YLJ where jd_dm='"+ssdw_dm+"'";
			System.out.println("monthTotal:"+sql);
			List<Map<String, Object>> list = this.getBs().query(sql);
			Map<String, String> map;
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, String>();
				for (String key : list.get(i).keySet()) {
					map.put(key.toLowerCase(), list.get(i).get(key).toString());
				}
				list1.add(map);
			}
			return this.toJson("000", "查询成功！",list1);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败", list1);
		} 
	}
	
	/**
	 * 税种占比
	 * @param rmap
	 * @return
	 */
	public String szzb(Map<String, Object> rmap){
		initMap(rmap);
		List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try{
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");
			String sql = "select zsxm_mc name,ROUND(dnljje/10000,2) value from FAST_NSZZB where zsxm_mc = '营改增增值税' and jd_dm='"+ssdw_dm+"' or  zsxm_mc = '增值税' and jd_dm='"+ssdw_dm+"' or  zsxm_mc = '企业所得税' and jd_dm='"+ssdw_dm+"' union select '其他' name,ROUND((select sum(dnljje) from FAST_NSZZB where zsxm_mc != '营改增增值税' and  zsxm_mc != '增值税' and  zsxm_mc != '企业所得税' and jd_dm='"+ssdw_dm+"')/10000, 2) value from dual";
			list = this.getBs().query(sql);
			System.out.println(sql);
			System.out.println(list);
			Map<String, String> map;
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, String>();
				for (String key : list.get(i).keySet()) {
					map.put(key.toLowerCase(), list.get(i).get(key).toString());
				}
				list1.add(map);
			}
			return this.toJson("000", "查询成功！",list1);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list1);
		}
	}
	
	/**
	 * 行业占比
	 * @param rmap
	 * @return
	 */
	public String hyzb(Map<String, Object> rmap){
		initMap(rmap);
		List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try{
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");
			String sql = "SELECT hymc name,ROUND(dnljje/10000,2) value FROM (SELECT T.*,ROW_NUMBER() OVER (ORDER BY T.dnljje DESC) AS RN FROM FAST_NHYZB T where T.jd_dm='"+ssdw_dm+"')WHERE RN <= 3 and jd_dm='"+ssdw_dm+"' union select '其他' name,ROUND(((select sum(dnljje) from FAST_NHYZB where jd_dm='"+ssdw_dm+"')-(select sum(dnljje) FROM (SELECT T.*,ROW_NUMBER() OVER (ORDER BY T.dnljje DESC) AS RN FROM FAST_NHYZB T where T.jd_dm='"+ssdw_dm+"')WHERE RN <= 3 and jd_dm='"+ssdw_dm+"'))/10000, 2) value from dual";
			System.out.println(sql);
			list = this.getBs().query(sql);
			Map<String, String> map;
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, String>();
				for (String key : list.get(i).keySet()) {
					map.put(key.toLowerCase(), list.get(i).get(key).toString());
				}
				list1.add(map);
			}
			return this.toJson("000", "查询成功！",list1);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list1);
		}
	}
	
	/**
	 * 一般公共预算前20
	 * @param rmap
	 * @return
	 */
	public String ssTop(Map<String, Object> rmap){
		initMap(rmap);
		List<Map<String, Object>> list = new ArrayList<>();
		try{
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");
			if (ssdw_dm.equals("00")) {
				ssdw_dm="%";
			}
			String sql = "select * from FAST_YBGGYS where bs = '0' and jd_dm like '"+ssdw_dm+"%' order by dnljje desc";
			System.out.println(sql);
			list = this.getBs().query(sql);
			return this.toJson("000", "查询成功！",list);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list);
		}
	}
	
	/**
	 * 一般公共预算增幅前20
	 * @param rmap
	 * @return
	 */
	public String ssZFTop(Map<String, Object> rmap){
		initMap(rmap);
		List<Map<String, Object>> list = new ArrayList<>();
		try{
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");
			if (ssdw_dm.equals("00")) {
				ssdw_dm="%";
			}
			String sql = "select * from FAST_YBGGYS where bs = '1' and jd_dm like '"+ssdw_dm+"%' order by zf desc";
			System.out.println(sql);
			list = this.getBs().query(sql);
			return this.toJson("000", "查询成功！",list);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list);
		}
	}
	
	/**
	 * 一般公共预算减幅前20
	 * @param rmap
	 * @return
	 */
	public String ssJFTop(Map<String, Object> rmap){
		initMap(rmap);
		List<Map<String, Object>> list = new ArrayList<>();
		try{
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");
			if (ssdw_dm.equals("00")) {
				ssdw_dm="%";
			}
			String sql = "select * from FAST_YBGGYS where bs = '2' and jd_dm like '"+ssdw_dm+"%' order by zf";
			System.out.println(sql);
			list = this.getBs().query(sql);
			return this.toJson("000", "查询成功！",list);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list);
		}
	}
	
	/**
	 * 月累计详情
	 * @param rmap
	 * @return
	 */
	public String yljAll(Map<String, Object> rmap){
		initMap(rmap);
		List<Map<String, Object>> list = new ArrayList<>();
		try{
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");
			String sql = "select * from FAST_YLJ where jd_dm='"+ssdw_dm+"'";
			System.out.println(sql);
			list = this.getBs().query(sql);
			return this.toJson("000", "查询成功！",list);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list);
		}
	}
	
	/**
	 * 税种占比详情
	 * @param rmap
	 * @return
	 */
	public String szzbAll(Map<String, Object> rmap){
		initMap(rmap);
		List<Map<String, Object>> list = new ArrayList<>();
		try{
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");
			String sql = "select * from FAST_NSZZB where jd_dm='"+ssdw_dm+"' order by dnljje desc";
			System.out.println(sql);
			list = this.getBs().query(sql);
			return this.toJson("000", "查询成功！",list);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list);
		}
	}
	
	/**
	 * 行业占比详情
	 * @param rmap
	 * @return
	 */
	public String hyzbAll(Map<String, Object> rmap){
		initMap(rmap);
		List<Map<String, Object>> list = new ArrayList<>();
		try{
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");
			String sql = "select * from FAST_NHYZB where jd_dm='"+ssdw_dm+"' order by dnljje desc";
			System.out.println(sql);
			list = this.getBs().query(sql);
			return this.toJson("000", "查询成功！",list);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list);
		}
	}

}
