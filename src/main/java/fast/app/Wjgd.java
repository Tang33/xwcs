package fast.app;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import fast.main.util.Super;

public class Wjgd extends Super {

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "sjfx/Wjgd";
		} catch (Exception e) {
			e.printStackTrace();
			return "sjfx/Wjgd";
		}
	}
	
	//删除
	public String del(Map<String, Object> rmap) {
		initMap(rmap);
		
		String wjid = getValue(this.getForm().get("WJID"));
		String updatesql = "delete from FAST_SCWJGL where wjid = '"+wjid+"' ";
		System.out.println(updatesql);
		Integer upnum = this.getBs().update(updatesql);
		if(upnum!=0) {
			return this.toJson("000", "删除成功！");
		}else {
			return this.toJson("001", "删除失败！");
		}	
	}
		
	//查询页面加载下拉框中的数据
	public String selectList(Map<String, Object> rmap) {
		// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
		try {
			initMap(rmap);
			String dwid = this.getRequest().getSession().getAttribute("dwid").toString();
			String sql = "select WJMC,WJID FROM  FAST_SCWJGL where scz='"+dwid+"' group by WJMC,WJID";
			List<Map<String,Object>> list = this.getBs().query(sql);
			for(int i=0;i<list.size();i++){
	            Map<String,Object> newMap = list.get(i);
	            for(Entry<String,Object> entry:newMap.entrySet()){
	            	if ("WJMC".equals(entry.getKey())) {
	            		entry.setValue(entry.getValue().toString().substring(0,entry.getValue().toString().indexOf("_")));
					}      
	            }
	        }
			return this.toJson("000","查询成功", list);
		}catch(Exception e) {
			return this.toJson("009", "查询异常");
		}
	}
	
	public String crwjzs(Map<String, Object> rmap) {
		
		initMap(rmap);
		String dwid = this.getRequest().getSession().getAttribute("dwid").toString();
		//获取limit和pagesize
		String page = getValue(this.getForm().get("page"));
		String pagesize = getValue(this.getForm().get("limit"));
		String WJID = getValue(this.getForm().get("wjid"));
		String SCRQ = getValue(this.getForm().get("scrq"));
		
		String sqlconut = "select * from FAST_SCWJGL WHERE scz = '"+dwid+"'";
		if(WJID!=null&&!"".equals(WJID)&&!"请选择".equals(WJID)){
			sqlconut=sqlconut+ "AND WJID ="+WJID;
		}
		if(SCRQ!=null&&!"".equals(SCRQ)&&!"请选择日期".equals(SCRQ)){
			sqlconut=sqlconut+ "AND to_char(SCRQ,'YYYYmm') = '"+SCRQ+"'";
		}
		sqlconut=sqlconut+"order by scrq desc";
		
		String sql = "select * from (select row_.*, rownum rowno from ("+sqlconut+") row_ where rownum <= " + pagesize + "*"
				+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize + " + 1";
		
		List<Map<String, Object>> sjjgall = this.getBs().query(sql);
		
		List<Map<String, Object>> listcont = this.getBs().query(sqlconut);
		
		
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
	}
	
	
}
