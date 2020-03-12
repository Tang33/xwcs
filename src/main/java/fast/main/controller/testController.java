package fast.main.controller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.Super;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonValueProcessor;

public class testController extends Super{
	
	@Autowired
	BaseService bs;
	
	/**
	 * 返回页面的示例
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("unLogin.do")
	public String unLogin(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("请求:unLogin....");
		request.getSession().removeAttribute("UserType");
//		List<Map<String, Object>> image2 = bs.query("select URL from T_INDEXIMG where isdelete='0' and type='2' and rownum<=5 order by imgindex asc");
//		List<Map<String, Object>> image3 = bs.query("select URL from T_INDEXIMG where isdelete='0' and type='3' and rownum<=4 order by imgindex asc");
//		request.setAttribute("image2", image2);//主页轮播图
//		request.setAttribute("image3", image3);//主页广告图
		return "index2";
	}
	
	/**
	 * ajax请求示例
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("querySsClass.do")
	@ResponseBody
	public String ActivityDelete(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		Map<String, Object> UserMap=(Map<String, Object>) request.getSession().getAttribute("UserType");
		String sql = "select tc.id id1, tc.name bkm,ts.name xsm,tf.name zym,tc1.name kcm from t_class_info tc left join t_school ts on tc.schoolid = ts.id left join T_FACULTY_PROFESSIONAL tf\r\n" + 
				"  on tc.subjectid =tf.id left join t_course tc1 on tc.courseid = tc1.id where tc.createuser  =? and tc.isdelete='0'";
		sql =  getSql2(sql, UserMap.get("ID"));
		List<Map<String, Object>> query = bs.query(sql);
		
		return this.toJson("000", "查询用户的所有班课成功",query);
		
		
	}
}
