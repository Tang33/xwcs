package fast.main.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.ExcelUtil;
import fast.main.util.Super;


@Controller
@RequestMapping("expecel")
public class ExpecelController extends Super {
	
	@Autowired
	BaseService bs;
	
	//(上面的第一张表格)
	@RequestMapping(value="/ssTop.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	 public Object ssTop(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> rmap) {
		  try {
		   String jd_dm = getValue(request.getSession().getAttribute("dwid"));
		   String sqlall = "select yf,ROUND(dyljje/10000,2) dyljje,ROUND(tqljje/10000,2) tqljje,ROUND(zf/10000,2) zf,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) zzbl from FAST_YLJ where JD_DM="+jd_dm;
		   List<Map<String, Object>> map=bs.query(sqlall);
	
		   Map<String, Object> map1 = new HashMap<String, Object>();
		   String[] cols = { "月份", "当月累计金额", "同期累计金额", "增幅" , "增长比例" };
		   String[] keys = { "YF", "DYLJJE", "TQLJJE", "ZF" , "ZZBL" };
		   map1.put("fileName", "一般公共预算收入分月情况.xls");
		   map1.put("cols", cols);
		   map1.put("keys", keys);
		   map1.put("list", map);
		   return map1;
		  } catch (Exception e) {
			  e.printStackTrace();
			  return this.toJson("009", "查询异常！");
		  }
	 }
	 
	//(上面的第二张表格)  select * from FAST_NHYZB order by dnljje desc
	@RequestMapping(value="/hyzbAll.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	 public Object hyzbAll(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> rmap) {
		  try {
		   String jd_dm = getValue(request.getSession().getAttribute("dwid"));
		  
		   String sqlall = "select hymc,ROUND(dnljje/10000,2) dnljje,ROUND(tqljje/10000,2) tqljje,ROUND(zf/10000,2) zf,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) zzbl,(case when zb is not null then ROUND(zb*100,2)||'%' else null end) zb from FAST_NHYZB where jd_dm='"+jd_dm+"' order by dnljje desc";
		   List<Map<String, Object>> map=bs.query(sqlall);
	
		   Map<String, Object> map1 = new HashMap<String, Object>();
		   String[] cols = { "行业名称", "当年累计金额", "同期累计金额", "增幅" , "增长比例" , "占比"};
		   String[] keys = { "HYMC", "DNLJJE", "TQLJJE", "ZF" , "ZZBL", "ZB" };
		   map1.put("fileName", "一般公共预算按行业税种占比情况.xls");
		   map1.put("cols", cols);
		   map1.put("keys", keys);
		   map1.put("list", map);
		   return map1;
		  } catch (Exception e) {
			  e.printStackTrace();
			  return this.toJson("009", "查询异常！");
		  }
	 }
	 
	 //szzbAll  select * from FAST_NSZZB order by dnljje desc
	@RequestMapping(value="/szzbAll.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	 public Object szzbAll(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> rmap) {
		  try {
		   String jd_dm=getValue(request.getSession().getAttribute("dwid"));
		   String sqlall = "select zsxm_mc,ROUND(dnljje/10000,2) dnljje,ROUND(tqljje/10000,2) tqljje,ROUND(zf/10000,2) zf,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) zzbl,(case when zb is not null then ROUND(zb*100,2)||'%' else null end) zb from FAST_NSZZB where jd_dm ='"+jd_dm+"' order by dnljje desc";
		   List<Map<String, Object>> map=bs.query(sqlall);
	
		   Map<String, Object> map1 = new HashMap<String, Object>();
		   String[] cols = { "征收项目", "当年累计金额", "同期累计金额", "增幅" , "增长比例" , "占比"};
		   String[] keys = { "ZSXM_MC", "DNLJJE", "TQLJJE", "ZF" , "ZZBL", "ZB" };
		   map1.put("fileName", "一般公共预算按税种占比情况.xls");
		   map1.put("cols", cols);
		   map1.put("keys", keys);
		   map1.put("list", map);
		   return map1;
		  } catch (Exception e) {
			  e.printStackTrace();
			  return this.toJson("009", "查询异常！");
		  }
	 }
	 
	// 导出excel(下面的第一张表格)
	@RequestMapping(value="/exportop1.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public Object exportop1(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> rmap) {
		  try {
		   String jd_dm=getValue(request.getSession().getAttribute("dwid"));
		   String yearNmonth = getValue(rmap.get("yearNmonth")).toString();//年月
		   // 街道
		   if (jd_dm.equals("00")) {
			   jd_dm="%";
			}
		   String sqlall="select nsrmc 纳税人名称,ROUND(dnljje/10000,2) 当年累计金额,ROUND(tqljje/10000,2) 同期累计金额,ROUND(zf/10000,2) 增幅,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end)"
		   		+ " 增长比例  from FAST_YBGGYS where bs = 0 AND JD_DM like '"+jd_dm+"%' order by dnljje desc";
		   List<Map<String, Object>> map=bs.query(sqlall);
	
		   Map<String, Object> map1 = new HashMap<String, Object>();
		   String[] cols = { "纳税人名称", "当年累计金额", "同期累计金额", "增幅" , "增长比例" };
		   String[] keys = { "NSRMC", "DNLJJE", "TQLJJE", "ZF" , "ZZBL" };
		   map1.put("fileName", "一般公共预算收入前二十.xls");
		   map1.put("cols", cols);
		   map1.put("keys", cols);
		   map1.put("list", map);
		   return map1;
		  } catch (Exception e) {
			  e.printStackTrace();
			  return this.toJson("009", "查询异常！");
		  }
	}
	
