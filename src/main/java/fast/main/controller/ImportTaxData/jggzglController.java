package fast.main.controller.ImportTaxData;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import fast.main.service.BaseService;
import fast.main.util.Super;

/**
 * 加工规则管理
 * 本功能页面用于对加工规则的管理！
 */
@Controller
@RequestMapping("jggzgl")
public class jggzglController extends Super {

	private Map<String, Object> user = null;
	@Autowired
	BaseService bs;
	
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "ImportTaxData/jggzgl";
		} catch (Exception e) {
			e.printStackTrace();
			return "ImportTaxData/jggzgl";
		}
	}
	
	/**
	 * 加工规则查询
	 * @return
	 */
	@RequestMapping("jggzquery.do")
	@ResponseBody
	public String doClean(HttpServletRequest request, HttpServletResponse response){
		user = (Map<String, Object>)request.getSession().getAttribute("user");
		try{
			String pageNo = getValue(request.getParameter("page"));			//第几页
			String pageSize = getValue(request.getParameter("limit"));		//显示几条		
			String sql="select t.id,t.mbmc,t.createtime cjsj,t.status zt,to_char(t.ms) ms from fast_mb t order by t.createtime desc";
			int count = bs.queryCount(sql);//查询数据总数
			List<Map<String, Object>> list = bs.query(sql,pageNo,pageSize);//查询具体数据	
			return this.toJsonct("000", "查询成功！", list, Integer.toString(count));		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return this.toJson("000", "系统异常!");
		}
	}
	
	/**
	 * 修改加个规则状态
	 * @return
	 */
	@RequestMapping("updatejggzStatus.do")
	@ResponseBody
	public String updatejggzStatus(HttpServletRequest request, HttpServletResponse response){
		//获取登录的用户信息
		user = (Map<String, Object>)request.getSession().getAttribute("user");
		try{
			String status = getValue(request.getParameter("status"));		//0 or 1
			String id = getValue(request.getParameter("id"));				//获取模板id
			//根据模板id修改模板表中当前数据的启用于不启用
			String updatesql = " update fast_mb set status=? where id =?";
			updatesql = getSql2(updatesql, new Object[] {status,id});
			Integer upnum = bs.update(updatesql);
			if(upnum!=0) {		
				return this.toJson("000", "修改成功！");
			}else {
				
				return this.toJson("000", "修改失败！");
			}	
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return this.toJson("000", "系统异常!");
		}
		
		
	
	}
	
	/**
	 * 删除加工规则
	 * @return
	 */
	@RequestMapping("deleteJggz.do")
	@ResponseBody
	public String deleteJggz(HttpServletRequest request, HttpServletResponse response){
		//获取登录的用户信息
		user = (Map<String, Object>)request.getSession().getAttribute("user");
		try{
			//获取模板id
			String id = getValue(request.getParameter("ID"));
			String updatesql = "delete from FAST_MB where id = ?";
			updatesql = getSql2(updatesql, id);
			Integer upnum = bs.update(updatesql);
			if(upnum!=0) {
				return this.toJson("000", "删除成功！");
			}else {
				return this.toJson("001", "删除失败！");
			}	
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return this.toJson("000", "系统异常!");
		}
		
		
	
	}
}
