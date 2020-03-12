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
import fast.main.util.ExcelUtil;
import fast.main.util.Super;

/**
 * 税户查询
 *
 */
@Controller
@RequestMapping("shcx")
public class ShcxContorller extends Super {
	
	@Autowired
	BaseService bs;
	
	/**
	 * 页面跳转至省下放页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		
		return "reportExcel/shcx";
		
	}
	
	/**
	 * 查询税户信息
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/findSh.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String findSh(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> form) {
		try {
//			String rkrq = getValue(form.get("date")).toString();
			String nsrmc = getValue(form.get("nsrmc"));
			String jdmc = getValue(form.get("jdmc"));
			String page = getValue(form.get("page"));
			String pageSize = getValue(form.get("limit"));
//			String starTime = ""; String endTime = ""; 
//			String[] star=rkrq.split(" - ");
//			if(rkrq!=null &&!rkrq.trim().equals("")&&star!=null&&star.length>0) {
//				starTime=star[0]; endTime=star[1];
//				starTime=starTime.substring(0,4)+starTime.substring(5,7);
//				endTime=endTime.substring(0,4)+endTime.substring(5,7); 
//			}
			String sql = "select * from xwcs_sh where 1 = 1 and (-nsrmc like '%' || ? || '%'-) and (-jdxz like '%' || ? || '%'-) ";
			sql = this.getSql2(sql, new Object[] {nsrmc, jdmc});
			System.out.println(sql);
			int count = bs.queryCount(sql);
			List<Map<String, Object>> shlist = bs.query(sql, page, pageSize);
			return this.toJson("000", "查询成功！", shlist,count);
		} catch (Exception e) {
			// TODO: handle exception
			return this.toJson("009", "查询失败！");
		}
	}
	
	/**
	 * 导出税户信息
	 * @param request
	 * @param response
	 * @param form
	 * @throws IOException
	 */
	@RequestMapping(value="/export.do",produces = "text/plain;charset=utf-8")
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form) throws IOException {
		
		Map<String, Object> o = new HashMap<String, Object>();
		
		try {
			String nsrmc = getValue(form.get("nsrmc"));
			String jdmc = getValue(form.get("jdmc"));
			String sql = "select * from xwcs_sh where 1 = 1 and (-nsrmc like '%' || ? || '%'-) and (-jdxz like '%' || ? || '%'-) ";
			sql = this.getSql2(sql, new Object[] {nsrmc, jdmc});
			List<Map<String, Object>> maplist=bs.query(sql);
			String[] cols= {"登记表单展示","纳税人联系信息（有独立查询功能）","社会信用代码（纳税人识别号）","纳税人名称",
					"纳税人状态","课征主体登记类型","登记注册类型","组织机构","国地管户类型","单位隶属关系","批准设立机关",
					"证照名称","证照编号","开业设立日期","从业人数","固定工人数","组织机构类型","会计制度（准则）","经营范围",
					"行业","登记机关","登记日期","主管税务机关","主管税务所（科、分局）","税收管理员","街道乡镇","国有控股类型",
					"国有投资比例","自然人投资比例","外资投资比例","注册资本","投资总额","营改增纳税人类型","办证方式",
					"核算放式","非居名企业标志","跨区财产税主体登记标志","有效标志","注册地址","注册地联系电话","经营地址",
					"经营地联系电话","法定代表人（负责人、业主）姓名","法定代表人（负责人、业主）身份证件名称",
					"法定代表人（负责人、业主）身份证件号码","法定代表人（负责人、业主）固定电话","法定代表人（负责人、业主）移动电话",
					"财务负责人姓名","财务负责人身份证件号码","财务负责人固定电话","财务负责人移动电话","办税人姓名",
					"办税人身份证件","办税人固定电话","办税人移动电话","录入人","录入日期","修改人","修改日期","纳税人编号",
					"税收档案编号","社会信用代码","原纳税人识别号","评估机关","工商注销日期","是否三证合一或两证整合纳税人",
					"受理信息","总分机构类型","总机构信息","分支机构信息","跨区域涉税事项报验管理编号","纳税人主体类型",
					"登记户自定义类别","民营企业"};
			String[] keys= {"DJBDS","NSRLXXX","SHXYDM_NSRSBH","NSRMC","NSRZT","KZZTDJLX","DJZCLX","ZZJG","GDGLLX","DWLSGX",
					"PZSLJG","ZZMC","ZZBH","KYSLRQ","CYRS","GDGRS","ZZJGLX","KJZDZZ","JYFW","HY","DJJG","DJRQ","ZGSWJG","ZGSWS","SSGLY",
					"JDXZ","GYKGLX","GYTZBL","ZRRTZBL","WZTZBL","ZCZB","TZZE","YGZNSRLX","BZFS","HSFS","FJMQYBZ","KQCCSZTDJBZ","YXBZ",
					"ZCDZ","ZCDLXDH","JYDZ","JYDLXDH","FDDBRXM","FDDBRSFZJMC","FDDBRSFZJHM","FDDBRGDDH","FDDBRYDDH","CWFZRXM",
					"CWFZRSFZJHM","CWFZRGDDH","CWFZRYDDH","BSRXM","BSRSFZJ","BSRGDDH","BSRYDDH","LRR","LRRQ","XGR","XGRQ","NSRBH",
					"SSDABH","SHXYDM","YNSRSBH","PGJG","GSZXRQ","SFSZHYHLZZHNSR","SLXX","ZFJGLX","ZJGXX","FZJGXX","KQYSSSXBYGLBH",
					"NSRZTLX","DJHZDYLB","MYQY"};
			o.put("fileName", "税户明细.xls");
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
	
	/**
	 * 获取纳税人信息表中当前最新日期
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping("/getNsDates.do")
	@ResponseBody
	public String getNsDate(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form){

		try {
			String sql = "SELECT to_char(max(RK_RQ),'yyyyMM') as rkrq FROM SB_NSRXX ORDER BY RK_RQ DESC  ";
			List<Map<String, Object>> result = bs.query(sql);
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < result.size(); i++) {
				Map<String, Object> map = result.get(i);
				lists.add(map);
			}

			return this.toJson("000", "查询成功！", lists);

		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}

	}
	
	/**
	 * 初始化查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/aqycx_queryInit.do", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String queryInit(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String dwid = request.getSession().getAttribute("dwid").toString();
			String sql = "";
			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
			// 街道
			if("00".equals(dwid)) {
				List<Map<String, Object>> jdlist = bs.query("select * from dm_jd where  xYbz = '1'");
				map.put("jdlist", jdlist);
			} else {
				sql = "select * from dm_jd where jd_dm = '"+dwid+"'";
				List<Map<String, Object>> jdlist = bs.query(sql);
				map.put("jdlist", jdlist);
			}
			return this.toJson("000", "查询成功！", map);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
}
