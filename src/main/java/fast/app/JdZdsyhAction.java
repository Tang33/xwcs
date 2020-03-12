package fast.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.Super;

public class JdZdsyhAction extends Super {
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
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
			List<Map<String, Object>> lblist = this.getBs().query(sql);
			
			sql=this.getSql("jd_query");
			List<Map<String, Object>> jdlist=this.getBs().query(sql);
			
			this.getRequest().setAttribute("jdlist", jdlist);
			this.getRequest().setAttribute("lblist", lblist);
			return "xtgl/ZdSyHAction";
		} catch (Exception e) {
			//e.printStackTrace();
			return "xtgl/ZdSyHAction";
		}
	
	}
	
	// 查询
	public String doQuery(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String ssdw_dm=this.getRequest().getSession().getAttribute("dwid").toString();
			String LB_DM=getValue(this.getForm().get("paramlb"));
			String pageNo=getValue(this.getForm().get("pageNo"));
			String pageSize=getValue(this.getForm().get("pageSize"));
			
			if("".equals(LB_DM) || LB_DM==null) {
				LB_DM="%";
			}
			
			String sql = "Select  t.NSRMC as NSRMC,t.JD_DM,t.LB_DM,"
					+ "(select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.lrry_dm) as LrRY_MC,"
					+ "to_char(t.LR_SJ,'yyyy-mm-dd') as LRRQ,"
					+ "(select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.xgry_dm) as XGRY_MC,"
					+ "to_char(t.XG_SJ,'yyyy-mm-dd') as XGRQ "
					+ " from JD_ZDSYH t where   t.jd_dm='" + ssdw_dm
					+ "' and t.lb_dm ='" + LB_DM + "' order by t.LB_DM";
			
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
			String NSRMC=getValue(this.getForm().get("NSRMC"));
			String sql = "delete from JD_ZDSYH where jd_dm='" + JD_DM
					+ "' and lb_dm='" + LB_DM + "' and nsrmc='" + NSRMC + "' ";
			this.getBs().update(sql);
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
			String LB_DM=getValue(this.getForm().get("paramlb"));
			
			if("".equals(LB_DM) || LB_DM==null){
				LB_DM="%";
			}
			String sql = "Select  t.NSRMC as NSRMC,"
					+ "(select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.lrry_dm) as LrRY_MC,"
					+ "to_char(t.LR_SJ,'yyyy-mm-dd') as LRRQ,"
					+ "(select czy.czy_mc from xtgl_czy czy where czy.czy_dm=t.xgry_dm) as XGRY_MC,"
					+ "to_char(t.XG_SJ,'yyyy-mm-dd') as XGRQ "
					+ " from JD_ZDSYH t where   t.jd_dm='" + ssdw_dm
					+ "' and t.lb_dm ='" + LB_DM + "' order by t.LB_DM";
			sql = getSql2(sql);
			List<Map<String, Object>> list = this.getBs().query(sql);
			
			Map<String, Object> map=new HashMap<String, Object>();
			String[] cols= {"纳税人名称","建立者","建立日期"};
			String[] keys= {"NSRMC","LRRY_MC","LRRQ"};
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
			String NSRMC=getValue(this.getForm().get("update_NSRMC"));
			String YNSRMC=getValue(this.getForm().get("update_YNSRMC"));
			String ssdw_dm=this.getRequest().getSession().getAttribute("dwid").toString();
			String LB_DM=getValue(this.getForm().get("update_LB_DM"));
			String uno=this.getRequest().getSession().getAttribute("uno").toString();
			
			String sql = " Update JD_ZDSYH  set NSRMC='" + NSRMC 
			+ "',JD_DM='" + ssdw_dm + "',LB_DM='" + LB_DM + "', XGRY_DM='"+ uno 
			+ "',XG_SJ=sysdate  where NSRMC='" + YNSRMC +"'";
			
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
			String NSRMC=getValue(this.getForm().get("add_NSRMC"));
			String ssdw_dm=this.getRequest().getSession().getAttribute("dwid").toString();
			String LB_DM=getValue(this.getForm().get("add_LB_DM"));
			String uno=this.getRequest().getSession().getAttribute("uno").toString();
			
			String sql = " Insert into JD_ZDSYH(NSRMC,JD_DM,LB_DM,LRRY_DM,LR_SJ) Values('"
					+ NSRMC
					+ "','"
					+ ssdw_dm
					+ "','"
					+ LB_DM
					+ "','"
					+ uno
					+ "',sysdate)";
			
			Integer count=this.getBs().insert(sql);
			return this.toJson("000", "查询成功！",count);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	//查询nsrsbh或者nsrmc是否已经存在
	public String doQueryCount(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String NSRMC=getValue(this.getForm().get("add_NSRMC"));
			
			String sql1="SELECT * FROM JD_ZDSYH WHERE nsrmc='"+NSRMC+"'";
			
			Integer  count1=this.getBs().queryCount(sql1);
			if(count1 >0){
				return this.toJson("001", "纳税人名称已存在！");
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
			String NSRMC=getValue(this.getForm().get("update_NSRMC"));
			String YNSRMC=getValue(this.getForm().get("update_YNSRMC"));
			if(NSRMC.equals(YNSRMC)) {
				return this.toJson("000", "没有修改纳税人名称！");
			}else {
				String sql2="SELECT * FROM JD_ZDSYH WHERE nsrmc='"+NSRMC+"'";
				
				Integer  count2=this.getBs().queryCount(sql2);
				if(count2 >0){
					return this.toJson("003", "纳税人名称已存在！");
				} else {
					return this.toJson("000", "没有重复！");
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	//
	public String doDeleteAndInsert(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String NSRMC=getValue(this.getForm().get("add_NSRMC"));
			String ssdw_dm=this.getRequest().getSession().getAttribute("dwid").toString();
			String LB_DM=getValue(this.getForm().get("add_LB_DM"));
			String uno=this.getRequest().getSession().getAttribute("uno").toString();
			
			String delSql="delete from JD_ZDSYH where jd_dm='" + ssdw_dm
					+ "' and nsrmc='" + NSRMC + "' ";
			
			Integer  count2=this.getBs().delete(delSql);
			
			String sql = " Insert into JD_ZDSYH(NSRMC,JD_DM,LB_DM,LRRY_DM,LR_SJ) Values('"
					+ NSRMC
					+ "','"
					+ ssdw_dm
					+ "','"
					+ LB_DM
					+ "','"
					+ uno
					+ "',sysdate)";
			
			Integer count=this.getBs().insert(sql);
			return this.toJson("000", "覆盖成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "操作异常！");
		}
	}
}
