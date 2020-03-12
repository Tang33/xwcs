package fast.main.controller.ImportTaxData;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
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
@Controller
@RequestMapping("jrkyssjcyController")
public class jrkyssjcyController extends Super {

	private Map<String, Object> user = null;
	@Autowired
	BaseService bs;
	
	
	/**
	 * 净入库原始数据查询
	 * @return
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping("jrkyssjquery.do")
	@ResponseBody
	public String query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		user = (Map<String, Object>) request.getSession().getAttribute("user");

		String jd = getValue(user.get("DWID"));
		String andjd = "";
		if( null != jd  && "00".equals(jd) ) {
			
			andjd  = " and 1 = 1";
			
		}else{
			
			andjd = " and jd_dm = "+jd+" " ;
			
		}
		if(null == user){
			
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
		try{
			String page = getValue(request.getParameter("page"));
			String pagesize = getValue(request.getParameter("limit"));
			
			//获取页面传递过来的input中的值
			String fz = getValue(request.getParameter("fz"));
			//获取表单中的数据
			String form = getValue(request.getParameter("form"));
			String rk_rq2 = getValue(request.getParameter("drrq"));
			String arr[] = rk_rq2.split("-");
			String rk_rq = arr[0] + arr[1];
			
			String sqlcs="";
			
			
			JSONArray form1  =JSONArray.parseArray(form);
			//下标从一开始取值
			if (form1.size() > 1) {
				for (int i = 1; i < form1.size(); i++) {
						JSONObject obj = (JSONObject) JSONObject.toJSON(form1.get(i));
						sqlcs += " " + getValue(obj.get("value"));
				}
			}
			
			String sql = "select * from xwcs_gsdr_yssjjrk where 1=1";
		    sql+=sqlcs;
		    
		    if (!rk_rq.equals("")) {
				sql +=" and rk_rq='"+rk_rq+"' ";
				
			}
		    
		    if (!fz.equals("")) {
		    	
				if (fz.equals("sjje")) {
					sql = "select t."+fz+"," + 
							"SUM(t.zyj) zyj,SUM(t.ssj) ssj,SUM(t.dsj) dsj,SUM(t.xzj) xzj from xwcs_gsdr_yssjjrk t where 1=1  "+andjd+"     ";
					
					
				} else {
					sql = "select t."+fz+",sum(t.sjje) sjje," + 
							"SUM(t.zyj) zyj,SUM(t.ssj) ssj,SUM(t.dsj) dsj,SUM(t.xzj) xzj from xwcs_gsdr_yssjjrk t where 1=1  "+andjd+"   ";
				}
				
				
			    sql+=sqlcs;
			    if (!rk_rq.equals("")) {
					sql +=" and rk_rq='"+rk_rq+"' ";
					
				}
			    sql+=" group by t."+fz+" ";
			}
			
			List<Map<String, Object>> sjjgall = bs.query(sql,page,pagesize);
			int queryCount = bs.queryCount(sql);
			
			return this.toJson("000", "查询成功！", sjjgall, queryCount);
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return this.toJson("000", "系统异常!");
		}
		
		
	
	}
	
	
	
	
}
