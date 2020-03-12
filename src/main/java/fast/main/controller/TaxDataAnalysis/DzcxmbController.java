package fast.main.controller.TaxDataAnalysis;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;
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
 * 定制模板查询
 *
 */
@Controller
@RequestMapping("dzcxmb")
public class DzcxmbController extends Super{
	@Autowired
	BaseService bs;

	/**
	 * 进入定制查询模板跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		
		return "TaxDataAnalysis/dzcxmb";
		
	}

	/**
	 * 定制模板查询
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/generateMb.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String generateMb(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form){
		Connection conn = null;
		String uid = UUID.randomUUID().toString().replace("-", "");
		String jd_dm = getValue(request.getSession().getAttribute("dwid"));			//街道
		String mbmc = getValue(form.get("mbmc"));			//模板名称
		Map<String, Object> rs = bs.queryOne("select count(*) count from FAST_ZDYXX where mbmc = '"+mbmc+"'");
		if(!"0".equals(rs.get("COUNT").toString())){
			return this.toJson("001", "模板名称已存在", "");
		}
		String mbms = getValue(form.get("mbms"));			//模板描述
		String str = getValue(form.get("data"));			//日期
		try{
			String czy = request.getSession().getAttribute("uname").toString();
			conn = Mbdy.getConnection();
			conn.setAutoCommit(false);
			String sql="insert into FAST_ZDYXX (u_id,bt,drsj,mbmc,czy,mbms,zt,mblx,jd_dm) values(?,?,?,?,?,?,?,?,?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, uid);
			stmt.setString(2, "");
			stmt.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
			stmt.setString(4, mbmc);
			stmt.setString(5, czy);
			StringReader reader = new StringReader(mbms);  
		    stmt.setCharacterStream(6, reader, mbms.length());
		    stmt.setString(7, "1");
		    stmt.setString(8, "定制查询模板");
		    stmt.setString(9, jd_dm);
			stmt.execute();
			com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONArray.parseArray(str);
			for(int i=0;i<jsonArray.size();i++){
				JSONObject json = jsonArray.getJSONObject(i);
				String xh = json.getString("xh");			//序号
				String cxxmc = json.getString("cxxmc");			//查询项名称
				String gs = json.getString("gs");			//公式
				if(gs==null){
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
					Matcher mtc_sz = Pattern.compile("增值税|营业税|个人所得税|房产税|印花税|车船税|企业所得税|营改增增值税|城市维护建设税|地方教育附加|教育附加|城镇土地使用税|环保税").matcher(cxxmc);
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
			return this.toJson("000", "模板生成成功！");
		}catch(Exception e){
			e.printStackTrace();
			try {
				if(conn!=null)
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return this.toJson("009", "模板生成失败！");
		}finally{
			try {
				if(conn != null)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
