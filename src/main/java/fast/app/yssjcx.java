package fast.app;

import java.util.List;
import java.util.Map;

import fast.main.util.Super;

public class yssjcx extends Super {
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/yssjcx";
		} catch (Exception e) {
			e.printStackTrace();
			return "xtgl/yssjcx";
		}
	}
	
	//查询页面加载下拉框中的数据
	public String selectList(Map<String, Object> rmap) {
		// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
		try {
			initMap(rmap);
			String tableName =getValue(this.getForm().get("tableName"));
			tableName = tableName.toLowerCase();
			if (tableName == "") {
				tableName = "xwcs_gsdr_yssjrk";
				String sql = "select MBM,ZDM FROM  FAST_YSSJCX_MB where DYCXB = '"+tableName+"' group by MBM,ZDM";
				List<Map<String,Object>> list = this.getBs().query(sql);
				return this.toJson("000","查询成功", list);
			} else {
				String sql = "select MBM,ZDM FROM  FAST_YSSJCX_MB where DYCXB = '"+tableName+"' group by MBM,ZDM";
				List<Map<String,Object>> list = this.getBs().query(sql);
				return this.toJson("000","查询成功", list);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常");
		}
	}
	
	//查询汇总信息
	public String queryfz(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String tableName =getValue(this.getForm().get("tableName"));
			tableName = tableName.toLowerCase();
			if (tableName == "") {
				tableName = "xwcs_gsdr_yssjrk";
				String sql = "SELECT DISTINCT mbm,zdm FROM FAST_YSSJCX_MB where DYCXB = '"+tableName+"'";
				List<Map<String,Object>> list = this.getBs().query(sql);
				return this.toJson("000","查询成功", list);
			} else {
				String sql = "SELECT DISTINCT mbm,zdm FROM FAST_YSSJCX_MB where DYCXB = '"+tableName+"'";
				List<Map<String,Object>> list = this.getBs().query(sql);
				return this.toJson("000","查询成功", list);
			}
		} catch (Exception e) {
			return this.toJson("009", "查询异常");
		}
	}
}
