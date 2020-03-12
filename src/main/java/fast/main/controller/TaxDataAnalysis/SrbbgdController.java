package fast.main.controller.TaxDataAnalysis;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.Super;

/**
 * 收入报表归档
 */
@Controller
@RequestMapping("wjdg")
public class SrbbgdController extends Super {

	private Map<String, Object> user = null;
	@Autowired
	BaseService bs;
	
	/**
	 * 进入收入报表归档跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		
		return "TaxDataAnalysis/Srbbgd";
		
	}
	
	/**
	 * 查询文档基本信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("crwjzs.do")
	@ResponseBody
	public String crwjzs(HttpServletRequest request, HttpServletResponse response){
		try{
			String dwid = request.getSession().getAttribute("dwid").toString();
			String page = getValue(request.getParameter("page"));
			String pagesize = getValue(request.getParameter("limit"));
			String WJID = getValue(request.getParameter("wjid"));
			String SCRQ = getValue(request.getParameter("scrq"));
			
			String sqlconut = "select * from FAST_SCWJGL WHERE scz = ?";
			sqlconut = getSql2(sqlconut, dwid);
			if(WJID!=null&&!"".equals(WJID)&&!"请选择".equals(WJID)){
				sqlconut=sqlconut+ "AND WJID ="+WJID;
			}
			if(SCRQ!=null&&!"".equals(SCRQ)&&!"请选择日期".equals(SCRQ)){
				sqlconut=sqlconut+ "AND to_char(SCRQ,'YYYYmm') = '"+SCRQ+"'";
			}
			sqlconut=sqlconut+"order by scrq desc";
			
			List<Map<String, Object>> sjjgall = bs.query(sqlconut,page,pagesize);
			
			List<Map<String, Object>> listcont = bs.query(sqlconut);
			
			
			for(int i=0;i<sjjgall.size();i++){
	            Map<String,Object> newMap = sjjgall.get(i);
	            String WJQC="";
	            for(Entry<String,Object> entry:newMap.entrySet()){
	            	
	            	if ("WJMC".equals(entry.getKey())) {
	            		WJQC=entry.getValue().toString();
	            		entry.setValue(entry.getValue().toString().substring(0,entry.getValue().toString().indexOf("_")));
					}
	            	
	            }
	            newMap.put("WJQC", WJQC);
	        }	
			
			return this.toJsonct("000", "查询成功！", sjjgall, Integer.toString(listcont.size()));
			
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return this.toJson("000", "系统异常!");
		}
	}
		
	/**
	 * 查询下拉选条件
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("selectList.do")
	@ResponseBody
	public String selectList(HttpServletRequest request, HttpServletResponse response){
		try{
			String dwid = request.getSession().getAttribute("dwid").toString();
			String sql = "select WJMC,WJID FROM  FAST_SCWJGL where scz=? group by WJMC,WJID";
			sql = getSql2(sql, dwid);
			List<Map<String,Object>> list = bs.query(sql);
			for(int i=0;i<list.size();i++){
	            Map<String,Object> newMap = list.get(i);
	            for(Entry<String,Object> entry:newMap.entrySet()){
	            	if ("WJMC".equals(entry.getKey())) {
	            		entry.setValue(entry.getValue().toString().substring(0,entry.getValue().toString().indexOf("_")));
					}      
	            }
	        }
			return this.toJson("000","查询成功", list);
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return this.toJson("000", "系统异常!");
		}
	}
	
	
	/**
	 * 删除归档文件
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("delwjdg.do")
	@ResponseBody
	public String delwjdg(HttpServletRequest request, HttpServletResponse response){
		try{
			String wjid = getValue(request.getParameter("WJID"));
			String updatesql = "delete from FAST_SCWJGL where wjid = ? ";
			updatesql = getSql2(updatesql, wjid);
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
