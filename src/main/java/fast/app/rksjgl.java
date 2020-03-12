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

import fast.main.util.ExcelRead;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;
import fast.main.util.ZipUtil;

public class rksjgl extends Super {
	private Map<String, Object> user = null;

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			return "sssjgl/rksjgl";
		} catch (Exception e) {
			e.printStackTrace();
			return "sssjgl/rksjgl";
		}
	}

	// 将数据从excel取出并存到临时表
	public String doInput(Map<String, Object> rmap) {
		String filenameszip = "";
		initMap(rmap);
		user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
		String filenames = getValue(this.getForm().get("filenames"));
		String drfs = getValue(this.getForm().get("drfs"));
		String rkrq = getValue(this.getForm().get("rkrq"));
		String path = this.getRequest().getRealPath("/upload");
		
		if (filenames.endsWith(".zip")) {
			// ZipUtil.unZip(new File(filenames), filenames);
			filenameszip = ZipUtil.unZipnew(path + "/", filenames);
			String delgsdrsql = "delete from xwcs_gsdr_temp";
			this.getBs().delete(delgsdrsql);
			InputStream is = null;
			String filenamezip = "";
			String results = "";
			try {
				// 循环将获取到的excel文件一次读取
				if (filenameszip.contains("、")) {
					String[] filsp = filenameszip.split("、");
					for (int i = 0; i < filsp.length; i++) {
						filenamezip = filsp[i];
						results = insttemp(is, drfs, filenamezip);
					}
				} else {
					filenamezip = filenames;
					results = insttemp(is, drfs, filenamezip);
				}
				return this.toJson("000", "查询成功！", results);

			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败！");
			}
		} else if(filenames.endsWith(".rar")){
			
			
			return null;
		}else {
			String delgsdrsql = "delete from xwcs_gsdr_temp";
			this.getBs().delete(delgsdrsql);
			InputStream is = null;
			String filename = "";
			String results = "";
			try {
				// 循环将获取到的excel文件一次读取
				if (filenames.contains("、")) {
					String[] filsp = filenames.split("、");
					for (int i = 0; i < filsp.length; i++) {
						filename = filsp[i];
						results = insttemp(is, drfs, filename);
					}
				} else {
					filename = filenames;
					results = insttemp(is, drfs, filename);
				}
				return this.toJson("000", "查询成功！", results);
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败！");
			}
		}
	}
	
	
	//根据导入方式选择不同的模板（0：入库，1：退库，2：个人所得税）
	private String insttemp(InputStream is, String drfs, String filename) {
		String result = "";
		try {
			is = new FileInputStream(new File(filename));
			// 判断接收到的文件格式
			Map<String, Integer> map = new HashMap<String, Integer>();
			if (drfs != null) {
				if (drfs.equals("0")) {
					map.put("社会信用代码（纳税人识别号）", -1);
					map.put("纳税人名称", -1);
					map.put("征收项目", -1);
					map.put("行业门类", -1);
					map.put("行业", -1);
					map.put("实缴金额", -1);
					map.put("预算分配比例", -1);
					map.put("预算科目代码", -1);
					map.put("预算科目", -1);
					map.put("街道乡镇", -1);
					map.put("税款属性", -1);
					map.put("区县级比例", -1);
					map.put("区县级", -1);
					map.put("征收品目", -1);
					map.put("征收品目代码", -1);
					map.put("行业大类", -1);
					map.put("行业中类", -1);
					map.put("电子税票号码", -1);
					map.put("登记序号", -1);
					map.put("应征凭证序号", -1);
				} else if (drfs.equals("1")) {
					map.put("社会信用代码（纳税人识别号）", -1);
					map.put("纳税人名称", -1);
					map.put("征收项目", -1);
					map.put("行业门类", -1);
					map.put("行业", -1);
					map.put("税额", -1);
					map.put("预算分配比例", -1);
					map.put("预算科目", -1);
					map.put("预算科目名称", -1);
					map.put("街道乡镇", -1);
					map.put("税款属性", -1);
					// map.put("区县级比例",-1);
					// map.put("区县级",-1);
					map.put("征收品目", -1);
					// map.put("征收品目代码",-1);
					map.put("行业大类", -1);
					map.put("行业中类", -1);
					map.put("电子税票号码", -1);
					// map.put("登记序号",-1);
					// map.put("应征凭证序号",-1);
				} else if (drfs.equals("2")) {
					map.put("纳税人识别号", -1);
					map.put("纳税人姓名", -1);
					map.put("征收项目", -1);
					// map.put("行业门类",-1);
					// map.put("行业",-1);
					map.put("应征税金额", -1);
					map.put("预算分配比例", -1);
					// map.put("预算科目代码",-1);
					map.put("预算科目", -1);
					map.put("街道乡镇", -1);
					map.put("税款属性", -1);
					// map.put("区县级比例",-1);
					// map.put("区县级",-1);
					map.put("征收品目", -1);
					// map.put("征收品目代码",-1);
					// map.put("行业大类",-1);
					// map.put("行业中类",-1);
					map.put("电子税票号码", -1);
					// map.put("登记序号",-1);
					// map.put("应征凭证序号",-1);
				}
			}
			List<Map<String, String>> list = ExcelRead.pomExcel(filename, is, map);
			// List<String[]> list = XLSXCovertCSVReader.readerExcel(is,
			// "Sheet1", 100);
			result = JdbcConnectedPro.insertAll(list, drfs);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	// 进行数据比对
	public String doCheck(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			List<Mode> paramsList = new ArrayList<Mode>();
			paramsList.add(new Mode("in", "String", "1"));
			paramsList.add(new Mode("out", "String", ""));
			Object obj = JdbcConnectedPro.call("xwcs_gsdr_bd", paramsList);
			String rs = (String) obj;
			System.out.println(rs);
			return this.toJson("000", "查询成功！", rs);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public static void main(String[] args) {
		String aa = "101";
		String bb = "10101";

		String cc = bb.substring(0, aa.length());
		System.out.println(cc);

	}

	// 设置可查不可查
	public String doState(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String rkrq = getValue(this.getForm().get("rkrq")).replaceAll("-", "");
			;
			String state = getValue(this.getForm().get("State"));
			String updSql = "";
			String sql = "select 1 from xwcs_cxzt where to_char(rkrq,'yyyymm')='" + rkrq + "' ";
			List<Map<String, Object>> list = this.getBs().query(sql);
			if (list != null && list.size() > 0) {
				updSql = "update xwcs_cxzt set state='" + state + "' where to_char(rkrq,'yyyymm')='" + rkrq + "' ";
				this.getBs().update(updSql);
			} else {
				updSql = "insert into xwcs_cxzt(rkrq,state) values(to_date('" + rkrq + "','yyyymm'),'" + state + "')";
				this.getBs().insert(updSql);
			}

			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String yskm(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String date = getValue(this.getForm().get("date"));
//			Object obj =  this.getRequest().getSession().getAttribute("date");
//			Object state = this.getRequest().getSession().getAttribute("state");
			int count = 0;
			String page = getValue(this.getForm().get("pageNo"));
			String pagesize = getValue(this.getForm().get("pageSize"));
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
//			if((obj == null) || !((String)obj).equals(date)){
//				this.getRequest().getSession().setAttribute("date",date);
//				this.getRequest().getSession().setAttribute("state",1);
//				String sqlall="select * from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) sum1,sum(dfzse) sum2 from sb_nsrxx where yskmdm is not null and to_char(rk_rq,'yyyy-mm')>='"+startdate+"' and to_char(rk_rq,'yyyy-mm')<='"+enddate+"' group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) sum1,sum(dfzse) sum2 from sb_nsrxx where yskmdm is not null and to_char(rk_rq,'yyyy-mm')>='"+startdate+"' and to_char(rk_rq,'yyyy-mm')<='"+enddate+"' group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) sum1,sum(dfzse) sum2 from sb_nsrxx where yskmdm is not null and to_char(rk_rq,'yyyy-mm')>='"+startdate+"' and to_char(rk_rq,'yyyy-mm')<='"+enddate+"' group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm)";
			String sql_check = "select * from xwcs_gsdr_temp where rk_rq='"+date+"'";
			if(this.getBs().queryOne(sql_check) == null){
				String sqlall="select f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) sum1,sum(se) sum2 from fast_jkdzd where yskmdm is not null and rk_rq='"+date+"' group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) sum1,sum(se) sum2 from fast_jkdzd where yskmdm is not null and rk_rq='"+date+"' group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) sum1,sum(se) sum2 from fast_jkdzd where yskmdm is not null and rk_rq='"+date+"' group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where sum2 !='0' order by f.yskmdm";
			    System.out.println(sqlall);
				List<Map<String, Object>> listd=this.getBs().query(sqlall);
				count = listd.size();
				this.getRequest().getSession().setAttribute("listd",listd);
				this.getRequest().getSession().setAttribute("count",count);
				for (int i = 0; i < listd.size(); i++) {
					if(i >= (Integer.parseInt(page)-1)*Integer.parseInt(pagesize) && i<=Integer.parseInt(page)*Integer.parseInt(pagesize)){
						Map<String, Object> map=listd.get(i);
						map.put("yskmdm", map.get("YSKMDM"));
						map.put("yskmmc", map.get("YSKMMC"));
						map.put("qkj", map.get("SUM1"));
						map.put("dfkj", map.get("SUM2"));
						map.put("sj", date);
						list.add(map);
					}
				}
				return this.toJson("000", "查询成功！",list, count);
			}else{
				String sqlall="select f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+date+"' group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+date+"' group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+date+"' group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where sum2 !='0' order by f.yskmdm";
			    System.out.println(sqlall);
				List<Map<String, Object>> listd=this.getBs().query(sqlall);
				count = listd.size();
				this.getRequest().getSession().setAttribute("listd",listd);
				this.getRequest().getSession().setAttribute("count",count);
				for (int i = 0; i < listd.size(); i++) {
					if(i >= (Integer.parseInt(page)-1)*Integer.parseInt(pagesize) && i<=Integer.parseInt(page)*Integer.parseInt(pagesize)){
						Map<String, Object> map=listd.get(i);
						map.put("yskmdm", map.get("YSKMDM"));
						map.put("yskmmc", map.get("YSKMMC"));
						map.put("qkj", map.get("SUM1"));
						map.put("dfkj", map.get("SUM2"));
						map.put("sj", date);
						list.add(map);
					}
				}
				return this.toJson("000", "查询成功！",list, count);
			}    
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		} 
	}

	private String formatDouble(double d) {
		NumberFormat nf = NumberFormat.getInstance();
		// 设置保留多少位小数
		nf.setMaximumFractionDigits(2);
		// 取消科学计数法
		nf.setGroupingUsed(false);
		// 返回结果
		return nf.format(d);
	}

	// 将数据从临时表插入正式表
	public String doLoadup(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String rkrq = getValue(this.getForm().get("rkrq")).replaceAll("-", "");
			String cslx = getValue(this.getForm().get("cslx")).replaceAll("-", "");
			String czy_dm = getValue(user.get("UUID"));
//			if (cslx.equals("0")) {
//				String deleteSql = "delete from sb_zsxx s where s.gds='0' and to_char(rk_rq,'yyyyMM')='" + rkrq + "'";
//				System.out.println(deleteSql);
//				this.getBs().delete(deleteSql);
//			}
			String sql = "insert into sb_zsxx s(xh,nsrsbh,nsrmc,hy,zsxm,zse,rk_rq,gds,sjse,sz_dm,"
					+ "hy_dm,jd_dm,zdsyh,wq,lrry_dm,lr_sj,qyxz,sfbg,bl,zspm,zspmdm,hydl,hyzl,yskmdm,dzsphm,djxhs,YZPZXH,SKSX,JD_MC,YSKM_MC,YSFPBL_MC) "
					+ "select S_ZSXX_LOG.nextval,nsrsbh,nsrmc,x.hy_mc,x.zsxm_mc,x.zse,to_date('" + rkrq
					+ "','yyyy-MM')," + "'0',x.se,zsxm_dm,hy_dm,jd_dm,x.zdsyh,x.wq,'" + czy_dm
					+ "',sysdate,'J','0',bl,x.zspm,x.zspmdm,x.hydl,x.hyzl,x.yskmdm,x.dzsphm,x.djxhs,x.YZPZXH,x.SKSX,x.JD_MC,x.YSKM_MC,x.YSFPBL_MC  from xwcs_gsdr_zjb x"
					+ " where not exists( select 1 from sb_zsxx ss where ss.dzsphm=x.dzsphm and ss.zsxm=x.zsxm_mc and ss.zse=x.zse)";
			System.out.println(sql);
			this.getBs().insert(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String dr(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String rkrq = getValue(this.getForm().get("rkrq")).replaceAll("-", "");
			String cslx = getValue(this.getForm().get("cslx")).replaceAll("-", "");
			String czy_dm = getValue(user.get("UUID"));

			String deleteSql = "delete from sb_nsrxx s where s.gds='0' and to_char(rk_rq,'yyyyMM')='" + rkrq + "'";
			System.out.println(deleteSql);
			this.getBs().delete(deleteSql);
			String istSql = "insert into sb_nsrxx(xh,dzsphm,nsrmc,nsrsbh,rk_rq,jd_dm,hy_dm,zzs,ygzzzs,yys,qysds_gs,qysds_ds,qysds,grsds,fcs,yhs,ccs,cswhjss,dfjyfj,jyfj,cztdsys,hbs,zse,dfzse,qyxz,hhnsrmc,lrry_dm,lr_sj,gds,bl,hydl,hyzl,yskmdm,djxhs) ";
			istSql += "  select seq_sb_nsrxx.nextval xh,s.*"
					+ "from (select distinct z.dzsphm,z.nsrmc,z.nsrsbh,z.rk_rq,z.jd_dm,z.hy_dm,"
					+ "     sum(decode(z.zsxm, '增值税', zse, 0)) as zzs,"
					+ "     sum(decode(zsxm, '营改增增值税', zse, 0)) as ygz,"
					+ "     sum(decode(zsxm, '营业税', zse, 0)) as yys,"
					+ "     sum(decode(zsxm, '企业所得税国税', zse, 0)) as qysdsgs,"
					+ "     sum(decode(zsxm, '企业所得税', zse, 0)) as qysdsds,"
					+ "     (sum(decode(zsxm, '企业所得税国税', zse, 0))+sum(decode(zsxm, '企业所得税', zse, 0))) as qysds,"
					+ "     sum(decode(zsxm, '个人所得税', zse, 0)) as grsds,"
					+ "     sum(decode(zsxm, '房产税', zse, 0)) as fcs," + "     sum(decode(zsxm, '印花税', zse, 0)) as yhs,"
					+ "     sum(decode(zsxm, '车船税', zse, 0)) as ccs,"
					+ "     sum(decode(zsxm, '城市维护建设税', zse, 0)) as cswhjss,"
					+ "     sum(decode(zsxm, '地方教育附加', zse, 0)) as dfjyfj,"
					+ "     sum(decode(zsxm, '教育费附加', zse, 0)) as jyfj,"
					+ "     sum(decode(zsxm, '城镇土地使用税', zse, 0)) as cztdsys,"
					+ "     sum(decode(zsxm, '环境保护税', zse, 0)) as hbs," + "     sum(zse) zse,"
					+ "     sum(z.sjse) dfzse," + "     z.qyxz," + "     z.nsrmc hhnsrmc," + "     z.lrry_dm,"
					+ "     z.lr_sj," + "     gds,bl,z.hydl,z.hyzl,z.yskmdm,z.djxhs" + " from sb_zsxx z"
					+ " where to_char(rk_rq, 'yyyyMM') = '" + rkrq + "'"
//					+ " and gds = '0' group by nsrmc,rk_rq,jd_dm,hy_dm,z.qyxz,z.lrry_dm,z.lr_sj,gds,bl,z.zspm,z.zspmdm,z.hydl,z.hyzl,z.yskmdm,z.dzsphm,z.djxhs) s ";
					//针对人保车船税涉及征收项目相同，但征收品目不同，导致最终结果数据产生多条，在此去除征收品目分组汇总
					+ " and gds = '0' group by nsrmc,nsrsbh,rk_rq,jd_dm,hy_dm,z.qyxz,z.lrry_dm,z.lr_sj,gds,bl,z.hydl,z.hyzl,z.yskmdm,z.dzsphm,z.djxhs) s ";

					System.out.println(istSql);
			this.getBs().insert(istSql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}

	}

	public String query(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String pageNo = getValue(this.getForm().get("pageNo"));
			String pageSize = getValue(this.getForm().get("pageSize"));

			String rkrq = getValue(this.getForm().get("rkrq"));
			String gjz = getValue(this.getForm().get("gjz"));
			String hylist = getValue(this.getForm().get("hylist"));
			String jdlist = getValue(this.getForm().get("jdlist"));
			String zslist = getValue(this.getForm().get("zslist"));

			String sql = "select xh,nsrsbh,nsrmc,HY,ZSXM,zse,to_char(rk_rq,'yyyyMM') rk_rq,gds,SJSE,sz_dm,"
					+ "hy_dm,jd_dm,zdsyh,wq,lrry_dm,to_char(lr_sj,'yyyyMM') lr_sj,qyxz,sfbg,bl,zspm,zspmdm,hydl,hyzl,yskmdm,dzsphm,djxhs,YZPZXH,SKSX,YSFPBL_MC,YSKM_MC,JD_MC"
					+ " from sb_zsxx  where 1 = 1 ";

			if (rkrq != null && rkrq != "") {
				sql += " and rk_rq = to_date('" + rkrq + "','yyyyMM') ";
			}

			if (gjz != null && gjz != "") {
				sql += " and ( DJXHS LIKE '%" + gjz + "%' or DZSPHM LIKE '%" + gjz + "%' OR YZPZXH LIKE '%" + gjz
						+ "%' or NSRSBH LIKE '%" + gjz + "%' or NSRMC LIKE '%" + gjz + "%' ) ";
			}
			if (hylist != null && hylist != "") {
				sql += " and HY LIKE '%" + hylist + "' ";
			}
			if (jdlist != null && jdlist != "") {
				sql += " and JD_DM LIKE '%" + jdlist + "' ";
			}
			if (zslist != null && zslist != "") {
				sql += " and ZSXM LIKE '%" + zslist + "' ";
			}
			sql += " order by nsrmc,nsrsbh ";

			List<Map<String, Object>> list = this.getBs().query(sql, pageNo, pageSize);
			int count = this.getBs().queryCount(sql);
			return this.toJson("000", "查询成功！", list, count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String Update(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String ids = getValue(this.getForm().get("ids"));
			String jddm = getValue(this.getForm().get("jddm"));
			String jdmc = getValue(this.getForm().get("jdmc"));
			String nsrmc = getValue(this.getForm().get("nsrmc"));
			if (ids.equals("") || (nsrmc.equals("") && jddm.equals(""))) {
				return this.toJson("001", "修改失败！");
			}
			ids = ids.substring(0, ids.length() - 1);
			System.out.println(ids);
			String deletesql = "delete from sb_zsxx_bf where xh in(" + ids + ") ";
			this.getBs().delete(deletesql);
			String bfsql = " insert into sb_zsxx_bf select * from sb_zsxx where xh in (" + ids + ")";
			this.getBs().insert(bfsql);
			String sql = "update sb_zsxx set SFBG='1' ";
			if (!nsrmc.equals("")) {
				sql += ",NSRMC='" + nsrmc + "'";
			}
			if (!jddm.equals("")) {
				sql += ",JD_DM='" + jddm + "',JD_MC='" + jdmc + "' ";
			}
			sql += "where xh in(" + ids + ")";
			System.out.println(sql);
			this.getBs().update(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String UpdateAll(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String jddm = getValue(this.getForm().get("jddm"));
			String nsrmc = getValue(this.getForm().get("nsrmc"));
			String rkrq = getValue(this.getForm().get("rkrq"));
			String gjz = getValue(this.getForm().get("gjz"));
			String jdmc = getValue(this.getForm().get("jdmc"));
			String hylist = getValue(this.getForm().get("hylist"));
			String jdlist = getValue(this.getForm().get("jdlist"));
			String zslist = getValue(this.getForm().get("zslist"));
			if (nsrmc.equals("") && jddm.equals("")) {
				return this.toJson("001", " 请输入要修改的值！");
			}
			String deletesql = "delete from sb_zsxx_bf where 1=1 ";
			if (rkrq != null && rkrq != "") {
				deletesql += " and rk_rq = to_date('" + rkrq + "','yyyyMM') ";
			}

			if (gjz != null && gjz != "") {
				deletesql += " and ( DJXHS LIKE '%" + gjz + "%' or DZSPHM LIKE '%" + gjz + "%' OR YZPZXH LIKE '%" + gjz
						+ "%' or NSRSBH LIKE '%" + gjz + "%' or NSRMC LIKE '%" + gjz + "%' ) ";
			}
			if (hylist != null && hylist != "") {
				deletesql += " and HY LIKE '%" + hylist + "' ";
			}
			if (jdlist != null && jdlist != "") {
				deletesql += " and JD_DM LIKE '%" + jdlist + "' ";
			}
			if (zslist != null && zslist != "") {
				deletesql += " and ZSXM LIKE '%" + zslist + "' ";
			}
			this.getBs().delete(deletesql);
			String bfsql = " insert into sb_zsxx_bf select * from sb_zsxx where 1=1 ";
			if (rkrq != null && rkrq != "") {
				bfsql += " and rk_rq = to_date('" + rkrq + "','yyyyMM') ";
			}

			if (gjz != null && gjz != "") {
				bfsql += " and ( DJXHS LIKE '%" + gjz + "%' or DZSPHM LIKE '%" + gjz + "%' OR YZPZXH LIKE '%" + gjz
						+ "%' or NSRSBH LIKE '%" + gjz + "%' or NSRMC LIKE '%" + gjz + "%' ) ";
			}
			if (hylist != null && hylist != "") {
				bfsql += " and HY LIKE '%" + hylist + "' ";
			}
			if (jdlist != null && jdlist != "") {
				bfsql += " and JD_DM LIKE '%" + jdlist + "' ";
			}
			if (zslist != null && zslist != "") {
				bfsql += " and ZSXM LIKE '%" + zslist + "' ";
			}
			this.getBs().insert(bfsql);
			String sql = "update sb_zsxx set SFBG='1' ";
			if (!nsrmc.equals("")) {
				sql += ",NSRMC='" + nsrmc + "'";
			}
			if (!jddm.equals("")) {
				sql += ",JD_DM='" + jddm + "',JD_MC='" + jdmc + "' ";
			}
			sql += " where 1=1 ";
			if (rkrq != null && rkrq != "") {
				sql += " and rk_rq = to_date('" + rkrq + "','yyyyMM') ";
			}

			if (gjz != null && gjz != "") {
				sql += " and ( DJXHS LIKE '%" + gjz + "%' or DZSPHM LIKE '%" + gjz + "%' OR YZPZXH LIKE '%" + gjz
						+ "%' or NSRSBH LIKE '%" + gjz + "%' or NSRMC LIKE '%" + gjz + "%' ) ";
			}
			if (hylist != null && hylist != "") {
				sql += " and HY LIKE '%" + hylist + "' ";
			}
			if (jdlist != null && jdlist != "") {
				sql += " and JD_DM LIKE '%" + jdlist + "' ";
			}
			if (zslist != null && zslist != "") {
				sql += " and ZSXM LIKE '%" + zslist + "' ";
			}
			this.getBs().update(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String nszhsnum(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String ssdw_dm = (String) this.getRequest().getSession().getAttribute("dwid");
			String sql = "select count(*) num,to_char(rk_rq,'yyyy') year from sb_zsxx where to_char(rk_rq,'yyyy') = (select TO_CHAR(SYSDATE,'yyyy') from dual) or to_char(rk_rq,'yyyy') = (select TO_CHAR(SYSDATE,'yyyy')-1 from dual)  group by to_char(rk_rq,'yyyy') where jd_dm='"+ssdw_dm+"'";
			List<Map<String, Object>> list = this.getBs().query(sql);
			return this.toJson("000", "查询失败！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String table1(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String sql = "SELECT * FROM  (SELECT a.*,rownum RN FROM (select k.hyml_mc,k.jzse,k.qzse,(k.jzse-k.qzse) zf,round(((k.jzse-k.qzse)/k.qzse)*100,2) tb from ("
					+ "select  s.hyml_mc,jn.jzse,qn.qzse from "
					+ "(select  hy_dm  ,sum(a.zse) jzse from sb_nsrxx a  where rk_rq = (select  Max(rk_rq) from sb_nsrxx) and hy_dm in (select hyml_dm from dm_hyml)   group by hy_dm) jn,"
					+ "(select  hy_dm ,sum(b.zse) qzse  from sb_nsrxx b where rk_rq = (select  ADD_MONTHS(Max(rk_rq),-12) from sb_nsrxx) and hy_dm in (select hyml_dm from dm_hyml)   group by hy_dm) qn,"
					+ " dm_hyml s where s.hyml_dm = jn.hy_dm and s.hyml_dm = qn.hy_dm ) k order by jzse desc) a WHERE ROWNUM <= 20) WHERE RN >0";
			List<Map<String, Object>> list = this.getBs().query(sql);
			return this.toJson("000", "查询失败！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String table2(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String sql = "SELECT * FROM  (SELECT a.*,rownum RN FROM (select k.hyml_mc,k.jzse,k.qzse,(k.jzse-k.qzse) zf,round(((k.jzse-k.qzse)/k.qzse)*100,2) tb from ("
					+ "select  s.hyml_mc,jn.jzse,qn.qzse from "
					+ "(select  hy_dm  ,sum(a.zse) jzse from sb_nsrxx a  where rk_rq = (select  Max(rk_rq) from sb_nsrxx) and hy_dm in (select hyml_dm from dm_hyml)   group by hy_dm) jn,"
					+ "(select  hy_dm ,sum(b.zse) qzse  from sb_nsrxx b where rk_rq = (select  ADD_MONTHS(Max(rk_rq),-12) from sb_nsrxx) and hy_dm in (select hyml_dm from dm_hyml)   group by hy_dm) qn,"
					+ " dm_hyml s where s.hyml_dm = jn.hy_dm and s.hyml_dm = qn.hy_dm ) k order by zf desc) a WHERE ROWNUM <= 20) WHERE RN >0";
			List<Map<String, Object>> list = this.getBs().query(sql);
			return this.toJson("000", "查询失败！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String table3(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String sql = "SELECT * FROM  (SELECT a.*,rownum RN FROM (select k.hyml_mc,k.jzse,k.qzse,(k.jzse-k.qzse) zf,round(((k.jzse-k.qzse)/k.qzse)*100,2) tb from ("
					+ "select  s.hyml_mc,jn.jzse,qn.qzse from "
					+ "(select  hy_dm  ,sum(a.zse) jzse from sb_nsrxx a  where rk_rq = (select  Max(rk_rq) from sb_nsrxx) and hy_dm in (select hyml_dm from dm_hyml)   group by hy_dm) jn,"
					+ "(select  hy_dm ,sum(b.zse) qzse  from sb_nsrxx b where rk_rq = (select  ADD_MONTHS(Max(rk_rq),-12) from sb_nsrxx) and hy_dm in (select hyml_dm from dm_hyml)   group by hy_dm) qn,"
					+ " dm_hyml s where s.hyml_dm = jn.hy_dm and s.hyml_dm = qn.hy_dm ) k order by zf asc) a WHERE ROWNUM <= 20) WHERE RN >0";
			List<Map<String, Object>> list = this.getBs().query(sql);
			return this.toJson("000", "查询失败！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String cstemp(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			String sql = "select * from XTGL_CZY where czy_dm != 'admin'  ";
			String num = "2";
			List<Map<String, Object>> list = this.getBs().query(sql);
			for (int i = 0; i < list.size(); i++) {
				String sql2 = this.getSql2("insert into FAST_USER (UUID,uno,uname,upwd,yxbz,dwid) values(?,?,?,?,?,?)",
						new Object[] { num + i + 1, list.get(i).get("CZY_DM").toString(),
								list.get(i).get("CZY_MC").toString(),
								DigestUtils.md5Hex(list.get(i).get("PASSWORD").toString()), 'Y',
								list.get(i).get("SSDW_DM").toString() });
				this.getBs().insert(sql2);
			}

			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String GetNowDate() {
		String temp_str = "";
		Date dt = new Date();
		// 最后的aa表示“上午”或“下午” HH表示24小时制 如果换成hh表示12小时制
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
		temp_str = sdf.format(dt);
		return temp_str;
	}

	public List<String> GetInsertSql(List<String[]> list) throws Exception {
		List<String> sqls = new ArrayList<String>();
		int index = 0;
		try {
			// 循环行Row
			int nsrsbhNo = -1;
			int nsrmcNo = -1;
			int zsxmNo = -1;
			int hyNo = -1;
			int zseNo = -1;
			int yskmNo = -1;
			int ysfpblNo = -1;
			int jdNo = -1;
			int sksxNo = -1;
			int blNo = -1;
			int hymlNo = -1;
			int qxjNo = -1;
			int zspmNo = -1;
			int zspmdmNo = -1;
			int hydl = -1;
			int hyzlNo = -1;
			int yskmdm = -1;
			int dzsphm = -1;
			int djxh = -1;
			for (int rowNum = 0; rowNum < list.size(); rowNum++) {
				String[] str = list.get(0);
				if (rowNum == 0) {
					for (int i = 0; i < str.length; i++) {
						String result = getValue(str[i]);
						if (result.indexOf("纳税人识别号") > -1) {
							nsrsbhNo = i;
						} else if (result.indexOf("纳税人名称") > -1) {
							nsrmcNo = i;
						} else if (result.indexOf("征收项目") > -1) {
							zsxmNo = i;
						} else if ((result.indexOf("行业") > -1 && result.indexOf("行业门类") < 0
								&& result.indexOf("行业大类") < 0 && result.indexOf("行业中类") < 0)) {
							hyNo = i;
						} else if (result.indexOf("行业门类") > -1) {
							hymlNo = i;
						} else if (result.indexOf("实缴金额") > -1) {
							zseNo = i;
						} else if (result.indexOf("预算科目") > -1) {
							yskmNo = i;
						} else if (result.indexOf("预算分配比例") > -1) {
							ysfpblNo = i;
						} else if (result.indexOf("街道") > -1) {
							jdNo = i;
						} else if (result.indexOf("税款属性") > -1) {
							sksxNo = i;
						} else if (result.indexOf("区县级比例") > -1) {
							blNo = i;
						} else if (result.indexOf("区县级") > -1 && result.indexOf("区县级比例") < 0) {
							qxjNo = i;
						} else if (result.indexOf("征收品目") > -1) {
							zspmNo = i;
						} else if (result.indexOf("征收品目代码") > -1) {
							zspmdmNo = i;
						} else if (result.indexOf("行业大类") > -1) {
							hydl = i;
						} else if (result.indexOf("行业中类") > -1) {
							hyzlNo = i;
						} else if (result.indexOf("预算科目代码") > -1) {
							yskmdm = i;
						} else if (result.indexOf("电子税票号码") > -1) {
							dzsphm = i;
						} else if (result.indexOf("登记序号") > -1) {
							djxh = i;
						}
					}
				} else {
					String[] record = list.get(rowNum);
					String zsxm_mc = "";
					String sksx = "";
					String zse = "";
					String yskm_mc = "";
					String ysfpbl_mc = "";
					String bl = "";
					String nsrsbh = "";
					String nsrmc = "";
					String hy_mc = "";
					String jd_mc = "";
					String hy = "";
					String qxj = "";
					String zspm = "";
					String zspmdm = "";
					String hydls = "";
					String hyzl = "";
					String yskmdms = "";
					String dzsphms = "";
					String djxhs = "";
					if (nsrsbhNo > -1) {
						nsrsbh = getValue(record[nsrsbhNo]);
					}
					if (nsrmcNo > -1) {
						nsrmc = getValue(record[nsrsbhNo]);
					}
					if (zsxmNo > -1) {
						zsxm_mc = getValue(record[zsxmNo]);
					}
					if (hyNo > -1) {
						hy = getValue(record[hyNo]);
					}
					if (hymlNo > -1) {
						hy_mc = getValue(record[hymlNo]);
					}
					if (zseNo > -1) {
						String cellValue = record[zseNo];
						if (cellValue != null && !cellValue.trim().equals("") && !cellValue.trim().equals("null")) {
							cellValue = cellValue.replaceAll(",", "");
						} else {
							cellValue = "0";
						}
						index = rowNum;
						zse = cellValue.replaceAll(",", "").replaceAll("\"", "");
						Double.parseDouble(zse);
					}
					if (yskmNo > -1) {
						yskm_mc = getValue(record[yskmNo]);
					}
					if (ysfpblNo > -1) {
						ysfpbl_mc = getValue(record[ysfpblNo]);
					}
					if (jdNo > -1) {
						jd_mc = getValue(record[jdNo]);
					}
					if (sksxNo > -1) {
						sksx = getValue(record[sksxNo]);
					}
					if (blNo > -1) {
						String cellValue = record[blNo];
						if (cellValue != null && !cellValue.trim().equals("") && !cellValue.trim().equals("null")) {
							cellValue = cellValue.replaceAll(",", "");
						} else {
							cellValue = "0";
						}
						bl = cellValue.replaceAll(",", "").replaceAll("\"", "");
						index = rowNum;
						Double.parseDouble(bl);
					}
					if (qxjNo > -1) {
						String cellValue = record[qxjNo];
						if (cellValue != null && !cellValue.trim().equals("") && !cellValue.trim().equals("null")) {
							cellValue = cellValue.replaceAll(",", "").replaceAll("\"", "");
						} else {
							cellValue = "0";
						}
						qxj = cellValue.replaceAll(",", "");
						index = rowNum;
						Double.parseDouble(qxj);
					}
					if (zspmNo > -1) {
						zspm = getValue(record[zspmNo]);
					}
					if (zspmdmNo > -1) {
						zspmdm = getValue(record[zspmdmNo]);
					}
					if (hydl > -1) {
						hydls = getValue(record[hydl]);
					}
					if (hyzlNo > -1) {
						hyzl = getValue(record[hyzlNo]);
					}
					if (yskmdm > -1) {
						yskmdms = getValue(record[yskmdm]);
					}
					if (dzsphm > -1) {
						dzsphms = getValue(record[dzsphm]);
					}
					if (djxh > -1) {
						djxhs = getValue(record[djxh]);
					}
					String istSql = "";
					if (zsxm_mc.indexOf("企业所得税") >= 0 && sksx.indexOf("代扣代缴税款") >= 0) {

						istSql = "insert into xwcs_gsdr_temp(id,nsrsbh,nsrmc, hy_mc, zse,zsxm_mc, yskm_mc, ysfpbl_mc,jd_mc, type,lr_sj,bl,HY,se,ZSPM,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS) "
								+ " select seq_xwcs_gsdr.nextval,'','预提企业所得税','',to_number('" + zse.replaceAll("\"", "")
								+ "'),'" + zsxm_mc.replaceAll("\"", "") + "','" + yskm_mc.replaceAll("\"", "") + "','"
								+ ysfpbl_mc.replaceAll("\"", "") + "','','0',sysdate,'" + bl.replaceAll("\"", "")
								+ "','" + hy.replaceAll("\"", "") + "',to_number('" + qxj.replaceAll("\"", "") + "'),'"
								+ zspm.replaceAll("\"", "") + "','" + zspmdm.replaceAll("\"", "") + "','"
								+ hydls.replaceAll("\"", "") + "','" + hyzl.replaceAll("\"", "") + "','"
								+ yskmdms.replaceAll("\"", "") + "','" + dzsphms.replaceAll("\"", "") + "','"
								+ djxhs.replaceAll("\"", "")
								+ "' from dual where not exists(select * from xwcs_gsdr_temp where  DZSPHM = '"
								+ dzsphms + "' );";
					} else {
						istSql = "insert into xwcs_gsdr_temp(id,nsrsbh, nsrmc, hy_mc, zse,zsxm_mc, yskm_mc, ysfpbl_mc,jd_mc, type,lr_sj,bl,HY,se,ZSPM,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS) "
								+ " select seq_xwcs_gsdr.nextval,'" + nsrsbh.replaceAll("\"", "") + "','"
								+ nsrmc.replaceAll("\"", "") + "','" + hy_mc.replaceAll("\"", "") + "',to_number('"
								+ zse + "'),'" + zsxm_mc.replaceAll("\"", "") + "','" + yskm_mc.replaceAll("\"", "")
								+ "','" + ysfpbl_mc.replaceAll("\"", "") + "','" + jd_mc.replaceAll("\"", "")
								+ "','0',sysdate,'" + bl.replaceAll("\"", "") + "','" + hy.replaceAll("\"", "")
								+ "',to_number('" + qxj.replaceAll("\"", "") + "'),'" + zspm.replaceAll("\"", "")
								+ "','" + zspmdm.replaceAll("\"", "") + "','" + hydls.replaceAll("\"", "") + "','"
								+ hyzl.replaceAll("\"", "") + "','" + yskmdms.replaceAll("\"", "") + "','"
								+ dzsphms.replaceAll("\"", "") + "','" + djxhs.replaceAll("\"", "")
								+ "' from dual where not exists(select * from xwcs_gsdr_temp where  DZSPHM = '"
								+ dzsphms.replaceAll("\"", "") + "' );";
					}
					sqls.add(istSql);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(index);
			// TODO: handle exception
		}

		return sqls;

	}

	public String queryInit(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");
			String sql = this.getSql("jd_query");
			List<Map<String, Object>> jdlist = this.getBs().query(sql);
			sql = "select * from dm_hyml";
			List<Map<String, Object>> hylist = this.getBs().query(sql);
			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
			String sqlzs = "select DISTINCT ZSXM  from SB_ZSXX where ZSXM is not null";
			List<Map<String, Object>> zslist = this.getBs().query(sqlzs);

			map.put("hylist", hylist);
			map.put("jdlist", jdlist);
			map.put("zslist", zslist);

			return this.toJson("000", "查询成功！", map);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

}
