package fast.main.controller.BusinessMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.Super;

/**
 * 企业迁移管理 统计查询
 *  本功能用于对系统的“迁入迁出统计查询”进行操作管理！
 * @author Administrator
 *
 */
@Controller
@RequestMapping("qrqctjcx")
public class QrqctjcxController extends Super {
	
	@Autowired
	BaseService bs;
	
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "BusinessMove/tjcx";
		} catch (Exception e) {
			e.printStackTrace();
			return "BusinessMove/tjcx";
		}
	}
	
	@RequestMapping(value="/doQuery.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doQuery(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			
			String yearNmonth = getValue(rmap.get("yearNmonth"));
			//处理时间
			String pageNo = getValue(rmap.get("page"));
			String pageSize = getValue(rmap.get("limit"));
			String starTime = "";
			String endTime = "";
			String[] star = yearNmonth.split(" - ");
			if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
				starTime = star[0];
				endTime = star[1];
				starTime = starTime.substring(0, 4) + starTime.substring(5, 7);
				endTime = endTime.substring(0, 4) + endTime.substring(5, 7);
			}
			String querysql = "select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where 1 = 1 and ssjd = '91' and (-to_char(fbrq,'yyyymm') >=?-) and (-to_char(fbrq,'yyyymm') <= ?-)) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '91') d\r\n" + 
					"UNION ALL\r\n" + 
					"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where 1 = 1 and ssjd = '92' and (-to_char(fbrq,'yyyymm') >=?-) and (-to_char(fbrq,'yyyymm') <= ?-)) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '92') d\r\n" + 
					"UNION ALL\r\n" + 
					"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where 1 = 1 and ssjd = '93' and (-to_char(fbrq,'yyyymm') >=?-) and (-to_char(fbrq,'yyyymm') <= ?-)) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '93') d\r\n" + 
					"UNION ALL\r\n" + 
					"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where 1 = 1 and ssjd = '94' and (-to_char(fbrq,'yyyymm') >=?-) and (-to_char(fbrq,'yyyymm') <= ?-)) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '94') d\r\n" + 
					"UNION ALL\r\n" + 
					"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where 1 = 1 and ssjd = '95' and (-to_char(fbrq,'yyyymm') >=?-) and (-to_char(fbrq,'yyyymm') <= ?-)) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '95') d\r\n" + 
					"UNION ALL\r\n" + 
					"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where 1 = 1 and ssjd = '96' and (-to_char(fbrq,'yyyymm') >=?-) and (-to_char(fbrq,'yyyymm') <= ?-)) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '96') d\r\n" + 
					"UNION ALL\r\n" + 
					"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where 1 = 1 and ssjd = '97' and (-to_char(fbrq,'yyyymm') >=?-) and (-to_char(fbrq,'yyyymm') <= ?-)) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '97') d\r\n" + 
					"UNION ALL\r\n" + 
					"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where 1 = 1 and ssjd = '98' and (-to_char(fbrq,'yyyymm') >=?-) and (-to_char(fbrq,'yyyymm') <= ?-)) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '98') d\r\n" + 
					"UNION ALL\r\n" + 
					"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where 1 = 1 and ssjd = '99' and (-to_char(fbrq,'yyyymm') >=?-) and (-to_char(fbrq,'yyyymm') <= ?-)) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '99') d\r\n" + "";
			
			querysql = this.getSql2(querysql, new Object[] {starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,starTime,endTime,});
			
			int count = bs.queryCount(querysql);
			
			List<Map<String, Object>> lists = bs.query(querysql, pageNo, pageSize);

			return this.toJson("000", "查询成功！", lists, count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	@RequestMapping(value="/getNsDate.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String getNsDate(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			String sql = "SELECT to_char(max(RK_RQ),'yyyyMM') as rkrq FROM SB_NSRXX ORDER BY RK_RQ DESC  ";

			List<Map<String, Object>> result = bs.query(sql);
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < result.size(); i++) {
				Map<String, Object> map = result.get(i);
				lists.add(map);
			}

			return this.toJson("000", "查询成功！", lists);

		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
}
