package fast.main.controller.TaxDataAnalysis;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import fast.main.service.BaseService;
import fast.main.util.EasyExcelUtil;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

/**
 * 定制企业模板
 *
 */
@Controller
@RequestMapping("Mbdy")
public class DzqymbController extends Super {

	private Map<String, Object> user = null;
	@Autowired
	BaseService bs;
	
	/**
	 * 进入定制企业模板跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		
		return "TaxDataAnalysis/Dzqymb";
		
	}
	
	/**
	 * 查询模板信息
	 * @return
	 */
	@RequestMapping("querymbmx.do")
	@ResponseBody
	public String querymbmx(HttpServletRequest request, HttpServletResponse response){
		user = (Map<String, Object>)request.getSession().getAttribute("user");
		try{
			
			String pageNo=getValue(request.getParameter("page")).replaceAll("-", "");
			String pageSize=getValue(request.getParameter("limit")).replaceAll("-", "");
			String jd_dm=getValue(request.getSession().getAttribute("dwid"));
			String sql = "select mbmc,drsj,czy,to_char(mbms) as mbms,to_char(bt) as bt,path,wjlj from FAST_ZDYXX where mblx='定制企业模板' and jd_dm="+jd_dm+" order by drsj desc";
			
			List<Map<String, Object>> mbmx = bs.query(sql,pageNo,pageSize);
			List<Map<String, Object>> mbmxct = bs.query(sql);
			
			return this.toJson("000", "查询成功", mbmx, mbmxct.size());
			
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return this.toJson("000", "系统异常!");
		}
	}
		
	/**
	 * 自定义模板上传
	 * @return
	 */
	@RequestMapping("impExcel.do")
	@ResponseBody
	public String impExcel(HttpServletRequest request, HttpServletResponse response){
		user = (Map<String, Object>)request.getSession().getAttribute("user");
		try{
			String mesg = "";
			String code = "";
			
			String uid = UUID.randomUUID().toString().replace("-", "");
			String jd_dm=getValue(request.getSession().getAttribute("dwid"));
			String path = getValue(request.getParameter("path"));			//保存路径
			String mbmc = getValue(request.getParameter("mbmc"));			//模板名称
			String mbms = getValue(request.getParameter("mbms"));			//模板描述
			String wjlj = getValue(request.getParameter("wjlj"));
			String head = "";
		
				Map<String, Object> rs = bs.queryOne("select count(*) count from FAST_ZDYXX where mbmc = '"+mbmc+"'");
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
						bs.insert(sql);*/
						
					
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
						bs.insert(insertSql + valueSql);
						
						
						
						
					}
				}
				//StringReader reader = new StringReader(mbms);  
//				Clob clob=new javax.sql.rowset.serial.SerialClob(mbms.toCharArray());
				String czy = request.getSession().getAttribute("uname").toString();
				String sqlsert="insert into FAST_ZDYXX (u_id,bt,drsj,mbmc,czy,mbms,zt,mblx,wjlj,path,jd_dm) values(?,?,sysdate,?,?,?,?,?,?,?,?)";
				//PreparedStatement stmt = connection.prepareStatement(sqlsert);
				sqlsert = getSql2(sqlsert, new Object[] {uid,head,
						mbmc,czy,mbms,"1",	"定制企业模板",wjlj,path,jd_dm
				});
				bs.query(sqlsert);
				
				mesg = "导入成功";
				code = "000";
			return this.toJson(code, mesg, uid);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
			return this.toJson("000", "系统异常!");
		}
	}
	
}
