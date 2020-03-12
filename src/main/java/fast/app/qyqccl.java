package fast.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fast.main.util.Super;

public class qyqccl extends Super {
	
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "qyqrqc/qyqccl";
		} catch (Exception e) {
			e.printStackTrace();
			return "qyqrqc/qyqccl";
		}
	}
	

	

	
	public String queryQyqc(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String jd = this.getRequest().getSession().getAttribute("dwid").toString();
			
			String nsrsbh = getValue(this.getForm().get("nsrsbh"));
			String nsrmc = getValue(this.getForm().get("nsrmc"));
			
			String querysql = "";
			
			
			querysql = "select s.*, j.jd_mc jdmc from fast_qyqc s, dm_jd j where s.ssjd = j.jd_dm and s.ssjd = '" + jd + "' ";
				if(StringUtils.isNotBlank(nsrsbh)) {
					querysql += " and s.nsrsbh = '" + nsrsbh + "' ";
				}
				if(StringUtils.isNotBlank(nsrmc)) {
					querysql += " and s.nsrmc like '%" + nsrmc + "%' ";
				}
				querysql +=  " order by zszt,status ASC,fbrq DESC";
			
			System.out.println(querysql);
			List<Map<String, Object>> list = this.getBs().query(querysql);

			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	

	
	public String checkQyqc(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String uno = this.getRequest().getSession().getAttribute("uno").toString();
			System.out.println("uno"+uno);
			String id = getValue(this.getForm().get("id"));
			String shyj = getValue(this.getForm().get("shyj"));
			System.out.println(shyj);
			
			String shly = getValue(this.getForm().get("shly"));
			System.out.println(shly);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			
			String insertsql = "update fast_qyqc f set f.SHYJ = '" + shyj + "', f.LY = '" + shly + "', f.FKRQ = to_date('" 
					+ formatter.format(date) + "','yyyy-MM-dd'), f.FKZ = '" + uno + "', f.STATUS = '1' where id = '" + id + "'";
			System.out.println(insertsql);
			this.getBs().insert(insertsql);

			return this.toJson("000", "审核成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "审核异常！");
		}
	}
	
	public String removeQyqc(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			
			String id = getValue(this.getForm().get("id"));
			
			String deletesql = "delete from fast_qyqc f where f.id = '" + id + "'";
			System.out.println(deletesql);
			this.getBs().delete(deletesql);

			return this.toJson("000", "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "删除异常！");
		}
	}

}
