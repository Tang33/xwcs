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

@Controller
@RequestMapping("Sdsz")
public class SdszController extends Super {
	
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
		
		return "reportExcel/Sdsz";
		
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/findSdsz.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String findSdsz(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		try {
			String date = getValue(map.get("date")).toString();//年月
//			Integer count = 30;
			String starTime = ""; String endTime = ""; 
			String[] star=date.split(" - ");
			if(date!=null &&!date.trim().equals("")&&star!=null&&star.length>0) {
				starTime=star[0]; endTime=star[1];
				starTime=starTime.substring(0,4)+starTime.substring(5,7);
				endTime=endTime.substring(0,4)+endTime.substring(5,7); 
			}
//			String page = this.getValue(map.get("page"));
//			String pageSize = this.getValue(map.get("limit"));
			String sql = "select szzs.srq,jnzzs,sszzs,qnzzs,stb,szb,jnqysds,ssqysds,qnqysds,qtb,qzb,jngrsds,ssgrsds,qngrsds,gtb,gzb from( " + 
					"select jn.rq srq,jnzzs,sszzs,qnzzs, round(((jnzzs-sszzs)/qnzzs-1)*100,1)||'%' stb,round((jnzzs-qnzzs)/qnzzs*100,1)||'%' szb from ( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, sum(YGZZZS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.5 END)) + sum(ZZS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.5 END)) jnzzs from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and  rk_rq <= to_date(?,'yyyymm') group by rk_rq " + 
					") jn,( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, sum(YGZZZS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.5 END)) + sum(ZZS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.5 END)) sszzs from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and  rk_rq <= to_date(?,'yyyymm') and jd_dm = '25' group by rk_rq " + 
					") jss,( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, sum(YGZZZS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.5 END)) + sum(ZZS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.5 END)) qnzzs from sb_nsrxx where rk_rq >= add_months(TO_DATE(?, 'YYYYMM'),-12) and rk_rq <= add_months(TO_DATE(?, 'YYYYMM'),-12) group by rk_rq " + 
					") qn " + 
					"where jn.rq = jss.rq and jn.rq = qn.rq) szzs,( " + 
					"select jn.rq qrq,jnqysds,ssqysds,qnqysds,round(((jnqysds-ssqysds)/qnqysds-1)*100,1)||'%' qtb,round((jnqysds-qnqysds)/qnqysds*100,1)||'%' qzb from ( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) + SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) jnqysds from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and  rk_rq <= to_date(?,'yyyymm') group by rk_rq " + 
					") jn,( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) + SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) ssqysds from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and  rk_rq <= to_date(?,'yyyymm') and jd_dm = '25' group by rk_rq " + 
					") jss,( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) + SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) qnqysds from sb_nsrxx where rk_rq >= add_months(TO_DATE(?, 'YYYYMM'),-12) and rk_rq <= add_months(TO_DATE(?, 'YYYYMM'),-12) group by rk_rq " + 
					") qn " + 
					"where jn.rq = jss.rq and jn.rq = qn.rq) sqysds,( " + 
					"select jn.rq grq,jngrsds,ssgrsds,qngrsds,round(((jngrsds-ssgrsds)/qngrsds-1)*100,1)||'%' gtb,round((jngrsds-qngrsds)/qngrsds*100,1)||'%' gzb from ( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, SUM(GRSDS * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05')) jngrsds from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and  rk_rq <= to_date(?,'yyyymm') group by rk_rq " + 
					") jn,( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, SUM(GRSDS * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05')) ssgrsds from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and  rk_rq <= to_date(?,'yyyymm') and jd_dm = '25' group by rk_rq " + 
					") jss,( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, SUM(GRSDS * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05')) qngrsds from sb_nsrxx where rk_rq >= add_months(TO_DATE(?, 'YYYYMM'),-12) and rk_rq <= add_months(TO_DATE(?, 'YYYYMM'),-12) group by rk_rq " + 
					") qn " + 
					"where jn.rq = jss.rq and jn.rq = qn.rq) sgrsds " + 
					"where szzs.srq = sqysds.qrq and szzs.srq = sgrsds.grq order by szzs.srq";
			sql = this.getSql2(sql, new Object[] {starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime});
			List<Map<String, Object>> listd=bs.query(sql);
			
			return this.toJson("000", "查询成功！",listd);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
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
			String sql = "select szzs.srq,jnzzs,sszzs,qnzzs,stb,szb,jnqysds,ssqysds,qnqysds,qtb,qzb,jngrsds,ssgrsds,qngrsds,gtb,gzb from( " + 
					"select jn.rq srq,jnzzs,sszzs,qnzzs, round(((jnzzs-sszzs)/qnzzs-1)*100,1)||'%' stb,round((jnzzs-qnzzs)/qnzzs*100,1)||'%' szb from ( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, sum(YGZZZS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.5 END)) + sum(ZZS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.5 END)) jnzzs from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and  rk_rq <= to_date(?,'yyyymm') group by rk_rq " + 
					") jn,( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, sum(YGZZZS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.5 END)) + sum(ZZS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.5 END)) sszzs from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and  rk_rq <= to_date(?,'yyyymm') and jd_dm = '25' group by rk_rq " + 
					") jss,( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, sum(YGZZZS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.5 END)) + sum(ZZS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.5 END)) qnzzs from sb_nsrxx where rk_rq >= add_months(TO_DATE(?, 'YYYYMM'),-12) and rk_rq <= add_months(TO_DATE(?, 'YYYYMM'),-12) group by rk_rq " + 
					") qn " + 
					"where jn.rq = jss.rq and jn.rq = qn.rq) szzs,( " + 
					"select jn.rq qrq,jnqysds,ssqysds,qnqysds,round(((jnqysds-ssqysds)/qnqysds-1)*100,1)||'%' qtb,round((jnqysds-qnqysds)/qnqysds*100,1)||'%' qzb from ( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) + SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) jnqysds from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and  rk_rq <= to_date(?,'yyyymm') group by rk_rq " + 
					") jn,( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) + SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) ssqysds from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and  rk_rq <= to_date(?,'yyyymm') and jd_dm = '25' group by rk_rq " + 
					") jss,( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) + SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) qnqysds from sb_nsrxx where rk_rq >= add_months(TO_DATE(?, 'YYYYMM'),-12) and rk_rq <= add_months(TO_DATE(?, 'YYYYMM'),-12) group by rk_rq " + 
					") qn " + 
					"where jn.rq = jss.rq and jn.rq = qn.rq) sqysds,( " + 
					"select jn.rq grq,jngrsds,ssgrsds,qngrsds,round(((jngrsds-ssgrsds)/qngrsds-1)*100,1)||'%' gtb,round((jngrsds-qngrsds)/qngrsds*100,1)||'%' gzb from ( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, SUM(GRSDS * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05')) jngrsds from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and  rk_rq <= to_date(?,'yyyymm') group by rk_rq " + 
					") jn,( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, SUM(GRSDS * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05')) ssgrsds from sb_nsrxx where rk_rq >= to_date(?,'yyyymm') and  rk_rq <= to_date(?,'yyyymm') and jd_dm = '25' group by rk_rq " + 
					") jss,( " + 
					"select substr(to_char(rk_rq,'yyyymm'),5,2)|| '月' rq, SUM(GRSDS * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05')) qngrsds from sb_nsrxx where rk_rq >= add_months(TO_DATE(?, 'YYYYMM'),-12) and rk_rq <= add_months(TO_DATE(?, 'YYYYMM'),-12) group by rk_rq " + 
					") qn " + 
					"where jn.rq = jss.rq and jn.rq = qn.rq) sgrsds " + 
					"where szzs.srq = sqysds.qrq and szzs.srq = sgrsds.grq order by szzs.srq";
			sql = this.getSql2(sql, new Object[] {starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime});
			List<Map<String, Object>> maplist=bs.query(sql);
			String[] cols = { "月份", "增值税当月收入", "增值税省属税源", "增值税去年同期","增值税同比增幅%","增值税直比增幅%","企业所得税当月收入","企业所得税省属税源","企业所得税去年同期","企业所得税同比增幅%","企业所得税直比增幅%","个人所得税当月收入","个人所得税省属税源","个人所得税去年同期","个人所得税同比增幅%","个人所得税直比增幅%" };
			String[] keys = { "SRQ", "JNZZS", "SSZZS", "QNZZS","STB","SZB","JNQYSDS","SSQYSDS","QNQYSDS","QTB","QZB","JNGRSDS","SSGRSDS","QNGRSDS","GTB","GZB" };
			o.put("fileName", "三大税种.xls");
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
