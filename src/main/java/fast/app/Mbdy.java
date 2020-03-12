package fast.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;

import fast.main.util.EasyExcelUtil;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Super;

public class Mbdy extends Super{
	
	private static Connection connection = null;

	private Map<String, Object> user = null;
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");

			return "sjfx/Mbdy";
		} catch (Exception e) {
			e.printStackTrace();
			return "sjfx/Mbdy";
		}
	}
	/**
	 * 自定义导入
	 * @param rmap
	 * @return
	 */
	public String impExcel(Map<String, Object> rmap){
		init(rmap);
		String mesg = "";
		String code = "";
		
		String uid = UUID.randomUUID().toString().replace("-", "");
		String jd_dm=getValue(this.getRequest().getSession().getAttribute("dwid"));
		String path = getValue(this.getForm().get("path"));
		String mbmc = getValue(this.getForm().get("mbmc"));
		String mbms = getValue(this.getForm().get("mbms"));
		String wjlj = getValue(this.getForm().get("wjlj"));
		System.out.println("uid:"+uid);
		String head = "";
		try {
			Map<String, Object> rs = this.getBs().queryOne("select count(*) count from FAST_ZDYXX where mbmc = '"+mbmc+"'");
			if(!"0".equals(rs.get("COUNT").toString())){
				return this.toJson("001", "模板名称已存在", "");
			}
			InputStream is = new FileInputStream(new File(path));
			List<List<String>> lists=new ArrayList<List<String>>();
			if(path.endsWith(".xls")){
				//97以前
				lists=EasyExcelUtil.readExcelWithStringList(is, ExcelTypeEnum.XLS);
			}else if(path.endsWith(".xlsx")){
				//07以后
				lists=EasyExcelUtil.readExcelWithStringList(is, ExcelTypeEnum.XLSX);
			}
			String insertSql = "insert into FAST_ZDYDR (";
			List<String> headList =null;
			for(int i = 0;i<lists.size();i++){
				if(i == 0){
					 headList = lists.get(i);
					 System.out.println("11111"+headList);
					if(!"纳税人名称".equals(headList.get(0)) || !"纳税人识别号".equals(headList.get(1)) || headList.size()>40){
						mesg = "文件格式不正确";
						code = "002";
						return this.toJson(code, mesg, uid);
					}
					
					for(int j = 0;j<headList.size();j++){
						head = head + headList.get(j) + ",";
						insertSql = insertSql + "A"+(j+1) + ",";
					}
					head = head.substring(0, head.length()-1);
					insertSql = insertSql + "u_id) values ";
					
					/*String sql = "insert into FAST_ZDYXX (u_id,bt,drsj,mbmc) values ('"+uid+"','"+head+"',sysdate,'"+mbmc+"')";
					this.getBs().insert(sql);*/
					
				
				}else{
					String valueSql = "(";
					List<String> valueList = lists.get(i);
					if (headList.size() == 2 && valueList.size() == 1) {
						valueList.add(1, null);
					}
					for(int j = 0;j<valueList.size();j++){
						String value = valueList.get(j);
						
						valueSql= valueSql + "'" + value + "',";
					}
					if(valueList.size()!=headList.size()){
						for(int j = 0;j<headList.size()-valueList.size();j++){
							valueSql = valueSql + "'',";
						}
						
					}
					valueSql = valueSql + "'"+uid+"')";
					this.getBs().insert(insertSql + valueSql);
					
					connection = getConnection();
					connection.setAutoCommit(false);
					
					
				}
			}
			String sqlsert="insert into FAST_ZDYXX (u_id,bt,drsj,mbmc,czy,mbms,zt,mblx,wjlj,path,jd_dm) values(?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement stmt = connection.prepareStatement(sqlsert);
			
			
			String czy = this.getRequest().getSession().getAttribute("uname").toString();
			
			stmt.setString(1, uid);
			stmt.setString(2, head);
			stmt.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
			stmt.setString(4, mbmc);
			stmt.setString(5, czy);
			StringReader reader = new StringReader(mbms);  
		    stmt.setCharacterStream(6, reader, mbms.length());
		    stmt.setString(7, "1");
		    stmt.setString(8, "定制企业模板");
		    stmt.setString(9, wjlj);
		    stmt.setString(10, path);
		    stmt.setString(11, jd_dm);
			stmt.execute();
			connection.commit();
			mesg = "导入成功";
			code = "000";
		} catch (Exception e) {
			mesg = "导入失败";
			code = "002";
			e.printStackTrace();
		}finally{
			try {
				if(connection!=null)
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return this.toJson(code, mesg, uid);
		
	}
	
	
	public String querymbmx(Map<String, Object> rmap){
		
		init(rmap);
		
		String pageNo=getValue(this.getForm().get("page")).replaceAll("-", "");
		String pageSize=getValue(this.getForm().get("limit")).replaceAll("-", "");
		String jd_dm=getValue(this.getRequest().getSession().getAttribute("dwid"));
		String sql = "select mbmc,drsj,czy,to_char(mbms) as mbms,to_char(bt) as bt,path,wjlj from FAST_ZDYXX where mblx='定制企业模板' and jd_dm="+jd_dm+" order by drsj desc";
		
		String sqlfy="select * from (select row_.*, rownum rowno from (" + sql + ") row_ where rownum <= " + pageSize + "*"
			    + pageNo + ") a where a.rowno >= (" + pageNo + "- 1) * " + pageSize + " + 1";
		
		
		List<Map<String, Object>> mbmx = this.getBs().query(sqlfy);
		List<Map<String, Object>> mbmxct = this.getBs().query(sql);
		
		return this.toJson("000", "查询成功", mbmx, mbmxct.size());
		
		
		
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

	
	 /** 
     * 通过文件路径获取Excel读取 
     * @param path 文件路径，只接受xls或xlsx结尾 
     * @param isHeader 是否表头 
     * @param headerCount 表头行数 
     * @return count 如果文件路径为空，返回0； 
     */  
	public String readRecordsInputPath(Map<String, Object> rmap) { 
		init(rmap);
		String path = getValue(this.getForm().get("path"));
		boolean isHeader = false;
		int headerCount = 0;
        List<String[]> poiList = new ArrayList<String[]>();  
        if(path == null){  
            return null;  
        }else if(!path.endsWith("xls") && !path.endsWith("xlsx")  
              && !path.endsWith("XLS") && !path.endsWith("XLSX")){  
            return null;  
        }  
        File file = new File(path);  
        try {  
            InputStream inputStream = new FileInputStream(file);       
            if(path.endsWith("xls") || path.endsWith("XLS")){  
                poiList = readXLSRecords(inputStream, isHeader, headerCount);   
            }else if(path.endsWith("xlsx") || path.endsWith("XLSX")){  
                poiList = readXLSXRecords(inputStream, isHeader, headerCount);   
            }  
        } catch (Exception e) {  
        	System.err.println(e);
            return null;  
        }  
        return JSON.toJSONString(poiList);  
    }  
	
	/** 
     * 解析EXCEL2003文件流 
     * 如果一行记录的行中或行尾出现空格，POI工具类可能会跳过空格不做处理，所以默认第一行是表头，所有待解析的记录都以表头为准 
     * @param inputStream  输入流 
     * @param isHeader  是否要跳过表头 
     * @param headerCount  表头占用行数 
     * @return 返回一个字符串数组List 
     */  
	public static List<String[]> readXLSRecords(InputStream inputStream, boolean isHeader, int headerCount) {
		List<String[]> poiList = new ArrayList<String[]>();
		try {
			HSSFWorkbook wbs = new HSSFWorkbook(inputStream);
			HSSFSheet childSheet = wbs.getSheetAt(0);
			// 获取表头
			int begin = childSheet.getFirstRowNum();
			HSSFRow firstRow = childSheet.getRow(begin);
			int cellTotal = firstRow.getPhysicalNumberOfCells();
			// 是否跳过表头解析数据
			if (isHeader) {
				begin += headerCount;
			}
			// 逐行获取单元格数据
			for (int i = begin; i <= childSheet.getLastRowNum(); i++) {
				HSSFRow row = childSheet.getRow(i); // 一行的所有单元格格式都是常规的情况下，返回的row为null
				if (null != row) {
					String[] cells = new String[cellTotal];
					for (int k = 0; k < cellTotal; k++) {
						HSSFCell cell = row.getCell(k);
						cells[k] = getStringXLSCellValue(cell);
					}
					poiList.add(cells);
				}
			}
		} catch (Exception e) {
			return null;
		}
		return poiList;
	}
	
	
	
	/**
	 * 解析EXCEL2007文件流 如果一行记录的行中或行尾出现空格，POI工具类可能会跳过空格不做处理，所以默认第一行是表头，所有待解析的记录都以表头为准
	 * 该处理方法中，表头对应都占用一行
	 * @param inputStream 输入流
	 * @param isHeader    是否要跳过表头
	 * @param headerCount 表头占用行数
	 * @return 返回一个字符串数组List
	 */
	public static List<String[]> readXLSXRecords(InputStream inputStream, boolean isHeader, int headerCount) {
		List<String[]> poiList = new ArrayList<String[]>();
		try {
			XSSFWorkbook wbs = new XSSFWorkbook(inputStream);
			XSSFSheet childSheet = wbs.getSheetAt(0);
			// 获取表头
			int begin = childSheet.getFirstRowNum();
			XSSFRow firstRow = childSheet.getRow(begin);
			int cellTotal = firstRow.getPhysicalNumberOfCells();
			// 是否跳过表头解析数据
			if (isHeader) {
				begin += headerCount;
			}
			for (int i = begin; i <= childSheet.getLastRowNum(); i++) {
				XSSFRow row = childSheet.getRow(i); // 一行的所有单元格格式都是常规的情况下，返回的row为null
				if (null != row) {
					String[] cells = new String[cellTotal];
					for (int k = 0; k < cellTotal; k++) {
						XSSFCell cell = row.getCell(k);
						cells[k] = getStringXLSXCellValue(cell);
					}
					poiList.add(cells);
				}
			}
		} catch (Exception e) {
			return null;
		}
		return poiList;
	}
	 
	 /** 
	  * 获取单元格数据内容为字符串类型的数据 
	  *  
	  * @param cell Excel单元格 
	  * @return String 单元格数据内容 
	  */  
	private static String getStringXLSCellValue(HSSFCell cell) {
		String strCell = "";
		if (cell == null) {
			return "";
		}
		// 将数值型参数转成文本格式，该算法不能保证1.00这种类型数值的精确度
		DecimalFormat df = (DecimalFormat) NumberFormat.getPercentInstance();
		StringBuffer sb = new StringBuffer();
		sb.append("0");
		df.applyPattern(sb.toString());
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			double value = cell.getNumericCellValue();
			while (Double.parseDouble(df.format(value)) != value) {
				if ("0".equals(sb.toString())) {
					sb.append(".0");
				} else {
					sb.append("0");
				}
				df.applyPattern(sb.toString());
			}
			strCell = df.format(value);
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell == null || "".equals(strCell)) {
			return "";
		}
		return strCell;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell Excel单元格
	 * @return String 单元格数据内容
	 */
	private static String getStringXLSXCellValue(XSSFCell cell) {
		String strCell = "";
		if (cell == null) {
			return "";
		}
		// 将数值型参数转成文本格式，该算法不能保证1.00这种类型数值的精确度
		DecimalFormat df = (DecimalFormat) NumberFormat.getPercentInstance();
		StringBuffer sb = new StringBuffer();
		sb.append("0");
		df.applyPattern(sb.toString());

		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			double value = cell.getNumericCellValue();
			while (Double.parseDouble(df.format(value)) != value) {
				if ("0".equals(sb.toString())) {
					sb.append(".0");
				} else {
					sb.append("0");
				}
				df.applyPattern(sb.toString());
			}
			strCell = df.format(value);
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case XSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell == null || "".equals(strCell)) {
			return "";
		}
		return strCell;
	}

}
