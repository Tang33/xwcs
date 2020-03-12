package fast.app;

import fast.main.util.ExcelRead;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *企业迁入新增
 * @author Administrator
 *
 */
public class qyqrglxz extends Super {
	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "qyqrqc/qyqrglxz";
		}catch(Exception e){
			e.printStackTrace();
			return "qyqrqc/qyqrglxz";
		}
	}

	/**
	 * 初始化查询数据
	 *
	 * @param rmap
	 * @return
	 */
	public String queryInit(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");

			String sql = this.getSql("jd_query");
			List<Map<String, Object>> jdlist = this.getBs().query(sql);
			sql = this.getSql("hy_query");
			List<Map<String, Object>> hylist = this.getBs().query(sql);

			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
			map.put("hylist", hylist);
			map.put("jdlist", jdlist);

			return this.toJson("000", "查询成功！", map);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	public String queryQyqr (Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");

			String nsrsbh = getValue(this.getForm().get("nsrsbh"));
			String nsrmc = getValue(this.getForm().get("nsrmc"));

			String querysql = "select s.*, j.jd_mc jdmc from fast_qyqr s, dm_jd j "
					+ "where s.xssjd = j.jd_dm and s.nsrmc like '%" + nsrmc + "%' or s.nsrsbh = '" + nsrsbh + "' order by s.id";
			System.out.println(querysql);
			List<Map<String, Object>> list = this.getBs().query(querysql);

			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	public String addQyqr(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String uno = this.getRequest().getSession().getAttribute("uno").toString();

			String nsrsbh = getValue(this.getForm().get("nsrsbh"));
			String nsrmc = getValue(this.getForm().get("nsrmc"));
			String xssjd = getValue(this.getForm().get("xssjd"));
			String yqrd = getValue(this.getForm().get("yqrd"));
			String dnljsk = getValue(this.getForm().get("dnljsk"));
			String qnljsk = getValue(this.getForm().get("qnljsk"));
			String qrrq = getValue(this.getForm().get("qrrq"));
			String qryy = getValue(this.getForm().get("qryy"));

			String querySql = "select f.* from fast_qyqr f where f.NSRSBH = '" + nsrsbh + "' and f.NSRMC = '" + nsrmc + "'"
					+ " and f.XSSJD = '" + xssjd + "' and f.YQRD = '" + yqrd + "'";
			System.out.println(querySql);
			List<Map<String, Object>> list = this.getBs().query(querySql);
			if(list.size() > 0) {
				return this.toJson("500", "新增失败，已有该迁入任务！");
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();

			String insertsql = "insert into fast_qyqr(ID,NSRSBH,NSRMC,XSSJD,YQRD,DNLJSK,QNLJSK,QRRQ,QRYY,CJRQ,CJR)"
					+ " values(seq_fast_qyqrqc.nextval,'" + nsrsbh + "','" + nsrmc + "','" + xssjd
					+ "','" + yqrd + "','" + dnljsk + "','" + qnljsk + "',to_date('" + qrrq + "','yyyy-MM-dd'),'" + qryy
					+ "',to_date('" + formatter.format(date) + "','yyyy-MM-dd'),'" + uno + "')";
			System.out.println(insertsql);
			this.getBs().insert(insertsql);

			return this.toJson("000", "新增成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "新增异常！");
		}
	}

	public String removeQyqr(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);

			String id = getValue(this.getForm().get("id"));

			String deletesql = "delete from fast_qyqr f where f.id = '" + id + "'";
			System.out.println(deletesql);
			this.getBs().delete(deletesql);

			return this.toJson("000", "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "删除异常！");
		}
	}

}
