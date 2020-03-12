package fast.main.controller.ImportTaxData;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.Super;

/**
 * 街道报表发布
 *
 */
@Controller
@RequestMapping("JDreport")
public class JdSswcqkLrActionController extends Super{
	@Autowired
	BaseService bs;
	
	@RequestMapping(value="/init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "ImportTaxData/JdSswcqkLrAction";
		} catch (Exception e) {
			e.printStackTrace();
			return "ImportTaxData/JdSswcqkLrAction";
		}
	}
		/**
		 * 添加上传记录
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("JdSswcqkLrAction_addBB.do")
		@ResponseBody
		public String addBB(HttpServletRequest request, HttpServletResponse response) {
			try{
				
				String yf=getValue(request.getParameter("yf"));//+"01";//时间
				String jd_dm=getValue(request.getParameter("jd_dm"));//街道id
				String scbbmc=getValue(request.getParameter("scbbmc"));//文件路径
				String bbmc=getValue(request.getParameter("bbmc"));//文件名字
				String uno=request.getSession().getAttribute("uno").toString();
				if("0".equals(jd_dm)) {
					return this.toJson("001", "插入失败！");
				} else {
					String querySQL = "select * FROM JD_SSWCQK where JD_DM = '"+jd_dm+"' and to_char(SSYF,'yyyymm') = '"+yf+"'";
					Integer count=bs.queryCount(querySQL);
					if(count>0) {
						return this.toJson("002", "该报表已存在！");
					}else {
						String sql = "insert into JD_SSWCQK(JD_DM,SSYF,BBMC,SCBBMC,STATE,LRRY_DM,LR_SJ) values ('"+jd_dm+"',to_date('"+yf+"01','yyyymmdd'),'"+bbmc+"','"+scbbmc+"','1','"+uno+"',sysdate)";
						bs.insert(sql);
						return this.toJson("000", "插入成功！");
					}	
				}	
			}catch(Exception e){
				e.printStackTrace();
				return this.toJson("009", "插入失败！");
			}
		}
		
		/**
		 * 查询
		 */
		@RequestMapping("JdSswcqkLrAction_doQuery.do")
		@ResponseBody
		public String doQuery(HttpServletRequest request, HttpServletResponse response) {
			try {
				// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
				String ssdw_dm=request.getSession().getAttribute("dwid").toString();		//
				String yf = getValue(request.getParameter("yf"));			//月份
				String pageNo=getValue(request.getParameter("pageNo"));			//第几页
				String pageSize=getValue(request.getParameter("pageSize"));			//显示条数
				
				String sql = " select jd.jd_dm as JD_DM,jd.jd_mc as JD_MC,nvl(ss.state,'0') as STATE,"
						+ " to_char(ss.ssyf,'yyyy-mm-dd') as ssyf,   ss.bbmc as BBMC,ss.scbbmc as SCBBMC,"
						+ " to_char(ss.lr_sj,'yyyy-mm-dd') as SCSJ,"
						+ "    nvl((Select czy.czy_mc from xtgl_czy czy where czy.czy_dm=ss.lrry_dm),'') as LRRY_MC,"
						+ " 'newFileList['||jd.jd_dm||']' as FILENAME "
						+ "   from JD_SSWCQK ss,dm_jd jd"
						+ " where jd.jd_dm=ss.jd_dm(+)";
					if(ssdw_dm!=null&&!ssdw_dm.equals("00")){
						sql+=" and jd.jd_dm='"+ssdw_dm+"'";
					}
					sql+="     and jd.XYBZ = '1'  and (-to_char(ss.ssyf,'yyyymm')=?-)";

				sql+= " order by ss.ssyf";
				
				
				sql = getSql2(sql, new Object[] {yf});
				List<Map<String, Object>> list = bs.query(sql,pageNo,pageSize);
				int count = bs.queryCount(sql);
				for (Map<String, Object> map : list) {
					String path = (String) map.get("SCBBMC");
					if(path==null) {
						map.put("SCBBMC", "-1");
						continue;
					}
					File file=new File(path);
					if(!(file.exists() && file.isFile())) {
						map.put("SCBBMC", "-1");
					}
				}
				return this.toJson("000", "查询成功！", list,count);
			} catch (Exception e) {
				e.printStackTrace();
				return this.toJson("009", "查询失败！");
			}
		}
		
		/**
		 * 删除街道报表
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("JdSswcqkLrAction_doDel.do")
		@ResponseBody
		public String doDel(HttpServletRequest request, HttpServletResponse response){
			try{
				//初始化   bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
				String JD_DM=getValue(request.getParameter("JD_DM"));
				String yf=getValue(request.getParameter("SSYF"));
				yf=yf.replace("-", "");
				yf=yf.substring(0, 6);
				String sql11 = getSql2("select SCBBMC from JD_SSWCQK where JD_DM=? and to_char(ssyf,'yyyymm')=?", new Object[] {JD_DM,yf});
				List<Map<String,Object>> list = bs.query(sql11);
				if(list.size() > 0) {
					String path = (String) list.get(0).get("SCBBMC");
					if(path!=null) {
						File file=new File(path);
						if(file.exists() && file.isFile()) {
							boolean yn = file.delete();
							System.out.println("删除文件是否成功:"+yn); 
						}
					}

				}
				String sql = " Delete from JD_SSWCQK where JD_DM='" + JD_DM
						+ "' and to_char(ssyf,'yyyymm')='" + yf + "' ";
				Integer count=bs.update(sql);
				if(count > 0 ) {
					return this.toJson("000", "删除成功！");
				} else {
					return this.toJson("009", "删除失败！");
				}
			}catch(Exception e){
				e.printStackTrace();
				return this.toJson("009", "删除失败！");
			}
		}
		
		//新增
		@RequestMapping("JdSswcqkLrAction_doInput.do")
		@ResponseBody
		public String doInput(HttpServletRequest request, HttpServletResponse response){
			try{
				//初始化   bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
				String JD_DM=getValue(request.getParameter("upload_JD_DM"));
				String FILENAME=getValue(request.getParameter("upload_name"));
				String yf=getValue(request.getParameter("upload_YF"));
				String uno=request.getSession().getAttribute("uno").toString();
				
				String sql = " Insert into JD_SSWCQK(JD_DM,SSYF,BBMC,SCBBMC,STATE,LRRY_DM,LR_SJ)"
						+ " values('"
						+ JD_DM
						+ "',to_date('"
						+ yf
						+ "01','yyyymmdd'),'"
						+ FILENAME
						+ "','"
						+ FILENAME
						+ "','1','" + uno + "',sysdate) ";
				Integer count=bs.insert(sql);
				if(count>0) {
					return this.toJson("000", "新增成功！");
				}
				return this.toJson("009", "新增失败！");
			}catch(Exception e){
				e.printStackTrace();
				return this.toJson("009", "新增失败！");
			}
		}
		
		/**
		 * 查询报表是否已存在
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("JdSswcqkLrAction_doQueryCount.do")
		@ResponseBody
		public String doQueryCount(HttpServletRequest request, HttpServletResponse response){
			try{
				//初始化   bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
				String JD_DM=getValue(request.getParameter("upload_JD_DM"));
				String yf=getValue(request.getParameter("upload_YF"));
				
				String sql = "select *  from JD_SSWCQK ss,dm_jd jd where jd.jd_dm=ss.jd_dm(+)  and jd.jd_dm = '"
							+JD_DM
							+"'  and to_char(ss.ssyf,'yyyymm')='"+
							yf
							+"' ";
				
				Integer count=bs.queryCount(sql);
				if(count>0) {
					return this.toJson("001", "该报表已存在！");
				}else {
					return this.toJson("000", "该报表不存在！");
				}
				
			}catch(Exception e){
				e.printStackTrace();
				return this.toJson("009", "新增失败！");
			}
		}
		
		//覆盖
		@RequestMapping("JdSswcqkLrAction_dodeleteAndInput.do")
		@ResponseBody
		public String dodeleteAndInput(HttpServletRequest request, HttpServletResponse response){
			try{
				//初始化   bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
				String JD_DM=getValue(request.getParameter("upload_JD_DM"));
				String FILENAME=getValue(request.getParameter("upload_name"));
				String yf=getValue(request.getParameter("upload_YF"));
				String uno=request.getSession().getAttribute("uno").toString();
				
				String sql = " Delete from JD_SSWCQK where JD_DM='" + JD_DM
						+ "' and to_char(ssyf,'yyyymm')='" + yf + "' ";
				Integer count=bs.delete(sql);
				
				String sql3 = " Insert into JD_SSWCQK(JD_DM,SSYF,BBMC,SCBBMC,STATE,LRRY_DM,LR_SJ)"
						+ " values('"
						+ JD_DM
						+ "',to_date('"
						+ yf
						+ "01','yyyymmdd'),'"
						+ FILENAME
						+ "','"
						+ FILENAME
						+ "','1','" + uno + "',sysdate) ";
				Integer count3=bs.insert(sql3);
				return this.toJson("000", "覆盖成功！");
				
			}catch(Exception e){
				e.printStackTrace();
				return this.toJson("009", "覆盖失败！");
			}
		}
		
		/**
		 * 街道下拉选项
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("JdSswcqkLrAction_jdInit.do")
		@ResponseBody
		public String jdInit(HttpServletRequest request, HttpServletResponse response){
			try{
				String sql = "select * from dm_jd  where xYbz = '1' ";
				List<Map<String, Object>> count3=bs.query(sql);
				return this.toJson("000", "成功！",count3);
				
			}catch(Exception e){
				e.printStackTrace();
				return this.toJson("009", "失败！");
			}
		}
}
