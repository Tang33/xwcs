package fast.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;
import net.sf.json.JSONArray;


/**
 * 企业税款属地变更(按税种)
 * @author Administrator
 *
 */
public class qysksdbgsz extends Super {
	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "sssjgl/qysksdbgsz";
		}catch(Exception e){
			e.printStackTrace();
			return "sssjgl/qysksdbgsz";
		}
	}

	
	/**
	 * 初始化查询数据
	 * @param rmap
	 * @return
	 */
	public String queryInit(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");
			String sql=this.getSql("jd_query");
			List<Map<String, Object>> jdlist=this.getBs().query(sql);
			sql=this.getSql("hy_query");
			List<Map<String, Object>> hylist=this.getBs().query(sql);
			
			Map<String, List<Map<String, Object>>> map=new HashMap<String, List<Map<String,Object>>>();
			map.put("hylist", hylist);
			map.put("jdlist", jdlist);

			
			return this.toJson("000", "查询成功！",map);
		}catch(Exception e){
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

			String cxrq = getValue(this.getForm().get("cxrq"));
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
			String sql ="";
			
			if ("企业所得税".equals(zsxm_dm)||"%".equals(zsxm_dm)) {
				sql= "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'企业所得税' zsxm,sum(qysds) zse,'"+cxrq
						+"' rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc='" + nsrmc + "' and to_char(rk_rq,'yyyyMM')='" + cxrq + "' and qysds!=0 "+"and jd_dm like '"+jd_dm +"' "
						+ "group by nsrmc,jd_dm,'企业所得税',rk_rq,qxj";
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
			if ("增值税".equals(zsxm_dm)||"%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'增值税' zsxm,sum(zzs) zse,'"+cxrq 
						+ "' rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc='" + nsrmc + "' and to_char(rk_rq,'yyyyMM')='" + cxrq + "' and zzs!=0 "+"and jd_dm like '"+jd_dm +"' "
						+ "group by nsrmc,jd_dm,'增值税' ,rk_rq,qxj";
				
				List<Map<String, Object>> zzs = this.getBs().query(sql);
				if (zzs != null) {
					list.add(zzs);
				}
			}
			if ("营改增增值税".equals(zsxm_dm)||"%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营改增增值税' zsxm,sum(ygzzzs) zse,'"+cxrq 
						+"' rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc='" + nsrmc + "' and to_char(rk_rq,'yyyyMM')='" + cxrq + "' and ygzzzs!=0 "+"and jd_dm like '"+jd_dm +"' "
						+ "group by nsrmc,jd_dm,'营改增增值税' ,rk_rq,qxj";
				List<Map<String, Object>> ygzzzs = this.getBs().query(sql);
				if (ygzzzs != null) {
					list.add(ygzzzs);
				}
			}
			if ("营业税".equals(zsxm_dm)||"%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营业税' zsxm,sum(yys) zse,'"+cxrq 
						+"' rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc='" + nsrmc + "' and to_char(rk_rq,'yyyyMM')='" + cxrq + "' and yys!=0 "+"and jd_dm like '"+jd_dm +"' "
						+ "group by nsrmc,jd_dm,'营业税' ,rk_rq,qxj";
				List<Map<String, Object>> yys = this.getBs().query(sql);
				if (yys != null) {
					list.add(yys);
				}
			}
			if ("个人所得税".equals(zsxm_dm)||"%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'个人所得税' zsxm,sum(grsds) zse,'"+cxrq 
						+"' rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc='" + nsrmc + "' and to_char(rk_rq,'yyyyMM')='" + cxrq + "' and grsds!=0 "+"and jd_dm like '"+jd_dm +"' "
						+ "group by nsrmc,jd_dm,'个人所得税' ,rk_rq,qxj";
				List<Map<String, Object>> grsds = this.getBs().query(sql);
				if (grsds != null) {
					list.add(grsds);
				}
			}
			if ("车船税".equals(zsxm_dm)||"%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'车船税' zsxm,sum(ccs) zse,'"+cxrq 
						+"' rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc='" + nsrmc + "' and to_char(rk_rq,'yyyyMM')='" + cxrq + "' and ccs!=0 "+"and jd_dm like '"+jd_dm +"' "
						+ "group by nsrmc,jd_dm,'车船税' ,rk_rq,qxj";
				List<Map<String, Object>> ccs = this.getBs().query(sql);
				if (ccs != null) {
					list.add(ccs);
				}
			}
			if ("房产税".equals(zsxm_dm)||"%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'房产税' zsxm,sum(fcs) zse,'"+cxrq 
						+"' rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc='" + nsrmc + "' and to_char(rk_rq,'yyyyMM')='" + cxrq + "' and ygzzzs!=0 "+"and jd_dm like '"+jd_dm +"' "
						+ "group by nsrmc,jd_dm,'房产税' ,rk_rq,qxj";
				List<Map<String, Object>> fcs = this.getBs().query(sql);
				if (fcs != null) {
					list.add(fcs);
				}
			}
			if ("印花税".equals(zsxm_dm)||"%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'印花税' zsxm,sum(yhs) zse,'"+cxrq 
						+"' rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc='" + nsrmc + "' and to_char(rk_rq,'yyyyMM')='" + cxrq + "' and ygzzzs!=0 "+"and jd_dm like '"+jd_dm +"' "
						+ "group by nsrmc,jd_dm,'印花税' ,rk_rq,qxj";
				List<Map<String, Object>> yhs = this.getBs().query(sql);
				if (yhs != null) {
					list.add(yhs);
				}
			}
			if ("城市维护建设税".equals(zsxm_dm)||"%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城市维护建设税' zsxm,sum(cswhjss) zse,'"+cxrq 
						+"' rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc='" + nsrmc + "' and to_char(rk_rq,'yyyyMM')='" + cxrq + "' and ygzzzs!=0 "+"and jd_dm like '"+jd_dm +"' "
						+ "group by nsrmc,jd_dm,'城市维护建设税' ,rk_rq,qxj";
				System.out.println(sql);
				List<Map<String, Object>> cswhjss = this.getBs().query(sql);
				if (cswhjss != null) {
					list.add(cswhjss);
				}
			}
			if ("地方教育附加".equals(zsxm_dm)||"%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'地方教育附加' zsxm,sum(dfjyfj) zse,'"+cxrq 
						+"' rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc='" + nsrmc + "' and to_char(rk_rq,'yyyyMM')='" + cxrq + "' and ygzzzs!=0 "+"and jd_dm like '"+jd_dm +"' "
						+ "group by nsrmc,jd_dm,'地方教育附加' ,rk_rq,qxj";
				List<Map<String, Object>> dfjyfj = this.getBs().query(sql);
				if (dfjyfj != null) {
					list.add(dfjyfj);
				}
			}
			if ("教育附加".equals(zsxm_dm)||"%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'教育附加' zsxm,sum(jyfj) zse,'"+cxrq 
						+"' rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc='" + nsrmc + "' and to_char(rk_rq,'yyyyMM')='" + cxrq + "' and ygzzzs!=0 "+"and jd_dm like '"+jd_dm +"' "
						+ "group by nsrmc,jd_dm,'教育附加' ,rk_rq,qxj";
				List<Map<String, Object>> jyfj = this.getBs().query(sql);
				if (jyfj != null) {
					list.add(jyfj);
				}
			}
			if ("城镇土地使用税".equals(zsxm_dm)||"%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城镇土地使用税' zsxm,sum(cztdsys) zse,'"+cxrq 
						+"' rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc='" + nsrmc + "' and to_char(rk_rq,'yyyyMM')='" + cxrq + "' and ygzzzs!=0 "+"and jd_dm like '"+jd_dm +"' "
						+ "group by nsrmc,jd_dm,'城镇土地使用税' ,rk_rq,qxj";
				List<Map<String, Object>> cztdsys = this.getBs().query(sql);
				if (cztdsys != null) {
					list.add(cztdsys);
				}
			}
			if ("环保税".equals(zsxm_dm)||"%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'环保税' zsxm,sum(hbs) zse,'"+cxrq 
						+"' rk_rq,qxj from sb_nsrxx s  "
						+ "where nsrmc='" + nsrmc + "' and to_char(rk_rq,'yyyyMM')='" + cxrq + "' and ygzzzs!=0 "+"and jd_dm like '"+jd_dm +"' "
						+ "group by nsrmc,jd_dm,'环保税' ,rk_rq,qxj";
				List<Map<String, Object>> hbs = this.getBs().query(sql);
				if (hbs != null) {
					list.add(hbs);
				}
			}
			List<Map<String, Object>> lists=new ArrayList<Map<String,Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Map<String, Object>> rlists=list.get(i);
				for (int j = 0; j < rlists.size(); j++) {
					Map<String, Object> map=rlists.get(j);
					System.out.println(map);
					lists.add(map);
				}
			}
			return this.toJson("000", "查询成功！", lists);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	// 导出excel
	public Object CF(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String str = getValue(this.getForm().get("str"));
			String jd = getValue(this.getForm().get("jd"));
			String sz = getValue(this.getForm().get("sz"));
			String nsrmc = getValue(this.getForm().get("nsrmc"));
			String cxrq = getValue(this.getForm().get("cxrq"));
			JSONArray ja = JSONArray.fromObject(str);
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
			List<Map<String, Object>> list = (List<Map<String, Object>>) ja;
			String querysql = "select * from sb_nsrxx s  " + "where s.nsrmc='" + nsrmc
					+ "' and to_char(s.rk_rq,'yyyyMM')='" + cxrq + "' and s." + szzd + "!=0 ";
			System.out.println(querysql);
			
			List<Map<String, Object>> querylist = this.getBs().query(querysql);
			String deletesql = "delete from sb_nsrxx_bf s  " + "where s.nsrmc='" + nsrmc
					+ "' and to_char(s.rk_rq,'yyyyMM')='" + cxrq + "' and s." + szzd + "!=0 ";
			this.getBs().delete(deletesql);
			
			String sql = "insert into sb_nsrxx_bf select * from sb_nsrxx s  " + "where s.nsrmc='" + nsrmc
					+ "' and to_char(s.rk_rq,'yyyyMM')='" + cxrq + "' and s." + szzd + "!=0 ";
			this.getBs().insert(sql);

			String updatesql = "update sb_nsrxx s set " + szzd + "='0',qxj='1' where s.nsrmc='" + nsrmc
					+ "' and to_char(s.rk_rq,'yyyyMM')='" + cxrq + "' and s." + szzd + "!=0 ";
			this.getBs().update(updatesql);
			
			for (int i = 0; i < list.size(); i++) {
				String szdm="";
				if (list.get(i).get("szdm").equals("增值税")) {
					szdm = "zzs";
				} else if (list.get(i).get("szdm").equals("营改增增值税")) {
					szdm = "ygzzzs";
				} else if (list.get(i).get("szdm").equals("营业税")) {
					szdm = "yys";
				} else if (list.get(i).get("szdm").equals("企业所得税")) {
					szdm = "qysds";
				} else if (list.get(i).get("szdm").equals("个人所得税")) {
					szdm = "grsds";
				} else if (list.get(i).get("szdm").equals("车船税")) {
					szdm = "ccs";
				} else if (list.get(i).get("szdm").equals("房产税")) {
					szdm = "fcs";
				} else if (list.get(i).get("szdm").equals("印花税")) {
					szdm = "yhs";
				} else if (list.get(i).get("szdm").equals("城市维护建设税")) {
					szdm = "cswhjss";
				} else if (list.get(i).get("szdm").equals("地方教育附加")) {
					szdm = "dfjyfj";
				} else if (list.get(i).get("szdm").equals("教育附加")) {
					szdm = "jyfj";
				} else if (list.get(i).get("szdm").equals("城镇土地使用税")) {
					szdm = "cztdsys";
				} else if (list.get(i).get("szdm").equals("环保税")) {
					szdm = "hbs";
				}
				Map<String, Object> map = querylist.get(0);
				String rkrq=getValue(map.get("RK_RQ"));
				rkrq=rkrq.substring(0,rkrq.lastIndexOf("-")).replaceAll("-","");
					String insetsql = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szdm
							+ ",QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX) "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('"
							+ rkrq + "','yyyyMM'),'" + getValue(map.get("JD_DM")) + "','"
							+ getValue(map.get("HY_DM")) + "','" + getValue(list.get(i).get("zse")) + "'" + ",'"
							+ getValue(map.get("QYXZ")) + "','" + getValue(map.get("HHNSRMC")) + "','"
							+ getValue(map.get("LRRY_DM")) + "',to_date('" + cxrq + "','yyyyMM'),'"
							+ getValue(map.get("XSSR")) + "','" + getValue(map.get("GDS")) + "','"
							+ getValue(map.get("ZSXM")) + "','" + getValue(map.get("BL")) + "','1','"
							+ getValue(map.get("ZSPMDM")) + "','" + getValue(map.get("HYDL")) + "','"
							+ getValue(map.get("HYZL")) + "','" + getValue(map.get("YSKMDM")) + "','"
							+ getValue(map.get("DZSPHM")) + "','" + getValue(map.get("DJXHS")) + "','"
							+ getValue(map.get("ZSPM")) + "','" + getValue(map.get("YZPZXH")) + "','"
							+ getValue(map.get("SKSX")) + "')";
					this.getBs().insert(insetsql);

			}
			System.out.println();

			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	
	
	/**
	 * 将拆分的删除，恢复原数据
	 * @param rmap
	 * @return 返回状态(000恢复成功,009恢复失败)
	 */
	public Object HF(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String sz = getValue(this.getForm().get("sz"));
			String jd = getValue(this.getForm().get("jd"));
			String nsrmc = getValue(this.getForm().get("nsrmc"));
			String cxrq = getValue(this.getForm().get("cxrq"));
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
			
			String jddmSql = "select JD_DM from dm_jd WHERE JD_MC='"+jd+"'";
			List<Map<String, Object>> jdlist=this.getBs().query(jddmSql);
			String jddm=jdlist.get(0).get("JD_DM").toString();
			
			String querysql = "select * from sb_nsrxx_bf sf  " + "where sf.nsrmc='" + nsrmc
					+ "' and to_char(sf.rk_rq,'yyyyMM')='" + cxrq + "' and sf.JD_DM = '"+jddm+"' ";
			List<Map<String, Object>> querylist = this.getBs().query(querysql);
			
			
			String deletesql = "delete from sb_nsrxx s  " + "where s.nsrmc='" + nsrmc
					+ "' and to_char(s.rk_rq,'yyyyMM')='" + cxrq + "' and s.QXJ='1' ";
			this.getBs().delete(deletesql);
			
			String sql = "insert into sb_nsrxx select * from sb_nsrxx_bf s  " + "where s.nsrmc='" + nsrmc
					+ "' and to_char(s.rk_rq,'yyyyMM')='" + cxrq + "' and s.JD_DM='" + jddm+"'";
			this.getBs().insert(sql);
			
			
//				Map<String, Object> map = querylist.get(0);
//				String rkrq=getValue(map.get("RK_RQ"));
//				System.out.println("这里是短点");
//				System.out.println(rkrq);
//				rkrq=rkrq.substring(0,rkrq.lastIndexOf("-")).replaceAll("-","");
//					String insetsql = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," 
//							+ "ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS"
//							+ ",QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
//							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,ZSE,DFZSE) "
//							+ "values('" + getValue(map.get("XH")) + "','" + getValue(map.get("NSRMC")) + "',to_date('"
//							+ rkrq + "','yyyyMM'),'" + getValue(map.get("JD_DM")) + "','"
//							+ getValue(map.get("HY_DM")) + "','" 
//							+ getValue(map.get("ZZS")) + "','"
//							+ getValue(map.get("YYS")) + "','"
//							+ getValue(map.get("GRSDS")) + "','"
//							+ getValue(map.get("FCS")) + "','"
//							+ getValue(map.get("YHS")) + "','"
//							+ getValue(map.get("CCS")) + "','"
//							+ getValue(map.get("QYSDS_GS")) + "','"
//							+ getValue(map.get("QYSDS_DS")) + "','"
//							+ getValue(map.get("QYSDS")) + "','"
//							+ getValue(map.get("QYXZ")) + "','" + getValue(map.get("HHNSRMC")) + "','"
//							+ getValue(map.get("LRRY_DM")) + "',to_date('" + cxrq + "','yyyyMM'),'"
//							+ getValue(map.get("XSSR")) + "','" + getValue(map.get("GDS")) + "','"
//							+ getValue(map.get("ZSXM")) + "','" + getValue(map.get("BL")) + "','','"
//							+ getValue(map.get("ZSPMDM")) + "','" + getValue(map.get("HYDL")) + "','"
//							+ getValue(map.get("HYZL")) + "','" + getValue(map.get("YSKMDM")) + "','"
//							+ getValue(map.get("DZSPHM")) + "','" + getValue(map.get("DJXHS")) + "','"
//							+ getValue(map.get("ZSPM")) + "','" + getValue(map.get("YZPZXH")) + "','"
//							+ getValue(map.get("SKSX")) + "','"+ getValue(map.get("ZSE")) +"','"
//							+ getValue(map.get("DFZSE"))+"')";
//					this.getBs().insert(insetsql);
					
					String deletesql1 = "delete from sb_nsrxx_bf s  " + "where s.nsrmc='" + nsrmc
							+ "' and to_char(s.rk_rq,'yyyyMM')='" + cxrq + "' and s.JD_DM = '"+jddm+"' ";
					this.getBs().delete(deletesql1);
			System.out.println();

			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	
	
}
