package fast.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import fast.main.util.ExcelRead;
import fast.main.util.Super;
import net.sf.json.JSONArray;

/**
 * 企业税款属地变更(按单笔数据税款)
 * 
 * @author Administrator
 *
 */
public class qysksdbgpl extends Super {

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "sssjgl/qysksdbgpl";
		} catch (Exception e) {
			e.printStackTrace();
			return "sssjgl/qysksdbgpl";
		}
	}

	/**
	 * 初始化查询数据
	 * 
	 * @param rmap
	 * @return
	 */
	public String queryInit(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");
			String sql = this.getSql("jd_query");
			List<Map<String, Object>> jdlist = this.getBs().query(sql);
			sql = this.getSql("hy_query");
			List<Map<String, Object>> hylist = this.getBs().query(sql);

			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
			map.put("hylist", hylist);
			map.put("jdlist", jdlist);

			return this.toJson("000", "查询成功！", map);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	/**
	 * 企业汇总表
	 * 
	 * @param rmap
	 * @return
	 */
	public String querySdbg(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);

			String cxrq = getValue(this.getForm().get("cxrq")).replaceAll(" ", "");
			String qsrq = cxrq.substring(0, cxrq.lastIndexOf("-"));
			String jsrq = cxrq.substring(cxrq.lastIndexOf("-") + 1);
			String jd_dm = getValue(this.getForm().get("jd_dm"));
			if ("".equals(jd_dm) || jd_dm == null) {
				jd_dm = "%";
			}
			String nsrmc = getValue(this.getForm().get("qymc"));
			String zsxm_dm = getValue(this.getForm().get("zsxm_dm"));
			if ("".equals(zsxm_dm) || zsxm_dm == null) {
				zsxm_dm = "%";
			}
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
			String sql = "";

			if ("企业所得税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'企业所得税' zsxm,sum(qysds) zse,'"
						+ cxrq + "' rk_rq,qxj from sb_nsrxx s  " + "where nsrmc like'%" + nsrmc
						+ "%' and rk_rq<=to_date('" + qsrq + "','yyyyMM') and rk_rq>=to_date('" + jsrq
						+ "','yyyyMM')  and qysds!=0 " + "and jd_dm like '" + jd_dm + "' "
						+ "group by nsrmc,jd_dm,'企业所得税',rk_rq,qxj,xh";
				System.out.println();
				List<Map<String, Object>> qysds = this.getBs().query(sql);
				if (qysds != null) {
					list.add(qysds);
				}
			}
//			sql= "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'企业所得税' zsxm,sum(qysds) zse,'"+cxrq
//					+"' rk_rq,qxj from sb_nsrxx s  "
//					+ "where nsrmc='" + nsrmc + "' and to_char(rk_rq,'yyyyMM')='" + cxrq + "' and qysds!=0 "+"and jd_dm like '"+jd_dm +"' "
//					+ "group by nsrmc,jd_dm,'企业所得税',rk_rq,qxj";
//			System.out.println();
//			List<Map<String, Object>> qysds = this.getBs().query(sql);
//			if (qysds != null) {
//				list.add(qysds);
//			}
			if ("增值税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'增值税' zsxm,sum(zzs) zse,rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc like'%" + nsrmc + "%'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and zzs!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'增值税' ,rk_rq,qxj,xh";

				List<Map<String, Object>> zzs = this.getBs().query(sql);
				if (zzs != null) {
					list.add(zzs);
				}
			}
			if ("营改增增值税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营改增增值税' zsxm,sum(ygzzzs) zse,rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc like'%" + nsrmc + "%'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and ygzzzs!=0 "
						+ "and jd_dm like '" + jd_dm + "' " + "group by nsrmc,jd_dm,'营改增增值税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> ygzzzs = this.getBs().query(sql);
				if (ygzzzs != null) {
					list.add(ygzzzs);
				}
			}
			if ("营业税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营业税' zsxm,sum(yys) zse, rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc like'%" + nsrmc + "%'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and yys!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'营业税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> yys = this.getBs().query(sql);
				if (yys != null) {
					list.add(yys);
				}
			}
			if ("个人所得税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'个人所得税' zsxm,sum(grsds) zse,rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc like'%" + nsrmc + "%'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and grsds!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'个人所得税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> grsds = this.getBs().query(sql);
				if (grsds != null) {
					list.add(grsds);
				}
			}
			if ("车船税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'车船税' zsxm,sum(ccs) zse,rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc like'%" + nsrmc + "%' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and ccs!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'车船税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> ccs = this.getBs().query(sql);
				if (ccs != null) {
					list.add(ccs);
				}
			}
			if ("房产税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'房产税' zsxm,sum(fcs) zse,rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc like'%" + nsrmc + "%'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and fcs!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'房产税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> fcs = this.getBs().query(sql);
				if (fcs != null) {
					list.add(fcs);
				}
			}
			if ("印花税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'印花税' zsxm,sum(yhs) zse,rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc like'%" + nsrmc + "%'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and yhs!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'印花税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> yhs = this.getBs().query(sql);
				if (yhs != null) {
					list.add(yhs);
				}
			}
			if ("城市维护建设税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城市维护建设税' zsxm,sum(cswhjss) zse,rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc like'%" + nsrmc + "%' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and cswhjss!=0 "
						+ "and jd_dm like '" + jd_dm + "' " + "group by nsrmc,jd_dm,'城市维护建设税' ,rk_rq,qxj,xh";
				System.out.println(sql);
				List<Map<String, Object>> cswhjss = this.getBs().query(sql);
				if (cswhjss != null) {
					list.add(cswhjss);
				}
			}
			if ("地方教育附加".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'地方教育附加' zsxm,sum(dfjyfj) zse,rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc like'%" + nsrmc + "%'   and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and dfjyfj!=0 "
						+ "and jd_dm like '" + jd_dm + "' " + "group by nsrmc,jd_dm,'地方教育附加' ,rk_rq,qxj,xh";
				List<Map<String, Object>> dfjyfj = this.getBs().query(sql);
				if (dfjyfj != null) {
					list.add(dfjyfj);
				}
			}
			if ("教育附加".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'教育附加' zsxm,sum(jyfj) zse,rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc like'%" + nsrmc + "%' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and jyfj!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'教育附加' ,rk_rq,qxj,xh";
				List<Map<String, Object>> jyfj = this.getBs().query(sql);
				if (jyfj != null) {
					list.add(jyfj);
				}
			}
			if ("城镇土地使用税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城镇土地使用税' zsxm,sum(cztdsys) zse,rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc like'%" + nsrmc + "%' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')  and cztdsys!=0 "
						+ "and jd_dm like '" + jd_dm + "' " + "group by nsrmc,jd_dm,'城镇土地使用税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> cztdsys = this.getBs().query(sql);
				if (cztdsys != null) {
					list.add(cztdsys);
				}
			}
			if ("环保税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'环保税' zsxm,sum(hbs) zse,rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc like'%" + nsrmc + "%' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and hbs!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'环保税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> hbs = this.getBs().query(sql);
				if (hbs != null) {
					list.add(hbs);
				}
			}
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Map<String, Object>> rlists = list.get(i);
				for (int j = 0; j < rlists.size(); j++) {
					Map<String, Object> map = rlists.get(j);
					String zse = String.valueOf(map.get("ZSE"));
					if (zse != null && !zse.equals("0")) {
						lists.add(map);
					}

				}
			}
			return this.toJson("000", "查询成功！", lists);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	// 按单笔拆分
	/**
	 * 按单笔拆分多个街道
	 * 
	 * @param rmap
	 * @return 返回状态 000 拆分成功，刷新页面（009 拆分失败）
	 */
	public Object PLXG(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String xhs = getValue(this.getForm().get("xh"));
			String jddm = getValue(this.getForm().get("jddm"));
			String szStr = getValue(this.getForm().get("sz"));
			String[] szArr = szStr.split(",");
			JSONArray ja = JSONArray.fromObject(xhs);
			List<Map<String, Object>> params = (List<Map<String, Object>>) ja;
			System.out.println(params.size());
			double totalAmount = 0.0;
			Set<String> companyNames = new HashSet<>();
			
			String xhStr = "";
			List<Map<String, Object>> tempList = new ArrayList<>();
			for (int i = 0; i < params.size(); i++) {
				String sz = getValue(params.get(i).get("ZSXM"));
				
				if(Arrays.asList(szArr).contains(sz)) {
					tempList.add(params.get(i));
					xhStr += params.get(i).get("XH") + ",";
				}
			}
			if(xhStr.length() > 0) {
				xhStr = xhStr.substring(0, xhStr.length() - 1);
			}
			
			String queryMaxRkrq = "SELECT TO_CHAR(max(s.RK_RQ), 'yyyyMM') rkrq FROM SB_NSRXX s";
			System.out.println(queryMaxRkrq);
			Map<String, Object> maxRkrq = this.getBs().queryOne(queryMaxRkrq);
			
			for (int i = 0; i < tempList.size(); i++) {
				
				String querysql = "select * from sb_nsrxx s  " + "where s.xh='" + tempList.get(i).get("XH") + "' ";
				System.out.println(querysql);
				List<Map<String, Object>> querylist = this.getBs().query(querysql);
				
				Map<String, Object> map = querylist.get(0);
				
				if(StringUtils.isNotBlank(getValue(map.get("HFID")))) {
					continue;
				}
				
				companyNames.add(getValue(tempList.get(i).get("NSRMC")));
				
				String deletesql = "delete from sb_nsrxx_bf s where s.xh='" + tempList.get(i).get("XH") + "'";
				System.out.println(deletesql);
				this.getBs().delete(deletesql);
				
				String sql = "insert into sb_nsrxx_bf select * from sb_nsrxx s where s.xh='" + tempList.get(i).get("XH") + "'";
				System.out.println(sql);
				this.getBs().insert(sql);
				
				String updatesql = "update sb_nsrxx s set qxj='" + tempList.get(i).get("XH") 
						+ "', hfid='" + xhStr + "' where s.xh='" + tempList.get(i).get("XH") + "' ";
				System.out.println(updatesql);
				this.getBs().update(updatesql);
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
				Date date = new Date();
				
				String sz = getValue(tempList.get(i).get("ZSXM"));
				String szzd = "";
				if (sz.equals("增值税")) {
					szzd = "zzs";
				} else if (sz.equals("营改增增值税")) {
					szzd = "ygzzzs";
				} else if (sz.equals("营业税")) {
					szzd = "yys";
				} else if (sz.equals("企业所得税")) {
					szzd = "qysds";
				} else if (sz.equals("个人所得税")) {
					szzd = "grsds";
				} else if (sz.equals("车船税")) {
					szzd = "ccs";
				} else if (sz.equals("房产税")) {
					szzd = "fcs";
				} else if (sz.equals("印花税")) {
					szzd = "yhs";
				} else if (sz.equals("城市维护建设税")) {
					szzd = "cswhjss";
				} else if (sz.equals("地方教育附加")) {
					szzd = "dfjyfj";
				} else if (sz.equals("教育附加")) {
					szzd = "jyfj";
				} else if (sz.equals("城镇土地使用税")) {
					szzd = "cztdsys";
				} else if (sz.equals("环保税")) {
					szzd = "hbs";
				}
				
				Object dfzse = tempList.get(i).get("ZSE");
				Object bl = map.get("BL");
				BigDecimal money = getBigDecimal(dfzse).multiply(getBigDecimal(100));
				BigDecimal zse = money.divide(getBigDecimal(bl), 2, RoundingMode.HALF_UP);
				String allsz=",qysds,qysds_gs,zzs,ygzzzs,yys,grsds,ccs,fcs,yhs,cswhjss,dfjyfj,jyfj,cztdsys,hbs,qysds_ds";
				
				if (szzd.lastIndexOf("qysds") > -1) {
					totalAmount += Double.valueOf(getValue(tempList.get(i).get("ZSE")));
					
					szzd = "qysds,qysds_gs";
					String insertNewStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID"
							+allsz.replace(","+szzd, "")+ ") "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + maxRkrq.get("RKRQ")
							+ "','yyyyMM'),'" + jddm + "','" + getValue(map.get("HY_DM"))
							+ "','" + zse + "'" + ",'" + zse
							+"','"+zse+"','"+dfzse
							+ "','" + getValue(map.get("QYXZ")) + "','" + getValue(map.get("HHNSRMC")) + "','"
							+ getValue(map.get("LRRY_DM")) + "',to_date('" + formatter.format(date) + "','yyyyMM'),'"
							+ getValue(map.get("XSSR")) + "','" + getValue(map.get("GDS")) + "','"
							+ getValue(map.get("ZSXM")) + "','" + getValue(map.get("BL")) + "','" + getValue(tempList.get(i).get("XH")) + "','"
							+ getValue(map.get("ZSPMDM")) + "','" + getValue(map.get("HYDL")) + "','"
							+ getValue(map.get("HYZL")) + "','" + getValue(map.get("YSKMDM")) + "','"
							+ getValue(map.get("DZSPHM")) + "','" + getValue(map.get("DJXHS")) + "','"
							+ getValue(map.get("ZSPM")) + "','" + getValue(map.get("YZPZXH")) + "','"
							+ getValue(map.get("SKSX")) + "','" + xhStr +"','0','0','0','0','0','0','0','0','0','0','0','0','0')";
					System.out.println(insertNewStreet);
					this.getBs().insert(insertNewStreet);
					
					String insertOldStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID"
							+allsz.replace(","+szzd, "")+ ") "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + maxRkrq.get("RKRQ")
							+ "','yyyyMM'),'" + getValue(map.get("JD_DM")) + "','" + getValue(map.get("HY_DM"))
							+ "','-" + zse + "'" + ",'-" + zse
							+ "','-"+zse+"','-"+dfzse
							+ "','" + getValue(map.get("QYXZ")) + "','" + getValue(map.get("HHNSRMC")) + "','"
							+ getValue(map.get("LRRY_DM")) + "',to_date('" + formatter.format(date) + "','yyyyMM'),'"
							+ getValue(map.get("XSSR")) + "','" + getValue(map.get("GDS")) + "','"
							+ getValue(map.get("ZSXM")) + "','" + getValue(map.get("BL")) + "','" + getValue(tempList.get(i).get("XH")) + "','"
							+ getValue(map.get("ZSPMDM")) + "','" + getValue(map.get("HYDL")) + "','"
							+ getValue(map.get("HYZL")) + "','" + getValue(map.get("YSKMDM")) + "','"
							+ getValue(map.get("DZSPHM")) + "','" + getValue(map.get("DJXHS")) + "','"
							+ getValue(map.get("ZSPM")) + "','" + getValue(map.get("YZPZXH")) + "','"
							+ getValue(map.get("SKSX")) + "','" + xhStr +"','0','0','0','0','0','0','0','0','0','0','0','0','0')";
					System.out.println(insertOldStreet);
					this.getBs().insert(insertOldStreet);
				} else {
					totalAmount += Double.valueOf(getValue(tempList.get(i).get("ZSE")));
					String insertNewStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID"
							+allsz.replace(","+szzd, "")+ ") "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + maxRkrq.get("RKRQ")
							+ "','yyyyMM'),'" + jddm + "','" + getValue(map.get("HY_DM"))
							+ "','" + zse + "','" +zse+"','"
							+ dfzse+"','"+getValue(map.get("QYXZ")) + "','"
							+ getValue(map.get("HHNSRMC")) + "','" + getValue(map.get("LRRY_DM")) + "',to_date('"
							+ formatter.format(date) + "','yyyyMM'),'" + getValue(map.get("XSSR")) + "','"
							+ getValue(map.get("GDS")) + "','" + getValue(map.get("ZSXM")) + "','"
							+ getValue(map.get("BL")) + "','" + getValue(tempList.get(i).get("XH")) + "','" + getValue(map.get("ZSPMDM")) + "','"
							+ getValue(map.get("HYDL")) + "','" + getValue(map.get("HYZL")) + "','"
							+ getValue(map.get("YSKMDM")) + "','" + getValue(map.get("DZSPHM")) + "','"
							+ getValue(map.get("DJXHS")) + "','" + getValue(map.get("ZSPM")) + "','"
							+ getValue(map.get("YZPZXH")) + "','" + getValue(map.get("SKSX")) + "','" + xhStr +"','0','0','0','0','0','0','0','0','0','0','0','0','0','0')";
					System.out.println(insertNewStreet);
					this.getBs().insert(insertNewStreet);
					
					String insertOldStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID"
							+allsz.replace(","+szzd, "")+ ") "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + maxRkrq.get("RKRQ")
							+ "','yyyyMM'),'" + getValue(map.get("JD_DM")) + "','" + getValue(map.get("HY_DM"))
							+ "','-" + zse + "','-"+zse+"','-"+dfzse+"','"
							+ getValue(map.get("QYXZ")) + "','"
							+ getValue(map.get("HHNSRMC")) + "','" + getValue(map.get("LRRY_DM")) + "',to_date('"
							+ formatter.format(date) + "','yyyyMM'),'" + getValue(map.get("XSSR")) + "','"
							+ getValue(map.get("GDS")) + "','" + getValue(map.get("ZSXM")) + "','"
							+ getValue(map.get("BL")) + "','" + getValue(tempList.get(i).get("XH")) + "','" + getValue(map.get("ZSPMDM")) + "','"
							+ getValue(map.get("HYDL")) + "','" + getValue(map.get("HYZL")) + "','"
							+ getValue(map.get("YSKMDM")) + "','" + getValue(map.get("DZSPHM")) + "','"
							+ getValue(map.get("DJXHS")) + "','" + getValue(map.get("ZSPM")) + "','"
							+ getValue(map.get("YZPZXH")) + "','" + getValue(map.get("SKSX")) + "','" + xhStr +"','0','0','0','0','0','0','0','0','0','0','0','0','0','0')";
					System.out.println(insertOldStreet);
					this.getBs().insert(insertOldStreet);
				}
				
			}

			System.out.println();
			String companyNamesStr = Arrays.toString(companyNames.toArray());
			companyNamesStr = companyNamesStr.replace("[", "");
			companyNamesStr = companyNamesStr.replace("]", "");
			
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("companyCount", companyNames.size());
			resultMap.put("totalAmount", (new BigDecimal(totalAmount).setScale(2,BigDecimal.ROUND_HALF_UP)).toString());
			resultMap.put("params", companyNamesStr);
			resultMap.put("sz", szStr);
			resultMap.put("jd", jddm);

			return this.toJson("000", "查询成功！", resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	// 按单笔拆分
	/**
	 * 按单笔拆分多个街道
	 * 
	 * @param rmap
	 * @return 返回状态 000 拆分成功，刷新页面（009 拆分失败）
	 */
	public Object CF(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String str = getValue(this.getForm().get("str"));
			String sz = getValue(this.getForm().get("sz"));//sz-税种
			JSONArray ja = JSONArray.fromObject(str);
			String szzd = "";//szzd-税种字段
			if (sz.equals("增值税")) {
				szzd = "zzs";
			} else if (sz.equals("营改增增值税")) {
				szzd = "ygzzzs";
			} else if (sz.equals("营业税")) {
				szzd = "yys";
			} else if (sz.equals("企业所得税")) {
				szzd = "qysds";
			} else if (sz.equals("个人所得税")) {
				szzd = "grsds";
			} else if (sz.equals("车船税")) {
				szzd = "ccs";
			} else if (sz.equals("房产税")) {
				szzd = "fcs";
			} else if (sz.equals("印花税")) {
				szzd = "yhs";
			} else if (sz.equals("城市维护建设税")) {
				szzd = "cswhjss";
			} else if (sz.equals("地方教育附加")) {
				szzd = "dfjyfj";
			} else if (sz.equals("教育附加")) {
				szzd = "jyfj";
			} else if (sz.equals("城镇土地使用税")) {
				szzd = "cztdsys";
			} else if (sz.equals("环保税")) {
				szzd = "hbs";
			}
			List<Map<String, Object>> list = (List<Map<String, Object>>) ja;

			for (int i = 0; i < list.size(); i++) {
				String querysql = "select * from sb_nsrxx s  " + "where s.xh='"+ getValue(list.get(i).get("xh")) + "'";
				System.out.println(querysql);
				Map<String, Object> map = this.getBs().queryOne(querysql);
				String deletesql = "delete from sb_nsrxx_bf s  " + "where s.xh='"+ getValue(list.get(i).get("xh")) + "'";
				System.out.println(deletesql);
				this.getBs().delete(deletesql);
				String sql = "insert into sb_nsrxx_bf select * from sb_nsrxx s  " + "where s.xh='"+ getValue(list.get(i).get("xh")) + "'";
				System.out.println(sql);
				this.getBs().insert(sql);
				String updatesql = "update sb_nsrxx s set " + szzd + "='0',qxj='"+getValue(list.get(i).get("xh"))+"' where s.xh='"
						+ getValue(list.get(i).get("xh")) + "'";//qxj-区县级
				System.out.println(updatesql);
				if (sz.equals("企业所得税")) {
					updatesql = "update sb_nsrxx s set " + szzd + "='0',qysds_gs='0',qysds_ds='0',qxj='"+getValue(list.get(i).get("xh"))+"' where s.xh='"
							+ getValue(list.get(i).get("xh")) + "'";
				}
				System.out.println(updatesql);
				this.getBs().update(updatesql);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
				String rkrq = getValue(map.get("RK_RQ")).substring(0, getValue(map.get("RK_RQ")).lastIndexOf("."));
				Date date = new Date();
				System.out.println("这里是短点");
				System.out.println(rkrq);
				rkrq = rkrq.substring(0, rkrq.lastIndexOf("-")).replaceAll("-", "");

				if (szzd.lastIndexOf("qysds") > -1) {
					szzd = "qysds,qysds_gs";
					String insetsql = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX) "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + rkrq
							+ "','yyyyMM'),'" + getValue(list.get(i).get("jddm")) + "','" + getValue(map.get("HY_DM"))
							+ "','" + getValue(list.get(i).get("zse")) + "'" + ",'" + getValue(list.get(i).get("zse"))
							+ "','" + getValue(map.get("QYXZ")) + "','" + getValue(map.get("HHNSRMC")) + "','"
							+ getValue(map.get("LRRY_DM")) + "',to_date('" + formatter.format(date) + "','yyyyMM'),'"
							+ getValue(map.get("XSSR")) + "','" + getValue(map.get("GDS")) + "','"
							+ getValue(map.get("ZSXM")) + "','" + getValue(map.get("BL")) + "','"+getValue(list.get(i).get("xh"))+"','"
							+ getValue(map.get("ZSPMDM")) + "','" + getValue(map.get("HYDL")) + "','"
							+ getValue(map.get("HYZL")) + "','" + getValue(map.get("YSKMDM")) + "','"
							+ getValue(map.get("DZSPHM")) + "','" + getValue(map.get("DJXHS")) + "','"
							+ getValue(map.get("ZSPM")) + "','" + getValue(map.get("YZPZXH")) + "','"
							+ getValue(map.get("SKSX")) + "')";
					System.out.println(insetsql);
					this.getBs().insert(insetsql);
				} else {
					String insetsql = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX) "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + rkrq
							+ "','yyyyMM'),'" + getValue(list.get(i).get("jddm")) + "','" + getValue(map.get("HY_DM"))
							+ "','" + getValue(list.get(i).get("zse")) + "','" + getValue(map.get("QYXZ")) + "','"
							+ getValue(map.get("HHNSRMC")) + "','" + getValue(map.get("LRRY_DM")) + "',to_date('"
							+ formatter.format(date) + "','yyyyMM'),'" + getValue(map.get("XSSR")) + "','"
							+ getValue(map.get("GDS")) + "','" + getValue(map.get("ZSXM")) + "','"
							+ getValue(map.get("BL")) + "','"+getValue(list.get(i).get("xh"))+"','" + getValue(map.get("ZSPMDM")) + "','"
							+ getValue(map.get("HYDL")) + "','" + getValue(map.get("HYZL")) + "','"
							+ getValue(map.get("YSKMDM")) + "','" + getValue(map.get("DZSPHM")) + "','"
							+ getValue(map.get("DJXHS")) + "','" + getValue(map.get("ZSPM")) + "','"
							+ getValue(map.get("YZPZXH")) + "','" + getValue(map.get("SKSX")) + "')";
					System.out.println(insetsql);
					this.getBs().insert(insetsql);
				}

			}

			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	// 恢复原数据
	/**
	 * 将拆分的删除，恢复原数据
	 * 
	 * @param rmap
	 * @return 返回状态(000恢复成功,009恢复失败)
	 */
	public Object HF(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String xh = getValue(this.getForm().get("xh"));
			
			String querysql = "select to_char(s.hfid) HFID from sb_nsrxx s  " + "where s.xh='" + xh + "'";
			System.out.println(querysql);
			Map<String, Object> map = this.getBs().queryOne(querysql);
			
			String hfid = getValue(map.get("HFID")).toString();
			String[] hfidArr = hfid.split(",");
			for(int i = 0; i < hfidArr.length; i++) {
				
				String deletesql = "delete from sb_nsrxx s  " + "where to_char(s.hfid)='" + hfid + "' ";
				System.out.println(deletesql);
				this.getBs().delete(deletesql);
				
				String sql = "insert into sb_nsrxx select * from sb_nsrxx_bf s " + "where s.xh='" + hfidArr[i] + "' ";
				System.out.println(sql);
				this.getBs().insert(sql);
				
				String deletesql1 = "delete from sb_nsrxx_bf s  " + "where s.xh='" + hfidArr[i] + "' ";
				System.out.println(deletesql1);
				this.getBs().delete(deletesql1);
				
			}

			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	// 将数据从excel取出并存到临时表
	public String doInput(Map<String, Object> rmap) {
		initMap(rmap);
		String filename = getValue(this.getForm().get("filename"));
		InputStream is = null;
		try {
			return this.toJson("000", "查询成功！", insttemp(is, filename));
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	//导入
	private List<Map<String, Object>> insttemp(InputStream is, String filename) {
		List<Map<String, Object>> results = new ArrayList<>();
		try {
			is = new FileInputStream(new File(filename));
			Map<String, Integer> titles = new HashMap<String, Integer>();
			titles.put("社会信用代码（纳税人识别号）", -1);
			titles.put("纳税人名称", -1);
			titles.put("税种", -1);
			
			List<Map<String, String>> list = ExcelRead.pomExcel(filename, is, titles);
			for(Map<String, String> map : list) {
				String nsrmc = map.get("纳税人名称");
				String sz = map.get("税种");
				List<Map<String, Object>> tempList = getNsr(nsrmc,sz);
				results.addAll(tempList);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("刷新后的数量为："+results.size());
		return results;
	}
	
	public Map<String, String> getSz(String sz){
		String result = "";
		if(sz.equals("增值税")){
			result = "ZZS";
		}else if(sz.equals("营业税")){
			result = "YYS";
		}else if(sz.equals("个人所得税")){
			result = "GRSDS";
		}else if(sz.equals("房产税")){
			result = "FCS";
		}else if(sz.equals("印花税")){
			result = "YHS";
		}else if(sz.equals("车船税")){
			result = "CCS";
		}else if(sz.equals("企业所得税")){
			result = "QYSDS";
		}else if(sz.equals("营改增增值税")){
			result = "YGZZZS";
		}else if(sz.equals("城市维护建设税")){
			result = "CSWHJSS";
		}else if(sz.equals("地方教育附加")){
			result = "DFJYFJ";
		}else if(sz.equals("教育附加")){
			result = "JYFJ";
		}else if(sz.equals("城镇土地使用税")){
			result = "CZTDSYS";
		}else if(sz.equals("环保税")){
			result = "HBS";
		}
		Map<String, String> map = new HashMap<String,String>();
		map.put("税种中文", sz);
		map.put("税种简称", result);
		return map;
	}
	
	public List<Map<String, Object>> getNsr(String nsrmc,String sz) {
		List<Map<String, Object>> results = new ArrayList<>();
		
		String sql_rkrq_max = "SELECT to_char(max(RK_RQ),'yyyyMM') as rkrq FROM SB_NSRXX ORDER BY RK_RQ DESC";
		List<Map<String, Object>> result_rkrq = this.getBs().query(sql_rkrq_max);
		String dateCondition = "' and rk_rq = to_date('" + result_rkrq.get(0).get("RKRQ").toString() + "','yyyyMM') ";
		if(sz!=null && !sz.trim().equals("")){
			Map<String,String> map = getSz(sz);
			String sql_sz = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'"+map.get("税种中文")+"' zsxm,sum("+map.get("税种简称")+"*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq, qxj "
					+ " from sb_nsrxx s " + "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'企业所得税',rk_rq,qxj,xh";
			List<Map<String, Object>> qysds = this.getBs().query(sql_sz);
			if (qysds != null) {
				results.addAll(qysds);
			}
		}else{
			String sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'企业所得税' zsxm,sum(qysds*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq, qxj "
					+ " from sb_nsrxx s " + "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'企业所得税',rk_rq,qxj,xh";
			List<Map<String, Object>> qysds = this.getBs().query(sql);
			if (qysds != null) {
				results.addAll(qysds);
				
			}
			
			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'增值税' zsxm,sum(zzs*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from sb_nsrxx s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'增值税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> zzs = this.getBs().query(sql);
			if (zzs != null) {
				results.addAll(zzs);
			}
			
			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营改增增值税' zsxm,sum(ygzzzs*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from sb_nsrxx s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'营改增增值税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> ygzzzs = this.getBs().query(sql);
			if (ygzzzs != null) {
				results.addAll(ygzzzs);
			}
				
			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营业税' zsxm,sum(yys*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from sb_nsrxx s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'营业税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> yys = this.getBs().query(sql);
			if (yys != null) {
				results.addAll(yys);
			}
			
			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'个人所得税' zsxm,sum(grsds*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from sb_nsrxx s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'个人所得税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> grsds = this.getBs().query(sql);
			if (grsds != null) {
				results.addAll(grsds);
			}
			
			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'车船税' zsxm,sum(ccs*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from sb_nsrxx s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'车船税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> ccs = this.getBs().query(sql);
			if (ccs != null) {
				results.addAll(ccs);
			}
			
			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'房产税' zsxm,sum(fcs*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from sb_nsrxx s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'房产税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> fcs = this.getBs().query(sql);
			if (fcs != null) {
				results.addAll(fcs);
			}
			
			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'印花税' zsxm,sum(yhs*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from sb_nsrxx s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'印花税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> yhs = this.getBs().query(sql);
			if (yhs != null) {
				results.addAll(yhs);
			}		
			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城市维护建设税' zsxm,sum(cswhjss*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from sb_nsrxx s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'城市维护建设税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> cswhjss = this.getBs().query(sql);
			if (cswhjss != null) {
				results.addAll(cswhjss);
			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'地方教育附加' zsxm,sum(dfjyfj*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from sb_nsrxx s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'地方教育附加' ,rk_rq,qxj,xh";
			List<Map<String, Object>> dfjyfj = this.getBs().query(sql);
			if (dfjyfj != null) {
				results.addAll(dfjyfj);
			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'教育附加' zsxm,sum(jyfj*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from sb_nsrxx s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'教育附加' ,rk_rq,qxj,xh";
			List<Map<String, Object>> jyfj = this.getBs().query(sql);
			if (jyfj != null) {
				results.addAll(jyfj);
			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城镇土地使用税' zsxm,sum(cztdsys*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from sb_nsrxx s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'城镇土地使用税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> cztdsys = this.getBs().query(sql);
			if (cztdsys != null) {
				results.addAll(cztdsys);
			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'环保税' zsxm,sum(hbs*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from sb_nsrxx s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'环保税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> hbs = this.getBs().query(sql);
			if (hbs != null) {
				results.addAll(hbs);
			}
		}
		System.out.println(results);
		for (int j = 0; j < results.size(); j++) {
			Map<String, Object> result = results.get(j);
			String zse = String.valueOf(result.get("ZSE"));
			if (zse .equals("null")  || "0".equals(zse)||zse==null) {		
				results.remove(j--);
			}else{
				BigDecimal bd = getBigDecimal(result.get("ZSE")).setScale(2, RoundingMode.HALF_UP);
				DecimalFormat dft = new DecimalFormat("0.00");
				result.replace("ZSE", dft.format(bd));
			}
		}	
		return results;
	}
	
	public List<Map<String, Object>> getNsr1(String nsrmc) {
		List<Map<String, Object>> results = new ArrayList<>();
		
		String sql_rkrq_max = "SELECT to_char(max(RK_RQ),'yyyyMM') as rkrq FROM SB_NSRXX ORDER BY RK_RQ DESC";
		List<Map<String, Object>> result_rkrq = this.getBs().query(sql_rkrq_max);
		System.out.println(result_rkrq.get(0));
		String dateCondition = "' and rk_rq = to_date('" + result_rkrq.get(0).get("RKRQ").toString() + "','yyyyMM') ";
		String sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'企业所得税' zsxm,sum(qysds*bl/100) zse, rk_rq, qxj "
				+ " from sb_nsrxx s " + "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'企业所得税',rk_rq,qxj,xh";
		List<Map<String, Object>> qysds = this.getBs().query(sql);
		if (qysds != null) {
			results.addAll(qysds);
		}
		
		sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'增值税' zsxm,sum(zzs*bl/100) zse,rk_rq,qxj from sb_nsrxx s  "
				+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'增值税' ,rk_rq,qxj,xh";
		List<Map<String, Object>> zzs = this.getBs().query(sql);
		if (zzs != null) {
			results.addAll(zzs);
		}
		
		sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营改增增值税' zsxm,sum(ygzzzs*bl/100) zse,rk_rq,qxj from sb_nsrxx s  "
				+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'营改增增值税' ,rk_rq,qxj,xh";
		List<Map<String, Object>> ygzzzs = this.getBs().query(sql);
		if (ygzzzs != null) {
			results.addAll(ygzzzs);
		}
			
		sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营业税' zsxm,sum(yys*bl/100) zse, rk_rq,qxj from sb_nsrxx s  "
				+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'营业税' ,rk_rq,qxj,xh";
		List<Map<String, Object>> yys = this.getBs().query(sql);
		if (yys != null) {
			results.addAll(yys);
		}
		
		sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'个人所得税' zsxm,sum(grsds*bl/100) zse,rk_rq,qxj from sb_nsrxx s  "
				+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'个人所得税' ,rk_rq,qxj,xh";
		List<Map<String, Object>> grsds = this.getBs().query(sql);
		if (grsds != null) {
			results.addAll(grsds);
		}
		
		sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'车船税' zsxm,sum(ccs*bl/100) zse,rk_rq,qxj from sb_nsrxx s  "
				+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'车船税' ,rk_rq,qxj,xh";
		List<Map<String, Object>> ccs = this.getBs().query(sql);
		if (ccs != null) {
			results.addAll(ccs);
		}
		
		sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'房产税' zsxm,sum(fcs*bl/100) zse,rk_rq,qxj from sb_nsrxx s  "
				+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'房产税' ,rk_rq,qxj,xh";
		List<Map<String, Object>> fcs = this.getBs().query(sql);
		if (fcs != null) {
			results.addAll(fcs);
		}
		
		sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'印花税' zsxm,sum(yhs*bl/100) zse,rk_rq,qxj from sb_nsrxx s  "
				+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'印花税' ,rk_rq,qxj,xh";
		List<Map<String, Object>> yhs = this.getBs().query(sql);
		if (yhs != null) {
			results.addAll(yhs);
		}
		
		sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城市维护建设税' zsxm,sum(cswhjss*bl/100) zse,rk_rq,qxj from sb_nsrxx s  "
				+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'城市维护建设税' ,rk_rq,qxj,xh";
		System.out.println(sql);
		List<Map<String, Object>> cswhjss = this.getBs().query(sql);
		if (cswhjss != null) {
			results.addAll(cswhjss);
		}

		sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'地方教育附加' zsxm,sum(dfjyfj*bl/100) zse,rk_rq,qxj from sb_nsrxx s  "
				+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'地方教育附加' ,rk_rq,qxj,xh";
		List<Map<String, Object>> dfjyfj = this.getBs().query(sql);
		if (dfjyfj != null) {
			results.addAll(dfjyfj);
		}

		sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'教育附加' zsxm,sum(jyfj*bl/100) zse,rk_rq,qxj from sb_nsrxx s  "
				+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'教育附加' ,rk_rq,qxj,xh";
		List<Map<String, Object>> jyfj = this.getBs().query(sql);
		if (jyfj != null) {
			results.addAll(jyfj);
		}

		sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城镇土地使用税' zsxm,sum(cztdsys*bl/100) zse,rk_rq,qxj from sb_nsrxx s  "
				+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'城镇土地使用税' ,rk_rq,qxj,xh";
		List<Map<String, Object>> cztdsys = this.getBs().query(sql);
		if (cztdsys != null) {
			results.addAll(cztdsys);
		}

		sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'环保税' zsxm,sum(hbs*bl/100) zse,rk_rq,qxj from sb_nsrxx s  "
				+ "where nsrmc = '" + nsrmc + dateCondition + " group by nsrmc,jd_dm,'环保税' ,rk_rq,qxj,xh";
		List<Map<String, Object>> hbs = this.getBs().query(sql);
		if (hbs != null) {
			results.addAll(hbs);
		}
		
		for (int j = 0; j < results.size(); j++) {
			Map<String, Object> result = results.get(j);
			String zse = String.valueOf(result.get("ZSE"));
			if (zse == null || "0".equals(zse)) {
				results.remove(j--);
			}
		}
		
		return results;
	}

	public boolean MBCZ(String paramNsr, String paramSz, String jddm) {
		
		try {
//			List<Map<String, Object>> params = (List<Map<String, Object>>) JSONArray.fromObject(paramNsr);
			String[] nsrArr = paramNsr.split(",");
			
			List<Map<String, Object>> params = new ArrayList<>();
			for(int i = 0; i < nsrArr.length; i++) {
				params.addAll(getNsr1(nsrArr[i]));
			}
			
			String[] szArr = paramSz.split(",");
			
			String xhStr = "";
			List<Map<String, Object>> tempList = new ArrayList<>();
			for (int i = 0; i < params.size(); i++) {
				String sz = getValue(params.get(i).get("ZSXM"));
				
				if(Arrays.asList(szArr).contains(sz)) {
					tempList.add(params.get(i));
					xhStr += params.get(i).get("XH") + ",";
				}
			}
			xhStr = xhStr.substring(0, xhStr.length() - 1);
			for (int i = 0; i < tempList.size(); i++) {
				
				String querysql = "select * from sb_nsrxx s  " + "where s.xh='" + tempList.get(i).get("XH") + "' ";
				System.out.println(querysql);
				List<Map<String, Object>> querylist = this.getBs().query(querysql);
				
				String deletesql = "delete from sb_nsrxx_bf s where s.xh='" + tempList.get(i).get("XH") + "'";
				System.out.println(deletesql);
				this.getBs().delete(deletesql);
				
				String sql = "insert into sb_nsrxx_bf select * from sb_nsrxx s where s.xh='" + tempList.get(i).get("XH") + "'";
				System.out.println(sql);
				this.getBs().insert(sql);
				
				String updatesql = "update sb_nsrxx s set qxj='" + tempList.get(i).get("XH") 
						+ "', hfid='" + xhStr + "' where s.xh='" + tempList.get(i).get("XH") + "' ";
				System.out.println(updatesql);
				this.getBs().update(updatesql);

				Map<String, Object> map = querylist.get(0);
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
				Date date = new Date();
				
				String sz = getValue(tempList.get(i).get("ZSXM"));
				String szzd = "";
				if (sz.equals("增值税")) {
					szzd = "zzs";
				} else if (sz.equals("营改增增值税")) {
					szzd = "ygzzzs";
				} else if (sz.equals("营业税")) {
					szzd = "yys";
				} else if (sz.equals("企业所得税")) {
					szzd = "qysds";
				} else if (sz.equals("个人所得税")) {
					szzd = "grsds";
				} else if (sz.equals("车船税")) {
					szzd = "ccs";
				} else if (sz.equals("房产税")) {
					szzd = "fcs";
				} else if (sz.equals("印花税")) {
					szzd = "yhs";
				} else if (sz.equals("城市维护建设税")) {
					szzd = "cswhjss";
				} else if (sz.equals("地方教育附加")) {
					szzd = "dfjyfj";
				} else if (sz.equals("教育附加")) {
					szzd = "jyfj";
				} else if (sz.equals("城镇土地使用税")) {
					szzd = "cztdsys";
				} else if (sz.equals("环保税")) {
					szzd = "hbs";
				}
				
				if (szzd.lastIndexOf("qysds") > -1) {
					
					szzd = "qysds,qysds_gs";
					String insertNewStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID) "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + formatter.format(date)
							+ "','yyyyMM'),'" + jddm + "','" + getValue(map.get("HY_DM"))
							+ "','" + getValue(tempList.get(i).get("ZSE")) + "'" + ",'" + getValue(tempList.get(i).get("ZSE"))
							+ "','" + getValue(map.get("QYXZ")) + "','" + getValue(map.get("HHNSRMC")) + "','"
							+ getValue(map.get("LRRY_DM")) + "',to_date('" + formatter.format(date) + "','yyyyMM'),'"
							+ getValue(map.get("XSSR")) + "','" + getValue(map.get("GDS")) + "','"
							+ getValue(map.get("ZSXM")) + "','" + getValue(map.get("BL")) + "','" + getValue(tempList.get(i).get("XH")) + "','"
							+ getValue(map.get("ZSPMDM")) + "','" + getValue(map.get("HYDL")) + "','"
							+ getValue(map.get("HYZL")) + "','" + getValue(map.get("YSKMDM")) + "','"
							+ getValue(map.get("DZSPHM")) + "','" + getValue(map.get("DJXHS")) + "','"
							+ getValue(map.get("ZSPM")) + "','" + getValue(map.get("YZPZXH")) + "','"
							+ getValue(map.get("SKSX")) + "','" + xhStr + "')";
					System.out.println(insertNewStreet);
					this.getBs().insert(insertNewStreet);
					
					String insertOldStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID) "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + formatter.format(date)
							+ "','yyyyMM'),'" + getValue(map.get("jddm")) + "','" + getValue(map.get("HY_DM"))
							+ "','-" + getValue(tempList.get(i).get("ZSE")) + "'" + ",'-" + getValue(tempList.get(i).get("ZSE"))
							+ "','" + getValue(map.get("QYXZ")) + "','" + getValue(map.get("HHNSRMC")) + "','"
							+ getValue(map.get("LRRY_DM")) + "',to_date('" + formatter.format(date) + "','yyyyMM'),'"
							+ getValue(map.get("XSSR")) + "','" + getValue(map.get("GDS")) + "','"
							+ getValue(map.get("ZSXM")) + "','" + getValue(map.get("BL")) + "','" + getValue(tempList.get(i).get("XH")) + "','"
							+ getValue(map.get("ZSPMDM")) + "','" + getValue(map.get("HYDL")) + "','"
							+ getValue(map.get("HYZL")) + "','" + getValue(map.get("YSKMDM")) + "','"
							+ getValue(map.get("DZSPHM")) + "','" + getValue(map.get("DJXHS")) + "','"
							+ getValue(map.get("ZSPM")) + "','" + getValue(map.get("YZPZXH")) + "','"
							+ getValue(map.get("SKSX")) + "','" + xhStr + "')";
					System.out.println(insertOldStreet);
					this.getBs().insert(insertOldStreet);
				} else {
					String insertNewStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID) "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + formatter.format(date)
							+ "','yyyyMM'),'" + jddm + "','" + getValue(map.get("HY_DM"))
							+ "','" + getValue(tempList.get(i).get("ZSE")) + "','" + getValue(map.get("QYXZ")) + "','"
							+ getValue(map.get("HHNSRMC")) + "','" + getValue(map.get("LRRY_DM")) + "',to_date('"
							+ formatter.format(date) + "','yyyyMM'),'" + getValue(map.get("XSSR")) + "','"
							+ getValue(map.get("GDS")) + "','" + getValue(map.get("ZSXM")) + "','"
							+ getValue(map.get("BL")) + "','" + getValue(tempList.get(i).get("XH")) + "','" + getValue(map.get("ZSPMDM")) + "','"
							+ getValue(map.get("HYDL")) + "','" + getValue(map.get("HYZL")) + "','"
							+ getValue(map.get("YSKMDM")) + "','" + getValue(map.get("DZSPHM")) + "','"
							+ getValue(map.get("DJXHS")) + "','" + getValue(map.get("ZSPM")) + "','"
							+ getValue(map.get("YZPZXH")) + "','" + getValue(map.get("SKSX")) + "','" + xhStr + "')";
					System.out.println(insertNewStreet);
					this.getBs().insert(insertNewStreet);
					
					String insertOldStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID) "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + formatter.format(date)
							+ "','yyyyMM'),'" + getValue(map.get("JD_DM")) + "','" + getValue(map.get("HY_DM"))
							+ "','-" + getValue(tempList.get(i).get("ZSE")) + "','" + getValue(map.get("QYXZ")) + "','"
							+ getValue(map.get("HHNSRMC")) + "','" + getValue(map.get("LRRY_DM")) + "',to_date('"
							+ formatter.format(date) + "','yyyyMM'),'" + getValue(map.get("XSSR")) + "','"
							+ getValue(map.get("GDS")) + "','" + getValue(map.get("ZSXM")) + "','"
							+ getValue(map.get("BL")) + "','" + getValue(tempList.get(i).get("XH")) + "','" + getValue(map.get("ZSPMDM")) + "','"
							+ getValue(map.get("HYDL")) + "','" + getValue(map.get("HYZL")) + "','"
							+ getValue(map.get("YSKMDM")) + "','" + getValue(map.get("DZSPHM")) + "','"
							+ getValue(map.get("DJXHS")) + "','" + getValue(map.get("ZSPM")) + "','"
							+ getValue(map.get("YZPZXH")) + "','" + getValue(map.get("SKSX")) + "','" + xhStr + "')";
					System.out.println(insertOldStreet);
					this.getBs().insert(insertOldStreet);
				}
				
			}
			
			return true;
		} catch (Exception e) {
			return false;
		}

	}
	
	// 保存模板
	/**
	 * 将拆分的删除，恢复原数据
	 * 
	 * @param rmap
	 * @return 返回状态(000恢复成功,009恢复失败)
	 */
	public Object bcmb(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String mbmc = getValue(this.getForm().get("mbmc"));
			String params = getValue(this.getForm().get("params"));
			String sz = getValue(this.getForm().get("sz"));
			String jd = getValue(this.getForm().get("jd"));
			
//			List<Map<String, Object>> nsrs = (List<Map<String, Object>>) JSONArray.fromObject(params);
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			
			String sql = "insert into fast_mb(MBMC, NSRMC, SZ, JD, STATUS, BS, HFID, CREATETIME) values('" 
					+ mbmc + "', '" + params + "' ,'" + sz + "' ,'" + jd + "', '1', '3', '', to_date('" + formatter.format(new Date()) + "', 'yyyy-MM-dd'))";
			//System.out.println(sql);
			this.getBs().insert(sql);
				

			return this.toJson("000", "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "保存异常！");
		}
	}
	
	public static BigDecimal getBigDecimal( Object value ) {  
        BigDecimal ret = null;  
        if( value != null ) {  
            if( value instanceof BigDecimal ) {  
                ret = (BigDecimal) value;  
            } else if( value instanceof String ) {  
                ret = new BigDecimal( (String) value );  
            } else if( value instanceof BigInteger ) {  
                ret = new BigDecimal( (BigInteger) value );  
            } else if( value instanceof Number ) {  
                ret = new BigDecimal( ((Number)value).doubleValue() );  
            } else {  
                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");  
            }  
        }  
        return ret;  
    }  
}
