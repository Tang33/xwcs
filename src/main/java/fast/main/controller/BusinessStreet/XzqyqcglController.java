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

/**
 * 企业属地管理
 * 徐庄企业清册管理
 * 本功能用于对系统的“重点税源类别管理”进行操作管理！
 * @author Administrator
 *
 */

@Controller
@RequestMapping("xzqyqcgl")
public class XzqyqcglController extends Super {

	@Autowired
	private BaseService bs;

	@RequestMapping(value="init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "BusinessStreet/XzAction";
		} catch (Exception e) {
			e.printStackTrace();
			return "BusinessStreet/XzAction";
		}
	}
	
	@RequestMapping(value="/doQuery.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	// 查询
	public String doQuery(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			String paramNsrmc = getValue(form.get("paramNsrmc"));
			String pageNo = getValue(form.get("page"));
			String pageSize= getValue(form.get("limit"));
			String sql = " Select  NSRSBH as NSRSBH,NSRMC as NSRMC,JYDZ as SCJYDZ,LY as LY,'徐庄' as JD_MC,"
					+ "to_char (nvl(XG_SJ, LR_SJ), 'yyyy-mm-dd hh24:mi' ) as RQ    from DJ_XZ t where t.NSRMC like '%"
					+ paramNsrmc + "%' and sfzjkc='0' order by t.nsrmc ";
			sql = getSql2(sql);
			List<Map<String, Object>> list = bs.query(sql,pageNo,pageSize);
			int count = bs.queryCount(sql);
			return this.toJson("000", "查询成功！", list,count);
		} catch (Exception e) {
			//e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}


	@RequestMapping(value="/doQueryCount.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	//点确认新增后先执行查询nsrsbh或者nsrmc是否已经存在
	public String doQueryCount(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）

			String NSRMC=getValue(form.get("add_NSRMC"));
			String NSRSBH=getValue(form.get("add_NSRSBH")).toUpperCase();

			String sql1="SELECT * FROM DJ_XZ WHERE nsrsbh='"+NSRSBH+"' and sfzjkc='0'";
			String sql2="SELECT * FROM DJ_XZ WHERE nsrmc='"+NSRMC+"' and sfzjkc='0'";
			Integer  count1=bs.queryCount(sql1);

			Integer  count2=bs.queryCount(sql2);
			if(count1>0 && count2>0) {
				return this.toJson("001", "纳税人识别号以及纳税人名称已存在！");
			} else if(count1 >0){
				return this.toJson("002", "纳税人识别号已存在！");
			} else if(count2 >0){
				return this.toJson("003", "纳税人名称已存在！");
			} else {
				return this.toJson("000", "没有重复！");
			}

		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}


	@RequestMapping(value="/doAdd.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	//新增
	public String doAdd(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form){
		try{

			String NSRMC=getValue(form.get("add_NSRMC"));
			String NSRSBH=getValue(form.get("add_NSRSBH"));
			String SCJYDZ=getValue(form.get("add_SCJYDZ")).toUpperCase();
			String id=request.getSession().getAttribute("id").toString();

			String sql = " Insert into DJ_XZ(NSRSBH,NSRMC,JYDZ,LY,LRRY_DM,LR_SJ,SFZJKC) Values('"
					+ NSRSBH
					+ "','"
					+ NSRMC
					+ "','"
					+ SCJYDZ
					+ "','人工','"
					+ id + "',sysdate,'0')";

			Integer count=bs.insert(sql);
			return this.toJson("000", "查询成功！",count);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	/**
	 * 新增时信息重复  确认覆盖
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/doDeleteAndInsert.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doDeleteAndInsert(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）

			String NSRMC=getValue(form.get("add_NSRMC"));
			String NSRSBH=getValue(form.get("add_NSRSBH"));
			String SCJYDZ=getValue(form.get("add_SCJYDZ")).toUpperCase();
			String id=this.getRequest().getSession().getAttribute("id").toString();

			String delSql1="Delete from DJ_XZ WHERE NSRSBH='" + NSRSBH + "'  and sfzjkc='0'";
			String delSql2="Delete from DJ_XZ WHERE NSRMC='"+NSRMC+"' and sfzjkc='0'";
			Integer  count1=bs.delete(delSql1);
			Integer  count2=bs.delete(delSql2);
			String sql = " Insert into DJ_XZ(NSRSBH,NSRMC,JYDZ,LY,LRRY_DM,LR_SJ,SFZJKC) Values('"
					+ NSRSBH
					+ "','"
					+ NSRMC
					+ "','"
					+ SCJYDZ
					+ "','人工','"
					+ id + "',sysdate,'0')";

			Integer count=bs.insert(sql);
			return this.toJson("000", "覆盖成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "操作异常！");
		}
	}

	/**
	 * 修改时查询纳税人是否存在
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/doQueryCount2.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doQueryCount2(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form){
		try{

			String NSRMC=getValue(form.get("update_NSRMC"));
			String YNSRMC=getValue(form.get("update_YNSRMC"));
			if(NSRMC.equals(YNSRMC)) {
				return this.toJson("000", "没有修改纳税人名称！");
			}else {
				String sql2="SELECT * FROM DJ_XZ WHERE nsrmc='"+NSRMC+"'  and sfzjkc='0'";

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

	/**
	 * 执行修改
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/doUpdate.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doUpdate(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form){
		try{

			String NSRSBH=getValue(form.get("update_NSRSBH"));
			String NSRMC=getValue(form.get("update_NSRMC"));
			String YNSRMC=getValue(form.get("update_YNSRMC"));
			String SCJYDZ=getValue(form.get("update_SCJYDZ")).toUpperCase();
			String id=request.getSession().getAttribute("id").toString();
			String sql = " Update DJ_XZ  set NSRMC='" + NSRMC + "' ,NSRSBH='" + NSRSBH
					+ "',JYDZ='" + SCJYDZ + "',XGRY_DM='" + id
					+ "',XG_SJ=sysdate  where trim(NSRMC)='" + YNSRMC + "' and sfzjkc='0' ";

			Integer count = bs.update(sql);
			return this.toJson("000", "修改成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "修改失败！");
		}
	}
	
	
	/**
	 * 删除
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/doDel.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doDel(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form){
		try{
			
			String key=getValue(form.get("NSRMC"));
			String sql = "Delete from DJ_XZ where trim(NSRMC)='" + key + "' and sfzjkc='0' ";
			bs.update(sql);
			return this.toJson("000", "删除成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "删除失败！");
		}
	}
	
	
	
	
	
	
	public Object export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			
			String paramNsrmc = getValue(form.get("paramNsrmc"));
			String sql = " Select  NSRSBH as NSRSBH,NSRMC as NSRMC,JYDZ as SCJYDZ,LY as LY,'徐庄' as JD_MC,"
					+ "to_char (nvl(XG_SJ, LR_SJ), 'yyyy-mm-dd hh24:mi' ) as RQ    from DJ_XZ t where t.NSRMC like '%"
					+ paramNsrmc + "%' and sfzjkc='0' order by t.nsrmc ";
			sql = getSql2(sql);
			List<Map<String, Object>> list = bs.query(sql);
			
			Map<String, Object> map=new HashMap<String, Object>();
			String[] cols= {"纳税人识别码","纳税人名称","生产经营地址","最后变更日期"};
			String[] keys= {"NSRSBH","NSRMC","SCJYDZ","RQ"};
			map.put("fileName", "徐庄企业清册.xlsx");
			map.put("cols", cols);
			map.put("keys", keys);
			map.put("list", list);
			return map;
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	@RequestMapping("exportExcel.do")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form) throws IOException {
		Map<String, Object> o = new HashMap<String, Object>();
		try {
			
			o = (Map<String, Object>)this.export(request, response, form);
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
			e.getMessage();
		
			
		}
	}

}


