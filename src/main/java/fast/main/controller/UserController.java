package fast.main.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.Super;


/**
 * 系统管理---->人员管理
 *
 */
@Controller
@RequestMapping("user")
public class UserController extends Super {
	
	@Autowired
	BaseService bs;
	
	
	/**
	 * 根据账号和用户名进行查询
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/query.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String query(HttpServletRequest request , HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		
		try{
			String uNo=getValue(rmap.get("uNo"));
			String uName=getValue(rmap.get("uName"));
			String pageNo=getValue(rmap.get("page"));
			String pageSize=getValue(rmap.get("limit"));
			String sql= "select u.*,d.ssdw_dm,d.ssdw_mc from fast_user u left join xtgl_dw d on d.ssdw_dm=u.dwid where 1=1 and u.uno like '%'||?||'%' and u.uname like '%'||?||'%'";
			sql = this.getSql2(sql, new Object[] {uNo,uName});
			List<Map<String, Object>> list = bs.query(sql,pageNo,pageSize);
			System.out.println(list);
			int count = bs.queryCount(sql);
			return this.toJson("000", "查询成功！", list,count);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
		
	}
	
	/**
	 * 查询街道信息
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/querydw.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String querydw(HttpServletRequest request , HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）

			String sql="select * from xtgl_dw ";
			List<Map<String, Object>> list = bs.query(sql);
			return this.toJson("000", "查询成功！", list);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	/**
	 * 根据人员ID删除人员账号
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/del.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String del(HttpServletRequest request , HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）

			String uuid=getValue(rmap.get("UUID"));
			String sql = "delete from fast_user where uuid=?";
			bs.delete(this.getSql2(sql, new Object[] {uuid}));
			
			return this.toJson("000", "查询成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	
	/**
	 * 修改人员ID查找并修改密码
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/update.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String update(HttpServletRequest request , HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）

			String uuid=getValue(rmap.get("uuid1"));
			String uno=getValue(rmap.get("uno1"));
			String uname=getValue(rmap.get("uname1"));
			String upwd=getValue(rmap.get("upwd1"));
			String yxbz=getValue(rmap.get("yxbz3"));
			String ssdw=getValue(rmap.get("ssdwedit"));
			String sql="select * from fast_user where uuid='"+uuid+"'";
			System.out.println(sql);
			List<Map<String, Object>> list= bs.query(sql);
			if(list!=null&&list.size()>0) {
				String uupwd=(String) list.get(0).get("UPWD");
				if(upwd==null||upwd.trim().equals("")) {
					upwd=uupwd;
				}else {
					if(uupwd!=null&&!uupwd.equals(upwd)) {
						upwd = DigestUtils.md5Hex(upwd); // 密码
						System.out.println("加密后" + upwd);
					}
				}
			}else {
				System.out.println("---");
			}
			
			String sql2 = "update fast_user set uno=?,uname=?,upwd=?,yxbz=?,dwid=? where uuid=?";
			
			bs.update(this.getSql2(sql2, new Object[]{uno,uname,upwd,yxbz,ssdw,uuid}));
			return this.toJson("000", "查询成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	/**
	 * 添加人员账号
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/add.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String add(HttpServletRequest request , HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）

			String uno=getValue(rmap.get("uno"));
			String uname=getValue(rmap.get("uname"));
			String upwd=getValue(rmap.get("upwd"));
			String yxbz=getValue(rmap.get("yxbz"));
			String ssdw=getValue(rmap.get("ssdw"));
			upwd = DigestUtils.md5Hex(upwd); // 密码
			System.out.println("加密后" + upwd);
			if(yxbz.equals("on")) {
				yxbz="Y";
			}else if(yxbz.equals("off")) {
				yxbz="N";
			}
			
			String sql = "insert into fast_user(uuid,uno,uname,upwd,yxbz,dwid) values(seq_fast_user.nextval,?,?,?,?,?)";
			bs.insert(this.getSql2(sql, new Object[]{uno,uname,upwd,yxbz,ssdw}));
			return this.toJson("000", "查询成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
}
