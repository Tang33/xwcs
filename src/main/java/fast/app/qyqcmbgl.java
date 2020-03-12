package fast.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fast.main.util.ExcelRead;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

public class qyqcmbgl extends Super {

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String ssdw_dm = this.getRequest().getSession().getAttribute("dwid").toString();
			this.getRequest().setAttribute("dwid", ssdw_dm);
			return "xtgl/qyqcmbgl";
		} catch (Exception e) {
			e.printStackTrace();
			return "xtgl/qyqcmbgl";
		}
	}

	public String queryInit(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().getAttribute("uno");
			// 街道
			String sql = this.getSql("jd_query");
			List<Map<String, Object>> jdlist = this.getBs().query(sql);
			// 行业
			sql = this.getSql("hy_query");
			List<Map<String, Object>> hylist = this.getBs().query(sql);
			// 重点税源企业
			/*
			 * sql=this.getSql("去查重点税源企业"); List<Map<String, Object>>
			 * zdsylist=this.getBs().query(sql);
			 */

			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();

			map.put("hylist", hylist);
			map.put("jdlist", jdlist);
			// map.put("zdsylist",zdsylist);
			return this.toJson("000", "查询成功！", map);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	public String doinput(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String filename = getValue(this.getForm().get("mbbdlj"));
			String mbid = getValue(this.getForm().get("mbid"));
			InputStream is = null;
			try {
				is = new FileInputStream(new File(filename));
				// 判断接收到的文件格式
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put("纳税人名称", -1);
				List<Map<String, String>> list = ExcelRead.pomExcel(filename, is, map);
				for(int i=0; i < list.size(); i++) {
					String sql = "insert into mbdc_nsrxx (mbid,nsrmc,rownumber) values('"
							+ mbid + "','" + getValue(list.get(i).get("纳税人名称")) + "','"+i+"')";
					this.getBs().insert(sql);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败！");
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	public String add(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String mbmc = getValue(this.getForm().get("mbmc"));
			String dch = getValue(this.getForm().get("dch"));
			String dcl = getValue(this.getForm().get("dcl"));
			String mbbdlj = getValue(this.getForm().get("mbbdlj"));
			String mblj = getValue(this.getForm().get("mblj"));
			String sql = "insert into mbdc_qyqcmbgl (uuid,mbmc,sjqsl,sjqsh,wjlj,wjbdlj) values(seq_mbdc_qyqcmbgl.nextval,'"
					+ mbmc + "','" + dcl + "','" + dch + "','" + mblj + "','" + mbbdlj + "')";
			this.getBs().insert(sql);
			sql="select * from mbdc_qyqcmbgl where mbmc='"+mbmc+"' and sjqsl='"+dcl+"' and sjqsh='"+dch+"' and wjlj='"+mblj+"' and wjbdlj='"+mbbdlj+"'";
			Map<String,Object> map=this.getBs().queryOne(sql);
			return this.toJson("000", "查询成功！",map);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	public String update(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String mbmc = getValue(this.getForm().get("mbmcedit"));
			String dch = getValue(this.getForm().get("dchedit"));
			String dcl = getValue(this.getForm().get("dcledit"));
			String xh = getValue(this.getForm().get("xhedit"));
			String sql = "update mbdc_qyqcmbgl set mbmc='"+mbmc+"',sjqsl='"+dcl+"',sjqsh='"+dch+"' where uuid='"+xh+"'";
			this.getBs().update(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	public String delete(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String xh = getValue(this.getForm().get("xh"));
			String sql = "delete from mbdc_qyqcmbgl  where uuid='"+xh+"'";
			this.getBs().delete(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	// 按企业查询数据查询
	public String queryData(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);

			this.getRequest().getSession().getAttribute("uno");

			String pageNo = getValue(this.getForm().get("pageNo"));// 写死 0
			String pageSize = getValue(this.getForm().get("pageSize"));// 写死 80000
			String Name = getValue(this.getForm().get("Name")).toString();// 纳税人名称
			String sql = "select * from MBDC_QYQCMBGL where mbmc like '%" + Name + "%' order by uuid";
			List<Map<String, Object>> list = this.getBs().query(sql, pageNo, pageSize);
			int count = this.getBs().queryCount(sql);
			return this.toJson("000", "查询成功！", list, count);

		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	private String formatDouble(double d) {
		NumberFormat nf = NumberFormat.getInstance();
		// 设置保留多少位小数
		nf.setMaximumFractionDigits(2);
		// 取消科学计数法
		nf.setGroupingUsed(false);
		// 返回结果
		return nf.format(d);
	}
}
