package fast.main.controller.ImportTaxData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
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
 * 金库对账单
 * 本功能页面用于对导入数据按预算科目代码汇总查询！
 */
@Controller
@RequestMapping("jkdzd")
public class JkdzdController extends Super {

	@Autowired
	BaseService bs;
	
	/**
	 * 进入金库对账单跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "ImportTaxData/jkdzd";
		} catch (Exception e) {
			e.printStackTrace();
			return "ImportTaxData/jkdzd";
		}	
	}
	
	private Map<String, Object> user = null;
	
	//查询当前用户所属街道
	public String findJd(HttpServletRequest request, HttpServletResponse response) {
		String andJd = "";
		try {
			user = (Map<String, Object>)request.getSession().getAttribute("user");
			Integer jd = Integer.parseInt((String)user.get("DWID"));
			if (user == null) {
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
			if (jd != null && jd == 00) {
				andJd = "and 1 = 1";
			}else {
				andJd = "and jd_dm = "+jd;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return andJd;
	}


	/**
	 * 获取纳税人最大月份
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/getNsDate.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String getNsDate(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form){

		try {			
			String jd = this.findJd(request, response);
			String sql = "SELECT to_char(max(RK_RQ),'yyyyMM') as rkrq FROM SB_NSRXX where 1 = 1 "+jd+" ORDER BY RK_RQ DESC  ";
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
	 * 金库对账单信息查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/jkdzdShow.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String jkdzdShow(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> form ) {
		try {
			String jd = this.findJd(request, response);
			Integer  count ;
			String date = request.getParameter("date");
			String page = getValue(form.get("page"));
			String pageSize = getValue(form.get("limit"));
			String sql_check = "select * from xwcs_gsdr_temp where rk_rq='"+date+"'"+ jd;
			if(bs.queryOne(sql_check) == null){
				String sqlall="select f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) QKJ,sum(se) DFKJ from fast_jkdzd where yskmdm is not null and (-rk_rq=?-) group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) QKJ,sum(se) DFKJ from fast_jkdzd where yskmdm is not null and (-rk_rq=?-) group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) QKJ,sum(se) DFKJ from fast_jkdzd where yskmdm is not null and (-rk_rq=?-) group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where DFKJ !='0'"+jd+" order by f.yskmdm";
				sqlall = this.getSql2(sqlall,new Object[] {date,date,date} );
				List<Map<String, Object>> listd=bs.query(sqlall,page,pageSize);
				String ssql = "select 1 from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) QKJ,sum(se) DFKJ from fast_jkdzd where yskmdm is not null and (-rk_rq=?-) group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) QKJ,sum(se) DFKJ from fast_jkdzd where yskmdm is not null and (-rk_rq=?-) group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) QKJ,sum(se) DFKJ from fast_jkdzd where yskmdm is not null and (-rk_rq=?-) group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where DFKJ !='0'"+jd+" order by f.yskmdm";
				count = bs.queryCount(this.getSql2(ssql,new Object[] {date,date,date}));
				return this.toJsonct("000", "查询成功！",listd, getValue(count));
			}else{
				String sqlall="select f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) QKJ,sum(se) DFKJ from xwcs_gsdr_temp where yskmdm is not null and (-rk_rq=?-) group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) QKJ,sum(se) DFKJ from xwcs_gsdr_temp where yskmdm is not null and (-rk_rq=?-) group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) QKJ,sum(se) DFKJ from xwcs_gsdr_temp where yskmdm is not null and (-rk_rq=?-) group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where DFKJ !='0'"+jd+" order by f.yskmdm";
				List<Map<String, Object>> listd = bs.query(this.getSql2(sqlall,new Object[] {date,date,date}),page,pageSize);
				String ssql ="select f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) QKJ,sum(se) DFKJ from xwcs_gsdr_temp where yskmdm is not null and (-rk_rq=?-) group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) QKJ,sum(se) DFKJ from xwcs_gsdr_temp where yskmdm is not null and (-rk_rq=?-) group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) QKJ,sum(se) DFKJ from xwcs_gsdr_temp where yskmdm is not null and (-rk_rq=?-) group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where DFKJ !='0'"+jd+" order by f.yskmdm";
				count =  bs.queryCount(this.getSql2(ssql,new Object[] {date,date,date}));
				return this.toJsonct("000", "查询成功！",listd, getValue(count));
			}    
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		} 
			
	}
	
	/**
	 * 金库对账单导出
	 * @param request
	 * @param response
	 * @param form
	 * @throws IOException
	 */
	@RequestMapping(value="/jkdzdDerive.do",produces = "text/plain;charset=utf-8")
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form) throws IOException {
		
		Map<String, Object> o = new HashMap<String, Object>();	
		try {
			String jd = this.findJd(request, response);
			String yearNmonth = getValue(form.get("yearNmonth")).toString();//年月
			String sql_check = "select * from xwcs_gsdr_temp where rk_rq='"+yearNmonth+"'"+jd;
			String sqlall="";
			if(bs.queryOne(sql_check) == null){
				sqlall="select f.YSKMDM dm,f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) sum1,sum(se) sum2 from fast_jkdzd where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) sum1,sum(se) sum2 from fast_jkdzd where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) sum1,sum(se) sum2 from fast_jkdzd where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where sum2 !='0'"+jd+" order by dm";
			}else{
				sqlall="select f.YSKMDM dm,f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where sum2 !='0'"+jd+" order by dm";
			}
			
			List<Map<String, Object>> map=bs.query(sqlall);
			String[] cols = { "预算科目代码", "预算科目名称", "地方口径" };
			String[] keys = { "YSKMDM", "YSKMMC", "SUM2" };
			o.put("fileName", yearNmonth+"按预算科目代码汇总.xls");
			o.put("cols", cols);
			o.put("keys", keys);
			o.put("list", map);
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
