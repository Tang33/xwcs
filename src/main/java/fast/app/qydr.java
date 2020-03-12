package fast.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.ExcelRead;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;
import fast.main.util.XLSXCovertCSVReader;

/**
 * 五百万企业导入
 * @author Administrator
 *
 */
public class qydr extends Super {
	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "wbw/qydr";
		}catch(Exception e){
			e.printStackTrace();
			return "wbw/qydr";
		}
	}
	
	
	//将数据从excel取出并存到临时表
		public String doInput(Map<String, Object> rmap) {
			try {
				initMap(rmap);
				String filename = getValue(this.getForm().get("filename"));
				String rq =  getValue(this.getForm().get("rq"));
				String deleteSql = "Truncate Table TEMP_WBW_SJDR ";
				this.getBs().delete(deleteSql);
				
				InputStream is = new FileInputStream(new File(filename));
				Map<String, Integer> map=new HashMap<String, Integer>();
	            map.put("纳税人名称",-1);
	            //List<Map<String, String>> list=XLSXCovertCSVReader.pomExcel(filename, is, map);
	            List<Map<String, String>> list=ExcelRead.pomExcel(filename, is, map);
				if(list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String insertSql = " Insert into TEMP_WBW_SJDR(nsrmc,sl,xxsr,xse,nd,rq) Values ('"+list.get(i).get("纳税人名称")+"',"+list.get(i).get("税率")+",0,0,"+rq+",to_date('"+rq+"','YYYY'))";
						this.getBs().insert(insertSql);
					}
				}else {
					return this.toJson("006", "数据上传失败！");
				}
				return this.toJson("000", "数据上传成功！");
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "数据上传失败！");
			}
		}
		
		public String doInputSL(Map<String, Object> rmap) {
			try {
				initMap(rmap);
				String filename = getValue(this.getForm().get("filename"));
				String rq =  getValue(this.getForm().get("rq"));
				String deleteSql = "Truncate Table TEMP_WBW_SJDR ";
				this.getBs().delete(deleteSql);
				
				InputStream is = new FileInputStream(new File(filename));
				Map<String, Integer> map=new HashMap<String, Integer>();
	            map.put("纳税人名称",-1);
	            map.put("税率",-1);
	            List<Map<String, String>> list=ExcelRead.pomExcel(filename, is, map);
				if(list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String insertSql = " Insert into TEMP_WBW_SJDR(nsrmc,sl,xxsr,xse,nd,rq) Values ('"+list.get(i).get("纳税人名称")+"',"+list.get(i).get("税率")+",0,0,"+rq+",to_date('"+rq+"','YYYY'))";
						this.getBs().insert(insertSql);
					}
				}else {
					return this.toJson("006", "数据上传失败！");
				}
				return this.toJson("000", "数据上传成功！");
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "数据上传失败！");
			}
		}
		
		public String doInputXSE(Map<String, Object> rmap) {
			try {
				initMap(rmap);
				String filename = getValue(this.getForm().get("filename"));
				String rq =  getValue(this.getForm().get("rq"));
				String deleteSql = "Truncate Table TEMP_WBW_QYXX ";
				this.getBs().delete(deleteSql);
				
				InputStream is = new FileInputStream(new File(filename));
				Map<String, Integer> map=new HashMap<String, Integer>();
	            map.put("纳税人名称",-1);
	            map.put("销售额",-1);
	            List<Map<String, String>> list=ExcelRead.pomExcel(filename, is, map);
				if(list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String insertSql = " Insert into TEMP_WBW_QYXX(nsrmc,xse,nd,rq) Values ('"+list.get(i).get("纳税人名称")+"',"+list.get(i).get("销售额")+","+rq+",to_date('"+rq+"','YYYY'))";
						this.getBs().insert(insertSql);
					}
				}else {
					return this.toJson("006", "数据上传失败！");
				}
				return this.toJson("000", "数据上传成功！");
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "数据上传失败！");
			}
		}
	
		public String execA(Map<String, Object> rmap) {
			try {
				initMap(rmap);
				String rq =  getValue(this.getForm().get("rq"));
				
				List<Mode> list=new ArrayList<Mode>();
				list.add(new Mode("IN","String","A"));
				list.add(new Mode("IN","String",rq));
				list.add(new Mode("OUT","RS",""));
				
				List<Map<String, Object>> sjList = (List<Map<String, Object>>) JdbcConnectedPro.call("PKG_CX_WBW.WBWQYMXCX_XSE", list);
				System.out.println(sjList);
				return this.toJson("000", "数据上传成功！",sjList);
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "数据上传失败！");
			}
		}
		
		public String execB(Map<String, Object> rmap) {
			try {
				initMap(rmap);
				String rq =  getValue(this.getForm().get("rq"));
				
				List<Mode> list=new ArrayList<Mode>();
				list.add(new Mode("IN","String","B"));
				list.add(new Mode("IN","String",rq));
				list.add(new Mode("OUT","RS",""));
				
				List<Map<String, Object>> sjList = (List<Map<String, Object>>) JdbcConnectedPro.call("PKG_CX_WBW.WBWQYMXCX_XSE", list);
				
				return this.toJson("000", "数据上传成功！",sjList);
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "数据上传失败！");
			}
		}

		public String execD(Map<String, Object> rmap) {
			try {
				initMap(rmap);
				String rq =  getValue(this.getForm().get("rq"));
				
				List<Mode> list=new ArrayList<Mode>();
				list.add(new Mode("IN","String","D"));
				list.add(new Mode("IN","String",rq));
				list.add(new Mode("OUT","RS",""));
				
				List<Map<String, Object>> sjList = (List<Map<String, Object>>) JdbcConnectedPro.call("PKG_CX_WBW.WBWQYMXCX_XSE", list);
				
				return this.toJson("000", "数据上传成功！",sjList);
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "数据上传失败！");
			}
		}
		
		public String getallList(Map<String, Object> rmap) {
			try {
				initMap(rmap);
				String pageNo = getValue(this.getForm().get("pageNo"));
				String pageSize = getValue(this.getForm().get("pageSize"));
				
				String sql = "select * from TEMP_WBW_SJDR";
				List<Map<String, Object>> list = this.getBs().query(sql, pageNo, pageSize);
				int conut=this.getBs().queryCount(sql);
				return this.toJson("000", "数据上传成功！",list,conut);
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "数据上传失败！");
			}
		}
		
		

	
	

}
