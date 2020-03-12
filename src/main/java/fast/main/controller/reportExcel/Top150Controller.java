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
import fast.main.util.Excel3;
import fast.main.util.Super;

/**
 * 前150户企业明细
 *
 */
@Controller
@RequestMapping("top150")
public class Top150Controller extends Super{
	
	@Autowired
	BaseService bs;
	private Map<String, Object> user = null;
	
	/**
	 * 页面跳转至前150查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		
		return "reportExcel/Top150";
		
	}
		
	/**
	 * 查询前150户企业
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
			String sql = "SELECT * FROM \r\n" + 
					"(SELECT B.JD_MC ,A.NSRMC,A.QN,A.JN,A.TQ,A.ZB FROM \r\n" + 
					"(select t1.nsrmc,NVL(t3.HJ,0) QN,NVL(t1.HJ,0) JN,NVL(t2.HJ,0) TQ,NVL(t1.HJ,0)-NVL(t2.HJ,0) ZB from \r\n" + 
					"(select nsrmc,sum(HJ) HJ from (SELECT NSRMC,to_char(rk_rq,'yyyymm') rk_rq,SUM(YGZZZS) * 0.5+SUM(ZZS) * 0.5+SUM(YYS) * 0.5\r\n" + 
					"+SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END))\r\n" + 
					"+SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END))\r\n" + 
					"+SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05')\r\n" + 
					"+SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12')\r\n" + 
					"+SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16')\r\n" + 
					"+SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22')\r\n" + 
					"+SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10')\r\n" + 
					"+SUM(DFJYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '63')\r\n" + 
					"+SUM(JYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '61')\r\n" + 
					"+SUM(CZTDSYS)+SUM(HBS) HJ from sb_nsrxx group by NSRMC,to_char(rk_rq,'yyyymm')) where rk_rq>=?\r\n" + 
					"and rk_rq <= ? group by nsrmc) t1 \r\n" + 
					"left join\r\n" + 
					"(select nsrmc,sum(HJ) HJ from (SELECT NSRMC,rk_rq,SUM(YGZZZS) * 0.5+SUM(ZZS) * 0.5+SUM(YYS) * 0.5\r\n" + 
					"+SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END))\r\n" + 
					"+SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END))\r\n" + 
					"+SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05')\r\n" + 
					"+SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12')\r\n" + 
					"+SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16')\r\n" + 
					"+SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22')\r\n" + 
					"+SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10')\r\n" + 
					"+SUM(DFJYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '63')\r\n" + 
					"+SUM(JYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '61')\r\n" + 
					"+SUM(CZTDSYS)+SUM(HBS) HJ from sb_nsrxx group by NSRMC,rk_rq) where rk_rq>=ADD_MONTHS(TO_DATE(?, 'YYYYMM'),-12)\r\n" + 
					"and rk_rq <= ADD_MONTHS(TO_DATE(?, 'YYYYMM'),-12) group by nsrmc) t2 \r\n" + 
					"on t1.nsrmc = t2.nsrmc\r\n" + 
					"left join \r\n" + 
					"(select nsrmc,sum(HJ) HJ from (SELECT NSRMC,rk_rq,SUM(YGZZZS) * 0.5+SUM(ZZS) * 0.5+SUM(YYS) * 0.5\r\n" + 
					"+SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END))\r\n" + 
					"+SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END))\r\n" + 
					"+SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05')\r\n" + 
					"+SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12')\r\n" + 
					"+SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16')\r\n" + 
					"+SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22')\r\n" + 
					"+SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10')\r\n" + 
					"+SUM(DFJYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '63')\r\n" + 
					"+SUM(JYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '61')\r\n" + 
					"+SUM(CZTDSYS)+SUM(HBS) HJ from sb_nsrxx group by NSRMC,rk_rq) where to_char(rk_rq,'yyyy') = to_char(sysdate,'yyyy')-1 group by nsrmc) t3\r\n" + 
					"on t1.nsrmc = t3.nsrmc ) A,\r\n" + 
					"(SELECT NSRMC,D.JD_MC,ROW_NUMBER() OVER (PARTITION BY NSRMC ORDER BY SUM(ZSE) DESC) RN,SUM(ZSE) HJ FROM SB_NSRXX S \r\n" + 
					"LEFT JOIN DM_JD D \r\n" + 
					"ON S.JD_DM = D.JD_DM \r\n" + 
					"WHERE RK_RQ >= TO_DATE(?, 'YYYYMM') AND RK_RQ <= TO_DATE(?, 'YYYYMM') \r\n" + 
					"GROUP BY NSRMC,JD_MC) B \r\n" + 
					"WHERE A.NSRMC = B.NSRMC AND B.RN = '1' and A.NSRMC NOT LIKE '%预提企业所得税%' AND A.NSRMC != '国家税务总局南京市税务局'\r\n" + 
					"AND B.JD_MC != '不可分' AND B.JD_MC != '外区'\r\n" + 
					"ORDER BY JN DESC) WHERE ROWNUM <=150 \r\n" + 
					"";
			sql = getSql2(sql, new Object[] {starTime,endTime,starTime,endTime,starTime,endTime});
			List<Map<String, Object>> list = bs.query(sql,pageNo,pageSize);	
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	/**
	 * 导出excel数据查询
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
			String sql = "SELECT * FROM \r\n" + 
					"(SELECT B.JD_MC ,A.NSRMC,A.QN,A.JN,A.TQ,A.ZB FROM \r\n" + 
					"(select t1.nsrmc,NVL(t3.HJ,0) QN,NVL(t1.HJ,0) JN,NVL(t2.HJ,0) TQ,NVL(t1.HJ,0)-NVL(t2.HJ,0) ZB from \r\n" + 
					"(select nsrmc,sum(HJ) HJ from (SELECT NSRMC,to_char(rk_rq,'yyyymm') rk_rq,SUM(YGZZZS) * 0.5+SUM(ZZS) * 0.5+SUM(YYS) * 0.5\r\n" + 
					"+SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END))\r\n" + 
					"+SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END))\r\n" + 
					"+SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05')\r\n" + 
					"+SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12')\r\n" + 
					"+SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16')\r\n" + 
					"+SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22')\r\n" + 
					"+SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10')\r\n" + 
					"+SUM(DFJYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '63')\r\n" + 
					"+SUM(JYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '61')\r\n" + 
					"+SUM(CZTDSYS)+SUM(HBS) HJ from sb_nsrxx group by NSRMC,to_char(rk_rq,'yyyymm')) where rk_rq>=?\r\n" + 
					"and rk_rq <= ? group by nsrmc) t1 \r\n" + 
					"left join\r\n" + 
					"(select nsrmc,sum(HJ) HJ from (SELECT NSRMC,rk_rq,SUM(YGZZZS) * 0.5+SUM(ZZS) * 0.5+SUM(YYS) * 0.5\r\n" + 
					"+SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END))\r\n" + 
					"+SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END))\r\n" + 
					"+SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05')\r\n" + 
					"+SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12')\r\n" + 
					"+SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16')\r\n" + 
					"+SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22')\r\n" + 
					"+SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10')\r\n" + 
					"+SUM(DFJYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '63')\r\n" + 
					"+SUM(JYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '61')\r\n" + 
					"+SUM(CZTDSYS)+SUM(HBS) HJ from sb_nsrxx group by NSRMC,rk_rq) where rk_rq>=ADD_MONTHS(TO_DATE(?, 'YYYYMM'),-12)\r\n" + 
					"and rk_rq <= ADD_MONTHS(TO_DATE(?, 'YYYYMM'),-12) group by nsrmc) t2 \r\n" + 
					"on t1.nsrmc = t2.nsrmc\r\n" + 
					"left join \r\n" + 
					"(select nsrmc,sum(HJ) HJ from (SELECT NSRMC,rk_rq,SUM(YGZZZS) * 0.5+SUM(ZZS) * 0.5+SUM(YYS) * 0.5\r\n" + 
					"+SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END))\r\n" + 
					"+SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END))\r\n" + 
					"+SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05')\r\n" + 
					"+SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12')\r\n" + 
					"+SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16')\r\n" + 
					"+SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22')\r\n" + 
					"+SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10')\r\n" + 
					"+SUM(DFJYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '63')\r\n" + 
					"+SUM(JYFJ) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '61')\r\n" + 
					"+SUM(CZTDSYS)+SUM(HBS) HJ from sb_nsrxx group by NSRMC,rk_rq) where to_char(rk_rq,'yyyy') = to_char(sysdate,'yyyy')-1 group by nsrmc) t3\r\n" + 
					"on t1.nsrmc = t3.nsrmc ) A,\r\n" + 
					"(SELECT NSRMC,D.JD_MC,ROW_NUMBER() OVER (PARTITION BY NSRMC ORDER BY SUM(ZSE) DESC) RN,SUM(ZSE) HJ FROM SB_NSRXX S \r\n" + 
					"LEFT JOIN DM_JD D \r\n" + 
					"ON S.JD_DM = D.JD_DM \r\n" + 
					"WHERE RK_RQ >= TO_DATE(?, 'YYYYMM') AND RK_RQ <= TO_DATE(?, 'YYYYMM') \r\n" + 
					"GROUP BY NSRMC,JD_MC) B \r\n" + 
					"WHERE A.NSRMC = B.NSRMC AND B.RN = '1' and A.NSRMC NOT LIKE '%预提企业所得税%' AND A.NSRMC != '国家税务总局南京市税务局'\r\n" + 
					"AND B.JD_MC != '不可分' AND B.JD_MC != '外区'\r\n" + 
					"ORDER BY JN DESC) WHERE ROWNUM <=150 \r\n" + 
					"";
			sql = getSql2(sql, new Object[] {starTime,endTime,starTime,endTime,starTime,endTime});
			List<Map<String, Object>> rs = bs.query(sql);	
			Map<String, Object> map = new HashMap<String, Object>();
			String[] cols= {"街道","企业名称","去年入库","今年入库", "今年同期","直比增减"};
			  String[] keys= {"JD_MC","NSRMC","QN","JN", "TQ", "ZB"};
			  map.put("fileName", "前150户企业明细.xlsx"); 
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
	 * 导出excel数据
	 * @param request
	 * @param response
	 * @param form
	 * @throws IOException
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
