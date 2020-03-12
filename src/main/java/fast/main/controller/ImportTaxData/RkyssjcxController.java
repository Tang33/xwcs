package fast.main.controller.ImportTaxData;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;

import fast.main.service.BaseService;
import fast.main.util.Super;

/**
 * 入库原始数据查询
 * @param rmap
 * @return
 */

@Controller
@RequestMapping("rkyssjcx")
public class RkyssjcxController extends Super{
	@Autowired 
	BaseService bs;
	private Map<String, Object> user = null;
	
	
	/**
	 * 查询展示 maplist table表格数据的查询
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping("/querySdbg.do")
	@ResponseBody
	public String querySdbg(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, String> form) {
		
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String page = getValue(form.get("page"));
			String pagesize = getValue(form.get("limit"));
			// 获取页面传递过来的input中的值
			String fz = getValue(form.get("fz"));
			// 获取表单中的数据
			String form1 = getValue(form.get("form"));
			String rk_rq2 = getValue(form.get("drrq"));
			String arr[] = rk_rq2.split("-");
			String rk_rq = arr[0] + arr[1];

			String sqlcs = "";

			JSONArray form2 = JSONArray.parseArray(form1);
			//下标从一开始取值
			if (form2.size() > 1) {
				for (int i = 1; i < form2.size(); i++) {
						JSONObject obj = (JSONObject) JSONObject.toJSON(form2.get(i));
						sqlcs += " " + getValue(obj.get("value"));
				}
			}

			String sql = "select * from xwcs_gsdr_yssjrk  where 1=1 and (-rk_rq=?-)";
			sql += sqlcs;

			if (!fz.equals("")) {
				
				if (fz.equals("sjje")) {
					sql = "select t." + fz + ",sum(t.qxj) qxj,"
							+ "SUM(t.zyj) zyj,SUM(t.ssj) ssj,SUM(t.dsj) dsj,SUM(t.xzj) xzj from xwcs_gsdr_yssjrk t where 1=1 and (-rk_rq=?-) ";
				} else {

					sql = "select t." + fz + ",sum(t.sjje) sjje,sum(t.qxj) qxj,"
							+ "SUM(t.zyj) zyj,SUM(t.ssj) ssj,SUM(t.dsj) dsj,SUM(t.xzj) xzj from xwcs_gsdr_yssjrk t where 1=1 and (-rk_rq=?-) ";
				}

				sql += sqlcs;
		
				sql += " group by t." + fz + " ";
			}
			
			sql=this.getSql2(sql, rk_rq);
			
			// 执行sql 执行sql

			List<Map<String, Object>> sjjgall = bs.query(sql,page,pagesize);
			
			// 查询count
			String sqlcount = "select 1 from xwcs_gsdr_yssjrk a where 1=1 and (-rk_rq=?-) " + sqlcs;
			if (!fz.equals("")) {
				sqlcount += " group by a." + fz + " ";
				sqlcount = "select 1  from (" + sqlcount + ")";
			}
			sqlcount=this.getSql2(sqlcount, rk_rq);

			int sjjgallcount = bs.queryCount(sqlcount);

			
			// 数据

			return this.toJson("000", "查询成功！", sjjgall, sjjgallcount);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
		
		
}
