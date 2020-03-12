package fast.main.controller.reportExcel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
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
import fast.main.util.ExcelRead;
import fast.main.util.ExcelUtil;
import fast.main.util.Super;

/**
 * 省下放
 *
 */
@Controller
@RequestMapping("Province")
public class ProvinceController extends Super{

	@Autowired
	BaseService bs;
	private Map<String, Object> user = null;
	private static Connection connection = null;

	/**
	 * 页面跳转至省下放页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		
		return "reportExcel/Province";
		
	}
	
	/**
	 * 查询省下放数据
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
			String sql = "select ID,nsrmc NSRMC,sum(DFZSE) DFZSE,sum(ZZS) ZZS,sum(YYS) YYS,sum(QYSDS) QYSDS,sum(GRSDS) GRSDS,sum(CSWHJSS) CSWHJSS,SUM(CZTDSYS) CZTDSYS,SUM(YHS) YHS,SUM(FCS) FCS,\r\n" + 
					"SUM(CCS) CCS,SUM(HBS) HBS,sum(ZZS)+sum(YYS)+sum(QYSDS)+sum(GRSDS)+sum(CSWHJSS)+SUM(CZTDSYS)+SUM(YHS)+SUM(FCS)+SUM(CCS)+SUM(HBS) HJ\r\n" + 
					"from PROVINCE where RK_RQ >= ? and RK_RQ <= ? GROUP BY NSRMC,ID ORDER BY id";
			sql = getSql2(sql, new Object[] {starTime,endTime});
			List<Map<String, Object>> list = bs.query(sql,pageNo,pageSize);	
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
		
	/**
	 * 根据模板匹配单月数据并存入数据库表中
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/addData.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String addData(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		InputStream is = null;
		try {
			//获取查询时间段
			String chooseMonth = getValue(form.get("date")).toString().replace("-", "");
			//匹配数据前先查询sb_nsrxx是否存在该月份数据
			int num = bs.queryCount("select * from sb_nsrxx where rk_rq = to_date('"+chooseMonth+"','yyyymm')");
			if (num != 0) {
				is = new FileInputStream(new File("D:\\uploadfiles1\\省下放模板.xlsx"));
				// 判断接收到的文件格式
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put("企业名称", -1);
				map.put("求和项:增值税（10101）", -1);
				map.put("求和项:营业税（10103）", -1);
				map.put("求和项:企业所得税（10104）", -1);
				map.put("求和项:个人所得税（10106）", -1);
				map.put("求和项:城市维护建设税（10109）", -1);
				map.put("求和项:城镇土地使用税（10112）", -1);
				map.put("求和项:印花税（10111）", -1);
				map.put("求和项:房产税（10110）", -1);
				map.put("求和项:车船税（10114）", -1);
				map.put("求和项:环境保护税（10121）", -1);
				//读取省下放数据的模板
				List<Map<String, String>> list = ExcelRead.pomExcel("D:\\uploadfiles1\\省下放模板.xlsx", is, map);
				//查询省下放企业的各税种
				String sql = "SELECT A.ID,A.NSRMC,NVL(B.ZZS,0) ZZS,NVL(B.YYS,0) YYS,NVL(B.QYSDS,0) QYSDS,NVL(B.GRSDS,0) GRSDS,\r\n" + 
						"NVL(B.CSWHJSS,0) CSWHJSS,NVL(B.CZTDSYS,0) CZTDSYS,NVL(B.YHS,0) YHS,NVL(B.FCS,0) FCS,NVL(B.CCS,0) CCS,\r\n" + 
						"NVL(B.HBS,0) HBS,NVL(B.DFZSE,0) DFZSE FROM PROVINCE_NSRMC A LEFT JOIN \r\n" + 
						"(SELECT NSRMC,SUM(YGZZZS + ZZS) * 0.5 ZZS,SUM(YYS) * 0.5 YYS,\r\n" + 
						"SUM(QYSDS_GS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) + \r\n" + 
						"SUM(QYSDS_DS * (CASE WHEN BL IS NOT NULL THEN BL / 100 ELSE 0.4 END)) QYSDS,\r\n" + 
						"SUM(GRSDS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '05') GRSDS,\r\n" + 
						"SUM(CSWHJSS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '10') CSWHJSS,\r\n" + 
						"SUM(CZTDSYS) CZTDSYS,\r\n" + 
						"SUM(YHS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '16') YHS,\r\n" + 
						"SUM(FCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '12') FCS,\r\n" + 
						"SUM(CCS) * (SELECT MAX(sz.dffpbl) FROM dm_zsxm sz WHERE sz.xybz = 'Y' AND sz.zsxm_dm = '22') CCS,\r\n" + 
						"SUM(HBS) HBS,SUM(DFZSE) DFZSE FROM SB_NSRXX WHERE RK_RQ = TO_DATE('"+chooseMonth+"', 'YYYYMM') GROUP BY NSRMC) B\r\n" + 
						"ON A.NSRMC= B.NSRMC ORDER BY A.ID";
				List<Map<String, Object>> list2 = bs.query(sql);
				//list3用来存匹配好的数据
				List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < list.size(); i++) {
					Map<String, String> map1 = list.get(i);
					for (int j = 0; j < list2.size(); j++) {
						Map<String, Object> map2 = list2.get(j);
						if (map1.get("企业名称").equals(map2.get("NSRMC"))) {
							Map<String, Object> map3 = new HashMap<String, Object>();
							if (map1.get("求和项:增值税（10101）").equals("0")) {
								map2.put("ZZS", "0");
							}
							if (map1.get("求和项:营业税（10103）").equals("0")) {
								map2.put("YYS", "0");
							}
							if (map1.get("求和项:企业所得税（10104）").equals("0")) {
								map2.put("QYSDS", "0");
							}
							if (map1.get("求和项:个人所得税（10106）").equals("0")) {
								map2.put("GRSDS", "0");
							}
							if (map1.get("求和项:城市维护建设税（10109）").equals("0")) {
								map2.put("CSWHJSS", "0");
							}
							if (map1.get("求和项:城镇土地使用税（10112）").equals("0")) {
								map2.put("CZTDSYS", "0");
							}
							if (map1.get("求和项:印花税（10111）").equals("0")) {
								map2.put("YHS", "0");
							}
							if (map1.get("求和项:房产税（10110）").equals("0")) {
								map2.put("FCS", "0");
							}
							if (map1.get("求和项:车船税（10114）").equals("0")) {
								map2.put("CCS", "0");
							}
							if (map1.get("求和项:环境保护税（10121）").equals("0")) {
								map2.put("HBS", "0");
							}
							map3.put("ID", map2.get("ID"));
							map3.put("NSRMC", map2.get("NSRMC"));
							map3.put("ZZS", map2.get("ZZS"));
							map3.put("YYS", map2.get("YYS"));
							map3.put("QYSDS", map2.get("QYSDS"));
							map3.put("GRSDS", map2.get("GRSDS"));
							map3.put("CSWHJSS", map2.get("CSWHJSS"));
							map3.put("CZTDSYS", map2.get("CZTDSYS"));
							map3.put("YHS", map2.get("YHS"));
							map3.put("FCS", map2.get("FCS"));
							map3.put("CCS", map2.get("CCS"));
							map3.put("HBS", map2.get("HBS"));
							map3.put("DFZSE", map2.get("DFZSE"));
							list3.add(map3);					
						}
					}
				}
				//插入数据前先查询是否存在该月份数据
				int count = bs.queryCount("select * from province where rk_rq = '"+chooseMonth+"'");
				if (count == 0) {
					//循环往表内插入数据
					String sql1 = "insert all ";
					for (Map<String, Object> mappp3 : list3) {
						sql1 += " into province (id,nsrmc,zzs,yys,qysds,grsds,cswhjss,cztdsys,yhs,fcs,ccs,hbs,rk_rq,dfzse) VALUES ("+mappp3.get("ID")+",'"+mappp3.get("NSRMC")+"','"+mappp3.get("ZZS")+"','"+mappp3.get("YYS")+"','"
								+ mappp3.get("QYSDS")+"','"+mappp3.get("GRSDS")+"','"+mappp3.get("CSWHJSS")+"','"+mappp3.get("CZTDSYS")+"','"
								+ mappp3.get("YHS")+ "','"+mappp3.get("FCS")+ "','"+mappp3.get("CCS")+ "','"+mappp3.get("HBS")+ "','"+chooseMonth+ "','"+mappp3.get("DFZSE")+"') ";
					}
					sql1 += "SELECT 1 FROM DUAL";
					//插入合计列
					String sql2 = "insert into province (id,nsrmc,zzs,yys,qysds,grsds,cswhjss,cztdsys,yhs,fcs,ccs,hbs,rk_rq,dfzse) "
							+ "select 277,'总合计',sum(ZZS),sum(YYS),sum(QYSDS),sum(GRSDS),sum(CSWHJSS),SUM(CZTDSYS),SUM(YHS),SUM(FCS),SUM(CCS),SUM(HBS),'"+chooseMonth+"',SUM(DFZSE) from province where rk_rq = '"+chooseMonth+"'";
					bs.insert(sql1);
					bs.insert(sql2);
					return this.toJson("000", "数据生成完成！");
				} else {
					return this.toJson("007", "已存在该月份数据！");
				}
			} else {
				return this.toJson("001", "该月份数据未加工！");
			}	
		} catch (IOException e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}      
	}
	
	/**
	 * 查询导出excel数据
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
			String sql = "select ID,nsrmc NSRMC,sum(DFZSE) DFZSE,sum(ZZS) ZZS,sum(YYS) YYS,sum(QYSDS) QYSDS,sum(GRSDS) GRSDS,sum(CSWHJSS) CSWHJSS,SUM(CZTDSYS) CZTDSYS,SUM(YHS) YHS,SUM(FCS) FCS,\r\n" + 
					"SUM(CCS) CCS,SUM(HBS) HBS,sum(ZZS)+sum(YYS)+sum(QYSDS)+sum(GRSDS)+sum(CSWHJSS)+SUM(CZTDSYS)+SUM(YHS)+SUM(FCS)+SUM(CCS)+SUM(HBS) HJ\r\n" + 
					"from PROVINCE where RK_RQ >= ? and RK_RQ <= ? GROUP BY NSRMC,ID ORDER BY id";
			sql = getSql2(sql, new Object[] {starTime,endTime});
			List<Map<String, Object>> rs = bs.query(sql);	
			Map<String, Object> map = new HashMap<String, Object>();
			String[] cols= {"企业名称","地方口径","合计","增值税","营业税","企业所得税", "个人所得税","城市维护建设税","城镇土地使用税",
					"印花税","房产税","车船税","环保税"};
			String[] keys= {"NSRMC","DFZSE","HJ","ZZS","YYS","QYSDS","GRSDS","CSWHJSS","CZTDSYS","YHS","FCS","CCS","HBS"};
			map.put("fileName", "省下放数据.xlsx"); 
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
	 * 导出excel
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
	
	
	/**
	 * 删除
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/deleteData.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String deleteData(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		//获取查询时间段
		String chooseMonth = getValue(form.get("date")).toString().replace("-", "");
		//删除数据前先查询是否存在该月份数据
		int num = bs.queryCount("select * from province where rk_rq = '"+chooseMonth+"'");
		if (num != 0) {
			String sql = "delete from province where rk_rq = '"+chooseMonth+"'";
			int count = bs.delete(sql);
			if(count != 0 ) {
				return this.toJson("001", "删除成功！");
			} else {
				return this.toJson("002", "删除失败！");
			}
		} else {
			return this.toJson("003", "不存在该月份数据！");
		}	
	}
}
