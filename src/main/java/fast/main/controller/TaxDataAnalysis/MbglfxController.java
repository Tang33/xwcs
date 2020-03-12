package fast.main.controller.TaxDataAnalysis;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import fast.app.Mbdy;
import fast.main.service.BaseService;
import fast.main.util.Super;

/**
 * 模板管理
 *
 */
@Controller
@RequestMapping("mbgl")
public class MbglfxController extends Super {
	
	@Autowired
	BaseService bs;
	
	private Map<String, Object> user = null;
	
	/**
	 * 进入模板管理跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		
		return "TaxDataAnalysis/Mbglfx";
		
	}
	
	/**
	 * 获取登陆用户的街道信息
	 * @param request
	 * @param response
	 * @return
	 */
	public String findJd(HttpServletRequest request, HttpServletResponse response) {
		String andJd = "";
		try {
			user = (Map<String, Object>)request.getSession().getAttribute("user");
			Integer jd = Integer.parseInt((String)user.get("DWID"));
			System.out.println(jd);
			
			if (user == null) {
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
			if (jd != null && jd == 00) {
				andJd = "and 1 = 1";
			}else {
				andJd = "and jd_dm = "+jd;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return andJd;
	}
	
	/**
	 * 查询模板信息
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/querymb.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String querymb(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			String jd = this.findJd(request, response);
			String jd_dm = getValue(request.getSession().getAttribute("dwid"));
			String pageNo = getValue(rmap.get("page"));
			String pageSize = getValue(rmap.get("limit"));
			String mbmc = getValue(rmap.get("mbmc"));			//模板名称
			String rq = getValue(rmap.get("rq"));				//日期
	
			String sql = "select mbmc,drsj,czy,to_char(mbms) as mbms,zt,u_id id,mblx,wjlj from FAST_ZDYXX where 1=1 and (-jd_dm=?-) and (-mbmc like '%'||?||'%'-) and (-to_char(drsj, 'yyyy-mm-dd')=?-) "+jd+" order by drsj desc";
			sql = this.getSql2(sql, new Object[] {jd_dm, mbmc, rq});
			System.out.println(sql);
			int count = bs.queryCount(sql);
			List<Map<String, Object>> list = bs.query(sql, pageNo, pageSize);
			return this.toJson("000", "查询成功！", list, count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	
		/**
		 * 将查询的结果进行修改
		 * @param request
		 * @param response
		 * @param rmap
		 * @return
		 */
		@RequestMapping(value="/modify.do",produces = "text/plain;charset=utf-8")
		@ResponseBody
		public String modify(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
			
			String status = getValue(rmap.get("status"));			//状态
			String id = getValue(rmap.get("id"));			//模板序号
			
			String updatesql = " update FAST_ZDYXX set zt='"+status+"' where u_id ='"+id+"'";

			Integer upnum = bs.update(updatesql);
			if(upnum!=0) {
				
				return this.toJson("000", "修改成功！");
			}else {
				
				return this.toJson("000", "修改失败！");
			}
			
		}
		
		/**
		 * 删除模板
		 * @param request
		 * @param response
		 * @param rmap
		 * @return
		 */
		@RequestMapping(value="/delmb.do",produces = "text/plain;charset=utf-8")
		@ResponseBody
		public String delmb(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
			
			String id = getValue(rmap.get("ID"));			//模板序号
			String mblx = getValue(rmap.get("MBLX"));			//模板类型
			String updatesql = "delete from FAST_ZDYXX where u_id = '"+id+"' ";
			Integer upnum = bs.update(updatesql);
			if(mblx.equals("定制企业模板")){
				String updatesql_dzxx = "delete from FAST_ZDYDR where u_id = '"+id+"' ";
				bs.update(updatesql_dzxx);				
			}else if(mblx.equals("定制查询模板")){
				String updatesql_cxx = "delete from FAST_DZCXMB where u_id = '"+id+"' ";
				bs.update(updatesql_cxx);
			}
			if(upnum!=0) {
				return this.toJson("000", "删除成功！");
			}else {
				return this.toJson("001", "删除失败！");
			}
			
			
		}
		
		
		/**
		 * 查询定制查询模板查询项信息
		 * @param request
		 * @param response
		 * @param rmap
		 * @return
		 */
		@RequestMapping(value="/selectCXX.do",produces = "text/plain;charset=utf-8")
		@ResponseBody
		public String selectCXX(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap){
			try{
				String jd = this.findJd(request, response);
				String id = getValue(rmap.get("id"));
				String sql = "select xh,cxxmc,gs from fast_dzcxmb where u_id='"+id+"'"+jd;
				List<Map<String, Object>> list_cxx = bs.query(sql);
				System.out.println(list_cxx);
				return this.toJson("000", "查询成功！", list_cxx);
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败！");
			}
			
		}
		
		/**
		 * 定制查询模板修改
		 * @param request
		 * @param response
		 * @param rmap
		 * @return
		 */
		@RequestMapping(value="/updateCXMB.do",produces = "text/plain;charset=utf-8")
		@ResponseBody
		public String updateCXMB(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap){
			Connection conn = null;
			try{

				String uid = getValue(rmap.get("id"));
				String mbmc = getValue(rmap.get("mbmc"));			//模板名称
				conn = Mbdy.getConnection();
				conn.setAutoCommit(false);
				String updatesql_cxx = "delete from FAST_DZCXMB where u_id = '"+uid+"' ";
				PreparedStatement stmt_delcxx = conn.prepareStatement(updatesql_cxx);
				stmt_delcxx.execute();
				String mbms = getValue(rmap.get("mbms"));		//模版描述
				String czy = request.getSession().getAttribute("uname").toString();			
				String updatesql_cxmb = "update FAST_ZDYXX set mbmc=?,mbms=?,czy=?,drsj=? where u_id=?";
				PreparedStatement stmt_cxmb = conn.prepareStatement(updatesql_cxmb);
				stmt_cxmb.setString(1, mbmc);
				StringReader reader = new StringReader(mbms);  
				stmt_cxmb.setCharacterStream(2, reader, mbms.length());
				stmt_cxmb.setString(3, czy);
				stmt_cxmb.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
				stmt_cxmb.setString(5, uid);
				stmt_cxmb.execute();
				String data = getValue(rmap.get("data"));			//日期
				com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONArray.parseArray(data);
				for(int i=0;i<jsonArray.size();i++){
					JSONObject json = jsonArray.getJSONObject(i);
					String xh = json.getString("xh");			//序号
					String cxxmc = json.getString("cxxmc");			//查询项名称
					String gs = json.getString("gs");			//公式
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
		 * @param request
		 * @param response
		 * @param rmap
		 * @return
		 */
		@RequestMapping(value="/selectMCMS.do",produces = "text/plain;charset=utf-8")
		@ResponseBody
		public String selectMCMS(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap){
			try{
				String jd = this.findJd(request, response);
				String id = getValue(rmap.get("id"));
				String sql = "select mbmc,to_char(mbms) as mbms from fast_zdyxx where u_id='"+id+"'"+jd;
				Map<String, Object> map_mcms = bs.queryOne(sql);
				return this.toJson("000", "查询成功！", map_mcms);
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败！");
			}
			
		}
	
}
