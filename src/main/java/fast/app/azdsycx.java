package fast.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;


/**
 * 按重点税源查询
 * @author Administrator
 *
 */
public class azdsycx extends Super {
	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "zdsSjcx/azdsycx";
		}catch(Exception e){
			e.printStackTrace();
			return "zdsSjcx/azdsycx";
		}
	}
	
	
	
	/**
	 * 企业汇总表
	 * @param rmap
	 * @return
	 */
	public String queryAzdsy(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			
			String pageNo = getValue(this.getForm().get("pageNo"));
			String pageSize = getValue(this.getForm().get("pageSize"));
			
			String qsrq = getValue(this.getForm().get("cxrq"));
			
			if("".equals(qsrq) || qsrq ==null) {
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				qsrq = sdf.format(dt);
			}
			
			List<Mode> list=new ArrayList<Mode>();
			list.add(new Mode("IN","String",qsrq));
			list.add(new Mode("OUT","RS",""));
			
			List<Map<String, Object>> sjList= (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYDH", list);
			
			return this.toJson("000", "查询成功！",sjList);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	// 导出excel
		public Object export(Map<String, Object> rmap) {
			try {
				// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
				initMap(rmap);

				String qsrq = getValue(this.getForm().get("cxrq"));
				
				if("".equals(qsrq) || qsrq ==null) {
					Date dt = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
					qsrq = sdf.format(dt);
				}

				List<Mode> list=new ArrayList<Mode>();
				list.add(new Mode("IN","String",qsrq));
				list.add(new Mode("OUT","RS",""));
				
				List<Map<String, Object>> sjList= (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYDH", list);
				

				Map<String, Object> map = new HashMap<String, Object>();
				String[] cols = { "纳税人名称", "税种", "年份", "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", "合计", "增减率"};
				String[] keys = { "NSRMC", "SZ_MC", "NF", "M01", "M02", "M03", "M04", "M05", "M06", "M07", "M08", "M09", "M10", "M11", "M12", "HJ", "HJ"};
				map.put("fileName", "导出excel.xlsx");
				map.put("cols", cols);
				map.put("keys", keys);
				map.put("list", sjList);
				return map;
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询异常！");
			}
		}

}
