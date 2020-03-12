package fast.main.controller.QueryTaxData;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import fast.main.service.BaseService;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;
@Controller
@RequestMapping("aqycxhzxx")
public class aqycxxhzController extends Super {

	private Map<String, Object> user = null;
	@Autowired
	BaseService bs;
	
	/**
	 * 进入按企业查询汇总信息跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		
		return "QueryTaxData/aqycxhzxx";
		
	}
	
	/**
	 * 查询数据库最大年月
	 * @return
	 */
	@RequestMapping("queryZdny.do")
	@ResponseBody
	public String queryZdny(HttpServletRequest request, HttpServletResponse response){
		user = (Map<String, Object>)request.getSession().getAttribute("user");
		try{			
			String sql = "SELECT to_char(max(RK_RQ),'yyyyMM') as rkrq FROM SB_NSRXX ORDER BY RK_RQ DESC  ";
			List<Map<String, Object>> result = bs.query(sql);
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < result.size(); i++) {
				Map<String, Object> map = result.get(i);
				lists.add(map);
			}
			return this.toJson("000", "查询成功！", lists);	
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return this.toJson("000", "系统异常!");
		}
	}
		/**
		 * 街道和行业条件查询
		 * @return
		 */
		@RequestMapping(value="queryAqyInit.do",produces = "text/plain;charset=utf-8")
		@ResponseBody
		public String queryAqyInit(HttpServletRequest request, HttpServletResponse response){
			user = (Map<String, Object>)request.getSession().getAttribute("user");
			response.setContentType("application/json");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding("UTF-8");
			try{
				String dwid = request.getSession().getAttribute("dwid").toString();
				String sql = "";
				Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
				// 街道
				if("00".equals(dwid)) {
					List<Map<String, Object>> jdlist = bs.query("select * from dm_jd");
					map.put("jdlist", jdlist);
				} else {
					sql = "select * from dm_jd where jd_dm = '"+dwid+"'";
					List<Map<String, Object>> jdlist = bs.query(sql);
					map.put("jdlist", jdlist);
				}
				// 行业
				List<Map<String, Object>> hylist = bs.query("select * from dm_hyml");
				map.put("hylist", hylist);
				return this.toJson("000", "查询成功！", map);	
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return this.toJson("000", "系统异常!");
			}
	
	}
	
		/**
		 * 查询企业汇总信息
		 * @return
		 */
		@RequestMapping(value="queryAqyhzxxhz.do",produces = "text/plain;charset=utf-8")
		@ResponseBody
		public String queryAqyhzxxhz(HttpServletRequest request, HttpServletResponse response){
			user = (Map<String, Object>)request.getSession().getAttribute("user");
			response.setContentType("application/json");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding("UTF-8");
			try{
				request.getSession().getAttribute("uno");
				String isadmin = (String) request.getSession().getAttribute("dwid");
				//高级权限
				if("00".equals(isadmin)) {
					
				}
				request.getSession().getAttribute("uno");
				String yearNmonth = getValue(request.getParameter("date")).toString();// 年月
				String starTime = "";
				String endTime = "";
				String[] star = yearNmonth.split(" - ");
				if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
					starTime = star[0];
					endTime = star[1];
					starTime = starTime.substring(0, 4) + starTime.substring(5, 7);
					endTime = endTime.substring(0, 4) + endTime.substring(5, 7);
				}
				//获取当前用户的街道代码
				String ssdw_dm = (String) request.getSession().getAttribute("dwid");

				String jd = getValue(request.getParameter("jd")).toString();// 街道
				if (!ssdw_dm.equals("00")) {
					jd=ssdw_dm;
					int staryear = Integer.parseInt(starTime.substring(0, 4));
					int starmonth = Integer.parseInt(starTime.substring(4, 6));
					int endryear = Integer.parseInt(endTime.substring(0, 4));
					int endmonth = Integer.parseInt(endTime.substring(4, 6));
					int m = ((endryear - staryear) * 12 + endmonth - starmonth)+1;
					for (int i = 0; i < m; i++) {
						String rq = staryear + "" + (starmonth<10?"0"+starmonth:starmonth);
						String statesql = "select state from xwcs_cxzt where to_Char(rkrq,'yyyyMM')='" + rq + "'";
						Map<String, Object> map = bs.queryOne(statesql);
						if (map == null) {
							SimpleDateFormat dateparse = new SimpleDateFormat("yyyyMM");
							Date date=dateparse.parse(rq);
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月");//可以方便地修改日期格式
							String resultrq = dateFormat.format(date); 
							return this.toJson("001", resultrq+"没有数据！");						
						} else {
							if (getValue(map.get("STATE")).equals("0")) {
								endTime = rq;
								break;
							}else if(getValue(map.get("STATE")).equals("")) {
								endTime = rq;
								break;
							}
						}
						starmonth++;
						if (starmonth > 12) {
							starmonth = 1;
							staryear++;
						}
					}
				}		

				String type = getValue(request.getParameter("type")).toString();// 合计或月明细
				String nsName = getValue(request.getParameter("nsName")).toString();// 纳税人名称
				String sortname = getValue(request.getParameter("sortname")).toString();// 税种名字
				String px = getValue(request.getParameter("px")).toString();// 排序
				String qyxz = getValue(request.getParameter("qyxz")).toString();// 企业性质
				String islp = getValue(request.getParameter("islp")).toString();// 是否合伙
				String tjkj = getValue(request.getParameter("tjkj")).toString();// 统计口径
				String hylist = getValue(request.getParameter("hylist")).toString();// 行业
				String zdsyh = getValue(request.getParameter("zdsyh")).toString();// 重点税源
				
				int count = Integer.parseInt(getValue(request.getParameter("page")));// 写死 0
				int count1 = Integer.parseInt(getValue(request.getParameter("limit")));// 写死 80000
				count = (count - 1) * count1;
				count1 = count + count1;
				
				List<Mode> listcount = new ArrayList<Mode>();
				listcount.add(new Mode("IN", "String", nsName.equals("")?"%":nsName));// 纳税人名字
				listcount.add(new Mode("IN", "String", starTime));// 时间起
				listcount.add(new Mode("IN", "String", endTime));// 时间止
				listcount.add(new Mode("IN", "String", type));// 是否按月合计
				listcount.add(new Mode("IN", "String", qyxz));// 企业性质
				listcount.add(new Mode("IN", "String", hylist));// 行业代码
				listcount.add(new Mode("IN", "String", islp));// 是否合伙
				listcount.add(new Mode("IN", "String", zdsyh));// 重点税源户代码
				listcount.add(new Mode("IN", "String", isadmin));// 街道
				listcount.add(new Mode("OUT", "RS", ""));
				
				@SuppressWarnings("unchecked")
				//List<Map<String, Object>> rscount2 = (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYQY_JLS", listcount);// 调用存储过程
				List<Map<String, Object>> rscount = (List<Map<String, Object>>) call("SJ_CX_2012.QUERYBYQY_JLS", listcount);
				System.out.println(rscount);
				

				List<Mode> list = new ArrayList<Mode>();
				list.add(new Mode("IN", "String", nsName));// 纳税人名字
				list.add(new Mode("IN", "String", starTime));// 时间起
				list.add(new Mode("IN", "String", endTime));// 时间止
				list.add(new Mode("IN", "String", type));// 是否按月合计
				list.add(new Mode("IN", "String", qyxz));// 企业性质
				list.add(new Mode("IN", "String", hylist));// 行业代码
				list.add(new Mode("IN", "String", islp));// 是否合伙
				list.add(new Mode("IN", "String", sortname));// 税种名字
				list.add(new Mode("IN", "String", px));// 排序方式
				list.add(new Mode("IN", "String", tjkj));// 统计口径
				list.add(new Mode("IN", "String", zdsyh));// 重点税源户代码		
				list.add(new Mode("IN", "String", getValue(count)));// 写死
				list.add(new Mode("IN", "String",getValue(count1)));// 写死
				list.add(new Mode("IN", "String",isadmin));// 写死
			
				list.add(new Mode("OUT", "RS", ""));
				System.out.println(list);
				// SJ_CX_NEW.QUERYBYQY201605_NEW1
//				List<Map<String, Object>> rs1 = (List<Map<String, Object>>) JdbcConnectedPro
//						.call("SJ_CX_NEW.QUERYBYQY201605_NEW1", list);// 调用存储过程
				List<Map<String, Object>> rs = (List<Map<String, Object>>) call("SJ_CX_2012.QUERYBYQY201605_NEW1", list);
				if(rs.size()!=0){
					System.out.println(rs.get(0));
					
				}else{
					System.out.println("SJ_CX_NEW.QUERYBYQY201605_NEW1存储过程查询结果为空！");
				}
				int counts = Integer.parseInt(getValue(rscount.get(0).get("COUNTS")));
				
				//HJ 合计
				//ZZS 增值税
				//YGZZZS 营改增增值税
				//YYS 营业税
				//QYSDS 企业所得税（合计）
				//GRSDS 个人所得税
				//FCS 房产税
				//YHS 印花税
				//CCS 车船税
				//CSWHJSS 城市维护建设税
				//DFJYFJ 地方教育附加
				//JYFJ 教育附加
				//CZTDSYS 城镇土地使用税
				//HBS 环保税
				DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
				for (int i = 0; i < rs.size(); i++) {
					Map map = rs.get(i);
					double hj =  Double.parseDouble((String)rs.get(i).get("ZZS")) 
							+ Double.parseDouble((String)rs.get(i).get("YGZZZS"))
							+ Double.parseDouble((String)rs.get(i).get("YYS"))
							+ Double.parseDouble((String)rs.get(i).get("QYSDS"))
							+ Double.parseDouble((String)rs.get(i).get("GRSDS"))
							+ Double.parseDouble((String)rs.get(i).get("FCS"))
							+ Double.parseDouble((String)rs.get(i).get("YHS"))
							+ Double.parseDouble((String)rs.get(i).get("CCS"))
							+ Double.parseDouble((String)rs.get(i).get("DFJYFJ"))
							+ Double.parseDouble((String)rs.get(i).get("JYFJ"))
							+ Double.parseDouble(rs.get(i).get("CSWHJSS") == null ? "0":(String)rs.get(i).get("CSWHJSS"))
							+ Double.parseDouble(rs.get(i).get("CZTDSYS") == null ? "0":(String)rs.get(i).get("CZTDSYS"))
							+ Double.parseDouble(rs.get(i).get("HBS") == null ? "0":(String)rs.get(i).get("HBS"));
					map.put("HJ", decimalFormat.format(hj));
					//不含附加税的合计
					double nofj =  Double.parseDouble((String)rs.get(i).get("ZZS")) 
							+ Double.parseDouble((String)rs.get(i).get("YGZZZS"))
							+ Double.parseDouble((String)rs.get(i).get("YYS"))
							+ Double.parseDouble((String)rs.get(i).get("QYSDS"))
							+ Double.parseDouble((String)rs.get(i).get("GRSDS"))
							+ Double.parseDouble((String)rs.get(i).get("FCS"))
							+ Double.parseDouble((String)rs.get(i).get("YHS"))
							+ Double.parseDouble((String)rs.get(i).get("CCS"));
					map.put("NOFJ", decimalFormat.format(nofj));
				}
				//其他街道
				if(!"00".equals(ssdw_dm)) {
					for (int i = 0; i < rs.size(); i++) {
						Map map = rs.get(i);
						double hj =  Double.parseDouble((String)rs.get(i).get("ZZS")) 
								+ Double.parseDouble((String)rs.get(i).get("YGZZZS"))
								+ Double.parseDouble((String)rs.get(i).get("YYS"))
								+ Double.parseDouble((String)rs.get(i).get("QYSDS"))
								+ Double.parseDouble((String)rs.get(i).get("GRSDS"))
								+ Double.parseDouble((String)rs.get(i).get("FCS"))
								+ Double.parseDouble((String)rs.get(i).get("YHS"))
								+ Double.parseDouble((String)rs.get(i).get("CCS"))
								+ Double.parseDouble((String)rs.get(i).get("DFJYFJ"))
								+ Double.parseDouble((String)rs.get(i).get("JYFJ"));
//								+ Double.parseDouble(sjList.get(i).get("CSWHJSS") == null ? "0":(String)sjList.get(i).get("CSWHJSS"))
//								+ Double.parseDouble(sjList.get(i).get("CZTDSYS") == null ? "0":(String)sjList.get(i).get("CZTDSYS"))
//								+ Double.parseDouble(sjList.get(i).get("HBS") == null ? "0":(String)sjList.get(i).get("HBS"));
						map.put("HJ", decimalFormat.format(hj));
						//不含附加税的合计
						double nofj =  Double.parseDouble((String)rs.get(i).get("ZZS")) 
								+ Double.parseDouble((String)rs.get(i).get("YGZZZS"))
								+ Double.parseDouble((String)rs.get(i).get("YYS"))
								+ Double.parseDouble((String)rs.get(i).get("QYSDS"))
								+ Double.parseDouble((String)rs.get(i).get("GRSDS"))
								+ Double.parseDouble((String)rs.get(i).get("FCS"))
								+ Double.parseDouble((String)rs.get(i).get("YHS"))
								+ Double.parseDouble((String)rs.get(i).get("CCS"));
						map.put("NOFJ", decimalFormat.format(nofj));
					}
				}
				if (sortname.equals("ZSE")&&rs.size()>1) {
					long start = System.currentTimeMillis();
					rs=Sort(rs,px);
					System.out.println("调用排序用时："+(System.currentTimeMillis()-start)/1000.0);
					Map<String, Object> map =rs.get(0);
					Map<String, Object> map1 =rs.get(1);
					rs.remove(0);
					rs.remove(0);
					if (getValue(map.get("JD_MC")).equals("总合计")) {
						rs.add(map1);					
						rs.add(map);
					}else {				
						rs.add(map);	
						rs.add(map1);					
					}
				}
				return this.toJson("000", "查询成功！", rs,counts);
				
			
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return this.toJson("000", "系统异常!");
			}
	
	}
		
		private List<Map<String, Object>> Sort(List<Map<String, Object>> list,String px) {
			System.out.println(px);
			if (px.equals("ASC")) {
				Collections.sort(list, new Comparator<Map<String, Object>>() {
		            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		            		Double name1 = Double.valueOf(o1.get("HJ").toString().replace(",", "").replace("\"", "")) ;//name1是从你list里面拿出来的一个 
		            		Double name2 = Double.valueOf(o2.get("HJ").toString().replace(",", "").replace("\"", "")) ; //name1是从你list里面拿出来的第二个name
		            		return name1.compareTo(name2);
		            }
		        });
			}else {
				Collections.sort(list, new Comparator<Map<String, Object>>() {
		            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		            	Double name1 = Double.valueOf(o1.get("HJ").toString().replace(",", "").replace("\"", "")) ;//name1是从你list里面拿出来的一个 
		            	Double name2 = Double.valueOf(o2.get("HJ").toString().replace(",", "").replace("\"", "")) ; //name1是从你list里面拿出来的第二个name
		                return name2.compareTo(name1);
		            }
		        });
			}
			 
		        return list;
		}
	
	
}
