package fast.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.excel.support.ExcelTypeEnum;

import fast.main.util.EasyExcelUtil;
import fast.main.util.Super;

public class Zdydr extends Super{

	private Map<String, Object> user = null;
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");

			return "sjfx/Zdydr";
		} catch (Exception e) {
			e.printStackTrace();
			return "sjfx/Zdydr";
		}
	}
	/**
	 * 自定义导入
	 * @param rmap
	 * @return
	 */
	public String impExcel(Map<String, Object> rmap){
		init(rmap);
		String mesg = "";
		String code = "";
		String uid = UUID.randomUUID().toString().replace("-", "");
		String path = getValue(this.getForm().get("path"));
		String mbmc = getValue(this.getForm().get("mbmc"));
		try {
			Map<String, Object> rs = this.getBs().queryOne("select count(*) count from FAST_ZDYXX where mbmc = '"+mbmc+"'");
			if(!"0".equals(rs.get("COUNT").toString())){
				return this.toJson("001", "模板名称已存在", "");
			}
			InputStream is = new FileInputStream(new File(path));
			List<List<String>> lists=new ArrayList<List<String>>();
			if(path.endsWith(".xls")){
				//97以前
				lists=EasyExcelUtil.readExcelWithStringList(is, ExcelTypeEnum.XLS);
			}else if(path.endsWith(".xlsx")){
				//07以后
				lists=EasyExcelUtil.readExcelWithStringList(is, ExcelTypeEnum.XLSX);
			}
			String insertSql = "insert into FAST_ZDYDR (";
			for(int i = 0;i<lists.size();i++){
				if(i == 0){
					List<String> headList = lists.get(i);
					if(!"纳税人名称".equals(headList.get(0)) || !"纳税人识别号".equals(headList.get(1)) || headList.size()>40){
						mesg = "文件格式不正确";
						code = "002";
						return this.toJson(code, mesg, uid);
					}
					String head = "";
					for(int j = 0;j<headList.size();j++){
						head = head + headList.get(j) + ",";
						insertSql = insertSql + "A"+(j+1) + ",";
					}
					head = head.substring(0, head.length()-1);
					insertSql = insertSql + "u_id) values ";
					String sql = "insert into FAST_ZDYXX (u_id,bt,drsj,mbmc) values ('"+uid+"','"+head+"',sysdate,'"+mbmc+"')";
					this.getBs().insert(sql);
				}else{
					String valueSql = "(";
					List<String> valueList = lists.get(i);
					for(int j = 0;j<valueList.size();j++){
						String value = valueList.get(j);
						valueSql= valueSql + "'" + value + "',";
					}
					valueSql = valueSql + "'"+uid+"')";
					this.getBs().insert(insertSql + valueSql);
				}
			}
			mesg = "导入成功";
			code = "000";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.toJson(code, mesg, uid);
		
	}
	
	/*public String getData(Map<String, Object> rmap){
		init(rmap);
		String date = getValue(this.getForm().get("date"));
		String uid = getValue(this.getForm().get("uid"));
		String page = getValue(this.getForm().get("page"));
		String rows = getValue(this.getForm().get("rows"));
		if(date == null || "".equals(date)){
			date = this.getBs().queryOne("SELECT to_char(max(RK_RQ),'yyyyMM') as rkrq FROM SB_NSRXX").get("RKRQ").toString();
		}
		String year = date.substring(0,4);
		String month = date.substring(4,date.length());
		String lastyear = String.valueOf(Integer.parseInt(year)-1);
		String lastmonth = lastyear + month;
		Map<String, Object> rs = this.getBs().queryOne("select (select count(*) from FAST_ZDYDR where u_id = '"+uid+"' and YF = '"+date+"') count1,(select count(*) from FAST_ZDYDR where u_id = '"+uid+"') count2 from dual");
		int total = Integer.parseInt(rs.get("COUNT1").toString());
		int count = Integer.parseInt(rs.get("COUNT2").toString());
		if(total == 0){
			String sql1 = "update FAST_ZDYDR t set (yf,dnljse) = (select '"+date+"',sum(case when to_char(rk_rq,'yyyymm')<='"+date+"' then dfzse end ) from sb_nsrxx a where to_char(rk_rq,'yyyy')='"+year+"' and a.nsrmc = t.a1) where u_id = '"+uid+"'";
			this.getBs().update(sql1);
			String sql2 = "update FAST_ZDYDR t set TQLJSE = (select sum(case when to_char(rk_rq,'yyyymm')<='"+lastmonth+"' then dfzse end ) from sb_nsrxx a where to_char(rk_rq,'yyyy')='"+lastyear+"' and a.nsrmc = t.a1) where u_id = '"+uid+"'";
			this.getBs().update(sql2);
			String sql3 = "update FAST_ZDYDR set zf = dnljse - tqljse,zzbl = (dnljse - tqljse)/tqljse where nvl(dnljse,0)!=0 and nvl(tqljse,0)!=0";
			this.getBs().update(sql3);
		}
		Map<String, Object> rs1 = this.getBs().queryOne("select to_char(bt) bt from FAST_ZDYXX where u_id = '" + uid + "'");
		String head = rs1.get("BT") == null ? "" : rs1.get("BT").toString();
		String[] arr = head.split(",");
		String sql = "select ";
		for(int i = 0;i<arr.length;i++){
			sql = sql + "A" + (i+1) + " " +arr[i] + ",";
		}
		sql = sql + "DNLJSE 当年累计税额,TQLJSE 同期累计税额,ZF 增幅,ZZBL 增长比例  from FAST_ZDYDR where u_id = '"+uid+"' order by a1";
		List<Map<String, Object>> query = this.getBs().query("select * from (select row_.*, rownum rowno from (" + sql + ") row_ where rownum <= " + rows + "*"
				+ page + ") a where a.rowno >= (" + page + "- 1) * " + rows + " + 1");
		return this.toJson("000", "查询成功", query,count);
	}*/
	
	public String getMbData(Map<String, Object> rmap){
		init(rmap);
		String date = getValue(this.getForm().get("date"));
		String uid = getValue(this.getForm().get("uid"));
		String page = getValue(this.getForm().get("page"));
		String rows = getValue(this.getForm().get("limit"));
		if(date == null || "".equals(date)){
			date = this.getBs().queryOne("SELECT to_char(max(RK_RQ),'yyyyMM') as rkrq FROM SB_NSRXX").get("RKRQ").toString();
		}
		String year = date.substring(0,4);
		String month = date.substring(4,date.length());
		String lastyear = String.valueOf(Integer.parseInt(year)-1);
		String lastmonth = lastyear + month;
		Map<String, Object> rs = this.getBs().queryOne("select count(*) count from FAST_ZDYDR where u_id = '" + uid + "'");
		int count = Integer.parseInt(rs.get("COUNT").toString());
		this.getBs().insert("insert into fast_setj (nsrmc,nsrsbh,yf) select a1,a2,'"+date+"' from FAST_ZDYDR t where (select count(*) from fast_setj a where a.nsrmc = t.a1 and yf = '"+date+"')=0 and u_id = '"+uid+"'");
		this.getBs().update("update fast_setj t set dnljse = (select sum(case when to_char(rk_rq,'yyyymm')<='"+date+"' then dfzse end ) from sb_nsrxx a where to_char(rk_rq,'yyyy')='"+year+"' and a.nsrmc = t.nsrmc) where statu is null");
		this.getBs().update("update fast_setj t set TQLJSE = (select sum(case when to_char(rk_rq,'yyyymm')<='"+lastmonth+"' then dfzse end ) from sb_nsrxx a where to_char(rk_rq,'yyyy')='"+lastyear+"' and a.nsrmc = t.nsrmc) where statu is null");
		this.getBs().update("update fast_setj set zf = dnljse - tqljse,zzbl = (dnljse - tqljse)/tqljse,statu = '1' where nvl(dnljse,0)!=0 and nvl(tqljse,0)!=0 and statu is null");
		Map<String, Object> rs1 = this.getBs().queryOne("select to_char(bt) bt from FAST_ZDYXX where u_id = '" + uid + "'");
		String head = rs1.get("BT") == null ? "" : rs1.get("BT").toString();
		String[] arr = head.split(",");
		String sql = "select ";
		for(int i = 0;i<arr.length;i++){
			sql = sql + "A" + (i+1) + ",";
		}
		sql = sql + "b.DNLJSE,b.TQLJSE,b.ZF,b.ZZBL from FAST_ZDYDR a inner join (select * from fast_setj where yf = '"+date+"') b on a.a1 = b.nsrmc";
		List<Map<String, Object>> query = this.getBs().query("select * from (select row_.*, rownum rowno from (" + sql + ") row_ where rownum <= " + rows + "*"
				+ page + ") a where a.rowno >= (" + page + "- 1) * " + rows + " + 1");
		return this.toJson("000", "查询成功", query,count);
	}
	
	public String getTitle(Map<String, Object> rmap){
		init(rmap);
		String mbmc = getValue(this.getForm().get("mbmc"));
		Map<String, Object> rs1 = this.getBs().queryOne("select to_char(bt) bt from FAST_ZDYXX where mbmc = '" + mbmc + "'");
		String head = rs1.get("BT") == null ? "" : rs1.get("BT").toString();
		String[] arr = head.split(",");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(int i = 0;i<arr.length;i++){
			String field = "A"+(i+1);
			String title = arr[i];
			Map<String,String> map = new LinkedHashMap<String,String>();
			map.put("field", field);
			map.put("title", title);
			list.add(map);
		}
		Map<String,String> map1 = new LinkedHashMap<String,String>();
		Map<String,String> map2 = new LinkedHashMap<String,String>();
		Map<String,String> map3 = new LinkedHashMap<String,String>();
		Map<String,String> map4 = new LinkedHashMap<String,String>();
		map1.put("field", "DNLJSE");
		map1.put("title", "当年累计税额");
		list.add(map1);
		map2.put("field", "TQLJSE");
		map2.put("title", "同期累计税额");
		list.add(map2);
		map3.put("field", "ZF");
		map3.put("title", "增幅");
		list.add(map3);
		map4.put("field", "ZZBL");
		map4.put("title", "增长比例");
		list.add(map4);
		return this.toJson("000", "查询成功", list);
	}
	
	
	public String querymbmc(Map<String, Object> rmap){
		init(rmap);
		String sql = "select mbmc,u_id from FAST_ZDYXX where mblx='定制企业模板' and zt='1'";
		List<Map<String, Object>> mbmc = this.getBs().query(sql);
		return this.toJson("000", "查询成功", mbmc);
	}
}
