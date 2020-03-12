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
 * 企业税款属地变更（单户批量）
 *
 */
@Controller
@RequestMapping("dhpl")
public class QysksdbsdhplController extends Super {
	
	@Autowired
	BaseService bs;
	
	private Map<String, Object> user = null;
	/**
	 * 页面跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "ImportTaxData/qysksdbgdbpl";
		} catch (Exception e) {
			e.printStackTrace();
			return "ImportTaxData/qysksdbgdbpl";
		}
	}
	
	//
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
	
	/**
	 * 单户企业税款查询
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/querySdbg.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String querySdbg(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			String jd = this.findJd(request, response);
			String pageNo=getValue(request.getParameter("page"));		//第几页
			String pageSize=getValue(request.getParameter("limit"));	//显示条数
			String cxrq = getValue(rmap.get("cxrq")).replaceAll(" ", "");		//日期
			String qsrq = cxrq.substring(0, cxrq.lastIndexOf("-"));			//起始日期
			String jsrq = cxrq.substring(cxrq.lastIndexOf("-") + 1);		//结束日期
			String jd_dm = getValue(rmap.get("jd_dm"));			//街道代码
			if ("".equals(jd_dm) || jd_dm == null) {
				jd_dm = "%";
			}
			String nsrmc = getValue(rmap.get("qymc"));
			String nsrsbh = getValue(rmap.get("nsrsbh"));
			String zsxm_dm = getValue(rmap.get("zsxm_dm"));
			if ("".equals(zsxm_dm) || zsxm_dm == null) {
				zsxm_dm = "%";
			}
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
			
			String sql = "";
			
			//判断所选税种进行查询，若未选税种条件则全部查询
			if ("企业所得税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = ""
						+ "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'企业所得税' zsxm,sum(qysds) zse,sum(qysds*bl/100) dfzse,"
						+ " TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  " + "where nsrmc = '" + nsrmc
						+ "' and rk_rq>=to_date('" + qsrq + "','yyyyMM') and rk_rq<=to_date('" + jsrq
						+ "','yyyyMM')  and qysds!=0 " + "and jd_dm like '" + jd_dm + "' "+jd
						+ "group by nsrmc,jd_dm,'企业所得税',rk_rq,qxj, to_char(hfid)";

				List<Map<String, Object>> qysds = bs.query(sql);
				if (qysds != null) {
					list.add(qysds);
				}
				
			}

			if ("增值税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'增值税' zsxm,sum(zzs) zse,sum(zzs*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and zzs!=0 " + "and jd_dm like '"
						+ jd_dm + "' "+jd + "group by nsrmc,jd_dm,'增值税' ,rk_rq,qxj,to_char(hfid)";

				List<Map<String, Object>> zzs = bs.query(sql);
				if (zzs != null) {
					list.add(zzs);
				}
				
			}
			if ("营改增增值税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营改增增值税' zsxm,sum(ygzzzs) zse,sum(ygzzzs*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and ygzzzs!=0 "+jd
						+ "and jd_dm like '" + jd_dm + "' " + "group by nsrmc,jd_dm,'营改增增值税' ,rk_rq,qxj, to_char(hfid)";
				List<Map<String, Object>> ygzzzs = bs.query(sql);
				if (ygzzzs != null) {
					list.add(ygzzzs);
				}
				
				
			}
			if ("营业税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营业税' zsxm,sum(yys) zse,sum(yys*bl/100) dfzse, TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and yys!=0 " + "and jd_dm like '"
						+ jd_dm + "' " +jd+ "group by nsrmc,jd_dm,'营业税' ,rk_rq,qxj,to_char(hfid)";
				List<Map<String, Object>> yys = bs.query(sql);
				if (yys != null) {
					list.add(yys);
				}
				
			}
			if ("个人所得税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'个人所得税' zsxm,sum(grsds) zse,sum(grsds*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and grsds!=0 " + "and jd_dm like '"
						+ jd_dm + "' " +jd+ "group by nsrmc,jd_dm,'个人所得税' ,rk_rq,qxj,to_char(hfid)";
				List<Map<String, Object>> grsds = bs.query(sql);
				if (grsds != null) {
					list.add(grsds);
				}
				
			}
			if ("车船税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'车船税' zsxm,sum(ccs) zse,sum(ccs*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and ccs!=0 " + "and jd_dm like '"
						+ jd_dm + "' "+jd + "group by nsrmc,jd_dm,'车船税' ,rk_rq,qxj,to_char(hfid)";
				List<Map<String, Object>> ccs = bs.query(sql);
				if (ccs != null) {
					list.add(ccs);
				}
				
			}
			if ("房产税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'房产税' zsxm,sum(fcs) zse,sum(fcs*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and fcs!=0 " + "and jd_dm like '"
						+ jd_dm + "' "+jd + "group by nsrmc,jd_dm,'房产税' ,rk_rq,qxj,to_char(hfid)";
				List<Map<String, Object>> fcs = bs.query(sql);
				if (fcs != null) {
					list.add(fcs);
				}
				
			}
			if ("印花税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'印花税' zsxm,sum(yhs) zse,sum(yhs*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'  and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and yhs!=0 " + "and jd_dm like '"
						+ jd_dm + "' "+jd + "group by nsrmc,jd_dm,'印花税' ,rk_rq,qxj,to_char(hfid)";
				List<Map<String, Object>> yhs = bs.query(sql);
				if (yhs != null) {
					list.add(yhs);
				}
				
			}
			if ("城市维护建设税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城市维护建设税' zsxm,sum(cswhjss) zse,sum(cswhjss*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and cswhjss!=0 "
						+ "and jd_dm like '" + jd_dm + "' " +jd+ "group by nsrmc,jd_dm,'城市维护建设税' ,rk_rq,qxj,to_char(hfid)";

				List<Map<String, Object>> cswhjss = bs.query(sql);
				if (cswhjss != null) {
					list.add(cswhjss);
				}
				
			}
			if ("地方教育附加".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'地方教育附加' zsxm,sum(dfjyfj) zse,sum(dfjyfj*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "'   and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and dfjyfj!=0 "
						+ "and jd_dm like '" + jd_dm + "' "+jd + "group by nsrmc,jd_dm,'地方教育附加' ,rk_rq,qxj,to_char(hfid)";
				List<Map<String, Object>> dfjyfj = bs.query(sql);
				if (dfjyfj != null) {
					list.add(dfjyfj);
				}
			
			}
			if ("教育附加".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'教育附加' zsxm,sum(jyfj) zse,sum(jyfj*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and jyfj!=0 " + "and jd_dm like '"
						+ jd_dm + "' " +jd+ "group by nsrmc,jd_dm,'教育附加' ,rk_rq,qxj,to_char(hfid)";
				List<Map<String, Object>> jyfj = bs.query(sql);
				if (jyfj != null) {
					list.add(jyfj);
				}
				
			}
			if ("城镇土地使用税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城镇土地使用税' zsxm,sum(cztdsys) zse,sum(cztdsys*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')  and cztdsys!=0 "
						+ "and jd_dm like '" + jd_dm + "' " +jd+ "group by nsrmc,jd_dm,'城镇土地使用税' ,rk_rq,qxj,to_char(hfid)";
				List<Map<String, Object>> cztdsys = bs.query(sql);
				if (cztdsys != null) {
					list.add(cztdsys);
				}
				
			}
			if ("环保税".equals(zsxm_dm) || "%".equals(zsxm_dm)) {
				sql = "select nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'环保税' zsxm,sum(hbs) zse,sum(hbs*bl/100) dfzse,TO_CHAR(RK_RQ,'YYYY-MM') RK_RQ,qxj,to_char(hfid) hfid from sb_nsrxx s  "
						+ "where nsrmc = '" + nsrmc + "' and rk_rq>=to_date('" + qsrq
						+ "','yyyyMM') and rk_rq<=to_date('" + jsrq + "','yyyyMM')   and hbs!=0 " + "and jd_dm like '"
						+ jd_dm + "' " +jd+ "group by nsrmc,jd_dm,'环保税' ,rk_rq,qxj,to_char(hfid)";
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
	 * 税款恢复
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/HF.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public Object HF(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			
			String sql_rkrq_max = this.getValue(form.get("rkrq"));			//入库日期
			
			String nsrmc = this.getValue(form.get("nsrmc"));			//纳税人名称
			String sz = this.getValue(form.get("sz"));			//税种
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

				String sql = "select to_char(hfid) hfid from SB_NSRXX  where nsrmc = '"+nsrmc+"'  and hfid is not null and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm')  and qxj !=2 and "+szzd+"!=0";
				List<Map<String,Object>> hfidList = bs.query(sql);
				String hfid = "";
				if(hfidList.size()>0){
					 hfid =  (String) hfidList.get(0).get("HFID");
				}
				String sql2 = "select to_char(hfid) hfid from SB_NSRXX  where nsrmc = '"+nsrmc+"'  and hfid is not null and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm')  and qxj =2 and "+szzd+"!=0";
				List<Map<String,Object>> hfidList2 = bs.query(sql2);
				if(hfidList2.size()>0){
				String del2 = "delete from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj = 2 and hfid is not null and "+szzd+"!=0";
				bs.delete(del2);
				
				}
				if(hfid!=""){
					String del = "delete from SB_NSRXX  where nsrmc = '"+nsrmc+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm')  and xh not in ("+hfid+") and qxj != 2 and "+szzd+"!=0";
					bs.delete(del);
				}
				String update = "update SB_NSRXX  set hfid='' , qxj=''  where nsrmc = '"+nsrmc+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm')  and  "+szzd+"!=0";
				bs.update(update);

			
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

}
