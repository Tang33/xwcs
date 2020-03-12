package fast.main.controller.BusinessMove;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
 * 企业迁移管理 流程终审
 *  本功能用于对系统的“企业迁出终审”进行操作管理！
 * @author Administrator
 *
 */
@Controller
@RequestMapping("qyqclczs")
public class QyqclczsController extends Super{
	@Autowired
	BaseService bs;
	private Map<String, Object> user = null;

	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "BusinessMove/lczs";
		} catch (Exception e) {
			e.printStackTrace();
			return "BusinessMove/lczs";
		}
	}
	
	//初始查询所有信息
	@RequestMapping(value="/queryQyqc.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String queryQyqc(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
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
			String querysql = "select s.*, j.jd_mc jdmc from fast_qyqc s, dm_jd j where s.ssjd = j.jd_dm and s.status in (1,4,5) ";
			if("0".equals(jd)) {
				if(StringUtils.isNotBlank(nsrsbh)) {
					querysql += " and s.nsrsbh = '" + nsrsbh + "' ";
				}
				if(StringUtils.isNotBlank(nsrmc)) {
					querysql += " and s.nsrmc like '%" + nsrmc + "%' ";
				}
				if(StringUtils.isNotBlank(yearNmonth)) {
					querysql += " and to_char(s.fbrq,'yyyymm') >='"+starTime+"' and to_char(s.fbrq,'yyyymm') <= '"+endTime+"' ";
				}
			} else {
				if(StringUtils.isNotBlank(nsrsbh)) {
					querysql += " and s.nsrsbh = '" + nsrsbh + "' ";
				}
				if(StringUtils.isNotBlank(nsrmc)) {
					querysql += " and s.nsrmc like '%" + nsrmc + "%' ";
				}
				if(StringUtils.isNotBlank(yearNmonth)) {
					querysql += " and to_char(s.fbrq,'yyyymm') >='"+starTime+"' and to_char(s.fbrq,'yyyymm') <= '"+endTime+"' ";
				}
				if(StringUtils.isNotBlank(jd)) {
					querysql += " and s.ssjd = '" + jd + "' ";
				}	
			}			
			querysql +=  " order by s.status,s.fbrq desc";
			System.out.println(querysql);
			List<Map<String, Object>> list = bs.query(querysql);
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	@RequestMapping(value="/removeQyqc.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String removeQyqc(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			String id = getValue(form.get("id"));	
			String LY = getValue(form.get("ly"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();	
			String msgId = UUID.randomUUID().toString();
		    msgId   = msgId.replace("-", ""); 
			String updatesql = "update FAST_QYQC set STATUS = '5' ,ZSRQ = to_date('" + formatter.format(date) + "','yyyy-mm-dd HH24:mi:ss'),SHYJ = '"+LY+"' where id = '"+id+"'";
			bs.update(updatesql);
			String insertsql ="  insert into FAST_QYQR X1 (X1.ID, X1.nsrsbh, X1.nsrmc, X1.xssjd, X1.yqrd, X1.qrrq, X1.status) SELECT ? id1, X2.nsrsbh, X2. nsrmc, X2.ssjd,X2.nqrd, X2.zsrq , X2.STATUS FROM FAST_QYQC X2 WHERE X2.ID=?";
			insertsql=this.getSql2(insertsql,new Object[] {msgId,id});
			Integer update = bs.update(updatesql);
			if(update > 0) {
				bs.insert(insertsql);
				return this.toJson("000", "终止成功！");
			}else {
				return this.toJson("009", "终止异常！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "终止异常！");
		}

	}

	//终审saveQyqczs
	@RequestMapping(value="/saveQyqczs.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String saveQyqczs(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			String id = getValue(form.get("id"));
			String LY = getValue(form.get("ly"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();	
			String updatesql = "update FAST_QYQC set STATUS = '4' ,ZSRQ = to_date('" + formatter.format(date) + "','yyyy-mm-dd HH24:mi:ss'),SHYJ = '"+LY+"' where id = '"+id+"'";
			String msgId = UUID.randomUUID().toString();
		    msgId   = msgId.replace("-", ""); 
		    String insertsql ="  insert into FAST_QYQR X1 (X1.ID, X1.nsrsbh, X1.nsrmc, X1.xssjd, X1.yqrd, X1.qrrq, X1.status) SELECT ? id1, X2.nsrsbh, X2. nsrmc, X2.ssjd,X2.nqrd, X2.zsrq , X2.STATUS FROM FAST_QYQC X2 WHERE X2.ID=?";
			insertsql=this.getSql2(insertsql,new Object[] {msgId,id});
			Integer update = bs.update(updatesql);
			if(update > 0) {
				bs.insert(insertsql);
				return this.toJson("000", "终审成功！");
			}else {
				return this.toJson("001", "终审失败！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "终审异常！");
		}
	}

	// 监听查询
	@RequestMapping(value="/doQueryByID.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doQueryByID(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			String id = getValue(form.get("ID"));
			String status = getValue(form.get("STATUS"));
			String sql = "";
			if ("0".equals(status)) {
				sql = "select FBRQ,NSRMC,NQRD from fast_qyqc where id = '"+id+"'";
			} else if ("1".equals(status) || "2".equals(status)) {
				sql = "select FBRQ,NSRMC,NQRD from fast_qyqc where id = '"+id+"' UNION ALL select FKRQ,NSRMC,NQRD from fast_qyqc where id = '"+id+"'";
			} else {
				sql = "select FBRQ,NSRMC,NQRD,STATUS from fast_qyqc where id = '"+id+"' UNION ALL select FKRQ,NSRMC,NQRD,STATUS from fast_qyqc where id = '"+id+"' UNION ALL select ZSRQ,NSRMC,NQRD,STATUS  from fast_qyqc where id = '"+id+"'";
			}

			System.out.println(sql);

			List<Map<String, Object>> list =bs.query(sql);
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

}
