package fast.main.controller.ImportTaxData;

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
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.Super;
import net.sf.json.JSONArray;

/**
 * 企业税款属地变更（单户）
 *
 */
@Controller
@RequestMapping("dh")
public class QysksdbsdhController extends Super {
	
	@Autowired
	BaseService bs;
	
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {	
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "ImportTaxData/qysksdbgdb";	
		} catch (Exception e) {
			e.printStackTrace();
			return "ImportTaxData/qysksdbgdb";	
		}
	}
	
	private Map<String, Object> user = null;
	
	//查询当前用户所属街道
	public String findJd(HttpServletRequest request, HttpServletResponse response) {
		String andJd = "";
		try {
			user = (Map<String, Object>)request.getSession().getAttribute("user");
			Integer jd = Integer.parseInt((String)user.get("DWID"));	
			if (jd != null && jd == 00) {
				andJd = "and 1 = 1";
			}else {
				andJd = "and jd_dm = "+jd;
			}		
		} catch (Exception e) {
			// TODO: handle exception
		}
		return andJd;
	}
	
	/**
	 * 根据条件查询纳税人名称
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/queryQymc.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String queryQymc(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String jd = this.findJd(request, response);
			String qymc = getValue(rmap.get("qymc"));		//获取企业名称
			String nsrsbh = getValue(rmap.get("nsrsbh"));	//获取纳税人识别号
			String cxrq = getValue(rmap.get("cxrq")).replaceAll(" ", "");		//获取日期
			String qsrq = cxrq.substring(0, cxrq.lastIndexOf("-"));			//拆分获取的日期，从中获取起始日期
			String jsrq = cxrq.substring(cxrq.lastIndexOf("-") + 1);		//拆分获取的日期，从中获取结束日期
			String pageNo = getValue(rmap.get("page"));			//获取第几页
			String pageSize = getValue(rmap.get("limit"));		//获取每页显示条数
			
			String querysql = "select distinct nsrmc from sb_nsrxx s where 1 = 1 and (-s.nsrsbh like '%' || ? || '%'-) and (-s.nsrmc like '%' || ? ||'%'-) and rk_rq >= to_date(?,'yyyyMM') and rk_rq <= to_date(?,'yyyyMM')"+jd;
			querysql = this.getSql2(querysql, new Object[] {nsrsbh,qymc,qsrq,jsrq});
			int queryCount = bs.queryCount(querysql);
			
			List<Map<String, Object>> list = bs.query(querysql,pageNo,pageSize);
			return this.toJson("000", "查询成功！", list,queryCount);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	
	/**
	 * 初始化查询数据
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/queryInit.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String queryInit(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			String jd = this.findJd(request, response);
			//查询街道信息
			String sql = "select * from dm_jd";
			sql = this.getSql2(sql);
			List<Map<String, Object>> jdlist = bs.query(sql);
			//查询行业信息
			sql = "select * from dm_hyml";
			sql = this.getSql2(sql);
			List<Map<String, Object>> hylist = bs.query(sql);
			//查询最大日期
			String queryMaxRkrq = "SELECT TO_CHAR(max(s.RK_RQ), 'yyyyMM') rkrq FROM sb_nsrxx s";

			Map<String, Object> maxRkrq = bs.queryOne(queryMaxRkrq);

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
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/querySdbg.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String querySdbg(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			String jd = this.findJd(request, response);
			String pageNo=getValue(request.getParameter("page"));		//获取第几页
			String pageSize=getValue(request.getParameter("limit"));	//显示条数
			String cxrq = getValue(rmap.get("cxrq")).replaceAll(" ", "");	//日期
			String qsrq = cxrq.substring(0, cxrq.lastIndexOf("-"));			//起始日期
			String jsrq = cxrq.substring(cxrq.lastIndexOf("-") + 1);		//结束日期
			String jd_dm = getValue(rmap.get("jd_dm"));			//街道代码
			if ("".equals(jd_dm) || jd_dm == null) {
				jd_dm = "%";
			}
			String nsrmc = getValue(rmap.get("qymc"));		//企业名称
			String nsrsbh = getValue(rmap.get("nsrsbh"));	//纳税人识别号
			String zsxm_dm = getValue(rmap.get("zsxm_dm"));		//税种
			if ("".equals(zsxm_dm) || zsxm_dm == null) {
				zsxm_dm = "%";
			}
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
			
			String sql = "";
			/*
			 * 根据税种查询相关信息 
			 * 有税种根据税种查 
			 * 没有税种条件查询全部
			 */
			if ("企业所得税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'企业所得税' zsxm,sum(qysds) zse,sum(qysds*bl/100) dfzse,"
						+ " TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  " + "where nsrmc = '" + nsrmc
						+ "' and rk_rq>=to_date('" + qsrq + "','yyyyMM') and rk_rq<=to_date('" + jsrq
						+ "','yyyyMM')  and qysds!=0 " + "and jd_dm like '" + jd_dm + "' "+jd
						+ "group by nsrmc,jd_dm,'企业所得税',rk_rq,qxj,xh, to_char(hfid)";

				List<Map<String, Object>> qysds = bs.query(sql);
				if (qysds != null) {
					list.add(qysds);
				}
				
			}
			
			if ("增值税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'增值税' zsxm,sum(zzs) zse,sum(zzs*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and zzs!=0 " + "and jd_dm like '"
						+ jd_dm + "' "+jd + "group by nsrmc,jd_dm,'增值税' ,rk_rq,qxj,xh,to_char(hfid)";

				List<Map<String, Object>> zzs = bs.query(sql);
				if (zzs != null) {
					list.add(zzs);
				}
				
			}
			if ("营改增增值税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营改增增值税' zsxm,sum(ygzzzs) zse,sum(ygzzzs*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and ygzzzs!=0 "+jd
						+ "and jd_dm like '" + jd_dm + "' " + "group by nsrmc,jd_dm,'营改增增值税' ,rk_rq,qxj,xh, to_char(hfid)";
				List<Map<String, Object>> ygzzzs = bs.query(sql);
				if (ygzzzs != null) {
					list.add(ygzzzs);
				}
				
				
			}
			if ("营业税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营业税' zsxm,sum(yys) zse,sum(yys*bl/100) dfzse, TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and yys!=0 " + "and jd_dm like '"
						+ jd_dm + "' " +jd+ "group by nsrmc,jd_dm,'营业税' ,rk_rq,qxj,xh,to_char(hfid)";
				List<Map<String, Object>> yys = bs.query(sql);
				if (yys != null) {
					list.add(yys);
				}
				
			}
			if ("个人所得税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'个人所得税' zsxm,sum(grsds) zse,sum(grsds*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and grsds!=0 " + "and jd_dm like '"
						+ jd_dm + "' " +jd+ "group by nsrmc,jd_dm,'个人所得税' ,rk_rq,qxj,xh,to_char(hfid)";
				List<Map<String, Object>> grsds = bs.query(sql);
				if (grsds != null) {
					list.add(grsds);
				}
				
			}
			if ("车船税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'车船税' zsxm,sum(ccs) zse,sum(ccs*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and ccs!=0 " + "and jd_dm like '"
						+ jd_dm + "' "+jd + "group by nsrmc,jd_dm,'车船税' ,rk_rq,qxj,xh,to_char(hfid)";
				List<Map<String, Object>> ccs = bs.query(sql);
				if (ccs != null) {
					list.add(ccs);
				}
				
			}
			if ("房产税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'房产税' zsxm,sum(fcs) zse,sum(fcs*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and fcs!=0 " + "and jd_dm like '"
						+ jd_dm + "' "+jd + "group by nsrmc,jd_dm,'房产税' ,rk_rq,qxj,xh,to_char(hfid)";
				List<Map<String, Object>> fcs = bs.query(sql);
				if (fcs != null) {
					list.add(fcs);
				}
				
			}
			if ("印花税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'印花税' zsxm,sum(yhs) zse,sum(yhs*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and yhs!=0 " + "and jd_dm like '"
						+ jd_dm + "' "+jd + "group by nsrmc,jd_dm,'印花税' ,rk_rq,qxj,xh,to_char(hfid)";
				List<Map<String, Object>> yhs = bs.query(sql);
				if (yhs != null) {
					list.add(yhs);
				}
				
			}
			if ("城市维护建设税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城市维护建设税' zsxm,sum(cswhjss) zse,sum(cswhjss*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and cswhjss!=0 "
						+ "and jd_dm like '" + jd_dm + "' " +jd+ "group by nsrmc,jd_dm,'城市维护建设税' ,rk_rq,qxj,xh,to_char(hfid)";

				List<Map<String, Object>> cswhjss = bs.query(sql);
				if (cswhjss != null) {
					list.add(cswhjss);
				}
				
			}
			if ("地方教育附加".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'地方教育附加' zsxm,sum(dfjyfj) zse,sum(dfjyfj*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'   and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and dfjyfj!=0 "
						+ "and jd_dm like '" + jd_dm + "' "+jd + "group by nsrmc,jd_dm,'地方教育附加' ,rk_rq,qxj,xh,to_char(hfid)";
				List<Map<String, Object>> dfjyfj = bs.query(sql);
				if (dfjyfj != null) {
					list.add(dfjyfj);
				}
			
			}
			if ("教育附加".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'教育附加' zsxm,sum(jyfj) zse,sum(jyfj*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and jyfj!=0 " + "and jd_dm like '"
						+ jd_dm + "' " +jd+ "group by nsrmc,jd_dm,'教育附加' ,rk_rq,qxj,xh,to_char(hfid)";
				List<Map<String, Object>> jyfj = bs.query(sql);
				if (jyfj != null) {
					list.add(jyfj);
				}
				
			}
			if ("城镇土地使用税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城镇土地使用税' zsxm,sum(cztdsys) zse,sum(cztdsys*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')  and cztdsys!=0 "
						+ "and jd_dm like '" + jd_dm + "' " +jd+ "group by nsrmc,jd_dm,'城镇土地使用税' ,rk_rq,qxj,xh,to_char(hfid)";
				List<Map<String, Object>> cztdsys = bs.query(sql);
				if (cztdsys != null) {
					list.add(cztdsys);
				}
				
			}
			if ("环保税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'环保税' zsxm,sum(hbs) zse,sum(hbs*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and hbs!=0 " + "and jd_dm like '"
						+ jd_dm + "' " +jd+ "group by nsrmc,jd_dm,'环保税' ,rk_rq,qxj,xh,to_char(hfid)";
				List<Map<String, Object>> hbs = bs.query(sql);
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

	/**
	 * 将拆分的删除，恢复原数据
	 * @param rmap
	 * @return 返回状态(000恢复成功,009恢复失败)
	 */
	@RequestMapping("/HF.do")
	@ResponseBody
	public Object HF(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			String jd = this.findJd(request, response);
			String xh = getValue(rmap.get("xh"));//获取序号		
			
			String sql = "";
			String updateSql = "";
			String deleteSql = "";
			
			sql = "select qxj,to_char(hfid) hfid ,to_char(rk_rq,'yyyy-MM') rkrq from sb_nsrxx where xh ='"+ xh +"'"+jd;
			List<Map<String, Object>> qxjlist = bs.query(sql);
			String qxj = qxjlist.get(0).get("QXJ").toString();		//获取区县级
			String hfid = qxjlist.get(0).get("HFID").toString();	//恢复id
			String rkrq = qxjlist.get(0).get("RKRQ").toString();	//入库日期
			
				//区县级为1时的恢复方法
				if (qxj.equals("1")) {
					updateSql = "update sb_nsrxx set qxj = '' where to_number(qxj)=1 and rk_rq = to_date('"+rkrq+"','yyyy-MM') and hfid is null";
					bs.update(updateSql);
					
					deleteSql = "delete from sb_nsrxx where to_number(qxj) = 1 and to_char(hfid) = '"+hfid+"' and rk_rq = to_date('"+rkrq+"','yyyy-MM')";
					bs.delete(deleteSql);
				} 
				//区县级为2时的恢复方法
				if (qxj.equals("2")) {
					updateSql = "update sb_nsrxx set qxj = '' , hfid = '' where xh in ("+hfid+") and to_number(qxj)=2 and rk_rq = to_date('"+rkrq+"','yyyy-MM')";
					bs.update(updateSql);
					
					deleteSql = "delete from sb_nsrxx where to_number(qxj) = 2 and to_char(hfid) = '"+hfid+"' and rk_rq = to_date('"+rkrq+"','yyyy-MM')";
					bs.delete(deleteSql);
					
				}
				//区县级不为1、不为2、不为空时的恢复方法
				if (qxj!=""&&qxj!=null&&!qxj.equals("2")&&!qxj.equals("1")) {
					updateSql = "update sb_nsrxx set qxj = '' , hfid = '' where xh in ("+hfid+") and qxj!=xh and rk_rq = to_date('"+rkrq+"','yyyy-MM')";
					bs.update(updateSql);
					
					deleteSql = "delete from sb_nsrxx where qxj !=xh and to_char(hfid) = '"+hfid+"' and rk_rq = to_date('"+rkrq+"','yyyy-MM')";
					bs.delete(deleteSql);
					
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
	
	
	@RequestMapping(value="/queryInits.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String queryInits(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			
			request.getSession().getAttribute("uno");
			// 街道
			String sql = "select * from dm_jd";
			List<Map<String, Object>> jdlist = bs.query(sql);
			// 行业
			sql = "select * from dm_hyml";
			List<Map<String, Object>> hylist = bs.query(sql);
			
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
	 * 企业税款属地变更（单户）拆分
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/CF.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public Object CF(HttpServletRequest request , HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			
			List<Map<String, Object>> params = (List<Map<String, Object>>) JSONArray.fromObject(getValue(rmap.get("str")));		//获取拆分信息
			
			String nsrmc = this.getValue(request.getParameter("nsrmc"));    //获取纳税人名称
			String sz = getValue(rmap.get("sz"));		//获取税种
			
			String xhStr = getValue(rmap.get("xh"));		//获取序号				
			String[] xhArr = xhStr.split(",");				//将序号拆分成数组
			String xh1 = xhArr[0];
			int qxj = 2;
			String szdm = "";
			
			if(sz!=""){
				//识别各个税收
				if (sz.equals("增值税")) {
					szdm= "zzs";
				} else if (sz.equals("营改增增值税")) {
					szdm = "ygzzzs";
				} else if (sz.equals("营业税")) {
					szdm= "yys";
				} else if (sz.equals("企业所得税")) {
					szdm= "qysds";
				} else if (sz.equals("个人所得税")) {
					szdm= "grsds";
				} else if (sz.equals("车船税")) {
					szdm= "ccs";
				} else if (sz.equals("房产税")) {
					szdm= "fcs";
				} else if (sz.equals("印花税")) {
					szdm= "yhs";
				} else if (sz.equals("城市维护建设税")) {
					szdm= "cswhjss";
				} else if (sz.equals("地方教育附加")) {
					szdm= "dfjyfj";
				} else if (sz.equals("教育附加")) {
					szdm= "jyfj";
				} else if (sz.equals("城镇土地使用税")) {
					szdm= "cztdsys";
				} else if (sz.equals("环保税")) {
					szdm= "hbs";
				}
			}
			
			Long date = System.currentTimeMillis();
			
			String updateSql = "update SB_NSRXX set qxj = '"+qxj+"' where xh in ("+xhStr+")";
			bs.update(updateSql);
			
			for(Map<String, Object> param : params){

				String jddm = getValue(param.get("jddm"));		//获取街道代码
				
				//上办部分负数 数据 下班部分正数数据
				if (szdm.equals("zzs")) {
					
					String nsrxxBfOld = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','-'||('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE xh = '"+xh1+"'";
					bs.insert(nsrxxBfOld);
					//======================================
					String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,'"+ getValue(param.get("jddm")) +"',HY_DM,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0',('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qxj+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBfNew);
				
				} else if (szdm.equals("ygzzzs")) {
					String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','-'||('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBf);
					
					//======================================
					String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,'"+ getValue(param.get("jddm")) +"',HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0',('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBfNew);
					
				} else if (szdm.equals("yys")) {
					String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','-'||('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,"+xhStr+",NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBf);
					
					//======================================
					String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,'"+ getValue(param.get("jddm")) +"',HY_DM,'-'||ZZS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0',('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qxj+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"'  ";
					bs.insert(nsrxxBfNew);
					
				} else if (szdm.equals("qysds")) {
					String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc"+ 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),QYSDS_DS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','-'||('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBf);
					
					//======================================
					String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,'"+ getValue(param.get("jddm")) +"',HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),QYSDS_DS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0',('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qxj+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBfNew);
					
				} else if (szdm.equals("grsds")) {
					String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','-'||('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBf);
					
					//======================================
					String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,'"+ getValue(param.get("jddm")) +"',HY_DM,'-'||ZZS,'-'||YYS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0',('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qxj+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBfNew);
					
				} else if (szdm.equals("ccs")) {
					String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),QYSDS_GS,QYSDS_DS,QYSDS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','-'||('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBf);
					
					//======================================
					String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,'"+ getValue(param.get("jddm")) +"',HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0',('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qxj+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBfNew);
					
				} else if (szdm.equals("fcs")) {
					String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','-'||('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBf);
					
					//======================================
					String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,'"+ getValue(param.get("jddm")) +"',HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0',('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBfNew);
					
				} else if (szdm.equals("yhs")) {
					String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),CCS,QYSDS_GS,QYSDS_DS,QYSDS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','-'||('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBf);
					
					//======================================
					String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,'"+ getValue(param.get("jddm")) +"',HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0',('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBfNew);
					
				} else if (szdm.equals("cswhjss")) {
					String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','-'||('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBf);
					
					//======================================
					String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,'"+ getValue(param.get("jddm")) +"',HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0',('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qxj+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBfNew);
					
				} else if (szdm.equals("dfjyfj")) {
					String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','-'||('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBf);
					
					//======================================
					String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,'"+ getValue(param.get("jddm")) +"',HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0',('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"')	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qxj+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBfNew);
					
				} else if (szdm.equals("jyfj")) {
					String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','-'||('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBf);
					
					//======================================
					String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,'"+ getValue(param.get("jddm")) +"',HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0',('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qxj+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBfNew);
					
				} else if (szdm.equals("cztdsys")) {
					String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','-'||('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),HBS,"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBf);
					
					//======================================
					String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,'"+ getValue(param.get("jddm")) +"',HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0',('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),'-'||HBS,	"+qxj+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBfNew);
					
				} else if (szdm.equals("hbs")) {
					String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0','-'||('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,decode('"+ param.get("zse") +"','0','0','-'||'"+ param.get("zse") +"'),"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBf);
					
					//======================================
					String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
							"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
							"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
							")"
							+ "SELECT "
							+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,'"+ getValue(param.get("jddm")) +"',HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),decode('"+ param.get("zse") +"','0','0',('"+ param.get("zse") +"'*BL/100)),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,decode('"+ param.get("zse") +"','0','0','"+ param.get("zse") +"'),"+qxj+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+date+"'"
							+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"' and xh = '"+xh1+"' ";
					bs.insert(nsrxxBfNew);
					
				}
				
				 
			}
			
			String bftoNsrxxNewSql = "insert into sb_nsrxx xh (xh,xh.nsrmc,xh.rk_rq,xh.jd_dm,xh.hy_dm,xh.zzs,xh.yys,xh.grsds,xh.fcs,xh.yhs,xh.ccs,xh.qysds_gs,xh.qysds_ds,xh.qysds,xh.zse,xh.dfzse,xh.qyxz,xh.hhnsrmc,xh.lrry_dm,xh.lr_sj,xh.ygzzzs,xh.cswhjss,xh.dfjyfj,xh.jyfj,xh.xssr,xh.gds,xh.zsxm,xh.bl,xh.cztdsys,xh.hbs,xh.qxj,xh.zspmdm,xh.hydl,xh.hyzl,xh.yskmdm,xh.dzsphm,xh.djxhs,xh.zspm,xh.yzpzxh,xh.sksx,xh.hfid,xh.nsrsbh) "
					+" select xh,NSRMC,RK_RQ,JD_DM,HY_DM,zzs,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,qxj,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,hfid,NSRSBH from SB_NSRXX_BF where sjc = '"+date+"'";
			bs.insert(bftoNsrxxNewSql);
			
