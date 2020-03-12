package fast.app;

import java.util.Map;

import fast.main.util.Super;


/**
 * 企业预警查询
 * @author Administrator
 *
 */
public class qyyjcx extends Super {
	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "wbw/qyyjcx";
		}catch(Exception e){
			e.printStackTrace();
			return "wbw/qyyjcx";
		}
	}

}
