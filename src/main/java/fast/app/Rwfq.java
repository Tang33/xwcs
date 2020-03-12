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

public class Rwfq extends Super{
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "qyqrqc/rwfq";
		}catch(Exception e){
			e.printStackTrace();
			return "qyqrqc/rwfq";
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
			String querysql = "select distinct nsrmc,nsrsbh from sb_nsrxx s  " + "where s.nsrmc like '%" + qymc + "%'";
			System.out.println(querysql);
			List<Map<String, Object>> list = this.getBs().query(querysql);

			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
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
			String querysql = "select a.zse dnljje,b.zse qnljje,c.dm jddm ,d.ylj sylj,e.排名 pm,f.贡献比例 gxbl  from "+ 
							  "(select sum(zse) zse from sb_nsrxx where to_char(rk_rq,'yyyy')=to_char(sysdate,'yyyy') and nsrmc='"+nsrmc+"') a,"+
							  "(select sum(zse) zse from sb_nsrxx where to_char(rk_rq,'yyyy')=to_char(sysdate,'yyyy')-1 and nsrmc='"+nsrmc+"') b,"+
							  "(select t.dm dm from (select jd_dm dm,rk_rq rq from sb_nsrxx where nsrmc='"+nsrmc+"') t where rownum=1 order by t.rq desc) c,"+
							  "(select sum(dfzse) ylj from sb_nsrxx where nsrmc='"+nsrmc+"' and rk_rq=(select max(rk_rq) from sb_nsrxx)) d,"+
							  "(select rn 排名 from (select rownum rn,a.nsrmc mc from (select nsrmc from sb_nsrxx where rk_rq=(select max(rk_rq) from sb_nsrxx) group by nsrmc order by sum(dfzse) desc) a) b where b.mc = '"+nsrmc+"') e,"+
							  "(select round((select sum(dfzse) from sb_nsrxx where nsrmc='"+nsrmc+"' and rk_rq=(select max(rk_rq) from sb_nsrxx))/(select sum(dfzse) from sb_nsrxx where rk_rq=(select max(rk_rq) from sb_nsrxx))*100,4) 贡献比例 from dual) f";
			System.out.println(querysql);
			List<Map<String, Object>> list = this.getBs().query(querysql);
			for (int j = 0; j < list.size(); j++) {
				Map<String, Object> result = list.get(j);
				String zse = String.valueOf(result.get("ZSE"));
				//System.out.println(zse);
				if (!(zse .equals("null")  || "0".equals(zse)||zse==null)) {
					//System.out.println("1111")
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
	
	public String addQyqc(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String uno = this.getRequest().getSession().getAttribute("uno").toString();
			
			String nsrsbh = getValue(this.getForm().get("nsrsbh"));
			String nsrmc = getValue(this.getForm().get("nsrmc"));
			String ssjd = getValue(this.getForm().get("ssjd"));
			String zczb = getValue(this.getForm().get("zczb"));
			String xzcdz = getValue(this.getForm().get("xzcdz"));
			String dnljsk = getValue(this.getForm().get("dnljsk"));
			String qnljsk = getValue(this.getForm().get("qnljsk"));
			String nqrd = getValue(this.getForm().get("yqrd"));
			String qcyy = getValue(this.getForm().get("qryy"));
			String filename = getValue(this.getForm().get("filename"));
			String filesrc = getValue(this.getForm().get("filesrc"));
			String SYSSQK = getValue(this.getForm().get("sylj"));
			String SYSSPM  = getValue(this.getForm().get("pm"));
			String SYSSGXBL = getValue(this.getForm().get("gxbl")).toString();
			System.out.println(SYSSGXBL);
			
			String querySql = "select f.* from fast_qyqc f where f.NSRSBH = '" + nsrsbh + "' and f.NSRMC = '" + nsrmc + "'" 
					+ " and f.SSJD = '" + ssjd + "' and f.ZCZB = '" + zczb + "'"
					+ " and f.ZCDZ = '" + xzcdz + "' and f.NQRD = '" + nqrd + "'";
			System.out.println(querySql);
			List<Map<String, Object>> list = this.getBs().query(querySql);
			if(list.size() > 0) {
				return this.toJson("500", "新增失败，已有该迁出任务！");
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			
			String insertsql = "insert into fast_qyqc(ID,NSRSBH,NSRMC,SSJD,ZCZB,ZCDZ,DNLJSK,QNLJSK,NQRD,QCYY,FILENAME,FILEURL,FBRQ,FBZ,SYSSQK,SYSSPM,SYSSGXBL)"
					+ " values(seq_fast_qyqrqc.nextval, '" + nsrsbh + "', '" + nsrmc + "', '" + ssjd + "', '" + zczb + "', '" + xzcdz
					+ "', '" + dnljsk + "', '" + qnljsk + "', '" + nqrd + "', '" + qcyy + "', '" + filename + "', '" + filesrc 
					+ "', to_date('" + formatter.format(date) + "','yyyy-MM-dd'), '" + uno +"','" 
					+SYSSQK+"',"+SYSSPM+",'"+SYSSGXBL+"')";
			System.out.println(insertsql);
			this.getBs().insert(insertsql);

			return this.toJson("000", "新增成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "新增异常！");
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
