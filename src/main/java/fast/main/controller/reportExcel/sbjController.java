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
 * 市本级三户下放明细
 *
 */
@Controller
@RequestMapping("sbj")
public class sbjController extends Super {
	
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
		
		return "reportExcel/sbj";
		
	}
	
	/**
	 * 市本级三户下放明细查询
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/findsbj.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String findZsTop(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		try {
			String date = getValue(map.get("date")).toString();//年月
			String starTime = ""; String endTime = ""; 
			String[] star=date.split(" - ");
			if(date!=null &&!date.trim().equals("")&&star!=null&&star.length>0) {
				starTime=star[0]; endTime=star[1];
				starTime=starTime.substring(0,4)+starTime.substring(5,7);
				endTime=endTime.substring(0,4)+endTime.substring(5,7); 
			}
			String page = this.getValue(map.get("page"));
			String pageSize = this.getValue(map.get("limit"));
			String sql = "select a.*,(zzs+yys)*0.5+(qysds+grsds)*0.4+zys+cswhjss+fcs+yhs+cztdsys+tdzzs+ccs+gdzys+jyfj+dfjyfj+whsyjsf hj1, \r\n" + 
					"zzs*(1-0.5-0.5*0.2)+(qysds+grsds)*(1-0.6-0.4*0.2)+(fcs+cztdsys+tdzzs)*(1-0.2)+cswhjss+yhs+jyfj+dfjyfj hj2 from \r\n" + 
					"(select nsrmc,sum(dfzse) dfzse,to_number('0') zzs,to_number('0') yys,to_number('0') qysds,sum(grsds) grsds,to_number('0') zys,sum(cswhjss) cswhjss,\r\n" + 
					"sum(fcs) fcs,sum(yhs) yhs,sum(cztdsys) cztdsys,to_number('0') tdzzs,to_number('0') ccs,to_number('0') gdzys,sum(jyfj) jyfj,\r\n" + 
					"sum(dfjyfj) dfjyfj,to_number('0') whsyjsf from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and rk_rq <= to_date(?,'yyyymm')  \r\n" + 
					"and nsrmc ='南京银行股份有限公司' group by nsrmc) a\r\n" + 
					"union\r\n" + 
					"select b.*,(zzs+yys)*0.5+(qysds+grsds)*0.4+zys+cswhjss+fcs+yhs+cztdsys+tdzzs+ccs+gdzys+jyfj+dfjyfj+whsyjsf hj1, \r\n" + 
					"zzs*(1-0.5-0.5*0.2)+(qysds+grsds)*(1-0.6-0.4*0.2)+(fcs+cztdsys+tdzzs)*(1-0.2)+cswhjss+yhs+jyfj+dfjyfj hj2 from \r\n" + 
					"(select nsrmc,sum(dfzse) dfzse,to_number('0') zzs,to_number('0') yys,to_number('0') qysds,sum(grsds) grsds,to_number('0') zys,to_number('0') cswhjss,\r\n" + 
					"sum(fcs) fcs,sum(yhs) yhs,sum(cztdsys) cztdsys,to_number('0') tdzzs,to_number('0') ccs,to_number('0') gdzys,to_number('0') jyfj,\r\n" + 
					"to_number('0') dfjyfj,to_number('0') whsyjsf from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and rk_rq <= to_date(?,'yyyymm')  \r\n" + 
					"and nsrmc ='中国电信股份有限公司江苏分公司' group by nsrmc) b\r\n" + 
					"union\r\n" + 
					"select c.*,(zzs+yys)*0.5+(qysds+grsds)*0.4+zys+cswhjss+fcs+yhs+cztdsys+tdzzs+ccs+gdzys+jyfj+dfjyfj+whsyjsf hj1, \r\n" + 
					"zzs*(1-0.5-0.5*0.2)+(qysds+grsds)*(1-0.6-0.4*0.2)+(fcs+cztdsys+tdzzs)*(1-0.2)+cswhjss+yhs+jyfj+dfjyfj hj2 from \r\n" + 
					"(select nsrmc,sum(dfzse) dfzse,sum(zzs) zzs,to_number('0') yys,to_number('0') qysds,sum(grsds) grsds,to_number('0') zys,sum(cswhjss) cswhjss,\r\n" + 
					"sum(fcs) fcs,sum(yhs) yhs,sum(cztdsys) cztdsys,to_number('0') tdzzs,to_number('0') ccs,to_number('0') gdzys,sum(jyfj) jyfj,\r\n" + 
					"sum(dfjyfj) dfjyfj,to_number('0') whsyjsf from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and rk_rq <= to_date(?,'yyyymm')  \r\n" + 
					"and nsrmc ='中国烟草总公司江苏省公司' group by nsrmc) c";
			sql = this.getSql2(sql, new Object[] {starTime,endTime,starTime,endTime,starTime,endTime});
			List<Map<String, Object>> listd=bs.query(sql,page,pageSize);
			
			return this.toJsonct("000", "查询成功！",listd, getValue(3));
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
			String starTime = ""; String endTime = ""; 
			String[] star=date.split(" - ");
			if(date!=null &&!date.trim().equals("")&&star!=null&&star.length>0) {
				starTime=star[0]; endTime=star[1];
				starTime=starTime.substring(0,4)+starTime.substring(5,7);
				endTime=endTime.substring(0,4)+endTime.substring(5,7); 
			}
			String sql = "select a.*,(zzs+yys)*0.5+(qysds+grsds)*0.4+zys+cswhjss+fcs+yhs+cztdsys+tdzzs+ccs+gdzys+jyfj+dfjyfj+whsyjsf hj1, \r\n" + 
					"zzs*(1-0.5-0.5*0.2)+(qysds+grsds)*(1-0.6-0.4*0.2)+(fcs+cztdsys+tdzzs)*(1-0.2)+cswhjss+yhs+jyfj+dfjyfj hj2 from \r\n" + 
					"(select nsrmc,sum(dfzse) dfzse,to_number('0') zzs,to_number('0') yys,to_number('0') qysds,sum(grsds) grsds,to_number('0') zys,sum(cswhjss) cswhjss,\r\n" + 
					"sum(fcs) fcs,sum(yhs) yhs,sum(cztdsys) cztdsys,to_number('0') tdzzs,to_number('0') ccs,to_number('0') gdzys,sum(jyfj) jyfj,\r\n" + 
					"sum(dfjyfj) dfjyfj,to_number('0') whsyjsf from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and rk_rq <= to_date(?,'yyyymm')  \r\n" + 
					"and nsrmc ='南京银行股份有限公司' group by nsrmc) a\r\n" + 
					"union\r\n" + 
					"select b.*,(zzs+yys)*0.5+(qysds+grsds)*0.4+zys+cswhjss+fcs+yhs+cztdsys+tdzzs+ccs+gdzys+jyfj+dfjyfj+whsyjsf hj1, \r\n" + 
					"zzs*(1-0.5-0.5*0.2)+(qysds+grsds)*(1-0.6-0.4*0.2)+(fcs+cztdsys+tdzzs)*(1-0.2)+cswhjss+yhs+jyfj+dfjyfj hj2 from \r\n" + 
					"(select nsrmc,sum(dfzse) dfzse,to_number('0') zzs,to_number('0') yys,to_number('0') qysds,sum(grsds) grsds,to_number('0') zys,to_number('0') cswhjss,\r\n" + 
					"sum(fcs) fcs,sum(yhs) yhs,sum(cztdsys) cztdsys,to_number('0') tdzzs,to_number('0') ccs,to_number('0') gdzys,to_number('0') jyfj,\r\n" + 
					"to_number('0') dfjyfj,to_number('0') whsyjsf from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and rk_rq <= to_date(?,'yyyymm')  \r\n" + 
					"and nsrmc ='中国电信股份有限公司江苏分公司' group by nsrmc) b\r\n" + 
					"union\r\n" + 
					"select c.*,(zzs+yys)*0.5+(qysds+grsds)*0.4+zys+cswhjss+fcs+yhs+cztdsys+tdzzs+ccs+gdzys+jyfj+dfjyfj+whsyjsf hj1, \r\n" + 
					"zzs*(1-0.5-0.5*0.2)+(qysds+grsds)*(1-0.6-0.4*0.2)+(fcs+cztdsys+tdzzs)*(1-0.2)+cswhjss+yhs+jyfj+dfjyfj hj2 from \r\n" + 
					"(select nsrmc,sum(dfzse) dfzse,sum(zzs) zzs,to_number('0') yys,to_number('0') qysds,sum(grsds) grsds,to_number('0') zys,sum(cswhjss) cswhjss,\r\n" + 
					"sum(fcs) fcs,sum(yhs) yhs,sum(cztdsys) cztdsys,to_number('0') tdzzs,to_number('0') ccs,to_number('0') gdzys,sum(jyfj) jyfj,\r\n" + 
					"sum(dfjyfj) dfjyfj,to_number('0') whsyjsf from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and rk_rq <= to_date(?,'yyyymm')  \r\n" + 
					"and nsrmc ='中国烟草总公司江苏省公司' group by nsrmc) c";
			sql = this.getSql2(sql, new Object[] {starTime,endTime,starTime,endTime,starTime,endTime});
			List<Map<String, Object>> maplist=bs.query(sql);
			String[] cols = { "纳税人名称", "增值税", "营业税", "企业所得税", "个人所得税", "资源税", 
					"城市维护建设税", "房产税", "印花税", "城镇土地使用税", "土地增值税", "车船税", 
					"耕地占用税","教育费附加","地方教育附加","文化事业建设费","地方口径收入","一般公共预算收入","市本级财力" };
			String[] keys = { "NSRMC", "ZZS", "YYS", "QYSDS", "GRSDS", "ZYS", "CSWHJSS", "FCS"
					, "YHS", "CZTDSYS", "TDZZS", "CCS", "GDZYS", "JYFJ", "DFJYFJ", "WHSYJSF","DFZSE", "HJ1", "HJ2" };
			o.put("fileName", starTime+"/"+endTime+"市本级三户下放明细.xls");
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
