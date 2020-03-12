package fast.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.map.LinkedMap;

import com.alibaba.excel.support.ExcelTypeEnum;

import fast.main.util.EasyExcelUtil;
import fast.main.util.Super;

public class Symbcx extends Super{

	private Map<String, Object> user = null;
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");

			return "sjfx/Symbcx";
		} catch (Exception e) {
			e.printStackTrace();
			return "sjfx/Symbcx";
		}
	}
	
	public String getdata(Map<String, Object> rmap){
		
		init(rmap);
		String jd_dm = getValue(this.getRequest().getSession().getAttribute("dwid"));
		String mbmc = getValue(this.getForm().get("mbmc"));
		String page = getValue(this.getForm().get("page"));
		String pagesize = getValue(this.getForm().get("limit"));
		String ybggys_type = "";
		pagesize=Integer.toString(Integer.parseInt(pagesize)-1);
		
		String sql="";
		if(mbmc.equals("FAST_YLJ")){
			sql="select yf 月份,ROUND(dyljje/10000,2) 当月累计金额,ROUND(tqljje/10000,2) 同期累计金额,ROUND(zf/10000,2) 增幅,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) 增长比例 from FAST_YLJ where JD_DM="+jd_dm;
		}else if(mbmc.equals("FAST_NSZZB")){
			sql="select zsxm_mc 征收项目,ROUND(dnljje/10000,2) 当年累计金额,ROUND(tqljje/10000,2) 同期累计金额,ROUND(zf/10000,2) 增幅,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) 增长比例,(case when zb is not null then ROUND(zb*100,2)||'%' else null end) 占比 from FAST_NSZZB WHERE JD_DM="+jd_dm+" order by dnljje desc";
		}
		else if(mbmc.equals("FAST_NHYZB")){
			sql="select hymc 行业名称,ROUND(dnljje/10000,2) 当年累计金额,ROUND(tqljje/10000,2) 同期累计金额,ROUND(zf/10000,2) 增幅,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) 增长比例,(case when zb is not null then ROUND(zb*100,2)||'%' else null end) 占比 from FAST_NHYZB WHERE  JD_DM='"+jd_dm+"'  order by dnljje desc";
		}else if(mbmc.matches(".*?_[0-2]{1}$")){
			ybggys_type = mbmc.substring(mbmc.length()-1, mbmc.length());
			mbmc = mbmc.replaceAll("_[0-9]{1}$", "");
			if(ybggys_type.equals("0")){
				sql="select nsrmc 纳税人名称,ROUND(dnljje/10000,2) 当年累计金额,ROUND(tqljje/10000,2) 同期累计金额,ROUND(zf/10000,2) 增幅,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) 增长比例  from "+mbmc+" where bs = '"+ybggys_type+"' AND JD_DM="+jd_dm+" order by dnljje desc";
			}else if(ybggys_type.equals("1")){
				sql="select nsrmc 纳税人名称,ROUND(dnljje/10000,2) 当年累计金额,ROUND(tqljje/10000,2) 同期累计金额,ROUND(zf/10000,2) 增幅,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) 增长比例  from "+mbmc+" where bs = '"+ybggys_type+"' AND JD_DM="+jd_dm+" order by zf desc";
			} else{
				sql="select nsrmc 纳税人名称,ROUND(dnljje/10000,2) 当年累计金额,ROUND(tqljje/10000,2) 同期累计金额,ROUND(zf/10000,2) 增幅,(case when zzbl is not null then ROUND(zzbl*100,2)||'%' else null end) 增长比例  from "+mbmc+" where bs = '"+ybggys_type+"' AND JD_DM="+jd_dm+" order by zf asc";
			}
		}else if (mbmc.equals("")) {
			return this.toJson("000", "查询成功", null, 0);
		}
		
		//获取count
		List<Map<String, Object>> jglistct = this.getBs().query(sql);
	if(mbmc.equals("FAST_YLJ")){
		sql = "select * from (select row_.*, rownum rowno from ("+sql+") row_ where rownum <= " + pagesize + "*"
				+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize + " + 1 union all select '合计' 月份,ROUND(SUM(dyljje)/10000,2) 当月累计金额,ROUND(SUM(tqljje)/10000,2) 同期累计金额, null 增幅, null 增长比例, 10 rowno from FAST_YLJ where jd_dm="+jd_dm;
			
		}else if(mbmc.equals("FAST_NSZZB")){
			sql = "select * from (select row_.*, rownum rowno from ("+sql+") row_ where rownum <= " + pagesize + "*"
					+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize + " + 1 union all select '合计' 征收项目,ROUND(SUM(dnljje)/10000,2) 当年累计金额,ROUND(SUM(tqljje)/10000,2) 同期累计金额, null 增幅, null 增长比例,null 占比, 10 rowno from FAST_NSZZB where jd_dm="+jd_dm;	
			
		}else if(mbmc.equals("FAST_NHYZB")){
			sql = "select * from (select row_.*, rownum rowno from ("+sql+") row_ where rownum <= " + pagesize + "*"
					+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize + " + 1 union all select '合计' 行业名称,ROUND(SUM(dnljje)/10000,2) 当年累计金额,ROUND(SUM(tqljje)/10000,2) 同期累计金额, null 增幅, null 增长比例,null 占比, 10 rowno from FAST_NHYZB where jd_dm='"+jd_dm+"'";	

		}else{
			sql = "select * from (select row_.*, rownum rowno from ("+sql+") row_ where rownum <= " + pagesize + "*"
					+ page + ") a where a.rowno >= (" + page + "- 1) * " + pagesize + " + 1 union all select '合计' 纳税人名称,ROUND(SUM(dnljje)/10000,2) 当年累计金额,ROUND(SUM(tqljje)/10000,2) 同期累计金额, null 增幅, null 增长比例, 10 rowno from "+mbmc+" where bs="+ybggys_type+" and jd_dm="+jd_dm;
		}
		
		
		List<Map<String, Object>> jglist = this.getBs().query(sql);
		return this.toJson("000", "查询成功", jglist, jglistct.size());

		
		
	}
	
	
	public String getheader(Map<String, Object> rmap){
		
		init(rmap);
		
		String mbmc = getValue(this.getForm().get("mbmc"));
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if(mbmc.equals("FAST_YLJ")){
			String[] arr = {"月份", "当月累计金额", "同期累计金额", "增幅", "增长比例"};
			for(int i=0;i<arr.length;i++){
				Map<String,String> map = new LinkedHashMap<>();
				map.put("field", arr[i]);
				map.put("title", arr[i]);
				map.put("align", "right");
				list.add(map);
				
			}
			
		}else if(mbmc.equals("FAST_NSZZB")){
			String[] arr = {"征收项目", "当年累计金额", "同期累计金额", "增幅", "增长比例", "占比"};
			for(int i=0;i<arr.length;i++){
				Map<String,String> map = new LinkedHashMap<>();
				map.put("field", arr[i]);
				map.put("title", arr[i]);
				
				if("征收项目".equals(arr[i])){
					map.put("align", "left");
				}else{
					map.put("align", "right");
				}
				
				list.add(map);
			}
			
		}else if(mbmc.equals("FAST_NHYZB")){
			
			
			String[] arr = {"行业名称", "当年累计金额", "同期累计金额", "增幅", "增长比例", "占比"};
			for(int i=0;i<arr.length;i++){
				Map<String,String> map = new LinkedHashMap<>();
				map.put("field", arr[i]);
				map.put("title", arr[i]);
				
				if("行业名称".equals(arr[i])){
					map.put("align", "left");
				}else{
					map.put("align", "right");
				}
				
				list.add(map);
				
			}
			
		}else{
			
			
			String[] arr = {"纳税人名称", "当年累计金额", "同期累计金额", "增幅", "增长比例"};
			for(int i=0;i<arr.length;i++){
				Map<String,String> map = new LinkedHashMap<>();
				map.put("field", arr[i]);
				map.put("title", arr[i]);
				if("纳税人名称".equals(arr[i])){
					map.put("align", "left");
				}else{
					map.put("align", "right");
				}
				
				list.add(map);
				
			}
			
		}
		
		return this.toJson("000", "查询成功", list);
		
		
	}
}
