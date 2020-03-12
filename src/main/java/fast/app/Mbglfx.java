package fast.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbcp.BasicDataSource;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;

import fast.main.util.EasyExcelUtil;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Super;

public class Mbglfx extends Super{
	
	private static Connection connection = null;

	private Map<String, Object> user = null;
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");

			return "sjfx/Mbglfx";
		} catch (Exception e) {
			e.printStackTrace();
			return "sjfx/Mbglfx";
		}
	}
	
	
	public String querymb(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String jd_dm = getValue(this.getRequest().getSession().getAttribute("dwid"));
			String pageNo = getValue(this.getForm().get("page"));
			String pageSize = getValue(this.getForm().get("limit"));
			
			String mbmc = getValue(this.getForm().get("mbmc"));
			String rq = getValue(this.getForm().get("rq"));
			
			String sql="select mbmc,drsj,czy,to_char(mbms) as mbms,zt,u_id id,mblx,wjlj from FAST_ZDYXX where 1=1 and jd_dm="+jd_dm;
			
			if(!mbmc.isEmpty()){
				
				sql+=" and mbmc like '%"+mbmc+"%'";
			}
			if(!rq.isEmpty()){
				
				sql+=" and to_char(drsj, 'yyyy-mm-dd')='"+rq+"'";
			}
			
			sql = sql+"order by drsj desc";
			
			String sqlfy="select * from (select row_.*, rownum rowno from (" + sql + ") row_ where rownum <= " + pageSize + "*"
				    + pageNo + ") a where a.rowno >= (" + pageNo + "- 1) * " + pageSize + " + 1";
			
			
			List<Map<String, Object>> listfy = this.getBs().query(sqlfy);
			List<Map<String, Object>> listall = this.getBs().query(sql);
			
			return this.toJson("000", "查询成功！", listfy, listall.size());
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！", new ArrayList<Map<String, Object>>(), 0);
		}
	}
	
	
		//将查询的结果进行修改
		public String modify(Map<String, Object> rmap) {
			initMap(rmap);
			
			String status = getValue(this.getForm().get("status"));
			String id = getValue(this.getForm().get("id"));
			
			
			String updatesql = " update FAST_ZDYXX set zt='"+status+"' where u_id ='"+id+"'";

			Integer upnum = this.getBs().update(updatesql);
			if(upnum!=0) {
				
				return this.toJson("000", "修改成功！");
			}else {
				
				return this.toJson("000", "修改失败！");
			}
			
		}
		
		
		
		public String delmb(Map<String, Object> rmap) {
			initMap(rmap);
			
			String id = getValue(this.getForm().get("ID"));
			String mblx = getValue(this.getForm().get("MBLX"));
			String updatesql = "delete from FAST_ZDYXX where u_id = '"+id+"' ";
			Integer upnum = this.getBs().update(updatesql);
			if(mblx.equals("定制企业模板")){
				String updatesql_dzxx = "delete from FAST_ZDYDR where u_id = '"+id+"' ";
				this.getBs().update(updatesql_dzxx);				
			}else if(mblx.equals("定制查询模板")){
				String updatesql_cxx = "delete from FAST_DZCXMB where u_id = '"+id+"' ";
				this.getBs().update(updatesql_cxx);
			}
			if(upnum!=0) {
				return this.toJson("000", "删除成功！");
			}else {
				return this.toJson("001", "删除失败！");
			}
			
			
		}
		
		
		/**
		 * 查询定制查询模板查询项信息
		 * */
		public String selectCXX(Map<String, Object> rmap){
			try{
				initMap(rmap);
				String id = getValue(this.getForm().get("id"));
				String sql = "select xh,cxxmc,gs from fast_dzcxmb where u_id='"+id+"'";
				List<Map<String, Object>> list_cxx = this.getBs().query(sql);
				return this.toJson("000", "查询成功！", list_cxx);
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败！");
			}
			
		}
		
		/**
		 * 定制查询模板修改
		 * */
		public String updateCXMB(Map<String, Object> rmap){
			Connection conn = null;
			try{
				initMap(rmap);
				String uid = getValue(this.getForm().get("id"));
				String mbmc = getValue(this.getForm().get("mbmc"));
				conn = Mbdy.getConnection();
				conn.setAutoCommit(false);
				String updatesql_cxx = "delete from FAST_DZCXMB where u_id = '"+uid+"' ";
				PreparedStatement stmt_delcxx = conn.prepareStatement(updatesql_cxx);
				stmt_delcxx.execute();
				String mbms = getValue(this.getForm().get("mbms"));
				String czy = this.getRequest().getSession().getAttribute("uname").toString();
				String updatesql_cxmb = "update FAST_ZDYXX set mbmc=?,mbms=?,czy=?,drsj=? where u_id=?";
				PreparedStatement stmt_cxmb = conn.prepareStatement(updatesql_cxmb);
				stmt_cxmb.setString(1, mbmc);
				StringReader reader = new StringReader(mbms);  
				stmt_cxmb.setCharacterStream(2, reader, mbms.length());
				stmt_cxmb.setString(3, czy);
				stmt_cxmb.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
				stmt_cxmb.setString(5, uid);
				stmt_cxmb.execute();
				String data = getValue(this.getForm().get("data"));
				com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONArray.parseArray(data);
				for(int i=0;i<jsonArray.size();i++){
					JSONObject json = jsonArray.getJSONObject(i);
					String xh = json.getString("xh");
					String cxxmc = json.getString("cxxmc");
					String gs = json.getString("gs");
					if(gs==null || gs.equals("undefined")){
						gs="";
						String nf="";
						String yfq="";
						String yfz="";
						String sz="";
						String kj="";
						Matcher mtc_nf = Pattern.compile("^(20[0-9]{2})年").matcher(cxxmc);
						if(mtc_nf.find()){
							nf = mtc_nf.group(1);
						}
						Matcher mtc_yfqz = Pattern.compile("(([0-9]{2})\\-)?([0-9]{2})月").matcher(cxxmc);
						if(mtc_yfqz.find()){
							if(mtc_yfqz.group(2) == null){
								yfq=mtc_yfqz.group(3);
							}else{
								yfq=mtc_yfqz.group(2);
								yfz=mtc_yfqz.group(3);
							}
						}
						Matcher mtc_sz = Pattern.compile("增值税|营业税|个人所得税|房产税|印花税|车船税|企业所得税|营改增增值税|城市维护建设税|地方教育附加|教育附加|城镇土地使用税").matcher(cxxmc);
						while(mtc_sz.find()){
							sz=sz.equals("")?sz+mtc_sz.group(0):sz+","+mtc_sz.group(0);
						}
						kj=cxxmc.contains("全口径")?"全口径":"地方口径";
						String sql_cxx="insert into fast_dzcxmb (u_id,xh,cxxmc,nf,yfq,yfz,sz,kj,gs,sfwgs) values(?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement stmt_cxx = conn.prepareStatement(sql_cxx);
						stmt_cxx.setString(1, uid);
						stmt_cxx.setString(2, xh);
						stmt_cxx.setString(3, cxxmc);
						stmt_cxx.setString(4, nf);
						stmt_cxx.setString(5, yfq);
						stmt_cxx.setString(6, yfz);
						stmt_cxx.setString(7, sz);
						stmt_cxx.setString(8, kj);
						stmt_cxx.setString(9, gs);
						stmt_cxx.setString(10, "N");
						stmt_cxx.execute();
					}else{
						String sql_gs="insert into fast_dzcxmb (u_id,xh,cxxmc,nf,yfq,yfz,sz,kj,gs,sfwgs) values(?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement stmt_gs = conn.prepareStatement(sql_gs);
						stmt_gs.setString(1, uid);
						stmt_gs.setString(2, xh);
						stmt_gs.setString(3, cxxmc);
						stmt_gs.setString(4, "");
						stmt_gs.setString(5, "");
						stmt_gs.setString(6, "");
						stmt_gs.setString(7, "");
						stmt_gs.setString(8, "");
						stmt_gs.setString(9, gs);
						stmt_gs.setString(10, "Y");
						stmt_gs.execute();
					}
				}
				conn.commit();
				return this.toJson("000", "模板修改成功！");
			}catch (Exception e) {
				e.printStackTrace();
				try {
					if(conn!=null)
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				return this.toJson("009", "模板修改失败！");
			}finally{
				try {
					if(conn != null)
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * 根据模板U_ID返回模板名称与模板描述
		 * */
		public String selectMCMS(Map<String, Object> rmap){
			try{
				initMap(rmap);
				String id = getValue(this.getForm().get("id"));
				String sql = "select mbmc,to_char(mbms) as mbms from fast_zdyxx where u_id='"+id+"'";
				Map<String, Object> map_mcms = this.getBs().queryOne(sql);
				return this.toJson("000", "查询成功！", map_mcms);
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败！");
			}
			
		}
		
		
		
		
}
