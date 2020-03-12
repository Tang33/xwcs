package fast.main.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.ibatis.transaction.jdbc.JdbcTransaction;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.support.json.JSONUtils;

import fast.main.service.BaseService;
import oracle.jdbc.OracleTypes;

public class Super {
	
	private BaseService bs;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<String, Object> form;
	private Map<String, Object> user=null;
	
	@SuppressWarnings("unchecked")
	public void initMap(Map<String, Object> rmap){
		BaseService bs=(BaseService) rmap.get("bs");
		HttpServletRequest request=(HttpServletRequest) rmap.get("request");
		HttpServletResponse response=(HttpServletResponse) rmap.get("response"); 
		this.setUser((Map<String, Object>) request.getSession().getAttribute("user"));
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type,Token,Accept, Connection, User-Agent, Cookie");
		Map<String, Object> form=(Map<String, Object>) rmap.get("form");
		if(form==null)form=new HashMap<String, Object>();
		this.setBs(bs);
		this.setRequest(request);
		this.setResponse(response);
		this.setForm(form);
		try{
			sqlMap=new HashMap<String, String>();
//			List<Map<String, Object>> resultlist = this.getBs().query("select * from dbgl_sql");
//			if(resultlist!=null&&resultlist.size()>0){
//				for (int i = 0; i < resultlist.size(); i++) {
//					Map<String, Object> map=resultlist.get(i);
//					String key=getValue(map.get("KEY"));
//					String value=getValue(map.get("VALUE"));
//					sqlMap.put(key, value);
//				}
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//SQLSERVER用
	@SuppressWarnings("unchecked")
	public void initMap2(Map<String, Object> rmap){
		BaseService bs=(BaseService) rmap.get("bs");
		HttpServletRequest request=(HttpServletRequest) rmap.get("request");
		HttpServletResponse response=(HttpServletResponse) rmap.get("response");
		Map<String, Object> form=(Map<String, Object>) rmap.get("form");
		if(form==null)form=new HashMap<String, Object>();
		this.setBs(bs);
		this.setRequest(request);
		this.setResponse(response);
		this.setForm(form);
	}
	
	

	public String getValue(Object obj){
		String result="";
		if(obj!=null&&!obj.toString().trim().equals("")&&!obj.toString().trim().equals("null")&&!obj.toString().trim().equals("undefiend")){
			result=obj.toString().trim();
		}
		return result;
	}
	
	/**
	 * layui解析table参数par
	 * @param obj
	 * @return
	 */
	public Map<String, Object> getLayuiPar(String str){
		Map<String, Object> map =new HashMap<String, Object>();
		if(str!=null&&!str.trim().equals("")&&!str.trim().equals("null")){
			String[] strs=str.split("&");
			if(strs!=null){
				for (int i = 0; i < strs.length; i++) {
					String[] arr=strs[i].split("=");
					if(arr!=null&&arr.length>1&&arr[1]!=null&&!arr[1].equals("null")&&!arr[1].equals("undefiend")){
						map.put(arr[0], arr[1]);
					}else{
						map.put(arr[0], "");
					}
				}
			}
		}
		return map;
		
	}
	
	
	public static Map<String, String> sqlMap=new HashMap<String, String>();
	//从数据库读
//	static{
//		try {
//			for (File tempFile : new File(Super.class.getClassLoader().getResource("/conf/sql/").getPath()).listFiles()) {
//				if(!(tempFile.isFile()&&tempFile.getName().matches("^jdbc-.*\\.xml$"))) continue;
//				//获取根节点
//				Element xmldoc= DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(tempFile).getDocumentElement();
//				//获取子节点
//				NodeList childs=xmldoc.getElementsByTagName("SqlStatement");
//				//加载子节点
//				List<Map<String, String>> list=new ArrayList<Map<String,String>>();
//				for (int i = 0; i < childs.getLength(); i++) {
//					Map<String, String> map=new HashMap<String, String>();
//					Element tempNode=(Element) childs.item(i);
//					String attrName=tempNode.getAttribute("key");
//					String content=tempNode.getTextContent();
//					sqlMap.put(attrName, content);
//					map.put(attrName, content);
//					list.add(map);
//				}
//				sqlMapList.put(tempFile.getName(), list);
//				System.out.println(">>>>>>>>>>>>>>>>>>JDBC文件："+tempFile.getName()+",加载["+childs.getLength()+"]条数据<<<<<<<<<<<<<<<");
//			}
//		} catch (SAXException e) {
//			System.out.println("Super超类异常："+e.getLocalizedMessage());
//			e.printStackTrace();
//		} catch (IOException e) {
//			System.out.println("Super超类异常："+e.getLocalizedMessage());
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			System.out.println("Super超类异常："+e.getLocalizedMessage());
//			e.printStackTrace();
//		}
//	}


	public String getSql(String key,Object... par){
		if(sqlMap.get(key)==null){
			return "";
		}
		String exeSql=sqlMap.get(key).trim();
		if(par.length==1&&Object[].class.isAssignableFrom(par[0].getClass())) par=(Object[])par[0];
		//忽略空参数模式
		StringBuilder sql=new StringBuilder(exeSql);
		List<Object> param=new ArrayList<Object>();
		int step=0;
		while (true) {
			int temp1=sql.indexOf("(-",step);
			int temp2=temp1==-1?-1:sql.indexOf("-)",temp1);
			if(temp1==-1||temp2==-1) break;
			step=temp2+2;
			sql.replace(temp1,step, "(1=? or "+sql.substring(temp1+2,temp2)+")");
		}
		step=0;
		for (int i = 0; i < par.length; ++i) {
			String pars=(String) par[i];
			while (pars.indexOf("?")>=0) {
				pars=pars.replace("?", "^^0^0^^^");
			}
			step=sql.indexOf("?",step)+1;
			if(step!=0&&"1=? or ".equals(sql.substring(step-3<sql.length()?(step-3):sql.length()-1,step+4<sql.length()?(step+4):sql.length()))){
				if(pars==null||(pars instanceof String && "".equals(pars))) param.add("1"); else param.add("0");
				i--;
			}else{
				param.add(pars);
			}
		}
		for (int i = 0; i < param.size(); i++) {
			int idx=sql.indexOf("?");
			if (String.valueOf(param.get(i)).equals("null")) {
				sql.replace(idx,idx+1 , "Null");
			}else {
				sql.replace(idx,idx+1 , "'"+String.valueOf(param.get(i)+"'"));
			}
			
		}
		String sqls=sql.toString();
		while (sqls.indexOf("?")>=0) {
			sqls=sqls.replace("^^0^0^^^","?");
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>sql输出>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n--key:  "+key+"\n--sql:  "+sqls.toString());

		return sqls;
	}
	public String getSql2(String exeSql,Object... par){
		
		if(par.length==1&&Object[].class.isAssignableFrom(par[0].getClass())) par=(Object[])par[0];
		//忽略空参数模式
		StringBuilder sql=new StringBuilder(exeSql);
		List<Object> param=new ArrayList<Object>();
		int step=0;
		while (true) {
			int temp1=sql.indexOf("(-",step);
			int temp2=temp1==-1?-1:sql.indexOf("-)",temp1);
			if(temp1==-1||temp2==-1) break;
			step=temp2+2;
			sql.replace(temp1,step, "(1=? or "+sql.substring(temp1+2,temp2)+")");
		}
		step=0;
		for (int i = 0; i < par.length; ++i) {
			step=sql.indexOf("?",step)+1;
			if(step!=0&&"1=? or ".equals(sql.substring(step-3<sql.length()?(step-3):sql.length()-1,step+4<sql.length()?(step+4):sql.length()))){
				if(par[i]==null||(par[i] instanceof String && "".equals(par[i]))) param.add("1"); else param.add("0");
				i--;
			}else{
				param.add(par[i]);
			}
		}
		for (int i = 0; i < param.size(); i++) {
			int idx=sql.indexOf("?");
			sql.replace(idx,idx+1 , "'"+String.valueOf(param.get(i)+"'"));
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>sql输出>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n--key:  all \n--sql:  "+sql.toString());
		return sql.toString();
	}
	
	
	public boolean isNotNull(String str){
		boolean flag=false;
		if(str!=null&&!str.trim().equals("")&&!str.trim().equals("null")&&!str.trim().equals("undefied")){
			flag=true;
		}
		return flag;
	}
	
	
	/**
	 * layui专属转换json方法
	 * @param code 编码
	 * @param message 消息
	 * @param data 数据
	 * @param total 总条数(应用于分页)
	 * @return
	 */
	public String toJson(String code,String message,Object data,int count){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("data", data);
        map.put("count", count);
        map.put("msg", message);
        map.put("code", code);
		return JSONUtils.toJSONString(map);
	}
	/**
	 * 一般性转换JSON格式字符串方法
	 * @param code 编码
	 * @param message 消息
	 * @param data 数据
	 * @return
	 */
	public String toJson(String code,String message,Object data){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("data", data);
        map.put("count", 0);
        map.put("msg", message);
        map.put("code", code);
		return JSONUtils.toJSONString(map);
	}
	/**
	 * 异常转换json方法
	 * @param code 编码
	 * @param message 消息
	 * @return
	 */
	public String toJson(String code,String message){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("data", null);
        map.put("count", 0);
        map.put("msg", message);
        map.put("code", code);
		return JSONUtils.toJSONString(map);
	}
	
	public String toJsonct(String code,String message,Object data,String cont){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("data", data);
        map.put("count", cont);
        map.put("msg", message);
        map.put("code", code);
		return JSONUtils.toJSONString(map);
	}
	
	
	
	public Map<String, Object> getForm() {
		return form;
	}

	public void setForm(Map<String, Object> form) {
		this.form = form;
	}

	public BaseService getBs() {
		return bs;
	}

	public void setBs(BaseService bs) {
		this.bs = bs;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public static Map<String, String> getSqlMap() {
		return sqlMap;
	}

	public static void setSqlMap(Map<String, String> sqlMap) {
		Super.sqlMap = sqlMap;
	}

	public Map<String, Object> getUser() {
		return user;
	}

	public void setUser(Map<String, Object> user) {
		this.user = user;
	}
	
	public  Object call(String name, List<Mode> list) throws Exception {
		
		Object rs = null;
		ResultSet rss = null;
		List<Map<String, Object>> rslist = new ArrayList<Map<String, Object>>();
		String ootype = "";
		String sql = "{call " + name + " (";
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (i == list.size() - 1) {
					sql += "?";
				} else {
					sql += "?,";
				}
			}
		}
		sql += ")}";
		System.out.println("执行存储过程：" + sql);
		Connection connection1 = null;
		CallableStatement callableStatement1 = null;
		try {
			connection1 = DruidUtil.getConnection();
			if (connection1 == null) {
				call(name, list);
			}
			callableStatement1 = connection1.prepareCall(sql);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Mode mode = list.get(i);
					String otype = mode.getOtype().toUpperCase();
					String type = mode.getType().toUpperCase();
					String value = mode.getValue().toUpperCase();
					if (otype.equals("IN")) {
						switch (type) {
						case "STRING":
							System.out.println(value);
							callableStatement1.setString(i + 1, value);
							break;
						case "INT":
							callableStatement1.setInt(i + 1, Integer.parseInt(value));
							break;
						default:
							break;
						}
					} else {
						switch (type) {
						case "STRING":
							ootype = "STRING";
							callableStatement1.registerOutParameter(i + 1, OracleTypes.VARCHAR);
							break;
						case "INT":
							ootype = "INT";
							callableStatement1.registerOutParameter(i + 1, OracleTypes.NUMBER);
							break;
						case "RS":
							ootype = "RS";
							callableStatement1.registerOutParameter(i + 1, OracleTypes.CURSOR);
							break;
						default:
							break;
						}
					}

				}
			}

			callableStatement1.execute();// 执行
			if (ootype != null && !ootype.equals("")) {
				if (ootype != null && !ootype.equals("") && !connection1.isClosed()) {
					switch (ootype) {
					case "STRING":
						rs = (String) callableStatement1.getObject(list.size());// 此处的2要与存储过程中cursor的问题对应
						break;
					case "INT":
						rs = (Integer) callableStatement1.getObject(list.size());// 此处的2要与存储过程中cursor的问题对应
						break;
					case "RS":
						ootype = "RS";
						rss = (ResultSet) callableStatement1.getObject(list.size());// 此处的2要与存储过程中cursor的问题对应
						ResultSetMetaData md = rss.getMetaData(); // 获得结果集结构信息,元数据
						int columnCount = md.getColumnCount(); // 获得列数
						while (rss.next()) {
							Map<String, Object> rowData = new HashMap<String, Object>();
							for (int i = 1; i <= columnCount; i++) {
								rowData.put(md.getColumnName(i), rss.getObject(i));
							}
							rslist.add(rowData);
						}
						rs = rslist;
						break;
					default:
						break;
					}
				} else {
					call(name, list);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DruidUtil.close(connection1, callableStatement1, rss);;
			/*if (dataSource != null) {
				try {
					dataSource.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (connection1 != null) {
				try {
					connection1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (callableStatement1 != null) {
					try {
						callableStatement1.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}*/

		}
		return rs;
	}

}
