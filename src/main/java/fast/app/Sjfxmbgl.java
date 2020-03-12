package fast.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.druid.util.StringUtils;

import fast.main.util.ExcelRead;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;
import fast.main.util.ZipUtil;

public class Sjfxmbgl extends Super {
	private Map<String, Object> user = null;

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");

			return "sjfx/Sjfxmbgl";
		} catch (Exception e) {
			e.printStackTrace();
			return "sjfx/Sjfxmbgl";
		}
	}
	
	
	public String querymb(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String pageNo = getValue(this.getForm().get("page"));
			String pageSize = getValue(this.getForm().get("limit"));
			
			String sql="select t.mbmc,t.createtime,t.status,t.id from fast_sjfxmb t";
			sql = "select * from (select row_.*, rownum rowno from (" + sql + ") row_ where rownum <= " + pageSize + "*"
				    + pageNo + ") a where a.rowno >= (" + pageNo + "- 1) * " + pageSize + " + 1";
			List<Map<String, Object>> list = this.getBs().query(sql);
			
			
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map=list.get(i);
				map.put("id", map.get("ID"));
				map.put("mbmc", map.get("MBMC"));
				map.put("cjsj", map.get("CREATETIME"));
				map.put("zt", map.get("STATUS"));
				
				lists.add(map);
			}
			
			
			int count = this.getBs().queryCount(sql);
			return this.toJsonct("000", "查询成功！", lists, Integer.toString(count));
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
			
			
			String updatesql = " update sjfxmb set status='"+status+"' where id ='"+id+"'";

			Integer upnum = this.getBs().update(updatesql);
			if(upnum!=0) {
				
				return this.toJson("000", "修改成功！");
			}else {
				
				return this.toJson("000", "修改失败！");
			}
			
		}


}
