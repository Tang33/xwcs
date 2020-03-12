package fast.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fast.main.util.ExcelRead;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;
import fast.main.util.XLSXCovertCSVReader;
import fast.main.util.ZipUtil;

public class SetDataState extends Super {
	private Map<String, Object> user = null;

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");

			return "sssjgl/SetDataState";
		} catch (Exception e) {
			e.printStackTrace();
			return "sssjgl/SetDataState";
		}
	}

	

	//设置可查不可查
	public String doState(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String rkrq = getValue(this.getForm().get("rkrq")).replaceAll("-", "");;
			String state = getValue(this.getForm().get("State"));
			String updSql="";
			String sql="select 1 from xwcs_cxzt where to_char(rkrq,'yyyymm')='"+rkrq+"' ";
			List<Map<String,Object>> list=this.getBs().query(sql);
			if(list!=null&&list.size()>0){
				updSql="update xwcs_cxzt set state='"+state+"' where to_char(rkrq,'yyyymm')='"+rkrq+"' ";
				this.getBs().update(updSql);
			}else{
				updSql="insert into xwcs_cxzt(rkrq,state) values(to_date('"+rkrq+"','yyyymm'),'"+state+"')";
				this.getBs().insert(updSql);
			}
			
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		} 
	}
	
	//设置可查不可查
		public String add(Map<String, Object> rmap) {
			try {
				initMap(rmap);
				user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
				String rkrq = getValue(this.getForm().get("rkrqadd")).replaceAll("-", "");;
				String sql="insert into xwcs_cxzt(rkrq,state) values(to_date('"+rkrq+"','yyyymm'),'0')";
				this.getBs().insert(sql);
				return this.toJson("000", "查询成功！");
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败！");
			} 
		}
	
	private String formatDouble(double d) {
        NumberFormat nf = NumberFormat.getInstance();
        //设置保留多少位小数
        nf.setMaximumFractionDigits(2);
         // 取消科学计数法
        nf.setGroupingUsed(false);
        //返回结果
        return nf.format(d);
    }
	
	public String query(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String pageNo = getValue(this.getForm().get("pageNo"));
			String pageSize = getValue(this.getForm().get("pageSize"));
			String yf = getValue(this.getForm().get("rkrq"));
			String sql = "select to_Char(rkrq,'yyyyMM') rkrq,state " +
					"from XWCS_CXZT ";
			if (!yf.equals("")) {
				sql +=" where to_Char(rkrq,'yyyyMM')='"+yf+"'";
			}
			sql +=" order by rkrq";
			List<Map<String, Object>> list = this.getBs().query(sql, pageNo, pageSize);
			int count = this.getBs().queryCount(sql);
			return this.toJson("000", "查询成功！", list, count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String GetNowDate() {
		String temp_str = "";
		Date dt = new Date();
		// 最后的aa表示“上午”或“下午” HH表示24小时制 如果换成hh表示12小时制
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
		temp_str = sdf.format(dt);
		return temp_str;
	}



	
}
