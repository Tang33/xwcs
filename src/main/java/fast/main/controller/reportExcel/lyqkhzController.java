package fast.main.controller.reportExcel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

/**
 * 楼宇情况汇总统计
 *
 */
@Controller
@RequestMapping("lyqkhz")
public class lyqkhzController extends Super {
	
	@Autowired
	BaseService bs;
	
	/**
	 * 页面跳转至增收查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		
		return "reportExcel/lyqkhz";
		
	}
	
	/**
	 * 楼宇查询
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/find.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String findZsTop(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		try {
			String date = getValue(map.get("date")).toString();//年月
			String nf = date.substring(0,4); //年份
			String starTime = ""; 
			String endTime = ""; 
			String[] star=date.split(" - ");
			if(date!=null &&!date.trim().equals("")&&star!=null&&star.length>0) {
				starTime=star[0]; endTime=star[1];
				starTime=starTime.substring(0,4)+starTime.substring(5,7);
				endTime=endTime.substring(0,4)+endTime.substring(5,7); 
			}
			String page = this.getValue(map.get("page"));
			String pageSize = this.getValue(map.get("limit"));
			String sql = "select lyname,sum(dnzse) dnzse,sum(dndfzse) dndfzse,sum(zse) zse,sum(dfzse) dfzse from (select a.nsrmc,a.lyname,b.zse dnzse,b.dfzse dndfzse,c.zse,c.dfzse\r\n" + 
					"from (select distinct nsrmc,lyname from LY_MD) a left join \r\n" + 
					"(select nsrmc,sum(zse) zse,sum(dfzse) dfzse from sb_nsrxx where  to_char(rk_rq,'yyyy') = '"+nf+"' group by nsrmc) b\r\n" + 
					"on a.nsrmc = b.nsrmc\r\n" + 
					"left join (select nsrmc,sum(zse) zse,sum(dfzse) dfzse from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and rk_rq <= to_date(?,'yyyymm') group by nsrmc) c\r\n" + 
					"on a.nsrmc = c.nsrmc) group by lyname";
			sql = this.getSql2(sql, new Object[] {starTime,endTime});
			String count = ""+bs.queryCount(sql);	
			List<Map<String, Object>> listd=bs.query(sql,page,pageSize);
			
			return this.toJsonct("000", "查询成功！",listd, count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	/**
	 * 数据导出
	 * @param request
	 * @param response
	 * @param map
	 * @throws IOException
	 */
	@RequestMapping(value="/export.do",produces = "text/plain;charset=utf-8")
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> map) throws IOException {
		
		Map<String, Object> o = new HashMap<String, Object>();
		
		try {
			String date = getValue(map.get("yearNmonth")).toString();//年月
			String nf = date.substring(0,4); //年份
			String starTime = ""; String endTime = ""; 
			String[] star=date.split(" - ");
			if(date!=null &&!date.trim().equals("")&&star!=null&&star.length>0) {
				starTime=star[0]; endTime=star[1];
				starTime=starTime.substring(0,4)+starTime.substring(5,7);
				endTime=endTime.substring(0,4)+endTime.substring(5,7); 
			}
			String sql = "select lyname,sum(dnzse) dnzse,sum(dndfzse) dndfzse,sum(zse) zse,sum(dfzse) dfzse from (select a.nsrmc,a.lyname,b.zse dnzse,b.dfzse dndfzse,c.zse,c.dfzse\r\n" + 
					"from (select distinct nsrmc,lyname from LY_MD) a left join \r\n" + 
					"(select nsrmc,sum(zse) zse,sum(dfzse) dfzse from sb_nsrxx where  to_char(rk_rq,'yyyy') = '"+nf+"' group by nsrmc) b\r\n" + 
					"on a.nsrmc = b.nsrmc\r\n" + 
					"left join (select nsrmc,sum(zse) zse,sum(dfzse) dfzse from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and rk_rq <= to_date(?,'yyyymm') group by nsrmc) c\r\n" + 
					"on a.nsrmc = c.nsrmc) group by lyname";
			sql = this.getSql2(sql, new Object[] {starTime,endTime});
			List<Map<String, Object>> maplist=bs.query(sql);
			String[] cols = { "楼宇名称", "当年全口径税收", "当年地方口径税收", "全口径税收", "地方口径税收"};
			String[] keys = { "LYNAME", "DNZSE", "DNDFZSE","ZSE","DFZSE"};
			o.put("fileName", starTime+"/"+endTime+"楼宇情况汇总表.xls");
			o.put("cols", cols);
			o.put("keys", keys);
			o.put("list", maplist);
			String fileName=(String) o.get("fileName");
			List<Map<String, Object>> list=(List<Map<String, Object>>) o.get("list");
			String col[] = (String[]) o.get("cols");//列名
			String key[] = (String[]) o.get("keys");//map中的key
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				ExcelUtil.createWorkBook(list, key, col).write(os);
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
		}catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
}
