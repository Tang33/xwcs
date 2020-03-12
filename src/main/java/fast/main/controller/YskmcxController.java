package fast.main.controller;

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
 * 预算科目查询
 *
 */
@Controller
@RequestMapping("yskmcx")
public class YskmcxController extends Super{
	@Autowired 
	BaseService bs;
	private Map<String, Object> user = null;
	
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
	 * 导出预算科目查询excel
	 * @param request
	 * @param response
	 * @param form
	 * @throws IOException
	 */
	@RequestMapping("/exports.do")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form) throws IOException {

		Map<String, Object> o = new HashMap<String, Object>();

		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String yearNmonth = getValue(form.get("yearNmonth")).toString();//年月
			//根据月份时间查询当月在xwcs_gsdr_temp表查询数据
			String sql_check = "select * from xwcs_gsdr_temp where rk_rq='"+yearNmonth+"'";
			String sqlall="";
			/*
			 * 根据sql_check中的sql语句查询出的数据进行判断
			 * 查询出有数据进入if，将if里面的sql赋值给sqlall
			 * 查询出无数据进入else，将else里面的sql赋值给sqlall
			 */
			if(bs.queryOne(sql_check) == null){
				//sql查询的时fast_jkdzd表里的数据
				sqlall="select f.YSKMDM dm,f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) sum1,sum(se) sum2 from fast_jkdzd where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) sum1,sum(se) sum2 from fast_jkdzd where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) sum1,sum(se) sum2 from fast_jkdzd where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where sum2 !='0' order by dm";
			}else{
				//sql查询的时xwcs_gsdr_temp表里的数据
				sqlall="select f.YSKMDM dm,f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where sum2 !='0' order by dm";
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

	/**
	 * 加载预算科目数据
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping("/yskm.do")
	@ResponseBody
	public String yskm(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, String> form) {
		try {
			String page = getValue(form.get("page"));			//获取第几页
			String pagesize = getValue(form.get("limit"));		//获取每页显示几条
			String date = getValue(form.get("date"));			//获取选择的日期
			int count = 0;
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			//根据日期查询临时表数据
			String sql_check = "select * from xwcs_gsdr_temp where rk_rq='"+date+"'";
			/*
			 * 根据sql_check中的sql语句查询出的数据进行判断
			 * 查询出有数据进入if，根据if里面的sql进行查询
			 * 查询出无数据进入else，根据else里面的sql进行查询
			 */
			if(bs.queryOne(sql_check) == null){
				String sqlall="select f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) qkj,sum(dfzse) dfkj from SB_NSRXX where yskmdm is not null and to_char(rk_rq,'yyyymm')='"+date+"' group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) qkj,sum(dfzse) dfkj from SB_NSRXX where yskmdm is not null and to_char(rk_rq,'yyyymm')='"+date+"' group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) qkj,sum(dfzse) dfkj from SB_NSRXX where yskmdm is not null and to_char(rk_rq,'yyyymm')='"+date+"' group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where dfkj !='0' order by f.yskmdm";
				List<Map<String, Object>> listd=bs.query(sqlall,page,pagesize);
				count = bs.queryCount(sqlall);
				return this.toJson("000", "查询成功！",listd, count);
			}else{
				String sqlall="select f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) qkj,sum(se) dfkj from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+date+"' group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) qkj,sum(se) dfkj from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+date+"' group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) qkj,sum(se) dfkj from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+date+"' group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where dfkj !='0' order by f.yskmdm";
				List<Map<String, Object>> listd=bs.query(sqlall,page,pagesize);
				count = bs.queryCount(sqlall);
				return this.toJson("000", "查询成功！",listd, count);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}


}
