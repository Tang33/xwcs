package fast.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fast.main.util.Super;

public class JdSswcqkLrAction extends Super {
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String sql2="select JD_DM,JD_MC from  dm_jd jd where jd.jd_dm like '9%'";
			List<Map<String, Object>> jdlist=this.getBs().query(sql2);
			this.getRequest().setAttribute("jdlist", jdlist);
			String ssdw_dm=this.getRequest().getSession().getAttribute("dwid").toString();
			this.getRequest().setAttribute("dwid", ssdw_dm);
			return "xtgl/JdSswcqkLrAction";
		} catch (Exception e) {
			//e.printStackTrace();
			return "xtgl/JdSswcqkLrAction";
		}
	
	}
	
	
	//插入上传记录
	public String addBB(Map<String, Object> rmap) {
		try{
			initMap(rmap);
			String yf=getValue(this.getForm().get("yf"))+"01";
			String jd_dm=getValue(this.getForm().get("jd_dm"));
			String scbbmc=getValue(this.getForm().get("scbbmc"));
			String bbmc=getValue(this.getForm().get("bbmc"));
			String uno=this.getRequest().getSession().getAttribute("uno").toString();
			if("0".equals(jd_dm)) {
				return this.toJson("001", "插入失败！");
			} else {
				String querySQL = "select * FROM JD_SSWCQK where JD_DM = '"+jd_dm+"' and SSYF = to_date('"+yf+"','yyyymmdd')";
				System.out.println(querySQL);
				Integer count=this.getBs().queryCount(querySQL);
				if(count>0) {
					return this.toJson("002", "该报表已存在！");
				}else {
					String sql = "insert into JD_SSWCQK(JD_DM,SSYF,BBMC,SCBBMC,STATE,LRRY_DM,LR_SJ) values ('"+jd_dm+"',to_date('"+yf+"','yyyymmdd'),'"+bbmc+"','"+scbbmc+"','1','"+uno+"',sysdate)";
					System.out.println(sql);
					this.getBs().insert(sql);
					return this.toJson("000", "插入成功！");
				}	
			}	
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "插入失败！");
		}
	}
	
	// 查询
	public String doQuery(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String ssdw_dm=this.getRequest().getSession().getAttribute("dwid").toString();
			String yf = getValue(this.getForm().get("yf"));
			String pageNo=getValue(this.getForm().get("pageNo"));
			String pageSize=getValue(this.getForm().get("pageSize"));
			
			String sql = " select jd.jd_dm as JD_DM,jd.jd_mc as JD_MC,nvl(ss.state,'0') as STATE,"
					+ " to_char(ss.ssyf,'yyyy-mm-dd') as ssyf,   ss.bbmc as BBMC,ss.scbbmc as SCBBMC,"
					+ " to_char(ss.lr_sj,'yyyy-mm-dd') as SCSJ,"
					+ "    nvl((Select czy.czy_mc from xtgl_czy czy where czy.czy_dm=ss.lrry_dm),'') as LRRY_MC,"
					+ " 'newFileList['||jd.jd_dm||']' as FILENAME "
					+ "   from JD_SSWCQK ss,dm_jd jd"
					+ " where jd.jd_dm=ss.jd_dm(+)";
				if(ssdw_dm!=null&&!ssdw_dm.equals("00")){
					sql+=" and jd.jd_dm='"+ssdw_dm+"'";
				}
				sql+="     and jd.jd_dm like '9%'     and (-to_char(ss.ssyf,'yyyymm')=?-)"
					+ " union "
					+ " select jd.jd_dm as JD_DM,       jd.jd_mc as JD_MC,      '0' as STATE,      '' as ssyf,       '' as BBMC,       '' as SCBBMC,  "
					+ "    '' as SCSJ,      '' as LRRY_MC,       'newFileList[' || jd.jd_dm || ']' as FILENAME"
					+ "  from  dm_jd jd " 
					+ "  where jd.jd_dm like '9%' and (-jd.jd_dm  in ( select ss.jd_dm from JD_SSWCQK ss where to_char(ss.ssyf,'yyyymm')=?)-)" ;
			if(ssdw_dm!=null&&!ssdw_dm.equals("00")){
				sql+=" and jd_dm='"+ssdw_dm+"'";
			}
			sql+= " order by 1";
			
			
			sql = getSql2(sql, new Object[] {yf,yf});
			System.out.println(sql);
			List<Map<String, Object>> list = this.getBs().query(sql,pageNo,pageSize);
			int count = this.getBs().queryCount(sql);
			
			return this.toJson("000", "查询成功！", list,count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	//删除
	public String doDel(Map<String, Object> rmap){
		try{
			//初始化   bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String JD_DM=getValue(this.getForm().get("JD_DM"));
			String yf=getValue(this.getForm().get("SSYF"));
			yf=yf.replace("-", "");
			yf=yf.substring(0, 6);			
			String sql = " Delete from JD_SSWCQK where JD_DM='" + JD_DM
					+ "' and to_char(ssyf,'yyyymm')='" + yf + "' ";
			System.out.println(sql);
			Integer count=this.getBs().update(sql);
			if(count > 0 ) {
				return this.toJson("000", "删除成功！");
			} else {
				return this.toJson("009", "删除失败！");
			}
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "删除失败！");
		}
	}
	
	//新增
	public String doInput(Map<String, Object> rmap){
		try{
			//初始化   bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String JD_DM=getValue(this.getForm().get("upload_JD_DM"));
			String FILENAME=getValue(this.getForm().get("upload_name"));
			String yf=getValue(this.getForm().get("upload_YF"));
			String uno=this.getRequest().getSession().getAttribute("uno").toString();
			
			String sql = " Insert into JD_SSWCQK(JD_DM,SSYF,BBMC,SCBBMC,STATE,LRRY_DM,LR_SJ)"
					+ " values('"
					+ JD_DM
					+ "',to_date('"
					+ yf
					+ "01','yyyymmdd'),'"
					+ FILENAME
					+ "','"
					+ FILENAME
					+ "','1','" + uno + "',sysdate) ";
			Integer count=this.getBs().insert(sql);
			return this.toJson("000", "新增成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "新增失败！");
		}
	}
	
	//查询报表是否已存在
	public String doQueryCount(Map<String, Object> rmap){
		try{
			//初始化   bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String JD_DM=getValue(this.getForm().get("upload_JD_DM"));
			String yf=getValue(this.getForm().get("upload_YF"));
			
			String sql = "select *  from JD_SSWCQK ss,dm_jd jd where jd.jd_dm=ss.jd_dm(+)  and jd.jd_dm = '"
						+JD_DM
						+"'  and to_char(ss.ssyf,'yyyymm')='"+
						yf
						+"' ";
			
			Integer count=this.getBs().queryCount(sql);
			if(count>0) {
				return this.toJson("001", "该报表已存在！");
			}else {
				return this.toJson("000", "该报表不存在！");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "新增失败！");
		}
	}
	
	//覆盖
	public String dodeleteAndInput(Map<String, Object> rmap){
		try{
			//初始化   bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String JD_DM=getValue(this.getForm().get("upload_JD_DM"));
			String FILENAME=getValue(this.getForm().get("upload_name"));
			String yf=getValue(this.getForm().get("upload_YF"));
			String uno=this.getRequest().getSession().getAttribute("uno").toString();
			
			String sql = " Delete from JD_SSWCQK where JD_DM='" + JD_DM
					+ "' and to_char(ssyf,'yyyymm')='" + yf + "' ";
			Integer count=this.getBs().delete(sql);
			
			String sql3 = " Insert into JD_SSWCQK(JD_DM,SSYF,BBMC,SCBBMC,STATE,LRRY_DM,LR_SJ)"
					+ " values('"
					+ JD_DM
					+ "',to_date('"
					+ yf
					+ "01','yyyymmdd'),'"
					+ FILENAME
					+ "','"
					+ FILENAME
					+ "','1','" + uno + "',sysdate) ";
			Integer count3=this.getBs().insert(sql3);
			return this.toJson("000", "覆盖成功！");
			
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "覆盖失败！");
		}
	}
	
	
}
