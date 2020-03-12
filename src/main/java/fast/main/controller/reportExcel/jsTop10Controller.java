package fast.main.controller.reportExcel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import fast.main.util.Excel3;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

/**
 * 减收排名前十
 *
 */
@Controller
@RequestMapping("jsTop10")
public class jsTop10Controller extends Super{
	
	@Autowired
	BaseService bs;
	private Map<String, Object> user = null;
		
	/**
	 * 页面跳转至减收查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	
	@RequestMapping(value="/init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		
		return "reportExcel/jsTop10";
		
	}
	
	/**
	 * 查询减收数据
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/queryData.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String queryData(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {	
			//获取查询时间段
			String yearNmonth = getValue(form.get("date")).toString();//年月
			String starTime = ""; 
			String endTime = ""; 
			String[] star=yearNmonth.split(" - ");
			if(yearNmonth!=null &&!yearNmonth.trim().equals("")&&star!=null&&star.length>0) {
				starTime=star[0]; endTime=star[1];
				starTime=starTime.substring(0,4)+starTime.substring(5,7);
				endTime=endTime.substring(0,4)+endTime.substring(5,7); 
			}
			//获取分页参数
			String pageNo = getValue(form.get("pageNo")).replaceAll("-", "");
			String pageSize = getValue(form.get("pageSize")).replaceAll("-", "");
			//查询sql
			String sql = "SELECT * FROM (SELECT A1.NSRMC NSRMC,A2.JD_MC JD_MC,A1.HJ JN,A2.HJ QN,A1.HJ-A2.HJ ZS FROM (SELECT T1.NSRMC,T2.JD_MC,T1.HJ FROM (SELECT A.NSRMC, NVL(A.ZZS, 0) + NVL(A.YGZZZS, 0) + NVL(A.YYS, 0) + NVL(A.QYSDS_GS, 0) + NVL(A.QYSDS_DS, 0) + NVL(A.GRSDS, 0) + NVL(A.FCS, 0) + NVL(A.YHS, 0) + NVL(A.CCS, 0) + NVL(A.CSWHJSS, 0) + NVL(A.DFJYFJ, 0) + NVL(A.JYFJ, 0) + NVL(A.cztdsys, 0) + NVL(A.hbs, 0) HJ\r\n" + 
					"        FROM (SELECT NSRMC,SUM(YGZZZS) * 0.5 YGZZZS, SUM(ZZS) * 0.5 ZZS, SUM(YYS) * 0.5 YYS, SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_GS,\r\n" + 
					"            SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_DS, SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05') GRSDS,\r\n" + 
					"            SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12') FCS, SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16') YHS,\r\n" + 
					"            SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22') CCS, SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10') CSWHJSS,\r\n" + 
					"            SUM(DFJYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '63') DFJYFJ, SUM(JYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '61') JYFJ,\r\n" + 
					"            SUM(CZTDSYS) CZTDSYS, SUM(HBS) HBS FROM SB_NSRXX WHERE RK_RQ >= TO_DATE(?, 'YYYYMM') AND RK_RQ <= TO_DATE(?, 'YYYYMM') GROUP BY NSRMC) A\r\n" + 
					" ) T1,( SELECT NSRMC,D.JD_MC,ROW_NUMBER() OVER (PARTITION BY NSRMC ORDER BY SUM(ZSE) DESC) RN,SUM(ZSE) HJ FROM SB_NSRXX S LEFT JOIN DM_JD D ON S.JD_DM = D.JD_DM WHERE\r\n" + 
					" RK_RQ >= TO_DATE(?, 'YYYYMM') AND RK_RQ <= TO_DATE(?, 'YYYYMM')  GROUP BY NSRMC,JD_MC order by NSRMC\r\n" + 
					" ) T2 WHERE T1.NSRMC = T2.NSRMC AND T2.RN = '1' ) A1,\r\n" + 
					" (SELECT T1.NSRMC,T2.JD_MC,T1.HJ FROM (SELECT A.NSRMC, NVL(A.ZZS, 0) + NVL(A.YGZZZS, 0) + NVL(A.YYS, 0) + NVL(A.QYSDS_GS, 0) + NVL(A.QYSDS_DS, 0) + NVL(A.GRSDS, 0) + NVL(A.FCS, 0) + NVL(A.YHS, 0) + NVL(A.CCS, 0) + NVL(A.CSWHJSS, 0) + NVL(A.DFJYFJ, 0) + NVL(A.JYFJ, 0) + NVL(A.cztdsys, 0) + NVL(A.hbs, 0) HJ\r\n" + 
					"        FROM (SELECT NSRMC,SUM(YGZZZS) * 0.5 YGZZZS, SUM(ZZS) * 0.5 ZZS, SUM(YYS) * 0.5 YYS, SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_GS,\r\n" + 
					"            SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_DS, SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05') GRSDS,\r\n" + 
					"            SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12') FCS, SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16') YHS,\r\n" + 
					"            SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22') CCS, SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10') CSWHJSS,\r\n" + 
					"            SUM(DFJYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '63') DFJYFJ, SUM(JYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '61') JYFJ,\r\n" + 
					"            SUM(CZTDSYS) CZTDSYS, SUM(HBS) HBS FROM SB_NSRXX WHERE RK_RQ >= ADD_MONTHS(TO_DATE(?, 'YYYYMM'),-12) AND RK_RQ <= ADD_MONTHS(TO_DATE(?, 'YYYYMM'),-12) GROUP BY NSRMC) A\r\n" + 
					" ) T1,( SELECT NSRMC,D.JD_MC,ROW_NUMBER() OVER (PARTITION BY NSRMC ORDER BY SUM(ZSE) DESC) RN,SUM(ZSE) HJ FROM SB_NSRXX S LEFT JOIN DM_JD D ON S.JD_DM = D.JD_DM WHERE\r\n" + 
					" RK_RQ >= ADD_MONTHS(TO_DATE(?, 'YYYYMM'),-12) AND RK_RQ <= ADD_MONTHS(TO_DATE(?, 'YYYYMM'),-12)  GROUP BY NSRMC,JD_MC order by NSRMC\r\n" + 
					" ) T2 WHERE T1.NSRMC = T2.NSRMC AND T2.RN = '1' ) A2\r\n" + 
					" WHERE A1.NSRMC = A2.NSRMC ORDER BY ZS) WHERE ROWNUM <=30";
			sql = getSql2(sql, new Object[] {starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime});
			List<Map<String, Object>> list = bs.query(sql,pageNo,pageSize);
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	/**
	 * 导出减收数据查询
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	public Object exportData(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, String> form){
		try{
			//获取查询时间段
			String yearNmonth = getValue(form.get("yearNmonth")).toString();//年月
			String starTime = ""; 
			String endTime = ""; 
			String[] star=yearNmonth.split(" - ");
			if(yearNmonth!=null &&!yearNmonth.trim().equals("")&&star!=null&&star.length>0) {
				starTime=star[0]; endTime=star[1];
				starTime=starTime.substring(0,4)+starTime.substring(5,7);
				endTime=endTime.substring(0,4)+endTime.substring(5,7); 
			}
			//查询sql
			String sql = "SELECT * FROM (SELECT A1.NSRMC NSRMC,A2.JD_MC JD_MC,A1.HJ JN,A2.HJ QN,A1.HJ-A2.HJ ZS FROM (SELECT T1.NSRMC,T2.JD_MC,T1.HJ FROM (SELECT A.NSRMC, NVL(A.ZZS, 0) + NVL(A.YGZZZS, 0) + NVL(A.YYS, 0) + NVL(A.QYSDS_GS, 0) + NVL(A.QYSDS_DS, 0) + NVL(A.GRSDS, 0) + NVL(A.FCS, 0) + NVL(A.YHS, 0) + NVL(A.CCS, 0) + NVL(A.CSWHJSS, 0) + NVL(A.DFJYFJ, 0) + NVL(A.JYFJ, 0) + NVL(A.cztdsys, 0) + NVL(A.hbs, 0) HJ\r\n" + 
					"        FROM (SELECT NSRMC,SUM(YGZZZS) * 0.5 YGZZZS, SUM(ZZS) * 0.5 ZZS, SUM(YYS) * 0.5 YYS, SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_GS,\r\n" + 
					"            SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_DS, SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05') GRSDS,\r\n" + 
					"            SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12') FCS, SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16') YHS,\r\n" + 
					"            SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22') CCS, SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10') CSWHJSS,\r\n" + 
					"            SUM(DFJYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '63') DFJYFJ, SUM(JYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '61') JYFJ,\r\n" + 
					"            SUM(CZTDSYS) CZTDSYS, SUM(HBS) HBS FROM SB_NSRXX WHERE RK_RQ >= TO_DATE(?, 'YYYYMM') AND RK_RQ <= TO_DATE(?, 'YYYYMM') GROUP BY NSRMC) A\r\n" + 
					" ) T1,( SELECT NSRMC,D.JD_MC,ROW_NUMBER() OVER (PARTITION BY NSRMC ORDER BY SUM(ZSE) DESC) RN,SUM(ZSE) HJ FROM SB_NSRXX S LEFT JOIN DM_JD D ON S.JD_DM = D.JD_DM WHERE\r\n" + 
					" RK_RQ >= TO_DATE(?, 'YYYYMM') AND RK_RQ <= TO_DATE(?, 'YYYYMM')  GROUP BY NSRMC,JD_MC order by NSRMC\r\n" + 
					" ) T2 WHERE T1.NSRMC = T2.NSRMC AND T2.RN = '1' ) A1,\r\n" + 
					" (SELECT T1.NSRMC,T2.JD_MC,T1.HJ FROM (SELECT A.NSRMC, NVL(A.ZZS, 0) + NVL(A.YGZZZS, 0) + NVL(A.YYS, 0) + NVL(A.QYSDS_GS, 0) + NVL(A.QYSDS_DS, 0) + NVL(A.GRSDS, 0) + NVL(A.FCS, 0) + NVL(A.YHS, 0) + NVL(A.CCS, 0) + NVL(A.CSWHJSS, 0) + NVL(A.DFJYFJ, 0) + NVL(A.JYFJ, 0) + NVL(A.cztdsys, 0) + NVL(A.hbs, 0) HJ\r\n" + 
					"        FROM (SELECT NSRMC,SUM(YGZZZS) * 0.5 YGZZZS, SUM(ZZS) * 0.5 ZZS, SUM(YYS) * 0.5 YYS, SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_GS,\r\n" + 
					"            SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS_DS, SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05') GRSDS,\r\n" + 
					"            SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12') FCS, SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16') YHS,\r\n" + 
					"            SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22') CCS, SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10') CSWHJSS,\r\n" + 
					"            SUM(DFJYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '63') DFJYFJ, SUM(JYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '61') JYFJ,\r\n" + 
					"            SUM(CZTDSYS) CZTDSYS, SUM(HBS) HBS FROM SB_NSRXX WHERE RK_RQ >= ADD_MONTHS(TO_DATE(?, 'YYYYMM'),-12) AND RK_RQ <= ADD_MONTHS(TO_DATE(?, 'YYYYMM'),-12) GROUP BY NSRMC) A\r\n" + 
					" ) T1,( SELECT NSRMC,D.JD_MC,ROW_NUMBER() OVER (PARTITION BY NSRMC ORDER BY SUM(ZSE) DESC) RN,SUM(ZSE) HJ FROM SB_NSRXX S LEFT JOIN DM_JD D ON S.JD_DM = D.JD_DM WHERE\r\n" + 
					" RK_RQ >= ADD_MONTHS(TO_DATE(?, 'YYYYMM'),-12) AND RK_RQ <= ADD_MONTHS(TO_DATE(?, 'YYYYMM'),-12)  GROUP BY NSRMC,JD_MC order by NSRMC\r\n" + 
					" ) T2 WHERE T1.NSRMC = T2.NSRMC AND T2.RN = '1' ) A2\r\n" + 
					" WHERE A1.NSRMC = A2.NSRMC ORDER BY ZS) WHERE ROWNUM <=30";
			sql = getSql2(sql, new Object[] {starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime});
			List<Map<String, Object>> rs = bs.query(sql);	
			Map<String, Object> map = new HashMap<String, Object>();
			String[] cols= {"街道","企业名称","今年地方口径","去年同期地方口径", "减收"};
			  String[] keys= {"JD_MC","NSRMC","JN","QN", "ZS"};
			  map.put("fileName", "减收排名前十户企业税收情况.xlsx"); 
			  map.put("cols", cols); 
			  map.put("keys", keys);
			  map.put("list", rs);
			 return map;		
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
		
	/**
	 * 导出减收数据
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/export.do",produces = "text/plain;charset=utf-8")
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
