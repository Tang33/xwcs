package fast.app;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


import fast.main.util.Super;
import net.sf.json.JSONArray;

/**
 * 企业税款属地变更(按单笔数据税款)
 * 
 * @author Administrator
 *
 */
public class qysksdbgdb extends Super {

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "sssjgl/qysksdbgdb";
		} catch (Exception e) {
			e.printStackTrace();
			return "sssjgl/qysksdbgdb";
		}
	}
	
	/**
	 * 查询企业名称
	 * 
	 * @param rmap
	 * @return
	 */
	public String queryQymc(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String qymc = getValue(this.getForm().get("qymc"));
			String nsrsbh = getValue(this.getForm().get("nsrsbh"));
			String cxrq = getValue(this.getForm().get("cxrq")).replaceAll(" ", "");
			String qsrq = cxrq.substring(0, cxrq.lastIndexOf("-"));
			String jsrq = cxrq.substring(cxrq.lastIndexOf("-") + 1);
			String pageNo = getValue(this.getForm().get("page"));;
			String pageSize = getValue(this.getForm().get("limit"));
			
//			if(StringUtils.isNotBlank(nsrsbh)) {
//				querysql = "select  nsrmc from sb_nsrxx s  " + "where s.nsrsbh like '%" + nsrsbh + "%' and rk_rq >= to_date('" + qsrq 
//						+ "','yyyyMM') and rk_rq <= to_date('" + jsrq + "','yyyyMM') group by nsrmc";
//			} else {
//				//distinct nsrmc
//				querysql = "select  nsrmc from sb_nsrxx s  " + "where s.nsrmc like '%" + qymc + "%' and rk_rq >= to_date('" + qsrq 
//						+ "','yyyyMM') and rk_rq <= to_date('" + jsrq + "','yyyyMM') group by nsrmc";
//			}
			
			String querysql = "select distinct nsrmc from sb_nsrxx s where 1 = 1 and (-s.nsrsbh like '%' || ? || '%'-) and (-s.nsrmc like '%' || ? ||'%'-) and rk_rq >= to_date(?,'yyyyMM') and rk_rq <= to_date(?,'yyyyMM')";
			querysql = this.getSql2(querysql, new Object[] {nsrsbh,qymc,qsrq,jsrq});
			int queryCount = this.getBs().queryCount(querysql);
			System.out.println(querysql);
			
			List<Map<String, Object>> list = this.getBs().query(querysql,pageNo,pageSize);
			
			return this.toJson("000", "查询成功！", list,queryCount);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
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
			
			String queryMaxRkrq = "SELECT TO_CHAR(max(s.RK_RQ), 'yyyyMM') rkrq FROM SB_NSRXX s";
			System.out.println(queryMaxRkrq);
			Map<String, Object> maxRkrq = this.getBs().queryOne(queryMaxRkrq);

			Map<String, Object> map = new HashMap<>();
			map.put("hylist", hylist);
			map.put("jdlist", jdlist);
			map.put("maxRkrq", maxRkrq.get("RKRQ"));

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
			String nsrsbh = getValue(this.getForm().get("nsrsbh"));
			String zsxm_dm = getValue(this.getForm().get("zsxm_dm"));
			if ("".equals(zsxm_dm) || zsxm_dm == null) {
				zsxm_dm = "%";
			}
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
			String sql = "";

			if ("企业所得税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'企业所得税' zsxm,sum(qysds*bl/100) zse,"
						+ " TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj from sb_nsrxx s  " + "where nsrmc = '" + nsrmc
						+ "' and rk_rq>=to_date('" + qsrq + "','yyyyMM') and rk_rq<=to_date('" + jsrq
						+ "','yyyyMM')  and qysds!=0 " + "and jd_dm like '" + jd_dm + "' "
						+ "group by nsrmc,jd_dm,'企业所得税',rk_rq,qxj,xh";
				System.out.println("----------------");
				System.out.println(sql);
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
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'增值税' zsxm,sum(zzs*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and zzs!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'增值税' ,rk_rq,qxj,xh";

				List<Map<String, Object>> zzs = this.getBs().query(sql);
				if (zzs != null) {
					list.add(zzs);
				}
			}
			if ("营改增增值税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营改增增值税' zsxm,sum(ygzzzs*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and ygzzzs!=0 "
						+ "and jd_dm like '" + jd_dm + "' " + "group by nsrmc,jd_dm,'营改增增值税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> ygzzzs = this.getBs().query(sql);
				if (ygzzzs != null) {
					list.add(ygzzzs);
				}
			}
			if ("营业税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营业税' zsxm,sum(yys*bl/100) zse, TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and yys!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'营业税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> yys = this.getBs().query(sql);
				if (yys != null) {
					list.add(yys);
				}
			}
			if ("个人所得税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'个人所得税' zsxm,sum(grsds*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and grsds!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'个人所得税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> grsds = this.getBs().query(sql);
				if (grsds != null) {
					list.add(grsds);
				}
			}
			if ("车船税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'车船税' zsxm,sum(ccs*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and ccs!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'车船税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> ccs = this.getBs().query(sql);
				if (ccs != null) {
					list.add(ccs);
				}
			}
			if ("房产税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'房产税' zsxm,sum(fcs*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and fcs!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'房产税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> fcs = this.getBs().query(sql);
				if (fcs != null) {
					list.add(fcs);
				}
			}
			if ("印花税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'印花税' zsxm,sum(yhs*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and yhs!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'印花税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> yhs = this.getBs().query(sql);
				if (yhs != null) {
					list.add(yhs);
				}
			}
			if ("城市维护建设税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城市维护建设税' zsxm,sum(cswhjss*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and cswhjss!=0 "
						+ "and jd_dm like '" + jd_dm + "' " + "group by nsrmc,jd_dm,'城市维护建设税' ,rk_rq,qxj,xh";
				System.out.println(sql);
				List<Map<String, Object>> cswhjss = this.getBs().query(sql);
				if (cswhjss != null) {
					list.add(cswhjss);
				}
			}
			if ("地方教育附加".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'地方教育附加' zsxm,sum(dfjyfj*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'   and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and dfjyfj!=0 "
						+ "and jd_dm like '" + jd_dm + "' " + "group by nsrmc,jd_dm,'地方教育附加' ,rk_rq,qxj,xh";
				List<Map<String, Object>> dfjyfj = this.getBs().query(sql);
				if (dfjyfj != null) {
					list.add(dfjyfj);
				}
			}
			if ("教育附加".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'教育附加' zsxm,sum(jyfj*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and jyfj!=0 " + "and jd_dm like '"
						+ jd_dm + "' " + "group by nsrmc,jd_dm,'教育附加' ,rk_rq,qxj,xh";
				List<Map<String, Object>> jyfj = this.getBs().query(sql);
				if (jyfj != null) {
					list.add(jyfj);
				}
			}
			if ("城镇土地使用税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城镇土地使用税' zsxm,sum(cztdsys*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')  and cztdsys!=0 "
						+ "and jd_dm like '" + jd_dm + "' " + "group by nsrmc,jd_dm,'城镇土地使用税' ,rk_rq,qxj,xh";
				List<Map<String, Object>> cztdsys = this.getBs().query(sql);
				if (cztdsys != null) {
					list.add(cztdsys);
				}
			}
			if ("环保税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'环保税' zsxm,sum(hbs*bl/100) zse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
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
					if (zse != null && !zse.equals("0")&& zse!="null") {
						lists.add(map);
					}

				}
			}
			System.out.println(sql);
			System.out.println(lists);
			
			for (int j = 0; j < lists.size(); j++) {
					Map<String, Object> result = lists.get(j);	
					BigDecimal bd = getBigDecimal(result.get("ZSE")).setScale(2, RoundingMode.HALF_UP);
					DecimalFormat dft = new DecimalFormat("0.00");
					result.replace("ZSE", dft.format(bd));
				
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
	public Object plxg(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String xhs = getValue(this.getForm().get("xh"));
			String jddm = getValue(this.getForm().get("jddm"));
			JSONArray ja = JSONArray.fromObject(xhs);

			List<Map<String, Object>> list = (List<Map<String, Object>>) ja;
			for (int i = 0; i < list.size(); i++) {
				String sz = getValue(list.get(i).get("sz"));
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
				String querysql = "select * from sb_nsrxx s  " + "where s.xh='" + list.get(i).get("xh") + "' ";
				System.out.println(querysql);
				List<Map<String, Object>> querylist = this.getBs().query(querysql);
				String deletesql = "delete from sb_nsrxx_bf s  " + "where s.xh='" + list.get(i).get("xh") + "' ";
				System.out.println(deletesql);
				this.getBs().delete(deletesql);
				String sql = "insert into sb_nsrxx_bf select * from sb_nsrxx s  " + "where s.xh='"
						+ list.get(i).get("xh") + "' ";
				System.out.println(sql);
				this.getBs().insert(sql);

				String updatesql = "update sb_nsrxx s set " + szzd + "='0',qxj='1',hfid='"+ list.get(i).get("xh")+"' where s.xh='" + list.get(i).get("xh")
						+ "' ";
				System.out.println(updatesql);
				if (sz.equals("企业所得税")) {
					updatesql = "update sb_nsrxx s set " + szzd + "='0',qysds_gs='0',qysds_ds='0',qxj='"+ list.get(i).get("xh")+"' where s.xh='"
							+ list.get(i).get("xh") + "' ";
				}
				System.out.println(updatesql);
				this.getBs().update(updatesql);
				Map<String, Object> map = querylist.get(0);
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
							+ "','yyyyMM'),'" + jddm + "','" + getValue(map.get("HY_DM")) + "','"
							+ getValue(map.get("QYSDS")) + "'" + ",'" + getValue(map.get("QYSDS_GS")) + "','"
							+ getValue(map.get("QYXZ")) + "','" + getValue(map.get("HHNSRMC")) + "','"
							+ getValue(map.get("LRRY_DM")) + "',to_date('" + formatter.format(date) + "','yyyyMM'),'"
							+ getValue(map.get("XSSR")) + "','" + getValue(map.get("GDS")) + "','"
							+ getValue(map.get("ZSXM")) + "','" + getValue(map.get("BL")) + "','"+ list.get(i).get("xh")+"','"
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
							+ "','yyyyMM'),'" + jddm + "','" + getValue(map.get("HY_DM")) + "','"
							+ getValue(map.get(szzd.toUpperCase())) + "'" + ",'" + getValue(map.get("QYXZ")) + "','"
							+ getValue(map.get("HHNSRMC")) + "','" + getValue(map.get("LRRY_DM")) + "',to_date('"
							+ formatter.format(date) + "','yyyyMM'),'" + getValue(map.get("XSSR")) + "','"
							+ getValue(map.get("GDS")) + "','" + getValue(map.get("ZSXM")) + "','"
							+ getValue(map.get("BL")) + "','"+ list.get(i).get("xh")+"','" + getValue(map.get("ZSPMDM")) + "','"
							+ getValue(map.get("HYDL")) + "','" + getValue(map.get("HYZL")) + "','"
							+ getValue(map.get("YSKMDM")) + "','" + getValue(map.get("DZSPHM")) + "','"
							+ getValue(map.get("DJXHS")) + "','" + getValue(map.get("ZSPM")) + "','"
							+ getValue(map.get("YZPZXH")) + "','" + getValue(map.get("SKSX")) + "')";
					System.out.println(insetsql);
					this.getBs().insert(insetsql);
				}

			}

			System.out.println();

			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	// 按多笔拆分
	/**
	 * 按多笔拆分多个街道和金额
	 * 
	 * @param rmap
	 * @return 返回状态 000 拆分成功，刷新页面（009 拆分失败）
	 */
	public Object PLCF(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			List<Map<String, Object>> params = (List<Map<String, Object>>) JSONArray.fromObject(getValue(this.getForm().get("str")));
			String sz = getValue(this.getForm().get("sz"));//sz-税种
			String total = getValue(this.getForm().get("total"));//总额
			String xhStr = getValue(this.getForm().get("xh"));
			String[] xhArr = xhStr.split(",");
			
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
			
			String queryMaxRkrq = "SELECT TO_CHAR(max(s.RK_RQ), 'yyyyMM') rkrq FROM SB_NSRXX s";
			System.out.println(queryMaxRkrq);
			Map<String, Object> maxRkrq = this.getBs().queryOne(queryMaxRkrq);
			
			Map<String, Object> map = new HashMap<>();
			String allsz=",qysds,qysds_gs,zzs,ygzzzs,yys,grsds,ccs,fcs,yhs,cswhjss,dfjyfj,jyfj,cztdsys,hbs,qysds_ds";
			for(int i = 0; i < xhArr.length; i++) {
			
				String querysql = "select * from sb_nsrxx s where s.xh='" + xhArr[i] + "'";
				System.out.println(querysql);
				map = this.getBs().queryOne(querysql);
				
				String deletesql = "delete from sb_nsrxx_bf s where s.xh='" + xhArr[i] + "'";
				System.out.println(deletesql);
				this.getBs().delete(deletesql);
				
				String sql = "insert into sb_nsrxx_bf select * from sb_nsrxx s where s.xh='" + xhArr[i] + "'";
				System.out.println(sql);
				this.getBs().insert(sql);
				
				String updatesql = "update sb_nsrxx s set qxj='1', hfid='" + xhStr + "' where s.xh='" + xhArr[i] + "' ";
				System.out.println(updatesql);
				this.getBs().update(updatesql);
				
			}
			
			for (Map<String, Object> param : params) {
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
				Date date = new Date();
				Object dfzse = param.get("zse");
				Object bl = map.get("BL");
				BigDecimal money = getBigDecimal(dfzse).multiply(getBigDecimal(100));
				BigDecimal zse = money.divide(getBigDecimal(bl), 2, RoundingMode.HALF_UP);
				
				if (szzd.lastIndexOf("qysds") > -1) {
					szzd = "qysds,qysds_gs";
					String insertNewStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID"
							+ allsz.replace(","+szzd, "") +") "
							
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + maxRkrq.get("RKRQ")
							+ "','yyyyMM'),'" + getValue(param.get("jddm")) + "','" + getValue(map.get("HY_DM"))
							+ "','" + zse + "'" + ",'" + zse+"','"+zse+"','"+dfzse
							+ "','" + getValue(map.get("QYXZ")) + "','" + getValue(map.get("HHNSRMC")) + "','"
							+ getValue(map.get("LRRY_DM")) + "',to_date('" + formatter.format(date) + "','yyyyMM'),'"
							+ getValue(map.get("XSSR")) + "','" + getValue(map.get("GDS")) + "','"
							+ getValue(map.get("ZSXM")) + "','" + getValue(map.get("BL")) + "','" + xhArr[0] + "','"
							+ getValue(map.get("ZSPMDM")) + "','" + getValue(map.get("HYDL")) + "','"
							+ getValue(map.get("HYZL")) + "','" + getValue(map.get("YSKMDM")) + "','"
							+ getValue(map.get("DZSPHM")) + "','" + getValue(map.get("DJXHS")) + "','"
							+ getValue(map.get("ZSPM")) + "','" + getValue(map.get("YZPZXH")) + "','"
							+ getValue(map.get("SKSX")) + "','" + xhStr  +"','0','0','0','0','0','0','0','0','0','0','0','0','0')";
					System.out.println(insertNewStreet);
					this.getBs().insert(insertNewStreet);
					
					String insertOldStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID"
							+ allsz.replace(","+szzd, "") +") "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + maxRkrq.get("RKRQ")
							+ "','yyyyMM'),'" + getValue(param.get("jddm")) + "','" + getValue(map.get("HY_DM"))
							+ "','-" + zse + "'" + ",'-" + zse+"','-"+zse+"','-"+dfzse
							+ "','" + getValue(map.get("QYXZ")) + "','" + getValue(map.get("HHNSRMC")) + "','"
							+ getValue(map.get("LRRY_DM")) + "',to_date('" + formatter.format(date) + "','yyyyMM'),'"
							+ getValue(map.get("XSSR")) + "','" + getValue(map.get("GDS")) + "','"
							+ getValue(map.get("ZSXM")) + "','" + getValue(map.get("BL")) + "','" + xhArr[0] + "','"
							+ getValue(map.get("ZSPMDM")) + "','" + getValue(map.get("HYDL")) + "','"
							+ getValue(map.get("HYZL")) + "','" + getValue(map.get("YSKMDM")) + "','"
							+ getValue(map.get("DZSPHM")) + "','" + getValue(map.get("DJXHS")) + "','"
							+ getValue(map.get("ZSPM")) + "','" + getValue(map.get("YZPZXH")) + "','"
							+ getValue(map.get("SKSX")) + "','" + xhStr +"','0','0','0','0','0','0','0','0','0','0','0','0','0')";
					System.out.println(insertOldStreet);
					this.getBs().insert(insertOldStreet);
				} else {
					String insertNewStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID"
							+ allsz.replace(","+szzd, "") +") "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + maxRkrq.get("RKRQ")
							+ "','yyyyMM'),'" + getValue(param.get("jddm")) + "','" + getValue(map.get("HY_DM"))
							+ "','" + zse+"','"+zse+"','"+dfzse 
							+ "','" + getValue(map.get("QYXZ")) + "','"+ getValue(map.get("HHNSRMC")) + "','" + getValue(map.get("LRRY_DM")) + "',to_date('"
							+ formatter.format(date) + "','yyyyMM'),'" + getValue(map.get("XSSR")) + "','"
							+ getValue(map.get("GDS")) + "','" + getValue(map.get("ZSXM")) + "','"
							+ getValue(map.get("BL")) + "','" + xhArr[0] + "','" + getValue(map.get("ZSPMDM")) + "','"
							+ getValue(map.get("HYDL")) + "','" + getValue(map.get("HYZL")) + "','"
							+ getValue(map.get("YSKMDM")) + "','" + getValue(map.get("DZSPHM")) + "','"
							+ getValue(map.get("DJXHS")) + "','" + getValue(map.get("ZSPM")) + "','"
							+ getValue(map.get("YZPZXH")) + "','" + getValue(map.get("SKSX")) + "','" + xhStr +"','0','0','0','0','0','0','0','0','0','0','0','0','0','0')";
					System.out.println(insertNewStreet);
					this.getBs().insert(insertNewStreet);
					
					String insertOldStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID"
							+ allsz.replace(","+szzd, "") +") "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + maxRkrq.get("RKRQ")
							+ "','yyyyMM'),'" + getValue(map.get("JD_DM")) + "','" + getValue(map.get("HY_DM"))
							+ "','-" + zse + "','-"+zse+"','-"+dfzse + "','"+getValue(map.get("QYXZ")) + "','"
							+ getValue(map.get("HHNSRMC")) + "','" + getValue(map.get("LRRY_DM")) + "',to_date('"
							+ formatter.format(date) + "','yyyyMM'),'" + getValue(map.get("XSSR")) + "','"
							+ getValue(map.get("GDS")) + "','" + getValue(map.get("ZSXM")) + "','"
							+ getValue(map.get("BL")) + "','" + xhArr[0] + "','" + getValue(map.get("ZSPMDM")) + "','"
							+ getValue(map.get("HYDL")) + "','" + getValue(map.get("HYZL")) + "','"
							+ getValue(map.get("YSKMDM")) + "','" + getValue(map.get("DZSPHM")) + "','"
							+ getValue(map.get("DJXHS")) + "','" + getValue(map.get("ZSPM")) + "','"
							+ getValue(map.get("YZPZXH")) + "','" + getValue(map.get("SKSX")) + "','"+ xhStr +"','0','0','0','0','0','0','0','0','0','0','0','0','0','0')";
					System.out.println(insertOldStreet);
					this.getBs().insert(insertOldStreet);
				}
			}

			return this.toJson("000", "查询成功！");
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
			String xh = getValue(this.getForm().get("xh"));//sz-税种
			List<Map<String, Object>> params = (List<Map<String, Object>>) JSONArray.fromObject(getValue(this.getForm().get("str")));
			String sz = getValue(this.getForm().get("sz"));//sz-税种
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
			
			String queryMaxRkrq = "SELECT TO_CHAR(max(s.RK_RQ), 'yyyyMM') rkrq FROM SB_NSRXX s";
			System.out.println(queryMaxRkrq);
			Map<String, Object> maxRkrq = this.getBs().queryOne(queryMaxRkrq);
			
			String querysql = "select * from sb_nsrxx s  " + "where s.xh='"+ xh + "'";
			System.out.println(querysql);
			Map<String, Object> map = this.getBs().queryOne(querysql);
			
			String deletesql = "delete from sb_nsrxx_bf s  " + "where s.xh='"+ xh + "'";
			System.out.println(deletesql);
			this.getBs().delete(deletesql);
			
			String sql = "insert into sb_nsrxx_bf select * from sb_nsrxx s  " + "where s.xh='"+ xh + "'";
			System.out.println(sql);
			this.getBs().insert(sql);
			
			String updatesql = "update sb_nsrxx s set qxj='1', hfid='" + xh + "' where s.xh='" + xh + "' ";
			System.out.println(updatesql);
			this.getBs().update(updatesql);
			
			String allsz=",qysds,qysds_gs,zzs,ygzzzs,yys,grsds,ccs,fcs,yhs,cswhjss,dfjyfj,jyfj,cztdsys,hbs,qysds_ds";
			for (Map<String, Object> param : params) {

				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
				Date date = new Date();
				Object dfzse = param.get("zse");
				System.out.println(dfzse);
				Object bl = map.get("BL");
				BigDecimal money = getBigDecimal(dfzse).multiply(getBigDecimal(100));
				BigDecimal zse = money.divide(getBigDecimal(bl), 2, RoundingMode.HALF_UP);
				System.out.println(zse);

				if (szzd.lastIndexOf("qysds") > -1) {
					szzd = "qysds,qysds_gs";
					
					String insertNewStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID"
							+allsz.replace(","+szzd, "")+ ") "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + maxRkrq.get("RKRQ")
							+ "','yyyyMM'),'" + getValue(param.get("jddm")) + "','" + getValue(map.get("HY_DM"))
							+ "','" + zse + "'" + ",'" + zse+"','"+zse+"','"+dfzse
							+ "','" + getValue(map.get("QYXZ")) + "','" + getValue(map.get("HHNSRMC")) + "','"
							+ getValue(map.get("LRRY_DM")) + "',to_date('" + formatter.format(date) + "','yyyyMM'),'"
							+ getValue(map.get("XSSR")) + "','" + getValue(map.get("GDS")) + "','"
							+ getValue(map.get("ZSXM")) + "','" + getValue(map.get("BL")) + "','" + xh + "','"
							+ getValue(map.get("ZSPMDM")) + "','" + getValue(map.get("HYDL")) + "','"
							+ getValue(map.get("HYZL")) + "','" + getValue(map.get("YSKMDM")) + "','"
							+ getValue(map.get("DZSPHM")) + "','" + getValue(map.get("DJXHS")) + "','"
							+ getValue(map.get("ZSPM")) + "','" + getValue(map.get("YZPZXH")) + "','"
							+ getValue(map.get("SKSX")) + "'," + xh +",'0','0','0','0','0','0','0','0','0','0','0','0','0')";
					System.out.println(insertNewStreet);
					this.getBs().insert(insertNewStreet);
					
					String insertOldStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID"
							+allsz.replace(","+szzd, "") +") "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + maxRkrq.get("RKRQ")
							+ "','yyyyMM'),'" + getValue(map.get("JD_DM")) + "','" + getValue(map.get("HY_DM"))
							+ "','-" + zse + "'" + ",'-" + zse+"','-"+zse+"','-"+dfzse
							+ "','" + getValue(map.get("QYXZ")) + "','" + getValue(map.get("HHNSRMC")) + "','"
							+ getValue(map.get("LRRY_DM")) + "',to_date('" + formatter.format(date) + "','yyyyMM'),'"
							+ getValue(map.get("XSSR")) + "','" + getValue(map.get("GDS")) + "','"
							+ getValue(map.get("ZSXM")) + "','" + getValue(map.get("BL")) + "','" + xh + "','"
							+ getValue(map.get("ZSPMDM")) + "','" + getValue(map.get("HYDL")) + "','"
							+ getValue(map.get("HYZL")) + "','" + getValue(map.get("YSKMDM")) + "','"
							+ getValue(map.get("DZSPHM")) + "','" + getValue(map.get("DJXHS")) + "','"
							+ getValue(map.get("ZSPM")) + "','" + getValue(map.get("YZPZXH")) + "','"
							+ getValue(map.get("SKSX")) + "'," + xh  +",'0','0','0','0','0','0','0','0','0','0','0','0','0')";
					System.out.println(insertOldStreet);
					this.getBs().insert(insertOldStreet);
				} else {
					String insertNewStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID"
							+ allsz.replace(","+szzd, "") +") "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + maxRkrq.get("RKRQ")
							+ "','yyyyMM'),'" + getValue(param.get("jddm")) + "','" + getValue(map.get("HY_DM"))
							+ "','" + zse + "','"+zse+"','"+dfzse+"','" + getValue(map.get("QYXZ")) + "','"
							+ getValue(map.get("HHNSRMC")) + "','" + getValue(map.get("LRRY_DM")) + "',to_date('"
							+ formatter.format(date) + "','yyyyMM'),'" + getValue(map.get("XSSR")) + "','"
							+ getValue(map.get("GDS")) + "','" + getValue(map.get("ZSXM")) + "','"
							+ getValue(map.get("BL")) + "','" + xh + "','" + getValue(map.get("ZSPMDM")) + "','"
							+ getValue(map.get("HYDL")) + "','" + getValue(map.get("HYZL")) + "','"
							+ getValue(map.get("YSKMDM")) + "','" + getValue(map.get("DZSPHM")) + "','"
							+ getValue(map.get("DJXHS")) + "','" + getValue(map.get("ZSPM")) + "','"
							+ getValue(map.get("YZPZXH")) + "','" + getValue(map.get("SKSX")) + "'," + xh  +",'0','0','0','0','0','0','0','0','0','0','0','0','0','0')";
					System.out.println(insertNewStreet);
					this.getBs().insert(insertNewStreet);
					
					String insertOldStreet = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,HFID"
							+ allsz.replace(","+szzd, "") +") "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + maxRkrq.get("RKRQ")
							+ "','yyyyMM'),'" + getValue(map.get("JD_DM")) + "','" + getValue(map.get("HY_DM"))
							+ "','-" +zse + "','-"+zse+"','-"+dfzse+"','" +  getValue(map.get("QYXZ")) + "','"
							+ getValue(map.get("HHNSRMC")) + "','" + getValue(map.get("LRRY_DM")) + "',to_date('"
							+ formatter.format(date) + "','yyyyMM'),'" + getValue(map.get("XSSR")) + "','"
							+ getValue(map.get("GDS")) + "','" + getValue(map.get("ZSXM")) + "','"
							+ getValue(map.get("BL")) + "','" + xh + "','" + getValue(map.get("ZSPMDM")) + "','"
							+ getValue(map.get("HYDL")) + "','" + getValue(map.get("HYZL")) + "','"
							+ getValue(map.get("YSKMDM")) + "','" + getValue(map.get("DZSPHM")) + "','"
							+ getValue(map.get("DJXHS")) + "','" + getValue(map.get("ZSPM")) + "','"
							+ getValue(map.get("YZPZXH")) + "','" + getValue(map.get("SKSX")) + "'," + xh +",'0','0','0','0','0','0','0','0','0','0','0','0','0','0')";
					System.out.println(insertOldStreet);
					this.getBs().insert(insertOldStreet);
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
