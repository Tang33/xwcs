package fast.main.controller.ImportTaxData;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;

import fast.main.service.BaseService;
import fast.main.util.Super;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 原始数据表
 * 个人所得税查询
 * @author Administrator
 *
 */
@Controller
@RequestMapping("grsdsyssjcx")
public class GrsdsyssjcxController  extends Super{

	private Map<String, Object> user = null;
	String andjd = "";
	@Autowired
	private BaseService bs;

	/**
	 * 个人所得税查询展示
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/querySdbg.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String querySdbg(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {

			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String jd = getValue(user.get("DWID"));

			String jdmcSql = "select JD_MC  FROM DM_JD where JD_DM = "+jd+" " ;
			List<Map<String,Object>> jdmcList = bs.query(jdmcSql);
			String jdmc = (String)jdmcList.get(0).get("JD_MC");

			if( null != jd  && "00".equals(jd) ) {

				andjd  = " and 1 = 1";

			}else{

				andjd = " and jdxz like '%"+jdmc+"%' " ;

			}

			String page = getValue(form.get("page"));
			String pagesize = getValue(form.get("limit"));
			//获取页面传递过来的input中的值 
			String fz = getValue(form.get("fz"));
			String rk_rq2 = getValue(form.get("drrq"));
			String arr[] = rk_rq2.split("-");
			String rk_rq = arr[0] + arr[1];
			//获取表单中的数据
			String form1 = getValue(form.get("form"));

			String sqlcs="";

			JSONArray form2  =JSONArray.parseArray(form1);
			//下标从一开始取值
			if (form2.size() > 1) {
				for (int i = 1; i < form2.size(); i++) {
					JSONObject obj = (JSONObject) JSONObject.toJSON(form2.get(i));
					sqlcs += " " + getValue(obj.get("value"));
				}
			}
			String sql = "select * from xwcs_gsdr_yssjgs where 1=1 and (-rk_rq=?-) "+andjd+" ";
			sql+=sqlcs;


			if (!fz.equals("")) {

				if (fz.equals("yzsje")) {
					sql = "select t."+fz+" from xwcs_gsdr_yssjgs t where 1=1 and (-rk_rq=?-) "+andjd+" ";

				} else {
					sql = "select t."+fz+",sum(t.yzsje) yzsje from xwcs_gsdr_yssjgs t where 1=1 and (-rk_rq=?-) "+andjd+" ";
				}


				sql+=sqlcs;


				sql+=" group by t."+fz+" ";
			}
			sql = this.getSql2(sql, rk_rq);

			List<Map<String, Object>> sjjgall = bs.query(sql,page,pagesize);



			//查询count
			int count =0;

			count = bs.queryCount(sql);


			return this.toJson("000", "查询成功！", sjjgall, count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}


	/**
	 * 查询条件下拉框
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/selectList.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String selectList(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {

			String tableName =getValue(form.get("tableName"));
			tableName = tableName.toLowerCase();
			if (tableName == "") {
				tableName = "xwcs_gsdr_yssjrk";
				String sql = "select MBM,ZDM FROM  FAST_YSSJCX_MB where DYCXB = '"+tableName+"' group by MBM,ZDM";
				List<Map<String,Object>> list = bs.query(sql);
				return this.toJson("000","查询成功", list);
			} else {
				String sql = "select MBM,ZDM FROM  FAST_YSSJCX_MB where DYCXB = '"+tableName+"' group by MBM,ZDM";
				List<Map<String,Object>> list = bs.query(sql);
				return this.toJson("000","查询成功", list);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常");
		}
	}

	/**
	 * 汇总条件下拉框查询
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/queryfz.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String queryfz(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			String tableName =getValue(form.get("tableName"));
			tableName = tableName.toLowerCase();
			if (tableName == "") {
				tableName = "xwcs_gsdr_yssjrk";
				String sql = "SELECT DISTINCT mbm,zdm FROM FAST_YSSJCX_MB where DYCXB = '"+tableName+"'";
				List<Map<String,Object>> list = bs.query(sql);
				return this.toJson("000","查询成功", list);
			} else {
				String sql = "SELECT DISTINCT mbm,zdm FROM FAST_YSSJCX_MB where DYCXB = '"+tableName+"'";
				List<Map<String,Object>> list = bs.query(sql);
				return this.toJson("000","查询成功", list);
			}
		} catch (Exception e) {
			return this.toJson("009", "查询异常");
		}
	}

	/**
	 * text=="街道乡镇"||text=="征收项目"||text=="征收品目"||text=="行业大类"||text=="行业中类"||text=="行业门类"||text=="行业"||text=="登记注册类型"
	 * 
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/selectSZ.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String selectSZ(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {


			user = (Map<String, Object>) request.getSession().getAttribute("user");

			String jd = getValue(user.get("DWID"));

			String jdmcSql = "select JD_MC  FROM DM_JD where JD_DM = "+jd+" " ;
			List<Map<String,Object>> jdmcList = bs.query(jdmcSql);
			String jdmc = (String)jdmcList.get(0).get("JD_MC");

			if( null != jd  && "00".equals(jd)) {

				andjd  = " and 1 = 1";

			}else{

				andjd = " and jdxz like '%"+jdmc+"%' " ;

			}

			String xz = getValue(form.get("xz"));
			String tableName = getValue(form.get("tableName")); 
			String sql = "select DISTINCT "+xz+" selname from "+tableName+" where 1 = 1 "+andjd+" ";

			List<Map<String,Object>> list = bs.query(sql);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) == null) {
					list.remove(i);
					i--;
				}
			}
			return this.toJson("000","查询成功", list);

		}catch(Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常");
		}
	}


	/**
	 * text=="登记注册类型"||text=="预算分配比例"||text=="税款种类"||text=="总分机构类型"||text=="跨地区税收转移企业类型"||text=="应征凭证种类"||text=="汇总票证种类"||text=="调账类型"||text=="银行行别"||text=="票证种类"
	 * 
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/queryzd.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String queryzd(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {

			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String jd = getValue(user.get("DWID"));

			String jdmcSql = "select JD_MC  FROM DM_JD where JD_DM = "+jd+" " ;
			List<Map<String,Object>> jdmcList = bs.query(jdmcSql);
			String jdmc = (String)jdmcList.get(0).get("JD_MC");

			if( null != jd  && "00".equals(jd) ) {
				andjd  = " where 1 = 1";
			}else{
				andjd = " where jdxz like '%"+jdmc+"%' " ;
			}

			String xz = getValue(form.get("xz"));
			SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -1);
			Date m = c.getTime();
			String rkrq = format.format(m);
			String tableName = getValue(form.get("tableName")); 
			String sql="select "+xz+" xmmc from "+tableName+"  "+andjd+" group by "+xz;
			List<Map<String, Object>> sjjgall = bs.query(sql);
			List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
			Map<String, String> map;
			for (int i = 0; i < sjjgall.size(); i++) {
				if (sjjgall.get(i) == null) {
					sjjgall.remove(i);
					i--;
				}else {
					map = new HashMap<String, String>();
					for (String key : sjjgall.get(i).keySet()) {
						map.put(key.toLowerCase(), sjjgall.get(i).get(key).toString());
					}

					list1.add(map);
				}  
			}
			return this.toJson("000", "查询成功！", list1);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	/**
	 * 带 期 子 查询
	 * 
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/selectislike.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String selectislike(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {

			String zdm = getValue(form.get("xz"));
			String tableName = getValue(form.get("tableName")); 
			String sql = "select * from fast_yssjcx_mb where ZDM = '" + zdm + "' and DYCXB = '"+tableName+"'";
			List<Map<String,Object>> list = bs.query(sql);
			return this.toJson("000","查询成功", list);
		}catch(Exception e) {
			return this.toJson("009", "查询异常");
		}
	}

}
