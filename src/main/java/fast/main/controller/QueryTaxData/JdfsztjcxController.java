package fast.main.controller.QueryTaxData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
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
 * 税收数据查询模板
 * 街道分税种统计查询
 * @author Administrator
 *
 */
@Controller
@RequestMapping("jdfsztjcx")
public class JdfsztjcxController extends Super {

	
	private Map<String, Object> user = null;
	@Autowired
	private BaseService bs;

	/**
	 * 进入街道分税种查询跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		
		return "QueryTaxData/jdfsztjcx";
		
	}

	/**
	 * 街道查询
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/queryJD.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String queryJD(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			
			user = (Map<String, Object>) request.getSession().getAttribute("user");
			
			System.out.println("===========我是街道开始==========");
			System.out.println(user);
			System.out.println("===========我是街道结束==========");
			String andjd = "";
			
			
			
			String dwid = request.getSession().getAttribute("dwid").toString();
			// 街道
			String sql = "";
			List<Map<String, Object>> jdlist = null;
			if("00".equals(dwid)) {
				sql = "select * from dm_jd";
				jdlist = bs.query(sql);
			} else {
				sql = "select * from dm_jd where jd_dm = '"+dwid+"'";
				jdlist = bs.query(sql);
			}
			return this.toJson("000", "查询成功!", jdlist);

		} catch (Exception e) {
			// TODO: handle exception
			return this.toJson("000", "查询失败!");

		}

	}

	/**
	 * 根据街道和日期条件查询信息
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
			String yearNmonth = getValue(form.get("date")).toString();// 年月
			String jddm  = getValue(form.get("jd"));
			String starTime = "";
			//结束时间
			String endTime = "";
			String[] star = yearNmonth.split(" - ");
			if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
				starTime = star[0];
				endTime = star[1];
				starTime = starTime.substring(0, 4) + starTime.substring(5, 7);
				endTime = endTime.substring(0, 4) + endTime.substring(5, 7);
			}
			String sql = "\r\n" + 
					"\r\n" + 
					"select jd.jd_mc JD,xx.* from dm_jd jd ,(\r\n" + 
					"select t.jd_dm JDDM,\r\n" + 
					"       sum(t.zzs * t.bl/100) \"ZZS\",\r\n" + 
					"       sum(yys * t.bl/100) \"YYS\",\r\n" + 
					"       sum(grsds * t.bl/100) \"GRSDS\",\r\n" + 
					"       sum(fcs * t.bl/100) \"FCS\",\r\n" + 
					"       sum(yhs * t.bl/100) \"YHS\",\r\n" + 
					"       sum(ccs * t.bl/100) \"CCS\",\r\n" + 
					"       sum(qysds * t.bl/100) \"QYSDS\",\r\n" + 
					"       sum(ygzzzs * t.bl/100) \"YGZZZS\",\r\n" + 
					"       sum(cswhjss * t.bl/100) \"CSWHJSS\",\r\n" + 
					"       sum(dfjyfj * t.bl/100) \"DFJYFJ\",\r\n" + 
					"       sum(jyfj * t.bl/100) \"JYFFJ\",\r\n" + 
					"       sum(CZTDSYS * t.bl/100) \"CZTDSYS\",\r\n" + 
					"       sum(HBS * t.bl/100) \"HBS\"\r\n" + 
					"       \r\n" + 
					"  from sb_nsrxx t\r\n" + 
					" where t.rk_rq >= to_date('"+starTime+"','yyyyMM')\r\n" + 
					"and t.rk_rq <= to_date('"+endTime+"','yyyyMM')\r\n" + 
					" group by t.jd_dm) xx where xx.JDDM = jd.jd_dm ";

			if(yearNmonth != null && yearNmonth != "" && jddm != "" && jddm != null) {
				sql = sql +"and xx.JDDM= '"+jddm+"'";
			}
			System.out.println(sql);
			List<Map<String, Object>> list = bs.query(sql);
			return this.toJson("000", "查询成功！",list);

		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	/**
	 * 导出excel显示方法
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	public Map<String, Object> export(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			String yearNmonth = getValue(form.get("date")).toString();// 年月
			String jddm  = getValue(form.get("jd"));
			String starTime = "";
			//结束时间
			String endTime = "";
			String[] star = yearNmonth.split(" - ");
			if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
				starTime = star[0];
				endTime = star[1];
				starTime = starTime.substring(0, 4) + starTime.substring(5, 7);
				endTime = endTime.substring(0, 4) + endTime.substring(5, 7);
			}

			String sql = "\r\n" + 
					"\r\n" + 
					"select jd.jd_mc JD,xx.* from dm_jd jd ,(\r\n" + 
					"select t.jd_dm JDDM,\r\n" + 
					"       sum(t.zzs * t.bl/100) \"ZZS\",\r\n" + 
					"       sum(yys * t.bl/100) \"YYS\",\r\n" + 
					"       sum(grsds * t.bl/100) \"GRSDS\",\r\n" + 
					"       sum(fcs * t.bl/100) \"FCS\",\r\n" + 
					"       sum(yhs * t.bl/100) \"YHS\",\r\n" + 
					"       sum(ccs * t.bl/100) \"CCS\",\r\n" + 
					"       sum(qysds * t.bl/100) \"QYSDS\",\r\n" + 
					"       sum(ygzzzs * t.bl/100) \"YGZZZS\",\r\n" + 
					"       sum(cswhjss * t.bl/100) \"CSWHJSS\",\r\n" + 
					"       sum(dfjyfj * t.bl/100) \"DFJYFJ\",\r\n" + 
					"       sum(jyfj * t.bl/100) \"JYFFJ\",\r\n" + 
					"       sum(CZTDSYS * t.bl/100) \"CZTDSYS\",\r\n" + 
					"       sum(HBS * t.bl/100) \"HBS\"\r\n" + 
					"       \r\n" + 
					"  from sb_nsrxx t\r\n" + 
					" where t.rk_rq >= to_date('"+starTime+"','yyyyMM')\r\n" + 
					"and t.rk_rq <= to_date('"+endTime+"','yyyyMM')\r\n" + 
					" group by t.jd_dm) xx where xx.JDDM = jd.jd_dm ";

			if(yearNmonth != null && yearNmonth != "" && jddm != "" && jddm != null) {
				sql = sql +"and xx.JDDM= '"+jddm+"'";
			}
			System.out.println(sql);

			List<Map<String, Object>> list = bs.query(sql);
			for(int i=0;i<list.size();i++){

				BigDecimal aa =(BigDecimal) list.get(i).get("ZZS");

				aa =aa.add((BigDecimal) list.get(i).get("YYS")).add((BigDecimal) list.get(i).get("GRSDS")).add((BigDecimal) list.get(i).get("FCS"))
						.add((BigDecimal) list.get(i).get("YHS")).add((BigDecimal) list.get(i).get("CCS")).add((BigDecimal) list.get(i).get("QYSDS"))
						.add((BigDecimal) list.get(i).get("YGZZZS")).add((BigDecimal) list.get(i).get("CSWHJSS")).add((BigDecimal) list.get(i).get("DFJYFJ"))
						.add((BigDecimal) list.get(i).get("JYFFJ")).add((BigDecimal) list.get(i).get("CZTDSYS")).add((BigDecimal) list.get(i).get("HBS"));
				list.get(i).put("ZJ", aa);
			}

			Map<String, Object> map1 = new HashMap<String, Object>();
			String[] cols = { "街道", "街道代码", "增值税", "营业税" , "个人所得税" , "房产税" , "印花税" , "车船税" , "企业所得税" , "营改增增值税", "城市维护建设税", "地方教育附加", "教育费附加", "城镇土地使用税","环保税","总计" };
			String[] keys = { "JD", "JDDM", "ZZS", "YYS" , "GRSDS", "FCS" , "YHS" , "CCS" , "QYSDS" , "YGZZZS" , "CSWHJSS" , "DFJYFJ" , "JYFFJ" , "CZTDSYS" , "HBS"  , "ZJ"  };
			map1.put("fileName", "街道分税种统计查询.xls");
			map1.put("cols", cols);
			map1.put("keys", keys);
			map1.put("list", list);

			return map1;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 *  执行导出操作
	 * @param request
	 * @param response
	 * @param form
	 * @throws IOException
	 */
	@RequestMapping(value="/exportExcel.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public void exportExcel(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) throws IOException {
		Map<String, Object> o = new HashMap<String, Object>();
		try {
			o = this.export(request, response, form);
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
		}catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}
	}

}
