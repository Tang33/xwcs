package fast.main.controller.ImportTaxData;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import fast.main.service.BaseService;
import fast.main.util.DruidUtil;
import fast.main.util.Super;

/**
 * 税收数据加工
 * 本页面用于对导入数据进行加工
 *
 */
@Controller
@RequestMapping("sssjjg")
public class SssjjgController extends Super {

	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "ImportTaxData/Sssjjg";
		} catch (Exception e) {
			e.printStackTrace();
			return "ImportTaxData/Sssjjg";
		}
	}
	
	private static Connection connection = null;
	private static PreparedStatement pstm = null;
	private static Statement stm = null;
	
	/**
	 * 加工数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("sjjg_doClean.do")
	@ResponseBody
	public String doClean(HttpServletRequest request, HttpServletResponse response){
			user = (Map<String, Object>)request.getSession().getAttribute("user");
			String uid = "";
			try{
				long time1 = System.currentTimeMillis();	
				//固化金库对账单
				Map<String, Object> map =  bs.queryOne("select distinct rk_rq from xwcs_gsdr_temp");
				
				if(map==null) {//map==null
					
					return toJson("008", "无加工数据!");
				}
				if(map!=null) {
					String rk_rq = map.get("RK_RQ").toString();
				    //更新金库对账单
				    bs.update("delete from fast_jkdzd where rk_rq='"+rk_rq+"'");
				    String sql_jkdzd="insert into fast_jkdzd select * from xwcs_gsdr_temp";

				    int num = bs.update(sql_jkdzd);
				    if(num > 0){
				    	System.out.println("本月金库对账单固化成功");
				    }else{
				    	System.out.println("本月金库对账单固化失败");
				    }
	
				}
				//插入加工状态表
				uid =  UUID.randomUUID().toString().replace("-", "");
				
			    bs.insert("insert into xwcs_jgzt(zt ,starttime ,createuser ,isdelete ,name ,RWSBM)"
			    		+ " values('1',sysdate,'"+getValue(user.get("UUID"))+"','0','数据加工','"+uid+"')");

			    //获取数据加工前台传来的参数（包括加工规则等）
				String data = getValue(request.getParameter("data"));	
				String ids1 = "";
				String ids2 = "";
				String ids3 = "";
				JSONArray jsonArray = JSONArray.parseArray(data);
				for(Object obj : jsonArray){
					JSONObject parseObject = JSONObject.parseObject(obj.toString());
					if(parseObject.getString("BS").equals("1")){
						ids1 += parseObject.getString("ID") + ",";
					}else if(parseObject.getString("BS").equals("2")){
						ids2 += parseObject.getString("ID") + ",";
					}else if(parseObject.getString("BS").equals("3")){
						ids3 += parseObject.getString("ID") + ",";
					}
				}
				ids1 = ids1.replaceAll(",$", "");
				ids2 = ids2.replaceAll(",$", "");
				ids3 = ids3.replaceAll(",$", "");
				
				String tag = "";
				tag = doMbSql("1",ids1,uid);//最高优先级
				if(tag.length()>1){
					return this.toJson("009", tag, "err");
				}
				if(!ids2.equals("")){//执行第二优先级 不为空就执行
					tag = doMbSql("2",ids2,uid);
					if(tag.length()>1){
						return this.toJson("009",tag, "err");
					}
				}
	//			tag = check();//执行存储过程
	//			if(tag == false){
	//				return this.toJson("009", "执行失败！", "err");
	//			}
				bs.delete("truncate table xwcs_gsdr_zjb");//清空中间表
				bs.update("update xwcs_gsdr_temp t set t.zsxm_mc='企业所得税国税' where t.zsxm_mc in ( '企业所得税','企业所得税退税')");
				
				bs.update("update xwcs_gsdr_temp t set t.zsxm_mc='营改增增值税' where t.zsxm_mc like '%增值税%' and  t.yskm_mc like '%改征增值税%'");
				
				bs.update("update xwcs_gsdr_temp t set t.zsxm_mc='增值税' where t.zsxm_mc != '营改增增值税' and t.zsxm_mc like '%增值税%'");
				
				bs.insert("insert into xwcs_gsdr_zjb(id,nsrsbh,nsrmc,hy_mc,jd_mc,type,lr_sj,zsxm_mc,se,zse,bl,HY,ZSPM,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM"
						+ ",DJXHS,yzpzxh,sksx,YSKM_MC,YSFPBL_MC) select seq_xwcs_gsdr_zjb.nextval,nsrsbh,nsrmc,hy_mc,jd_mc,type,sysdate,zsxm_mc,se,zse,bl"
						+ ",HY,ZSPM,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,yzpzxh,sksx,YSKM_MC,YSFPBL_MC "
						+ "from ( select nsrsbh,nsrmc,hy_mc,jd_mc,type,t.ZSXM_MC zsxm_mc,sum(se) se,sum(zse) zse,bl,HY,ZSPM,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM"
						+ ",DJXHS,yzpzxh,sksx,YSKM_MC,YSFPBL_MC from xwcs_gsdr_temp t group by nsrmc,nsrsbh,hy_mc,zsxm_mc,jd_mc,type,bl,HY,ZSPM,ZSPMDM,HYDL"
						+ ",HYZL,YSKMDM,DZSPHM,DJXHS,yzpzxh,sksx,YSKM_MC,YSFPBL_MC)");
				//修改   街道代码 行业代码  外区 为默认管理员
				bs.update("Update xwcs_gsdr_zjb a set a.jd_dm = '00', a.hy_dm = '00', a.zdsyh = '0', a.wq = '0'");
				//修改 税种   zsxm_dm 改之前为空  
				bs.update("update xwcs_gsdr_zjb t set t.zsxm_dm= (select z.zsxm_dm from dm_zsxm z where t.zsxm_mc=z.zsxm_mc and z.xybz='Y' and rownum = 1) "
						+ "where exists (select 1 from dm_zsxm zz where t.zsxm_mc=zz.zsxm_mc and zz.xybz='Y')");
				//根据 街道名称修改街道代码
				bs.update("Update xwcs_gsdr_zjb t set t.jd_dm = (select distinct jd.jd_dm from DM_JD jd where INSTR(t.jd_mc, jd.jd_mc) > 0)"
						+ "  where exists (select 1 from DM_JD where INSTR(t.jd_mc, DM_JD.jd_mc) > 0)");
				//根据行业名称 修改行业代码
				bs.update("Update xwcs_gsdr_zjb t set t.hy_dm = (select  distinct hy.hyml_dm from dm_hydl hy where trim(t.hy_mc) = trim(hy.hydl_jc) and hy.xybz = 'Y')"
						+ " Where exists (select 1 from dm_hydl where trim(t.hy_mc) = trim(dm_hydl.hydl_jc) and dm_hydl.xybz = 'Y')");
				// 根据 用户清册 修改 街道代码 和 外区代码  根据纳税人名称修改
				bs.update("Update xwcs_gsdr_zjb t set (t.jd_dm, t.wq) = (select distinct wq.jd_dm, '1' from DJ_GS_WQSYH wq where t.nsrmc = wq.nsrmc)"
						+ " where exists (select 1 from DJ_GS_WQSYH where t.nsrmc = DJ_GS_WQSYH.nsrmc)");
				// 根据 国税大企业清册 修改 街道代码 和 外区代码  根据纳税人名称修改
				bs.update("Update xwcs_gsdr_zjb t set (t.jd_dm, t.hy_dm, t.zdsyh) = (select distinct nvl(dqy.jd_dm, '00'), "
						+ "nvl(dqy.hy_dm, '00'), '1' from DJ_GS_DQYQC dqy where t.nsrmc = dqy.nsrmc)"
						+ " where exists (select 1 from DJ_GS_DQYQC where t.nsrmc = DJ_GS_DQYQC.Nsrmc)");
				// 根据不可分企业清册 修改 街道代码 为01  为什么为01
				bs.update("update xwcs_gsdr_zjb t set t.jd_dm = '01' Where exists (select 1 from Dj_Bkfqy where t.nsrmc = trim(Dj_Bkfqy.nsrmc))");
				// 徐庄企业清册 修改 街道代码 为 99 为什么为99 
				bs.update("update xwcs_gsdr_zjb t set t.jd_dm = '99'  Where exists (select 1 from DJ_XZ where t.nsrmc = trim(DJ_XZ.nsrmc))");
				// 根据xwcs_gsdr_zjb里面的 街道代码修改街道名称
				bs.update("update xwcs_gsdr_zjb t set t.jd_mc=(select d.jd_mc from dm_jd d where t.jd_dm=d.jd_dm)"
						+ " where exists(select 1 from dm_jd d where t.jd_dm=d.jd_dm)");
				
							
				//首页关系表更新
				Analysis(ids3,uid);
				
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("加工失败！");
				bs.update("update  XWCS_JGZT set zt = '2' where RWSBM='"+uid+"'");
				return this.toJson("009", "执行失败！", "err");
			}finally{
				//close();
				System.out.println("加工程序结束");
			}
		return this.toJson("000", "执行完成！", "success");
			
	}

	private Map<String, Object> user = null;
	@Autowired
	BaseService bs;
	
	/**
	 * 查询获取拆分规则
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping("sjjg_getMb.do")
	@ResponseBody
	public String getMb() throws IOException, SQLException{
		
		try {
			List<Map<String,String>> mbInfo = getMbInfo();
			return this.toJson("000", "查询成功！", mbInfo);
		} catch (SQLException e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！", "");
		}
	}
	
		
	@RequestMapping("sjjg_refresh.do")
	@ResponseBody
	public String refresh() {
		Date date = new Date();
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
		String time = dateFormat.format(date);
		try {
			one();
			tow();
			Long times2 = System.currentTimeMillis();
			three();
			Long times3 = System.currentTimeMillis();
			System.err.println("执行THREE时间总耗时"+((times3-times2)/1000)+"秒");
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			System.out.println("加工失败");
			bs.update("flashback table FAST_NHYZB to timestamp to_timestamp('"+time+"','yyyy-mm-dd HH24:MI:SS')");
			bs.update("flashback table FAST_YLJ to timestamp to_timestamp('"+time+"','yyyy-mm-dd HH24:MI:SS')");
			bs.update("flashback table FAST_YBGGYS to timestamp to_timestamp('"+time+"','yyyy-mm-dd HH24:MI:SS')");
			bs.update("flashback table fast_nszzb to timestamp to_timestamp('"+time+"','yyyy-mm-dd HH24:MI:SS')");
			e.printStackTrace();
			return this.toJson("009", "查询失败！", "");
		}
	}
	
	/**
	 * 获取右侧的拆分规则
	 * @return
	 */
	@RequestMapping("sjjg_getRule.do")
	@ResponseBody
	public String getRule() {
		
		String sql = "select mbmc,id,bs from fast_mb where bs = '1' and status = '1'";
		List<Map<String, Object>> list = bs.query(sql);
		
		return this.toJson("000", "查询成功",list);
	}
	
	public String doMbSql(String bs,String ids,String uid) throws SQLException{
		
		connection = DruidUtil.getConnection();
		connection.setAutoCommit(false);
		Statement cs = connection.createStatement();
		Statement cs1 = connection.createStatement();
		String  sql = "";
		String str = "";
		if(bs.equals("1")){
			//sql = "select * from fast_mb where status = '1' and bs = '1' order by id";
			sql = "select * from fast_mb where id in (" + ids + ") order by id";
			ResultSet rs = cs.executeQuery(sql);//获取规则
			while(rs.next()){
				String doSql = rs.getString("SQL")== null ? "" : rs.getString("SQL").replaceAll(";$", "");
				if(doSql.equals("")){
					continue;
				}
				try{
					int num = cs1.executeUpdate(doSql);//执行规则
					System.out.println("规则id：" + rs.getObject("ID") + "执行完成,影响了"+num+"条记录");
				}catch(SQLException e){
					e.printStackTrace();
					this.bs.update("update  XWCS_JGZT set zt = '2' where RWSBM='"+uid+"'");
					System.err.println("规则id：" + rs.getObject("ID") + "执行失败");
					str +="规则id：" + rs.getObject("ID") + "执行失败,";
					//break;
				}
			}
			connection.commit();
		}
		if(ids.equals("2")){
			sql = "select * from fast_mb where id in (" + ids + ") order by id";
			ResultSet rs = cs.executeQuery(sql);//获取规则
			
			while(rs.next()){
				String doSql = rs.getString("SQL")== null ? "" : rs.getString("SQL").replaceAll(";$", "");
				if(doSql.equals("")){
					continue;
				}
				try{
					int num = cs1.executeUpdate(doSql);//执行规则
					System.out.println("规则id：" + rs.getObject("ID") + "执行完成,影响了"+num+"条记录");
				}catch(SQLException e){
					e.printStackTrace();
					this.bs.update("update  XWCS_JGZT set zt = '2' where RWSBM='"+uid+"'");
					System.err.println("规则id：" + rs.getObject("ID") + "执行失败");
					str +="规则id：" + rs.getObject("ID") + "执行失败,";
					//break;
				}
			}
			connection.commit();
		}
		
		if(bs.equals("3")){
			String id = "";
				String rk_rqMax = "SELECT to_char(max(RK_RQ),'yyyyMM') as rkrq FROM SB_NSRXX ORDER BY RK_RQ DESC";
				List<Map<String, Object>> result_rkrq = this.bs.query(rk_rqMax);
				String rkrq = result_rkrq.get(0).get("RKRQ").toString();
				String sql2 = "select id,to_char(NSRMC) NSRMC,to_char(SQL) SQl,jd,sz from FAST_MB t where id in ("+ids+") and  bs = 3";
				List<Map<String,Object>> mb = this.bs.query(sql2);
				for(int i = 0 ; i < mb.size() ; i++){
					id = mb.get(i).get("ID").toString();
					String jddm = getValue(mb.get(i).get("JD"));
					String sz = getValue(mb.get(i).get("SZ"));
					String nsrmc = getValue(mb.get(i).get("NSRMC"));
					//String mbSql = getValue(mb.get(i).get("SQL"));
					
					try {
						QysksdbgplController qs = new QysksdbgplController();
						qs.PLXG2(nsrmc, sz, rkrq, jddm,this.bs);
						//request.getRequestDispatcher(mbSql+"?jddm="+jddm+"&nsrmcs="+nsrmc+"&sz="+sz+"&rkrq="+rkrq).forward(request, response);
					} catch (Exception e) {
						System.err.println("规则执行失败ID:"+id);
						str +="规则执行失败ID:"+id;
						e.printStackTrace();
					} 
				}
				
		}
		
		return str;
	}
	
	/**
	 * 查询数据加工的任务详情
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("sjjg_getTable.do")
	@ResponseBody
	public String getTable(HttpServletRequest request, HttpServletResponse response) {
		
		List<Map<String , Object>> list = bs.query("select * from XWCS_JGZT where isdelete = '0' and NAME='数据加工' order by STARTTIME desc");
		
		return toJson("000", "OK",list);
		
	}
	
	/**
	 * 删除任务详情中的任务记录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("sjjg_deleteTable.do")
	@ResponseBody
	public String deleteTable(HttpServletRequest request, HttpServletResponse response) {
		
		String id = getValue(request.getParameter("id"));
//		Integer rows = bs.queryCount("select * from   XWCS_JGZT where id='"+id+"' and zt ='1'");
//		if(rows>0) {
//			return toJson("001", "加工中不允许删除");
//		}
		bs.update("update  XWCS_JGZT set isdelete = '1' where id='"+id+"'");
		Integer rows = bs.queryCount("select * from XWCS_JGZT where zt='1' and NAME='数据加工' and isdelete='0'");
		request.setAttribute("JGZT", rows);
		Map<String,Integer> map = new HashMap<>();
		map.put("code", rows);
		return toJson("000", "OK" ,map);
		
	}
	
	/**
	 * 数据加工验证信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("sjjg_ifTable.do")
	@ResponseBody
	public String ifTable(HttpServletRequest request, HttpServletResponse response) {
		

		Integer rows = bs.queryCount("select * from XWCS_JGZT where zt='1' and NAME='数据加工' and isdelete='0'");
		request.setAttribute("JGZT_SJJG", rows);
		Map<String,Integer> map = new HashMap<>();
		map.put("code", rows);
		return toJson("000", "OK" ,map);
		
	}
	public void Analysis(String ids3,String uid){
		new Thread() {
			public void run() {
				long time1 = System.currentTimeMillis();
				//获取当前时间
				Date date = new Date();
				SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
				String time = dateFormat.format(date);
			
				
					String rkrq = getRkrq();//获取临时表的日期
					System.out.println("正在导入征收信息表。。。。。");
					//导入征收信息表
					try {
						doLoadup(rkrq);
					} catch (Exception e1) {
						bs.update("update  XWCS_JGZT set zt = '2' where RWSBM='"+uid+"'");
						
						bs.update("flashback table SB_ZSXX to timestamp to_timestamp('"+time+"','yyyy-mm-dd HH24:MI:SS')");
						e1.printStackTrace();
					}
					
					System.out.println("正在导入纳税人信息表。。。。。");
					//导入纳税人信息表
					try {
						dr(rkrq);
					} catch (Exception e1) {
						bs.update("update  XWCS_JGZT set zt = '2' where RWSBM='"+uid+"'");
						bs.update("flashback table SB_ZSXX to timestamp to_timestamp('"+time+"','yyyy-mm-dd HH24:MI:SS')");
						bs.update("flashback table SB_NSRXX to timestamp to_timestamp('"+time+"','yyyy-mm-dd HH24:MI:SS')");
						e1.printStackTrace();
					}

					//清空临时表
					bs.delete("truncate table xwcs_gsdr_temp");
					Long times4 = System.currentTimeMillis();
					System.err.println("执行导入纳税人信息表,正在导入征收信息表时间总耗时"+((times4-time1)/1000)+"秒");
					
//				String rkrq = getRkrq();//获取临时表的日期
//				System.out.println("正在导入征收信息表。。。。。");
//				doLoadup(rkrq);//导入征收信息表
//				System.out.println("正在导入纳税人信息表。。。。。");
//				dr(rkrq);//导入纳税人信息表
				
//				List<Mode> list = new ArrayList<Mode>();
//				list.add(new Mode("IN", "String", ""));
//				list.add(new Mode("out", "String", ""));
				//String mes = (String)call("XWCS_GSDR_SYGX_ONE",list);
				
//				if(mes.equals("1")){
//					Long times2 = System.currentTimeMillis();
//					System.out.println("================一阶段执行完毕====耗时："+(times2-times1)/1000+"s ============");
//					times1 = System.currentTimeMillis();
//					String mes2 = (String)call("XWCS_GSDR_SYGX_TWO_DN",list);
//					if(mes2.equals("1")){
//						times2 = System.currentTimeMillis();
//						System.out.println("================二阶段当年同比执行完毕=====耗时："+(times2-times1)/1000+"s===========");
//						times1 = System.currentTimeMillis();
//						String mes3 = (String)call("XWCS_GSDR_SYGX_TWO_TN",list);
//						
//					}
//					
//				}
				try {
					if(!ids3.equals("")){//执行 最低优先级
						doMbSql("3",ids3,uid);
						
					}
					one();
					tow();
					Long times2 = System.currentTimeMillis();
					three();
					Long times3 = System.currentTimeMillis();
					System.err.println("执行THREE时间总耗时"+((times3-times2)/1000)+"秒");
			

				} catch (Exception e) {
					//加工失败
					System.out.println("更新首页关系表失败");
					System.out.println("加工失败");
					bs.update("update  XWCS_JGZT set zt = '2' where RWSBM='"+uid+"'");
					
					bs.update("flashback table FAST_NHYZB to timestamp to_timestamp('"+time+"','yyyy-mm-dd HH24:MI:SS')");
					bs.update("flashback table FAST_YLJ to timestamp to_timestamp('"+time+"','yyyy-mm-dd HH24:MI:SS')");
					bs.update("flashback table FAST_YBGGYS to timestamp to_timestamp('"+time+"','yyyy-mm-dd HH24:MI:SS')");
					bs.update("flashback table fast_nszzb to timestamp to_timestamp('"+time+"','yyyy-mm-dd HH24:MI:SS')");
					
					e.printStackTrace();
					
					return;
				}
				//加工成功
				bs.update("update  XWCS_JGZT set zt = '0' where RWSBM='"+uid+"'");
				long time2 = System.currentTimeMillis();
				System.err.println("执行时间总耗时"+((time2-time1)/1000)+"秒");
//				 Connection conn = null;
//			        String sql = "";
//			        String proceName = "xwcs_gsdr_sygx";
//			        String[] parameterArr = {"",""};
//					try {
//						conn = getConnection();
//						conn.setAutoCommit(true);
//						if(parameterArr.length > 0){
//							sql = "call "+proceName+" (";
//							for(int i = 0;i<parameterArr.length;i++){
//								sql = sql + "?,";
//							}
//							sql = sql.replaceAll(",$", ")");
//							CallableStatement stat = conn.prepareCall(sql);
//							
//							for(int i = 0;i<parameterArr.length;i++){
//												
//								stat.setObject(i+1, parameterArr[i]);
//							}
//							stat.execute();
//							System.out.println("首页关系表执行完成！");
//						}else{
//							sql = "call " + proceName + "()";
//							CallableStatement stat = conn.prepareCall(sql);
//							stat = conn.prepareCall(sql);
//							stat.execute();
//							System.out.println("首页关系表执行完成！");
//						}
//					}catch (SQLException e) {
//						e.printStackTrace();
//					}finally{
//						try {
//							conn.close();
//						} catch (SQLException e) {
//							e.printStackTrace();
//						}
//					}
					
			}
		}.start();
       
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
			Map<String, Object> queryOne = bs.queryOne("select distinct rk_rq from xwcs_gsdr_temp where rk_rq is not null");
			rkrq = queryOne== null?"" :queryOne.get("RK_RQ").toString();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return rkrq;
	}
	
	public boolean dr(String rkrq) throws Exception{
		boolean tag = false;
		
			//bs.delete("delete from sb_nsrxx z where to_char(rk_rq, 'yyyyMM') = '" + rkrq + "'");
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
			try {
			connection = DruidUtil.getConnection();
			stm = connection.createStatement();
			connection.setAutoCommit(false);
			
			stm.addBatch(istSql);
			stm.executeBatch();
			stm.close();
			connection.commit();
			connection.setAutoCommit(true);
			if (null != connection) {
				connection.close();
				connection = null;
			}
		
		} catch (Exception e) {
	            if (connection != null) {
	                connection.rollback();
	            }
	            e.printStackTrace();
	            throw e;
		}
			//bs.insert(istSql);
			//bs.delete("delete from sb_nsrxx a where (a.xh) in(select xh from sb_nsrxx group by xh having count(*) > 1)" + 
			//		"    and rowid not in (select min(rowid) from SB_NSRXX group by xh having count(*)>1)");	
			tag = true;
		
		return tag;
	}
	
	public boolean doLoadup(String rkrq) throws Exception{
		boolean tag = false;
		
			//bs.delete("delete from  sb_zsxx where nsrsbh in (select nsrsbh from xwcs_gsdr_zjb)");
			
			String czy_dm = getValue(user.get("UUID"));
			String sql = "insert into sb_zsxx s(xh,nsrsbh,nsrmc,hy,zsxm,zse,rk_rq,gds,sjse,sz_dm,"
					+ "hy_dm,jd_dm,zdsyh,wq,lrry_dm,lr_sj,qyxz,sfbg,bl,zspm,zspmdm,hydl,hyzl,yskmdm,dzsphm,djxhs,YZPZXH,SKSX,JD_MC,YSKM_MC,YSFPBL_MC) "
					+ "select S_SB_NSRXX.nextval,nsrsbh,nsrmc,x.hy_mc,x.zsxm_mc,x.zse,to_date('" + rkrq
					+ "','yyyyMM')," + "'0',x.se,zsxm_dm,hy_dm,jd_dm,x.zdsyh,x.wq,'" + czy_dm
					+ "',sysdate,'J','0',bl,x.zspm,x.zspmdm,x.hydl,x.hyzl,x.yskmdm,x.dzsphm,x.djxhs,x.YZPZXH,x.SKSX,x.JD_MC,x.YSKM_MC,x.YSFPBL_MC  from xwcs_gsdr_zjb x";
				
			try {
				connection = DruidUtil.getConnection();
				stm = connection.createStatement();
				connection.setAutoCommit(false);
				
				stm.addBatch(sql);
				stm.executeBatch();
				stm.close();
				connection.commit();
				connection.setAutoCommit(true);
				if (null != connection) {
					connection.close();
					connection = null;
				}
			
			} catch (Exception e) {
		            if (connection != null) {
		                connection.rollback();
		            }
		            e.printStackTrace();
		            throw e;
			}
			//bs.insert(sql);
			
//		bs.delete("delete from sb_zsxx " + 
//					" where (xh) in(select xh from sb_zsxx group by xh having count(*) > 1)  and rowid not in (select min(rowid) from SB_NSRXX group by xh having count(*)>1) ");	
			tag = true;
		
		return tag;
	}
	
	public List<Map<String,String>> getMbInfo() throws SQLException{
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();;
		connection = DruidUtil.getConnection();
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
	
	/**
	 * 更新年税种占比表的当年累计金额(区级用户)
	 * 更新年税种占比表的同期累计金额（区级用户）
	 * @throws SQLException
	 */
	public void one() throws Exception{
		long time1 = System.currentTimeMillis();
		
		try {
			
			//更新年税种占比表的当年累计金额(区级用户)
			String sql1 = "select sum(zzs*bl/100) 增值税,sum(yys*bl/100) 营业税,sum(grsds*bl/100) 个人所得税,\r\n" + 
					"              \r\n" + 
					"              sum(fcs*bl/100) 房产税,sum(yhs*bl/100) 印花税,sum(ccs*bl/100) 车船税,sum(qysds*bl/100) 企业所得税,\r\n" + 
					"              \r\n" + 
					"              sum(ygzzzs*bl/100) 营改增增值税,sum(cswhjss*bl/100) 城市维护建设税,sum(dfjyfj*bl/100) 地方教育附加,\r\n" + 
					"              \r\n" + 
					"              sum(jyfj*bl/100) 教育附加,sum(cztdsys*bl/100) 城镇土地使用税,sum(hbs*bl/100) 环保税" + 
					"                      from sb_nsrxx\r\n" + 
					"                     where to_char(rk_rq, 'yyyy') = to_char(sysdate, 'yyyy')";
			
			List<Map<String, Object>> list1 = bs.query(sql1);
			List<String> list = new ArrayList<>();
			
			for (Map<String, Object> map : list1) {
				for(Entry<String, Object> entry : map.entrySet()) {
					
					//bs.update("  update fast_nszzb b  set dnljje = '"+entry.getValue()+"'   where b.zsxm_mc = '"+entry.getKey()+"' and b.jd_dm ='"+map.get("JD_DM")+"'");
					list.add("  update fast_nszzb b  set dnljje = '"+entry.getValue()+"'   where b.zsxm_mc = '"+entry.getKey()+"' and b.jd_dm ='00'");
				}
				
			}
			// 更新年税种占比表的同期累计金额（区级用户）
			String sql2 = "select sum(zzs*bl/100) 增值税,\r\n" + 
					"sum(yys*bl/100) 营业税,\r\n" + 
					"sum(grsds*bl/100) 个人所得税,\r\n" + 
					"sum(fcs*bl/100) 房产税,\r\n" + 
					"sum(yhs*bl/100) 印花税,\r\n" + 
					"sum(ccs*bl/100) 车船税,\r\n" + 
					"sum(qysds*bl/100) 企业所得税,\r\n" + 
					"sum(ygzzzs*bl/100) 营改增增值税,\r\n" + 
					"sum(cswhjss*bl/100) 城市维护建设税,\r\n" + 
					"sum(dfjyfj*bl/100) 地方教育附加,\r\n" + 
					"sum(jyfj*bl/100) 教育附加,\r\n" + 
					"sum(cztdsys*bl/100) 城镇土地使用税,\r\n" + 
					"sum(hbs*bl/100) 环保税" + 
					" from sb_nsrxx\r\n" + 
					" where to_char(rk_rq,'yyyy') = to_char(sysdate,'yyyy')-1"+ 
					" and rk_rq<=add_months((select max(rk_rq) from sb_nsrxx),-12)\r\n";
			List<Map<String, Object>> list2 = bs.query(sql2);
			
			for (Map<String, Object> map : list2) {
				for(Entry<String, Object> entry : map.entrySet()) {
					//bs.update("  update fast_nszzb b  set dnljje = '"+entry.getValue()+"'   where b.zsxm_mc = '"+entry.getKey()+"' and b.jd_dm ='"+map.get("JD_DM")+"'");
					list.add("  update fast_nszzb b  set tqljje = '"+entry.getValue()+"'   where b.zsxm_mc = '"+entry.getKey()+"' and b.jd_dm ='00'");
					
				}
				
			}
			
	//		for (String string : list) {
	//			bs.update(string);
	//		}
			connection = DruidUtil.getConnection();
			stm = connection.createStatement();
			connection.setAutoCommit(false);
			for (String string : list) {
				stm.addBatch(string);
			}
			stm.executeBatch();
			stm.close();
			connection.commit();
			
			connection.setAutoCommit(true);
			if (null != connection) {
				connection.close();
				connection = null;
			}
		
		} catch (Exception e) {
				
	            if (connection != null) {
	                connection.rollback();
	            }
	            e.printStackTrace();
	            throw e;
		}
		Long time2 = System.currentTimeMillis();
		System.err.println("one执行时间耗时"+((time2-time1)/1000)+"秒");
		
	}
	
	/**
	 * 更新年税种占比表的当年累计金额
	 * 更新年税种占比表的同期累计金额
	 * @throws SQLException
	 */
	public void tow() throws Exception{
		long time1 = System.currentTimeMillis();
		
		try {
			
			
			//更新年税种占比表的当年累计金额
			String sql1 = "select sum(zzs*bl/100) 增值税,sum(yys*bl/100) 营业税,sum(grsds*bl/100) 个人所得税,\r\n" + 
					"              \r\n" + 
					"              sum(fcs*bl/100) 房产税,sum(yhs*bl/100) 印花税,sum(ccs*bl/100) 车船税,sum(qysds*bl/100) 企业所得税,\r\n" + 
					"              \r\n" + 
					"              sum(ygzzzs*bl/100) 营改增增值税,sum(cswhjss*bl/100) 城市维护建设税,sum(dfjyfj*bl/100) 地方教育附加,\r\n" + 
					"              \r\n" + 
					"              sum(jyfj*bl/100) 教育附加,sum(cztdsys*bl/100) 城镇土地使用税,sum(hbs*bl/100) 环保税, jd_dm\r\n" + 
					"                      from sb_nsrxx\r\n" + 
					"                     where jd_dm!='00' and jd_dm is not null and  to_char(rk_rq, 'yyyy') = to_char(sysdate, 'yyyy')\r\n" + 
					"                     group by jd_dm";
			
			List<Map<String, Object>> list1 = bs.query(sql1);
			List<String> list = new ArrayList<>();
			
			for (Map<String, Object> map : list1) {
				Integer rows = bs.queryCount("select * from FAST_NSZZB where jd_dm='"+map.get("JD_DM")+"'");
				if(rows>1) {
					for(Entry<String, Object> entry : map.entrySet()) {
						if(entry.getKey().equals("JD_DM")) {
							continue;
						}
						//bs.update("  update fast_nszzb b  set dnljje = '"+entry.getValue()+"'   where b.zsxm_mc = '"+entry.getKey()+"' and b.jd_dm ='"+map.get("JD_DM")+"'");
						list.add("  update fast_nszzb b  set dnljje = '"+entry.getValue()+"' "
								+ "  where b.zsxm_mc = '"+entry.getKey()+"' and b.jd_dm ='"+map.get("JD_DM")+"'");
					}
				}else {
					for(Entry<String, Object> entry : map.entrySet()) {
						if(entry.getKey().equals("JD_DM")) {
							continue;
						}
						if(!(entry.getValue().equals("0"))) {
							//bs.update("  update fast_nszzb b  set dnljje = '"+entry.getValue()+"'   where b.zsxm_mc = '"+entry.getKey()+"' and b.jd_dm ='"+map.get("JD_DM")+"'");
							
							bs.insert("insert into fast_nszzb(dnljje,zsxm_mc,jd_dm) values('"+entry.getValue()+"','"+entry.getKey()+"','"+map.get("JD_DM")+"')");
						
						}
						
					}
				}
				
				
				
			}
			//更新年税种占比表的同期累计金额
			String sql2 = "select sum(zzs*bl/100) 增值税,\r\n" + 
					"sum(yys*bl/100) 营业税,\r\n" + 
					"sum(grsds*bl/100) 个人所得税,\r\n" + 
					"sum(fcs*bl/100) 房产税,\r\n" + 
					"sum(yhs*bl/100) 印花税,\r\n" + 
					"sum(ccs*bl/100) 车船税,\r\n" + 
					"sum(qysds*bl/100) 企业所得税,\r\n" + 
					"sum(ygzzzs*bl/100) 营改增增值税,\r\n" + 
					"sum(cswhjss*bl/100) 城市维护建设税,\r\n" + 
					"sum(dfjyfj*bl/100) 地方教育附加,\r\n" + 
					"sum(jyfj*bl/100) 教育附加,\r\n" + 
					"sum(cztdsys*bl/100) 城镇土地使用税,\r\n" + 
					"sum(hbs*bl/100) 环保税, jd_dm\r\n" + 
					" from sb_nsrxx\r\n" + 
					" where jd_dm!='00' and jd_dm is not null and to_char(rk_rq, 'yyyy') = to_char(sysdate, 'yyyy')-1"+ 
					" and rk_rq<=add_months((select max(rk_rq) from sb_nsrxx),-12)\r\n" + 
					" group by jd_dm";
			List<Map<String, Object>> list2 = bs.query(sql2);
			
			for (Map<String, Object> map : list2) {
				for(Entry<String, Object> entry : map.entrySet()) {
					//bs.update("  update fast_nszzb b  set dnljje = '"+entry.getValue()+"'   where b.zsxm_mc = '"+entry.getKey()+"' and b.jd_dm ='"+map.get("JD_DM")+"'");
					list.add("  update fast_nszzb b  set tqljje = '"+entry.getValue()+"'   where b.zsxm_mc = '"+entry.getKey()+"' and b.jd_dm ='"+map.get("JD_DM")+"'");
					
				}
				
			}
			//计算增幅  
			list.add("update FAST_NSZZB set zf=dnljje-tqljje");
			//计算增长比例
			list.add("update FAST_NSZZB set zzbl=to_char(round(dnljje/tqljje-1,4),'FM99999999999990.0000') where tqljje!=0");
			//计算占比
			list.add("update FAST_NSZZB a\r\n" + 
					"       set zb = to_char(round(dnljje / (select b.zje\r\n" + 
					"                                          from (select sum(dnljje) zje, jd_dm\r\n" + 
					"                                                  from FAST_NSZZB\r\n" + 
					"                                                 group by jd_dm) b\r\n" + 
					"                                         where a.jd_dm = b.jd_dm),\r\n" + 
					"                              4),\r\n" + 
					"                        'FM99999999999990.0000')");
	//		for (String string : list) {
	//			bs.update(string);
	//		}
			connection = DruidUtil.getConnection();
			stm = connection.createStatement();
			connection.setAutoCommit(false);
			int j = list.size();
			for (int i = 0;i<j;i++) {
				if(i==(j-3)) {
					stm.executeBatch();
					connection.commit();
					stm.clearBatch();
				}
				stm.addBatch(list.get(i));
			}
			stm.executeBatch();
			stm.close();
			connection.commit();
			connection.setAutoCommit(true);
			if (null != connection) {
				connection.close();
				connection = null;
			}
		} catch (Exception e) {
				
		        if (connection != null) {
		            connection.rollback();
		        }
		        e.printStackTrace();
		        throw e;
		}
		Long time2 = System.currentTimeMillis();
		System.err.println("tow执行时间耗时"+((time2-time1)/1000)+"秒");
		
	}
	
	/**
	 * 更新行业占比表
	 * @throws Exception 
	 */
	public void three() throws Exception{
		
		
		try {
			//第一次先插入本年数据  
			bs.delete("delete FAST_NHYZB");
			bs.insert("insert into FAST_NHYZB\r\n" + 
					"  (HYMC, DNLJJE,JD_DM)\r\n" + 
					"  select t.hyml_mc HYMC, （x.je） DNLJJE, x.jd_dm\r\n" + 
					"    from dm_hyml t,\r\n" + 
					"         (select sum(s.dfzse) je, s.hy_dm dm,s.jd_dm \r\n" + 
					"            from sb_nsrxx s\r\n" + 
					"           where to_char(s.rk_rq, 'yyyy') = to_char(sysdate, 'yyyy') \r\n" + 
					"           group by hy_dm,s.jd_dm) x\r\n" + 
					"   where t.hyml_dm = x.dm");
			
			String sql1 = "select (b.je) x, y from (select sum(s.dfzse) je,  (select hyml_mc from dm_hyml where hyml_dm = s.hy_dm) y\r\n" + 
					"                              from sb_nsrxx s\r\n" + 
					"                             where to_char(s.rk_rq, 'yyyy') =\r\n" + 
					"                                   to_char(sysdate, 'yyyy') - 1\r\n" + 
					"                               and s.rk_rq <=\r\n" + 
					"                                   add_months((select max(rk_rq) from sb_nsrxx),-12) \r\n" + 
					"                             group by hy_dm) b where  y is not null \r\n";
			
			List<Map<String, Object>> list1 = bs.query(sql1);
			for (Map<String, Object> map : list1) {
				bs.update("update fast_nhyzb set tqljje = '"+map.get("X")+"'    where hymc = '"+map.get("Y")+"'");
			}
			//FAST_YLJ里没有街道的数据就插入
			bs.insert("insert into FAST_YLJ (Dyljje,Jd_Dm,yf)  select sum(dfzse) dfzse, jd_dm, to_char(rk_rq, 'mm') yf\r\n" + 
					"      from sb_nsrxx" + 
					"     where to_char(rk_rq, 'yyyy') = to_char(sysdate, 'yyyy') and  jd_dm not in(select jd_dm from FAST_YLJ ) and jd_dm !='null'\r\n" + 
					"     group by jd_dm, to_char(rk_rq, 'mm')");
			
			bs.update("update fast_nhyzb set zf = dnljje - tqljje");
			bs.update("update fast_nhyzb set zzbl = to_char(round(dnljje / tqljje - 1, 4), 'FM99999999999990.0000')");
			bs.update(" update fast_nhyzb a set zb = to_char(round(dnljje / (select sum(dnljje)"
					+ " from fast_nhyzb b where a.jd_dm=b.jd_dm), 4), 'FM99999999999990.0000')");
			//更新月累计表
			String sql3 = "select * from ( select NVL(sum(dfzse),0) dfzse ,to_char(rk_rq,'mm') yf,jd_dm from sb_nsrxx" + 
					"					 where to_char(rk_rq,'yyyy') = to_char(sysdate,'yyyy') and jd_dm !='null' group by to_char(rk_rq,'mm'),jd_dm) where dfzse!='0'";
			List<Map<String, Object>> list3 = bs.query(sql3);
			for (Map<String, Object> map : list3) {
				bs.update(" update FAST_YLJ set dyljje = '"+map.get("DFZSE")+"'where yf = '"+map.get("YF")+"' and jd_dm= '"+map.get("JD_DM")+"'");
			}
			
			String sql4 = "select * from (select NVL(sum(dfzse),0) dfzse,to_char(rk_rq,'mm') yf,jd_dm from sb_nsrxx where to_char(rk_rq,'yyyy') = to_char(sysdate,'yyyy')-1"
					+ " and rk_rq<=add_months((select max(rk_rq) from sb_nsrxx),-12) and jd_dm !='null' group by to_char(rk_rq,'mm'),jd_dm )where dfzse!='0'";
			List<Map<String, Object>> list4 = bs.query(sql4);
			for (Map<String, Object> map : list4) {
				bs.update(" update FAST_YLJ set tqljje = '"+map.get("DFZSE")+"'where yf = '"+map.get("YF")+"' and jd_dm= '"+map.get("JD_DM")+"'");
			}
			
			bs.update("update FAST_YLJ set zf=dyljje-tqljje");
			bs.update("update FAST_YLJ set zzbl=to_char(round(dyljje/tqljje-1,4),'FM99999999999990.0000')");
			//更新一般公共预算数据表
			bs.delete("delete  FAST_YBGGYS");
			List<Map<String,Object>> list = bs.query("select jddm from fast_ybggys_vw where jddm !='null' and jddm !='00' group by jddm ");
			for(Map<String,Object> map2:list) {
				bs.insert("insert into fast_ybggys (nsrmc,dnljje,TQLJJE,bs,jd_dm) select nvl(xx.nsrmc,'无'),xx.dnlj,xx.tqlj,'0',xx.jddm jd_dm" //xx.jddm
						+ " from (SELECT T.*,ROW_NUMBER() OVER (ORDER BY T.dnlj desc nulls last) AS RN FROM fast_ybggys_vw T where jddm='"+getValue(map2.get("JDDM"))+"') xx where RN<=20 ");
				//增幅前二十
				bs.insert("insert into fast_ybggys (nsrmc,dnljje,TQLJJE,bs,jd_dm) (select nvl(xx.nsrmc,'无'),xx.dnlj,xx.tqlj,'1',xx.jddm jd_dm"
						+ " from (SELECT T.*,ROW_NUMBER() OVER (ORDER BY (T.dnlj-nvl(T.tqlj,0)) desc nulls last) AS RN FROM fast_ybggys_vw T where jddm='"+getValue(map2.get("JDDM"))+"') xx where RN<=20 ) ");
				//减负前二十
				bs.insert("insert into fast_ybggys (nsrmc,dnljje,TQLJJE,bs,jd_dm) (select nvl(xx.nsrmc,'无'),xx.dnlj,xx.tqlj,'2',xx.jddm jd_dm"
						+ " from (SELECT T.*,ROW_NUMBER() OVER (ORDER BY (T.dnlj-nvl(T.tqlj,0)) nulls last) AS RN FROM fast_ybggys_vw T where jddm='"+getValue(map2.get("JDDM"))+"') xx where RN<=20 ) ");
			}
				
			
			bs.insert("insert into fast_ybggys (nsrmc,dnljje,TQLJJE,bs,jd_dm) select nvl(xx.nsrmc,'无'),xx.dnlj,xx.tqlj,'0','00' jd_dm" //xx.jddm
					+ " from (SELECT T.*,ROW_NUMBER() OVER (ORDER BY T.dnlj desc nulls last) AS RN FROM fast_ybggys_admin_vw T) xx where RN<=20");
			//增幅前二十
			bs.insert("insert into fast_ybggys (nsrmc,dnljje,TQLJJE,bs,jd_dm) (select nvl(xx.nsrmc,'无'),xx.dnlj,xx.tqlj,'1','00'jd_dm"
					+ " from (SELECT T.*,ROW_NUMBER() OVER (ORDER BY (T.dnlj-nvl(T.tqlj,0)) desc nulls last) AS RN FROM fast_ybggys_admin_vw T) xx where RN<=20)");
			//减负前二十
			bs.insert("insert into fast_ybggys (nsrmc,dnljje,TQLJJE,bs,jd_dm) (select nvl(xx.nsrmc,'无'),xx.dnlj,xx.tqlj,'2','00' jd_dm"
					+ " from (SELECT T.*,ROW_NUMBER() OVER (ORDER BY (T.dnlj-nvl(T.tqlj,0)) nulls last) AS RN FROM fast_ybggys_admin_vw T) xx where RN<=20)");
			
			//计算增幅增长比例
			bs.update("update FAST_YBGGYS set TQLJJE='0' where TQLJJE is null");
			bs.update("update FAST_YBGGYS set zf=dnljje-tqljje");
			bs.update("update FAST_YBGGYS set zzbl=to_char(round(dnljje/tqljje-1,4),'FM99999999999990.0000') where tqljje != 0");
			
			//call("XWCS_GSDR_SYGX_THREE",li);

		} catch (Exception e) {
		        e.printStackTrace();
		        throw e;
		}
	
		
	}
	
	
	
	
	

	
	
}
