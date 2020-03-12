package fast.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.Super;

public class ZjkcAction extends Super {
	// 紫金科创
//	private String type = "10";
	
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/ZjkcAction";
		} catch (Exception e) {
			//e.printStackTrace();
			return "xtgl/ZjkcAction";
		}
	
	}
	// 查询
	public String doQuery(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String paramNsrmc = getValue(this.getForm().get("paramNsrmc"));
			String pageNo=getValue(this.getForm().get("pageNo"));
			String pageSize=getValue(this.getForm().get("pageSize"));
			String sql = " Select  NSRSBH as NSRSBH,NSRMC as NSRMC,JYDZ as SCJYDZ,LY as LY,'徐庄' as JD_MC,"
					+ "to_char (nvl(XG_SJ, LR_SJ), 'yyyy-mm-dd hh24:mi' ) as RQ    from DJ_XZ t where t.NSRMC like '%"
					+ paramNsrmc + "%' and SFZJKC='1' order by t.nsrmc ";
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
			String key=getValue(this.getForm().get("NSRMC"));
			String sql = "Delete from DJ_XZ where trim(NSRMC)='" + key + "' and sfzjkc='1'";
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
			String paramNsrmc = getValue(this.getForm().get("paramNsrmc"));
			String sql = " Select  NSRSBH as NSRSBH,NSRMC as NSRMC,JYDZ as SCJYDZ,LY as LY,'徐庄' as JD_MC,"
					+ "to_char (nvl(XG_SJ, LR_SJ), 'yyyy-mm-dd hh24:mi' ) as RQ    from DJ_XZ t where t.NSRMC like '%"
					+ paramNsrmc + "%' and SFZJKC='1' order by t.nsrmc ";
			sql = getSql2(sql);
			List<Map<String, Object>> list = this.getBs().query(sql);
			
			
			Map<String, Object> map=new HashMap<String, Object>();
			String[] cols= {"纳税人识别码","纳税人名称","生产经营地址","最后变更日期"};
			String[] keys= {"NSRSBH","NSRMC","SCJYDZ","RQ"};
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
			String id=this.getRequest().getSession().getAttribute("id").toString();
			
			String sql = " Update DJ_XZ  set NSRMC='" + NSRMC + "' ,NSRSBH='" + NSRSBH
			+ "',JYDZ='" + SCJYDZ + "',XGRY_DM='" + id
			+ "',XG_SJ=sysdate  where NSRMC='" + YNSRMC + "' and sfzjkc='1'";
			
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
			String NSRSBH=getValue(this.getForm().get("add_NSRSBH"));
			String SCJYDZ=getValue(this.getForm().get("add_SCJYDZ")).toUpperCase();
			String id=this.getRequest().getSession().getAttribute("id").toString();
			
			String sql = " Insert into DJ_XZ(NSRSBH,NSRMC,JYDZ,LY,LRRY_DM,LR_SJ,SFZJKC) Values('"
					+ NSRSBH
					+ "','"
					+ NSRMC
					+ "','"
					+ SCJYDZ
					+ "','人工','"
					+ id + "',sysdate,'1')";
			
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
			
			String sql1="SELECT * FROM DJ_XZ WHERE nsrsbh='"+NSRSBH+"' and sfzjkc='1'";
			String sql2="SELECT * FROM DJ_XZ WHERE nsrmc='"+NSRMC+"' and sfzjkc='1'";
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
				String sql2="SELECT * FROM DJ_XZ WHERE nsrmc='"+NSRMC+"'  and sfzjkc='1' ";
				
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
			String NSRSBH=getValue(this.getForm().get("add_NSRSBH"));
			String SCJYDZ=getValue(this.getForm().get("add_SCJYDZ")).toUpperCase();
			String id=this.getRequest().getSession().getAttribute("id").toString();
			
			String delSql1="Delete from DJ_XZ WHERE NSRSBH='" + NSRSBH + "'  and sfzjkc='1'";
			String delSql2="Delete from DJ_XZ WHERE NSRMC='"+NSRMC+"' and sfzjkc='1'";
			Integer  count1=this.getBs().delete(delSql1);
			Integer  count2=this.getBs().delete(delSql2);
			
			
			
			
			String sql = " Insert into DJ_XZ(NSRSBH,NSRMC,JYDZ,LY,LRRY_DM,LR_SJ,SFZJKC) Values('"
					+ NSRSBH
					+ "','"
					+ NSRMC
					+ "','"
					+ SCJYDZ
					+ "','人工','"
					+ id + "',sysdate,'1')";
			
			Integer count=this.getBs().insert(sql);
			return this.toJson("000", "覆盖成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "操作异常！");
		}
	}
}
