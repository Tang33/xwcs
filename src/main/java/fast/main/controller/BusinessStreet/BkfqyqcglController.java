package fast.main.controller.BusinessStreet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.ExcelUtil;
import fast.main.util.Super;



@Controller
@RequestMapping("bkfqyqcgl")
public class BkfqyqcglController extends Super {
	
	@Autowired
	BaseService bs;

	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "BusinessStreet/BkfqcAction";
		} catch (Exception e) {
			e.printStackTrace();
			return "BusinessStreet/BkfqcAction";
		}
	}
	
	/**
	 * 按纳税人查找
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/doQuery.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doQuery(HttpServletRequest request , HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String paramNsrmc = getValue(rmap.get("paramNsrmc"));
			String pageNo=getValue(rmap.get("pageNo"));
			String pageSize=getValue(rmap.get("pageSize"));
			String sql = " Select  t.SWGLM as SWGLM, NSRSBH as NSRSBH,NSRMC as NSRMC,JYDZ as SCJYDZ,LY as LY,'' as JD_MC,"
					+ "to_char (nvl(XG_SJ, LR_SJ), 'yyyy-mm-dd hh24:mi' ) as RQ  from DJ_BKFQY t where t.NSRMC like '%"
					+ paramNsrmc + "%' order by t.nsrmc ";
			sql = getSql2(sql);
			List<Map<String, Object>> list = bs.query(sql,pageNo,pageSize);
			int count = bs.queryCount(sql);
			return this.toJson("000", "查询成功！", list,count);
		} catch (Exception e) {
			//e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
		
	/**
	 * 删除信息
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/doDel.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doDel(HttpServletRequest request , HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		try{
			//初始化   bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String key=getValue(rmap.get("NSRMC")).toUpperCase();
			String sql = "Delete from DJ_BKFQY where NSRMC='" + key + "' ";
			Integer count=bs.update(sql);
			return this.toJson("000", "删除成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "删除失败！");
		}
	}
		
	/**
	 * 导出excel
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	
	
	public Object exporti(HttpServletRequest request , HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String paramNsrmc = getValue(rmap.get("paramNsrmc"));
			String sql = " Select  t.SWGLM as SWGLM, NSRSBH as NSRSBH,NSRMC as NSRMC,JYDZ as SCJYDZ,LY as LY,'' as JD_MC,"
					+ "to_char (nvl(XG_SJ, LR_SJ), 'yyyy-mm-dd hh24:mi' ) as RQ  from DJ_BKFQY t where t.NSRMC like '%"
					+ paramNsrmc + "%' order by t.nsrmc ";
			sql = getSql2(sql);
			List<Map<String, Object>> list = bs.query(sql);
			
			Map<String, Object> map=new HashMap<String, Object>();
			String[] cols= {"税务管理码","纳税人识别码","纳税人名称","生产经营地址","最后变更日期"};
			String[] keys= {"SWGLM","NSRSBH","NSRMC","SCJYDZ","RQ"};
			map.put("fileName", "不可分企业清册.xls");
			map.put("cols", cols);
			map.put("keys", keys);
			map.put("list", list);
			
			return map;
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
		
	/**
	 * 修改信息
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/doUpdate.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doUpdate(HttpServletRequest request , HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		try{
			//初始化   bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String NSRMC=getValue(rmap.get("update_NSRMC"));
			String NSRSBH=getValue(rmap.get("update_NSRSBH"));
			String YNSRMC=getValue(rmap.get("update_YNSRMC"));
			String SCJYDZ=getValue(rmap.get("update_SCJYDZ")).toUpperCase();
			String SWGLM=getValue(rmap.get("update_SWGLM")).toUpperCase();
			String id=request.getSession().getAttribute("id").toString();
			
			String sql = " Update DJ_BKFQY  set NSRMC='" + NSRMC + "' ," +
					"  SWGLM='" + SWGLM + "',NSRSBH='"
					+ NSRSBH + "',JYDZ='" + SCJYDZ
					+ "',XGRY_DM='" + id
					+ "',XG_SJ=sysdate  where trim(NSRMC)='" + YNSRMC + "' ";
			
			bs.update(sql);
			return this.toJson("000", "修改成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "修改失败！");
		}
	}
		
	/**
	 * 新增信息
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/doAdd.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doAdd(HttpServletRequest request , HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String NSRMC=getValue(rmap.get("add_NSRMC"));
			String NSRSBH=getValue(rmap.get("add_NSRSBH"));
			String SCJYDZ=getValue(rmap.get("add_SCJYDZ")).toUpperCase();
			String SWGLM=getValue(rmap.get("add_SWGLM")).toUpperCase();
			String id=request.getSession().getAttribute("id").toString();
			
			String sql = " Insert into DJ_BKFQY(SWGLM,NSRSBH,NSRMC,JYDZ,LY,LRRY_DM,LR_SJ) Values('"
					+ SWGLM
					+ "','"
					+ NSRSBH
					+ "','"
					+ NSRMC
					+ "','"
					+ SCJYDZ
					+ "','人工','"
					+ id + "',sysdate)";
				
			Integer count=bs.insert(sql);
			return this.toJson("000", "查询成功！",count);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
		
	/**
	 * 查询nsrsbh或者nsrmc是否已经存在
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/doQueryCount.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doQueryCount(HttpServletRequest request , HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String NSRMC=getValue(rmap.get("add_NSRMC"));
			String NSRSBH=getValue(rmap.get("add_NSRSBH"));
			String SWGLM=getValue(rmap.get("add_SWGLM")).toUpperCase();
			
			String sql1="SELECT * FROM DJ_BKFQY WHERE nsrsbh='"+NSRSBH+"'";
			String sql2="SELECT * FROM DJ_BKFQY WHERE nsrmc='"+NSRMC+"'";
			String sql3="SELECT * FROM DJ_BKFQY WHERE SWGLM='"+SWGLM+"'";
			Integer  count1=bs.queryCount(sql1);
			
			Integer  count2=bs.queryCount(sql2);
			Integer  count3=bs.queryCount(sql3);
			if(count1>0 && count2>0) {
				return this.toJson("001", "纳税人识别号以及纳税人名称已存在！");
			} else if(count1 >0){
				return this.toJson("002", "纳税人识别号已存在！");
			} else if(count2 >0){
				return this.toJson("003", "纳税人名称已存在！");
			}else if(count3 >0){
				return this.toJson("004", "税务管理码已存在！");
			} else {
				return this.toJson("000", "没有重复！");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
		
	/**
	 * 查询nsrmc是否已经存在
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/doQueryCount2.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doQueryCount2(HttpServletRequest request , HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String NSRMC=getValue(rmap.get("update_NSRMC"));
			String YNSRMC=getValue(rmap.get("update_YNSRMC"));
			if(NSRMC.equals(YNSRMC)) {
				return this.toJson("000", "没有修改纳税人名称！");
			}else {
				String sql2="SELECT * FROM DJ_BKFQY WHERE nsrmc='"+NSRMC+"'";
				
				Integer  count2=bs.queryCount(sql2);
				if(count2 >0){
					return this.toJson("003", "纳税人名称已存在！");
				} else {
					return this.toJson("000", "没有重复！");
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
		
	//
	@RequestMapping(value="/doDeleteAndInsert.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doDeleteAndInsert(HttpServletRequest request , HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			String NSRMC=getValue(rmap.get("add_NSRMC"));
			String NSRSBH=getValue(rmap.get("add_NSRSBH"));
			String SCJYDZ=getValue(rmap.get("add_SCJYDZ")).toUpperCase();
			String SWGLM=getValue(rmap.get("add_SWGLM")).toUpperCase();
			String id=this.getRequest().getSession().getAttribute("id").toString();
			
			String delSql1="Delete from DJ_BKFQY WHERE NSRSBH='" + NSRSBH + "' ";
			String delSql2="Delete from DJ_BKFQY WHERE NSRMC='"+NSRMC+"'";
			String delSql3="Delete from DJ_BKFQY WHERE SWGLM='"+SWGLM+"'";
			Integer  count1=bs.delete(delSql1);
			Integer  count2=bs.delete(delSql2);
			Integer  count3=bs.delete(delSql3);
				
				
			String sql = " Insert into DJ_BKFQY(SWGLM,NSRSBH,NSRMC,JYDZ,LY,LRRY_DM,LR_SJ) Values('"
					+ SWGLM
					+ "','"
					+ NSRSBH
					+ "','"
					+ NSRMC
					+ "','"
					+ SCJYDZ
					+ "','人工','"
					+ id + "',sysdate)";
			
			Integer count=bs.insert(sql);
			return this.toJson("000", "覆盖成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "操作异常！");
		}
	}
	

	@RequestMapping("/export.do")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> form) throws IOException {
		Map<String, Object> o = new HashMap<String, Object>();
		try {
		    o= (Map<String, Object>) this.exporti(request,response,form);
			String fileName=(String) o.get("fileName");
			List<Map<String, Object>> list=(List<Map<String, Object>>) o.get("list");
			String cols[] = (String[]) o.get("cols");//列名
			String keys[] = (String[]) o.get("keys");//map中的key
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				ExcelUtil.createWorkBook(list, keys, cols).write(os);
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "iso-8859-1"));
			ServletOutputStream out = response.getOutputStream();
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				bis = new BufferedInputStream(is);
				bos = new BufferedOutputStream(out);
				byte[] buff = new byte[2048];
				int bytesRead;
				// Simple read/write loop.
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}
			} catch (final IOException e) {
				throw e;
			} finally {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		} 
	}
}
