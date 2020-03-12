package fast.app;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import fast.main.util.Super;

public class qyqc1 extends Super {
	
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "qrqcgl/qyqr2";
		} catch (Exception e) {
			e.printStackTrace();
			return "qrqcgl/qyqr2";
		}
	}
	
	public String doQuery(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String yearNmonth = getValue(this.getForm().get("yearNmonth"));
			String jd=getValue(this.getForm().get("ssjd"));
			String nsrsbh = getValue(this.getForm().get("nsrsbh"));
			String nsrmc = getValue(this.getForm().get("nsrmc"));
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
			String querysql = "select s.*, j.jd_mc jdmc from FAST_QYQR s, dm_jd j where s.xssjd = j.jd_dm ";	
			List<Map<String, Object>> list = this.getBs().query(querysql);
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	
}
