package fast.app;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fast.main.util.Super;

public class tjcx extends Super {
	
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "qrqcgl/tjcx";
		} catch (Exception e) {
			e.printStackTrace();
			return "qrqcgl/tjcx";
		}
	}
	
	public String doQuery(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String yearNmonth = getValue(this.getForm().get("yearNmonth"));
			//处理时间
			String starTime = "";
			String endTime = "";
			String[] star = yearNmonth.split(" - ");
			if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
				starTime = star[0];
				endTime = star[1];
				starTime = starTime.substring(0, 4) + starTime.substring(5, 7);
				endTime = endTime.substring(0, 4) + endTime.substring(5, 7);
			}
			String querysql = "";
			if(StringUtils.isNotBlank(yearNmonth)) {
				querysql = "select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '91' and to_char(fbrq,'yyyymm') >='"+starTime+"' and to_char(fbrq,'yyyymm') <= '"+endTime+"') a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '91') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '92' and to_char(fbrq,'yyyymm') >='"+starTime+"' and to_char(fbrq,'yyyymm') <= '"+endTime+"') a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '92') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '93' and to_char(fbrq,'yyyymm') >='"+starTime+"' and to_char(fbrq,'yyyymm') <= '"+endTime+"') a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '93') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '94' and to_char(fbrq,'yyyymm') >='"+starTime+"' and to_char(fbrq,'yyyymm') <= '"+endTime+"') a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '94') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '95' and to_char(fbrq,'yyyymm') >='"+starTime+"' and to_char(fbrq,'yyyymm') <= '"+endTime+"') a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '95') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '96' and to_char(fbrq,'yyyymm') >='"+starTime+"' and to_char(fbrq,'yyyymm') <= '"+endTime+"') a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '96') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '97' and to_char(fbrq,'yyyymm') >='"+starTime+"' and to_char(fbrq,'yyyymm') <= '"+endTime+"') a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '97') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '98' and to_char(fbrq,'yyyymm') >='"+starTime+"' and to_char(fbrq,'yyyymm') <= '"+endTime+"') a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '98') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '99' and to_char(fbrq,'yyyymm') >='"+starTime+"' and to_char(fbrq,'yyyymm') <= '"+endTime+"') a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '99') d\r\n" + 
						"";
			} else {
				querysql = "select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '91' ) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '91') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '92' ) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '92') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '93' ) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '93') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '94' ) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '94') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '95' ) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '95') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '96' ) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '96') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '97' ) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '97') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '98' ) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '98') d\r\n" + 
						"UNION ALL\r\n" + 
						"select d.jd_mc,a.hs,a.sk,round((a.hs/b.zhs),4)* 100 || '%' hszb,round((a.sk/c.zsk),4)* 100 || '%' skzb from (SELECT count(*) hs,NVL(sum(DNLJSK),0) sk FROM FAST_QYQC where ssjd = '99' ) a,(select count(*) zhs from FAST_QYQC) b,(select NVL(sum(DNLJSK),0) zsk from FAST_QYQC) c,(SELECT jd_mc from DM_JD where jd_dm = '99') d\r\n" + 
						"";
			}
			List<Map<String, Object>> list = this.getBs().query(querysql);
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
}
