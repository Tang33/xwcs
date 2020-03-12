package fast.main.controller.ImportTaxData;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;

import fast.main.service.BaseService;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Super;

/**
 * 税收数据管理功能
 * 本功能页面用于对数据的自定义条件查询和修改并存为规则！
 *
 */
@Controller
@RequestMapping("sssjgl")
public class SssjglController extends  Super {

	@Autowired
	BaseService bs;

	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {		
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "ImportTaxData/Sssjgl";
		} catch (Exception e) {
			e.printStackTrace();
			return "ImportTaxData/Sssjgl";
		}
	}
	
	private static Connection connection = null;
	private Map<String, Object> user = null;
	String andjd = "";
 

	/**
	 * 根须获取到的表名查询相应表
	 * @param request
	 * @param response
	 * @param Map 前台获取参数集合
	 * @return
	 */
	@RequestMapping("/querySdbg.do")
	@ResponseBody
	public String querySdbg(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			//获取登录页面时的一系列用户信息
			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String jd = getValue(user.get("DWID"));			//获取街道代码
			String jdmcSql = "select JD_MC  FROM DM_JD where JD_DM = "+jd+" " ;
			List<Map<String,Object>> jdmcList = bs.query(jdmcSql);
			String jdmc = (String)jdmcList.get(0).get("JD_MC");
			//根据街道名称来判断拼接Sql语句的条件
			if( null != jd  && "00".equals(jd) ) {	
				andjd  = " and 1 = 1";				
			}else{				
				andjd = " and jdxz like '%"+jdmc+"%' " ;				
			}
			
			//this.getValue()获取数据判断是否为空，为空赋"";
			String page = getValue(form.get("page"));		//获取从第几页

			String pagesize = getValue(form.get("limit"));	//获取每页显示的条数
			//获取表单中的数据
			String form1 = getValue(form.get("form"));		
			String fz = getValue(form.get("fz"));
			//选择日期
			String rk_rq =getValue(form.get("drrq"));		//获取入库日期
			//根据入库日期是否为空，拼接sql语句
			if(rk_rq.equals("")) {				
				rk_rq = "1=1";
			}else{
				rk_rq = "to_date(a.rk_rq,'yyyyMM')=to_date("+rk_rq+",'yyyyMM')";
			}
			//获取需要查询的表
			String tableName =getValue(form.get("tableName"));
			String sqlcs="";	
			JSONArray form2  =JSONArray.parseArray(form1);
			//下标从一开始取值
			String sql = "select t.zse,t.bl,t.nsrmc,t.nsrsbh,t.zsxm_mc,t.jd_mc JD_MC,t.hy,t.se,t.qxj,t.yskmdm from xwcs_gsdr_temp t,"+tableName+" a where 1=1 and "+rk_rq+" "+andjd+" ";
			String sqlcount = "";
			if (form2.size() > 1) {
				for (int i = 1; i < form2.size(); i++) {
					JSONObject obj = (JSONObject) JSONObject.toJSON(form2.get(i));
					sqlcs += " " + getValue(obj.get("value"));
				}
				
			}
			sqlcs+=" and a.id=t.ysid";
			if(tableName.equals("")) {
					sql = " select t.zse,t.bl,t.nsrmc,t.nsrsbh,t.zsxm_mc,t.jd_mc JD_MC,t.hy,t.se,t.qxj,t.yskmdm from xwcs_gsdr_temp t"
							+ " left join XWCS_GSDR_YSSJRK a on a.id=t.ysid where "+rk_rq+" " + sqlcs+
							" union\r\n" + 
							" select t.zse,t.bl,t.nsrmc,t.nsrsbh,t.zsxm_mc,t.jd_mc JD_MC,t.hy,t.se,t.qxj,t.yskmdm from xwcs_gsdr_temp t"
							+ " left join XWCS_GSDR_YSSJJRK a on a.id=t.ysid where "+rk_rq+sqlcs;
					sqlcount = sql;
			}else {
				sql = "select t.zse,t.bl,t.nsrmc,t.nsrsbh,t.zsxm_mc,t.jd_mc JD_MC,t.hy,t.se,t.qxj,t.yskmdm from xwcs_gsdr_temp t,"+tableName+" a where 1=1 and "+rk_rq+" "+andjd+" ";
				sql+=sqlcs;
				sqlcount = "select 1 from xwcs_gsdr_temp t,"+tableName+" a where 1=1" + sqlcs +" and "+rk_rq+" "+andjd+" ";
			}	
			
			
			//可用bs调用方法直接分页
			List<Map<String, Object>> sjjgall = bs.query(sql,page,pagesize);
			//查询count
			int sjjgallcount = bs.queryCount(sqlcount);
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < sjjgall.size(); i++) {
				Map<String, Object> map=sjjgall.get(i);
				if (!fz.equals("")) {
					BigDecimal bd2 = getBigDecimal(map.get("DFKJ")).setScale(2, RoundingMode.HALF_UP);
					BigDecimal bd3 = getBigDecimal(map.get("ZSE")).setScale(2, RoundingMode.HALF_UP);
					DecimalFormat dft = new DecimalFormat("0.00");
					map.replace("DFKJ", dft.format(bd2));
					map.replace("ZSE", dft.format(bd3));
					lists.add(map);	
				}else {
					Object zse = map.get("ZSE");
					Object bl = map.get("BL");
					BigDecimal money = getBigDecimal(zse).multiply(getBigDecimal(bl));
					BigDecimal money1 = money.divide(getBigDecimal(100), 2, RoundingMode.HALF_UP);
					DecimalFormat dft = new DecimalFormat("0.00");
					map.put("DFKJ", dft.format(money1));
					map.put("QKJ", dft.format(zse));
					lists.add(map);			
				}				
			}
			return toJson("000", "查询成功！",lists,sjjgallcount);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}

	}


	/**
	 * 选中查询表后加载查询条件框中的数据
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping("/selectList.do")
	@ResponseBody
	public String selectList(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			//跟据选中的表来查询 模板名称,对应原始数据表的字段名
			String tableName =getValue(form.get("tableName"));
			tableName = tableName.toLowerCase();
			//根据tableName进行判断，使用相应的sql语句进行查询
			if (tableName == "") {
				tableName = "xwcs_gsdr_yssjrk";
				String sql = "select MBM,ZDM FROM  FAST_YSSJCX_MB where DYCXB = '"+tableName+"' group by MBM,ZDM";
				List<Map<String,Object>> list = bs.query(sql);
				return this.toJson("000","查询成功", list);
			}else {
				String sql = "select MBM,ZDM FROM  FAST_YSSJCX_MB where DYCXB = '"+tableName+"' group by MBM,ZDM";
				List<Map<String,Object>> list = bs.query(sql);
				return this.toJson("000","查询成功", list);
			}
		}catch(Exception e) {
			return this.toJson("009", "查询异常");
		}
	}




	/**
	 * 根据查询条件中选择的条件查询条件定义中的下拉框
	 * 税种。。。
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectSZ.do")
	@ResponseBody
	public String selectSZ(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			//获取登录时相应的用户信息
			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String rk_rq = request.getParameter("rkrq");	//获取入库日期
			String jd = getValue(user.get("DWID"));			//获取街道代码
			String jdmcSql = "select JD_MC  FROM DM_JD where JD_DM = "+jd+" " ;
			List<Map<String,Object>> jdmcList = bs.query(jdmcSql);
			String jdmc = (String)jdmcList.get(0).get("JD_MC");
			if( null != jd  && "00".equals(jd)) {			
				andjd  = " and 1 = 1";			
			}else{			
				andjd = " and jdxz like '%"+jdmc+"%' " ;			
			}
			//获取选择的表名
			String tableName =getValue(form.get("tableName"));
			tableName = tableName.toLowerCase();
			//获取查询条件中的条件
			String xz = getValue(form.get("xz"));
			String sql = "select DISTINCT "+xz+" zsxmmc from "+tableName+" where 1 = 1 "+andjd+"and (-RK_RQ=?-)";
			sql = getSql2(sql, rk_rq);
			List<Map<String,Object>> list = bs.query(sql);
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i) == null || list.get(i).size() == 0){
					list.remove(i);
				}
			}
			return this.toJson("000","查询成功", list);
		}catch(Exception e) {
			return this.toJson("009", "查询异常");
		}
	}


	/**
	 * 根据查询条件中选择的条件查询条件定义中的下拉框
	 * 税收管理员，税率，主管税务。。。。
	 * @param map
	 * @return
	 */
	@RequestMapping("/queryzd.do")
	@ResponseBody
	public String queryzd(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			user = (Map<String, Object>) request.getSession().getAttribute("user");
			String jd = getValue(user.get("DWID"));			//获取街道代码
			String jdmcSql = "select JD_MC  FROM DM_JD where JD_DM = "+jd+" " ;
			List<Map<String,Object>> jdmcList = bs.query(jdmcSql);
			String jdmc = (String)jdmcList.get(0).get("JD_MC");
			if( null != jd  && "00".equals(jd) ) {			
				andjd  = " and 1 = 1";			
			}else{			
				andjd = " and jdxz like '%"+jdmc+"%' " ;			
			}
			//获取查询条件中的条件
			String xz = getValue(form.get("xz"));
			String tableName =getValue(form.get("tableName"));
			tableName = tableName.toLowerCase();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -1);
			Date m = c.getTime();
			String rkrq = format.format(m);
			String sql="select "+xz+" from "+tableName+" where rk_rq='"+rkrq+"' where 1 = 1 "+andjd+" group by "+xz;
			List<Map<String, Object>> sjjgall = bs.query(sql);
			List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
			if (sjjgall!=null) {
				Map<String, String> map;
				for (int i = 0; i < sjjgall.size(); i++) {
					map = new HashMap<String, String>();
					//判断集合索引是否为空
					if(null != sjjgall.get(i)) {
						for (String key : sjjgall.get(i).keySet()) {
							map.put(key.toLowerCase(), sjjgall.get(i).get(key).toString());
						}
						list1.add(map);
					}

				}
				return this.toJson("000", "查询成功！", list1);
			}else {
				return this.toJson("000", "查询失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}


	/**
	 * 根据查询条件中选择的条件查询条件定义中的下拉框
	 * 选中其他条件
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectislike.do")
	@ResponseBody
	public String selectislike(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			//获取表名
			String tableName =getValue(form.get("tableName"));
			tableName = tableName.toLowerCase();
			//获取查询条件中的条件
			String zdm = getValue(form.get("xz"));
			String sql = "select * FROM FAST_YSSJCX_MB where ZDM = '" + zdm + "' and DYCXB = '"+tableName+"' ";
			List<Map<String,Object>> list = this.bs.query(sql);
			return this.toJson("000","查询成功", list);
		}catch(Exception e) {
			return this.toJson("009", "查询异常");
		}
	}


	/**
	 * 查询展示  需修改条数    金额   纳税人数
	 * @param rmap
	 * @return
	 */
	@RequestMapping("/querySdbg1.do")
	@ResponseBody
	public String querySdbg1(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			//获取表单中的数据
			String form1 = getValue(form.get("form"));
			String fz = getValue(form.get("fz"));
			String rk_rq =getValue(form.get("drrq"));		//入库日期
			String tableName =getValue(form.get("tableName"));		//表名
			String sqlcs="";
			JSONArray form2  =JSONArray.parseArray(form1);
			//下标从一开始取值
			if (form2.size() > 1) {
				for (int i = 1; i < form2.size(); i++) {
					JSONObject obj = (JSONObject) JSONObject.toJSON(form2.get(i));
					sqlcs += " " + getValue(obj.get("value"));
				}
			}
			sqlcs+=" and a.id=t.ysid";
			//List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
			//判断入库日期是否为空，不为空时sqlcs拼接语句
			if (!rk_rq.equals("")) {
				sqlcs +=" and a.rk_rq='"+rk_rq+"' ";
			}
			//数据个数
			String sql = "select  COUNT(*) allcount from xwcs_gsdr_temp t,"+tableName+" a where 1=1"+sqlcs;
			//纳税金额
			String sq2 = "select SUM(t.se) dfzse  from xwcs_gsdr_temp t,"+tableName+" a where 1=1"+sqlcs;
			//纳税人数
			String sq3 = "select   COUNT (DISTINCT t.nsrmc) nsrno from xwcs_gsdr_temp t,"+tableName+" a where 1=1"+sqlcs;
			String sqlall="SELECT * from"+"("+sql+"),"+"("+sq2+"),"+"("+sq3+")";
			List<Map<String, Object>> lists = bs.query(sqlall);
			for (int i = 0; i < lists.size(); i++) {
				Map<String, Object> map = lists.get(i);
				BigDecimal bd3 = getBigDecimal(map.get("DFZSE")).setScale(2, RoundingMode.HALF_UP);
				DecimalFormat dft = new DecimalFormat("0.00");
				map.replace("dfzse", dft.format(bd3));
			}
			return this.toJson("000", "查询成功！", lists);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}


	/**
	 * 获取街道
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/getjd.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String getjd(HttpServletRequest request , HttpServletResponse response) {
		String sql = "select distinct jd_mc from dm_jd";
		List<Map<String, Object>> list = bs.query(sql);
		return this.toJson("000", "查询成功！", list);
	}
	
	/**
	 * 获取纳税人最大月份
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/getNsDate.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String getNsDate(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form){
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

	/**
	 * 保存模板
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/addmb.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String addmb(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		String jdxz = getValue(form.get("jdxz"));		//街道名称
		String mbmc = getValue(form.get("mbmc"));		//模板名称
		String tableName =getValue(form.get("tableName"));		//表名
		String ms = getValue(form.get("ms"));		//规则描述
		String maplist = getValue(form.get("maplist"));
		JSONObject json = JSONObject.parseObject(maplist);
		String sqlcs="";
		for(String key : json.keySet()) {
			String value = json.getString(key);
			if(!StringUtils.isEmpty(value)) {
				String sqlcsdg=getsql(key, value);
				sqlcs+=sqlcsdg;
			}
		}
		//修改语句
		String updatesql = " update xwcs_gsdr_temp set jd_mc='"+jdxz+"' where ysid in (select t.ysid from xwcs_gsdr_temp t,"+tableName  +" a where 1=1";
		updatesql+=sqlcs;
		updatesql+=" and a.id=t.ysid)";
		//将名称以及sql语句插入模板表
		String sqlsert="insert into fast_mb (mbmc,sql,status,createtime,bs,ms) values(?,?,?,?,?,?)";
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			PreparedStatement stmt = connection.prepareStatement(sqlsert);
			stmt.setString(1, mbmc);
			//stmt.setClob(2, new StringReader(updatesql),updatesql.length());
			StringReader reader = new StringReader(updatesql);  
			stmt.setCharacterStream(2, reader, updatesql.length());  
			stmt.setString(3, "1");
			stmt.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
			stmt.setString(5, "2");
			stmt.setString(6, ms);
			stmt.execute();
			connection.commit();
			return this.toJson("000", "插入成功！");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}


	/**
	 * 修改查询数据街道
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/modify.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String modify(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		String jdxz = getValue(form.get("jdxz"));		//街道名称
		String maplist = getValue(form.get("maplist"));
		//获取表单中的数据
		String form1 = getValue(form.get("form"));
		String rk_rq =getValue(form.get("drrq"));		//入库日期
		String tableName =getValue(form.get("tableName"));		//表名
		String sqlcs="";
		JSONArray form2  =JSONArray.parseArray(form1);
		//下标从一开始取值
		if (form2.size() > 1) {
			for (int i = 1; i < form2.size(); i++) {
				JSONObject obj = (JSONObject) JSONObject.toJSON(form2.get(i));
				sqlcs += " " + getValue(obj.get("value"));
			}
		}
		sqlcs+=" and a.id=t.ysid";
		String updatesql = " update xwcs_gsdr_temp set jd_mc='"+jdxz+"' where ysid in (select t.ysid from xwcs_gsdr_temp t,"+tableName+" a where 1=1";
		updatesql+=sqlcs+")";
		Integer upnum = bs.update(updatesql);
		if(upnum!=0) {
			return this.toJson("000", Integer.toString(upnum));
		}else {

			return this.toJson("000", "修改失败！");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	private String getsql(String key, String value) {

		String sqlcs="";
		if(key.equals("jsyj")||key.equals("sl")||key.equals("sjje")||key.equals("zyj")||key.equals("ssj")
				||key.equals("dsj")||key.equals("qxj")||key.equals("xzj")||key.equals("zydfp")||key.equals("sjdfp")||key.equals("dsdfp")) {

			if(!StringUtils.isEmpty(value)) {

				sqlcs=sqlcs+" and a."+key+"="+value+"";

			}

		}else {

			if(!StringUtils.isEmpty(value)) {

				if(key.equals("nsrsbh")){

					sqlcs=sqlcs+" and a."+key+" like '"+value+"%'";
				}else{

					sqlcs=sqlcs+" and a."+key+"='"+value+"'";
				}

			}

		}
		return sqlcs;
	}
}
