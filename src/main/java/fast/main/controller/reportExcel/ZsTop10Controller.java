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
 * 增收排名前十
 *
 */
@Controller
@RequestMapping("zsTop")
public class ZsTop10Controller extends Super {
	
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
		
		return "reportExcel/ZsTop10";
		
	}
	
	/**
	 * 增收排名前三十户企业税收情况查询
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/findZsTop.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String findZsTop(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
		try {
			String date = getValue(map.get("date")).toString();//年月
			Integer count = 30;
			String starTime = ""; String endTime = ""; 
			String[] star=date.split(" - ");
			if(date!=null &&!date.trim().equals("")&&star!=null&&star.length>0) {
				starTime=star[0]; endTime=star[1];
				starTime=starTime.substring(0,4)+starTime.substring(5,7);
				endTime=endTime.substring(0,4)+endTime.substring(5,7); 
			}
			String page = this.getValue(map.get("page"));
			String pageSize = this.getValue(map.get("limit"));
			String sql = "SELECT * FROM (SELECT A1.NSRMC NSRMC,A2.JD_MC JDMC,A1.HJ JN,A2.HJ QN,A1.HJ-A2.HJ ZS  FROM (SELECT T1.NSRMC,T2.JD_MC,T1.HJ FROM (SELECT A.NSRMC, NVL(A.ZZS, 0) + NVL(A.YGZZZS, 0) + NVL(A.YYS, 0) + NVL(A.QYSDS_GS, 0) + NVL(A.QYSDS_DS, 0) + NVL(A.GRSDS, 0) + NVL(A.FCS, 0) + NVL(A.YHS, 0) + NVL(A.CCS, 0) + NVL(A.CSWHJSS, 0) + NVL(A.DFJYFJ, 0) + NVL(A.JYFJ, 0) + NVL(A.cztdsys, 0) + NVL(A.hbs, 0) HJ" + 
					"        FROM (SELECT NSRMC,SUM(YGZZZS) * 0.5 YGZZZS, SUM(ZZS) * 0.5 ZZS, SUM(YYS) * 0.5 YYS, SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_GS," + 
					"            SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_DS, SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05') GRSDS," + 
					"            SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12') FCS, SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16') YHS," + 
					"            SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22') CCS, SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10') CSWHJSS," + 
					"            SUM(DFJYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '63') DFJYFJ, SUM(JYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '61') JYFJ," + 
					"            SUM(CZTDSYS) CZTDSYS, SUM(HBS) HBS FROM SB_NSRXX WHERE RK_RQ >= TO_DATE(?, 'YYYYMM') AND RK_RQ <= TO_DATE(?, 'YYYYMM') GROUP BY NSRMC) A" + 
					" ) T1,( SELECT NSRMC,D.JD_MC,ROW_NUMBER() OVER (PARTITION BY NSRMC ORDER BY SUM(ZSE) DESC) RN,SUM(ZSE) HJ FROM SB_NSRXX S LEFT JOIN DM_JD D ON S.JD_DM = D.JD_DM WHERE" + 
					" RK_RQ >= TO_DATE(?, 'YYYYMM') AND RK_RQ <= TO_DATE(?, 'YYYYMM')  GROUP BY NSRMC,JD_MC order by NSRMC" + 
					" ) T2 WHERE T1.NSRMC = T2.NSRMC AND T2.RN = '1' ) A1," + 
					" (SELECT T1.NSRMC,T2.JD_MC,T1.HJ FROM (SELECT A.NSRMC, NVL(A.ZZS, 0) + NVL(A.YGZZZS, 0) + NVL(A.YYS, 0) + NVL(A.QYSDS_GS, 0) + NVL(A.QYSDS_DS, 0) + NVL(A.GRSDS, 0) + NVL(A.FCS, 0) + NVL(A.YHS, 0) + NVL(A.CCS, 0) + NVL(A.CSWHJSS, 0) + NVL(A.DFJYFJ, 0) + NVL(A.JYFJ, 0) + NVL(A.cztdsys, 0) + NVL(A.hbs, 0) HJ" + 
					"        FROM (SELECT NSRMC,SUM(YGZZZS) * 0.5 YGZZZS, SUM(ZZS) * 0.5 ZZS, SUM(YYS) * 0.5 YYS, SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_GS," + 
					"            SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_DS, SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05') GRSDS," + 
					"            SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12') FCS, SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16') YHS," + 
					"            SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22') CCS, SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10') CSWHJSS," + 
					"            SUM(DFJYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '63') DFJYFJ, SUM(JYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '61') JYFJ," + 
					"            SUM(CZTDSYS) CZTDSYS, SUM(HBS) HBS FROM SB_NSRXX WHERE RK_RQ >= add_months(TO_DATE(?, 'YYYYMM'),-12) AND RK_RQ <= add_months(TO_DATE(?, 'YYYYMM'),-12) GROUP BY NSRMC) A" + 
					" ) T1,( SELECT NSRMC,D.JD_MC,ROW_NUMBER() OVER (PARTITION BY NSRMC ORDER BY SUM(ZSE) DESC) RN,SUM(ZSE) HJ FROM SB_NSRXX S LEFT JOIN DM_JD D ON S.JD_DM = D.JD_DM WHERE" + 
					" RK_RQ >= add_months(TO_DATE(?, 'YYYYMM'),-12) AND RK_RQ <= add_months(TO_DATE(?, 'YYYYMM'),-12)  GROUP BY NSRMC,JD_MC order by NSRMC" + 
					" ) T2 WHERE T1.NSRMC = T2.NSRMC AND T2.RN = '1' ) A2" + 
					" WHERE A1.NSRMC = A2.NSRMC ORDER BY ZS DESC) WHERE ROWNUM <=30";
			sql = this.getSql2(sql, new Object[] {starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime});
			List<Map<String, Object>> listd=bs.query(sql,page,pageSize);
			
			return this.toJsonct("000", "查询成功！",listd, getValue(count));
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	/**
	 * 增收排名前三十户数据导出
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
			String sql = "SELECT * FROM (SELECT A1.NSRMC NSRMC,A2.JD_MC JDMC,A1.HJ JN,A2.HJ QN,A1.HJ-A2.HJ ZS  FROM (SELECT T1.NSRMC,T2.JD_MC,T1.HJ FROM (SELECT A.NSRMC, NVL(A.ZZS, 0) + NVL(A.YGZZZS, 0) + NVL(A.YYS, 0) + NVL(A.QYSDS_GS, 0) + NVL(A.QYSDS_DS, 0) + NVL(A.GRSDS, 0) + NVL(A.FCS, 0) + NVL(A.YHS, 0) + NVL(A.CCS, 0) + NVL(A.CSWHJSS, 0) + NVL(A.DFJYFJ, 0) + NVL(A.JYFJ, 0) + NVL(A.cztdsys, 0) + NVL(A.hbs, 0) HJ" + 
					"        FROM (SELECT NSRMC,SUM(YGZZZS) * 0.5 YGZZZS, SUM(ZZS) * 0.5 ZZS, SUM(YYS) * 0.5 YYS, SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_GS," + 
					"            SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_DS, SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05') GRSDS," + 
					"            SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12') FCS, SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16') YHS," + 
					"            SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22') CCS, SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10') CSWHJSS," + 
					"            SUM(DFJYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '63') DFJYFJ, SUM(JYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '61') JYFJ," + 
					"            SUM(CZTDSYS) CZTDSYS, SUM(HBS) HBS FROM SB_NSRXX WHERE RK_RQ >= TO_DATE(?, 'YYYYMM') AND RK_RQ <= TO_DATE(?, 'YYYYMM') GROUP BY NSRMC) A" + 
					" ) T1,( SELECT NSRMC,D.JD_MC,ROW_NUMBER() OVER (PARTITION BY NSRMC ORDER BY SUM(ZSE) DESC) RN,SUM(ZSE) HJ FROM SB_NSRXX S LEFT JOIN DM_JD D ON S.JD_DM = D.JD_DM WHERE" + 
					" RK_RQ >= TO_DATE(?, 'YYYYMM') AND RK_RQ <= TO_DATE(?, 'YYYYMM')  GROUP BY NSRMC,JD_MC order by NSRMC" + 
					" ) T2 WHERE T1.NSRMC = T2.NSRMC AND T2.RN = '1' ) A1," + 
					" (SELECT T1.NSRMC,T2.JD_MC,T1.HJ FROM (SELECT A.NSRMC, NVL(A.ZZS, 0) + NVL(A.YGZZZS, 0) + NVL(A.YYS, 0) + NVL(A.QYSDS_GS, 0) + NVL(A.QYSDS_DS, 0) + NVL(A.GRSDS, 0) + NVL(A.FCS, 0) + NVL(A.YHS, 0) + NVL(A.CCS, 0) + NVL(A.CSWHJSS, 0) + NVL(A.DFJYFJ, 0) + NVL(A.JYFJ, 0) + NVL(A.cztdsys, 0) + NVL(A.hbs, 0) HJ" + 
					"        FROM (SELECT NSRMC,SUM(YGZZZS) * 0.5 YGZZZS, SUM(ZZS) * 0.5 ZZS, SUM(YYS) * 0.5 YYS, SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_GS," + 
					"            SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_DS, SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05') GRSDS," + 
					"            SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12') FCS, SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16') YHS," + 
					"            SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22') CCS, SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10') CSWHJSS," + 
					"            SUM(DFJYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '63') DFJYFJ, SUM(JYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '61') JYFJ," + 
					"            SUM(CZTDSYS) CZTDSYS, SUM(HBS) HBS FROM SB_NSRXX WHERE RK_RQ >= add_months(TO_DATE(?, 'YYYYMM'),-12) AND RK_RQ <= add_months(TO_DATE(?, 'YYYYMM'),-12) GROUP BY NSRMC) A" + 
					" ) T1,( SELECT NSRMC,D.JD_MC,ROW_NUMBER() OVER (PARTITION BY NSRMC ORDER BY SUM(ZSE) DESC) RN,SUM(ZSE) HJ FROM SB_NSRXX S LEFT JOIN DM_JD D ON S.JD_DM = D.JD_DM WHERE" + 
					" RK_RQ >= add_months(TO_DATE(?, 'YYYYMM'),-12) AND RK_RQ <= add_months(TO_DATE(?, 'YYYYMM'),-12)  GROUP BY NSRMC,JD_MC order by NSRMC" + 
					" ) T2 WHERE T1.NSRMC = T2.NSRMC AND T2.RN = '1' ) A2" + 
					" WHERE A1.NSRMC = A2.NSRMC ORDER BY ZS DESC) WHERE ROWNUM <=30";
			sql = this.getSql2(sql, new Object[] {starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime});
			List<Map<String, Object>> maplist=bs.query(sql);
			String[] cols = { "纳税人名称", "街道", "本年地方口径", "去年地方口径","增收" };
			String[] keys = { "NSRMC", "JDMC", "JN", "QN","ZS" };
			o.put("fileName", starTime+"/"+endTime+"增收排名前十户企业税收情况.xls");
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