//			//再次清空备份表
			String deltemp = "delete from SB_NSRXX_BF where  sjc = '"+date+"'";
			bs.delete(deltemp);
			
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
		
		
	}
	
	/**
	 * 按单笔拆分多个街道
	 * 
	 * @param rmap
	 * @return 返回状态 000 拆分成功，刷新页面（009 拆分失败）
	 */
	@RequestMapping(value="/qbcf.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public Object PLXG(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			Long sjq = System.currentTimeMillis();
			
			String rkrq = this.getValue(form.get("rkrq"));
			String[] rq = rkrq.split("-");
			String qrq = rq[0];
			String hrq = rq[1];
			
			String nsrmc = this.getValue(form.get("nsrmc"));
			
			String jddm = this.getValue(form.get("jddm"));
			
			String sz = this.getValue(form.get("sz"));
			String[] szs = sz.split(","); 
			
			int qjxId = 1 ;
			
			//获取税种并拆分成数组
//			String [] szs = {"增值税","营改增增值税","营业税","企业所得税","个人所得税","车船税","房产税","印花税","城市维护建设税","地方教育附加","教育附加","城镇土地使用税","环保税"};

			//查询纳税人表最大日期
			
			Double  zse = 0.00;
			
			
				
				String delnsr = " delete from SB_NSRXX_BF s where NSRMC =  ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') ";
				bs.delete(delnsr);
				String nsrXh = "select xh from  SB_NSRXX s where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and hfid is null";
				String xhStr = "";
				List<Map<String,Object>> nsrXhs = bs.query(nsrXh);
				for(int x = 0 ; x < nsrXhs.size() ; x++){
					
					xhStr+=nsrXhs.get(x).get("XH")+",";
				}
				
				if(xhStr.length() > 0) {
					xhStr = xhStr.substring(0, xhStr.length() - 1);
				}
				String updateNsrxxZs =""; /*"update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and qxj is null";
				bs.update(updateNsrxxZs);*/
				for(int i = 0 ; i < szs.length ; i++){
					if(szs[i]!=""){
						//识别各个税收
						if (szs[i].equals("增值税")) {
							szs[i]= "zzs";
						} else if (szs[i].equals("营改增增值税")) {
							szs[i] = "ygzzzs";
						} else if (szs[i].equals("营业税")) {
							szs[i]= "yys";
						} else if (szs[i].equals("企业所得税")) {
							szs[i]= "qysds";
						} else if (szs[i].equals("个人所得税")) {
							szs[i]= "grsds";
						} else if (szs[i].equals("车船税")) {
							szs[i]= "ccs";
						} else if (szs[i].equals("房产税")) {
							szs[i]= "fcs";
						} else if (szs[i].equals("印花税")) {
							szs[i]= "yhs";
						} else if (szs[i].equals("城市维护建设税")) {
							szs[i]= "cswhjss";
						} else if (szs[i].equals("地方教育附加")) {
							szs[i]= "dfjyfj";
						} else if (szs[i].equals("教育附加")) {
							szs[i]= "jyfj";
						} else if (szs[i].equals("城镇土地使用税")) {
							szs[i]= "cztdsys";
						} else if (szs[i].equals("环保税")) {
							szs[i]= "hbs";
						}
						
						
						//根据每个税种去往备份表中添加要操作的数据
						//上办部分负数 数据 下班部分正数数据
						if (szs[i].equals("zzs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							
							int count = bs.queryCount(sql);
							if(count>0){
								continue;
							}
							
							updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and qxj is null and "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							
							String nsrxxBfOld = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc"+
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,'-'||ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||ZZS,'-'||(ZZS*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"'  ,NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfOld);
							//======================================
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,zzs,(ZZS*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
						
						} else if (szs[i].equals("ygzzzs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count>0){
								continue;
							}
							
							updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and qxj is null and "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||ygzzzs,'-'||(ygzzzs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,'-'||YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							//======================================
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,ygzzzs,(ygzzzs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("yys")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count>0){
								continue;
							}
							
							updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and qxj is null and "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,'-'||YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||yys,'-'||(yys*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							//======================================
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,yys,(yys*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("qysds")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count>0){
								continue;
							}
							
							updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and qxj is null and "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc"+ 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,'-'||QYSDS_GS,QYSDS_DS,'-'||QYSDS,'-'||qysds,'-'||(qysds*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							//======================================
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,QYSDS_GS,QYSDS_DS,QYSDS,qysds,(qysds*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("grsds")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count>0){
								continue;
							}
							
							updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and qxj is null and "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,'-'||GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||grsds,'-'||(grsds*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							Integer insert = bs.insert(nsrxxBf);
							
							//======================================
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,grsds,(grsds*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							Integer insert1 =bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("ccs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count>0){
								continue;
							}
							
							updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and qxj is null and "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,'-'||CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||ccs,'-'||(ccs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							//======================================
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,ccs,(ccs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("fcs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count>0){
								continue;
							}
							
							updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and qxj is null and "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,'-'||FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||fcs,'-'||(fcs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							//======================================
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,fcs,(fcs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("yhs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count>0){
								continue;
							}
							
							updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and qxj is null and "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,'-'||YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||yhs,'-'||(yhs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							//======================================
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,yhs,(yhs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("cswhjss")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count>0){
								continue;
							}
							
							updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and qxj is null and "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||cswhjss,'-'||(cswhjss*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,'-'||CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							//======================================
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,cswhjss,(cswhjss*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("dfjyfj")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count>0){
								continue;
							}
							
							updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and qxj is null and "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||dfjyfj,'-'||(dfjyfj*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,'-'||DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							//======================================
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,dfjyfj,(dfjyfj*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("jyfj")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count>0){
								continue;
							}
							
							updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and qxj is null and "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||jyfj,'-'||(jyfj*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,'-'||JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							//======================================
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,jyfj,(jyfj*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("cztdsys")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count>0){
								continue;
							}
							
							updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and qxj is null and "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||cztdsys,'-'||(cztdsys*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							//======================================
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,cztdsys,(cztdsys*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("hbs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count>0){
								continue;
							}
							
							updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmc+"') and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and qxj is null and "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||hbs,'-'||(hbs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,'-'||HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							//======================================
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,hbs,(hbs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmc+"'  and s.Rk_Rq between to_date('"+qrq+"','yyyy-mm') and to_date('"+hrq+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						}
						
						 
					}
					
					String selecSum = "select sum(zse) a from SB_NSRXX_BF  where nsrmc = '"+nsrmc+"' and zse > 0 group by nsrmc  ";
					List<Map<String, Object>> sum = bs.query(selecSum);
					if(sum.size()>0){
						zse+=Double.parseDouble(this.getValue(sum.get(0).get("A")));
					}
					
				}
				
			//导回真实表
			String bftoNsrxxNewSql = "insert into SB_NSRXX xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
					"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
					"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh) " + 
					"SELECT " + 
					"xh,NSRMC,RK_RQ,jd_dm,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,YGZZZS,CSWHJSS,DFJYFJ	,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,qxj,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,hfid,NSRSBH " + 
					" FROM SB_NSRXX_BF s WHERE  s.sjc = '"+sjq+"' ";
			bs.insert(bftoNsrxxNewSql);
			//再次清空备份表
			String deltemp = "delete from SB_NSRXX_BF where sjc = '"+sjq+"'";
			bs.delete(deltemp);
			
			return this.toJson("000", "查询成功！");

		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		  }
		
		
	}
	
}