	//(下面的第二张表格)
	@RequestMapping(value="/ssZFTop.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public Object ssZFTop(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> rmap) {
		  try {
		   String jd_dm=getValue(request.getSession().getAttribute("dwid"));
		   String yearNmonth = getValue(rmap.get("yearNmonth")).toString();//年月
		   // 街道
		   // 街道
		   if (jd_dm.equals("00")) {
			   jd_dm="%";
			}
		   String sqlall="select nsrmc 纳税人名称,ROUND(dnljje/10000,2) 当年累计金额,ROUND(tqljje/10000,2) 同期累计金额,ROUND(zf/10000,2) 增幅,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) 增长比例  from FAST_YBGGYS where bs = 1 AND JD_DM like '"+jd_dm+"%' order by dnljje desc";
		   List<Map<String, Object>> map=bs.query(sqlall);
	
		   Map<String, Object> map1 = new HashMap<String, Object>();
		   String[] cols = { "纳税人名称", "当年累计金额", "同期累计金额", "增幅" , "增长比例" };
		   String[] keys = { "NSRMC", "DNLJJE", "TQLJJE", "ZF" , "ZZBL" };
		   map1.put("fileName", "一般公共预算收入增幅前二十.xls");
		   map1.put("cols", cols);
		   map1.put("keys", cols);
		   map1.put("list", map);
		   return map1;
		  } catch (Exception e) {
			  e.printStackTrace();
			  return this.toJson("009", "查询异常！");
		  }
	}
	
	
	//(下面的第三张表格)
	@RequestMapping(value="/ssJFTop.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public Object ssJFTop(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> rmap) {
		  try {
		   String jd_dm=getValue(request.getSession().getAttribute("dwid"));
		   String yearNmonth = getValue(rmap.get("yearNmonth")).toString();//年月
		   // 街道
		   // 街道
		   if (jd_dm.equals("00")) {
			   jd_dm="%";
			}
		   String sqlall="select nsrmc 纳税人名称,ROUND(dnljje/10000,2) 当年累计金额,ROUND(tqljje/10000,2) 同期累计金额,ROUND(zf/10000,2) 增幅,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) 增长比例  from FAST_YBGGYS where bs = 2 AND JD_DM like '"+jd_dm+"%' order by dnljje desc";
		   List<Map<String, Object>> map=bs.query(sqlall);
	
		   Map<String, Object> map1 = new HashMap<String, Object>();
		   String[] cols = { "纳税人名称", "当年累计金额", "同期累计金额", "增幅" , "增长比例" };
		   String[] keys = { "NSRMC", "DNLJJE", "TQLJJE", "ZF" , "ZZBL" };
		   map1.put("fileName", "一般公共预算收入减幅前二十.xls");
		   map1.put("cols", cols);
		   map1.put("keys", cols);
		   map1.put("list", map);
		   return map1;
		  } catch (Exception e) {
			  e.printStackTrace();
			  return this.toJson("009", "查询异常！");
		  }
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/export.do")
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form) throws IOException {
		Map<String, Object> o = new HashMap<String, Object>();
		try {
			String no = request.getParameter("no");
			if (no.equals("1")) {
				o = (Map<String, Object>) this.ssTop(request, response, form);
			}else if (no.equals("3")) {
				o = (Map<String, Object>) this.hyzbAll(request, response, form);
			}else if (no.equals("2")) {
				o = (Map<String, Object>) this.szzbAll(request, response, form);
			}else if (no.equals("4")) {
				o = (Map<String, Object>) this.exportop1(request, response, form);
			}else if (no.equals("5")) {
				o = (Map<String, Object>) this.ssZFTop(request, response, form);
			}else if (no.equals("6")) {
				o = (Map<String, Object>) this.ssJFTop(request, response, form);
			}
//			o = (Map<String, Object>) this.exportop1(request, response, form);
			String fileName=(String) o.get("fileName");
		
			List<Map<String, Object>> list=(List<Map<String, Object>>) o.get("list");
			String cols[] = (String[]) o.get("cols");//列名
			String keys[] = (String[]) o.get("keys");//map中的key
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				ExcelUtil.createWorkBook(list, keys, cols).write(os);
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
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}
