package fast.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.Super;

public class GsDqyAction extends Super {
	
	// 国税大企业
//	private String type = "1";

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			//获取用户信息
			this.getRequest().getSession().getAttribute("userinfo");
			String sql=this.getSql("jd_query");
			List<Map<String, Object>> jdlist=this.getBs().query(sql);
			sql=this.getSql("hy_query");
			List<Map<String, Object>> hylist=this.getBs().query(sql);
			this.getRequest().setAttribute("jdlist", jdlist);
			this.getRequest().setAttribute("hylist", hylist);
			return "xtgl/GsDqyAction";
		} catch (Exception e) {
			//e.printStackTrace();
			return "xtgl/GsDqyAction";
		}
	
	}
	
	// 查询
	public String doQuery(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String paramNsrmc = getValue(this.getForm().get("paramNsrmc"));
			String paramJd = getValue(this.getForm().get("paramJd"));
			String pageNo=getValue(this.getForm().get("pageNo"));
			String pageSize=getValue(this.getForm().get("pageSize"));
			String sql = " select  ' ' as  SWGLM,t.nsrsbh as NSRSBH,t.nsrmc as NSRMC,t.jydz as SCJYDZ ,t.jd_dm as JD_DM,jd.jd_mc as JD_MC,t.LY as LY,"
					+ "t.HY_DM as HY_DM,hy.HYML_MC as HYML_MC,to_char (nvl(XG_SJ, LR_SJ), 'yyyy-mm-dd hh24:mi' ) as RQ  from DJ_GS_DQYQC t,DM_JD jd,DM_HYML hy "
					+ " where t.jd_dm=jd.jd_dm(+) and t.HY_DM=hy.HYML_DM(+) and t.nsrmc like '%"+paramNsrmc+"%' and t.jd_dm like ? order by t.nsrmc";
			sql = getSql2(sql,new Object[] {paramJd});
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
			String key=getValue(this.getForm().get("NSRMC")).toUpperCase();
			String sql=" Delete from DJ_GS_DQYQC where NSRMC='" + key + "' ";
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
			String paramNsrmc = getValue(this.getForm().get("paramNsrmc"));
			String paramJd = getValue(this.getForm().get("paramJd"));
			if("".equals(paramJd) || paramJd ==null) {
				paramJd="%";
			}
			String sql = " select  ' ' as  SWGLM,t.nsrsbh as NSRSBH,t.nsrmc as NSRMC,t.jydz as SCJYDZ ,t.jd_dm as JD_DM,jd.jd_mc as JD_MC,t.LY as LY,"
					+ "t.HY_DM as HY_DM,hy.HYML_MC as HYML_MC,to_char (nvl(XG_SJ, LR_SJ), 'yyyy-mm-dd hh24:mi' ) as RQ  from DJ_GS_DQYQC t,DM_JD jd,DM_HYML hy "
					+ " where t.jd_dm=jd.jd_dm(+) and t.HY_DM=hy.HYML_DM(+) and t.nsrmc like '%"+paramNsrmc+"%' and t.jd_dm like ? order by t.nsrmc";
			sql = getSql2(sql,new Object[] {paramJd});
			List<Map<String, Object>> list = this.getBs().query(sql);
			
			Map<String, Object> map=new HashMap<String, Object>();
			String[] cols= {"纳税人识别码","纳税人名称","生产经营地址","所属街道","所属行业","最后变更日期"};
			String[] keys= {"NSRSBH","NSRMC","SCJYDZ","JD_MC","HYML_MC","RQ"};
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
			String NSRSBH=getValue(this.getForm().get("update_NSRSBH"));
			String NSRMC=getValue(this.getForm().get("update_NSRMC"));
			String YNSRMC=getValue(this.getForm().get("update_YNSRMC"));
			String SCJYDZ=getValue(this.getForm().get("update_SCJYDZ")).toUpperCase();
			String JD_DM=getValue(this.getForm().get("update_JD_MC")).toUpperCase();
			String HY_DM=getValue(this.getForm().get("update_HYML_MC")).toUpperCase();
			String id=this.getRequest().getSession().getAttribute("id").toString();
			
			String sql = " Update DJ_GS_DQYQC set NSRMC='" + NSRMC
			+ "',JD_DM='" + JD_DM + "',JYDZ='" + SCJYDZ
			+ "',HY_DM='" + HY_DM + "', XGRY_DM='"
			+ id + "',XG_SJ=sysdate  where NSRMC='"
			+ YNSRMC + "' ";
			
			this.getBs().update(sql);
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
			String NSRSBH=getValue(this.getForm().get("add_NSRSBH"));
			String NSRMC=getValue(this.getForm().get("add_NSRMC"));
			String SCJYDZ=getValue(this.getForm().get("add_SCJYDZ")).toUpperCase();
			String JD_DM=getValue(this.getForm().get("add_JD_DM")).toUpperCase();
			String HY_DM=getValue(this.getForm().get("add_HYML_DM")).toUpperCase();
			String id=this.getRequest().getSession().getAttribute("id").toString();
			
			String sql = " Insert into DJ_GS_DQYQC(NSRSBH,NSRMC,JD_DM,JYDZ,HY_DM,LY,LRRY_DM,LR_SJ) Values('"
					+ NSRSBH
					+ "','"
					+ NSRMC
					+ "','"
					+ JD_DM
					+ "','"
					+ SCJYDZ
					+ "','"
					+ HY_DM + "','人工','" + id + "',sysdate)";
			
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
			String NSRSBH=getValue(this.getForm().get("add_NSRSBH")).toUpperCase();
			
			String sql1="SELECT * FROM DJ_GS_DQYQC WHERE nsrsbh='"+NSRSBH+"'";
			String sql2="SELECT * FROM DJ_GS_DQYQC WHERE nsrmc='"+NSRMC+"'";
			Integer  count1=this.getBs().queryCount(sql1);
			
			Integer  count2=this.getBs().queryCount(sql2);
			if(count1>0 && count2>0) {
				return this.toJson("001", "纳税人识别号以及纳税人名称已存在！");
			} else if(count1 >0){
				return this.toJson("002", "纳税人识别号已存在！");
			} else if(count2 >0){
				return this.toJson("003", "纳税人名称已存在！");
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
				String sql2="SELECT * FROM DJ_GS_DQYQC WHERE nsrmc='"+NSRMC+"'";
				
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
			String NSRSBH=getValue(this.getForm().get("add_NSRSBH"));
			String NSRMC=getValue(this.getForm().get("add_NSRMC"));
			String SCJYDZ=getValue(this.getForm().get("add_SCJYDZ")).toUpperCase();
			String JD_DM=getValue(this.getForm().get("add_JD_DM")).toUpperCase();
			String HY_DM=getValue(this.getForm().get("add_HYML_DM")).toUpperCase();
			String id=this.getRequest().getSession().getAttribute("id").toString();
			
			String delSql1="Delete from DJ_GS_DQYQC WHERE NSRSBH='" + NSRSBH + "' ";
			String delSql2="Delete from DJ_GS_DQYQC WHERE NSRMC='"+NSRMC+"'";
			Integer  count1=this.getBs().delete(delSql1);
			Integer  count2=this.getBs().delete(delSql2);
			
			
			
			String sql = " Insert into DJ_GS_DQYQC(NSRSBH,NSRMC,JD_DM,JYDZ,HY_DM,LY,LRRY_DM,LR_SJ) Values('"
					+ NSRSBH
					+ "','"
					+ NSRMC
					+ "','"
					+ JD_DM
					+ "','"
					+ SCJYDZ
					+ "','"
					+ HY_DM + "','人工','" + id + "',sysdate)";
			Integer count=this.getBs().insert(sql);
					
			return this.toJson("000", "覆盖成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "操作异常！");
		}
	}
	
}
