package fast.app;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

public class dnsjdbBn extends Super{

	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "sjfx/dnsjdbBn";
		}catch(Exception e){
			e.printStackTrace();
			return "sjfx/dnsjdbBn";
		}
	}
	
	
	public String dataQuery(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String yf = getValue(this.getForm().get("jzTime"));// 年月份
			String cxkj = getValue(this.getForm().get("optext"));// 统计口径
			String pageNum = getValue(this.getForm().get("pageNo"));// 页数
			String counts = getValue(this.getForm().get("counts"));// 数据总量
			String pageSize = getValue(this.getForm().get("pageSize"));// 查询条数
			
			yf = yf.replace("-", "");// 去掉年月中"-"
			
			// 如果当前页数是1,没有数据，就是第一次查询
			if ("1".equals(pageNum) || counts == null || "".equals(counts)) {
				List<Mode> list = new ArrayList<Mode>();
				list.add(new Mode("IN","String",yf));
				list.add(new Mode("IN","String",cxkj));
				list.add(new Mode("OUT","RS",""));
				
				List<Map<String,Object>> rs = (List<Map<String, Object>>) JdbcConnectedPro.call("sj_cx_2012.EIGHT_ANAYBYDNSJBD_BN_JLS", list);// 调用存储过程
				
				counts = rs.get(0).get("COUNTS").toString();// 保存数据总量用于分页
				
				pageNum = "1";
				
			}
			
			String recorders1 = String.valueOf((Integer.parseInt(pageNum)-1)*Integer.parseInt(pageSize));// 查询条数开始数
			String recorders2 = String.valueOf(Integer.parseInt(pageNum)*Integer.parseInt(pageSize));// 查询条数结束数
			
			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN","String",yf));
			list.add(new Mode("IN","String",recorders1));
			list.add(new Mode("IN","String",recorders2));
			list.add(new Mode("IN","String",cxkj));
			list.add(new Mode("OUT","RS",""));
			List<Map<String,Object>> rs1 = (List<Map<String, Object>>) JdbcConnectedPro.call("sj_cx_2012.EIGHT_ANAYBYDNSJBD_BN", list);// 调用存储过程
			//System.out.println(rs1);
			
			int count = Integer.parseInt(counts);// 字符串转为int，数据总量
			
			return this.toJson("000", "查询成功！",rs1,count);
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

			String yf = getValue(this.getForm().get("jzTime"));
			String cxkj = getValue(this.getForm().get("optext"));


			List<Mode> list=new ArrayList<Mode>();
			list.add(new Mode("IN","String",yf));
			list.add(new Mode("IN","String","1"));
			list.add(new Mode("IN","String","80000"));
			list.add(new Mode("IN","String",cxkj));
			list.add(new Mode("OUT","RS",""));

			List<Map<String, Object>> sjList= (List<Map<String, Object>>) JdbcConnectedPro.call("sj_cx_2012.EIGHT_ANAYBYDNSJBD_BN", list);// 调用存储过程
			//System.out.println(sjList);

			Map<String, Object> map = new HashMap<String, Object>();
			String[] cols = { "纳税人名称", "年份", "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", "截至本月合计", "全年合计"};// 表头
			String[] keys = { "NSRMC", "NF", "M01", "M02", "M03", "M04", "M05", "M06", "M07", "M08", "M09", "M10", "M11", "M12", "HJ", "NHJ"};// 内容
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