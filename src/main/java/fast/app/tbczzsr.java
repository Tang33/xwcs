package fast.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

public class tbczzsr extends Super{

	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "tbcx/tbczzsr";
		}catch(Exception e){
			e.printStackTrace();
			return "tbcx/tbczzsr";
		}
	}
	
	
	public String dataQuery(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String ny = getValue(this.getForm().get("jzTime"));// 年

			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN","String",ny));
			list.add(new Mode("OUT","RS",""));

			List<Map<String,Object>> rs = (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_TB_NEW.GRAPHBYCZZSR", list);// 调用存储过程
			//System.out.println(rs);

			// 判断有没有数据
			if(rs.size() == 0 || rs == null) {
				return this.toJson("006", "查询异常！");
			}

			return this.toJson("000", "查询成功！",rs);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
}