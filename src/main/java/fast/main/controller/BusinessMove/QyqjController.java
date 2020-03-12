package fast.main.controller.BusinessMove;

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
 * 企业迁移管理 企业迁入
 *  本功能用于对系统的“企业迁入”进行操作管理！
 * @author Administrator
 *
 */
@Controller
@RequestMapping("qyqr")
public class QyqjController extends Super{
	
@Autowired
BaseService bs;
private Map<String, Object> user = null;

	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "BusinessMove/qyqr2";
		} catch (Exception e) {
			e.printStackTrace();
			return "BusinessMove/qyqr2";
		}
	}

@RequestMapping("doQuery.do")
@ResponseBody
public String doQuery(HttpServletRequest request , HttpServletResponse response,
		@RequestParam Map<String, Object> form) {
	user = (Map<String, Object>) request.getSession().getAttribute("user");
	String jds = getValue(user.get("DWID"));
	String and ="";
	if(null != jds && "00".equals(jds)) {
		and =" and 1=1";
		
	}else {
		and =" and jd_dm = "+jds+"";
	}
	try {
		String yearNmonth = getValue(form.get("yearNmonth"));
		String jd=getValue(form.get("ssjd"));
		String nsrsbh = getValue(form.get("nsrsbh"));
		String nsrmc = getValue(form.get("nsrmc"));
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
		String querysql = "select s.*, j.jd_mc jdmc from FAST_QYQR s, dm_jd j where s.xssjd = j.jd_dm and s.status in (1,4,5) "+and+"";	
		System.out.println(querysql);
		if("0".equals(jd)) {
			if(StringUtils.isNotBlank(nsrsbh)) {
				querysql += " and s.nsrsbh = '" + nsrsbh + "' ";
			}
			if(StringUtils.isNotBlank(nsrmc)) {
				querysql += " and s.nsrmc like '%" + nsrmc + "%' ";
			}
			if(StringUtils.isNotBlank(yearNmonth)) {
				querysql += " and s.qrrq >=to_date('"+starTime+"','yyyymm') and s.qrrq<=to_date('"+endTime+"','yyyymm')";
			}
			
		} else {
			if(StringUtils.isNotBlank(nsrsbh)) {
				querysql += " and s.nsrsbh = '" + nsrsbh + "' ";
			}
			if(StringUtils.isNotBlank(nsrmc)) {
				querysql += " and s.nsrmc like '%" + nsrmc + "%' ";
			}
			if(StringUtils.isNotBlank(yearNmonth)) {
				querysql += " and s.qrrq >=to_date('"+starTime+"','yyyymm') and s.qrrq <= to_date('"+endTime+"','yyyymm') ";
			}
			if(StringUtils.isNotBlank(jd)) {
				querysql += " and  s.xssjd = '" + jd + "' ";
			}	
		}		
		List<Map<String, Object>> list = bs.query(querysql);
		return this.toJson("000", "查询成功！", list);
	} catch (Exception e) {
		e.printStackTrace();
		return this.toJson("009", "查询异常！");
	}
}

}
