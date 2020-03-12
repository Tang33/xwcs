package fast.main.controller.QueryTaxData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.Excel3;
import fast.main.util.ExcelUtil;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

/**
 * 按行业查询
 *
 */
@Controller
@RequestMapping("ahycx")
public class AhycxController extends Super{
	@Autowired
	BaseService bs;
	private Map<String, Object> user = null;
	
	/**
	 * 进入按行业查询跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		
		return "QueryTaxData/ahycx";
		
	}
	
	/**
	 * 获取行业信息
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/queryInit.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String queryInit(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
	
		try {
			String sql = "select * from dm_hyml";
			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
			List<Map<String, Object>> hylist = bs.query(sql);		
			map.put("hylist", hylist);		
			return this.toJson("000", "查询成功！", map);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	/**
	 * 分税种查询
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/queryData.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
		public String queryData(HttpServletRequest request , HttpServletResponse response,
				@RequestParam Map<String, Object> form){
		user=(Map<String, Object>) request.getSession().getAttribute("user");
		String jd = getValue(user.get("DWID"));
			try{
				request.getSession().getAttribute("uno");
				String yearNmonth = getValue(form.get("date")).toString();//年月
				String starTime = ""; String endTime = ""; 
				String[] star=yearNmonth.split(" - ");
				if(yearNmonth!=null &&!yearNmonth.trim().equals("")&&star!=null&&star.length>0) {
					starTime=star[0]; endTime=star[1];
					starTime=starTime.substring(0,4)+starTime.substring(5,7);
					endTime=endTime.substring(0,4)+endTime.substring(5,7); 
				}
				String hylist =getValue(form.get("hylist")).toString();//行业 
				String tjkj =getValue(form.get("tjkj")).toString();//统计口径 
				int count = Integer.parseInt(getValue(form.get("pageNo")));//
				int count1 = Integer.parseInt(getValue(form.get("pageSize")));//
				count = (count - 1) * count1;
				count1 = count + count1;
//				List<Mode> listcount = new ArrayList<Mode>();
//				listcount.add(new Mode("IN", "String", "%"));// 纳税人名字	
//				listcount.add(new Mode("IN", "String", starTime));// 时间起
//				listcount.add(new Mode("IN", "String", endTime));// 时间止
//				listcount.add(new Mode("IN", "String", "0"));// 是否按月合计
//				listcount.add(new Mode("IN", "String", "%"));// 企业性质
//				listcount.add(new Mode("IN", "String", hylist));// 行业代码
//				listcount.add(new Mode("IN", "String", "1"));// 是否合伙
//				listcount.add(new Mode("IN", "String", "%"));// 重点税源户代码
//				if(null != jd && jd == 00) {
//					listcount.add(new Mode("IN","String",jd+"00"));//写死判断街道
//				}else {
//					listcount.add(new Mode("IN","String",jd+""));
//				}
//				listcount.add(new Mode("OUT", "RS", ""));
				//List<Map<String, Object>> rscount = (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_2012.QUERYBYQY_JLS", listcount);// 调用存储过程
				List<Mode> list = new ArrayList<Mode>();
				list.add(new Mode("IN","String","%"));//纳税人名字
				list.add(new Mode("IN","String",starTime));//时间起
				list.add(new Mode("IN","String",endTime));//时间止
				list.add(new Mode("IN","String","0"));//是否按月合计
				list.add(new Mode("IN","String","%"));//企业性质
				list.add(new Mode("IN","String",hylist));//行业代码
				list.add(new Mode("IN","String","1"));//是否合伙
				list.add(new Mode("IN","String","ZSE"));//税种名字
				list.add(new Mode("IN","String","DESC"));//排序方式
				list.add(new Mode("IN","String",tjkj));//统计口径
				list.add(new Mode("IN","String","%"));//重点税源户代码			
				list.add(new Mode("IN","String",count+""));//写死
				list.add(new Mode("IN","String",count1+""));//写死
				String jd1 = "";
				list.add(new Mode("IN","String",jd+""));
				
				list.add(new Mode("OUT","RS",""));	
				List<Map<String, Object>> rs =  (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_2012.QUERYBYQY201605_NEW1", list);//调用存储过程
			
				String wheresql ="";
				if(!"00".equals(jd)) {
					wheresql +="and zs.jd_dm='"+jd+"'";
				}
				String sql = " select * from (select count(zs.NSRMC) from sb_nsrxx  zs"
						+"   where   zs.RK_RQ>=to_date(?,'yyyyMM') and  zs.RK_RQ< to_date(?,'yyyyMM')+1   "
						+" and zs.hy_dm like '"+hylist+"'    "
						+wheresql
						+ " group by zs.NSRMC)";
				sql = getSql2(sql, new Object[] {starTime,endTime});
				int queryCount = bs.queryCount(sql);
				
				return this.toJson("000", "查询成功！",rs,queryCount);
				
			}catch(Exception e){
				e.printStackTrace();
				return this.toJson("009", "查询异常！");
			}
		}
	
	/**
	 * 导出数据
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	public Object exportData(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, String> form){
		try{
			user=(Map<String, Object>) request.getSession().getAttribute("user");
			String jd = getValue(user.get("DWID"));
			request.getSession().getAttribute("uno");
			String yearNmonth = getValue(form.get("yearNmonth")).toString();//年月
			String starTime = "";
			String endTime = ""; 
			String[] star=yearNmonth.split(" - ");
			if(yearNmonth!=null &&!yearNmonth.trim().equals("")&&star!=null&&star.length>0) {
				starTime=star[0]; endTime=star[1];
				starTime=starTime.substring(0,4)+starTime.substring(5,7);
				endTime=endTime.substring(0,4)+endTime.substring(5,7); 
			}
			String hylist =getValue(form.get("hylist")).toString();//行业 
			String tjkj =getValue(form.get("tjkj")).toString();//统计口径 
			String countTotal =getValue(form.get("myCount")).toString();//条数		
			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN","String","%"));//纳税人名字
			list.add(new Mode("IN","String",starTime));//时间起
			list.add(new Mode("IN","String",endTime));//时间止
			list.add(new Mode("IN","String","0"));//是否按月合计
			list.add(new Mode("IN","String","%"));//企业性质
			list.add(new Mode("IN","String",hylist));//行业代码
			list.add(new Mode("IN","String","1"));//是否合伙
			list.add(new Mode("IN","String","ZSE"));//税种名字
			list.add(new Mode("IN","String","DESC"));//排序方式
			list.add(new Mode("IN","String",tjkj));//统计口径
			list.add(new Mode("IN","String","%"));//重点税源户代码			
			list.add(new Mode("IN","String",""));//写死
			list.add(new Mode("IN","String",""));//写死
			String jd1 = "";
			if(null != jd && "00".equals(jd)) {
				list.add(new Mode("IN","String",jd+""));//写死判断街道
				jd1 = null;
			}else {
				list.add(new Mode("IN","String",jd+""));
				jd1 = jd+"";
			}
			
			list.add(new Mode("OUT","RS",""));	
			String sortname="ZSE";
			List<Map<String, Object>> rs =  (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_2012.QUERYBYQY201605_NEW1", list);//调用存储过程
			Map<String, Object> map = new HashMap<String, Object>();
			String[] cols= {"企业名称","增值税","营改增增值税", "营业税","企业所得税（国税）","企业所得税（地税）",
					  		"个人所得税","房产税","印花税", "车船税","城市维护建设税","地方教育附加","教育费附加",
					  			"城镇土地使用税", "环保税","合计"};
			  String[] keys= {"NSRMC","ZZS","YGZZZS", "YYS","QYSDS_GS","QYSDS_DS",
					  		"GRSDS","FCS","YHS", "CCS","CSWHJSS","DFJYFJ","JYFJ",
					  			"CZTDSYS","HBS","HJ"};
			  map.put("fileName", "导出excel.xlsx"); 
			  map.put("cols", cols); 
			  map.put("keys", keys);
			  map.put("list", rs);
			 return map;		
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	//下载
	@RequestMapping(value="export.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form) throws IOException {
		Map<String, Object> o = new HashMap<String, Object>();
		try {
			o=(Map<String, Object>) this.exportData(request,response,form);
			String fileName=(String) o.get("fileName");
			List<Map<String, Object>> list=(List<Map<String, Object>>) o.get("list");
			String cols[] = (String[]) o.get("cols");//列名
			String keys[] = (String[]) o.get("keys");//map中的key
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				Excel3.createWorkBook(list, keys, cols).write(os);
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "iso-8859-1"));
			ServletOutputStream out = response.getOutputStream();
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				bis = new BufferedInputStream(is);
				bos = new BufferedOutputStream(out);
				byte[] buff = new byte[2048];
				int bytesRead;
				// Simple read/write loop.
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}
			} catch (final IOException e) {
				throw e;
			} finally {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			}
		}catch (Exception e) {
			// TODO: handle exception
		e.getMessage();
		}
	}
	
}
