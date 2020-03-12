package fast.app;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;
//import fast.app.gysksdbgpl;

public class Sjjggz extends Super {
	private static Connection connection = null;
	private Map<String, Object> user = null;
	private static CallableStatement callableStatement = null;

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");

			return "sssjgl/Sjjggz";
		} catch (Exception e) {
			e.printStackTrace();
			return "sssjgl/Sjjggz";
		}
	}
	
	/**
	 * 开始加工
	 * @return
	 */
	public String doClean(Map<String, Object> rmap){
		init(rmap);
		connection = getConnection();
		try{
			//固化金库对账单
		    String rk_rq = this.getBs().queryOne("select distinct rk_rq from xwcs_gsdr_temp order by rk_rq desc").get("RK_RQ").toString();
		    this.getBs().update("delete from fast_jkdzd where rk_rq='"+rk_rq+"'");
		    String sql_jkdzd="insert into fast_jkdzd select * from xwcs_gsdr_temp"; 
		    System.out.println("固化本月金库对账单："+sql_jkdzd);
		    int num = this.getBs().update(sql_jkdzd);
		    if(num > 0){
		    	System.out.println("本月金库对账单固化成功");
		    }else{
		    	System.out.println("本月金库对账单固化失败");
		    }
		    		    
		    //获取数据加工前台传来的参数（包括加工规则等）
			String data = getValue(this.getForm().get("data"));			
			String ids2 = "";
			String ids3 = "";
			JSONArray jsonArray = JSONArray.parseArray(data);
			for(Object obj : jsonArray){
				JSONObject parseObject = JSONObject.parseObject(obj.toString());
				if(parseObject.getString("BS").equals("2")){
					ids2 += parseObject.getString("ID") + ",";
				}else if(parseObject.getString("BS").equals("3")){
					ids3 += parseObject.getString("ID") + ",";
				}
			}
			ids2 = ids2.replaceAll(",$", "");
			ids3 = ids3.replaceAll(",$", "");
			
			boolean tag = true;
			tag = doMbSql("1","");
			if(tag == false){
				return this.toJson("009", "执行失败！", "err");
			}
			if(!ids2.equals("")){
				tag = doMbSql("2",ids2);
				if(tag == false){
					return this.toJson("009", "执行失败！", "err");
				}
			}
//			tag = check();//执行存储过程
//			if(tag == false){
//				return this.toJson("009", "执行失败！", "err");
//			}
			Integer rows = 0;
			rows = this.getBs().delete("truncate table xwcs_gsdr_zjb");
			rows = this.getBs().update("update xwcs_gsdr_temp t set t.zsxm_mc='企业所得税国税' where t.zsxm_mc in ( '企业所得税','企业所得税退税')");
			rows = this.getBs().update("update xwcs_gsdr_temp t set t.zsxm_mc='营改增增值税' where t.zsxm_mc like '%增值税%' and  t.yskm_mc like '%改征增值税%'");
			rows = this.getBs().update("update xwcs_gsdr_temp t set t.zsxm_mc='增值税' where t.zsxm_mc != '营改增增值税' and t.zsxm_mc like '%增值税%'");
			
			rows = this.getBs().insert("insert into xwcs_gsdr_zjb(id,nsrsbh,nsrmc,hy_mc,jd_mc,type,lr_sj,zsxm_mc,se,zse,bl,HY,ZSPM,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,yzpzxh,sksx,YSKM_MC,YSFPBL_MC)"
					+ " select seq_xwcs_gsdr_zjb.nextval,nsrsbh,nsrmc,hy_mc,jd_mc,type,sysdate,zsxm_mc,se,zse,bl,HY,ZSPM,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,yzpzxh,sksx,YSKM_MC,YSFPBL_MC "
					+ "from ( select nsrsbh,nsrmc,hy_mc,jd_mc,type,t.ZSXM_MC zsxm_mc,sum(se) se,sum(zse) zse,bl,HY,ZSPM,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,yzpzxh,sksx,YSKM_MC,YSFPBL_MC "
					+ "from xwcs_gsdr_temp t group by nsrmc,nsrsbh,hy_mc,zsxm_mc,jd_mc,type,bl,HY,ZSPM,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,yzpzxh,sksx,YSKM_MC,YSFPBL_MC)");
			
			rows = this.getBs().update("Update xwcs_gsdr_zjb a set a.jd_dm = '00', a.hy_dm = '00', a.zdsyh = '0', a.wq = '0'");
			rows = this.getBs().update("update xwcs_gsdr_zjb t set t.zsxm_dm= (select z.zsxm_dm from dm_zsxm z where t.zsxm_mc=z.zsxm_mc and z.xybz='Y' and rownum = 1) where exists (select 1 from dm_zsxm zz where t.zsxm_mc=zz.zsxm_mc and zz.xybz='Y')");
			rows = this.getBs().update("Update xwcs_gsdr_zjb t set t.jd_dm = (select distinct jd.jd_dm from DM_JD jd where INSTR(t.jd_mc, jd.jd_mc) > 0)  where exists (select 1 from DM_JD where INSTR(t.jd_mc, DM_JD.jd_mc) > 0)");
			rows = this.getBs().update("Update xwcs_gsdr_zjb t set t.hy_dm = (select  distinct hy.hyml_dm from dm_hydl hy where trim(t.hy_mc) = trim(hy.hydl_jc) and hy.xybz = 'Y') Where exists (select 1 from dm_hydl where trim(t.hy_mc) = trim(dm_hydl.hydl_jc) and dm_hydl.xybz = 'Y')");
			rows = this.getBs().update("Update xwcs_gsdr_zjb t set (t.jd_dm, t.wq) = (select distinct wq.jd_dm, '1' from DJ_GS_WQSYH wq where t.nsrmc = wq.nsrmc) where exists (select 1 from DJ_GS_WQSYH where t.nsrmc = DJ_GS_WQSYH.nsrmc)");
			rows = this.getBs().update("Update xwcs_gsdr_zjb t set (t.jd_dm, t.hy_dm, t.zdsyh) = (select distinct nvl(dqy.jd_dm, '00'), nvl(dqy.hy_dm, '00'), '1' from DJ_GS_DQYQC dqy where t.nsrmc = dqy.nsrmc) where exists (select 1 from DJ_GS_DQYQC where t.nsrmc = DJ_GS_DQYQC.Nsrmc)");
			rows = this.getBs().update("update xwcs_gsdr_zjb t set t.jd_dm = '01' Where exists (select 1 from Dj_Bkfqy where t.nsrmc = trim(Dj_Bkfqy.nsrmc))");
			rows = this.getBs().update("update xwcs_gsdr_zjb t set t.jd_dm = '99'  Where exists (select 1 from DJ_XZ where t.nsrmc = trim(DJ_XZ.nsrmc))");
			rows = this.getBs().update("update xwcs_gsdr_zjb t set t.jd_mc=(select d.jd_mc from dm_jd d where t.jd_dm=d.jd_dm) where exists(select 1 from dm_jd d where t.jd_dm=d.jd_dm)");
			//xwcs_dsdr_temp这张表不存在，关于登记纳税人信息的维护通过本系统税务登记功能维护
			//			this.getBs().insert("insert into dj_nsrxx d (d.nsrsbh,d.nsrmc,d.jd_dm,hangye,d.hydl_dm) select distinct t.nsrsbh,t.nsrmc,t.jd_dm,t.hy_mc,t.hy_dm from xwcs_dsdr_temp t where not exists(select 1 from dj_nsrxx dd where (dd.nsrmc=t.nsrmc or DD.NSRSBH=t.NSRSBH))");
			
			String rkrq = getRkrq();
			System.out.println("正在导入征收信息表。。。。。");
			tag = doLoadup(rkrq);//导入征收信息表
			if(tag == false){
				return this.toJson("009", "执行失败！", "err");
			}
			System.out.println("正在导入纳税人信息表。。。。。");
			tag = dr(rkrq);//导入纳税人信息表
			
			if(!ids3.equals("")){
				tag = doMbSql("3",ids3);
				if(tag == false){
					return this.toJson("009", "执行失败！", "err");
				}
			}			
			//首页关系表更新
			Analysis();
			
			//清空表
			/*this.getBs().delete("truncate table xwcs_gsdr_yssjrk");
			this.getBs().delete("truncate table xwcs_gsdr_yssjtk");
			this.getBs().delete("truncate table xwcs_gsdr_yssjgs");*/
			//this.getBs().delete("truncate table xwcs_gsdr_temp");
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("加工失败！");
		}finally{
			close();
			System.out.println("加工程序结束");
		}
		return this.toJson("000", "执行完成！", "success");
		
	}
	
	public boolean Analysis(){
	        Connection conn = null;
	        String sql = "";
	        String proceName = "xwcs_gsdr_sygx";
	        String[] parameterArr = {"",""};
			try {
				conn = getConnection();
				conn.setAutoCommit(true);
				if(parameterArr.length > 0){
					sql = "call "+proceName+" (";
					for(int i = 0;i<parameterArr.length;i++){
						sql = sql + "?,";
					}
					sql = sql.replaceAll(",$", ")");
					CallableStatement stat = conn.prepareCall(sql);
					
					for(int i = 0;i<parameterArr.length;i++){
										
						stat.setObject(i+1, parameterArr[i]);
					}
					stat.execute();
					System.out.println("首页关系表执行完成！");
				}else{
					sql = "call " + proceName + "()";
					CallableStatement stat = conn.prepareCall(sql);
					stat = conn.prepareCall(sql);
					stat.execute();
					System.out.println("首页关系表执行完成！");
				}
			}catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return true;
	}
	
	public boolean dr(String rkrq) {
		boolean tag = false;
		try {
			String istSql = "insert into sb_nsrxx(xh,dzsphm,nsrmc,rk_rq,jd_dm,hy_dm,zzs,ygzzzs,yys,qysds_gs,qysds_ds,qysds,grsds,fcs,yhs,ccs,cswhjss,dfjyfj,jyfj,cztdsys,hbs,zse,dfzse,qyxz,hhnsrmc,lrry_dm,lr_sj,gds,bl,zspm,zspmdm,hydl,hyzl,yskmdm,djxhs) ";
			istSql += "  select seq_sb_nsrxx.nextval xh,s.*"
					+ " from (select distinct z.dzsphm,z.nsrmc,z.rk_rq,z.jd_dm,z.hy_dm,"
					+ "     sum(decode(z.zsxm, '增值税', zse, 0)) as zzs,"
					+ "     sum(decode(zsxm, '营改增增值税', zse, 0)) as ygz,"
					+ "     sum(decode(zsxm, '营业税', zse, 0)) as yys,"
					+ "     sum(decode(zsxm, '企业所得税国税', zse, 0)) as qysdsgs,"
					+ "     sum(decode(zsxm, '企业所得税', zse, 0)) as qysdsds,"
					+ "     (sum(decode(zsxm, '企业所得税国税', zse, 0))+sum(decode(zsxm, '企业所得税', zse, 0))) as qysds,"
					+ "     sum(decode(zsxm, '个人所得税', zse, 0)) as grsds,"
					+ "     sum(decode(zsxm, '房产税', zse, 0)) as fcs," + "     sum(decode(zsxm, '印花税', zse, 0)) as yhs,"
					+ "     sum(decode(zsxm, '车船税', zse, 0)) as ccs,"
					+ "     sum(decode(zsxm, '城市维护建设税', zse, 0)) as cswhjss,"
					+ "     sum(decode(zsxm, '地方教育附加', zse, 0)) as dfjyfj,"
					+ "     sum(decode(zsxm, '教育费附加', zse, 0)) as jyfj,"
					+ "     sum(decode(zsxm, '城镇土地使用税', zse, 0)) as cztdsys,"
					+ "     sum(decode(zsxm, '环境保护税', zse, 0)) as hbs," + "     sum(zse) zse,"
					+ "     sum(z.sjse) dfzse," + "     z.qyxz," + "     z.nsrmc hhnsrmc," + "     z.lrry_dm,"
					+ "     z.lr_sj," + "     gds,bl,z.zspm,z.zspmdm,z.hydl,z.hyzl,z.yskmdm,z.djxhs" + " from sb_zsxx z"
					+ " where to_char(rk_rq, 'yyyyMM') = '" + rkrq + "'"
					+ " and gds = '0' group by nsrmc,rk_rq,jd_dm,hy_dm,z.qyxz,z.lrry_dm,z.lr_sj,gds,bl,z.zspm,z.zspmdm,z.hydl,z.hyzl,z.yskmdm,z.dzsphm,z.djxhs) s ";
			System.out.println(istSql);
			this.getBs().insert(istSql);
			tag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tag;
	}
	
	public boolean doLoadup(String rkrq) {
		boolean tag = false;
		try {
			String czy_dm = getValue(user.get("UUID"));
			String sql = "insert into sb_zsxx s(xh,nsrsbh,nsrmc,hy,zsxm,zse,rk_rq,gds,sjse,sz_dm,"
					+ "hy_dm,jd_dm,zdsyh,wq,lrry_dm,lr_sj,qyxz,sfbg,bl,zspm,zspmdm,hydl,hyzl,yskmdm,dzsphm,djxhs,YZPZXH,SKSX,JD_MC,YSKM_MC,YSFPBL_MC) "
					+ "select S_SB_NSRXX.nextval,nsrsbh,nsrmc,x.hy_mc,x.zsxm_mc,x.zse,to_date('" + rkrq
					+ "','yyyyMM')," + "'0',x.se,zsxm_dm,hy_dm,jd_dm,x.zdsyh,x.wq,'" + czy_dm
					+ "',sysdate,'J','0',bl,x.zspm,x.zspmdm,x.hydl,x.hyzl,x.yskmdm,x.dzsphm,x.djxhs,x.YZPZXH,x.SKSX,x.JD_MC,x.YSKM_MC,x.YSFPBL_MC  from xwcs_gsdr_zjb x";
					
			this.getBs().insert(sql);
			tag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tag;
	}
	
	public boolean check(){
		List<Mode> paramsList = new ArrayList<Mode>();
		paramsList.add(new Mode("in", "String", "1"));
		paramsList.add(new Mode("out", "String", ""));
		Object obj = JdbcConnectedPro.call("xwcs_gsdr_bd_new", paramsList);
		String rs = (String) obj;
		if(rs.equals("1")){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean doMbSql(String bs,String ids){
		boolean tag = true;
		try {
			if(connection == null){
				connection = getConnection();
			}
			connection.setAutoCommit(false);
			Statement cs = connection.createStatement();
			Statement cs1 = connection.createStatement();
			String  sql = "";
			if(bs.equals("1")){
				sql = "select * from fast_mb where status = '1' and bs = '1' order by id";
			}else if(!ids.equals("")){
				sql = "select * from fast_mb where id in (" + ids + ") order by id";
			}
			ResultSet rs = cs.executeQuery(sql);
			while(rs.next()){
				String doSql = rs.getString("SQL")== null ? "" : rs.getString("SQL").replaceAll(";$", "");
				if(doSql.equals("")){
					continue;
				}
				try{
					int num = cs1.executeUpdate(doSql);
					System.out.println("规则id：" + rs.getObject("ID") + "执行完成,影响了"+num+"条记录");
				}catch(Exception e){
					tag=false;
					e.printStackTrace();
					System.err.println("规则id：" + rs.getObject("ID") + "执行失败");
					break;
				}
			}
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return tag;
	}
	
	public String getRkrq (){
		String rkrq = "";
//		if(connection == null){
//			connection = getConnection();
//		}
		try {
//			Statement cs = connection.createStatement();
//			ResultSet rs = cs.executeQuery("select rk_rq from xwcs_gsdr_temp where rk_rq is not null");
//			if(rs.next()){
//				rkrq = rs.getString(1);
//			}
			Map<String, Object> queryOne = this.getBs().queryOne("select rk_rq from xwcs_gsdr_temp where rk_rq is not null");
			rkrq = queryOne.get("RK_RQ") == null ? "" : queryOne.get("RK_RQ").toString();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return rkrq;
	}
	
	/**
	 * 获取模板信息
	 * @return
	 */
	public String getMb(Map<String, Object> rmap){
		initMap(rmap);
		if(connection == null){
			connection = getConnection();
		}
		try {
			List<Map<String,String>> mbInfo = getMbInfo();
			return this.toJson("000", "查询成功！", mbInfo);
		} catch (SQLException e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！", "");
		}finally{
			close();
		}
	}
	
	
	
	public String getRule(Map<String, Object> rmap) {
		initMap(rmap);
		String sql = "select mbmc,id,bs from fast_mb where bs = '1' and status = '1'";
		List<Map<String, Object>> list = this.getBs().query(sql);
		
		return this.toJson("000", "查询成功",list);
	}
	
	
	
	public List<Map<String,String>> getMbInfo() throws SQLException{
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();;
		connection = getConnection();
		Statement cs = connection.createStatement();
		ResultSet rs = cs.executeQuery("select * from FAST_MB where STATUS = '1' and BS != '1' order by bs,id");
		while(rs.next()){
			Map<String,String> map = new LinkedHashMap<String,String>();
			map.put("MBMC", rs.getString("MBMC") == null ? "" : rs.getString("MBMC"));
			map.put("ID", rs.getString("ID") == null ? "" : rs.getString("ID"));
			map.put("BS", rs.getString("BS") == null ? "" : rs.getString("BS"));
			list.add(map);
		}
		connection.close();
		return list;
	}
	
	
	
	
	public static Connection getConnection() {

		try {
			Properties properties = new Properties();
			// 使用ClassLoader加载properties配置文件生成对应的输入流
			InputStream in = JdbcConnectedPro.class.getClassLoader().getResourceAsStream("conf/jdbc.properties");
			// 使用properties对象加载输入流
			properties.load(in);
			// 获取key对应的value值
			String driver = properties.getProperty("jdbc.driver");
			String url = properties.getProperty("jdbc.url");
			String username = properties.getProperty("jdbc.username");
			String pwd = properties.getProperty("jdbc.password");
			// 创建dataSource
			BasicDataSource dataSource = new BasicDataSource();
			// 加载数据库驱动
			dataSource.setDriverClassName(driver);
			// 设置用户名和密码
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(pwd);
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		return connection;
	}
	
	public static void close() {
		if (null != callableStatement) {
			try {
				callableStatement.close();
				if (null != connection) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	}

}
