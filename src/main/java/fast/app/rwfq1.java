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

public class rwfq1 extends Super {
	
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "qrqcgl/rwfq";
		} catch (Exception e) {
			e.printStackTrace();
			return "qrqcgl/rwfq";
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
			String dwid = this.getRequest().getSession().getAttribute("dwid").toString();
			String querysql = "";
			if("00".equals(dwid) || "01".equals(dwid)) {
				querysql = "select distinct A.nsrmc,A.JD_DM,B.JD_MC from sb_nsrxx A,DM_JD B where A.JD_DM = B.JD_DM AND A.nsrmc like '%"+qymc+"%' ORDER BY A.NSRMC";
			} else {
				querysql = "select distinct A.nsrmc,A.JD_DM,B.JD_MC from sb_nsrxx A,DM_JD B where A.JD_DM = B.JD_DM AND A.JD_DM='"+dwid+"' AND A.nsrmc like '%"+qymc+"%' ORDER BY A.NSRMC";
			}
			List<Map<String, Object>> list = this.getBs().query(querysql);
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	public String addQyqc(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String dwid = this.getRequest().getSession().getAttribute("dwid").toString();
			String nsrsbh = getValue(this.getForm().get("nsrsbh"));
			String nsrmc = getValue(this.getForm().get("nsrmc"));
			String ssjd = getValue(this.getForm().get("ssjd"));
			String zczb = getValue(this.getForm().get("zczb"));
			String dnljsk = getValue(this.getForm().get("dnljsk"));
			String qnljsk = getValue(this.getForm().get("qnljsk"));
			String SYSSQK = getValue(this.getForm().get("sylj"));
			String SYSSPM  = getValue(this.getForm().get("pm"));
			String SYSSGXBL = getValue(this.getForm().get("gxbl"));
			String nqrd = getValue(this.getForm().get("jdlist"));
			String qcyy = getValue(this.getForm().get("qryy"));
			if("0".equals(nqrd)) {
				return this.toJson("501", "请选择迁入地！");
			}
			String querySql = "select * from FAST_QYQC WHERE NSRMC = '"+nsrmc+"' and NSRSBH = '"+nsrsbh+"' and SSJD = (SELECT jd_dm from DM_JD WHERE jd_mc = '"+ssjd+"') and NQRD = '"+nqrd+"' and STATUS = '0'";
			List<Map<String, Object>> count = this.getBs().query(querySql);
			if(count.size() > 0) {
				return this.toJson("500", "新增失败，已有该迁出任务！");
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			
			String insertsql = "insert into fast_qyqc(ID,NSRSBH,NSRMC,SSJD,ZCZB,DNLJSK,QNLJSK,NQRD,QCYY,FBRQ,FBZ,SYSSQK,SYSSPM,SYSSGXBL,STATUS)"
					+ " values(seq_fast_qyqrqc.nextval, '" + nsrsbh + "', '" + nsrmc + "',(SELECT jd_dm from DM_JD WHERE jd_mc = '"+ssjd+"'), '" + zczb
					+ "', '" + dnljsk + "', '" + qnljsk + "',(SELECT jd_mc from DM_JD WHERE jd_dm = '"+nqrd+"'), '" + qcyy
					+ "', to_date('" + formatter.format(date) + "','yyyy-mm-dd HH24:mi:ss'), '" + dwid +"','" 
					+SYSSQK+"',"+SYSSPM+",'"+SYSSGXBL+"','0')";
			this.getBs().insert(insertsql);	
			return this.toJson("000", "新增成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "新增异常！");
		}
	}

	/**
	 * 查询企业信息
	 * 
	 * @param rmap
	 * @return
	 */
	public String queryAll(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String nsrmc = getValue(this.getForm().get("nsrmc"));
			String jd = getValue(this.getForm().get("jd_dm"));
			String querysql = "select a.zse dnljje,b.zse qnljje,d.ylj sylj,e.排名 pm,f.贡献比例 gxbl  from "+ 
							  "(select sum(zse) zse from sb_nsrxx where to_char(rk_rq,'yyyy')=to_char(sysdate,'yyyy') and nsrmc='"+nsrmc+"' and jd_dm ='"+jd+"') a,"+
							  "(select sum(zse) zse from sb_nsrxx where to_char(rk_rq,'yyyy')=to_char(sysdate,'yyyy')-1 and nsrmc='"+nsrmc+"' and jd_dm ='"+jd+"') b,"+
							  "(select sum(dfzse) ylj from sb_nsrxx where nsrmc='"+nsrmc+"' and jd_dm ='"+jd+"' and rk_rq=(select max(rk_rq) from sb_nsrxx)) d,"+
							  "(select rn 排名 from (select rownum rn,a.nsrmc mc from (select nsrmc from sb_nsrxx where rk_rq=(select max(rk_rq) from sb_nsrxx) group by nsrmc order by sum(dfzse) desc) a) g where g.mc = '"+nsrmc+"') e,"+
							  "(select round((select sum(dfzse) from sb_nsrxx where nsrmc='"+nsrmc+"' and jd_dm ='"+jd+"' and rk_rq=(select max(rk_rq) from sb_nsrxx))/(select sum(dfzse) from sb_nsrxx where rk_rq=(select max(rk_rq) from sb_nsrxx))*100,4) 贡献比例 from dual) f";
			System.out.println(querysql);
			List<Map<String, Object>> list = this.getBs().query(querysql);
			for (int j = 0; j < list.size(); j++) {
				Map<String, Object> result = list.get(j);
				String zse = String.valueOf(result.get("ZSE"));
				if (!("null".equals(zse)  || "0".equals(zse)|| zse==null)) {
					BigDecimal bd1 = getBigDecimal(result.get("dnljje")).setScale(2, RoundingMode.HALF_UP);
					BigDecimal bd2 = getBigDecimal(result.get("qnljje")).setScale(2, RoundingMode.HALF_UP);
					BigDecimal bd3 = getBigDecimal(result.get("sylj")).setScale(2, RoundingMode.HALF_UP);
					DecimalFormat dft = new DecimalFormat("0.00");
					
					result.replace("dnljje", dft.format(bd1));
					result.replace("qnljje", dft.format(bd2));
					result.replace("sylj", dft.format(bd3));
				}
			}
			return this.toJson("000", "查询成功！", list);
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
