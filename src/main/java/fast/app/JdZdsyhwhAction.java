package fast.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.Super;

public class JdZdsyhwhAction extends Super {
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String sql=this.getSql("jd_query");
			List<Map<String, Object>> jdlist=this.getBs().query(sql);
			this.getRequest().setAttribute("jdlist", jdlist);
			return "xtgl/ZdSyLbAction";
		} catch (Exception e) {
			//e.printStackTrace();
			return "xtgl/ZdSyLbAction";
		}
	
	}
	// 查询
	public String doQuery(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String ssdw_dm=this.getRequest().getSession().getAttribute("dwid").toString();
			
			String pageNo=getValue(this.getForm().get("pageNo"));
			String pageSize=getValue(this.getForm().get("pageSize"));
			String sql = "Select t.jd_dm as JD_DM,jd.jd_mc as JD_MC,t.lb_dm as LB_DM,t.lb_mc as LB_MC,"
					+ " (select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.lrry_dm) as LRRY_MC,"
					+ "to_char(t.LR_SJ,'yyyy-mm-dd') as LRRQ,"
					+ "(select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.xgry_dm) as XGRY_MC,"
					+ "to_char(t.XG_SJ,'yyyy-mm-dd') as XGRQ,"
					+ "to_char((select count(*) from JD_ZDSYH zdsyh where zdsyh.jd_dm=t.jd_dm and zdsyh.lb_dm=t.lb_dm)) as NSRSL"
					+ "   from JD_ZDSYLB t,dm_jd jd where t.jd_dm=jd.jd_dm and t.jd_dm='"
					+ ssdw_dm + "' order by t.LB_DM";
			sql = getSql2(sql);
			List<Map<String, Object>> list = this.getBs().query(sql,pageNo,pageSize);
			int count = this.getBs().queryCount(sql);
			return this.toJson("000", "查询成功！", list,count);
		} catch (Exception e) {
			//e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	//删除
	public String doDel(Map<String, Object> rmap){
		try{
			//初始化   bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String JD_DM=getValue(this.getForm().get("JD_DM"));
			String LB_DM=getValue(this.getForm().get("LB_DM"));
			String sql = "delete from JD_ZDSYLB where jd_dm='" + JD_DM
					+ "' and lb_dm='" + LB_DM + "' ";
			Integer count=this.getBs().update(sql);
			return this.toJson("000", "删除成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "删除失败！");
		}
	}

	//导出excel
	public Object export(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String ssdw_dm=this.getRequest().getSession().getAttribute("dwid").toString();
			
			String sql = "Select t.jd_dm as JD_DM,jd.jd_mc as JD_MC,t.lb_dm as LB_DM,t.lb_mc as LB_MC,"
					+ " (select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.lrry_dm) as LRRY_MC,"
					+ "to_char(t.LR_SJ,'yyyy-mm-dd') as LRRQ,"
					+ "(select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.xgry_dm) as XGRY_MC,"
					+ "to_char(t.XG_SJ,'yyyy-mm-dd') as XGRQ,"
					+ "to_char((select count(*) from JD_ZDSYH zdsyh where zdsyh.jd_dm=t.jd_dm and zdsyh.lb_dm=t.lb_dm)) as NSRSL"
					+ "   from JD_ZDSYLB t,dm_jd jd where t.jd_dm=jd.jd_dm and t.jd_dm='"
					+ ssdw_dm + "' order by t.LB_DM";
			sql = getSql2(sql);
			List<Map<String, Object>> list = this.getBs().query(sql);
			
			Map<String, Object> map=new HashMap<String, Object>();
			String[] cols= {"街道代码","重点税源代码","重点税源名称","建立者","建立日期","修改者","修改日期","纳税人数量"};
			String[] keys= {"JD_DM","LB_DM","LB_MC","LRRY_MC","LRRQ","XGRY_MC","XGRQ","NSRSL"};
			map.put("fileName", "导出excel.xlsx");
			map.put("cols", cols);
			map.put("keys", keys);
			map.put("list", list);
			return map;
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	//修改
	public String doUpdate(Map<String, Object> rmap){
		try{
			//初始化   bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String LB_DM=getValue(this.getForm().get("update_LB_DM"));
			String LB_MC=getValue(this.getForm().get("update_LB_MC"));
			String JD_DM=getValue(this.getForm().get("update_JD_DM")).toUpperCase();
			String id=this.getRequest().getSession().getAttribute("id").toString();
			
			String sql = " update JD_ZDSYLB t set t.lb_mc='" + LB_MC
					+ "',XGRY_DM='" + id + "',XG_SJ=sysdate "
					+ " where t.jd_dm='" + JD_DM + "' and t.lb_dm='" + LB_DM
					+ "' ";
			
			Integer count=this.getBs().update(sql);
			return this.toJson("000", "修改成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "修改失败！");
		}
	}
	
	//新增
	public String doAdd(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String LB_DM=getValue(this.getForm().get("add_LB_DM"));
			String LB_MC=getValue(this.getForm().get("add_LB_MC"));
			String id=this.getRequest().getSession().getAttribute("id").toString();
			String ssdw_dm=this.getRequest().getSession().getAttribute("dwid").toString();
			if(id!=null && !"".equals(id)) {
				String sql = "Insert into  JD_ZDSYLB(jd_dm,lb_dm,lb_mc,LRRY_DM,LR_SJ) values('"
						+ ssdw_dm
						+ "','"
						+ LB_DM
						+ "','"
						+ LB_MC
						+ "','"
						+ id
						+ "',sysdate)";
				
				Integer count=this.getBs().insert(sql);
				return this.toJson("000", "查询成功！",count);
			}else {
				return this.toJson("009", "查询失败,请重新登陆！");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("006", "查询异常！");
		}
	}
	
	//查询nsrmc是否已经存在
	public String doQueryCount(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String ssdw_dm=this.getRequest().getSession().getAttribute("dwid").toString();
			String LB_MC=getValue(this.getForm().get("add_LB_MC"));
			String LB_DM=getValue(this.getForm().get("add_LB_DM"));
			
			String sql = "Select t.jd_dm as JD_DM,jd.jd_mc as JD_MC,t.lb_dm as LB_DM,t.lb_mc as LB_MC,"
					+ " (select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.lrry_dm) as LRRY_MC,"
					+ "to_char(t.LR_SJ,'yyyy-mm-dd') as LRRQ,"
					+ "(select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.xgry_dm) as XGRY_MC,"
					+ "to_char(t.XG_SJ,'yyyy-mm-dd') as XGRQ,"
					+ "to_char((select count(*) from JD_ZDSYH zdsyh where zdsyh.jd_dm=t.jd_dm and zdsyh.lb_dm=t.lb_dm)) as NSRSL"
					+ "   from JD_ZDSYLB t,dm_jd jd where t.jd_dm=jd.jd_dm and t.jd_dm='"
					+ ssdw_dm + "' and t.lb_mc='"+LB_MC+"'";
			
			
			Integer  count=this.getBs().queryCount(sql);
			
			String sql2 = "Select t.jd_dm as JD_DM,jd.jd_mc as JD_MC,t.lb_dm as LB_DM,t.lb_mc as LB_MC,"
					+ " (select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.lrry_dm) as LRRY_MC,"
					+ "to_char(t.LR_SJ,'yyyy-mm-dd') as LRRQ,"
					+ "(select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.xgry_dm) as XGRY_MC,"
					+ "to_char(t.XG_SJ,'yyyy-mm-dd') as XGRQ,"
					+ "to_char((select count(*) from JD_ZDSYH zdsyh where zdsyh.jd_dm=t.jd_dm and zdsyh.lb_dm=t.lb_dm)) as NSRSL"
					+ "   from JD_ZDSYLB t,dm_jd jd where t.jd_dm=jd.jd_dm and t.jd_dm='"
					+ ssdw_dm + "' and t.lb_dm='"+LB_DM+"'";
			
			
			Integer  count2=this.getBs().queryCount(sql2);
			
			
			if(count >0 && count2>0){
				return this.toJson("001", "重点税源名称及代码已存在！");
			} else if(count2>0){
				return this.toJson("002", "重点税源代码已存在！");
			} else if(count >0){
				return this.toJson("003", "重点税源名称已存在！");
			} else {
				return this.toJson("000", "没有重复！");
			}
				
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	//查询nsrmc是否已经存在
	public String doQueryCount2(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String ssdw_dm=this.getRequest().getSession().getAttribute("dwid").toString();
			String LB_MC=getValue(this.getForm().get("update_LB_MC"));
			String YLB_MC=getValue(this.getForm().get("update_LB_MC_Y"));
			
			if(LB_MC.equals(YLB_MC)) {
				return this.toJson("000", "没有修改纳税人名称！");
			}else {
				
				String sql = "Select t.jd_dm as JD_DM,jd.jd_mc as JD_MC,t.lb_dm as LB_DM,t.lb_mc as LB_MC,"
						+ " (select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.lrry_dm) as LRRY_MC,"
						+ "to_char(t.LR_SJ,'yyyy-mm-dd') as LRRQ,"
						+ "(select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.xgry_dm) as XGRY_MC,"
						+ "to_char(t.XG_SJ,'yyyy-mm-dd') as XGRQ,"
						+ "to_char((select count(*) from JD_ZDSYH zdsyh where zdsyh.jd_dm=t.jd_dm and zdsyh.lb_dm=t.lb_dm)) as NSRSL"
						+ "   from JD_ZDSYLB t,dm_jd jd where t.jd_dm=jd.jd_dm and t.jd_dm='"
						+ ssdw_dm + "' and t.lb_mc='"+LB_MC+"'";
				
				Integer  count=this.getBs().queryCount(sql);
				if(count >0){
					return this.toJson("003", "重点税源名称已存在！");
				} else {
					return this.toJson("000", "没有重复！");
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
}
